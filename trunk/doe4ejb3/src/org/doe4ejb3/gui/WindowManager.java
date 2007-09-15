/*
 * WindowManager.java
 * 
 * Created on Sep 14, 2007, 6:24:46 PM
 * @author Jordi Mariné Fort
 */

package org.doe4ejb3.gui;

import javax.swing.event.EventListenerList;


public interface WindowManager 
{
    Object findWindow(Object key);
    Object getActiveWindow();
    Object getWindowFromComponent(Object source);

    void   showWindow(Object window, boolean center);
    void   hideWindow(Object window);
    void   closeWindow(Object window);
    void   bringToFront(Object window);
    
    void   showMessageDialog(String msg, String title, int dialogType);
    int    showConfirmDialog(String msg, String title, int buttonOptions);

    EventListenerList getEventListenerList(Object window);
    
    Object openInternalFrameEntityEditor(String puName, Class entityClass, Object entity) throws Exception;
    void   openInternalFrameEntityManager(String puName, Class entityClass) throws Exception;
    
    void showStatus(String msg);
}
