/*
 * DoeWindowManagerForNetbeans.java
 * 
 * Created on Sep 16, 2007, 12:37:16 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.netbeans.navigator;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

import org.openide.awt.StatusDisplayer;
import org.openide.loaders.DataObject;
import org.openide.windows.TopComponent;


public class DoeWindowManagerForNetbeans implements org.doe4ejb3.gui.WindowManager
{
    private Frame mainWindow;
    private HashMap<Object,TopComponent> openedInternalFrames;
    
    public DoeWindowManagerForNetbeans(Frame mainWindow) {
        this.mainWindow = mainWindow;
        this.openedInternalFrames = new HashMap<Object,TopComponent>();
    }

    public Frame getMainWindow()
    {
	return mainWindow;
    }
    
    public Object createWindow(final Object key, String title, ImageIcon icon, Object contentPane)
    {
        boolean withSaveCookie = false;
        boolean withPrintCookie = false;
        JComponent component = null;
        try {
            component = (JComponent)contentPane;
            if(component != null) {
                withSaveCookie = component.getActionMap().get("save") != null;
                withPrintCookie = component.getActionMap().get("print") != null;
            }
        } catch(Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        TopComponent window = new DoeTopComponent(withSaveCookie, withPrintCookie) {
            @Override
            public void componentClosed() {
                openedInternalFrames.remove(key);
            }
        };
        
        openedInternalFrames.put(key, window);

        window.setName(title);
        if(icon != null) window.setIcon(icon.getImage());

	window.setLayout(new BorderLayout());            
        if(component != null) {
            window.add(component, BorderLayout.CENTER);
            window.getActionMap().put("print", component.getActionMap().get("print"));
            window.getActionMap().put("save", component.getActionMap().get("save"));
        }
        
        return window;
    }
    
    public Object findWindow(Object key) {
        return openedInternalFrames.get(key);
    }

    public Object getActiveWindow() {
        return this;  // FIXME
    }

    public Object getWindowFromComponent(Object window) {
        java.awt.Component parent = (java.awt.Component)window;
        while( (parent != null) && (!(parent instanceof TopComponent)) ) {
            parent = parent.getParent();
        }
        return (TopComponent)parent;
    }

    public void showWindow(Object window, boolean center) {
        TopComponent top = (TopComponent)window;
        top.open();
        top.requestActive();
    }

    public void hideWindow(Object window) {
        TopComponent top = (TopComponent)window;
        top.setVisible(false);
    }

    public void closeWindow(Object window) {
        TopComponent top = (TopComponent)window;
        top.close();
    }

    public void bringToFront(Object window) {
        TopComponent top = (TopComponent)window;
        top.requestActive();
    }

    public void showMessageDialog(String msg, String title, int dialogType) {
        JOptionPane.showMessageDialog(mainWindow, msg,title, dialogType);
    }

    public int showConfirmDialog(String msg, String title, int buttonOptions) {
        return JOptionPane.showConfirmDialog(mainWindow, msg,title, buttonOptions);
    }

    public Object showInputDialog(String msg, String title, Object[] selectionValues, Object initialSelectionValue) {
        return JOptionPane.showInputDialog(mainWindow, msg, title, JOptionPane.QUESTION_MESSAGE, (javax.swing.Icon)null, selectionValues, initialSelectionValue);
    }


    public EventListenerList getEventListenerList(Object window)
    {
        javax.swing.JComponent component = (javax.swing.JComponent)window;
        EventListenerList eventListenerList = (EventListenerList)component.getClientProperty("entityListeners");
        if(eventListenerList == null) {
            eventListenerList = new EventListenerList();
            component.putClientProperty("entityListeners", eventListenerList);
        }
        return eventListenerList;
    }
    
    public void setWindowTitle(Object window, String title)
    {
        TopComponent top = (TopComponent)window;        
        if(title == null) title = "";
        top.setName(title);
    }
    
    public void showStatus(Object window, String msg)
    {
        StatusDisplayer.getDefault().setStatusText(msg);
    }


}


class DoeTopComponent extends TopComponent
{
            public DoeTopComponent(boolean withSaveCookie, boolean withPrintCookie) {
                associateLookup(new DoeNode(withSaveCookie, withPrintCookie).getLookup());
            }
            
            public int getPersistenceType() {
                return TopComponent.PERSISTENCE_NEVER;
            }
            
            public java.awt.print.Printable getPrintableFromWindow()
            {
                return (java.awt.print.Printable)this.getClientProperty("printableContent");
            }
            
            class DoeNode extends org.openide.nodes.AbstractNode {
                 public DoeNode(boolean withSaveCookie, boolean withPrintCookie) {
                    super(org.openide.nodes.Children.LEAF);
                    org.openide.nodes.CookieSet cookies = getCookieSet();
                    if(withPrintCookie) cookies.add(new MyPrintCookie());
                    if(withSaveCookie) {
                        cookies.add(new MySaveCookie());
                    }
                 }
            }
            
            
            class MySaveCookie implements org.openide.cookies.SaveCookie {

                public void save() throws IOException {
                    try {
                        javax.swing.Action action = DoeTopComponent.this.getActionMap().get("save");
                        if(action != null) {
                            setCursor( java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                            action.actionPerformed(new ActionEvent(this, 0, "save"));
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        System.err.println("Saving error: "+ex.toString());
                    } finally {
                        setCursor( java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                }
                
            }
            
            class MyPrintCookie implements org.openide.cookies.PrintCookie {
                
                public void print() {
                    try {
                        javax.swing.Action action = DoeTopComponent.this.getActionMap().get("print");
                        if(action != null) {
                            setCursor( java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                            action.actionPerformed(null);
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        System.err.println("Printing error: "+ex.toString());
                    } finally {
                        setCursor( java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                }
            }
}



