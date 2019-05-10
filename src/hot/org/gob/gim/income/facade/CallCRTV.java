package org.gob.gim.income.facade;

import java.rmi.RemoteException;

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
}
