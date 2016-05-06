package org.gob.gim.revenue.action;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.revenue.model.SolvencyHistory;

@Name("solvencyHistoryHome")
public class SolvencyHistoryHome extends EntityHome<SolvencyHistory> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;

	private String criteria;

	@In
	FacesMessages facesMessages;
	
	public boolean isFirsTime = true;
	
	private Date startDate;
	private Date endDate;

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
//		if (!isFirsTime) return;
		getInstance();
	}

	public boolean isWired() {
		return true;
	}
	
	public SolvencyHistoryHome(){
		
	}
	
	public String save(){
//		if(this.instance.getBloodRelation() == null){
//			String message = Interpolator.instance().interpolate("#{messages['cremation.notExistBloodRelation']}", new Object[0]);
//			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
//			return "failed";
//		}
//		updateRateValue();
//		if(this.isManaged()) return super.update();
		
		return super.persist();
	}
	
	public SolvencyHistory getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setCremationId(Long id) {
		setId(id);
	}

	public Long getSolvencyHistoryId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


}