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
	
	private Boolean nullified;
	
	private Boolean inconsistent;
	
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

	public Boolean getNullified() {
		return nullified;
	}

	public void setNullified(Boolean nullified) {
		this.nullified = nullified;
	}

	public Boolean getInconsistent() {
		return inconsistent;
	}

	public void setInconsistent(Boolean inconsistent) {
		this.inconsistent = inconsistent;
	}
	
}