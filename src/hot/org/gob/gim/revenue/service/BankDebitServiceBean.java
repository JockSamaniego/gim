package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.service.CrudService;

import ec.gob.gim.revenue.model.bankDebit.BankDebit;
import ec.gob.gim.revenue.model.bankDebit.criteria.BankDebitSearchCriteria;
import ec.gob.gim.revenue.model.bankDebit.dto.BankDebitDTO;
import ec.gob.gim.waterservice.model.WaterSupply;

/**
 * * @author René Ortega * @Fecha 2019-05-22
 */

@Stateless(name = "BankDebitService")
public class BankDebitServiceBean implements BankDebitService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@Override
	public List<BankDebitDTO> findDebitsForCriteria(
			BankDebitSearchCriteria criteria) {
		Query query = entityManager.createNativeQuery("SELECT ban.id, "
															+"res.identificationnumber, "
															+"res.name, "
															+"ica.name, "
															+"ban.accountnumber, "
															+"ban.accountholder, "
															+"was.servicenumber, "
															+"ban.active "
												+"FROM bankdebit ban " 
												+"INNER JOIN watersupply was ON was.id = ban.watersupply_id "
												+"INNER JOIN resident res ON res.id = was.serviceowner_id "
												+"INNER JOIN itemcatalog ica ON ica.id = ban.accounttype_itm_id");

		List<BankDebitDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), BankDebitDTO.class);

		return retorno;

	}

	@Override
	public WaterSupply findWaterSupplyByServiceNumber(Integer serviceNumber) {
		try {
			Query query = entityManager
					.createNamedQuery("WaterSupply.findByServiceNumber");
			query.setParameter("serviceNumber", serviceNumber);
			List<WaterSupply> results = query.getResultList();
			if (!results.isEmpty()) {
				return results.get(0);
			}
			return null;

		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	@Override
	public BankDebit save(BankDebit debit) {
		return crudService.create(debit);
	}

	@Override
	public BankDebit findById(Long bankDebitId) {
		Query query = entityManager.createNamedQuery("BankDebit.findById");
		query.setParameter("bankDebitId", bankDebitId);
		List<BankDebit> resultList = query.getResultList();
		if(resultList.isEmpty()){
			return null;
		}
		return resultList.get(0);
	}

	@Override
	public BankDebit update(BankDebit debit) {
		return crudService.update(debit);
	}

}
