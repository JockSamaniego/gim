package ec.gob.gim.firestation.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "TransportTechnicalInformationGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "TransportTechnicalInformation", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "TransportTechnicalInformation.findAll", query = "SELECT transportTechnicalInformation FROM TransportTechnicalInformation transportTechnicalInformation order by transportTechnicalInformation.id"),
		@NamedQuery(name = "TransportTechnicalInformation.findByInspection", query = "SELECT tt FROM TransportTechnicalInformation tt where tt.inspections.id = :idInspection") })
public class TransportTechnicalInformation {

	@Id
	@GeneratedValue(generator = "TransportTechnicalInformationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne
	private Inspections inspections;

	@Temporal(TemporalType.DATE)
	private Date transportTechnicalInformationDate;

	private Boolean isActive;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private TypeInspections typeInspections;
	@Column(length = 30)
	private String parish;

	private String numberTransportTechnicalInformation;

	// tipo de vehiculo
	private BigDecimal tonnage;
	@Column(length = 15)
	private String carPlate;
	private BigDecimal numberDieselGallons;
	private BigDecimal numberGasolineGallons;
	private Integer numberCylinders;
	private BigDecimal capacityTotal;
	@Column(length = 60)
	private String othersCar;
	private Integer yearManufacturing;
	@Column(length = 40)
	private String engineNumber;
	@Column(length = 40)
	private String chassisNumber;
	// sistema de seguridad y senializacion
	private Boolean isSign;
	private Boolean isSimbology;
	private Boolean isTriangles;
	private Boolean isMechanicalCat;
	private Boolean isFirstAidKits;
	private Boolean isEmergencyTire;
	@Column(length = 15)
	private String stateTire;
	private Integer extinguishersPQS;
	private Integer extinguishersCO2;
	private Integer extinguishersPK;
	private String transportRecomendations;
	
	@OneToMany(mappedBy = "transportTechnicalInformation", cascade = CascadeType.ALL)
	private List<InspectionObservation> observations;

	public TransportTechnicalInformation() {
		observations = new ArrayList<InspectionObservation>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Inspections getInspections() {
		return inspections;
	}

	public void setInspections(Inspections inspections) {
		this.inspections = inspections;
	}

	public Date getTransportTechnicalInformationDate() {
		return transportTechnicalInformationDate;
	}

	public void setTransportTechnicalInformationDate(
			Date transportTechnicalInformationDate) {
		this.transportTechnicalInformationDate = transportTechnicalInformationDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public TypeInspections getTypeInspections() {
		return typeInspections;
	}

	public void setTypeInspections(TypeInspections typeInspections) {
		this.typeInspections = typeInspections;
	}

	public String getNumberTransportTechnicalInformation() {
		return numberTransportTechnicalInformation;
	}

	public void setNumberTransportTechnicalInformation(
			String numberTransportTechnicalInformation) {
		this.numberTransportTechnicalInformation = numberTransportTechnicalInformation;
	}

	public BigDecimal getTonnage() {
		return tonnage;
	}

	public void setTonnage(BigDecimal tonnage) {
		this.tonnage = tonnage;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public BigDecimal getNumberDieselGallons() {
		return numberDieselGallons;
	}

	public void setNumberDieselGallons(BigDecimal numberDieselGallons) {
		this.numberDieselGallons = numberDieselGallons;
	}

	public BigDecimal getNumberGasolineGallons() {
		return numberGasolineGallons;
	}

	public void setNumberGasolineGallons(BigDecimal numberGasolineGallons) {
		this.numberGasolineGallons = numberGasolineGallons;
	}

	public Integer getNumberCylinders() {
		return numberCylinders;
	}

	public void setNumberCylinders(Integer numberCylinders) {
		this.numberCylinders = numberCylinders;
	}

	public BigDecimal getCapacityTotal() {
		return capacityTotal;
	}

	public void setCapacityTotal(BigDecimal capacityTotal) {
		this.capacityTotal = capacityTotal;
	}

	public String getOthersCar() {
		return othersCar;
	}

	public void setOthersCar(String othersCar) {
		this.othersCar = othersCar;
	}

	public Integer getYearManufacturing() {
		return yearManufacturing;
	}

	public void setYearManufacturing(Integer yearManufacturing) {
		this.yearManufacturing = yearManufacturing;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public Boolean getIsSign() {
		return isSign;
	}

	public void setIsSign(Boolean isSign) {
		this.isSign = isSign;
	}

	public Boolean getIsSimbology() {
		return isSimbology;
	}

	public void setIsSimbology(Boolean isSimbology) {
		this.isSimbology = isSimbology;
	}

	public Boolean getIsTriangles() {
		return isTriangles;
	}

	public void setIsTriangles(Boolean isTriangles) {
		this.isTriangles = isTriangles;
	}

	public Boolean getIsMechanicalCat() {
		return isMechanicalCat;
	}

	public void setIsMechanicalCat(Boolean isMechanicalCat) {
		this.isMechanicalCat = isMechanicalCat;
	}

	public Boolean getIsFirstAidKits() {
		return isFirstAidKits;
	}

	public void setIsFirstAidKits(Boolean isFirstAidKits) {
		this.isFirstAidKits = isFirstAidKits;
	}

	public Boolean getIsEmergencyTire() {
		return isEmergencyTire;
	}

	public void setIsEmergencyTire(Boolean isEmergencyTire) {
		this.isEmergencyTire = isEmergencyTire;
	}

	public String getStateTire() {
		return stateTire;
	}

	public void setStateTire(String stateTire) {
		this.stateTire = stateTire;
	}

	public Integer getExtinguishersPQS() {
		return extinguishersPQS;
	}

	public void setExtinguishersPQS(Integer extinguishersPQS) {
		this.extinguishersPQS = extinguishersPQS;
	}

	public Integer getExtinguishersCO2() {
		return extinguishersCO2;
	}

	public void setExtinguishersCO2(Integer extinguishersCO2) {
		this.extinguishersCO2 = extinguishersCO2;
	}

	public Integer getExtinguishersPK() {
		return extinguishersPK;
	}

	public void setExtinguishersPK(Integer extinguishersPK) {
		this.extinguishersPK = extinguishersPK;
	}

	public String getTransportRecomendations() {
		return transportRecomendations;
	}

	public void setTransportRecomendations(String transportRecomendations) {
		this.transportRecomendations = transportRecomendations;
	}

	public String getParish() {
		return parish;
	}

	public void setParish(String parish) {
		this.parish = parish;
	}

	public List<InspectionObservation> getObservations() {
		return observations;
	}

	public void setObservations(List<InspectionObservation> observations) {
		this.observations = observations;
	}
	
	public void add(InspectionObservation io) {
		if (!this.observations.contains(io)) {
			this.observations.add(io);
			io.setTransportTechnicalInformation(this);
		}
	}

	public void remove(InspectionObservation io) {
		boolean removed = this.observations.remove(io);
		if (removed)
			io.setTransportTechnicalInformation(null);
	}

}// end Inspections

