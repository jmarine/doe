/*
 * EditorFactory.java
 *
 * Created on 18 / august / 2006, 21:38
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.DefaultListModel;
import javax.swing.text.JTextComponent;


import javax.persistence.TemporalType;

import org.doe4ejb3.annotation.Editor;
import org.doe4ejb3.util.JPAUtils;


/**
 *
 * @author Jordi Marine Fort
 */
public class EditorFactory {
    
    /**
     * Creates a new instance of EditorFactory
     */
    public static EntityEditorInterface getEntityEditor(Class entityClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        // search for @Editor annotation in @Entity/@Embedded class.
        EntityEditorInterface entityEditor = null;
        Editor editorAnnotation = (Editor)entityClass.getAnnotation(Editor.class);
        if( (editorAnnotation != null) && (editorAnnotation.className() != null) && (editorAnnotation.className().length() > 0) ) {
            entityEditor = (EntityEditorInterface)Class.forName(editorAnnotation.className()).newInstance();
        } else {
            entityEditor = new EntityEditorImpl();
        }
        return entityEditor;
    }
    
    public static JComponent getEditor(Property property, int defaultLength, TemporalType defaultTemporalType)
    {
        JComponent comp = null;
        Method compGetter = null;
        java.beans.PropertyEditor editor = null;        
        JComponentDataBinder binder = null;
        
        boolean isCollection = false;
        Class memberClass = null;
        try {
            memberClass = property.getType();
            if(java.util.Collection.class.isAssignableFrom(memberClass)) {
                isCollection = true;
                System.out.println("EditorFactory: property " + property.getName() + " is a collection");
                ParameterizedType paramType = (ParameterizedType)property.getGenericType();
                if(paramType != null) memberClass = (Class)(paramType.getActualTypeArguments()[0]);
            } else {
                System.out.println("EditorFactory: property " + property.getName() + " is not a collection");
            }
        } catch(Exception ex) {
            throw new RuntimeException("Property type error: " + ex.getMessage());
        }
        
        
        if(memberClass.getAnnotation(javax.persistence.Entity.class) != null) {
            if(isCollection) {
                // OneToMany || ManyToMany
                try {
                    System.out.println("EditorFactory: OneToMany or ManyToMany!!!");
                    
                    final DefaultListModel listModel = new DefaultListModel();
                    Method modelGetter = listModel.getClass().getMethod("toArray");
                    binder = new JComponentDataBinder(listModel, modelGetter, editor, property);
                    comp = getEditorForMultivaluedProperty(memberClass, property, listModel);

                } catch(Exception ex) {
                    System.out.println("Error loading property: " + ex.getMessage());
                    ex.printStackTrace();
                }
            
            } else {
                
                // OneToOne || ManyToOne
                try {
                    JComboBox combo = new JComboBox(JPAUtils.findAllEntities(memberClass).toArray());
                    comp = combo;
                    compGetter = comp.getClass().getMethod("getSelectedItem");
                    Object value = property.getValue();
                    if(value != null) combo.setSelectedItem(value);
                } catch(Exception ex) {
                    System.out.println("Error loading property: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else if(java.util.Date.class.isAssignableFrom(memberClass)) {
            TemporalType temporalType = defaultTemporalType;
            if(memberClass.isAssignableFrom(java.sql.Date.class)) temporalType = TemporalType.DATE;
            else if(memberClass.isAssignableFrom(java.sql.Time.class)) temporalType = TemporalType.TIME;
            else if(memberClass.isAssignableFrom(java.sql.Timestamp.class)) temporalType = TemporalType.TIMESTAMP;

            JSpinner spinner = new JSpinner();
            comp = spinner;

            switch(temporalType) {
                case DATE:
                  spinner.setModel(new SpinnerDateModel());
                  spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
                  break;
                case TIME:
                  spinner.setModel(new SpinnerDateModel());
                  spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
                  break;
                case TIMESTAMP:
                  spinner.setModel(new SpinnerDateModel());
                  break;
            }

            try {
                compGetter = spinner.getClass().getMethod("getValue");
                Object value = property.getValue();
                if(value != null) spinner.setValue(value);
            } catch(Exception ex) {
                spinner.setValue(new Date());
                ex.printStackTrace();
            }
                
        } else {
            
            try {
                editor = java.beans.PropertyEditorManager.findEditor(memberClass);
                System.out.println("EditorFactory: property editor for " + memberClass.getName() + "=" + editor);
                
                Component customComponent = null;
                if( (editor != null) && (editor.supportsCustomEditor()) && ((customComponent = editor.getCustomEditor()) != null) ) {
                    if(customComponent instanceof JComponent) {
                        comp = (JComponent)customComponent;
                    } else {
                        JPanel panel = new JPanel();  // A JComponent is needed to putClientProperty("dataBinder", binder)
                        panel.setLayout(new FlowLayout());
                        panel.add(customComponent);
                        comp = panel;
                    }

                    Method editorGetter = editor.getClass().getMethod("getValue");
                    Object value = property.getValue();
                    if(value != null) editor.setValue(value);

                    binder = new JComponentDataBinder(editor, editorGetter, editor, property);
                    
                } else if( (editor != null) && ((memberClass == Boolean.TYPE) || (java.lang.Boolean.class.isAssignableFrom(memberClass))) ) { 
                    JCheckBox checkBox = new JCheckBox();
                    comp = checkBox;
                    compGetter = checkBox.getClass().getMethod("isSelected");

                    Object booleanObject = property.getValue();
                    System.out.println("EditorFactory: Boolean TYPE: property class=" + booleanObject.getClass().getName() + ", value=" + booleanObject);

                    Boolean value = (Boolean)booleanObject;
                    if(value != null) {
                        checkBox.setSelected(value.booleanValue());
                    }
                    
                } else { // using JTextField or JTextArea depending on Column's length attribute
                    int length = defaultLength;
                    if(property instanceof ObjectProperty) {
                        ObjectProperty objectProperty = (ObjectProperty)property;
                        javax.persistence.Column column = (javax.persistence.Column)objectProperty.getAnnotation(javax.persistence.Column.class);
                        if( (column != null) && (column.length() > 0) ) length = column.length();
                    }
                    
                    JTextComponent textField = null;
                    if(length < 100) {
                        textField = new JTextField(length);
                        compGetter = textField.getClass().getMethod("getText");
                        comp = textField;
                    } else {
                        textField = new javax.swing.JTextPane();
                        // textField.setMinimumSize(new java.awt.Dimension(defaultLength * 10, 80));
                        textField.setPreferredSize(new java.awt.Dimension(defaultLength * 10, 80));
                        compGetter = textField.getClass().getMethod("getText");
                        binder = new JComponentDataBinder(textField, compGetter, editor, property);                        
                        comp = new JScrollPane(textField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    }

                    Object value = property.getValue();
                    if(value != null) {
                        if(editor != null) {
                            editor.setValue(value);
                            textField.setText(editor.getAsText());
                        } else {
                            textField.setText(value.toString());
                        }
                    }
                }
            } catch(Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }

        }

            
        if(comp != null) {
            if(binder == null) {
                binder = new JComponentDataBinder(comp, compGetter, editor, property);
            }
            if( (comp != null) && (comp instanceof JComponent) ) {
                ((JComponent)comp).putClientProperty("dataBinder", binder);
            }
        }
        return comp;
    }
    

    /** setup an editor for a multi-valued property */
    private static JComponent getEditorForMultivaluedProperty(final Class memberClass, final Property property, final DefaultListModel listModel) throws InvocationTargetException, IllegalAccessException {
        final JPanel panel = new JPanel();
        final List allValues = JPAUtils.findAllEntities(memberClass);

        // populate listmodel with property's values
        Collection values = (Collection)property.getValue();
        System.out.println("Property " + property.getName() + " collection size = " + values.size());
        Iterator iter = values.iterator();
        while(iter.hasNext()) {
            Object valueToSelect = iter.next();
            listModel.addElement(valueToSelect);
        }

        // configure JList component
        final JList jList = new JList();
        jList.setVisibleRowCount(6);
        jList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jList.setModel(listModel);
        // TODO: allow drag and drop operations
        
        // configure "add" button
        JButton btnAddItem = new JButton("Add");
        btnAddItem.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt)  {
               Object newItem = JOptionPane.showInternalInputDialog(panel, "Select new item:", "Add new item", JOptionPane.QUESTION_MESSAGE, null, allValues.toArray(), null);
               if(newItem != null) {
                  if(!listModel.contains(newItem)) {
                     listModel.addElement(newItem);
                  }
               }
           }
        });

        // configure "delete" button
        JButton btnDeleteItem = new JButton("Delete");
        btnDeleteItem.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt)  {
               int indexesToRemove[] = jList.getSelectedIndices();
               System.out.println("EditorFactory: removing selected items: " + indexesToRemove);
               if(indexesToRemove != null) {
                   for(int i = 0; i < indexesToRemove.length; i++) {
                       System.out.println("EditorFactory: removing item at: " + indexesToRemove[i]);
                       listModel.removeElementAt(indexesToRemove[i]);
                   }
               }
           }
        });
        
        
        // configure panel layout
        panel.setLayout(new GridBagLayout());
        panel.add(new JScrollPane(jList), gbcList);
        panel.add(btnAddItem, gbcButton);
        panel.add(btnDeleteItem, gbcButton);
        
        return panel;
    }

    
    /** Private constants to layout item list */
    private final static GridBagConstraints gbcList = new GridBagConstraints(0,0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,0,2,0), 0,0);
    
    /** Private constants to layout buttons */
    private final static GridBagConstraints gbcButton = new GridBagConstraints(GridBagConstraints.RELATIVE,1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,0,5,3), 0,0);
    
}
