/*
 * QueryParametersEditorImpl.java
 *
 * Created on June 6, 2006, 5:45 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import org.doe4ejb3.util.I18n;
import org.doe4ejb3.binding.HashKeyProperty;
import org.doe4ejb3.binding.DoeProperty;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


import org.jdesktop.beansbinding.Binding;
import org.doe4ejb3.binding.BindingContext;


public class QueryParametersEditorImpl extends JPanel implements EditorLayoutInterface
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
    public QueryParametersEditorImpl(EntityManagerPane manager, HashMap parameterTypes) 
    {
        this.manager = manager;
        this.parameterTypes = parameterTypes;
        this.parameterValues = new HashMap();
        this.bindingContext = new BindingContext();
        initComponents();
    }    

    
    public HashMap getParameterValues() throws IllegalAccessException, InvocationTargetException, PropertyVetoException
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
            HashKeyProperty property = new HashKeyProperty((String)parameterName, (Class)parameterTypes.get(parameterName), null);
            handleParameterProperty(property);
        }
        
        bindingContext.bind();
    }

    
    private void handleParameterProperty(DoeProperty property)
    {
        int defaultLength = 20;
        JComponent comp = EditorFactory.getPropertyEditor(this, manager.getPersistenceUnitName(), parameterValues, property, defaultLength);
        if(comp != null) {
            Binding binding = (Binding)comp.getClientProperty("dataBinding");
            if(binding != null) bindingContext.addBinding(binding);

            add(new JLabel(capitalize(I18n.getLiteral(property.getName())) + ":"), gbcLabel);
            add(comp, gbcComponent);
        }
    }
    
    
    private String capitalize(String name)
    {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    
    @Override
    public Insets getInsets() {
        return borderInsets;
    }

    
    public JComponent getCustomEditorLayout() {
        return null;
    }
        
    public JComponent getComponentFromEditorLayout(Class componentType, String componentName) {
        return null;
    }
  
}
