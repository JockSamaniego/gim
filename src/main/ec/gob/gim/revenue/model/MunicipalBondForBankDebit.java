package ec.gob.gim.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.bankDebit.BankDebitForLiquidation;

@Audited
@Entity
@TableGenerator(name = "MunicipalBondForBankDebitGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "MunicipalBondForBankDebit", initialValue = 1, allocationSize = 1)
public class MunicipalBondForBankDebit implements Serializable{
	
	private static final long serialVersionUID = 18386387333339876L;
	
	@Id
	@GeneratedValue(generator = "MunicipalBondForBankDebitGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Version
	private Long version = 0L;
	
	private Long municipalBondId;
	
	private BigDecimal paidTotal;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Temporal(TemporalType.TIME)
	private Date creationTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bankdebit_id")
	private BankDebitForLiquidation bankDebitForLiquidation;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMunicipalBondId() {
		return municipalBondId;
	}

	public void setMunicipalBondId(Long municipalBondId) {
		this.municipalBondId = municipalBondId;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public BankDebitForLiquidation getBankDebitForLiquidation() {
		return bankDebitForLiquidation;
	}

	public void setBankDebitForLiquidation(
			BankDebitForLiquidation bankDebitForLiquidation) {
		this.bankDebitForLiquidation = bankDebitForLiquidation;
	}

}
