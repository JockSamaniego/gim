/**
 * 
 */
package ec.gob.gim.revenue.model.criteria;

import java.util.Date;

/**
 * @author Rene
 *
 */
public class ReportEmissionCriteria {
	
	private Long account_id;
	private Date startDate;
	private Date endDate;
	private String status_ids;
	public Long getAccount_id() {
		return account_id;
	}
	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus_ids() {
		return status_ids;
	}
	public void setStatus_ids(String status_ids) {
		this.status_ids = status_ids;
	}
}