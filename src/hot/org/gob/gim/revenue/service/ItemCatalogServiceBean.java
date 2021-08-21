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
 * * @author René Ortega * @Fecha 2016-07-14
 */

@Stateless(name = "ItemCatalogService")
public class ItemCatalogServiceBean implements ItemCatalogService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@Override
	public List<ItemCatalog> findItemsForCatalogCode(String catalogCode) {
		Query query = entityManager
				.createQuery("select i from ItemCatalog i where i.catalogCode=:catalogCode ORDER BY i.name ASC");
		query.setParameter("catalogCode", catalogCode);
		List<ItemCatalog> listResult = query.getResultList();
		return listResult;
	}
	
	@Override
	public List<ItemCatalog> findItemsForCatalogCodeOrderById(String catalogCode) {
		Query query = entityManager
				.createQuery("select i from ItemCatalog i where i.catalogCode=:catalogCode ORDER BY i.id ASC");
		query.setParameter("catalogCode", catalogCode);
		List<ItemCatalog> listResult = query.getResultList();
		return listResult;
	}

	@Override
	public ItemCatalog findItemByCodeAndCodeCatalog(String catalogCode,
			String itemCode) {
		Query query = entityManager
				.createQuery("select i from ItemCatalog i inner join i.catalog c where c.code=:catalogCode and i.code=:itemCode");
		query.setParameter("catalogCode", catalogCode);
		query.setParameter("itemCode", itemCode);
		List<ItemCatalog> listResult = query.getResultList();
		if (!listResult.isEmpty()) {
			return listResult.get(0);
		}
		return null;
	}

	@Override
	public List<ItemCatalog> findItemsForCatalogCodeExceptIds(
			String catalogCode, List<Long> itemsIds) {
		Query query = entityManager
				.createQuery("select i from ItemCatalog i where i.catalogCode=:catalogCode and i.id not in (:itemsIds)");
		query.setParameter("catalogCode", catalogCode);
		query.setParameter("itemsIds", itemsIds);
		List<ItemCatalog> listResult = query.getResultList();
		return listResult;
	}

	@Override
	public ItemCatalog findById(Long id) {
		Query query = entityManager
				.createQuery("select i from ItemCatalog i where i.id=:id");
		query.setParameter("id", id);
		List<ItemCatalog> listResult = query.getResultList();
		if(!listResult.isEmpty()){
			return listResult.get(0);
		}
		return null;
	}

}
