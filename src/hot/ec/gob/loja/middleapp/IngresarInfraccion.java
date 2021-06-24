
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.Infraccion;


/**
 * <p>Java class for ingresarInfraccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ingresarInfraccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="infraccion" type="{http://www.ant.gob.ec/}infraccion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ingresarInfraccion", propOrder = {
    "infraccion"
})
public class IngresarInfraccion {

    protected Infraccion infraccion;

    /**
     * Gets the value of the infraccion property.
     * 
     * @return
     *     possible object is
     *     {@link Infraccion }
     *     
     */
    public Infraccion getInfraccion() {
        return infraccion;
    }

    /**
     * Sets the value of the infraccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Infraccion }
     *     
     */
    public void setInfraccion(Infraccion value) {
        this.infraccion = value;
    }

}
