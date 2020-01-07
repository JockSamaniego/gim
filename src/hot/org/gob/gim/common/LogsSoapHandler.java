package org.gob.gim.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.gob.gim.common.service.LogService;

import ec.gob.gim.common.model.Logs;

public class LogsSoapHandler implements SOAPHandler<SOAPMessageContext> {

	@EJB
	LogService logService;

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("***handleMessage***");

		logSoapMessage(context);

		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("***handleFault***");
		logSoapMessage(context);
		return true;
	}

	@Override
	public void close(MessageContext context) {
		System.out.println("_______________close_____________ ");
	}

	@Override
	public Set<QName> getHeaders() {
		return new HashSet<QName>();
	}

	private void logSoapMessage(SOAPMessageContext context) {
		Boolean isOutBound = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		SOAPMessage soapMsg = context.getMessage();

		try {
			if (isOutBound) {
				// System.out.println("Intercepting outbound message:");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				soapMsg.writeTo(baos);

				Map<String, String> payload = new HashMap<String, String>();
				payload.put("tipo", "RESPONSE");
				payload.put("body", baos.toString());

				String json = "";
				try {
					json = new ObjectMapper().writeValueAsString(payload);
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

				Logs _log = new Logs();
				// TODO setear valores en objeto log
				_log.setLogger(this.getClass().getSimpleName());
				_log.setMessage(json);
				this.logService.save(_log);
			} else {

				// System.out.println("Intercepting inbound message:");
				HttpServletRequest request = (HttpServletRequest) context
						.get(SOAPMessageContext.SERVLET_REQUEST);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				soapMsg.writeTo(baos);
				// System.out.println(baos);

				Map<String, String> payload = new HashMap<String, String>();
				payload.put("tipo", "REQUEST");
				payload.put("remoteAddr", request.getRemoteAddr());
				payload.put("remoteHost", request.getRemoteHost());
				payload.put("remoteUser", request.getRemoteUser());
				payload.put("method", request.getMethod());
				payload.put("requestURI", request.getRequestURI());
				payload.put("requestURL", request.getRequestURL().toString());
				payload.put("body", baos.toString());

				String json = "";
				try {
					json = new ObjectMapper().writeValueAsString(payload);
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

				Logs _log = new Logs();
				_log.setLogger(this.getClass().getSimpleName());
				_log.setMessage(json);
				this.logService.save(_log);

			}

		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
