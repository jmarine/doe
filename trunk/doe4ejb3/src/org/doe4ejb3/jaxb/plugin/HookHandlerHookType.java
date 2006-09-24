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
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.doe4ejb3.jaxb.plugin.HookHandlerHookType.HookHandler;


/**
 * <p>Java class for hookHandlerHookType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="hookHandlerHookType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://jcp.org/jsr/198/extension-manifest}hookType">
 *       &lt;sequence>
 *         &lt;element name="hook-handler" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="handler-class" use="required" type="{http://jcp.org/jsr/198/extension-manifest}className" />
 *                 &lt;attribute name="namespace" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                 &lt;attribute name="schema-location" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="tag-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hookHandlerHookType", propOrder = {
    "hookHandler"
})
public class HookHandlerHookType
    extends HookType
{

    @XmlElement(name = "hook-handler", required = true)
    protected List<HookHandler> hookHandler;

    /**
     * Gets the value of the hookHandler property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hookHandler property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHookHandler().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HookHandler }
     * 
     * 
     */
    public List<HookHandler> getHookHandler() {
        if (hookHandler == null) {
            hookHandler = new ArrayList<HookHandler>();
        }
        return this.hookHandler;
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
     *       &lt;attribute name="handler-class" use="required" type="{http://jcp.org/jsr/198/extension-manifest}className" />
     *       &lt;attribute name="namespace" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *       &lt;attribute name="schema-location" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="tag-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class HookHandler {

        @XmlAttribute(name = "handler-class", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String handlerClass;
        @XmlAttribute(required = true)
        protected String namespace;
        @XmlAttribute(name = "schema-location")
        protected String schemaLocation;
        @XmlAttribute(name = "tag-name", required = true)
        protected String tagName;

        /**
         * Gets the value of the handlerClass property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHandlerClass() {
            return handlerClass;
        }

        /**
         * Sets the value of the handlerClass property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHandlerClass(String value) {
            this.handlerClass = value;
        }

        /**
         * Gets the value of the namespace property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNamespace() {
            return namespace;
        }

        /**
         * Sets the value of the namespace property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNamespace(String value) {
            this.namespace = value;
        }

        /**
         * Gets the value of the schemaLocation property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSchemaLocation() {
            return schemaLocation;
        }

        /**
         * Sets the value of the schemaLocation property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSchemaLocation(String value) {
            this.schemaLocation = value;
        }

        /**
         * Gets the value of the tagName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTagName() {
            return tagName;
        }

        /**
         * Sets the value of the tagName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTagName(String value) {
            this.tagName = value;
        }

    }

}
