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


public class EditorValidationErrorPopup extends JDialog implements KeyListener, ActionListener
{
    private JLabel messageLabel;
    private JButton image;
    private Point point;
    private Dimension cDim;
    private Color color;
    private Color oldColor;
    private JComponent c;
        
    private EditorValidationErrorPopup(Frame parent, JComponent c, String message) {
        super (parent, true);

        this.c = c;
        oldColor = c.getBackground();
        color = new Color(243, 255, 159);
        messageLabel = new JLabel(message + " ");
        image = new JButton(new javax.swing.ImageIcon(getClass().getResource("/org/doe4ejb3/gui/resources/cancel.png")));
        image.setContentAreaFilled(false);
        image.setMargin(new java.awt.Insets(0, 0, 0, 0));
        image.setBorderPainted(false);
        image.addActionListener(this);
        c.addKeyListener(this);

        getContentPane().setLayout(new FlowLayout());
        setUndecorated(true);
        getContentPane().setBackground(color);
        getContentPane().add(image);
        getContentPane().add(messageLabel);
        setFocusableWindowState(false);
        
        c.setBackground(Color.PINK);
        setSize(0, 0);
        setLocationRelativeTo(c);
        point = getLocation();
        cDim = c.getSize();
        setLocation(point.x-(int)cDim.getWidth()/2,
                          point.y+(int)cDim.getHeight()/2);     
        
        
        pack();
    }
    
    public static void showErrorPopup(Frame parent, JComponent c, String message) {
        EditorValidationErrorPopup popup = new EditorValidationErrorPopup(parent, c, message);
        popup.setVisible(true);
    }
        
        
        
    /**
     * Changes the message that appears in the popup help tip when a component's
     * data is invalid. Subclasses can use this to provide context sensitive help
     * depending on what the user did wrong.
     * 
     * @param message
     */
        
    protected void setMessage(String message) {
       messageLabel.setText(message);
    }
        
    
    
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
        
    
    public void keyPressed(KeyEvent e) {
        c.setBackground(oldColor);
        if(isVisible()) setVisible(false);        
    }
        
    public void keyTyped(KeyEvent e) {}
        

    public void keyReleased(KeyEvent e) {}


}
