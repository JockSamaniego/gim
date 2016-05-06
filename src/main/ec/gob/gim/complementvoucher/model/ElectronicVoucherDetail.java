package ec.gob.gim.complementvoucher.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;

/**
 * @author manuel
 * @version 1.0
 * @created 17-abril-2015 12:32:33
 */
@Audited
@Entity
@TableGenerator(name = "ElectronicVoucherDetailGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ElectronicVoucherDetail", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "ElectronicVoucherDetail.findAll", query = "SELECT evd FROM ElectronicVoucherDetail evd") })
public class ElectronicVoucherDetail {

	@Id
	@GeneratedValue(generator = "ElectronicVoucherDetailGenerator", strategy = GenerationType.TABLE)
	private Long id;


	@Column(nullable = false)
	private BigDecimal taxableBase;

	
	@Column(nullable = false)
	private BigDecimal taxesTotal;
	
	@Enumerated(EnumType.STRING)
	private RetentionTax retentionTax;
	
	private BigDecimal retentionPercent;
		
	@ManyToOne
	private RetentionCode retentionCode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private ElectronicVoucher electronicVoucher;
	
	@Transient
	private String suggestion;
		
	
	public ElectronicVoucherDetail() {
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTaxableBase() {
		return taxableBase;
	}

	public void setTaxableBase(BigDecimal  taxableBase) {
		this.taxableBase = taxableBase;
	}
 

	public BigDecimal getTaxesTotal() {
		return taxesTotal;
	}

	public void setTaxesTotal(BigDecimal taxesTotal) {
		this.taxesTotal = taxesTotal;
	}

	public RetentionTax getRetentionTax() {
		return retentionTax;
	}

	public void setRetentionTax(RetentionTax retentionTax) {
		this.retentionTax = retentionTax;
	}

	public RetentionCode getRetentionCode() {
		return retentionCode;
	}

	public void setRetentionCode(RetentionCode retentionCode) {
		this.retentionCode = retentionCode;
	}

	public ElectronicVoucher getElectronicVoucher() {
		return electronicVoucher;
	}

	public void setElectronicVoucher(ElectronicVoucher electronicVoucher) {
		this.electronicVoucher = electronicVoucher;
	}



	public BigDecimal getRetentionPercent() {
		return retentionPercent;
	}

	public void setRetentionPercent(BigDecimal retentionPercent) {
		this.retentionPercent = retentionPercent;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
}// end ComplementVoucher