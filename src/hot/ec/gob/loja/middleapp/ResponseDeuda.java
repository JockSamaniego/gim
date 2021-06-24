
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.Deuda;


/**
 * <p>Java class for responseDeuda complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseDeuda">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="debt" type="{http://www.ant.gob.ec/}deuda" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseDeuda", propOrder = {
    "debt"
})
public class ResponseDeuda
    extends GeneralResponse
{

    protected Deuda debt;

    /**
     * Gets the value of the debt property.
     * 
     * @return
     *     possible object is
     *     {@link Deuda }
     *     
     */
    public Deuda getDebt() {
        return debt;
    }

    /**
     * Sets the value of the debt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Deuda }
     *     
     */
    public void setDebt(Deuda value) {
        this.debt = value;
    }

}
