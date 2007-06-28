/*
 * DomainObjectExplorer.java
 */

package org.doe4ejb3.gui;

import application.ApplicationContext;
import application.SingleFrameApplication;
import java.awt.Window;

/**
 * The main class of the application.
 */
public class Application extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
       setMainFrame(DomainObjectExplorer.getInstance());
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
     * A convenient static getter for the application instance.
     * @return the instance of DomainObjectExplorer
     */
    public static Application getApplication() {
        return (Application) ApplicationContext.getInstance().getApplication();
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(Application.class, args);
    }
}