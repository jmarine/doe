/*
 * EntityEditorImpl.java
 *
 * Created on June 6, 2006, 5:45 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.beans.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

import java.awt.*;
import java.awt.event.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.*;
import org.doe4ejb3.util.JPAUtils;
import org.doe4ejb3.util.ReflectionUtils;

/**
 *
 * @author Jordi Marine Fort
 */
public class EntityEditorImpl extends JPanel implements EntityEditorInterface 
{

    private final static Insets borderInsets = new Insets(20,10,20,10);

    private final static GridBagConstraints gbcTitle = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5,5,10,5), 0,0);
    private final static GridBagConstraints gbcLabel = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcEmbeddedComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,-3, 5,-3), 0,0);
    private final static GridBagConstraints gbcGlue = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
    
    
    private Object entity = null;
    private boolean objIsNew = false;
    private boolean embedded = false;
    private ArrayList<JComponentDataBinder> binders = new ArrayList();
    
    
    /**
     * Creates a new instance of EntityEditorImpl
     */
    public EntityEditorImpl() {
        this(false);
    }    
    
    public EntityEditorImpl(boolean embedded) 
    {
        this.embedded = embedded;
    }

    public void clearBinders()
    {
        binders.clear();
    }
    
    public boolean isNew()
    {
        return this.objIsNew;
    }

    public Object getEntity() throws IllegalAccessException, InvocationTargetException
    {
        for(JComponentDataBinder binder : binders) {
            binder.executeObjSetterWithValueFromCompGetter();
        }
        return this.entity;
    }

    public void newEntity(Class entityClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        Constructor init = entityClass.getConstructor();
        this.entity = init.newInstance();
        System.out.println("newInstance: entity = " + entity);
        setEntity(entity, true);
    }
    
    public void setEntity(Object entity)
    {
        setEntity(entity, false);
    }
    

    private void setEntity(Object entity, boolean objIsNew)
    {
        System.out.println("Entity: " + entity + ", isNew=" + objIsNew);
        
        this.entity = entity;
        this.objIsNew = objIsNew;
        
        clearBinders();
        removeAll();
        setLayout(new GridBagLayout());

        Class entityClass = entity.getClass();

        
        if(embedded) {
            setBorder(javax.swing.BorderFactory.createTitledBorder(I18n.getEntityName(entityClass)));
        } 
        /*
        else {
            JLabel title = new JLabel(I18n.getEntityName(entityClass));
            title.setFont(title.getFont().deriveFont(16.0f));
            add(title, gbcTitle);
        }
        */


        ArrayList<ObjectProperty> properties = new ArrayList<ObjectProperty>();
        try {
            BeanInfo bi = Introspector.getBeanInfo(entityClass);
            for(java.beans.PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                // TODO: inherited properties?
                ObjectProperty entityProperty = new ObjectProperty(entity, pd);
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
            for(Field field : entityClass.getFields()) {
                // TODO: inherited fields
                ObjectProperty entityProperty = new ObjectProperty(entity, field);
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
        } catch(Exception ex) {
            System.out.println("EntityEditorImpl: ERROR: " + ex.getMessage());
        }
        


        PropertyOrderComparator orderComparator = new PropertyOrderComparator();
        Collections.sort(properties, orderComparator);
        for(ObjectProperty property : properties) 
        {
            handlePersistenceAnnotations(property);
        }
        

        add(Box.createVerticalGlue(), gbcGlue);
    }
    
    public void handlePersistenceAnnotations(ObjectProperty entityProperty)
    {
        // TODO: @Embedable --> new EntityEditorImpl(field.getType() || method.getReturnType())
        // (with "getEntity" method as "getter")
        String name = entityProperty.getName();
        Annotation annotations[] = entityProperty.getAnnotations();
        System.out.println("handlePersistenceAnnotations: Processing property: " + entityProperty.getName());
        
        Class memberClass = null;
        try {
            memberClass = entityProperty.getType();
            if(java.util.Collection.class.isAssignableFrom(memberClass)) {
                ParameterizedType paramType = (ParameterizedType)entityProperty.getGenericType();
                memberClass = (Class)(paramType.getActualTypeArguments()[0]);
            }
        } catch(Exception ex) {
            return;
        }

        
        // Only assume setter/getters to be persistence members, except those annotated with javax.persistence.Transient
        // (public fields must be annotated with persistence annotations, to be considered also as persistence members)
        boolean persistent = (entityProperty.getPropertyDescriptor() != null) 
                                && (entityProperty.getPropertyDescriptor().getReadMethod() != null) && (!entityProperty.getPropertyDescriptor().getReadMethod().isAnnotationPresent(javax.persistence.Transient.class))
                                && (entityProperty.getPropertyDescriptor().getWriteMethod() != null) && (!entityProperty.getPropertyDescriptor().getWriteMethod().isAnnotationPresent(javax.persistence.Transient.class));
        boolean generatedValue = false;
        boolean embedded = false;
        javax.persistence.Temporal temporal = null;
        Object relationType = null;
        Component comp = null;
        JComponentDataBinder binder = null;
        java.beans.PropertyEditor editor = null;
        org.doe4ejb3.annotation.PropertyDescriptor propertyDescriptor = null;
        
        Method compGetter = null;
        int defaultLength = 40;

        System.out.println("Begin: process property annotation");
        for(int i = 0; (annotations != null) && (i < annotations.length); i++) 
        {
            Annotation a = annotations[i];
            System.out.println("> process property annotation: " + a.toString());

            if(a instanceof org.doe4ejb3.annotation.PropertyDescriptor)
            {
                propertyDescriptor = (org.doe4ejb3.annotation.PropertyDescriptor)a;
            }
            
            if(a instanceof javax.persistence.GeneratedValue) 
            {
                persistent = true;
                generatedValue = true;
                defaultLength = 0;
                System.out.println("Has GeneratedValue annotation!");
            }
            else if(a instanceof javax.persistence.Basic) 
            {
                persistent = true;
            }
            else if(a instanceof javax.persistence.Column) 
            {
                javax.persistence.Column column = (javax.persistence.Column)a;
                if( (column.insertable()) || (column.updatable()) ) {
                    if(!generatedValue) defaultLength = column.length();
                    String columnName = column.name();
                    if(columnName != null) name = columnName;
                    persistent = true;
                }
            }
            else if(a instanceof javax.persistence.JoinColumn) 
            {
                // FIXME: Relation annotations supose that are persistent,
                // but should not be enabled.
                javax.persistence.JoinColumn column = (javax.persistence.JoinColumn)a;
                String columnName = column.name();
                if(columnName != null) name = columnName;

                if( (!column.insertable()) && (objIsNew) ) {
                    generatedValue = true;
                } else if((!column.updatable()) && (!objIsNew) ) {
                    generatedValue = true;
                }
            }
            else if(a instanceof javax.persistence.Temporal) 
            {
                persistent = true;
                temporal = (javax.persistence.Temporal)a;
            } 
            else if(a instanceof javax.persistence.Embedded) 
            {
                persistent = true;
                embedded = true;
            } 
            else if( (a instanceof javax.persistence.OneToOne) || (a instanceof javax.persistence.OneToMany) 
                     || (a instanceof javax.persistence.ManyToOne) || (a instanceof javax.persistence.ManyToMany) )
            {
                relationType = a;
                persistent = true;
            }
                
        }
        
        if( (name != null) && (persistent) && (!generatedValue || !objIsNew) ) {
            if(embedded) {
                // TO TEST:
                try {
                    EntityEditorImpl ee = new EntityEditorImpl(embedded);
                    comp = ee;

                    compGetter = ee.getClass().getMethod("getEntity");
                    Object value = entityProperty.getValue();
                    if(value != null) ee.setEntity(value);
                    else ee.newEntity(memberClass);
                    
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                
            } else {
                
                // Other editors:
                javax.persistence.TemporalType defaultTemporalType = javax.persistence.TemporalType.TIMESTAMP;
                if( (temporal != null) && (temporal.value() != null) ) defaultTemporalType = temporal.value();
                comp = EditorFactory.getPropertyEditor(entityProperty, defaultLength, defaultTemporalType);
                binder = (JComponentDataBinder)((JComponent)comp).getClientProperty("dataBinder");
                
            }

            
            if(comp != null) {
                if(generatedValue && (!objIsNew) ) {
                    comp.setEnabled(false);
                } else {
                    if(binder == null) {
                        binder = new JComponentDataBinder(comp, compGetter, editor, entityProperty);
                    }
                    if( (comp != null) && (comp instanceof JComponent) ) {
                        ((JComponent)comp).putClientProperty("dataBinder", binder);
                    }
                    binders.add(binder);
                }
            }

            if(comp != null) {
                if(!embedded) {
                    String labelText = I18n.getLiteral(name);
                    if( (propertyDescriptor != null) && (propertyDescriptor.displayName() != null) && (propertyDescriptor.displayName().length() > 0) )
                    {
                        labelText = propertyDescriptor.displayName();
                        if( (propertyDescriptor.resourceBundle() != null) && (propertyDescriptor.resourceBundle().length() > 0) )
                        {
                            try {
                                ResourceBundle bundle = ResourceBundle.getBundle(propertyDescriptor.resourceBundle());
                                String i18nLabelText = bundle.getString(labelText);
                                if( (i18nLabelText != null) && (i18nLabelText.length() > 0) ) labelText = i18nLabelText;
                            } catch(Exception ex) {
                                System.out.println("EntityEditorImpl: Error loading bundle" + propertyDescriptor.resourceBundle());
                            }
                        }
                    }
                    
                    add(new JLabel(labelText), gbcLabel);
                    add(comp, gbcComponent);
                    
                } else {
                    add(comp, gbcEmbeddedComponent);
                }
            }
        }
        
    }
    
    private String capitalize(String name)
    {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    
    public Insets getInsets() {
        return borderInsets;
    }

   
}
