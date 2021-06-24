
package ec.gob.ant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarLicencia complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarLicencia">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datos" type="{http://www.ant.gob.ec/}entradaPersona" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarLicencia", propOrder = {
    "datos"
})
public class ConsultarLicencia {

    protected EntradaPersona datos;

    /**
     * Gets the value of the datos property.
     * 
     * @return
     *     possible object is
     *     {@link EntradaPersona }
     *     
     */
    public EntradaPersona getDatos() {
        return datos;
    }

    /**
     * Sets the value of the datos property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntradaPersona }
     *     
     */
    public void setDatos(EntradaPersona value) {
        this.datos = value;
    }

}
