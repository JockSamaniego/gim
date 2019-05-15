package SMTATM;

public class ConsultaRTVwsSoapPortProxy implements SMTATM.ConsultaRTVwsSoapPort {
  private String _endpoint = null;
  private SMTATM.ConsultaRTVwsSoapPort consultaRTVwsSoapPort = null;
  
  public ConsultaRTVwsSoapPortProxy() {
    _initConsultaRTVwsSoapPortProxy();
  }
  
  public ConsultaRTVwsSoapPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initConsultaRTVwsSoapPortProxy();
  }
  
  private void _initConsultaRTVwsSoapPortProxy() {
    try {
      consultaRTVwsSoapPort = (new SMTATM.ConsultaRTVwsLocator()).getConsultaRTVwsSoapPort();
      if (consultaRTVwsSoapPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)consultaRTVwsSoapPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)consultaRTVwsSoapPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (consultaRTVwsSoapPort != null)
      ((javax.xml.rpc.Stub)consultaRTVwsSoapPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SMTATM.ConsultaRTVwsSoapPort getConsultaRTVwsSoapPort() {
    if (consultaRTVwsSoapPort == null)
      _initConsultaRTVwsSoapPortProxy();
    return consultaRTVwsSoapPort;
  }
  
  public SMTATM.ConsultaRTVwsExecuteResponse execute(SMTATM.ConsultaRTVwsExecute parameters) throws java.rmi.RemoteException{
    if (consultaRTVwsSoapPort == null)
      _initConsultaRTVwsSoapPortProxy();
    return consultaRTVwsSoapPort.execute(parameters);
  }
  
  
}