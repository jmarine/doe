package org.doe4ejb3.netbeans.navigator;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows DoeNavigator component.
 */
public class DoeNavigatorAction extends AbstractAction {
    
    public DoeNavigatorAction() {
        super(NbBundle.getMessage(DoeNavigatorAction.class, "CTL_DoeNavigatorAction"));
        //        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(DoeNavigatorTopComponent.ICON_PATH, true)));
    }
    
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = DoeNavigatorTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
    
}
