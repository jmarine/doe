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
import org.doe4ejb3.jaxb.plugin.XmlDocType.DocType;


/**
 * <p>Java class for xmlDocType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xmlDocType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="suffix" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="doc-type" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="public-id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="system-id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="document-class" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlDocType", propOrder = {
    "suffix",
    "docType"
})
public class XmlDocType {

    @XmlElement(required = true)
    protected List<Object> suffix;
    @XmlElement(name = "doc-type", required = true)
    protected List<DocType> docType;
    @XmlAttribute(name = "document-class")
    protected String documentClass;

    /**
     * Gets the value of the suffix property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the suffix property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuffix().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getSuffix() {
        if (suffix == null) {
            suffix = new ArrayList<Object>();
        }
        return this.suffix;
    }

    /**
     * Gets the value of the docType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocType }
     * 
     * 
     */
    public List<DocType> getDocType() {
        if (docType == null) {
            docType = new ArrayList<DocType>();
        }
        return this.docType;
    }

    /**
     * Gets the value of the documentClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentClass() {
        return documentClass;
    }

    /**
     * Sets the value of the documentClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentClass(String value) {
        this.documentClass = value;
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
     *       &lt;attribute name="public-id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="system-id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DocType {

        @XmlAttribute(name = "public-id")
        protected String publicId;
        @XmlAttribute(name = "system-id")
        protected String systemId;

        /**
         * Gets the value of the publicId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPublicId() {
            return publicId;
        }

        /**
         * Sets the value of the publicId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPublicId(String value) {
            this.publicId = value;
        }

        /**
         * Gets the value of the systemId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSystemId() {
            return systemId;
        }

        /**
         * Sets the value of the systemId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSystemId(String value) {
            this.systemId = value;
        }

    }

}
