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

import ec.gob.gim.cadaster.model.RoofMaterial;

/**
 * @author IML
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalTotalRoofGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalTotalRoof",
	 initialValue=1, allocationSize=1
)

public class AppraisalTotalRoof {

	@Id
	@GeneratedValue(generator="AppraisalTotalRoofGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private BigDecimal total;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private RoofMaterial roofMaterial;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalTotalRoof_id")
	@OrderBy("id")
	private List<AppraisalItemRoof> appraisalItemsRoof;

	@ManyToOne
	private AppraisalPeriod appraisalPeriod;
	
	public AppraisalTotalRoof() {
		this.appraisalItemsRoof = new ArrayList<AppraisalItemRoof>();
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

	public RoofMaterial getRoofMaterial() {
		return roofMaterial;
	}

	public void setRoofMaterial(RoofMaterial roofMaterial) {
		this.roofMaterial = roofMaterial;
	}

	public List<AppraisalItemRoof> getAppraisalItemsRoof() {
		return appraisalItemsRoof;
	}

	public void setAppraisalItemsRoof(List<AppraisalItemRoof> appraisalItemsRoof) {
		this.appraisalItemsRoof = appraisalItemsRoof;
	}

	public AppraisalPeriod getAppraisalPeriod() {
		return appraisalPeriod;
	}

	public void setAppraisalPeriod(AppraisalPeriod appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

	public void add(AppraisalItemRoof appraisalItemRoof){
		if (!this.appraisalItemsRoof.contains(appraisalItemRoof)){
			this.appraisalItemsRoof.add(appraisalItemRoof);
			appraisalItemRoof.setAppraisalTotalRoof(this);
		}
	}
	
	public void remove(AppraisalItemRoof appraisalItemRoof){
		boolean removed = this.appraisalItemsRoof.remove(appraisalItemRoof);
		if (removed) appraisalItemRoof.setAppraisalTotalRoof((AppraisalTotalRoof)null);
	}
	
}
