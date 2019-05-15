/**
 * WsPagoSolicitudLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMTATM;

public class WsPagoSolicitudLocator extends org.apache.axis.client.Service implements SMTATM.WsPagoSolicitud {

    public WsPagoSolicitudLocator() {
    }


    public WsPagoSolicitudLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsPagoSolicitudLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for wsPagoSolicitudSoapPort
    private java.lang.String wsPagoSolicitudSoapPort_address = "http://190.214.31.163:8080/WSSCRTV/awspagosolicitud.aspx";

    public java.lang.String getwsPagoSolicitudSoapPortAddress() {
        return wsPagoSolicitudSoapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String wsPagoSolicitudSoapPortWSDDServiceName = "wsPagoSolicitudSoapPort";

    public java.lang.String getwsPagoSolicitudSoapPortWSDDServiceName() {
        return wsPagoSolicitudSoapPortWSDDServiceName;
    }

    public void setwsPagoSolicitudSoapPortWSDDServiceName(java.lang.String name) {
        wsPagoSolicitudSoapPortWSDDServiceName = name;
    }

    public SMTATM.WsPagoSolicitudSoapPort getwsPagoSolicitudSoapPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(wsPagoSolicitudSoapPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getwsPagoSolicitudSoapPort(endpoint);
    }

    public SMTATM.WsPagoSolicitudSoapPort getwsPagoSolicitudSoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SMTATM.WsPagoSolicitudSoapBindingStub _stub = new SMTATM.WsPagoSolicitudSoapBindingStub(portAddress, this);
            _stub.setPortName(getwsPagoSolicitudSoapPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setwsPagoSolicitudSoapPortEndpointAddress(java.lang.String address) {
        wsPagoSolicitudSoapPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SMTATM.WsPagoSolicitudSoapPort.class.isAssignableFrom(serviceEndpointInterface)) {
                SMTATM.WsPagoSolicitudSoapBindingStub _stub = new SMTATM.WsPagoSolicitudSoapBindingStub(new java.net.URL(wsPagoSolicitudSoapPort_address), this);
                _stub.setPortName(getwsPagoSolicitudSoapPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("wsPagoSolicitudSoapPort".equals(inputPortName)) {
            return getwsPagoSolicitudSoapPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("SMTATM", "wsPagoSolicitud");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SMTATM", "wsPagoSolicitudSoapPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("wsPagoSolicitudSoapPort".equals(portName)) {
            setwsPagoSolicitudSoapPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
