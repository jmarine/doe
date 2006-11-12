/**
 * EditorFactory.java
 *
 * Created on 18 / august / 2006, 21:38
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.JTextComponent;

import javax.persistence.TemporalType;

import org.doe4ejb3.event.EntityEvent;
import org.doe4ejb3.event.EntityListener;
import org.doe4ejb3.exception.ApplicationException;
import org.doe4ejb3.annotation.EntityDescriptor;
import org.doe4ejb3.util.JPAUtils;


public class EditorFactory 
{
    
    /**
     * Creates a new instance of EditorFactory
     */
    public static EntityEditorInterface getEntityEditor(Class entityClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        // search for @EntityDescriptor annotation in @Entity/@Embedded class.
        EntityEditorInterface entityEditor = null;
        EntityDescriptor editorAnnotation = (EntityDescriptor)entityClass.getAnnotation(EntityDescriptor.class);
        if( (editorAnnotation != null) && (editorAnnotation.editorClassName() != null) && (editorAnnotation.editorClassName().length() > 0) ) {
            entityEditor = (EntityEditorInterface)Class.forName(editorAnnotation.editorClassName()).newInstance();
        } else {
            entityEditor = new EntityEditorImpl();
        }
        return entityEditor;
    }
    
    public static JComponent getPropertyEditor(Property property, int defaultLength, TemporalType defaultTemporalType)
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
        
        org.doe4ejb3.beans.TemporalTypeEditorSupport.registerTemporalTypeEditors();
        if(memberClass.getAnnotation(javax.persistence.Entity.class) != null) {
            if(isCollection) {
                // OneToMany || ManyToMany
                try {
                    System.out.println("EditorFactory: OneToMany or ManyToMany!!!");
                    JComponentDataBinder binderOutParam[] = new JComponentDataBinder[1];
                    comp = getCollectionEditor(property, memberClass, false, defaultLength, binderOutParam);
                    binder = binderOutParam[0];

                } catch(Exception ex) {
                    System.out.println("Error loading property: " + ex.getMessage());
                    ex.printStackTrace();
                }
            
            } else {
                
                // OneToOne || ManyToOne
                try {
                    final javax.swing.DefaultComboBoxModel comboBoxModel = new javax.swing.DefaultComboBoxModel();
                    final JComboBox combo = new JComboBox(comboBoxModel);
                    final Class optionClass = memberClass;

                    // define combobox prototype dimensions
                    combo.setPrototypeDisplayValue("sample value to calculate drop-down list dimension for combobox!");
                    for(int i = 0; i < 10; i++) combo.addItem(null);

                    combo.addPopupMenuListener(new PopupMenuListener() {
                        public void popupMenuCanceled(PopupMenuEvent e) { }
                        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
                        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                            // Lazy load of drop-down list items:
                            if(combo.getClientProperty("lazyModel") == null) 
                            {
                                try {
                                    combo.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                    Object selectedItem = comboBoxModel.getSelectedItem();
                                    comboBoxModel.removeAllElements();
                                    comboBoxModel.addElement(null);
                                    System.out.println("Searching items!!!");
                                    for(Object option : JPAUtils.findAllEntities(optionClass).toArray()) {
                                        comboBoxModel.addElement(option);
                                    }
                                    comboBoxModel.setSelectedItem(selectedItem);
                                
                                    combo.putClientProperty("lazyModel", comboBoxModel);
                                    // combo.hidePopup();
                                    // combo.showPopup();

                                    // combo.getUI().setPopupVisible( combo, true );
                                    
                                } finally {
                                    combo.putClientProperty("lazyModel", null);
                                    combo.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                }
                            }
                            
                        }
                    });

                    comp = combo;
                    compGetter = comp.getClass().getMethod("getSelectedItem");

                    Object value = property.getValue();
                    if(value != null) {
                        combo.addItem(value);
                        combo.setSelectedItem(value);
                    } else {
                        combo.setSelectedIndex(0);
                    }
                    
                } catch(Exception ex) {
                    System.out.println("Error loading property: " + ex.getMessage());
                    ex.printStackTrace();
                }
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

                    binder = new JComponentDataBinder(editor, editorGetter, null, property);  // editor is null to get real value from "editor.getValue" method (no conversion to string representation).
                    
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

                    JTextComponent textField = null;
                    if(defaultLength < 100) {
                        textField = new JTextField(defaultLength);
                        compGetter = textField.getClass().getMethod("getText");
                        comp = textField;
                    } else {
                        textField = new javax.swing.JTextPane();
                        textField.setPreferredSize(new java.awt.Dimension(400, 80));
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
    

    /** 
     * Setup an editor for a multi-valued property 
     * TODO: allow drag and drop operations
     */
    public static JComponent getCollectionEditor(final Property property, final Class memberClass, final boolean isManagerWindow, int defaultLength, JComponentDataBinder binderOutParam[]) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, Exception {
        final JPanel panel = new JPanel();
        final JTable jTable = new JTable();
        final DefaultListModel listModel = new DefaultListModel();
        final ListSelectionModel listSelectionModel = jTable.getSelectionModel();
        final ObjectPropertyTableModel objectPropertyTableModel = new ObjectPropertyTableModel(memberClass, listModel);
        
        panel.putClientProperty("listModel", listModel);
        panel.putClientProperty("listSelectionModel", listSelectionModel);
        
        if(property != null) {
            // configure data binder
            Method modelGetter = listModel.getClass().getMethod("toArray");
            binderOutParam[0] = new JComponentDataBinder(listModel, modelGetter, null, property);

            // populate listmodel with property's values
            Collection values = (Collection)property.getValue();
            System.out.println("Property " + property.getName() + " collection size = " + values);
            if(values != null) {
                Iterator iter = values.iterator();
                while(iter.hasNext()) {
                    Object valueToSelect = iter.next();
                    listModel.addElement(valueToSelect);
                }
            }
        }

        // configure actions:
        AbstractAction newAction = new AbstractAction("New") {
            public void actionPerformed(ActionEvent evt)  {
                try { 
                    JInternalFrame iFrame = DomainObjectExplorer.getInstance().openInternalFrameEntityEditor(memberClass, null);
                    if(!isManagerWindow) {
                        final EventListenerList listenerList = (EventListenerList)iFrame.getClientProperty("entityListeners");
                        listenerList.add(EntityListener.class, new EntityListener() {
                            public void entityChanged(EntityEvent event) {
                                if(event.getEventType() == EntityEvent.ENTITY_INSERT) {
                                    listModel.addElement(event.getNewEntity());
                                }
                            }
                        });
                    }
                } catch(ApplicationException ex) { 
                    JOptionPane.showInternalMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                } catch(Exception ex) { 
                    JOptionPane.showInternalMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                }
            }
        };
        
        AbstractAction addExistingAction = new AbstractAction("Add") {
                public void actionPerformed(ActionEvent evt)  {
                    try {
                        panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        List allValues = JPAUtils.findAllEntities(memberClass);
                        Object newItem = JOptionPane.showInternalInputDialog(panel, "Select new item:", "Add new " + I18n.getEntityName(memberClass), JOptionPane.QUESTION_MESSAGE, null, allValues.toArray(), null);
                        if(newItem != null) {
                            // FIXME: caution with duplicated relations and "Set" collection types.
                            if(!listModel.contains(newItem)) {  
                                listModel.addElement(newItem);
                            } else {
                                JOptionPane.showInternalMessageDialog(panel, "Selected item already exists!", "Error:", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } finally {
                        panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
               }
        };
        
        final AbstractAction editAction = new AbstractAction("Edit") {
            public void actionPerformed(ActionEvent evt)  {
                try { 
                    if(listSelectionModel.isSelectionEmpty()) {
                        throw new ApplicationException("No items selected");
                    } else {
                        for(int index = listSelectionModel.getMaxSelectionIndex(); index >= listSelectionModel.getMinSelectionIndex(); index--) {
                            if(listSelectionModel.isSelectedIndex(index)) {
                                JInternalFrame iFrame = DomainObjectExplorer.getInstance().openInternalFrameEntityEditor(memberClass, listModel.getElementAt(index)); 
                                EventListenerList listenerList = (EventListenerList)iFrame.getClientProperty("entityListeners");
                                listenerList.add(EntityListener.class, new EntityListener() {
                                    public void entityChanged(EntityEvent event) {
                                        if(event.getEventType() == EntityEvent.ENTITY_UPDATE) {
                                            // update JTable
                                            System.out.println("EditorFactory: searching index of OldEntity = " + event.getOldEntity());
                                            System.out.println("EditorFactory: searching index of NewEntity = " + event.getNewEntity());
                                            int index = listModel.indexOf(event.getOldEntity());
                                            System.out.println("EditorFactory: index found =  " + index);
                                            if(index != -1) listModel.setElementAt(event.getNewEntity(), index);
                                            else listModel.setElementAt(listModel.getElementAt(0), 0);  // refresh rows/columns
                                        }
                                    }
                                });
                            }
                        }
                    }
                    
                } catch(ApplicationException ex) { 
                    JOptionPane.showInternalMessageDialog(panel, ex.getMessage(), "Edit error", JOptionPane.ERROR_MESSAGE);                
                    
                } catch(Exception ex) { 
                    JOptionPane.showInternalMessageDialog(panel, "Error: " + ex.getMessage(), "Edit error", JOptionPane.ERROR_MESSAGE);                
                }
           }
        };
        
        final AbstractAction deleteAction = new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent evt)  {
                try {
                    if(listSelectionModel.isSelectionEmpty()) {
                        throw new ApplicationException("No items selected");
                    } else {
                        int confirm = JOptionPane.showInternalConfirmDialog(DomainObjectExplorer.getInstance().getDesktopPane(), "Do you really want to delete selected objects?", "Confirm operation", JOptionPane.OK_CANCEL_OPTION);
                        if(confirm == JOptionPane.OK_OPTION) 
                        {
                            try {
                                panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                for(int index = listSelectionModel.getMaxSelectionIndex(); index >= listSelectionModel.getMinSelectionIndex(); index--) {
                                    if(listSelectionModel.isSelectedIndex(index)) {
                                        System.out.println("EditorFactory: removing selected index: " + index);
                                        if(isManagerWindow) {  // delete command from "EntityManagerPane"
                                            Object entity = listModel.getElementAt(index);
                                            JPAUtils.removeEntity(entity);                                
                                        }
                                        listModel.removeElementAt(index);
                                    }
                                }
                            } finally {
                                panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
                    }

                } catch(ApplicationException ex) { 
                    JOptionPane.showInternalMessageDialog(panel, ex.getMessage(), "Delete error", JOptionPane.ERROR_MESSAGE);
                    
                } catch(Exception ex) { 
                    JOptionPane.showInternalMessageDialog(panel, "Error: " + ex.getMessage(), "Delete error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };         

        AbstractAction printAction = new AbstractAction("Print") {
                public void actionPerformed(ActionEvent evt)  {
                    try {
                        if(objectPropertyTableModel.getRowCount() == 0) {
                            throw new ApplicationException("There aren't rows to print.");
                        } else {
                            MessageFormat headerFormat = new MessageFormat(I18n.getEntityName(memberClass) + " list:");
                            MessageFormat footerFormat = new MessageFormat("Page {0}");
                            jTable.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
                        }
                    } catch(ApplicationException ex) { 
                        JOptionPane.showInternalMessageDialog(panel, ex.getMessage(), "Printing error", JOptionPane.ERROR_MESSAGE);                
                    } catch(Exception ex) {
                        JOptionPane.showInternalMessageDialog(panel, "Error: " + ex.getMessage(), "Printing error", JOptionPane.ERROR_MESSAGE);
                    }
                }
        };
        
        
        AbstractAction closeAction = new AbstractAction("Close") {
                public void actionPerformed(ActionEvent evt)  
                {        
                    Component parent = (Component)evt.getSource();
                    while( (parent != null) && (!(parent instanceof JInternalFrame)) ) {
                        parent = parent.getParent();
                    }
                
                    if(parent != null) {
                        JInternalFrame iFrame = (JInternalFrame)parent;
                        iFrame.dispose();
                    }
                }
        };

        
        // setup enable state, and enable change listeners:
        editAction.setEnabled(false);
        deleteAction.setEnabled(false);
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enabled = !listSelectionModel.isSelectionEmpty();
                editAction.setEnabled(enabled);
                deleteAction.setEnabled(enabled);
            }
        });


        // configure popup menu
        javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
        popupMenu.add(newAction).setMnemonic('n');
        popupMenu.add(editAction).setMnemonic('e');
        popupMenu.add(new javax.swing.JSeparator());
        popupMenu.add(deleteAction).setMnemonic('d');
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent e) { }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                boolean enabled = !listSelectionModel.isSelectionEmpty();
                editAction.setEnabled(enabled);
                deleteAction.setEnabled(enabled);
            }
        });
        

        // configure button panel:
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        if(!isManagerWindow) {  
            // configure "add" button (only used in OneToMany and ManytoMany relationships)
            JButton btnAddExistingItem = new JButton(addExistingAction);
            buttonPanel.add(btnAddExistingItem);
        }

        // configure "new" button
        JButton btnNewItem = new JButton(newAction);
        buttonPanel.add(btnNewItem);

        // configure "edit" button
        JButton btnEditItem = new JButton(editAction);
        btnEditItem.addActionListener(editAction);
        buttonPanel.add(btnEditItem);

        // configure "delete" button
        JButton btnDeleteItem = new JButton(deleteAction);
        buttonPanel.add(btnDeleteItem);

        if(isManagerWindow) {
            // configure "print" button
            JButton btnPrint = new JButton(printAction);
            buttonPanel.add(btnPrint);

            // configure "close" button
            JButton btnClose = new JButton(closeAction);
            buttonPanel.add(btnClose);
            
            // configure mnemonics:
            btnNewItem.setMnemonic('n');
            btnEditItem.setMnemonic('e');
            btnDeleteItem.setMnemonic('d');
            btnPrint.setMnemonic('p');
            btnClose.setMnemonic('c');
        }

        
        // configure panel layout
        JScrollPane scrollableItems = new JScrollPane(jTable);
        scrollableItems.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrollableItems.setComponentPopupMenu(popupMenu);
        
        jTable.setModel(objectPropertyTableModel);
        jTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable.setCellSelectionEnabled(false);
        jTable.setRowSelectionAllowed(true);
        jTable.setComponentPopupMenu(popupMenu);

        panel.setPreferredSize(new java.awt.Dimension(450, 180));        
        panel.setLayout(new BorderLayout());
        panel.add("Center", scrollableItems);
        panel.add("South", buttonPanel);
        
        return panel;
    }
    
}


