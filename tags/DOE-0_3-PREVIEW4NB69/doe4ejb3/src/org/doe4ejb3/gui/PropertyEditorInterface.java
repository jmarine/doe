/*
 * PropertyEditorInterface.java
 * 
 * Created on Jul 14, 2007, 9:21:37 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.beans.PropertyChangeListener;
import javax.swing.JComponent;


public interface PropertyEditorInterface 
{
    /**
     * Get an component UI instance to edit the property
     */
    JComponent getJComponent();

    /**
     * Dimension of component
     */
    void setDimension(java.awt.Dimension dim);

    
    /** 
     * Set value in the editor UI of the property
     * Important: value changes should be notified to registered PropertyChangeListeners 
     */
    void setValue(Object value) throws Exception;

    /** 
     * Get the edited value of the editor UI
     */
    Object getValue();
    
    
    /**
     * Register a listener of value changes (needed for jsr295 to bind the edited values back to entity object)
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Unregister a listener of value changes
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
