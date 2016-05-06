package ec.gob.gim.complementvoucher.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;

/**
 * @author manuel
 * @version 1.0
 * @created 17-abril-2015 12:32:33
 */
public class DetailReport {

	
	private String retentionTax;
	
		
	private BigDecimal retentionPercent;

	private BigDecimal total;
	
	private List<ElectronicVoucherDetail> details;
	
	

	public DetailReport() {
		details  = new ArrayList<ElectronicVoucherDetail>();
	}

	public String getRetentionTax() {
		return retentionTax;
	}

	public void setRetentionTax(String retentionTax) {
		this.retentionTax = retentionTax;
	}

	public BigDecimal getRetentionPercent() {
		return retentionPercent;
	}

	public void setRetentionPercent(BigDecimal retentionPercent) {
		this.retentionPercent = retentionPercent;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<ElectronicVoucherDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ElectronicVoucherDetail> details) {
		this.details = details;
	}
	
}// end ComplementVoucher