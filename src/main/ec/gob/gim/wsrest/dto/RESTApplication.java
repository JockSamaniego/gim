/**
 * 
 */
package ec.gob.gim.wsrest.dto;

import java.util.HashSet;
import java.util.Set;

import org.gob.loja.gim.wsrest.PurchaseOrderResource;
import org.gob.loja.gim.wsrest.cadaster.CadasterWS;

/**
 * @author Rene
 *
 */
public class RESTApplication extends javax.ws.rs.core.Application{
	
	private Set<Object> singletons = new HashSet<Object>();
	
	public RESTApplication () {
	    singletons.add(new PurchaseOrderResource());
	    singletons.add(new CadasterWS());
	    
	}
	
	@Override
	public Set<Object> getSingletons() {
	    return singletons;
	}

}
