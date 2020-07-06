package org.gob.loja.gim.ws.dto.ant;

/**
 * @author Rene
 *
 */
public class PreemissionInfractionRequest {
	
	private String accountCode;
	private InfractorDTO infractor;
	private InfractionDTO infraction;
	/**
	 * @return the accountCode
	 */
	public String getAccountCode() {
		return accountCode;
	}
	/**
	 * @param accountCode the accountCode to set
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	/**
	 * @return the infractor
	 */
	public InfractorDTO getInfractor() {
		return infractor;
	}
	/**
	 * @param infractor the infractor to set
	 */
	public void setInfractor(InfractorDTO infractor) {
		this.infractor = infractor;
	}
	/**
	 * @return the infraction
	 */
	public InfractionDTO getInfraction() {
		return infraction;
	}
	/**
	 * @param infraction the infraction to set
	 */
	public void setInfraction(InfractionDTO infraction) {
		this.infraction = infraction;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PreemissionInfractionRequest [accountCode=" + accountCode
				+ ", infractor=" + infractor + ", infraction=" + infraction
				+ "]";
	}
	
}
