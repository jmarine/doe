/*
 * BindingContext.java
 * 
 * Created on Jun 27, 2007, 9:13:57 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.doe4ejb3.binding;

import java.util.ArrayList;

/**
 *
 * @author jordi
 */;
public class BindingContext extends ArrayList<JComponentDataBinding> {
    
    private boolean bindActive = true;

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
        if(bindActive) {
            for(JComponentDataBinding binding : this) {
                try {
                    binding.commit();
                } catch(Exception ex) {
                    throw new PropertyResolverException("Binding error", binding, null, ex);
                }
            }
        }
    }
    
    public void bind()
    {
        bindActive = true;
    }
    
    public void unbind()
    {
        bindActive = false;
    }
    
}
