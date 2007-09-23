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
public class Course implements Serializable {

    private Integer id;
    
    private String name;
    
    private float price;
    
    private Set<Teacher> teachers = new HashSet<Teacher>();
    
 
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId()
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    @Column(name="NAME", length=50)
    public String getName()
    {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Column(name="PRICE")
    public float getPrice()
    {
        return price;
    }
    /**
     * @param id The id to set.
     */
    public void setPrice(float price)
    {
        this.price = price;
    }
    

    @JoinTable(name = "COURSE_TEACHER", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "teacher_ssn")) 
    @ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE })  // don't delete ingredients used in other recipes
    public Set<Teacher> getTeachers()
    {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers)
    {
        this.teachers = teachers;
    }

    
    public boolean equals(Object obj)
    {
        if( (obj != null) && (obj instanceof Course) ) {
            Course other = (Course)obj;
            return id.equals(other.getId());
        }
        return false;
    }

    public int hashCode()
    {
        return id.hashCode();
    }
    
    public String toString()
    {
       return getName();
    }

}
