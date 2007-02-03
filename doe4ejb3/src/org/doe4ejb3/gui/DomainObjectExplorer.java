/*
 * DomainObjectExplorer.java
 *
 * Created on 18 / august / 2006, 21:38
 * @author Jordi Marine Fort
 */
package org.doe4ejb3.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.print.*;
import java.beans.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

// import javax.jnlp.*;

import org.doe4ejb3.annotation.EntityDescriptor;
import org.doe4ejb3.event.EntityEvent;
import org.doe4ejb3.event.EntityListener;
import org.doe4ejb3.exception.ApplicationException;
import org.doe4ejb3.util.JPAUtils;
import org.doe4ejb3.util.EJBQLUtils;
import org.doe4ejb3.util.PrintUtils;




public class DomainObjectExplorer extends javax.swing.JFrame
{
    
    /**
     * Creates new form DomainObjectExplorer
     */
    // <editor-fold defaultstate="collapsed" desc=" Constructor ">
    protected DomainObjectExplorer() 
    {
        Exception error = null;
        initComponents();

        try {
            initPersistenceEntities();
            // TODO: create menu actions (e.g: "File-->New-->EntityXYZ")"
        } catch(Exception ex) {
            ex.printStackTrace();            
            error = ex;
        }
        
        
        // replace JDesktopPane with a better  MDI container
        mdiDesktopPane = new MDIDesktopPane();
        jScrollDesktopPane.setViewportView(mdiDesktopPane);
        jSplitPaneCentral.setRightComponent(jScrollDesktopPane);
        jMainMenuBar.add(new WindowMenu(mdiDesktopPane), 2);
        
        setSize(950,700);
        
        // TODO: advice user when no persistence entities were found.
        // if(jComboBoxEntityClass.getItemCount() == 0) error = new ApplicationException("No persistent entities found.");
        
        if(error != null) {
           JOptionPane.showInternalMessageDialog(mdiDesktopPane, "Error: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    // </editor-fold> 
  
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPopupMenuContextual = new javax.swing.JPopupMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemManager = new javax.swing.JMenuItem();
        jToolBar = new javax.swing.JToolBar();
        jButtonConnectionProperties = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jStatusPanel = new javax.swing.JPanel();
        jLabelStatus = new javax.swing.JLabel();
        jSplitPaneCentral = new javax.swing.JSplitPane();
        jOutlinePanePersistenceUnits = new org.doe4ejb3.gui.JOutlinePane();
        jScrollDesktopPane = new javax.swing.JScrollPane();
        jMainMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemConnectionProperties = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jMenuItemNew.setMnemonic('n');
        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewActionPerformed(evt);
            }
        });

        jPopupMenuContextual.add(jMenuItemNew);

        jMenuItemManager.setMnemonic('m');
        jMenuItemManager.setText("Manage");
        jMenuItemManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemManagerActionPerformed(evt);
            }
        });

        jPopupMenuContextual.add(jMenuItemManager);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Domain Object Explorer for EJB3");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jButtonConnectionProperties.setMnemonic('n');
        jButtonConnectionProperties.setText("Connection properties");
        jButtonConnectionProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConnectionPropertiesActionPerformed(evt);
            }
        });

        jToolBar.add(jButtonConnectionProperties);

        jButtonExit.setMnemonic('x');
        jButtonExit.setText("Exit");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jToolBar.add(jButtonExit);

        getContentPane().add(jToolBar, java.awt.BorderLayout.NORTH);

        jStatusPanel.setLayout(new java.awt.BorderLayout());

        jStatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jStatusPanel.add(jLabelStatus, java.awt.BorderLayout.CENTER);

        getContentPane().add(jStatusPanel, java.awt.BorderLayout.SOUTH);

        jSplitPaneCentral.setDividerLocation(300);
        jSplitPaneCentral.setLeftComponent(jOutlinePanePersistenceUnits);

        jSplitPaneCentral.setRightComponent(jScrollDesktopPane);

        getContentPane().add(jSplitPaneCentral, java.awt.BorderLayout.CENTER);

        jMenuFile.setMnemonic('f');
        jMenuFile.setText("File");
        jMenuNew.setMnemonic('n');
        jMenuNew.setText("New");
        jMenuFile.add(jMenuNew);

        jMenuFile.add(jSeparator1);

        jMenuItemExit.setMnemonic('x');
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });

        jMenuFile.add(jMenuItemExit);

        jMainMenuBar.add(jMenuFile);

        jMenuEdit.setMnemonic('e');
        jMenuEdit.setText("Edit");
        jMenuItemCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemCut.setMnemonic('t');
        jMenuItemCut.setText("Cut");
        jMenuItemCut.setActionCommand((String)TransferHandler.getCutAction().getValue(Action.NAME));
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });

        jMenuEdit.add(jMenuItemCut);

        jMenuItemCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemCopy.setMnemonic('c');
        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });

        jMenuEdit.add(jMenuItemCopy);

        jMenuItemPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemPaste.setMnemonic('p');
        jMenuItemPaste.setText("Paste");
        jMenuItemPaste.setActionCommand((String)TransferHandler.getPasteAction().getValue(Action.NAME));
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });

        jMenuEdit.add(jMenuItemPaste);

        jMenuEdit.add(jSeparator2);

        jMenuItemConnectionProperties.setMnemonic('n');
        jMenuItemConnectionProperties.setText("Connection properties...");
        jMenuItemConnectionProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConnectionPropertiesActionPerformed(evt);
            }
        });

        jMenuEdit.add(jMenuItemConnectionProperties);

        jMainMenuBar.add(jMenuEdit);

        jMenuHelp.setMnemonic('h');
        jMenuHelp.setText("Help");
        jMenuItemAbout.setMnemonic('a');
        jMenuItemAbout.setText("About");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });

        jMenuHelp.add(jMenuItemAbout);

        jMainMenuBar.add(jMenuHelp);

        setJMenuBar(jMainMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemConnectionPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConnectionPropertiesActionPerformed
        openConnectionManager();
    }//GEN-LAST:event_jMenuItemConnectionPropertiesActionPerformed

    private void jButtonConnectionPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConnectionPropertiesActionPerformed
        openConnectionManager();
    }//GEN-LAST:event_jButtonConnectionPropertiesActionPerformed

    private void jMenuItemClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClipboardActionPerformed
        System.out.println("Clipboard action original source : " + evt.getSource());
        JInternalFrame iFrame = mdiDesktopPane.getSelectedFrame();
        System.out.println("Clipboard action on frame: " + iFrame + ", selected=" + iFrame.isSelected());
        if(iFrame != null) {
            iFrame.restoreSubcomponentFocus(); // fix focus
            JComponent focusOwner = (JComponent)KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();  //  iFrame.getMostRecentFocusOwner(); ???
            clipboardActionPerformed(focusOwner, evt);
        }
    }//GEN-LAST:event_jMenuItemClipboardActionPerformed
   
    private void jMenuItemManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemManagerActionPerformed
        try {
            DomainObjectExplorer.getInstance().showStatus("");
            
            Object invoker = ((javax.swing.JPopupMenu)((javax.swing.JMenuItem)evt.getSource()).getParent()).getInvoker();
            JComponent sourceControl = (JComponent)invoker;

            System.out.println("Source control: " + sourceControl);
            if(sourceControl instanceof JList) {
                // Nueva entidad:
                JList list = (JList)sourceControl; 
                Class entityClass = (Class)list.getSelectedValue();
                if(entityClass != null) DomainObjectExplorer.getInstance().openInternalFrameEntityManager(entityClass);
                else throw new ApplicationException("A class must be selected");
            }
            
        } catch(ApplicationException ex) {
            
            JOptionPane.showInternalMessageDialog(DomainObjectExplorer.getInstance().getDesktopPane(), "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch(Exception ex) {
            
            JOptionPane.showInternalMessageDialog(DomainObjectExplorer.getInstance().getDesktopPane(), "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            
        }
        
    }//GEN-LAST:event_jMenuItemManagerActionPerformed

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
        try {
            DomainObjectExplorer.getInstance().showStatus("");

            // Check "File-->New-->Entity" class:
            javax.swing.JMenuItem menuItem = (javax.swing.JMenuItem)evt.getSource();
            Class entityClass = (Class)menuItem.getClientProperty("org.doe4ejb3.entityClass");
            
            // Check contextual popup menu in left panel entity lists:
            if(entityClass == null) {
                JComponent sourceControl = (JComponent)((javax.swing.JPopupMenu)menuItem.getParent()).getInvoker();
                if(sourceControl instanceof JList) {
                    JList list = (JList)sourceControl; 
                    entityClass = (Class)list.getSelectedValue();
                    if(entityClass == null) {
                        throw new ApplicationException("A class must be selected");
                    }
                }
            }
            
            if(entityClass != null) DomainObjectExplorer.getInstance().openInternalFrameEntityEditor(entityClass, null);
            
        } catch(ApplicationException ex) {
            
            JOptionPane.showInternalMessageDialog(DomainObjectExplorer.getInstance().getDesktopPane(), "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch(Exception ex) {
            
            JOptionPane.showInternalMessageDialog(DomainObjectExplorer.getInstance().getDesktopPane(), "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            
        }

    }//GEN-LAST:event_jMenuItemNewActionPerformed


    // <editor-fold defaultstate="collapsed" desc=" Window events ">
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        openConnectionManager();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit(); 
    }//GEN-LAST:event_formWindowClosing

    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc=" Main menu events ">
    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        exit();
    }//GEN-LAST:event_jMenuItemExitActionPerformed


    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        JOptionPane.showInternalMessageDialog(mdiDesktopPane, "Domain Object Explorer for EJB3 - version 0.2 alpha\nDevelopers: Jordi Marine Fort <jmarine@dev.java.net>", "About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    public void clipboardActionPerformed(JComponent focusOwner, java.awt.event.ActionEvent evt) {
        System.out.println("Clipboard action on control: " + focusOwner);
        if(focusOwner != null) {
            ActionMap actionMap = focusOwner.getActionMap();
            Action action = actionMap.get(evt.getActionCommand());
            System.out.println("Clipboard action : " + action);
            if (action != null) {
                action.actionPerformed(new ActionEvent(focusOwner,
                                          ActionEvent.ACTION_PERFORMED,
                                          null));
            }
        }
    }                    
    
    /*
    public java.awt.datatransfer.Clipboard getClipboard()
    {
       java.awt.datatransfer.Clipboard clipboard = getToolkit().getSystemClipboard(); 
       System.out.println("System clipboard: " + clipboard);
       return clipboard;
    }
    */

    // </editor-fold>
 
    
    // <editor-fold defaultstate="collapsed" desc=" Contextual menu events ">
    
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc=" ToolBar events ">
    
    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        exit();
    }//GEN-LAST:event_jButtonExitActionPerformed

    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc=" Other application events ">

    
    // </editor-fold>

   
    // <editor-fold defaultstate="collapsed" desc=" Public methods ">
    public HashMap getConnectionParams()
    {
        return connectionParams;
    }
    
    public static final void main(String args[]) throws Exception
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                getInstance().setVisible(true);
            }
        });
    
    }
    
    public static final DomainObjectExplorer getInstance()
    {
        if(DOE == null) {
            DOE = new DomainObjectExplorer();
        }
        return DOE;
    }


    public JDesktopPane getDesktopPane()
    {
        return this.mdiDesktopPane;
    }
    

    public void showStatus(String msg)
    {
        if(msg == null) msg = "";
        this.jLabelStatus.setText(" " + msg);
    }

    public void exit()
    {
        this.setVisible(false);
        this.dispose();
        
        System.exit(0);
    }


    public void addEntityClassActions(Class entityClass)
    {
        String entityName = I18n.getEntityName(entityClass);
        String puName = JPAUtils.getPersistentUnitNameForEntity(entityClass);
        
        JMenuItem menuItem = new JMenuItem(entityName);
        menuItem.putClientProperty("org.doe4ejb3.entityClass", entityClass);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemNewActionPerformed(evt);
            }
        });

        newMenuItemsForEntityClasses.put(entityClass, menuItem);
        jMenuNew.add(menuItem);
                
        System.out.println("DomainObjectExplorer: added managed entity class: " +  entityClass.getName());
    }
    
    public void removeEntityClass(Class entityClass)
    {
        JMenuItem menuItem = newMenuItemsForEntityClasses.get(entityClass);
        if(menuItem != null) {
            jMenuNew.remove(menuItem);
        }

        System.out.println("DomainObjectExplorer: removed managed entity class: " +  entityClass.getName());
    }

/*    
    public byte[] openFileDialogFromJwsApp(String extensionFilters[]) throws Exception
    {
        BufferedInputStream bis = null;
        try {
            FileOpenService fos = (FileOpenService)ServiceManager.lookup("javax.jnlp.FileOpenService");
            FileContents fileContents = fos.openFileDialog(null, extensionFilters);
            byte data[] = new byte[(int)fileContents.getLength()];
            bis = new BufferedInputStream(fileContents.getInputStream());
            int len = bis.read(data, 0, data.length);
            if(len < data.length) throw new Exception("Cannot get all content in 1 read.");
            return data;
           
        } finally {
            if(bis != null) {
                try { bis.close(); bis = null; }
                catch(Exception ex) { }
            }
        }
    }
    
    
    public ImageIcon createImageIcon(byte imageRawData[]) throws Exception
    {
        Image img = this.getToolkit().createImage(imageRawData);
        return new ImageIcon(img);
    }
 */



    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc=" Private/protected methods ">
    protected void initPersistenceEntities() throws Exception
    {
        System.out.println("EntityContainer: initPersistentEntities...");
        Collection<String> persistenceUnits = org.doe4ejb3.util.JPAUtils.getPersistenceUnits();
        for(String persistenceUnit : persistenceUnits) 
        {
            Collection<Class> persistenceEntities = getVisiblePersistentEntities(persistenceUnit);
            JList entityList = new JList(persistenceEntities.toArray());
            entityList.setCellRenderer(EntityClassListCellRenderer.getInstance());
            entityList.setComponentPopupMenu(jPopupMenuContextual);
            entityList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    try {
                        JList list = (JList)evt.getSource();
                        Class entityClass = (Class)list.getSelectedValue();
                        if( (evt.getClickCount() > 1) && (entityClass != null) ) {
                            openInternalFrameEntityManager(entityClass);
                        }
                    } catch(Exception ex) {
                        showStatus("Error: " + ex.getMessage());
                    }
                }
            });
            
            String title = "PU: " + persistenceUnit;
            if(persistenceUnit.length() == 0) title = "Default PU";
            jOutlinePanePersistenceUnits.addTab(title, new JScrollPane(entityList));
            
            for(Class entityClass : persistenceEntities) addEntityClassActions(entityClass);
        }
    }
    
    public Collection<Class> getVisiblePersistentEntities(String persistenceUnit) throws Exception
    {
        Collection<Class> persistenceEntities = org.doe4ejb3.util.JPAUtils.getPersistentEntities(persistenceUnit);
        Iterator<Class> iter = persistenceEntities.iterator();
        while(iter.hasNext()) {
            Class entityClass = iter.next();
            EntityDescriptor ed = (EntityDescriptor)entityClass.getAnnotation(EntityDescriptor.class);
            if( (ed != null) && (ed.hidden()) ) {
                iter.remove();
            }
        }

        return persistenceEntities;
    }
    
    public void openConnectionManager()
    {
        try {
            if(connectionManagerFrame == null) {
                connectionManagerFrame = new ConnectionManager(connectionParams);
            }
            if(connectionManagerFrame.getParent() == null) {
                mdiDesktopPane.add(connectionManagerFrame, true);
            }
            mdiDesktopPane.centerFrame(connectionManagerFrame);
            connectionManagerFrame.setVisible(true);
            connectionManagerFrame.setSelected(true);
        } catch(java.beans.PropertyVetoException pve) {
            System.out.println("DOE.openConnectionManager(): Error " + pve.getMessage());
            pve.printStackTrace();
        }
    }
    

    public void openInternalFrameEntityManager(Class entityClass) throws Exception
    {
        final Object key = entityClass.getName() + "Manager";
        JInternalFrame oldFrame = openedInternalFrames.get(key);
        if(oldFrame != null) {

            if(oldFrame.isIcon()) oldFrame.setIcon(false);
            oldFrame.setSelected(true);
            
        } else {

            String title = org.doe4ejb3.gui.I18n.getEntityName(entityClass) + " manager";
            JInternalFrame iFrame = new JInternalFrame(title, true, true, true, false );
            iFrame.setFrameIcon(EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass));
            iFrame.getContentPane().setLayout(new BorderLayout());
            iFrame.getContentPane().add(new EntityManagerPane(entityClass), BorderLayout.CENTER);
            
            iFrame.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent evt) {
                    JInternalFrame iFrame = evt.getInternalFrame();
                    iFrame.putClientProperty("acceptListeners", null);
                    openedInternalFrames.remove(key);
                }
              });


            openedInternalFrames.put(key, iFrame);
            mdiDesktopPane.add(iFrame);
            iFrame.setVisible(true);
            iFrame.setSelected(true);

        }
    }
    
    
    public JInternalFrame openInternalFrameEntityEditor(Class entityClass, Object entity) throws Exception
    {
        final Object key = (entity != null) ? entity : entityClass.getName() + "Editor";
        JInternalFrame iFrame = openedInternalFrames.get(key);

        if(iFrame == null) {
            iFrame = createInternalFrameEntityEditor(entityClass, entity);
            iFrame.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent evt) {
                    openedInternalFrames.remove(key);
                }
              });

            openedInternalFrames.put(key, iFrame);
            mdiDesktopPane.add(iFrame);
        }

        if(iFrame.isIcon()) iFrame.setIcon(false);
        iFrame.setVisible(true);
        iFrame.setSelected(true);
        
        return iFrame;
    }
    

    private JInternalFrame createInternalFrameEntityEditor(final Class entityClass, Object entity) throws Exception 
    {
        showStatus("");
        String title = org.doe4ejb3.gui.I18n.getEntityName(entityClass);
        if(entity == null) title = org.doe4ejb3.gui.I18n.getLiteral("New") + " " + title.toLowerCase();
        else title = org.doe4ejb3.gui.I18n.getLiteral("Edit") + " " + title + ": " + entity.toString();
        
        System.out.println("Creating internal frame");
        final JInternalFrame iFrame = new JInternalFrame(title, true, true, true, false );
        iFrame.setFrameIcon(EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass));
        iFrame.putClientProperty("entityListeners", new EventListenerList());
        
        System.out.println("Preparing editor ");
        final EntityEditorInterface editor = EditorFactory.getEntityEditor(entityClass);
        if(entity == null) editor.newEntity(entityClass);
        else editor.setEntity(entity);
        
        System.out.println("Preparing buttons...");
        JButton btnAccept = new JButton("Accept");
        btnAccept.setMnemonic('a');
        btnAccept.setActionCommand("accept");
        btnAccept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    iFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Object oldEntity = editor.getEntity();
                    Object newEntity = JPAUtils.saveEntity(getConnectionParams(), oldEntity);
                    showStatus(MessageFormat.format("{0} saved.", JPAUtils.getEntityName(entityClass)));
                    
                    EntityEvent entityEvent = new EntityEvent(evt.getSource(), editor.isNew()? EntityEvent.ENTITY_INSERT : EntityEvent.ENTITY_UPDATE, oldEntity, newEntity);
                    EventListenerList listenerList = (EventListenerList)iFrame.getClientProperty("entityListeners");
                    Object[] listeners = listenerList.getListenerList();
                    for (int i = listeners.length-2; i>=0; i-=2) {
                        if (listeners[i]==EntityListener.class) {
                            System.out.println("EditorFactory: notification of tableChanged to: " + listeners[i+1]);
                            ((EntityListener)listeners[i+1]).entityChanged(entityEvent);
                        }
                    }
                    
                    iFrame.dispose();
                } catch(Exception ex) {
                    iFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    showStatus(MessageFormat.format("Error saving {0}: {1}", JPAUtils.getEntityName(entityClass), ex.getMessage()));
                    ex.printStackTrace();
                } finally {
                    iFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                
            }
        });
        
        JButton btnClose = new JButton("Close");
        btnClose.setMnemonic('c');
        btnClose.setActionCommand("close");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                iFrame.dispose();
            }
        });
        
        JButton btnPrint = new JButton("Print");
        btnPrint.setMnemonic('p');
        btnPrint.setActionCommand("print");
        btnPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(editor instanceof Printable) {
                    PrinterJob printJob = PrinterJob.getPrinterJob();
                    printJob.setPrintable((Printable)editor);
                    if (printJob.printDialog()) {
                        try {
                            printJob.print();
                        } catch(PrinterException pe) {
                            System.out.println("Error printing: " + pe);
                        }                    
                    }
                } else {
                    PrintUtils.printComponent((Component)editor);
                }
            }
        });
        

        JPanel buttons = new JPanel();
        buttons.add(btnAccept);
        buttons.add(btnPrint);
        buttons.add(btnClose);


        JScrollPane scrollPaneForEditor = new JAutoScrollPaneOnComponentFocus((java.awt.Container)editor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        iFrame.getContentPane().setLayout(new BorderLayout());
        iFrame.getContentPane().add(scrollPaneForEditor, BorderLayout.CENTER);
        iFrame.getContentPane().add(buttons, BorderLayout.SOUTH);

        iFrame.pack();

        return iFrame;
    }
    
 
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc=" Attributes ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonConnectionProperties;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JMenuBar jMainMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemConnectionProperties;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemManager;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenu jMenuNew;
    private org.doe4ejb3.gui.JOutlinePane jOutlinePanePersistenceUnits;
    private javax.swing.JPopupMenu jPopupMenuContextual;
    private javax.swing.JScrollPane jScrollDesktopPane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPaneCentral;
    private javax.swing.JPanel jStatusPanel;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables

    /** Other UI components */
    private static DomainObjectExplorer DOE = null;
    private MDIDesktopPane mdiDesktopPane = null;

    /** Connection Manager */ 
    private HashMap<String,String> connectionParams = new HashMap<String,String>();
    private ConnectionManager connectionManagerFrame = null;

    /** Caches */ 
    private HashMap<Object,JInternalFrame> openedInternalFrames = new HashMap<Object,JInternalFrame>();
    private HashMap<Class,JMenuItem> newMenuItemsForEntityClasses = new HashMap<Class,JMenuItem>();

    // </editor-fold>
    

}
