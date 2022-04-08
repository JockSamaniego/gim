/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts.request;

import javax.validation.constraints.NotNull;

/**
 * @author René
 *
 */
public class PDFBondRequest {
	
	@NotNull(message = "bondId no puede ser nulo")
	private Long bondId;

	/**
	 * @return the bondId
	 */
	public Long getBondId() {
		return bondId;
	}

	/**
	 * @param bondId the bondId to set
	 */
	public void setBondId(Long bondId) {
		this.bondId = bondId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PDFBondRequest [bondId=" + bondId + "]";
	}
	

}
