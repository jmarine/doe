/*
 * EntityClassListCellRenderer.java
 *
 * Created on 14 October 2006, 12:37
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import org.doe4ejb3.util.I18n;
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
        
        //retValue.setHorizontalAlignment(SwingConstants.CENTER);
        //retValue.setVerticalAlignment(SwingConstants.CENTER);
        //retValue.setHorizontalTextPosition(SwingConstants.CENTER);
        //retValue.setVerticalTextPosition(SwingConstants.BOTTOM);
        //retValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        retValue.setIcon(getEntityIcon(clazz));
        retValue.setText(I18n.getEntityName(clazz));
        if(isSelected) retValue.setForeground(java.awt.Color.WHITE);  // avoid nimbus bug

        return retValue;
    }
    
    public static ImageIcon getEntityIcon(Class entityClass) {
        ImageIcon imageIcon = entityIconCache.get(entityClass);
        if(imageIcon == null) {
            EntityDescriptor entityDescriptor = (EntityDescriptor)entityClass.getAnnotation(EntityDescriptor.class);
            if( (entityDescriptor != null) && (entityDescriptor.iconPath() != null) && (entityDescriptor.iconPath().length() > 0) ) {
                try { imageIcon = new ImageIcon(entityClass.getResource(entityDescriptor.iconPath())); } 
                catch(Exception ex) { System.out.println("WARNING: there was a problem loading icon " + entityDescriptor.iconPath() + ": " + ex.getMessage()); } 
            } 
            
            if(imageIcon == null) {
                imageIcon = new ImageIcon(EntityClassListCellRenderer.class.getResource("/org/doe4ejb3/gui/resources/defaultEntityIcon.gif"));
            }

            entityIconCache.put(entityClass, imageIcon);
        }
        return imageIcon;
    }
    
    
}
