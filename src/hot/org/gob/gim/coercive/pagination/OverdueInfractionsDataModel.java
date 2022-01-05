/**
 * 
 */
package org.gob.gim.coercive.pagination;

import java.util.List;

import org.gob.gim.coercive.dto.OverdueInfractionDTO;
import org.gob.gim.coercive.dto.OverdueInfractionsSearchCriteria;
import org.gob.gim.coercive.service.OverdueInfractionsService;
import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.service.BankDebitService;

import ec.gob.gim.revenue.model.bankDebit.criteria.BankDebitSearchCriteria;
import ec.gob.gim.revenue.model.bankDebit.dto.BankDebitDTO;

/**
 * @author René
 *
 */
public class OverdueInfractionsDataModel extends
		PageableDataModel<OverdueInfractionDTO, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Criteria
	private OverdueInfractionsSearchCriteria criteria;

	private OverdueInfractionsService overdueInfractionsService;

	/**
	 * 
	 */
	public OverdueInfractionsDataModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void initializeService() {
		if (overdueInfractionsService == null) {
			overdueInfractionsService = ServiceLocator.getInstance()
					.findResource(overdueInfractionsService.LOCAL_NAME);
		}
	}

	/**
	 * @return the criteria
	 */
	public OverdueInfractionsSearchCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(OverdueInfractionsSearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Long getId(OverdueInfractionDTO object) {
		return object.getId();
	}

	@Override
	public List<OverdueInfractionDTO> findObjects(int firstRow,
			int numberOfRows, String sortField, boolean descending) {
		List<OverdueInfractionDTO> debits = overdueInfractionsService
				.findInfractions(criteria, firstRow, numberOfRows);
		return debits;
	}

	@Override
	public OverdueInfractionDTO getObjectById(Long id) {
		return this.overdueInfractionsService.findDtoById(id);
	}

	@Override
	public String getDefaultSortField() {
		return "attribute(EOD)";
	}

	@Override
	public int getObjectsNumber() {
		return overdueInfractionsService.findOverdueInfractionsNumber(criteria)
				.intValue();
	}

	@Override
	public int getRowCount() {
		return rowCount.intValue();
	}
	
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

}
