/*
 * EntityClassListCellRenderer.java
 *
 * Created on 14 October 2006, 12:37
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.Component;
import java.lang.annotation.Annotation;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;

import org.doe4ejb3.annotation.EntityDescriptor;


public class EntityClassListCellRenderer extends DefaultListCellRenderer 
{
    private static EntityClassListCellRenderer instance = new EntityClassListCellRenderer();
    private static HashMap<Class, ImageIcon> entityIconCache = new HashMap<Class,ImageIcon>();

    private EntityClassListCellRenderer() { }


    public static EntityClassListCellRenderer getInstance()
    {
        return instance;
    }
    
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel retValue = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Class clazz = (Class)value;
        
        //setHorizontalAlignment(SwingConstants.CENTER);
        //setVerticalAlignment(SwingConstants.CENTER);
        //setHorizontalTextPosition(SwingConstants.CENTER);
        //setVerticalTextPosition(SwingConstants.BOTTOM);
        
        setIcon(getEntityIcon(clazz));
        setText(I18n.getEntityName(clazz));
        return retValue;
    }
    
    public ImageIcon getEntityIcon(Class entity) {
        ImageIcon imageIcon = entityIconCache.get(entity);
        if(imageIcon == null) {
            EntityDescriptor entityDescriptor = (EntityDescriptor)entity.getAnnotation(EntityDescriptor.class);
            if( (entityDescriptor != null) && (entityDescriptor.iconPath() != null) && (entityDescriptor.iconPath().length() > 0) ) {
                try { imageIcon = new ImageIcon(entity.getResource(entityDescriptor.iconPath())); } 
                catch(Exception ex) { System.out.println("WARNING: there was a problem loading icon " + entityDescriptor.iconPath() + ": " + ex.getMessage()); } 
            } 
            
            if(imageIcon == null) {
                imageIcon = new ImageIcon(this.getClass().getResource("/META-INF/images/defaultEntityIcon.gif"));
            }

            entityIconCache.put(entity, imageIcon);
        }
        return imageIcon;
    }
    
    
}
