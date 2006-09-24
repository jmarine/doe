/*
 * JAutoScrollPaneOnComponentFocus.java
 *
 * Created on 2 September 2006, 18:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.doe4ejb3.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * JAutoScrollPaneOnComponentFocus.java
 *
 * Created on 02 September 2006, 19:20
 * @author Jordi Marine Fort
 */
public class JAutoScrollPaneOnComponentFocus extends JScrollPane implements ContainerListener, FocusListener
{
    private static final int PADY = 3;

    /**
     * Creates a new instance of JAutoScrollPaneOnComponentFocus
     */
    public JAutoScrollPaneOnComponentFocus(Container container, int vsbPolicy, int hsbPolicy) {
        super(container, vsbPolicy, hsbPolicy);
        container.addContainerListener(this);
        for(Component component : container.getComponents()) {
            component.addFocusListener(this);
        }
    }

    public void componentAdded(ContainerEvent event) {
        Component component = event.getComponent();
        component.addFocusListener(this);
    }

    public void componentRemoved(ContainerEvent event) {
        Component component = event.getComponent();
        component.removeFocusListener(this);
    }

    public void focusGained(FocusEvent event) {
       
        Component c = (Component)event.getSource();
        JViewport vp = this.getViewport();
        Point vpp = vp.getViewPosition();
        Rectangle rect = c.getBounds();
        
        vpp.x = 0;
        if(vpp.y > rect.y) {
            vpp.y = rect.y - PADY;
        } else if(vpp.y+vp.getExtentSize().height < rect.y+rect.height+PADY) {
            vpp.y = rect.y + rect.height - vp.getExtentSize().height + PADY;                
        }
        
        this.getViewport().setViewPosition(vpp);
    }

    public void focusLost(FocusEvent e) {
    }
    
}
