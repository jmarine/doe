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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vfsHookType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vfsHookType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="helper" type="{http://jcp.org/jsr/198/extension-manifest}vfsHelperType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vfsHookType", propOrder = {
    "helper"
})
public class VfsHookType {

    @XmlElement(required = true)
    protected List<VfsHelperType> helper;

    /**
     * Gets the value of the helper property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the helper property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHelper().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VfsHelperType }
     * 
     * 
     */
    public List<VfsHelperType> getHelper() {
        if (helper == null) {
            helper = new ArrayList<VfsHelperType>();
        }
        return this.helper;
    }

}
