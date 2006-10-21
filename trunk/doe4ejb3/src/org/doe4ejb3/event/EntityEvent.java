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

    private Object entity;
    private int eventType;
    
    
    /** Creates a new instance of EntityEvent */
    public EntityEvent(Object source, int eventType, Object entity) {
        super(source);
        this.entity = entity;
        this.eventType = eventType;
    }
    
    public Object getEntity() {
        return this.entity;
    }
    
    public int getEventType() {
        return eventType;
    }
          
}
