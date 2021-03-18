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
import org.gob.loja.gim.ws.dto.preemission.PreemisionAdministerServicesResponse;
import org.gob.loja.gim.ws.dto.preemission.PreemitAdministrativeServicesRequest;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

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

	@POST
	@Path("/administrativeServices")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response administrativeServices(
			@Valid PreemitAdministrativeServicesRequest request) {
		try {
			System.out.println(request);
			
			PreemisionAdministerServicesResponse resp = new PreemisionAdministerServicesResponse();

			List<String> errorsValidation = GimUtils.validateRequest(request);
			if(errorsValidation.size()>0){
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
			
			if(emitter == null){
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
			if(user == null){
				resp.setMessage("No existe usuario con la identificación proporcionada");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}
			
			if(!user.getIsActive()){
				resp.setMessage("Usuario inactivo");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}
			
			if(user.getIsBlocked()){
				resp.setMessage("Usuario bloqueado");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}
			
			Boolean hasRol = userService.checkUserRole(user.getId(), "WS_PREEMISOR");
			if(!hasRol) {
				resp.setMessage("El usuario no cuenta con el rol necesario para preemitir");
				return Response.ok(resp)
						.header("Access-Control-Allow-Origin", "*")
						.header("Content-Language", "es-EC").build();
			}
			
			
			// WS_PREEMISOR

			// InfringementEmisionResponse res =
			// emissionService.generateANTEmissionInfringement(userSession.getUser().getName(),
			// params);
			return Response.ok(user.getName())
					.header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC").build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
