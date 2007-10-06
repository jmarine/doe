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
import javax.swing.SwingUtilities;

import org.doe4ejb3.util.DOEUtils;


public class JAutoScrollPaneOnComponentFocus extends JScrollPane implements ContainerListener, FocusListener
{
    private static final int PADY = 3;
    
    /**
     * Creates a new instance of JAutoScrollPaneOnComponentFocus
     */
    public JAutoScrollPaneOnComponentFocus(Container container, int vsbPolicy, int hsbPolicy) 
    {
        super(container, vsbPolicy, hsbPolicy);
        addFocusListeners(container);
    }

    
    public void componentAdded(ContainerEvent event) 
    {
        Component component = event.getComponent();
        addFocusListeners(component);
    }

    
    public void componentRemoved(ContainerEvent event) 
    {
        Component component = event.getComponent();
        removeFocusListeners(component);
    }
    
    
    private void addFocusListeners(Component component)
    {
        component.addFocusListener(this);
        if(component instanceof Container) {
            Container container = (Container)component;
            container.addContainerListener(this);
            for(Component contained : container.getComponents()) {
                addFocusListeners(contained);
            }
        }
    }

    
    private void removeFocusListeners(Component component)
    {
        component.removeFocusListener(this);
        if(component instanceof Container) {
            Container container = (Container)component;
            container.removeContainerListener(this);
            for(Component contained : container.getComponents()) {
                removeFocusListeners(contained);
            }
        }
    }    
    

    public void focusGained(FocusEvent event) 
    {
        Component c    = (Component)event.getSource();
        JViewport vp   = this.getViewport();
        Point     vpp  = vp.getViewPosition();
        Component view = getViewport().getView();

        Rectangle rect = c.getBounds();
        for(Component tmp = c.getParent(); tmp != view; tmp = tmp.getParent()) {
            Point p = tmp.getLocation();
            rect.x = rect.x + p.x;
            rect.y = rect.y + p.y;
        }
        
        // vpp.x = 0;
        if(vpp.y > rect.y) {
            vpp.y = rect.y - PADY;
        } else if(vpp.y+vp.getExtentSize().height < rect.y+rect.height+PADY) {
            vpp.y = rect.y + rect.height - vp.getExtentSize().height + PADY;                
        }
        
        this.getViewport().setViewPosition(vpp);
    }

    
    public void focusLost(FocusEvent e) { }
    
}
