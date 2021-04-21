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
import org.gob.loja.gim.ws.dto.queries.DebtsDTO;
import org.gob.loja.gim.ws.dto.queries.EntryDTO;
import org.gob.loja.gim.ws.dto.queries.LocalDTO;
import org.gob.loja.gim.ws.dto.queries.OperatingPermitDTO;
import org.gob.loja.gim.ws.dto.queries.request.BondByIdRequest;
import org.gob.loja.gim.ws.dto.queries.request.DebtsRequest;
import org.gob.loja.gim.ws.dto.queries.request.EntryRequest;
import org.gob.loja.gim.ws.dto.queries.request.LocalsRequest;
import org.gob.loja.gim.ws.dto.queries.request.OperatingPermitRequest;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;
import org.gob.loja.gim.ws.dto.queries.response.BondResponse;
import org.gob.loja.gim.ws.dto.queries.response.DebtsResponse;
import org.gob.loja.gim.ws.dto.queries.response.EntryResponse;
import org.gob.loja.gim.ws.dto.queries.response.LocalsResponse;
import org.gob.loja.gim.ws.dto.queries.response.OperatingPermitResponse;
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
			if (bond == null) {
				resp.setMessage("No existe obligacion con el identificador " + request.getBondId());
			} else {

				resp.setMessage("Consulta exitosa");
			}

			return Response.ok(resp).header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError()
					.header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC").build();
		}
	}

	@POST
	@Path("/operatingPermit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response operatingPermit(@Valid OperatingPermitRequest request) {
		try {
			System.out.println(request);

			OperatingPermitResponse resp = new OperatingPermitResponse();

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
			List<OperatingPermitDTO> permits = this.queriesService
					.findOperatingPermits(request.getIdentification());
			resp.setPermits(permits);

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

	@POST
	@Path("/locals")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response locals(@Valid LocalsRequest request) {
		try {
			System.out.println(request);

			LocalsResponse resp = new LocalsResponse();

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
			List<LocalDTO> locals = this.queriesService.findLocals(request
					.getIdentification());
			resp.setLocals(locals);

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

	@POST
	@Path("/entry")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response entry(@Valid EntryRequest request) {
		try {
			System.out.println(request);

			EntryResponse resp = new EntryResponse();

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
			EntryDTO entry = this.queriesService.findEntry(request.getCode());
			resp.setEntry(entry);
			if (entry == null) {
				resp.setMessage("No existe Entry con el codigo "
						+ request.getCode());
			} else {
				resp.setMessage("Consulta exitosa");
			}

			return Response.ok(resp).header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError()
					.header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC").build();
		}
	}

	@POST
	@Path("/debts")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response debts(@Valid DebtsRequest request) {
		try {
			System.out.println(request);

			DebtsResponse resp = new DebtsResponse();

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
			DebtsDTO result = this.queriesService.findDebts(request
					.getIdentification());
			resp.setTaxpayer(result.getTaxpayer());
			resp.setBonds(result.getBonds());

			if (resp.getTaxpayer() == null) {
				resp.setMessage("No existe Contribuyente con la identificacion "
						+ request.getIdentification());
			} else {
				resp.setMessage("Consulta exitosa");
			}

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
