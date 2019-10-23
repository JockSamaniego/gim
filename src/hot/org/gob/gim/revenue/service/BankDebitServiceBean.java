package org.gob.gim.revenue.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
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
import ec.gob.gim.revenue.model.bankDebit.dto.BankDebitReportDTO;
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
		Query query = entityManager
				.createNativeQuery("SELECT ban.id, "
						+ "res.identificationnumber, "
						+ "res.name, "
						+ "ica.name as account_type, "
						+ "ban.accountnumber, "
						+ "ban.accountholder, "
						+ "was.servicenumber, "
						+ "ban.active "
						+ "FROM bankdebit ban "
						+ "INNER JOIN watersupply was ON was.id = ban.watersupply_id "
						+ "INNER JOIN resident res ON res.id = was.recipeowner_id "
						+ "INNER JOIN itemcatalog ica ON ica.id = ban.accounttype_itm_id");

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
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	@Override
	public BankDebit update(BankDebit debit) {
		return crudService.update(debit);
	}

	@Override
	public List<BankDebitDTO> findBankDebits(BankDebitSearchCriteria criteria, Integer firstRow, Integer numberOfRows) {
		String qryBase = "SELECT ban.id, "
								+ "res.identificationnumber, "
								+ "res.name, "
								+ "ica.name as account_type, "
								+ "ban.accountnumber, "
								+ "ban.accountholder, "
								+ "was.servicenumber, "
								+ "ban.active "
						+ "FROM bankdebit ban "
						+ "INNER JOIN watersupply was ON was.id = ban.watersupply_id "
						+ "INNER JOIN resident res ON res.id = was.recipeowner_id "
						+ "INNER JOIN itemcatalog ica ON ica.id = ban.accounttype_itm_id "
						+ "WHERE 1 = 1 ";

		String finalQuery = qryBase;
		if (criteria.getServicenumber() != null ) { 
			finalQuery = finalQuery + "AND was.servicenumber =:servicenumber " ; 
		}
		
		if (criteria.getReceiptIdentification() != null && criteria.getReceiptIdentification() != "") { 
			finalQuery = finalQuery + "AND res.identificationnumber =:receiptIdentification " ; 
		}
		
		if (criteria.getReceiptName() != null && criteria.getReceiptName() != "") { 
			finalQuery = finalQuery + "AND res.name like :receiptName " ; 
		}
	  
		finalQuery = finalQuery + " ORDER BY ban.active DESC,res.name ASC, was.servicenumber ASC";
		

		Query query = entityManager.createNativeQuery(finalQuery);
		
		if (criteria.getServicenumber() != null) { 
			query.setParameter("servicenumber", criteria.getServicenumber());
		}
		
		if (criteria.getReceiptIdentification() != null && criteria.getReceiptIdentification() != "") { 
			query.setParameter("receiptIdentification", criteria.getReceiptIdentification()); 
		}
		
		if (criteria.getReceiptName() != null && criteria.getReceiptName() != "") { 
			query.setParameter("receiptName", "%"+criteria.getReceiptName()+"%"); 
		}
		
		/*
		 * query.setParameter("workDeal_id", workDeal_id); if (cadastralCode !=
		 * null) { query.setParameter("cadastralCode", cadastralCode+"%"); }
		 */
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<BankDebitDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), BankDebitDTO.class);

		return retorno;
	}

	@Override
	public Integer findBankDebitNumber(BankDebitSearchCriteria criteria) {

		String qryBase = "SELECT count(*) " + "FROM bankdebit ban "
				+ "INNER JOIN watersupply was ON was.id = ban.watersupply_id "
				+ "INNER JOIN resident res ON res.id = was.recipeowner_id "
				+ "INNER JOIN itemcatalog ica ON ica.id = ban.accounttype_itm_id";
		
		//String conditionAdditional = " and wf.property.cadastralCode=:cadastralCode";
		String finalQuery = qryBase;

		/*if (cadastralCode != null) {
			finalQuery = finalQuery + conditionAdditional;
		}*/

		Query query = entityManager.createNativeQuery(finalQuery);
		/*query.setParameter("workDeal_id", workDeal_id);
		if (cadastralCode != null) {
			query.setParameter("cadastralCode", cadastralCode);
		}*/
		Integer size = ((BigInteger)query.getSingleResult()).intValue(); 
		return size;

	}

	@Override
	public BankDebitDTO findDtoById(Long bankDebitId) {
		String qryBase = "SELECT ban.id, "
				+ "res.identificationnumber, "
				+ "res.name, "
				+ "ica.name as account_type, "
				+ "ban.accountnumber, "
				+ "ban.accountholder, "
				+ "was.servicenumber, "
				+ "ban.active "
				+ "FROM bankdebit ban "
				+ "INNER JOIN watersupply was ON was.id = ban.watersupply_id "
				+ "INNER JOIN resident res ON res.id = was.recipeowner_id "
				+ "INNER JOIN itemcatalog ica ON ica.id = ban.accounttype_itm_id "
				+ "WHERE ban.id = ?1";
		Query query = entityManager.createNativeQuery(qryBase);
		query.setParameter(1, bankDebitId);
		
		List<BankDebitDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), BankDebitDTO.class);
		
		if(retorno.size() > 0 ){
			return retorno.get(0);
		}

		return null;
		
	}

	@Override
	public List<Long> getBankDebitResidents() {
		String sql = "SELECT "
						+"distinct(CAST (res.id AS INTEGER)) as id "
						+"FROM "
						+"bankdebit AS ban "
						+"INNER JOIN watersupply was ON ban.watersupply_id = was.id "
						+"INNER JOIN resident AS res ON was.recipeowner_id = res.id "
						+"WHERE ban.active = true"; 
		Query query = entityManager.createNativeQuery(sql);
		List<Integer> resultList = query.getResultList();
		List<Long> result = new ArrayList<Long>();
		
		for (Integer _value : resultList) {
			result.add(_value.longValue());
		}
		
		return result;
	}

	@Override
	public List<BankDebitReportDTO> getDataReport() {
		
		String qryBase = "SELECT "
									+"res.identificationnumber, "
									+"res.name contribuyente, "
									+"itm.name tipo_cuenta, "
									+"ban.accountnumber as numero_cuenta, "
									+"ban.accountholder as titular, "
									+"was.servicenumber servicio, "
									+"COUNT(distinct mbo.id) as cantidad, "
									+"SUM(mbo.paidtotal) as valor "
						+"FROM "
						+"bankdebit AS ban "
						+"INNER JOIN watersupply was ON was.id = ban.watersupply_id "
						+"INNER JOIN consumption AS con ON con.watersupply_id = was.id "
						+"INNER JOIN waterservicereference AS wsr ON wsr.consumption_id = con.id "
						+"INNER JOIN municipalbond AS mbo ON wsr.id = mbo.adjunct_id "
						+"INNER JOIN resident res ON res.id = was.recipeowner_id "
						+"INNER JOIN itemcatalog itm ON itm.id = ban.accounttype_itm_id "
						+"WHERE ban.active = true "
						+"AND mbo.municipalbondstatus_id IN (3) "
						+"GROUP BY res.identificationnumber, "
									+"res.name, "
									+"itm.name, "
									+"ban.accountnumber, "
									+"ban.accountholder, "
									+"was.servicenumber "
						+"ORDER BY 2, 6 ASC";
		Query query = entityManager.createNativeQuery(qryBase);
		
		List<BankDebitReportDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), BankDebitReportDTO.class);
		
		List<BankDebitReportDTO> _retorno = new ArrayList<BankDebitReportDTO> ();
		
		for (int i = 0; i < retorno.size(); i++) {
			BankDebitReportDTO _bdr = retorno.get(i);
			if(_bdr.getValor().compareTo(BigDecimal.ZERO) == 1){
				_retorno.add(_bdr);
			}
		}
		
		return _retorno;
		
	}

}
