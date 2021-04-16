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
public class EntryRequest {

	@NotNull(message = "code no puede ser nulo")
	@NotEmpty(message = "code no puede ser vacío")
	private String code;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EntryRequest [code=" + code + "]";
	}

}
