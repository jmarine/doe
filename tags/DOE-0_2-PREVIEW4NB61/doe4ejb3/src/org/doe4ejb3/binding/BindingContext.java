/*
 * BindingContext.java
 * 
 * Created on Jun 27, 2007, 9:13:57 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;

import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.Binding.SyncFailure;

import org.doe4ejb3.gui.WindowManager;
import org.doe4ejb3.util.DOEUtils;


public class BindingContext extends org.jdesktop.beansbinding.BindingGroup
{
    public void commitUncommittedValues() throws java.beans.PropertyVetoException
    {
        // check all new values
        Object window = null;
        PropertyVetoException veto = null;
        for(org.jdesktop.beansbinding.Binding binding : getBindings()) {
            Object entity = binding.getSourceObject();
            Property entityProperty = binding.getSourceProperty();

            boolean validateNewValues = (entityProperty instanceof EntityProperty);  // TODO? create new attribute in EntityDescriptor to disable validation?
            if(validateNewValues) {
                String name = entityProperty.toString();
                Object oldValue = entityProperty.getValue(entity);
                Object fromComponent = binding.getTargetObject();
                Property fromProperty = binding.getTargetProperty();
                Object newValue = fromProperty.getValue(fromComponent);  // it also can throw PropertyVetoExceptions with embedded entities (but don't catch this exception to back propagate dirty flag)
                
                try {
                    // try change
                    entityProperty.setValue(entity, newValue);
                    
                } catch(RuntimeException error) {
                    Throwable cause = error;
                    if( (cause != null) && (cause.getCause() != null) && (cause.getCause() instanceof InvocationTargetException) ) {
                        InvocationTargetException invocationException = (InvocationTargetException)cause.getCause();
                        veto = new PropertyVetoException(invocationException.getTargetException().getMessage(), new PropertyChangeEvent(fromComponent, entityProperty.toString(), oldValue, newValue));
                        veto.initCause(error);
                        throw veto;
                    } 
                    throw error;
                    
                } finally {
                    // restore initial value
                    try { 
                        entityProperty.setValue(entity, oldValue); 
                    } catch(Exception ex) { 
                        // set dirty flag
                        WindowManager windowManager = DOEUtils.getWindowManager();
                        window = windowManager.getWindowFromComponent(fromComponent);
                        if( (window != null) && (window instanceof javax.swing.JComponent) ) {
                            ((javax.swing.JComponent)window).putClientProperty("dirtyEntity", true);
                        }
                    }
                }
            }
        }
        

        // commit changes:
        for(org.jdesktop.beansbinding.Binding binding : getBindings()) {
            SyncFailure failure = binding.save();
            if(failure != null) throw new PropertyResolverException("Binding failure: " + failure.toString(), binding, null, null);
        }

        // clear dirty flag
        if( (window != null) && (window instanceof javax.swing.JComponent) ) {
             ((javax.swing.JComponent)window).putClientProperty("dirtyEntity", null);
        }

        
    }    
    
}
