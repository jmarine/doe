/*
 * DOEUtils.java
 * 
 * Created on Sep 16, 2007, 1:19:36 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.util;

import java.awt.BorderLayout;
import java.awt.Container;


import org.doe4ejb3.gui.WindowManager;
import org.doe4ejb3.gui.EntityClassListCellRenderer;
import org.doe4ejb3.gui.EntityManagerPane;
import org.doe4ejb3.gui.EntityEditorView;


public class DOEUtils
{
    public static final Object APPLICATION_WINDOW = null;


    private static WindowManager windowManager = null;


    public static void setWindowManager(WindowManager wm)
    {
        windowManager = wm;
    }
    
    public static WindowManager getWindowManager()
    {
        if(windowManager == null) throw new RuntimeException("Window Manager not configured");
        return windowManager;
    }    
    
    


    
    
    public static void openInternalFrameEntityManager(String puName, Class entityClass) throws Exception
    {
        Object key = entityClass.getName() + "Manager";
        Container window = (Container)windowManager.findWindow(key);
        if(window != null) {
            windowManager.bringToFront(window);
        } else {
            String title = org.doe4ejb3.gui.I18n.getEntityName(entityClass) + " manager";
            window = (Container)windowManager.createWindow(key, title, EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass));
            window.setLayout(new BorderLayout());
            window.add(new EntityManagerPane(puName, entityClass), BorderLayout.CENTER);

            windowManager.showWindow(window, false);
        }

    }


    public static Object openInternalFrameEntityEditor(String puName, Class entityClass, Object entity) throws Exception
    {
        Object key = (entity != null) ? entity : entityClass.getName() + "Editor";
        Container window = (Container)windowManager.findWindow(key);

        if(window != null) {
            windowManager.bringToFront(window);
        } else {
            String title = org.doe4ejb3.gui.I18n.getEntityName(entityClass);
            if(entity == null) title = org.doe4ejb3.gui.I18n.getLiteral("New") + " " + title.toLowerCase();
            else title = org.doe4ejb3.gui.I18n.getLiteral("Edit") + " " + title + ": " + entity.toString();
            
            window = (Container)windowManager.createWindow(key, title, EntityClassListCellRenderer.getInstance().getEntityIcon(entityClass));
            window.setLayout(new BorderLayout());            
            window.add(new EntityEditorView(puName, entityClass, entity), BorderLayout.CENTER);

            windowManager.showWindow(window, false);
        }

        return window;
    }
    
}
