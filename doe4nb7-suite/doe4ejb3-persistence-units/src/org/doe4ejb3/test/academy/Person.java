/*
 * Person.java
 * 
 * Created on Sep 2, 2007, 11:47:23 AM
 * @author Jordi Mariné Fort
 */

package org.doe4ejb3.test.academy;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

import org.doe4ejb3.annotation.EntityDescriptor;
import org.doe4ejb3.annotation.PropertyDescriptor;


@Entity()
@Inheritance(strategy = InheritanceType.JOINED)
@EntityDescriptor(layoutClassName="org.doe4ejb3.test.academy.PersonEditor")
public class Person implements Serializable {

    private String ssn;
    
    private String firstName;
    
    private String lastName;

    private Gender gender;
    
    private Date bornDay;
    
    private Address address;

    
    @Id
    @Column(name="ssn", length=10)
    @PropertyDescriptor(index=1, showInLists=true)    
    public String getSsn() {
        return ssn;
    }

    protected void setSsn(String ssn) throws IllegalArgumentException {
        try {
            Integer.parseInt(ssn);
        } catch(Exception ex) {
            throw new IllegalArgumentException("SSN must be a number");
        }
        this.ssn = ssn;
    }

        
    @Column(name="firstName", length=50)
    @PropertyDescriptor(index=2, showInLists=true)    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name="lastName", length=50)
    @PropertyDescriptor(index=3, showInLists=true)    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="gender", length=10)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    
    @Column(name="bornDay")
    public Date getBornDay() {
        return bornDay;
    }

    public void setBornDay(Date bornDay) {
        this.bornDay = bornDay;
    }

    

    @Embedded
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public boolean equals(Object obj)
    {
        if( (obj != null) && (obj instanceof Person) ) {
            Person other = (Person)obj;
            return ssn.equals(other.getSsn());
        }
        return false;
    }

    public int hashCode()
    {
        return ssn.hashCode();
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if(firstName != null) sb.append(firstName);
        sb.append(' ');
        if(lastName != null) sb.append(lastName);
        return sb.toString().trim();
    }

}
