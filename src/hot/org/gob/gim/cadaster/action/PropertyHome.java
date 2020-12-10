package org.gob.gim.cadaster.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.gob.gim.appraisal.facade.AppraisalService;
import org.gob.gim.cadaster.facade.CadasterService;
import org.gob.gim.cadaster.webServiceConsumption.PropertyRegistrationService;
import org.gob.gim.cadaster.webServiceConsumption.PropertyWs;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import ec.gob.gim.ant.ucot.model.CoipDTO;
import ec.gob.gim.appraisal.model.AppraisalPeriod;
import ec.gob.gim.cadaster.model.Appraisal;
import ec.gob.gim.cadaster.model.Block;
import ec.gob.gim.cadaster.model.BlockLimit;
import ec.gob.gim.cadaster.model.Boundary;
import ec.gob.gim.cadaster.model.Building;
import ec.gob.gim.cadaster.model.BuildingMaterialValue;
import ec.gob.gim.cadaster.model.CompassPoint;
import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.DomainOwner;
import ec.gob.gim.cadaster.model.DomainOwnerType;
import ec.gob.gim.cadaster.model.FenceMaterial;
import ec.gob.gim.cadaster.model.LandUse;
import ec.gob.gim.cadaster.model.Location;
import ec.gob.gim.cadaster.model.LotPosition;
import ec.gob.gim.cadaster.model.Neighborhood;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.PropertyLandUse;
import ec.gob.gim.cadaster.model.PropertyType;
import ec.gob.gim.cadaster.model.StreetMaterial;
import ec.gob.gim.cadaster.model.StructureMaterial;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.cadaster.model.TransferDomainComplement;
import ec.gob.gim.cadaster.model.dto.BoundaryDTO;
import ec.gob.gim.cadaster.model.dto.BuildingDTO;
import ec.gob.gim.cadaster.model.dto.CadastralCertificateDTO;
import ec.gob.gim.cadaster.model.dto.DomainHistoryDTO;
import ec.gob.gim.cadaster.model.dto.InformationToCalculateCEMDto;
import ec.gob.gim.cadaster.model.dto.DomainTransferDTO;
import ec.gob.gim.cadaster.model.dto.PropertyHistoryDTO;
import ec.gob.gim.common.model.Attachment;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.CheckingRecord;
import ec.gob.gim.common.model.CheckingRecordType;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.IdentificationType; 
import ec.gob.gim.common.model.LegalEntity;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.waterservice.model.WaterSupply;

import java.util.Arrays;

@Name("propertyHome")
public class PropertyHome extends EntityHome<Property> {

	private static final long serialVersionUID = 1L;

	public static String CADASTER_SERVICE_NAME = "/gim/CadasterService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	public static String APPRAISAL_SERVICE_NAME = "/gim/AppraisalService/local";

	public static int lengthPreviousCadastralCode = 15;

	@Logger
	Log logger;

	@In(create = true)
	TerritorialDivisionHome territorialDivisionHome;

	@In(create = true, value = "defaultProvince")
	TerritorialDivision province;

	@In(create = true, value = "defaultCanton")
	TerritorialDivision canton;

	@In
	UserSession userSession;

	@In
	FacesMessages facesMessages;

	@In(create = true)
	BlockHome blockHome;
	@In(create = true)
	FenceMaterialHome fenceMaterialHome;
	@In(create = true)
	LocationHome locationHome;
	@In(create = true)
	LotPositionHome lotPositionHome;
	@In(create = true)
	PropertyTypeHome propertyTypeHome;
	@In(create = true)
	StreetMaterialHome streetMaterialHome;
	@In(create = true)
	DomainHome domainHome;

	private List<CheckingRecord> checkingRecords;
	private List<CheckingRecord> checkingRecordsForProperty;
	private CheckingRecordType checkingRecordType;

	private boolean isFromTemplate;
	private boolean exoneration = Boolean.FALSE;
	private boolean isUrban = true;
	private boolean isFirstCalculate = true;
	private boolean fromAddMortgage;
	private boolean changeOwner;
	private boolean changedOwnerComplete = false;
	private boolean confirmChangeOwner;
	private boolean isFirstTime = true;

	private String reasonExoneration;
	private String editObservation;
	private String criteria;
	private String criteriaEntry;
	private String identificationNumber;
	private Date startDate;
	private Date endDate;
	private SystemParameterService systemParameterService;
	private TerritorialDivision parish;
	private TerritorialDivision zone;
	private TerritorialDivision sector;
	private BlockLimit blockLimit;
	private Building building;
	private Boundary boundary;
	private Domain domain;
	private Neighborhood neighborhood;
	private Long propertyTypeId;
	private Resident owner = new Person();
	private Charge appraisalCharge;
	private Charge procuratorCharge;
	private Delegate appraisalDelegate;
	private Delegate procuratorDelegate;
	private String tipoTramite;
	private String areaParTer;
	private String areaParCon;
	private String mensaje;
	private String parroquiaAnt;
	private String claveAnt;

	private List<Resident> residents;
	private List<TerritorialDivision> zones;
	private List<TerritorialDivision> sectors;
	private List<Block> blocks;
	private List<BlockLimit> limits;

	private AppraisalPeriod appraisalPeriod;
	private Appraisal appraisal;
	private int anioAppraisal = 2014;

	/* historial cambios propiedad y dominio */
	private Property selectedPropertyViewHistory;
	private List<PropertyHistoryDTO> listPropertyHistory = new ArrayList<PropertyHistoryDTO>();
	private List<DomainHistoryDTO> listDomainHistory = new ArrayList<DomainHistoryDTO>();
	private Long propertySelectedId;
	private String propertySelectedCadastralCode;

	// constructorrrr...................
	private Property property;

	private CadasterService cadasterService;

	private CadastralCertificateDTO _dataCadastralCerfificate;
	
	//macartuche
	//2019-11-27
	private String riskParameter="";
	private String threatParamter="";
	
	//rfam
	//2020-02-21
	private DomainOwner domainOwner;
	
	public DomainOwner getDomainOwner() {
		return domainOwner;
	}

	public void setDomainOwner(DomainOwner domainOwner) {
		this.domainOwner = domainOwner;
	}


	private PropertyWs propertyWs;
	
	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public void setPropertyId(Long id) {
		setId(id);
		this.propertySelectedId = id;
	}

	public Long getPropertyId() {
		return (Long) getId();
	}

	public List<PropertyHistoryDTO> getListPropertyHistory() {
		return listPropertyHistory;
	}

	public void setListPropertyHistory(
			List<PropertyHistoryDTO> listPropertyHistory) {
		this.listPropertyHistory = listPropertyHistory;
	}

	public List<DomainHistoryDTO> getListDomainHistory() {
		return listDomainHistory;
	}

	public void setListDomainHistory(List<DomainHistoryDTO> listDomainHistory) {
		this.listDomainHistory = listDomainHistory;
	}

	public Property getSelectedPropertyViewHistory() {
		return selectedPropertyViewHistory;
	}

	public void setSelectedPropertyViewHistory(
			Property selectedPropertyViewHistory) {
		this.selectedPropertyViewHistory = selectedPropertyViewHistory;
	}

	public Long getPropertySelectedId() {
		return propertySelectedId;
	}

	public void setPropertySelectedId(Long propertySelectedId) {
		this.propertySelectedId = propertySelectedId;
	}

	public String getPropertySelectedCadastralCode() {
		return propertySelectedCadastralCode;
	}

	public void setPropertySelectedCadastralCode(
			String propertySelectedCadastralCode) {
		this.propertySelectedCadastralCode = propertySelectedCadastralCode;
	}
	
	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	/**
	 * Verifica si ya existe la clave catastral
	 * 
	 * @return true si existe, false si no existe
	 */
	@SuppressWarnings("unchecked")
	private boolean existsCadastralCode() {
		List<Property> list = getPersistenceContext()
				.createNamedQuery("Property.findByCadastralCode")
				.setParameter("criteria", this.getInstance().getCadastralCode())
				.getResultList();
		if (list != null && list.size() > 0) {
			if (this.getInstance().getId() == null || list.size() > 1
					|| this.getInstance().getId() != list.get(0).getId()) {
				return true;
			}
			return false;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private PropertyType findPropertyType(Long id) {
		List<?> list = getPersistenceContext()
				.createNamedQuery("PropertyType.findById")
				.setParameter("id", id).getResultList();
		if (list != null && list.size() > 0) {
			return (PropertyType) list.get(0);
		}
		return null;
	}

	/**
	 * Agrega un checkingRecordType al property
	 * 
	 * @param checkingRecordType
	 */
	public void addCheck(CheckingRecordType checkingRecordType) {
		CheckingRecord cr = new CheckingRecord();
		cr.setChecker(userSession.getPerson());
		cr.setCheckingRecordType(checkingRecordType);
		cr.setDate(new Date());
		cr.setTime(new Date());
		cr.setObservation(editObservation);
		this.getInstance().add(cr);
	}

	/**
	 * Guarda o actualiza el Property
	 * 
	 * @return String 'persisted' en caso de guardar, 'updated' en caso de
	 *         actualizar o 'failed' en caso de error
	 */
	public String save() {
		// TODO Auto-generated method stub
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);

		if (existsCadastralCode()) {
			String message = Interpolator.instance().interpolate(
					"#{messages['property.existsCadastralCode']}",
					new Object[0]);
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}

		if (isUrban) {
			if (editObservation != null)
				editObservation = editObservation.trim();
			if ((editObservation == null) || (editObservation.length() < 50)) {
				String message = Interpolator.instance().interpolate(
						"#{messages['property.changeHistoryError']}",
						new Object[0]);
				facesMessages
						.addToControl(
								"",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
				return "failed";
			}
		}
		CheckingRecordType checkingRecordType = null;

		if (isManaged()) {
			checkingRecordType = CheckingRecordType.CHECKED;
		} else {
			checkingRecordType = CheckingRecordType.REGISTERED;
		}

		if (isUrban) {
			/*
			 * if (!isValidPreviousCadastralCode()) { String message =
			 * Interpolator.instance()
			 * .interpolate("#{messages['property.errorPreviousCadastralCode']}"
			 * , new Object[0]); facesMessages.addToControl("",
			 * org.jboss.seam.international.StatusMessage.Severity.ERROR,
			 * message); return "failed"; }
			 */
			if (!isValidAliquotForProperty()) {
				String message = Interpolator.instance().interpolate(
						"#{messages['property.errorPropertyAliquots']}",
						new Object[0]);
				facesMessages
						.addToControl(
								"",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
				return "failed";
			}
		}
		if (isUrban)
			calculateUrbanAppraisalOnlyProperty(this.instance);

		addCheck(checkingRecordType);

		domainHome.setInstance(this.getInstance().getCurrentDomain());
		domainHome.addCheck(checkingRecordType);

		if (!isUrban)
			this.getInstance().setBlock(null);

		if (isManaged()) {
			// if (this.instance.getAppraisal() != null){
			// Appraisal appraisal = new Appraisal();
			// this.instance.getAppraisal()
			// appraisal.setLot(this.instance.getCurrentDomain().getLotAppraisal());
			// appraisal.setBuilding(this.instance.getCurrentDomain().getBuildingAppraisal());
			// appraisal.setCommercialAppraisal(this.instance.getCurrentDomain().getCommercialAppraisal());
			// appraisal.setValueBySquareMeter(this.instance.getCurrentDomain().getValueBySquareMeter());
			// appraisal.setDomain(this.instance.getCurrentDomain());
			// }
			//this.instance.setPhoto(null); 
		    //this.instance.setSketch(null); 
			return super.update();
		} else {
			this.getInstance().getCurrentDomain().setChangeOwnerConfirmed(true);
			this.getInstance().getCurrentDomain().setCreationDate(new Date());
			if (this.getInstance().getLotAliquot() == null) {
				this.getInstance().setLotAliquot(new BigDecimal(100));
			}
			if (this.getInstance().getBuildingAliquot() == null) {
				this.getInstance().setBuildingAliquot(new BigDecimal(100));
			}
			if (isUrban) {
				// if (appraisalChange){
				// Appraisal appraisal = new Appraisal();
				// appraisal.setLot(this.instance.getCurrentDomain().getLotAppraisal());
				// appraisal.setBuilding(this.instance.getCurrentDomain().getBuildingAppraisal());
				// appraisal.setCommercialAppraisal(this.instance.getCurrentDomain().getCommercialAppraisal());
				// appraisal.setValueBySquareMeter(this.instance.getCurrentDomain().getValueBySquareMeter());
				// appraisal.setDomain(this.instance.getCurrentDomain());
				// }
				this.getInstance().setPropertyType(
						(PropertyType) systemParameterService.materialize(
								PropertyType.class, "PROPERTY_TYPE_ID_URBAN"));
			} else {
				this.getInstance().setPropertyType(
						(PropertyType) systemParameterService.materialize(
								PropertyType.class, "PROPERTY_TYPE_ID_RUSTIC"));
			}

			return super.persist();

		}
	}

	/**
	 * Elimina una propiedad lógicamente
	 * 
	 * @return String 'updated' en caso de actualizar o 'failed' en caso de
	 *         error
	 */
	public String deleteProperty() {
		this.getInstance().setDeleted(Boolean.TRUE);
		if (isUrban) {
			if (editObservation != null)
				editObservation = editObservation.trim();
			if ((editObservation == null) || (editObservation.length() < 50)) {
				String message = Interpolator.instance().interpolate(
						"#{messages['property.changeHistoryError']}",
						new Object[0]);
				facesMessages
						.addToControl(
								"",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
				return "failed";
			}
		}
		this.addCheck(CheckingRecordType.DELETED);
		// this.getInstance().setObservations(this.getInstance().getPreviousCadastralCode()
		// + " - " + this.getInstance().getCadastralCode());
		return super.update();
	}

	private List<CheckingRecord> findCheckingRecords(Date start, Date end,
			CheckingRecordType ct) {
		Query query = getPersistenceContext().createNamedQuery(
				"Property.findCheckingRecordsByChekingRecordTypeAndDates");
		query.setParameter("checkingRecordType", ct);
		query.setParameter("startDate", start);
		query.setParameter("endDate", end);
		return query.getResultList();
	}

	public void findChekingRecords() {
		if (checkingRecordType == null) {
			checkingRecords = findCheckingRecords(startDate, endDate,
					CheckingRecordType.REGISTERED);
			checkingRecords.addAll(findCheckingRecords(startDate, endDate,
					CheckingRecordType.CHECKED));
		} else {
			checkingRecords = findCheckingRecords(startDate, endDate,
					checkingRecordType);
		}
	}

	/**
	 * Solo actualiza la propiedad
	 * 
	 * @return String 'updated' en caso de actualizar o 'failed' en caso de
	 *         error
	 */
	public String onlyUpdate() {
		// addCheck(CheckingRecordType.CHECKED);
		if (!isUrban)
			this.getInstance().setBlock(null);
		return super.update();
	}

	private BigDecimal major(BigDecimal a, BigDecimal b) {
		if (a.compareTo(b) == -1) {
			return b;
		}
		return a;
	}

	/**
	 * Calcula el valor de transaccion: el mayor de: valor de transacción o
	 * avalúo comercial
	 * 
	 * @return BigDecimal valor de transacción
	 */
	public BigDecimal calculateValueTransaction() {
		if (this.getInstance().getCurrentDomain() != null) {
			if (this.getInstance().getCurrentDomain().getValueTransaction() == null)
				this.getInstance().getCurrentDomain()
						.setValueTransaction(new BigDecimal(0));
			if (this.getInstance().getCurrentDomain().getCommercialAppraisal() == null)
				this.getInstance().getCurrentDomain()
						.setCommercialAppraisal(new BigDecimal(0));
			return major(this.getInstance().getCurrentDomain()
					.getValueTransaction(), this.getInstance()
					.getCurrentDomain().getCommercialAppraisal());
		}

		return new BigDecimal(0);

	}

	/**
	 * Calcula el avalúo del terreno
	 */
	public void calculateLotAppraisal() {
		if (this.getInstance().getArea() != null
				&& this.getInstance().getCurrentDomain()
						.getValueBySquareMeter() != null) {
			this.getInstance()
					.getCurrentDomain()
					.setLotAppraisal(
							this.getInstance()
									.getArea()
									.multiply(
											this.getInstance()
													.getCurrentDomain()
													.getValueBySquareMeter()));
		}
	}

	/**
	 * Calcula el avalúo de construcción
	 */
	public void calculateBuildingAppraisal() {
		if (this.getInstance().getBuildings().get(0).getArea() != null
				&& this.getInstance().getBuildings().get(0)
						.getStructureMaterial() != null) {
			BigDecimal appraisal = findValueBySquareMeterByStructureMaterialAndFiscalPeriod(this
					.getInstance().getBuildings().get(0).getStructureMaterial());
			if (appraisal == null) {
				this.getInstance().getCurrentDomain()
						.setBuildingAppraisal(new BigDecimal(0));
				String message = Interpolator
						.instance()
						.interpolate(
								"#{messages['buildingMaterialValue.noValueInFiscalPeriod']}",
								new Object[0]);
				facesMessages
						.addToControl(
								"structureMaterial",
								org.jboss.seam.international.StatusMessage.Severity.INFO,
								message);
				return;
			}
			this.getInstance()
					.getCurrentDomain()
					.setBuildingAppraisal(
							this.getInstance().getBuildings().get(0).getArea()
									.multiply(appraisal));
		}
		calculateCommercialAppraisal();
	}

	private BigDecimal findValueBySquareMeterByStructureMaterialAndFiscalPeriod(
			StructureMaterial s) {
		Query query = getPersistenceContext().createNamedQuery(
				"BuildingMaterialValue.findByStructureMaterialAndFiscalPeriod");
		query.setParameter("structureMaterial", s);
		query.setParameter("fiscalPeriodId", userSession.getFiscalPeriod()
				.getId());
		List<?> result = query.getResultList();
		if (result != null && result.size() > 0)
			return ((BuildingMaterialValue) result.get(0))
					.getValueBySquareMeter();
		return null;
	}

	/**
	 * Actualización cambio de propietario
	 * 
	 * @return String 'updated' en caso de actualizar, 'failed' en caso de error
	 * @throws Exception
	 */
	public String changeOwnerUpdate() throws Exception {
		Redirect r = Redirect.instance();
		r.setViewId("/cadaster/ChangeOwnerProperty.xhtml");
		if (domainHome.getInstance().getResident() == null) {
			String message = Interpolator.instance().interpolate(
					"#{messages['resident.required']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			r.execute();
			return "failed";
		}

		if (!isUrban)
			this.getInstance().setBlock(null);

		this.getInstance().setUrban(isUrban);
		// rarmijos 2015-11-23
		this.getInstance().setNeedConfirmChangeOwner(Boolean.TRUE);

		domainHome.getInstance().setCreationDate(new Date());
		domainHome.getInstance().setCreationTime(new Date());
		domainHome.getInstance().setUserRegister(userSession.getUser());
		domainHome.getInstance().setLotAreaTransfer(
				this.calculateAliquotLotArea());
		domainHome.getInstance().setBuildingAreaTransfer(
				this.calculateAliquotConstructionArea());
		// if
		// (domainHome.getInstance().getPurchaseType().getName().trim().equalsIgnoreCase("REMATE"))
		// {
		// domainHome.getInstance().setValueForCalculate(domainHome.getInstance().getValueTransaction());
		// } else {
		// domainHome.getInstance().setValueForCalculate(major(domainHome.getInstance().getValueTransaction(),domainHome.getInstance().getCommercialAppraisal()));
		// }
		if (getInstance().getCurrentDomain().getValueTransaction() == null)
			getInstance().getCurrentDomain().setValueTransaction(
					new BigDecimal(0));

		// CadasterService cadasterService =
		// ServiceLocator.getInstance().findResource(CADASTER_SERVICE_NAME);
		// try {
		// this.getInstance().setTotalYearsFromLastChangeOwner(yearsBetween(this.getInstance().getCurrentDomain().getDate(),
		// domainHome.getInstance().getCreationDate()));
		// getInstance().getCurrentDomain().setValueForCalculate(getInstance().getCurrentDomain().getValueTransaction());
		// if (isUrban) {
		// cadasterService.preEmitUtilityTax(this.getInstance(),domainHome.getInstance(),userSession.getFiscalPeriod(),
		// userSession.getPerson());
		// }
		// getInstance().getCurrentDomain().setValueForCalculate(major(getInstance().getCurrentDomain().getValueTransaction(),getInstance().getCurrentDomain().getCommercialAppraisal()));
		// if (!isUrban)this.getInstance().setBlock(null);
		// cadasterService.preEmitAlcabalaTax(this.getInstance(),domainHome.getInstance(),
		// userSession.getFiscalPeriod(),userSession.getPerson(), exoneration,
		// reasonExoneration);
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// String message =
		// Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}",
		// new Object[0]);
		// facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
		// r.execute();
		// return "failed";
		// }
		editObservation = domainHome.getChangeHistory();
		domainHome.addCheck(CheckingRecordType.REGISTERED);
		addCheck(CheckingRecordType.DOMAIN_TRANSFER);
		changedOwnerComplete = true;
		domainHome.getInstance().setPreviousDomain(getInstance().getCurrentDomain());
		r.execute();
		return super.update();
	}
	
	
	// para guardar registro de la informacion adicional en un transferencia
	// Jock Samaniego
	
	private TransferDomainComplement TDC;
	private BigDecimal ABDValue = new BigDecimal(0);	
	private BigDecimal ACDValue = new BigDecimal(0);
	private Property propertyDocument;
	
	public TransferDomainComplement getTDC() {
		return TDC;
	}

	public void setTDC(TransferDomainComplement tDC) {
		TDC = tDC;
	}
	
	public BigDecimal getABDValue() {
		return ABDValue;
	}

	public void setABDValue(BigDecimal aBDValue) {
		ABDValue = aBDValue;
	}

	public BigDecimal getACDValue() {
		return ACDValue;
	}

	public void setACDValue(BigDecimal aCDValue) {
		ACDValue = aCDValue;
	}

	public Property getPropertyDocument() {
		return propertyDocument;
	}

	public void setPropertyDocument(Property propertyDocument) {
		this.propertyDocument = propertyDocument;
	}

	public void calculateDomainValues(BigDecimal valueA, BigDecimal valueB, BigDecimal valueC, BigDecimal valueD){
		ABDValue = new BigDecimal(0);
		ACDValue = new BigDecimal(0);
		if(valueA != null){
			ABDValue = ABDValue.add(valueA);
			ACDValue = ACDValue.add(valueA);
		}
		if(valueB != null){
			ABDValue = ABDValue.add(valueB);
		}
		if(valueC != null){
			ACDValue = ACDValue.add(valueC);
		}
		if(valueD != null){
			ABDValue = ABDValue.add(valueD);
			ACDValue = ACDValue.add(valueD);
		}
	}
	
	public void chargeDomainsToPrintDocument(Long property_id){
		this.propertyDocument = getEntityManager().find(Property.class, property_id);	
		TDC = new TransferDomainComplement();
		TransferDomainComplement TDCaux = new TransferDomainComplement();
		Query query = getEntityManager().createNamedQuery(
				"DomainComplement.findFinalByDomain");
		query.setParameter("domain_id", this.propertyDocument.getCurrentDomain().getId());
		try {
			TDCaux = (TransferDomainComplement) query.getSingleResult();
			TDC.setAcquisitionValue(TDCaux.getAcquisitionValue());
			TDC.setBuildingType(TDCaux.getBuildingType());
			TDC.setCEMValues(TDCaux.getCEMValues());
			TDC.setContractualValue(TDCaux.getContractualValue());
			TDC.setCountry(TDCaux.getCountry());
			TDC.setDocumentsDescription(TDCaux.getDocumentsDescription());
			TDC.setImprovementsValues(TDCaux.getImprovementsValues());
			TDC.setNotary(TDCaux.getNotary());
			TDC.setObservations(TDCaux.getObservations());
			TDC.setOthersValues(TDCaux.getOthersValues());
			TDC.setPreviousParish(TDCaux.getPreviousParish());
			TDC.setPropertyNumber(TDCaux.getPropertyNumber());
			TDC.setProportionalValues(TDCaux.getProportionalValues());
			TDC.setSecondBuyerIdentification(TDCaux.getSecondBuyerIdentification());
			TDC.setSecondBuyerName(TDCaux.getSecondBuyerName());
			TDC.setSecondBuyerTitle(TDCaux.getSecondBuyerTitle());
			TDC.setSecondSellerIdentification(TDCaux.getSecondSellerIdentification());
			TDC.setSecondSellerName(TDCaux.getSecondSellerName());
			TDC.setSecondSellerTitle(TDCaux.getSecondSellerTitle());
			TDC.setValidityDate(TDCaux.getValidityDate());
			TDC.setPreviousOwnerTitle(TDCaux.getPreviousOwnerTitle());
			TDC.setNewOwnerTitle(TDCaux.getNewOwnerTitle());
			this.calculateDomainValues(TDC.getAcquisitionValue(), TDC.getImprovementsValues(), TDC.getCEMValues(), TDC.getOthersValues());
		} catch (Exception e) {
			addFacesMessageFromResourceBundle("no existe información registrada");
		}
	}
	
	public String chargeDomainsFormReport(Long id){
		this.TDC = getEntityManager().find(TransferDomainComplement.class, id);
		Domain currentDomainAux = getEntityManager().find(Domain.class, this.TDC.getDomain().getId());
		this.propertyDocument = getEntityManager().find(Property.class, currentDomainAux.getProperty().getId());
		this.propertyDocument.setCurrentDomain(currentDomainAux);
		return "/cadaster/report/DomainTransferDocumentPDF.xhtml";
	}
	
	public String saveDomainTransferComplement(){
		EntityManager em = getEntityManager();
		TDC.setDomain(this.propertyDocument.getCurrentDomain());
		TDC.setUserDocument(userSession.getPerson().getName());
		em.persist(TDC);
		em.flush();
		Date date = new Date(); 
		TDC.setCreationTime(date);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");  
		String strDate = dateFormat.format(date);  
		TDC.setCodeDocument(strDate+"-"+TDC.getId());
		em.persist(TDC);
		em.flush();
		return "/cadaster/report/DomainTransferDocumentPDF.xhtml";
	}
	
	// para busqueda de domainsTransfer y reportes
	
	private String criteriaCadastralCode;
	private String criteriaDocumentCode;
	private List<DomainTransferDTO> dtDTO;
	
	public String getCriteriaCadastralCode() {
		return criteriaCadastralCode;
	}

	public void setCriteriaCadastralCode(String criteriaCadastralCode) {
		this.criteriaCadastralCode = criteriaCadastralCode;
	}

	public String getCriteriaDocumentCode() {
		return criteriaDocumentCode;
	}

	public void setCriteriaDocumentCode(String criteriaDocumentCode) {
		this.criteriaDocumentCode = criteriaDocumentCode;
	}
	
	public List<DomainTransferDTO> getDtDTO() {
		return dtDTO;
	}

	public void setDtDTO(List<DomainTransferDTO> dtDTO) {
		this.dtDTO = dtDTO;
	}
	
	public void searchDomainTransferReport(){
		dtDTO = new ArrayList();
		if((criteriaCadastralCode != null && criteriaCadastralCode != "") || (criteriaDocumentCode != null && criteriaDocumentCode != "")){
			String query = "Select tdc.codeDocument, tdc.creationDate, p.cadastralCode, pr.name as name1, cr.name as name2, tdc.userDocument, cd.id as id1, tdc.id as id2 "
					+ "from TransferDomainComplement tdc "
					+ "Left Join Domain cd ON tdc.domain_id = cd.id "
					+ "Left Join Domain pd ON pd.id = cd.previousDomain_id "
					+ "Left Join Resident pr ON pr.id = pd.resident_id "
					+ "Left Join Resident cr ON cr.id = cd.resident_id "
					+ "Left Join Property p ON p.id = cd.property_id "
					+ "where tdc.isActive = true ";
			
				if(criteriaCadastralCode != null && criteriaCadastralCode != ""){
					query = query + "and p.cadastralCode =:cadastralCode ";
				}
				if(criteriaDocumentCode != null && criteriaDocumentCode != ""){
					query = query + "and tdc.codeDocument =:codeDocument ";
				}
				
				Query q = this.getEntityManager().createNativeQuery(query);
				
				if(criteriaCadastralCode != null && criteriaCadastralCode != ""){
					q.setParameter("cadastralCode", criteriaCadastralCode);	
				}
				if(criteriaDocumentCode != null && criteriaDocumentCode != ""){
					q.setParameter("codeDocument", criteriaDocumentCode);	
				}
				query = query + "Order by tdc.creationDate DESC";
				dtDTO = NativeQueryResultsMapper.map(q.getResultList(), DomainTransferDTO.class);
		}
	}

	/**
	 * Calcula la diferencia de años entre 2 fechas
	 * 
	 * @param first
	 *            fecha inicial
	 * @param after
	 *            fecha final
	 * @return int numero de años
	 */
	private int yearsBetween(Date first, Date after) {
		Calendar begin = DateUtils.getTruncatedInstance(first);
		Calendar end = DateUtils.getTruncatedInstance(after);

		int y1 = begin.get(Calendar.YEAR);
		int d1 = begin.get(Calendar.DAY_OF_YEAR);
		int y2 = end.get(Calendar.YEAR);
		int d2 = end.get(Calendar.DAY_OF_YEAR);

		int difYears = y2 - y1;
		if (d2 < d1) {
			difYears--;
		}

		return difYears;
	}

	//
	// @Deprecated
	// private void loadTerritorialDivision(Block block) {
	// // if (block == null){
	// // block = new Block();
	// if (sector == null) {
	// this.parish = new TerritorialDivision();
	// this.zone = new TerritorialDivision();
	// TerritorialDivision sector = new TerritorialDivision();
	//
	// this.parish.add(zone);
	// this.zone.add(sector);
	// block.setSector(sector);
	//
	// } else {
	// this.zone = block.getSector().getParent();
	// this.parish = zone.getParent();
	// }
	// // getInstance().setBlock(block);
	// }

	private void loadLocation(Location location) {
		if (location == null) {
			location = new Location();
			// Ojo deberia ir la Inicializacion en el constructor
			// para que funcione en la vista
			// Gerson esta manipulando esas clases;
			location.setNeighborhood(new Neighborhood());
		}
		getInstance().setLocation(location);
	}

	// @Deprecated
	// private void loadCurrentDomain(Domain currentDomain) {
	// if (currentDomain == null) {
	// currentDomain = new Domain();
	// // Ojo deberia ir la Inicializacion en el constructor
	// // para que funcione en la vista
	// // Gerson esta manipulando esas clases;
	// currentDomain.setResident(new Person());
	//
	// getInstance().getDomains().add(currentDomain);
	// }
	// getInstance().setCurrentDomain(currentDomain);
	// }

	/**
	 * Busca un territorialDivision en base a un codigo que se lo toma como
	 * substring de la clave catastral
	 * 
	 * @param x
	 *            posicion inicial para obtener el substring
	 * @param y
	 *            posicion final para obtener el substring
	 * @param territorialDivision
	 *            es el padre del territorialDivision que se va a obtener
	 * @return devuelve un TerritorialDivision
	 */
	private TerritorialDivision findTerritorialDivision(int x, int y,
			TerritorialDivision td) {

		if (this.getInstance().getCadastralCode().length() < y)
			return null;

		String code = this.getInstance().getCadastralCode().substring(x, y);

		Query query = getPersistenceContext().createNamedQuery(
				"TerritorialDivision.findByCodeAndParent");
		query.setParameter("code", code);
		query.setParameter("parent", td);
		List<?> result = query.getResultList();
		if (result != null && result.size() > 0)
			return (TerritorialDivision) result.get(0);

		return null;
	}

	/**
	 * Carga los valores de los territorialDivision en base al código catastral
	 * 
	 */
	public void loadValues() {
		if (!isFirstTime)
			return;

		if (this.getInstance().getCadastralCode() == null)
			return;

		if (parish == null)
			parish = findTerritorialDivision(4, 6,
					territorialDivisionHome.findDefaultCanton());

		if (zone == null)
			zone = findTerritorialDivision(6, 8, parish);

		if (sector == null) {
			sector = findTerritorialDivision(8, 10, zone);
		}

		prepareViewHistory();

		calculateCommercialAppraisal();
	}

	private void rusticLoadValues() {
		if (parish == null)
			parish = findTerritorialDivision(5, 9,
					territorialDivisionHome.findDefaultCanton());

	}

	/**
	 * Calcula el área total de construcción
	 * 
	 * @param building
	 */
	private void updateTotalArea(Building building) {
		if (isUrban) {
			building.setTotalArea(calculateTotalArea(building));
		} else {
			if (building.getArea() != null) {
				building.setTotalArea(building.getArea());
			} else {
				building.setTotalArea(BigDecimal.ZERO);
			}
		}

	}

	public void updateTotalAreaCurrentBuilding() {
		building.setTotalArea(calculateTotalArea(building));
	}

	/**
	 * Calcula el área total de construcción (area por el # de pisos)
	 * 
	 * @param building
	 */
	private BigDecimal calculateTotalArea(Building building) {
		if (building != null && building.getArea() != null
				&& building.getFloorsNumber() != null)
			return building.getArea().multiply(
					BigDecimal.valueOf(building.getFloorsNumber()));
		return new BigDecimal(0);
	}

	/**
	 * Calcula la alicuota del área del lote
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal calculateAliquotLotArea() {
		if (this.getInstance().getLotAliquot() == null)
			return new BigDecimal(0);

		return getInstance().getArea()
				.multiply(this.getInstance().getLotAliquot())
				.divide(new BigDecimal(100));
	}

	/**
	 * Calcula la alicuota del área de construcción
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal calculateAliquotConstructionArea() {

		if (this.getInstance().getBuildingAliquot() == null)
			return new BigDecimal(0);
		if (this.getInstance().getCurrentDomain().getTotalAreaConstruction() == null)
			calculateTotalAreaConstruction();
		return this.getInstance().getCurrentDomain().getTotalAreaConstruction()
				.multiply(this.getInstance().getBuildingAliquot())
				.divide(new BigDecimal(100));
	}

	/**
	 * Calcula el área total de construcción
	 */
	private void calculateTotalAreaConstruction() {
		BigDecimal res = new BigDecimal(0);
		if (this.getInstance() != null) {
			for (Building b : this.getInstance().getBuildings()) {
				if (b.getTotalArea() == null) {
					updateTotalArea(b);
					res = res.add(b.getTotalArea());
				} else {
					res = res.add(b.getTotalArea());
				}
			}
		}

		this.getInstance().getCurrentDomain().setTotalAreaConstruction(res);
	}

	/**
	 * Calcula el avalúo comercial de la propiedad
	 */
	public void calculateCommercialAppraisal() {
		if (this.getInstance() != null
				&& this.getInstance().getCurrentDomain().getBuildingAppraisal() != null
				&& this.getInstance().getCurrentDomain().getLotAppraisal() != null) {
			this.getInstance()
					.getCurrentDomain()
					.setCommercialAppraisal(
							this.getInstance()
									.getCurrentDomain()
									.getBuildingAppraisal()
									.add(this.getInstance().getCurrentDomain()
											.getLotAppraisal()));
		} else {
			this.getInstance().getCurrentDomain()
					.setCommercialAppraisal(new BigDecimal(0));
		}
	}

	/**
	 * Calcula el avalúo comercial del dominio
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal calculateCommercialDomainAppraisal() {
		if (domain == null) {
			return BigDecimal.ZERO;
		}
		if (domain != null && domain.getBuildingAppraisal() != null
				&& domain.getLotAppraisal() != null) {
			domain.setCommercialAppraisal(domain.getBuildingAppraisal().add(
					domain.getLotAppraisal()));
		} else {
			domain.setCommercialAppraisal(new BigDecimal(0));
		}
		return domain.getCommercialAppraisal();
	}

	/**
	 * Completa el cadastralCode y los limites de la propiedad
	 */
	public void populateCadastralCodeAndLimits() {
		populateCadastralCode();
		populateLimits();
		this.getInstance()
				.getCurrentDomain()
				.setValueBySquareMeter(
						this.getInstance().getBlock().getValueBySquareMeter());
		this.getInstance()
				.getLocation()
				.setNeighborhood(
						this.getInstance().getBlock().getNeighborhood());
	}

	/**
	 * Concatena el cadastralcode
	 */
	public void populateCadastralCode() {
		StringBuffer cadastralCodeBuffer = new StringBuffer();
		cadastralCodeBuffer.append(province.getCode());
		cadastralCodeBuffer.append(canton.getCode());

		// logger.info("populateCadastralCode() parishe #0, zone #1", parish,
		// zone);

		// cadastralCodeBuffer.append(parish != null ? parish.getCode() :
		// "0000"); //antigua clave
		cadastralCodeBuffer.append(parish != null ? parish.getCode() : "00"); // nueva
																				// clave
																				// catastral
		cadastralCodeBuffer.append(zone != null ? zone.getCode() : "00");
		cadastralCodeBuffer.append(sector != null ? sector.getCode() : "00");
		cadastralCodeBuffer.append(this.getInstance().getBlock() != null
				&& this.getInstance().getBlock().getId() != null ? this
				.getInstance().getBlock().getCode() : "000");
		cadastralCodeBuffer.append(getInstance().getFormattedNumber());
		cadastralCodeBuffer.append(getInstance().getFormattedBuildingNumber());
		cadastralCodeBuffer.append(getInstance().getFormattedFloorNumber());
		cadastralCodeBuffer.append(getInstance()
				.getFormattedHousingUnitNumber());
		getInstance().setCadastralCode(cadastralCodeBuffer.toString());

		// getInstance().setPreviousCadastralCode(cadastralCodeBuffer.toString());

		// logger.info("--- Updated cadastreCode #0",
		// getInstance().getCadastralCode());
	}

	private Location createLocation(Location l) {
		if (l == null)
			return null;
		Location loc = new Location();
		loc.setMainBlockLimit(l.getMainBlockLimit());
		loc.setNeighborhood(l.getNeighborhood());
		return loc;
	}

	/**
	 * Crea y agrega PropertyLandUse a Property
	 * 
	 * @param list
	 *            lista de PropertyLandUse
	 * @param property
	 *            Propiedad
	 */
	private void createPropertyLandUse(List<PropertyLandUse> list,
			Property property) {
		if (list == null || list.size() == 0)
			return;

		for (PropertyLandUse pl : list) {
			PropertyLandUse plu = new PropertyLandUse();
			plu.setHasOperatingPermit(pl.getHasOperatingPermit());
			plu.setLandUse(pl.getLandUse());
			plu.setSlices(pl.getSlices());
			property.add(plu);
		}

	}

	/**
	 * Crea y agrega Boundary a Domain
	 * 
	 * @param list
	 *            lista de Boundary
	 * @param domain
	 *            Dominio
	 */
	private void createBoundaries(List<Boundary> list, Domain domain) {
		if (list == null || list.size() == 0)
			return;

		for (Boundary b : list) {
			Boundary boundary = new Boundary();
			boundary.setCompassPoint(b.getCompassPoint());
			boundary.setDescription(b.getDescription());
			boundary.setLength(b.getLength());
			domain.add(boundary);
		}

	}

	/**
	 * Crea y agrega Building a Property
	 * 
	 * @param list
	 *            lista de Building
	 * @param property
	 *            Propiedad
	 */
	private void createBuildingCopy(List<Building> list, Property property) {
		if (list == null || list.size() == 0)
			return;

		for (Building b : list) {
			Building bc = new Building();
			bc.setAnioConst(b.getAnioConst());
			bc.setArea(b.getArea());
			bc.setBuildingYear(b.getBuildingYear());
			bc.setExternalFinishing(b.getExternalFinishing());
			bc.setFloorsNumber(b.getFloorsNumber());
			bc.setHasEquipment(b.getHasEquipment());
			bc.setIsFinished(b.getIsFinished());
			bc.setNumber(b.getNumber());
			bc.setPreservationState(b.getPreservationState());
			bc.setRoofMaterial(b.getRoofMaterial());
			bc.setStructureMaterial(b.getStructureMaterial());
			bc.setTotalArea(b.getTotalArea());
			bc.setWallMaterial(b.getWallMaterial());
			property.add(bc);
		}

	}

	/**
	 * Crea una propiedad, en base a otra propiedad ya existente
	 */
	private void copyFromTemplate() {
		Property p = new Property();
		p.setArea(this.getInstance().getArea());
		p.setFront(this.getInstance().getFront());
		p.setFrontsLength(this.getInstance().getFrontsLength());
		p.setFrontsNumber(this.getInstance().getFrontsNumber());
		p.setBlock(this.getInstance().getBlock());
		p.setBuildingNumber(this.getInstance().getBuildingNumber());
		p.setFenceMaterial(this.getInstance().getFenceMaterial());
		p.setFloorNumber(this.getInstance().getFloorNumber());
		p.setGarbageCollection(this.getInstance().getGarbageCollection());
		p.setHasElectricity(this.getInstance().getHasElectricity());
		p.setHasFrontFence(this.getInstance().getHasFrontFence());
		p.setHasPerimeterFence(this.getInstance().getHasPerimeterFence());
		p.setHasTelephoneNetwork(this.getInstance().getHasTelephoneNetwork());
		p.setHasWaterService(this.getInstance().getHasWaterService());
		p.setHousingUnitNumber(this.getInstance().getHousingUnitNumber());
		p.setLocation(createLocation(this.getInstance().getLocation()));
		createPropertyLandUse(this.getInstance().getPropertyLandUses(), p);
		createBuildingCopy(this.getInstance().getBuildings(), p);
		p.setLotPosition(this.getInstance().getLotPosition());
		p.setLotTopography(this.getInstance().getLotTopography());
		p.setNumber(this.getInstance().getNumber());
		p.setPropertyType(this.getInstance().getPropertyType());
		p.setSewerage(this.getInstance().getSewerage());
		p.setSide(this.getInstance().getSide());
		p.setSidewalk(this.getInstance().getSidewalk());
		p.setSidewalkWidth(this.getInstance().getSidewalkWidth());
		p.setStreetMaterial(this.getInstance().getStreetMaterial());
		p.setStreetWidth(this.getInstance().getStreetWidth());
		p.setTelephoneLinesNumber(this.getInstance().getTelephoneLinesNumber());
		p.setElectricMetersNumber(this.getInstance().getElectricMetersNumber());
		p.setWaterMetersNumber(this.getInstance().getWaterMetersNumber());
		p.setCadastralCode(this.getInstance().getCadastralCode());
		p.setPhoto(this.getInstance().getPhoto());
		p.setPreviousCadastralCode(this.getInstance()
				.getPreviousCadastralCode());
		p.setDuplicate(true);
		this.setInstance(p);
	}

	public void wire() {
		// System.out.println("--Ingreso a wire PropertyHome isfirsttime" +
		// isFirstTime);

		getDefinedInstance();

		if (isFromTemplate && isFirstTime) {
			isFromTemplate = false;
			copyFromTemplate();
		}

		loadLocation(this.getInstance().getLocation());

		neighborhood = this.getInstance().getLocation().getNeighborhood();

		FenceMaterial fenceMaterial = fenceMaterialHome.getDefinedInstance();
		if (fenceMaterial != null) {
			getInstance().setFenceMaterial(fenceMaterial);
		}

		LotPosition lotPosition = lotPositionHome.getDefinedInstance();
		if (lotPosition != null) {
			getInstance().setLotPosition(lotPosition);
		}
		PropertyType propertyType = propertyTypeHome.getDefinedInstance();
		if (propertyType != null) {
			getInstance().setPropertyType(propertyType);
		}
		StreetMaterial streetMaterial = streetMaterialHome.getDefinedInstance();
		if (streetMaterial != null) {
			getInstance().setStreetMaterial(streetMaterial);
		}
		Domain currentDomain = domainHome.getDefinedInstance();

		if (currentDomain != null) {
			getInstance().setCurrentDomain(currentDomain);
		}
		if (this.getInstance().getCurrentDomain().getResident() != null) {
			identificationNumber = getInstance().getCurrentDomain()
					.getResident().getIdentificationNumber();
		}
		limits = getInstance().getBlock() != null ? getInstance().getBlock()
				.getLimits() : new ArrayList<BlockLimit>();

		blockLimit = getInstance().getLocation().getMainBlockLimit();

		if (getInstance().getLotAliquot() == null) {
			if (getInstance().isDuplicate())
				getInstance().setLotAliquot(new BigDecimal(0));
			else
				getInstance().setLotAliquot(new BigDecimal(100));
		}

		if (getInstance().getBuildingAliquot() == null) {
			if (getInstance().isDuplicate())
				getInstance().setBuildingAliquot(new BigDecimal(0));
			else
				getInstance().setBuildingAliquot(new BigDecimal(100));
		}

		calculateCommercialAppraisal();
		calculateTotalAreaConstruction();

		anioAppraisal = GregorianCalendar.getInstance().get(Calendar.YEAR);
		// System.out.println(">>>>>>>>>>>>>>>>> anioAppraisal: " +
		// anioAppraisal);
		if (appraisalPeriod == null)
			appraisalPeriod = findActiveAppraisalPeriod();

		if (!isUrban) {
			this.getInstance().setBlock(null);
		}

		checkingRecordsForProperty = findCheckingRecordsForProperty();
		// System.out.println("--sale de wire" + this.instance);

		//macartuche 2019-11-27
		//parametros para riesgo y amenaza
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		
		this.riskParameter = (String) systemParameterService.findParameter("URBAN_PARISH_CODES");
		this.threatParamter = (String) systemParameterService.findParameter("URBANPARISH_PARISH_CODES");
		
	}

	public void rusticWire() {

		if (!isFirstTime)
			return;

		//logger.info("--Ingreso a rustic wire", this);
		getDefinedInstance();

		PropertyType propertyType = propertyTypeHome.getDefinedInstance();
		if (propertyType != null) {
			getInstance().setPropertyType(propertyType);
		}

		Domain currentDomain = domainHome.getDefinedInstance();
		if (currentDomain != null) {
			getInstance().setCurrentDomain(currentDomain);
		}
		if (this.getInstance().getCurrentDomain().getResident() != null) {
			identificationNumber = getInstance().getCurrentDomain()
					.getResident().getIdentificationNumber();
		}

		if (this.getInstance().getCadastralCode() == null)
			populateCadastralCode();

		if (!isUrban && isFirstTime) {
			if (this.getInstance().getBuildings().size() == 0) {
				this.getInstance().add(new Building());
			}

			this.getInstance().setBlock(null);
			rusticLoadValues();
			isFirstTime = false;
		}
		calculateCommercialAppraisal();
	}

	public boolean isWired() {
		return true;
	}

	public Property getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	/**
	 * Ordena los dominios por fechas
	 * 
	 * @param domains
	 *            dominios quse se va a ordenar
	 * @return ArrayList<Domain>
	 */
	private ArrayList<Domain> orderByDate(ArrayList<Domain> domains) {
		ArrayList<Date> dates = new ArrayList<Date>();
		ArrayList<Domain> orderDomains = new ArrayList<Domain>();
		for (Domain d : domains) {
			if (d.getDate() != null)
				dates.add(d.getDate());
		}
		Collections.sort(dates);
		ArrayList<Date> descOrderDates = new ArrayList<Date>();
		for (int i = dates.size() - 1; i >= 0; i--) {
			descOrderDates.add(dates.get(i));
		}
		for (int i = 0; i < descOrderDates.size(); i++) {
			for (int j = 0; j < domains.size(); j++) {
				if (descOrderDates.get(i).equals(domains.get(j).getDate())
						&& !orderDomains.contains(domains.get(j))) {
					orderDomains.add(domains.get(j));
				}
			}
		}
		for (Domain d : domains) {
			if (d.getDate() == null)
				orderDomains.add(0, d);
		}
		return orderDomains;
	}

	public List<Domain> getDomains() {
		if (getInstance() == null || getInstance().getDomains() == null)
			return new ArrayList<Domain>();
		ArrayList<Domain> domains = new ArrayList<Domain>();
		for (int i = 0; i < getInstance().getDomains().size(); i++) {
			if (getInstance().getDomains().get(i).getDate() == null)
				domains.add(getInstance().getDomains().get(i));
		}
		return orderByDate(domains);
	}

	/**
	 * Devuelve los dominios en los que se ha confimado el traspaso
	 * 
	 * @return List<Domain>
	 */
	public List<Domain> getConfirmDomains() {
		if (getInstance() == null || getInstance().getDomains() == null)
			return new ArrayList<Domain>();
		ArrayList<Domain> domains = new ArrayList<Domain>();
		for (int i = 0; i < getInstance().getDomains().size(); i++) {
			if (getInstance().getDomains().get(i).getChangeOwnerConfirmed() != null
					&& getInstance().getDomains().get(i)
							.getChangeOwnerConfirmed())
				domains.add(getInstance().getDomains().get(i));
		}
		return orderByDate(domains);
	}

	/**
	 * Devuelve las construcciones ordenadas por número y actualizada el total
	 * del área
	 * 
	 * @return List<Building>
	 */
	public List<Building> getBuildings() {
		if (getInstance() != null && isFirstCalculate) {
			for (Building b : getInstance().getBuildings()) {
				updateTotalArea(b);
			}
			calculateTotalAreaConstruction();
			isFirstCalculate = false;
		}
		if (this.getInstance() != null
				&& this.getInstance().getBuildings().size() > 1) {
			List<Integer> numbers = new ArrayList<Integer>();
			List<Building> buildings = new ArrayList<Building>();
			for (Building b : getInstance().getBuildings()) {
				numbers.add(b.getNumber());
			}
			Collections.sort(numbers);
			for (Integer i : numbers) {
				for (Building b : getInstance().getBuildings()) {
					if (b.getNumber() == i) {
						buildings.add(b);
					}
				}
			}
			return buildings;
		}

		return getInstance() == null ? null : new ArrayList<Building>(
				getInstance().getBuildings());
	}

	public List<PropertyLandUse> getPropertyLandUses() {
		return getInstance() == null ? null : new ArrayList<PropertyLandUse>(
				getInstance().getPropertyLandUses());
	}

	public List<WaterSupply> getWaterSupplies() {
		return getInstance() == null ? null : new ArrayList<WaterSupply>(
				getInstance().getWaterSupplies());
	}

	/**
	 * Busca residents por un criterio
	 */
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		//logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	/**
	 * Busca resident por número de identificación
	 */
	public void searchResident() {
		//logger.info("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			//logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			// resident.add(this.getInstance());
			this.getInstance().getCurrentDomain().setResident(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().getCurrentDomain().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	/**
	 * Selecciona el resident del modalPanel del residentChooser
	 * 
	 * @param event
	 */
	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().getCurrentDomain().setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	/**
	 * Limpia panel de búsqueda de los resident
	 */
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	@SuppressWarnings("unchecked")
	private List<TerritorialDivision> findTerritorialDivisions(Long parentId) {
		Query query = getPersistenceContext().createNamedQuery(
				"TerritorialDivision.findByParent");
		query.setParameter("parentId", parentId);
		List<TerritorialDivision> td = query.getResultList();
		return td;
	}

	/**
	 * 
	 * @return Las parroquias de acuerdo al canton
	 */

	/*
	 * @SuppressWarnings("unchecked") public List<TerritorialDivision>
	 * getParishes(){ TerritorialDivision canton =
	 * territorialDivisionHome.findDefaultCanton(); Query query =
	 * getEntityManager().createNamedQuery("TerritorialDivision.findByParent");
	 * query.setParameter("parentId", canton.getId()); return
	 * query.getResultList(); }
	 */

	public List<TerritorialDivision> findParishes(Long defaultCantonId) {
		// return findTerritorialDivisions(defaultCantonId);
		// @tag cambioClave
		return findTerritorialDivisionsNew(defaultCantonId);
	}

	/**
	 * macartuche
	 * 
	 * @tag cambioClave
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<TerritorialDivision> findTerritorialDivisionsNew(Long parentId) {
		Query query = getPersistenceContext().createNamedQuery(
				"TerritorialDivision.findByParentNew");
		query.setParameter("parentId", parentId);
		query.setParameter("classifierGeo", Boolean.TRUE);
		return query.getResultList();
	}

	public List<TerritorialDivision> findZones() {
		if (parish != null) {
			return findTerritorialDivisions(parish.getId());
		}
		return new ArrayList<TerritorialDivision>();
	}

	/**
	 * Pone a null la zona, sector y el código catastral cuando hay un cambio en
	 * la parroquia
	 * 
	 */
	public void resetAll() {
		zone = null;
		resetCadastralCode();
		resetSector();
		resetBlock();
		resetLimits();
	}

	/**
	 * Pone a null la manzana límite, y el barrio
	 */
	public void resetLimits() {
		limits = null;
		this.getInstance().getLocation().setMainBlockLimit(null);
		this.getInstance().getLocation().setNeighborhood(null);
	}

	public void resetCadastralCode() {
		this.getInstance().setCadastralCode(null);
	}

	public void resetSector() {
		sector = null;
		resetCadastralCode();
		resetBlock();
	}

	public void resetBlock() {
		resetCadastralCode();
		// setBlock(null);
		this.getInstance().setBlock(new Block());
	}

	/**
	 * Encuentra las zonas por parroquia
	 */
	@SuppressWarnings("unchecked")
	public void populateZones() {
		// logger.info("========= Ingreso a populateZones(), con parroquia: #0",
		// parish != null ? parish.getId() : parish);
		if (this.parish != null && this.parish.getId() != null) {
			zones = findTerritorialDivisions(parish.getId());
		} else {
			zones = new ArrayList<TerritorialDivision>();
		}

		this.zone = null;
		this.getInstance().setBlock(new Block());
	}

	/**
	 * Encuentra las zonas por sector
	 * 
	 * @return List<TerritorialDivision>
	 */
	@SuppressWarnings("unchecked")
	public List<TerritorialDivision> populateSectors() {
		// logger.info("========= Ingreso a populateSectors(), con zona: #0",
		// zone != null ? zone.getId() : zone);
		if (this.zone != null && this.zone.getId() != null) {
			sectors = findTerritorialDivisions(zone.getId());
		} else {
			sectors = new ArrayList<TerritorialDivision>();
		}

		if (this.getInstance().getBlock() == null)
			this.getInstance().setBlock(new Block());
		return sectors;
		// this.populateCadastralCode();
	}

	/**
	 * Encuentra las manzanas por sector
	 * 
	 * @return List<TerritorialDivision>
	 */
	@SuppressWarnings("unchecked")
	public List<Block> populateBlocks() {
		// logger.info("========= Ingreso a populateBlocks(), con sector: #0",
		// sector != null ? sector.getId() : sector);
		if (sector != null && sector.getId() != null) {
			Query query = getEntityManager().createNamedQuery(
					"Block.findBySector");
			query.setParameter("sectorId", sector.getId());
			setBlocks((List<Block>) query.getResultList());
		} else {
			setBlocks(new ArrayList<Block>());
		}

		return getBlocks();
	}

	/**
	 * Devuelve los límites de la manzana de la propiedad
	 * 
	 * @return List<BlockLimit>
	 */
	public List<BlockLimit> populateLimits() {
		//logger.info("========= Ingreso a populateLimits(), con instance");
		// block: #0",
		// this.getInstance().getBlock().getId());
		if (this.getInstance().getBlock() != null
				&& this.getInstance().getBlock().getId() != null) {
			limits = this.getInstance().getBlock().getLimits();
		} else {
			limits = new ArrayList<BlockLimit>();
		}

		// logger.info("========= saliendo de populateLimits(), con instance
		// block: #0",
		// this.getInstance().getBlock().getId());
		return limits;

	}

	public List<Boundary> getBoundaries() {
		return this.getInstance().getCurrentDomain().getBoundaries();
	}

	public void setOwnerCurrentDomain(Resident owner) {
		//logger.info("=====> Ingreso a fijar propietario #0",				owner.getIdentificationNumber());
		this.owner = owner;
		owner.add(this.getInstance().getCurrentDomain());

	}

	/**
	 * Busca los barrios según el criterio ingresado el suggestionBox
	 * 
	 * @param suggest
	 *            criterio ingresado el suggestionBox
	 * @return List<Neighborhood>
	 */
	@SuppressWarnings("unchecked")
	public List<Neighborhood> findNeighborhoods(Object suggest) {
		String pref = (String) suggest;
		Query query = this.getEntityManager().createNamedQuery(
				"Neighborhood.findByName");
		query.setParameter("name", pref);
		return (List<Neighborhood>) query.getResultList();
	}

	/**
	 * Devuelve el tipo de contribuyente
	 * 
	 * @return String
	 */
	public String getOwnerType() {
		if (getInstance().getCurrentDomain().getResident() == null)
			return "";

		String message;

		if (getInstance().getCurrentDomain().getResident() instanceof Person) {
			message = Interpolator.instance().interpolate(
					"#{messages['common.Natural']}", new Object[0]);
		} else {
			LegalEntity legalEntity = (LegalEntity) getInstance()
					.getCurrentDomain().getResident();

			if (legalEntity.getLegalEntityType() == null)
				return "";

			message = Interpolator.instance().interpolate(
					"#{messages[" + legalEntity.getLegalEntityType().name()
							+ "]}", new Object[0]);
		}
		return message;
	}

	@SuppressWarnings("unchecked")
	public List<TerritorialDivision> getCantons() {
		return showCantons(this.getInstance().getCurrentDomain());
	}

	@SuppressWarnings("unchecked")
	public List<TerritorialDivision> getCantonsForSpecificDomain() {
		if (domain == null) {
			return new ArrayList<TerritorialDivision>();
		}
		return showCantons(domain);
	}

	/**
	 * Devuelve los cantones que pertenecen a la provincia de la notaría donde
	 * se realizó el traspaso o se registró el dominio
	 * 
	 * @return List<TerritorialDivision>
	 */
	@SuppressWarnings("unchecked")
	private List<TerritorialDivision> showCantons(Domain d) {
		if (d.getNotarysProvince() != null) {
			Query query = getPersistenceContext().createNamedQuery(
					"TerritorialDivision.findByParent");
			query.setParameter("parentId", d.getNotarysProvince().getId());
			return query.getResultList();
		} else {
			return new ArrayList<TerritorialDivision>();
		}
	}

	public void addPropertyLandUse() {
		//logger.info("======= INGRESO A AGREGAR PropertyLandUse", this);
		getInstance().add(new PropertyLandUse());
	}

	/**
	 * Calcula el avalúo comercial
	 * 
	 * @param appraisal
	 */
	public void calculateCommercialAppraisal(Appraisal appraisal) {
		appraisal.setCommercialAppraisal(appraisal.getLot().add(
				appraisal.getBuilding()));
		this.getInstance().getCurrentDomain()
				.setLotAppraisal(appraisal.getLot());
		this.getInstance().getCurrentDomain()
				.setBuildingAppraisal(appraisal.getBuilding());
		calculateCommercialAppraisal();
	}

	public void remove(PropertyLandUse propertyLandUse) {
		this.getInstance().remove(propertyLandUse);
	}

	public void addBoundary() {
		this.getInstance().getCurrentDomain().add(new Boundary());
	}

	public void remove(Boundary boundary) {
		this.getInstance().getCurrentDomain().remove(boundary);
	}

	/**
	 * Busca los usos de suelo según el criterio ingresado el suggestionBox
	 * 
	 * @param suggest
	 *            criterio ingresado el suggestionBox
	 * @return List<LandUse>
	 */
	@SuppressWarnings("unchecked")
	public List<LandUse> findLandUse(Object suggest) {
		String pref = (String) suggest;
		Query query = this.getEntityManager().createNamedQuery(
				"LandUse.findByName");
		query.setParameter("name", pref);
		return (List<LandUse>) query.getResultList();
	}

	/**
	 * Se encarga de subir la imagen de la propiedad
	 * 
	 * @param event
	 *            evento de carga de archivo
	 */
	public void photoListener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		//logger.info("===> Photo listener executed...  Data: " + item.getData());
		if (item != null && item.getData() != null) {
			//logger.info(item.getFileName() + ", size: " + item.getFileSize());
			if (getInstance().getPhoto() == null) {
				getInstance().setPhoto(new Attachment());
			}
			getInstance().getPhoto().setData(item.getData());
			getInstance().getPhoto().setName(item.getFileName());
			getInstance().getPhoto().setContentType(item.getContentType());
			getInstance().getPhoto().setSize(item.getFileSize());
		}
	}

	public void clearPhoto() {
		getInstance().setPhoto(null);
	}

	/**
	 * Se encarga de subir la imagen con el croquis de la propiedad
	 * 
	 * @param event
	 *            evento de carga de archivo
	 */
	public void sketchListener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		//logger.info("===> Sketch listener executed...  Data: " + item.getData());
		if (item != null && item.getData() != null) {
			//logger.info(item.getFileName() + ", size: " + item.getFileSize());
			if (getInstance().getSketch() == null) {
				getInstance().setSketch(new Attachment());
			}
			getInstance().getSketch().setData(item.getData());
			getInstance().getSketch().setName(item.getFileName());
			getInstance().getSketch().setContentType(item.getContentType());
			getInstance().getSketch().setSize(item.getFileSize());
		}
	}

	public boolean needConfirmChange() {
		return needConfirmChangeOwner(this.getInstance());
	}

	/**
	 * Verifica si necesita confirmación de cambio de propietario en caso de
	 * traspaso de dominio
	 * 
	 * @param property
	 * @return Boolean
	 */
	public boolean needConfirmChangeOwner(Property property) {

		if (property.getNeedConfirmChangeOwner() == null) {
			if (property.getNeedConfirmChangeOwner() == null) {
				Domain res = null;

				for (Domain d : property.getDomains()) {
					if (res == null || res.getId() < d.getId())
						res = d;
				}

				if (res.getId() != property.getCurrentDomain().getId()
						&& res.getChangeOwnerConfirmed() == null) {
					//System.out.println("=>TRUE" + property.getId());
					property.setNeedConfirmChangeOwner(true);
					// super.getEntityManager().persist(property);
				} else {

					//System.out.println("=>FALSE" + property.getId());
					property.setNeedConfirmChangeOwner(false);
					// super.getEntityManager().persist(property);

				}
				super.update();

			}
		}

		return property.getNeedConfirmChangeOwner();

		// return property.getNeedConfirmChangeOwner();
	}

	/**
	 * Fija valores para generar el reporte de traspaso de dominio por propiedad
	 * 
	 * @param property
	 * @return String
	 */
	public String generateChangeOwnerReport(Property property) {
		setInstance(property);
		Domain aux = null;
		List<Long> ids = new ArrayList<Long>();
		for (Domain d : property.getDomains()) {
			ids.add(d.getId());
		}
		Collections.sort(ids);
		for (Long id : ids) {
			for (Domain d : property.getDomains()) {
				if (d.getId().equals(id)
						&& (d.getChangeOwnerConfirmed() == null || d
								.getChangeOwnerConfirmed())) {
					aux = d;
					break;
				}
			}
		}

		this.setParish(getInstance().getBlock().getSector().getParent()
				.getParent());

		domainHome.setInstance(aux);
		domainHome.getInstance().setValueForCalculate(
				domainHome.calculateValueTransaction());
		getInstance().getCurrentDomain().setValueForCalculate(
				calculateValueTransaction());
		return "/cadaster/report/ChangeOwnerReport.xhtml";
	}

	/**
	 * Verifica si se ha realizado algún traspaso de dominio
	 * 
	 * @param id
	 * @return Boolean
	 */
	public boolean haveChangeOwnerProperty(Long id) {

		List<Long> ids = new ArrayList<Long>();

		for (Domain dom : this.getInstance().getDomains()) {
			ids.add(dom.getId());
		}

		Collections.sort(ids);

		int n = ids.indexOf(id);

		if (n == 0) {
			return false;
		}

		return true;
	}

	/**
	 * Devuelve el dominio anterior
	 * 
	 * @param domain
	 * @return Domain
	 */
	private Domain findBeforeDomain(Domain domain) {

		List<Long> ids = new ArrayList<Long>();

		for (Domain dom : this.getInstance().getDomains()) {
			if (dom.getChangeOwnerConfirmed() != null
					&& dom.getChangeOwnerConfirmed()) {
				ids.add(dom.getId());
			}
		}

		Collections.sort(ids);

		int n = ids.indexOf(domain.getId());

		if (n > 0) {
			Long id = ids.get(n - 1);
			for (Domain d1 : this.getInstance().getDomains()) {
				if (d1.getId() == id)
					return d1;
			}
		}

		return null;
	}

	/**
	 * Fija valores para generar el reporte de traspaso de dominio
	 * 
	 * @param property
	 * @return String
	 */
	public String generateChangeOwnerReport(Domain d) {
		Domain anterior = findBeforeDomain(d);
		if (!haveChangeOwnerProperty(d.getId()) && anterior == null)
			return "/cadaster/HistoryProperty.xhtml";

		this.setParish(getInstance().getBlock().getSector().getParent()
				.getParent());
		domainHome.setInstance(d);
		domainHome.getInstance().setValueForCalculate(
				domainHome.calculateValueTransaction());
		getInstance().setCurrentDomain(anterior);
		getInstance().getCurrentDomain().setValueForCalculate(
				calculateValueTransaction());
		return "/cadaster/report/ChangeOwnerReport.xhtml";
	}

	/**
	 * Devuelve el último dominio de la propiedad
	 * 
	 * @param property
	 * @return Domain
	 */
	public Domain lastDomainOfProperty(Property property) {
		Domain res = null;
		for (Domain d : property.getDomains()) {
			if (res == null || res.getId() < d.getId())
				res = d;
		}
		return res;
	}

	public void clearSketch() {
		getInstance().setSketch(null);
	}

	public void removeBuilding(Building building) {
		if (building != null)
			this.getInstance().remove(building);
		calculateTotalAreaConstruction();
	}

	/**
	 * Agrega un building a la propiedad
	 */
	public void addBuilding() {
		Calendar c = Calendar.getInstance();
		if (this.building.getBuildingYear() > (c.getTime().getYear() + 1900)
				|| this.building.getBuildingYear() < 1900) {
			String message = Interpolator.instance().interpolate(
					"#{messages['building.incorrectBuildingYear']}",
					new Object[0]);
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return;
		}
		this.getInstance().add(this.building);
		//logger.info("===> Building #0 Add, size list: #1 ",
			//	this.building.getNumber(), this.getInstance().getBuildings()
						//.size());
		calculateTotalAreaConstruction();
	}

	/**
	 * Genera una nueva clave catastral para el caso de los predios rústicos
	 */
	public void updateCadastralCode() {
		populateCadastralCode();
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		BigInteger newCadastralCode = findMaxCadastralCode(this.getInstance()
				.getCadastralCode().substring(0, 9),
				((Long) systemParameterService
						.findParameter("PROPERTY_TYPE_ID_RUSTIC")));
		if (newCadastralCode.compareTo(BigInteger.ZERO) == 0) {
			newCadastralCode = new BigInteger(this.getInstance()
					.getCadastralCode());
		}
		newCadastralCode = newCadastralCode.add(BigInteger.ONE);
		this.getInstance().setCadastralCode(newCadastralCode.toString());
		this.getInstance().setPreviousCadastralCode(
				newCadastralCode.toString().substring(5));
	}

	/**
	 * Devuelve el código catastral mayor de los predios urbanos o rústicos
	 * 
	 * @param String
	 *            cadastralCode codigo catastral
	 * @param Long
	 *            propertyTypeId tipo de propiead 'urbana' o 'rústica'
	 */
	private BigInteger findMaxCadastralCode(String cadastralCode,
			Long propertyTypeId) {
		List<?> list = getPersistenceContext()
				.createNamedQuery("Property.findMaxCadastralCodeByParish")
				.setParameter("cadastralCode", cadastralCode)
				.setParameter("propertyTypeId", propertyTypeId).getResultList();

		if (list != null && list.size() > 0 && list.get(0) != null
				&& list.get(0).toString().length() > 0) {
			return new BigInteger(list.get(0).toString());
		}
		return BigInteger.ZERO;
	}

	public void createBuilding() {
		this.building = new Building();
	}

	public void editBuilding(Building building) {
		this.building = building;
	}

	private Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,
				systemParameter);
		return charge;
	}

	/**
	 * Se carga el encargado de catastros y de procuradoría
	 */
	public void loadCharge() {
		appraisalCharge = getCharge("DELEGATE_ID_CADASTER");
		if (appraisalCharge != null) {
			for (Delegate d : appraisalCharge.getDelegates()) {
				if (d.getIsActive())
					appraisalDelegate = d;
			}
		}
		procuratorCharge = getCharge("DELEGATE_ID_PROCURATOR");
		if (procuratorCharge != null) {
			for (Delegate d : procuratorCharge.getDelegates()) {
				if (d.getIsActive())
					procuratorDelegate = d;
			}
		}
		
	}

	public String ComprobarValores() {

		if (areaParTer == "" && areaParCon == "") {
			mensaje = "*campos vacios";
			return "true";
		} else if (areaParTer != "" && areaParCon == "") {
			double parcial1 = Double.parseDouble(areaParTer);
			double total1 = this.instance.getArea().doubleValue();
			if (parcial1 > total1) {
				mensaje = "*área parcial de terreno incorrecta! "
						+ String.valueOf(parcial1) + " es mayor que "
						+ String.valueOf(total1);
			} else {
				mensaje = "*valor de área parcial de terreno correcto";
				areaParTer = String.valueOf(parcial1);
				return "true";
			}
		} else if (areaParTer == "" && areaParCon != "") {
			double parcial2 = Double.parseDouble(areaParCon);
			double total2 = this.instance.getCurrentDomain()
					.getTotalAreaConstruction().doubleValue();
			if (parcial2 > total2) {
				mensaje = "*área parcial de constucción incorrecta! "
						+ String.valueOf(parcial2) + " es mayor que "
						+ String.valueOf(total2);
			} else {
				mensaje = "*valor de área parcial de construcción correcto";
				areaParCon = String.valueOf(parcial2);
				return "true";
			}
		} else if (areaParTer != "" && areaParCon != "") {
			double parcial1 = Double.parseDouble(areaParTer);
			double total1 = this.instance.getArea().doubleValue();
			double parcial2 = Double.parseDouble(areaParCon);
			double total2 = this.instance.getCurrentDomain()
					.getTotalAreaConstruction().doubleValue();
			if (parcial1 > total1 && parcial2 > total2) {
				mensaje = "*datos incorrectos! " + String.valueOf(parcial1)
						+ " es mayor que " + String.valueOf(total1) + " y "
						+ String.valueOf(parcial2) + " es mayor que "
						+ String.valueOf(total2);
			} else if (parcial1 > total1 && parcial2 <= total2) {
				mensaje = "*área parcial de terreno incorrecta! "
						+ String.valueOf(parcial1) + " es mayor que "
						+ String.valueOf(total1);
			} else if (parcial1 <= total1 && parcial2 > total2) {
				mensaje = "*área parcial de constucción incorrecta! "
						+ String.valueOf(parcial2) + " es mayor que "
						+ String.valueOf(total2);
			} else if (parcial1 <= total1 && parcial2 <= total2) {
				mensaje = "*valores parciales correctos";
				areaParTer = String.valueOf(parcial1);
				areaParCon = String.valueOf(parcial2);
				return "true";
			}
		}
		return null;
	}

	public String getParroquiaAnt() {
		return parroquiaAnt;
	}

	public void setParroquiaAnt(String parroquiaAnt) {
		this.parroquiaAnt = parroquiaAnt;
	}

	public String getClaveAnt() {
		return claveAnt;
	}

	public void setClaveAnt(String claveAnt) {
		this.claveAnt = claveAnt;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipoTramite() {
		return tipoTramite;
	}

	public void setTipoTramite(String tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

	public String getAreaParTer() {
		return areaParTer;
	}

	public void setAreaParTer(String areaParTer) {
		this.areaParTer = areaParTer;
	}

	public String getAreaParCon() {
		return areaParCon;
	}

	public void setAreaParCon(String areaParCon) {
		this.areaParCon = areaParCon;
	}

	/**
	 * @return the owner
	 */
	public Resident getOwner() {
		return owner;
	}

	public void setOwner(Resident owner) {
		this.owner = owner;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	public void setSector(TerritorialDivision sector) {
		this.sector = sector;
	}

	public TerritorialDivision getSector() {
		return sector;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlockLimit(BlockLimit blockLimit) {
		this.blockLimit = blockLimit;
	}

	public BlockLimit getBlockLimit() {
		return blockLimit;
	}

	public void changeDomain(Domain d) {
		setDomain(d);
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setConfirmChangeOwner(boolean confirmChangeOwner) {
		this.confirmChangeOwner = confirmChangeOwner;
	}

	public boolean isConfirmChangeOwner() {
		return confirmChangeOwner;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setFromAddMortgage(boolean fromAddMortgage) {
		this.fromAddMortgage = fromAddMortgage;
	}

	public boolean isFromAddMortgage() {
		return fromAddMortgage;
	}

	public void setChangeOwner(boolean changeOwner) {
		this.changeOwner = changeOwner;
	}

	public boolean isChangeOwner() {
		return changeOwner;
	}

	public void setChangedOwnerComplete(boolean changedOwnerComplete) {
		this.changedOwnerComplete = changedOwnerComplete;
	}

	public boolean isChangedOwnerComplete() {
		return changedOwnerComplete;
	}

	public void setUrban(boolean isUrban) {
		this.isUrban = isUrban;
	}

	public boolean isUrban() {
		return isUrban;
	}

	public void setPropertyTypeId(Long propertyTypeId) {
		this.propertyTypeId = propertyTypeId;
	}

	public Long getPropertyTypeId() {
		return propertyTypeId;
	}

	public void setEditObservation(String editObservation) {
		this.editObservation = editObservation;
	}

	public String getEditObservation() {
		return editObservation;
	}

	public void setProcuratorCharge(Charge procuratorCharge) {
		this.procuratorCharge = procuratorCharge;
	}

	public Charge getProcuratorCharge() {
		return procuratorCharge;
	}

	public void setAppraisalCharge(Charge appraisalCharge) {
		this.appraisalCharge = appraisalCharge;
	}

	public Charge getAppraisalCharge() {
		return appraisalCharge;
	}

	public Delegate getAppraisalDelegate() {
		return appraisalDelegate;
	}

	public void setAppraisalDelegate(Delegate appraisalDelegate) {
		this.appraisalDelegate = appraisalDelegate;
	}

	public Delegate getProcuratorDelegate() {
		return procuratorDelegate;
	}

	public void setProcuratorDelegate(Delegate procuratorDelegate) {
		this.procuratorDelegate = procuratorDelegate;
	}

	public boolean isFromTemplate() {
		return isFromTemplate;
	}

	public void setFromTemplate(boolean isFromTemplate) {
		this.isFromTemplate = isFromTemplate;
	}

	public CheckingRecordType getCheckingRecordType() {
		return checkingRecordType;
	}

	public void setCheckingRecordType(CheckingRecordType checkingRecordType) {
		this.checkingRecordType = checkingRecordType;
	}

	public List<CheckingRecord> getCheckingRecords() {
		return checkingRecords;
	}

	public void setCheckingRecords(List<CheckingRecord> checkingRecords) {
		this.checkingRecords = checkingRecords;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReasonExoneration() {
		return reasonExoneration;
	}

	public void setReasonExoneration(String reasonExoneration) {
		this.reasonExoneration = reasonExoneration;
	}

	public boolean isExoneration() {
		return exoneration;
	}

	public void setExoneration(boolean exoneration) {
		if (!exoneration)
			setReasonExoneration(null);
		this.exoneration = exoneration;
	}

	/**
	 * @param zones
	 *            the zones to set
	 */
	public void setZones(List<TerritorialDivision> zones) {
		this.zones = zones;
	}

	/**
	 * @return the zones
	 */
	public List<TerritorialDivision> getZones() {
		return zones;
	}

	/**
	 * @return the sectors
	 */
	public List<TerritorialDivision> getSectors() {
		return sectors;
	}

	/**
	 * @param sectors
	 *            the sectors to set
	 */
	public void setSectors(List<TerritorialDivision> sectors) {
		this.sectors = sectors;
	}

	/**
	 * @return the limits
	 */
	public List<BlockLimit> getLimits() {
		return limits;
	}

	public void setLimits(List<BlockLimit> limits) {
		this.limits = limits;
	}

	/**
	 * @return the parishe
	 */
	public TerritorialDivision getParish() {
		return parish;
	}

	/**
	 * @param parishe
	 *            the parishe to set
	 */
	public void setParish(TerritorialDivision parish) {
		this.parish = parish;
	}

	/**
	 * @return the zone
	 */
	public TerritorialDivision getZone() {
		return zone;
	}

	/**
	 * @param zone
	 *            the zone to set
	 */
	public void setZone(TerritorialDivision zone) {
		this.zone = zone;
	}

	/**
	 * @return the building
	 */
	public Building getBuilding() {
		return building;
	}

	/**
	 * @param building
	 *            the building to set
	 */
	public void setBuilding(Building building) {
		this.building = building;
	}

	/**
	 * @return the appraisalPeriod
	 */
	public AppraisalPeriod getAppraisalPeriod() {
		return appraisalPeriod;
	}

	/**
	 * @param appraisalPeriod
	 *            the appraisalPeriod to set
	 */
	public void setAppraisalPeriod(AppraisalPeriod appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

	/**
	 * @return the anioAppraisal
	 */
	public int getAnioAppraisal() {
		return anioAppraisal;
	}

	/**
	 * @param anioAppraisal
	 *            the anioAppraisal to set
	 */
	public void setAnioAppraisal(int anioAppraisal) {
		this.anioAppraisal = anioAppraisal;
	}

	public void calculateUrbanAppraisalOnlyProperty(Property property) {
		anioAppraisal = GregorianCalendar.getInstance().get(Calendar.YEAR);
		/*System.out
				.println(">>>>>>>>>>>>>>>>> anioAppraisal calculateUrbanAppraisalOnlyProperty: "
						+ anioAppraisal);*/
		List<Property> properties = new ArrayList<Property>();
		properties.add(property);
		AppraisalService appraisalService = ServiceLocator.getInstance()
				.findResource(APPRAISAL_SERVICE_NAME);
		properties = appraisalService.calculateUrbanAppraisal(appraisalPeriod,
				anioAppraisal, properties, false);
		Appraisal lastAppraisal = findLastAppraisal(property);
		if (appraisal == null)
			appraisal = new Appraisal();
		appraisal.setLot(this.instance.getCurrentDomain().getLotAppraisal());
		appraisal.setBuilding(this.instance.getCurrentDomain()
				.getBuildingAppraisal());
		appraisal.setCommercialAppraisal(this.instance.getCurrentDomain()
				.getCommercialAppraisal());
		appraisal.setValueBySquareMeter(this.instance.getCurrentDomain()
				.getValueBySquareMeter());
		if (lastAppraisal == null)
			this.instance.add(appraisal);
		else {
			if ((lastAppraisal.getLot().compareTo(appraisal.getLot()) != 0)
					|| (lastAppraisal.getBuilding().compareTo(
							appraisal.getBuilding()) != 0)) {
				this.instance.add(appraisal);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Appraisal findLastAppraisal(Property property) {
		Long idProperty = new Long(0);
		if (property.getId() != null)
			idProperty = property.getId();
		String strQuery = "SELECT * FROM appraisal where id = (select max(id) from appraisal where property_id="
				+ idProperty + ")";
		Query query = this.getEntityManager().createNativeQuery(strQuery,
				ec.gob.gim.cadaster.model.Appraisal.class);
		if (idProperty == 0)
			return null;
		else {
			List<Appraisal> lista = (List<Appraisal>) query.getResultList();
			if (lista.size() > 0)
				return (Appraisal) lista.get(0);
			else
				return null;
		}
	}

	private AppraisalPeriod findActiveAppraisalPeriod() {
		Query query = this.getEntityManager().createNamedQuery(
				"AppraisalPeriod.findUniqueActiveAndNotForTest");
		return (AppraisalPeriod) query.getSingleResult();
	}

	private boolean isValidAliquotForProperty() {
		BigDecimal bgdCien = new BigDecimal(100);
		BigDecimal bgdCero = BigDecimal.ZERO;
		String horizontalPropertyCode = this.instance.getCadastralCode()
				.substring(18, this.instance.getCadastralCode().length());
		if ((this.instance.getBuildingAliquot().compareTo(bgdCien) > 0)
				|| (this.instance.getLotAliquot().compareTo(bgdCien) > 0)) {
			return false;
		}
		if (Integer.valueOf(horizontalPropertyCode) > 0) {
			if (this.instance.getLotAliquot().compareTo(bgdCero) == 0) {
				return false;
			}
			if (this.instance.getCurrentDomain().getTotalAreaConstruction()
					.compareTo(bgdCero) == 0) {
				if ((this.instance.getLotAliquot().compareTo(bgdCero) == 0)
						|| (this.instance.getLotAliquot().compareTo(bgdCien) == 0)) {
					return false;
				} else {
					return true;
				}
			} else {
				if (this.instance.getBuildingAliquot().compareTo(bgdCero) == 0) {
					return false;
				}
			}
			return true;
		} else {
			if ((this.instance.getBuildingAliquot().compareTo(bgdCien) == 0)
					&& (this.instance.getLotAliquot().compareTo(bgdCien) == 0)) {
				return true;
			} else {
				return false;
			}
		}
	}

	private boolean isValidPreviousCadastralCode() {
		this.instance.setPreviousCadastralCode(this.instance
				.getPreviousCadastralCode().trim());
		if ((this.instance.getPreviousCadastralCode().length() < lengthPreviousCadastralCode)
				|| (this.instance.getPreviousCadastralCode().length() > 19))
			return false;
		if (isManaged()) {
			Query query = this.getEntityManager().createNamedQuery(
					"Property.countPreviousCadastralCodeAndId");
			query.setParameter("previousCadastralCode",
					this.instance.getPreviousCadastralCode());
			if (isIdDefined())
				query.setParameter("propertyId", this.instance.getId());
			else
				query.setParameter("propertyId", 0);
			Long count = (Long) query.getSingleResult();
			if (count >= 1)
				return false;
			else
				return true;
		} else {
			Query query = this.getEntityManager().createNamedQuery(
					"Property.countPreviousCadastralCode");
			query.setParameter("previousCadastralCode",
					this.instance.getPreviousCadastralCode());
			Long count = (Long) query.getSingleResult();
			if (count >= 1)
				return false;
			else
				return true;
		}
	}

	public List<CheckingRecord> getCheckingRecordsForProperty() {
		return checkingRecordsForProperty;
	}

	public void setCheckingRecordsForProperty(
			List<CheckingRecord> checkingRecordsForProperty) {
		this.checkingRecordsForProperty = checkingRecordsForProperty;
	}

	@SuppressWarnings("unchecked")
	private List<CheckingRecord> findCheckingRecordsForProperty() {
		if (isManaged()) {
			Query query = this.getEntityManager().createNamedQuery(
					"CheckingRecord.findByProperty");
			query.setParameter("property", this.instance);
			// if (isIdDefined())
			// query.setParameter("propertyId", this.instance.getId());
			// else
			// query.setParameter("propertyId", 0);
			return (List<CheckingRecord>) query.getResultList();
		} else {
			return new ArrayList<CheckingRecord>();
		}
	}

	public void prepareViewHistory() {

		if (propertySelectedId != null) {
			this.selectedPropertyViewHistory = getEntityManager().find(
					Property.class, propertySelectedId);
			this.listPropertyHistory.clear();
			this.listDomainHistory.clear();
			// this.propertySelectedCadastralCode =
			// selectedPropertyViewHistory.getPreviousCadastralCode();

			/* PROPIEDAD */
			String qryProperty = "select rev.timestamp," + "rev.username,"
					+ "pro.CADASTRALCODE," + "pro.area," + "pro.front,"
					+ "pro.frontslength," + "pro.observations," + "pro.side "
					+ "from gimprod.revision rev "
					+ "inner join gimaudit.property_aud pro on rev.id=pro.rev "
					+ "where pro.id = ? " + "order by rev.timestamp desc";

			Query queryProperty = this.getEntityManager().createNativeQuery(
					qryProperty);

			// queryProperty.setParameter(1,
			// this.propertySelectedCadastralCode);
			queryProperty.setParameter(1, propertySelectedId);
			List<Object[]> resultProperties = queryProperty.getResultList();

			/* DOMINIOS */
			String qryDomains = "select  rev.timestamp,"
					+ "rev.username,"
					+ "dom.buildingappraisal,"
					+ "dom.commercialappraisal,"
					+ "dom.lotappraisal,"
					+ "dom.observations,"
					+ "dom.totalareaconstruction,"
					+ "dom.valuebysquaremeter,"
					+ "res.identificationnumber,"
					+ "res.name "
					+ "from gimprod.revision rev "
					+ "inner join gimaudit.domain_aud dom on rev.id= dom.rev "
					+ "inner join gimprod.resident res on dom.resident_id=res.id "
					+ "where dom.currentproperty_id= ? "
					+ "order by rev.timestamp desc;";

			Query queryDomains = this.getEntityManager().createNativeQuery(
					qryDomains);

			queryDomains.setParameter(1, propertySelectedId);
			List<Object[]> resultDomains = queryDomains.getResultList();

			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");

			for (Object[] row : resultProperties) {
				PropertyHistoryDTO reg = new PropertyHistoryDTO();
				try {
					reg.setDate(sdf.parse(row[0].toString()));
					reg.setUsername(row[1].toString());
					reg.setCadastralCode(row[2] == null ? "" : row[2]
							.toString());
					reg.setArea(row[3] == null ? BigDecimal.ZERO : BigDecimal
							.valueOf(Double.valueOf(row[3].toString())));
					reg.setFront(row[4] == null ? BigDecimal.ZERO : BigDecimal
							.valueOf(Double.valueOf(row[4].toString())));
					reg.setFrontsLength(row[5] == null ? BigDecimal.ZERO
							: BigDecimal.valueOf(Double.valueOf(row[5]
									.toString())));
					reg.setObservations(row[6] == null ? "" : row[6].toString());
					reg.setSide(row[7] == null ? BigDecimal.ZERO : BigDecimal
							.valueOf(Double.valueOf(row[7].toString())));

					listPropertyHistory.add(reg);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			for (Object[] row : resultDomains) {
				DomainHistoryDTO reg = new DomainHistoryDTO();
				try {
					reg.setDate(sdf.parse(row[0].toString()));
					reg.setUsername(row[1].toString());
					reg.setBuildingAppraisal(row[2] == null ? BigDecimal.ZERO
							: BigDecimal.valueOf(Double.valueOf(row[2]
									.toString())));
					reg.setCommercialAppraisal(row[3] == null ? BigDecimal.ZERO
							: BigDecimal.valueOf(Double.valueOf(row[3]
									.toString())));
					reg.setLotAppraisal(row[4] == null ? BigDecimal.ZERO
							: BigDecimal.valueOf(Double.valueOf(row[4]
									.toString())));
					reg.setObservations(row[5] == null ? "" : row[5].toString());
					reg.setTotalAreaConstruction(row[6] == null ? BigDecimal.ZERO
							: BigDecimal.valueOf(Double.valueOf(row[6]
									.toString())));
					reg.setValueBySquareMeter(row[7] == null ? BigDecimal.ZERO
							: BigDecimal.valueOf(Double.valueOf(row[7]
									.toString())));
					reg.setIdentificationnumber(row[8] == null ? "" : row[8]
							.toString());
					reg.setName(row[9] == null ? "" : row[9].toString());

					listDomainHistory.add(reg);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Manuel U. No cargar por defecto imagenes de predios 18-12-2015
	 */

	private boolean statusImage = true;

	public boolean isStatusImage() {
		return statusImage;
	}

	public void setStatusImage(boolean statusImage) {
		this.statusImage = statusImage;
	}

	public void uploadImage() {
		this.statusImage = false;
		//System.out.println("uploadImage--->statusImageValor: " + statusImage);
	}

	public CadastralCertificateDTO get_dataCadastralCerfificate() {
		return _dataCadastralCerfificate;
	}

	public void set_dataCadastralCerfificate(
			CadastralCertificateDTO _dataCadastralCerfificate) {
		this._dataCadastralCerfificate = _dataCadastralCerfificate;
	}

	// Jock Samaniego
	// 23-09-2016
	// Para activar predios rústicos deshabilitados

	public void activeRusticProperty(Property property) {
		try {
			if (property.getPropertyType().getId() == 2) {
				property.setDeleted(Boolean.FALSE);
				CadasterService cadasterService = ServiceLocator.getInstance()
						.findResource(CadasterService.LOCAL_NAME);
				cadasterService.updateRusticProperty(property);
			}
		} catch (Exception e) {
			//System.out.println("error: no se completó la activación!!");
			e.printStackTrace();
		}
	}

	public Boolean hasRole(String roleKey) {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		String role = systemParameterService.findParameter(roleKey);
		if (role != null) {
			return userSession.getUser().hasRole(role);
		}
		return false;
	}

	public String printCadastralCertificate(Long propertyId) {

		if (cadasterService == null) {
			cadasterService = ServiceLocator.getInstance().findResource(
					cadasterService.LOCAL_NAME);
		}

		this._dataCadastralCerfificate = this.cadasterService
				.getCadastralCertificateData(propertyId);
		
		return "/cadaster/report/CadastralCertificatePDF.xhtml";
	}

	public BoundaryDTO getBoundaryDTO(CompassPoint type) {
		try {
			switch (type) {
			case NORTH: {
				if (this._dataCadastralCerfificate.getLindero_norte() != null) {

					BoundaryDTO _boundary = new ObjectMapper().readValue(
							this._dataCadastralCerfificate.getLindero_norte(),
							BoundaryDTO.class);
					return _boundary;

				}
				break;
			}
			case SOUTH: {

				if (this._dataCadastralCerfificate.getLindero_sur() != null) {

					BoundaryDTO _boundary = new ObjectMapper().readValue(
							this._dataCadastralCerfificate.getLindero_sur(),
							BoundaryDTO.class);
					return _boundary;

				}
				break;
			}
			case EAST: {

				if (this._dataCadastralCerfificate.getLindero_este() != null) {

					BoundaryDTO _boundary = new ObjectMapper().readValue(
							this._dataCadastralCerfificate.getLindero_este(),
							BoundaryDTO.class);
					return _boundary;

				}
				break;
			}
			case WEST: {

				if (this._dataCadastralCerfificate.getLindero_oeste() != null) {

					BoundaryDTO _boundary = new ObjectMapper().readValue(
							this._dataCadastralCerfificate.getLindero_oeste(),
							BoundaryDTO.class);
					return _boundary;

				}
				break;
			}
			default:
				break;
			}

			return new BoundaryDTO();
		} catch (JsonParseException e) {
			e.printStackTrace();
			return new BoundaryDTO();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new BoundaryDTO();
		} catch (IOException e) {
			e.printStackTrace();
			return new BoundaryDTO();
		}
	}

	public List<BuildingDTO> getBuildingsDTO() {
		try {

			if (this._dataCadastralCerfificate.getConstrucciones() != null) {

				List<BuildingDTO> _construcciones = Arrays
						.asList(new ObjectMapper().readValue(
								this._dataCadastralCerfificate
										.getConstrucciones(),
								BuildingDTO[].class));

				return _construcciones;

			}

			return new ArrayList<BuildingDTO>();

		} catch (JsonParseException e) {
			e.printStackTrace();
			return new ArrayList<BuildingDTO>();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new ArrayList<BuildingDTO>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<BuildingDTO>();
		}
	}

	public boolean showRisk() {		 
		if(parish == null) return false; 
		return (riskParameter.indexOf(parish.getCode())!= -1);
	}
	
	public boolean showThreat() {
		if(parish == null) return false;
		return (threatParamter.indexOf(parish.getCode())!= -1);
	}
	
	public void findRegistrationForm() {
		this.propertyWs = null;
		if (this.getInstance().getRegistrationCardNumber() != null
				&& this.getInstance().getRegistrationCardNumber() != "") {
			boolean isValid = false;
			try {
				Double.valueOf(this.getInstance().getRegistrationCardNumber());
				isValid = true;
			} catch (Exception e) {
				facesMessages
						.addToControl(
								"",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								"Número de ficha no válido");
				isValid = false;
			}

			if (isValid) {
				try {
					PropertyRegistrationService propertyRegistrationService = ServiceLocator
							.getInstance().findResource(
									PropertyRegistrationService.LOCAL_NAME);
					this.propertyWs = propertyRegistrationService
							.findByRegistrationForm(this.getInstance()
									.getRegistrationCardNumber());
				} catch (Exception e) {
					e.printStackTrace();
					facesMessages
							.addToControl(
									"",
									org.jboss.seam.international.StatusMessage.Severity.ERROR,
									"Error en la consulta al servicio web");
				}
			}
		} else {
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Campo Nro. ficha registral está vacío");
		}
	}

	public PropertyWs getPropertyWs() {
		return propertyWs;
	}

	public void setPropertyWs(PropertyWs propertyWs) {
		this.propertyWs = propertyWs;
	}
	
	
	//Jock Samaniego
	//Para obtener informacion de propiedades para calculo de CEM
	
	private String cadastralCodeToCEM;
	private TerritorialDivision parishToCEM;
	private TerritorialDivision zoneToCEM;
	private TerritorialDivision sectorToCEM;
	private Neighborhood neighborhoodToCEM;
	private List<InformationToCalculateCEMDto> propertiesToCalculateCEM;
	private List<TerritorialDivision> zonesToCalculateCEM =  new ArrayList<TerritorialDivision>();
	private List<TerritorialDivision> sectorsToCalculateCEM = new ArrayList<TerritorialDivision>();
	private InformationToCalculateCEMDto propertySelectedToCalculateCEM;
	private String optionToSearchCEM;


	public String getCadastralCodeToCEM() {
		return cadastralCodeToCEM;
	}

	public void setCadastralCodeToCEM(String cadastralCodeToCEM) {
		this.cadastralCodeToCEM = cadastralCodeToCEM;
	}

	public String getOptionToSearchCEM() {
		return optionToSearchCEM;
	}

	public void setOptionToSearchCEM(String optionToSearchCEM) {
		this.optionToSearchCEM = optionToSearchCEM;
	}

	public List<InformationToCalculateCEMDto> getPropertiesToCalculateCEM() {
		return propertiesToCalculateCEM;
	}

	public void setPropertiesToCalculateCEM(
			List<InformationToCalculateCEMDto> propertiesToCalculateCEM) {
		this.propertiesToCalculateCEM = propertiesToCalculateCEM;
	}

	public InformationToCalculateCEMDto getPropertySelectedToCalculateCEM() {
		return propertySelectedToCalculateCEM;
	}

	public void setPropertySelectedToCalculateCEM(
			InformationToCalculateCEMDto propertySelectedToCalculateCEM) {
		this.propertySelectedToCalculateCEM = propertySelectedToCalculateCEM;
	}
	
	public TerritorialDivision getParishToCEM() {
		return parishToCEM;
	}

	public void setParishToCEM(TerritorialDivision parishToCEM) {
		this.parishToCEM = parishToCEM;
	}

	public Neighborhood getNeighborhoodToCEM() {
		return neighborhoodToCEM;
	}

	public void setNeighborhoodToCEM(Neighborhood neighborhoodToCEM) {
		this.neighborhoodToCEM = neighborhoodToCEM;
	}

	public TerritorialDivision getZoneToCEM() {
		return zoneToCEM;
	}

	public void setZoneToCEM(TerritorialDivision zoneToCEM) {
		this.zoneToCEM = zoneToCEM;
	}

	public TerritorialDivision getSectorToCEM() {
		return sectorToCEM;
	}

	public void setSectorToCEM(TerritorialDivision sectorToCEM) {
		this.sectorToCEM = sectorToCEM;
	}
	
	public List<TerritorialDivision> getZonesToCalculateCEM() {
		return zonesToCalculateCEM;
	}

	public void setZonesToCalculateCEM(List<TerritorialDivision> zonesToCalculateCEM) {
		this.zonesToCalculateCEM = zonesToCalculateCEM;
	}

	public List<TerritorialDivision> getSectorsToCalculateCEM() {
		return sectorsToCalculateCEM;
	}

	public void setSectorsToCalculateCEM(
			List<TerritorialDivision> sectorsToCalculateCEM) {
		this.sectorsToCalculateCEM = sectorsToCalculateCEM;
	}

	public void searchPropertiesInformationToCalculateCEM(){
		this.propertiesToCalculateCEM = new ArrayList<InformationToCalculateCEMDto>();
		if(this.optionToSearchCEM.equals("cadastral")){
			Query query = getEntityManager().createNamedQuery(
					"Property.findPropertiesByCalculateCEM");
			query.setParameter("cadastralCode", this.cadastralCodeToCEM);
			this.propertiesToCalculateCEM = query.getResultList();
		} else if (this.optionToSearchCEM.equals("parish")){
			if (this.sectorToCEM != null){
				Query query = getEntityManager().createNamedQuery(
						"Property.findPropertiesBySectorCalculateCEM");
				query.setParameter("sectorId", this.sectorToCEM.getId());
				this.propertiesToCalculateCEM = query.getResultList();
			} else if (this.zoneToCEM != null){
				Query query = getEntityManager().createNamedQuery(
						"Property.findPropertiesByZoneCalculateCEM");
				query.setParameter("zoneId", this.zoneToCEM.getId());
				this.propertiesToCalculateCEM = query.getResultList();
			} else if (this.parishToCEM != null){
				Query query = getEntityManager().createNamedQuery(
						"Property.findPropertiesByParishCalculateCEM");
				query.setParameter("parishId", this.parishToCEM.getId());
				this.propertiesToCalculateCEM = query.getResultList();
			}
			
		} else if (this.optionToSearchCEM.equals("neighborhood")){
			Query query = getEntityManager().createNamedQuery(
					"Property.findPropertiesByNeighborhoodCalculateCEM");
			query.setParameter("neighborhoodId", this.neighborhoodToCEM.getId());
			this.propertiesToCalculateCEM = query.getResultList();
		}
			
	}
		
	public String printPropertyInformation(InformationToCalculateCEMDto property){
		this.propertySelectedToCalculateCEM = property;
		return "";
	}
	
	public List<Neighborhood> findNeighborhoods(){		
		Query query = getPersistenceContext().createNamedQuery("Neighborhood.findAll");		
		return query.getResultList();	
	}
	
	public void resetValuesToCalculateCEM(){
		cadastralCodeToCEM = "";
		parishToCEM = null;
		zoneToCEM = null;
		sectorToCEM = null;
		neighborhoodToCEM = null;
		propertySelectedToCalculateCEM = null;
		propertiesToCalculateCEM = new ArrayList<InformationToCalculateCEMDto>();
		zonesToCalculateCEM =  new ArrayList<TerritorialDivision>();
		sectorsToCalculateCEM = new ArrayList<TerritorialDivision>();
	}
	
	public void searchZonesToCalculateCEM(){
		if(parishToCEM != null){
			zonesToCalculateCEM = this.findTerritorialDivisions(parishToCEM.getId());
		}else{
			zonesToCalculateCEM = new ArrayList<TerritorialDivision>();
		}
		sectorsToCalculateCEM = new ArrayList<TerritorialDivision>();
	}
	
	public void searchSectorsToCalculateCEM(){
		if(zoneToCEM != null){
			sectorsToCalculateCEM = this.findTerritorialDivisions(zoneToCEM.getId());
		}else{
			sectorsToCalculateCEM = new ArrayList<TerritorialDivision>();
		}
	}
	
	// para co-propietarios
	// Jock Samaniego
	
	private Resident newOwner;
	private String criteriaNewOwner;
	private String identificationNumberNewOwner;
	private List<Resident> residentsNewOwner;

	public Resident getNewOwner() {
		return newOwner;
	}

	public void setNewOwner(Resident newOwner) {
		this.newOwner = newOwner;
	}

	public String getCriteriaNewOwner() {
		return criteriaNewOwner;
	}

	public void setCriteriaNewOwner(String criteriaNewOwner) {
		this.criteriaNewOwner = criteriaNewOwner;
	}
	
	public String getIdentificationNumberNewOwner() {
		return identificationNumberNewOwner;
	}

	public void setIdentificationNumberNewOwner(String identificationNumberNewOwner) {
		this.identificationNumberNewOwner = identificationNumberNewOwner;
	}

	public List<Resident> getResidentsNewOwner() {
		return residentsNewOwner;
	}

	public void setResidentsNewOwner(List<Resident> residentsNewOwner) {
		this.residentsNewOwner = residentsNewOwner;
	}

	
	public void searchResidentNewOwner() {
		//logger.info("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumberNewOwner);
		try {
			Resident resident = (Resident) query.getSingleResult();
			//logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.domainOwner.setResident(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().getCurrentDomain().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteriaNewOwner() {
		//logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteriaNewOwner != null && !this.criteriaNewOwner.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteriaNewOwner);
			setResidentsNewOwner(query.getResultList());
		}
	}
	
	public void clearSearchResidentPanelNewOwner() {
		this.setCriteriaNewOwner(null);
		setResidentsNewOwner(null);
	}
	
	public void createDomainOwner() {
		isNewOwner = Boolean.TRUE;
		this.identificationNumberNewOwner = ""; 
		this.domainOwner = new DomainOwner();
		this.domainOwner.setIsEnabled(Boolean.TRUE);
	}
	
	
	public void addDomainOwner() {
		isNewOwner = Boolean.FALSE;
		this.domainOwner.setTransactionDate(new Date()); 
	    this.getInstance().getCurrentDomain().getDomainOwners().add(this.domainOwner); 
	    this.domainOwner.setDomain(this.getInstance().getCurrentDomain()); 
	    this.identificationNumberNewOwner = ""; 
	    this.domainOwner = null;
	}
	
	public void remove(DomainOwner localOwner) {
		this.getInstance().getCurrentDomain().remove(localOwner);
		localOwner.setDomain(null); 
	}
	
	public void ownerSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.domainOwner.setResident(resident);
		this.setIdentificationNumberNewOwner(resident.getIdentificationNumber());
	}
	
	public List<DomainOwnerType> getDomainOwnerTypes() { 
        List<DomainOwnerType> domainOwnerType = new ArrayList<DomainOwnerType>(); 
          domainOwnerType.add(DomainOwnerType.CO_OWNER); 
          domainOwnerType.add(DomainOwnerType.INHERITOR); 
        return domainOwnerType; 
    } 
   
	 public void calculateOwnersPercentage(){
		isValidPercentaje = Boolean.TRUE;
	    if(this.domainOwner != null){ 
	      BigDecimal total = new BigDecimal(0); 
	      for(DomainOwner domOwn : this.instance.getCurrentDomain().getDomainOwners()){ 
	        if (domOwn.getPercentage() != null){ 
	          total = total.add(domOwn.getPercentage()); 
	        } 
	      } 
	      if(this.domainOwner.getPercentage() != null){ 
	        total = total.add(this.domainOwner.getPercentage()); 
	      } 
	      if(total.compareTo(new BigDecimal(100)) > 0){ 
	    	  isValidPercentaje = Boolean.FALSE;
	      }else{
	    	  isValidPercentaje = Boolean.TRUE;
	      }
	    }  
	}
  
  	private Boolean isNewOwner = Boolean.FALSE;
  	private Boolean isValidPercentaje = Boolean.TRUE;

	public Boolean getIsNewOwner() {
		return isNewOwner;
	}
	
	public void setIsNewOwner(Boolean isNewOwner) {
		this.isNewOwner = isNewOwner;
	}

	public Boolean getIsValidPercentaje() {
		return isValidPercentaje;
	}

	public void setIsValidPercentaje(Boolean isValidPercentaje) {
		this.isValidPercentaje = isValidPercentaje;
	}
	
	public void cleanNewOwner(){
		isNewOwner = Boolean.FALSE;
	    this.identificationNumberNewOwner = ""; 
	    this.domainOwner = null;
	}
  
}