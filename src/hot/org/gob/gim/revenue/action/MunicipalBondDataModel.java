package org.gob.gim.revenue.action;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import ec.gob.gim.revenue.model.MunicipalBond;


@Name("municipalBonds")
@Scope(ScopeType.CONVERSATION)
public class MunicipalBondDataModel extends PageableDataModel<MunicipalBond, Long> {

	private static final long serialVersionUID = 2672142810059859813L;

	private MunicipalBondService municipalBondService;
	
	@In
	private EntityManager entityManager;
	
	/** */
	MunicipalBond currentEntity;
	/** */
	@Logger
	Log log;
	
	// Criteria
	private Long residentId;
	private Long entryId;
	private Date startDate;
	private Date endDate;
	private Long municipalBondStatusId;
	private Long municipalBondNumber;
	private Boolean future = Boolean.FALSE;
	private Date now;
	private Boolean bondsWasInAgreement = Boolean.FALSE;
	
	
	public MunicipalBondDataModel() {
		initializeService();
	}

	/**
	 * @see PaginatingDataModel#getId(java.lang.Object)
	 */
	@Override
	public Long getId(MunicipalBond object) {
		return object.getId();
	}

	/**
	 * @see PaginatingDataModel#findObjects(int, int, java.lang.String, boolean)
	 */
	@Override
	public List<MunicipalBond> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {
		if(future){
			return municipalBondService.findMunicipalBonds(residentId, entryId, municipalBondStatusId, firstRow, numberOfRows);
		}
		else{
			return municipalBondService.findMunicipalBonds(residentId, entryId, startDate, endDate, municipalBondStatusId, firstRow, numberOfRows, municipalBondNumber, bondsWasInAgreement);
		}
	}

	/**
	 * @see PaginatingDataModel#getObjectById(java.lang.Object)
	 */
	@Override
	public MunicipalBond getObjectById(Long id) {
		return findMunicipalBondById(currentId);
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
		if (future){
			return municipalBondService.findMunicipalBondsNumber(residentId, entryId, municipalBondStatusId).intValue();
		}
		else{
			return municipalBondService.findMunicipalBondsNumber(residentId, entryId, startDate, endDate, municipalBondStatusId, municipalBondNumber, bondsWasInAgreement).intValue();
		}
		
	}
		
	@Override
	public int getRowCount() {
		return rowCount.intValue();
	}

	public void setRowCount(Integer rowCount){
		this.rowCount = rowCount;
	}	
	
	
	public void initializeService() {
		if (municipalBondService == null) {
			municipalBondService = ServiceLocator.getInstance().findResource(MunicipalBondService.LOCAL_NAME);
		}
	}
	

	public void setCriteria(Long residentId, Long entryId, Date startDate, Date endDate, Long municipalBondStatusId, Long municipalBondNumber, Boolean bondsWasInAgreement){
		this.residentId = residentId;
		this.entryId = entryId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.municipalBondStatusId = municipalBondStatusId;
		this.municipalBondNumber = municipalBondNumber;
		this.future = Boolean.FALSE;
		this.now = null;
		this.bondsWasInAgreement = bondsWasInAgreement;
	}
	
	public void setCriteria(Long residentId, Long entryId, Long municipalBondStatusId, Boolean future){
		this.residentId = residentId;
		this.entryId = entryId;
		this.startDate = null;
		this.endDate = null;
		this.municipalBondStatusId = municipalBondStatusId;
		this.municipalBondNumber = null;
		this.future = future;
		this.now = null;
	}
	
	public List<MunicipalBond> getMunicipalBonds(){
		List<MunicipalBond> mbs = new LinkedList<MunicipalBond>();
		mbs.addAll(wrappedData.values());
		return mbs;
	}	
	
	
	public MunicipalBond findMunicipalBondById(Long municipalBondId){
		Query query = entityManager.createQuery("SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.municipalBondStatus WHERE mb.id = :municipalBondId");
		query.setParameter("municipalBondId", municipalBondId);
		query.setFirstResult(rowCount.intValue()).setMaxResults(12);
		return (MunicipalBond) query.getSingleResult();
	}
	
	public Boolean hasMunicipalBond(Long municipalBondId){
		try{
			MunicipalBond mb = findMunicipalBondById(municipalBondId);
			if(mb == null){
				return Boolean.FALSE;
			} else {
				return Boolean.TRUE;
			}
		} catch (Exception e){
			return Boolean.FALSE;
		}
		
	}
	

	
}
