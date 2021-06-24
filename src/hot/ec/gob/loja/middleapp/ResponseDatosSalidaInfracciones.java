
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.DatosSalidaInfracciones;


/**
 * <p>Java class for responseDatosSalidaInfracciones complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseDatosSalidaInfracciones">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="dataOutput" type="{http://www.ant.gob.ec/}datosSalidaInfracciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseDatosSalidaInfracciones", propOrder = {
    "dataOutput"
})
public class ResponseDatosSalidaInfracciones
    extends GeneralResponse
{

    protected DatosSalidaInfracciones dataOutput;

    /**
     * Gets the value of the dataOutput property.
     * 
     * @return
     *     possible object is
     *     {@link DatosSalidaInfracciones }
     *     
     */
    public DatosSalidaInfracciones getDataOutput() {
        return dataOutput;
    }

    /**
     * Sets the value of the dataOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosSalidaInfracciones }
     *     
     */
    public void setDataOutput(DatosSalidaInfracciones value) {
        this.dataOutput = value;
    }

}
