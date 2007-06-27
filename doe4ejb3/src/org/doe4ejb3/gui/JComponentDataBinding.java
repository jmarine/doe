/*
 * JComponentDataBinding.java
 *
 * Created on 24 July 2006, 20:01
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.beans.PropertyEditor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.HashSet;

import org.doe4ejb3.util.ReflectionUtils;


public class JComponentDataBinding 
{
    private Object comp;
    private Method compGetter;
    private PropertyEditor editor;
    private Property entityProperty;
    
    
    public JComponentDataBinding(Object comp, Method compGetter, PropertyEditor editor, Property entityProperty) 
    {
        this.comp = comp;
        this.compGetter = compGetter;
        this.editor = editor;
        this.entityProperty = entityProperty;

    }
    
    public void commitUncommittedValues() throws IllegalAccessException, InvocationTargetException
    {
        try {
        
            Object value = ReflectionUtils.getMemberValue(comp, compGetter); 
            
            Class memberClass = entityProperty.getType();
            boolean convertToCollection = java.util.Collection.class.isAssignableFrom(memberClass);
            if( (convertToCollection) && (value.getClass().isArray()) ) {
                Class collectionClass = memberClass;

                // ParameterizedType paramType = (ParameterizedType)memberType;
                // memberClass = (Class)(paramType.getActualTypeArguments()[0]);            

                // FIXME: Create generic collection for "collectionClass"?
                java.util.Collection list = (java.util.Set.class.isAssignableFrom(memberClass))? new HashSet() : new java.util.ArrayList();
                for(int index = 0; index < Array.getLength(value); index++) {
                    list.add(Array.get(value, index));
                }

                // FIXME: Is this cast really needed?
                value = collectionClass.cast(list);

            } else if(editor != null) {
                System.out.println("- Converting value with editor " + editor);
                editor.setAsText(value.toString());  // string representation
                value = editor.getValue();           // convert to editor's real type
            }

            entityProperty.setValue(value);

        } catch(Exception ex) {
            System.out.println("JComponentDataBinding.commitUncommittedValues: ERROR: " + ex.getMessage());
            ex.printStackTrace();
            // throw ex;  // FIXME: SHOULD REALLY FAIL
        }
    }
    
}
