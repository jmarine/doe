/*
 * BindingContext.java
 * 
 * Created on Jun 27, 2007, 9:13:57 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.util.ArrayList;
import org.jdesktop.beansbinding.Binding.SyncFailure;


public class BindingContext extends ArrayList<JComponentDataBinding> 
{
    
    private org.jdesktop.beansbinding.BindingGroup bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    
    public void addBinding(Object binding)
    {
        if(binding != null) {
            if(binding instanceof org.jdesktop.beansbinding.Binding)
                bindingGroup.addBinding((org.jdesktop.beansbinding.Binding)binding);
            else if(binding instanceof JComponentDataBinding)
                add((JComponentDataBinding)binding);
            else
                throw new RuntimeException("Unsupported binding type: " + binding.getClass().getName());
        }
    }

    public void removeBinding(Object binding)
    {
        if(binding != null) {
            if(binding instanceof org.jdesktop.beansbinding.Binding)
                bindingGroup.removeBinding((org.jdesktop.beansbinding.Binding)binding);
            else if(binding instanceof JComponentDataBinding)
                remove((JComponentDataBinding)binding);
            else
                throw new RuntimeException("Unsupported binding type: " + binding.getClass().getName());
        }
    }
        
    public void commitUncommittedValues()
    {
        for(org.jdesktop.beansbinding.Binding binding : bindingGroup.getBindings()) {
            SyncFailure failure = binding.save();
            if(failure != null) throw new PropertyResolverException("Binding failure: " + failure.toString(), binding, null, null);
        }
        
        for(JComponentDataBinding binding : this) {
            try {
                binding.commitChanges();
            } catch(Exception ex) {
                throw new PropertyResolverException("Binding error", binding, null, ex);
            }
         }
    }    
       
    public void bind()
    {
        bindingGroup.bind();
        for(JComponentDataBinding binding : this) {
            try {
                binding.bind();
            } catch(Exception ex) {
                throw new PropertyResolverException("Binding error", binding, null, ex);
            }
        }

    }

    public void unbind()
    {
        bindingGroup.unbind();
        for(JComponentDataBinding binding : this) {
            try {
                binding.unbind();
            } catch(Exception ex) {
                throw new PropertyResolverException("Unbinding error", binding, null, ex);
            }
        }
    }
    
}
