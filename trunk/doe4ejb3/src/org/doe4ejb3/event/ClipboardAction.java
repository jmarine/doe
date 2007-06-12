/**
 * ClipboardAction.java
 *
 * Created on 20 / november / 2006, 21:01
 * @author Jordi Marine Fort
 */
package org.doe4ejb3.event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import org.doe4ejb3.gui.DomainObjectExplorer;


public class ClipboardAction extends AbstractAction
{
    public ClipboardAction(String name, char mnemonic, Action clipboardAction, javax.swing.Icon icon) 
    {
        putValue(NAME, name);
        putValue(MNEMONIC_KEY, new Integer((int)mnemonic));
        putValue(ACTION_COMMAND_KEY, clipboardAction.getValue(NAME));
        putValue(SMALL_ICON, icon);
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
