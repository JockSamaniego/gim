/**
 * 
 */
package org.gob.gim.coercive.dto.criteria;

import java.util.Calendar;
import java.util.Date;

/**
 * @author René
 *
 */
public class OverdueInfractionsSearchCriteria {
	
	private String identification;
	private String name;
	private String licensePlate;
	private Date emisionFrom;
	private Date emisionUntil;
	private Date expirationFrom;
	private Date expirationUntil;
	private String ticket;
	private String article;
	
		
	/**
	 * 
	 */
	public OverdueInfractionsSearchCriteria() {
		super();
		Calendar now = Calendar.getInstance();
		int lastDayMonth = now.getActualMaximum(Calendar.DATE);
		now.set(Calendar.DATE, lastDayMonth);
		int yearCurrent = now.get(Calendar.YEAR);
		Calendar from = Calendar.getInstance();
		from.set(Calendar.YEAR, yearCurrent - 5);

		if (this.expirationFrom == null) {
			setExpirationFrom(from.getTime());
		}

		if (this.expirationUntil == null) {
			setExpirationUntil(now.getTime());
		}

		if (this.emisionFrom == null) {
			setEmisionFrom(from.getTime());
		}

		if (this.emisionUntil == null) {
			setEmisionUntil(now.getTime());
		}
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public Date getEmisionFrom() {
		return emisionFrom;
	}
	public void setEmisionFrom(Date emisionFrom) {
		this.emisionFrom = emisionFrom;
	}
	public Date getEmisionUntil() {
		return emisionUntil;
	}
	public void setEmisionUntil(Date emisionUntil) {
		this.emisionUntil = emisionUntil;
	}
	public Date getExpirationFrom() {
		return expirationFrom;
	}
	public void setExpirationFrom(Date expirationFrom) {
		this.expirationFrom = expirationFrom;
	}
	public Date getExpirationUntil() {
		return expirationUntil;
	}
	public void setExpirationUntil(Date expirationUntil) {
		this.expirationUntil = expirationUntil;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	@Override
	public String toString() {
		return "OverdueInfractionsSearchCriteria [identification="
				+ identification + ", name=" + name + ", licensePlate="
				+ licensePlate + ", emisionFrom=" + emisionFrom
				+ ", emisionUntil=" + emisionUntil + ", expirationFrom="
				+ expirationFrom + ", expirationUntil=" + expirationUntil
				+ ", ticket=" + ticket + ", article=" + article + "]";
	}
	
}
