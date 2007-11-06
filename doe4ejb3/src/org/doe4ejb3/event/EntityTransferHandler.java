/*
 * EntityTransferHandler.java is used by the 1.4
 * based on DragPictureDemo.java example.
 */
package org.doe4ejb3.event;

import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;

import org.doe4ejb3.gui.EntityTableModel;
import org.doe4ejb3.util.I18n;


public class EntityTransferHandler extends TransferHandler 
{
    JComponent sourceControl;
    //Object items[];
    Class entityClass;
    DataFlavor entityFlavor;
    boolean shouldRemove;
    boolean isValidTarget;
    

    public EntityTransferHandler(Class entityClass, boolean isValidTarget) throws ClassNotFoundException
    {
        this.entityClass = entityClass;
        this.entityFlavor = new DataFlavor(getLocalMimeType(entityClass));
        this.isValidTarget = isValidTarget;
    }
    
    private static String getLocalMimeType(Class entityClass)
    {
        return DataFlavor.javaJVMLocalObjectMimeType + ";class=" + entityClass.getName();
    }

    public Class getEntityClass()
    {
        return entityClass;
    }
        
    public DataFlavor getEntityDataFlavor()
    {
        return entityFlavor;
    }

    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            
            // JScrollPane allows to drop content into an empty JTable
            if(c instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane)c;
                c = (JComponent) scroll.getViewport().getView();
            }
            
            
            //Don't drop on myself.
            if (sourceControl == c) {
                shouldRemove = false;
                return true;
            }
            try {
                Object items[] = null;
                Object item = t.getTransferData(entityFlavor);
                if(item.getClass().isArray()) {
                    items = (Object[])item;
                } else {
                    items = new Object[1];
                    items[0] = item;
                }
                
                //Set the component to the new entity.
                if(items != null) {
                    if(c instanceof JComboBox) {
                        JComboBox combo = (JComboBox)c;
                        for(int index = 0; (items != null) && (index < items.length); index++) {
                            combo.addItem(items[index]);
                            if(index == 0) combo.setSelectedItem(items[index]);
                        }
                    } else if(c instanceof JTable) {
                        JTable table = (JTable)c;
                        EntityTableModel model = (EntityTableModel)table.getModel();
                        for(int index = 0; (items != null) && (index < items.length); index++) {
                            if(!model.getListModel().contains(items[index])) {
                                model.getListModel().addElement(items[index]);
                            }
                        }
                    }
                    return true;
                }
                return false;
            } catch (UnsupportedFlavorException ufe) {
                System.out.println("importData: unsupported data flavor");
            } catch (IOException ioe) {
                System.out.println("importData: I/O exception");
            }
        }
        return false;
    }

    protected Transferable createTransferable(JComponent c) {
        sourceControl = c;
        shouldRemove = isValidTarget;
        return new EntityTransferable(sourceControl);
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
        if(shouldRemove && (action == MOVE) 
                && (data != null) && (data.getTransferDataFlavors() != null) && (data.getTransferDataFlavors().length > 0) ) {
            Object items[] = null;
            try {
                items = (Object[])data.getTransferData(getEntityDataFlavor());
                if(sourceControl instanceof JComboBox) {
                    JComboBox combo = (JComboBox)sourceControl;
                    for(int index = 0; (items != null) && (index < items.length); index++) {
                        combo.removeItem(items[index]);
                    }
                } else if(sourceControl instanceof JTable) {
                    JTable table = (JTable)sourceControl;
                    EntityTableModel model = (EntityTableModel)table.getModel();
                    for(int index = 0; (items != null) && (index < items.length); index++) {
                        int position = model.getListModel().indexOf(items[index]);
                        if(position != -1) {
                             model.getListModel().removeElementAt(position);
                        }
                    }
                }
            } catch(Exception ex) {
                System.out.println(I18n.getLiteral("msg.error") + ex.getMessage());
                System.err.println(I18n.getLiteral("msg.error") + ex.getMessage());
                ex.printStackTrace();
            }
        }
        sourceControl = null;
        //items = null;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if(!isValidTarget) return false;
        for (int i = 0; i < flavors.length; i++) {
            if (isDataFlavorSupported(flavors[i])) {
                return true;
            } else {
                System.out.println("EntityTransferHandler: unsupported flavor: "  + flavors[i].getMimeType());
            }
        }
        return false;
    }
    
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return entityFlavor.match(flavor);
    }
    

    class EntityTransferable implements Transferable {
        // TODO: define supported DataFlavor
        private Object items[];

        EntityTransferable(JComponent component) {
            try { 
                JTable table = (JTable)component;
                EntityTableModel model = (EntityTableModel)table.getModel();

                items = null;
                int selection[] = table.getSelectedRows();
                if(selection != null) {
                    items = (Object[])Array.newInstance(EntityTransferHandler.this.getEntityClass(), selection.length);
                    for(int index = 0; index < items.length; index++) {
                        items[index] = model.getListModel().getElementAt(table.convertRowIndexToModel(selection[index]));
                    }
                }
            
            } 
            catch(Exception ex) { }
        }

        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return items;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { EntityTransferHandler.this.getEntityDataFlavor() };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return EntityTransferHandler.this.isDataFlavorSupported(flavor);
            // flavor.getMimeType().startsWith(DataFlavor.javaJVMLocalObjectMimeType);
        }

    }
}
