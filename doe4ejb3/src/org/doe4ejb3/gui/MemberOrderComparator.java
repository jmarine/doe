/**
 * PropertyOrderComparator.java
 *
 * Created on 29 July 2006, 21:39
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.lang.reflect.Member;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import org.doe4ejb3.binding.EntityProperty;


public class MemberOrderComparator implements Comparator<Member>
{
    private PropertyDescriptorOrderComparator pdComparator;
    
    /** Creates a new instance of OrderAttributeComparator */
    public MemberOrderComparator() 
    { 
        pdComparator = new PropertyDescriptorOrderComparator();
    }


    public int compare(Member member1, Member member2) 
    {
        org.doe4ejb3.annotation.PropertyDescriptor pd1 = null;
        org.doe4ejb3.annotation.PropertyDescriptor pd2 = null;
        
        if(member1 != null) {
            if(member1 instanceof Field) {
                Field f1 = (Field)member1;
                pd1 = f1.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            } else if(member1 instanceof Method) {
                Method method1 = (Method)member1;
                pd1 = method1.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            }
        }

        if(member2 != null) {
            if(member2 instanceof Field) {
                Field f2 = (Field)member2;
                pd2 = f2.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            } else if(member2 instanceof Method) {
                Method method2 = (Method)member2;
                pd2 = method2.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
            }
        }

        int diff = pdComparator.compare(pd1, pd2);
        if(diff != 0) return diff;
        else return member1.getName().compareTo(member2.getName());

    }

}
