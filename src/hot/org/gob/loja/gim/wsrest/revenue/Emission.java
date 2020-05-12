package org.gob.loja.gim.wsrest.revenue;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.gob.gim.ws.service.InfringementEmisionResponse;
import org.gob.loja.gim.ws.dto.InfringementEmisionDetail;
import org.gob.loja.gim.ws.service.GimService;
import org.gob.loja.gim.ws.service.RestService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

@Name("EmissionWS")
@Path("/emission")
@Transactional
public class Emission {
	
	@In(create = true,required = false, value = "restService")
	private RestService restService;
	
	//@EJB
	@In(create = true, required = false, value = "gimService")
	private GimService gimService;

	
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
	@Path("/generateInfringement/{propertyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public InfringementEmisionResponse generateANTEmissionInfringement(String name, String password,
			String identificationNumber, String accountCode, InfringementEmisionDetail emisionDetail) {
		try {
			return gimService.generateANTEmissionInfringement(name, password, identificationNumber, accountCode, emisionDetail);
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
	
	
	
	
}
