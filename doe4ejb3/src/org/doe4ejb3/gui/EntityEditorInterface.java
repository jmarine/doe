/*
 * EntityEditorInterface.java
 *
 * Created on 18 August 2006, 20:23
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.lang.reflect.InvocationTargetException;


/**
 * Entity editors must also be a java.awt.Component
 *
 * @author Jordi Marine Fort
 */
public interface EntityEditorInterface {
    
    Object getEntity() throws IllegalAccessException, InvocationTargetException;
    
    void setEntity(Object obj);
    
    void newEntity(Class entityClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;
    
}
