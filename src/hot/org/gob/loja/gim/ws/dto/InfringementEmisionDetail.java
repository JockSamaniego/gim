package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.Date;

public class InfringementEmisionDetail {

	private String citationNumber;
	private String infringementType;
	private Date infringementDate;
	private String infringementaddress;
	private String numberPlate;
	private String explanation;
	private String reference;
	private String entry;
	private Integer lostPoints;
	private String transitAgentIdentification;
	private String origin;
	private String type;
	
	private BigDecimal amount;
	private BigDecimal value;
	
	private Long municipalBondNumber;
	private Date expirationDate;
	
	private String infractorIdentification;
	private String accountCode;

	public String getCitationNumber() {
		return citationNumber;
	}

	public void setCitationNumber(String citationNumber) {
		this.citationNumber = citationNumber;
	}
	
	public String getInfringementType() {
		return infringementType;
	}

	public void setInfringementType(String infringementType) {
		this.infringementType = infringementType;
	}

	public Date getInfringementDate() {
		return infringementDate;
	}

	public void setInfringementDate(Date infringementDate) {
		this.infringementDate = infringementDate;
	}

	public String getInfringementaddress() {
		return infringementaddress;
	}

	public void setInfringementaddress(String infringementaddress) {
		this.infringementaddress = infringementaddress;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Integer getLostPoints() {
		return lostPoints;
	}

	public void setLostPoints(Integer lostPoints) {
		this.lostPoints = lostPoints;
	}

	public String getTransitAgentIdentification() {
		return transitAgentIdentification;
	}

	public void setTransitAgentIdentification(String transitAgentIdentification) {
		this.transitAgentIdentification = transitAgentIdentification;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Long getMunicipalBondNumber() {
		return municipalBondNumber;
	}

	public void setMunicipalBondNumber(Long municipalBondNumber) {
		this.municipalBondNumber = municipalBondNumber;
	}

	public String getInfractorIdentification() {
		return infractorIdentification;
	}

	public void setInfractorIdentification(String infractorIdentification) {
		this.infractorIdentification = infractorIdentification;
	}
	
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

		
}
