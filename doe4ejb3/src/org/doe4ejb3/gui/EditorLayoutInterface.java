/*
 * EntityLayoutInterface.java
 * 
 * Created on Aug 26, 2007, 8:29:21 PM
 * @author Jordi Mariné Fort
 */

package org.doe4ejb3.gui;

import javax.swing.JComponent;

        
public interface EditorLayoutInterface 
{
 
    JComponent getCustomEditorLayout();
    
    JComponent getComponentFromEditorLayout(Class componentType, String componentName);

}
