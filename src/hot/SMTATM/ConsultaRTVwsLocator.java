/**
 * ConsultaRTVwsLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMTATM;

public class ConsultaRTVwsLocator extends org.apache.axis.client.Service implements SMTATM.ConsultaRTVws {

    public ConsultaRTVwsLocator() {
    }


    public ConsultaRTVwsLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConsultaRTVwsLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ConsultaRTVwsSoapPort
    private java.lang.String ConsultaRTVwsSoapPort_address = "http://190.214.31.163:8080/WSSCRTV/aconsultartvws.aspx";

    public java.lang.String getConsultaRTVwsSoapPortAddress() {
        return ConsultaRTVwsSoapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConsultaRTVwsSoapPortWSDDServiceName = "ConsultaRTVwsSoapPort";

    public java.lang.String getConsultaRTVwsSoapPortWSDDServiceName() {
        return ConsultaRTVwsSoapPortWSDDServiceName;
    }

    public void setConsultaRTVwsSoapPortWSDDServiceName(java.lang.String name) {
        ConsultaRTVwsSoapPortWSDDServiceName = name;
    }

    public SMTATM.ConsultaRTVwsSoapPort getConsultaRTVwsSoapPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ConsultaRTVwsSoapPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getConsultaRTVwsSoapPort(endpoint);
    }

    public SMTATM.ConsultaRTVwsSoapPort getConsultaRTVwsSoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SMTATM.ConsultaRTVwsSoapBindingStub _stub = new SMTATM.ConsultaRTVwsSoapBindingStub(portAddress, this);
            _stub.setPortName(getConsultaRTVwsSoapPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setConsultaRTVwsSoapPortEndpointAddress(java.lang.String address) {
        ConsultaRTVwsSoapPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SMTATM.ConsultaRTVwsSoapPort.class.isAssignableFrom(serviceEndpointInterface)) {
                SMTATM.ConsultaRTVwsSoapBindingStub _stub = new SMTATM.ConsultaRTVwsSoapBindingStub(new java.net.URL(ConsultaRTVwsSoapPort_address), this);
                _stub.setPortName(getConsultaRTVwsSoapPortWSDDServiceName());
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
        if ("ConsultaRTVwsSoapPort".equals(inputPortName)) {
            return getConsultaRTVwsSoapPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("SMTATM", "ConsultaRTVws");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SMTATM", "ConsultaRTVwsSoapPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ConsultaRTVwsSoapPort".equals(portName)) {
            setConsultaRTVwsSoapPortEndpointAddress(address);
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
