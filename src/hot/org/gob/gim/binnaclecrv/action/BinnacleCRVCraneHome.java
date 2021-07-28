package org.gob.gim.binnaclecrv.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.binnaclecrv.model.BinnacleCRVCrane;

@Name("binnacleCRVCraneHome")
public class BinnacleCRVCraneHome extends EntityHome<BinnacleCRVCrane> {

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	@Logger
	Log logger;

	@In
	FacesMessages facesMessages;

	private boolean isFirstTime = true;
	
	private String criteria;

	public void setBinnacleCRVCraneId(Long id) {
		setId(id);
	}

	public Long getBinnacleCRVCraneId() {
		return (Long) getId();
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

	public BinnacleCRVCrane getDefinedInstance() {
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

	public void createBinnacleCRVCrane() {
	}
	
	public void editBinnacleCRVCrane() {
	}
	
	@Override
	@Transactional
	public String update() {
		return super.update();
	}
	
}