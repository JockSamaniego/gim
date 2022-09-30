package ec.gob.gim.appraisal.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import org.hibernate.envers.Audited;
import ec.gob.gim.cadaster.model.Equipments;

/**
 * @author jock
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalTotalEquipmentGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalTotalEquipment",
	 initialValue=1, allocationSize=1
)

public class AppraisalTotalEquipment {

	@Id
	@GeneratedValue(generator="AppraisalTotalEquipmentGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private BigDecimal total;
	
	@ManyToOne
	@JoinColumn(name="equipment_id")
	private Equipments equipments;
	

	@ManyToOne
	private AppraisalPeriod appraisalPeriod;
	
	public AppraisalTotalEquipment() {

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

	public AppraisalPeriod getAppraisalPeriod() {
		return appraisalPeriod;
	}

	public void setAppraisalPeriod(AppraisalPeriod appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

	public Equipments getEquipments() {
		return equipments;
	}

	public void setEquipments(Equipments equipments) {
		this.equipments = equipments;
	}
	
}
