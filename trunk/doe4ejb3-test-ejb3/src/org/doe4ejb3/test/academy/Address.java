/*
 * Address.java
 * 
 * Created on Sep 2, 2007, 11:55:17 AM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.test.academy;

import java.io.Serializable;
import javax.persistence.*;


@Embeddable
public class Address implements Serializable {

    private String location;

    private String city;

    private String state;

    private String country;

    private String zip;


    @Column(name = "location", length=30)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    @Column(name = "city", length=30)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    @Column(name = "state", length=30)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    @Column(name = "country", length=2)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Column(name = "zip", length=6)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
}
