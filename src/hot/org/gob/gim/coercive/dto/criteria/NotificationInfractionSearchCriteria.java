/**
 * 
 */
package org.gob.gim.coercive.dto.criteria;

/**
 * @author René
 *
 */
public class NotificationInfractionSearchCriteria {
	
	private String identification = null;
	
	private String number = null;
	
	/**
	 * 
	 */
	public NotificationInfractionSearchCriteria() {
		super();
		this.identification = null;
		this.number =  null;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
