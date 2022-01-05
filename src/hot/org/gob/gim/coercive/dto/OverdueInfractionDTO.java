/**
 * 
 */
package org.gob.gim.coercive.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author René
 *
 */
@NativeQueryResultEntity
public class OverdueInfractionDTO {

	@NativeQueryResultColumn(index = 0)
	private Long id;

	@NativeQueryResultColumn(index = 1)
	private String identification;

	@NativeQueryResultColumn(index = 2)
	private String names;

	@NativeQueryResultColumn(index = 3)
	private String licensePlate;

	@NativeQueryResultColumn(index = 4)
	private String article;

	@NativeQueryResultColumn(index = 5)
	private Date emision;

	@NativeQueryResultColumn(index = 6)
	private Date expiration;

	@NativeQueryResultColumn(index = 7)
	private String ticket;
	
	@NativeQueryResultColumn(index = 8)
	private String articleDescription;
	
	@NativeQueryResultColumn(index = 9)
	private BigDecimal value;
	
	@NativeQueryResultColumn(index = 10)
	private BigDecimal interest;
	
	@NativeQueryResultColumn(index = 11)
	private BigDecimal totalValue;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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
	 * @return the names
	 */
	public String getNames() {
		return names;
	}

	/**
	 * @param names
	 *            the names to set
	 */
	public void setNames(String names) {
		this.names = names;
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

	/**
	 * @return the emision
	 */
	public Date getEmision() {
		return emision;
	}

	/**
	 * @param emision
	 *            the emision to set
	 */
	public void setEmision(Date emision) {
		this.emision = emision;
	}

	/**
	 * @return the expiration
	 */
	public Date getExpiration() {
		return expiration;
	}

	/**
	 * @param expiration
	 *            the expiration to set
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
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
	 * @return the articleDescription
	 */
	public String getArticleDescription() {
		return articleDescription;
	}

	/**
	 * @param articleDescription the articleDescription to set
	 */
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the interest
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * @param interest the interest to set
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * @return the totalValue
	 */
	public BigDecimal getTotalValue() {
		return totalValue;
	}

	/**
	 * @param totalValue the totalValue to set
	 */
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OverdueInfractionDTO [id=" + id + ", identification="
				+ identification + ", names=" + names + ", licensePlate="
				+ licensePlate + ", article=" + article + ", emision="
				+ emision + ", expiration=" + expiration + ", ticket=" + ticket
				+ ", articleDescription=" + articleDescription + ", value="
				+ value + ", interest=" + interest + ", totalValue="
				+ totalValue + "]";
	}

}
