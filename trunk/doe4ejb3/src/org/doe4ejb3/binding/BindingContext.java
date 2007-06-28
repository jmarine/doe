/*
 * BindingContext.java
 * 
 * Created on Jun 27, 2007, 9:13:57 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.util.ArrayList;


public class BindingContext extends ArrayList<JComponentDataBinding> {
    
    public BindingContext() {
    }
    
    public void addBinding(JComponentDataBinding binding) {
        add(binding);
    }

    public void removeBinding(JComponentDataBinding binding) {
        remove(binding);
    }    
    
    public void commitUncommittedValues()
    {
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
        for(JComponentDataBinding binding : this) {
            try {
                binding.unbind();
            } catch(Exception ex) {
                throw new PropertyResolverException("Unbinding error", binding, null, ex);
            }
        }
    }
    
}
