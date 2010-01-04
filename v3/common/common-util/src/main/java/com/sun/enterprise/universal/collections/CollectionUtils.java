/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.enterprise.universal.collections;

import java.util.*;

/**
 * all-static methods for handling operations with Collections
 * @author bnevins
 */
public class CollectionUtils {
    private CollectionUtils() {
        
    }
    /**
     * Convert a Properties object, which is a Map<Object,Object> into
     * a Map<String,String>
     * @param p The Properties object to convert
     * @return The converted Map
     */
    public static Map<String,String> propertiesToStringMap(Properties p)
    {
        Map<String,String> map = new HashMap<String,String>();
        Set<Object> names = p.keySet();
        
        for(Object name : names) {
            if(name == null) // impossible
                continue;
            
            Object value = p.get(name);
            
            if(value == null)
                map.put(name.toString(), null);
            else
                map.put(name.toString(), value.toString());
        }
        return map;
    }
    
    /**
     * Tired of dumping a String representation of a Map?
     * Then call me!
     * @param map The map to turn into a printable String
     * @return The pretty String 
     */
    public static String toString(Map<String,String> map) {
        String[] arr = toStringArray(map);
        StringBuilder sb = new StringBuilder();
        
        for(String s : arr) {
            sb.append(s);
            sb.append(EOL);
        }
        return sb.toString();
    }
    
    /**
     * Convert a String[] into a space-delimited String
     * @param arr The String array to convert
     * @return The pretty String 
     */
    public static String toString(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for(String s : arr) {
            sb.append(s);
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String[] toStringArray(Map<String,String> map) {
        Set<String> set = map.keySet();
        String[] ss = new String[map.size()];
        int i = 0;
        
        for(String name : set) {
            String value = map.get(name);
            String s = name;
            
            if(value != null) {
                s += "=" + value;
            }
            ss[i++] = s;
        }
        return ss;
    }
    private static final String EOL = System.getProperty("line.separator");
}
