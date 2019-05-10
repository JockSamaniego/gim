package org.gob.gim.income.facade;

import java.io.IOException;
import java.rmi.RemoteException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import SMTATM.WsPagoSolicitudExecute;
import SMTATM.WsPagoSolicitudExecuteResponse;
import SMTATM.WsPagoSolicitudSoapPortProxy;

public class CallCRTV {

	public static WsPagoSolicitudExecuteResponse callNotification(String identification, String identificationType, 
			double valor, String callDate, String orderNumber) throws RemoteException {
		//llamar al servicio web
		WsPagoSolicitudExecute wspago = new WsPagoSolicitudExecute();
		wspago.setOrdenespagotipoiden(identificationType);
		wspago.setOrdenespagoidentidad(identification);
		wspago.setOrdenespagoservicio("SOL");
		wspago.setValor_pagar(valor);
		wspago.setCodigo_transaccion("13");
		wspago.setOrdenespagobanco("LOJ");
		wspago.setOrdenespagosucursal("SUC");
		wspago.setOrdenespagocanal("WEB");
		wspago.setProvincia("LOJ");
		wspago.setFecha_hora_trx(callDate);
		wspago.setFecha_hora_conta(callDate);							
		wspago.setNro_solicitud(orderNumber);
		
		WsPagoSolicitudSoapPortProxy wsportProxy = new WsPagoSolicitudSoapPortProxy();
		return wsportProxy.execute(wspago);

	}
	
	
	public static String notificationResultTOJson(WsPagoSolicitudExecuteResponse result) {
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(result);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
