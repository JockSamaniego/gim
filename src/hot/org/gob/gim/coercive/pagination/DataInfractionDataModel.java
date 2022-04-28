/**
 * 
 */
package org.gob.gim.coercive.pagination;

import java.util.List;

import org.gob.gim.coercive.dto.criteria.DataInfractionSearchCriteria;
import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.coercive.model.infractions.Datainfraction;

/**
 * @author René
 *
 */
@Name("dataInfraction")
@Scope(ScopeType.CONVERSATION)
public class DataInfractionDataModel extends
		PageableDataModel<Datainfraction, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Criteria
	private DataInfractionSearchCriteria criteria;

	private DatainfractionService service;

	/**
	 * 
	 */
	public DataInfractionDataModel() {
		super();
		this.initializeService();
		// TODO Auto-generated constructor stub
	}

	public void initializeService() {
		if (service == null) {
			service = ServiceLocator.getInstance().findResource(
					service.LOCAL_NAME);
		}
	}

	/**
	 * @return the criteria
	 */
	public DataInfractionSearchCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(DataInfractionSearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public List<Datainfraction> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {
		List<Datainfraction> result = service
				.findDataInfractionByCriteria(criteria, firstRow,
						numberOfRows);
		return result;
	}

	@Override
	public String getDefaultSortField() {
		return "attribute(EOD)";
	}

	@Override
	public int getObjectsNumber() {
		return service.findDataInfractionNumber(criteria).intValue();
	}

	@Override
	public int getRowCount() {
		return rowCount.intValue();
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}


	@Override
	public Long getId(Datainfraction object) {
		// TODO Auto-generated method stub
		return object.getId();
	}

	@Override
	public Datainfraction getObjectById(Long id) {
		return this.service.getDataInfractionById(id);
	}

}
