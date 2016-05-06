package ec.gob.gim.cadaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FiscalPeriod;


@Audited
@Entity
@TableGenerator(name="UnbuiltLotGenerator",
				pkColumnName="name",
				pkColumnValue="UnbuiltLot",
				table="IdentityGenerator",
				valueColumnName="value",
				allocationSize = 1, initialValue = 1)
@NamedQueries({
	@NamedQuery(name = "UnbuiltLot.findByFiscalPeriodAndProperty", 
			query = "select u from UnbuiltLot u " +
					"where u.fiscalPeriod.id =:fiscalPeriodId and u.property.id = :propertyId"),
	@NamedQuery(name = "UnbuiltLot.findByFiscalPeriod", 
			query = "select u from UnbuiltLot u " +
					"where u.fiscalPeriod.id =:fiscalPeriodId"),
	
	@NamedQuery(name = "UnbuiltLot.findByFiscalPeriodAndMinAppraisal", 
			query = "select u from UnbuiltLot u "
					+ "left join fetch u.fiscalPeriod fp "
					+ "left join fetch u.property property "
					+ "left join fetch property.currentDomain currentDomain "
					+ "left join fetch currentDomain.notarysProvince "		
					+ "left join fetch currentDomain.notarysCity "
					+ "left join fetch currentDomain.purchaseType purchaseType "
					+ "left join fetch property.streetMaterial sm "		
					+ "left join fetch property.location l "
					+ "left join fetch l.neighborhood nb "
					+ "left join fetch nb.place place "
					+ "left JOIN fetch l.mainBlockLimit bl " 
					+ "left join fetch bl.sidewalkMaterial swm "
					+ "left join fetch bl.street street "
					+ "left join fetch bl.streetMaterial streetMaterial "
					+ "left join fetch bl.streetType streetType "		
					+ "left join fetch property.lotPosition lp "
					+ "left join fetch property.fenceMaterial fm "
					+ "left join fetch property.block block "
					+ "left join fetch block.sector sector "
					+ "left join fetch sector.territorialDivisionType "
					+ "left join fetch property.propertyType pt "
					+ "left join fetch pt.entry entry "
					+ "left join fetch currentDomain.resident resident "		
					+ "left join fetch resident.currentAddress address "		
					+ "where fp.id =:fiscalPeriodId and currentDomain.lotAppraisal > :minimumAppraisal"
					+ " and u.emited=:emited")
	})
public class UnbuiltLot {
	
	@Id
	@GeneratedValue(generator="UnbuiltLotGenerator", strategy=GenerationType.TABLE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="fiscalPeriod_id")
	private FiscalPeriod fiscalPeriod;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private PropertyUse propertyUse;
	
	@ManyToOne
	@JoinColumn(name = "property_id")
	private Property property;
	
	private String observation;
	
	private boolean emited;
	
	public UnbuiltLot(){		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public PropertyUse getPropertyUse() {
		return propertyUse;
	}

	public void setPropertyUse(PropertyUse propertyUse) {
		this.propertyUse = propertyUse;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public boolean isEmited() {
		return emited;
	}

	public void setEmited(boolean emited) {
		this.emited = emited;
	}
	
}
