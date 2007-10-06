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
    
    @ManyToMany
    @JoinTable(name = "COURSE_TEACHER", joinColumns = @JoinColumn(name = "teacher_ssn"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    public Set<Course> getCourses()
    {
        return courses;
    }

    public void setCourses(Set<Course> courses)
    {
        this.courses = courses;
    }

}
