/**
 * 
 */
package org.gob.gim.coercive.pagination;

import java.util.LinkedList;
import java.util.List;

import org.gob.gim.coercive.dto.InfractionItemDTO;
import org.gob.gim.coercive.dto.criteria.OverdueInfractionsSearchCriteria;
import org.gob.gim.coercive.service.OverdueInfractionsService;
import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author René
 *
 */
@Name("overdueInfractions")
@Scope(ScopeType.CONVERSATION)
public class OverdueInfractionsDataModel extends
		PageableDataModel<InfractionItemDTO, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Criteria
	private OverdueInfractionsSearchCriteria criteria;

	private OverdueInfractionsService service;

	private Boolean selected = Boolean.FALSE;

	/**
	 * @return the selected
	 */
	public Boolean getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	/**
	 * 
	 */
	public OverdueInfractionsDataModel() {
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
	public String getId(InfractionItemDTO object) {
		return object.getIdentification();
	}

	@Override
	public List<InfractionItemDTO> findObjects(int firstRow, int numberOfRows,
			String sortField, boolean descending) {
		List<InfractionItemDTO> result = service
				.searchInfractionGroupByCriteria(criteria, firstRow,
						numberOfRows);
		return result;
	}

	@Override
	public InfractionItemDTO getObjectById(String id) {
		return this.service.findOverdueInfractionById(id, criteria);
	}

	@Override
	public String getDefaultSortField() {
		return "attribute(EOD)";
	}

	@Override
	public int getObjectsNumber() {
		return service.findOverdueInfractionsNumber(criteria).intValue();
	}

	@Override
	public int getRowCount() {
		return rowCount.intValue();
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public List<InfractionItemDTO> getItems() {
		List<InfractionItemDTO> items = new LinkedList<InfractionItemDTO>();
		items.addAll(wrappedData.values());
		return items;
	}

}
