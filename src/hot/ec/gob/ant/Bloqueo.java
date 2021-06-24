
package ec.gob.ant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bloqueo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bloqueo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaEjecucion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaIngreso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaInicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaTerminacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idAlterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idBloqueo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idCanalTrx" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idContrato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEmpresa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEmpresaTrx" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEmpresaTrxDesb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idLocalidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOficinaTrx" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOficinaTrxDesb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idServicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idSubproducto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoBloqueo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTramiteBlo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTramiteDes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTransaccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bloqueo", propOrder = {
    "estado",
    "fechaDesde",
    "fechaEjecucion",
    "fechaHasta",
    "fechaIngreso",
    "fechaInicio",
    "fechaTerminacion",
    "idAlterno",
    "idBloqueo",
    "idCanalTrx",
    "idContrato",
    "idEmpresa",
    "idEmpresaTrx",
    "idEmpresaTrxDesb",
    "idLocalidad",
    "idOficinaTrx",
    "idOficinaTrxDesb",
    "idServicio",
    "idSubproducto",
    "idTipoBloqueo",
    "idTramiteBlo",
    "idTramiteDes",
    "idTransaccion",
    "idUsuario",
    "observacion"
})
public class Bloqueo {

    protected String estado;
    protected String fechaDesde;
    protected String fechaEjecucion;
    protected String fechaHasta;
    protected String fechaIngreso;
    protected String fechaInicio;
    protected String fechaTerminacion;
    protected String idAlterno;
    protected String idBloqueo;
    protected String idCanalTrx;
    protected String idContrato;
    protected String idEmpresa;
    protected String idEmpresaTrx;
    protected String idEmpresaTrxDesb;
    protected String idLocalidad;
    protected String idOficinaTrx;
    protected String idOficinaTrxDesb;
    protected String idServicio;
    protected String idSubproducto;
    protected String idTipoBloqueo;
    protected String idTramiteBlo;
    protected String idTramiteDes;
    protected String idTransaccion;
    protected String idUsuario;
    protected String observacion;

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Gets the value of the fechaDesde property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaDesde() {
        return fechaDesde;
    }

    /**
     * Sets the value of the fechaDesde property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaDesde(String value) {
        this.fechaDesde = value;
    }

    /**
     * Gets the value of the fechaEjecucion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEjecucion() {
        return fechaEjecucion;
    }

    /**
     * Sets the value of the fechaEjecucion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEjecucion(String value) {
        this.fechaEjecucion = value;
    }

    /**
     * Gets the value of the fechaHasta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaHasta() {
        return fechaHasta;
    }

    /**
     * Sets the value of the fechaHasta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaHasta(String value) {
        this.fechaHasta = value;
    }

    /**
     * Gets the value of the fechaIngreso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaIngreso() {
        return fechaIngreso;
    }

    /**
     * Sets the value of the fechaIngreso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaIngreso(String value) {
        this.fechaIngreso = value;
    }

    /**
     * Gets the value of the fechaInicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Sets the value of the fechaInicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaInicio(String value) {
        this.fechaInicio = value;
    }

    /**
     * Gets the value of the fechaTerminacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaTerminacion() {
        return fechaTerminacion;
    }

    /**
     * Sets the value of the fechaTerminacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaTerminacion(String value) {
        this.fechaTerminacion = value;
    }

    /**
     * Gets the value of the idAlterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAlterno() {
        return idAlterno;
    }

    /**
     * Sets the value of the idAlterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAlterno(String value) {
        this.idAlterno = value;
    }

    /**
     * Gets the value of the idBloqueo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdBloqueo() {
        return idBloqueo;
    }

    /**
     * Sets the value of the idBloqueo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdBloqueo(String value) {
        this.idBloqueo = value;
    }

    /**
     * Gets the value of the idCanalTrx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCanalTrx() {
        return idCanalTrx;
    }

    /**
     * Sets the value of the idCanalTrx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCanalTrx(String value) {
        this.idCanalTrx = value;
    }

    /**
     * Gets the value of the idContrato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdContrato() {
        return idContrato;
    }

    /**
     * Sets the value of the idContrato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdContrato(String value) {
        this.idContrato = value;
    }

    /**
     * Gets the value of the idEmpresa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * Sets the value of the idEmpresa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEmpresa(String value) {
        this.idEmpresa = value;
    }

    /**
     * Gets the value of the idEmpresaTrx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEmpresaTrx() {
        return idEmpresaTrx;
    }

    /**
     * Sets the value of the idEmpresaTrx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEmpresaTrx(String value) {
        this.idEmpresaTrx = value;
    }

    /**
     * Gets the value of the idEmpresaTrxDesb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEmpresaTrxDesb() {
        return idEmpresaTrxDesb;
    }

    /**
     * Sets the value of the idEmpresaTrxDesb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEmpresaTrxDesb(String value) {
        this.idEmpresaTrxDesb = value;
    }

    /**
     * Gets the value of the idLocalidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLocalidad() {
        return idLocalidad;
    }

    /**
     * Sets the value of the idLocalidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLocalidad(String value) {
        this.idLocalidad = value;
    }

    /**
     * Gets the value of the idOficinaTrx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOficinaTrx() {
        return idOficinaTrx;
    }

    /**
     * Sets the value of the idOficinaTrx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOficinaTrx(String value) {
        this.idOficinaTrx = value;
    }

    /**
     * Gets the value of the idOficinaTrxDesb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOficinaTrxDesb() {
        return idOficinaTrxDesb;
    }

    /**
     * Sets the value of the idOficinaTrxDesb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOficinaTrxDesb(String value) {
        this.idOficinaTrxDesb = value;
    }

    /**
     * Gets the value of the idServicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdServicio() {
        return idServicio;
    }

    /**
     * Sets the value of the idServicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdServicio(String value) {
        this.idServicio = value;
    }

    /**
     * Gets the value of the idSubproducto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSubproducto() {
        return idSubproducto;
    }

    /**
     * Sets the value of the idSubproducto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSubproducto(String value) {
        this.idSubproducto = value;
    }

    /**
     * Gets the value of the idTipoBloqueo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoBloqueo() {
        return idTipoBloqueo;
    }

    /**
     * Sets the value of the idTipoBloqueo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoBloqueo(String value) {
        this.idTipoBloqueo = value;
    }

    /**
     * Gets the value of the idTramiteBlo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTramiteBlo() {
        return idTramiteBlo;
    }

    /**
     * Sets the value of the idTramiteBlo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTramiteBlo(String value) {
        this.idTramiteBlo = value;
    }

    /**
     * Gets the value of the idTramiteDes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTramiteDes() {
        return idTramiteDes;
    }

    /**
     * Sets the value of the idTramiteDes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTramiteDes(String value) {
        this.idTramiteDes = value;
    }

    /**
     * Gets the value of the idTransaccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * Sets the value of the idTransaccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTransaccion(String value) {
        this.idTransaccion = value;
    }

    /**
     * Gets the value of the idUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the value of the idUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUsuario(String value) {
        this.idUsuario = value;
    }

    /**
     * Gets the value of the observacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * Sets the value of the observacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservacion(String value) {
        this.observacion = value;
    }

}
