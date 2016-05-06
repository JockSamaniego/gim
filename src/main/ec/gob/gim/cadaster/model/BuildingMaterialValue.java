package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FiscalPeriod;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:46
 */
@Audited
@Entity
@TableGenerator(
	 name="BuildingMaterialValueGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="BuildingMaterialValue",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="BuildingMaterialValue.findByStructureMaterial",
			query="select b from BuildingMaterialValue b where b.structureMaterial = :structureMaterial"
	),
	@NamedQuery(name="BuildingMaterialValue.findByStructureMaterialAndFiscalPeriod",
			query="select b from BuildingMaterialValue b where b.structureMaterial = :structureMaterial and b.fiscalPeriod.id = :fiscalPeriodId"
	)
})
public class BuildingMaterialValue {

	@Id
	@GeneratedValue(generator="BuildingMaterialValueGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private BigDecimal valueBySquareMeter;
	
	@ManyToOne
	private FiscalPeriod fiscalPeriod;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private StructureMaterial structureMaterial;

	public BuildingMaterialValue(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setValueBySquareMeter(BigDecimal valueBySquareMeter) {
		this.valueBySquareMeter = valueBySquareMeter;
	}

	public BigDecimal getValueBySquareMeter() {
		return valueBySquareMeter;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setStructureMaterial(StructureMaterial structureMaterial) {
		this.structureMaterial = structureMaterial;
	}

	public StructureMaterial getStructureMaterial() {
		return structureMaterial;
	}

	
}//end StreetMaterial