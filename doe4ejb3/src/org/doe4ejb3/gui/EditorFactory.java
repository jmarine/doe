/**
 * EditorFactory.java
 *
 * Created on 18 / august / 2006, 21:38
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Converter;

import org.doe4ejb3.annotation.EntityDescriptor;
import org.doe4ejb3.beans.TemporalTypeEditorSupport;
import org.doe4ejb3.binding.EntityProperty;
import org.doe4ejb3.binding.JComponentDataBinding;
import org.doe4ejb3.binding.StatefulProperty;
import org.doe4ejb3.event.ClipboardAction;
import org.doe4ejb3.event.EntityEvent;
import org.doe4ejb3.event.EntityListener;
import org.doe4ejb3.event.EntityTransferHandler;
import org.doe4ejb3.exception.ApplicationException;
import org.doe4ejb3.util.DOEUtils;
import org.doe4ejb3.util.JPAUtils;


public class EditorFactory 
{
    /**
     * Creates a new instance of EditorFactory
     */
    public static EntityEditorInterface getEntityEditor(EditorLayoutInterface parentLayout, String puName, Class entityClass, String childLayoutPath) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        // search for @EntityDescriptor annotation in @Entity/@Embedded class.
        EntityEditorInterface entityEditor = null;
        EntityDescriptor entityDescriptor = (EntityDescriptor)entityClass.getAnnotation(EntityDescriptor.class);
        if( (entityDescriptor != null) && (entityDescriptor.editorClassName() != null) && (entityDescriptor.editorClassName().length() > 0) ) {
            entityEditor = (EntityEditorInterface)Class.forName(entityDescriptor.editorClassName()).newInstance();
        } else {
            entityEditor = new EntityEditorImpl();
        }
        // inject useful information to editor:
        entityEditor.setPersistenceUnitName(puName);
        entityEditor.setEntityDescriptor(entityDescriptor);
        entityEditor.setLayoutPath(parentLayout, childLayoutPath);
        return entityEditor;
    }
    
    public static JComponent getPropertyEditor(EditorLayoutInterface layout, final String puName, StatefulProperty property, int maxLength)
    {
        JComponent comp = null;
        java.beans.PropertyEditor editor = null;        
        Object binding = null;

        TemporalTypeEditorSupport.registerTemporalTypeEditors();
        
        boolean isCollection = false;
        Class memberClass = null;
        try {
            memberClass = property.getType();
            if(java.util.Collection.class.isAssignableFrom(memberClass)) {
                isCollection = true;
                System.out.println("EditorFactory: property " + property.getName() + " is a collection");
                ParameterizedType paramType = (ParameterizedType)property.getGenericType();
                if(paramType != null) memberClass = (Class)(paramType.getActualTypeArguments()[0]);
            } else {
                System.out.println("EditorFactory: property " + property.getName() + " is not a collection");
            }
        } catch(Exception ex) {
            throw new RuntimeException("Property type error: " + ex.getMessage());
        }
        
        // relations editors
        if(comp == null) {
            if(memberClass.getAnnotation(javax.persistence.Entity.class) != null) {
                if(isCollection) {
                    // OneToMany || ManyToMany
                    try {
                        System.out.println("EditorFactory: OneToMany or ManyToMany!!!");
                        Object bindingOutParam[] = new Object[1];
                        comp = getCollectionEditor(layout, puName, property, memberClass, false, bindingOutParam);
                        binding = bindingOutParam[0];

                    } catch(Exception ex) {
                        System.out.println("Error loading property: " + ex.getMessage());
                        ex.printStackTrace();
                    }

                } else {

                    // OneToOne || ManyToOne
                    try {
                        Object container = layout.getComponentFromEditorLayout(JComboBox.class, property.getName());

                        final Class optionClass = memberClass;
                        final javax.swing.DefaultComboBoxModel comboBoxModel = new javax.swing.DefaultComboBoxModel();            
                        final JComboBox combo = ((container != null) && (JComboBox.class.isAssignableFrom(container.getClass())))? (JComboBox)container : new JComboBox();
                        combo.setModel(comboBoxModel);

                        // define combobox prototype dimensions
                        EntityTransferHandler entityTransferHandler = new EntityTransferHandler(memberClass, true);
                        combo.setMinimumSize(new java.awt.Dimension(50,26));  // it was too wided
                        combo.setTransferHandler(entityTransferHandler);
                        combo.setPrototypeDisplayValue("sample value to calculate drop-down list dimension for combobox!");  // define width dimension
                        for(int i = 0; i < 10; i++) combo.addItem(null);  // define height dimension

                        // define some actions and listeners:
                        final EntityListener relationshipListener = new EntityListener() {
                            public void entityChanged(EntityEvent event) {
                                if(event.getEventType() == EntityEvent.ENTITY_INSERT) {
                                     comboBoxModel.addElement(event.getNewEntity());
                                     comboBoxModel.setSelectedItem(event.getNewEntity());
                                } else {
                                    int oldPos = comboBoxModel.getIndexOf(event.getOldEntity());
                                    boolean wasSelected = (oldPos == combo.getSelectedIndex());                                                        
                                    if(oldPos > 0) {
                                        if(event.getEventType() == EntityEvent.ENTITY_DELETE) {
                                            comboBoxModel.removeElementAt(oldPos);
                                            if(wasSelected) combo.setSelectedIndex(0);
                                        } else if(event.getEventType() == EntityEvent.ENTITY_UPDATE) {
                                            comboBoxModel.removeElementAt(oldPos);
                                            comboBoxModel.insertElementAt(event.getNewEntity(), oldPos);
                                            if(wasSelected) combo.setSelectedIndex(oldPos);
                                        }
                                    }
                                }
                            }
                        };
                            
                        final AbstractAction editItemAction = new AbstractAction("Edit", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/edit.png"))) {
                            public void actionPerformed(ActionEvent evt)  {
                                try { 
                                    if(combo.getSelectedIndex() >= 0) {
                                        Object entity = comboBoxModel.getSelectedItem();
                                        WindowManager wm = DOEUtils.getWindowManager();
                                        Object window = DOEUtils.openInternalFrameEntityEditor(puName, optionClass, entity);
                                        final EventListenerList listenerList = wm.getEventListenerList(window);
                                        listenerList.add(EntityListener.class, relationshipListener);
                                    }
                                } catch(ApplicationException ex) { 
                                    DOEUtils.getWindowManager().showMessageDialog( ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                                } catch(Exception ex) { 
                                    DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                                }
                            }
                        };         

                        AbstractAction newItemAction = new AbstractAction("New", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/new.png"))) {
                            public void actionPerformed(ActionEvent evt)  {
                                try { 
                                    WindowManager wm = DOEUtils.getWindowManager();
                                    Object window = DOEUtils.openInternalFrameEntityEditor(puName, optionClass, null);
                                    final EventListenerList listenerList = wm.getEventListenerList(window);
                                    listenerList.add(EntityListener.class, relationshipListener);
                                } catch(ApplicationException ex) { 
                                    DOEUtils.getWindowManager().showMessageDialog( ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                                } catch(Exception ex) { 
                                    DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                                }
                            }
                        };

                        combo.addItemListener(new ItemListener() {
                            public void itemStateChanged(ItemEvent e) {
                                boolean newState = (combo.getSelectedIndex() > 0);
                                editItemAction.setEnabled(newState);
                            }
                        });
                        
                        combo.addPopupMenuListener(new PopupMenuListener() {
                            public void popupMenuCanceled(PopupMenuEvent e) { }
                            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
                            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                                // Lazy load of drop-down list items:
                                if(combo.getClientProperty("lazyModel") == null) 
                                {
                                    try {
                                        combo.putClientProperty("lazyModel", comboBoxModel);

                                        combo.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                        Object selectedItem = comboBoxModel.getSelectedItem();
                                        comboBoxModel.removeAllElements();
                                        comboBoxModel.addElement(null);
                                        System.out.println("Searching items!!!");
                                        for(Object option : JPAUtils.findAllEntities(puName, optionClass).toArray()) {
                                            comboBoxModel.addElement(option);
                                        }
                                        comboBoxModel.setSelectedItem(selectedItem);

                                        // combo.hidePopup();
                                        // combo.showPopup();

                                        // combo.getUI().setPopupVisible( combo, true );

                                    } finally {
                                        combo.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                        combo.putClientProperty("lazyModel", null);
                                    }
                                }

                            }
                        });
                        

                        // define UI:
                        if((container != null) && (JComboBox.class.isAssignableFrom(container.getClass()))) {
                            combo.putClientProperty("layout", layout);  // existing JComboBox or JPanel
                            comp = combo;  // without navigation buttons
                        } else {
                            JPanel panel = ((container != null) && (JPanel.class.isAssignableFrom(container.getClass()))) ? (JPanel)container : new JPanel();
                            if(container != null) panel.putClientProperty("layout", layout);  // existing JComboBox or JPanel
                            panel.setLayout(new BorderLayout());
                            panel.add(combo, BorderLayout.CENTER);
                            comp = panel;

                            if(property instanceof EntityProperty) {
                                // include navigation buttons
                                JPanel jButtonsPanel = new JPanel();
                                jButtonsPanel.setLayout(new java.awt.FlowLayout(FlowLayout.LEFT, 0, 0));

                                JButton jButtonEdit = new JButton(editItemAction);
                                jButtonEdit.setHideActionText(true);
                                jButtonsPanel.add(jButtonEdit);
                                
                                JButton jButtonNew = new JButton(newItemAction);
                                jButtonNew.setHideActionText(true);
                                jButtonsPanel.add(jButtonNew);

                                java.awt.Insets buttonMargin = jButtonNew.getMargin();
                                buttonMargin = new java.awt.Insets(buttonMargin.top, 1, buttonMargin.bottom, 1);
                                jButtonNew.setMargin(buttonMargin);
                                jButtonEdit.setMargin(buttonMargin);

                                panel.add(jButtonsPanel, BorderLayout.EAST);
                            }
                        }


                        // Original binding (with setup of initial value):
                        Method compGetter = combo.getClass().getMethod("getSelectedItem");
                        binding = new JComponentDataBinding(combo, compGetter, null, property);

                        Object value = property.getValue();
                        if(value != null) {
                            combo.addItem(value);
                            combo.setSelectedItem(value);
                        } else {
                            combo.setSelectedIndex(0);
                        }

                        
                        /* Using beans binding 1.0 (previous version 0.6.1 didn't work with Glassfish v2)
                        // TODO? change StatefulProperty "property" --> source + beansbinding 1.0 stateless property
                        System.out.println("WARNING: jsr295 binding of PropertyDescriptor with JComboBox  (which requires beansbinding.jar endorsed in JavaWebStart's JRE)");
                        Object value = property.getValue();
                        if(value != null) combo.addItem(value);
                        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, property, BeanProperty.create("value"), combo, BeanProperty.create("selectedItem"));
                        */
                        
                    } catch(Exception ex) {
                        System.out.println("Error loading property: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }

            } 
        }

        
        // custom property editors
        if(comp == null) {
            if(property instanceof EntityProperty) {
                EntityProperty entityProperty = (EntityProperty)property;
                org.doe4ejb3.annotation.PropertyDescriptor pd = entityProperty.getAnnotation(org.doe4ejb3.annotation.PropertyDescriptor.class);
                if( (pd != null) && (pd.editorClassName() != null) && (pd.editorClassName().length() > 0) ) {
                    try {
                        Class editorClass = Class.forName(pd.editorClassName());
                        Object container = layout.getComponentFromEditorLayout(editorClass, property.getName());

                        org.doe4ejb3.gui.PropertyEditorInterface propertyComponent = null;
                        if((container != null) && (editorClass.isAssignableFrom(container.getClass()))) {  // normally it will be a JPanel container, but it can also be the real editor UI (with dual interface)
                            propertyComponent = (org.doe4ejb3.gui.PropertyEditorInterface)container;
                        } else {
                            propertyComponent = (org.doe4ejb3.gui.PropertyEditorInterface)(editorClass.newInstance());
                        }
                        
                        comp = propertyComponent.getJComponent();
                        if(container != null) {
                            if(JPanel.class.isAssignableFrom(container.getClass())) {
                                JPanel panel = (JPanel)container;
                                panel.setLayout(new BorderLayout());
                                panel.add(comp, BorderLayout.CENTER);
                                comp = panel;
                            }
                            comp.putClientProperty("layout", layout);
                        }
                        
                        // set size when required (ie: ImagePropertyEditor)
                        if(pd.width() != 0 && pd.height() != 0) {
                            propertyComponent.setDimension(new java.awt.Dimension(pd.width(), pd.height()));
                            comp.putClientProperty("fixedSize", "true");
                        }


                        // Original binding (with setup of initial value):
                        Method editorGetter = propertyComponent.getClass().getMethod("getValue");
                        Object value = property.getValue();
                        if(value != null) propertyComponent.setValue(value);
                        binding = new JComponentDataBinding(propertyComponent, editorGetter, null, property);  // editor is null to get real value from "editor.getValue" method (no conversion to string representation).                
                        
                    
                        /* Using beans binding 1.0 (previous version 0.6.1 didn't work with Glassfish v2)
                        // TODO? change StatefulProperty "property" --> source + beansbinding 1.0 stateless property
                        System.out.println("WARNING: jsr295 binding of PropertyDescriptor with editorClassName  (which requires beansbinding.jar endorsed in JavaWebStart's JRE)");
                        binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, property, BeanProperty.create("value"), propertyComponent, BeanProperty.create("value"));
                        */
                        
                    } catch(Exception ex) {
                        comp = null;
                        System.out.println("Error creating custom property editor: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        }

        
        // normal property editors
        // TODO: enable use of custom layout predefined components/JPanel holders.
        if(comp == null) {
            try {
                editor = findEditor(memberClass);
                System.out.println("EditorFactory: property editor for " + memberClass.getName() + "=" + editor);

                Component customComponent = null;
                try {
                    if( (editor != null) && (editor.supportsCustomEditor()) ) {
                        // Note: Netbeans' customized editors need the "attachEnv" method call (otherwise, they generate a NullPointerException)
                        customComponent = editor.getCustomEditor();
                    }
                } catch(Exception ex) {
                    System.out.println("WARNING: " + ex.getMessage());
                    //ex.printStackTrace();
                }
                
                if(customComponent != null) {
                    // Note: a JComponent will be needed to putClientProperty("dataBinding", binding)
                    Object container = layout.getComponentFromEditorLayout(JPanel.class, property.getName());  // search a JPanel holder for the customComponent generated by the PropertyEditor.
                    if( (container == null) && (customComponent instanceof JComponent) ) {
                        comp = (JComponent)customComponent;
                    } else {  // convert to JComponent or integrate in customized layout holder:
                        JPanel panel = (container != null) ? (JPanel)container : new JPanel();  
                        if(container != null) panel.putClientProperty("layout", layout);
                        panel.setLayout(new FlowLayout());
                        panel.add(customComponent);
                        comp = panel;
                    }

                    // Original binding (with setup of initial value):
                    Method editorGetter = editor.getClass().getMethod("getValue");
                    Object value = property.getValue();
                    if(value != null) editor.setValue(value);
                    binding = new JComponentDataBinding(editor, editorGetter, null, property);  // the 3 parameter is null to get real value from "editor.getValue" method (no conversion to string representation).

                    /** 
                     * Using beans binding 1.0 (previous version 0.6.1 didn't work with Glassfish v2)
                     * Warning: the target has been configured with a PropertyEditor, that should fire property changes to actually bind the edited values back to entity object
                     * (so it rather never works, because modifications are made with the custom editor and the propertychange events aren't normally propagated to PropertyEditor listeners)
                    final java.beans.PropertyEditor editorFinal = editor;
                    comp.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                       public void propertyChange(PropertyChangeEvent evt) {
                            // ¿should the real value be filtered with evt.getPropertyName()?
                            if(editorFinal instanceof PropertyEditorSupport) {
                                ((PropertyEditorSupport)editorFinal).firePropertyChange();
                            } else {
                                System.out.println("WARNING: jsr295 binding of non PropertyEditorSupport: remember to propagate UI changes from custom editor to PropertyEditor listeners");
                            }
                       }
                    });
                    
                    System.out.println("WARNING: jsr295 binding of PropertyEditor with custom control (which requires beansbinding.jar endorsed in JavaWebStart's JRE)");
                    binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, property, BeanProperty.create("value"), editor, BeanProperty.create("value"));
                    */


                } else if( (editor != null) && ((memberClass == Boolean.TYPE) || (java.lang.Boolean.class.isAssignableFrom(memberClass))) ) { 
                    Object container = layout.getComponentFromEditorLayout(JCheckBox.class, property.getName());
                    
                    JCheckBox checkBox = ((container != null) && (JCheckBox.class.isAssignableFrom(container.getClass())))? (JCheckBox)container : new JCheckBox();
                    comp = checkBox;
                    if(container != null) {
                        if(JPanel.class.isAssignableFrom(container.getClass())) {
                            JPanel panel = (JPanel)container;  
                            panel.setLayout(new FlowLayout());
                            panel.add(checkBox);
                            comp = panel;
                        }
                        comp.putClientProperty("layout", layout);
                    }
                    comp.putClientProperty("fixedSize", "true");

                    // Original binding (with setup of initial value):
                    Method compGetter = checkBox.getClass().getMethod("isSelected");
                    binding = new JComponentDataBinding(checkBox, compGetter, editor, property);

                    Object booleanObject = property.getValue();
                    Boolean value = (Boolean)booleanObject;
                    if(value != null) {
                        checkBox.setSelected(value.booleanValue());
                    }
                    
                    /* Using beans binding 1.0 (previous version 0.6.1 didn't work with Glassfish v2)
                    // TODO? change StatefulProperty "property" --> source + beansbinding 1.0 stateless property
                    System.out.println("WARNING: jsr295 binding of boolean property (which requires beansbinding.jar endorsed in JavaWebStart's JRE)");                    
                    org.jdesktop.beansbinding.Binding stdBinding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, property, BeanProperty.create("value"), checkBox, BeanProperty.create("selected"));
                    stdBinding.setSourceNullValue(false);
                    binding = stdBinding;
                    */
                   
                } else { // using JTextField or JTextArea depending on Column's length attribute or existing component in customized layout
                    Object container = layout.getComponentFromEditorLayout(JTextComponent.class, property.getName());

                    // TODO: use spinner for small numbers?
                    // adjunt number lengths 
                    if(maxLength != 0) {
                        if( ((memberClass == Byte.TYPE) || (java.lang.Byte.class.isAssignableFrom(memberClass))) ) maxLength = 4;
                        else if( ((memberClass == Short.TYPE) || (java.lang.Short.class.isAssignableFrom(memberClass))) ) maxLength = 6;
                        else if( ((memberClass == Integer.TYPE) || (java.lang.Integer.class.isAssignableFrom(memberClass))) ) maxLength = 12;
                        else if( ((memberClass == Long.TYPE) || (java.lang.Long.class.isAssignableFrom(memberClass))) ) maxLength = 25;
                        else if( ((memberClass == Float.TYPE) || (java.lang.Float.class.isAssignableFrom(memberClass))) ) maxLength = 12;
                        else if( ((memberClass == Double.TYPE) || (java.lang.Double.class.isAssignableFrom(memberClass)) || (java.lang.Number.class.isAssignableFrom(memberClass))) ) maxLength = 40;
                    }

                    JTextComponent textField = null;
                    Method compGetter = null;
                    if( (container != null) && (JTextComponent.class.isAssignableFrom(container.getClass())) ) {
                        textField = (JTextComponent)container;
                        compGetter = textField.getClass().getMethod("getText");
                        comp = textField;
                    } else if( (maxLength >= 0) && (maxLength < 100) ) {
                        textField = (maxLength > 0) ? new JTextField(maxLength) : new JTextField();
                        compGetter = textField.getClass().getMethod("getText");
                        comp = textField;
                        comp.putClientProperty("fixedSize", "true");
                    } else {
                        textField = new javax.swing.JTextPane();
                        compGetter = textField.getClass().getMethod("getText");
                        comp = new JScrollPane(textField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        comp.setPreferredSize(new java.awt.Dimension(400, 80));
                    }

                    if(maxLength > 0) {
                        AbstractDocument doc = (AbstractDocument)textField.getDocument();
                        doc.setDocumentFilter(new DocumentSizeFilter(maxLength));
                    }                    
                    
                    if(container != null) {
                        if(JPanel.class.isAssignableFrom(container.getClass())) {
                            JPanel panel = (JPanel)container;
                            panel.setLayout(new BorderLayout());
                            panel.add(textField, BorderLayout.CENTER);
                            comp = panel;
                        }
                        comp.putClientProperty("layout", layout);                        
                    }
                    
                    
                    // Original binding (with setup of initial value):
                    binding = new JComponentDataBinding(textField, compGetter, editor, property);                        
                    Object value = property.getValue();
                    if(value != null) {
                        if(editor != null) {
                            editor.setValue(value);
                            textField.setText(editor.getAsText());
                        } else {
                            textField.setText(value.toString());
                        }
                    }
                    
                    /* Using beans binding 1.0 (previous version 0.6.1 didn't work with Glassfish v2)
                    // TODO? change StatefulProperty "property" --> source + beansbinding 1.0 stateless property
                    System.out.println("WARNING: jsr295 binding of text property (which requires beansbinding.jar endorsed in JavaWebStart's JRE)");                    
                    org.jdesktop.beansbinding.Binding stdBinding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, property, BeanProperty.create("value"), textField, BeanProperty.create("text"));
                    stdBinding.setSourceNullValue("");
                    binding = stdBinding;
                    
                    Converter converter = null;
                    if( (stdBinding != null) && (editor != null) ) {
                        final java.beans.PropertyEditor editorFinal = editor;
                        System.out.println("Creating binding converter for property: " + property.getName());
                        converter = new Converter() {
                            public Object convertForward(Object arg0) {  // source-to-target
                                System.out.println("**** SOURCE TO TARGET: obj=" + arg0);
                                editorFinal.setValue(arg0);
                                System.out.println("**** SOURCE TO TARGET: text=" + editorFinal.getAsText());
                                return editorFinal.getAsText();
                            }
                            public Object convertReverse(Object arg0) {  // target-to-source
                                System.out.println("**** TARGET TO SOURCE : text=" + arg0);
                                editorFinal.setAsText(arg0.toString());
                                System.out.println("**** TARGET TO SOURCE : value=" + editorFinal.getValue());
                                return editorFinal.getValue();
                            }
                        };
                        stdBinding.setConverter(converter);
                    }
                    */
                    
                }
            } catch(Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

            
        if( (comp != null) && (binding != null) ) {
            comp.putClientProperty("dataBinding", binding);
        }
        
        return comp;
    }
    
    protected static java.beans.PropertyEditor findEditor(Class memberClass)
    {
        // FIXME? invert class order
        if(memberClass.isAssignableFrom(java.lang.Byte.class)) memberClass = Byte.TYPE;
        else if(memberClass.isAssignableFrom(java.lang.Short.class)) memberClass = Short.TYPE;
        else if(memberClass.isAssignableFrom(java.lang.Integer.class)) memberClass = Integer.TYPE;
        else if(memberClass.isAssignableFrom(java.lang.Long.class)) memberClass = Long.TYPE;
        else if(memberClass.isAssignableFrom(java.lang.Float.class)) memberClass = Float.TYPE;
        else if(memberClass.isAssignableFrom(java.lang.Double.class)) memberClass = Double.TYPE;
        return java.beans.PropertyEditorManager.findEditor(memberClass);
    }
    

    /** 
     * Setup an editor for a multi-valued property 
     */
    public static JComponent getCollectionEditor(EditorLayoutInterface layout, final String puName, final StatefulProperty property, final Class memberClass, final boolean isManagerWindow,Object bindingOutParam[]) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, Exception {
        Object container = (property != null) ? layout.getComponentFromEditorLayout(JPanel.class, property.getName()) : null;  // search a JPanel holder for the relation and navigation commands.
        
        final JTable jTable = new JTable();
        final JScrollPane scrollableItems = new JScrollPane(jTable);
        final JPanel panel = (container != null) ? (JPanel)container : new JPanel();  
        
        final DefaultListModel listModel = new DefaultListModel();
        final ListSelectionModel listSelectionModel = jTable.getSelectionModel();
        final EntityTableModel entityTableModel = new EntityTableModel(memberClass, listModel);
        final EntityTransferHandler entityTransferHandler = new EntityTransferHandler(memberClass, !isManagerWindow);

        panel.putClientProperty("printableContent", scrollableItems);
        panel.putClientProperty("scrollPane", scrollableItems);
        panel.putClientProperty("table", jTable);
        panel.putClientProperty("listModel", listModel);
        panel.putClientProperty("listSelectionModel", listSelectionModel);
        if(container != null) panel.putClientProperty("layout", layout);
        
        if(property != null) {
            // populate listmodel with property's values
            Collection values = (Collection)property.getValue();
            System.out.println("Property " + property.getName() + " collection size = " + values);
            if(values != null) {
                Iterator iter = values.iterator();
                while(iter.hasNext()) {
                    Object valueToSelect = iter.next();
                    listModel.addElement(valueToSelect);
                }
            }
            
            // Original binding (with setup of initial value):
            Method modelGetter = listModel.getClass().getMethod("toArray");
            bindingOutParam[0] = new JComponentDataBinding(listModel, modelGetter, null, property);

            
            /* Using beans binding 1.0 (previous version 0.6.1 didn't work with Glassfish v2)
            // TODO? change StatefulProperty "property" --> source + beansbinding 1.0 stateless property
            System.out.println("WARNING: jsr295 binding of PropertyDescriptor with JTable (which requires beansbinding.jar endorsed in JavaWebStart's JRE)");
            bindingOutParam[0] = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, property, BeanProperty.create("value"), entityTableModel, BeanProperty.create("values"));
            */
        }

        // configure actions:
        AbstractAction newAction = new AbstractAction("New", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/new.png"))) {
            public void actionPerformed(ActionEvent evt)  {
                try { 
                    WindowManager wm = DOEUtils.getWindowManager();
                    Object window = DOEUtils.openInternalFrameEntityEditor(puName, memberClass, null);
                    if(!isManagerWindow) {
                        final EventListenerList listenerList = wm.getEventListenerList(window);
                        listenerList.add(EntityListener.class, new EntityListener() {
                            public void entityChanged(EntityEvent event) {
                                if(event.getEventType() == EntityEvent.ENTITY_INSERT) {
                                    listModel.addElement(event.getNewEntity());
                                }
                            }
                        });
                    }
                } catch(ApplicationException ex) { 
                    DOEUtils.getWindowManager().showMessageDialog( ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                } catch(Exception ex) { 
                    DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);  
                    ex.printStackTrace();
                }
            }
        };
        
        AbstractAction addExistingAction = new AbstractAction("Add", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/add.png"))) {
                public void actionPerformed(ActionEvent evt)  {
                    try {
                        panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        List allValues = JPAUtils.findAllEntities(puName, memberClass);
                        Object newItem = JOptionPane.showInternalInputDialog(panel, "Select new item:", "Add new " + I18n.getEntityName(memberClass), JOptionPane.QUESTION_MESSAGE, null, allValues.toArray(), null);
                        if(newItem != null) {
                            // FIXME: caution with duplicated relations and "Set" collection types.
                            if(!listModel.contains(newItem)) {  
                                listModel.addElement(newItem);
                            } else {
                                DOEUtils.getWindowManager().showMessageDialog( "Selected item already exists!", "Error:", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } finally {
                        panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
               }
        };
        
        final AbstractAction editAction = new AbstractAction("Edit", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/edit.png"))) {
            public void actionPerformed(ActionEvent evt)  {
                try { 
                    if(listSelectionModel.isSelectionEmpty()) {
                        throw new ApplicationException("No items selected");
                    } else {
                        for(int index = listSelectionModel.getMaxSelectionIndex(); index >= listSelectionModel.getMinSelectionIndex(); index--) {
                            if(listSelectionModel.isSelectedIndex(index)) {
                                WindowManager wm = DOEUtils.getWindowManager();
                                Object window = DOEUtils.openInternalFrameEntityEditor(puName, memberClass, listModel.getElementAt(index)); 
                                EventListenerList listenerList = wm.getEventListenerList(window);
                                listenerList.add(EntityListener.class, new EntityListener() {
                                    public void entityChanged(EntityEvent event) {
                                        int index;
                                        switch(event.getEventType())
                                        { 
                                            case EntityEvent.ENTITY_UPDATE:
                                                // update JTable
                                                System.out.println("EditorFactory: searching index of OldEntity = " + event.getOldEntity());
                                                System.out.println("EditorFactory: searching index of NewEntity = " + event.getNewEntity());
                                                index = listModel.indexOf(event.getOldEntity());
                                                System.out.println("EditorFactory: index found =  " + index);
                                                if(index != -1) listModel.setElementAt(event.getNewEntity(), index);
                                                else if(listModel.size() > 0) listModel.setElementAt(listModel.getElementAt(0), 0);  // refresh rows/columns
                                                break;
                                                
                                            case EntityEvent.ENTITY_DELETE:
                                                System.out.println("EditorFactory: searching index of OldEntity = " + event.getOldEntity());
                                                index = listModel.indexOf(event.getOldEntity());
                                                System.out.println("EditorFactory: index found =  " + index);
                                                if(index != -1) listModel.removeElementAt(index);
                                                else if(listModel.size() > 0) listModel.setElementAt(listModel.getElementAt(0), 0);  // refresh rows/columns
                                                break;
                                            
                                            default:
                                                System.out.println("EditorFactory: event type not expected: " + event.getEventType());
                                        }
                                    }
                                });
                            }
                        }
                    }
                    
                } catch(ApplicationException ex) { 
                    DOEUtils.getWindowManager().showMessageDialog( ex.getMessage(), "Edit error", JOptionPane.ERROR_MESSAGE);                
                    
                } catch(Exception ex) { 
                    DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Edit error", JOptionPane.ERROR_MESSAGE);                
                    ex.printStackTrace();
                }
           }
        };
        
        final AbstractAction deleteAction = new AbstractAction("Delete", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/delete.png"))) {
            public void actionPerformed(ActionEvent evt)  {
                try {
                    if(listSelectionModel.isSelectionEmpty()) {
                        throw new ApplicationException("No items selected");
                    } else {
                        int confirm = DOEUtils.getWindowManager().showConfirmDialog( "Do you really want to delete selected objects?", "Confirm operation", JOptionPane.OK_CANCEL_OPTION);
                        if(confirm == JOptionPane.OK_OPTION) 
                        {
                            try {
                                panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                for(int index = listSelectionModel.getMaxSelectionIndex(); index >= listSelectionModel.getMinSelectionIndex(); index--) {
                                    if(listSelectionModel.isSelectedIndex(index)) {
                                        System.out.println("EditorFactory: removing selected index: " + index);
                                        if(isManagerWindow) {  // delete command from "EntityManagerPane"
                                            Object entity = listModel.getElementAt(index);
                                            JPAUtils.removeEntity(puName, entity);                                
                                        }
                                        listModel.removeElementAt(index);
                                    }
                                }
                            } finally {
                                panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
                    }

                } catch(ApplicationException ex) { 
                    DOEUtils.getWindowManager().showMessageDialog( ex.getMessage(), "Delete error", JOptionPane.ERROR_MESSAGE);
                    
                } catch(Exception ex) { 
                    DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Delete error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };         

        AbstractAction printAction = new AbstractAction("Print", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/print.png"))) {
                public void actionPerformed(ActionEvent evt)  {
                    try {
                        if(entityTableModel.getRowCount() == 0) {
                            throw new ApplicationException("There aren't rows to print.");
                        } else {
                            MessageFormat headerFormat = new MessageFormat(I18n.getEntityName(memberClass) + " list:");
                            MessageFormat footerFormat = new MessageFormat("Page {0}");
                            jTable.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
                        }
                    } catch(ApplicationException ex) { 
                        DOEUtils.getWindowManager().showMessageDialog( ex.getMessage(), "Printing error", JOptionPane.ERROR_MESSAGE);                
                    } catch(Exception ex) {
                        DOEUtils.getWindowManager().showMessageDialog( "Error: " + ex.getMessage(), "Printing error", JOptionPane.ERROR_MESSAGE);
                    }
                }
        };
        
        AbstractAction closeAction = new AbstractAction("Close", new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/cancel.png"))) {
                public void actionPerformed(ActionEvent evt)  
                {        
                    JComponent source = (JComponent)evt.getSource();
                    WindowManager wm = DOEUtils.getWindowManager();
                    Object window = wm.getWindowFromComponent(source);
                    if(window != null) wm.closeWindow(window);
                }
        };

        // cut, copy & paste actions
        final AbstractAction cutAction = new ClipboardAction("Cut", 't', TransferHandler.getCutAction(), new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/cut.png")));  // org.jdesktop.application.ApplicationContext.getInstance().getActionMap(DomainObjectExplorer.class, DomainObjectExplorer.getInstance()).get("cut"));
        final AbstractAction copyAction = new ClipboardAction("Copy", 'c', TransferHandler.getCopyAction(), new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/copy.png")));  // org.jdesktop.application.ApplicationContext.getInstance().getActionMap(DomainObjectExplorer.class, DomainObjectExplorer.getInstance()).get("copy"));
        final AbstractAction pasteAction = new ClipboardAction("Paste", 'p', TransferHandler.getPasteAction(), new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/paste.png")));  // org.jdesktop.application.ApplicationContext.getInstance().getActionMap(DomainObjectExplorer.class, DomainObjectExplorer.getInstance()).get("paste"));
        
        // setup enable state, and enable change listeners:
        editAction.setEnabled(false);
        deleteAction.setEnabled(false);
        copyAction.setEnabled(false);
        cutAction.setEnabled(false);
        pasteAction.setEnabled(ClipboardAction.getClipboard().isDataFlavorAvailable(entityTransferHandler.getEntityDataFlavor()));
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enabled = !listSelectionModel.isSelectionEmpty();
                editAction.setEnabled(enabled);
                deleteAction.setEnabled(enabled);
                copyAction.setEnabled(enabled);
                cutAction.setEnabled(!isManagerWindow && enabled);
                pasteAction.setEnabled(ClipboardAction.getClipboard().isDataFlavorAvailable(entityTransferHandler.getEntityDataFlavor()));
            }
        });
        

        // configure popup menu
        javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
        popupMenu.add(newAction).setMnemonic('n');
        popupMenu.add(editAction).setMnemonic('e');
        popupMenu.add(new javax.swing.JSeparator());
        popupMenu.add(cutAction);
        popupMenu.add(copyAction);
        popupMenu.add(pasteAction);
        popupMenu.add(new javax.swing.JSeparator());
        popupMenu.add(deleteAction).setMnemonic('d');
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent e) { }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                boolean enabled = !listSelectionModel.isSelectionEmpty();
                editAction.setEnabled(enabled);
                deleteAction.setEnabled(enabled);
                copyAction.setEnabled(enabled);
                cutAction.setEnabled(!isManagerWindow && enabled);
                if(isManagerWindow) pasteAction.setEnabled(false);
                pasteAction.setEnabled(ClipboardAction.getClipboard().isDataFlavorAvailable(entityTransferHandler.getEntityDataFlavor()));
            }
        });



        // configure button panel:
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        if(!isManagerWindow) {  
            // configure "add" button (only used in OneToMany and ManytoMany relationships)
            JButton btnAddExistingItem = new JButton(addExistingAction);
            // btnAddExistingItem.setIcon(new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/add.png")));
            buttonPanel.add(btnAddExistingItem);
        }

        // configure "new" button
        JButton btnNewItem = new JButton(newAction);
        // btnNewItem.setIcon(new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/new.png")));        
        buttonPanel.add(btnNewItem);

        // configure "edit" button
        JButton btnEditItem = new JButton(editAction);
        btnEditItem.addActionListener(editAction);
        // btnEditItem.setIcon(new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/edit.png")));
        buttonPanel.add(btnEditItem);

        // configure "delete" button
        JButton btnDeleteItem = new JButton(deleteAction);
        // btnDeleteItem.setIcon(new javax.swing.ImageIcon(EditorFactory.class.getResource("/org/doe4ejb3/gui/resources/delete.png")));        
        buttonPanel.add(btnDeleteItem);

        if(isManagerWindow) {
            // configure "print" button
            JButton btnPrint = new JButton(printAction);
            buttonPanel.add(btnPrint);

            // configure "close" button
            JButton btnClose = new JButton(closeAction);
            buttonPanel.add(btnClose);
            
            // configure mnemonics:
            btnNewItem.setMnemonic('n');
            btnEditItem.setMnemonic('e');
            btnDeleteItem.setMnemonic('d');
            btnPrint.setMnemonic('p');
            btnClose.setMnemonic('c');
        }

        
        // configure panel layout
        scrollableItems.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrollableItems.setComponentPopupMenu(popupMenu);

        jTable.setModel(entityTableModel);
        jTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable.setCellSelectionEnabled(false);
        jTable.setRowSelectionAllowed(true);
        jTable.setComponentPopupMenu(popupMenu);

        panel.setPreferredSize(new java.awt.Dimension(400, 180));        
        panel.setLayout(new BorderLayout());
        panel.add("Center", scrollableItems);
        panel.add("South", buttonPanel);
        
        // configure drag and drop"
        jTable.setDragEnabled(true);
        jTable.setTransferHandler(entityTransferHandler);
        scrollableItems.setTransferHandler(entityTransferHandler);

        // Configure "copy & paste"
        ActionMap map = jTable.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                TransferHandler.getPasteAction());

        map = scrollableItems.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                TransferHandler.getPasteAction());

        
        return panel;
    }
    
}


