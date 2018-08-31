package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class BondSummary {

	private Long id;
	private Long number;
	private String account;
	private String serviceCode;
	private Date emisionDate;
	private Date serviceDate;
	private Date expirationDate;
	private BigDecimal value;

	private String reversedResolution;

	@Temporal(TemporalType.DATE)
	private Date reversedDate;

	public BondSummary(Long id, Long number, String account, String serviceCode, Date emisionDate, Date serviceDate,
			Date expirationDate, BigDecimal value, String reversedResolution, Date reversedDate) {
		super();
		this.id = id;
		this.number = number;
		this.account = account;
		this.serviceCode = serviceCode;
		this.emisionDate = emisionDate;
		this.serviceDate = serviceDate;
		this.expirationDate = expirationDate;
		this.value = value;
		this.reversedResolution = reversedResolution;
		this.reversedDate = reversedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getReversedResolution() {
		return reversedResolution;
	}

	public void setReversedResolution(String reversedResolution) {
		this.reversedResolution = reversedResolution;
	}

	public Date getReversedDate() {
		return reversedDate;
	}

	public void setReversedDate(Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	public Date getEmisionDate() {
		return emisionDate;
	}

	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

}
