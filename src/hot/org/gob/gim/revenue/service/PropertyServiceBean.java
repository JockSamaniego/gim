package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.service.CrudService;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.revenue.model.DTO.PropertyDTO;
import ec.gob.gim.revenue.model.impugnment.dto.ImpugnmentDTO;

/**
 * * @author René Ortega * @Fecha 2018-02-20
 */

@Stateless(name = "PropertyService")
public class PropertyServiceBean implements PropertyService {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<PropertyDTO> findByResidentIds(List<Long> idsResidents, List<Long> idsProperties) {
		
		Query query = entityManager
				.createNativeQuery("SELECT 	pro.id AS property_id, "+
										"pro.cadastralcode, "+
										"pro.previouscadastralcode, "+
										"res.name AS resident_name "+
									"FROM property pro "+
									"INNER JOIN domain dom ON dom.currentproperty_id = pro.id "+
									"INNER JOIN resident res ON res.id = dom.resident_id "+
									"WHERE res.id IN (?1) "+
									"AND pro.deleted = FALSE "+
									"AND pro.id NOT IN (?2) "+
									"ORDER BY res.name, pro.cadastralcode");
		query.setParameter(1, idsResidents);
		query.setParameter(2, idsProperties);
		System.out.println(query.toString());
		List<PropertyDTO> retorno = NativeQueryResultsMapper.map(query.getResultList(), PropertyDTO.class);
		return retorno;
		
	}

	@Override
	public Property findPropertyById(Long id) {
		return this.entityManager.find(Property.class, id);
	}
}
