/*
 * ConnectionManager.java
 *
 * Created on January 27, 2007, 8:38 PM
 */

package org.doe4ejb3.gui;
import java.util.HashMap;

/**
 *
 * @author  jordi
 */
public class ConnectionManager extends javax.swing.JInternalFrame {
    private HashMap<String,String> connectionParams = null;
    private String username = "";
    private String password = "";

    
    /** Creates new form ConnectionManager */
    public ConnectionManager() {
        this(new HashMap<String,String>());
    }
    public ConnectionManager(HashMap<String,String> connectionParams) {
        initComponents();
        this.connectionParams = connectionParams;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabelJPAimpl = new javax.swing.JLabel();
        jComboBoxJPAimpl = new javax.swing.JComboBox();
        jLabelUsername = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jButtonAccept = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setClosable(true);
        setTitle("Connection properties");
        jLabelJPAimpl.setLabelFor(jComboBoxJPAimpl);
        jLabelJPAimpl.setText("JPA implementation:");

        jComboBoxJPAimpl.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TopLink Essentials" }));

        jLabelUsername.setLabelFor(jTextFieldUsername);
        jLabelUsername.setText("Username:");

        jLabelPassword.setLabelFor(jPasswordField);
        jLabelPassword.setText("Password:");

        jButtonAccept.setText("Accept");
        jButtonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcceptActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelJPAimpl)
                            .addComponent(jLabelUsername)
                            .addComponent(jLabelPassword))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPasswordField)
                            .addComponent(jTextFieldUsername)
                            .addComponent(jComboBoxJPAimpl, 0, 167, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAccept)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelJPAimpl)
                    .addComponent(jComboBoxJPAimpl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsername)
                    .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPassword)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAccept)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        jTextFieldUsername.setText(username);
        jPasswordField.setText(password);
        close();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAcceptActionPerformed
        username = jTextFieldUsername.getText();
        password = new String(jPasswordField.getPassword());
        if(jComboBoxJPAimpl.getSelectedIndex() == 0) {
            // Setup TopLink properties:
            if( (username != null) && (username.length() > 0) ) {
                connectionParams.put(oracle.toplink.essentials.config.TopLinkProperties.JDBC_USER, username);
                if( (password != null) && (password.length() > 0) ) connectionParams.put(oracle.toplink.essentials.config.TopLinkProperties.JDBC_PASSWORD, password);
                else connectionParams.remove(oracle.toplink.essentials.config.TopLinkProperties.JDBC_PASSWORD);
            } else {
                connectionParams.remove(oracle.toplink.essentials.config.TopLinkProperties.JDBC_USER);
                connectionParams.remove(oracle.toplink.essentials.config.TopLinkProperties.JDBC_PASSWORD);
            }
        }
        close();
    }//GEN-LAST:event_jButtonAcceptActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccept;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JComboBox jComboBoxJPAimpl;
    private javax.swing.JLabel jLabelJPAimpl;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables

    
    public HashMap getConnectionParams()
    {
        return connectionParams;
    }
    
    private void close()
    {
        this.setVisible(false);
        this.dispose(); // remove from JDesktop' windows list
    }
    
    
}
