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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for menusType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="menusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="menubar" type="{http://jcp.org/jsr/198/extension-manifest}menuBarType"/>
 *           &lt;element name="popup" type="{http://jcp.org/jsr/198/extension-manifest}popupType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "menusType", propOrder = {
    "menubarOrPopup"
})
public class MenusType {

    @XmlElements({
        @XmlElement(name = "menubar", type = MenuBarType.class),
        @XmlElement(name = "popup", type = PopupType.class)
    })
    protected List<IdentifiableType> menubarOrPopup;

    /**
     * Gets the value of the menubarOrPopup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the menubarOrPopup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMenubarOrPopup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MenuBarType }
     * {@link PopupType }
     * 
     * 
     */
    public List<IdentifiableType> getMenubarOrPopup() {
        if (menubarOrPopup == null) {
            menubarOrPopup = new ArrayList<IdentifiableType>();
        }
        return this.menubarOrPopup;
    }

}
