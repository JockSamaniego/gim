/**
 * 
 */
package org.gob.loja.gim.wsrest.Hygiene;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gob.gim.common.ServiceLocator;
import org.gob.loja.gim.ws.dto.HygieneDTOWs;
import org.gob.loja.gim.ws.service.RestService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;


/**
 * @author Jock
 *
 */
@Name("hygieneWS")
@Path("/hygiene")
@Transactional
//@Interceptors(CorsInterceptor.class)
public class HygieneWS {
	
	@In(create = true,required = false, value = "restService")
	private RestService restService;
	
	@GET
	@Path("/{identification}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHygieneDataByIdentification(@PathParam("identification") String identification) {
		try {
			if (restService == null) {
				restService = ServiceLocator.getInstance().findResource(
						restService.LOCAL_NAME);
			}
			 List<HygieneDTOWs> res = this.restService.findHygieneDataByIdentification(identification);
			//Resident res = this.residentService.find(identification);
			//System.out.println(res);
			return Response.ok(res).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok().build();
		}
		
	}

}