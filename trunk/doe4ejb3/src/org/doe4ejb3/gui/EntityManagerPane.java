/*
 * EntityManagerPane.java
 *
 * Created on 14 October 2006, 13:44
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.doe4ejb3.exception.ApplicationException;
import org.doe4ejb3.util.JPAUtils;
import org.doe4ejb3.util.EJBQLUtils;


public class EntityManagerPane extends javax.swing.JPanel implements EditorLayoutInterface
{
    
    private String puName = null;
    private Class entityClass = null;
    private CustomQueryEditorImpl customQueryEditor = null;
    private QueryParametersEditorImpl queryParametersPanel = null;
    private JComponent entityListEditor = null;
    private ListSelectionModel listSelectionModel = null;
    private DefaultListModel   listModel = null;
    
    /**
     * Creates new form EntityManagerPane
     */
    public EntityManagerPane(String puName, Class entityClass) {
        initComponents();
        this.puName = puName;
        this.entityClass = entityClass;

        try {
            // post initialization:
            Object outBinding[] = new Object[1];
            entityListEditor = EditorFactory.getCollectionEditor(this, puName, null, entityClass, true, outBinding);
            listModel = (DefaultListModel)entityListEditor.getClientProperty("listModel");
            listSelectionModel = (ListSelectionModel)entityListEditor.getClientProperty("listSelectionModel");

            jComboBoxNamedQuery.removeAllItems();                                           
            jPanelQueryParams.setVisible(false);
            jPanelResults.setLayout(new java.awt.BorderLayout());
            jPanelResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,0,0,0));
            jPanelResults.add("Center", entityListEditor);


            // load queries in combobox:
            jComboBoxNamedQuery.addItem("All");
            jComboBoxNamedQuery.addItem("Custom");
            if(entityClass.getAnnotations() != null) {                                          
                for(Annotation annotation : entityClass.getAnnotations()) {                         
                    if(annotation instanceof NamedQuery) {                                              
                        NamedQuery namedQuery = (NamedQuery)annotation;                                 
                        String ejbql = namedQuery.query();                                              
                        if( (ejbql != null) && (ejbql.toUpperCase().startsWith("SELECT")) ) {                                                                                               
                            ListItem item = new ListItem(namedQuery, namedQuery.name());                                                                                                    
                            jComboBoxNamedQuery.addItem(item);
                        }
                    }
                    else if(annotation instanceof NamedQueries) {
                        NamedQueries namedQueries = (NamedQueries)annotation;
                        for(NamedQuery namedQuery : namedQueries.value()) {
                            String ejbql = namedQuery.query();                                              
                            if( (ejbql != null) && (ejbql.toUpperCase().startsWith("SELECT")) ) {                                                                                               
                                ListItem item = new ListItem(namedQuery, namedQuery.name());                                                                                                    
                                jComboBoxNamedQuery.addItem(item);
                            }
                        }
                    }
                }
            }

            
        } catch(Exception ex) {
            DomainObjectExplorer.getInstance().getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);            
            ex.printStackTrace();
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuContextual = new javax.swing.JPopupMenu();
        jMenuItemContextualNew = new javax.swing.JMenuItem();
        jMenuItemContextualEdit = new javax.swing.JMenuItem();
        jMenuItemContextualSeparator1 = new javax.swing.JSeparator();
        jMenuItemContextualDelete = new javax.swing.JMenuItem();
        jPanelEntidad = new javax.swing.JPanel();
        jLabelQuery = new javax.swing.JLabel();
        jComboBoxNamedQuery = new javax.swing.JComboBox();
        jPanelQueryParams = new javax.swing.JPanel();
        jButtonSearch = new javax.swing.JButton();
        jPanelResults = new javax.swing.JPanel();

        jMenuItemContextualNew.setMnemonic('n');
        jMenuItemContextualNew.setText("New");
        jMenuItemContextualNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemContextualNewActionPerformed(evt);
            }
        });
        jPopupMenuContextual.add(jMenuItemContextualNew);

        jMenuItemContextualEdit.setMnemonic('e');
        jMenuItemContextualEdit.setText("Edit");
        jMenuItemContextualEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemContextualEditActionPerformed(evt);
            }
        });
        jPopupMenuContextual.add(jMenuItemContextualEdit);
        jPopupMenuContextual.add(jMenuItemContextualSeparator1);

        jMenuItemContextualDelete.setMnemonic('d');
        jMenuItemContextualDelete.setText("Delete");
        jMenuItemContextualDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemContextualDeleteActionPerformed(evt);
            }
        });
        jPopupMenuContextual.add(jMenuItemContextualDelete);

        setLayout(new java.awt.BorderLayout());

        jPanelEntidad.setMinimumSize(new java.awt.Dimension(500, 400));
        jPanelEntidad.setPreferredSize(new java.awt.Dimension(500, 401));

        jLabelQuery.setText("Query:");

        jComboBoxNamedQuery.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        jComboBoxNamedQuery.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxNamedQueryItemStateChanged(evt);
            }
        });

        jPanelQueryParams.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters:"));

        jButtonSearch.setAction(org.jdesktop.application.Application.getInstance().getContext().getActionMap(EntityManagerPane.class, this).get("searchInstances"));
        jButtonSearch.setMnemonic('s');

        org.jdesktop.layout.GroupLayout jPanelResultsLayout = new org.jdesktop.layout.GroupLayout(jPanelResults);
        jPanelResults.setLayout(jPanelResultsLayout);
        jPanelResultsLayout.setHorizontalGroup(
            jPanelResultsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 581, Short.MAX_VALUE)
        );
        jPanelResultsLayout.setVerticalGroup(
            jPanelResultsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 316, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanelEntidadLayout = new org.jdesktop.layout.GroupLayout(jPanelEntidad);
        jPanelEntidad.setLayout(jPanelEntidadLayout);
        jPanelEntidadLayout.setHorizontalGroup(
            jPanelEntidadLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelEntidadLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelEntidadLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanelQueryParams, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelEntidadLayout.createSequentialGroup()
                        .add(jLabelQuery)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jComboBoxNamedQuery, 0, 429, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonSearch)))
                .addContainerGap())
        );
        jPanelEntidadLayout.setVerticalGroup(
            jPanelEntidadLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelEntidadLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelEntidadLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelQuery)
                    .add(jButtonSearch)
                    .add(jComboBoxNamedQuery, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelQueryParams, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanelEntidad, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemContextualDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemContextualDeleteActionPerformed
        try {
            DomainObjectExplorer.getInstance().getWindowManager().showStatus("");
            
            if(listSelectionModel.getMinSelectionIndex() == -1) {
                throw new ApplicationException("There are not selected objects.");
            }

            int confirm = DomainObjectExplorer.getInstance().getWindowManager().showConfirmDialog( "Do you really want to delete selected objects?", "Confirm operation", JOptionPane.OK_CANCEL_OPTION);
            if(confirm == JOptionPane.OK_OPTION) 
            {
                Object invoker = ((javax.swing.JPopupMenu)((javax.swing.JMenuItem)evt.getSource()).getParent()).getInvoker();
                JComponent sourceControl = (JComponent)invoker;
                Class entityClass = (Class)sourceControl.getClientProperty("org.doe4ejb3.entityClass");                
                for(int index = listSelectionModel.getMaxSelectionIndex(); index >= listSelectionModel.getMinSelectionIndex(); index--) 
                {
                    if(listSelectionModel.isSelectedIndex(index)) {
                        Object entity = listModel.getElementAt(index);
                        JPAUtils.removeEntity(DomainObjectExplorer.getInstance().getConnectionParams(), puName, entity);
                        listModel.removeElementAt(index);
                    }
                }
            }
            

            DomainObjectExplorer.getInstance().getWindowManager().showStatus("Selected items has been deleted.");
            
            
        } catch(ApplicationException ex) {
            
            DomainObjectExplorer.getInstance().getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch(Exception ex) {
            
            DomainObjectExplorer.getInstance().getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemContextualDeleteActionPerformed

    private void jMenuItemContextualEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemContextualEditActionPerformed
        try {
            DomainObjectExplorer.getInstance().getWindowManager().showStatus("");
            
            Object invoker = ((javax.swing.JPopupMenu)((javax.swing.JMenuItem)evt.getSource()).getParent()).getInvoker();
            JComponent sourceControl = (JComponent)invoker;
            
            int selectedIndexToEdit = listSelectionModel.getMinSelectionIndex();
            if(selectedIndexToEdit == -1) {
                throw new ApplicationException("No se ha seleccionado ningun valor.");                
            } else {
                Object entity = listModel.getElementAt(selectedIndexToEdit);
                Class entityClass = entity.getClass();  // (Class)sourceControl.getClientProperty("org.doe4ejb3.entityClass");
                DomainObjectExplorer.getInstance().getWindowManager().openInternalFrameEntityEditor(puName, entityClass, entity);
            }
            
        } catch(ApplicationException ex) {
            
            DomainObjectExplorer.getInstance().getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch(Exception ex) {
            
            DomainObjectExplorer.getInstance().getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            
        }
    }//GEN-LAST:event_jMenuItemContextualEditActionPerformed

    private void jMenuItemContextualNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemContextualNewActionPerformed
        try {
            DomainObjectExplorer.getInstance().getWindowManager().showStatus("");
            
            Object invoker = ((javax.swing.JPopupMenu)((javax.swing.JMenuItem)evt.getSource()).getParent()).getInvoker();
            JComponent sourceControl = (JComponent)invoker;
            Class entityClass = (Class)sourceControl.getClientProperty("org.doe4ejb3.entityClass");
            DomainObjectExplorer.getInstance().getWindowManager().openInternalFrameEntityEditor(puName, entityClass, null);
            
        } catch(ApplicationException ex) {
            
            DomainObjectExplorer.getInstance().getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch(Exception ex) {
            
            DomainObjectExplorer.getInstance().getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            
        }
    }//GEN-LAST:event_jMenuItemContextualNewActionPerformed

    
    private void jComboBoxNamedQueryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxNamedQueryItemStateChanged
        listModel.clear();
        jPanelQueryParams.removeAll();

        if(jComboBoxNamedQuery.getSelectedIndex() <= 0) {
            jPanelQueryParams.setVisible(false);
            jPanelResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,0,0,0));
        } else if (jComboBoxNamedQuery.getSelectedIndex() == 1) {
            customQueryEditor = new CustomQueryEditorImpl(this, entityClass);
            jPanelQueryParams.setLayout(new java.awt.BorderLayout());
            jPanelQueryParams.add(customQueryEditor, java.awt.BorderLayout.CENTER);
            jPanelQueryParams.setVisible(true);
            jPanelResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));

        } else {
            DomainObjectExplorer.getInstance().getWindowManager().showStatus("Searching parameter types...");
            
            ListItem listItem = (ListItem)jComboBoxNamedQuery.getSelectedItem();
            NamedQuery namedQuery = (NamedQuery)listItem.getValue();
            String ejbql = namedQuery.query();
            
            try {
                queryParametersPanel = null;
            
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                HashMap ejbqlParameterTypes = EJBQLUtils.parseEJBQLParameterTypes(puName, ejbql);
                // TODO: create controls for query parameters (depending on its type)
                
                if(ejbqlParameterTypes.size() == 0) {
                    jPanelQueryParams.setVisible(false);
                    jPanelResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,0,0,0));                    
                } else {
                    queryParametersPanel = new QueryParametersEditorImpl(this, ejbqlParameterTypes);
                    jPanelQueryParams.setLayout(new java.awt.BorderLayout());
                    jPanelQueryParams.add(queryParametersPanel, java.awt.BorderLayout.CENTER);
                    jPanelQueryParams.setVisible(true);
                    jPanelResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
                }
                
            } catch(Exception ex) {
                jPanelQueryParams.setLayout(new FlowLayout());
                jPanelQueryParams.add(new JLabel("Unknown parameter types"));
                jPanelQueryParams.setVisible(true);
                jPanelResults.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
            } finally {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            
        }
        
        DomainObjectExplorer.getInstance().getWindowManager().showStatus("done");            
        revalidate();

    }//GEN-LAST:event_jComboBoxNamedQueryItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JComboBox jComboBoxNamedQuery;
    private javax.swing.JLabel jLabelQuery;
    private javax.swing.JMenuItem jMenuItemContextualDelete;
    private javax.swing.JMenuItem jMenuItemContextualEdit;
    private javax.swing.JMenuItem jMenuItemContextualNew;
    private javax.swing.JSeparator jMenuItemContextualSeparator1;
    private javax.swing.JPanel jPanelEntidad;
    private javax.swing.JPanel jPanelQueryParams;
    private javax.swing.JPanel jPanelResults;
    private javax.swing.JPopupMenu jPopupMenuContextual;
    // End of variables declaration//GEN-END:variables


    public String getPersistenceUnitName()
    {
        return puName;
    }
    
    @org.jdesktop.application.Action
    public org.jdesktop.application.Task searchInstances()
    {
        return new org.jdesktop.application.Task<List,Void>(Application.getInstance()) 
        {
            @Override 
            protected List doInBackground() throws Exception
            {
                List entities = null;
                try {
                    EntityManagerPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DomainObjectExplorer.getInstance().getWindowManager().showStatus("Searching...");
                    if(jComboBoxNamedQuery.getSelectedIndex() == 0) {   // ALL
                        entities = JPAUtils.findAllEntities(DomainObjectExplorer.getInstance().getConnectionParams(), puName, entityClass);

                    } else if(jComboBoxNamedQuery.getSelectedIndex() == 1) {   // Custom query editor
                        String ejbql = customQueryEditor.getEJBQL();
                        HashMap parameterValues = customQueryEditor.getParameterValues();
                        entities = JPAUtils.executeQuery(DomainObjectExplorer.getInstance().getConnectionParams(), puName, ejbql, parameterValues);

                    } else {  // NamedQuery: jComboBoxNamedQuery.getSelectedIndex() > 1
                        ListItem listItem = (ListItem)jComboBoxNamedQuery.getSelectedItem();
                        NamedQuery namedQuery = (NamedQuery)listItem.getValue();
                        HashMap parameterValues = null;
                        if(queryParametersPanel != null) parameterValues = queryParametersPanel.getParameterValues();
                        entities = JPAUtils.executeNamedQuery(DomainObjectExplorer.getInstance().getConnectionParams(), puName, namedQuery.name(), parameterValues);
                    }

                } finally {
                    EntityManagerPane.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }    

                return entities;
            }

            @Override
            protected void cancelled() 
            {
                setMessage("Search cancelled.");
            }        

            @Override
            protected void succeeded(List result) 
            {
                int count = 0;
                if(result != null) {
                    //JTable table = (JTable)entityListEditor.getClientProperty("table");
                    //table.setIgnoreRepaint(true);
                    listModel.clear();
                    if (result.size() > 0) {
                        for (java.lang.Object obj : result) {
                            listModel.addElement(obj);
                            count++;
                        }
                    }
                    //table.setIgnoreRepaint(false);
                    //table.repaint();
                }

                setMessage(MessageFormat.format("Search done: {0} entities found", count));
            }


            @Override
            protected void interrupted(InterruptedException ex) 
            {
                setMessage("Search interrupted: " + ex.getMessage());
                ex.printStackTrace();
            }

            @Override
            protected void failed(Throwable cause) 
            {
                setMessage("Error: " + cause.getClass().getName() + ":" + getMessage());
                cause.printStackTrace();
            }         
        };
    }

    
    public JComponent getCustomEditorLayout() {
        return null;
    }
    
    public JComponent getComponentFromEditorLayout(Class componentType, String componentName) {
        if(componentName.equalsIgnoreCase("params")) {
            return jPanelQueryParams;
        } else if(componentName.equalsIgnoreCase("results")) {
            return jPanelResults;
        }
        return null;
    }

}
