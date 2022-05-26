/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import ec.gob.gim.common.model.Resident;

/**
 * @author René
 *
 */
public class ResidentDTO {

	private Long id;

	private String identificationType;

	protected String identificationNumber;

	private String name;

	private String email;

	private String street;

	public ResidentDTO(Resident resident) {
		this.email = resident.getEmail();
		this.id = resident.getId();
		this.identificationNumber = resident.getIdentificationNumber();
		this.identificationType = resident.getIdentificationType().name();
		this.name = resident.getName();
		this.street = resident.getCurrentAddress() == null ? "" : resident.getCurrentAddress().getStreet();
	}

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
	 * @return the identificationType
	 */
	public String getIdentificationType() {
		return identificationType;
	}

	/**
	 * @param identificationType
	 *            the identificationType to set
	 */
	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber
	 *            the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResidentDTO [id=" + id + ", identificationType="
				+ identificationType + ", identificationNumber="
				+ identificationNumber + ", name=" + name + ", email=" + email
				+ ", street=" + street + "]";
	}

}
