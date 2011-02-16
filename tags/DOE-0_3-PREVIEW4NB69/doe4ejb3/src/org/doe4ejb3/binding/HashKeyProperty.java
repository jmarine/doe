/**
 * HashKeyProperty.java
 *
 * Created on 26 August 2006, 19:21
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 *
 * @author jordi
 */
public class HashKeyProperty extends DoeProperty
{
    private String key;
    private Class type;
    private Type genericType;
    
    public HashKeyProperty(String key, Class type, Type genericType) throws IllegalArgumentException
    {
        this.key = key;
        this.type = type;
        this.genericType = genericType;
    }

    
    public String getName()
    {
        return key;
    }
    
    
    public Class getWriteType(Object ignoredObject) // throws IllegalAccessException, InvocationTargetException
    {
        Class returnType = type;
        if(java.util.Collection.class.isAssignableFrom(returnType)) {
            System.out.println("HashKeyProperty: property " + getName() + " is a collection, but only 1 item can be selected");
            try {
                Type type = getGenericType(ignoredObject);
                if(type instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType)type;
                    if(paramType != null) returnType = (Class)(paramType.getActualTypeArguments()[0]);
                } 
                
            } catch(Exception ex) {
                System.out.println("HashKeyProperty.getType(): ERROR = " + ex.getMessage());
                ex.printStackTrace();
            }
            
            if( (java.util.Collection.class.isAssignableFrom(returnType)) 
                    || (returnType.isAssignableFrom(Object.class)) )  {
                throw new RuntimeException("Undefined target entity type on generic collection.");
            }
            
        }
        return returnType;
    }

    
    public boolean isCollectionType()
    {
        return (java.util.Collection.class.isAssignableFrom(type));
    }

    
    public Type getGenericType(Object ignoredObject) throws IllegalAccessException, InvocationTargetException
    {
        return genericType;
    }


    public Object getValue(Object obj)
    {
        HashMap map = (HashMap)obj;
        return map.get(key);
    }

    
    public void setValue(Object obj, Object value)
    {
        HashMap map = (HashMap)obj;
        map.put(key, value);
    }
    
    
    public String toString()
    {
        return key;
    }

}
