package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

// import ec.gob.gim.revenue.model.adjunct.BinnacleCRVReference;

/**
 * Bitácora Digital: información general de la Bitácora Digital del Centro de Retención Vehicular.
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 06-May-2021
 */
@Audited
@Entity
@TableGenerator(name = "BinnacleCRVGenerator", 
	table = "IdentityGenerator", 
	pkColumnName = "name", 
	valueColumnName = "value", 
	pkColumnValue = "BinnacleCRV", 
	initialValue = 1, allocationSize = 1)

@NamedQueries(value = {
	@NamedQuery(name = "BinnacleCRV.findGarajePendingsByIdentificationNumber", 
		query = "SELECT distinct b FROM BinnacleCRV b " +
			"join b.arrivalHistoryBinnacleCRVs a " +
			"WHERE b.ownerIdentification = :ownerIdentification " +
			"and a.exitDate = null "),
	@NamedQuery(name = "BinnacleCRV.findCranePendingsByIdentificationNumber", 
	query = "SELECT distinct b FROM BinnacleCRV b " +
		"join b.arrivalHistoryBinnacleCRVs a " +
		"WHERE b.ownerIdentification = :ownerIdentification " +
		"and a.hasCraneService = true " +
		"and a.exitDateCrane = null ")
	})


public class BinnacleCRV implements Serializable {

	private static final long serialVersionUID = -7725282715946820494L;

	@Id
	@GeneratedValue(generator = "BinnacleCRVGenerator", strategy = GenerationType.TABLE)
	private Long id; 
    
	@Column(length = 20)
	private String licensePlate; //Placa/RAMV/CPN
	@Column(length = 15)
	private String ownerIdentification; //Cédula del Propietario
	@Column(length = 200)
	private String ownerName; //Nombres del Propietario
	@Column(length = 100)
	private String ownerAddress; //Direccion del Propietario
	@Column(length = 100)
	private String ownerEmail; //Email del Propietario
	@Temporal(TemporalType.DATE)
	private Date admissionDate; //Fecha de Ingreso
	@Temporal(TemporalType.TIME)
	private Date admissionTime; //Hora de Ingreso
	@Column(length = 20)
	private String motiveArticle; //Artículo que motiva el ingreso
	@Column(length = 20)
	private String motiveNumber; //numeral que motiva el ingreso
	@Column(length = 20)
	private String motiveLiteral; //Literal que motiva el ingreso
	@Column(length = 30)
	private String motiveResolution; //Resolución que motiva el ingreso
	@Column(length = 100)
	private String motiveAuthority; //Autoridad que motiva el ingreso
	@Column(length = 30)
	private String motiveOffice; //Oficio que motiva el ingreso
	@Column(length = 10)
	private String serialNumber; //Número de Serie
	@Column(length = 30)
	private String motor; //Motor
	@Column(length = 30)
	private String chassis; //Chasis
	@Column(length = 100)
	private String model; //Modelo

	private Integer year; //Año
	private Double tonnage; //Tonelaje
	@Column(length = 30)
	private String type; //Tipo
	@Column(length = 30)
	private String brand; //Marca
	@Column(length = 30)
	private String color; //Color
	@Column(length = 30)
	private String service; //Servicio
	private BigDecimal km; //kms recorridos por la grúa municipal
	@Column(columnDefinition = "TEXT")
	private String transferInfo; //Datos de Traslado
	@Column(length = 15)
	private String admissionDriverIdentification; //Cédula del conductor
	@Column(length = 80)
	private String admissionDriverNames; //Nombres del conductor
	@Column(length = 15)
	private String admissionAgentIdentification; //Cédula del Agente de Ingreso
	@Column(length = 80)
	private String admissionAgentNames; //Nombres del Agente de Ingreso
	@Column(length = 15)
	private String admissionResponsableIdentification; //Cédula de Responsable Ingreso
	@Column(length = 80)
	private String admissionResponsableName; //Nombres de Responsable Ingreso
	@Column(length = 15)
	private String deliveryAgentIdentification; //Cédula de Agente Responsable de Entrega
	@Column(length = 80)
	private String deliveryAgentNames; //Nombres de Agente Responsable de Entrega
	@Column(length = 15)
	private String deliveryResponsableIdentification; //Cédula de Responsable de Salida
	@Column(length = 80)
	private String deliveryResponsableNames; //Nombres de Responsable de Salida
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private DocumentTypeBinnacleCRV documentTypeBinnacleCRV;// Tipo de documento para el Parte
	private boolean hasJudicialDocument; //Tiene generado el documento judicial
	private String nameOfJudicialPart; //Nombre del archivo escaneado del Parte Judicial
	@Version
	private Long version;
    
	@OneToOne
	@JoinColumn(name = "admissionType_id")
	private AdmissionType admissionType; //Tipo de Ingreso
	@OneToOne
	@JoinColumn(name = "admissionCategory_id")
	private AdmissionCategory admissionCategory; //Categoría de Ingreso
	@OneToOne
	@JoinColumn(name = "admissionMean_id")
	private AdmissionMean admissionMean; //Medio de Ingreso
	@OneToOne
	@JoinColumn(name = "binnacleCRVCrane_id")
	private BinnacleCRVCrane binnacleCRVCrane; //nombre de empresa de grúa y numero de grúa

	@OneToMany(mappedBy = "binnacleCRV", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(value = "orderColumn")
	private List<VehicleInventory> vehicleInventories; //Inventario del vehículo

	@OneToMany(mappedBy = "binnacleCRV", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<VehicleItem> vehicleItems; //Artículos encontrados en el vehículo
	
	@OneToMany(mappedBy = "binnacleCRV", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ObsBinnacleCRV> obsBinnacleCRVs; //Observaciones en el ingreso y salida del vehículo al CRV

	@OneToMany(mappedBy = "binnacleCRV", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(value = "id DESC")
	private List<ArrivalHistoryBinnacleCRV> arrivalHistoryBinnacleCRVs; //Historial de ingresos del Vehículo al CRV

	@OneToMany(mappedBy = "binnacleCRV", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<PhotographicEvidence> photographicEvidences; //Imagenes que evidencien el estado del vehículo
															  //se permitirá el ingreso de dos imagenes

	/*@OneToMany(mappedBy = "binnacleCRV", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<BinnacleCRVReference> binnacleCRVReferences; //son los adjuntas del municipalmunicipalBond*/

	@OneToOne
	@JoinColumn(name = "informativePart_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	private InformativePart informativePart; //Parte informativo por la retención vehicular
	
	@OneToOne
	@JoinColumn(name = "accidentPart_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	private AccidentPart accidentPart; //Parte de Accidente por la retención vehicular
	
	@OneToOne
	@JoinColumn(name = "ballotPart_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	private BallotPart ballotPart; //Parte de Boleta por la retención vehicular Personal o Vehicular

	@Transient
	private ArrivalHistoryBinnacleCRV lastArrivalHistoryBinnacleCRV;
	
	public BinnacleCRV() {
		vehicleInventories = new ArrayList<VehicleInventory>();
		vehicleItems = new ArrayList<VehicleItem>();
		obsBinnacleCRVs = new ArrayList<ObsBinnacleCRV>();
		arrivalHistoryBinnacleCRVs = new ArrayList<ArrivalHistoryBinnacleCRV>();
		photographicEvidences = new ArrayList<PhotographicEvidence>();
		PhotographicEvidence evidence = new PhotographicEvidence();
		photographicEvidences.add(evidence);
		evidence = new PhotographicEvidence();
		photographicEvidences.add(evidence);
		hasJudicialDocument = false;
		nameOfJudicialPart = "";
		km = BigDecimal.ZERO;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the licensePlate
	 */
	public String getLicensePlate() {
		return licensePlate;
	}

	/**
	 * @param licensePlate the licensePlate to set
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	/**
	 * @return the ownerIdentification
	 */
	public String getOwnerIdentification() {
		return ownerIdentification;
	}

	/**
	 * @param ownerIdentification the ownerIdentification to set
	 */
	public void setOwnerIdentification(String ownerIdentification) {
		this.ownerIdentification = ownerIdentification;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return the ownerAddress
	 */
	public String getOwnerAddress() {
		return ownerAddress;
	}

	/**
	 * @param ownerAddress the ownerAddress to set
	 */
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	/**
	 * @return the ownerEmail
	 */
	public String getOwnerEmail() {
		return ownerEmail;
	}

	/**
	 * @param ownerEmail the ownerEmail to set
	 */
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	/**
	 * @return the admissionDate
	 */
	public Date getAdmissionDate() {
		return admissionDate;
	}

	/**
	 * @param admissionDate the admissionDate to set
	 */
	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	/**
	 * @return the admissionTime
	 */
	public Date getAdmissionTime() {
		return admissionTime;
	}

	/**
	 * @param admissionTime the admissionTime to set
	 */
	public void setAdmissionTime(Date admissionTime) {
		this.admissionTime = admissionTime;
	}

	/**
	 * @return the motiveArticle
	 */
	public String getMotiveArticle() {
		return motiveArticle;
	}

	/**
	 * @param motiveArticle the motiveArticle to set
	 */
	public void setMotiveArticle(String motiveArticle) {
		this.motiveArticle = motiveArticle;
	}

	/**
	 * @return the motiveNumber
	 */
	public String getMotiveNumber() {
		return motiveNumber;
	}

	/**
	 * @param motiveNumber the motiveNumber to set
	 */
	public void setMotiveNumber(String motiveNumber) {
		this.motiveNumber = motiveNumber;
	}

	/**
	 * @return the motiveLiteral
	 */
	public String getMotiveLiteral() {
		return motiveLiteral;
	}

	/**
	 * @param motiveLiteral the motiveLiteral to set
	 */
	public void setMotiveLiteral(String motiveLiteral) {
		this.motiveLiteral = motiveLiteral;
	}

	/**
	 * @return the motiveResolution
	 */
	public String getMotiveResolution() {
		return motiveResolution;
	}

	/**
	 * @param motiveResolution the motiveResolution to set
	 */
	public void setMotiveResolution(String motiveResolution) {
		this.motiveResolution = motiveResolution;
	}

	/**
	 * @return the motiveAuthority
	 */
	public String getMotiveAuthority() {
		return motiveAuthority;
	}

	/**
	 * @param motiveAuthority the motiveAuthority to set
	 */
	public void setMotiveAuthority(String motiveAuthority) {
		this.motiveAuthority = motiveAuthority;
	}

	/**
	 * @return the motiveOffice
	 */
	public String getMotiveOffice() {
		return motiveOffice;
	}

	/**
	 * @param motiveOffice the motiveOffice to set
	 */
	public void setMotiveOffice(String motiveOffice) {
		this.motiveOffice = motiveOffice;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the motor
	 */
	public String getMotor() {
		return motor;
	}

	/**
	 * @param motor the motor to set
	 */
	public void setMotor(String motor) {
		this.motor = motor;
	}

	/**
	 * @return the chassis
	 */
	public String getChassis() {
		return chassis;
	}

	/**
	 * @param chassis the chassis to set
	 */
	public void setChassis(String chassis) {
		this.chassis = chassis;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the tonnage
	 */
	public Double getTonnage() {
		return tonnage;
	}

	/**
	 * @param tonnage the tonnage to set
	 */
	public void setTonnage(Double tonnage) {
		this.tonnage = tonnage;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the km
	 */
	public BigDecimal getKm() {
		return km;
	}

	/**
	 * @param km the km to set
	 */
	public void setKm(BigDecimal km) {
		this.km = km;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the transferInfo
	 */
	public String getTransferInfo() {
		return transferInfo;
	}

	/**
	 * @param transferInfo the transferInfo to set
	 */
	public void setTransferInfo(String transferInfo) {
		this.transferInfo = transferInfo;
	}

	/**
	 * @return the admissionDriverIdentification
	 */
	public String getAdmissionDriverIdentification() {
		return admissionDriverIdentification;
	}

	/**
	 * @param admissionDriverIdentification the admissionDriverIdentification to set
	 */
	public void setAdmissionDriverIdentification(
			String admissionDriverIdentification) {
		this.admissionDriverIdentification = admissionDriverIdentification;
	}

	/**
	 * @return the admissionDriverNames
	 */
	public String getAdmissionDriverNames() {
		return admissionDriverNames;
	}

	/**
	 * @param admissionDriverNames the admissionDriverNames to set
	 */
	public void setAdmissionDriverNames(String admissionDriverNames) {
		this.admissionDriverNames = admissionDriverNames;
	}

	/**
	 * @return the admissionAgentIdentification
	 */
	public String getAdmissionAgentIdentification() {
		return admissionAgentIdentification;
	}

	/**
	 * @param admissionAgentIdentification the admissionAgentIdentification to set
	 */
	public void setAdmissionAgentIdentification(String admissionAgentIdentification) {
		this.admissionAgentIdentification = admissionAgentIdentification;
	}

	/**
	 * @return the admissionAgentNames
	 */
	public String getAdmissionAgentNames() {
		return admissionAgentNames;
	}

	/**
	 * @param admissionAgentNames the admissionAgentNames to set
	 */
	public void setAdmissionAgentNames(String admissionAgentNames) {
		this.admissionAgentNames = admissionAgentNames;
	}

	/**
	 * @return the admissionResponsableIdentification
	 */
	public String getAdmissionResponsableIdentification() {
		return admissionResponsableIdentification;
	}

	/**
	 * @param admissionResponsableIdentification the admissionResponsableIdentification to set
	 */
	public void setAdmissionResponsableIdentification(String admissionResponsableIdentification) {
		this.admissionResponsableIdentification = admissionResponsableIdentification;
	}

	/**
	 * @return the admissionResponsableName
	 */
	public String getAdmissionResponsableName() {
		return admissionResponsableName;
	}

	/**
	 * @param admissionResponsableName the admissionResponsableName to set
	 */
	public void setAdmissionResponsableName(String admissionResponsableName) {
		this.admissionResponsableName = admissionResponsableName;
	}

	/**
	 * @return the deliveryAgentIdentification
	 */
	public String getDeliveryAgentIdentification() {
		return deliveryAgentIdentification;
	}

	/**
	 * @param deliveryAgentIdentification the deliveryAgentIdentification to set
	 */
	public void setDeliveryAgentIdentification(String deliveryAgentIdentification) {
		this.deliveryAgentIdentification = deliveryAgentIdentification;
	}

	/**
	 * @return the deliveryAgentNames
	 */
	public String getDeliveryAgentNames() {
		return deliveryAgentNames;
	}

	/**
	 * @param deliveryAgentNames the deliveryAgentNames to set
	 */
	public void setDeliveryAgentNames(String deliveryAgentNames) {
		this.deliveryAgentNames = deliveryAgentNames;
	}

	/**
	 * @return the deliveryResponsableIdentification
	 */
	public String getDeliveryResponsableIdentification() {
		return deliveryResponsableIdentification;
	}

	/**
	 * @param deliveryResponsableIdentification the deliveryResponsableIdentification to set
	 */
	public void setDeliveryResponsableIdentification(String deliveryResponsableIdentification) {
		this.deliveryResponsableIdentification = deliveryResponsableIdentification;
	}

	/**
	 * @return the deliveryResponsableNames
	 */
	public String getDeliveryResponsableNames() {
		return deliveryResponsableNames;
	}

	/**
	 * @param deliveryResponsableNames the deliveryResponsableNames to set
	 */
	public void setDeliveryResponsableNames(String deliveryResponsableNames) {
		this.deliveryResponsableNames = deliveryResponsableNames;
	}

	/**
	 * @return the admissionType
	 */
	public AdmissionType getAdmissionType() {
		return admissionType;
	}

	/**
	 * @param admissionType the admissionType to set
	 */
	public void setAdmissionType(AdmissionType admissionType) {
		this.admissionType = admissionType;
	}

	/**
	 * @return the admissionCategory
	 */
	public AdmissionCategory getAdmissionCategory() {
		return admissionCategory;
	}

	/**
	 * @param admissionCategory the admissionCategory to set
	 */
	public void setAdmissionCategory(AdmissionCategory admissionCategory) {
		this.admissionCategory = admissionCategory;
	}

	/**
	 * @return the admissionMean
	 */
	public AdmissionMean getAdmissionMean() {
		return admissionMean;
	}

	/**
	 * @param admissionMean the admissionMean to set
	 */
	public void setAdmissionMean(AdmissionMean admissionMean) {
		this.admissionMean = admissionMean;
	}

	/**
	 * @return the binnacleCRVCrane
	 */
	public BinnacleCRVCrane getBinnacleCRVCrane() {
		return binnacleCRVCrane;
	}

	/**
	 * @param binnacleCRVCrane the binnacleCRVCrane to set
	 */
	public void setBinnacleCRVCrane(BinnacleCRVCrane binnacleCRVCrane) {
		this.binnacleCRVCrane = binnacleCRVCrane;
	}

	/**
	 * @return the vehicleInventories
	 */
	public List<VehicleInventory> getVehicleInventories() {
		return vehicleInventories;
	}

	/**
	 * @param vehicleInventories the vehicleInventories to set
	 */
	public void setVehicleInventories(List<VehicleInventory> vehicleInventories) {
		this.vehicleInventories = vehicleInventories;
	}

	/**
	 * @return the vehicleItems
	 */
	public List<VehicleItem> getVehicleItems() {
		return vehicleItems;
	}

	/**
	 * @param vehicleItems the vehicleItems to set
	 */
	public void setVehicleItems(List<VehicleItem> vehicleItems) {
		this.vehicleItems = vehicleItems;
	}

	/**
	 * @return the obsBinnacleCRVs
	 */
	public List<ObsBinnacleCRV> getObsBinnacleCRVs() {
		return obsBinnacleCRVs;
	}

	/**
	 * @param obsBinnacleCRVs the obsBinnacleCRVs to set
	 */
	public void setObsBinnacleCRVs(List<ObsBinnacleCRV> obsBinnacleCRVs) {
		this.obsBinnacleCRVs = obsBinnacleCRVs;
	}

	/**
	 * @return the obsBinnacleCRVs by ObsTypeBinnacleCRV
	 */
	public List<ObsBinnacleCRV> getObsBinnacleCRVs(ObsTypeBinnacleCRV obsTypeBinnacleCRV) {
		List<ObsBinnacleCRV> obs = new ArrayList<ObsBinnacleCRV>();
		for (ObsBinnacleCRV obsBinnacleCRV : getObsBinnacleCRVs()) {
			if (obsBinnacleCRV.getObsTypeBinnacleCRV().equals(obsTypeBinnacleCRV))
				obs.add(obsBinnacleCRV);
		}
		return obs;
	}

	/**
	 * @return the arrivalHistoryBinnacleCRVs
	 */
	public List<ArrivalHistoryBinnacleCRV> getArrivalHistoryBinnacleCRVs() {
		return arrivalHistoryBinnacleCRVs;
	}

	/**
	 * @param arrivalHistoryBinnacleCRVs the arrivalHistoryBinnacleCRVs to set
	 */
	public void setArrivalHistoryBinnacleCRVs(
			List<ArrivalHistoryBinnacleCRV> arrivalHistoryBinnacleCRVs) {
		this.arrivalHistoryBinnacleCRVs = arrivalHistoryBinnacleCRVs;
	}

	/**
	 * @return the photographicEvidences
	 */
	public List<PhotographicEvidence> getPhotographicEvidences() {
		return photographicEvidences;
	}

	/**
	 * @param photographicEvidences the photographicEvidences to set
	 */
	public void setPhotographicEvidences(List<PhotographicEvidence> photographicEvidences) {
		this.photographicEvidences = photographicEvidences;
	}

	/**
	 * @return the binnacleCRVReferences
	 */
	/*public List<BinnacleCRVReference> getBinnacleCRVReferences() {
		return binnacleCRVReferences;
	}*/

	/**
	 * @param binnacleCRVReferences the binnacleCRVReferences to set
	 */
	/*public void setBinnacleCRVReferences(
			List<BinnacleCRVReference> binnacleCRVReferences) {
		this.binnacleCRVReferences = binnacleCRVReferences;
	}*/

	/**
	 * @return the documentTypeBinnacleCRV
	 */
	public DocumentTypeBinnacleCRV getDocumentTypeBinnacleCRV() {
		return documentTypeBinnacleCRV;
	}

	/**
	 * @param documentTypeBinnacleCRV the documentTypeBinnacleCRV to set
	 */
	public void setDocumentTypeBinnacleCRV(
			DocumentTypeBinnacleCRV documentTypeBinnacleCRV) {
		this.documentTypeBinnacleCRV = documentTypeBinnacleCRV;
	}

	/**
	 * @return the hasJudicialDocument
	 */
	public boolean isHasJudicialDocument() {
		return hasJudicialDocument;
	}

	/**
	 * @param hasJudicialDocument the hasJudicialDocument to set
	 */
	public void setHasJudicialDocument(boolean hasJudicialDocument) {
		this.hasJudicialDocument = hasJudicialDocument;
	}

	/**
	 * @return the nameOfJudicialPart
	 */
	public String getNameOfJudicialPart() {
		return nameOfJudicialPart;
	}

	/**
	 * @param nameOfJudicialPart the nameOfJudicialPart to set
	 */
	public void setNameOfJudicialPart(String nameOfJudicialPart) {
		this.nameOfJudicialPart = nameOfJudicialPart;
	}

	/**
	 * @return the informativePart
	 */
	public InformativePart getInformativePart() {
		return informativePart;
	}

	/**
	 * @param informativePart the informativePart to set
	 */
	public void setInformativePart(InformativePart informativePart) {
		this.informativePart = informativePart;
	}

	/**
	 * @return the accidentPart
	 */
	public AccidentPart getAccidentPart() {
		return accidentPart;
	}

	/**
	 * @param accidentPart the accidentPart to set
	 */
	public void setAccidentPart(AccidentPart accidentPart) {
		this.accidentPart = accidentPart;
	}

	/**
	 * @return the ballotPart
	 */
	public BallotPart getBallotPart() {
		return ballotPart;
	}

	/**
	 * @param ballotPart the ballotPart to set
	 */
	public void setBallotPart(BallotPart ballotPart) {
		this.ballotPart = ballotPart;
	}

	/**
	 * @return the lastArrivalHistoryBinnacleCRV
	 */
	public ArrivalHistoryBinnacleCRV getLastArrivalHistoryBinnacleCRV() {
		lastArrivalHistoryBinnacleCRV = arrivalHistoryBinnacleCRVs.get(0);
		return lastArrivalHistoryBinnacleCRV;
	}

	public void add(VehicleInventory vehicleInventory) {
		if (!vehicleInventories.contains(vehicleInventory)) {
			vehicleInventories.add(vehicleInventory);
			vehicleInventory.setBinnacleCRV(this);
		}
	}
	
	public void remove(VehicleInventory vehicleInventory) {
		if (vehicleInventories.contains(vehicleInventory)) {
			vehicleInventories.remove(vehicleInventory);
			vehicleInventory.setBinnacleCRV(null);
		}
	}
	
	public void add(VehicleItem vehicleItem) {
		if (!vehicleItems.contains(vehicleItem)) {
			vehicleItems.add(vehicleItem);
			vehicleItem.setBinnacleCRV(this);
		}
	}
	
	public void remove(VehicleItem vehicleItem) {
		if (vehicleItems.contains(vehicleItem)) {
			vehicleItems.remove(vehicleItem);
			vehicleItem.setBinnacleCRV(null);
		}
	}
	
	public void add(ObsBinnacleCRV obsBinnacleCRV) {
		if (!obsBinnacleCRVs.contains(obsBinnacleCRV)) {
			obsBinnacleCRVs.add(obsBinnacleCRV);
			obsBinnacleCRV.setBinnacleCRV(this);
		}
	}
	
	public void remove(ObsBinnacleCRV obsBinnacleCRV) {
		if (obsBinnacleCRVs.contains(obsBinnacleCRV)) {
			obsBinnacleCRVs.remove(obsBinnacleCRV);
			obsBinnacleCRV.setBinnacleCRV(null);
		}
	}
	
	public void add(ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV) {
		if (!arrivalHistoryBinnacleCRVs.contains(arrivalHistoryBinnacleCRV)) {
			arrivalHistoryBinnacleCRVs.add(arrivalHistoryBinnacleCRV);
			arrivalHistoryBinnacleCRV.setBinnacleCRV(this);
		}
	}
	
	public void remove(ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV) {
		if (arrivalHistoryBinnacleCRVs.contains(arrivalHistoryBinnacleCRV)) {
			arrivalHistoryBinnacleCRVs.remove(arrivalHistoryBinnacleCRV);
			arrivalHistoryBinnacleCRV.setBinnacleCRV(null);
		}
	}
	
	public void add(PhotographicEvidence photographicEvidence) {
		if (!photographicEvidences.contains(photographicEvidence)) {
			photographicEvidences.add(photographicEvidence);
			photographicEvidence.setBinnacleCRV(this);
		}
	}
	
	public void remove(PhotographicEvidence photographicEvidence) {
		if (photographicEvidences.contains(photographicEvidence)) {
			photographicEvidences.remove(photographicEvidence);
			photographicEvidence.setBinnacleCRV(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((admissionAgentIdentification == null) ? 0 : admissionAgentIdentification.hashCode());
		result = prime * result + ((admissionAgentNames == null) ? 0 : admissionAgentNames.hashCode());
		result = prime * result + ((admissionCategory == null) ? 0 : admissionCategory.hashCode());
		result = prime * result + ((admissionDate == null) ? 0 : admissionDate.hashCode());
		result = prime * result + ((admissionMean == null) ? 0 : admissionMean.hashCode());
		result = prime * result
				+ ((admissionResponsableIdentification == null) ? 0 : admissionResponsableIdentification.hashCode());
		result = prime * result + ((admissionResponsableName == null) ? 0 : admissionResponsableName.hashCode());
		result = prime * result + ((admissionType == null) ? 0 : admissionType.hashCode());
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((chassis == null) ? 0 : chassis.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((deliveryAgentIdentification == null) ? 0 : deliveryAgentIdentification.hashCode());
		result = prime * result + ((deliveryAgentNames == null) ? 0 : deliveryAgentNames.hashCode());
		result = prime * result
				+ ((deliveryResponsableIdentification == null) ? 0 : deliveryResponsableIdentification.hashCode());
		result = prime * result + ((deliveryResponsableNames == null) ? 0 : deliveryResponsableNames.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((licensePlate == null) ? 0 : licensePlate.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((motor == null) ? 0 : motor.hashCode());
		result = prime * result + ((obsBinnacleCRVs == null) ? 0 : obsBinnacleCRVs.hashCode());
		result = prime * result + ((photographicEvidences == null) ? 0 : photographicEvidences.hashCode());
		result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((tonnage == null) ? 0 : tonnage.hashCode());
		result = prime * result + ((transferInfo == null) ? 0 : transferInfo.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((vehicleInventories == null) ? 0 : vehicleInventories.hashCode());
		result = prime * result + ((vehicleItems == null) ? 0 : vehicleItems.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinnacleCRV other = (BinnacleCRV) obj;
		if (admissionAgentIdentification == null) {
			if (other.admissionAgentIdentification != null)
				return false;
		} else if (!admissionAgentIdentification.equals(other.admissionAgentIdentification))
			return false;
		if (admissionAgentNames == null) {
			if (other.admissionAgentNames != null)
				return false;
		} else if (!admissionAgentNames.equals(other.admissionAgentNames))
			return false;
		if (admissionCategory == null) {
			if (other.admissionCategory != null)
				return false;
		} else if (!admissionCategory.equals(other.admissionCategory))
			return false;
		if (admissionDate == null) {
			if (other.admissionDate != null)
				return false;
		} else if (!admissionDate.equals(other.admissionDate))
			return false;
		if (admissionMean == null) {
			if (other.admissionMean != null)
				return false;
		} else if (!admissionMean.equals(other.admissionMean))
			return false;
		if (admissionResponsableIdentification == null) {
			if (other.admissionResponsableIdentification != null)
				return false;
		} else if (!admissionResponsableIdentification.equals(other.admissionResponsableIdentification))
			return false;
		if (admissionResponsableName == null) {
			if (other.admissionResponsableName != null)
				return false;
		} else if (!admissionResponsableName.equals(other.admissionResponsableName))
			return false;
		if (admissionType == null) {
			if (other.admissionType != null)
				return false;
		} else if (!admissionType.equals(other.admissionType))
			return false;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (chassis == null) {
			if (other.chassis != null)
				return false;
		} else if (!chassis.equals(other.chassis))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (deliveryAgentIdentification == null) {
			if (other.deliveryAgentIdentification != null)
				return false;
		} else if (!deliveryAgentIdentification.equals(other.deliveryAgentIdentification))
			return false;
		if (deliveryAgentNames == null) {
			if (other.deliveryAgentNames != null)
				return false;
		} else if (!deliveryAgentNames.equals(other.deliveryAgentNames))
			return false;
		if (deliveryResponsableIdentification == null) {
			if (other.deliveryResponsableIdentification != null)
				return false;
		} else if (!deliveryResponsableIdentification.equals(other.deliveryResponsableIdentification))
			return false;
		if (deliveryResponsableNames == null) {
			if (other.deliveryResponsableNames != null)
				return false;
		} else if (!deliveryResponsableNames.equals(other.deliveryResponsableNames))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (licensePlate == null) {
			if (other.licensePlate != null)
				return false;
		} else if (!licensePlate.equals(other.licensePlate))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (motor == null) {
			if (other.motor != null)
				return false;
		} else if (!motor.equals(other.motor))
			return false;
		if (obsBinnacleCRVs == null) {
			if (other.obsBinnacleCRVs != null)
				return false;
		} else if (!obsBinnacleCRVs.equals(other.obsBinnacleCRVs))
			return false;
		if (photographicEvidences == null) {
			if (other.photographicEvidences != null)
				return false;
		} else if (!photographicEvidences.equals(other.photographicEvidences))
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (tonnage == null) {
			if (other.tonnage != null)
				return false;
		} else if (!tonnage.equals(other.tonnage))
			return false;
		if (transferInfo == null) {
			if (other.transferInfo != null)
				return false;
		} else if (!transferInfo.equals(other.transferInfo))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (vehicleInventories == null) {
			if (other.vehicleInventories != null)
				return false;
		} else if (!vehicleInventories.equals(other.vehicleInventories))
			return false;
		if (vehicleItems == null) {
			if (other.vehicleItems != null)
				return false;
		} else if (!vehicleItems.equals(other.vehicleItems))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BinnacleCRV [id=" + id + ", licensePlate=" + licensePlate + ", admissionDate=" + admissionDate
				+ ", serialNumber=" + serialNumber + ", motor=" + motor + ", chassis=" + chassis + ", model=" + model
				+ ", year=" + year + ", tonnage=" + tonnage + ", type=" + type + ", brand=" + brand + ", color=" + color
				+ ", service=" + service + ", transferInfo=" + transferInfo + ", admissionAgentIdentification="
				+ admissionAgentIdentification + ", admissionAgentNames=" + admissionAgentNames
				+ ", admissionResponsableIdentification=" + admissionResponsableIdentification
				+ ", admissionResponsableName=" + admissionResponsableName + ", deliveryAgentIdentification="
				+ deliveryAgentIdentification + ", deliveryAgentNames=" + deliveryAgentNames
				+ ", deliveryResponsableIdentification=" + deliveryResponsableIdentification
				+ ", deliveryResponsableNames=" + deliveryResponsableNames + ", admissionType=" + admissionType
				+ ", admissionCategory=" + admissionCategory + ", admissionMean=" + admissionMean + "]";
	}
    
}
