/*
 * JComponentDataBinding.java
 *
 * Created on 24 July 2006, 20:01
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import org.doe4ejb3.gui.*;
import java.beans.PropertyEditor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.HashSet;

import org.doe4ejb3.util.ReflectionUtils;


public class JComponentDataBinding
{
    private boolean active;
     private Object  componentUI;
    private Method  componentDataGetter;
    private PropertyEditor editor;
    private StatefulProperty entityProperty;
    
    
    public JComponentDataBinding(Object componentUI, Method componentDataGetter, PropertyEditor editor, StatefulProperty entityProperty) 
    {
        this.active = false; 
        this.componentUI = componentUI;
        this.componentDataGetter = componentDataGetter;
        this.editor = editor;
        this.entityProperty = entityProperty;
     }
    

    public void bind()
    {
        if(active) throw new IllegalStateException("Binding is already active");
        active = true;
    }
        
    
    void commitChanges() throws IllegalStateException, IllegalAccessException, InvocationTargetException
    {
        if(!active) throw new IllegalStateException("Binding is not active");
                
        try {
        
            Object value = ReflectionUtils.getMemberValue(componentUI, componentDataGetter); 
            
            Class memberClass = entityProperty.getType();
            boolean convertToCollection = java.util.Collection.class.isAssignableFrom(memberClass);
            if( (convertToCollection) && (value.getClass().isArray()) ) {  // Code has already been copied to EntityProperty.setValue method
                Class collectionClass = memberClass;

                // ParameterizedType paramType = (ParameterizedType)memberType;
                // memberClass = (Class)(paramType.getActualTypeArguments()[0]);            

                // FIXME: Create generic collection for "collectionClass"?
                java.util.Collection<Object> list = (java.util.Set.class.isAssignableFrom(memberClass))? new HashSet<Object>() : new java.util.ArrayList<Object>();
                for(int index = 0; index < Array.getLength(value); index++) {
                    list.add(Array.get(value, index));
                }

                // FIXME: Is this cast really needed?
                value = collectionClass.cast(list);
            } 
            else 
            if(editor != null) {
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

    public void unbind()
    {
        if(!active) throw new IllegalStateException("Binding was not active");
        active = false;
    }
    
}
