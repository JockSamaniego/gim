/**
 * 
 */
package org.gob.loja.gim.ws.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.gob.gim.cadaster.facade.CadasterService;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.exception.NonUniqueIdentificationNumberException;
import org.gob.gim.common.service.ResidentService;
import org.gob.loja.gim.ws.dto.CadastralCertificateDTOWs;
import org.gob.loja.gim.ws.dto.PropertyDTOWs;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.SeamResourceBundle;

import ec.gob.gim.cadaster.model.dto.BoundaryDTO;
import ec.gob.gim.cadaster.model.dto.BuildingDTO;
import ec.gob.gim.common.model.Resident;

/**
 * @author Rene
 *
 */
@Stateless(name = "RestService")
public class RestServiceBean implements RestService {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private ResidentService residentService;

	@EJB
	private CadasterService cadasterService;

	@Override
	public Resident findUserByIdentification(String identification) {
		// TODO Auto-generated method stub
		try {
			return residentService.find(identification);
		} catch (NonUniqueIdentificationNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PropertyDTOWs> findPropertiesByIdentification(
			String identification) {

		String query1 = "SELECT pro.id, " + "res.name, "
				+ "pro.cadastralcode, " + "pro.previouscadastralcode, "
				+ "nei.name as neighborhood, " + "pro.addressreference "
				+ "FROM property pro "
				+ "INNER JOIN domain dom ON dom.currentproperty_id = pro.id "
				+ "INNER JOIN resident res ON res.id = dom.resident_id "
				+ "LEFT JOIN location loc ON loc.id = pro.location_id "
				+ "LEFT JOIN neighborhood nei ON nei.id = loc.neighborhood_id "
				+ "WHERE pro.propertytype_id = 1 " + "AND pro.deleted = FALSE "
				+ "AND res.identificationnumber = ?0";

		Query q = em.createNativeQuery(query1);
		q.setParameter(0, identification.trim());

		List<PropertyDTOWs> list = NativeQueryResultsMapper.map(
				q.getResultList(), PropertyDTOWs.class);

		return list;
	}

	@Override
	public CadastralCertificateDTOWs getCadastralCertificateData(
			Long property_id) {
		CadastralCertificateDTOWs resp = cadasterService
				.getCadastralCertificateDataWs(property_id);

		try {

			if (resp.getLindero_norte() != null) {
				BoundaryDTO _boundary = new ObjectMapper().readValue(
						resp.getLindero_norte(), BoundaryDTO.class);
				resp.setLinderoNorteJson(_boundary);
			}

			if (resp.getLindero_sur() != null) {
				BoundaryDTO _boundary = new ObjectMapper().readValue(
						resp.getLindero_sur(), BoundaryDTO.class);
				resp.setLinderoSurJson(_boundary);
			}

			if (resp.getLindero_este() != null) {
				BoundaryDTO _boundary = new ObjectMapper().readValue(
						resp.getLindero_este(), BoundaryDTO.class);
				resp.setLinderoEsteJson(_boundary);
			}

			if (resp.getLindero_oeste() != null) {
				BoundaryDTO _boundary = new ObjectMapper().readValue(
						resp.getLindero_oeste(), BoundaryDTO.class);
				resp.setLinderoOesteJson(_boundary);
			}

			if (resp.getConstrucciones() != null) {

				List<BuildingDTO> _buildings = Arrays.asList(new ObjectMapper()
						.readValue(resp.getConstrucciones(),
								BuildingDTO[].class));

				resp.setConstruccionesJson(_buildings);

			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Setear valores que vienen como json
		return resp;
	}

}