package SMTATM;

public class WsPagoSolicitudSoapPortProxy implements SMTATM.WsPagoSolicitudSoapPort {
  private String _endpoint = null;
  private SMTATM.WsPagoSolicitudSoapPort wsPagoSolicitudSoapPort = null;
  
  public WsPagoSolicitudSoapPortProxy() {
    _initWsPagoSolicitudSoapPortProxy();
  }
  
  public WsPagoSolicitudSoapPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initWsPagoSolicitudSoapPortProxy();
  }
  
  private void _initWsPagoSolicitudSoapPortProxy() {
    try {
      wsPagoSolicitudSoapPort = (new SMTATM.WsPagoSolicitudLocator()).getwsPagoSolicitudSoapPort();
      if (wsPagoSolicitudSoapPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wsPagoSolicitudSoapPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wsPagoSolicitudSoapPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wsPagoSolicitudSoapPort != null)
      ((javax.xml.rpc.Stub)wsPagoSolicitudSoapPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SMTATM.WsPagoSolicitudSoapPort getWsPagoSolicitudSoapPort() {
    if (wsPagoSolicitudSoapPort == null)
      _initWsPagoSolicitudSoapPortProxy();
    return wsPagoSolicitudSoapPort;
  }
  
  public SMTATM.WsPagoSolicitudExecuteResponse execute(SMTATM.WsPagoSolicitudExecute parameters) throws java.rmi.RemoteException{
    if (wsPagoSolicitudSoapPort == null)
      _initWsPagoSolicitudSoapPortProxy();
    return wsPagoSolicitudSoapPort.execute(parameters);
  }
  
  
}