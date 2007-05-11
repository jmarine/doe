//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-382 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.05.11 at 07:02:52 PM CEST 
//


package org.doe4ejb3.jaxb.plugin;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for menuType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="menuType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://jcp.org/jsr/198/extension-manifest}identifiableType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="label" type="{http://jcp.org/jsr/198/extension-manifest}i18n_string" minOccurs="0"/>
 *         &lt;element name="mnemonic" type="{http://jcp.org/jsr/198/extension-manifest}i18n_char" minOccurs="0"/>
 *         &lt;element name="tooltip" type="{http://jcp.org/jsr/198/extension-manifest}i18n_string" minOccurs="0"/>
 *         &lt;element name="iconpath" type="{http://jcp.org/jsr/198/extension-manifest}i18n_string" minOccurs="0"/>
 *         &lt;element name="section" type="{http://jcp.org/jsr/198/extension-manifest}sectionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://jcp.org/jsr/198/extension-manifest}menuPositionAttributes"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "menuType", propOrder = {
    "label",
    "mnemonic",
    "tooltip",
    "iconpath",
    "section"
})
public class MenuType
    extends IdentifiableType
{

    protected I18NString label;
    protected I18NChar mnemonic;
    protected I18NString tooltip;
    protected I18NString iconpath;
    protected List<SectionType> section;
    @XmlAttribute
    protected String before;
    @XmlAttribute
    protected String after;

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
     * Gets the value of the mnemonic property.
     * 
     * @return
     *     possible object is
     *     {@link I18NChar }
     *     
     */
    public I18NChar getMnemonic() {
        return mnemonic;
    }

    /**
     * Sets the value of the mnemonic property.
     * 
     * @param value
     *     allowed object is
     *     {@link I18NChar }
     *     
     */
    public void setMnemonic(I18NChar value) {
        this.mnemonic = value;
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
     * Gets the value of the section property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the section property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SectionType }
     * 
     * 
     */
    public List<SectionType> getSection() {
        if (section == null) {
            section = new ArrayList<SectionType>();
        }
        return this.section;
    }

    /**
     * Gets the value of the before property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBefore() {
        return before;
    }

    /**
     * Sets the value of the before property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBefore(String value) {
        this.before = value;
    }

    /**
     * Gets the value of the after property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAfter() {
        return after;
    }

    /**
     * Sets the value of the after property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAfter(String value) {
        this.after = value;
    }

}
