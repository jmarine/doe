/**
 * EntityProperty.java
 *
 * Created on 26 August 2006, 19:21
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class EntityProperty extends DoeProperty 
{
    private String propertyName;
    
    private Field  field;
    private PropertyDescriptor propertyDescriptor;

    private Class  memberClass;
    
    private Annotation annotations[];
    

    
    public EntityProperty(Class sourceClass, Field field) throws IllegalArgumentException
    {
        this.field = field;
        this.memberClass = field.getType();
        this.propertyName = decapitalize(field.getName());
    }

    public EntityProperty(Class sourceClass, PropertyDescriptor propertyDescriptor) throws IllegalArgumentException
    {
        this.propertyDescriptor = propertyDescriptor;
        this.memberClass = propertyDescriptor.getPropertyType();
        this.propertyName = decapitalize(propertyDescriptor.getName());

        // search protected write method (inherited from other entities)
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if(writeMethod == null) {
            String name = this.propertyDescriptor.getReadMethod().getName();  // exact getter name
            if(name.startsWith("is")) name = name.substring(2);
            else if(name.startsWith("get")) name = name.substring(3);
            else name = null;

            if(name != null) {
                name = "set" + name;  // exact setter name
                for(Class inspectedClass = sourceClass; (writeMethod == null) && (inspectedClass != null); inspectedClass = inspectedClass.getSuperclass()) {
                    try { 
                        writeMethod = inspectedClass.getDeclaredMethod(name, this.propertyDescriptor.getReadMethod().getReturnType());
                        if(writeMethod != null) {
                            propertyDescriptor.setWriteMethod(writeMethod);
                            break;
                        } 
                    } catch(Exception ex) { }
                }
            }
        }

        if(writeMethod == null) { 
            System.out.println("EntityProperty: WARN: propertyDescriptor without write method: " + propertyName);
        }


        // search possible field with the same property name (it may contain additional annotations)
        for(Class inspectedClass = sourceClass; (field == null) && (inspectedClass != null); inspectedClass = inspectedClass.getSuperclass()) {
            try { 
                this.field = inspectedClass.getDeclaredField(propertyName); 
                System.out.println("EntityProperty: INFO: field attribute found for propertyDescriptor  " + propertyName);
            } catch(Exception ex) { }
        }

        if(field == null) {
            System.out.println("EntityProperty: WARN: propertyDescriptor without field attribute: " + propertyName);
        }

    }
    
    
    public String getName()
    {
        return propertyName;
    }
    
    public PropertyDescriptor getPropertyDescriptor()
    {
        return propertyDescriptor;
    }
    
    public Annotation[] getAnnotations()
    {
        if(annotations == null) {
            if(propertyDescriptor != null) {
                Collection<Annotation> all = new ArrayList<Annotation>();
                if(propertyDescriptor.getReadMethod() != null) Collections.addAll(all, propertyDescriptor.getReadMethod().getAnnotations());
                if(propertyDescriptor.getWriteMethod() != null) Collections.addAll(all, propertyDescriptor.getWriteMethod().getAnnotations());
                if(all.size() > 0) {
                    annotations = new Annotation[all.size()];
                    annotations = all.toArray(annotations);
                }
            } 
            
            if( (annotations == null) && (field != null) ) {
                annotations = field.getAnnotations();
            } 
        } 
        return annotations;
    }

    public boolean hasAnnotation(Class annotationClass)
    {
        // TODO: use "annotations" cache
        
        if(propertyDescriptor != null) {
            if(propertyDescriptor.getReadMethod() != null) {
                Annotation annotation = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
                if(annotation != null) return true;
            }
            if(propertyDescriptor.getWriteMethod() != null) {
                Annotation annotation = propertyDescriptor.getWriteMethod().getAnnotation(annotationClass);
                if(annotation != null) return true;
            }
        } 

        if(field != null) {
            Annotation annotation = field.getAnnotation(annotationClass);
            if(annotation != null) return true;
        } 

        return false;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
    {
        // TODO: use "annotations" cache
        T annotation = null;
        if(propertyDescriptor != null) {
            if(propertyDescriptor.getReadMethod() != null) {
                annotation = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
            }
            if( (annotation == null) && (propertyDescriptor.getWriteMethod() != null) ) {
                annotation = propertyDescriptor.getWriteMethod().getAnnotation(annotationClass);
            }
        } 
        
        if( (annotation == null) && (field != null) ) {
            annotation = field.getAnnotation(annotationClass);
        }
        
        return annotation;
    }
    
    
    public Class getWriteType(Object ignoredObject) // throws IllegalAccessException, InvocationTargetException
    {
        Class retval = memberClass;
        /*
        if(propertyDescriptor != null) {
            retval = propertyDescriptor.getReadMethod().getReturnType();
        } 

        if( (retval == null) && (field != null) ) {
            retval = field.getType();
        } 
        */
        
        return retval;
    }
    

    public Type getGenericType(Object ignoredObject) throws IllegalAccessException, InvocationTargetException
    {
        Type retval = null;

        if(propertyDescriptor != null) {
            retval = propertyDescriptor.getReadMethod().getGenericReturnType();
        } 
        
        if( (retval == null) && (field != null) ) {
            retval = field.getGenericType();
        } 
        
        return retval;
    }

    
    public Object getValue(Object obj)
    {
      Object retval = null;
      try {
        if( (propertyDescriptor != null) && (propertyDescriptor.getReadMethod() != null) ) {
            retval = propertyDescriptor.getReadMethod().invoke(obj);
        } else if(field != null) {
            retval = field.get(obj);
        } else {
            throw new IllegalAccessException("Object without getter method/field");
        }
 
      } catch(Exception ex) {
        throw new RuntimeException("EntityProperty.getValue: Error: " + ex.getMessage(), ex);
      }

      return retval;
    }


    public void setValue(Object obj, Object value) 
    {
      try {
        boolean convertToCollection = java.util.Collection.class.isAssignableFrom(memberClass);
        if( (convertToCollection) && (value.getClass().isArray()) ) {
                Class collectionClass = memberClass;

                // ParameterizedType paramType = (ParameterizedType)memberType;
                // memberClass = (Class)(paramType.getActualTypeArguments()[0]);            

                // FIXME: Create generic collection for "collectionClass"?
                java.util.Collection<Object> list = (java.util.Set.class.isAssignableFrom(memberClass))? new java.util.HashSet<Object>() : new java.util.ArrayList<Object>();
                for(int index = 0; index < java.lang.reflect.Array.getLength(value); index++) {
                    list.add(java.lang.reflect.Array.get(value, index));
                }

                // FIXME: Is this cast really needed?
                value = collectionClass.cast(list);
        }
        
        if( (propertyDescriptor != null) && (propertyDescriptor.getWriteMethod() != null) ) {
            System.out.println("> Setting " + propertyName + " (type: " + propertyDescriptor.getPropertyType().getName() + ") with value = " + value);
            if(value != null) System.out.println("- Value type: " + value.getClass().getName());
            propertyDescriptor.getWriteMethod().setAccessible(true);
            propertyDescriptor.getWriteMethod().invoke(obj, value);
        } else if(field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        } else {
            throw new IllegalAccessException("Object without setter method/field");
        }
      } catch(Exception ex) {
        throw new RuntimeException("EntityProperty.setValue: Error: " + ex.getMessage(), ex);
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
    
    
    public int hashCode()
    {
        return propertyName.hashCode();
    }
    
    
    public boolean equals(Object obj)
    {
        if( (obj != null) && (obj instanceof DoeProperty) ) {
            DoeProperty p2 = (DoeProperty)obj;
            return propertyName.equals(p2.getName());
        }
        return false;
    }
    
}
