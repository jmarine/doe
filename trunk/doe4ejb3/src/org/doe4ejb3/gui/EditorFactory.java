/*
 * EditorFactory.java
 *
 * Created on 18 / august / 2006, 21:38
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import com.sun.imageio.plugins.common.I18N;
import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.EventListenerList;
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
                    JComponentDataBinder binderOutParam[] = new JComponentDataBinder[1];
                    comp = getEditorForMultivaluedProperty(memberClass, property, editor, defaultLength, binderOutParam);
                    binder = binderOutParam[0];

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
    

    /** 
     * Setup an editor for a multi-valued property 
     * TODO: allow drag and drop operations
     */
    private static JComponent getEditorForMultivaluedProperty(final Class memberClass, final Property property, final java.beans.PropertyEditor editor, int defaultLength, JComponentDataBinder binderOutParam[]) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, Exception {
        final JPanel panel = new JPanel();
        final DefaultListModel listModel = new DefaultListModel();
        
        // configure data binder
        Method modelGetter = listModel.getClass().getMethod("toArray");
        binderOutParam[0] = new JComponentDataBinder(listModel, modelGetter, editor, property);

        // populate listmodel with property's values
        Collection values = (Collection)property.getValue();
        System.out.println("Property " + property.getName() + " collection size = " + values.size());
        Iterator iter = values.iterator();
        while(iter.hasNext()) {
            Object valueToSelect = iter.next();
            listModel.addElement(valueToSelect);
        }

        // choose best UI control to display item list
        JScrollPane scrollableItems = null;
        ListSelectionModel listSelectionModel = null;
        ObjectPropertyTableModel objectPropertyTableModel = new ObjectPropertyTableModel(memberClass, listModel);
        if(objectPropertyTableModel.getColumnCount() > 0) {
            JTable jTable = new JTable(objectPropertyTableModel);
            jTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            jTable.setRowSelectionAllowed(true);
            listSelectionModel = jTable.getSelectionModel();
            scrollableItems = new JScrollPane(jTable);
        } else {
            JList jList = new JList();
            jList.setVisibleRowCount(6);
            jList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            jList.setModel(listModel);
            listSelectionModel = jList.getSelectionModel();
            scrollableItems = new JScrollPane(jList);
        }
      
        
        // configure "add" button
        JButton btnAddItem = new JButton("Add");
        btnAddItem.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt)  {
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
           }
        });

        // configure "delete" button
        final ListSelectionModel listSelectionModelFinal = listSelectionModel;
        JButton btnDeleteItem = new JButton("Delete");
        btnDeleteItem.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt)  {
               if(!listSelectionModelFinal.isSelectionEmpty()) {
                   for(int index = listSelectionModelFinal.getMaxSelectionIndex(); index >= listSelectionModelFinal.getMinSelectionIndex(); index--) {
                       if(listSelectionModelFinal.isSelectedIndex(index)) {
                           System.out.println("EditorFactory: removing selected index: " + index);
                           listModel.removeElementAt(index);
                       }
                   }
               }
           }
        });
        
        
        // configure panel layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        buttonPanel.add(btnAddItem, gbcButton);
        buttonPanel.add(btnDeleteItem, gbcButton);
        
        panel.setPreferredSize(new java.awt.Dimension(450, 180));        
        panel.setLayout(new BorderLayout());
        panel.add("Center", scrollableItems);
        panel.add("South", buttonPanel);
        
        return panel;
    }

    
    /** Private constants to layout item list */
    private final static GridBagConstraints gbcList = new GridBagConstraints(0,0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,0,2,0), 0,0);
    
    /** Private constants to layout buttons */
    private final static GridBagConstraints gbcButton = new GridBagConstraints(GridBagConstraints.RELATIVE,1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0,0,5,3), 0,0);
    
}


class ObjectPropertyTableModel implements javax.swing.table.TableModel, javax.swing.event.ListDataListener
{
    private Class itemClass;
    private DefaultListModel listModel;
    private ArrayList<Member> columnMembers;
    private ArrayList<org.doe4ejb3.annotation.PropertyDescriptor> columnPropertyDescriptors;
    private EventListenerList listenerList;
    
    public ObjectPropertyTableModel(Class itemClass, DefaultListModel listModel) throws Exception
    {
        this.itemClass = itemClass;
        this.listModel = listModel;
        this.listModel.addListDataListener(this);
        
        this.columnMembers = new ArrayList<Member>();
        this.columnPropertyDescriptors = new ArrayList<org.doe4ejb3.annotation.PropertyDescriptor>();
        this.listenerList = new EventListenerList();

        // TODO: order by index
        System.out.println("ObjectPropertyDescriptorTableModel: Scan columns...");
        for(Field field : itemClass.getFields()) {
            org.doe4ejb3.annotation.PropertyDescriptor pd = field.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            if( (pd != null) && (pd.showInLists()) ) {
                columnMembers.add(field);
                columnPropertyDescriptors.add(pd);
                System.out.println("ObjectPropertyDescriptorTableModel: found column: " + field.getName());
            }
        }

        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(itemClass);
        for(java.beans.PropertyDescriptor bpd : bi.getPropertyDescriptors()) {
            Method method = bpd.getReadMethod();
            if(method != null) {
                org.doe4ejb3.annotation.PropertyDescriptor pd = method.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
                if( (pd != null) && (pd.showInLists()) ) {
                    columnMembers.add(method);
                    columnPropertyDescriptors.add(pd);
                    System.out.println("ObjectPropertyDescriptorTableModel: found column: " + method.getName());
                }
            }
        }
        System.out.println("ObjectPropertyDescriptorTableModel: Scan done.");        

    }

    public int getRowCount() {
        int rows = listModel.getSize();
        System.out.println("ObjectPropertyDescriptorTableModel: RowCount =" + rows);
        return rows;
    }

    public int getColumnCount() {
        int cols = columnMembers.size();
        System.out.println("ObjectPropertyDescriptorTableModel: Num columns =" + cols);
        return cols;
    }

    public String getColumnName(int columnIndex) {
        String columnName = "";
        try {
            if( (columnIndex >= 0) && (columnIndex < columnPropertyDescriptors.size()) ) {
                org.doe4ejb3.annotation.PropertyDescriptor pd = columnPropertyDescriptors.get(columnIndex);
                if((pd != null) && (pd.displayName() != null) && (pd.displayName().length() > 0) ) {
                    columnName = pd.displayName();
                } else {
                    Member member = columnMembers.get(columnIndex);
                    if(member != null) {
                        columnName = member.getName();
                        if(columnName.startsWith("set")) columnName = columnName.substring(3);
                        else if(columnName.startsWith("get")) columnName = columnName.substring(3);
                        else if(columnName.startsWith("is")) columnName = columnName.substring(2);
                    }
                }
            }
        } catch(Exception ex) {
            columnName = "N/A";
        }
        return columnName;
    }

    public Class getColumnClass(int columnIndex) {
        if( (columnIndex >= 0) && (columnIndex < columnMembers.size()) ) {
            Member member = columnMembers.get(columnIndex);
            if(member != null) {
                if(member instanceof Field) return ((Field)member).getType();
                else if(member instanceof Method) return ((Method)member).getReturnType();
            }
        }
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = listModel.getElementAt(rowIndex);
        try {
            Object retval = obj.toString();
            if( (columnIndex >= 0) && (columnIndex < columnMembers.size()) ) {
                Member member = columnMembers.get(columnIndex);
                if(member != null) {
                    if(member instanceof Field) retval = ((Field)member).get(obj);
                    else if(member instanceof Method) retval = ((Method)member).invoke(obj);
                }
            }        
            System.out.println("Object " + rowIndex + ", property " + columnIndex + " = " + retval);
            return retval;
        } catch(Exception ex) {
            return "N/A";
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // not needed
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param   l               the TableModelListener
     */
    public void addTableModelListener(TableModelListener l) {
        System.out.println("EditorFactory: addTableModelListener.");
        listenerList.add(TableModelListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param   l               the TableModelListener
     */
    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    public void intervalAdded(ListDataEvent e) {
        fireChangeEvents(e);
    }

    public void intervalRemoved(ListDataEvent e) {
        System.out.println("EditorFactory: intervalRemoved: " + e);
        fireChangeEvents(e);
    }

    public void contentsChanged(ListDataEvent e) {
        fireChangeEvents(e);
    }
    
    private void fireChangeEvents(ListDataEvent e)
    {
        int tableModelEventType = TableModelEvent.UPDATE;
        switch(e.getType()) {
            case ListDataEvent.INTERVAL_ADDED: tableModelEventType = TableModelEvent.INSERT; break;
            case ListDataEvent.INTERVAL_REMOVED: tableModelEventType = TableModelEvent.DELETE; break;
            case ListDataEvent.CONTENTS_CHANGED: tableModelEventType = TableModelEvent.UPDATE; break;
        }
        
        TableModelEvent event = new TableModelEvent(this, e.getIndex0(), e.getIndex1(), javax.swing.event.TableModelEvent.ALL_COLUMNS, tableModelEventType);
        // Process the listeners last to first, notifying
        // those that are interested in this event
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TableModelListener.class) {
                System.out.println("EditorFactory: notification of tableChanged to: " + listeners[i+1]);
                ((TableModelListener)listeners[i+1]).tableChanged(event);
            }
        }
    }
}