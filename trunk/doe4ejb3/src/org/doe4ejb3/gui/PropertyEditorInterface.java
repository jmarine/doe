/*
 * PropertyEditorInterface.java
 * 
 * Created on Jul 14, 2007, 9:21:37 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import javax.swing.JComponent;


public interface PropertyEditorInterface 
{
    void setValue(Object value) throws Exception;
    Object getValue();
    
    JComponent getJComponent();
}
