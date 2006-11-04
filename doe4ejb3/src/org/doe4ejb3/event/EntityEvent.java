/*
 * EntityEvent.java
 *
 * Created on 15 October 2006, 18:46
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.event;


public class EntityEvent extends java.util.EventObject 
{
    public static final int ENTITY_INSERT = 1;
    public static final int ENTITY_UPDATE = 2;
    public static final int ENTITY_DELETE = 3;

    private int    eventType;
    private Object oldEntity;
    private Object newEntity;
    
    
    /** Creates a new instance of EntityEvent */
    public EntityEvent(Object source, int eventType, Object oldEntity, Object newEntity) {
        super(source);
        this.eventType = eventType;
        this.oldEntity = oldEntity;
        this.newEntity = newEntity;
    }
    
    public Object getNewEntity() {
        return this.newEntity;
    }

    public Object getOldEntity() {
        return this.oldEntity;
    }

    
    public int getEventType() {
        return eventType;
    }
          
}
