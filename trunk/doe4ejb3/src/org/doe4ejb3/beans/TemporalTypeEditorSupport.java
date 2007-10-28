/*
 * TemporalTypeEditorSupport.java
 *
 * Created on 28 October 2006, 19:18
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.beans;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.text.DateFormat;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.persistence.TemporalType;


public class TemporalTypeEditorSupport extends java.beans.PropertyEditorSupport implements PropertyChangeListener
{

    private java.util.Date polymorphicTemporalTypeValue;
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
                polymorphicTemporalTypeValue = new java.sql.Date(0l);
                pattern = "MM/dd/yyyy";  // NOI18N
                try {
                    DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    pattern = ((java.text.SimpleDateFormat)formatter).toPattern();
                } catch(Exception ex) { }
            } else {
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
            firePropertyChange();
        } catch(Exception ex) {
            System.out.println("TemporalTypeEditorSupport.setValue: Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getAsText() {
        JSpinner.DateEditor editor = (JSpinner.DateEditor)spinner.getEditor();
        return editor.getFormat().format(spinner.getValue());
    }

    public Object getValue() {
        try {
            long time = ((java.util.Date)spinner.getValue()).getTime();
            polymorphicTemporalTypeValue.setTime(time);
            return polymorphicTemporalTypeValue;
        } catch(Exception ex) {
            System.out.println("TemporalTypeEditorSupport.getValue: Error: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public Component getCustomEditor() {
        return spinner;
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        if("value".equals(evt.getPropertyName())) {
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
