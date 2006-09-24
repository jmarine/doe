/**
 * ObjectProperty.java
 *
 * Created on 26 August 2006, 19:21
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Member;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author jordi
 */
public class HashKeyProperty implements Property 
{
    private HashMap hashMap;
    private String key;
    private Class type;
    
    public HashKeyProperty(HashMap hashMap, String key, Class type) throws IllegalArgumentException
    {
        this.hashMap = hashMap;
        this.key = key;
        this.type = type;
    }

    
    public String getName()
    {
        return key;
    }
    
    
    public Class getType() // throws IllegalAccessException, InvocationTargetException
    {
        return type;
    }

    public Type getGenericType() throws IllegalAccessException, InvocationTargetException
    {
        // TODO
        return null;
    }


    public Object getValue() throws IllegalAccessException, InvocationTargetException
    {
        return hashMap.get(key);
    }

    public void setValue(Object value) throws IllegalAccessException, InvocationTargetException
    {
        hashMap.put(key, value);
    }

}
