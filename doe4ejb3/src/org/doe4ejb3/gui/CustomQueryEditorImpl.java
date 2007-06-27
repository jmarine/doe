/*
 * CustomQueryEditorImpl.java
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.swing.*;

import javax.persistence.TemporalType;
import javax.persistence.Entity;

import org.doe4ejb3.util.JPAUtils;
import org.doe4ejb3.util.ReflectionUtils;
import org.doe4ejb3.gui.HashKeyProperty;


public class CustomQueryEditorImpl extends JPanel implements java.awt.event.ItemListener
{

    private final static Insets borderInsets = new Insets(4,2,4,2);

    private final static GridBagConstraints gbcProperty = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,4,2,4), 0,0);
    private final static GridBagConstraints gbcOperator = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,4,2,4), 0,0);
    private final static GridBagConstraints gbcComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,4,6,4), 0,0);
    
    
    private Class entityClass = null;
    private HashMap parameterValues = null;
    private ArrayList<JComponentDataBinding> bindingContext = null;
    private ArrayList<JComboBox> propertySelectors = null;
    
    
    /**
     * Creates a new instance of QueryParametersEditorImpl
     */
    public CustomQueryEditorImpl(Class entityClass) {
        // this.parameterTypes = parameterTypes;
        // this.parameterValues = new HashMap();
        this.entityClass = entityClass;
        this.bindingContext = new ArrayList();
        this.propertySelectors = new ArrayList();
        initComponents();
    }    
    
    
    public String prepareEJBQL()
    {
        int count = 0;
        this.parameterValues = new HashMap();
        
        String entityName = JPAUtils.getEntityName(entityClass);        
        StringBuffer sb = new StringBuffer("SELECT OBJECT(e) FROM " + entityName + " e");
        StringBuffer where = new StringBuffer();

        for(JComboBox selector : propertySelectors) {
            count++;
            JComboBox operator = (JComboBox)selector.getClientProperty("operator");
            if( (selector.getSelectedIndex() > 0) && (operator.getSelectedIndex() > 0) ) {
                JComponentDataBinding binding = (JComponentDataBinding)selector.getClientProperty("dataBinding");
                try {
                    HashKeyProperty property = (HashKeyProperty)selector.getSelectedItem();
                    String parameterName = property.getName() + count;
                    binding.commitUncommittedValues();

                    if(where.length() > 0) where.append(" AND ");
                    if(operator.getSelectedItem().toString().indexOf("MEMBER") == -1) {
                        // normal operands order
                        where.append("e."); where.append(property.getName()); where.append(" ");
                        where.append(operator.getSelectedItem()); where.append(" ");
                        where.append(":" + parameterName);
                    } else {
                        // inverse operands order
                        where.append(":" + parameterName); where.append(" ");
                        where.append(operator.getSelectedItem()); where.append(" ");
                        where.append("e."); where.append(property.getName()); 
                    }
                    parameterValues.put(parameterName, property.getValue());

                    System.out.println("DEBUG: Parameter " + parameterName + " = " + property.getValue());
                } catch(Exception ex) {
                    System.out.println("CustomQueryEditorImpl.prepareEJBQL: Error executing binding (" + binding + "): " + ex.getMessage());
                    System.err.println("CustomQueryEditorImpl.prepareEJBQL: Error executing binding (" + binding + "): " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        }
        
        if(where.length() > 0) {
            sb.append(" WHERE ");
            sb.append(where);
        }
        
        System.out.println("DEBUG: Custom EJBQL = " + sb.toString() );
        return sb.toString();
    }

    
    public HashMap prepareParameterValues() throws IllegalAccessException, InvocationTargetException
    {
        return parameterValues;
    }
    

    private void initComponents()
    {
        setLayout(new GridBagLayout());
        addPropertySelector();

        //for(Object parameterName : parameterTypes.keySet()) 
        //{
        //    HashKeyProperty property = new HashKeyProperty(parameterValues, (String)parameterName, (Class)parameterTypes.get(parameterName));
        //    handleParameterProperty(property);
        //}
        
    }
    
    private void addPropertySelector()
    {
        String operators[] = { "" , "=", "<>", "<", "<=", ">", ">=", "LIKE", "NOT LIKE", "MEMBER", "NOT MEMBER" }; 
        JComboBox selector = new JComboBox(getProperties(entityClass).toArray());
        JComboBox operator = new JComboBox(operators);
        JPanel editorContainer = new JPanel();
        editorContainer.setLayout(new BorderLayout());

        ((DefaultComboBoxModel)selector.getModel()).insertElementAt("", 0);
        selector.setSelectedIndex(0);
        selector.putClientProperty("operator", operator);
        selector.putClientProperty("editorContainer", editorContainer);
        selector.addItemListener(this);
        propertySelectors.add(selector);
       
        add(selector, gbcProperty);
        add(operator, gbcOperator);
        add(editorContainer, gbcComponent);
        revalidate();
    }


    public void itemStateChanged(java.awt.event.ItemEvent event) 
    {
        JComboBox selector = (JComboBox)event.getSource();
        JComboBox operator = (JComboBox)selector.getClientProperty("operator");
        JPanel editorContainer = (JPanel)selector.getClientProperty("editorContainer");

        // clear previous bindings/editors
        JComponentDataBinding oldBinding = (JComponentDataBinding)selector.getClientProperty("dataBinding");
        if(oldBinding != null) 
        {
            // oldBinding.unbind();
            bindingContext.remove(oldBinding);
            selector.putClientProperty("dataBinding", null);
        }
        editorContainer.removeAll();
        // editorContainer.invalidate();

        if(selector.getSelectedIndex() == 0) {

            selector.putClientProperty("editorValue", null);
            operator.setSelectedIndex(0);
            editorContainer.revalidate();

        } else {


            HashKeyProperty property = (HashKeyProperty)selector.getSelectedItem();
            JComponent comp = EditorFactory.getPropertyEditor(property, 0, TemporalType.TIMESTAMP);
            selector.putClientProperty("editorValue", comp);

            editorContainer.add("Center", comp);
            editorContainer.revalidate();
            if(operator.getSelectedIndex() == 0) {
                operator.setSelectedIndex(1);
            }

            JComponentDataBinding binding = (JComponentDataBinding)comp.getClientProperty("dataBinding");
            if(binding != null) {
                bindingContext.add(binding);
                //binding.bind();
                selector.putClientProperty("dataBinding", binding);
            }

        }
        
        // Add new condition that may be needed
        boolean emptyPropertySelector = false;
        for(JComboBox combo : propertySelectors) {
            if(combo.getSelectedIndex() == 0) {
                emptyPropertySelector = true;
            }
        }
        if(!emptyPropertySelector) addPropertySelector();
        
    }


    private String capitalize(String name)
    {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    
    public Insets getInsets() {
        return borderInsets;
    }

    public static ArrayList<HashKeyProperty> getProperties(Class entityClass) {
        HashMap map = new HashMap();
        ArrayList<HashKeyProperty> properties = new ArrayList<HashKeyProperty>();
        try {
            BeanInfo bi = Introspector.getBeanInfo(entityClass);
            for(java.beans.PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                // TODO: inherited properties?
                if(pd.getName().equals("class")) continue;
                HashKeyProperty entityProperty = new HashKeyProperty(map, pd.getName(), pd.getPropertyType(), pd.getReadMethod().getGenericReturnType());
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
            for(Field field : entityClass.getFields()) {
                // TODO: inherited fields
                if(field.getName().equals("class")) continue;
                HashKeyProperty entityProperty = new HashKeyProperty(map, field.getName(), field.getType(), field.getGenericType());
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
        } catch(Exception ex) {
            System.out.println("ReflectionUtils: ERROR: " + ex.getMessage());
        }
        
        return properties;
    }
   
}
