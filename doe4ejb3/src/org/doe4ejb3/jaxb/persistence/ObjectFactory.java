//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-382 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.05.11 at 07:02:51 PM CEST 
//


package org.doe4ejb3.jaxb.persistence;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.doe4ejb3.jaxb.persistence package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.doe4ejb3.jaxb.persistence
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Persistence.PersistenceUnit.Properties }
     * 
     */
    public Persistence.PersistenceUnit.Properties createPersistencePersistenceUnitProperties() {
        return new Persistence.PersistenceUnit.Properties();
    }

    /**
     * Create an instance of {@link Persistence.PersistenceUnit }
     * 
     */
    public Persistence.PersistenceUnit createPersistencePersistenceUnit() {
        return new Persistence.PersistenceUnit();
    }

    /**
     * Create an instance of {@link Persistence }
     * 
     */
    public Persistence createPersistence() {
        return new Persistence();
    }

    /**
     * Create an instance of {@link Persistence.PersistenceUnit.Properties.Property }
     * 
     */
    public Persistence.PersistenceUnit.Properties.Property createPersistencePersistenceUnitPropertiesProperty() {
        return new Persistence.PersistenceUnit.Properties.Property();
    }

}