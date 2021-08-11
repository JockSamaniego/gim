package org.gob.loja.gim.wsrest.income;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.revenue.service.EmissionService;
import org.gob.gim.revenue.service.EntryService;
import org.gob.gim.ws.service.InfringementEmisionResponse;
import org.gob.loja.gim.ws.dto.InfringementEmisionDetail;
import org.gob.loja.gim.ws.dto.ant.PreemissionInfractionRequest;
import org.gob.loja.gim.ws.service.GimService;
import org.gob.loja.gim.ws.service.RestService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

import ec.gob.gim.revenue.model.EntryStructure;
import ec.gob.gim.revenue.model.EntryStructureType;

@Name("EmissionWS")
@Path("/emission")
@Transactional
public class EmissionWS {
	
	@In(create = true,required = false, value = "restService")
	private RestService restService;
	
	//@EJB
	@In(create = true, required = false, value = "gimService")
	private GimService gimService;
	
	@In(create = true, required = false, value = "emissionService")
	private EmissionService emissionService;
	
	@In(create = true, required = false, value = "entryService")
	private EntryService entryService;
	
	@In(create = true)
	UserSession userSession;

	
	/*@POST
	@Path("/property/{propertyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCadastralCertificateProperty(@PathParam("propertyId") Long propertyId) {
		try {
			if (restService == null) {
				restService = ServiceLocator.getInstance().findResource(
						restService.LOCAL_NAME);
			}
			
			CadastralCertificateDTOWs res = this.restService.getCadastralCertificateData(propertyId);
			
			// TRADUCCION DE ENUMS
			res.setTopografia_terreno(Interpolator.instance().interpolate(
					"#{messages['" + res.getTopografia_terreno() + "']}",
					new Object[0]));
			
			res.setAlcantarillado(Interpolator.instance().interpolate(
					"#{messages['" + res.getAlcantarillado() + "']}",
					new Object[0]));
			
			res.setAlcantarillado(Interpolator.instance().interpolate(
					"#{messages['" + res.getAlcantarillado() + "']}",
					new Object[0]));
			
			for (int i = 0; i < res.getConstruccionesJson().size(); i++) {
				BuildingDTO cons = res.getConstruccionesJson().get(i);
				res.getConstruccionesJson().get(i).setExternalfinishing(Interpolator.instance().interpolate(
						"#{messages['" + cons.getExternalfinishing()+ "']}",
						new Object[0]));
				
				res.getConstruccionesJson().get(i).setPreservationstate(Interpolator.instance().interpolate(
						"#{messages['" + cons.getPreservationstate()+ "']}",
						new Object[0]));
				
				res.getConstruccionesJson().get(i).setRoofmaterial(Interpolator.instance().interpolate(
						"#{messages['" + cons.getRoofmaterial()+ "']}",
						new Object[0]));
				
				res.getConstruccionesJson().get(i).setStructurematerial(Interpolator.instance().interpolate(
						"#{messages['" + cons.getStructurematerial()+ "']}",
						new Object[0]));
				
				
				res.getConstruccionesJson().get(i).setWallmaterial(Interpolator.instance().interpolate(
						"#{messages['" + cons.getWallmaterial()+ "']}",
						new Object[0]));
						
			}
			
			return Response.ok(res).header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC")
					.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok().build();
		}		
	}*/
	
		
	@POST
	@Path("/generateInfringement")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateANTEmissionInfringement(PreemissionInfractionRequest params) {
		try {
			System.out.println(params);
			
			if (emissionService == null) {
				emissionService = ServiceLocator.getInstance().findResource(
						emissionService.LOCAL_NAME);
			}
			
			InfringementEmisionResponse res = emissionService.generateANTEmissionInfringement(userSession.getUser().getName(), params);
			return Response.ok(res).header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@PUT
	@Path("/confirmInfringement/{propertyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public InfringementEmisionResponse confirmANTEmissionInfringement(String name, String password,
			InfringementEmisionDetail emisionDetail) {
		try {
			return gimService.confirmANTEmissionInfringement(name, password, emisionDetail);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*@POST
	@Path("/entryByCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEntryStructureByCode(EntryByCodeRequest request) {
		try {
			System.out.println(request);
			
			if (entryService == null) {
				entryService = ServiceLocator.getInstance().findResource(
						entryService.LOCAL_NAME);
			}
			
			// InfringementEmisionResponse res = emissionService.generateANTEmissionInfringement(userSession.getUser().getName(), params);
			List<EntryStructure> structure = entryService.findEntryStructureChildrenByType(55L, EntryStructureType.NORMAL);
			return Response.ok(structure).header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@POST
	@Path("/emitAdministrativeServices")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response emitAdministrativeServices(EntryByCodeRequest request) {
		try {
			System.out.println(request);
			
			if (entryService == null) {
				entryService = ServiceLocator.getInstance().findResource(
						entryService.LOCAL_NAME);
			}
			
			// InfringementEmisionResponse res = emissionService.generateANTEmissionInfringement(userSession.getUser().getName(), params);
			List<EntryStructure> structure = entryService.findEntryStructureChildrenByType(55L, EntryStructureType.NORMAL);
			return Response.ok(structure).header("Access-Control-Allow-Origin", "*")
					.header("Content-Language", "es-EC")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	
}
