
package ec.gob.ant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vistaInfraccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vistaInfraccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agenteIdentificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agenteNombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agenteTipoId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="anulada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="articulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="articuloDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="articuloObservacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="articuloPuntos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comprobantePago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contravencion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="convenio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="direccionAlterna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="empresa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaAnulacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaCoactiva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaConvenio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaEmision" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaImpugnacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaNotificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idAgenteTransito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idArticulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idCanal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idCircuito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idContrato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idDistrito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEmpresa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idInfraccionANT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idInfraccionGAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idInfractor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idLibretin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idLocalidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idPropietario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idRubro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoContrato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoLicencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idZona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infractorIdentificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infractorNombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infractorTipoId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="intereses" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="literal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lugarInfraccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origenPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pagada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="placa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="propietarioIdentificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="propietarioNombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="propietarioTipoId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="puntosInfraccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reclamo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultado" type="{http://www.ant.gob.ec/}resultado" minOccurs="0"/>
 *         &lt;element name="saldoMultas" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoEmision" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoInfraccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valorExoneradoMulta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valorTotal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vistaInfraccion", propOrder = {
    "agenteIdentificacion",
    "agenteNombre",
    "agenteTipoId",
    "anulada",
    "articulo",
    "articuloDescripcion",
    "articuloObservacion",
    "articuloPuntos",
    "comprobantePago",
    "contravencion",
    "convenio",
    "direccionAlterna",
    "empresa",
    "estado",
    "fechaAnulacion",
    "fechaCoactiva",
    "fechaConvenio",
    "fechaEmision",
    "fechaImpugnacion",
    "fechaNotificacion",
    "fechaPago",
    "fechaRegistro",
    "idAgenteTransito",
    "idArticulo",
    "idCanal",
    "idCircuito",
    "idContrato",
    "idDistrito",
    "idEmpresa",
    "idInfraccionANT",
    "idInfraccionGAD",
    "idInfractor",
    "idLibretin",
    "idLocalidad",
    "idOficina",
    "idPropietario",
    "idProvincia",
    "idRubro",
    "idTipoContrato",
    "idTipoLicencia",
    "idUsuario",
    "idZona",
    "infractorIdentificacion",
    "infractorNombre",
    "infractorTipoId",
    "intereses",
    "legal",
    "literal",
    "lugarInfraccion",
    "observacion",
    "oficina",
    "origen",
    "origenPago",
    "pagada",
    "placa",
    "propietarioIdentificacion",
    "propietarioNombre",
    "propietarioTipoId",
    "puntosInfraccion",
    "reclamo",
    "resultado",
    "saldoMultas",
    "tipo",
    "tipoEmision",
    "tipoInfraccion",
    "valor",
    "valorExoneradoMulta",
    "valorTotal"
})
public class VistaInfraccion {

    protected String agenteIdentificacion;
    protected String agenteNombre;
    protected String agenteTipoId;
    protected String anulada;
    protected String articulo;
    protected String articuloDescripcion;
    protected String articuloObservacion;
    protected String articuloPuntos;
    protected String comprobantePago;
    protected String contravencion;
    protected String convenio;
    protected String direccionAlterna;
    protected String empresa;
    protected String estado;
    protected String fechaAnulacion;
    protected String fechaCoactiva;
    protected String fechaConvenio;
    protected String fechaEmision;
    protected String fechaImpugnacion;
    protected String fechaNotificacion;
    protected String fechaPago;
    protected String fechaRegistro;
    protected String idAgenteTransito;
    protected String idArticulo;
    protected String idCanal;
    protected String idCircuito;
    protected String idContrato;
    protected String idDistrito;
    protected String idEmpresa;
    protected String idInfraccionANT;
    protected String idInfraccionGAD;
    protected String idInfractor;
    protected String idLibretin;
    protected String idLocalidad;
    protected String idOficina;
    protected String idPropietario;
    protected String idProvincia;
    protected String idRubro;
    protected String idTipoContrato;
    protected String idTipoLicencia;
    protected String idUsuario;
    protected String idZona;
    protected String infractorIdentificacion;
    protected String infractorNombre;
    protected String infractorTipoId;
    protected String intereses;
    protected String legal;
    protected String literal;
    protected String lugarInfraccion;
    protected String observacion;
    protected String oficina;
    protected String origen;
    protected String origenPago;
    protected String pagada;
    protected String placa;
    protected String propietarioIdentificacion;
    protected String propietarioNombre;
    protected String propietarioTipoId;
    protected String puntosInfraccion;
    protected String reclamo;
    protected Resultado resultado;
    protected String saldoMultas;
    protected String tipo;
    protected String tipoEmision;
    protected String tipoInfraccion;
    protected String valor;
    protected String valorExoneradoMulta;
    protected String valorTotal;

    /**
     * Gets the value of the agenteIdentificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgenteIdentificacion() {
        return agenteIdentificacion;
    }

    /**
     * Sets the value of the agenteIdentificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgenteIdentificacion(String value) {
        this.agenteIdentificacion = value;
    }

    /**
     * Gets the value of the agenteNombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgenteNombre() {
        return agenteNombre;
    }

    /**
     * Sets the value of the agenteNombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgenteNombre(String value) {
        this.agenteNombre = value;
    }

    /**
     * Gets the value of the agenteTipoId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgenteTipoId() {
        return agenteTipoId;
    }

    /**
     * Sets the value of the agenteTipoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgenteTipoId(String value) {
        this.agenteTipoId = value;
    }

    /**
     * Gets the value of the anulada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnulada() {
        return anulada;
    }

    /**
     * Sets the value of the anulada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnulada(String value) {
        this.anulada = value;
    }

    /**
     * Gets the value of the articulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticulo() {
        return articulo;
    }

    /**
     * Sets the value of the articulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticulo(String value) {
        this.articulo = value;
    }

    /**
     * Gets the value of the articuloDescripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticuloDescripcion() {
        return articuloDescripcion;
    }

    /**
     * Sets the value of the articuloDescripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticuloDescripcion(String value) {
        this.articuloDescripcion = value;
    }

    /**
     * Gets the value of the articuloObservacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticuloObservacion() {
        return articuloObservacion;
    }

    /**
     * Sets the value of the articuloObservacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticuloObservacion(String value) {
        this.articuloObservacion = value;
    }

    /**
     * Gets the value of the articuloPuntos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticuloPuntos() {
        return articuloPuntos;
    }

    /**
     * Sets the value of the articuloPuntos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticuloPuntos(String value) {
        this.articuloPuntos = value;
    }

    /**
     * Gets the value of the comprobantePago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComprobantePago() {
        return comprobantePago;
    }

    /**
     * Sets the value of the comprobantePago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComprobantePago(String value) {
        this.comprobantePago = value;
    }

    /**
     * Gets the value of the contravencion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContravencion() {
        return contravencion;
    }

    /**
     * Sets the value of the contravencion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContravencion(String value) {
        this.contravencion = value;
    }

    /**
     * Gets the value of the convenio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConvenio() {
        return convenio;
    }

    /**
     * Sets the value of the convenio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConvenio(String value) {
        this.convenio = value;
    }

    /**
     * Gets the value of the direccionAlterna property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionAlterna() {
        return direccionAlterna;
    }

    /**
     * Sets the value of the direccionAlterna property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionAlterna(String value) {
        this.direccionAlterna = value;
    }

    /**
     * Gets the value of the empresa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * Sets the value of the empresa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmpresa(String value) {
        this.empresa = value;
    }

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
     * Gets the value of the fechaAnulacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaAnulacion() {
        return fechaAnulacion;
    }

    /**
     * Sets the value of the fechaAnulacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaAnulacion(String value) {
        this.fechaAnulacion = value;
    }

    /**
     * Gets the value of the fechaCoactiva property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCoactiva() {
        return fechaCoactiva;
    }

    /**
     * Sets the value of the fechaCoactiva property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCoactiva(String value) {
        this.fechaCoactiva = value;
    }

    /**
     * Gets the value of the fechaConvenio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaConvenio() {
        return fechaConvenio;
    }

    /**
     * Sets the value of the fechaConvenio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaConvenio(String value) {
        this.fechaConvenio = value;
    }

    /**
     * Gets the value of the fechaEmision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Sets the value of the fechaEmision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmision(String value) {
        this.fechaEmision = value;
    }

    /**
     * Gets the value of the fechaImpugnacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaImpugnacion() {
        return fechaImpugnacion;
    }

    /**
     * Sets the value of the fechaImpugnacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaImpugnacion(String value) {
        this.fechaImpugnacion = value;
    }

    /**
     * Gets the value of the fechaNotificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNotificacion() {
        return fechaNotificacion;
    }

    /**
     * Sets the value of the fechaNotificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNotificacion(String value) {
        this.fechaNotificacion = value;
    }

    /**
     * Gets the value of the fechaPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaPago() {
        return fechaPago;
    }

    /**
     * Sets the value of the fechaPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaPago(String value) {
        this.fechaPago = value;
    }

    /**
     * Gets the value of the fechaRegistro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Sets the value of the fechaRegistro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaRegistro(String value) {
        this.fechaRegistro = value;
    }

    /**
     * Gets the value of the idAgenteTransito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAgenteTransito() {
        return idAgenteTransito;
    }

    /**
     * Sets the value of the idAgenteTransito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAgenteTransito(String value) {
        this.idAgenteTransito = value;
    }

    /**
     * Gets the value of the idArticulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdArticulo() {
        return idArticulo;
    }

    /**
     * Sets the value of the idArticulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdArticulo(String value) {
        this.idArticulo = value;
    }

    /**
     * Gets the value of the idCanal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCanal() {
        return idCanal;
    }

    /**
     * Sets the value of the idCanal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCanal(String value) {
        this.idCanal = value;
    }

    /**
     * Gets the value of the idCircuito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCircuito() {
        return idCircuito;
    }

    /**
     * Sets the value of the idCircuito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCircuito(String value) {
        this.idCircuito = value;
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
     * Gets the value of the idDistrito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDistrito() {
        return idDistrito;
    }

    /**
     * Sets the value of the idDistrito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDistrito(String value) {
        this.idDistrito = value;
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
     * Gets the value of the idInfraccionANT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdInfraccionANT() {
        return idInfraccionANT;
    }

    /**
     * Sets the value of the idInfraccionANT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdInfraccionANT(String value) {
        this.idInfraccionANT = value;
    }

    /**
     * Gets the value of the idInfraccionGAD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdInfraccionGAD() {
        return idInfraccionGAD;
    }

    /**
     * Sets the value of the idInfraccionGAD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdInfraccionGAD(String value) {
        this.idInfraccionGAD = value;
    }

    /**
     * Gets the value of the idInfractor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdInfractor() {
        return idInfractor;
    }

    /**
     * Sets the value of the idInfractor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdInfractor(String value) {
        this.idInfractor = value;
    }

    /**
     * Gets the value of the idLibretin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLibretin() {
        return idLibretin;
    }

    /**
     * Sets the value of the idLibretin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLibretin(String value) {
        this.idLibretin = value;
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
     * Gets the value of the idOficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOficina() {
        return idOficina;
    }

    /**
     * Sets the value of the idOficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOficina(String value) {
        this.idOficina = value;
    }

    /**
     * Gets the value of the idPropietario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPropietario() {
        return idPropietario;
    }

    /**
     * Sets the value of the idPropietario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPropietario(String value) {
        this.idPropietario = value;
    }

    /**
     * Gets the value of the idProvincia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProvincia() {
        return idProvincia;
    }

    /**
     * Sets the value of the idProvincia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProvincia(String value) {
        this.idProvincia = value;
    }

    /**
     * Gets the value of the idRubro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdRubro() {
        return idRubro;
    }

    /**
     * Sets the value of the idRubro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdRubro(String value) {
        this.idRubro = value;
    }

    /**
     * Gets the value of the idTipoContrato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoContrato() {
        return idTipoContrato;
    }

    /**
     * Sets the value of the idTipoContrato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoContrato(String value) {
        this.idTipoContrato = value;
    }

    /**
     * Gets the value of the idTipoLicencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTipoLicencia() {
        return idTipoLicencia;
    }

    /**
     * Sets the value of the idTipoLicencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTipoLicencia(String value) {
        this.idTipoLicencia = value;
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
     * Gets the value of the idZona property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdZona() {
        return idZona;
    }

    /**
     * Sets the value of the idZona property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdZona(String value) {
        this.idZona = value;
    }

    /**
     * Gets the value of the infractorIdentificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfractorIdentificacion() {
        return infractorIdentificacion;
    }

    /**
     * Sets the value of the infractorIdentificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfractorIdentificacion(String value) {
        this.infractorIdentificacion = value;
    }

    /**
     * Gets the value of the infractorNombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfractorNombre() {
        return infractorNombre;
    }

    /**
     * Sets the value of the infractorNombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfractorNombre(String value) {
        this.infractorNombre = value;
    }

    /**
     * Gets the value of the infractorTipoId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfractorTipoId() {
        return infractorTipoId;
    }

    /**
     * Sets the value of the infractorTipoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfractorTipoId(String value) {
        this.infractorTipoId = value;
    }

    /**
     * Gets the value of the intereses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntereses() {
        return intereses;
    }

    /**
     * Sets the value of the intereses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntereses(String value) {
        this.intereses = value;
    }

    /**
     * Gets the value of the legal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegal() {
        return legal;
    }

    /**
     * Sets the value of the legal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegal(String value) {
        this.legal = value;
    }

    /**
     * Gets the value of the literal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Sets the value of the literal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLiteral(String value) {
        this.literal = value;
    }

    /**
     * Gets the value of the lugarInfraccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLugarInfraccion() {
        return lugarInfraccion;
    }

    /**
     * Sets the value of the lugarInfraccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLugarInfraccion(String value) {
        this.lugarInfraccion = value;
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

    /**
     * Gets the value of the oficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficina() {
        return oficina;
    }

    /**
     * Sets the value of the oficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficina(String value) {
        this.oficina = value;
    }

    /**
     * Gets the value of the origen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Sets the value of the origen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigen(String value) {
        this.origen = value;
    }

    /**
     * Gets the value of the origenPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenPago() {
        return origenPago;
    }

    /**
     * Sets the value of the origenPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenPago(String value) {
        this.origenPago = value;
    }

    /**
     * Gets the value of the pagada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagada() {
        return pagada;
    }

    /**
     * Sets the value of the pagada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagada(String value) {
        this.pagada = value;
    }

    /**
     * Gets the value of the placa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Sets the value of the placa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaca(String value) {
        this.placa = value;
    }

    /**
     * Gets the value of the propietarioIdentificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropietarioIdentificacion() {
        return propietarioIdentificacion;
    }

    /**
     * Sets the value of the propietarioIdentificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropietarioIdentificacion(String value) {
        this.propietarioIdentificacion = value;
    }

    /**
     * Gets the value of the propietarioNombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropietarioNombre() {
        return propietarioNombre;
    }

    /**
     * Sets the value of the propietarioNombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropietarioNombre(String value) {
        this.propietarioNombre = value;
    }

    /**
     * Gets the value of the propietarioTipoId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropietarioTipoId() {
        return propietarioTipoId;
    }

    /**
     * Sets the value of the propietarioTipoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropietarioTipoId(String value) {
        this.propietarioTipoId = value;
    }

    /**
     * Gets the value of the puntosInfraccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuntosInfraccion() {
        return puntosInfraccion;
    }

    /**
     * Sets the value of the puntosInfraccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuntosInfraccion(String value) {
        this.puntosInfraccion = value;
    }

    /**
     * Gets the value of the reclamo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReclamo() {
        return reclamo;
    }

    /**
     * Sets the value of the reclamo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReclamo(String value) {
        this.reclamo = value;
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
     * Gets the value of the saldoMultas property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaldoMultas() {
        return saldoMultas;
    }

    /**
     * Sets the value of the saldoMultas property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaldoMultas(String value) {
        this.saldoMultas = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the tipoEmision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEmision() {
        return tipoEmision;
    }

    /**
     * Sets the value of the tipoEmision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEmision(String value) {
        this.tipoEmision = value;
    }

    /**
     * Gets the value of the tipoInfraccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoInfraccion() {
        return tipoInfraccion;
    }

    /**
     * Sets the value of the tipoInfraccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoInfraccion(String value) {
        this.tipoInfraccion = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValor() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValor(String value) {
        this.valor = value;
    }

    /**
     * Gets the value of the valorExoneradoMulta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorExoneradoMulta() {
        return valorExoneradoMulta;
    }

    /**
     * Sets the value of the valorExoneradoMulta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorExoneradoMulta(String value) {
        this.valorExoneradoMulta = value;
    }

    /**
     * Gets the value of the valorTotal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorTotal() {
        return valorTotal;
    }

    /**
     * Sets the value of the valorTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorTotal(String value) {
        this.valorTotal = value;
    }

}
