/**
 * EditorValidationErrorPopup.java
 * 
 * This class was based on org.javalobby.validation.AbstractValidator,
 * developed by Michael Urban (Beta 1)
 */
package org.doe4ejb3.gui;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.doe4ejb3.util.DOEUtils;


public class ValidationErrorPopup
{
    private static final Color POPUP_COLOR = new Color(243, 255, 159);

    public static void showErrorPopup(final JComponent c, final String message) {
        final Color oldColor = c.getBackground();
        final JDialog popup = new JDialog(DOEUtils.getWindowManager().getMainWindow(), true);
        
        JLabel messageLabel = new JLabel(message);
        JButton image = new JButton(new javax.swing.ImageIcon(ValidationErrorPopup.class.getResource("/org/doe4ejb3/gui/resources/cancel.png")));
        image.setContentAreaFilled(false);
        image.setMargin(new java.awt.Insets(0, 0, 0, 0));
        image.setBorderPainted(false);
        image.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                popup.setVisible(false);
                c.setBackground(oldColor);
            }            
        });

        JPanel container = new JPanel();
        container.setLayout(new FlowLayout());
        container.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK,1));
        container.setBackground(POPUP_COLOR);
        container.add(image);
        container.add(messageLabel);

        popup.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
        popup.add(container);
        popup.setUndecorated(true);
        popup.setBackground(POPUP_COLOR);
        popup.setFocusableWindowState(false);        
        
        c.setBackground(Color.RED);
        c.requestFocus();                            // may move scrollbars for component visibility
        SwingUtilities.invokeLater(new Runnable() {  // so, delay to open popup on new position
            public void run() {
                popup.setSize(0, 0);
                popup.setLocationRelativeTo(c);
                Point point = popup.getLocation();
                Dimension cDim = c.getSize();
                popup.setLocation(point.x-(int)cDim.getWidth()/2,
                                  point.y+(int)cDim.getHeight()/2);

                popup.pack();
                popup.setVisible(true);
            }
        });
    }
        
}
