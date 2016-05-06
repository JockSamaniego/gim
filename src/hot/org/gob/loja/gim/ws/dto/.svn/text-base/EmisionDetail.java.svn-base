package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.Date;

public class EmisionDetail {

	private String numberPlate;
	private String reference;
	private String description;
	private String address;
	/**
	 * numero generado por empresa
	 */
	private Long antNumber;
	/**
	 * exceso de velocidad
	 */
	private BigDecimal speeding;
	/**
	 * tipo liviano o pesado
	 */
	private String vehicleType;

	/**
	 * publico o privado
	 */
	private String serviceType;
	
	/**
	 * yyyy-MM-dd
	 */
	private Date citationDate; 

	private BigDecimal amount;
	private BigDecimal total;

	public EmisionDetail() {

	}

	/*
	 * public EmisionDetail(String numberPlate, String reference, String
	 * description, String address, BigDecimal amount, BigDecimal total) {
	 * super(); this.numberPlate = numberPlate; this.reference = reference;
	 * this.description = description; this.address = address; this.amount =
	 * amount; this.total = total; }
	 */

	public EmisionDetail(String numberPlate, String reference, String description, String address, Long antNumber,
			BigDecimal speeding, BigDecimal amount, BigDecimal total) {
		super();
		this.numberPlate = numberPlate;
		this.reference = reference;
		this.description = description;
		this.address = address;
		this.antNumber = antNumber;
		this.speeding = speeding;
		this.amount = amount;
		this.total = total;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Date getCitationDate() {
		return citationDate;
	}

	public void setCitationDate(Date citationDate) {
		this.citationDate = citationDate;
	}
	
}
