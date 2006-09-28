/*
 * OrderAnnotation.java
 *
 * Created on 29 July 2006, 21:55
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Jordi Marine Fort
 */
@Target({ElementType.METHOD, ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyDescriptor {
    int index() default 0;
    String displayName() default "";
    String resourceBundle() default "";
    String format() default "";
}
