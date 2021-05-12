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

	private List<CheckPaidBondDTO> request = new ArrayList<CheckPaidBondDTO>();
	private List<CheckPaidDebtDTO> debts = new ArrayList<CheckPaidDebtDTO>();

	/**
	 * @return the request
	 */
	public List<CheckPaidBondDTO> getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(List<CheckPaidBondDTO> request) {
		this.request = request;
	}

	/**
	 * @return the debts
	 */
	public List<CheckPaidDebtDTO> getDebts() {
		return debts;
	}

	/**
	 * @param debts
	 *            the debts to set
	 */
	public void setDebts(List<CheckPaidDebtDTO> debts) {
		this.debts = debts;
	}
	
	
}
