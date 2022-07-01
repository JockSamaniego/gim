/**
 * 
 */
package org.gob.gim.coercive.dto.criteria;

import java.util.Calendar;
import java.util.Date;

/**
 * @author macartuche
 *
 */
public class PaymentInfractionsSearchCriteria {
	
	private Date from;
	private Date until;
	private Long statusid;
		
	/**
	 * 
	 */
	public PaymentInfractionsSearchCriteria() {
		super();
		Calendar now = Calendar.getInstance();
		 
		if (this.from == null) {
			setFrom(now.getTime());
		}

		if (this.until == null) {
			setUntil(now.getTime());
		}
	}


	public Date getFrom() {
		return from;
	}


	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getUntil() {
		return until;
	}

	public void setUntil(Date until) {
		this.until = until;
	}


	public Long getStatusid() {
		return statusid;
	}


	public void setStatusid(Long statusid) {
		this.statusid = statusid;
	} 
}
