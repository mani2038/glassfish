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
import java.util.jar.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

/**
 * all-static methods for handling operations with Manifests
 * It automatically replace all occurences of EOL_TOKEN with linefeeds
 * @author bnevins
 */
public class ManifestUtils {
    /**
     * Embed this token to encode linefeeds in Strings that are placed 
     * in Manifest objects
     */
    public static final String EOL_TOKEN = "%%%EOL%%%";
    /**
     * The name of the Manifest's main attributes.
     */
    public static final String MAIN_ATTS = "main";

    /**
     * The line separator character on this OS
     */
    public static final String EOL = System.getProperty("line.separator");

    /**
     * Convert a Manifest into an easier data structure.  It returns a Map of Maps.
     * The main attributes become the map where the key is MAIN_ATTS.
     * Entries become named maps as in the Manifest
     * @param m
     * @return
     */
    public final static Map<String, Map<String,String>> normalize(Manifest m)
    {
        // first add the "main attributes
        Map<String, Map<String,String>> all = new HashMap<String, Map<String,String>>();
        Attributes mainAtt = m.getMainAttributes();
        all.put(MAIN_ATTS, normalize(mainAtt));

        // now add all the "sub-attributes"
        Map<String,Attributes> unwashed = m.getEntries();
        Set<String> entryNames = unwashed.keySet();
        
        for(String entryName : entryNames) {
            all.put(entryName, normalize(unwashed.get(entryName)));
        }
        
        return all;
    }

    /**
     * Convert an Aattributes object into a Map
     * @param att
     * @return
     */
    public final static Map<String,String> normalize(Attributes att)
    {
        Set<Map.Entry<Object,Object>> entries = att.entrySet();
        Map<String,String> pristine = new HashMap<String,String>(entries.size());
        
        for(Map.Entry<Object,Object> entry : entries) {
            String key = entry.getKey().toString();
            String value = decode(entry.getValue().toString());
            if(key == null) // impossible!
                continue;
            pristine.put(key, value);
        }
        
        return pristine;
    }
    
    public final static String encode(String s) {
        // do DOS linefeed first!
        s = s.replaceAll("\r\n", EOL_TOKEN);
        
        return s.replaceAll("\n", EOL_TOKEN);
    }

    public static Map<String,String> getMain(Map<String, Map<String,String>> exManifest) {
        return exManifest.get(MAIN_ATTS);
    }
    
    private final static String decode(String s) {
        // replace "null" with null
        if(s == null || s.equals("null"))
            return null;
        
        // replace special tokens with eol
        return s.replaceAll(EOL_TOKEN, EOL);
    }
    private ManifestUtils() {
    }
    
}
