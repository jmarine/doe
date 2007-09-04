/*
 * JComboBoxJSR295.java
 * 
 * Created on Aug 8, 2007, 4:30:29 PM
 * @author Jordi Marine Fort
 */
package org.doe4ejb3.gui;

import javax.swing.DefaultComboBoxModel;


public class JComboBoxJSR295 extends javax.swing.JComboBox 
{
    public JComboBoxJSR295(DefaultComboBoxModel model) 
    {
        super(model);
    }
    
    @Override
    public Object getSelectedItem() 
    {
        return super.getSelectedItem();
    }
    
    @Override
    public void setSelectedItem(Object newItem) 
    {
        Object oldItem = super.getSelectedItem();
        super.setSelectedItem(newItem);
        super.firePropertyChange("selectedItem", oldItem, newItem);  // to bind the edited values back to entity object
    }

}
