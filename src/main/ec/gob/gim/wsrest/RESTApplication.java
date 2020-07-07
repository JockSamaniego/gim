/**
 * 
 */
package ec.gob.gim.wsrest;

import java.util.HashSet;
import java.util.Set;

import org.gob.loja.gim.wsrest.Hygiene.HygieneWS;
import org.gob.loja.gim.wsrest.cadaster.CadasterWS;



/**
 * @author Rene
 *
 */
public class RESTApplication extends javax.ws.rs.core.Application{
	
	private Set<Object> singletons = new HashSet<Object>();
	
	public RESTApplication () {
	    singletons.add(new CadasterWS());
	    singletons.add(new HygieneWS());
	    //singletons.add(new EmissionWS());
	}
	
	@Override
	public Set<Object> getSingletons() {
	    return singletons;
	}

}
