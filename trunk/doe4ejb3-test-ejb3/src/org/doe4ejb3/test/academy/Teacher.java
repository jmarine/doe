/*
 * Person.java
 * 
 * Created on Sep 2, 2007, 11:47:23 AM
 * @author Jordi Mariné Fort
 */

package org.doe4ejb3.test.academy;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;
import org.doe4ejb3.annotation.EntityDescriptor;


@Entity
public class Teacher extends Person 
{
    private Set<Course> courses = new HashSet<Course>();
    
    @ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE })  // don't delete ingredients used in other recipes
    public Set<Course> getCourses()
    {
        return courses;
    }

    public void setCourses(Set<Course> courses)
    {
        this.courses = courses;
    }

}
