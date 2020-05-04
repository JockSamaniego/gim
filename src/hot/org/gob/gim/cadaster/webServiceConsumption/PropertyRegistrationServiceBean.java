package org.gob.gim.cadaster.webServiceConsumption;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.Stateless;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;

import sun.misc.BASE64Encoder;

@Stateless(name = "PropertyRegistrationService")
public class PropertyRegistrationServiceBean implements
		PropertyRegistrationService {

	private String getURL() {
		SystemParameterService systemParameterService = ServiceLocator
				.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		systemParameterService.initialize();
		return systemParameterService
				.findParameter("URL_PROPERTY_REGISTRATION");
	}
	
	private String getCredentials() {
		SystemParameterService systemParameterService = ServiceLocator
				.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		systemParameterService.initialize();
		return systemParameterService
				.findParameter("CREDENTIALS_PROPERTY_REGISTRATION");
	}

	@Override
	public PropertyWs findByRegistrationForm(String code) {
		// String url =
		// "http://186.47.44.172:8790/api/rpl/fichaRegistral?numFicha=1305";
		String url = getURL() + code.trim();
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		//String authString = "mmunicipio" + ":" + "bOKGfLKKwn";
		String authString = getCredentials();
		String authStringEnc = new BASE64Encoder()
				.encode(authString.getBytes());
		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(uri);

		Invocation.Builder invocationBuilder = webTarget.request(
				MediaType.APPLICATION_JSON).header("Authorization",
				"Basic " + authStringEnc);

		Response response = invocationBuilder.get();

		/*System.out.println("==============");
		System.out.println("allow " + response.getAllowedMethods());
		System.out.println("entity " + response.getEntity());
		System.out.println("status " + response.getStatus());
		System.out.println("class " + response.getClass());
		System.out.println("galle " + response.getCookies());
		System.out.println("date " + response.getDate());
		System.out.println("tag " + response.getEntityTag());
		System.out.println("headers " + response.getHeaders());
		System.out.println("media " + response.getMediaType());
		
		System.out.println("read Str " + output);
		System.out.println("============== end ");*/
		String output = response.readEntity(String.class);

		ObjectMapper mapper = new ObjectMapper();
		try {
			// JsonNode actualObj = mapper.readTree(output);
			PropertyWs pro = mapper.readValue(output, PropertyWs.class);
			// System.out.println(actualObj);
			/*
			 * System.out.println(pro); for (FichaPropietario fp :
			 * pro.getFichaPropietarios()) { System.out.println("------------");
			 * System.out.println(fp.toString()); }
			 */

			return pro;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
