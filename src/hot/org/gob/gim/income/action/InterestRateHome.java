package org.gob.gim.income.action;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.InterestRate;

@Name("interestRateHome")
public class InterestRateHome extends EntityHome<InterestRate> {

	private static final long serialVersionUID = 1L;

	public void setInterestRateId(Long id) {
		setId(id);
	}

	public Long getInterestRateId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();		
		if(!isManaged()){
			getInstance().setBeginDate(findSinceDate());			
		}
	}

	public boolean isWired() {
		return true;
	}

	public InterestRate getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public Date findSinceDate(){
		Date last = findLastEndDate();
		if(last == null){
			return new Date();
		}else{			
			Calendar ca = Calendar.getInstance();
			ca.setTime(last);			
			ca.add(Calendar.DATE, 1);			
			return ca.getTime();
		}
	}
	
	@In
	FacesMessages facesMessages;
	
	public String save(){
		if(getInstance().getBeginDate().after(getInstance().getEndDate())){
			String message = Interpolator.instance().interpolate("#{messages['common.startDateIsAfterEndDate']}", new Object[0]);
			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			return "failed";
		}
		if(isManaged()){
			return super.update();
		}else{
			return super.persist();
		}		
			
	}
	
	
	private Date findLastEndDate(){
		Query query = getEntityManager().createNamedQuery("InterestRate.findLastEndDate");
		return (Date)query.getSingleResult();
	}

}
