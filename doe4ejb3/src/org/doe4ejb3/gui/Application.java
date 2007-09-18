/*
 * Application.java
 *
 * Created on 9 / september / 2007
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.Window;
import java.util.EventObject;
import javax.swing.JOptionPane;

import org.jdesktop.application.SingleFrameApplication;
import org.doe4ejb3.util.DOEUtils;



public class Application extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
       setMainFrame(DomainObjectExplorer.getInstance());
       addExitListener(new ExitListener() {
            public boolean canExit(EventObject e) {
                int option = DOEUtils.getWindowManager().showConfirmDialog( 
                                "Do you really want to exit?", "Exit confirmation", JOptionPane.YES_NO_OPTION) ;
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