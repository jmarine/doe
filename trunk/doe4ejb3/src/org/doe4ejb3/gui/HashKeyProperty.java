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
    private Type genericType;
    
    public HashKeyProperty(HashMap hashMap, String key, Class type, Type genericType) throws IllegalArgumentException
    {
        this.hashMap = hashMap;
        this.key = key;
        this.type = type;
        this.genericType = genericType;
    }

   
    public String getName()
    {
        return key;
    }
    
    
    public Class getType() // throws IllegalAccessException, InvocationTargetException
    {
        Class returnType = type;
        if(java.util.Collection.class.isAssignableFrom(returnType)) {
            System.out.println("HashKeyProperty: property " + getName() + " is a collection, but only 1 item can be selected");
            try {
                ParameterizedType paramType = (ParameterizedType)getGenericType();
                if(paramType != null) returnType = (Class)(paramType.getActualTypeArguments()[0]);
            } catch(Exception ex) {
                System.out.println("HashKeyProperty.getType(): ERROR = " + ex.getMessage());
                System.err.println("HashKeyProperty.getType(): ERROR = " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return returnType;
    }

    public Type getGenericType() throws IllegalAccessException, InvocationTargetException
    {
        return genericType;
    }


    public Object getValue() throws IllegalAccessException, InvocationTargetException
    {
        return hashMap.get(key);
    }

    public void setValue(Object value) throws IllegalAccessException, InvocationTargetException
    {
        hashMap.put(key, value);
    }
    
    public String toString()
    {
        return key;
    }

}
