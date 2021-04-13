package org.gob.gim.waterservice.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.action.MunicipalBondManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.ContractType;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.StatusChange;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.Route;
import ec.gob.gim.waterservice.model.RoutePreviewEmission;
import ec.gob.gim.waterservice.model.WaterBlockLog;
import ec.gob.gim.waterservice.model.WaterMeter;
import ec.gob.gim.waterservice.model.WaterSupply;
import ec.gob.gim.waterservice.model.dto.WaterBlockDTO;


@Name("waterSupplyHome")
@Scope(ScopeType.CONVERSATION)
public class WaterSupplyHome extends EntityHome<WaterSupply> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	@Logger
	Log logger;

	// @In(create = true)
	// ContractHome contractHome;
	// @In(create = true)
	// PropertyHome propertyHome;
	// @In(create = true)
	// RouteHome routeHome;
	// @In(create = true)
	// WaterSupplyCategoryHome waterSupplyCategoryHome;
	//
	private String criteriaProperty;
	private List<Property> properties;

	private WaterMeter waterMeter;

	private String routeName;

	private String streetName;

	private WaterSupply serviceInfo;

	private SystemParameterService systemParameterService;

	// inspection report
	private Integer monthsNumber = 4;
	private List<Consumption> consumptions;
	private Consumption consumption = new Consumption();
	private ConsumptionState checkedConsumptionState;

	// para aplicar Exemptions
	private Resident residentExemption;
	private List<WaterSupply> waterSupplies;

	@In
	UserSession userSession;

	private Integer sequence = new Integer(0);

	public void setWaterSupplyId(Long id) {
		setId(id);
	}

	public Long getWaterSupplyId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	private boolean isFirstTime = true;

	public void wire() {
		if (!isFirstTime)
			return;
		isFirstTime = false;
		getInstance();

		if (getInstance().getServiceOwner() != null)
			identificationNumber = this.getInstance().getServiceOwner()
					.getIdentificationNumber();
		if (getInstance().getRecipeOwner() != null)
			identificationNumberRecipe = this.getInstance().getRecipeOwner()
					.getIdentificationNumber();
		if (getInstance().getStreet() != null)
			streetName = this.getInstance().getStreet().getName();
		if (getInstance().getRoute() != null)
			routeName = this.getInstance().getRoute().getName();

		loadServiceNumber();

	}

	public void loadServiceNumber() {
		if (this.getInstance().getId() == null) {
			Query query = this.getEntityManager().createNamedQuery(
					"WaterSupply.findMaxService");
			List<Object> result = query.getResultList();
			if (result.size() > 0) {
				if (result.get(0) == null) {
					this.getInstance().setServiceNumber(1);
				} else {
					this.getInstance().setServiceNumber(
							Integer.parseInt(result.get(0).toString()) + 1);
				}
			}
		}
	}

	private List<Resident> residents;

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + criteria);
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void searchResident() {
		logger.info("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.getInstance().setServiceOwner(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setServiceOwner(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().setServiceOwner(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	// / para la asignacion del recipeOwner searchResident
	public void searchRecipeOwner() {
		logger.info("RESIDENT CHOOSER CRITERIA... "
				+ this.identificationNumberRecipe);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber",
				this.identificationNumberRecipe);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("recipeOwner CHOOSER ACTION " + resident.getName());

			this.getInstance().setRecipeOwner(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setRecipeOwner(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	// / para la asignacion del recipeOwner residentSelectedListener
	public void recipeOwnerSelectedListener(ActionEvent event) {

		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().setRecipeOwner(resident);
		this.setIdentificationNumberRecipe(resident.getIdentificationNumber());
	}

	public void clearSearchRecipeOwnerPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	private String criteria;

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	private String identificationNumber;
	private String identificationNumberRecipe;

	public boolean isWired() {
		return true;
	}

	public WaterSupply getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<WaterMeter> getWaterMeters() {
		return getInstance() == null ? null : new ArrayList<WaterMeter>(
				getInstance().getWaterMeters());
	}

	public void createWaterMeter() {
		this.waterMeter = new WaterMeter();
	}

	public void editWaterMeter(WaterMeter waterMeter) {
		this.waterMeter = waterMeter;
	}

	public void addWaterMeter() {
		this.getInstance().add(this.waterMeter);
		logger.info(this.getInstance().getWaterMeters().size());
	}

	public void addWaterMeterConsumption() {
		this.getInstance().add(this.waterMeter);
		if (this.update().equals("complete")) {
			this.createWaterMeter();
		}
		logger.info(this.getInstance().getWaterMeters().size());
	}

	public void removeWaterMeter(WaterMeter waterMeter) {
		this.getInstance().remove(waterMeter);
	}

	public WaterMeter getWaterMeter() {
		return waterMeter;
	}

	public void setWaterMeter(WaterMeter waterMeter) {
		this.waterMeter = waterMeter;
	}

	/*
	 * private WaterSupply findActiveWaterSupplyIdForExemptions(Long residentId)
	 * { Query query = this.getEntityManager().createNamedQuery(
	 * "WaterSuppply.findActiveWaterSupplyForExemptions");
	 * query.setParameter("residentId", residentId); List<WaterSupply> list =
	 * query.getResultList(); return list.size() == 0 ? null : list.get(0); }
	 * 
	 * private boolean existActiveWaterSupplyIdForExemptions(){
	 * 
	 * WaterSupply waterSupply =
	 * findActiveWaterSupplyIdForExemptions(this.getInstance
	 * ().getServiceOwner().getId()); if(waterSupply == null) return
	 * Boolean.FALSE; if(waterSupply.getId().equals(this.getInstance().getId()))
	 * return Boolean.FALSE; return Boolean.TRUE; }
	 */

	@In
	FacesMessages facesMessages;

	@Override
	public String persist() {
		boolean failed = false;
		if (this.getInstance().getRoute() == null) {
			setRouteName(null);
			failed = true;
		}

		if (failed) {
			facesMessages.addToControl("street",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"#{messages['common.emptyOrInvalidFields']}");
			return "failed";
		}

		if (this.getInstance().getServiceOwner() == null
				|| this.getInstance().getRecipeOwner() == null) {
			failed = true;
		}

		if (failed) {
			facesMessages.addToControl("resident",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"#{messages['resident.required']}");
			return "failed";
		}

		/*
		 * if (this.getInstance().getApplyExemptions() &&
		 * existActiveWaterSupplyIdForExemptions()) {
		 * facesMessages.addToControl("street",
		 * org.jboss.seam.international.StatusMessage.Severity.ERROR,
		 * "#{messages['waterSupply.existActiveWaterSupplyForExemptions']}");
		 * return "failed"; }
		 */

		if (this.getInstance().getWaterMeters().size() == 0) {
			facesMessages.addToControl("resident",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"#{messages['waterSupply.waterMerterRequired']}");
			return "failed";
		}

		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		ContractType contractType = systemParameterService.materialize(
				ContractType.class, "CONTRACT_TYPE_ID_WATERSUPLY");
		this.instance.getContract().setContractType(contractType);
		this.instance.getContract().setCreationDate(new Date());
		this.instance.getContract().setSubscriber(
				getInstance().getServiceOwner());

		generateConsumption();

		this.getEntityManager().persist(consumption);

		return super.persist();
	}

	@Override
	public String update() {

		boolean failed = false;
		if (this.getInstance().getServiceOwner() == null
				|| this.getInstance().getRecipeOwner() == null) {
			failed = true;
		}

		if (failed) {
			facesMessages.addToControl("resident",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"#{messages['resident.required']}");
			return "failed";
		}

		/*
		 * if (this.getInstance().getApplyExemptions() &&
		 * existActiveWaterSupplyIdForExemptions()) {
		 * facesMessages.addToControl("street",
		 * org.jboss.seam.international.StatusMessage.Severity.ERROR,
		 * "#{messages['waterSupply.existActiveWaterSupplyForExemptions']}");
		 * return "failed"; }
		 */

		// this.instance.getContract().setSubscriber(getInstance().getServiceOwner());

		return super.update();
	}

	public void generateConsumption() {
		if (checkedConsumptionState == null)
			loadConsumptionStates();
		consumption.setConsumptionState(checkedConsumptionState);
		consumption.setWaterSupply(this.getInstance());
		consumption.setMonth(consumption.getMonthType().getMonthInt());
		consumption.setReadingDate(new Date());
	}

	private List<Consumption> consumptionList;

	public List<Consumption> getConsumptionList() {
		return consumptionList;
	}

	public void setConsumptionList(List<Consumption> consumptionList) {
		this.consumptionList = consumptionList;
	}

	public void findConsumptionsByWaterSupply(WaterSupply waterSupply) {
		if (waterSupply != null) {
			Query query = this.getEntityManager().createNamedQuery(
					"Consumption.findByWaterSupply");
			query.setParameter("waterSupplyId", waterSupply.getId());
			consumptionList = (List<Consumption>) query.getResultList();
		} else {
			consumptionList = new ArrayList<Consumption>();
		}

	}

	public WaterSupply getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(WaterSupply serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public void selectServiceInformation(WaterSupply serviceInfo) {
		if (serviceInfo != null) {
			this.serviceInfo = serviceInfo;
			findConsumptionsByWaterSupply(serviceInfo);
		} else {
			consumptionList = new ArrayList<Consumption>();
		}
	}

	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	// eventos para el componente de busqueda de propiedades
	// metodos busqueda de propiedades del contribuyente
	public void searchProperty() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Route> autoCompleteRouteByName(Object o) {
		Query query = this.getEntityManager().createNamedQuery(
				"Route.findByName");
		query.setParameter("routeName", o.toString());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Street> findStreets(Object suggestion) {
		String name = suggestion.toString();
		Query query = getPersistenceContext().createNamedQuery(
				"Street.findByName");
		query.setParameter("name", name);
		return query.getResultList();
	}

	public void searchPropertyByCriteria() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}

	public void clearSearchPropertyPanel() {
		this.setCriteriaProperty(null);
		properties = null;
	}

	private Route currentRoute;

	public Route getCurrentRoute() {
		return currentRoute;
	}

	public void setCurrentRoute(Route currentRoute) {
		this.currentRoute = currentRoute;
	}

	public void beforeAutcompleteRoute(Route s) {
		this.getInstance().setRoute(s);
		if (s == null) {
			setRouteName(null);
		} else {
			setRouteName(s.getName());
		}
	}

	public void beforeAutcompleteStreet(Street s) {
		this.getInstance().setStreet(s);
		if (s == null) {
			setStreetName(null);
		} else {
			setStreetName(s.getName());
		}
	}

	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes()
				.get("property");
		this.getInstance().setProperty(property);
	}

	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public Integer getMonthsNumber() {
		return monthsNumber;
	}

	public void setMonthsNumber(Integer monthsNumber) {
		this.monthsNumber = monthsNumber;
	}

	private Boolean firstLoadConsumptions = Boolean.TRUE;

	public void loadConsumptions() {
		if (!firstLoadConsumptions)
			return;
		findConsumptions();
		firstLoadConsumptions = Boolean.FALSE;
	}

	public void findConsumptions() {
		int actualMonth = Integer.parseInt(new SimpleDateFormat("MM")
				.format(new Date()));
		int startMonth = actualMonth - monthsNumber;
		int year = Integer.parseInt(new SimpleDateFormat("yyyy")
				.format(new Date()));
		/*System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX " + actualMonth
				+ " ------ " + startMonth + " ------ " + year);*/
		Query q = this.getEntityManager().createNamedQuery(
				"Consumption.findByWaterSupplyBetweenMonth");
		q.setParameter("waterSupplyId", this.getInstance().getId());
		q.setParameter("year", year);
		q.setParameter("monthStart", actualMonth);
		q.setParameter("monthEnd", startMonth);
		consumptions = q.getResultList();

		// en el caso de q hay ano atras
		if ((actualMonth - monthsNumber) <= 0) {
			int monthBack = monthsNumber - actualMonth;
			int yearBack = year - 1;

			Query qBack = this.getEntityManager().createNamedQuery(
					"Consumption.findByWaterSupplyBetweenMonth");
			qBack.setParameter("waterSupplyId", this.getInstance().getId());
			qBack.setParameter("year", yearBack);
			qBack.setParameter("monthStart", 12);
			qBack.setParameter("monthEnd", (12 - monthBack));
			consumptions.addAll(qBack.getResultList());
			/*System.out.println("meses atras................... " + monthBack
					+ " " + yearBack + " el tamanio es " + consumptions.size());*/
		}

		findMunicipalBound();
	}

	/**
	 * Buscar los MunicipalBonds para un consumo determinado
	 */
	public void findMunicipalBound() {

		String sentence = "SELECT mbs.name, mb.paidTotal FROM  MunicipalBond mb "
				+ "LEFT JOIN mb.municipalBondStatus mbs "
				+ "WHERE mb.serviceDate between :firstDayMonth and :lastDayMonth and "
				+ "mb.groupingCode like :code and "
				+ "mbs.id in (2,3,4,6,7,11,12)";

		int actualMonth;
		int year;

		Query q = this.getEntityManager().createQuery(sentence);

		for (Consumption c : consumptions) {
			String code = c.getWaterSupply().getServiceNumber().toString();

			actualMonth = c.getMonth() - 1;
			year = c.getYear();

			Calendar startDate = Calendar.getInstance();
			startDate.set(year, actualMonth, 1);

			int endDayOfMonth = startDate
					.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

			Calendar endDate = Calendar.getInstance();
			endDate.set(year, actualMonth, endDayOfMonth);

			q.setParameter("firstDayMonth", startDate.getTime());
			q.setParameter("lastDayMonth", endDate.getTime());
			q.setParameter("code", code);

			List<Object[]> values = q.getResultList();
			if (values.size() > 0) {
				Object[] row = values.get(0);
				if (row[0] != null) {
					c.setMunicipalBondState(row[0].toString());
				}
				if (row[1] != null) {
					try {
						c.setPaidTotal(new BigDecimal(row[1].toString()));
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public List<Consumption> getConsumptions() {
		return consumptions;
	}

	public void setConsumptions(List<Consumption> consumptions) {
		this.consumptions = consumptions;
	}

	public Consumption getConsumption() {
		return consumption;
	}

	public void setConsumption(Consumption consumption) {
		this.consumption = consumption;
	}

	public Boolean checkCorrectReading() {
		if (checkedConsumptionState == null)
			loadConsumptionStates();

		Boolean isChecked = false;

		if (consumption.getCurrentReading() != null) {
			if (consumption.getCurrentReading() > -1) {
				if (consumption.getPreviousReading() > consumption
						.getCurrentReading()) {
					consumption.setAmount(new BigDecimal(consumption
							.getDifferenceInReading()));
					consumption.setConsumptionState(checkedConsumptionState);
					consumption.setIsValidReading(true);
					isChecked = true;
					consumption.setErrorMessage("");
				} else {
					consumption.setIsValidReading(false);
					consumption
							.setErrorMessage("Revisar el consumo, no debe ser menor");
					consumption.setDifferenceInReading(null);
					consumption.setAmount(null);
				}

			} else {
				consumption.setIsValidReading(false);
				consumption.setDifferenceInReading(null);
				consumption.setAmount(null);
			}
		} else {
			consumption.setIsValidReading(false);
			consumption.setDifferenceInReading(null);
			consumption.setAmount(null);
		}

		return isChecked;
	}

	public void loadConsumptionStates() {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		checkedConsumptionState = systemParameterService.materialize(
				ConsumptionState.class, "CONSUMPTIONSTATE_ID_ENTERED");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persisRoutePreviewEmission(RoutePreviewEmission emission) {
		this.getEntityManager().merge(emission);
		// this.getEntityManager().flush();
	}

	public void clearValues() {
		waterTotal = BigDecimal.ZERO;
		paidTotal = BigDecimal.ZERO;
		sawerageTotal = BigDecimal.ZERO;
		masterPlanTotal = BigDecimal.ZERO;
		securityTotal = BigDecimal.ZERO;
		watershedTotal = BigDecimal.ZERO;
		basicTotal = BigDecimal.ZERO;
		trashTotal = BigDecimal.ZERO;
		municipalBondsState = new ArrayList<MunicipalBond>();
		statusChanges = new ArrayList<StatusChange>();
	}
	


	private Date starDate = new Date();
	private Date endDate = new Date();
	private List<MunicipalBond> municipalBondsState;
	private MunicipalBondStatus municipalBondStatus;

	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public List<StatusChange> statusChanges;
	private Date printDate = new Date();
	private BigDecimal waterTotal;
	private BigDecimal sawerageTotal;
	private BigDecimal masterPlanTotal;
	private BigDecimal securityTotal;
	private BigDecimal watershedTotal;
	private BigDecimal basicTotal;
	private BigDecimal trashTotal;
	private BigDecimal paidTotal;
	private BigDecimal interest;
	private List<Long> entryIds;

	private MunicipalBondStatus blockedStatus;


	private MunicipalBondStatus preEmitStatus;

	public void loadMunicipalBondStatus() {
		this.sequence = 0;
		if (municipalBondStatuses != null)
			return;

		municipalBondStatuses = new ArrayList<MunicipalBondStatus>();
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		
		preEmitStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		blockedStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		reversedStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REVERSED");
		
		municipalBondStatuses.add(preEmitStatus);
		municipalBondStatuses.add(reversedStatus);
		municipalBondStatuses.add(blockedStatus);
		//System.out.println("loadMunicipalBondStatus>>>>>>>>>municipalBondStatusesValor:"+municipalBondStatuses);
		return;
	}

	public void waxingSequence() {
		this.sequence = 0;
	}

	public void runReport() {
		this.sequence = 0;
		if (municipalBondStatus == null)
			return;
		loadMunicipalBondStatus();
		clearValues();
		if (municipalBondStatus.equals(preEmitStatus)) {
			findMunicipalBondRePreEmited();
		}
		if (municipalBondStatus.equals(blockedStatus)) {
			findMunicipalBondByState();
		}
	}


	// public void runReportBlocked() {
	// this.sequence = 0;
	// //blockedStatus =
	// systemParameterService.materialize(MunicipalBondStatus.class,
	// "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
	// //municipalBondStatuses.add(blockedStatus);
	// //municipalBondStatus.setId(7L);
	// municipalBondStatus.setName("BLOQUEADA");
	// //System.out.print("pass3muni: "+municipalBondStatus.getName());
	// if(municipalBondStatus == null) return;
	// loadMunicipalBondStatus();
	// clearValues();
	// findMunicipalBondByState();
	// }

	private Long waterSupplyEntryId;

	private Long findWaterSupplyEntryId() {
		if (waterSupplyEntryId != null)
			return waterSupplyEntryId;
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		waterSupplyEntryId = systemParameterService
				.findParameter("ENTRY_ID_WATER_SERVICE_TAX");
		return waterSupplyEntryId;
	}

public void findMunicipalBondByState() {
		String sentencia = "select distinct(sc) from StatusChange sc "
				+ "LEFT JOIN FETCH sc.municipalBond mb "
				+ "LEFT JOIN FETCH mb.adjunct " 
				+ "LEFT JOIN FETCH mb.entry e "
				//+ "LEFT JOIN FETCH sc.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.resident res "
				//+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt " 
				//+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				//+ "LEFT JOIN FETCH it.electronicItem "
				
				//+ "LEFT JOIN FETCH mb.discountItems di "
				//+ "LEFT JOIN FETCH di.entry "
				//+ "LEFT JOIN FETCH mb.deposits deposit "
				//+ "LEFT JOIN FETCH mb.surchargeItems si "
				//+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "where e.id in (76) "
				// "and mbs = :municipalBondStatus and sc.user.id = :userId and mb.municipalBondStatus = :municipalBondStatus "

				+ "and mbs = :municipalBondStatus and sc.user.id = :userId "
				+ "and sc.date between :starDate and :endDate ORDER BY sc.date";

		Query q = this.getEntityManager().createQuery(sentencia);
		// agua potable
		//q.setParameter("entry_id", findWaterSupplyEntryId());
		// bloqueada
		q.setParameter("municipalBondStatus", this.municipalBondStatus);
		q.setParameter("starDate", starDate);
		q.setParameter("endDate", endDate);
		// q.setParameter("userId", userSession.getUser().getId());		
		q.setParameter("userId", userSession.getUser().getId());
		statusChanges = q.getResultList();
		findTotalsByMunicipalBondByState(statusChanges);
	}

	public void findTotalsByMunicipalBondByState(List<StatusChange> scs) {
		// todo mejorar la consulta a la base de datos, para evitar los
		// repetidos, puede ser un DTO
		// que cargue los campos nececesario y los items como una lista,
		// necesito la fecha del StatusChange en le reporte.
		paidTotal = new BigDecimal(0);
		interest = new BigDecimal(0);
		int posicion = 0;
		List<Integer> locations;
		List<Integer> removeLocations = new ArrayList<Integer>();
		int matches = 0;
		for (StatusChange sc : scs) {
			posicion = 0;
			locations = new ArrayList<Integer>();
			matches = 0;
			//System.out.println("findTotalsByMunicipalBondByState>>>>>>>>>>sc.getMunicipalBond().getNumberValor:"+ sc.getMunicipalBond().getNumber());

			for (StatusChange scaux : scs) {
				if (sc.getMunicipalBond().getId().equals(scaux.getMunicipalBond().getId())) {
					/*System.out.println(posicion+ " en: comparing========================"+ sc.getMunicipalBond().getId() + " = "
							+ scaux.getMunicipalBond().getId());*/
					matches++;
					locations.add(posicion);
				}
				posicion++;
			}
			//System.out.println("");
			if (matches > 1) {
				int passses = 0;
				for (Integer location : locations) {
					if (passses != 0) {
						/*System.out
								.println("removiendo: >>>>>>>>>>>>>>>>>>>>>>> "
										+ location.intValue());*/
						if (!removeLocations.contains(location)) {
							removeLocations.add(location);
						}
					}
					passses++;
				}
			}
		}
		statusChanges = scs;

		Collections.sort(removeLocations, Collections.reverseOrder());

		for (Integer location : removeLocations) {
			StatusChange sc_reomved = statusChanges.remove(location.intValue());
			// System.out.println("removiendo al final..........: >>>>>>>>>>>>>>>>>>>>>>> "+
			// location.intValue()+" ------ "+sc_reomved.getMunicipalBond().getPaidTotal());
		}

		/*System.out.println(":::::::::::: los tamanos son : " + scs.size()
				+ " lo eliminado " + statusChanges.size());*/

		for (StatusChange sc : statusChanges) {
			interest = interest.add(sc.getMunicipalBond().getInterest());
			/*System.out.println(sc.getMunicipalBond().getValue() + " ---- "
					+ sc.getMunicipalBond().getPaidTotal() + " ---- "
					+ sc.getMunicipalBond().getInterest() + " ---- "
					+ sc.getMunicipalBond().getTaxesTotal());*/
			paidTotal = paidTotal.add(sc.getMunicipalBond().getValue());
		}
		System.out
				.println("findTotalsByMunicipalBondByState>>>>>>>>>>statusChangesSize2Valor:"
						+ statusChanges.size());
	}

	/**
	 * Todas las re-planilladas por usuario para un entry determinado en este
	 * caso solo agua potable el 76
	 */
	public void findMunicipalBondRePreEmited() {
		String sentencia = "select distinct(mb) from MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry e "
				//+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "LEFT JOIN FETCH mb.resident res "
				//+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				//+ "LEFT JOIN FETCH it.electronicItem "
				//+ "LEFT JOIN FETCH mb.discountItems di "
				//+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				//+ "LEFT JOIN FETCH mb.surchargeItems si "
				//+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH mb.adjunct adjunct "
				//+ "LEFT JOIN FETCH adjunct.consumption consumption "
				//+ "left join fetch consumption.consumptionState cs "
				//+ "left join fetch consumption.waterSupply waterSupply "
				//+ "left join fetch consumption.waterMeterStatus ws "
				//+ "left join fetch waterSupply.contract contract "
				//+ "left join fetch contract.contractType contractType "
				//+ "left join fetch contractType.entry entry "
				//+ "left join fetch waterSupply.route route "
				
				//+ "left join fetch waterSupply.waterSupplyCategory waterSupplyCategory "
				+ "LEFT JOIN FETCH ti.tax "
				+ "where e.id in (76) "
				+ "and mbs.id <> :municipalBondStatusId "
				+ "and mb.originator.id = :emitterId "
				+ "and mb.creationDate between :starDate and :endDate order by mb.creationDate";
		Query q = this.getEntityManager().createQuery(sentencia);
		// agua potable
		// q.setParameter("entry_id", findWaterSupplyEntryId());
		// bloqueada
		q.setParameter("municipalBondStatusId", blockedStatus.getId());
		q.setParameter("starDate", starDate);
		q.setParameter("endDate", endDate);
		q.setParameter("emitterId", userSession.getPerson().getId());
		municipalBondsState = q.getResultList();
		findTotalsByMunicipalBondRePreEmited();
	}

	public void findTotalsByMunicipalBondRePreEmited() {
		paidTotal = new BigDecimal(0);
		for (MunicipalBond mb : municipalBondsState) {
			paidTotal = paidTotal.add(mb.getPaidTotal());
		}
		/*
		 * if (entryIds == null) { loadEntries(); }
		 * 
		 * String sentencia =
		 * "select SUM(item.total), item.entry.id from MunicipalBond mb " +
		 * "LEFT JOIN mb.adjunct " + "LEFT JOIN mb.entry e " +
		 * "LEFT JOIN mb.municipalBondStatus mbs " + "JOIN mb.items item " +
		 * "where e.id = :entry_id " + "and mbs.id <> :municipalBondStatusId " +
		 * "and mb.originator.id = :emitterId " +
		 * "and mb.creationDate between :starDate and :endDate " +
		 * "GROUP BY item.entry.id";
		 * 
		 * Query q = this.getEntityManager().createQuery(sentencia); // agua
		 * potable q.setParameter("entry_id", findWaterSupplyEntryId()); //
		 * bloqueada q.setParameter("municipalBondStatusId",
		 * blockedStatus.getId()); q.setParameter("starDate", starDate);
		 * q.setParameter("endDate", endDate); q.setParameter("emitterId",
		 * userSession.getPerson().getId());
		 * 
		 * 
		 * System.out.println(":::::::::::::::::before 2:::::::::::::::::::::::::::"
		 * ); List<Object[]> results = q.getResultList();
		 * System.out.println(":::::::::::::::::after 2:::::::::::::::::::::::::::"
		 * );
		 * 
		 * for(Object[] row : results){ Long id = (Long)row[1]; BigDecimal res =
		 * (BigDecimal)row[0]; if(res!=null){ if (id.equals(waterSupplyEntryId))
		 * { waterTotal = res; paidTotal = paidTotal.add(waterTotal); } if
		 * (id.equals(sewerageEntryId)) { sawerageTotal = res; paidTotal =
		 * paidTotal.add(sawerageTotal); } if (id.equals(masterPlanEntryId)) {
		 * masterPlanTotal = res; paidTotal = paidTotal.add(masterPlanTotal); }
		 * if (id.equals(securityEntryId)) { securityTotal = res; paidTotal =
		 * paidTotal.add(securityTotal); } if
		 * (id.equals(microWaterShedsEntryId)) { watershedTotal = res; paidTotal
		 * = paidTotal.add(watershedTotal); } if (id.equals(basicCostEntryId)) {
		 * basicTotal = res; paidTotal = paidTotal.add(basicTotal); } if
		 * (id.equals(trashEntryId)) { trashTotal = res; paidTotal =
		 * paidTotal.add(trashTotal); } }
		 * 
		 * }
		 * 
		 * }
		 */

	}

	private Long sewerageEntryId; // alcantarillado
	private Long masterPlanEntryId; // plan maestro
	private Long securityEntryId; // seguridad
	private Long microWaterShedsEntryId; // microcuencas
	private Long basicCostEntryId; // costo basico
	private Long trashEntryId; // basura

	private void loadEntriesIds() {
		if (sewerageEntryId != null)
			return;
		if (waterSupplyEntryId == null)
			findWaterSupplyEntryId();
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		sewerageEntryId = systemParameterService
				.findParameter("ENTRY_ID_SEWERAGE");
		masterPlanEntryId = systemParameterService
				.findParameter("ENTRY_ID_MASTER_PLAN");
		securityEntryId = systemParameterService
				.findParameter("ENTRY_ID_SECURITY");
		microWaterShedsEntryId = systemParameterService
				.findParameter("ENTRY_ID_MICRO_WATERSHEDS");
		basicCostEntryId = systemParameterService
				.findParameter("ENTRY_ID_BASIC_COST");
		trashEntryId = systemParameterService.findParameter("ENTRY_ID_TRASH");
	}

	public void loadEntries() {
		loadEntriesIds();
		entryIds = new ArrayList<Long>();
		entryIds.add(waterSupplyEntryId);// alcantarillado
		entryIds.add(sewerageEntryId);// alcantarillado
		entryIds.add(masterPlanEntryId);// plan maestro
		entryIds.add(securityEntryId);// seguridad
		entryIds.add(microWaterShedsEntryId);// micro cuencas
		entryIds.add(basicCostEntryId);// costo basico
		entryIds.add(trashEntryId);// basura
	}

	public Date getStarDate() {
		return starDate;
	}

	public void setStarDate(Date starDate) {
		this.starDate = starDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<MunicipalBond> getMunicipalBondsState() {
		return municipalBondsState;
	}

	public void setMunicipalBondsState(List<MunicipalBond> municipalBondsState) {
		this.municipalBondsState = municipalBondsState;
	}

	public List<StatusChange> getStatusChanges() {
		return statusChanges;
	}

	public void setStatusChanges(List<StatusChange> statusChanges) {
		this.statusChanges = statusChanges;
	}

	/**
	 * agregar consumos faltantes, al watersupply esto debido a que algunos
	 * datos no fueron migrados, se lo usara por unos meses
	 */
	public String addNewFixConsumption() {
		generateConsumption();
		this.getEntityManager().persist(consumption);

		return super.update();
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public BigDecimal getWaterTotal() {
		return waterTotal;
	}

	public void setWaterTotal(BigDecimal waterTotal) {
		this.waterTotal = waterTotal;
	}

	public BigDecimal getSawerageTotal() {
		return sawerageTotal;
	}

	public void setSawerageTotal(BigDecimal sawerageTotal) {
		this.sawerageTotal = sawerageTotal;
	}

	public BigDecimal getSecurityTotal() {
		return securityTotal;
	}

	public void setSecurityTotal(BigDecimal securityTotal) {
		this.securityTotal = securityTotal;
	}

	public BigDecimal getWatershedTotal() {
		return watershedTotal;
	}

	public void setWatershedTotal(BigDecimal watershedTotal) {
		this.watershedTotal = watershedTotal;
	}

	public BigDecimal getBasicTotal() {
		return basicTotal;
	}

	public void setBasicTotal(BigDecimal basicTotal) {
		this.basicTotal = basicTotal;
	}

	public BigDecimal getTrashTotal() {
		return trashTotal;
	}

	public void setTrashTotal(BigDecimal trahsTotal) {
		this.trashTotal = trahsTotal;
	}

	public BigDecimal getMasterPlanTotal() {
		return masterPlanTotal;
	}

	public void setMasterPlanTotal(BigDecimal masterPlanTotal) {
		this.masterPlanTotal = masterPlanTotal;
	}

	public Long getWaterSupplyEntryId() {
		return waterSupplyEntryId;
	}

	public void setWaterSupplyEntryId(Long waterSupplyEntryId) {
		this.waterSupplyEntryId = waterSupplyEntryId;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public MunicipalBondStatus getBlockedStatus() {
		return blockedStatus;
	}

	public void setBlockedStatus(MunicipalBondStatus blockedStatus) {
		this.blockedStatus = blockedStatus;
	}

	public MunicipalBondStatus getPreEmitStatus() {
		return preEmitStatus;
	}

	public void setPreEmitStatus(MunicipalBondStatus preEmitStatus) {
		this.preEmitStatus = preEmitStatus;
	}

	public Boolean getFirstLoadConsumptions() {
		return firstLoadConsumptions;
	}

	public void setFirstLoadConsumptions(Boolean firstLoadConsumptions) {
		this.firstLoadConsumptions = firstLoadConsumptions;
	}

	public List<MunicipalBondStatus> getMunicipalBondStatuses() {
		return municipalBondStatuses;
	}

	public void setMunicipalBondStatuses(
			List<MunicipalBondStatus> municipalBondStatuses) {
		this.municipalBondStatuses = municipalBondStatuses;
	}

	/**
	 * Para el reporte de emisiones totals por fecha
	 */
	public void waterTotalEmitedReport() {
		String totalsSentence = "select SUM(mb.paidTotal) from MunicipalBond mb "
				+ "LEFT JOIN mb.entry e "
				+ "where mb.serviceDate between :startDate and :endDate and e.id = :entryId";
		Query q = this.getEntityManager().createQuery(totalsSentence);
		// agua potable
		q.setParameter("entryId", findWaterSupplyEntryId());
		q.setParameter("startDate", starDate);
		q.setParameter("endDate", endDate);
		if (q.getSingleResult() != null) {
			/*System.out
					.println("entr a foooooooooooooooooooooooooorrrrrrrrrrrrssssssssssssssssss "
							+ q.getSingleResult());*/
			this.paidTotal = getBigDecimal(q.getSingleResult());
		}
		/*
		 * List<Object[]> results = q.getResultList(); for(Object[] row :
		 * results){ //Long id = (Long)row[1]; System.out.println(
		 * "entr a foooooooooooooooooooooooooorrrrrrrrrrrrssssssssssssssssss");
		 * this.paidTotal = (BigDecimal)row[0]; }
		 */
	}

	public BigDecimal getBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException(
						"No es posible convertir a decimal [" + value
								+ "] de la clase " + value.getClass());
			}
		}
		return ret;
	}

	public String getIdentificationNumberRecipe() {
		return identificationNumberRecipe;
	}

	public void setIdentificationNumberRecipe(String identificationNumberRecipe) {
		this.identificationNumberRecipe = identificationNumberRecipe;
	}

	public Resident getResidentExemption() {
		return residentExemption;
	}

	public void setResidentExemption(Resident residentExemption) {
		this.residentExemption = residentExemption;
	}

	// para la asignacion del recipeOwner searchResident
	public void searchResidentExemption() {
		logger.info("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("recipeOwner CHOOSER ACTION " + resident.getName());

			this.residentExemption = resident;

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setRecipeOwner(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
		searchWaterSupplies();
	}

	// / para la asignacion del recipeOwner residentSelectedListener
	public void recipeResidentExemptionSelectedListener(ActionEvent event) {

		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.residentExemption = resident;
		this.setIdentificationNumber(resident.getIdentificationNumber());
		searchWaterSupplies();
	}

	public void clearSearchResdientExemptionPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	@SuppressWarnings("unchecked")
	public void searchWaterSupplies() {
		if (this.residentExemption != null) {
			Query q = this.getEntityManager().createNamedQuery(
					"WaterSupply.findByResident");
			q.setParameter("idResident", this.residentExemption.getId());
			this.waterSupplies = q.getResultList();
		} else {
			this.waterSupplies = new ArrayList<WaterSupply>();
		}
	}

	public List<WaterSupply> getWaterSupplies() {
		return waterSupplies;
	}

	public void setWaterSupplies(List<WaterSupply> waterSupplies) {
		this.waterSupplies = waterSupplies;
	}

	public void applyExemption(WaterSupply ws) {
		Query q = this.getEntityManager().createNamedQuery(
				"WaterSupply.findByActiveExemption");
		q.setParameter("idResident", this.residentExemption.getId());
		int cantidad = 0;
		if (q.getResultList().size() > 0) {
			cantidad = Integer.parseInt(q.getSingleResult().toString());
			//System.out.println("la cantidad en consulta es::::: " + cantidad);
		}
		/*System.out.println("la cantidad es::::: " + cantidad + " -------- "
				+ checkExemption());*/
		if (cantidad <= 0) {
			this.setInstance(ws);
			this.update();
			facesMessages.addToControl("street",
					org.jboss.seam.international.StatusMessage.Severity.INFO,
					"Exención aplicada");
		} else {
			facesMessages.addToControl("street",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Ya existe un servicio activo, solo puede ser uno");
		}

		/*
		 * Query q=this.getEntityManager().createNamedQuery(
		 * "WaterSupply.findByActiveExemption"); q.setParameter("idResident",
		 * this.residentExemption.getId()); int cantidad=0; if
		 * (q.getResultList().size() > 0) {
		 * cantidad=Integer.parseInt(q.getSingleResult().toString());
		 * System.out.println("la cantidad en consulta es::::: "+cantidad); }
		 * System.out.println("la cantidad es::::: "+cantidad
		 * +" -------- "+checkExemption()); if (cantidad == 0) {
		 * ws.setApplyElderlyExemption(Boolean.TRUE); this.setInstance(ws);
		 * this.update();
		 * facesMessages.addToControl("street",org.jboss.seam.international
		 * .StatusMessage.Severity.INFO,"Exención aplicada"); }else{
		 * facesMessages.addToControl("street",
		 * org.jboss.seam.international.StatusMessage.Severity.ERROR,
		 * "Ya existe un servicio activo, solo puede ser uno"); }
		 */
	}

	public boolean checkExemption() {
		for (WaterSupply ws : waterSupplies) {
			if (ws.getApplyElderlyExemption()) {
				return true;
			}
		}
		return false;
	}

	public void removeExemption(WaterSupply ws) {
		ws.setApplyElderlyExemption(Boolean.FALSE);
		ws.setApplySpecialExemption(Boolean.FALSE);
		this.setInstance(ws);
		this.update();
		facesMessages.addToControl("street",
				org.jboss.seam.international.StatusMessage.Severity.INFO,
				"Exención aplicada");
	}

	/*
	 * private boolean existActiveWaterSupplyIdForExemptions(){
	 * 
	 * WaterSupply waterSupply =
	 * findActiveWaterSupplyIdForExemptions(this.getInstance
	 * ().getServiceOwner().getId()); if(waterSupply == null) return
	 * Boolean.FALSE; if(waterSupply.getId().equals(this.getInstance().getId()))
	 * return Boolean.FALSE; return Boolean.TRUE; }
	 */

	public Integer getSequence() {
		sequence = sequence + 1;
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	private List<Route> lisRoutes = new ArrayList<Route>();

	public void initRoutes() {
		Route r = new Route();
		r.setId(new Long(lisRoutes.size() + 1));
		lisRoutes.add(new Route());
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	
	
	/**
	 * Nueva implementacion: pasar toda la lista de Obligaciones Municipales que estan en estado "bloqueados" a estado "de Baja"
	 */
	
	private List<MunicipalBondStatus> municipalBondStatuses;
	
	public static String observationAll;

	public static String getObservationAll() {
		return observationAll;
	}

	public static void setObservationAll(String observationAll) {
		WaterSupplyHome.observationAll = observationAll;
	}
	
	private MunicipalBondStatus reversedBondStatus;

	public MunicipalBondStatus getReversedBondStatus() {
		return reversedBondStatus;
	}

	public void setReversedBondStatus(MunicipalBondStatus reversedBondStatus) {
		this.reversedBondStatus = reversedBondStatus;
	}
	
	private MunicipalBondStatus reversedStatus;

	public MunicipalBondStatus getPaidStatus() {
		return reversedStatus;
	}

	public void setPaidStatus(MunicipalBondStatus paidStatus) {
		this.reversedStatus = paidStatus;
	}
	
	public void loadMunicipalBondStatusStart() {
		this.sequence = 0;
		if (municipalBondStatuses != null)
			return;
		municipalBondStatuses = new ArrayList<MunicipalBondStatus>();		
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		blockedStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		municipalBondStatuses.add(blockedStatus);
		this.municipalBondStatus = blockedStatus;		
		return;
	}	
	
	public void runReportAll() {
		this.sequence = 0;
		//System.out.println("runReport>>>>>>>>>>>>>>>municipalBondStatusValue:"+ municipalBondStatus);
		loadMunicipalBondStatusStart();
		clearValuesAll();
		findMunicipalBondByStateAll();
	}
	
	public void findMunicipalBondByStateAll() {

		//System.out.println("findMunicipalBondByState>>>>>>>>>>>>>>>userSession.getUser().getIdValue:"+ userSession.getUser().getId());
		String sentencia = "select distinct(sc) from StatusChange sc "
				+ "LEFT JOIN FETCH sc.municipalBond mb "
				+ "LEFT JOIN FETCH mb.adjunct "
				+ "LEFT JOIN FETCH mb.entry e "
				+ "LEFT JOIN FETCH sc.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "where e.id in (76) "
				+ "and mb.municipalBondStatus = :municipalBondStatus and sc.user.id = 42 "
				+ "and sc.date between :starDate and :endDate ORDER BY sc.date";

		Query q = this.getEntityManager().createQuery(sentencia);
		q.setParameter("municipalBondStatus", this.municipalBondStatus);
		// q.setParameter("userId", userSession.getUser().getId());
		q.setParameter("starDate", starDate);
		q.setParameter("endDate", endDate);
		statusChanges = q.getResultList();
		
		for(StatusChange sc : statusChanges){
			MunicipalBondManager.listForReverseAll.add(sc.getMunicipalBond().getId());
			//System.out.println("findMunicipalBondByStateAll>>>>>>sc.getMunicipalBond().getIdValor:"+sc.getMunicipalBond().getId());
		}
		
		/*System.out.println("findMunicipalBondByState>>>>>>listForReverseAllValor:"+ MunicipalBondManager.listForReverseAll.toString()
						   +" >>>tamano"+MunicipalBondManager.listForReverseAll.size());*/
		
		findTotalsByMunicipalBondByState(statusChanges);
	}
	
	public void clearValuesAll() {
		municipalBondsState = new ArrayList<MunicipalBond>();
		statusChanges = new ArrayList<StatusChange>();
		MunicipalBondManager.listForReverseAll.clear();
	}

	//rfarmijosm 2016-06-02 implementacion de reporte para nueva solicitud de baja
	private Date actualFromDate;
	private Date actualToDate;
	private Boolean isPreviosYear; 
	private Date previousFromDate;
	private Date previousToDate;
	private Integer actualYear;
	private Integer actualFromMonth;
	private Integer actualToMonth;
	
	private Integer previousYear;
	private Integer previousFromMonth;
	private Integer previousToMonth;
	
	private WaterSupply wSupply;
	private WaterBlockLog wblSelected;
	
	private List <WaterBlockDTO> blokingReport;
	
	
	
	/**
	 * reporte de solicitud de baja
	 * @param wbl
	 */
	public String buildReportUnsubscribeRequest(WaterBlockLog wbl){
		
		this.wblSelected = wbl;
		//limpiar lista
		blokingReport =new ArrayList<WaterBlockDTO>();
		
		wSupply = findWaterSupplyByServiceNumber(wbl);
		//inicia fechas consulta
		initializeReportDates();
		//inicia reporte
		String sql = getSqlReport();
		
		
		/*System.out.println("---------------------------isPreviosYear "+isPreviosYear);
		System.out.println("-------------- "+wbl.getId());
		System.out.println("-------------- "+wSupply.getId()+" ----- "+wSupply.getServiceNumber());
		System.out.println("-------------- "+actualYear);
		System.out.println("-------------- "+actualFromMonth);
		System.out.println("-------------- "+actualToMonth);
		System.out.println("-------------- "+actualFromDate);
		System.out.println("-------------- "+actualToDate);
		System.out.println("--------------------------- ");*/
		
		if(isPreviosYear){
			Query query = this.getEntityManager().createNativeQuery(sql);
			blokingReport = NativeQueryResultsMapper.map(query.getResultList(), WaterBlockDTO.class);
		}else{
			Query query = this.getEntityManager().createNativeQuery(sql);
			/*query.setParameter("sericeId", wSupply.getId());
			query.setParameter("year", actualYear);
			query.setParameter("startMonth", actualFromMonth);
			query.setParameter("endMonth", actualToMonth);
			query.setParameter("startDate", actualFromDate);
			query.setParameter("endDate", actualToDate);
			query.setParameter("sericeNumber", wSupply.getServiceNumber());*/
			query.setParameter(1, wSupply.getId());
			query.setParameter(2, actualYear);
			query.setParameter(3, actualFromMonth);
			query.setParameter(4, actualToMonth);
			query.setParameter(5, actualFromDate);
			query.setParameter(6, actualToDate);
			query.setParameter(7, String.valueOf(wSupply.getServiceNumber()));
			
			blokingReport = NativeQueryResultsMapper.map(query.getResultList(), WaterBlockDTO.class);
			
			/*
			 System.out.println("el tamaño es...........................ZZZZZ "+blokingReport.size());
			 for(WaterBlockDTO br:blokingReport){
				System.out.println(br.getEstado_consumption().substring(0, 4)+"\t"+br.getEstado_mb().substring(0, 4)+"\t"+br.getAct()+"\t"
			+br.getAnt()+"\t"+br.getConsumo()+"\t"
			+br.getTotal()+"\t"+br.getNumero()+"\t"
			);
			}*/
		}
		return "/waterservice/report/WaterSupplyBlockLogReport.xhtml";
	}
	
	
	public String getSqlReport(){
		return "select "
				+ "consumos.year anio, consumos.month mes, consumos.estado est_consumo, consumos.ant l_ante, consumos.act lect_act, consumos.consumo cons_mens, "
				+ "obligaciones.numero numb_oblig, obligaciones.estado estadobond, obligaciones.total totalpago "
				+ "from ( "
				+ "		select c.year, c.month, wms.name estado, c.previousreading ant, c.currentreading act , c.amount consumo "
				+ "		from Consumption c "
				+ "		left join watersupply w on c.watersupply_id = w.id "
				+ "		left join waterMeterStatus wms on c.watermeterstatus_id = wms.id "
				+ "		WHERE w.id = ? and c.year = ? and c.month between ? and ? "
				+ "		ORDER by c.month ASC "
				+ ") consumos inner join ( "
				+ "		select EXTRACT(MONTH FROM mb.serviceDate) mes, mb.number numero, mbs.name estado, mb.paidTotal total "
				+ "		from municipalbond mb "
				+ "		inner join municipalbondstatus mbs on mb.municipalbondstatus_id = mbs.id "
				+ "		WHERE mb.serviceDate between ? and ? "
				+ "		and mb.groupingCode like ? and mbs.id in (2,3,4,6,7,11,12) "
				+ "		order by mes "
				+ ") obligaciones on consumos.month = obligaciones.mes "
				+ "order by consumos.year, consumos.month, obligaciones.numero";
	}
	
	
	public WaterSupply findWaterSupplyByServiceNumber(WaterBlockLog wbl){
		
		String query ="select ws from WaterSupply ws "
				+ "where ws.serviceNumber =:serviceNumber";
		try{
			Query q=this.getEntityManager().createQuery(query);
			q.setParameter("serviceNumber", wbl.getServiceNumber());
			WaterSupply ws=	(WaterSupply) q.getSingleResult();
			return ws;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void initializeReportDates(){
		Calendar actualDate = Calendar.getInstance();
        actualDate.set(Calendar.DATE, actualDate.getActualMaximum(Calendar.DATE));
        
        Calendar previousDate = Calendar.getInstance();
        previousDate.set(Calendar.DAY_OF_MONTH, 1);
        previousDate.add(Calendar.MONTH, -4);
                
        SimpleDateFormat print = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println("actual fecha: " +print.format(actualDate.getTime()));
        //System.out.println("privia fecha: " +print.format(previousDate.getTime()));
        
        int actualYear = actualDate.get(Calendar.YEAR);
		this.actualYear = actualYear;
        int previousYear = previousDate.get(Calendar.YEAR);
        
        //System.out.println("los años::::::... actual "+actualYear+"   previo "+previousYear );
        
		if (actualYear > previousYear) {
			isPreviosYear=Boolean.TRUE;
			//la fecha para el anio actual
			Calendar auxFromDate = Calendar.getInstance();
			auxFromDate.set(actualYear, 0, 1);
			actualFromDate = auxFromDate.getTime();
			//la fehca de inicil del anio previo
			auxFromDate.set(actualYear, 0, 1);
			previousFromDate = previousDate.getTime();
			//la fecha de fin del anio previo
			auxFromDate.set(previousYear, 11, 31);
			previousToDate = auxFromDate.getTime();
			
        }else{
        	isPreviosYear=Boolean.FALSE;
        	actualFromDate = previousDate.getTime();
        	actualYear = previousDate.get(Calendar.YEAR);
        	actualFromMonth = previousDate.get(Calendar.MONTH) +1 ;
        	
        	actualToDate = actualDate.getTime();
        	actualToMonth = actualDate.get(Calendar.MONTH) +1 ;
        }
	}

	public WaterSupply getwSupply() {
		return wSupply;
	}

	public void setwSupply(WaterSupply wSupply) {
		this.wSupply = wSupply;
	}

	public List<WaterBlockDTO> getBlokingReport() {
		return blokingReport;
	}

	public void setBlokingReport(List<WaterBlockDTO> blokingReport) {
		this.blokingReport = blokingReport;
	}

	public WaterBlockLog getWblSelected() {
		return wblSelected;
	}

	public void setWblSelected(WaterBlockLog wblSelected) {
		this.wblSelected = wblSelected;
	}
	
	/**
	 * rfam 2021-03-15 controlar por rol el boton actualizar categoria
	 * @param roleKey
	 * @return
	 */
	public Boolean hasRole(String roleKey) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		String role = systemParameterService.findParameter(roleKey);
		if (role != null) {
			return userSession.getUser().hasRole(role);
		}
		return false;
	}
		
	
}
