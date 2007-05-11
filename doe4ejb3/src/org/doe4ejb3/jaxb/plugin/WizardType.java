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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for wizardType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wizardType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="label" type="{http://jcp.org/jsr/198/extension-manifest}i18n_string"/>
 *         &lt;element name="iconpath" type="{http://jcp.org/jsr/198/extension-manifest}i18n_string"/>
 *         &lt;element name="tooltip" type="{http://jcp.org/jsr/198/extension-manifest}i18n_string"/>
 *       &lt;/all>
 *       &lt;attribute name="wizard-class" use="required" type="{http://jcp.org/jsr/198/extension-manifest}className" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wizardType", propOrder = {

})
@XmlSeeAlso({
    org.doe4ejb3.jaxb.plugin.WizardsType.Wizard.class
})
public class WizardType {

    @XmlElement(required = true)
    protected I18NString label;
    @XmlElement(required = true)
    protected I18NString iconpath;
    @XmlElement(required = true)
    protected I18NString tooltip;
    @XmlAttribute(name = "wizard-class", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String wizardClass;

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
     * Gets the value of the iconpath property.
     * 
     * @return
     *     possible object is
     *     {@link I18NString }
     *     
     */
    public I18NString getIconpath() {
        return iconpath;
    }

    /**
     * Sets the value of the iconpath property.
     * 
     * @param value
     *     allowed object is
     *     {@link I18NString }
     *     
     */
    public void setIconpath(I18NString value) {
        this.iconpath = value;
    }

    /**
     * Gets the value of the tooltip property.
     * 
     * @return
     *     possible object is
     *     {@link I18NString }
     *     
     */
    public I18NString getTooltip() {
        return tooltip;
    }

    /**
     * Sets the value of the tooltip property.
     * 
     * @param value
     *     allowed object is
     *     {@link I18NString }
     *     
     */
    public void setTooltip(I18NString value) {
        this.tooltip = value;
    }

    /**
     * Gets the value of the wizardClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWizardClass() {
        return wizardClass;
    }

    /**
     * Sets the value of the wizardClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWizardClass(String value) {
        this.wizardClass = value;
    }

}
