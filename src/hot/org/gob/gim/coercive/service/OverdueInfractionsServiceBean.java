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

import org.gob.gim.coercive.dto.InfractionItemDTO;
import org.gob.gim.coercive.dto.criteria.OverdueInfractionsSearchCriteria;
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
	public List<InfractionItemDTO> searchInfractionGroupByCriteria(
			OverdueInfractionsSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows) {
		String qry = "SELECT dat.identificacion, " + "dat.nombre,  "
				+ "count(*),  " + "sum(dat.valor), " + "sum(dat.interes),  "
				+ "sum(dat.valor_total) " + "FROM infracciones.data_excel dat "
				+ "INNER JOIN itemcatalog itm ON itm.id = dat.estado_id  "
				+ "WHERE 1=1 " + "AND itm.id = 56 "
				+ "AND dat.emision BETWEEN ?1 AND ?2 "
				+ "GROUP BY dat.identificacion, dat.nombre "
				+ "ORDER BY dat.nombre ASC";

		Query query = entityManager.createNativeQuery(qry);
		query.setParameter(1, criteria.getEmisionFrom());
		query.setParameter(2, criteria.getEmisionUntil());

		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<InfractionItemDTO> ret = NativeQueryResultsMapper.map(
				query.getResultList(), InfractionItemDTO.class);

		return ret;
	}

	@Override
	public InfractionItemDTO findOverdueInfractionById(String identification,
			OverdueInfractionsSearchCriteria criteria) {
		String qry = "SELECT dat.identificacion, " + "dat.nombre,  "
				+ "count(*),  " + "sum(dat.valor), " + "sum(dat.interes),  "
				+ "sum(dat.valor_total) " + "FROM infracciones.data_excel dat "
				+ "INNER JOIN itemcatalog itm ON itm.id = dat.estado_id  "
				+ "WHERE 1=1 " + "AND itm.id = 56 "
				+ "AND dat.emision BETWEEN ?1 AND ?2 "
				+ "AND dat.identifcacion = ?3 "
				+ "GROUP BY dat.identificacion, dat.nombre "
				+ "ORDER BY dat.nombre ASC";;

		Query query = entityManager.createNativeQuery(qry);
		query.setParameter(1, criteria.getEmisionFrom());
		query.setParameter(2, criteria.getEmisionUntil());
		query.setParameter(3, identification);

		List<InfractionItemDTO> ret = NativeQueryResultsMapper.map(
				query.getResultList(), InfractionItemDTO.class);

		if (ret.size() > 0) {
			return ret.get(0);
		}

		return null;

	}

	@Override
	public Integer findOverdueInfractionsNumber(
			OverdueInfractionsSearchCriteria criteria) {

		String qry = "SELECT COUNT(*) FROM (SELECT dat.identificacion, " + "dat.nombre,  "
				+ "count(*),  " + "sum(dat.valor), " + "sum(dat.interes),  "
				+ "sum(dat.valor_total) " + "FROM infracciones.data_excel dat "
				+ "INNER JOIN itemcatalog itm ON itm.id = dat.estado_id  "
				+ "WHERE 1=1 " + "AND itm.id = 56 "
				+ "AND dat.emision BETWEEN ?1 AND ?2 "
				+ "GROUP BY dat.identificacion, dat.nombre "
				+ "ORDER BY dat.nombre ASC"
				+ ") qry";

		Query query = entityManager.createNativeQuery(qry);
		query.setParameter(1, criteria.getEmisionFrom());
		query.setParameter(2, criteria.getEmisionUntil());

		BigInteger size = (BigInteger) query.getSingleResult();

		return size.intValue();
	}

	@Override
	public Integer getTotalSyncInfractions() {
		Query query = entityManager.createQuery(
				"Select count(*) from Datainfraction di ");

		Long size = (Long) query.getSingleResult();

		return size.intValue();
	}
	
	

}
