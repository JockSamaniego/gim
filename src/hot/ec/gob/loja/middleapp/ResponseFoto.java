
package ec.gob.loja.middleapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ec.gob.ant.Imagen;


/**
 * <p>Java class for responseFoto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseFoto">
 *   &lt;complexContent>
 *     &lt;extension base="{http://middleapp.loja.gob.ec/}generalResponse">
 *       &lt;sequence>
 *         &lt;element name="imagen" type="{http://www.ant.gob.ec/}imagen" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseFoto", propOrder = {
    "imagen"
})
public class ResponseFoto
    extends GeneralResponse
{

    protected Imagen imagen;

    /**
     * Gets the value of the imagen property.
     * 
     * @return
     *     possible object is
     *     {@link Imagen }
     *     
     */
    public Imagen getImagen() {
        return imagen;
    }

    /**
     * Sets the value of the imagen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Imagen }
     *     
     */
    public void setImagen(Imagen value) {
        this.imagen = value;
    }

}
