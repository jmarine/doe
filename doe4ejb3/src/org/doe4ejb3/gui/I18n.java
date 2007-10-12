/*
 * I18n.java
 *
 * Created on 16 August 2006, 19:20
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import org.jdesktop.application.ResourceMap;


public class I18n 
{
    private static ResourceMap doeResourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class);
    

    public static String getLiteral(String key, Object... args)
    {
        String value = doeResourceMap.getString(key, args);
        if(value == null) value = key;
        return value;
    }
    
    public static String getLiteral(Class baseClass, String key, Object... args) 
    {
        ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(baseClass);
        String value = resourceMap.getString(key, args);
        if(value == null) value = key;
        return value;
    }
    
    
    public static String getEntityName(Class entity)
    {
        String name = entity.getName();
        String msg = I18n.getLiteral(entity, name);
        if( (msg == null) || (name.equals(msg)) ) {
            int pos = msg.lastIndexOf(".");
            msg = name.substring(pos+1);
        }
        return msg;
    }
    
    
}
