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

import ec.gob.gim.revenue.model.EntryDefinition;

/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "TechnicalInformationGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "TechnicalInformation", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "TechnicalInformation.findAll", query = "SELECT technicalInformation FROM TechnicalInformation technicalInformation order by technicalInformation.id"),
		@NamedQuery(name = "TechnicalInformation.findByInspection", query = "SELECT ti FROM TechnicalInformation ti where ti.inspections.id = :idInspection") })
public class TechnicalInformation {

	@Id
	@GeneratedValue(generator = "TechnicalInformationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne
	private Inspections inspections;
	
	@Temporal(TemporalType.DATE)
	private Date technicalInformationDate;

	private Boolean isActive;

	@Enumerated(EnumType.STRING)
	@Column(length = 25)
	private TypeInformation typeInformation;

	private BigDecimal intervenedArea;
	private BigDecimal totalArea;
	private Integer numberOccupants;
	@Column(length = 100)
	private String riskPlace;
	@Column(length = 30)
	private String parish;
	@Column(length = 20)
	private String numberTechnicalInformation;

	// sistema electrico
	private Boolean isInternalElectricalInstallations;
	private Boolean isExternalElectricalInstallations;
	private Boolean isImprovisedElectricalInstallations;
	private Boolean isDefectiveElectricalInstallations;
	private Boolean isOpenBoxes;
	private Boolean isApropiateCablesBrekers;
	private Boolean isCleaningOrder;
	// riesgo de incendio
	private Boolean isGLPStorage;
	private Boolean isInflammableLiquids;
	private Boolean isSolidFuels;
	private Boolean isAdequateAccumulation;
	private Boolean isWarehousing;
	@Column(length = 100)
	private String otherInstallations;

	// instalaciones contra incendios
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StateFireInstallation stateFireInstallation;
	private Integer numberExtinguishers;
	private Boolean isSiamese;
	private Boolean isBoxes;
	private Boolean isAlarm;
	private Boolean isWaterReserve;
	private Boolean isSprayers;
	private Boolean isDetectors;
	private Boolean isAutoProtectionPlan;
	private Boolean isFireBrigades;
	private Boolean isFirstAid;
	private Boolean isRescueEvacuation;
	private Boolean isTypeConcreteConstruction;
	private Boolean isTypeConstructionMetalStructure;
	private Boolean isTypeConstructionMixed;
	// instalaciones de gas
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StateGasInstallation stateGasInstallation;
	// sistema de evacuacion y escape
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StateSystemEvacuationEscape stateSystemEvacuationEscape;
	private Boolean isEscapeRoutes;
	private Boolean isSignaling;
	private Boolean isEmergencyDoors;
	private Boolean isEmergencyLights;
	@Column(length = 100)
	private String others;
	// sistema de ventilacion y contaminacion
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StateSystemVentilationContamination stateSystemVentilationContamination;
	private Boolean isSmoke;
	private Boolean isGases;
	private Boolean isNoise;
	@Column(length = 100)
	private String othersSystemVentilationContamination;

	private String recommendations;
		
	@OneToMany(mappedBy = "technicalInformation", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<InspectionObservation> observations;

	public TechnicalInformation() {
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

	public Date getTechnicalInformationDate() {
		return technicalInformationDate;
	}

	public void setTechnicalInformationDate(Date technicalInformationDate) {
		this.technicalInformationDate = technicalInformationDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public TypeInformation getTypeInformation() {
		return typeInformation;
	}

	public void setTypeInformation(TypeInformation typeInformation) {
		this.typeInformation = typeInformation;
	}

	public BigDecimal getIntervenedArea() {
		return intervenedArea;
	}

	public void setIntervenedArea(BigDecimal intervenedArea) {
		this.intervenedArea = intervenedArea;
	}

	public BigDecimal getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(BigDecimal totalArea) {
		this.totalArea = totalArea;
	}

	public Integer getNumberOccupants() {
		return numberOccupants;
	}

	public void setNumberOccupants(Integer numberOccupants) {
		this.numberOccupants = numberOccupants;
	}

	public String getRiskPlace() {
		return riskPlace;
	}

	public void setRiskPlace(String riskPlace) {
		this.riskPlace = riskPlace;
	}

	public Boolean getIsInternalElectricalInstallations() {
		return isInternalElectricalInstallations;
	}

	public void setIsInternalElectricalInstallations(
			Boolean isInternalElectricalInstallations) {
		this.isInternalElectricalInstallations = isInternalElectricalInstallations;
	}

	public Boolean getIsExternalElectricalInstallations() {
		return isExternalElectricalInstallations;
	}

	public void setIsExternalElectricalInstallations(
			Boolean isExternalElectricalInstallations) {
		this.isExternalElectricalInstallations = isExternalElectricalInstallations;
	}

	public Boolean getIsImprovisedElectricalInstallations() {
		return isImprovisedElectricalInstallations;
	}

	public void setIsImprovisedElectricalInstallations(
			Boolean isImprovisedElectricalInstallations) {
		this.isImprovisedElectricalInstallations = isImprovisedElectricalInstallations;
	}

	public Boolean getIsDefectiveElectricalInstallations() {
		return isDefectiveElectricalInstallations;
	}

	public void setIsDefectiveElectricalInstallations(
			Boolean isDefectiveElectricalInstallations) {
		this.isDefectiveElectricalInstallations = isDefectiveElectricalInstallations;
	}

	public Boolean getIsOpenBoxes() {
		return isOpenBoxes;
	}

	public void setIsOpenBoxes(Boolean isOpenBoxes) {
		this.isOpenBoxes = isOpenBoxes;
	}

	public Boolean getIsApropiateCablesBrekers() {
		return isApropiateCablesBrekers;
	}

	public void setIsApropiateCablesBrekers(Boolean isApropiateCablesBrekers) {
		this.isApropiateCablesBrekers = isApropiateCablesBrekers;
	}

	public Boolean getIsCleaningOrder() {
		return isCleaningOrder;
	}

	public void setIsCleaningOrder(Boolean isCleaningOrder) {
		this.isCleaningOrder = isCleaningOrder;
	}

	public Boolean getIsGLPStorage() {
		return isGLPStorage;
	}

	public void setIsGLPStorage(Boolean isGLPStorage) {
		this.isGLPStorage = isGLPStorage;
	}

	public Boolean getIsInflammableLiquids() {
		return isInflammableLiquids;
	}

	public void setIsInflammableLiquids(Boolean isInflammableLiquids) {
		this.isInflammableLiquids = isInflammableLiquids;
	}

	public Boolean getIsSolidFuels() {
		return isSolidFuels;
	}

	public void setIsSolidFuels(Boolean isSolidFuels) {
		this.isSolidFuels = isSolidFuels;
	}

	public Boolean getIsAdequateAccumulation() {
		return isAdequateAccumulation;
	}

	public void setIsAdequateAccumulation(Boolean isAdequateAccumulation) {
		this.isAdequateAccumulation = isAdequateAccumulation;
	}

	public Boolean getIsWarehousing() {
		return isWarehousing;
	}

	public void setIsWarehousing(Boolean isWarehousing) {
		this.isWarehousing = isWarehousing;
	}

	public String getOtherInstallations() {
		return otherInstallations;
	}

	public void setOtherInstallations(String otherInstallations) {
		this.otherInstallations = otherInstallations;
	}

	public StateFireInstallation getStateFireInstallation() {
		return stateFireInstallation;
	}

	public void setStateFireInstallation(
			StateFireInstallation stateFireInstallation) {
		this.stateFireInstallation = stateFireInstallation;
	}

	public Integer getNumberExtinguishers() {
		return numberExtinguishers;
	}

	public void setNumberExtinguishers(Integer numberExtinguishers) {
		this.numberExtinguishers = numberExtinguishers;
	}

	public Boolean getIsSiamese() {
		return isSiamese;
	}

	public void setIsSiamese(Boolean isSiamese) {
		this.isSiamese = isSiamese;
	}

	public Boolean getIsBoxes() {
		return isBoxes;
	}

	public void setIsBoxes(Boolean isBoxes) {
		this.isBoxes = isBoxes;
	}

	public Boolean getIsAlarm() {
		return isAlarm;
	}

	public void setIsAlarm(Boolean isAlarm) {
		this.isAlarm = isAlarm;
	}

	public Boolean getIsWaterReserve() {
		return isWaterReserve;
	}

	public void setIsWaterReserve(Boolean isWaterReserve) {
		this.isWaterReserve = isWaterReserve;
	}

	public Boolean getIsSprayers() {
		return isSprayers;
	}

	public void setIsSprayers(Boolean isSprayers) {
		this.isSprayers = isSprayers;
	}

	public Boolean getIsDetectors() {
		return isDetectors;
	}

	public void setIsDetectors(Boolean isDetectors) {
		this.isDetectors = isDetectors;
	}

	public Boolean getIsAutoProtectionPlan() {
		return isAutoProtectionPlan;
	}

	public void setIsAutoProtectionPlan(Boolean isAutoProtectionPlan) {
		this.isAutoProtectionPlan = isAutoProtectionPlan;
	}

	public Boolean getIsFireBrigades() {
		return isFireBrigades;
	}

	public void setIsFireBrigades(Boolean isFireBrigades) {
		this.isFireBrigades = isFireBrigades;
	}

	public Boolean getIsFirstAid() {
		return isFirstAid;
	}

	public void setIsFirstAid(Boolean isFirstAid) {
		this.isFirstAid = isFirstAid;
	}

	public Boolean getIsRescueEvacuation() {
		return isRescueEvacuation;
	}

	public void setIsRescueEvacuation(Boolean isRescueEvacuation) {
		this.isRescueEvacuation = isRescueEvacuation;
	}

	public Boolean getIsTypeConcreteConstruction() {
		return isTypeConcreteConstruction;
	}

	public void setIsTypeConcreteConstruction(Boolean isTypeConcreteConstruction) {
		this.isTypeConcreteConstruction = isTypeConcreteConstruction;
	}

	public Boolean getIsTypeConstructionMetalStructure() {
		return isTypeConstructionMetalStructure;
	}

	public void setIsTypeConstructionMetalStructure(
			Boolean isTypeConstructionMetalStructure) {
		this.isTypeConstructionMetalStructure = isTypeConstructionMetalStructure;
	}

	public Boolean getIsTypeConstructionMixed() {
		return isTypeConstructionMixed;
	}

	public void setIsTypeConstructionMixed(Boolean isTypeConstructionMixed) {
		this.isTypeConstructionMixed = isTypeConstructionMixed;
	}

	public StateGasInstallation getStateGasInstallation() {
		return stateGasInstallation;
	}

	public void setStateGasInstallation(
			StateGasInstallation stateGasInstallation) {
		this.stateGasInstallation = stateGasInstallation;
	}

	public StateSystemEvacuationEscape getStateSystemEvacuationEscape() {
		return stateSystemEvacuationEscape;
	}

	public void setStateSystemEvacuationEscape(
			StateSystemEvacuationEscape stateSystemEvacuationEscape) {
		this.stateSystemEvacuationEscape = stateSystemEvacuationEscape;
	}

	public Boolean getIsEscapeRoutes() {
		return isEscapeRoutes;
	}

	public void setIsEscapeRoutes(Boolean isEscapeRoutes) {
		this.isEscapeRoutes = isEscapeRoutes;
	}

	public Boolean getIsSignaling() {
		return isSignaling;
	}

	public void setIsSignaling(Boolean isSignaling) {
		this.isSignaling = isSignaling;
	}

	public Boolean getIsEmergencyDoors() {
		return isEmergencyDoors;
	}

	public void setIsEmergencyDoors(Boolean isEmergencyDoors) {
		this.isEmergencyDoors = isEmergencyDoors;
	}

	public Boolean getIsEmergencyLights() {
		return isEmergencyLights;
	}

	public void setIsEmergencyLights(Boolean isEmergencyLights) {
		this.isEmergencyLights = isEmergencyLights;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public StateSystemVentilationContamination getStateSystemVentilationContamination() {
		return stateSystemVentilationContamination;
	}

	public void setStateSystemVentilationContamination(
			StateSystemVentilationContamination stateSystemVentilationContamination) {
		this.stateSystemVentilationContamination = stateSystemVentilationContamination;
	}

	public Boolean getIsSmoke() {
		return isSmoke;
	}

	public void setIsSmoke(Boolean isSmoke) {
		this.isSmoke = isSmoke;
	}

	public Boolean getIsGases() {
		return isGases;
	}

	public void setIsGases(Boolean isGases) {
		this.isGases = isGases;
	}

	public Boolean getIsNoise() {
		return isNoise;
	}

	public void setIsNoise(Boolean isNoise) {
		this.isNoise = isNoise;
	}

	public String getOthersSystemVentilationContamination() {
		return othersSystemVentilationContamination;
	}

	public void setOthersSystemVentilationContamination(
			String othersSystemVentilationContamination) {
		this.othersSystemVentilationContamination = othersSystemVentilationContamination;
	}

	public String getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(String recommendations) {
		this.recommendations = recommendations;
	}

	public String getNumberTechnicalInformation() {
		return numberTechnicalInformation;
	}

	public void setNumberTechnicalInformation(String numberTechnicalInformation) {
		this.numberTechnicalInformation = numberTechnicalInformation;
	}

	public void setParish(String parish) {
		this.parish = parish;
	}

	public String getParish() {
		return parish;
	}

	public List<InspectionObservation> getObservations() {
		return observations;
	}

	public void setObservations(List<InspectionObservation> observations) {
		this.observations = observations;
	}
	
	public void add(InspectionObservation io) {
		if (!this.observations.contains(io)) {
			io.setTechnicalInformation(this);
			this.observations.add(io);
			
		}
	}

	public void remove(InspectionObservation io) {
		boolean removed = this.observations.remove(io);
		if (removed)
			io.setTechnicalInformation(null);
	}

}// end Inspections

