package org.gob.loja.gim.ws.dto;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class ObligationsHistoryFotoMulta {

	private Long id;
	private String identificationNumber;
	private String name;
	private Long number;
	private Date emisiondate;
	private Date expirationdate;
	private BigDecimal value;
	private BigDecimal paidtotal;
	private String description;
	private String reference;
	private String groupingcode;
	private String numberPlate;
	private Long antNumber;
	private BigDecimal speeding;
	private Date citationDate;


	public ObligationsHistoryFotoMulta() {
	}

	public ObligationsHistoryFotoMulta(Long id, String identificationNumber,String name, Long number, Date emisiondate, Date expirationdate,
			BigDecimal value, BigDecimal paidtotal, String description,	String reference, String groupingcode,String numberPlate,Long antNumber,BigDecimal speeding,Date citationDate) {
		this.id = id;
		this.identificationNumber = identificationNumber;
		this.name = name;
		this.number = number;
		this.emisiondate = emisiondate;
		this.expirationdate = expirationdate;
		this.value = value;
		this.paidtotal = paidtotal;
		this.description = description;
		this.reference = reference;
		this.groupingcode = groupingcode;
		this.numberPlate = numberPlate;
		this.antNumber = antNumber;
		this.speeding = speeding;
		this.citationDate = citationDate;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Date getEmisiondate() {
		return emisiondate;
	}

	public void setEmisiondate(Date emisiondate) {
		this.emisiondate = emisiondate;
	}

	public Date getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(Date expirationdate) {
		this.expirationdate = expirationdate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getPaidtotal() {
		return paidtotal;
	}

	public void setPaidtotal(BigDecimal paidtotal) {
		this.paidtotal = paidtotal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getGroupingcode() {
		return groupingcode;
	}

	public void setGroupingcode(String groupingcode) {
		this.groupingcode = groupingcode;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public Long getAntNumber() {
		return antNumber;
	}

	public void setAntNumber(Long antNumber) {
		this.antNumber = antNumber;
	}

	public BigDecimal getSpeeding() {
		return speeding;
	}

	public void setSpeeding(BigDecimal speeding) {
		this.speeding = speeding;
	}

	public Date getCitationDate() {
		return citationDate;
	}

	public void setCitationDate(Date citationDate) {
		this.citationDate = citationDate;
	}

}
