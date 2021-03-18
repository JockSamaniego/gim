package org.gob.loja.gim.ws.dto.preemission;

import javax.validation.constraints.NotNull;
import org.gob.gim.common.validators.NotEmpty;;

public class PreemitAdministrativeServicesRequest {

	@NotNull(message="emiterIdentification no puede ser nulo")
	@NotEmpty(message="emiterIdentification no puede ser vacío")
	private String emiterIdentification;

	public String getEmiterIdentification() {
		return emiterIdentification;
	}

	public void setEmiterIdentification(String emiterIdentification) {
		this.emiterIdentification = emiterIdentification;
	}

	@Override
	public String toString() {
		return "PreemitAdministrativeServicesRequest [emiterIdentification="
				+ emiterIdentification + "]";
	}

}
