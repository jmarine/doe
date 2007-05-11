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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for actionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="action" type="{http://jcp.org/jsr/198/extension-manifest}actionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="action-override" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="invoke-class" type="{http://jcp.org/jsr/198/extension-manifest}className"/>
 *                   &lt;element name="update-class" type="{http://jcp.org/jsr/198/extension-manifest}className" minOccurs="0"/>
 *                 &lt;/all>
 *                 &lt;attribute name="action-ref" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actionsType", propOrder = {
    "action",
    "actionOverride"
})
public class ActionsType {

    protected List<ActionType> action;
    @XmlElement(name = "action-override")
    protected List<ActionsType.ActionOverride> actionOverride;

    /**
     * Gets the value of the action property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the action property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActionType }
     * 
     * 
     */
    public List<ActionType> getAction() {
        if (action == null) {
            action = new ArrayList<ActionType>();
        }
        return this.action;
    }

    /**
     * Gets the value of the actionOverride property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionOverride property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActionOverride().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActionsType.ActionOverride }
     * 
     * 
     */
    public List<ActionsType.ActionOverride> getActionOverride() {
        if (actionOverride == null) {
            actionOverride = new ArrayList<ActionsType.ActionOverride>();
        }
        return this.actionOverride;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="invoke-class" type="{http://jcp.org/jsr/198/extension-manifest}className"/>
     *         &lt;element name="update-class" type="{http://jcp.org/jsr/198/extension-manifest}className" minOccurs="0"/>
     *       &lt;/all>
     *       &lt;attribute name="action-ref" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class ActionOverride {

        @XmlElement(name = "invoke-class", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String invokeClass;
        @XmlElement(name = "update-class")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String updateClass;
        @XmlAttribute(name = "action-ref", required = true)
        @XmlSchemaType(name = "anySimpleType")
        protected String actionRef;

        /**
         * Gets the value of the invokeClass property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInvokeClass() {
            return invokeClass;
        }

        /**
         * Sets the value of the invokeClass property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInvokeClass(String value) {
            this.invokeClass = value;
        }

        /**
         * Gets the value of the updateClass property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUpdateClass() {
            return updateClass;
        }

        /**
         * Sets the value of the updateClass property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUpdateClass(String value) {
            this.updateClass = value;
        }

        /**
         * Gets the value of the actionRef property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActionRef() {
            return actionRef;
        }

        /**
         * Sets the value of the actionRef property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActionRef(String value) {
            this.actionRef = value;
        }

    }

}
