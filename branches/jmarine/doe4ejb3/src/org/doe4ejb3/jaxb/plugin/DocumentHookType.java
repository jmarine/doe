//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b17-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.08.28 at 08:29:25 PM CEST 
//


package org.doe4ejb3.jaxb.plugin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for documentHookType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documentHookType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://jcp.org/jsr/198/extension-manifest}hookType">
 *       &lt;all>
 *         &lt;element name="documents" type="{http://jcp.org/jsr/198/extension-manifest}recognizersType" minOccurs="0"/>
 *         &lt;element name="listeners" type="{http://jcp.org/jsr/198/extension-manifest}modelListenersType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentHookType", propOrder = {
    "documents",
    "listeners"
})
public class DocumentHookType
    extends HookType
{

    protected RecognizersType documents;
    protected ModelListenersType listeners;

    /**
     * Gets the value of the documents property.
     * 
     * @return
     *     possible object is
     *     {@link RecognizersType }
     *     
     */
    public RecognizersType getDocuments() {
        return documents;
    }

    /**
     * Sets the value of the documents property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecognizersType }
     *     
     */
    public void setDocuments(RecognizersType value) {
        this.documents = value;
    }

    /**
     * Gets the value of the listeners property.
     * 
     * @return
     *     possible object is
     *     {@link ModelListenersType }
     *     
     */
    public ModelListenersType getListeners() {
        return listeners;
    }

    /**
     * Sets the value of the listeners property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModelListenersType }
     *     
     */
    public void setListeners(ModelListenersType value) {
        this.listeners = value;
    }

}
