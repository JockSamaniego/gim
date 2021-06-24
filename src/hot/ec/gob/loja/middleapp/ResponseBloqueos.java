
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.Bloqueos;


/**
 * <p>Java class for responseBloqueos complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseBloqueos">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="blocks" type="{http://www.ant.gob.ec/}bloqueos" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseBloqueos", propOrder = {
    "blocks"
})
public class ResponseBloqueos
    extends GeneralResponse
{

    protected Bloqueos blocks;

    /**
     * Gets the value of the blocks property.
     * 
     * @return
     *     possible object is
     *     {@link Bloqueos }
     *     
     */
    public Bloqueos getBlocks() {
        return blocks;
    }

    /**
     * Sets the value of the blocks property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bloqueos }
     *     
     */
    public void setBlocks(Bloqueos value) {
        this.blocks = value;
    }

}
