package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

 
@NativeQueryResultEntity	
public class EmitterReportDTO{

//	mb.emisiondate, mb."number", mb.servicedate, mbs.name, re.identificationnumber, 
//	re.name, vfr.infringementdate, vfr.notificationnumber, vfr.numberplate,
//	emi.name, emi.identificationnumber, ent.name, mb.value, mb.paidtotal
	
	@NativeQueryResultColumn(index = 0)
	private Date emisionDate;
	@NativeQueryResultColumn(index = 1)
	private Long number;
	@NativeQueryResultColumn(index = 2)
	private Date serviceDate;
	@NativeQueryResultColumn(index = 3)
	private String status;
	@NativeQueryResultColumn(index = 4)
	private String identificationNumber;
	@NativeQueryResultColumn(index = 5)
	private String name;
	@NativeQueryResultColumn(index = 6)
	private Date infringementDate;
	@NativeQueryResultColumn(index = 7)
	private String notificationNumber;
	@NativeQueryResultColumn(index = 8)
	private String numberPlate;
	@NativeQueryResultColumn(index = 9)
	private String emitter;
	@NativeQueryResultColumn(index = 10)
	private String emitterNumber;
	@NativeQueryResultColumn(index = 11)
	private String entryName;	
	@NativeQueryResultColumn(index = 12)
	private BigDecimal value;
	@NativeQueryResultColumn(index = 13)
	private BigDecimal paidTotal;
	 

	public Date getEmisionDate() {
		return emisionDate;
	}

	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getInfringementDate() {
		return infringementDate;
	}

	public void setInfringementDate(Date infringementDate) {
		this.infringementDate = infringementDate;
	}

	public String getNotificationNumber() {
		return notificationNumber;
	}

	public void setNotificationNumber(String notificationNumber) {
		this.notificationNumber = notificationNumber;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getEmitter() {
		return emitter;
	}

	public void setEmitter(String emitter) {
		this.emitter = emitter;
	}

	public String getEmitterNumber() {
		return emitterNumber;
	}

	public void setEmitterNumber(String emitterNumber) {
		this.emitterNumber = emitterNumber;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}	
}