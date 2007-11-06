/*
 * DOEUtils.java
 * 
 * Created on Sep 16, 2007, 1:19:36 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.util;

import org.jdesktop.application.Application;
import org.jdesktop.application.TaskMonitor;

import org.doe4ejb3.gui.WindowManager;
import org.doe4ejb3.gui.EntityClassListCellRenderer;
import org.doe4ejb3.gui.EntityManagerPane;
import org.doe4ejb3.gui.EntityEditorView;


public class DOEUtils
{
    public static final Object APPLICATION_WINDOW = null;


    private static WindowManager windowManager = null;
    private static TaskMonitor   taskMonitor = null;


    public static void setWindowManager(WindowManager wm)
    {
        windowManager = wm;
        if(taskMonitor == null) taskMonitor = getTaskMonitor();
    }
    
    public static WindowManager getWindowManager()
    {
        if(windowManager == null) throw new RuntimeException("Window Manager not configured");
        return windowManager;
    }    
    
    public static TaskMonitor getTaskMonitor()
    {
        if(taskMonitor == null) {
            taskMonitor = new org.jdesktop.application.TaskMonitor(Application.getInstance().getContext());        
            taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    taskMonitorPropertyChange(evt);
                }
            });
        }
        return taskMonitor;
    }
    
    private static void taskMonitorPropertyChange(java.beans.PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("message".equals(propertyName)) {
            String text = (String)(evt.getNewValue());
            if(windowManager != null) {
                windowManager.showStatus(DOEUtils.APPLICATION_WINDOW, (text == null) ? "" : text);
            }
        } 
        else if ("started".equals(propertyName)) {
            // setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        else if ("done".equals(propertyName)) {
            // setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        else if ("progress".equals(propertyName)) {
            // int value = (Integer)(evt.getNewValue());
            // jProgressBar.setVisible(true);
            // jProgressBar.setIndeterminate(false);
            // jProgressBar.setValue(value);
        }
    }  
    
    
    public static void openEntityManager(String puName, Class entityClass) throws Exception
    {
        Object key = entityClass.getName() + "Manager";
        Object window = windowManager.findWindow(key);
        if(window != null) {
            windowManager.bringToFront(window);
        } else {
            EntityManagerPane manager = new EntityManagerPane(puName, entityClass);
            String title = I18n.getLiteral("entityManager.title", I18n.getEntityName(entityClass));
            window = windowManager.createWindow(key, title, EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass), manager);
            
            windowManager.showWindow(window, false);
        }

    }


    public static Object openEntityEditor(String puName, Class entityClass, Object entity) throws Exception
    {
        Object key = (entity != null) ? entity : entityClass.getName() + "Editor";
        Object window = windowManager.findWindow(key);

        if(window != null) {
            windowManager.bringToFront(window);
        } else {
            EntityEditorView editorViewer = new EntityEditorView(puName, entityClass, entity);
            String title = org.doe4ejb3.util.I18n.getEntityName(entityClass);
            if(entity == null) title = I18n.getLiteral("entityCreator.title", title.toLowerCase());
            else title = I18n.getLiteral("entityEditor.title", title) + ": " + entity.toString();
            
            window = windowManager.createWindow(key, title, EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass), editorViewer);

            windowManager.showWindow(window, false);
        }

        return window;
    }
    
}
