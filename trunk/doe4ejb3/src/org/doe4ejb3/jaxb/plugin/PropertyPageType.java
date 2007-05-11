//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-382 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.05.11 at 07:02:52 PM CEST 
//


package org.doe4ejb3.jaxb.plugin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for propertyPageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="propertyPageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="label" type="{http://jcp.org/jsr/198/extension-manifest}i18n_string"/>
 *         &lt;element name="object-class" type="{http://jcp.org/jsr/198/extension-manifest}className"/>
 *       &lt;/all>
 *       &lt;attribute name="property-page-class" use="required" type="{http://jcp.org/jsr/198/extension-manifest}className" />
 *       &lt;attribute name="parent-page" type="{http://jcp.org/jsr/198/extension-manifest}className" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propertyPageType", propOrder = {

})
public class PropertyPageType {

    @XmlElement(required = true)
    protected I18NString label;
    @XmlElement(name = "object-class", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String objectClass;
    @XmlAttribute(name = "property-page-class", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String propertyPageClass;
    @XmlAttribute(name = "parent-page")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String parentPage;

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link I18NString }
     *     
     */
    public I18NString getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link I18NString }
     *     
     */
    public void setLabel(I18NString value) {
        this.label = value;
    }

    /**
     * Gets the value of the objectClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectClass() {
        return objectClass;
    }

    /**
     * Sets the value of the objectClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectClass(String value) {
        this.objectClass = value;
    }

    /**
     * Gets the value of the propertyPageClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyPageClass() {
        return propertyPageClass;
    }

    /**
     * Sets the value of the propertyPageClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyPageClass(String value) {
        this.propertyPageClass = value;
    }

    /**
     * Gets the value of the parentPage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentPage() {
        return parentPage;
    }

    /**
     * Sets the value of the parentPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentPage(String value) {
        this.parentPage = value;
    }

}
