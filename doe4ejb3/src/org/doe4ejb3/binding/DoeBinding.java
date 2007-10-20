/*
 * DoeBinding.java
 *
 * Created on 24 July 2006, 20:01
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.beans.PropertyEditor;
import java.beans.PropertyVetoException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.beansbinding.Binding;

import org.doe4ejb3.gui.*;
import org.doe4ejb3.util.ReflectionUtils;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyHelper;


public class DoeBinding extends Binding
{
    
    public static Binding createSaveBinding(Object entity, DoeProperty entityProperty,Object componentUI, Method componentValueGetter, PropertyEditor componentValueConverter)
    {
        EditorReaderProperty componentProperty = new EditorReaderProperty(entityProperty.getWriteType(entity), componentValueGetter, componentValueConverter);
        return new DoeBinding(entity, entityProperty, componentUI, componentProperty, entityProperty.getName());
    }
    
    private DoeBinding(Object source, Property sourceProperty, Object target, Property targetProperty, String name) 
    {
        super(source, sourceProperty, target, targetProperty, name );
    }
    

    protected void bindImpl() {
        // DOE already binds entity property value with their Editor.
    }

    protected void unbindImpl() {
        // DOE already unbinds entity property value from their Editor.
    }
    
    
}    


class EditorReaderProperty extends PropertyHelper
{
        private Method  componentValueGetter;
        private PropertyEditor componentValueConverter;
        private Class entityPropertyClass;

        
        public EditorReaderProperty(Class entityPropertyClass, Method componentValueGetter, PropertyEditor componentValueConverter)
        {
            this.componentValueGetter = componentValueGetter;
            this.componentValueConverter = componentValueConverter;
            this.entityPropertyClass = entityPropertyClass;
        }

        public Class getWriteType(Object source) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Object getValue(Object source) {
            try {
                // TODO? Migrate to a jsr295 converter?
                Object value = ReflectionUtils.getMemberValue(source, componentValueGetter); 

                boolean convertToCollection = java.util.Collection.class.isAssignableFrom(entityPropertyClass);
                if( (convertToCollection) && (value.getClass().isArray()) ) {  // Code has already been copied to EntityProperty.setValue method
                    Class collectionClass = entityPropertyClass;

                    // ParameterizedType paramType = (ParameterizedType)memberType;
                    // entityPropertyClass = (Class)(paramType.getActualTypeArguments()[0]);            

                    // FIXME: Create generic collection for "collectionClass"?
                    java.util.Collection<Object> list = (java.util.Set.class.isAssignableFrom(entityPropertyClass))? new HashSet<Object>() : new java.util.ArrayList<Object>();
                    for(int index = 0; index < Array.getLength(value); index++) {
                        list.add(Array.get(value, index));
                    }

                    // FIXME: Is this cast really needed?
                    value = collectionClass.cast(list);
                } 
                else
                if(value instanceof ListItem) {
                    ListItem listItem = (ListItem)value;
                    value = listItem.getValue();
                }
                else 
                if(componentValueConverter != null) {
                    System.out.println("- Converting value with editor " + componentValueConverter);
                    componentValueConverter.setAsText(value.toString());  // string representation
                    value = componentValueConverter.getValue();           // convert to editor's real type
                }
                
                return value;

            } catch(Exception ex) {
                throw new PropertyResolverException("Error: " + ex.getMessage(), source, componentValueGetter.getName(), ex);
            }
        }

        public void setValue(Object arg0, Object arg1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isReadable(Object arg0) {
            return true;
        }

        public boolean isWriteable(Object arg0) {
            return false;
        }
}
