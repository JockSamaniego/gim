
package ec.gob.loja.middleapp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ec.gob.loja.middleapp package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ConsultarInfraccionResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarInfraccionResponse");
    private final static QName _IngresarImagenResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "ingresarImagenResponse");
    private final static QName _ConsultarBloqueos_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarBloqueos");
    private final static QName _ConsultarVehiculoResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarVehiculoResponse");
    private final static QName _ConsultarLicencia_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarLicencia");
    private final static QName _ConsultarFotoResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarFotoResponse");
    private final static QName _ConsultarHistorial_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarHistorial");
    private final static QName _ConsultarFoto_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarFoto");
    private final static QName _IngresarImagen_QNAME = new QName("http://middleapp.loja.gob.ec/", "ingresarImagen");
    private final static QName _ConsultarHistorialResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarHistorialResponse");
    private final static QName _ReplicarInfraccionResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "replicarInfraccionResponse");
    private final static QName _IngresarInfraccionResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "ingresarInfraccionResponse");
    private final static QName _ConsultarInfraccion_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarInfraccion");
    private final static QName _ConsultarDeudasResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarDeudasResponse");
    private final static QName _ConsultarBloqueosResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarBloqueosResponse");
    private final static QName _ReplicarInfraccion_QNAME = new QName("http://middleapp.loja.gob.ec/", "replicarInfraccion");
    private final static QName _ConsultarDeudas_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarDeudas");
    private final static QName _ConsultarLicenciaResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarLicenciaResponse");
    private final static QName _ConsultarVehiculo_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarVehiculo");
    private final static QName _ConsultarDeclaracionJurResponse_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarDeclaracionJurResponse");
    private final static QName _IngresarInfraccion_QNAME = new QName("http://middleapp.loja.gob.ec/", "ingresarInfraccion");
    private final static QName _ConsultarDeclaracionJur_QNAME = new QName("http://middleapp.loja.gob.ec/", "consultarDeclaracionJur");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ec.gob.loja.middleapp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConsultarFotoResponse }
     * 
     */
    public ConsultarFotoResponse createConsultarFotoResponse() {
        return new ConsultarFotoResponse();
    }

    /**
     * Create an instance of {@link ConsultarBloqueosResponse }
     * 
     */
    public ConsultarBloqueosResponse createConsultarBloqueosResponse() {
        return new ConsultarBloqueosResponse();
    }

    /**
     * Create an instance of {@link ConsultarFoto }
     * 
     */
    public ConsultarFoto createConsultarFoto() {
        return new ConsultarFoto();
    }

    /**
     * Create an instance of {@link ResponseDeclaracion }
     * 
     */
    public ResponseDeclaracion createResponseDeclaracion() {
        return new ResponseDeclaracion();
    }

    /**
     * Create an instance of {@link ConsultarVehiculo }
     * 
     */
    public ConsultarVehiculo createConsultarVehiculo() {
        return new ConsultarVehiculo();
    }

    /**
     * Create an instance of {@link ConsultarInfraccionResponse }
     * 
     */
    public ConsultarInfraccionResponse createConsultarInfraccionResponse() {
        return new ConsultarInfraccionResponse();
    }

    /**
     * Create an instance of {@link ConsultarVehiculoResponse }
     * 
     */
    public ConsultarVehiculoResponse createConsultarVehiculoResponse() {
        return new ConsultarVehiculoResponse();
    }

    /**
     * Create an instance of {@link ReplicarInfraccion }
     * 
     */
    public ReplicarInfraccion createReplicarInfraccion() {
        return new ReplicarInfraccion();
    }

    /**
     * Create an instance of {@link IngresarImagenResponse }
     * 
     */
    public IngresarImagenResponse createIngresarImagenResponse() {
        return new IngresarImagenResponse();
    }

    /**
     * Create an instance of {@link ResponseBloqueos }
     * 
     */
    public ResponseBloqueos createResponseBloqueos() {
        return new ResponseBloqueos();
    }

    /**
     * Create an instance of {@link ResponseFoto }
     * 
     */
    public ResponseFoto createResponseFoto() {
        return new ResponseFoto();
    }

    /**
     * Create an instance of {@link ConsultarHistorialResponse }
     * 
     */
    public ConsultarHistorialResponse createConsultarHistorialResponse() {
        return new ConsultarHistorialResponse();
    }

    /**
     * Create an instance of {@link ResponseHistorialDominio }
     * 
     */
    public ResponseHistorialDominio createResponseHistorialDominio() {
        return new ResponseHistorialDominio();
    }

    /**
     * Create an instance of {@link ResponseDatosSalidaInfracciones }
     * 
     */
    public ResponseDatosSalidaInfracciones createResponseDatosSalidaInfracciones() {
        return new ResponseDatosSalidaInfracciones();
    }

    /**
     * Create an instance of {@link GeneralResponse }
     * 
     */
    public GeneralResponse createGeneralResponse() {
        return new GeneralResponse();
    }

    /**
     * Create an instance of {@link IngresarInfraccion }
     * 
     */
    public IngresarInfraccion createIngresarInfraccion() {
        return new IngresarInfraccion();
    }

    /**
     * Create an instance of {@link ConsultarDeclaracionJur }
     * 
     */
    public ConsultarDeclaracionJur createConsultarDeclaracionJur() {
        return new ConsultarDeclaracionJur();
    }

    /**
     * Create an instance of {@link ConsultarBloqueos }
     * 
     */
    public ConsultarBloqueos createConsultarBloqueos() {
        return new ConsultarBloqueos();
    }

    /**
     * Create an instance of {@link ConsultarInfraccion }
     * 
     */
    public ConsultarInfraccion createConsultarInfraccion() {
        return new ConsultarInfraccion();
    }

    /**
     * Create an instance of {@link ConsultarHistorial }
     * 
     */
    public ConsultarHistorial createConsultarHistorial() {
        return new ConsultarHistorial();
    }

    /**
     * Create an instance of {@link ReplicarInfraccionResponse }
     * 
     */
    public ReplicarInfraccionResponse createReplicarInfraccionResponse() {
        return new ReplicarInfraccionResponse();
    }

    /**
     * Create an instance of {@link ResponseVehiculo }
     * 
     */
    public ResponseVehiculo createResponseVehiculo() {
        return new ResponseVehiculo();
    }

    /**
     * Create an instance of {@link ResponseResultado }
     * 
     */
    public ResponseResultado createResponseResultado() {
        return new ResponseResultado();
    }

    /**
     * Create an instance of {@link ConsultarDeclaracionJurResponse }
     * 
     */
    public ConsultarDeclaracionJurResponse createConsultarDeclaracionJurResponse() {
        return new ConsultarDeclaracionJurResponse();
    }

    /**
     * Create an instance of {@link IngresarInfraccionResponse }
     * 
     */
    public IngresarInfraccionResponse createIngresarInfraccionResponse() {
        return new IngresarInfraccionResponse();
    }

    /**
     * Create an instance of {@link ResponseDeuda }
     * 
     */
    public ResponseDeuda createResponseDeuda() {
        return new ResponseDeuda();
    }

    /**
     * Create an instance of {@link IngresarImagen }
     * 
     */
    public IngresarImagen createIngresarImagen() {
        return new IngresarImagen();
    }

    /**
     * Create an instance of {@link ConsultarLicenciaResponse }
     * 
     */
    public ConsultarLicenciaResponse createConsultarLicenciaResponse() {
        return new ConsultarLicenciaResponse();
    }

    /**
     * Create an instance of {@link ResponseDatosLicencia }
     * 
     */
    public ResponseDatosLicencia createResponseDatosLicencia() {
        return new ResponseDatosLicencia();
    }

    /**
     * Create an instance of {@link ConsultarDeudas }
     * 
     */
    public ConsultarDeudas createConsultarDeudas() {
        return new ConsultarDeudas();
    }

    /**
     * Create an instance of {@link ConsultarLicencia }
     * 
     */
    public ConsultarLicencia createConsultarLicencia() {
        return new ConsultarLicencia();
    }

    /**
     * Create an instance of {@link ConsultarDeudasResponse }
     * 
     */
    public ConsultarDeudasResponse createConsultarDeudasResponse() {
        return new ConsultarDeudasResponse();
    }

    /**
     * Create an instance of {@link ResponseInfraccion }
     * 
     */
    public ResponseInfraccion createResponseInfraccion() {
        return new ResponseInfraccion();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarInfraccionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarInfraccionResponse")
    public JAXBElement<ConsultarInfraccionResponse> createConsultarInfraccionResponse(ConsultarInfraccionResponse value) {
        return new JAXBElement<ConsultarInfraccionResponse>(_ConsultarInfraccionResponse_QNAME, ConsultarInfraccionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IngresarImagenResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "ingresarImagenResponse")
    public JAXBElement<IngresarImagenResponse> createIngresarImagenResponse(IngresarImagenResponse value) {
        return new JAXBElement<IngresarImagenResponse>(_IngresarImagenResponse_QNAME, IngresarImagenResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarBloqueos }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarBloqueos")
    public JAXBElement<ConsultarBloqueos> createConsultarBloqueos(ConsultarBloqueos value) {
        return new JAXBElement<ConsultarBloqueos>(_ConsultarBloqueos_QNAME, ConsultarBloqueos.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarVehiculoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarVehiculoResponse")
    public JAXBElement<ConsultarVehiculoResponse> createConsultarVehiculoResponse(ConsultarVehiculoResponse value) {
        return new JAXBElement<ConsultarVehiculoResponse>(_ConsultarVehiculoResponse_QNAME, ConsultarVehiculoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarLicencia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarLicencia")
    public JAXBElement<ConsultarLicencia> createConsultarLicencia(ConsultarLicencia value) {
        return new JAXBElement<ConsultarLicencia>(_ConsultarLicencia_QNAME, ConsultarLicencia.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarFotoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarFotoResponse")
    public JAXBElement<ConsultarFotoResponse> createConsultarFotoResponse(ConsultarFotoResponse value) {
        return new JAXBElement<ConsultarFotoResponse>(_ConsultarFotoResponse_QNAME, ConsultarFotoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarHistorial }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarHistorial")
    public JAXBElement<ConsultarHistorial> createConsultarHistorial(ConsultarHistorial value) {
        return new JAXBElement<ConsultarHistorial>(_ConsultarHistorial_QNAME, ConsultarHistorial.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarFoto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarFoto")
    public JAXBElement<ConsultarFoto> createConsultarFoto(ConsultarFoto value) {
        return new JAXBElement<ConsultarFoto>(_ConsultarFoto_QNAME, ConsultarFoto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IngresarImagen }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "ingresarImagen")
    public JAXBElement<IngresarImagen> createIngresarImagen(IngresarImagen value) {
        return new JAXBElement<IngresarImagen>(_IngresarImagen_QNAME, IngresarImagen.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarHistorialResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarHistorialResponse")
    public JAXBElement<ConsultarHistorialResponse> createConsultarHistorialResponse(ConsultarHistorialResponse value) {
        return new JAXBElement<ConsultarHistorialResponse>(_ConsultarHistorialResponse_QNAME, ConsultarHistorialResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReplicarInfraccionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "replicarInfraccionResponse")
    public JAXBElement<ReplicarInfraccionResponse> createReplicarInfraccionResponse(ReplicarInfraccionResponse value) {
        return new JAXBElement<ReplicarInfraccionResponse>(_ReplicarInfraccionResponse_QNAME, ReplicarInfraccionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IngresarInfraccionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "ingresarInfraccionResponse")
    public JAXBElement<IngresarInfraccionResponse> createIngresarInfraccionResponse(IngresarInfraccionResponse value) {
        return new JAXBElement<IngresarInfraccionResponse>(_IngresarInfraccionResponse_QNAME, IngresarInfraccionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarInfraccion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarInfraccion")
    public JAXBElement<ConsultarInfraccion> createConsultarInfraccion(ConsultarInfraccion value) {
        return new JAXBElement<ConsultarInfraccion>(_ConsultarInfraccion_QNAME, ConsultarInfraccion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarDeudasResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarDeudasResponse")
    public JAXBElement<ConsultarDeudasResponse> createConsultarDeudasResponse(ConsultarDeudasResponse value) {
        return new JAXBElement<ConsultarDeudasResponse>(_ConsultarDeudasResponse_QNAME, ConsultarDeudasResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarBloqueosResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarBloqueosResponse")
    public JAXBElement<ConsultarBloqueosResponse> createConsultarBloqueosResponse(ConsultarBloqueosResponse value) {
        return new JAXBElement<ConsultarBloqueosResponse>(_ConsultarBloqueosResponse_QNAME, ConsultarBloqueosResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReplicarInfraccion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "replicarInfraccion")
    public JAXBElement<ReplicarInfraccion> createReplicarInfraccion(ReplicarInfraccion value) {
        return new JAXBElement<ReplicarInfraccion>(_ReplicarInfraccion_QNAME, ReplicarInfraccion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarDeudas }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarDeudas")
    public JAXBElement<ConsultarDeudas> createConsultarDeudas(ConsultarDeudas value) {
        return new JAXBElement<ConsultarDeudas>(_ConsultarDeudas_QNAME, ConsultarDeudas.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarLicenciaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarLicenciaResponse")
    public JAXBElement<ConsultarLicenciaResponse> createConsultarLicenciaResponse(ConsultarLicenciaResponse value) {
        return new JAXBElement<ConsultarLicenciaResponse>(_ConsultarLicenciaResponse_QNAME, ConsultarLicenciaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarVehiculo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarVehiculo")
    public JAXBElement<ConsultarVehiculo> createConsultarVehiculo(ConsultarVehiculo value) {
        return new JAXBElement<ConsultarVehiculo>(_ConsultarVehiculo_QNAME, ConsultarVehiculo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarDeclaracionJurResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarDeclaracionJurResponse")
    public JAXBElement<ConsultarDeclaracionJurResponse> createConsultarDeclaracionJurResponse(ConsultarDeclaracionJurResponse value) {
        return new JAXBElement<ConsultarDeclaracionJurResponse>(_ConsultarDeclaracionJurResponse_QNAME, ConsultarDeclaracionJurResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IngresarInfraccion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "ingresarInfraccion")
    public JAXBElement<IngresarInfraccion> createIngresarInfraccion(IngresarInfraccion value) {
        return new JAXBElement<IngresarInfraccion>(_IngresarInfraccion_QNAME, IngresarInfraccion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarDeclaracionJur }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://middleapp.loja.gob.ec/", name = "consultarDeclaracionJur")
    public JAXBElement<ConsultarDeclaracionJur> createConsultarDeclaracionJur(ConsultarDeclaracionJur value) {
        return new JAXBElement<ConsultarDeclaracionJur>(_ConsultarDeclaracionJur_QNAME, ConsultarDeclaracionJur.class, null, value);
    }

}
