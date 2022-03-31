/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts.response;

import java.util.ArrayList;
import java.util.List;

import org.gob.loja.gim.ws.dto.CommonResponseWS;
import org.gob.loja.gim.ws.dto.digitalReceipts.DepositDTO;
import org.gob.loja.gim.ws.dto.digitalReceipts.TaxpayerRecordDTO;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;

/**
 * @author René
 *
 */
public class BondResponse extends CommonResponseWS {
	
	private TaxpayerRecordDTO institution;
	
	private BondDTO bond;
	
	private List<DepositDTO> deposits = new ArrayList<DepositDTO>();
	
	private boolean branchMain;
	
	private String branchOfficeName;
	
	private String branchOfficeAddress;
	
	/**
	 * @return the bond
	 */
	public BondDTO getBond() {
		return bond;
	}

	/**
	 * @param bond the bond to set
	 */
	public void setBond(BondDTO bond) {
		this.bond = bond;
	}

	/**
	 * @return the deposits
	 */
	public List<DepositDTO> getDeposits() {
		return deposits;
	}

	/**
	 * @param deposits the deposits to set
	 */
	public void setDeposits(List<DepositDTO> deposits) {
		this.deposits = deposits;
	}
	
	/**
	 * @return the institution
	 */
	public TaxpayerRecordDTO getInstitution() {
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(TaxpayerRecordDTO institution) {
		this.institution = institution;
	}
	
	/**
	 * @return the branchMain
	 */
	public boolean isBranchMain() {
		return branchMain;
	}

	/**
	 * @param branchMain the branchMain to set
	 */
	public void setBranchMain(boolean branchMain) {
		this.branchMain = branchMain;
	}

	/**
	 * @return the branchOfficeName
	 */
	public String getBranchOfficeName() {
		return branchOfficeName;
	}

	/**
	 * @param branchOfficeName the branchOfficeName to set
	 */
	public void setBranchOfficeName(String branchOfficeName) {
		this.branchOfficeName = branchOfficeName;
	}
	
	/**
	 * @return the branchOfficeAddress
	 */
	public String getBranchOfficeAddress() {
		return branchOfficeAddress;
	}

	/**
	 * @param branchOfficeAddress the branchOfficeAddress to set
	 */
	public void setBranchOfficeAddress(String branchOfficeAddress) {
		this.branchOfficeAddress = branchOfficeAddress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BondResponse [bond=" + bond + ", deposits=" + deposits + "]";
	}
	
}
