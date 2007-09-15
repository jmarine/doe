/**
 * DefaultWindowManager.java
 * 
 * Created on Sep 14, 2007, 8:56:39 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;


public class DefaultWindowManager implements WindowManager 
{
    private MDIDesktopPane mdiDesktopPane;
    private HashMap<Object,JInternalFrame> openedInternalFrames;

    public Object findWindow(Object key) 
    {
        return openedInternalFrames.get(key);
    }
    
    public DefaultWindowManager(MDIDesktopPane mdiDesktopPane)
    {
        this.mdiDesktopPane = mdiDesktopPane;
        this.openedInternalFrames = new HashMap<Object,JInternalFrame>();
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
                mdiDesktopPane.add(iFrame, center);
            }

            if(iFrame.isIcon()) iFrame.setIcon(false);                
            if(center) {
                iFrame.setLocation( Math.max(1,(mdiDesktopPane.getParent().getWidth()  - iFrame.getWidth())/2),
                                    Math.max(1,(mdiDesktopPane.getParent().getHeight() - iFrame.getHeight())/2) );
            } 

            iFrame.setSelected(true);        
            iFrame.setVisible(true);

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

    public void openInternalFrameEntityManager(String puName, Class entityClass) throws Exception
    {
        final Object key = entityClass.getName() + "Manager";
        JInternalFrame iFrame = (JInternalFrame)findWindow(key);
        if(iFrame != null) {
            bringToFront(iFrame);
        } else {
            String title = org.doe4ejb3.gui.I18n.getEntityName(entityClass) + " manager";
            iFrame = new JInternalFrame(title, true, true, true, false );
            iFrame.setFrameIcon(EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass));
            iFrame.getContentPane().setLayout(new BorderLayout());
            iFrame.getContentPane().add(new EntityManagerPane(puName, entityClass), BorderLayout.CENTER);

            iFrame.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent evt) {
                    JInternalFrame iFrame = evt.getInternalFrame();
                    iFrame.putClientProperty("acceptListeners", null);
                    openedInternalFrames.remove(key);
                }
              });


            openedInternalFrames.put(key, iFrame);
            showWindow(iFrame, false);
        }

    }


    public Object openInternalFrameEntityEditor(String puName, Class entityClass, Object entity) throws Exception
    {
        final Object key = (entity != null) ? entity : entityClass.getName() + "Editor";
        JInternalFrame iFrame = (JInternalFrame)findWindow(key);

        if(iFrame != null) {
            bringToFront(iFrame);
        } else {
            
            String title = org.doe4ejb3.gui.I18n.getEntityName(entityClass);
            if(entity == null) title = org.doe4ejb3.gui.I18n.getLiteral("New") + " " + title.toLowerCase();
            else title = org.doe4ejb3.gui.I18n.getLiteral("Edit") + " " + title + ": " + entity.toString();
            
            iFrame = new JInternalFrame(title, true, true, true, false ); 
            iFrame.setFrameIcon(EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass));
            iFrame.getContentPane().setLayout(new BorderLayout());            
            iFrame.getContentPane().add(new EntityEditorView(puName, entityClass, entity), BorderLayout.CENTER);
            iFrame.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent evt) {
                    openedInternalFrames.remove(key);
                }
              });

            openedInternalFrames.put(key, iFrame);
            showWindow(iFrame, false);
        }

        return iFrame;
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
    
    public void showStatus(String msg)
    {
        DomainObjectExplorer.getInstance().showStatus(msg);
    }

}
