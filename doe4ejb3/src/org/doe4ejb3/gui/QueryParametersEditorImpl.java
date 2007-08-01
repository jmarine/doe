/*
 * QueryParametersEditorImpl.java
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
import java.util.HashMap;

import javax.swing.*;

import javax.persistence.TemporalType;

import org.doe4ejb3.binding.*;

/**
 *
 * @author Jordi Marine Fort
 */
public class QueryParametersEditorImpl extends JPanel  
{

    private final static Insets borderInsets = new Insets(4,2,4,2);

    private final static GridBagConstraints gbcLabel = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,4,2,4), 0,0);
    private final static GridBagConstraints gbcComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,4,6,4), 0,0);
    
    
    private EntityManagerPane manager = null;
    private HashMap parameterTypes = null;
    private HashMap parameterValues = null;
    private BindingContext bindingContext = null;
    
    
    /**
     * Creates a new instance of QueryParametersEditorImpl
     */
    public QueryParametersEditorImpl(EntityManagerPane manager, HashMap parameterTypes) {
        this.manager = manager;
        this.parameterTypes = parameterTypes;
        this.parameterValues = new HashMap();
        this.bindingContext = new BindingContext();
        initComponents();
    }    

    
    public HashMap getParameterValues() throws IllegalAccessException, InvocationTargetException
    {
        bindingContext.commitUncommittedValues();
        return parameterValues;
    }
    

    private void initComponents()
    {
        setLayout(new GridBagLayout());

        for(Object parameterName : parameterTypes.keySet()) 
        {
            // TODO: implement generic type (i.e.: entity has property relation with some value?)
            HashKeyProperty property = new HashKeyProperty(parameterValues, (String)parameterName, (Class)parameterTypes.get(parameterName), null);
            handleParameterProperty(property);
        }
        
        bindingContext.bind();
    }



    
    private void handleParameterProperty(Property property)
    {
        int defaultLength = 20;
        JComponent comp = EditorFactory.getPropertyEditor(manager.getPersistenceUnitName(), property, defaultLength, TemporalType.TIMESTAMP);
        if(comp != null) {
            Object binding = comp.getClientProperty("dataBinding");
            if(binding != null) bindingContext.addBinding(binding);

            add(new JLabel(capitalize(I18n.getLiteral(property.getName())) + ":"), gbcLabel);
            add(comp, gbcComponent);
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
