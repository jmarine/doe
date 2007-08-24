/*
 * EntityEditorInterface.java
 *
 * To display EJB3 entity details within a customized UI,
 * you have to develop a class that implement the "org.doe4ejb3.gui.EntityEditorInterface",
 * and configure the EJB3 entity class with EntityDescriptor annotation
 * (setting the editorClassName attribute with the name of the developed class that builds the new UI)
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
     * (it might be useful to get related entities with JPAUtils).
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
