package ec.gob.gim.appraisal.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToOne;

import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.PreservationState;

/**
 * @author jock
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalTotalStructureConditionGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalTotalStructureCondition",
	 initialValue=1, allocationSize=1
)

public class AppraisalTotalStructureCondition {

	@Id
	@GeneratedValue(generator="AppraisalTotalStructureConditionGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private BigDecimal coefficient;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private PreservationState preservationState;
	
	@ManyToOne
	private AppraisalPeriod appraisalPeriod;
	
	public AppraisalTotalStructureCondition() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PreservationState getPreservationState() {
		return preservationState;
	}

	public void setPreservationState(PreservationState preservationState) {
		this.preservationState = preservationState;
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

	
}
