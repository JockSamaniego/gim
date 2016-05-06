package org.gob.loja.gim.ws.dto;

/**
 * Solicitud de un servicio para un contribuyente especifico
 */
public class ServiceRequest {
	/**
	 * username Nombre de usuario de la entidad financiera
	 */
	private String username;
	/**
	 * Contraseña de la entidad financiera o del cliente que solicita un
	 * servicio
	 */
	private String password;

	/**
	 * Identificacion del contribuyente: Cedula, RUC o Numero pasaporte
	 */
	private String identificationNumber;
	
	/**
	 * Numero de trámite
	 */
	private String tramitId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getTramitId() {
		return tramitId;
	}

	public void setTramitId(String tramitId) {
		this.tramitId = tramitId;
	}
}
