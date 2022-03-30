/**
 * 
 */
package org.gob.loja.gim.wsrest.digitalReceipts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.gob.loja.gim.ws.dto.digitalReceipts.BondShortDTO;
import org.gob.loja.gim.ws.dto.digitalReceipts.request.ExternalPaidsRequest;
import org.gob.loja.gim.ws.dto.digitalReceipts.request.PDFBondRequest;
import org.gob.loja.gim.ws.dto.digitalReceipts.response.BondResponse;
import org.gob.loja.gim.ws.dto.digitalReceipts.response.ExternalPaidsResponse;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

/**
 * @author René
 *
 */

@Name("DigitalReceiptsWS")
@Path("/digitalReceipts")
@Transactional
public class DigitalReceiptsWS {

	@In(create = true, required = false, value = "queriesService")
	private QueriesService queriesService;

	@POST
	@Path("/externalPaids")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getExternalPaids(@Valid ExternalPaidsRequest request) {
		try {
			// System.out.println(request);

			ExternalPaidsResponse resp = new ExternalPaidsResponse();

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

			List<BondShortDTO> bonds = queriesService
					.getExternalPayments(request);
			resp.setBonds(bonds);

			/*
			 * List<OperatingPermitDTO> permits = this.queriesService
			 * .findOperatingPermits(request.getIdentification());
			 * resp.setPermits(permits);
			 */

			resp.setMessage("OK");

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
	@Path("/getBondData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getBondData(@Valid PDFBondRequest request) {
		try {
			// System.out.println(request);

			BondResponse resp = new BondResponse();
			
            System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!");
			
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
			
			BondResponse respAux  = queriesService.getBondDto(request.getBondId());
			resp.setBond(respAux.getBond());
			resp.setDeposits(respAux.getDeposits());
			
			resp.setInstitution(respAux.getInstitution());

			resp.setMessage("OK");

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
