/**
 * 
 */
package org.gob.gim.finances.pagination;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.finances.service.WriteOffService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.finances.model.DTO.WriteOffRequestDTO;

/**
 * @author René
 *
 */
@Name("writeOffRequests")
@Scope(ScopeType.CONVERSATION)
public class WriteOffRequestDataModel extends PageableDataModel<WriteOffRequestDTO, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private EntityManager entityManager;
	
	// Criteria
	
	private String number_request_criteria;
	
	private String identification_number_criteria;
	
	private String name_resident_criteria;
	
	private WriteOffService writeOffService;
	
	public WriteOffRequestDataModel() {
		initializeService();
	}
	
	public void initializeService() {
		if (writeOffService == null) {
			writeOffService = ServiceLocator.getInstance().findResource(WriteOffService.LOCAL_NAME);
		}
	}
	
	public void setCriteria(String number_request_criteria, String identification_number_criteria, String name_resident_criteria){
		this.identification_number_criteria = identification_number_criteria;
		this.name_resident_criteria = name_resident_criteria;
		this.number_request_criteria = number_request_criteria;
	}
	

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getId(java.lang.Object)
	 */
	@Override
	public Long getId(WriteOffRequestDTO object) {
		// TODO Auto-generated method stub
		return object.getId().longValue();
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#findObjects(int, int, java.lang.String, boolean)
	 */
	@Override
	public List<WriteOffRequestDTO> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {
		// TODO Auto-generated method stub
		 
			List<WriteOffRequestDTO> _return = writeOffService.findByCriteria(number_request_criteria, identification_number_criteria, name_resident_criteria,firstRow,numberOfRows);
			return _return;
	}
	
	public WriteOffRequestDTO findWriteOffRequestById(Long writeOffRequestId){
		Query query = this.entityManager
				.createNativeQuery("SELECT "
										+"wor.id, "
										+"wor.date, "
										+"res.identificationnumber, "
										+"res.name AS resident_name, "
										+"was.servicenumber, "
										+"wme.serial, "
										+"wrt.name AS _type, "
										+"to_char(seq.code,'0000') number_code, "
										+"EXTRACT(YEAR FROM wor.date) as _year "
									+"FROM "
										+"writeoffrequest wor "
									+"INNER JOIN resident res ON wor.resident_id = res.id "
									+"INNER JOIN watermeter wme ON wor.watermeter_id = wme.id "
									+"INNER JOIN watersupply was ON wme.watersupply_id = was.id "
									+"INNER JOIN writeofftype wrt ON wor.writeofftype_id = wrt.id "
									+"INNER JOIN sequencemanager seq ON wor.sequencemanager_id = seq.id "
									+"WHERE wor.id = ?1");
		query.setParameter(1, writeOffRequestId);
		query.setFirstResult(rowCount.intValue()).setMaxResults(1);
		return (WriteOffRequestDTO) query.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.PageableDataModel#getObjectById(java.lang.Object)
	 */
	@Override
	public WriteOffRequestDTO getObjectById(Long id) {
		return findWriteOffRequestById(id);
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
		return writeOffService.findWriteOffRequestsNumber(this.number_request_criteria, this.identification_number_criteria, this.name_resident_criteria).intValue();
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
