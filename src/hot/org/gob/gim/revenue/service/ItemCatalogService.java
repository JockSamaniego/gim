package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.common.model.ItemCatalog;

/**
 *  * @author René Ortega
 *  * @Fecha 2016-07-14
 */

@Local
public interface ItemCatalogService {

	public String LOCAL_NAME = "/gim/ItemCatalogService/local";

	public List<ItemCatalog> findItemsForCatalogCode(String catalogCode);
	
	public List<ItemCatalog> findItemsForCatalogCodeOrderById(String catalogCode);
	
	public List<ItemCatalog> findCompleteItemsForCatalogCodeOrderById(String catalogCode);
	
	public List<ItemCatalog> findItemsForSentStatus(String catalogCode);

	public List<ItemCatalog> findItemsForCatalogCodeExceptIds(String catalogCode,List<Long> itemsIds);
	
	public ItemCatalog findItemByCodeAndCodeCatalog(String catalogCode, String itemCode);
	
	public ItemCatalog findById(Long id);
	
}
