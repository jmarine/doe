/*
 * Property.java
 *
 * Created on 9 September 2006, 12:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.doe4ejb3.binding;

import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author jordi
 */
public interface StatefulProperty {
    
    Object getSource();
    
    String getName(); 
    
    Class getType();

    Type getGenericType() throws IllegalAccessException, InvocationTargetException;
   
    Object getValue() throws IllegalAccessException, InvocationTargetException;
    
    void setValue(Object value) throws IllegalAccessException, InvocationTargetException;
}
