
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.DatosLicencia;


/**
 * <p>Java class for responseDatosLicencia complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseDatosLicencia">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="licenseData" type="{http://www.ant.gob.ec/}datosLicencia" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseDatosLicencia", propOrder = {
    "licenseData"
})
public class ResponseDatosLicencia
    extends GeneralResponse
{

    protected DatosLicencia licenseData;

    /**
     * Gets the value of the licenseData property.
     * 
     * @return
     *     possible object is
     *     {@link DatosLicencia }
     *     
     */
    public DatosLicencia getLicenseData() {
        return licenseData;
    }

    /**
     * Sets the value of the licenseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosLicencia }
     *     
     */
    public void setLicenseData(DatosLicencia value) {
        this.licenseData = value;
    }

}
