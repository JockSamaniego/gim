package org.gob.gim.firestation.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jboss.seam.annotations.Factory;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import javax.persistence.Query;

import ec.gob.gim.firestation.model.StoreValue;
import ec.gob.gim.firestation.model.GroupStores;
import ec.gob.gim.revenue.model.Adjunct;

@Name("storeValueHome")
public class StoreValueHome extends EntityHome<StoreValue> {

	public void setStoreValueId(Long id) {
		setId(id);
	}

	public Long getStoreValueId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public StoreValue getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Factory ("groupStores")
	@SuppressWarnings("unchecked")
	public List<GroupStores> findGroupStores() {
		Query query = getPersistenceContext().createNamedQuery("GroupStores.findAll");
		return query.getResultList();
	}

}
