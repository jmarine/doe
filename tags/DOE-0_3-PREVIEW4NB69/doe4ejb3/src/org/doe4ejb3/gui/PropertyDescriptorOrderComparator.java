/**
 * PropertyDescriptorOrderComparator.java
 *
 * Created on 29 July 2006, 21:39
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.util.Comparator;
import org.doe4ejb3.annotation.PropertyDescriptor;


public class PropertyDescriptorOrderComparator implements Comparator<PropertyDescriptor>
{
    
    /** Creates a new instance of OrderAttributeComparator */
    public PropertyDescriptorOrderComparator() { }


    public int compare(PropertyDescriptor order1, PropertyDescriptor order2) 
    {

        if( (order1 != null) && (order2 == null) ) {
            return -1;
        } else if( (order1 == null) && (order2 != null) ) {
            return 1;
        } else if( (order1 != null) && (order2 != null) ) {
            int diff = (order1.index() - order2.index());
            if(diff != 0) return diff;
        }

        return 0;

    }

}
