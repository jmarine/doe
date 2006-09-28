/*
 * EditorFactory.java
 *
 * Created on 18 / august / 2006, 21:38
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.Component;
import java.awt.FlowLayout;
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
                    comp = getEditorForEntityCollection(memberClass, property, listModel);

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
                
                if( (editor != null) && (editor.supportsCustomEditor()) ) {
                    System.out.println("EditorFactory: searching for custom editor: " + editor.getClass());
                    Component awtComponent = editor.getCustomEditor();
                    if(awtComponent instanceof JComponent) {
                        comp = (JComponent)awtComponent;
                    } else {
                        JPanel panel = new JPanel();
                        panel.setLayout(new FlowLayout());
                        panel.add(awtComponent);
                        comp = panel;
                    }
                    System.out.println("EditorFactory: custom editor: " + comp);

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
                    
                } else {
                    JTextComponent textField = (defaultLength < 100)? new JTextField(defaultLength) : new JTextArea(6, 100);
                    comp = textField;
                    compGetter = textField.getClass().getMethod("getText");

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

    private static JComponent getEditorForEntityCollection(final Class memberClass, final Property property, final DefaultListModel listModel) throws InvocationTargetException, IllegalAccessException {
        JComponent comp;

        List allValues = JPAUtils.findAllEntities(memberClass);
        final JList list = new JList();
        list.setVisibleRowCount(6);
        list.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setModel(listModel);
        
        
        // TODO: refactor to method & better layout
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JScrollPane(list));
        
        final JComboBox newItemComboBox = new JComboBox(allValues.toArray());
        newItemComboBox.insertItemAt("(select new value)", 0);
        newItemComboBox.setSelectedIndex(0);
        panel.add(newItemComboBox);
        
        JButton btnAddItem = new JButton("Add");
        btnAddItem.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt)  {
             System.out.println("EditorFactory: adding selected index: " + newItemComboBox.getSelectedIndex());
             if(newItemComboBox.getSelectedIndex() > 0) {
                 Object newItem = newItemComboBox.getSelectedItem();
                 if(!listModel.contains(newItem)) {
                     System.out.println("EditorFactory: adding selected item: " + newItemComboBox.getSelectedIndex());                             
                     listModel.addElement(newItem);
                 }
             }
           }
        });
        panel.add(btnAddItem);
        
        JButton btnDeleteItem = new JButton("Delete");
        btnDeleteItem.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt)  {
               int indexesToRemove[] = list.getSelectedIndices();
               System.out.println("EditorFactory: removing selected items: " + indexesToRemove);
               if(indexesToRemove != null) {
                   for(int i = 0; i < indexesToRemove.length; i++) {
                       System.out.println("EditorFactory: removing item at: " + indexesToRemove[i]);
                       listModel.removeElementAt(indexesToRemove[i]);
                   }
               }
           }
        });
        panel.add(btnDeleteItem);
        
        comp = panel;                        

        

        ListSelectionModel selectionModel = list.getSelectionModel();
        Collection values = (Collection)property.getValue();
        System.out.println("Property " + property.getName() + " collection size = " + values.size());
        Iterator iter = values.iterator();
        while(iter.hasNext()) {
            Object valueToSelect = iter.next();
            listModel.addElement(valueToSelect);
            /* before
            int pos = allValues.indexOf(valueToSelect);
            selectionModel.addSelectionInterval(pos,pos);
             */
        }
        return comp;
    }
    
}
