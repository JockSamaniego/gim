/**
 * 
 */
package org.gob.gim.coercive.dto;

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
	 * @return the identification
	 */
	public String getIdentification() {
		return identification;
	}

	/**
	 * @param identification
	 *            the identification to set
	 */
	public void setIdentification(String identification) {
		this.identification = identification;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the licensePlate
	 */
	public String getLicensePlate() {
		return licensePlate;
	}

	/**
	 * @param licensePlate
	 *            the licensePlate to set
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	/**
	 * @return the emisionFrom
	 */
	public Date getEmisionFrom() {
		return emisionFrom;
	}

	/**
	 * @param emisionFrom
	 *            the emisionFrom to set
	 */
	public void setEmisionFrom(Date emisionFrom) {
		this.emisionFrom = emisionFrom;
	}

	/**
	 * @return the emisionUntil
	 */
	public Date getEmisionUntil() {
		return emisionUntil;
	}

	/**
	 * @param emisionUntil
	 *            the emisionUntil to set
	 */
	public void setEmisionUntil(Date emisionUntil) {
		this.emisionUntil = emisionUntil;
	}

	/**
	 * @return the expirationFrom
	 */
	public Date getExpirationFrom() {
		return expirationFrom;
	}

	/**
	 * @param expirationFrom
	 *            the expirationFrom to set
	 */
	public void setExpirationFrom(Date expirationFrom) {
		this.expirationFrom = expirationFrom;
	}

	/**
	 * @return the expirationUntil
	 */
	public Date getExpirationUntil() {
		return expirationUntil;
	}

	/**
	 * @param expirationUntil
	 *            the expirationUntil to set
	 */
	public void setExpirationUntil(Date expirationUntil) {
		this.expirationUntil = expirationUntil;
	}

	/**
	 * @return the ticket
	 */
	public String getTicket() {
		return ticket;
	}

	/**
	 * @param ticket
	 *            the ticket to set
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	/**
	 * @return the article
	 */
	public String getArticle() {
		return article;
	}

	/**
	 * @param article
	 *            the article to set
	 */
	public void setArticle(String article) {
		this.article = article;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
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
