/**
 * DefaultWindowManager.java
 * 
 * Created on Sep 14, 2007, 8:56:39 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.doe4ejb3.util.DOEUtils;


public class DefaultWindowManager implements WindowManager 
{
    private Frame mainWindow;
    private JDesktopPane mdiDesktopPane;
    private JLabel jLabelStatusBar;
    
    private HashMap<Object,JInternalFrame> openedInternalFrames;

    
    public DefaultWindowManager(Frame mainWindow, JDesktopPane mdiDesktopPane, JLabel jLabelStatusBar)
    {
        this.mainWindow = mainWindow;
        this.mdiDesktopPane = mdiDesktopPane;
        this.jLabelStatusBar = jLabelStatusBar;
        this.openedInternalFrames = new HashMap<Object,JInternalFrame>();
    }
    
    public Frame getMainWindow()
    {
        return mainWindow;
    }
    
    public Object createWindow(final Object key, String title, ImageIcon icon, Object contentPane)
    {
        JInternalFrame window = new JInternalFrame(title, true, true, true, false );

        openedInternalFrames.put(key, window);
        window.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent evt) {
                openedInternalFrames.remove(key);
            }
        });

        // if(icon != null) window.setFrameIcon(icon);  // avoid nimbus bug
        
        window.setLayout(new BorderLayout());            
        if(contentPane != null) {
            window.add((Component)contentPane, BorderLayout.CENTER);
        }
        
        return window;
    }
    
    public Object findWindow(Object key) 
    {
        return openedInternalFrames.get(key);
    }    

    public Object getActiveWindow() 
    {
        return mdiDesktopPane.getSelectedFrame();
    }

    public Object getWindowFromComponent(Object source) 
    {
        Component parent = (Component)source;
        while( (parent != null) && (!(parent instanceof JInternalFrame)) ) {
            parent = parent.getParent();
        }
        return parent;
    }        

    public void showWindow(Object window, boolean center) 
    {
        try { 
            JInternalFrame iFrame = (JInternalFrame)window;

            if(iFrame.getParent() == null) {
                //iFrame.setSize(iFrame.getPreferredSize());
                mdiDesktopPane.add(iFrame);
            }


            if(iFrame.isIcon()) iFrame.setIcon(false); 
            
            if(center) {
                iFrame.setLocation( Math.max(1,(mdiDesktopPane.getParent().getWidth()  - iFrame.getWidth())/2),
                                    Math.max(1,(mdiDesktopPane.getParent().getHeight() - iFrame.getHeight())/2) );
            } 

            iFrame.setVisible(true);
            iFrame.setSelected(true);

        } catch(Exception ex) {
            showMessageDialog("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void hideWindow(Object window) 
    {
        try { 
            JInternalFrame iFrame = (JInternalFrame)window;
            iFrame.setVisible(false);
        } catch(Exception ex) {
            showMessageDialog("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();                
        }
    }

    public void closeWindow(Object window) 
    {
        try { 
            JInternalFrame iFrame = (JInternalFrame)window;
            iFrame.setVisible(false);
            iFrame.dispose();
        } catch(Exception ex) {
            showMessageDialog("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();                
        }
    }

    public void bringToFront(Object window) 
    {
        try {
            JInternalFrame iFrame = (JInternalFrame)window;
            if(iFrame.isIcon()) iFrame.setIcon(false);                
            iFrame.setSelected(true);
        } catch(Exception ex) {
            showMessageDialog("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void showMessageDialog(String msg, String title, int dialogType) 
    {
        JOptionPane.showInternalMessageDialog(mdiDesktopPane, msg, title, dialogType);
    }

    public int showConfirmDialog(String msg, String title, int buttonOptions) 
    {
        return JOptionPane.showInternalConfirmDialog(mdiDesktopPane, msg, title, buttonOptions);
    }

    public Object showInputDialog(String msg, String title, Object[] selectionValues, Object initialSelectionValue) {
        return JOptionPane.showInternalInputDialog(mdiDesktopPane, msg, title, JOptionPane.QUESTION_MESSAGE, (javax.swing.Icon)null, selectionValues, initialSelectionValue);
    }


    public EventListenerList getEventListenerList(Object window) 
    {
        JInternalFrame iFrame = (JInternalFrame)window;
        EventListenerList eventListenerList = (EventListenerList)iFrame.getClientProperty("entityListeners");
        if(eventListenerList == null) {
            eventListenerList = new EventListenerList();
            iFrame.putClientProperty("entityListeners", eventListenerList);
        }
        return eventListenerList;
    }
    
    public void setWindowTitle(Object window, String title)
    {
        JInternalFrame iFrame = (JInternalFrame)window;
        if(title == null) title = "";
        iFrame.setTitle(title);
    }
    
    public void showStatus(Object window, String msg)
    {
        if(msg == null) msg = "";
        jLabelStatusBar.setText(" " + msg);
    }

}
