/*
 * ReflectionUtils.java
 *
 * Created on 24 July 2006, 20:31
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.util;

import java.beans.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;



public class ReflectionUtils 
{
    public static Object getMemberValue(Object obj, Member getter) throws IllegalAccessException, InvocationTargetException
    {
        Object retval = null;
        if(getter instanceof Field) {
            Field field = (Field)getter;
            retval = field.get(obj);
        } else if(getter instanceof Method) {
            Method method = (Method)getter;
            retval = method.invoke(obj);
        }
        return retval;
    }


    public static void setMemberValue(Object obj, Member setter, Object value) throws IllegalAccessException, InvocationTargetException
    {
        if(setter instanceof Field) {
            Field field = (Field)setter;
            field.setAccessible(true);
            field.set(obj, value);
        } else if(setter instanceof Method) {
            Method method = (Method)setter;
            method.setAccessible(true);
            method.invoke(obj, value);
        }
    }

   
    public static Class getMemberType(Object obj, Member getter) throws IllegalAccessException, InvocationTargetException
    {
        Class retval = null;
        if(getter instanceof Field) {
            Field field = (Field)getter;
            retval = field.getType();
        } else if(getter instanceof Method) {
            Method method = (Method)getter;
            retval = method.getReturnType();
        } 
        return retval;
    }
    
    public static Type getMemberGenericType(Object obj, Member getter) throws IllegalAccessException, InvocationTargetException
    {
        Type retval = null;
        if(getter instanceof Field) {
            Field field = (Field)getter;
            retval = field.getGenericType();
        } else if(getter instanceof Method) {
            Method method = (Method)getter;
            retval = method.getGenericReturnType();
        } 
        return retval;
    }

    
    public static boolean hasAnnotation(Member member, Class T)
    {
        Annotation a = getAnnotation(member, T);
        return (a != null);
    }

    public static Annotation getAnnotation(Member member, Class T)
    {
        Annotation retval = null;
        if(member != null) {
            if(member instanceof Field) {
               retval = ((Field)member).getAnnotation(T);
            } else if(member instanceof Method) {
               retval = ((Method)member).getAnnotation(T);
            } else if(member instanceof Constructor) {
               retval = ((Constructor)member).getAnnotation(T);
            }
        }
        return retval;
    }
    
}
