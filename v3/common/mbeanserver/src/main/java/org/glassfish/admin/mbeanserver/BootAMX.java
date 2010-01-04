/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2006-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.admin.mbeanserver;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.jvnet.hk2.component.Habitat;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.StandardMBean;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;

import org.glassfish.external.amx.BootAMXMBean;
import org.glassfish.external.amx.AMXGlassfish;

/**
The MBean implementation for BootAMXMBean.

Public API is the name of the booter MBean eg {@link BootAMXMBean.OBJECT_NAME}
 */
final class BootAMX implements BootAMXMBean
{
    private final MBeanServer mMBeanServer;
    private final ObjectName mObjectName;
    private final Habitat mHabitat;
    private ObjectName mDomainRootObjectName;


    private static void debug(final String s)
    {
        System.out.println(s);
    }


    private BootAMX(
        final Habitat habitat,
        final MBeanServer mbeanServer)
    {
        mHabitat = habitat;
        mMBeanServer = mbeanServer;
        mObjectName = getBootAMXMBeanObjectName();
        mDomainRootObjectName = null;

        if (mMBeanServer.isRegistered(mObjectName))
        {
            throw new IllegalStateException("AMX Booter MBean is already registered: " + mObjectName);
        }
    }

    public static ObjectName getBootAMXMBeanObjectName()
    {
        return AMXGlassfish.DEFAULT.getBootAMXMBeanObjectName();
    }
    

    /**
    Create an instance of the booter.
     */
    public static synchronized BootAMX create(final Habitat habitat, final MBeanServer server)
    {
        final BootAMX booter = new BootAMX(habitat, server);
        final ObjectName objectName = getBootAMXMBeanObjectName();

        try
        {
            final StandardMBean mbean = new StandardMBean(booter, BootAMXMBean.class);

            if (!server.registerMBean(mbean, objectName).getObjectName().equals(objectName))
            {
                throw new IllegalStateException();
            }
        }
        catch (JMException e)
        {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        return booter;
    }


    AMXStartupServiceMBean getLoader()
    {
        try
        {
            return mHabitat.getByContract(AMXStartupServiceMBean.class);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }


    /**
    We need to dynamically load the AMX module.  HOW?  we can't depend on the amx-impl module.

    For now though, assume that a well-known MBean is available through other means via
    the amx-impl module.
     */
    public synchronized ObjectName bootAMX()
    {
        if (mDomainRootObjectName == null)
        {
            //debug( "Booter.bootAMX: getting AMXStartupServiceMBean via contract" );
            final AMXStartupServiceMBean loader = getLoader();

            //debug( "Got loader for AMXStartupServiceMBean: " + loader );

            final ObjectName startupON = AMXStartupServiceMBean.OBJECT_NAME;
            if (!mMBeanServer.isRegistered(startupON))
            {
                //debug("Booter.bootAMX(): AMX MBean not yet available: " + startupON);
                throw new IllegalStateException("AMX MBean not yet available: " + startupON);
            }

            try
            {
                //debug( "Booter.bootAMX: invoking loadAMXMBeans() on " + startupON);
                mDomainRootObjectName = (ObjectName) mMBeanServer.invoke(startupON, "loadAMXMBeans", null, null);
                //debug( "Booter.bootAMX: domainRoot = " + mDomainRootObjectName);
            }
            catch (final JMException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return mDomainRootObjectName;
    }


    /**
    Return the JMXServiceURLs for all connectors we've loaded.
     */
    public JMXServiceURL[] getJMXServiceURLs()
    {
        return JMXStartupService.getJMXServiceURLs(mMBeanServer);
    }
    
    public void shutdown() 
    {
        try
        {
            mMBeanServer.unregisterMBean(getBootAMXMBeanObjectName());
        }
        catch( final Exception e )
        {
            Util.getLogger().log( java.util.logging.Level.WARNING, "BootAMX.shutdown", e);
        }
   }
}


















