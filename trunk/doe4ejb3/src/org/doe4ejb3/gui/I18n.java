/*
 * I18n.java
 *
 * Created on 16 August 2006, 19:20
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;


public class I18n 
{
    
    /** Creates a new instance of I18n */
    public static String getLiteral(String key) 
    {
        // TODO: get localized message
        return key;
    }
    
    public static String getEntityName(Class entity)
    {
        String name = entity.getName();
        String msg = I18n.getLiteral(name);
        if( (msg == null) || (name.equals(msg)) ) {
            int pos = msg.lastIndexOf(".");
            msg = name.substring(pos+1);
        }
        return msg;
    }
    
    
}
