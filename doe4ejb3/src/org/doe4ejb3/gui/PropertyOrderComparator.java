/**
 * PropertyOrderComparator.java
 *
 * Created on 29 July 2006, 21:39
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.lang.reflect.Member;
import java.lang.annotation.Annotation;

import java.util.Comparator;
import org.doe4ejb3.annotation.PropertyDescriptor;
import org.doe4ejb3.binding.EntityProperty;
import org.doe4ejb3.util.ReflectionUtils;


public class PropertyOrderComparator implements Comparator<EntityProperty>
{
    
    /** Creates a new instance of OrderAttributeComparator */
    public PropertyOrderComparator() { }


    public int compare(EntityProperty p1, EntityProperty p2) 
    {
        boolean m1IsIdentity = p1.hasAnnotation(javax.persistence.Id.class);
        boolean m2IsIdentity = p2.hasAnnotation(javax.persistence.Id.class);
        
        boolean m1IsEmbedded = p1.hasAnnotation(javax.persistence.Embedded.class);
        boolean m2IsEmbedded = p2.hasAnnotation(javax.persistence.Embedded.class);
        
        if( (m1IsIdentity) && (!m2IsIdentity) ) {
            return -1;
        } else if( (!m1IsIdentity) && (m2IsIdentity) ) {
            return 1;
        } else if( (m1IsEmbedded) && (!m2IsEmbedded) ) {
            return 1;
        } else if( (!m1IsEmbedded) && (m2IsEmbedded) ) {
            return -1;
        } else {
            org.doe4ejb3.annotation.PropertyDescriptor order1 = (org.doe4ejb3.annotation.PropertyDescriptor)p1.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            org.doe4ejb3.annotation.PropertyDescriptor order2 = (org.doe4ejb3.annotation.PropertyDescriptor)p2.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            if( (order1 != null) && (order2 == null) ) {
                return -1;
            } else if( (order1 == null) && (order2 != null) ) {
                return 1;
            } else if( (order1 != null) && (order2 != null) ) {
                int diff = (order1.index() - order2.index());
                if(diff != 0) return diff;
            }
            
            return p1.getName().compareTo(p2.getName());
        }
    }

}
