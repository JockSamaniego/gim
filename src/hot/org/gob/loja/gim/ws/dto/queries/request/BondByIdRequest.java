/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.request;

import javax.validation.constraints.NotNull;

/**
 * @author René
 *
 */
public class BondByIdRequest {

	@NotNull(message = "bondId no puede ser nulo")
	private Long bondId;

	public Long getBondId() {
		return bondId;
	}

	public void setBondId(Long bondId) {
		this.bondId = bondId;
	}

	@Override
	public String toString() {
		return "BondByIdRequest [bondId=" + bondId + "]";
	}

}
