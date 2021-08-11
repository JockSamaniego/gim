/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import java.util.List;

import org.gob.loja.gim.ws.dto.CommonResponseWS;
import org.gob.loja.gim.ws.dto.queries.LocalDTO;

/**
 * @author René
 *
 */
public class LocalsResponse extends CommonResponseWS {
	private List<LocalDTO> locals;

	/**
	 * @return the locals
	 */
	public List<LocalDTO> getLocals() {
		return locals;
	}

	/**
	 * @param locals
	 *            the locals to set
	 */
	public void setLocals(List<LocalDTO> locals) {
		this.locals = locals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalsResponse [locals=" + locals + "]";
	}

}
