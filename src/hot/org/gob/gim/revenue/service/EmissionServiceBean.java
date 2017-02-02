/**
 * 
 */
package org.gob.gim.revenue.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;

import ec.gob.gim.revenue.model.DTO.ReportEmissionDTO;
import ec.gob.gim.revenue.model.criteria.ReportEmissionCriteria;

/**
 * @author Rene Ortega
 * @Fecha 2017-02-02
 */
@Stateless(name = "EmissionService")
public class EmissionServiceBean implements EmissionService {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<ReportEmissionDTO> findEmissionReport(
			ReportEmissionCriteria criteria) {
		// TODO Auto-generated method stub
		try {
			Query query = entityManager
					.createNativeQuery("select * from reports.sp_reporte_emision(?1, ?2, ?3, ?4)");
			query.setParameter(1, criteria.getStartDate());
			query.setParameter(2, criteria.getEndDate());
			query.setParameter(3, criteria.getStatus_ids());
			query.setParameter(4, criteria.getAccount_id());

			List<ReportEmissionDTO> retorno = NativeQueryResultsMapper.map(
					query.getResultList(), ReportEmissionDTO.class);

			return retorno;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

}
