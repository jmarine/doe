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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for toolbarsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="toolbarsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="toolbar" type="{http://jcp.org/jsr/198/extension-manifest}sectionContainerType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "toolbarsType", propOrder = {
    "toolbar"
})
public class ToolbarsType {

    @XmlElement(required = true)
    protected List<SectionContainerType> toolbar;

    /**
     * Gets the value of the toolbar property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the toolbar property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getToolbar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SectionContainerType }
     * 
     * 
     */
    public List<SectionContainerType> getToolbar() {
        if (toolbar == null) {
            toolbar = new ArrayList<SectionContainerType>();
        }
        return this.toolbar;
    }

}
