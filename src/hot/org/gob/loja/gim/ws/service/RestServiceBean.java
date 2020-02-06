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
import org.gob.loja.gim.ws.dto.HygieneDTOWs;
import org.gob.loja.gim.ws.dto.PropertyDTOWs;

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
	
	@Override
	public List<HygieneDTOWs> findHygieneDataByIdentification(
			String identification) {
		

		String query1 = "(	select 'Dir. Domiciliaria', re.identificationnumber, re.name, ad.street, ad.phonenumber, ad.mobilenumber "
						+	"from resident re " 
						+	"left join address ad on re.currentaddress_id = ad.id "
						+	"where re.identificationnumber ilike :idNumber "
						+") union (	"
						+	"select 'Dir. Medidores', cast(ws.servicenumber as varchar), ro.name ruta, st.name calle_bd, ws.ncalle calle_digitado, cast(ws.ncasa as varchar) "
						+	"from watersupply ws "
						+	"join resident re on ws.serviceowner_id = re.id "
						+	"join route ro on ws.route_id = ro.id "
						+	"left join street st on ws.street_id = st.id "
						+	"where re.identificationnumber ilike :idNumber "
						+") union ( "
						+	"select 'Dir. Propiedades', pro.cadastralcode, st.name calle, CONCAT(nh.code,CONCAT('-',nh.name)) barrio, pro.addressreference referencia, lo.housenumber "
						+	"from property pro "
						+	"join domain dom on pro.id = dom.currentproperty_id "
						+	"join resident re on dom.resident_id = re.id "
						+	"left join location lo on pro.location_id = lo.id "
						+	"left join blocklimit bl on lo.mainstreet_id = bl.id "
						+	"left join street st on bl.street_id = st.id "
						+	"left join neighborhood nh on lo.neighborhood_id = nh.id "
						+	"where re.identificationnumber ilike :idNumber "
						+	"and pro.deleted = false "
						+") union ( "
						+	"select 'Dir. Comercial', "
						+	"CONCAT(extract(year from op.date_emission),CONCAT('-',op.paper_code)) as codigo, bu.name comercio, "
						+	"replace(replace(op.economic_activity,chr(10), ''),chr(13), '') actividad, "
						+	"CONCAT(ad.parish,CONCAT('-',ad.street)), CONCAT(ad.phonenumber,CONCAT('-',ad.mobilenumber)) telefonos "
						+	"from operatinglicense op "
						+	"inner join local lo on op.local_code = lo.code "
						+	"left join business bu on lo.business_id = bu.id "
						+	"join resident re on bu.owner_id = re.id "
						+	"left join address ad on lo.address_id = ad.id "
						+	"where "
						+	"re.identificationnumber ilike :idNumber "
						+	"order by op.paper_code )"; 

		Query q = em.createNativeQuery(query1);
		q.setParameter("idNumber", "%"+identification.trim()+"%");

		List<HygieneDTOWs> list = NativeQueryResultsMapper.map(
				q.getResultList(), HygieneDTOWs.class);

		return list;
	}

}