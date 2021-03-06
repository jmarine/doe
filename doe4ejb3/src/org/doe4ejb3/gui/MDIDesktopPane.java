/**
 * MDIDesktopPane.java
 *
 * An extension of JDesktopPane that supports often used MDI functionality. This
 * class also handles setting scroll bars for when windows move too far to the left or
 * bottom, providing the MDIDesktopPane is in a ScrollPane.
 * <p>
 * Thanks to <em>Gerald Nunn</em> for his article:
 * <a href="http://www.javaworld.com/javaworld/jw-05-2001/jw-0525-mdi.html">Conquer Swing deficiencies in MDI development</a>.
 * 
 * Author   Gerald Nunn 
 * Modified Jordi Marine Fort (2007/01/01)
 */

package org.doe4ejb3.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.ArrayList;
import java.util.Vector;


public class MDIDesktopPane extends JDesktopPane {
    private static int FRAME_POSITION=0;
    private static int FRAME_OFFSET_X=20;
    private static int FRAME_OFFSET_Y=26;
    private static int FRAME_BORDER_SIZE=2;
    private static int VERTICAL_SCROLL_SIZE=20;
    private MDIDesktopManager manager;

    public MDIDesktopPane() {
        manager=new MDIDesktopManager(this);
        setDesktopManager(manager);
        setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }

    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x,y,w,h);
        checkDesktopSize();
    }
    
    public void centerFrame(JInternalFrame frame) 
    {
        frame.setLocation( Math.max(1,(getParent().getWidth()  - frame.getWidth())/2), 
                           Math.max(1,(getParent().getHeight() - frame.getHeight())/2) );
    }
    
    public Component add(Component frame) {
        add(frame, null);
        return frame;
    }
    
    public void add(JInternalFrame frame, boolean center) {
        add((Component)frame, (Object)center);
    }

    public void add(Component comp, Object constraints) {
        if( (comp != null) && (comp instanceof JInternalFrame) ) {
            JInternalFrame frame = (JInternalFrame)comp;
            boolean center = ( (constraints != null) && (constraints == Boolean.TRUE) ) ? true : false;
            Point p = comp.getLocation();
            if(!frame.isVisible()) p = null;

            frame.pack();
            super.add(comp, constraints);
            
            if(center) {
                p = new Point( Math.max(1,(getParent().getWidth()  - frame.getWidth())/2), 
                               Math.max(1,(getParent().getHeight() - frame.getHeight())/2) );
            } else if(p == null) { 
                p = new Point( 1 + (FRAME_POSITION * FRAME_OFFSET_X) % 100,
                               1 + (FRAME_POSITION * FRAME_OFFSET_Y) % 120);
                FRAME_POSITION++;
            }
            frame.setLocation(p.x, p.y);

            if (frame.isResizable()) {
                int w = (int)frame.getSize().getWidth() + VERTICAL_SCROLL_SIZE;
                int h = (int)frame.getSize().getHeight();
                if (p.x + w + FRAME_BORDER_SIZE > getParent().getWidth()) w = getParent().getWidth() - p.x - FRAME_BORDER_SIZE;
                if (p.y + h + FRAME_BORDER_SIZE > getParent().getHeight()) h = getParent().getHeight() - p.y - FRAME_BORDER_SIZE;
                frame.setSize(w, h);
            }

            moveToFront(frame);
            frame.setVisible(true);
            try {
                frame.setSelected(true);
            } catch (PropertyVetoException e) {
                frame.toBack();
            }

            checkDesktopSize();
            
        } else {
            
            super.add(comp, constraints);
            
        }
        
    }

    public void remove(Component c) {
        super.remove(c);
        checkDesktopSize();
    }

    /**
     * Cascade all internal frames
     */
    public void cascadeFrames() {
        int x = 0;
        int y = 0;
        JInternalFrame allFrames[] = getAllFrames();

        manager.setNormalSize();
        int frameHeight = (getBounds().height - 5) - allFrames.length * FRAME_OFFSET_Y;
        int frameWidth = (getBounds().width - 5) - allFrames.length * FRAME_OFFSET_X;
        for (int i = allFrames.length - 1; i >= 0; i--) {
            try { 
                ((JInternalFrame)allFrames[i]).setIcon(false);
                allFrames[i].setSize(frameWidth,frameHeight);
                allFrames[i].setLocation(x,y);
                x = x + FRAME_OFFSET_X;
                y = y + FRAME_OFFSET_Y;
            } catch(Exception ex) { }
        }
    }

    /**
     * Tile all internal frames
     */
    public void tileFrames() {
        java.awt.Component allFrames[] = getAllFrames();
        manager.setNormalSize();
        int frameHeight = getBounds().height/allFrames.length;
        int y = 0;
        for (int i = 0; i < allFrames.length; i++) {
            try {
                ((JInternalFrame)allFrames[i]).setIcon(false);
                allFrames[i].setSize(getBounds().width,frameHeight);
                allFrames[i].setLocation(0,y);
                y = y + frameHeight;
            } catch(Exception ex) { }
        }
    }

    /**
     * Sets all component size properties ( maximum, minimum, preferred)
     * to the given dimension.
     */
    public void setAllSize(Dimension d){
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
    }

    /**
     * Sets all component size properties ( maximum, minimum, preferred)
     * to the given width and height.
     */
    public void setAllSize(int width, int height){
        setAllSize(new Dimension(width,height));
    }

    private void checkDesktopSize() {
        if (getParent()!=null&&isVisible()) manager.resizeDesktop();
    }
    
}

/**
 * Private class used to replace the standard DesktopManager for JDesktopPane.
 * Used to provide scrollbar functionality.
 */
class MDIDesktopManager extends DefaultDesktopManager {
    private MDIDesktopPane desktop;

    public MDIDesktopManager(MDIDesktopPane desktop) {
        this.desktop = desktop;
    }

    public void activateFrame(javax.swing.JInternalFrame iFrame)
    {
        super.activateFrame(iFrame);
        desktop.firePropertyChange("mdiFrameActive", false, true);
    }

    public void deactivateFrame(javax.swing.JInternalFrame iFrame)
    {
        super.deactivateFrame(iFrame);
        desktop.firePropertyChange("mdiFrameActive", true, false);
    }

    public void endResizingFrame(JComponent f) {
        super.endResizingFrame(f);
        resizeDesktop();
    }

    public void endDraggingFrame(JComponent f) {
        super.endDraggingFrame(f);
        resizeDesktop();
    }

    public void setNormalSize() {
        JScrollPane scrollPane=getScrollPane();
        int x = 0;
        int y = 0;
        Insets scrollInsets = getScrollPaneInsets();

        if (scrollPane != null) {
            Dimension d = scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
               d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right,
                         d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }

            d.setSize(d.getWidth() - 20, d.getHeight() - 20);
            desktop.setAllSize(x,y);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }

    private Insets getScrollPaneInsets() {
        JScrollPane scrollPane=getScrollPane();
        if (scrollPane==null) return new Insets(0,0,0,0);
        else return getScrollPane().getBorder().getBorderInsets(scrollPane);
    }

    private JScrollPane getScrollPane() {
        if (desktop.getParent() instanceof JViewport) {
            JViewport viewPort = (JViewport)desktop.getParent();
            if (viewPort.getParent() instanceof JScrollPane)
                return (JScrollPane)viewPort.getParent();
        }
        return null;
    }

    protected void resizeDesktop() {
        int x = 0;
        int y = 0;
        JScrollPane scrollPane = getScrollPane();
        Insets scrollInsets = getScrollPaneInsets();

        if (scrollPane != null) {
            JInternalFrame allFrames[] = desktop.getAllFrames();
            for (int i = 0; i < allFrames.length; i++) {
                if (allFrames[i].getX()+allFrames[i].getWidth()>x) {
                    x = allFrames[i].getX() + allFrames[i].getWidth();
                }
                if (allFrames[i].getY()+allFrames[i].getHeight()>y) {
                    y = allFrames[i].getY() + allFrames[i].getHeight();
                }
            }
            Dimension d=scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
               d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right,
                         d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }

            if (x <= d.getWidth()) x = ((int)d.getWidth()) - 20;
            if (y <= d.getHeight()) y = ((int)d.getHeight()) - 20;
            desktop.setAllSize(x,y);
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }
}