
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.VistaInfraccion;


/**
 * <p>Java class for responseInfraccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseInfraccion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="infraccion" type="{http://www.ant.gob.ec/}vistaInfraccion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseInfraccion", propOrder = {
    "infraccion"
})
public class ResponseInfraccion
    extends GeneralResponse
{

    protected VistaInfraccion infraccion;

    /**
     * Gets the value of the infraccion property.
     * 
     * @return
     *     possible object is
     *     {@link VistaInfraccion }
     *     
     */
    public VistaInfraccion getInfraccion() {
        return infraccion;
    }

    /**
     * Sets the value of the infraccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link VistaInfraccion }
     *     
     */
    public void setInfraccion(VistaInfraccion value) {
        this.infraccion = value;
    }

}
