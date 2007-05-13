/*
 * EntityEditorImpl.java
 *
 * Created on June 6, 2006, 5:45 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.beans.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.*;
import org.doe4ejb3.util.JPAUtils;
import org.doe4ejb3.util.ReflectionUtils;


public class EntityEditorImpl extends JPanel implements EntityEditorInterface, Printable
{

    private final static Insets borderInsets = new Insets(20,10,20,10);

    private final static GridBagConstraints gbcTitle = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5,5,10,5), 0,0);
    private final static GridBagConstraints gbcLabel = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcFixedSizeComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcEmbeddedComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,-3, 5,-3), 0,0);
    private final static GridBagConstraints gbcGlue = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
    
    
    private Object entity = null;
    private boolean objIsNew = false;
    private boolean embedded = false;
    private ArrayList<JComponentDataBinder> bindings = new ArrayList();
    
    
    /**
     * Creates a new instance of EntityEditorImpl
     */
    public EntityEditorImpl() {
        this(false);
    }    
    
    public EntityEditorImpl(boolean embedded) 
    {
        this.embedded = embedded;
        setMinimumSize(new java.awt.Dimension(550, 400));
    }

    public void clearBinders()
    {
        bindings.clear();
    }
    
    public boolean isNew()
    {
        return this.objIsNew;
    }

    public Object getEntity() throws IllegalAccessException, InvocationTargetException
    {
        for(JComponentDataBinder binding : bindings) {
            binding.executeObjSetterWithValueFromCompGetter();
        }
        return this.entity;
    }

    public void newEntity(Class entityClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        Constructor init = entityClass.getConstructor();
        this.entity = init.newInstance();
        System.out.println("newInstance: entity = " + entity);
        setEntity(entity, true);
    }
    
    public void setEntity(Object entity)
    {
        setEntity(entity, false);
    }
    

    private void setEntity(Object entity, boolean objIsNew)
    {
        System.out.println("Entity: " + entity + ", isNew=" + objIsNew);
        
        this.entity = entity;
        this.objIsNew = objIsNew;
        
        clearBinders();
        removeAll();
        setLayout(new GridBagLayout());

        Class entityClass = entity.getClass();

        
        if(embedded) {
            setBorder(javax.swing.BorderFactory.createTitledBorder(I18n.getEntityName(entityClass)));
        } 
        /*
        else {
            JLabel title = new JLabel(I18n.getEntityName(entityClass));
            title.setFont(title.getFont().deriveFont(16.0f));
            add(title, gbcTitle);
        }
        */


        ArrayList<ObjectProperty> properties = new ArrayList<ObjectProperty>();
        try {
            BeanInfo bi = Introspector.getBeanInfo(entityClass);
            for(java.beans.PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                // TODO: inherited properties?
                ObjectProperty entityProperty = new ObjectProperty(entity, pd);
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
            for(Field field : entityClass.getFields()) {
                // TODO: inherited fields
                ObjectProperty entityProperty = new ObjectProperty(entity, field);
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
        } catch(Exception ex) {
            System.out.println("EntityEditorImpl: ERROR: " + ex.getMessage());
        }
        


        PropertyOrderComparator orderComparator = new PropertyOrderComparator();
        Collections.sort(properties, orderComparator);
        for(ObjectProperty property : properties) 
        {
            handlePersistenceAnnotations(property);
        }
        

        add(Box.createVerticalGlue(), gbcGlue);
    }
    
    public void handlePersistenceAnnotations(ObjectProperty entityProperty)
    {
        // TODO: @Embedable --> new EntityEditorImpl(field.getType() || method.getReturnType())
        // (with "getEntity" method as "getter")
        String name = entityProperty.getName();
        Annotation annotations[] = entityProperty.getAnnotations();
        System.out.println("handlePersistenceAnnotations: Processing property: " + entityProperty.getName());
        
        Class memberClass = null;
        try {
            memberClass = entityProperty.getType();
            if(java.util.Collection.class.isAssignableFrom(memberClass)) {
                ParameterizedType paramType = (ParameterizedType)entityProperty.getGenericType();
                memberClass = (Class)(paramType.getActualTypeArguments()[0]);
            }
        } catch(Exception ex) {
            return;
        }

        
        // Only assume setter/getters to be persistence members, except those annotated with javax.persistence.Transient
        // (public fields must be annotated with persistence annotations, to be considered also as persistence members)
        boolean persistent = (entityProperty.getPropertyDescriptor() != null) 
                                && (entityProperty.getPropertyDescriptor().getReadMethod() != null) && (!entityProperty.getPropertyDescriptor().getReadMethod().isAnnotationPresent(javax.persistence.Transient.class))
                                && (entityProperty.getPropertyDescriptor().getWriteMethod() != null) && (!entityProperty.getPropertyDescriptor().getWriteMethod().isAnnotationPresent(javax.persistence.Transient.class));
        boolean generatedValue = false;
        boolean embedded = false;
        javax.persistence.Temporal temporal = null;
        Object relationType = null;
        GridBagConstraints gbc = gbcComponent;
        Component comp = null;
        JComponentDataBinder binder = null;
        java.beans.PropertyEditor editor = null;
        org.doe4ejb3.annotation.PropertyDescriptor propertyDescriptor = null;
        
        Method compGetter = null;
        int maxLength = 255;

        System.out.println("Begin: process property annotation");
        for(int i = 0; (annotations != null) && (i < annotations.length); i++) 
        {
            Annotation a = annotations[i];
            System.out.println("> process property annotation: " + a.toString());

            if(a instanceof org.doe4ejb3.annotation.PropertyDescriptor)
            {
                propertyDescriptor = (org.doe4ejb3.annotation.PropertyDescriptor)a;
            }
            
            if(a instanceof javax.persistence.GeneratedValue) 
            {
                persistent = true;
                generatedValue = true;
                maxLength = 0;
                System.out.println("Has GeneratedValue annotation!");
            }
            else if(a instanceof javax.persistence.Basic) 
            {
                persistent = true;
            }
            else if(a instanceof javax.persistence.Column) 
            {
                javax.persistence.Column column = (javax.persistence.Column)a;
                if( (column.insertable()) || (column.updatable()) ) {
                    if(!generatedValue) maxLength = column.length();
                    String columnName = column.name();
                    if(columnName != null) name = columnName;
                    persistent = true;
                }
            }
            else if(a instanceof javax.persistence.JoinColumn) 
            {
                // FIXME: Relation annotations supose that are persistent,
                // but should not be enabled.
                javax.persistence.JoinColumn column = (javax.persistence.JoinColumn)a;
                String columnName = column.name();
                if(columnName != null) name = columnName;

                if( (!column.insertable()) && (objIsNew) ) {
                    generatedValue = true;
                } else if((!column.updatable()) && (!objIsNew) ) {
                    generatedValue = true;
                }
            }
            else if(a instanceof javax.persistence.Temporal) 
            {
                persistent = true;
                temporal = (javax.persistence.Temporal)a;
            } 
            else if(a instanceof javax.persistence.Embedded) 
            {
                persistent = true;
                embedded = true;
            } 
            else if( (a instanceof javax.persistence.OneToOne) || (a instanceof javax.persistence.OneToMany) 
                     || (a instanceof javax.persistence.ManyToOne) || (a instanceof javax.persistence.ManyToMany) )
            {
                relationType = a;
                persistent = true;
            }
            else if(a instanceof javax.persistence.Version) 
            {
                // TODO? Hide "version" fields
                persistent = true;      
                generatedValue = true;
                maxLength = 0;
            } 
            else if(a instanceof javax.persistence.Lob) 
            {
                persistent = true;
                maxLength = Integer.MAX_VALUE;
            }
                
        }
        
        if( (name != null) && (persistent) && (!generatedValue || !objIsNew) ) {
            if(embedded) {
                // TO TEST:
                try {
                    EntityEditorImpl ee = new EntityEditorImpl(embedded);
                    comp = ee;

                    compGetter = ee.getClass().getMethod("getEntity");
                    Object value = entityProperty.getValue();
                    if(value != null) ee.setEntity(value);
                    else ee.newEntity(memberClass);
                    
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                
            } else {
                
                // Other editors:
                javax.persistence.TemporalType defaultTemporalType = javax.persistence.TemporalType.TIMESTAMP;
                if( (temporal != null) && (temporal.value() != null) ) defaultTemporalType = temporal.value();
                comp = EditorFactory.getPropertyEditor(entityProperty, maxLength, defaultTemporalType);
                binder = (JComponentDataBinder)((JComponent)comp).getClientProperty("dataBinder");
                
            }

            
            if(comp != null) {
                if(generatedValue && (!objIsNew)) {
                    if(comp instanceof JScrollPane) {
                        JScrollPane scroll = (JScrollPane)comp;
                        scroll.getViewport().getView().setEnabled(false);
                    } else {
                        comp.setEnabled(false);
                    }
                } else {
                    if(binder == null) {
                        binder = new JComponentDataBinder(comp, compGetter, editor, entityProperty);
                    }
                    if( (comp != null) && (comp instanceof JComponent) ) {
                        ((JComponent)comp).putClientProperty("dataBinder", binder);
                    }
                    bindings.add(binder);
                }
            }

            if(comp != null) {
                if(!embedded) {
                    String labelText = I18n.getLiteral(name.toUpperCase());
                    if( (propertyDescriptor != null) && (propertyDescriptor.displayName() != null) && (propertyDescriptor.displayName().length() > 0) )
                    {
                        labelText = propertyDescriptor.displayName();
                        if( (propertyDescriptor.resourceBundle() != null) && (propertyDescriptor.resourceBundle().length() > 0) )
                        {
                            try {
                                ResourceBundle bundle = ResourceBundle.getBundle(propertyDescriptor.resourceBundle());
                                String i18nLabelText = bundle.getString(labelText);
                                if( (i18nLabelText != null) && (i18nLabelText.length() > 0) ) labelText = i18nLabelText;
                            } catch(Exception ex) {
                                System.out.println("EntityEditorImpl: Error loading bundle" + propertyDescriptor.resourceBundle());
                            }
                        }
                    }

                    if( (comp instanceof JComponent) 
                            && (((JComponent)comp).getClientProperty("fixedSize") != null) ) {
                        gbc = gbcFixedSizeComponent;
                    }
                    
                    add(new JLabel(labelText), gbcLabel);
                    add(comp, gbc);
                    
                } else {
                    add(comp, gbcEmbeddedComponent);
                }
            }
        }
        
    }
    
    private String capitalize(String name)
    {
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    
    public Insets getInsets() {
        return borderInsets;
    }


  private int getPrintingHeight()
  {
    // TODO: SMALL COMPONENT SHOULDN'T BE CUTTED IN TWO PAGES
    // TODO: COMPOSITE EDITORS SHOULD EXTRACT PROPERTY VALUES/TABLES      
    int total = 0;
    int EXTRA_SPACE = 50;
    Component all[] = this.getComponents();
    for(int i = 1; (all != null) && (i < all.length); i=i+2) {
        Component label = all[i-1];
        Component comp  = all[i];
        int height = Math.max(getComponentPrintHeight(label), getComponentPrintHeight(comp));
        total = total + height;
    }
    return total + EXTRA_SPACE;
  }
  
  private int getComponentPrintHeight(Component comp)
  {
      int headerHeight = 0;
      int componentHeight = 0;
      if( comp != null) {
          if(comp instanceof ComposedEditorHolder)
          {
              ComposedEditorHolder composedEditor = (ComposedEditorHolder)comp;
              comp = composedEditor.getComponentWithValues();
          }
          if(comp instanceof JScrollPane) {
              JScrollPane scrollPane = (JScrollPane)comp;
              if(scrollPane.getColumnHeader() != null) {
                headerHeight = scrollPane.getColumnHeader().getHeight();
              }
              comp = scrollPane.getViewport().getView();
          } 
          componentHeight = comp.getHeight();
      }
      return headerHeight + componentHeight;
  }
    
  public int print(Graphics g, PageFormat pf, int pageIndex) {
    // TODO: SMALL COMPONENT SHOULDN'T BE CUTTED IN TWO PAGES
    // TODO: COMPOSITE EDITORS SHOULD EXTRACT PROPERTY VALUES/TABLES
      
    int response = NO_SUCH_PAGE;

    Graphics2D g2 = (Graphics2D) g;

    //  for faster printing, turn off double buffering
    disableDoubleBuffering(this);

    Dimension d = this.getSize(); //get size of document
    double panelWidth = d.width; //width in pixels
    double panelHeight = getPrintingHeight(); //height in pixels

    double pageHeight = pf.getImageableHeight(); //height of printer page
    double pageWidth = pf.getImageableWidth(); //width of printer page

    double scale = pageWidth / panelWidth;
    int totalNumPages = (int) Math.ceil(scale * panelHeight / pageHeight);

    System.out.println("Printing: " + pageIndex + "/" + totalNumPages);
    //  make sure not print empty pages
    if (pageIndex >= totalNumPages) {
      System.out.println("Printing done.");
      response = NO_SUCH_PAGE;
    }
    else {

      //  shift Graphic to line up with beginning of print-imageable region
      g2.translate(pf.getImageableX(), pf.getImageableY());

      //  shift Graphic to line up with beginning of next page to print
      g2.translate(0f, -pageIndex * pageHeight);

      //  scale the page so the width fits...
      g2.scale(scale, scale);
      
      

      Color bg = this.getBackground();
      try {
          this.setBackground(Color.WHITE);

      
          double interlineHeight = 10.0;
          Component all[] = this.getComponents();
          for(int i = 1; (all != null) && (i < all.length); i=i+2) {
                Component label = all[i-1];
                Component comp  = all[i];
                Component header = null;
                double offset = (double)comp.getX();

                if(comp instanceof ComposedEditorHolder)
                {
                    ComposedEditorHolder composedEditor = (ComposedEditorHolder)comp;
                    comp = composedEditor.getComponentWithValues();
                }
                if(comp instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane)comp;
                    header = scrollPane.getColumnHeader();
                    comp = scrollPane.getViewport().getView();
                }
                double height = Math.max(getComponentPrintHeight(label), getComponentPrintHeight(comp));
                
                System.out.println("Printing label? " + label);
                label.print(g2);

                g2.translate(offset, 0.0);
                if(header != null) {
                    header.print(g2);
                    g2.translate(0.0, (double)header.getHeight());
                }

                System.out.println("Printing component? " + comp);
                comp.print(g2);

                System.out.println("Next component height " + height);
                g2.translate((double)-offset, (double)height + interlineHeight);
          }
      
      
      } finally {
          this.setBackground(bg);
      }

      enableDoubleBuffering(this);
      response = Printable.PAGE_EXISTS;
    }
    return response;
  }
 

  /** The speed and quality of printing suffers dramatically if
   *  any of the containers have double buffering turned on.
   *  So this turns if off globally.
   *  @see enableDoubleBuffering
   */
  public static void disableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(false);
  }

  /* Re-enables double buffering globally. */
  
  public static void enableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(true);
  }
 
}
