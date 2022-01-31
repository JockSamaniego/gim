package org.gob.gim.factoryline.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.factoryline.FactoryLineRoad;


@Name("factoryLineRoadHome")
@Scope(ScopeType.CONVERSATION)
public class FactoryLineRoadHome extends EntityHome<FactoryLineRoad> {

	private static final long serialVersionUID = -4660686721826730296L;

	@In
	FacesMessages facesMessages;

	private boolean isFirstTime = true;
	
	private String criteria;
	private String criteriaSearch;

	public void setFactoryLineRoadId(Long id) {
		setId(id);
	}

	public Long getFactoryLineRoadId() {
		return (Long) getId();
	}

	/**
	 * @return the criteriaSearch
	 */
	public String getCriteriaSearch() {
		return criteriaSearch;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if (!isFirstTime)
			return;
		isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	public FactoryLineRoad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	/**
	 * @return the isFirstTime
	 */
	public boolean isFirstTime() {
		return isFirstTime;
	}

	/**
	 * @param isFirstTime the isFirstTime to set
	 */
	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	/* (non-Javadoc)
	 * @see org.jboss.seam.framework.EntityHome#persist()
	 */
	@Override
	public String persist() {
		if (!isIdDefined()) {
		}
		return super.persist();
	}

	@Override
	@Transactional
	public String update() {
		return super.update();
	}
	
}