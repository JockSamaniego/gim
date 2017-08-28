package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:49
 */
@Audited
@Entity
@TableGenerator(name = "WaterSupplyGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WaterSupply", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "WaterSupply.findByRoute", query = "SELECT waterSupply FROM WaterSupply waterSupply "
				// + "LEFT JOIN FETCH waterSupply.contract cont "
				// + "LEFT JOIN FETCH cont.contractType ct "
				// + "LEFT JOIN FETCH ct.entry "
				+ "LEFT JOIN FETCH waterSupply.serviceOwner "
				+ "left join fetch waterSupply.recipeOwner recipeOwner "
				+ "LEFT JOIN FETCH waterSupply.street s "
				+ "LEFT JOIN FETCH s.place "
				+ "LEFT JOIN FETCH waterSupply.waterMeters wm "
				+ "LEFT JOIN FETCH wm.waterMeterStatus wms "
				+ "LEFT JOIN FETCH waterSupply.waterSupplyCategory "
				+ "LEFT JOIN FETCH waterSupply.route route "
				+ "WHERE route.id = :routeId and waterSupply.isCanceled = :isCanceled order by waterSupply.routeOrder"),
		/*
		 * @NamedQuery(name = "WaterSuppply.findActiveWaterSupplyForExemptions",
		 * query = "SELECT waterSupply FROM WaterSupply waterSupply " +
		 * "WHERE waterSupply.applyExemptions = true and waterSupply.serviceOwner.id = :residentId"
		 * ),
		 */
		@NamedQuery(name = "WaterSupply.findActualMeter", query = "SELECT waterSupply FROM WaterSupply waterSupply "
				+ "WHERE waterSupply.route.id = :routeId order by waterSupply.routeOrder"),
		@NamedQuery(name = "WaterSupply.findMaxService", query = "SELECT MAX(waterSupply.serviceNumber) FROM WaterSupply waterSupply"),
		@NamedQuery(name = "WaterSupply.findById", query = "SELECT waterSupply FROM WaterSupply waterSupply "
				+ "WHERE waterSupply.id=:id"),
		@NamedQuery(name = "WaterSupply.findByResident", query = "SELECT waterSupply FROM WaterSupply waterSupply "
				+ "LEFT JOIN FETCH waterSupply.serviceOwner serviceOwner "
				+ "left join fetch waterSupply.recipeOwner recipeOwner "
				+ "WHERE serviceOwner.id = :idResident"),
		@NamedQuery(name = "WaterSupply.findByWaterMeter", query = "SELECT waterSupply FROM WaterSupply waterSupply "
				+ "LEFT JOIN FETCH waterSupply.waterMeters wm "
				+ "WHERE wm.id = :idWaterMeter"),
		@NamedQuery(name = "WaterSupply.findByActiveExemption", query = "SELECT count(waterSupply.id) FROM WaterSupply waterSupply "
				+ "LEFT JOIN waterSupply.serviceOwner serviceOwner "
				+ "WHERE waterSupply.serviceOwner.id = :idResident and (waterSupply.applyElderlyExemption = true or waterSupply.applySpecialExemption = true) ") })
public class WaterSupply {

	@Id
	@GeneratedValue(generator = "WaterSupplyGenerator", strategy = GenerationType.TABLE)
	private Long id;
	private Integer routeOrder;
	private Integer serviceNumber;
	@Column(length = 255)
	private String observations;

	@Column(length = 80)
	private String ncalle;

	@Column(length = 10)
	private String ncasa;

	private Boolean hasSewerage;

	private Boolean applySpecialExemption;
	private Boolean applyElderlyExemption;

	private Boolean isCanceled;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "waterSupply_id")
	private List<WaterMeter> waterMeters;

	@ManyToOne
	@JoinColumn(name = "street_id")
	private Street street;

	@ManyToOne
	@JoinColumn(name = "route_id")
	private Route route;

	@ManyToOne
	@JoinColumn(name = "waterSupplyCategory_id")
	private WaterSupplyCategory waterSupplyCategory;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "contract_id")
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	private Property property;

	@ManyToOne
	@JoinColumn(name = "serviceOwner_id")
	private Resident serviceOwner;

	@ManyToOne
	@JoinColumn(name = "recipeOwner_id")
	private Resident recipeOwner;

	@Transient
	private String numberActualMeter;
	@Transient
	private String digitsActualMeter;
	@Transient
	private String ncalleReport;
	@Transient
	private String subscriberName;
	@Transient
	private Integer notPayMonths;
	@Transient
	private BigDecimal totalDebt;

	@Transient
	private Integer sequence;

	public WaterSupply() {
		this.waterMeters = new ArrayList<WaterMeter>();
		this.contract = new Contract();
		this.hasSewerage = new Boolean(true);
		this.isCanceled = new Boolean(false);
		this.applySpecialExemption = Boolean.FALSE;
		this.applyElderlyExemption = Boolean.FALSE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRouteOrder() {
		return routeOrder;
	}

	public void setRouteOrder(Integer routeOrder) {
		this.routeOrder = routeOrder;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public List<WaterMeter> getWaterMeters() {
		return waterMeters;
	}

	public void setWaterMeters(List<WaterMeter> waterMeters) {
		this.waterMeters = waterMeters;
	}

	public void add(WaterMeter waterMeter) {
		for (WaterMeter wm : waterMeters) {
			wm.setIsActive(new Boolean(false));
		}
		if (!waterMeters.contains(waterMeter)) {
			// Revisar esta parte por la navegabilidad
			// waterMeter.setWaterSupply(this)
			waterMeters.add(waterMeter);
		}
	}

	public void remove(WaterMeter waterMeter) {
		if (waterMeters.contains(waterMeter)) {
			waterMeters.remove(waterMeter);
		}
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public WaterSupplyCategory getWaterSupplyCategory() {
		return waterSupplyCategory;
	}

	public void setWaterSupplyCategory(WaterSupplyCategory waterSupplyCategory) {
		this.waterSupplyCategory = waterSupplyCategory;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

	public Integer getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(Integer serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public Resident getServiceOwner() {
		return serviceOwner;
	}

	public void setServiceOwner(Resident serviceOwner) {
		this.serviceOwner = serviceOwner;
	}

	public String getNumberActualMeter() {
		if (this.waterMeters != null) {
			if (this.waterMeters.size() >= 0) {
				numberActualMeter = waterMeters.get(0).getSerial();
			}
		}
		return numberActualMeter;
	}

	public void setNumberActualMeter(String numberActualMeter) {
		this.numberActualMeter = numberActualMeter;
	}

	public String getDigitsActualMeter() {
		if (this.waterMeters != null) {
			if (this.waterMeters.size() >= 0) {
				digitsActualMeter = waterMeters.get(0).getDigitsNumber()
						.toString();
			}
		}
		return digitsActualMeter;
	}

	public void setDigitsActualMeter(String digitsActualMeter) {
		this.digitsActualMeter = digitsActualMeter;
	}

	public String getNcalle() {
		return ncalle;
	}

	public void setNcalle(String ncalle) {
		this.ncalle = ncalle.toUpperCase();
	}

	public String getNcasa() {
		return ncasa;
	}

	public void setNcasa(String ncasa) {
		this.ncasa = ncasa.toUpperCase();
	}

	public String getNcalleReport() {
		if (ncalle != null) {
			if (ncalle.length() > 22) {
				this.ncalleReport = ncalle.substring(0, 22);
			} else {
				this.ncalleReport = ncalle;
			}
		}

		return ncalleReport;
	}

	public void setNcalleReport(String ncalleReport) {
		this.ncalleReport = ncalleReport;
	}

	public String getSubscriberName() {
		if (this.contract.getSubscriber() != null) {
			if (this.contract.getSubscriber().getName().length() > 26) {
				this.subscriberName = this.contract.getSubscriber().getName()
						.substring(0, 26);
			} else {
				this.subscriberName = this.contract.getSubscriber().getName();
			}
		}
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public Boolean getHasSewerage() {
		return hasSewerage;
	}

	public void setHasSewerage(Boolean hasSewerage) {
		this.hasSewerage = hasSewerage;
	}

	public Boolean getIsCanceled() {
		return isCanceled;
	}

	public void setIsCanceled(Boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	/**
	 * Contribuyente a cual se va a emitir la factura
	 * 
	 * @return
	 */
	public Resident getRecipeOwner() {
		return recipeOwner;
	}

	/**
	 * Contribuyente a cual se va a emitir la factura
	 * 
	 * @param recipeOwner
	 */
	public void setRecipeOwner(Resident recipeOwner) {
		this.recipeOwner = recipeOwner;
	}

	public Boolean getApplySpecialExemption() {
		return applySpecialExemption;
	}

	public void setApplySpecialExemption(Boolean applySpecialExemption) {
		this.applySpecialExemption = applySpecialExemption;
	}

	public Boolean getApplyElderlyExemption() {
		return applyElderlyExemption;
	}

	public void setApplyElderlyExemption(Boolean applyElderlyExemption) {
		this.applyElderlyExemption = applyElderlyExemption;
	}

	public Integer getNotPayMonths() {
		return notPayMonths;
	}

	public void setNotPayMonths(Integer notPayMonths) {
		this.notPayMonths = notPayMonths;
	}

	public BigDecimal getTotalDebt() {
		return totalDebt;
	}

	public void setTotalDebt(BigDecimal totalDebt) {
		this.totalDebt = totalDebt;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}// end WaterSupply
