/**
 * EntityEditorFrame.java
 *
 * Created on July 5, 2007, 1:02 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.print.*;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.text.*;

import javax.swing.*;
import javax.swing.event.EventListenerList;

import org.doe4ejb3.event.*;
import org.doe4ejb3.util.*;


public class EntityEditorView extends javax.swing.JPanel 
{
    private String  puName = null;
    private Object  entity = null;
    private Class   entityClass = null;
    private EntityEditorInterface editor = null;
    private int defaultActionsCount = 0;
    
    /** Creates new form EntityEditorFrame */
    public EntityEditorView(String puName, Class entityClass, Object entity) throws Exception {
        
        initComponents();
        
        this.puName = puName;
        this.entityClass = entityClass;
        this.defaultActionsCount = jButtonsPanel.getComponentCount();
        
        DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, "");
        
        System.out.println("Creating internal frame");
        
        System.out.println("Preparing editor ");
        editor = EditorFactory.getEntityEditor(null, puName, entityClass, "");
        setEntity(entity);
        
        JScrollPane scrollPaneForEditor = new JAutoScrollPaneOnComponentFocus(editor.getJComponent(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPaneForEditor, BorderLayout.CENTER);

        // Define public actions (for Netbeans integration)
        this.getActionMap().put("printAction", org.jdesktop.application.Application.getInstance().getContext().getActionMap(EntityEditorView.class, this).get("print"));
    }
    
    private void setEntity(Object entity) throws Exception 
    {
        this.entity = entity;
        String title = org.doe4ejb3.gui.I18n.getEntityName(entityClass);
        if(entity == null) title = org.doe4ejb3.gui.I18n.getLiteral("New") + " " + title.toLowerCase();
        else title = org.doe4ejb3.gui.I18n.getLiteral("Edit") + " " + title + ": " + entity.toString();

        if(entity == null) {
            editor.newEntity(entityClass);
        } else {
            editor.setEntity(entity);
        }

        updateActions();
        revalidate();    
        repaint();
    }
    
    private Object getEntity() throws Exception
    {
        try {
            return editor.getEntity();
        } catch(Exception error) {
            
            for(Exception cause = error; (cause != null); cause = (Exception)cause.getCause()) {
                if( (cause != null) && (cause.getCause() != null) && (cause.getCause() instanceof InvocationTargetException) ) {
                    InvocationTargetException invocationException = (InvocationTargetException)cause.getCause();
                    cause = (Exception)invocationException.getTargetException();
                }
                if( (cause != null) && (cause instanceof PropertyVetoException) ) {
                    PropertyVetoException veto = (PropertyVetoException)cause;
                    ValidationErrorPopup.showErrorPopup((javax.swing.JComponent)veto.getPropertyChangeEvent().getSource(), veto.getMessage());
                    throw cause;
                }
            }
            throw error;
        }
    }    
    
    private void updateActions()
    {
        clearCustomActions();
        if(entity == null) {
            jButtonDelete.setVisible(false);
            jButtonPrint.setVisible(false);
        } else {
            jButtonDelete.setVisible(true);
            jButtonPrint.setVisible(true);
            ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(entityClass, entity);
            if( (actionMap != null) && (actionMap.keys() != null) ) {
                for(Object action : actionMap.keys()) {
                    System.out.println("Action found: " + action);
                    JButton btnAction = new JButton(actionMap.get(action));
                    jButtonsPanel.add(btnAction, defaultActionsCount-1);  // before "Close" button
                }
            }
        }
    }
    
    private void clearCustomActions()
    {
        while(jButtonsPanel.getComponentCount() > defaultActionsCount) 
        {
            jButtonsPanel.remove(defaultActionsCount-1);  // before "Close" button
        }
    }
    

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jButtonsPanel = new JPanel() {
            public Insets getInsets() {
                return new Insets(0,7,7,7);
            }
        };
        jButtonAccept = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonPrint = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(jScrollPane, java.awt.BorderLayout.CENTER);

        jButtonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(EntityEditorView.class, this);
        jButtonAccept.setAction(actionMap.get("accept")); // NOI18N
        jButtonsPanel.add(jButtonAccept);

        jButtonSave.setAction(actionMap.get("save")); // NOI18N
        jButtonsPanel.add(jButtonSave);

        jButtonDelete.setAction(actionMap.get("delete")); // NOI18N
        jButtonDelete.setMnemonic('d');
        jButtonsPanel.add(jButtonDelete);

        jButtonPrint.setAction(actionMap.get("print")); // NOI18N
        jButtonPrint.setMnemonic('p');
        jButtonsPanel.add(jButtonPrint);

        jButtonClose.setAction(actionMap.get("close")); // NOI18N
        jButtonClose.setMnemonic('c');
        jButtonsPanel.add(jButtonClose);

        add(jButtonsPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    
    @org.jdesktop.application.Action
    public org.jdesktop.application.Task accept() 
    {
        return new SaveTask(Application.getInstance(), true);
    }

    
    @org.jdesktop.application.Action
    public org.jdesktop.application.Task save() 
    {
        return new SaveTask(Application.getInstance(), false);
    }
    

    @org.jdesktop.application.Action
    public org.jdesktop.application.Task delete() 
    {
        int confirm = DOEUtils.getWindowManager().showConfirmDialog( "Do you really want to delete this object?", "Confirm operation", JOptionPane.OK_CANCEL_OPTION);
        if(confirm != JOptionPane.OK_OPTION) {                
            return null;
        } else {
            return new DeleteTask(Application.getInstance());
        }
    }
    

    @org.jdesktop.application.Action
    public void print() 
    {
        // Don't run in background (UI refresh problems).
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if(editor instanceof Printable) {
            printJob.setPrintable((Printable)editor);
        } else if(editor.getJComponent() instanceof Printable) {
            printJob.setPrintable((Printable)editor.getJComponent());
        } else {
            printJob.setPrintable(PrintUtils.createPrintableComponent(editor.getJComponent()));
        }
        
        if(printJob.printDialog()) {        
            try {
                DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, MessageFormat.format("Printing {0}.", JPAUtils.getEntityName(entityClass)));
                printJob.print();
                DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, "Printing job has been submited");
            } catch(PrinterException pe) {
                DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, "Printing error: " + pe.getMessage());
                System.out.println("Printing error: " + pe.getMessage());
                pe.printStackTrace();
            }                    
        }
    }
    

    @org.jdesktop.application.Action
    public void close() {
        WindowManager wm = DOEUtils.getWindowManager();
        Object window = wm.getWindowFromComponent(this);
        wm.closeWindow(window);
        
        // FIXME: pending to move this code to its window close event handler:
        if( (!editor.isNew()) && (window != null) && (window instanceof JComponent) ) {
            Object dirtyEntity = ((JComponent)window).getClientProperty("dirtyEntity");
            if(dirtyEntity != null) {
                DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, "WARNING: Entity has been modified for validations, but there was a problem while restoring original value, so it should be refreshed from database.");  // rollback problem
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonPrint;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JPanel jButtonsPanel;
    private javax.swing.JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables

    
    class SaveTask extends org.jdesktop.application.Task<Object, Void>
    {
        private boolean close = false;
        
        SaveTask(org.jdesktop.application.Application app, boolean close)
        {
            super(app);
            this.close = close;
        }
        
        @Override 
        protected Object doInBackground() throws java.lang.Exception
        {
            EntityEditorView.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            setMessage(MessageFormat.format("Saving {0}.", JPAUtils.getEntityName(entityClass)));                    
            Object oldEntity = getEntity();
            Object newEntity = oldEntity;
            if(editor.isNew()) {
                newEntity = JPAUtils.createEntity(puName, oldEntity);  // safer than "saveEntity" method.
            } else {
                newEntity = JPAUtils.saveEntity(puName, oldEntity);  
            }
                
            try {
                EntityEvent entityEvent = new EntityEvent(this, editor.isNew()? EntityEvent.ENTITY_INSERT : EntityEvent.ENTITY_UPDATE, oldEntity, newEntity);
                WindowManager wm = DOEUtils.getWindowManager();
                Object window = wm.getWindowFromComponent(EntityEditorView.this);
                EventListenerList listenerList = DOEUtils.getWindowManager().getEventListenerList(window);
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                    if (listeners[i]==EntityListener.class) {
                        System.out.println("EditorFactory: notification of tableChanged to: " + listeners[i+1]);
                        ((EntityListener)listeners[i+1]).entityChanged(entityEvent);
                    }
                }                
            } catch(Exception ex) {
                setMessage(MessageFormat.format("Saving notification error for {0}: {1}", JPAUtils.getEntityName(entityClass), ex.getMessage()));
                ex.printStackTrace();
            } 
            
            return newEntity;
        }
        

        
        @Override
        protected void cancelled() 
        {
            setMessage("Not saved.");
        }        

        @Override
        protected void succeeded(Object newEntity) 
        {
            setMessage(MessageFormat.format("{0} saved.", JPAUtils.getEntityName(entityClass)));

            if(close) {
                close();
            } else {
                try {
                    // FIXME: DOE's window manager allows to open created entities in other windows
                    EntityEditorView.this.setEntity(newEntity);
                } catch(Exception ex) {
                    setMessage("Error loading saved entity: " + ex.getMessage());
                }
            }

        }

        @Override
        protected void interrupted(InterruptedException ex) 
        {
            setMessage("Not saved: " + ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        protected void failed(Throwable error) 
        {
            setMessage("Error: " + error.getMessage());
            error.printStackTrace();
        }
        
        @Override
        protected void finished()
        {
            EntityEditorView.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));            
        }

    }
    
    class DeleteTask extends org.jdesktop.application.Task<Void, Void>
    {
        DeleteTask(org.jdesktop.application.Application app)
        {
            super(app);
        }

        @Override 
        protected Void doInBackground() throws java.lang.Exception
        {        
            EntityEditorView.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setMessage(MessageFormat.format("Deleting {0}.", JPAUtils.getEntityName(entityClass)));                    
            JPAUtils.removeEntity(puName, entity);
            setMessage(MessageFormat.format("{0} removed.", JPAUtils.getEntityName(entityClass)));

            try {
                EntityEvent entityEvent = new EntityEvent(this, EntityEvent.ENTITY_DELETE, entity, null);
                WindowManager wm = DOEUtils.getWindowManager();
                Object window = wm.getWindowFromComponent(EntityEditorView.this);
                EventListenerList listenerList = DOEUtils.getWindowManager().getEventListenerList(window);
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length-2; i>=0; i-=2) {
                    if (listeners[i]==EntityListener.class) {
                        System.out.println("EditorFactory: notification of tableChanged to: " + listeners[i+1]);
                        ((EntityListener)listeners[i+1]).entityChanged(entityEvent);
                    }
                }
            } catch(Exception ex) {
                setMessage(MessageFormat.format("Delete notification error for {0}: {1}", JPAUtils.getEntityName(entityClass), ex.getMessage()));
                ex.printStackTrace();
            }

            return null;
        }
        
        @Override
        protected void cancelled() 
        {
            setMessage("Not saved.");
        }        

        @Override
        protected void succeeded(Void result) 
        {
            close();
        }

        @Override
        protected void interrupted(InterruptedException ex) 
        {
            setMessage("Not deleted: " + ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        protected void failed(Throwable cause) 
        {
            setMessage("Error: " + cause.getClass().getName() + ":" + getMessage());
            cause.printStackTrace();
        }
                
        
        @Override
        protected void finished()
        {
            EntityEditorView.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));            
        }
        
    }
    
}
