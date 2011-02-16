/*
 * TemporalTypeEditorSupport.java
 *
 * Created on 28 October 2006, 19:18
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.beans;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorManager;
import java.text.DateFormat;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.jdesktop.swingx.JXDatePicker;

import org.doe4ejb3.util.I18n;


public class TemporalTypeEditorSupport extends java.beans.PropertyEditorSupport implements PropertyChangeListener
{

    private java.util.Date polymorphicTemporalTypeValue;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JSpinner spinner;
    private String pattern;
    
    
    public TemporalTypeEditorSupport(javax.persistence.TemporalType temporalType)
    {
        try {
            spinner = new JSpinner();
            spinner.addPropertyChangeListener(this);  // if TemporalTypeEditorSupport is derived from PropertyEditorSupport, it is done automagically by EditorFactory (to bind edited properties back to entity object via JSR 295)
            spinner.putClientProperty("fixedSize", "true");

            if(temporalType == javax.persistence.TemporalType.TIME) {
                polymorphicTemporalTypeValue = new java.sql.Time(0l);
                pattern = "HH:mm:ss";  // NOI18N
                try {
                    DateFormat formatter = DateFormat.getTimeInstance(DateFormat.MEDIUM);
                    pattern = ((java.text.SimpleDateFormat)formatter).toPattern();
                } catch(Exception ex) { }
            } else if(temporalType == javax.persistence.TemporalType.DATE) {
                datePicker = new JXDatePicker();
                datePicker.addPropertyChangeListener(this);
                datePicker.putClientProperty("fixedSize", "true");
                polymorphicTemporalTypeValue = new java.sql.Date(0l);
                pattern = "MM/dd/yyyy";  // NOI18N
                try {
                    DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    pattern = ((java.text.SimpleDateFormat)formatter).toPattern();
                } catch(Exception ex) { }
            } else {
                datePicker = new DateTimePicker();
                datePicker.addPropertyChangeListener(this);
                datePicker.putClientProperty("fixedSize", "true");
                polymorphicTemporalTypeValue = new java.sql.Timestamp(0l);
                pattern = "MM/dd/yyyy HH:mm:ss";  // NOI18N
                try {
                    DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
                    pattern = ((java.text.SimpleDateFormat)formatter).toPattern();
                } catch(Exception ex) { }
            } 

            spinner.setModel(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, pattern));
            
        } catch(Exception ex) {
            System.out.println("TemporalTypeEditorSupport: ERROR = " + ex.getMessage());
            ex.printStackTrace();
            throw new IllegalArgumentException("temporalClass");
        }
       
    }
    
    
    public void setAsText(String text) throws IllegalArgumentException {
        System.out.println("TemporalTypeEditorSupport.setAsText: " + text);
        JSpinner.DateEditor editor = (JSpinner.DateEditor)spinner.getEditor();
        try {
            java.util.Date tmp = editor.getFormat().parse(text);
            setValue(tmp);
        } catch(Exception ex) {
            throw new IllegalArgumentException("incorrect date format: " + text, ex);
        }

    }
    
    public void setValue(Object value) {
        try {
            if(value == null) value = new java.util.Date();
            spinner.setValue((java.util.Date)value);
            if(datePicker != null) datePicker.setDate((java.util.Date)value);
            firePropertyChange();
        } catch(Exception ex) {
            System.out.println("TemporalTypeEditorSupport.setValue: " + I18n.getLiteral("msg.error") + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getAsText() {
        System.out.println("TemporalTypeEditorSupport.getAsText()");
        JSpinner.DateEditor editor = (JSpinner.DateEditor)spinner.getEditor();
        if(datePicker != null) {
            return editor.getFormat().format(datePicker.getDate());
        } else {
            return editor.getFormat().format(spinner.getValue());
        }
    }

    public Object getValue() {
        try {
            long time = ((java.util.Date)spinner.getValue()).getTime();
            if(datePicker != null) time = datePicker.getDate().getTime();
            polymorphicTemporalTypeValue.setTime(time);
            return polymorphicTemporalTypeValue;
        } catch(Exception ex) {
            System.out.println("TemporalTypeEditorSupport.getValue: " + I18n.getLiteral("msg.error")  + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public Component getCustomEditor() {
        if(datePicker != null) {
            return datePicker;
        } else {
            return spinner;
        }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        if("value".equals(evt.getPropertyName())) {
            firePropertyChange();
        }
        if("date".equals(evt.getPropertyName())) {
            firePropertyChange();
        }        
    }


    public static void registerTemporalTypeEditors()
    {
        PropertyEditorManager.registerEditor(java.sql.Date.class, CustomSqlDateEditor.class);
        PropertyEditorManager.registerEditor(java.sql.Time.class, CustomSqlTimeEditor.class);
        PropertyEditorManager.registerEditor(java.sql.Timestamp.class, CustomSqlTimestampEditor.class);
    }

    
}
