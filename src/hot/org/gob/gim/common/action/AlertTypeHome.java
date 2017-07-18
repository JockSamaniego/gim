package org.gob.gim.common.action;

import java.util.Calendar;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.AlertType;

@Name("alertTypeHome")
public class AlertTypeHome extends EntityHome<AlertType> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2343245935635548909L;

	private boolean isFirstTime = true;
	private boolean enabledSave = true;
	
	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	
	public void setAlertTypeId(Long id) {
		setId(id);
	}

	public Long getAlertTypeId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	private Boolean isNew;
	public void wire() {
		if (isFirstTime) {
			getInstance();
			isFirstTime = false;
			Calendar now = Calendar.getInstance();
			if(this.getInstance().getId() == null){
				this.instance = new AlertType();
				this.instance.setCreateDate(now.getTime());
				this.instance.setOpenUser(userSession.getUser());
				this.isNew = Boolean.TRUE;
			}else{
				this.isNew = Boolean.FALSE;
			}
		}
	}

	public boolean isWired() {
		return true;
	}

	public AlertType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
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

	/**
	 * @return the enabledSave
	 */
	public boolean isEnabledSave() {
		return enabledSave;
	}

	/**
	 * @param enabledSave the enabledSave to set
	 */
	public void setEnabledSave(boolean enabledSave) {
		this.enabledSave = enabledSave;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public String editAlertType() {
		super.update();
		return "persisted";
	}
	
	public String saveAlertType() {
		if(this.isNew){
			super.persist();
			return "persisted";
		}else{
			super.update();
			return "updated";
		}	
	}
	
}