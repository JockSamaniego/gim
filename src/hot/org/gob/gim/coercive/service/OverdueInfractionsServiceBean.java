/**
 * 
 */
package org.gob.gim.coercive.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.coercive.dto.OverdueInfractionDTO;
import org.gob.gim.coercive.dto.OverdueInfractionsSearchCriteria;
import org.gob.gim.common.NativeQueryResultsMapper;

/**
 * @author René
 *
 */
@Stateless(name = "OverdueInfractionsService")
public class OverdueInfractionsServiceBean implements OverdueInfractionsService {
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<OverdueInfractionDTO> findInfractions(
			OverdueInfractionsSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows) {
		String qry = "SELECT dat.id, " +
		        "dat.identificacion, " +
		        "dat.nombre, " +
		        "dat.placa, " +
		        "dat.articulo, " +
		        "dat.emision, " +
		        "dat.vencimiento, " +
		        "dat.boleta, " +
		        "dat.articulo_descripcion, " +
		        "dat.valor, " +
		        "dat.interes, " +
		        "dat.valor_total " +
		"FROM infracciones.data_excel dat " +
		"WHERE 1 = 1 ";
		
		Query query = entityManager
				.createNativeQuery(qry);

		List<OverdueInfractionDTO> ret = NativeQueryResultsMapper.map(
				query.getResultList(), OverdueInfractionDTO.class);

		return ret;
	}

	@Override
	public OverdueInfractionDTO findDtoById(Long id) {
		String qry = "SELECT dat.id, " +
		        "dat.identificacion, " +
		        "dat.nombre, " +
		        "dat.placa, " +
		        "dat.articulo, " +
		        "dat.emision, " +
		        "dat.vencimiento, " +
		        "dat.boleta, " +
		        "dat.articulo_descripcion, " +
		        "dat.valor, " +
		        "dat.interes, " +
		        "dat.valor_total " +
		"FROM infracciones.data_excel dat " +
		"WHERE 1 = 1 " +
		"AND dat.id =:infractionId";
		Query query = entityManager.createNativeQuery(qry);
		query.setParameter("infractionId", id);
		List<OverdueInfractionDTO> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	@Override
	public Integer findOverdueInfractionsNumber(
			OverdueInfractionsSearchCriteria criteria) {
		String qry = "SELECT count(*) " +
		"FROM infracciones.data_excel dat ";
		Query query = entityManager.createNativeQuery(qry);
		Integer size = ((BigInteger)query.getSingleResult()).intValue(); 
		return size;
	}

}
