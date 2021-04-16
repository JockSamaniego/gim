/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import java.util.List;

import org.gob.loja.gim.ws.dto.CommonResponseWS;
import org.gob.loja.gim.ws.dto.queries.OperatingPermitDTO;

/**
 * @author René
 *
 */
public class OperatingPermitResponse extends CommonResponseWS {
	private List<OperatingPermitDTO> permits;

	/**
	 * @return the permits
	 */
	public List<OperatingPermitDTO> getPermits() {
		return permits;
	}

	/**
	 * @param permits
	 *            the permits to set
	 */
	public void setPermits(List<OperatingPermitDTO> permits) {
		this.permits = permits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OperatingPermitResponse [permits=" + permits + "]";
	}

}
