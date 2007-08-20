/*
 * EntityEditorInterface.java
 *
 * Created on 18 August 2006, 20:23
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;


public interface EntityEditorInterface 
{
    /**
     * Injected persistence unit name
     * (may be useful to get related entities with JPAUtils).
     */
    void setPersistenceUnitName(String puName);
    
    
    /**
     * Get the UI provided by the EntityEditorInterface
     */
    public JComponent getJComponent();


    /**
     * To edit an existing object
     */
    void setEntity(Object obj);

    /**
     * To edit a new object 
     */    
    void newEntity(Class entityClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;

    /**
     * Get the edited object
     */
    Object getEntity() throws IllegalAccessException, InvocationTargetException;

    /**
     * Returns true when editing a new object, o false when editing an existing object.
     */
    boolean isNew();
    
}
