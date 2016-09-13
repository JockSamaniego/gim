package org.gob.gim.revenue.action.impugnment;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.service.ImpugnmentService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import ec.gob.gim.revenue.model.impugnment.Impugnment;

/**
 *  * @author René Ortega
 *  * @Fecha 2016-07-14
 */
@Name("impugnments")
@Scope(ScopeType.CONVERSATION)
public class ImpugnmentDataModel extends PageableDataModel<Impugnment, Long> {

	private static final long serialVersionUID = 2672142810059859813L;

	private ImpugnmentService impugnmentService;

	@In
	private EntityManager entityManager;

	@Logger
	Log log;
	
	/** */
	Impugnment currentEntity;
	/** */

	// Criteria
	private Integer numberProsecution;

	private Integer numberInfringement;

	public ImpugnmentDataModel() {
		initializeService();
	}

	/**
	 * @see PaginatingDataModel#getId(java.lang.Object)
	 */
	@Override
	public Long getId(Impugnment object) {
		return object.getId();
	}

	/**
	 * @see PaginatingDataModel#findObjects(int, int, java.lang.String, boolean)
	 */
	@Override
	public List<Impugnment> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {

		return impugnmentService.findImpugnments(numberProsecution,
				numberInfringement, firstRow, numberOfRows);

	}

	/**
	 * @see PaginatingDataModel#getObjectById(java.lang.Object)
	 */
	@Override
	public Impugnment getObjectById(Long id) {
		return findImpugnmentById(currentId);
	}

	/**
	 * @see PaginatingDataModel#getDefaultSortField()
	 */
	@Override
	public String getDefaultSortField() {
		return "attribute(EOD)";
	}

	/**
	 * @see PaginatingDataModel#getNumRecords()
	 */
	@Override
	public int getObjectsNumber() {

		return impugnmentService.findImpugnmentsNumber(numberProsecution,
				numberInfringement).intValue();

	}

	@Override
	public int getRowCount() {
		return rowCount.intValue();
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public void initializeService() {
		if (impugnmentService == null) {
			impugnmentService = ServiceLocator.getInstance().findResource(
					ImpugnmentService.LOCAL_NAME);
		}
	}

	public void setCriteria(Integer numberProsecution,
			Integer numberInfringement) {
		this.numberProsecution = numberProsecution;
		this.numberInfringement = numberInfringement;
	}

	public List<Impugnment> getImpugnments() {
		List<Impugnment> imp = new LinkedList<Impugnment>();
		imp.addAll(wrappedData.values());
		return imp;
	}

	public Impugnment findImpugnmentById(Long impugnmentId) {
		Query query = entityManager
				.createQuery("SELECT i FROM Impugnment i WHERE i.id = :impugnmentId");
		query.setParameter("impugnmentId", impugnmentId);
		query.setFirstResult(rowCount.intValue()).setMaxResults(12);
		return (Impugnment) query.getSingleResult();
	}
	
	public Boolean hasImpugnment(Long impugnmentId){
		try{
			Impugnment imp = findImpugnmentById(impugnmentId);
			if(imp == null){
				return Boolean.FALSE;
			} else {
				return Boolean.TRUE;
			}
		} catch (Exception e){
			return Boolean.FALSE;
		}
		
	}

}
