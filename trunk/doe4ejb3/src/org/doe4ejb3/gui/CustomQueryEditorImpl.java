/*
 * CustomQueryEditorImpl.java
 *
 * Created on June 6, 2006, 5:45 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.event.ItemEvent;
import org.doe4ejb3.binding.HashKeyProperty;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.beansbinding.Binding;

import org.doe4ejb3.binding.*;
import org.doe4ejb3.util.JPAUtils;


public class CustomQueryEditorImpl extends JPanel implements java.awt.event.ItemListener, EditorLayoutInterface
{

    private final static Insets borderInsets = new Insets(4,2,4,2);
    private final static String defaultOperators[] = { "             " };
    private final static String simpleFieldOperators[] = { "" , "=", "<>", "<", "<=", ">", ">=", "LIKE", "NOT LIKE", "IS NULL", "IS NOT NULL" }; 
    private final static String manyRelationOperators[] = { "" , "MEMBER", "NOT MEMBER", "IS EMPTY", "IS NOT EMPTY" }; 
    private final static String oneRelationOperators[] = { "" , "=", "<>", "IS NULL", "IS NOT NULL" };

    private final static GridBagConstraints gbcProperty = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,4,2,4), 0,0);
    private final static GridBagConstraints gbcOperator = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,4,2,4), 0,0);
    private final static GridBagConstraints gbcComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,4,6,4), 0,0);
    
    
    private EntityManagerPane manager = null;
    private Class entityClass = null;
    private HashMap parameterValues = null;
    private BindingContext bindingContext = null;
    private ArrayList<JComboBox> propertySelectors = null;
    private JPanel conditionPanel = null;
    private JComboBox filterTypeComboBox = null;
    
    
    /**
     * Creates a new instance of QueryParametersEditorImpl
     */
    public CustomQueryEditorImpl(EntityManagerPane manager, Class entityClass) 
    {
        this.manager = manager;
        this.entityClass = entityClass;
        this.bindingContext = new BindingContext();
        this.propertySelectors = new ArrayList<JComboBox>();
        initComponents();
    }    
    
    
    private void initComponents()
    {
        setLayout(new BorderLayout());
        conditionPanel = new JPanel();
        conditionPanel.setLayout(new GridBagLayout());
        
        filterTypeComboBox = new JComboBox(new String[] { "AND", "OR" });
        filterTypeComboBox.setVisible(false);
        add("West", filterTypeComboBox);
        add("Center", conditionPanel);
        addPropertySelector();
    }

    
    private void addPropertySelector()
    {
        HashMap target = new HashMap();
        JComboBox selector = new JComboBox(getProperties(target, entityClass).toArray());
        JComboBox operator = new JComboBox(defaultOperators);
        JPanel editorContainer = new JPanel();
        editorContainer.setLayout(new BorderLayout());

        ((DefaultComboBoxModel)selector.getModel()).insertElementAt("", 0);
        selector.setSelectedIndex(0);
        selector.putClientProperty("operator", operator);
        selector.putClientProperty("target", target);        
        selector.putClientProperty("editorContainer", editorContainer);
        selector.putClientProperty("dataBinding", null);
        selector.addItemListener(this);
        propertySelectors.add(selector);
       

        operator.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox operator = (JComboBox)e.getSource();
                JComponent editor = (JComponent)operator.getClientProperty("editor");
                if(editor != null) {
                    if((operator.getSelectedIndex() == 0) || (operator.getSelectedItem().toString().toUpperCase().startsWith("IS ")) ) {
                        editor.setVisible(false);  // unary operator or unselected
                    } else {
                        editor.setVisible(true);   // binary operator
                    }
                }
            }
        });
        
        conditionPanel.add(selector, gbcProperty);
        conditionPanel.add(operator, gbcOperator);
        conditionPanel.add(editorContainer, gbcComponent);
        revalidate();
        repaint();
    }

    
    public void itemStateChanged(java.awt.event.ItemEvent event) 
    {
        JComboBox selector = (JComboBox)event.getSource();
        JComboBox operator = (JComboBox)selector.getClientProperty("operator");
        HashMap target = (HashMap)selector.getClientProperty("target");
        JPanel editorContainer = (JPanel)selector.getClientProperty("editorContainer");

        // clear previous bindings/editors
        Binding oldBinding = (Binding)selector.getClientProperty("dataBinding");
        if(oldBinding != null) 
        {
            oldBinding.unbind();
            bindingContext.removeBinding(oldBinding);
            selector.putClientProperty("dataBinding", null);
        }
        editorContainer.removeAll();
        // editorContainer.invalidate();

        if(selector.getSelectedIndex() == 0) {

            operator.setSelectedIndex(0);
            operator.putClientProperty("editor", null);
            editorContainer.revalidate();
            editorContainer.repaint();

        } else {

            HashKeyProperty property = (HashKeyProperty)selector.getSelectedItem();
            JComponent comp = EditorFactory.getPropertyEditor(this, manager.getPersistenceUnitName(), target, property, 0);
            if(comp != null) {
                operator.putClientProperty("editor", comp);
                editorContainer.add("Center", comp);
                editorContainer.revalidate();
            
                if(property.getWriteType(target).getAnnotation(javax.persistence.Entity.class) != null) {
                    operator.setModel(new DefaultComboBoxModel(property.isCollectionType() ? manyRelationOperators : oneRelationOperators));
                } else {
                    operator.setModel(new DefaultComboBoxModel(simpleFieldOperators));
                }
                operator.setSelectedIndex(1);

                Binding binding = (Binding)comp.getClientProperty("dataBinding");
                if(binding != null) {
                    bindingContext.addBinding(binding);
                    binding.bind();
                    selector.putClientProperty("dataBinding", binding);
                }

            } 
        }
        
        // Add a new condition (when may be needed)
        boolean emptyPropertySelector = false;
        for(JComboBox combo : propertySelectors) {
            if(combo.getSelectedIndex() == 0) {
                emptyPropertySelector = true;
            }
        }
        if(!emptyPropertySelector) {
            addPropertySelector();
            filterTypeComboBox.setVisible(true);
            revalidate();
            repaint();
        }
        
    }



    private String capitalize(String name)
    {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    
    @Override
    public Insets getInsets() 
    {
        return borderInsets;
    }

    private ArrayList<HashKeyProperty> getProperties(HashMap target, Class entityClass) 
    {
        ArrayList<HashKeyProperty> properties = new ArrayList<HashKeyProperty>();
        return getProperties(target, properties, entityClass, ""); 
    }
    
    private ArrayList<HashKeyProperty> getProperties(HashMap target, ArrayList<HashKeyProperty> properties, Class entityClass, String prefix) 
    {
        try {
            BeanInfo bi = Introspector.getBeanInfo(entityClass);
            for(java.beans.PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                try {             
                    // FIXME: inherited properties are included?
                    if(pd.getName().equals("class")) continue;
                    HashKeyProperty property = new HashKeyProperty(prefix + pd.getName(), pd.getPropertyType(), pd.getReadMethod().getGenericReturnType());
                    if(property.getWriteType(target).getAnnotation(javax.persistence.Embeddable.class) != null) {
                        getProperties(target, properties, property.getWriteType(target), prefix + property.getName() + ".");    // but don't directly search by embbedded entity
                    } else if(!properties.contains(property)) {
                        properties.add(property);
                        if( (prefix.indexOf(".") == -1) && (property.getWriteType(target).getAnnotation(javax.persistence.Entity.class) != null) && (!property.isCollectionType()) ) {  // 1 level navigation
                            getProperties(target, properties, property.getWriteType(target), prefix + property.getName() + "."); 
                        }
                    }
                    
                } catch(Exception ex) {
                    System.out.println("CustomQueryEditorImpl.getProperties: ERROR: " + ex.getMessage());
                }
            }
            
            for(Field field : entityClass.getFields()) {
                try {
                    // TODO: inherited fields
                    if(field.getName().equals("class")) continue;
                    HashKeyProperty property = new HashKeyProperty(prefix + field.getName(), field.getType(), field.getGenericType());
                    if(property.getWriteType(target).getAnnotation(javax.persistence.Embeddable.class) != null) {
                        getProperties(target, properties, property.getWriteType(target), prefix + property.getName() + ".");  // but don't directly search by embbedded entity
                    } else if(!properties.contains(property)) {
                        properties.add(property);
                        if( (prefix.indexOf(".") == -1) && (property.getWriteType(target).getAnnotation(javax.persistence.Entity.class) != null) && (!property.isCollectionType()) ) {  // 1 level navigation
                            getProperties(target, properties, property.getWriteType(target), prefix + property.getName() + "."); 
                        }
                    }
                
                } catch(Exception ex) {
                    System.out.println("CustomQueryEditorImpl.getProperties: ERROR: " + ex.getMessage());
                }
            }

        } catch(Exception ex) {
            System.out.println("CustomQueryEditorImpl.getProperties: ERROR: " + ex.getMessage());
        }
        
        return properties;
    }


    
    public String getEJBQL()
    {
        bindingContext.commitUncommittedValues();
        
        String entityName = JPAUtils.getEntityName(entityClass);        
        StringBuffer ejbql = new StringBuffer("SELECT OBJECT(e) FROM " + entityName + " e");
        StringBuffer where = new StringBuffer();
        parameterValues = new HashMap();

        int count = 0;
        for(JComboBox selector : propertySelectors) {
            count++;
            JComboBox operator = (JComboBox)selector.getClientProperty("operator");
            if( (selector.getSelectedIndex() > 0) && (operator.getSelectedIndex() > 0) ) {
                HashMap target = (HashMap)selector.getClientProperty("target");
                Object binding = selector.getClientProperty("dataBinding");
                try {
                    HashKeyProperty property = (HashKeyProperty)selector.getSelectedItem();

                    if(where.length() > 0) where.append(" " + filterTypeComboBox.getSelectedItem() + " ");  // AND / OR
                    
                    if(operator.getSelectedItem().toString().toUpperCase().startsWith("IS ")) {
                        where.append("e."); where.append(property.getName()); where.append(" ");
                        where.append(operator.getSelectedItem()); 
                    } else {
                        String parameterName = property.getName() + count;
                        int lastDot = parameterName.lastIndexOf(".");
                        if(lastDot != -1) parameterName = parameterName.substring(lastDot+1);
                        //bindingContext.commitUncommittedValues();

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
                        
                        Object value = property.getValue(target);
                        parameterValues.put(parameterName, value);
                        System.out.println("DEBUG: Parameter " + parameterName + " = " + value);
                    }

                } catch(Exception ex) {
                    System.out.println("CustomQueryEditorImpl.getEJBQL: Error executing binding (" + binding + "): " + ex.getMessage());
                    System.err.println("CustomQueryEditorImpl.getEJBQL: Error executing binding (" + binding + "): " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            }
        }
        
        if(where.length() > 0) {
            ejbql.append(" WHERE ");
            ejbql.append(where);
        }
        
        String retval = ejbql.toString();
        System.out.println("DEBUG: Custom EJBQL = " + retval);
        return retval;
    }

    
    public HashMap getParameterValues() throws IllegalAccessException, InvocationTargetException
    {
        if(parameterValues == null) getEJBQL();
        return parameterValues;
    }


    
    public JComponent getCustomEditorLayout() {
        return null;
    }
    
    public JComponent getComponentFromEditorLayout(Class componentType, String componentName) {
        return null;
    }
    
}
