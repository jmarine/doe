//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b17-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.08.28 at 08:29:25 PM CEST 
//


package org.doe4ejb3.jaxb.plugin;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for radioGroupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="radioGroupType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://jcp.org/jsr/198/extension-manifest}identifiableType">
 *       &lt;sequence>
 *         &lt;element name="radio-item" type="{http://jcp.org/jsr/198/extension-manifest}itemType" maxOccurs="unbounded" minOccurs="2"/>
 *       &lt;/sequence>
 *       &lt;attribute name="weight" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "radioGroupType", propOrder = {
    "radioItem"
})
public class RadioGroupType
    extends IdentifiableType
{

    @XmlElement(name = "radio-item", required = true)
    protected List<ItemType> radioItem;
    @XmlAttribute
    protected Float weight;

    /**
     * Gets the value of the radioItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the radioItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRadioItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemType }
     * 
     * 
     */
    public List<ItemType> getRadioItem() {
        if (radioItem == null) {
            radioItem = new ArrayList<ItemType>();
        }
        return this.radioItem;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setWeight(Float value) {
        this.weight = value;
    }

}
