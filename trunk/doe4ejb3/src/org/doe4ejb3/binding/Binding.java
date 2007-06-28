/*
 * Binding.java
 * 
 * Created on Jun 28, 2007, 7:43:21 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.binding;

import java.lang.reflect.InvocationTargetException;


public class Binding 
{
    public enum UpdateStrategy { READ_FROM_SOURCE, READ_ONCE, READ_WRITE }

    private boolean active;
    private UpdateStrategy updateStrategy;
    

    // During migration to JSR 295, I want to prevent direct use of "Binding" class
    protected Binding() 
    { 
        active = false; 
    }
    
    public void bind()
    {
        if(active) throw new IllegalStateException("Binding is already active");
        active = true;
    }
    
    public void unbind()
    {
        if(!active) throw new IllegalStateException("Binding was not active");
        active = false;
    }
    
    public void setUpdateStrategy(UpdateStrategy strategy)
    {
        this.updateStrategy = strategy;
    }
    
    public UpdateStrategy getUpdateStrategy() 
    {
        return updateStrategy;
    }

    void commitChanges() throws IllegalStateException, IllegalAccessException, InvocationTargetException
    {
        if(!active) throw new IllegalStateException("Binding is not active");
    }            
    

}
