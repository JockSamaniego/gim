package org.gob.gim.photoMults.pagination;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.photoMults.dto.CriteriaReport;
import org.gob.gim.photoMults.dto.ReportPhotoMultsDTO;
import org.gob.gim.photoMults.service.PhotoMultsService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author René
 * @date 2020-01-09
 */
@Name("photoMults")
@Scope(ScopeType.CONVERSATION)
public class PhotoMultsDataModel extends PageableDataModel<ReportPhotoMultsDTO, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private EntityManager entityManager;
	
	// Criteria
	
	private CriteriaReport criteria = new CriteriaReport();
	
	private PhotoMultsService photoMultsService;
	
	public PhotoMultsDataModel() {
		initializeService();
	}
	
	public void initializeService() {
		if (photoMultsService == null) {
			photoMultsService = ServiceLocator.getInstance().findResource(photoMultsService.LOCAL_NAME);
		}
	}
	
	public void setCriteria(CriteriaReport criteria){
		this.criteria = criteria;
	}
	

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getId(java.lang.Object)
	 */
	@Override
	public Long getId(ReportPhotoMultsDTO object) {
		// TODO Auto-generated method stub
		return object.getNumber().longValue();
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#findObjects(int, int, java.lang.String, boolean)
	 */
	@Override
	public List<ReportPhotoMultsDTO> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {
		// TODO Auto-generated method stub
		 
			List<ReportPhotoMultsDTO> _return = photoMultsService.findPhotoMults(criteria, firstRow, numberOfRows);
			return _return;
	}
	
	public ReportPhotoMultsDTO findPhotoMultById(Long photoMultId){
		Query query = entityManager
				.createNativeQuery("SELECT mb.number, "
						+ "mb.emisiondate, "
						+ "mbs.name as status, "
						+ "ent. name as entry, "
						+ "mb.base as value, "
						+ "res.identificationnumber, "
						+ "res.name, "
						+ "ant.citationdate, "
						+ "ant.contraventionnumber, "
						+ "ant.numberplate, "
						+ "ant.physicalinfractionnumber, "
						+ "ant.servicetype, "
						+ "ant.speeding, "
						+ "ant.supportdocumenturl, "
						+ "ant.vehicletype, "
						+ "mb.creationdate, "
						+ "ant.id, "
						+ "ant.documentVisualizationsNumber "
						+ "FROM municipalbond mb "
						+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
						+ "INNER JOIN resident res ON res.id = mb.resident_id "
						+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
						+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
						+ "where mb.municipalbondstatus_id IN (3,4,5,6,7,9,11,13,14) "
						+ "AND mb.entry_id IN (643,644) "
						+ "AND mb.number = ?1");

		query.setParameter(1, photoMultId);
		query.setFirstResult(rowCount.intValue()).setMaxResults(1);
		return (ReportPhotoMultsDTO) query.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getObjectById(java.lang.Object)
	 */
	@Override
	public ReportPhotoMultsDTO getObjectById(Long id) {
		return findPhotoMultById(id);
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getDefaultSortField()
	 */
	@Override
	public String getDefaultSortField() {
		return "attribute(EOD)";
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getObjectsNumber()
	 */
	@Override
	public int getObjectsNumber() {
		return photoMultsService.findPhotoMultsNumber(criteria).intValue();
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
