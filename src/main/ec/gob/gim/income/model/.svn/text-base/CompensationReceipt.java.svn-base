package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.revenue.model.MunicipalBond;

@Audited
@Entity
@TableGenerator(name = "CompensationReceiptGenerator", 
				table = "IdentityGenerator", 
				pkColumnName = "name", 
				valueColumnName = "value", 
				pkColumnValue = "CompensationReceipt", 
				initialValue = 1, 
				allocationSize = 1)
public class CompensationReceipt {
	@Id
	@GeneratedValue(generator = "CompensationReceiptGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date serviceDate;
	
	@Column(length = 60)
	private String groupingCode;
	

	private BigDecimal interest;

	private BigDecimal residualInterest;
	
	@OneToOne
	@JoinColumn(name="receipt_id")
	private Receipt receipt; 
	
	private Boolean available;
	 
	private Boolean isPaid=false;
	 
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getGroupingCode() {
		return groupingCode;
	}

	public void setGroupingCode(String groupingCode) {
		this.groupingCode = groupingCode;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	public BigDecimal getResidualInterest() {
		return residualInterest;
	}

	public void setResidualInterest(BigDecimal residualInterest) {
		this.residualInterest = residualInterest;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	} 
	
}