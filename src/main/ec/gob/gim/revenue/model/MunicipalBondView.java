package ec.gob.gim.revenue.model;

import java.math.BigDecimal;
import java.util.Date;


public class MunicipalBondView {
	
	private String entry;
	private String description;
	private Long id;
	private String resident;
	private String identification;
	private Long municipalBondNumber;
	private Long entryId;
	private String address;
	private BigDecimal value;
	private Date date;
	private Date time;
	
	private BigDecimal total;
	
	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	public Long getMunicipalBondNumber() {
		return municipalBondNumber;
	}

	public void setMunicipalBondNumber(Long municipalBondNumber) {
		this.municipalBondNumber = municipalBondNumber;
	}
	
	public MunicipalBondView(Long id,String resident, String description){
		this.id = id;
		this.resident = resident;		
		this.description = description;
	}
	
	public MunicipalBondView(Long id,String resident, String description,BigDecimal value, BigDecimal total){
		this.id = id;
		this.resident = resident;		
		this.description = description;
		this.value=value;
		this.total=total;
	}
	
	public MunicipalBondView(String identification,String entry,String resident,Long municipalBondNumber,Date date, Date time, BigDecimal value){
		this.identification = identification;
		this.entry = entry;
		this.resident = resident;
		this.municipalBondNumber = municipalBondNumber;
		this.date = date;
		this.time = time;
		this.value = value;
	}
	
	public MunicipalBondView(Long entryId,String entry,String resident,Long municipalBondNumber,	String address, BigDecimal value){
		this.entryId = entryId;
		this.entry = entry;
		this.resident = resident;
		this.municipalBondNumber = municipalBondNumber;
		this.address = address;
		this.value = value;
	}
	
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	public String getResident() {
		return resident;
	}
	public void setResident(String resident) {
		this.resident = resident;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
