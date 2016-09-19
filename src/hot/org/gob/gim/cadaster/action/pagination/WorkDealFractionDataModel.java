/**
 * 
 */
package org.gob.gim.cadaster.action.pagination;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.cadaster.service.WorkDealFractionService;
import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.cadaster.model.WorkDealFraction;

/**
 * @author rene
 *
 */
@Name("fractions")
@Scope(ScopeType.CONVERSATION)
public class WorkDealFractionDataModel extends PageableDataModel<WorkDealFraction, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private EntityManager entityManager;
	
	// Criteria
	private Long workDealId;
	
	private String cadastralCode;
	
	private WorkDealFractionService workDealFractionService;
	
	public WorkDealFractionDataModel() {
		initializeService();
	}
	
	public void initializeService() {
		if (workDealFractionService == null) {
			workDealFractionService = ServiceLocator.getInstance().findResource(WorkDealFractionService.LOCAL_NAME);
		}
	}
	
	public void setCriteria(Long workDealId, String cadastralCode){
		this.workDealId = workDealId;
		this.cadastralCode = cadastralCode;
	}
	

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getId(java.lang.Object)
	 */
	@Override
	public Long getId(WorkDealFraction object) {
		// TODO Auto-generated method stub
		return object.getId();
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#findObjects(int, int, java.lang.String, boolean)
	 */
	@Override
	public List<WorkDealFraction> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {
		// TODO Auto-generated method stub
		 
			List<WorkDealFraction> fractions = workDealFractionService.findFractions(this.workDealId,this.cadastralCode,firstRow,numberOfRows);
			return fractions;
	}
	
	public WorkDealFraction findWorkDealFractionById(Long fractionId){
		Query query = entityManager.createQuery("SELECT wf FROM WorkDealFraction wf WHERE wf.id=:fractionId");
		query.setParameter("fractionId", fractionId);
		query.setFirstResult(rowCount.intValue()).setMaxResults(12);
		return (WorkDealFraction) query.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getObjectById(java.lang.Object)
	 */
	@Override
	public WorkDealFraction getObjectById(Long id) {
		// TODO Auto-generated method stub
		return findWorkDealFractionById(currentId);
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getDefaultSortField()
	 */
	@Override
	public String getDefaultSortField() {
		// TODO Auto-generated method stub
		return "attribute(EOD)";
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getObjectsNumber()
	 */
	@Override
	public int getObjectsNumber() {
		return workDealFractionService.findWorkDealFractionsNumber(workDealId,cadastralCode).intValue();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rowCount.intValue();
	}
	
	public void setRowCount(Integer rowCount){
		this.rowCount = rowCount;
	}	

}
