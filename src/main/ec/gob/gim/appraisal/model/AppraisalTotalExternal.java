package ec.gob.gim.appraisal.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.ExternalFinishing;

/**
 * @author IML
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalTotalExternalGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalTotalExternal",
	 initialValue=1, allocationSize=1
)

public class AppraisalTotalExternal {

	@Id
	@GeneratedValue(generator="AppraisalTotalExternalGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private BigDecimal total;
	
	private BigDecimal coefficient;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private ExternalFinishing externalFinishing;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalTotalExternal_id")
	@OrderBy("id")
	private List<AppraisalItemExternal> appraisalItemsExternal;

	@ManyToOne
	private AppraisalPeriod appraisalPeriod;
	
	public AppraisalTotalExternal() {
		this.appraisalItemsExternal = new ArrayList<AppraisalItemExternal>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public ExternalFinishing getExternalFinishing() {
		return externalFinishing;
	}

	public void setExternalFinishing(ExternalFinishing externalFinishing) {
		this.externalFinishing = externalFinishing;
	}

	public List<AppraisalItemExternal> getAppraisalItemsExternal() {
		return appraisalItemsExternal;
	}

	public void setAppraisalItemExternal(
			List<AppraisalItemExternal> appraisalItemsExternal) {
		this.appraisalItemsExternal = appraisalItemsExternal;
	}

	public AppraisalPeriod getAppraisalPeriod() {
		return appraisalPeriod;
	}

	public void setAppraisalPeriod(AppraisalPeriod appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

	public BigDecimal getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(BigDecimal coefficient) {
		this.coefficient = coefficient;
	}

	public void add(AppraisalItemExternal appraisalItemExternal){
		if (!this.appraisalItemsExternal.contains(appraisalItemExternal)){
			this.appraisalItemsExternal.add(appraisalItemExternal);
			appraisalItemExternal.setAppraisalTotalExternal(this);
		}
	}
	
	public void remove(AppraisalItemExternal appraisalItemExternal){
		boolean removed = this.appraisalItemsExternal.remove(appraisalItemExternal);
		if (removed) appraisalItemExternal.setAppraisalTotalExternal((AppraisalTotalExternal)null);
	}
	
}
