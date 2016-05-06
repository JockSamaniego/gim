package ec.gob.gim.coercive.model;

import java.math.BigDecimal;
import java.util.Date;

public class DuePortfolioDTO {
	
	private Long  bondNumber;
	
	private String  department;
	
	private String  resident;
	
	private String  identificationNumber;
	
	private String address;

	private String entry;
	
	private String description;
	
	private Date emisionDate;
	
	private Date expirationDate;
	
	private BigDecimal value;
	
	public DuePortfolioDTO (){
		
	}
	
	public DuePortfolioDTO (Long bondNumber, String	department, String resident, String identificationNumber, String address,
							String entry, String description, Date emisionDate, Date expirationDate, BigDecimal value){
		this.bondNumber = bondNumber;		
		this.department = department;		
		this.resident = resident;		
		this.identificationNumber = identificationNumber;		
		this.address = address;
		this.entry = entry;		
		this.description = description;		
		this.emisionDate = emisionDate;		
		this.expirationDate = expirationDate;		
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String aux= (this.bondNumber == null ? "" : this.bondNumber) + ";" + 
				(this.department == null ? "" : this.department) + ";" + 
				(this.resident == null ? "" : this.resident) + ";" + 
				(this.identificationNumber == null ? "" : this.identificationNumber) + ";" + 
				(this.address == null ? "" : this.address) + ";" + 
				(this.entry == null ? "" : this.entry) + ";" + 
				(this.description == null ? "" : this.description) + ";" + 
				(this.emisionDate == null ? "" : this.emisionDate) + ";" + 
				(this.expirationDate == null ? "" : this.expirationDate) + ";" + 
				(this.value == null ? "" : this.value);
//		El caracter * reemplaza a las comas (,) para luego diferenciar en el toString de 
//		List<DuePortfolioDTO> que se colocan para separar los elementos
		aux = aux.replace(',', '*');
		return aux;		
	}

	public Long getBondNumber() {
		return bondNumber;
	}

	public void setBondNumber(Long bondNumber) {
		this.bondNumber = bondNumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEmisionDate() {
		return emisionDate;
	}

	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}	
}