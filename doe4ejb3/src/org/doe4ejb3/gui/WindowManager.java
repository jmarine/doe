/*
 * MDIWindowManager.java
 * 
 * Created on Sep 14, 2007, 6:24:46 PM
 * @author Jordi Mariné Fort
 */

package org.doe4ejb3.gui;

import javax.swing.JComponent;
import javax.swing.event.EventListenerList;


public interface WindowManager 
{
    JComponent getActiveWindow();
    JComponent getWindowFromComponent(JComponent source);
    
    void   showWindow(JComponent window, boolean center);
    void   hideWindow(JComponent window);
    void   closeWindow(JComponent window);
    void   bringToFront(JComponent window);
    
    void   showMessageDialog(String msg, String title, int dialogType);
    int    showConfirmDialog(String msg, String title, int buttonOptions);

    EventListenerList getEventListenerList(Object window);
    
    JComponent openInternalFrameEntityEditor(String puName, Class entityClass, Object entity) throws Exception;
    void openInternalFrameEntityManager(String puName, Class entityClass) throws Exception;
}
