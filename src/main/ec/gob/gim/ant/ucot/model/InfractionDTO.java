/**
 * 
 */
package ec.gob.gim.ant.ucot.model;

/**
 * @author jock
 *
 */
public class InfractionDTO {

	private String serial;
	
	private Boolean delivered;
	
	public InfractionDTO() {
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Boolean getDelivered() {
		return delivered;
	}

	public void setDelivered(Boolean delivered) {
		this.delivered = delivered;
	}
	
}