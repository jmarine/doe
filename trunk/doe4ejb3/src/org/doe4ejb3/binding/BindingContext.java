/*
 * BindingContext.java
 * 
 * Created on Jun 27, 2007, 9:13:57 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import org.jdesktop.beansbinding.Binding.SyncFailure;


public class BindingContext extends org.jdesktop.beansbinding.BindingGroup
{
    public void commitUncommittedValues()
    {
        for(org.jdesktop.beansbinding.Binding binding : getBindings()) {
            SyncFailure failure = binding.save();
            if(failure != null) throw new PropertyResolverException("Binding failure: " + failure.toString(), binding, null, null);
        }
        
    }    
    
}
