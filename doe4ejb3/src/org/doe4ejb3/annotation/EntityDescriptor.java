/*
 * EntityDescriptor.java
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
public @interface EntityDescriptor {
    String editorClassName() default "org.doe4ejb3.gui.EntityEditorImpl";
    String iconPath() default "";
    boolean hidden() default false;
}