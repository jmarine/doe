/*
 * Created on Feb 6, 2005
 *
 * Copyright 2004 Chris Nelson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 * Modified by Jordi Mariné Fort (18/8/2006)
 */
package org.doe4ejb3.test.recipe;
import org.jdesktop.application.Action;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.doe4ejb3.annotation.PropertyDescriptor;
import org.doe4ejb3.util.DOEUtils;


@Entity
@NamedQueries({
    @NamedQuery(name="searchPublished",query="SELECT OBJECT(r) FROM Recipe r WHERE r.published = true"),
    @NamedQuery(name="searchByCategory",query="SELECT OBJECT(r) FROM Recipe r WHERE r.category = :category")
})
public class Recipe implements java.io.Serializable
{
    private Integer id;

    private String title;

    private String description;

    private Date date;
    
    private byte photo[];
    
    private boolean published;

    private Category category;
    
    private String instructions;
    
    private Set<Ingredient> ingredients = new HashSet<Ingredient>();
    


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId()
    {
        return id;
    }

    protected void setId(Integer id)
    {
        this.id = id;
    }

    @PropertyDescriptor(index=1)
    @Column(name="TITLE", nullable=false, unique=true, length=100)
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @PropertyDescriptor(index=2)
    @Column(name="DESCRIPTION", length=2000)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
    

    @PropertyDescriptor(index=3, displayName="First Cooked On")
    @Temporal(value=TemporalType.DATE)
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }


    @ManyToOne
    @PropertyDescriptor(index=4)
    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }


    @OneToMany(fetch=FetchType.EAGER)
    @PropertyDescriptor(index=5)
    //@Collection(child = true)
    public Set<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }
    

    @PropertyDescriptor(index=6)
    @Column(name="INSTRUCTIONS", length=4000)
    public String getInstructions()
    {
        return instructions;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }
    
    @PropertyDescriptor(index=7,editorClassName="org.doe4ejb3.gui.ImagePropertyEditor",displayName="Photo", width=300, height=175)
    @Lob
    @Column(name = "PHOTO", columnDefinition="BLOB")
    public byte[] getPhoto()
    {
        return this.photo;
    }

    public void setPhoto(byte photo[])
    {
        this.photo = photo;
    }  

   
    @PropertyDescriptor(index=8)
    @Column(name="PUBLISHED", nullable=true)
    public boolean getPublished() 
    {
        return published;
    }

    public void setPublished(boolean published) 
    {
        this.published = published;
    }


    @Override
    public boolean equals(Object obj)
    {
        if( (obj != null) && (obj instanceof Recipe) ) {
            Recipe other = (Recipe)obj;
            return id.equals(other.getId());
        }
        return false;
    }  

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }
    
    @Override
    public String toString()
    {
        return title;
    }
    
    @Action
    public void cookIt()
    {
        DOEUtils.getWindowManager().showMessageDialog("Ready to eat.", "CookIt result", JOptionPane.INFORMATION_MESSAGE);
    }

}
