
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.HistorialDominio;


/**
 * <p>Java class for responseHistorialDominio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseHistorialDominio">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="domainHistory" type="{http://www.ant.gob.ec/}historialDominio" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseHistorialDominio", propOrder = {
    "domainHistory"
})
public class ResponseHistorialDominio
    extends GeneralResponse
{

    protected HistorialDominio domainHistory;

    /**
     * Gets the value of the domainHistory property.
     * 
     * @return
     *     possible object is
     *     {@link HistorialDominio }
     *     
     */
    public HistorialDominio getDomainHistory() {
        return domainHistory;
    }

    /**
     * Sets the value of the domainHistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link HistorialDominio }
     *     
     */
    public void setDomainHistory(HistorialDominio value) {
        this.domainHistory = value;
    }

}
