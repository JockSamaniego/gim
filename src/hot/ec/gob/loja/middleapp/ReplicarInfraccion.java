
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.EntradaReplicacion;


/**
 * <p>Java class for replicarInfraccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="replicarInfraccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="infraccion" type="{http://www.ant.gob.ec/}entradaReplicacion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "replicarInfraccion", propOrder = {
    "infraccion"
})
public class ReplicarInfraccion {

    protected EntradaReplicacion infraccion;

    /**
     * Gets the value of the infraccion property.
     * 
     * @return
     *     possible object is
     *     {@link EntradaReplicacion }
     *     
     */
    public EntradaReplicacion getInfraccion() {
        return infraccion;
    }

    /**
     * Sets the value of the infraccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntradaReplicacion }
     *     
     */
    public void setInfraccion(EntradaReplicacion value) {
        this.infraccion = value;
    }

}
