package ec.gob.gim.market.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;

/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "StandGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Stand", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {

		@NamedQuery(name = "Stand.findByResidentMarket", query = "select stand from Stand stand "
				+ "left join stand.market market "
				+ "left join stand.currentContract cc "
				+ "where market = :market and stand.standType = :standType and "
				+ "cc.subscriber = :subscriber"),

		@NamedQuery(name = "Stand.findStandTypeByMarket", query = "select stand.standType from Stand stand "
				+ "left join stand.market market where market = :market "),

		@NamedQuery(name = "Stand.findStandByMarket", query = "select stand from Stand stand "
				+ "left join stand.market market where market = :market and stand.standStatus = :standStatus order by stand.standType,stand.number "),

		@NamedQuery(name = "Stand.findStandByType", query = "select stand from Stand stand "
				+ "left join stand.market market "
				+ "left join stand.standType standType "
				+ "where market = :market and standType = :standType and stand.standStatus = :standStatus order by stand.number") })
public class Stand {

	@Id
	@GeneratedValue(generator = "StandGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private double area;

	@Column(length = 30, nullable = false)
	private String name;

	// @Column(length = 30, nullable = false)
	@Column(length = 30)
	private Integer number;

	private Boolean isActive;

	private Boolean isOldAdjudication;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private StandStatus standStatus;

	private Boolean hasPreEmit;

	private Boolean isCalculated;

	// TODO variable momentanea hasta corregir la migracion
	@Column(length = 200)
	private String explicacion;

	// TODO variable momentanea hasta corregir la migracion
	@Column(length = 200)
	private String concepto;

	@ManyToOne
	private Stand linkedTo;

	@OrderBy("number ASC")
	@OneToMany(mappedBy = "linkedTo")
	private List<Stand> linkedStands;

	/**
	 * valor final del puesto
	 */
	private BigDecimal value;
	
	/**
	 * valor por metro cuadrado de puesto
	 * se debe calcular con el area ingresada
	 */
	private BigDecimal valueSquareMeter;

	@OneToOne
	private StandType standType;
	// Stanf Ubication

	@Temporal(TemporalType.DATE)
	private Date lastPreEmission;

	@ManyToOne
	@JoinColumn(name = "street_id")
	private Street street;

	@ManyToOne
	private Market market;

	@OneToOne(cascade = CascadeType.ALL)
	// @Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	// @JoinColumn(name="currentContract_id")
	private Contract currentContract;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "stand_id")
	private List<Contract> contracts;

	/*
	 * @OneToMany(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "stand_id") private List<ProductType> sellProducts;
	 */

	@ManyToOne
	@JoinColumn(name = "productType_id")
	private ProductType productType;

	@Transient
	private EmissionOrder emissionOrder;

	public Stand() {
		isCalculated = new Boolean(false);
		// sellProducts = new ArrayList<ProductType>();
		contracts = new ArrayList<Contract>();
		isOldAdjudication = new Boolean(false);
		hasPreEmit = new Boolean(false);
		isActive = new Boolean(true);
		linkedStands = new ArrayList<Stand>();
		currentContract = new Contract();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public StandType getStandType() {
		return standType;
	}

	public void setStandType(StandType standType) {
		this.standType = standType;
	}

	public StandStatus getStandStatus() {
		return standStatus;
	}

	public void setStandStatus(StandStatus standStatus) {
		this.standStatus = standStatus;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	/*
	 * public List<ProductType> getSellProducts() { return sellProducts; }
	 * 
	 * public void setSellProducts(List<ProductType> sellProducts) {
	 * this.sellProducts = sellProducts; }
	 * 
	 * public void add(ProductType productType) { if
	 * (!sellProducts.contains(productType)) { sellProducts.add(productType); }
	 * }
	 * 
	 * public void remove(ProductType productType) { if
	 * (sellProducts.remove(productType)) {
	 * 
	 * } }
	 */

	public Contract getCurrentContract() {
		return currentContract;
	}

	public void setCurrentContract(Contract currentContract) {
		this.currentContract = currentContract;
	}

	public void add(Contract c) {
		if (!this.contracts.contains(c) && c != null) {
			this.contracts.add(c);
		}
	}

	public void remove(Contract c) {
		if (c != null && this.contracts.contains(c)) {
			this.contracts.remove(c);
		}
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public Boolean getHasPreEmit() {
		return hasPreEmit;
	}

	public void setHasPreEmit(Boolean hasPreEmit) {
		this.hasPreEmit = hasPreEmit;
	}

	public Boolean getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(Boolean isCalculated) {
		this.isCalculated = isCalculated;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public EmissionOrder getEmissionOrder() {
		return emissionOrder;
	}

	public void setEmissionOrder(EmissionOrder emissionOrder) {
		this.emissionOrder = emissionOrder;
	}

	public Date getLastPreEmission() {
		return lastPreEmission;
	}

	public void setLastPreEmission(Date lastPreEmission) {
		this.lastPreEmission = lastPreEmission;
	}

	public Boolean getIsOldAdjudication() {
		return isOldAdjudication;
	}

	public void setIsOldAdjudication(Boolean isOldAdjudication) {
		this.isOldAdjudication = isOldAdjudication;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public Stand getLinkedTo() {
		return linkedTo;
	}

	public void setLinkedTo(Stand linkedTo) {
		this.linkedTo = linkedTo;
	}

	public List<Stand> getLinkedStands() {
		return linkedStands;
	}

	public void setLinkedStands(List<Stand> linkedStands) {
		this.linkedStands = linkedStands;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getExplicacion() {
		return explicacion;
	}

	public void setExplicacion(String explicacion) {
		this.explicacion = explicacion;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public BigDecimal getValueSquareMeter() {
		return valueSquareMeter;
	}

	public void setValueSquareMeter(BigDecimal valueSquareMeter) {
		this.valueSquareMeter = valueSquareMeter;
	}

}// end Place