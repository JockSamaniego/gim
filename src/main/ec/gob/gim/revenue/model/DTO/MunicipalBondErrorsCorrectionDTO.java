package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;


@NativeQueryResultEntity
public class MunicipalBondErrorsCorrectionDTO {
	
	@NativeQueryResultColumn(index = 0)
	private int number;
	
	@NativeQueryResultColumn(index = 1)
	private Date emisionDate;
	
	@NativeQueryResultColumn(index = 2)
	private String residentIdentification;
	
	@NativeQueryResultColumn(index = 3)
	private String residentName;
	
	@NativeQueryResultColumn(index = 4)
	private String entryName;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal paidTotal;
	
	@NativeQueryResultColumn(index = 6)
	private Date changeStatusDate;
	
	@NativeQueryResultColumn(index = 7)
	private String changeExplanation;
	
	@NativeQueryResultColumn(index = 8)
	private String userName;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getEmisionDate() {
		return emisionDate;
	}

	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

	public String getResidentIdentification() {
		return residentIdentification;
	}

	public void setResidentIdentification(String residentIdentification) {
		this.residentIdentification = residentIdentification;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public Date getChangeStatusDate() {
		return changeStatusDate;
	}

	public void setChangeStatusDate(Date changeStatusDate) {
		this.changeStatusDate = changeStatusDate;
	}

	public String getChangeExplanation() {
		return changeExplanation;
	}

	public void setChangeExplanation(String changeExplanation) {
		this.changeExplanation = changeExplanation;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
