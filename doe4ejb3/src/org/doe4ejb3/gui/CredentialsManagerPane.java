/**
 * CredentialsManagerPane.java
 *
 * Created on January 27, 2007, 8:38 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import org.doe4ejb3.util.DOEUtils;
import org.doe4ejb3.util.JPAUtils;
import org.jdesktop.application.Action;


public class CredentialsManagerPane extends javax.swing.JPanel
{
    /** Creates new form CredentialsManagerPane */
    public CredentialsManagerPane() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelPersistenceUnit = new javax.swing.JLabel();
        jComboBoxPersistenceUnit = new javax.swing.JComboBox();
        jLabelUsername = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jButtonAccept = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(CredentialsManagerPane.class);
        jLabelPersistenceUnit.setText(resourceMap.getString("jLabelPersistenceUnit.text")); // NOI18N

        jComboBoxPersistenceUnit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { org.doe4ejb3.util.I18n.getLiteral(CredentialsManagerPane.class, "msg.anyPersistenceUnit"), "", "", "", "" }));
        jComboBoxPersistenceUnit.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxPersistenceUnitPopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBoxPersistenceUnit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxPersistenceUnitItemStateChanged(evt);
            }
        });

        jLabelUsername.setLabelFor(jTextFieldUsername);
        jLabelUsername.setText(resourceMap.getString("jLabelUsername.text")); // NOI18N

        jLabelPassword.setLabelFor(jPasswordField);
        jLabelPassword.setText(resourceMap.getString("jLabelPassword.text")); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(CredentialsManagerPane.class, this);
        jButtonAccept.setAction(actionMap.get("accept")); // NOI18N
        jButtonAccept.setMnemonic('a');

        jButtonCancel.setAction(actionMap.get("cancel")); // NOI18N
        jButtonCancel.setMnemonic('c');

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jButtonAccept)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonCancel))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelUsername)
                            .add(jLabelPassword)
                            .add(jLabelPersistenceUnit))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jPasswordField)
                            .add(jTextFieldUsername)
                            .add(jComboBoxPersistenceUnit, 0, 156, Short.MAX_VALUE))))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPersistenceUnit)
                    .add(jComboBoxPersistenceUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(7, 7, 7)
                        .add(jLabelUsername)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelPassword))
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jTextFieldUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 46, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonAccept)
                    .add(jButtonCancel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxPersistenceUnitPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxPersistenceUnitPopupMenuWillBecomeVisible

        java.util.Collection<String> puNames = null;
        try { puNames = JPAUtils.getPersistenceUnits(); }
        catch(Exception ex) { }
        
        if(puNames != null) {

            // remember current PU
            ListItem selected = null;
            if(jComboBoxPersistenceUnit.getSelectedIndex() > 0) selected = (ListItem)jComboBoxPersistenceUnit.getSelectedItem();

            // refresh persistence units:
            while(jComboBoxPersistenceUnit.getItemCount() > 1) {
                jComboBoxPersistenceUnit.removeItemAt(1);
            }
        
            for(String puName : puNames) {
                jComboBoxPersistenceUnit.addItem(new ListItem(puName, JPAUtils.getPersistenceUnitTitle(puName)));
            }
            
            // select previous PU
            if(selected != null) jComboBoxPersistenceUnit.setSelectedItem(selected);            
        }
        

    }//GEN-LAST:event_jComboBoxPersistenceUnitPopupMenuWillBecomeVisible

    private void jComboBoxPersistenceUnitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxPersistenceUnitItemStateChanged

        String puName = JPAUtils.GENERIC_PERSISTENCE_UNIT;
        if(jComboBoxPersistenceUnit.getSelectedIndex() > 0) puName = (String)((ListItem)jComboBoxPersistenceUnit.getSelectedItem()).getValue();

        String username = JPAUtils.getConnectionParam(puName, JPAUtils.GENERIC_USER_PROPERTY_NAME);
        String password = JPAUtils.getConnectionParam(puName, JPAUtils.GENERIC_PASSWORD_PROPERTY_NAME);

        if(username == null) username = "";
        if(password == null) password = "";
        
        jTextFieldUsername.setText(username);
        jPasswordField.setText(password);

    }//GEN-LAST:event_jComboBoxPersistenceUnitItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JComboBox jComboBoxPersistenceUnit;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelPersistenceUnit;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables

    
    @Action
    public void accept()
    {
        String username = jTextFieldUsername.getText();
        String password = new String(jPasswordField.getPassword());

        String puName = JPAUtils.GENERIC_PERSISTENCE_UNIT;
        if(jComboBoxPersistenceUnit.getSelectedIndex() > 0) puName = (String)((ListItem)jComboBoxPersistenceUnit.getSelectedItem()).getValue();

        JPAUtils.setConnectionParams(puName, username, password);        
        
        Object window = DOEUtils.getWindowManager().getWindowFromComponent(this);
        DOEUtils.getWindowManager().closeWindow(window);  

    }

    @Action
    public void cancel() 
    {
        jComboBoxPersistenceUnitItemStateChanged(null);
        
        Object window = DOEUtils.getWindowManager().getWindowFromComponent(this);
        DOEUtils.getWindowManager().closeWindow(window);  
    }
   
}