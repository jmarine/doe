/*
 * TransferActionListener.java is used by the 1.4
 * DragPictureDemo.java example.
 */
package org.doe4ejb3.event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import org.doe4ejb3.gui.DomainObjectExplorer;

/*
 * A class that tracks the focused component.  This is necessary
 * to delegate the menu cut/copy/paste commands to the right
 * component.  An instance of this class is listening and
 * when the user fires one of these commands, it calls the
 * appropriate action on the currently focused component.
 */
public class ClipboardAction extends AbstractAction
{
    public ClipboardAction(String name, char mnemonic, Action clipboardAction) {
        putValue(NAME, name);
        putValue(MNEMONIC_KEY, new Integer((int)mnemonic));
        putValue(ACTION_COMMAND_KEY, clipboardAction.getValue(NAME));
    }
    
    public void actionPerformed(ActionEvent event) {
        Component target = (JComponent)event.getSource();
 
        // This tangled mess determines which component the action has to occur on.
        if (target instanceof JMenuItem)
        {
            Component invoker = ((JPopupMenu) target.getParent()).getInvoker();
            if (invoker instanceof JMenu)
            {
                // Container topLevel = ((JMenu) invoker).getTopLevelAncestor();
                // target = ((Window) topLevel).getFocusOwner();                
                JComponent focusOwner = (JComponent)KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
                if (focusOwner != null)
                {
                    target = focusOwner;
                }
                else
                {
                    throw new IllegalStateException("The top-level component was not a window");
                }
            }
            else
            {
                target = invoker;
            }
        }        
        else
        {
            throw new IllegalStateException("The source was not a menu item");
        }
 
        DomainObjectExplorer.getInstance().clipboardActionPerformed((JComponent)target, event);
    }
    
}
