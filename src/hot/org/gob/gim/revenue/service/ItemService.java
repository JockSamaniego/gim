package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.revenue.model.Item;

@Local
public interface ItemService {
	
	List<Item> findItems(Long municipalBondId);

}
