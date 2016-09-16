package org.gob.gim.revenue.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.service.CrudService;

import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.impugnment.Impugnment;
import ec.gob.gim.revenue.model.impugnment.criteria.ImpugnmentSearchCriteria;
import ec.gob.gim.revenue.model.impugnment.dto.ImpugnmentDTO;
import ec.gob.gim.waterservice.model.dto.WaterBlockDTO;

/**
 *  * @author René Ortega
 *  * @Fecha 2016-07-14
 */


@Stateless(name = "ImpugnmentService")
public class ImpugnmentServiceBean implements ImpugnmentService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@Override
	public Impugnment save(Impugnment impugnment) {
		return crudService.create(impugnment);
	}

	@Override
	public Impugnment update(Impugnment impugnment) {
		return crudService.update(impugnment);
	}

	@Override
	public List<Impugnment> findImpugnments(Integer numberProsecution,
			Integer numberInfringement, Integer firstRow, Integer numberOfRows) {
		Query query = entityManager.createQuery("select i from Impugnment i");
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<Impugnment> result = query.getResultList();

		return result;

	}

	@Override
	public Long findImpugnmentsNumber(Integer numberProsecution,
			Integer numberInfringement) {
//		Query query = entityManager.createQuery("select i from Impugnment i");
//		query.setFirstResult(firstRow);
//		query.setMaxResults(numberOfRows);
//
//		List<Impugnment> result = query.getResultList();

		return new Long(1);
	}

	@Override
	public List<Impugnment> findAll() {
		Query query = entityManager.createQuery("select i from Impugnment i");
		return query.getResultList();
	}

	@Override
	public MunicipalBond findMunicipalBondForImpugnment(
			String numberInfringement) {
		try {
			Query query = entityManager.createNativeQuery("select mb.id from municipalbond mb inner join antreference ant on mb.adjunct_id = ant.id where ant.contraventionNumber=:numberInfringement");
			query.setParameter("numberInfringement", numberInfringement);
			BigInteger municipalBondId = (BigInteger) query.getSingleResult();
			if(municipalBondId != null){
				Query query1 = entityManager.createNamedQuery("MunicipalBond.findById");
				query1.setParameter("municipalBondId", municipalBondId.longValue());
				List<MunicipalBond> results = query1.getResultList();
				if(!results.isEmpty()){
					return results.get(0);
				}
			}
			return null;
			
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	@Override
	public List<ImpugnmentDTO> findImpugnmentsForCriteria(ImpugnmentSearchCriteria criteria) {
		Query query  = entityManager.createNativeQuery("SELECT imp.id AS id, "
														+"imp.creationdate, "
														+"imp.numberinfringement, "
														+"imp.numberprosecution, "
														+"imp.numbertramit, "
														+"imp.observation, "
														+"imp.updatedate, "
														+"mb.id AS municipalbond_id, "
														+"itm.id AS status_id, "
														+"imp.userregister_id, "
														+"imp.userupdate_id, "
														+"res.identificationnumber, "
														+"res.name AS resident_name, "
														+"itm.name AS statusname, "
														+"mb.number AS municipalbond_number, "
														+"mb.value AS municipalbond_value, "
														+"itm.code AS statuscode "
													+"FROM Impugnment imp "
													+"INNER JOIN municipalBond mb ON (mb.id = imp.municipalbond_id)"
													+"INNER JOIN resident res ON (res.id = mb.resident_id) "
													+"INNER JOIN itemcatalog itm ON (itm.id = imp.status_itm_id) "
													+"WHERE (CAST(?1 AS text) = '' OR imp.numberInfringement = CAST(?1 AS text)) "
													+"AND (CAST(?2 AS text) = '' OR imp.numberProsecution = CAST(?2 AS text)) "
													+"AND (CAST(?3 AS text) = '' OR res.identificationNumber = CAST(?3 AS text)) "
													+"AND (?4 = 0 OR itm.id = ?4) "
													+"ORDER BY imp.id DESC");
		query.setParameter(1, criteria.getNumberInfringement() == null ? "" :criteria.getNumberInfringement());
		query.setParameter(2, criteria.getNumberProsecution() ==null ? "" :criteria.getNumberProsecution());
		query.setParameter(3, criteria.getIdentificationNumber() == null ? "" :criteria.getIdentificationNumber());
		query.setParameter(4, criteria.getState() == null ? BigInteger.ZERO :criteria.getState().getId());
		
		List<ImpugnmentDTO> retorno = NativeQueryResultsMapper.map(query.getResultList(), ImpugnmentDTO.class);
		
		return retorno;
	}

	@Override
	public Impugnment findById(Long impugnmentId) {
		Query query = entityManager.createNamedQuery("Impugnment.findById");
		query.setParameter("impugnmentId", impugnmentId);
		List<Impugnment> resultList = query.getResultList();
		if(resultList.isEmpty()){
			return null;
		}
		return resultList.get(0);
	}

	@Override
	public MunicipalBond findMunicipalBondByNumber(Long municipalBondNumber) {
		try {
			Query query = entityManager.createNativeQuery("select mb.id from municipalbond mb where mb.number=:municipalBondNumber and mb.entry_id in (643,644)");
			query.setParameter("municipalBondNumber", municipalBondNumber);
			BigInteger municipalBondId = (BigInteger) query.getSingleResult();
			if(municipalBondId != null){
				Query query1 = entityManager.createNamedQuery("MunicipalBond.findById");
				query1.setParameter("municipalBondId", municipalBondId.longValue());
				List<MunicipalBond> results = query1.getResultList();
				if(!results.isEmpty()){
					return results.get(0);
				}
			}
			return null;
			
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}
	
}
