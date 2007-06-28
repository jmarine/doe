/*
 * PropertyResolverException.java
 * 
 * Created on Jun 27, 2007, 9:20:23 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;


public class PropertyResolverException extends java.lang.RuntimeException
{
    public PropertyResolverException(String msg, Object source, String path)
    {
        this(msg, source, path, null);
    }
    
    public PropertyResolverException(String msg, Object source, String path, java.lang.Exception ex)
    {
        super(msg, ex);
    }

}