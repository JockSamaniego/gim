/**
 * 
 */
package org.gob.gim.revenue.action.bankDebit.pagination;

import java.util.List;

import javax.persistence.EntityManager;

import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.service.BankDebitService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.revenue.model.bankDebit.criteria.BankDebitSearchCriteria;
import ec.gob.gim.revenue.model.bankDebit.dto.BankDebitDTO;

/**
 * @author rene
 *
 */
@Name("bankDebits")
@Scope(ScopeType.CONVERSATION)
public class BankDebitDataModel extends PageableDataModel<BankDebitDTO, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private EntityManager entityManager;

	// Criteria
	private BankDebitSearchCriteria criteria;

	private BankDebitService bankDebitService;

	public BankDebitDataModel() {
		initializeService();
	}

	public void initializeService() {
		if (bankDebitService == null) {
			bankDebitService = ServiceLocator.getInstance().findResource(
					bankDebitService.LOCAL_NAME);
		}
	}

	public void setCriteria(BankDebitSearchCriteria criteria) {
		this.criteria = criteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.common.PageableDataModel#getId(java.lang.Object)
	 */
	@Override
	public Long getId(BankDebitDTO object) {
		// TODO Auto-generated method stub
		return object.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.common.PageableDataModel#findObjects(int, int,
	 * java.lang.String, boolean)
	 */
	@Override
	public List<BankDebitDTO> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {
		// TODO Auto-generated method stub

		List<BankDebitDTO> debits = bankDebitService.findBankDebits(
				this.criteria, firstRow, numberOfRows);
		return debits;
	}

	public BankDebitDTO findBankDebitById(Long fractionId) {
		return this.bankDebitService.findDtoById(fractionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.common.PageableDataModel#getObjectById(java.lang.Object)
	 */
	@Override
	public BankDebitDTO getObjectById(Long id) {
		// TODO Auto-generated method stub
		return findBankDebitById(currentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.common.PageableDataModel#getDefaultSortField()
	 */
	@Override
	public String getDefaultSortField() {
		// TODO Auto-generated method stub
		return "attribute(EOD)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.common.PageableDataModel#getObjectsNumber()
	 */
	@Override
	public int getObjectsNumber() {
		return bankDebitService.findBankDebitNumber(this.criteria).intValue();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rowCount.intValue();
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

}
