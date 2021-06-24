
package ec.gob.ant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vehiculo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vehiculo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activoVig" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ambitoOper" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="anio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="anioMatriculado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apellido1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apellido2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aseguraSoat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="avaluoComercial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cambioPropietario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="canvcp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="capacidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="carroceria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="casaComercial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cedulaPropAnterior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="celular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chasis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cilindraje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claseServicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claseTran" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claseVehiculo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="color2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="color3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="combustible" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contrato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cooperativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="desdePcir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="desdeSoat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="direccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disco" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="docPropietario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoCon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoInf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoPcir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoSer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoSoat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="facturaComercial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaCaducidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaCompra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaCompraVenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaFinCon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaIniCon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaMatricula" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="finVig" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hastaPcir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hastaSoat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idAlterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idAlternoMov" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identBenef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identPotencialProp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infraestructura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inicioPcir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inicioSoat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inicioVig" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="institucionRenova" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kilometraje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="marca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="marcaDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modeloDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreBenef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombrePotencialProp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombrePropAnterior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numEjes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numRuedas" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroRevisado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroTraspaso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pais" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="placaActual" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="placaAnterior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prendaComercial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prendaIndustrial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prohibidoEnajenar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="propietario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registroPcir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registroSoat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remarcadoChasis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remarcadoMotor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reservaDominio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="residencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultado" type="{http://www.ant.gob.ec/}resultado" minOccurs="0"/>
 *         &lt;element name="robado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rucCooperativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subClaseTran" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoIdent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoIdentBenef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoIdentPotencialProp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoPeso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoServicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoTran" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoVehiculo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tonelaje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehiculo", propOrder = {
    "activoVig",
    "ambitoOper",
    "anio",
    "anioMatriculado",
    "apellido1",
    "apellido2",
    "aseguraSoat",
    "avaluoComercial",
    "cambioPropietario",
    "canvcp",
    "capacidad",
    "carroceria",
    "casaComercial",
    "cedulaPropAnterior",
    "celular",
    "chasis",
    "cilindraje",
    "claseServicio",
    "claseTran",
    "claseVehiculo",
    "color",
    "color2",
    "color3",
    "combustible",
    "comprobante",
    "contrato",
    "cooperativa",
    "correo",
    "desdePcir",
    "desdeSoat",
    "direccion",
    "disco",
    "docPropietario",
    "estado",
    "estadoCon",
    "estadoInf",
    "estadoPcir",
    "estadoSer",
    "estadoSoat",
    "facturaComercial",
    "fechaCaducidad",
    "fechaCompra",
    "fechaCompraVenta",
    "fechaFinCon",
    "fechaIniCon",
    "fechaMatricula",
    "finVig",
    "hastaPcir",
    "hastaSoat",
    "idAlterno",
    "idAlternoMov",
    "identBenef",
    "identPotencialProp",
    "indor",
    "infraestructura",
    "inicioPcir",
    "inicioSoat",
    "inicioVig",
    "institucionRenova",
    "kilometraje",
    "marca",
    "marcaDesc",
    "modelo",
    "modeloDesc",
    "motor",
    "nombreBenef",
    "nombrePotencialProp",
    "nombrePropAnterior",
    "numEjes",
    "numRuedas",
    "numeroRevisado",
    "numeroTraspaso",
    "pais",
    "placaActual",
    "placaAnterior",
    "prendaComercial",
    "prendaIndustrial",
    "prohibidoEnajenar",
    "propietario",
    "registroPcir",
    "registroSoat",
    "remarcadoChasis",
    "remarcadoMotor",
    "reservaDominio",
    "residencia",
    "resultado",
    "robado",
    "rucCooperativa",
    "subClaseTran",
    "telefono",
    "tipoIdent",
    "tipoIdentBenef",
    "tipoIdentPotencialProp",
    "tipoPeso",
    "tipoServicio",
    "tipoTran",
    "tipoVehiculo",
    "tonelaje"
})
public class Vehiculo {

    protected String activoVig;
    protected String ambitoOper;
    protected String anio;
    protected String anioMatriculado;
    protected String apellido1;
    protected String apellido2;
    protected String aseguraSoat;
    protected String avaluoComercial;
    protected String cambioPropietario;
    protected String canvcp;
    protected String capacidad;
    protected String carroceria;
    protected String casaComercial;
    protected String cedulaPropAnterior;
    protected String celular;
    protected String chasis;
    protected String cilindraje;
    protected String claseServicio;
    protected String claseTran;
    protected String claseVehiculo;
    protected String color;
    protected String color2;
    protected String color3;
    protected String combustible;
    protected String comprobante;
    protected String contrato;
    protected String cooperativa;
    protected String correo;
    protected String desdePcir;
    protected String desdeSoat;
    protected String direccion;
    protected String disco;
    protected String docPropietario;
    protected String estado;
    protected String estadoCon;
    protected String estadoInf;
    protected String estadoPcir;
    protected String estadoSer;
    protected String estadoSoat;
    protected String facturaComercial;
    protected String fechaCaducidad;
    protected String fechaCompra;
    protected String fechaCompraVenta;
    protected String fechaFinCon;
    protected String fechaIniCon;
    protected String fechaMatricula;
    protected String finVig;
    protected String hastaPcir;
    protected String hastaSoat;
    protected String idAlterno;
    protected String idAlternoMov;
    protected String identBenef;
    protected String identPotencialProp;
    protected String indor;
    protected String infraestructura;
    protected String inicioPcir;
    protected String inicioSoat;
    protected String inicioVig;
    protected String institucionRenova;
    protected String kilometraje;
    protected String marca;
    protected String marcaDesc;
    protected String modelo;
    protected String modeloDesc;
    protected String motor;
    protected String nombreBenef;
    protected String nombrePotencialProp;
    protected String nombrePropAnterior;
    protected String numEjes;
    protected String numRuedas;
    protected String numeroRevisado;
    protected String numeroTraspaso;
    protected String pais;
    protected String placaActual;
    protected String placaAnterior;
    protected String prendaComercial;
    protected String prendaIndustrial;
    protected String prohibidoEnajenar;
    protected String propietario;
    protected String registroPcir;
    protected String registroSoat;
    protected String remarcadoChasis;
    protected String remarcadoMotor;
    protected String reservaDominio;
    protected String residencia;
    protected Resultado resultado;
    protected String robado;
    protected String rucCooperativa;
    protected String subClaseTran;
    protected String telefono;
    protected String tipoIdent;
    protected String tipoIdentBenef;
    protected String tipoIdentPotencialProp;
    protected String tipoPeso;
    protected String tipoServicio;
    protected String tipoTran;
    protected String tipoVehiculo;
    protected String tonelaje;

    /**
     * Gets the value of the activoVig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivoVig() {
        return activoVig;
    }

    /**
     * Sets the value of the activoVig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivoVig(String value) {
        this.activoVig = value;
    }

    /**
     * Gets the value of the ambitoOper property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmbitoOper() {
        return ambitoOper;
    }

    /**
     * Sets the value of the ambitoOper property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmbitoOper(String value) {
        this.ambitoOper = value;
    }

    /**
     * Gets the value of the anio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnio() {
        return anio;
    }

    /**
     * Sets the value of the anio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnio(String value) {
        this.anio = value;
    }

    /**
     * Gets the value of the anioMatriculado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnioMatriculado() {
        return anioMatriculado;
    }

    /**
     * Sets the value of the anioMatriculado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnioMatriculado(String value) {
        this.anioMatriculado = value;
    }

    /**
     * Gets the value of the apellido1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Sets the value of the apellido1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido1(String value) {
        this.apellido1 = value;
    }

    /**
     * Gets the value of the apellido2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Sets the value of the apellido2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido2(String value) {
        this.apellido2 = value;
    }

    /**
     * Gets the value of the aseguraSoat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAseguraSoat() {
        return aseguraSoat;
    }

    /**
     * Sets the value of the aseguraSoat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAseguraSoat(String value) {
        this.aseguraSoat = value;
    }

    /**
     * Gets the value of the avaluoComercial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvaluoComercial() {
        return avaluoComercial;
    }

    /**
     * Sets the value of the avaluoComercial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvaluoComercial(String value) {
        this.avaluoComercial = value;
    }

    /**
     * Gets the value of the cambioPropietario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCambioPropietario() {
        return cambioPropietario;
    }

    /**
     * Sets the value of the cambioPropietario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCambioPropietario(String value) {
        this.cambioPropietario = value;
    }

    /**
     * Gets the value of the canvcp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCanvcp() {
        return canvcp;
    }

    /**
     * Sets the value of the canvcp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCanvcp(String value) {
        this.canvcp = value;
    }

    /**
     * Gets the value of the capacidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapacidad() {
        return capacidad;
    }

    /**
     * Sets the value of the capacidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapacidad(String value) {
        this.capacidad = value;
    }

    /**
     * Gets the value of the carroceria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarroceria() {
        return carroceria;
    }

    /**
     * Sets the value of the carroceria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarroceria(String value) {
        this.carroceria = value;
    }

    /**
     * Gets the value of the casaComercial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCasaComercial() {
        return casaComercial;
    }

    /**
     * Sets the value of the casaComercial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCasaComercial(String value) {
        this.casaComercial = value;
    }

    /**
     * Gets the value of the cedulaPropAnterior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCedulaPropAnterior() {
        return cedulaPropAnterior;
    }

    /**
     * Sets the value of the cedulaPropAnterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCedulaPropAnterior(String value) {
        this.cedulaPropAnterior = value;
    }

    /**
     * Gets the value of the celular property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCelular() {
        return celular;
    }

    /**
     * Sets the value of the celular property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCelular(String value) {
        this.celular = value;
    }

    /**
     * Gets the value of the chasis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChasis() {
        return chasis;
    }

    /**
     * Sets the value of the chasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChasis(String value) {
        this.chasis = value;
    }

    /**
     * Gets the value of the cilindraje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCilindraje() {
        return cilindraje;
    }

    /**
     * Sets the value of the cilindraje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCilindraje(String value) {
        this.cilindraje = value;
    }

    /**
     * Gets the value of the claseServicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaseServicio() {
        return claseServicio;
    }

    /**
     * Sets the value of the claseServicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaseServicio(String value) {
        this.claseServicio = value;
    }

    /**
     * Gets the value of the claseTran property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaseTran() {
        return claseTran;
    }

    /**
     * Sets the value of the claseTran property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaseTran(String value) {
        this.claseTran = value;
    }

    /**
     * Gets the value of the claseVehiculo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaseVehiculo() {
        return claseVehiculo;
    }

    /**
     * Sets the value of the claseVehiculo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaseVehiculo(String value) {
        this.claseVehiculo = value;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the color2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor2() {
        return color2;
    }

    /**
     * Sets the value of the color2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor2(String value) {
        this.color2 = value;
    }

    /**
     * Gets the value of the color3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor3() {
        return color3;
    }

    /**
     * Sets the value of the color3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor3(String value) {
        this.color3 = value;
    }

    /**
     * Gets the value of the combustible property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCombustible() {
        return combustible;
    }

    /**
     * Sets the value of the combustible property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCombustible(String value) {
        this.combustible = value;
    }

    /**
     * Gets the value of the comprobante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComprobante() {
        return comprobante;
    }

    /**
     * Sets the value of the comprobante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComprobante(String value) {
        this.comprobante = value;
    }

    /**
     * Gets the value of the contrato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContrato() {
        return contrato;
    }

    /**
     * Sets the value of the contrato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContrato(String value) {
        this.contrato = value;
    }

    /**
     * Gets the value of the cooperativa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCooperativa() {
        return cooperativa;
    }

    /**
     * Sets the value of the cooperativa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCooperativa(String value) {
        this.cooperativa = value;
    }

    /**
     * Gets the value of the correo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Sets the value of the correo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreo(String value) {
        this.correo = value;
    }

    /**
     * Gets the value of the desdePcir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesdePcir() {
        return desdePcir;
    }

    /**
     * Sets the value of the desdePcir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesdePcir(String value) {
        this.desdePcir = value;
    }

    /**
     * Gets the value of the desdeSoat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesdeSoat() {
        return desdeSoat;
    }

    /**
     * Sets the value of the desdeSoat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesdeSoat(String value) {
        this.desdeSoat = value;
    }

    /**
     * Gets the value of the direccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Sets the value of the direccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccion(String value) {
        this.direccion = value;
    }

    /**
     * Gets the value of the disco property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisco() {
        return disco;
    }

    /**
     * Sets the value of the disco property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisco(String value) {
        this.disco = value;
    }

    /**
     * Gets the value of the docPropietario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocPropietario() {
        return docPropietario;
    }

    /**
     * Sets the value of the docPropietario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocPropietario(String value) {
        this.docPropietario = value;
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
     * Gets the value of the estadoCon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoCon() {
        return estadoCon;
    }

    /**
     * Sets the value of the estadoCon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoCon(String value) {
        this.estadoCon = value;
    }

    /**
     * Gets the value of the estadoInf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoInf() {
        return estadoInf;
    }

    /**
     * Sets the value of the estadoInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoInf(String value) {
        this.estadoInf = value;
    }

    /**
     * Gets the value of the estadoPcir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoPcir() {
        return estadoPcir;
    }

    /**
     * Sets the value of the estadoPcir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoPcir(String value) {
        this.estadoPcir = value;
    }

    /**
     * Gets the value of the estadoSer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoSer() {
        return estadoSer;
    }

    /**
     * Sets the value of the estadoSer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoSer(String value) {
        this.estadoSer = value;
    }

    /**
     * Gets the value of the estadoSoat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoSoat() {
        return estadoSoat;
    }

    /**
     * Sets the value of the estadoSoat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoSoat(String value) {
        this.estadoSoat = value;
    }

    /**
     * Gets the value of the facturaComercial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFacturaComercial() {
        return facturaComercial;
    }

    /**
     * Sets the value of the facturaComercial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFacturaComercial(String value) {
        this.facturaComercial = value;
    }

    /**
     * Gets the value of the fechaCaducidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    /**
     * Sets the value of the fechaCaducidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCaducidad(String value) {
        this.fechaCaducidad = value;
    }

    /**
     * Gets the value of the fechaCompra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCompra() {
        return fechaCompra;
    }

    /**
     * Sets the value of the fechaCompra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCompra(String value) {
        this.fechaCompra = value;
    }

    /**
     * Gets the value of the fechaCompraVenta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCompraVenta() {
        return fechaCompraVenta;
    }

    /**
     * Sets the value of the fechaCompraVenta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCompraVenta(String value) {
        this.fechaCompraVenta = value;
    }

    /**
     * Gets the value of the fechaFinCon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaFinCon() {
        return fechaFinCon;
    }

    /**
     * Sets the value of the fechaFinCon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaFinCon(String value) {
        this.fechaFinCon = value;
    }

    /**
     * Gets the value of the fechaIniCon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaIniCon() {
        return fechaIniCon;
    }

    /**
     * Sets the value of the fechaIniCon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaIniCon(String value) {
        this.fechaIniCon = value;
    }

    /**
     * Gets the value of the fechaMatricula property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaMatricula() {
        return fechaMatricula;
    }

    /**
     * Sets the value of the fechaMatricula property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaMatricula(String value) {
        this.fechaMatricula = value;
    }

    /**
     * Gets the value of the finVig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinVig() {
        return finVig;
    }

    /**
     * Sets the value of the finVig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinVig(String value) {
        this.finVig = value;
    }

    /**
     * Gets the value of the hastaPcir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHastaPcir() {
        return hastaPcir;
    }

    /**
     * Sets the value of the hastaPcir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHastaPcir(String value) {
        this.hastaPcir = value;
    }

    /**
     * Gets the value of the hastaSoat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHastaSoat() {
        return hastaSoat;
    }

    /**
     * Sets the value of the hastaSoat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHastaSoat(String value) {
        this.hastaSoat = value;
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
     * Gets the value of the idAlternoMov property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAlternoMov() {
        return idAlternoMov;
    }

    /**
     * Sets the value of the idAlternoMov property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAlternoMov(String value) {
        this.idAlternoMov = value;
    }

    /**
     * Gets the value of the identBenef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentBenef() {
        return identBenef;
    }

    /**
     * Sets the value of the identBenef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentBenef(String value) {
        this.identBenef = value;
    }

    /**
     * Gets the value of the identPotencialProp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentPotencialProp() {
        return identPotencialProp;
    }

    /**
     * Sets the value of the identPotencialProp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentPotencialProp(String value) {
        this.identPotencialProp = value;
    }

    /**
     * Gets the value of the indor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndor() {
        return indor;
    }

    /**
     * Sets the value of the indor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndor(String value) {
        this.indor = value;
    }

    /**
     * Gets the value of the infraestructura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfraestructura() {
        return infraestructura;
    }

    /**
     * Sets the value of the infraestructura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfraestructura(String value) {
        this.infraestructura = value;
    }

    /**
     * Gets the value of the inicioPcir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInicioPcir() {
        return inicioPcir;
    }

    /**
     * Sets the value of the inicioPcir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInicioPcir(String value) {
        this.inicioPcir = value;
    }

    /**
     * Gets the value of the inicioSoat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInicioSoat() {
        return inicioSoat;
    }

    /**
     * Sets the value of the inicioSoat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInicioSoat(String value) {
        this.inicioSoat = value;
    }

    /**
     * Gets the value of the inicioVig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInicioVig() {
        return inicioVig;
    }

    /**
     * Sets the value of the inicioVig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInicioVig(String value) {
        this.inicioVig = value;
    }

    /**
     * Gets the value of the institucionRenova property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitucionRenova() {
        return institucionRenova;
    }

    /**
     * Sets the value of the institucionRenova property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitucionRenova(String value) {
        this.institucionRenova = value;
    }

    /**
     * Gets the value of the kilometraje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKilometraje() {
        return kilometraje;
    }

    /**
     * Sets the value of the kilometraje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKilometraje(String value) {
        this.kilometraje = value;
    }

    /**
     * Gets the value of the marca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Sets the value of the marca property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarca(String value) {
        this.marca = value;
    }

    /**
     * Gets the value of the marcaDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarcaDesc() {
        return marcaDesc;
    }

    /**
     * Sets the value of the marcaDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarcaDesc(String value) {
        this.marcaDesc = value;
    }

    /**
     * Gets the value of the modelo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Sets the value of the modelo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelo(String value) {
        this.modelo = value;
    }

    /**
     * Gets the value of the modeloDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModeloDesc() {
        return modeloDesc;
    }

    /**
     * Sets the value of the modeloDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModeloDesc(String value) {
        this.modeloDesc = value;
    }

    /**
     * Gets the value of the motor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotor() {
        return motor;
    }

    /**
     * Sets the value of the motor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotor(String value) {
        this.motor = value;
    }

    /**
     * Gets the value of the nombreBenef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreBenef() {
        return nombreBenef;
    }

    /**
     * Sets the value of the nombreBenef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreBenef(String value) {
        this.nombreBenef = value;
    }

    /**
     * Gets the value of the nombrePotencialProp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombrePotencialProp() {
        return nombrePotencialProp;
    }

    /**
     * Sets the value of the nombrePotencialProp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombrePotencialProp(String value) {
        this.nombrePotencialProp = value;
    }

    /**
     * Gets the value of the nombrePropAnterior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombrePropAnterior() {
        return nombrePropAnterior;
    }

    /**
     * Sets the value of the nombrePropAnterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombrePropAnterior(String value) {
        this.nombrePropAnterior = value;
    }

    /**
     * Gets the value of the numEjes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumEjes() {
        return numEjes;
    }

    /**
     * Sets the value of the numEjes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumEjes(String value) {
        this.numEjes = value;
    }

    /**
     * Gets the value of the numRuedas property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumRuedas() {
        return numRuedas;
    }

    /**
     * Sets the value of the numRuedas property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumRuedas(String value) {
        this.numRuedas = value;
    }

    /**
     * Gets the value of the numeroRevisado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRevisado() {
        return numeroRevisado;
    }

    /**
     * Sets the value of the numeroRevisado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRevisado(String value) {
        this.numeroRevisado = value;
    }

    /**
     * Gets the value of the numeroTraspaso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroTraspaso() {
        return numeroTraspaso;
    }

    /**
     * Sets the value of the numeroTraspaso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTraspaso(String value) {
        this.numeroTraspaso = value;
    }

    /**
     * Gets the value of the pais property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Sets the value of the pais property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Gets the value of the placaActual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlacaActual() {
        return placaActual;
    }

    /**
     * Sets the value of the placaActual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlacaActual(String value) {
        this.placaActual = value;
    }

    /**
     * Gets the value of the placaAnterior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlacaAnterior() {
        return placaAnterior;
    }

    /**
     * Sets the value of the placaAnterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlacaAnterior(String value) {
        this.placaAnterior = value;
    }

    /**
     * Gets the value of the prendaComercial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrendaComercial() {
        return prendaComercial;
    }

    /**
     * Sets the value of the prendaComercial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrendaComercial(String value) {
        this.prendaComercial = value;
    }

    /**
     * Gets the value of the prendaIndustrial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrendaIndustrial() {
        return prendaIndustrial;
    }

    /**
     * Sets the value of the prendaIndustrial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrendaIndustrial(String value) {
        this.prendaIndustrial = value;
    }

    /**
     * Gets the value of the prohibidoEnajenar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProhibidoEnajenar() {
        return prohibidoEnajenar;
    }

    /**
     * Sets the value of the prohibidoEnajenar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProhibidoEnajenar(String value) {
        this.prohibidoEnajenar = value;
    }

    /**
     * Gets the value of the propietario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropietario() {
        return propietario;
    }

    /**
     * Sets the value of the propietario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropietario(String value) {
        this.propietario = value;
    }

    /**
     * Gets the value of the registroPcir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistroPcir() {
        return registroPcir;
    }

    /**
     * Sets the value of the registroPcir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistroPcir(String value) {
        this.registroPcir = value;
    }

    /**
     * Gets the value of the registroSoat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistroSoat() {
        return registroSoat;
    }

    /**
     * Sets the value of the registroSoat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistroSoat(String value) {
        this.registroSoat = value;
    }

    /**
     * Gets the value of the remarcadoChasis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarcadoChasis() {
        return remarcadoChasis;
    }

    /**
     * Sets the value of the remarcadoChasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarcadoChasis(String value) {
        this.remarcadoChasis = value;
    }

    /**
     * Gets the value of the remarcadoMotor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarcadoMotor() {
        return remarcadoMotor;
    }

    /**
     * Sets the value of the remarcadoMotor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarcadoMotor(String value) {
        this.remarcadoMotor = value;
    }

    /**
     * Gets the value of the reservaDominio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReservaDominio() {
        return reservaDominio;
    }

    /**
     * Sets the value of the reservaDominio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReservaDominio(String value) {
        this.reservaDominio = value;
    }

    /**
     * Gets the value of the residencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidencia() {
        return residencia;
    }

    /**
     * Sets the value of the residencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidencia(String value) {
        this.residencia = value;
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
     * Gets the value of the robado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRobado() {
        return robado;
    }

    /**
     * Sets the value of the robado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRobado(String value) {
        this.robado = value;
    }

    /**
     * Gets the value of the rucCooperativa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRucCooperativa() {
        return rucCooperativa;
    }

    /**
     * Sets the value of the rucCooperativa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRucCooperativa(String value) {
        this.rucCooperativa = value;
    }

    /**
     * Gets the value of the subClaseTran property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubClaseTran() {
        return subClaseTran;
    }

    /**
     * Sets the value of the subClaseTran property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubClaseTran(String value) {
        this.subClaseTran = value;
    }

    /**
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Gets the value of the tipoIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIdent() {
        return tipoIdent;
    }

    /**
     * Sets the value of the tipoIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIdent(String value) {
        this.tipoIdent = value;
    }

    /**
     * Gets the value of the tipoIdentBenef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIdentBenef() {
        return tipoIdentBenef;
    }

    /**
     * Sets the value of the tipoIdentBenef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIdentBenef(String value) {
        this.tipoIdentBenef = value;
    }

    /**
     * Gets the value of the tipoIdentPotencialProp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIdentPotencialProp() {
        return tipoIdentPotencialProp;
    }

    /**
     * Sets the value of the tipoIdentPotencialProp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIdentPotencialProp(String value) {
        this.tipoIdentPotencialProp = value;
    }

    /**
     * Gets the value of the tipoPeso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPeso() {
        return tipoPeso;
    }

    /**
     * Sets the value of the tipoPeso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPeso(String value) {
        this.tipoPeso = value;
    }

    /**
     * Gets the value of the tipoServicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoServicio() {
        return tipoServicio;
    }

    /**
     * Sets the value of the tipoServicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoServicio(String value) {
        this.tipoServicio = value;
    }

    /**
     * Gets the value of the tipoTran property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoTran() {
        return tipoTran;
    }

    /**
     * Sets the value of the tipoTran property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoTran(String value) {
        this.tipoTran = value;
    }

    /**
     * Gets the value of the tipoVehiculo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    /**
     * Sets the value of the tipoVehiculo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoVehiculo(String value) {
        this.tipoVehiculo = value;
    }

    /**
     * Gets the value of the tonelaje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTonelaje() {
        return tonelaje;
    }

    /**
     * Sets the value of the tonelaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTonelaje(String value) {
        this.tonelaje = value;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Vehiculo [activoVig=");
		builder.append(activoVig);
		builder.append(", ambitoOper=");
		builder.append(ambitoOper);
		builder.append(", anio=");
		builder.append(anio);
		builder.append(", anioMatriculado=");
		builder.append(anioMatriculado);
		builder.append(", apellido1=");
		builder.append(apellido1);
		builder.append(", apellido2=");
		builder.append(apellido2);
		builder.append(", aseguraSoat=");
		builder.append(aseguraSoat);
		builder.append(", avaluoComercial=");
		builder.append(avaluoComercial);
		builder.append(", cambioPropietario=");
		builder.append(cambioPropietario);
		builder.append(", canvcp=");
		builder.append(canvcp);
		builder.append(", capacidad=");
		builder.append(capacidad);
		builder.append(", carroceria=");
		builder.append(carroceria);
		builder.append(", casaComercial=");
		builder.append(casaComercial);
		builder.append(", cedulaPropAnterior=");
		builder.append(cedulaPropAnterior);
		builder.append(", celular=");
		builder.append(celular);
		builder.append(", chasis=");
		builder.append(chasis);
		builder.append(", cilindraje=");
		builder.append(cilindraje);
		builder.append(", claseServicio=");
		builder.append(claseServicio);
		builder.append(", claseTran=");
		builder.append(claseTran);
		builder.append(", claseVehiculo=");
		builder.append(claseVehiculo);
		builder.append(", color=");
		builder.append(color);
		builder.append(", color2=");
		builder.append(color2);
		builder.append(", color3=");
		builder.append(color3);
		builder.append(", combustible=");
		builder.append(combustible);
		builder.append(", comprobante=");
		builder.append(comprobante);
		builder.append(", contrato=");
		builder.append(contrato);
		builder.append(", cooperativa=");
		builder.append(cooperativa);
		builder.append(", correo=");
		builder.append(correo);
		builder.append(", desdePcir=");
		builder.append(desdePcir);
		builder.append(", desdeSoat=");
		builder.append(desdeSoat);
		builder.append(", direccion=");
		builder.append(direccion);
		builder.append(", disco=");
		builder.append(disco);
		builder.append(", docPropietario=");
		builder.append(docPropietario);
		builder.append(", estado=");
		builder.append(estado);
		builder.append(", estadoCon=");
		builder.append(estadoCon);
		builder.append(", estadoInf=");
		builder.append(estadoInf);
		builder.append(", estadoPcir=");
		builder.append(estadoPcir);
		builder.append(", estadoSer=");
		builder.append(estadoSer);
		builder.append(", estadoSoat=");
		builder.append(estadoSoat);
		builder.append(", facturaComercial=");
		builder.append(facturaComercial);
		builder.append(", fechaCaducidad=");
		builder.append(fechaCaducidad);
		builder.append(", fechaCompra=");
		builder.append(fechaCompra);
		builder.append(", fechaCompraVenta=");
		builder.append(fechaCompraVenta);
		builder.append(", fechaFinCon=");
		builder.append(fechaFinCon);
		builder.append(", fechaIniCon=");
		builder.append(fechaIniCon);
		builder.append(", fechaMatricula=");
		builder.append(fechaMatricula);
		builder.append(", finVig=");
		builder.append(finVig);
		builder.append(", hastaPcir=");
		builder.append(hastaPcir);
		builder.append(", hastaSoat=");
		builder.append(hastaSoat);
		builder.append(", idAlterno=");
		builder.append(idAlterno);
		builder.append(", idAlternoMov=");
		builder.append(idAlternoMov);
		builder.append(", identBenef=");
		builder.append(identBenef);
		builder.append(", identPotencialProp=");
		builder.append(identPotencialProp);
		builder.append(", indor=");
		builder.append(indor);
		builder.append(", infraestructura=");
		builder.append(infraestructura);
		builder.append(", inicioPcir=");
		builder.append(inicioPcir);
		builder.append(", inicioSoat=");
		builder.append(inicioSoat);
		builder.append(", inicioVig=");
		builder.append(inicioVig);
		builder.append(", institucionRenova=");
		builder.append(institucionRenova);
		builder.append(", kilometraje=");
		builder.append(kilometraje);
		builder.append(", marca=");
		builder.append(marca);
		builder.append(", marcaDesc=");
		builder.append(marcaDesc);
		builder.append(", modelo=");
		builder.append(modelo);
		builder.append(", modeloDesc=");
		builder.append(modeloDesc);
		builder.append(", motor=");
		builder.append(motor);
		builder.append(", nombreBenef=");
		builder.append(nombreBenef);
		builder.append(", nombrePotencialProp=");
		builder.append(nombrePotencialProp);
		builder.append(", nombrePropAnterior=");
		builder.append(nombrePropAnterior);
		builder.append(", numEjes=");
		builder.append(numEjes);
		builder.append(", numRuedas=");
		builder.append(numRuedas);
		builder.append(", numeroRevisado=");
		builder.append(numeroRevisado);
		builder.append(", numeroTraspaso=");
		builder.append(numeroTraspaso);
		builder.append(", pais=");
		builder.append(pais);
		builder.append(", placaActual=");
		builder.append(placaActual);
		builder.append(", placaAnterior=");
		builder.append(placaAnterior);
		builder.append(", prendaComercial=");
		builder.append(prendaComercial);
		builder.append(", prendaIndustrial=");
		builder.append(prendaIndustrial);
		builder.append(", prohibidoEnajenar=");
		builder.append(prohibidoEnajenar);
		builder.append(", propietario=");
		builder.append(propietario);
		builder.append(", registroPcir=");
		builder.append(registroPcir);
		builder.append(", registroSoat=");
		builder.append(registroSoat);
		builder.append(", remarcadoChasis=");
		builder.append(remarcadoChasis);
		builder.append(", remarcadoMotor=");
		builder.append(remarcadoMotor);
		builder.append(", reservaDominio=");
		builder.append(reservaDominio);
		builder.append(", residencia=");
		builder.append(residencia);
		builder.append(", resultado=");
		builder.append(resultado);
		builder.append(", robado=");
		builder.append(robado);
		builder.append(", rucCooperativa=");
		builder.append(rucCooperativa);
		builder.append(", subClaseTran=");
		builder.append(subClaseTran);
		builder.append(", telefono=");
		builder.append(telefono);
		builder.append(", tipoIdent=");
		builder.append(tipoIdent);
		builder.append(", tipoIdentBenef=");
		builder.append(tipoIdentBenef);
		builder.append(", tipoIdentPotencialProp=");
		builder.append(tipoIdentPotencialProp);
		builder.append(", tipoPeso=");
		builder.append(tipoPeso);
		builder.append(", tipoServicio=");
		builder.append(tipoServicio);
		builder.append(", tipoTran=");
		builder.append(tipoTran);
		builder.append(", tipoVehiculo=");
		builder.append(tipoVehiculo);
		builder.append(", tonelaje=");
		builder.append(tonelaje);
		builder.append("]");
		return builder.toString();
	}

}
