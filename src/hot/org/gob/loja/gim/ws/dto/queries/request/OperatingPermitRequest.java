/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.request;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;

/**
 * @author René
 *
 */
public class OperatingPermitRequest {

	@NotNull(message = "identification no puede ser nulo")
	@NotEmpty(message = "identification no puede ser vacío")
	private String identification;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OperatingPermitRequest [identification=" + identification + "]";
	}

}
