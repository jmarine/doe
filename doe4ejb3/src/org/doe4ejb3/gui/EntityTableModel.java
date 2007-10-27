/*
 * EntityTableModel.java
 *
 * Created on 15 October 2006, 17:32
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


public class EntityTableModel implements javax.swing.table.TableModel, javax.swing.event.ListDataListener
{
    private Class itemClass;
    private DefaultListModel listModel;
    private ArrayList<Member> columnMembers;
    private EventListenerList listenerList;
    private java.beans.PropertyChangeSupport changeSupport;
    
    public EntityTableModel(Class itemClass, DefaultListModel listModel) throws Exception
    {
        this.itemClass = itemClass;
        this.listModel = listModel;
        this.listModel.addListDataListener(this);
        
        this.columnMembers = new ArrayList<Member>();
        this.listenerList = new EventListenerList();
        this.changeSupport = null;

        System.out.println("EntityTableModel: Search fields to showInLists...");
        for(Field field : itemClass.getFields()) {
            org.doe4ejb3.annotation.PropertyDescriptor pd = field.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            if( (pd != null) && (pd.showInLists()) ) {
                columnMembers.add(field);
                System.out.println("EntityTableModel: found column: " + field.getName());
            }
        }

        System.out.println("EntityTableModel: Search properties to showInLists...");
        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(itemClass);
        for(java.beans.PropertyDescriptor bpd : bi.getPropertyDescriptors()) {
            Method method = bpd.getReadMethod();
            if(method != null) {
                org.doe4ejb3.annotation.PropertyDescriptor pd = method.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
                if( (pd != null) && (pd.showInLists()) ) {
                    columnMembers.add(method);
                    System.out.println("EntityTableModel: found column: " + method.getName());
                }
            }
        }
        System.out.println("EntityTableModel: Scan done.");        
        
        System.out.println("EntityTableModel: Ordering columns.");        
        MemberOrderComparator orderComparator = new MemberOrderComparator();
        java.util.Collections.sort(columnMembers, orderComparator);
        System.out.println("EntityTableModel: Ordering done.");        
    }
    
    public DefaultListModel getListModel()
    {
        return listModel;
    }

    public int getRowCount() {
        int rows = listModel.getSize();
        System.out.println("EntityTableModel: RowCount =" + rows);
        return rows;
    }

    public int getColumnCount() {
        int cols = Math.max(1,columnMembers.size());
        System.out.println("EntityTableModel: Num columns =" + cols);
        return cols;
    }

    public String getColumnName(int columnIndex) {
        String columnName = "";
        try {
            if( (columnIndex >= 0) && (columnIndex < columnMembers.size()) ) {
                Member member = columnMembers.get(columnIndex);
                org.doe4ejb3.annotation.PropertyDescriptor pd = null;
                if(member instanceof Field) {
                    Field field = (Field)member;
                    pd = field.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
                } else if(member instanceof Method) {
                    Method method = (Method)member;
                    pd = method.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
                }            
                
                if((pd != null) && (pd.displayName() != null) && (pd.displayName().length() > 0) ) {
                    columnName = pd.displayName();
                } else {
                    if(member != null) {
                        columnName = member.getName();
                        if(columnName.startsWith("set")) columnName = columnName.substring(3);
                        else if(columnName.startsWith("get")) columnName = columnName.substring(3);
                        else if(columnName.startsWith("is")) columnName = columnName.substring(2);
                    }
                }
            } else {
                return I18n.getLiteral("msg.sort");
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
    
    
    public Object[] getValues()
    {
        return listModel.toArray();
    }


    public void setValues(Object values)
    {
        listModel.clear();
        if(values != null) {
            if(values instanceof java.util.Collection) {
                values = ((java.util.Collection)values).toArray();
            }
            
            // listModel.copyInto((Object[])values);
            for(Object o : (Object[])values) {
                listModel.addElement(o);
            }
        }
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

    // this method is not really used in the application
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, rowIndex,rowIndex);
        fireChangeEvents(e);
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
        // Change notification for JSR 295 bindings:
        firePropertyChange("values", null, getValues());

        // Change notification for JTable listeners:
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
    
    public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) 
    {
        if (listener == null) {
            return;
        }
        if (changeSupport == null) {
            changeSupport = new java.beans.PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);        
    }

    public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener)
    {
        if (listener != null && changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }
    
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) 
    {
        if(changeSupport != null) changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
}