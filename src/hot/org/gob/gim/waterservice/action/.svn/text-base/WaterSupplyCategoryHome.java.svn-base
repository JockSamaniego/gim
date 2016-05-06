package org.gob.gim.waterservice.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.waterservice.model.Tariff;
import ec.gob.gim.waterservice.model.WaterSupplyCategory;

@Name("waterSupplyCategoryHome")
@Scope(ScopeType.CONVERSATION)
public class WaterSupplyCategoryHome extends EntityHome<WaterSupplyCategory> {

	public void setWaterSupplyCategoryId(Long id) {
		setId(id);
	}

	public Long getWaterSupplyCategoryId() {
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

	public WaterSupplyCategory getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	// mi codigo

//	public void add() {
//		Tariff tariff = new Tariff();
//		this.getInstance().getTariffs().add(tariff);
//	}
//	
//	public void remove(Tariff tariff){		
//		getInstance().remove(tariff);
//	}
//
	public List<Tariff> getTariffs() {
		return getInstance() == null ? null : new ArrayList<Tariff>(getInstance().getTariffs());
	}

	/*
	 * para autocompletado en cuadro de texto escribiendo el nombre
	 */
//	public List<WaterSupplyCategory> autoCompleteCategoryByName(Object o) {
//		Query query = this.getEntityManager().createNamedQuery(
//				"WaterSupplyCategory.findByName");
//		query.setParameter("waterSupplyCategoryName", o.toString());
//		return (List<WaterSupplyCategory>) query.getResultList();
//	}

	@Factory("waterSupplyCategories")
	@SuppressWarnings("unchecked")
	public List<WaterSupplyCategory> findWaterSupplyCategories(){
		Query query = getPersistenceContext().createNamedQuery("WaterSupplyCategory.findAll");
		return query.getResultList();
	}
		
}
