/**
 * JOutlinePane.java
 *
 * Created on 4 / juny / 2005, 18:31
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.BorderLayout;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



/**
 *
 * @author jordi
 */
public class JOutlinePane extends JComponent implements ActionListener {
    
    private ButtonGroup buttonGroup;
    private JPanel outlineInternalPane;
    private int    selectedTabComponent;
    
    /** Creates a new instance of JOutlinePane */
    public JOutlinePane()
    {
        buttonGroup = new ButtonGroup();
        outlineInternalPane = new JPanel();
        outlineInternalPane.setAlignmentY(0.0f);
        outlineInternalPane.setLayout(new GridBagLayout());
        selectedTabComponent = -1;
    }

    public void addListSelectionListener(ListSelectionListener listener)
    {
        listenerList.add(ListSelectionListener.class, listener);
    }

    public void removeListSelectionListener(ListSelectionListener listener) {
        listenerList.remove(ListSelectionListener.class, listener);
    }

    public ListSelectionListener[] getListSelectionListeners() {
        return (ListSelectionListener[])listenerList.getListeners(ListSelectionListener.class);
    }

    protected void fireSelectionValueChanged()
    {
        Object[] listeners = listenerList.getListenerList();
        ListSelectionEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListSelectionListener.class) {
                if (e == null) {
                    e = new ListSelectionEvent(this, selectedTabComponent, selectedTabComponent, false);
                }
                ((ListSelectionListener)listeners[i+1]).valueChanged(e);
            }
        }
    }


    public Component getSelectedTabComponent()
    {
        if(selectedTabComponent != -1) {
            return outlineInternalPane.getComponent(selectedTabComponent);
        } else {
            return null;
        }
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
        if(selectedTabComponent == -1) {
            selectedTabComponent = 1;
            fireSelectionValueChanged();
        }

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
                if(selectedTabRemoved) {
                    if(outlineInternalPane.getComponentCount() > 1) {
                        ((AbstractButton)outlineInternalPane.getComponent(0)).setSelected(true);
                        outlineInternalPane.getComponent(1).setVisible(true);
                        selectedTabComponent = 1;
                    } else {
                        selectedTabComponent = -1;
                    }
                    fireSelectionValueChanged();
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
                    selectedTabComponent = i+1;
                    fireSelectionValueChanged();
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

