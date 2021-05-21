/**
 * 
 */
package ec.gob.gim.revenue.model.criteria;

/**
 * @author René
 *
 */
public class EmissionOrderSearchCriteria {

	private String resident;
	private String identificationNumber;
	private String department;
	private String entry;

	public String getResident() {
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	@Override
	public String toString() {
		return "EmissionOrderSearchCriteria [resident=" + resident
				+ ", identificationNumber=" + identificationNumber
				+ ", department=" + department + ", entry=" + entry + "]";
	}

}
