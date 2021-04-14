/**
 * 
 */
package org.gob.loja.gim.wsrest.queries;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gob.gim.common.GimUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.ws.service.QueriesService;
import org.gob.loja.gim.ws.dto.queries.request.BondByIdRequest;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;
import org.gob.loja.gim.ws.dto.queries.response.BondResponse;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

/**
 * @author René
 *
 */
@Name("QueriesWS")
@Path("/queries")
@Transactional
public class QueriesWS {

	@In(create = true, required = false, value = "queriesService")
	private QueriesService queriesService;

	@POST
	@Path("/bondById")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response bondById(@Valid BondByIdRequest request) {
		try {
			System.out.println(request);

			BondResponse resp = new BondResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (queriesService == null) {
				queriesService = ServiceLocator.getInstance().findResource(
						queriesService.LOCAL_NAME);
			}
			BondDTO bond = this.queriesService
					.findBondById(request.getBondId());
			resp.setBond(bond);

			resp.setMessage("Consulta exitosa");

			return Response.ok(resp).header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError()
					.header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC").build();
		}
	}
}
