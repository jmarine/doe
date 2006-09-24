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
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author jordi
 */
public class ObjectProperty implements Property 
{
    private Object obj;
    private String propertyName;
    
    private Field  field;
    private PropertyDescriptor propertyDescriptor;

    private Class  memberClass;
    private Type   memberType;
    
    private Annotation annotations[];
    

    
    public ObjectProperty(Object obj, Field field) throws IllegalArgumentException
    {
        this.obj = obj;
        this.field = field;
        this.propertyName = field.getName();
    }

    public ObjectProperty(Object obj, PropertyDescriptor propertyDescriptor) throws IllegalArgumentException
    {
        this.obj = obj;
        this.propertyName = propertyDescriptor.getName();
        this.propertyDescriptor = propertyDescriptor;
    }

    
    public String getName()
    {
        return propertyName;
    }
    
    public Annotation[] getAnnotations()
    {
        if(annotations == null) {
            if(field != null) {
                annotations = field.getAnnotations();
            } else if(propertyDescriptor != null) {
                Collection<Annotation> all = new ArrayList<Annotation>();
                if(propertyDescriptor.getReadMethod() != null) Collections.addAll(all, propertyDescriptor.getReadMethod().getAnnotations());
                if(propertyDescriptor.getWriteMethod() != null) Collections.addAll(all, propertyDescriptor.getWriteMethod().getAnnotations());
                annotations = new Annotation[all.size()];
                annotations = all.toArray(annotations);
            }
        } 
        return annotations;
    }

    public boolean hasAnnotation(Class annotationClass)
    {
        // TODO: use "annotations" cache
        
        if(field != null) {
                Annotation annotation = field.getAnnotation(annotationClass);
                if(annotation != null) return true;
        } else if(propertyDescriptor != null) {
                if(propertyDescriptor.getReadMethod() != null) {
                    Annotation annotation = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
                    if(annotation != null) return true;
                }
                if(propertyDescriptor.getWriteMethod() != null) {
                    Annotation annotation = propertyDescriptor.getWriteMethod().getAnnotation(annotationClass);
                    if(annotation != null) return true;
                }
        } 
        return false;
    }

    public Annotation getAnnotation(Class annotationClass)
    {
        // TODO: use "annotations" cache
        Annotation annotation = null;
        if(field != null) {
                annotation = field.getAnnotation(annotationClass);
        } else if(propertyDescriptor != null) {
                if(propertyDescriptor.getReadMethod() != null) {
                    annotation = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
                }
                if( (annotation != null) && (propertyDescriptor.getWriteMethod() != null) ) {
                    annotation = propertyDescriptor.getWriteMethod().getAnnotation(annotationClass);
                }
        } 
        return annotation;
    }
    
    
    
    public Class getType() // throws IllegalAccessException, InvocationTargetException
    {
        Class retval = null;
        if(field != null) {
            retval = field.getType();
        } else if(propertyDescriptor != null) {
            retval = propertyDescriptor.getReadMethod().getReturnType();
        } 
        return retval;
    }

    public Type getGenericType() throws IllegalAccessException, InvocationTargetException
    {
        Type retval = null;
        if(field != null) {
            retval = field.getGenericType();
        } else if(propertyDescriptor != null) {
            retval = propertyDescriptor.getReadMethod().getGenericReturnType();
        } 
        return retval;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException
    {
        Object retval = null;
        if(field != null) {
            retval = field.get(obj);
        } else if( (propertyDescriptor != null) && (propertyDescriptor.getReadMethod() != null) ) {
            retval = propertyDescriptor.getReadMethod().invoke(obj);
        } else {
            throw new IllegalAccessException("Object without getter method/field");
        }
        return retval;
    }

    public void setValue(Object value) throws IllegalAccessException, InvocationTargetException
    {
        if(field != null) {
            field.set(obj, value);
        } else if( (propertyDescriptor != null) && (propertyDescriptor.getWriteMethod() != null) ) {
            propertyDescriptor.getWriteMethod().invoke(obj, value);
        } else {
            throw new IllegalAccessException("Object without setter method/field");
        }
    }

    private static String capitalize(String name)
    {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }


    private static String decapitalize(String name)
    {
        if( (name != null) && (name.length() > 0) && (Character.isUpperCase(name.charAt(0))) ) {
            name = "" + Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
        return name;
    }

}
