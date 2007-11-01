/*
 * CustomDateEditor.java
 *
 * Created on 28 October 2006, 19:18
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.beans;

import java.util.Calendar;
import java.util.Date;
import org.doe4ejb3.gui.I18n;


public class CustomCalendarEditor extends TemporalTypeEditorSupport
{
    public CustomCalendarEditor(javax.persistence.TemporalType temporalType) 
    {
        super(temporalType);
    }
    
    public void setValue(Object value) {
        try {
            if(value == null) value = Calendar.getInstance();
            Calendar cal = (Calendar)value;
            super.setValue(cal.getTime());
        } catch(Exception ex) {
            System.out.println("CustomCalendarEditor.setValue: " + I18n.getLiteral("msg.error") + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Object getValue() {
        try {
            Date date = (Date)super.getValue();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch(Exception ex) {
            System.out.println("CustomCalendarEditor.getValue: " + I18n.getLiteral("msg.error") + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
}