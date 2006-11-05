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
 */
package org.doe4ejb3.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.NamedQuery;

import org.doe4ejb3.annotation.PropertyDescriptor;


@Entity
@NamedQuery(name="searchByCategory",query="SELECT OBJECT(r) FROM Recipe r WHERE r.category = :category")
public class Recipe implements java.io.Serializable
{
    private Integer id;

    private String title;

    private String description;

    private Date date;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    @PropertyDescriptor(index=3,format="MM/dd/yyyy",displayName="First Cooked On")
    @Temporal(value=TemporalType.DATE)
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    private Category category;

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

    private Set<Ingredient> ingredients = new HashSet<Ingredient>();

    @PropertyDescriptor(index=5)
    @OneToMany(cascade = CascadeType.ALL)
    //@Collection(child = true)
    public Set<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }
    
    private String instructions;

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
    
    public String toString()
    {
        return title;
    }

}