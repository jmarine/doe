//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b17-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.08.28 at 08:29:25 PM CEST 
//


package org.doe4ejb3.jaxb.plugin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for propertyHookType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="propertyHookType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://jcp.org/jsr/198/extension-manifest}hookType">
 *       &lt;sequence>
 *         &lt;element name="property-pages" type="{http://jcp.org/jsr/198/extension-manifest}propertyPagesType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propertyHookType", propOrder = {
    "propertyPages"
})
public class PropertyHookType
    extends HookType
{

    @XmlElement(name = "property-pages", required = true)
    protected PropertyPagesType propertyPages;

    /**
     * Gets the value of the propertyPages property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyPagesType }
     *     
     */
    public PropertyPagesType getPropertyPages() {
        return propertyPages;
    }

    /**
     * Sets the value of the propertyPages property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyPagesType }
     *     
     */
    public void setPropertyPages(PropertyPagesType value) {
        this.propertyPages = value;
    }

}
