/*
 * ComposedEditorHolder.java
 *
 * Created on January 31, 2007, 8:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.doe4ejb3.gui;

/**
 *
 * @author jordi
 */
public class ComposedEditorHolder extends javax.swing.JPanel {
    
    private java.awt.Component componentWithValues;
    
    /** Creates a new instance of ComposedEditorHolder */
    public ComposedEditorHolder(java.awt.Component componentWithValues) {
        this.componentWithValues = componentWithValues;
    }
    
    public java.awt.Component getComponentWithValues()
    {
        return componentWithValues;
    }
    
}
