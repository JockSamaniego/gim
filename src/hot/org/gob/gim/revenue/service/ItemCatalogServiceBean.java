package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.common.model.ItemCatalog;

/**
 *  * @author René Ortega
 *  * @Fecha 2016-07-14
 */


@Stateless(name = "ItemCatalogService")
public class ItemCatalogServiceBean implements ItemCatalogService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@Override
	public List<ItemCatalog> findItemsForCatalogCode(String catalogCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemCatalog findItemByCodeAndCodeCatalog(String catalogCode,
			String itemCode) {
		Query query = entityManager.createQuery("select i from ItemCatalog i inner join i.catalog c where c.code=:catalogCode and i.code=:itemCode");
		query.setParameter("catalogCode", catalogCode);
		query.setParameter("itemCode", itemCode);
		List<ItemCatalog> listResult = query.getResultList();
		if(!listResult.isEmpty()){
			return listResult.get(0);
		}
		return null;
	}

//	@Override
//	public Impugnment save(Impugnment impugnment) {
//		return crudService.create(impugnment);
//	}
//
//	@Override
//	public Impugnment update(Impugnment impugnment) {
//		return crudService.update(impugnment);
//	}
//
//	@Override
//	public List<Impugnment> findImpugnments(Integer numberProsecution,
//			Integer numberInfringement, Integer firstRow, Integer numberOfRows) {
//		Query query = entityManager.createQuery("select i from Impugnment i");
//		query.setFirstResult(firstRow);
//		query.setMaxResults(numberOfRows);
//
//		List<Impugnment> result = query.getResultList();
//
//		return result;
//
//	}
//
//	@Override
//	public Long findImpugnmentsNumber(Integer numberProsecution,
//			Integer numberInfringement) {
////		Query query = entityManager.createQuery("select i from Impugnment i");
////		query.setFirstResult(firstRow);
////		query.setMaxResults(numberOfRows);
////
////		List<Impugnment> result = query.getResultList();
//
//		return new Long(1);
//	}
//
//	@Override
//	public List<Impugnment> findAll() {
//		Query query = entityManager.createQuery("select i from Impugnment i");
//		return query.getResultList();
//	}
//
//	@Override
//	public MunicipalBond findMunicipalBondForImpugnment(
//			Integer numberInfringement) {
//		try {
//			Query query = entityManager.createNativeQuery("select mb.id from municipalbond mb inner join antreference ant on mb.adjunct_id = ant.id where ant.antnumber=:numberInfringement");
//			query.setParameter("numberInfringement", numberInfringement);
//			BigInteger municipalBondId = (BigInteger) query.getSingleResult();
//			if(municipalBondId != null){
//				Query query1 = entityManager.createNamedQuery("MunicipalBond.findById");
//				query1.setParameter("municipalBondId", municipalBondId.longValue());
//				List<MunicipalBond> results = query1.getResultList();
//				if(!results.isEmpty()){
//					return results.get(0);
//				}
//			}
//			return null;
//			
//		} catch (javax.persistence.NoResultException e) {
//			return null;
//		}
//	}
}
