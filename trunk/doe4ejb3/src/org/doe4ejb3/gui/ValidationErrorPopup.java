/**
 * EditorValidationErrorPopup.java
 * 
 * This class is based on org.javalobby.validation.AbstractValidator,
 * developed by Michael Urban (Beta 1)
 */
package org.doe4ejb3.gui;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.doe4ejb3.util.DOEUtils;


public class ValidationErrorPopup implements ActionListener
{
    private static final Color POPUP_COLOR = new Color(243, 255, 159);

    private JDialog popup;
    
    
    private JDialog getPopup()
    {
        if(popup == null) {
            popup = new JDialog(DOEUtils.getWindowManager().getMainWindow(), true);
        }
        return popup;
    }
    
    
    public static void showErrorPopup(JComponent c, String message) {
        ValidationErrorPopup popupListener = new ValidationErrorPopup();
	Color color = c.getBackground();
        c.setBackground(Color.PINK);
        c.requestFocus();  // move scroll for component visibility
        
        JLabel messageLabel = new JLabel(message);

        JButton image = new JButton(new javax.swing.ImageIcon(ValidationErrorPopup.class.getResource("/org/doe4ejb3/gui/resources/cancel.png")));
        image.setContentAreaFilled(false);
        image.setMargin(new java.awt.Insets(0, 0, 0, 0));
        image.setBorderPainted(false);
        image.addActionListener(popupListener);

        JPanel container = new JPanel();
        container.setLayout(new FlowLayout());
        container.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK,1));
        container.setBackground(POPUP_COLOR);
        container.add(image);
        container.add(messageLabel);

        JDialog popup = popupListener.getPopup();
        popup.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
        popup.setUndecorated(true);
        popup.getContentPane().setBackground(POPUP_COLOR);
        popup.getContentPane().add(container);
        popup.setFocusableWindowState(false);


        popup.setSize(0, 0);
        popup.setLocationRelativeTo(c);
        Point point = popup.getLocation();
        Dimension cDim = c.getSize();
        popup.setLocation(point.x-(int)cDim.getWidth()/2,
                          point.y+(int)cDim.getHeight()/2);
        
        popup.pack();
        popup.setVisible(true);
        
        c.setBackground(color);

    }
        
    
    public void actionPerformed(ActionEvent e) {
        popup.setVisible(false);
    }

}
