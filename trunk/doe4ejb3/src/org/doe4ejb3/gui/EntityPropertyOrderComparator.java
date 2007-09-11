/**
 * EntityPropertyOrderComparator.java
 *
 * Created on 29 July 2006, 21:39
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.util.Comparator;
import org.doe4ejb3.binding.EntityProperty;


public class EntityPropertyOrderComparator implements Comparator<EntityProperty>
{
    private PropertyDescriptorOrderComparator pdComparator;
    
    /** Creates a new instance of OrderAttributeComparator */
    public EntityPropertyOrderComparator() 
    { 
        pdComparator = new PropertyDescriptorOrderComparator();
    }


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
            org.doe4ejb3.annotation.PropertyDescriptor pd1 = p1.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            org.doe4ejb3.annotation.PropertyDescriptor pd2 = p2.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);

            int diff = pdComparator.compare(pd1, pd2);
            if(diff != 0) return diff;
            else return p1.getName().compareTo(p2.getName());
        }
    }

}
