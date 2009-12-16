/*
 * Application.java
 *
 * Created on 9 / september / 2007
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import org.doe4ejb3.util.I18n;
import java.awt.Window;
import java.util.EventObject;
import javax.swing.JOptionPane;

import org.jdesktop.application.SingleFrameApplication;
import org.doe4ejb3.util.DOEUtils;


// Note: ProxyActions only works when values are defined in actionmaps of the focused component
// (but doesn't execute actions defined hierarchically in actionmaps of parents containers)
// @org.jdesktop.application.ProxyActions(value={"save", "print"})
public class Application extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
       setMainFrame(DomainObjectExplorer.getInstance());
       addExitListener(new ExitListener() {
            public boolean canExit(EventObject e) {
                int option = DOEUtils.getWindowManager().showConfirmDialog( I18n.getLiteral("exitDialog.message"), I18n.getLiteral("exitDialog.title"), JOptionPane.YES_NO_OPTION) ;
                return option == JOptionPane.YES_OPTION;
            }
          
            public void willExit(EventObject event) { 
            }
       });
       show(getMainFrame());
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(Window root) {
    }

    
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(Application.class, args);
    }
}
