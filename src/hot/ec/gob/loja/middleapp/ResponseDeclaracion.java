
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.DatosSalidaDeclaracionJ;


/**
 * <p>Java class for responseDeclaracion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseDeclaracion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="declaracion" type="{http://www.ant.gob.ec/}datosSalidaDeclaracionJ" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseDeclaracion", propOrder = {
    "declaracion"
})
public class ResponseDeclaracion
    extends GeneralResponse
{

    protected DatosSalidaDeclaracionJ declaracion;

    /**
     * Gets the value of the declaracion property.
     * 
     * @return
     *     possible object is
     *     {@link DatosSalidaDeclaracionJ }
     *     
     */
    public DatosSalidaDeclaracionJ getDeclaracion() {
        return declaracion;
    }

    /**
     * Sets the value of the declaracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosSalidaDeclaracionJ }
     *     
     */
    public void setDeclaracion(DatosSalidaDeclaracionJ value) {
        this.declaracion = value;
    }

}
