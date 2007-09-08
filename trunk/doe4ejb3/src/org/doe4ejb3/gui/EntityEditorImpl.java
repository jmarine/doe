/**
 * EntityEditorImpl.java
 *
 * Created on June 6, 2006, 5:45 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import org.doe4ejb3.binding.EntityProperty;
import java.beans.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javax.swing.*;

import org.doe4ejb3.annotation.EntityDescriptor;
import org.doe4ejb3.binding.BindingContext;


public class EntityEditorImpl extends JPanel implements EntityEditorInterface, EditorLayoutInterface, Printable
{

    private final static Insets borderInsets = new Insets(20,10,20,10);

    private final static GridBagConstraints gbcTitle = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5,5,10,5), 0,0);
    private final static GridBagConstraints gbcLabel = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcFixedSizeComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0,0);
    private final static GridBagConstraints gbcEmbeddedComponent = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,-3, 5,-3), 0,0);
    private final static GridBagConstraints gbcCustomLayout = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5, 5,5), 0,0);
    private final static GridBagConstraints gbcEmptyLabelForEmbeddedAndCustomLayoutPrinting = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
    private final static GridBagConstraints gbcGlue = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE, 0, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
    

    private String  puName   = null;
    private Object  entity   = null;
    private boolean objIsNew = false;
    private BindingContext bindingContext = new BindingContext();
    private EntityDescriptor entityDescriptor = null;
    private JComponent customLayout = null;
    private String layoutPath = "";
    private EditorLayoutInterface parentLayout = null;
    private boolean validUI = false;
    private boolean layoutResourceSearched = false;
    
    
    /**
     * Creates a new instance of EntityEditorImpl
     */
    public EntityEditorImpl() 
    {
        setMinimumSize(new java.awt.Dimension(550, 400));
    }

    private void setValidUI(boolean valid)
    {
        this.validUI = valid;
    }

    public JComponent getJComponent() 
    {
        return (validUI) ? this : null;
    }


    public void setPersistenceUnitName(String puName)
    {
        this.puName = puName;
    }

    public String getPersistenceUnitName()
    {
        return this.puName;
    }
    
    public void setEntityDescriptor(EntityDescriptor descriptor)
    {
        this.entityDescriptor = descriptor;
    }

    public EntityDescriptor getEntityDescriptor()
    {
        return this.entityDescriptor;
    }
    
    public void setLayoutPath(EditorLayoutInterface parentLayout, String layoutPath)
    {
        this.parentLayout = parentLayout;
        this.layoutPath = layoutPath;
    }

    public String getLayoutPath()
    {
        return this.layoutPath;
    }    
    
    public EditorLayoutInterface getParentLayout()
    {
        return this.parentLayout;
    }
    
    public JComponent getCustomEditorLayout()
    {
        if( (customLayout == null) && (!layoutResourceSearched) ) {
            layoutResourceSearched = true;
            if( (entityDescriptor != null) && (!entityDescriptor.layoutClassName().equalsIgnoreCase("default")) ) {
                // Custom layout class loader
                try { 
                    customLayout = (JComponent)Class.forName(entityDescriptor.layoutClassName()).newInstance(); 
                } catch(Exception ex) { 
                    System.out.println("EntityEditorImpl.getLayout: Error loading editor class: " + ex.getMessage());
                    ex.printStackTrace(); 
                }
            } else  {
                // Abeille form loader
                java.io.InputStream resourceStream = null;
                try {
                    Class entityClass = entity.getClass();
                    resourceStream = entityClass.getClassLoader().getResourceAsStream(entityClass.getName().replace('.', '/') + ".jfrm");
                    if(resourceStream != null) customLayout = new com.jeta.forms.components.panel.FormPanel(resourceStream);  
                } catch(Exception ex) {
                    System.out.println("EntityEditorImpl.getLayout: Error loading editor layout: " + ex.getMessage());
                    ex.printStackTrace();                    
                } finally {
                    if(resourceStream != null) {
                        try { resourceStream.close(); }
                        catch(Exception ex) { }
                    }
                }
            }
        }
        return customLayout;
    }
    
    private boolean containedInCustomEditorLayout()
    {
        return (getCustomEditorLayout() != null) ||
               ( (parentLayout != null) && (parentLayout.getCustomEditorLayout() != null) && (parentLayout.getComponentFromEditorLayout(JPanel.class, getLayoutPath()) == null));
    }

    public JComponent getComponentFromEditorLayout(Class componentType, String componentName)
    {
        JComponent retval = null;
        if(componentName.startsWith(".")) componentName = componentName.substring(1);
        
        if(parentLayout != null) {
            if(parentLayout.getComponentFromEditorLayout(JPanel.class, getLayoutPath()) != null) return null;
            retval = parentLayout.getComponentFromEditorLayout(componentType, getLayoutPath() + "." + componentName);
            if(retval != null) return retval;
        }

        JComponent layout = getCustomEditorLayout();
        if(layout == null) return null;

        if(layout instanceof com.jeta.forms.components.panel.FormPanel) {
            com.jeta.forms.components.panel.FormPanel abeilleFormPanel = (com.jeta.forms.components.panel.FormPanel)layout;
            Object tmp = abeilleFormPanel.getComponentByName(componentName);
            retval = (JComponent)tmp;
        } else {        
            componentName = componentName.replace('.', '_').toLowerCase();
            
            Class layoutClass = layout.getClass();
            try { 
                Field field1 = layoutClass.getDeclaredField(componentName);
                if( (componentType.isAssignableFrom(field1.getType())) 
                    || (JPanel.class.isAssignableFrom(componentType)) ) {
                    retval = (JComponent)field1.get(layout);
                }
            } catch(Exception ex) {
                System.out.println("EntityEditorImpl: Error getting editor from custom layout: " + ex.getMessage());
                ex.printStackTrace();
            }


            if(retval == null) {
                for(Field field2 : layout.getClass().getDeclaredFields()) {
                    String fieldName = field2.getName().toLowerCase();
                    if( (fieldName.indexOf(componentName) != -1) 
                            && ( (componentType.isAssignableFrom(field2.getType())) 
                                || (JPanel.class.isAssignableFrom(componentType)) ) ) {
                        try {
                            retval = (JComponent)field2.get(layout);
                            break;
                        } catch(Exception ex) {
                            System.out.println("EntityEditorImpl: Error getting editor from custom layout: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return retval;
    }    
    
    
    protected void clearBindings()
    {
        if(bindingContext != null) bindingContext.unbind();
        bindingContext = new BindingContext();
    }

    
    public boolean isNew()
    {
        return this.objIsNew;
    }

    public Object getEntity() throws IllegalAccessException, InvocationTargetException
    {
        bindingContext.commitUncommittedValues();
        return this.entity;
    }

    public void newEntity(Class entityClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException
    {
        Constructor init = entityClass.getConstructor();
        this.entity = init.newInstance();
        setEntity(entity, true);
    }
    
    public void setEntity(Object entity)
    {
        setEntity(entity, false);
    }
    

    protected void setEntity(Object entity, boolean objIsNew)
    {
        System.out.println("Entity: " + entity + ", isNew=" + objIsNew);
        
        this.entity = entity;
        this.objIsNew = objIsNew;
        
        clearBindings();
        removeAll();
        setLayout(new GridBagLayout());
        
        ArrayList<EntityProperty> properties = new ArrayList<EntityProperty>();
        try {
            Class entityClass = entity.getClass();
            BeanInfo bi = Introspector.getBeanInfo(entityClass);
            for(java.beans.PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                // TODO: inherited properties?
                EntityProperty entityProperty = new EntityProperty(entity, pd);
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
            for(Field field : entityClass.getFields()) {
                // TODO: inherited fields
                EntityProperty entityProperty = new EntityProperty(entity, field);
                if(!properties.contains(entityProperty)) properties.add(entityProperty);
            }
            
        } catch(Exception ex) {
            System.out.println("EntityEditorImpl: ERROR: " + ex.getMessage());
        }
        


        PropertyOrderComparator orderComparator = new PropertyOrderComparator();
        Collections.sort(properties, orderComparator);
        for(EntityProperty property : properties) 
        {
            handlePersistenceAnnotations(property);
        }
        

        if(getCustomEditorLayout() != null) {
            add(new JLabel(""), gbcEmptyLabelForEmbeddedAndCustomLayoutPrinting);  // easy printing
            add(getCustomEditorLayout(), gbcCustomLayout);
            setValidUI(true);
        }
        add(Box.createVerticalGlue(), gbcGlue);
        
        bindingContext.bind();
    }
    
    public void handlePersistenceAnnotations(EntityProperty entityProperty)
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
        Object binding = null;
        java.beans.PropertyEditor editor = null;
        org.doe4ejb3.annotation.PropertyDescriptor propertyDescriptor = null;
        
        Method compGetter = null;
        int maxLength = 255;
        boolean isNewComponent = false;
        JComponent container = null;

        System.out.println("Begin: process property annotation");
        for(int i = 0; (annotations != null) && (i < annotations.length); i++) 
        {
            Annotation a = annotations[i];
            System.out.println("> process property annotation: " + a.toString());

            if(a instanceof org.doe4ejb3.annotation.PropertyDescriptor)
            {
                propertyDescriptor = (org.doe4ejb3.annotation.PropertyDescriptor)a;
            }
            
            if(a instanceof javax.persistence.Id) 
            {
                persistent = true;
                if(!objIsNew) generatedValue = true;
            }               
            else if(a instanceof javax.persistence.GeneratedValue) 
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
                // FIXME: suposing that relation annotations  are persistent,
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
            else if(a instanceof javax.persistence.PrimaryKeyJoinColumn) 
            {
                // FIXME: suposing that relation annotations  are persistent,
                // but should not be enabled.
                javax.persistence.PrimaryKeyJoinColumn column = (javax.persistence.PrimaryKeyJoinColumn)a;
                String columnName = column.name();
                if(columnName != null) name = columnName;
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
                // "version" fields will be hidden for new entities (like GeneratedValues)
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
                    container = this.getComponentFromEditorLayout(JPanel.class, entityProperty.getName());  // search a JPanel holder for the relation and navigation commands.
                    
                    EntityEditorInterface entityEditor = EditorFactory.getEntityEditor(this, puName, memberClass, getLayoutPath() + "." + entityProperty.getName());
                    compGetter = entityEditor.getClass().getMethod("getEntity");
                    binding = new org.doe4ejb3.binding.JComponentDataBinding(entityEditor, compGetter, editor, entityProperty);
                    
                    Object value = entityProperty.getValue();
                    if(value != null) entityEditor.setEntity(value);
                    else entityEditor.newEntity(memberClass);
                    
                    // TODO: migrate to jsr295 beans binding
                    
                    JComponent entityEditorUI = entityEditor.getJComponent();
                    if(entityEditorUI == null) {
                        isNewComponent = false;
                        comp = null;
                    } else {
                        if(container == null) {
                            entityEditorUI.setBorder(javax.swing.BorderFactory.createTitledBorder(I18n.getEntityName(memberClass)));
                            comp = entityEditorUI;
                            isNewComponent = true;
                        } else {
                            container.setLayout(new BorderLayout());
                            container.setMinimumSize(entityEditorUI.getMinimumSize());
                            container.setPreferredSize(entityEditorUI.getPreferredSize());
                            container.add(entityEditorUI, BorderLayout.CENTER);
                            container.putClientProperty("layout", this);
                            comp = container;
                            isNewComponent = false;
                        }
                    }
                    
                    
                } catch(Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
                
            } else {
                
                // Find the best editor for the property:
                comp = EditorFactory.getPropertyEditor(this, puName, entityProperty, maxLength);
                binding = ((JComponent)comp).getClientProperty("dataBinding");
                isNewComponent = ((JComponent)comp).getClientProperty("layout") == null;
                
            }


            // ignore new components in customEditorLayouts
            if(!(isNewComponent && containedInCustomEditorLayout())) {
                if(generatedValue && (!objIsNew)) {
                    // disable binding of generated values:
                    if(binding != null) {
                        if(binding instanceof org.jdesktop.beansbinding.Binding) {
                            org.jdesktop.beansbinding.Binding stdBinding = (org.jdesktop.beansbinding.Binding)binding;
                            stdBinding.bind();    // load initial value of property to UI,
                            stdBinding.unbind();  // but don't commit changes to entity object
                        }
                    }

                    if(comp != null) {
                        if(comp instanceof JScrollPane) {
                            JScrollPane scroll = (JScrollPane)comp;
                            scroll.getViewport().getView().setEnabled(false);
                        } else {
                            comp.setEnabled(false);
                        }
                    }

                } else {

                    bindingContext.addBinding(binding);
                    if( (comp != null) && (comp instanceof JComponent) ) {
                        ((JComponent)comp).putClientProperty("dataBinding", binding);
                    }

                }

                // include new components in layout:
                if( (comp != null) && (isNewComponent) && (!containedInCustomEditorLayout()) ) {
                    if(embedded) {
                        add(new JLabel(""), gbcEmptyLabelForEmbeddedAndCustomLayoutPrinting);  // easy printing
                        add(comp, gbcEmbeddedComponent);
                        setValidUI(true);
                    } else {
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
                        setValidUI(true);
                    }
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
          if(comp instanceof JComponent)
          {
              Component tmp = (Component)((JComponent)comp).getClientProperty("printableContent");
              if(tmp != null) comp = tmp;
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
    // TODO: SMALL COMPONENTS SHOULDN'T BE CUTTED IN TWO PAGES
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

                if(comp instanceof JComponent)
                {
                    Component tmp = (Component)((JComponent)comp).getClientProperty("printableContent");
                    if(tmp != null) comp = tmp;
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
