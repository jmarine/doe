/*
 * ObjectPropertyTableModel.java
 *
 * Created on 15 October 2006, 17:32
 * @author jordi
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

import org.doe4ejb3.annotation.PropertyDescriptor;


public class ObjectPropertyTableModel implements javax.swing.table.TableModel, javax.swing.event.ListDataListener
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
    
    public DefaultListModel getListModel()
    {
        return listModel;
    }

    public int getRowCount() {
        int rows = listModel.getSize();
        System.out.println("ObjectPropertyDescriptorTableModel: RowCount =" + rows);
        return rows;
    }

    public int getColumnCount() {
        int cols = Math.max(1,columnMembers.size());
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