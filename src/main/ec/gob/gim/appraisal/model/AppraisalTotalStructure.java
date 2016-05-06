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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.StructureMaterial;

/**
 * @author IML
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalTotalStructureGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalTotalStructure",
	 initialValue=1, allocationSize=1
)

public class AppraisalTotalStructure {

	@Id
	@GeneratedValue(generator="AppraisalTotalStructureGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private BigDecimal total;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private StructureMaterial structureMaterial;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "AppraisalTotalStructure_id")
	@OrderBy("id")
	private List<AppraisalItemStructure> appraisalItemsStructure;

	public AppraisalTotalStructure() {
		this.appraisalItemsStructure = new ArrayList<AppraisalItemStructure>();
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

	public StructureMaterial getStructureMaterial() {
		return structureMaterial;
	}

	public void setStructureMaterial(StructureMaterial structureMaterial) {
		this.structureMaterial = structureMaterial;
	}

	public List<AppraisalItemStructure> getAppraisalItemsStructure() {
		return appraisalItemsStructure;
	}

	public void setAppraisalItemsStructure(
			List<AppraisalItemStructure> appraisalItemsStructure) {
		this.appraisalItemsStructure = appraisalItemsStructure;
	}

	public void add(AppraisalItemStructure appraisalItemStructure){
		if (!this.appraisalItemsStructure.contains(appraisalItemStructure)){
			this.appraisalItemsStructure.add(appraisalItemStructure);
			appraisalItemStructure.setAppraisalTotalStructure(this);
		}
	}
	
	public void remove(AppraisalItemStructure appraisalItemStructure){
		boolean removed = this.appraisalItemsStructure.remove(appraisalItemStructure);
		if (removed) appraisalItemStructure.setAppraisalTotalStructure((AppraisalTotalStructure)null);
	}
	
}
