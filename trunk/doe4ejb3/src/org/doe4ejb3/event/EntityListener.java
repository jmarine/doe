/*
 * EntityListener.java
 *
 * Created on 15 October 2006, 18:55
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.event;


public interface EntityListener extends java.util.EventListener 
{
    public void entityChanged(EntityEvent evt);
    
}
