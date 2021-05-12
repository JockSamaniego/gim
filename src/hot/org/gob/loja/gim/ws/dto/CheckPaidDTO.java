/**
 * 
 */
package org.gob.loja.gim.ws.dto;
import java.util.List;
import java.util.ArrayList;

/**
 * @author hack
 *
 */
public class CheckPaidDTO {

	private List<CheckPaidByEntryDTO> request = new ArrayList<CheckPaidByEntryDTO>();
	private List<CheckPaidByEntryDTO> debts = new ArrayList<CheckPaidByEntryDTO>();
		
	/**
	 * @return the debts
	 */
	public List<CheckPaidByEntryDTO> getDebts() {
		return debts;
	}

	/**
	 * @param debts
	 *            the debts to set
	 */
	public void setDebts(List<CheckPaidByEntryDTO> debts) {
		this.debts = debts;
	}

	/**
	 * @return the request
	 */
	public List<CheckPaidByEntryDTO> getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(List<CheckPaidByEntryDTO> request) {
		this.request = request;
	}
	
	
	
	
	
	
}
