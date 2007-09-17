/*
 * ListItem.java
 *
 * Created on 28 / maig / 2005, 10:02
 * @author Jordi Mariné Fort
 */

package org.doe4ejb3.gui;

import java.io.Serializable;


public class ListItem implements Serializable 
{
    /** Text attribute */
    private Object text;
    
    /** Value attribute */
    private Object value;
    
    /** Creates a new instance of ListItem */
    public ListItem() {
        this(null, null);
    }

    /** Creates a new instance of ListItem */
    public ListItem(Object value) {
        this(value,value);
    }

    /** Creates a new instance of ListItem */
    public ListItem(Object value, Object text) {
        setValue(value);
        setText(text);
    }
    
    /** Establish the value of this ListItem */
    public void setValue(Object value)
    {
        this.value = value;
    }
    
    /** Get the value of this ListItem */
    public Object getValue()
    {
        return value;
    }
    
    
    /** Establish the text of this ListItem */
    public void setText(Object text)
    {
        this.text = text;
    }
    
    /** Gets the value of this ListItem */
    public Object getText()
    {
        return text;
    }
    
    public String toString()
    {
        return text.toString();
    }
    
    public int hashCode()
    {
        return value.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof ListItem)
        {
            ListItem listItem = (ListItem)o;
            if(value == null) return (listItem.getValue() == null);
            else return ((listItem.getValue() != null) && (value.equals(listItem.getValue())));
        } else {
            return value.equals(o);
        }
    }
    
}
