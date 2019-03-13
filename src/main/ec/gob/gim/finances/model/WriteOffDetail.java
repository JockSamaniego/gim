package ec.gob.gim.finances.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.waterservice.model.MonthType;

@Audited
@Entity
@TableGenerator(name = "WriteOffDetailGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WriteOffDetail", initialValue = 1, allocationSize = 1)
public class WriteOffDetail {

	@Id
	@GeneratedValue(generator = "WriteOffDetailGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private Integer year;
	
	private Integer month;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private MonthType monthType;

	private BigDecimal oldAmount;
	
	private Long oldCurrentReading;
	
	private Long oldPreviousReading;

	private BigDecimal newAmount;
	
	private Long newCurrentReading;
	
	private Long newPreviousReading;
	
	private Boolean isActive = Boolean.TRUE;

	@ManyToOne
	@JoinColumn(name = "oldmb_id")
	private MunicipalBond oldMunicipalBond;

	@ManyToOne
	@JoinColumn(name = "newmb_id")
	private MunicipalBond newMunicipalBond;

	@ManyToOne
	@JoinColumn(name = "writeoffrequest_id")
	private WriteOffRequest writeOffRequest;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public MonthType getMonthType() {
		return monthType;
	}

	public void setMonthType(MonthType monthType) {
		this.monthType = monthType;
	}

	public BigDecimal getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(BigDecimal oldAmount) {
		this.oldAmount = oldAmount;
	}

	public Long getOldCurrentReading() {
		return oldCurrentReading;
	}

	public void setOldCurrentReading(Long oldCurrentReading) {
		this.oldCurrentReading = oldCurrentReading;
	}

	public Long getOldPreviousReading() {
		return oldPreviousReading;
	}

	public void setOldPreviousReading(Long oldPreviousReading) {
		this.oldPreviousReading = oldPreviousReading;
	}

	public BigDecimal getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(BigDecimal newAmount) {
		this.newAmount = newAmount;
	}

	public Long getNewCurrentReading() {
		return newCurrentReading;
	}

	public void setNewCurrentReading(Long newCurrentReading) {
		this.newCurrentReading = newCurrentReading;
	}

	public Long getNewPreviousReading() {
		return newPreviousReading;
	}

	public void setNewPreviousReading(Long newPreviousReading) {
		this.newPreviousReading = newPreviousReading;
	}

	public MunicipalBond getOldMunicipalBond() {
		return oldMunicipalBond;
	}

	public void setOldMunicipalBond(MunicipalBond oldMunicipalBond) {
		this.oldMunicipalBond = oldMunicipalBond;
	}

	public MunicipalBond getNewMunicipalBond() {
		return newMunicipalBond;
	}

	public void setNewMunicipalBond(MunicipalBond newMunicipalBond) {
		this.newMunicipalBond = newMunicipalBond;
	}

	public WriteOffRequest getWriteOffRequest() {
		return writeOffRequest;
	}

	public void setWriteOffRequest(WriteOffRequest writeOffRequest) {
		this.writeOffRequest = writeOffRequest;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "WriteOffDetail [id=" + id + ", year=" + year + ", month="
				+ month + ", monthType=" + monthType + ", oldAmount="
				+ oldAmount + ", oldCurrentReading=" + oldCurrentReading
				+ ", oldPreviousReading=" + oldPreviousReading + ", newAmount="
				+ newAmount + ", newCurrentReading=" + newCurrentReading
				+ ", newPreviousReading=" + newPreviousReading
				+ ", oldMunicipalBond=" + oldMunicipalBond
				+ ", newMunicipalBond=" + newMunicipalBond
				+ ", writeOffRequest=" + writeOffRequest + "]";
	}
	
}