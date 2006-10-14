/*
 * JOutlinePane.java
 *
 * Created on 4 / juny / 2005, 18:31
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.doe4ejb3.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ButtonGroup;



/**
 *
 * @author jordi
 */
public class JOutlinePane extends JComponent implements ActionListener {
    
    private ButtonGroup buttonGroup;
    private JPanel outlineInternalPane;
    
    /** Creates a new instance of JOutlinePane */
    public JOutlinePane()
    {
        buttonGroup = new ButtonGroup();
        outlineInternalPane = new JPanel();
        outlineInternalPane.setAlignmentY(0.0f);
        outlineInternalPane.setLayout(new GridBagLayout());

    }
    
    public void addTabPagesFromJTabbedPane(JTabbedPane tabs)
    {
        if(tabs != null) {
            while(tabs.getTabCount() > 0) {
                addTab(tabs.getTitleAt(0), tabs.getComponentAt(0));
            }
        }
    }
    
    public void addTab(String tabName, Component panel)
    {
        panel = new JScrollPane(panel);
        JButton button = new JButton(tabName);
        button.addActionListener(this);
        buttonGroup.add(button);
        if(buttonGroup.getSelection() == null) {
            button.setSelected(true);
        }
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        outlineInternalPane.add(button, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        outlineInternalPane.add(panel, gbc);
        panel.setVisible(outlineInternalPane.getComponentCount() <= 2);  // Expands 1st panel, and collapse other ones

        setLayout(new java.awt.BorderLayout());         // OVERRIDE IDE LAYOUT
        add(outlineInternalPane, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    public void removeTab(Component panel)
    {
        for(int i = 1; i < outlineInternalPane.getComponentCount(); i = i + 2) {
            JScrollPane component = (JScrollPane)outlineInternalPane.getComponent(i);
            if(component.getViewport() == panel.getParent()) {
                // remove tab:
                boolean selectedTabRemoved = panel.isVisible();
                outlineInternalPane.remove(i);
                outlineInternalPane.remove(i-1);
                
                // if selected tab is removed, then select first tab:
                if( (selectedTabRemoved) && (outlineInternalPane.getComponentCount() > 1) ) {
                    ((AbstractButton)outlineInternalPane.getComponent(0)).setSelected(true);
                    outlineInternalPane.getComponent(1).setVisible(true);
                }
                
                // recalculate layout:
                revalidate();
                repaint();
                
                break;
            }
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Component button = (Component)evt.getSource();
        for(int i = 0; i < outlineInternalPane.getComponentCount(); i++) {
            Component component1 = outlineInternalPane.getComponent(i);
            if(component1 instanceof JButton) {
                Component component2 = outlineInternalPane.getComponent(i+1);
                if(component1 == button) {
                    component2.setVisible(true);
                } else {
                    component2.setVisible(false);
                }
            }
            i++;
        }
        revalidate();
        repaint();
    }
    
    
    
}

