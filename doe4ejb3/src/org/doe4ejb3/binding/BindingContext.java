/*
 * BindingContext.java
 * 
 * Created on Jun 27, 2007, 9:13:57 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.util.ArrayList;


public class BindingContext extends ArrayList<JComponentDataBinding> {
    
    private javax.beans.binding.BindingContext stdBindingContext = new javax.beans.binding.BindingContext();

    
    public void addBinding(Object binding)
    {
        if(binding != null) {
            if(binding instanceof javax.beans.binding.Binding)
                stdBindingContext.addBinding((javax.beans.binding.Binding)binding);
            else if(binding instanceof JComponentDataBinding)
                add((JComponentDataBinding)binding);
            else
                throw new RuntimeException("Unsupported binding type: " + binding.getClass().getName());
        }
    }

    public void removeBinding(Object binding)
    {
        if(binding != null) {
            if(binding instanceof javax.beans.binding.Binding)
                stdBindingContext.removeBinding((javax.beans.binding.Binding)binding);
            else if(binding instanceof JComponentDataBinding)
                remove((JComponentDataBinding)binding);
            else
                throw new RuntimeException("Unsupported binding type: " + binding.getClass().getName());
        }
    }
        
    public void commitUncommittedValues()
    {
        stdBindingContext.commitUncommittedValues();
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
        stdBindingContext.bind();
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
        stdBindingContext.unbind();
        for(JComponentDataBinding binding : this) {
            try {
                binding.unbind();
            } catch(Exception ex) {
                throw new PropertyResolverException("Unbinding error", binding, null, ex);
            }
        }
    }
    
}
