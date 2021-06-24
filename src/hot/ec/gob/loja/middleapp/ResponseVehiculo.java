
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.Vehiculo;


/**
 * <p>Java class for responseVehiculo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseVehiculo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="vehicle" type="{http://www.ant.gob.ec/}vehiculo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseVehiculo", propOrder = {
    "vehicle"
})
public class ResponseVehiculo
    extends GeneralResponse
{

    protected Vehiculo vehicle;

    /**
     * Gets the value of the vehicle property.
     * 
     * @return
     *     possible object is
     *     {@link Vehiculo }
     *     
     */
    public Vehiculo getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vehiculo }
     *     
     */
    public void setVehicle(Vehiculo value) {
        this.vehicle = value;
    }

}
