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
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.persistence.TemporalType;


public class TemporalTypeEditorSupport extends java.beans.PropertyEditorSupport 
{

    private java.util.Date polymorphicTemporalTypeValue;
    private java.text.SimpleDateFormat simpleDateFormat;
    private javax.swing.JSpinner spinner;
    
    
    public TemporalTypeEditorSupport(java.lang.Class temporalClass)
    {
        try {
            Constructor constructor = temporalClass.getConstructor(Long.TYPE);
            this.polymorphicTemporalTypeValue = (java.util.Date)constructor.newInstance(0);
            this.simpleDateFormat = null;
            this.spinner = new JSpinner();
            this.spinner.putClientProperty("fixedSize", "true");
            if(temporalClass.isAssignableFrom(java.sql.Date.class)) {
                this.spinner.setModel(new SpinnerDateModel());
                this.spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
            } else if(temporalClass.isAssignableFrom(java.sql.Time.class)) {
                this.spinner.setModel(new SpinnerDateModel());
                this.spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
            } else if(temporalClass.isAssignableFrom(java.sql.Timestamp.class)) {
                spinner.setModel(new SpinnerDateModel());
            } else if(temporalClass.isAssignableFrom(java.util.Date.class)) {
                spinner.setModel(new SpinnerDateModel());
                this.simpleDateFormat = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            } 
        } catch(Exception ex) {
            System.out.println("TemporalTypeEditorSupport: ERROR = " + ex.getMessage());
            ex.printStackTrace();
            throw new IllegalArgumentException("temporalClass");
        }
       
    }
    
    

    public void setAsText(String text) throws java.lang.IllegalArgumentException {
        try { 
            if(simpleDateFormat != null) {
                spinner.setValue(simpleDateFormat.parse(text)); 
            } else {
                Method valueOfMethod = polymorphicTemporalTypeValue.getClass().getMethod("valueOf", String.class);
                java.util.Date tmp = (java.util.Date)valueOfMethod.invoke(null, text);
                spinner.setValue(tmp);
            }
        } catch(Exception ex) { 
            throw new IllegalArgumentException(ex.getMessage(), ex); 
        }
    }
    
    public void setValue(Object value) {
        try {
            spinner.setValue((java.util.Date)value);
        } catch(Exception ex) {
            System.out.println("TemporalTypeEditorSupport.setValue: Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getAsText() {
        return simpleDateFormat.format((java.util.Date)spinner.getValue());
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
    

    public static void registerTemporalTypeEditors()
    {
        PropertyEditorManager.registerEditor(java.util.Date.class, CustomDateEditor.class);
        PropertyEditorManager.registerEditor(java.sql.Date.class, CustomSqlDateEditor.class);
        PropertyEditorManager.registerEditor(java.sql.Time.class, CustomSqlTimeEditor.class);
        PropertyEditorManager.registerEditor(java.sql.Timestamp.class, CustomSqlTimestampEditor.class);
    }

    
}
