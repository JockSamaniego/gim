package org.gob.gim.appraisal.action;

import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.appraisal.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("appraisalItemTypeHome")
public class AppraisalItemTypeHome extends EntityHome<AppraisalItemType> {
	
	public void setAppraisalItemTypeId(Long id) {
		setId(id);
	}

	public Long getAppraisalItemType() {
		return (Long) getId();
	}

	@Override
	protected AppraisalItemType createInstance() {
		AppraisalItemType appraisalItemType = new AppraisalItemType();
		return appraisalItemType;
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

	public AppraisalItemType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<AppraisalItemType> appraisalItemTypeRelations(){			
		Query query = getPersistenceContext().createQuery("Select appraisalItemBase.id from AppraisalItemBase appraisalItemBase where appraisalItemBase.appraisalItemType.id = :id");
		query.setParameter("id",this.getInstance().getId());
		return query.getResultList();			
    }
	
	
	
	public boolean canBeRemoved(){
		List<AppraisalItemType> appraisalItemBaseInAppraisalItemType = appraisalItemTypeRelations();
		if((appraisalItemBaseInAppraisalItemType == null || appraisalItemBaseInAppraisalItemType.size() == 0) ){			
			return true;
		}
		return false;
	}
	
	

}
