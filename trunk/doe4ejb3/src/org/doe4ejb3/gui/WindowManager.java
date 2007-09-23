/*
 * WindowManager.java
 * 
 * Created on Sep 14, 2007, 6:24:46 PM
 * @author Jordi Mariné Fort
 */

package org.doe4ejb3.gui;

import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;


public interface WindowManager 
{
    Object createWindow(final Object key, String title, ImageIcon icon, Object contentPane);
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
    
    void setWindowTitle(Object window, String title);
    void showStatus(Object window, String msg);
}
