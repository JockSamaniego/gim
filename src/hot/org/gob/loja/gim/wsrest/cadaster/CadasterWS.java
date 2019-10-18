/**
 * 
 */
package org.gob.loja.gim.wsrest.cadaster;

import java.util.List;

import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gob.gim.common.ServiceLocator;
import org.gob.loja.gim.ws.dto.CadastralCertificateDTOWs;
import org.gob.loja.gim.ws.dto.PropertyDTOWs;
import org.gob.loja.gim.ws.service.RestService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Interpolator;

import ec.gob.gim.cadaster.model.dto.BuildingDTO;
import ec.gob.gim.wsrest.CorsInterceptor;

/**
 * @author Rene
 *
 */
@Name("cadasterWS")
@Path("/cadaster")
@Transactional
@Interceptors(CorsInterceptor.class)
public class CadasterWS {
	
	@In(create = true,required = false, value = "restService")
	private RestService restService;
	
	@GET
	@Path("/{identification}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPropertiesByIdentification(@PathParam("identification") String identification) {
		try {
			if (restService == null) {
				restService = ServiceLocator.getInstance().findResource(
						restService.LOCAL_NAME);
			}
			 List<PropertyDTOWs> res = this.restService.findPropertiesByIdentification(identification);
			//Resident res = this.residentService.find(identification);
			//System.out.println(res);
			return Response.ok(res).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok().build();
		}
		
	}
	
	@GET
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
		
	}

}