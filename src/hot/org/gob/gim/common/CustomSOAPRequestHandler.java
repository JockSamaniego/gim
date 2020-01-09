package org.gob.gim.common;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.jboss.seam.log.LogProvider;
import org.jboss.seam.log.Logging;
import org.jboss.seam.webservice.SOAPRequestHandler;

public class CustomSOAPRequestHandler extends SOAPRequestHandler{
	
	public static final QName CIDQN = new QName("http://www.jboss.org/seam/webservice", "conversationId", "seam");  
	  
    private static final LogProvider log = Logging.getLogProvider(SOAPRequestHandler.class);
    
    @Override
    public boolean handleMessage(MessageContext msgContext) {
    	// TODO Auto-generated method stub
    	System.out.println("llega al handleMessage");
    	return super.handleMessage(msgContext);
    }
    
    @Override
    public boolean handleInbound(MessageContext arg0) {
    	// TODO Auto-generated method stub
    	System.out.println("llega al handleInbound");
    	return super.handleInbound(arg0);
    }
    
    

}
