/**
 * EntityDescriptor.java
 *
 * Use this annotation to configure EJB3 entity bean UI, visiblity and icon.
 * 
 * Created on 29 July 2006, 21:55
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityDescriptor 
{
    /**
     * To display the EJB3 entity bean details with a customized editor.
     * The class must have to implement the interface EntityEditorInterface.
     */
    String editorClassName() default "org.doe4ejb3.gui.EntityEditorImpl";

    /**
     * To hide the EJB3 entity bean from menús and navigator panel.
     * (entities are only visible when used as child relations).
     */
    boolean hidden() default false;

    
    /**
     * To configure the icon that should be displayed with the EJB3 entity name 
     * in the navigator panel and internal frames.
     */
    String iconPath() default "";
    
}
