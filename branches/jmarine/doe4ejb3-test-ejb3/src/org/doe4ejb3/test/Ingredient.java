package org.doe4ejb3.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Ingredient
{

    private Integer id;
    
    private String amount;
    
    private String name;

    @Column(name="AMOUNT", length=20)
    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    //@PropertyDescriptor(index=0)
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

    @Column(name="NAME", length=50)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public boolean equals(Object obj)
    {
        if( (obj != null) && (obj instanceof Ingredient) ) {
            Ingredient otherIngredient = (Ingredient)obj;
            return id.equals(otherIngredient.getId());
        }
        return false;
    }

    public String toString()
    {
        return getAmount() + " " + getName();
    }
}
