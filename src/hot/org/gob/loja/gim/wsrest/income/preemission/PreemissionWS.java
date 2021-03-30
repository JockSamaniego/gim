package org.gob.loja.gim.wsrest.income.preemission;

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
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.UserService;
import org.gob.gim.revenue.service.EmissionService;
import org.gob.gim.revenue.service.PropertyService;
import org.gob.loja.gim.ws.dto.preemission.request.AccountWithoutAdjunctRequest;
import org.gob.loja.gim.ws.dto.preemission.request.AlcabalaRequest;
import org.gob.loja.gim.ws.dto.preemission.request.ApprovalPlansRequest;
import org.gob.loja.gim.ws.dto.preemission.request.BuildingPermitRequest;
import org.gob.loja.gim.ws.dto.preemission.request.RuralPropertyRequest;
import org.gob.loja.gim.ws.dto.preemission.request.UnbuiltLotRequest;
import org.gob.loja.gim.ws.dto.preemission.request.UrbanPropertyRequest;
import org.gob.loja.gim.ws.dto.preemission.request.UtilityRequest;
import org.gob.loja.gim.ws.dto.preemission.response.AccountWithoutAdjunctResponse;
import org.gob.loja.gim.ws.dto.preemission.response.AlcabalaResponse;
import org.gob.loja.gim.ws.dto.preemission.response.ApprovalPlansResponse;
import org.gob.loja.gim.ws.dto.preemission.response.BuildingPermitResponse;
import org.gob.loja.gim.ws.dto.preemission.response.PreemissionServiceResponse;
import org.gob.loja.gim.ws.dto.preemission.response.RuralPropertyResponse;
import org.gob.loja.gim.ws.dto.preemission.response.UnbuiltLotResponse;
import org.gob.loja.gim.ws.dto.preemission.response.UrbanPropertyResponse;
import org.gob.loja.gim.ws.dto.preemission.response.UtilityResponse;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.security.model.User;

@Name("PreemissionWS")
@Path("/preemission")
@Transactional
public class PreemissionWS {

	@In(create = true, required = false, value = "residentService")
	private ResidentService residentService;

	@In(create = true, required = false, value = "userService")
	private UserService userService;

	@In(create = true, required = false, value = "emissionService")
	private EmissionService emissionService;

	@In(create = true, required = false, value = "propertyService")
	private PropertyService propertyService;

	@POST
	@Path("/accountWithoutAdjunct")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response accountWithoutAdjunct(
			@Valid AccountWithoutAdjunctRequest request) {
		try {
			System.out.println(request);

			AccountWithoutAdjunctResponse resp = new AccountWithoutAdjunctResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderWithoutAdjunctWS(request, user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
	@Path("/buildingPermit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response buildingPermit(@Valid BuildingPermitRequest request) {
		try {
			System.out.println(request);

			BuildingPermitResponse resp = new BuildingPermitResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (propertyService == null) {
				propertyService = ServiceLocator.getInstance().findResource(
						propertyService.LOCAL_NAME);
			}

			Property property = propertyService
					.findPropertyByCadastralCode(request.getCadastralCode());

			if (property == null) {
				resp.setMessage("No existe propiedad con la clave catastral proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderBuildingPermitWS(request, property,
							user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
	@Path("/approvalPlans")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response approvalPlans(@Valid ApprovalPlansRequest request) {
		try {
			System.out.println(request);

			ApprovalPlansResponse resp = new ApprovalPlansResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (propertyService == null) {
				propertyService = ServiceLocator.getInstance().findResource(
						propertyService.LOCAL_NAME);
			}

			Property property = propertyService
					.findPropertyByCadastralCode(request.getCadastralCode());

			if (property == null) {
				resp.setMessage("No existe propiedad con la clave catastral proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderApprovalPlansWS(request, property,
							user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
	@Path("/urbanProperty")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response urbanProperty(@Valid UrbanPropertyRequest request) {
		try {
			System.out.println(request);

			UrbanPropertyResponse resp = new UrbanPropertyResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (propertyService == null) {
				propertyService = ServiceLocator.getInstance().findResource(
						propertyService.LOCAL_NAME);
			}

			Property property = propertyService
					.findPropertyByCadastralCode(request.getCadastralCode());

			if (property == null) {
				resp.setMessage("No existe propiedad con la clave catastral proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderUrbanPropertyWS(request, property,
							user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
	@Path("/ruralProperty")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ruralProperty(@Valid RuralPropertyRequest request) {
		try {
			System.out.println(request);

			RuralPropertyResponse resp = new RuralPropertyResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (propertyService == null) {
				propertyService = ServiceLocator.getInstance().findResource(
						propertyService.LOCAL_NAME);
			}

			Property property = propertyService
					.findPropertyByCadastralCode(request.getCadastralCode());

			if (property == null) {
				resp.setMessage("No existe propiedad con la clave catastral proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderRuralPropertyWS(request, property,
							user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
	@Path("/unbuiltLot")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response unbuiltLot(@Valid UnbuiltLotRequest request) {
		try {
			System.out.println(request);

			UnbuiltLotResponse resp = new UnbuiltLotResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (propertyService == null) {
				propertyService = ServiceLocator.getInstance().findResource(
						propertyService.LOCAL_NAME);
			}

			Property property = propertyService
					.findPropertyByCadastralCode(request.getCadastralCode());

			if (property == null) {
				resp.setMessage("No existe propiedad con la clave catastral proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderUnbuiltLotWS(request, property, user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
	@Path("/utility")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response utility(@Valid UtilityRequest request) {
		try {
			System.out.println(request);

			UtilityResponse resp = new UtilityResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (propertyService == null) {
				propertyService = ServiceLocator.getInstance().findResource(
						propertyService.LOCAL_NAME);
			}

			Property property = propertyService
					.findPropertyByCadastralCode(request.getCadastralCode());

			if (property == null) {
				resp.setMessage("No existe propiedad con la clave catastral proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderUtilityWS(request, property, user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
	@Path("/alcabala")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response utility(@Valid AlcabalaRequest request) {
		try {
			System.out.println(request);

			AlcabalaResponse resp = new AlcabalaResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if (errorsValidation.size() > 0) {
				resp.setMessage("Error en validaciones de request");
				resp.setErrors(errorsValidation);
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (residentService == null) {
				residentService = ServiceLocator.getInstance().findResource(
						residentService.LOCAL_NAME);
			}

			Resident emitter = residentService.find(request
					.getEmiterIdentification());

			if (emitter == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (userService == null) {
				userService = ServiceLocator.getInstance().findResource(
						userService.LOCAL_NAME);
			}

			User user = userService.getUserByResident(emitter.getId());
			if (user == null) {
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (!user.getIsActive()) {
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (user.getIsBlocked()) {
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			Boolean hasRol = userService.checkUserRole(user.getId(),
					"WS_PREEMISOR");
			if (!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (propertyService == null) {
				propertyService = ServiceLocator.getInstance().findResource(
						propertyService.LOCAL_NAME);
			}

			Property property = propertyService
					.findPropertyByCadastralCode(request.getCadastralCode());

			if (property == null) {
				resp.setMessage("No existe propiedad con la clave catastral proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}

			PreemissionServiceResponse responseService = emissionService
					.generateEmissionOrderAlcabalaWS(request, property, user);

			if (responseService.getError()) {
				resp.setMessage(responseService.getErrorMessage());
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}

			resp.setBondId(responseService.getBondId());
			resp.setEmissionOrderId(responseService.getEmissionOrderId());
			resp.setMessage("Preemision exitosa");

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
