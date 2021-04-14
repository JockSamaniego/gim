package org.gob.loja.gim.ws.dto;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonResponseWS {

	private List<String> errors = new ArrayList<String>();
	private String message;

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "GenericResponseWS [errors=" + errors + ", message=" + message
				+ "]";
	}
	
}
