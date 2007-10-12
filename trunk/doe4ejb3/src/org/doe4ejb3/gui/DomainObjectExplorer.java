/**
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;



import org.jdesktop.application.ResourceMap;

import org.doe4ejb3.annotation.EntityDescriptor;
import org.doe4ejb3.exception.ApplicationException;
import org.doe4ejb3.util.JPAUtils;
import org.doe4ejb3.util.DOEUtils;



public class DomainObjectExplorer extends javax.swing.JFrame 
{
    
    /**
     * Creates new form DomainObjectExplorer
     */
    // <editor-fold defaultstate="collapsed" desc=" Constructor ">
    protected DomainObjectExplorer() 
    {
        Exception error = null;
        initI18n();
        initComponents();
        
        DOEUtils.setWindowManager(new DefaultWindowManager(this, mdiDesktopPane, jLabelStatus));

        try {
            initPersistenceEntities();
            // TODO: create menu actions (e.g: "File-->New-->EntityXYZ")"
        } catch(Exception ex) {
            ex.printStackTrace();            
            error = ex;
        }

        // Replace left component with jOutlinePane
        jSplitPaneCentral.setLeftComponent(jOutlinePanePersistenceUnits);
        
        // replace JDesktopPane with a better  MDI container
        jScrollDesktopPane.setViewportView(mdiDesktopPane);
        jSplitPaneCentral.setRightComponent(jScrollDesktopPane);
        jMainMenuBar.add(new WindowMenu(mdiDesktopPane), 2);

        jSplitPaneCentral.setDividerLocation(200);
        
        
        // status bar initialization
        ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                jStatusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        }); 
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        jStatusAnimationLabel.setIcon(idleIcon);
        jProgressBar.setVisible(false);


        taskMonitor = DOEUtils.getTaskMonitor();
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                taskMonitorPropertyChange(evt);
            }
        });

        
        setPreferredSize(new Dimension(950,700));

        
        // TODO: advice user when no persistence entities were found.
        // if(jComboBoxEntityClass.getItemCount() == 0) error = new ApplicationException("No persistent entities found.");
        
        if(error != null) {
           DOEUtils.getWindowManager().showMessageDialog("Error: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        jSeparator3 = new javax.swing.JSeparator();
        jButtonCut = new javax.swing.JButton();
        jButtonCopy = new javax.swing.JButton();
        jButtonPaste = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jStatusPanel = new javax.swing.JPanel();
        jLabelStatus = new javax.swing.JLabel();
        jProgressPanel = new javax.swing.JPanel();
        jProgressBar = new javax.swing.JProgressBar();
        jStatusAnimationLabel = new javax.swing.JLabel();
        jSplitPaneCentral = new javax.swing.JSplitPane();
        jScrollDesktopPane = new javax.swing.JScrollPane();
        jMainMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuManage = new javax.swing.JMenu();
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

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(DomainObjectExplorer.class, this);

        jMenuItemNew.setAction(actionMap.get("createNewEntity"));
        jMenuItemNew.setMnemonic('n');
        jPopupMenuContextual.add(jMenuItemNew);

        jMenuItemManager.setAction(actionMap.get("manageEntityClass"));
        jMenuItemManager.setMnemonic('m');
        jMenuItemManager.setText(org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class).getString("managerMenu.text")); // NOI18N
        jPopupMenuContextual.add(jMenuItemManager);

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Domain Object Explorer for EJB3");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jButtonConnectionProperties.setAction(actionMap.get("openConnectionManager"));
        jButtonConnectionProperties.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonConnectionProperties.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonConnectionProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConnectionPropertiesActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonConnectionProperties);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setMaximumSize(new java.awt.Dimension(4, 0));
        jToolBar.add(jSeparator3);

        jButtonCut.setAction(actionMap.get("cut"));
        jButtonCut.setFocusable(false);
        jButtonCut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonCut.setName("cutToolBarButton"); // NOI18N
        jButtonCut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonCut);

        jButtonCopy.setAction(actionMap.get("copy"));
        jButtonCopy.setFocusable(false);
        jButtonCopy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonCopy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonCopy);

        jButtonPaste.setAction(actionMap.get("paste"));
        jButtonPaste.setFocusable(false);
        jButtonPaste.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPaste.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonPaste);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator4.setMaximumSize(new java.awt.Dimension(4, 0));
        jToolBar.add(jSeparator4);

        getContentPane().add(jToolBar, java.awt.BorderLayout.NORTH);

        jStatusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jStatusPanel.setLayout(new java.awt.BorderLayout());
        jStatusPanel.add(jLabelStatus, java.awt.BorderLayout.CENTER);

        jProgressPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jProgressPanel.add(jProgressBar);
        jProgressPanel.add(jStatusAnimationLabel);

        jStatusPanel.add(jProgressPanel, java.awt.BorderLayout.EAST);

        getContentPane().add(jStatusPanel, java.awt.BorderLayout.SOUTH);

        jSplitPaneCentral.setDividerLocation(200);
        jSplitPaneCentral.setRightComponent(jScrollDesktopPane);

        getContentPane().add(jSplitPaneCentral, java.awt.BorderLayout.CENTER);

        jMenuFile.setMnemonic('f');
        jMenuFile.setText(org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class).getString("fileMenu.text")); // NOI18N

        jMenuNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/doe4ejb3/gui/resources/new.png"))); // NOI18N
        jMenuNew.setMnemonic('n');
        jMenuNew.setText(org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class).getString("newMenu.text")); // NOI18N
        jMenuFile.add(jMenuNew);

        jMenuManage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/doe4ejb3/gui/resources/manager.png"))); // NOI18N
        jMenuManage.setMnemonic('m');
        jMenuManage.setText(org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class).getString("managerMenu.text")); // NOI18N
        jMenuFile.add(jMenuManage);
        jMenuFile.add(jSeparator1);

        jMenuItemExit.setAction(actionMap.get("exit"));
        jMenuItemExit.setMnemonic('x');
        jMenuFile.add(jMenuItemExit);

        jMainMenuBar.add(jMenuFile);

        jMenuEdit.setMnemonic('e');
        jMenuEdit.setText(org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class).getString("editMenu.text")); // NOI18N

        jMenuItemCut.setAction(actionMap.get("cut"));
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemCut);

        jMenuItemCopy.setAction(actionMap.get("copy"));
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemCopy);

        jMenuItemPaste.setAction(actionMap.get("paste"));
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClipboardActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemPaste);
        jMenuEdit.add(jSeparator2);

        jMenuItemConnectionProperties.setAction(actionMap.get("openConnectionManager"));
        jMenuItemConnectionProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConnectionPropertiesActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemConnectionProperties);

        jMainMenuBar.add(jMenuEdit);

        jMenuHelp.setMnemonic('h');
        jMenuHelp.setText(org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DomainObjectExplorer.class).getString("helpMenu.text")); // NOI18N

        jMenuItemAbout.setAction(actionMap.get("about"));
        jMenuItemAbout.setMnemonic('a');
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
            org.doe4ejb3.event.ClipboardAction.performClipboardAction(focusOwner, evt);
        }
    }//GEN-LAST:event_jMenuItemClipboardActionPerformed
   

    // <editor-fold defaultstate="collapsed" desc=" Window events ">
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        openConnectionManager();
    }//GEN-LAST:event_formWindowOpened

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc=" Public methods ">
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

    
    public static Collection<Class> getVisiblePersistentEntities(String persistenceUnit) throws Exception
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


    public void addEntityClassActions(String puName, Class entityClass)
    {
        String entityName = I18n.getEntityName(entityClass);
        
        JMenuItem newMenuItem = new JMenuItem(entityName);
        newMenuItem.setAction(org.jdesktop.application.Application.getInstance().getContext().getActionMap(DomainObjectExplorer.class, this).get("createNewEntity"));
        newMenuItem.setText(entityName);
        newMenuItem.putClientProperty("org.doe4ejb3.entityClass", entityClass);
        newMenuItem.putClientProperty("org.doe4ejb3.persistenceUnit", puName);

        JMenuItem manageMenuItem = new JMenuItem(entityName);
        manageMenuItem.setAction(org.jdesktop.application.Application.getInstance().getContext().getActionMap(DomainObjectExplorer.class, this).get("manageEntityClass"));
        manageMenuItem.setText(entityName);
        manageMenuItem.putClientProperty("org.doe4ejb3.entityClass", entityClass);
        manageMenuItem.putClientProperty("org.doe4ejb3.persistenceUnit", puName);
        
        
        JMenu newMenuPU = (JMenu)newMenuItemsForPUandEntityClasses.get(puName);
        if(newMenuPU == null) {
            newMenuPU = new JMenu(JPAUtils.getPersistenceUnitTitle(puName));
            jMenuNew.add(newMenuPU);
            newMenuItemsForPUandEntityClasses.put(puName, newMenuPU);
        }

        JMenu manageMenuPU = (JMenu)manageMenuItemsForPUandEntityClasses.get(puName);
        if(manageMenuPU == null) {
            manageMenuPU = new JMenu(JPAUtils.getPersistenceUnitTitle(puName));
            jMenuManage.add(manageMenuPU);
            manageMenuItemsForPUandEntityClasses.put(puName, manageMenuPU);
        }        
        
        String keyName = puName+"/"+entityClass.getName();
        newMenuItemsForPUandEntityClasses.put(keyName, newMenuItem);
        manageMenuItemsForPUandEntityClasses.put(keyName, manageMenuItem);
        newMenuPU.add(newMenuItem);
        manageMenuPU.add(manageMenuItem);
                
        System.out.println("DomainObjectExplorer: added managed entity class: " +  entityClass.getName());
    }
    
    
    public void removePersistenceUnit(String puName)
    {
        // TODO:
        // String title = JPAUtils.getPersistenceUnitTitle(persistenceUnit);
        // jOutlinePanePersistenceUnits.removeTab(title);

        JMenu newMenuPU = (JMenu)newMenuItemsForPUandEntityClasses.get(puName);
        if( (newMenuPU.getSubElements() == null) || (newMenuPU.getSubElements().length == 0) ) {
            jMenuNew.remove(newMenuPU);
        }

        JMenu manageMenuPU = (JMenu)manageMenuItemsForPUandEntityClasses.get(puName);
        if( (manageMenuPU.getSubElements() == null) || (manageMenuPU.getSubElements().length == 0) ) {
            jMenuManage.remove(manageMenuPU);
        }        
        
        System.out.println("DomainObjectExplorer: removed persistence unit: " + puName);
    }


    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc=" Private/protected methods ">
    private void initI18n()
    {
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(DomainObjectExplorer.class, this);        
        actionMap.get("cut").putValue(Action.NAME, I18n.getLiteral("cut.Action.text"));
        actionMap.get("cut").putValue(Action.SHORT_DESCRIPTION, I18n.getLiteral("cut.Action.shortDescription"));
        //actionMap.get("cut").putValue(Action.LONG_DESCRIPTION, I18n.getLiteral("cut.Action.longDescription"));

        actionMap.get("copy").putValue(Action.NAME, I18n.getLiteral("copy.Action.text"));
        actionMap.get("copy").putValue(Action.SHORT_DESCRIPTION, I18n.getLiteral("copy.Action.shortDescription"));
        //actionMap.get("copy").putValue(Action.LONG_DESCRIPTION, I18n.getLiteral("copy.Action.longDescription"));

        actionMap.get("paste").putValue(Action.NAME, I18n.getLiteral("paste.Action.text"));
        actionMap.get("paste").putValue(Action.SHORT_DESCRIPTION, I18n.getLiteral("paste.Action.shortDescription"));
        //actionMap.get("paste").putValue(Action.LONG_DESCRIPTION, I18n.getLiteral("paste.Action.longDescription"));

        actionMap.get("delete").putValue(Action.NAME, I18n.getLiteral("delete.Action.text"));
        actionMap.get("delete").putValue(Action.SHORT_DESCRIPTION, I18n.getLiteral("delete.Action.shortDescription"));
        //actionMap.get("delete").putValue(Action.LONG_DESCRIPTION, I18n.getLiteral("delete.Action.longDescription"));

        actionMap.get("quit").putValue(Action.NAME, I18n.getLiteral("exit.Action.text"));
        actionMap.get("quit").putValue(Action.SHORT_DESCRIPTION, I18n.getLiteral("exit.Action.shortDescription"));
        //actionMap.get("quit").putValue(Action.LONG_DESCRIPTION, I18n.getLiteral("exit.Action.longDescription"));
    }
    
    
    private void taskMonitorPropertyChange(java.beans.PropertyChangeEvent evt) {                                     
        String propertyName = evt.getPropertyName();
        if ("started".equals(propertyName)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (!busyIconTimer.isRunning()) {
                jStatusAnimationLabel.setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
            }
            jProgressBar.setVisible(true);
            jProgressBar.setIndeterminate(true);
        }
        else if ("done".equals(propertyName)) {
            busyIconTimer.stop();
            jStatusAnimationLabel.setIcon(idleIcon);
            jProgressBar.setVisible(false);
            jProgressBar.setValue(0);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        else if ("progress".equals(propertyName)) {
            int value = (Integer)(evt.getNewValue());
            jProgressBar.setVisible(true);
            jProgressBar.setIndeterminate(false);
            jProgressBar.setValue(value);
        }
        /*
        else if ("message".equals(propertyName)) {
            String text = (String)(evt.getNewValue());
            DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, (text == null) ? "" : text);
        } 
        */
    }      
    
    
    protected void initPersistenceEntities() throws Exception
    {
        System.out.println("EntityContainer: initPersistentEntities...");
        Collection<String> persistenceUnits = org.doe4ejb3.util.JPAUtils.getPersistenceUnits();
        for(String persistenceUnit : persistenceUnits) 
        {
            Collection<Class> persistenceEntities = getVisiblePersistentEntities(persistenceUnit);
            JList entityList = new JList(persistenceEntities.toArray());
            entityList.putClientProperty("org.doe4ejb3.persistenceUnit", persistenceUnit);
            entityList.setCellRenderer(EntityClassListCellRenderer.getInstance());
            entityList.setComponentPopupMenu(jPopupMenuContextual);
            entityList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    try {
                        JList  list = (JList)evt.getSource();
                        String puName = (String)list.getClientProperty("org.doe4ejb3.persistenceUnit");
                        Class  entityClass = (Class)list.getSelectedValue();
                        if( (evt.getClickCount() > 1) && (entityClass != null) ) {
                            DOEUtils.openEntityManager(puName, entityClass);
                        }
                    } catch(Exception ex) {
                        DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, "Error: " + ex.getMessage());
                    }
                }
            });
            
            String title = JPAUtils.getPersistenceUnitTitle(persistenceUnit);
            jOutlinePanePersistenceUnits.addTab(title, new JScrollPane(entityList));
            
            for(Class entityClass : persistenceEntities) addEntityClassActions(persistenceUnit, entityClass);
        }
    }
    
    

    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc=" Actions ">
    
    @org.jdesktop.application.Action
    public void about() {
        StringBuffer info = new StringBuffer();
        info.append(I18n.getLiteral(Application.class, "Application.title") + " - v" + I18n.getLiteral(Application.class, "Application.version")); info.append("\n");
        info.append("\n");
        info.append(I18n.getLiteral("msg.developers")); info.append("\n");
        info.append("   Jordi Marine Fort <jmarine@dev.java.net>\n");
        info.append("\n");
        info.append(I18n.getLiteral("msg.thanksTo")); info.append("\n");
        info.append("   SUN Microsystems, Inc.\n");
        info.append("   NetBeans team.\n");
        info.append("   GlassFish community.\n");
        info.append("   Oracle TopLink Essentials team.\n");
        info.append("   Trails framework team.\n");
        info.append("   ANTLR project team.\n");
        info.append("   Beans binding project team.\n");
        info.append("   Swing application framework team.\n");
        info.append("   JAXB framework team.\n");
        info.append("   Java tutorial and demo writers.\n");
        info.append("   Gerald Nunn (MDIDesktopPane/WindowMenu article)\n");
        info.append("   Michael Urban (AbstractValidator article)\n");
        info.append("\n");
        DOEUtils.getWindowManager().showMessageDialog(info.toString(), I18n.getLiteral("about.Action.text"), JOptionPane.INFORMATION_MESSAGE);
    }

    
    @org.jdesktop.application.Action
    public void exit()
    {
       Application.getInstance().exit();
    }    

    @org.jdesktop.application.Action    
    public void openConnectionManager()
    {
        try {
            if(connectionManagerFrame == null) {
                connectionManagerFrame = new ConnectionManager();
            }
            
            DOEUtils.getWindowManager().showWindow(connectionManagerFrame, true);
            
        } catch(Exception ex) {
            System.out.println("DOE.openConnectionManager(): Error " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
    @org.jdesktop.application.Action
    public void manageEntityClass(ActionEvent evt) {
        try {
            DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, "");
            
            // Check "File-->Manage-->Entity" class:
            javax.swing.JMenuItem menuItem = (javax.swing.JMenuItem)evt.getSource();
            String puName = (String)menuItem.getClientProperty("org.doe4ejb3.persistenceUnit");
            Class  entityClass = (Class)menuItem.getClientProperty("org.doe4ejb3.entityClass");
            
            if(entityClass == null) {
                JComponent sourceControl = (JComponent)((javax.swing.JPopupMenu)((javax.swing.JMenuItem)evt.getSource()).getParent()).getInvoker();
                if( (sourceControl != null) && (sourceControl instanceof JList) ) {
                    // Manage entidad:
                    JList  list = (JList)sourceControl; 
                    puName = (String)list.getClientProperty("org.doe4ejb3.persistenceUnit");
                    entityClass = (Class)list.getSelectedValue();
                    if(entityClass == null) {
                        throw new ApplicationException("A class must be selected");
                    }
                }
            }

            DOEUtils.openEntityManager(puName, entityClass);
            
        } catch(ApplicationException ex) {
            
            DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch(Exception ex) {
            
            DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            
        }
    }
    

    @org.jdesktop.application.Action
    public void createNewEntity(ActionEvent evt) {
        try {
            DOEUtils.getWindowManager().showStatus(DOEUtils.APPLICATION_WINDOW, "");

            // Check "File-->New-->Entity" class:
            javax.swing.JMenuItem menuItem = (javax.swing.JMenuItem)evt.getSource();
            String puName = (String)menuItem.getClientProperty("org.doe4ejb3.persistenceUnit");
            Class  entityClass = (Class)menuItem.getClientProperty("org.doe4ejb3.entityClass");
            
            // Check contextual popup menu in left panel entity lists:
            if(entityClass == null) {
                JComponent sourceControl = (JComponent)((javax.swing.JPopupMenu)menuItem.getParent()).getInvoker();
                if(sourceControl instanceof JList) {
                    JList list = (JList)sourceControl; 
                    puName = (String)list.getClientProperty("org.doe4ejb3.persistenceUnit");
                    entityClass = (Class)list.getSelectedValue();
                    if(entityClass == null) {
                        throw new ApplicationException("A class must be selected");
                    }
                }
            }
            
            DOEUtils.openEntityEditor(puName, entityClass, null);
            
        } catch(ApplicationException ex) {
            
            DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch(Exception ex) {
            
            DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            
        }
    }

    


    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc=" Attributes ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonConnectionProperties;
    private javax.swing.JButton jButtonCopy;
    private javax.swing.JButton jButtonCut;
    private javax.swing.JButton jButtonPaste;
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
    private javax.swing.JMenu jMenuManage;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JPopupMenu jPopupMenuContextual;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JPanel jProgressPanel;
    private javax.swing.JScrollPane jScrollDesktopPane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSplitPane jSplitPaneCentral;
    private javax.swing.JLabel jStatusAnimationLabel;
    private javax.swing.JPanel jStatusPanel;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables

    /** Other UI components */
    private static DomainObjectExplorer DOE = null;
    
    private MDIDesktopPane mdiDesktopPane = new MDIDesktopPane();
    private JOutlinePane   jOutlinePanePersistenceUnits = new JOutlinePane();

    /** Connection Manager */ 
    private ConnectionManager connectionManagerFrame = null;

    /** Caches */ 
    private HashMap<String,JMenuItem> newMenuItemsForPUandEntityClasses = new HashMap<String,JMenuItem>();
    private HashMap<String,JMenuItem> manageMenuItemsForPUandEntityClasses = new HashMap<String,JMenuItem>();

    

    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    
    private org.jdesktop.application.TaskMonitor taskMonitor;

    // </editor-fold>
    


    
}
