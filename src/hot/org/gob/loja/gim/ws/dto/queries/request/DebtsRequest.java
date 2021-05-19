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
public class DebtsRequest {

	@NotNull(message = "identification no puede ser nulo")
	@NotEmpty(message = "identification no puede ser vacío")
	private String identification;

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@Override
	public String toString() {
		return "DebtsRequest [identification=" + identification + "]";
	}

}
