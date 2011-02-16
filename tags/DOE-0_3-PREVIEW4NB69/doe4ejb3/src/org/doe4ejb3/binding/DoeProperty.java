/*
 * Property.java
 *
 * Created on 9 September 2006, 12:54
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import javax.swing.event.EventListenerList;

import org.jdesktop.beansbinding.PropertyHelper;
import org.jdesktop.beansbinding.PropertyStateListener;


public abstract class DoeProperty extends PropertyHelper     
{
    
    public abstract String getName(); 

    public abstract Type   getGenericType(Object obj) throws IllegalAccessException, InvocationTargetException;
   

    
    public boolean isReadable(Object ignoredObject)
    {
        return true;
    }

    public boolean isWriteable(Object ignoredObject)
    {
        return true;
    }
    
    public String toString()
    {
        return getName();
    }
     
}
