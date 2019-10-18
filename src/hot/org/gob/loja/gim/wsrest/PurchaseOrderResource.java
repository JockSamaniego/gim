/**
 * 
 */
package org.gob.loja.gim.wsrest;

import java.io.Serializable;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.log.Log;

/**
 * @author Rene
 *
 */
@Name("purchaseOrderResource")
@Path("/purchaseOrder")
@Transactional
public class PurchaseOrderResource implements Serializable{
	private static final long serialVersionUID = 8655977096519941447L;

	@Logger
	private Log log;


	@GET
	@Path("/{reference}")
	@Produces("text/plain")
	@Restrict("#{s:hasRole('ADMIN')}")
	public String closePurchaseOrder(@PathParam("reference") String reference) {
		log.debug("request received [reference:'" + reference + "']");
		return "ok";
	}
}
