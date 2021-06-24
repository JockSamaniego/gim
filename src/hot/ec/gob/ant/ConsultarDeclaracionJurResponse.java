
package ec.gob.ant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarDeclaracionJurResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarDeclaracionJurResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.ant.gob.ec/}datosSalidaDeclaracionJ" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarDeclaracionJurResponse", propOrder = {
    "_return"
})
public class ConsultarDeclaracionJurResponse {

    @XmlElement(name = "return")
    protected DatosSalidaDeclaracionJ _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link DatosSalidaDeclaracionJ }
     *     
     */
    public DatosSalidaDeclaracionJ getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosSalidaDeclaracionJ }
     *     
     */
    public void setReturn(DatosSalidaDeclaracionJ value) {
        this._return = value;
    }

}
