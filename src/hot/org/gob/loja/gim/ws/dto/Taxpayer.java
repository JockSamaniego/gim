package org.gob.loja.gim.ws.dto;

public class Taxpayer {
	private Long id;
	private String identificationNumber;
	private String firstName; // firstName en caso de Entidades Legales es el name
	private String lastName;
	private String email;
	private String street;
	private String phoneNumber;
	
	public Taxpayer() {
	}
	
	public Taxpayer(Long id, String identificationNumber, String name, String email) {
		this();
		this.id = id;
		this.identificationNumber = identificationNumber;
		this.firstName = name; 
		this.lastName = name; // Tambien se fija en para los EntityLegal
		this.email = email;
	}
	
	/**
	 * @Deprecated debido a que existen contribuyentes que no tienen registrada su direccion actual
	 * se reemplaza por el constructor 
	 * public Taxpayer(Long id, String identificationNumber, String name, 
			String email)
	 */
	@Deprecated
	public Taxpayer(Long id, String identificationNumber, String name, 
			String email, String street, String phoneNumber) {
		this();
		this.id = id;
		this.identificationNumber = identificationNumber;
		this.firstName = name;
		this.email = email;
		this.street = street;
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * @Deprecated debido a que existen contribuyentes que no tienen registrada su direccion actual
	 * se reemplaza por el constructor 
	 * public Taxpayer(Long id, String identificationNumber, String firstName, String lastName, 
			String email)
	 */
	//@Deprecated 2020-02-03 rfam se habilita, la informacion del contribuyente debe estar actulizada y tambien retornar lo q hay del contribuyente
	public Taxpayer(Long id, String identificationNumber, String firstName, String lastName, 
			String email, String street, String phoneNumber) {
		this();
		this.id = id;
		this.identificationNumber = identificationNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.street = street;
		this.phoneNumber = phoneNumber;
	}
	
	public Taxpayer(Long id, String identificationNumber, String firstName, String lastName, 
			String email) {
		this();
		this.id = id;
		this.identificationNumber = identificationNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
		
}
