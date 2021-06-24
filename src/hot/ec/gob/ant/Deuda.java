
package ec.gob.ant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deuda complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deuda">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cambioProp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cantidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pagadoSri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultado" type="{http://www.ant.gob.ec/}resultado" minOccurs="0"/>
 *         &lt;element name="saldoTotal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="saldoTotalCTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="saldoTotalOtros" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoIdentificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deuda", propOrder = {
    "cambioProp",
    "cantidad",
    "identificacion",
    "nombres",
    "pagadoSri",
    "resultado",
    "saldoTotal",
    "saldoTotalCTE",
    "saldoTotalOtros",
    "tipoIdentificacion"
})
public class Deuda {

    protected String cambioProp;
    protected String cantidad;
    protected String identificacion;
    protected String nombres;
    protected String pagadoSri;
    protected Resultado resultado;
    protected String saldoTotal;
    protected String saldoTotalCTE;
    protected String saldoTotalOtros;
    protected String tipoIdentificacion;

    /**
     * Gets the value of the cambioProp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCambioProp() {
        return cambioProp;
    }

    /**
     * Sets the value of the cambioProp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCambioProp(String value) {
        this.cambioProp = value;
    }

    /**
     * Gets the value of the cantidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCantidad() {
        return cantidad;
    }

    /**
     * Sets the value of the cantidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCantidad(String value) {
        this.cantidad = value;
    }

    /**
     * Gets the value of the identificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * Sets the value of the identificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificacion(String value) {
        this.identificacion = value;
    }

    /**
     * Gets the value of the nombres property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Sets the value of the nombres property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombres(String value) {
        this.nombres = value;
    }

    /**
     * Gets the value of the pagadoSri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagadoSri() {
        return pagadoSri;
    }

    /**
     * Sets the value of the pagadoSri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagadoSri(String value) {
        this.pagadoSri = value;
    }

    /**
     * Gets the value of the resultado property.
     * 
     * @return
     *     possible object is
     *     {@link Resultado }
     *     
     */
    public Resultado getResultado() {
        return resultado;
    }

    /**
     * Sets the value of the resultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Resultado }
     *     
     */
    public void setResultado(Resultado value) {
        this.resultado = value;
    }

    /**
     * Gets the value of the saldoTotal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaldoTotal() {
        return saldoTotal;
    }

    /**
     * Sets the value of the saldoTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaldoTotal(String value) {
        this.saldoTotal = value;
    }

    /**
     * Gets the value of the saldoTotalCTE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaldoTotalCTE() {
        return saldoTotalCTE;
    }

    /**
     * Sets the value of the saldoTotalCTE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaldoTotalCTE(String value) {
        this.saldoTotalCTE = value;
    }

    /**
     * Gets the value of the saldoTotalOtros property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaldoTotalOtros() {
        return saldoTotalOtros;
    }

    /**
     * Sets the value of the saldoTotalOtros property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaldoTotalOtros(String value) {
        this.saldoTotalOtros = value;
    }

    /**
     * Gets the value of the tipoIdentificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    /**
     * Sets the value of the tipoIdentificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIdentificacion(String value) {
        this.tipoIdentificacion = value;
    }

}
