package org.gob.gim.appraisal.action;

import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.appraisal.model.*;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("appraisalItemBaseHome")
public class AppraisalItemBaseHome extends EntityHome<AppraisalItemBase> {
	
	private AppraisalItemType appraisalItemType;
	
	@In(create=true)
	AppraisalItemTypeHome appraisalItemTypeHome;

	private boolean correctPersist = false;
	
	public void setAppraisalItemBaseId(Long id) {
		setId(id);
	}

	public Long getAppraisalItemBaseId() {
		return (Long) getId();
	}

	@Override
	protected AppraisalItemBase createInstance() {
		AppraisalItemBase appraisalItemBase = new AppraisalItemBase();
		return appraisalItemBase;
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

	public AppraisalItemBase getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<AppraisalItemType> findAppraisalItemType(){			
			Query query = getPersistenceContext().createNamedQuery("AppraisalItemType.findAll");		
			return query.getResultList();			
	}
	
	public void createAppraisalItemType(){		
		this.appraisalItemType = new AppraisalItemType();
		setCorrectPersist(false);
	}

	public void persistAppraisalItemType(){
		appraisalItemTypeHome.setInstance(appraisalItemType);		
		appraisalItemTypeHome.persist();
		this.getInstance().setAppraisalItemType(appraisalItemType);
		correctPersist = true;
	}

	public void setPlace(AppraisalItemType appraisalItemType) {
		this.appraisalItemType = appraisalItemType;
	}

	public AppraisalItemType getAppraisalItemType() {
		return appraisalItemType;
	}

	public void setCorrectPersist(boolean correctPersist) {
		this.correctPersist = correctPersist;
	}

	public boolean isCorrectPersist() {
		return correctPersist;
	}

}
