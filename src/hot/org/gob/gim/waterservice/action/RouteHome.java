package org.gob.gim.waterservice.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.waterservice.facade.WaterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.waterservice.model.Consumption;
//import ec.gob.gim.waterservice.model.ConsumptionControl;
import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.MonthType;
import ec.gob.gim.waterservice.model.Route;
import ec.gob.gim.waterservice.model.RoutePeriod;
import ec.gob.gim.waterservice.model.RoutePreviewEmission;
import ec.gob.gim.waterservice.model.WaterMeter;
import ec.gob.gim.waterservice.model.WaterMeterDTO;
import ec.gob.gim.waterservice.model.WaterMeterStatus;
import ec.gob.gim.waterservice.model.WaterSupply;
import ec.gob.gim.waterservice.model.WaterSupplyAverage;
import ec.gob.gim.waterservice.model.WaterSupplyCategory;

@Name("routeHome")
public class RouteHome extends EntityHome<Route> {

	@In(create = true)
	RoutePeriodHome routePeriodHome;
	@In(create = true)
	WaterSupplyHome waterSupplyHome;
	@In
	private FacesMessages facesMessages;

	// editar Ruta
	Person readingMan;
	RoutePeriod routePeriod;

	// orden de lectura
	List<WaterSupply> waterSupplies;
	private Boolean disablereorganizeButton = true;

	// registrar Lecturas
	private Integer year = -1;
	private List<Consumption> consumptions;
	MonthType month;

	// para el resident chooser
	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;
	
	private Integer serviceNumber;
	//List<ConsumptionState> consumptionStates;

	// para las pre-emisiones
	public static String WATER_SERVICE_NAME = "/gim/WaterService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	// private SystemParameterService systemParameterService;
	private WaterService waterService;
	@In
	UserSession userSession;
	private BigDecimal avaragePreviousReading;
	private BigDecimal avarageActualReading;
	private EmissionOrder eo;

	private Consumption actualConsumption = new Consumption();
	private Consumption infoConsumption;
	//
	private int actualReadingPosition;
	private Integer changeIn;
	private Integer startYear;
	private SystemParameterService systemParameterService;

	// para las validaciones
	private String validationSearchType = "over";
	private BigDecimal valueValidation;

	// para panel luego de preemitido
	private Boolean isPreEmited = new Boolean(false);
	List<Consumption> consumptionsReport;
	private Date actualDate = new Date();
	private WaterMeterStatus waterMeterStatusWorking;
	private Boolean shoudPreEmit = new Boolean(false);
	private Boolean hasEditionReadingRole;

	private Date hourReport;

	public void setRouteId(Long id) {
		setId(id);
	}

	public Long getRouteId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	boolean isFirstTime = true;

	public void wire() {
		if (!isFirstTime) return;
		routePeriod = new RoutePeriod();
		getInstance();
		loadConsumptionStates();
		isFirstTime = false;
	}
	
	public void loadValues() {
		if (!isFirstTime) return;		
		getInstance();		
		year = getYears().get(0);
		month = MonthType.JANUARY;
		isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	private boolean newLoad = false;

	public boolean isNewLoad() {
		return newLoad;
	}

	public void setNewLoad(boolean newLoad) {
		this.newLoad = newLoad;
	}

	public void cleanList() {
		consumptions = new ArrayList<Consumption>();
		waterSuppliesIds = null;
		newLoad = false;
	}

	public void addRoutePeriod() {
		routePeriod.setReadingMan(readingMan);
		this.getInstance().add(routePeriod);
		// crear nuevos objetos
		routePeriod = new RoutePeriod();
		readingMan = new Person();
		this.identificationNumber = null;
	}

	public void editRoutePeriod(RoutePeriod rp) {
		routePeriod = rp;
		readingMan = rp.getReadingMan();
		if (readingMan != null) identificationNumber = readingMan.getIdentificationNumber();		
	}

	@Override
	public String persist() {
		String stringReturn = "";
		Query query = this.getEntityManager().createNamedQuery("Route.countByName");
		query.setParameter("routeName", this.getInstance().getName());
		int count = Integer.parseInt(query.getSingleResult().toString());
		if (count == 0) {
			stringReturn = super.persist();
		} else {
			String message = Interpolator.instance().interpolate("#{messages['route.routeNameAlreadyExist']}", new Object[0]);
			facesMessages.addFromResourceBundle(Severity.ERROR, message, new Object());
			stringReturn = "";
		}
		return stringReturn;
	}

	public List<Person> autoComplete(Object o) {
		Query query = this.getEntityManager().createNamedQuery("Person.findReadingMan");
		query.setParameter("criteria", o.toString());
		return (List<Person>) query.getResultList();
	}

	/*
	 * para autocompletado en cuadro de texto escribiendo el nombre
	 */
	public List<Route> autoCompleteRouteByName(Object o) {
		Query query = this.getEntityManager().createNamedQuery("Route.findByName");
		query.setParameter("routeName", o.toString());
		return (List<Route>) query.getResultList();
	}
	
	private List<Integer> years;

	public List<Integer> getYears() {
		if(years != null && years.size() > 0) return years;
		years = new ArrayList<Integer>();
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		startYear = systemParameterService.findParameter("MINIMUM_EMISSION_YEAR");

		if (startYear != null) {
			int iStart = startYear;
			int iActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
			int amount = (iActual - iStart);
			for (int i = 0; i < amount; i++) {
				years.add(iActual);
				iActual--;
			}
		} else {
			System.out.println("es nulo no entiendo pos? " + startYear);
		}

		return years;
	}

	/*
	 * eventos orden de lectura
	 */

	public void loadWaterSupplyes() {
		Query query = this.getEntityManager().createNamedQuery("WaterSupply.findByRoute");
		query.setParameter("routeId", this.getInstance().getId());
		query.setParameter("isCanceled", new Boolean(false));
		
		waterSupplies = query.getResultList();
		
		if (waterSupplies.size() > 0) {
			disablereorganizeButton = false;
		} else {
			disablereorganizeButton = true;
		}
	}

	/**
	 * Update the order of the services one by one, depends on the event in the
	 * table
	 * 
	 * @param ws
	 */
	public void updateServiceOrderSelected(WaterSupply ws) {
		joinTransaction();
		getEntityManager().merge(ws);
		getEntityManager().flush();
	}

	public String save() {
		for (WaterSupply ws : waterSupplies) {
			joinTransaction();
			getEntityManager().merge(ws);
			getEntityManager().flush();
		}
		return "updated";
	}

	/**
	 * Updates all reading orders, depending on the parameter "changeIn"
	 */
	public void updateServiceOrderAll() {
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		changeIn = systemParameterService.findParameter("AMOUNT_BETWEEN_WATER_ROUTE");
		System.out.println("========> changeIn " + changeIn);
		if (changeIn != null) {
			if (waterSupplies != null) {
				//int routeOrderActual = 0;
				//int routeOrderAnterior = 0;
				int i = 1;
				for (WaterSupply ws : waterSupplies) {
					if (i == 0) {
						int routeOrderActual = ws.getRouteOrder() + changeIn;
						ws.setRouteOrder(routeOrderActual);
						//System.out.println("si 0 : ");
						//routeOrderAnterior =  ws.getRouteOrder();
						//routeOrderActual = ws.getRouteOrder() + changeIn;
						//ws.setRouteOrder(routeOrderActual);
					}else{
						int routeOrderActual = ws.getRouteOrder() + (changeIn * i);
						ws.setRouteOrder(routeOrderActual);
						//routeOrderActual = ws.getRouteOrder();
						//int resta= changeIn - (routeOrderActual - routeOrderAnterior);
						//ws.setRouteOrder(routeOrderActual + resta);
						//routeOrderAnterior =  ws.getRouteOrder();
					}
					//routeOrderActual = ws.getRouteOrder() + changeIn;
					//ws.setRouteOrder(routeOrderActual);
					/*if (i == 0) {
						routeOrderActual = ws.getRouteOrder();
					} else {
						routeOrderActual = routeOrderActual + changeIn + 1;
						ws.setRouteOrder(routeOrderActual);
					}
					i++;*/
					i++;
				}
			} else {
				String message = Interpolator.instance().interpolate("#{messages['route.routeLoadServices']}", new Object[0]);
				facesMessages.addFromResourceBundle(Severity.ERROR, message, new Object());
			}
		} else {
			String message = Interpolator.instance().interpolate("#{messages['route.parameterNoRegistred']}", new Object[0]);
			facesMessages.addFromResourceBundle(Severity.ERROR, message, new Object());
		}

	}
	
	private List<Consumption> loadRecordReading(Integer yearInt, Integer monthInt, Long routeId, Boolean isCanceled){
		Query query = this.getEntityManager().createNamedQuery("Consumption.findByYearMonth");
		query.setParameter("year", yearInt);
		query.setParameter("month", monthInt);
		query.setParameter("routeId", routeId);
		query.setParameter("isCanceled", isCanceled);		
		return query.getResultList();
	}
	
	private Boolean readyForPrint;
	
	public String loadPreviousRecordReading() {		
		int monthInt = month.getMonthInt() - 1;
		int yearInt = year;
		// en el caso q sea enero el año debe disminuir en 1 y el mes es 12
		if (monthInt == 0) {
			yearInt = yearInt - 1;
			monthInt = 12;
		}
		consumptions = loadRecordReading(yearInt, monthInt, this.getInstance().getId(), Boolean.FALSE);
		if (consumptions.size() > 0) {
			readyForPrint = Boolean.TRUE;
			return "print";
		}
		readyForPrint = Boolean.FALSE;		
		return "empty";
	}
	
	public void cleanForPrint(){		
		readyForPrint = null;
	}
	
	
	public void findByServiceNumber(){		
		if(serviceNumber == null) return;
		Consumption cs = findConsumptionByServiceNumber(serviceNumber);		
		consumptions = new ArrayList<Consumption>();
		if(cs == null) return;
		consumptions.add(cs);
	}
	
	private Consumption findConsumptionByServiceNumber(Integer serviceNumber){
		for(Consumption cs: consumptions){
			if(cs.getWaterSupply().getServiceNumber().equals(serviceNumber)){
				return cs;
			}
		}
		return null;
	}


	/*
	 * eventos registro de lecturas
	 */
	public void loadRecordReadings() {
		serviceNumber = null;
		countServicesNumber();
		shoudPreEmit = isRouteEmited();
		System.out.println("ha sido pre-emitida..........................................." + shoudPreEmit);
		// la consulta debe ser de un mes atras para poder cargar la lectura
		// anterior
		int monthInt = month.getMonthInt() - 1;
		int yearInt = year;
		// en el caso q sea enero el año debe disminuir en 1 y el mes es 12
		if (monthInt == 0) {
			yearInt = yearInt - 1;
			monthInt = 12;
		}

		findSelectMonthRecordReadings();		
		List<Consumption> list = loadRecordReading(yearInt, monthInt, this.getInstance().getId(), Boolean.FALSE);		
		waterMeterDTOs = loadWaterMeterByWaterSupplies();
		consumptions = loadNewConsumtionList(list);

		if (consumptions.size() > 0) newLoad = true;

		//whereStartRecordReading();
	}
	
	Integer servicesNumber = 0;
	
	public void countServicesNumber() {
		Query q = this.getEntityManager().createQuery("select COUNT(ws) from WaterSupply ws "
								+ "where ws.route.id = :routeId and ws.isCanceled = false");
		q.setParameter("routeId", this.getInstance().getId());
		if (q.getResultList().size() > 0) {
			servicesNumber = Integer.parseInt(q.getSingleResult().toString());
		}
	}
	
	private WaterSupplyCategory commercialWaterSupplyCategory;
	
	public WaterSupplyCategory loadCommercialWaterSupplyCategory(){
		if(commercialWaterSupplyCategory != null) return commercialWaterSupplyCategory;
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
					
		 commercialWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_COMMERCIAL");
		return commercialWaterSupplyCategory;
	}

	// ///////////////////////////////////

//	public void loadRecordReadingsRecalculate() {		
//		System.out.println("entra a cargar::::!!!!!!!!!!!!!!!!! recalculate");
//		// la coinsulta debe ser de un mes atras para poder cargar la lectura
//		// anterior
//		int monthInt = month.getMonthInt() - 1;
//		int yearInt = year;
//		// en el caso q sea enero el año debe disminuir en 1 y el mes es 12
//		if (monthInt == 0) {
//			yearInt = yearInt - 1;
//			monthInt = 12;
//		}
//
//		findSelectMonthRecordReadings();
//		Query query = this.getEntityManager().createNamedQuery("Consumption.findByYearMonthwaterSupplyCategory");
//		query.setParameter("year", yearInt);
//		query.setParameter("month", monthInt);
//		query.setParameter("routeId", this.getInstance().getId());
//		query.setParameter("waterSupplyCategoryId", loadCommercialWaterSupplyCategory());
//
//		consumptions = loadNewConsumtionList(query.getResultList());
//		// consumptions = loadNewConsumtionList(currentsConsumptions);
//		// = consumptions;
//		if (consumptions.size() > 0)
//			newLoad = true;
//
//		whereStartRecordReading();
//	}

	// ////////////////////////////

	public BigDecimal findAverage(WaterSupply ws) {
		for (WaterSupplyAverage wsa : waterSupplyAverages) {
			if (wsa.getWaterSupplyId() == ws.getId()) {
				return wsa.getAmount();
			}
		}
		return BigDecimal.ZERO;
	}
	
	private Boolean findWaterRegisterEnabled() {		
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		return systemParameterService.findParameter("WATER_SERVICE_REGISTER_ENABLED");		
	}

	/**
	 * Create a new Consumption's list, if a Consumption exist is added to the
	 * new list
	 * 
	 * @param oldList
	 * @return
	 */
	public List<Consumption> loadNewConsumtionList(List<Consumption> oldList) {
		Boolean registerEnabled = findWaterRegisterEnabled();
		avaragePreviousReading = new BigDecimal(0);		
		List<Consumption> newConsumptions = new ArrayList<Consumption>();
		Consumption conNew;

		/*List<Long> waterSupplies = new ArrayList<Long>();

		for (Consumption conOld : oldList) {
			waterSupplies.add(conOld.getWaterSupply().getId());
		}

		generateAverageConsumption(waterSupplies);*/

		for (Consumption conOld : oldList) {			
			
			conNew = findIfExist(conOld);
					
									
			if (conNew == null) {
				conNew = new Consumption();
				//conNew.setCheckingRecord(conOld.getCheckingRecord());
				conNew.setConsumptionState(conOld.getConsumptionState());
				conNew.setMonth(month.getMonthInt());
				// conNew.setMunicipalBond(conOld.getMunicipalBond());
				conNew.setPreviousReading(conOld.getCurrentReading());
				conNew.setReadingDate(new Date());
				conNew.setWaterMeterStatus(conOld.getWaterMeterStatus());				
				conNew.setWaterSupply(conOld.getWaterSupply());
				conNew.setYear(year);
				conNew.setConsumptionState(generatedConsumptionState);
				conNew.setApplyElderlyExemption(conOld.getApplyElderlyExemption());
				conNew.setApplySpecialExemption(conOld.getApplySpecialExemption());
				
				
				//conNew.setPreviousAverage(findAverage(conOld.getWaterSupply()));
				if(registerEnabled != null) conNew.setDisabled(registerEnabled);
				newConsumptions.add(conNew);
				/*
				 * this.avaragePreviousReading = avaragePreviousReading .add(new
				 * BigDecimal(conOld.getCurrentReading()));
				 */
			} else {
				// conNew.setMunicipalBond(findMunicipalBond(conNew));
				//conNew.setPreviousAverage(findAverage(conNew.getWaterSupply()));				
				checkCorrectReading(conNew);
				if(registerEnabled != null) conNew.setDisabled(registerEnabled);
				newConsumptions.add(conNew);

			}			
		}
		return newConsumptions;
	}

	/**
	 * encontrar el municipalbond para un determinado consumo emitido
	 * 
	 * @param c
	 * @return
	 */
	public MunicipalBond findMunicipalBond(Consumption c) {
		String sentence = "SELECT mb FROM MunicipalBond mb WHERE "
				+ "mb.serviceDate between :firstDayMonth and :lastDayMonth and "
				+ "mb.adjunct.code like concat(:code,'%')";

		String code = c.getWaterSupply().getServiceNumber().toString();

		int actualMonth;
		int year;

		actualMonth = c.getMonth() - 1;
		year = c.getYear();

		Calendar startDate = Calendar.getInstance();
		startDate.set(year, actualMonth, 1);

		int endDayOfMonth = startDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		Calendar endDate = Calendar.getInstance();
		endDate.set(year, actualMonth, endDayOfMonth);

		Query q = this.getEntityManager().createQuery(sentence);
		q.setParameter("firstDayMonth", startDate.getTime());
		q.setParameter("lastDayMonth", endDate.getTime());
		q.setParameter("code", code);

		List<MunicipalBond> bonds = q.getResultList();
		if (bonds.size() > 0) {
			return bonds.get(0);
		} else {
			return null;
		}
	}

	/*public void loadRecordReadingsPanel() {
		if (consumptions == null) return;
		whereStartRecordReading();
	}*/

	// /////////////////

	/**
	 * cuando se lanza el panel de registro de lecturas determina donde se quedo
	 * el ultimo registro inrgesado
	 
	public void whereStartRecordReading() {
		actualReadingPosition = 0;
		//int i = 0;
		for (Consumption c : consumptions) {
			if (c.getCurrentReading() != null) {
				actualReadingPosition++;
			} else {
				System.out.println("entra a un brake");
				break;
			}
		}
		System.out.println("where 1. " + actualReadingPosition);
		if (actualReadingPosition >= consumptions.size()) {
			actualReadingPosition = consumptions.size() - 1;
		}
		System.out.println("where 2. " + actualReadingPosition);
		if (consumptions.size() > 0) {
			actualConsumption = consumptions.get(actualReadingPosition);
		}

		if (waterMeterStatusWorking == null) loadWaterMeterStatusWorking();

		actualConsumption.setWaterMeterStatus(waterMeterStatusWorking);
	}*/

	public void loadWaterMeterStatusWorking() {
		Query q = this.getEntityManager().createNamedQuery("WaterMeterStatus.findWaterMeterStatus");
		List<WaterMeterStatus> list = q.getResultList(); 
		if (list.size() > 0) {
			waterMeterStatusWorking = (WaterMeterStatus) list.get(0);
		}
	}

/*	public void nextConsumption() {
		actualReadingPosition++;
		if (actualReadingPosition < consumptions.size()) {
			actualConsumption = consumptions.get(actualReadingPosition);
		} else {
			actualReadingPosition--;
		}
		if (waterMeterStatusWorking == null) loadWaterMeterStatusWorking();
		actualConsumption.setWaterMeterStatus(waterMeterStatusWorking);
		System.out.println("genera la sigueinte posicion " + actualReadingPosition);
	}

	public void previousConsumption() {
		actualReadingPosition--;
		if (actualReadingPosition >= 0) {
			actualConsumption = consumptions.get(actualReadingPosition);
		} else {
			actualReadingPosition++;
		}

	}*/

		
	private  ConsumptionState generatedConsumptionState;
	private  ConsumptionState enteredConsumptionState;
	private  ConsumptionState checkedConsumptionState;
	private  ConsumptionState preEmittedConsumptionState;
	
	public void loadConsumptionStates() {
		if(enteredConsumptionState != null) return;
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		enteredConsumptionState = systemParameterService.materialize(ConsumptionState.class, "CONSUMPTIONSTATE_ID_ENTERED");
		generatedConsumptionState = systemParameterService.materialize(ConsumptionState.class, "CONSUMPTIONSTATE_ID_GENERATED");		
		checkedConsumptionState = systemParameterService.materialize(ConsumptionState.class, "CONSUMPTIONSTATE_ID_CHECKED");
		preEmittedConsumptionState = systemParameterService.materialize(ConsumptionState.class, "CONSUMPTIONSTATE_ID_PREEMITTED");		
	}

	/**
	 * generates the average consumption of the last 3 months
	 * 
	 * @param ws
	 * @return
	 */
	List<WaterSupplyAverage> waterSupplyAverages;

	public void generateAverageConsumption(List<Long> ws) {
		List<WaterSupplyAverage> waterSupplyAveragesLastYear = null;
		int monthEnd = month.getMonthInt() - 1;
		int monthStart = monthEnd - 2;
		int yearAux = year;
		boolean lastYear = false;
		if (monthEnd == 0) {
			yearAux = yearAux - 1;
			monthEnd = 12;
			monthStart = monthEnd - 2;
		} else {
			// es necesario obtener los datos del año pasado octubre, noviembre
			// y/o diciembre
			lastYear = true;
			if (monthEnd == 1) {
				waterSupplyAveragesLastYear = runAverageQuery(ws, yearAux - 1,
						11, 12);
			}
			if (monthEnd == 2) {
				waterSupplyAveragesLastYear = runAverageQuery(ws, yearAux - 1,
						12, 12);
			}
		}
		waterSupplyAverages = runAverageQuery(ws, yearAux, monthStart, monthEnd);
		if (lastYear) {
			for (WaterSupplyAverage wa : waterSupplyAverages) {
				if (waterSupplyAveragesLastYear == null) {
					wa.setAmount(wa.getAmount().divide(BigDecimal.valueOf(2)));
				} else {
					for (WaterSupplyAverage waly : waterSupplyAveragesLastYear) {
						if (wa.getWaterSupplyId() == waly.getWaterSupplyId()) {
							wa.setAmount(wa.getAmount().add(waly.getAmount())
									.divide(BigDecimal.valueOf(2)));
							waterSupplyAveragesLastYear.remove(waly);
							break;
						}
					}
				}
			}
		}
	}

	public List<WaterSupplyAverage> runAverageQuery(List<Long> ws, int year,
			int monthStart, int monthEnd) {
		if (ws.size() == 0)
			return new ArrayList<WaterSupplyAverage>();
		if (monthStart == -1 || monthStart == 0) {
			monthStart = 1;
		}

		Query query = this.getEntityManager().createNamedQuery("Consumption.amountAverageByWaterSupply");
		query.setParameter("year", year);
		query.setParameter("monthStart", monthStart);
		query.setParameter("monthEnd", monthEnd);
		query.setParameter("waterSupplyIds", ws);
		return query.getResultList();
	}

	List<Consumption> currentsConsumptions;

	public void findSelectMonthRecordReadings() {

		Query query = this.getEntityManager().createNamedQuery("Consumption.findByYearMonth");
		query.setParameter("year", year);
		query.setParameter("month", month.getMonthInt());
		query.setParameter("routeId", this.getInstance().getId());
		query.setParameter("isCanceled", Boolean.FALSE);
		currentsConsumptions = query.getResultList();
	}
	
	private List<Long> waterSuppliesIds;

	public Consumption findIfExist(Consumption conOld) {		
		Long id = conOld.getWaterSupply().getId();
		if(waterSuppliesIds == null){
			waterSuppliesIds = new ArrayList<Long>();			
			for (Consumption cs : currentsConsumptions) {
				waterSuppliesIds.add(cs.getWaterSupply().getId());
			}
		}
		
		if(waterSuppliesIds.contains(id)){			
			return currentsConsumptions.get(waterSuppliesIds.indexOf(id));			
		}		
		
		return null;
	}

	/**
	 * saves the new reading
	 */
	public void checkRecordReading(Consumption conNew) {
		if (checkCorrectReading(conNew)) {
			conNew.setMonthType(month);
		}
	}

	public String saveAll() {
		for (Consumption c : consumptions) {
			if (c.getIsValidReading() != null && c.getIsValidReading()) {
				if (c.getId() == null) {
					joinTransaction();
					getEntityManager().persist(c);
					getEntityManager().flush();
				} else {
					joinTransaction();
					getEntityManager().flush();
					raiseAfterTransactionSuccessEvent();
				}
			}
		}
		return "ready";
	}

	/*public void checkCurrentReadingPanel() {
		Consumption conNew = actualConsumption;
		// int pos = consumptions.indexOf(conNew);
		if (checkCorrectReading(conNew)) {
			System.out.println("is ok");
		} else {
			System.out.println("bad");
		}
	}*/
	
	private List<WaterMeterDTO> waterMeterDTOs;
	
	private List<WaterMeterDTO> loadWaterMeterByWaterSupplies(){
		Query q = this.getEntityManager().createNamedQuery("WaterMeter.findDTOByWaterSupplyActualMeter");
		q.setParameter("active", Boolean.TRUE);		
		return q.getResultList();
		
	}
		
	private Integer findCurrentWaterMeterDigitsNumber(Long waterSupplyId){
		for(WaterMeterDTO wmd: waterMeterDTOs){
			if(wmd.getWaterSupplyId().equals(waterSupplyId)){
				return wmd.getWaterMeterDigitsNumber();
			}
		}
		return null;
	}

	/**
	 * checks if the current reading is correct
	 * 
	 * @param conNew
	 * @return
	 */
	public Boolean checkCorrectReading(Consumption conNew) {
		Boolean isChecked = false;		
		//WaterMeter actualWaterMeter = findWaterMeter(conNew.getWaterSupply());
		Integer waterMeterDigitsNumber = findCurrentWaterMeterDigitsNumber(conNew.getWaterSupply().getId());

		if (conNew.getCurrentReading() != null) {
			if (conNew.getCurrentReading() > -1) {
				if (conNew.getPreviousReading() > conNew.getCurrentReading()) {

					int verificacion = checkLowerConsumption(waterMeterDigitsNumber, conNew);
					System.out.println(":::::::::::::==========>>>> la verificacion es "+ verificacion);
					if (verificacion > 0) {
						conNew.setDifferenceInReading(verificacion + conNew.getCurrentReading());
						conNew.setAmount(new BigDecimal(conNew.getDifferenceInReading()));
						conNew.setConsumptionState(checkedConsumptionState);
						conNew.setIsValidReading(true);
						isChecked = true;
						conNew.setErrorMessage("");
					} else {
						conNew.setIsValidReading(false);
						conNew.setErrorMessage(noLessValue);
						conNew.setDifferenceInReading(null);
						conNew.setAmount(null);
					}
				} else {
					conNew.setDifferenceInReading(conNew.getCurrentReading() - conNew.getPreviousReading());
					conNew.setAmount(new BigDecimal(conNew.getDifferenceInReading()));
					conNew.setConsumptionState(checkedConsumptionState);
					conNew.setIsValidReading(true);
					isChecked = true;
					conNew.setErrorMessage("");
				}

			} else {
				conNew.setIsValidReading(false);
				conNew.setErrorMessage(noNegative);
				conNew.setDifferenceInReading(null);
				conNew.setAmount(null);
			}
		} else {
			conNew.setIsValidReading(false);
			conNew.setErrorMessage(needvalue);
			conNew.setDifferenceInReading(null);
			conNew.setAmount(null);
		}

		return isChecked;
	}

	private String noLessValue = Interpolator.instance().interpolate("#{messages['recordReading.noLessValue']}", new Object[0]);
	private String noNegative = Interpolator.instance().interpolate("#{messages['recordReading.NoNegative']}", new Object[0]);
	private String needvalue = Interpolator.instance().interpolate("#{messages['recordReading.needValue']}", new Object[0]);

	/**
	 * comprueba si es posible ingresar valores menores al consumo actual p.j.
	 * ingresar 3 si el consumo enterior es 99, depende del numero de digitos
	 * del medidor
	 * 
	 * @param actualWaterMeter
	 * @param conNew
	 * @return si es 0 no es posible ingresar, si es mayor a cero si hay como
	 *         ingresar
	 */
	public int checkLowerConsumption(Integer digitsNumber, Consumption conNew) {
		int number = 0;
		if (digitsNumber != null) {
			String val = "";
			for (int i = 0; i < digitsNumber; i++) {
				val = val + "9";
			}
			number = Integer.parseInt(val);
			int resultado = number - conNew.getPreviousReading().intValue();
			if (resultado <= 9999) {
				//return resultado;
				if(resultado==0){
                    return 1;
                }else{
                    return resultado;
                }
			}
			return 0;
		} else {
			return 0;
		}
	}

	public WaterMeter findWaterMeter(WaterSupply ws) {
		Query q = this.getEntityManager().createNamedQuery("WaterMeter.findByWaterSupplyActualMeter");
		q.setParameter("active", new Boolean(true));
		q.setParameter("wsId", ws.getId());
		List<WaterMeter> list = q.getResultList();
		if (list.size() > 0) {
			return (WaterMeter) list.get(0);
		} else {
			return null;
		}
	}

	// //////////////////////////////////////
	/**
	 * saves the new reading
	 */
	/*public void saveRecordReadingPanel() {
		System.out.println(">>>>>>>>>>>>> entra a a guardar desde el panel");
		Consumption conNew = actualConsumption;
		// int pos = consumptions.indexOf(conNew);
		if (checkCorrectReading(conNew)) {
			System.out.println(">>>>>>>>>>>>> entra a a guardar desde el panel >>>> luego del chek");
			conNew.setMonthType(month);
			if (conNew.getId() == null) {
				joinTransaction();
				getEntityManager().persist(conNew);
				getEntityManager().flush();
				// consumptions.set(pos, conNew);
			} else {
				joinTransaction();
				getEntityManager().flush();
				raiseAfterTransactionSuccessEvent();
			}
			System.out.println(">>>>>>>>>>>>> entra a a guardar desde el panel >>>> luego de guardar o midificar");
			nextConsumption();
			System.out.println(">>>>>>>>>>>>> entra a a guardar desde el panel >>>> luego de nextConsuntion");
			// addAvarageNewConsumption();
		}
	}*/

	/**
	 * checks if the current reading is correct
	 * 
	 * @param conNew
	 * @return
	 */
	/*public Boolean checkCorrectReadingPanel(Consumption conNew) {
		conNew = actualConsumption;
		Boolean isChecked = false;
		// falta verificar el numero dedigitos del medidor.. no existen aun
		// medidores
		if (conNew.getCurrentReading() != null) {
			if (conNew.getCurrentReading() > -1) {
				conNew.setDifferenceInReading(conNew.getCurrentReading() - conNew.getPreviousReading());
				conNew.setAmount(new BigDecimal(conNew.getDifferenceInReading()));
				conNew.setConsumptionState(checkedConsumptionState);
				conNew.setIsValidReading(true);
				isChecked = true;
				conNew.setErrorMessage("");
			} else {
				conNew.setIsValidReading(false);
				conNew.setErrorMessage(noNegative);
			}
		} else {
			conNew.setIsValidReading(false);
			conNew.setErrorMessage(needvalue);
		}
		return isChecked;
	}*/

	public Route getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public Person getReadingMan() {
		return readingMan;
	}

	public void setReadingMan(Person readingMan) {
		this.readingMan = readingMan;
	}

	public RoutePeriod getRoutePeriod() {
		return routePeriod;
	}

	public void setRoutePeriod(RoutePeriod routePeriod) {
		this.routePeriod = routePeriod;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<WaterSupply> getWaterSupplies() {
		return waterSupplies;
	}

	public void setWaterSupplies(List<WaterSupply> waterSupplies) {
		this.waterSupplies = waterSupplies;
	}

	public Boolean getDisablereorganizeButton() {
		return disablereorganizeButton;
	}

	public void setDisablereorganizeButton(Boolean disablereorganizeButton) {
		this.disablereorganizeButton = disablereorganizeButton;
	}

	public List<Consumption> getConsumptions() {
		return consumptions;
	}

	public void setConsumptions(List<Consumption> consumptions) {
		this.consumptions = consumptions;
	}

	public MonthType getMonth() {
		return month;
	}

	public void setMonth(MonthType month) {
		this.month = month;
	}

	// panel de busqueda
	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
		this.resident = null;
		setReadingMan(null);
	}

	public void search() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident1 = (Resident) query.getSingleResult();
			setReadingMan((Person) resident1);
			// this.concession.setResident();
			this.resident = resident1;
		} catch (Exception e) {
			setReadingMan(null);
			this.resident = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident1 = (Resident) component.getAttributes().get("resident");
		setReadingMan((Person) resident1);
		this.resident = resident1;
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

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

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	/**
	 * .
	 * 
	 * @throws Exception
	 */

	public String preEmiteWater() {
		if (this.consumptions.size() == servicesNumber.intValue()) {
			if (shouldEmit()) {
				if (!isRouteEmited()) {
					if (waterService == null)
						waterService = ServiceLocator.getInstance().findResource(WATER_SERVICE_NAME);
					try {
						
						eo = waterService.calculatePreEmissionOrderWaterConsumption(this.consumptions,
										userSession.getFiscalPeriod(),
										userSession.getPerson(), year,
										month.getMonthInt(), observation);
						
						waterService.saveEmissionOrderMerchandising(eo, true,createRoutePreviewEmission(),findWaterSuppliesIds());
						
						if (eo != null) {
							String message = Interpolator.instance().interpolate("La Ruta "
													+ this.getInstance().getName()
													+ " ha sido pre-emitida", new Object[0]);
							facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.INFO,message);
							isPreEmited = true;
							
							//updateConsumption(waterService.getConsumptionsToUpdate());
							
							
							return "complete";
						}
						isPreEmited = false;
						return null;
					} catch (Exception e) {
						e.printStackTrace();
						String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}",new Object[0]);
						isPreEmited = false;
						facesMessages.addToControl(
										"residentChooser",
										org.jboss.seam.international.StatusMessage.Severity.ERROR,
										message);
						return null;
					}
	
				} else {
					String message = Interpolator.instance().interpolate("Ya Ruta ya ha sido Pre-Emitida", new Object[0]);
					isPreEmited = false;
					facesMessages.addToControl(
									"residentChooser",
									org.jboss.seam.international.StatusMessage.Severity.ERROR,
									message);
					return null;
				}
			} else {
				String message = Interpolator.instance().interpolate(
								"No se han registrado todas las lecturas para la ruta seleccionada",
								new Object[0]);
				isPreEmited = false;
				facesMessages.addToControl("residentChooser",
						org.jboss.seam.international.StatusMessage.Severity.ERROR,
						message);
				return null;
			}
		}else{
			String message = Interpolator
					.instance()
					.interpolate(
							"La cantidad de servicios y de lecturas no coinciden",
							new Object[0]);
			isPreEmited = false;
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void updateConsumption(List<Consumption> consumptionsToUpdate){
		if (consumptionsToUpdate.size() == 0)return;
		System.out.println("_______________________________ llega para modificarssssssssssss "+consumptionsToUpdate.size());
		//entityManager.getTransaction().begin();
		int i=0;
		for (Consumption cc : consumptionsToUpdate) {	
			entityManager.merge(cc);
			if ((i % 100) == 0) {
				entityManager.flush();
				entityManager.clear();
			}
			i++;
		}
		entityManager.flush();
		entityManager.clear();
	}

	private List<Long> findWaterSuppliesIds() {
		List<Long> list = new ArrayList<Long>();
		for (Consumption c : consumptions) {
			list.add(c.getWaterSupply().getId());
		}
		return list;
	}

	public RoutePreviewEmission createRoutePreviewEmission() {
		RoutePreviewEmission rpe = new RoutePreviewEmission();
		rpe.setDate(new Date());
		rpe.setHasPreEmit(true);
		rpe.setMonthType(month);
		rpe.setMonth(month.getMonthInt());
		rpe.setRoute(this.getInstance());
		rpe.setYear(this.year);
		return rpe;
	}

	/**
	 * .
	 * 
	 * @throws Exception
	 
	public String preEmiteOneWaterService() {

		if (waterService == null)
			waterService = ServiceLocator.getInstance().findResource(WATER_SERVICE_NAME);
		try {
			/*
			 * eo = waterService.calculatePreEmissionOrderByService(
			 * this.consumptions, userSession.getFiscalPeriod(),
			 * userSession.getPerson(), year, month.getMonthInt()); if (eo !=
			 * null) { // loadRecordReadings(); String message =
			 * Interpolator.instance().interpolate( "La Ruta " +
			 * this.getInstance().getName() + " ha sido pre-emitida", new
			 * Object[0]); facesMessages .addToControl( "residentChooser",
			 * org.jboss.seam.international.StatusMessage.Severity.INFO,
			 * message); isPreEmited = true; return "complete"; } isPreEmited =
			 * false;
			 */
	/*		return null;
		} catch (Exception e) {
			e.printStackTrace();
			String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}", new Object[0]);
			isPreEmited = false;
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}

	}*/

	public void saveRecordReading(Consumption conNew) {

		if (checkCorrectReading(conNew)) {
			conNew.setMonthType(month);
			if (conNew.getId() == null) {
				joinTransaction();
				getEntityManager().persist(conNew);
				getEntityManager().flush();
			} else {
				joinTransaction();
				getEntityManager().flush();
				raiseAfterTransactionSuccessEvent();
			}
		}
	}

	/*public void startPreEmiting() {
		String message = Interpolator.instance().interpolate("La Ruta " + this.getInstance().getName()
						+ " ha sido pre-emitida", new Object[0]);
		facesMessages.addToControl("residentChooser",
				org.jboss.seam.international.StatusMessage.Severity.INFO,
				message);
		isPreEmited = true;
		// preEmiteWater();
	}*/

	public String waterPreEmited() {
		System.out.println(">>>>>>>>>>>>>>>>>>>entra a preemitir agua");
		if (this.consumptions.size() == servicesNumber.intValue()) {
			return preEmiteWater();
		} else {
			String message = Interpolator
					.instance()
					.interpolate(
							"Revisar medidores de esta RUTA",
							new Object[0]);
			isPreEmited = false;
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}

	/**
	 * determina si la ruta esta preEmitida
	 * 
	 * @return
	 */
	public Boolean findIfPreEmited() {
		if (month != null && year != null) {
			Query query = this.getEntityManager().createNamedQuery("Consumption.isEmitedByYearMonth");
			query.setParameter("year", year);
			query.setParameter("month", month.getMonthInt());
			query.setParameter("routeId", this.getInstance().getId());
			int actualConsumptions = 0;
			if (query.getResultList().size() > 0) {
				actualConsumptions = query.getResultList().size();
			}
			if (actualConsumptions == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Comprueba que ya estan ingresadas todas las lecturas y sean correctas
	 * <i><b>Cuenta el numero de lecturas en la base de datos y lo compara con
	 * el numero de registro de la lista</b></i>
	 * 
	 * @return
	 */
	public boolean shouldEmit() {
		if(consumptions != null){
			for(Consumption c:consumptions){
				if(c.getCurrentReading() == null) return false;
			}
		}
		if (month != null && year != null) {
			Query query = this.getEntityManager().createNamedQuery("Consumption.countByYearMonth");
			query.setParameter("year", year);
			query.setParameter("month", month.getMonthInt());
			query.setParameter("routeId", this.getInstance().getId());
			int actualConsumptions = 0;
			if (query.getResultList().size() > 0) {
				actualConsumptions = query.getResultList().size();
			}
			if (consumptions != null) {
				int intConsumptions = consumptions.size();
				System.out.println("............................. " + actualConsumptions + " ...... " + intConsumptions);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Determina si los consumos de la ruta ya estan emitidos
	 * 
	 * @return
	 */
	public boolean isRouteEmited() {
		Query query = this.getEntityManager().createNamedQuery("RoutePreviewEmission.finIfPreEmission");
		query.setParameter("year", year);
		query.setParameter("month", month.getMonthInt());
		query.setParameter("routeId", this.getInstance().getId());
		List<RoutePreviewEmission> totalConsumptionEmited = query.getResultList();
		if (query.getResultList().size() > 0) {
			int emissionNumber= Integer.parseInt(query.getSingleResult().toString());
			if(emissionNumber>0){
				return true;
			}else{
				return false;
			}
		}
		//regreso true, si por algun caso ahy problema, q no se emita hasta revisar
		return true;
	}

	public BigDecimal getAvaragePreviousReading() {
		return avaragePreviousReading;
	}

	public void setAvaragePreviousReading(BigDecimal avaragePreviousReading) {
		this.avaragePreviousReading = avaragePreviousReading;
	}

	public BigDecimal getAvarageActualReading() {
		return avarageActualReading;
	}

	public void setAvarageActualReading(BigDecimal avarageActualReading) {
		this.avarageActualReading = avarageActualReading;
	}

	public EmissionOrder getEo() {
		return eo;
	}

	public void setEo(EmissionOrder eo) {
		this.eo = eo;
	}

	public Consumption getActualConsumption() {
		return actualConsumption;
	}

	public void setActualConsumption(Consumption actualConsumption) {
		this.actualConsumption = actualConsumption;
	}

	public Consumption getInfoConsumption() {
		return infoConsumption;
	}

	public void setInfoConsumption(Consumption infoConsumption) {
		this.infoConsumption = infoConsumption;
	}

	// para el panel de informacion
	public void selectInfoConsumption(Consumption infoConsumption) {
		if (infoConsumption != null) {
			this.infoConsumption = infoConsumption;
			waterSupplyHome.setInstance(infoConsumption.getWaterSupply());
			waterSupplyHome.createWaterMeter();
		}
	}

	public Integer getChangeIn() {
		return changeIn;
	}

	public void setChangeIn(Integer changeIn) {
		this.changeIn = changeIn;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public String getValidationSearchType() {
		return validationSearchType;
	}

	public void setValidationSearchType(String validationSearchType) {
		this.validationSearchType = validationSearchType;
	}

	public BigDecimal getValueValidation() {
		return valueValidation;
	}

	public void setValueValidation(BigDecimal valueValidation) {
		this.valueValidation = valueValidation;
	}

	public Boolean getIsPreEmited() {
		return isPreEmited;
	}

	public void setIsPreEmited(Boolean isPreEmited) {
		this.isPreEmited = isPreEmited;
	}

	public void validateConsumtionsReport() {
		if (validationSearchType.equals("over")) {
			findConsumptionOver();
		}
		if (validationSearchType.equals("down")) {
			findConsumptionLessThan();
		}
	}

	public void findConsumptionOver() {

		if (valueValidation != null) {
			Query query = this.getEntityManager().createNamedQuery("Consumption.findOver");
			query.setParameter("year", year);
			query.setParameter("month", month.getMonthInt());
			query.setParameter("routeId", this.getInstance().getId());
			query.setParameter("overAmount", this.valueValidation);
			consumptionsReport = query.getResultList();
			System.out.println("los errores sobre " + consumptionsReport.size());
		}
	}

	public void findConsumptionLessThan() {

		if (valueValidation != null) {
			Query query = this.getEntityManager().createNamedQuery("Consumption.findLessThan");
			query.setParameter("year", year);
			query.setParameter("month", month.getMonthInt());
			query.setParameter("routeId", this.getInstance().getId());
			query.setParameter("overAmount", this.valueValidation);
			consumptionsReport = query.getResultList();
			System.out.println("los errores bajo " + consumptionsReport.size());
		}
	}

	public List<Consumption> getConsumptionsReport() {
		return consumptionsReport;
	}

	public void setConsumptionsReport(List<Consumption> consumptionsReport) {
		this.consumptionsReport = consumptionsReport;
	}

	public int getActualReadingPosition() {
		return actualReadingPosition;
	}

	public void setActualReadingPosition(int actualReadingPosition) {
		this.actualReadingPosition = actualReadingPosition;
	}

	public Date getActualDate() {
		return actualDate;
	}

	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}

	// //////////////////////////
	// //////////////////////////
	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	/*private EmissionOrder createEmissionOrder() throws Exception {
		// TODO Auto-generated method stub
		if (preEmittedConsumptionState == null) loadConsumptionStates();

		RevenueService revenueService = ServiceLocator.getInstance().findResource("/gim/RevenueService/local");

		Date currentDate = new Date();
		Entry entry = null;
		entry = systemParameterService.materialize(Entry.class,"ENTRY_ID_WATER_SERVICE_TAX");

		FiscalPeriod f = userSession.getFiscalPeriod();
		Person p = userSession.getPerson();
		Route r = this.getInstance();

		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(f.getStartDate());
		emissionOrder.setDescription(entry.getName() + ":  Ruta " + r.getName()	+ ", " + "año/mes: " + year + "/" + month);
		emissionOrder.setEmisor(p);
		
		WaterServiceReference wsr;
		BigDecimal amount;
		String code = "";
		String serviceCode = "";
		String waterMeter = "";

		for (Consumption c : consumptions) {
			System.out.println(".:.:.:.:>>>>>>>>>>>>>>>>>>>>>>>entra a crear nuevo MB<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			amount = c.getAmount();
			serviceCode = c.getWaterSupply().getServiceNumber().toString();
			waterMeter = c.getWaterSupply().getWaterMeters().get(0).getSerial();
			code = serviceCode + " - " + waterMeter;

			c.setHasPreEmit(new Boolean(true));
			c.setConsumptionState(preEmittedConsumptionState);

			EntryValueItem entryValueItem = new EntryValueItem();
			entryValueItem.setDescription(emissionOrder.getDescription());
			entryValueItem.setMainValue(amount);
			entryValueItem.setServiceDate(currentDate);
			entryValueItem.setReference("");

			Resident person = c.getWaterSupply().getContract().getSubscriber();
			MunicipalBond mb = revenueService.createMunicipalBond(person, entry, f, entryValueItem, true, c);

			// start Adjunt
			wsr = new WaterServiceReference();
			wsr.setCode(code);
			wsr.setConsumptionAmount(amount);
			wsr.setConsumptionCurrentReading(c.getCurrentReading());
			wsr.setConsumptionPreviousReading(c.getPreviousReading());
			wsr.setWaterMeterNumber(waterMeter);
			wsr.setWaterMeterStatus(c.getWaterMeterStatus().getName());
			wsr.setWaterSupplyCategory(c.getWaterSupply().getWaterSupplyCategory().getName());
			wsr.setRouteName(r.getName());
			mb.setAdjunct(wsr);
			// end Adjunt

			mb.setDescription(entry.getDescription());
			mb.setCreationTime(new Date());
			mb.setGroupingCode(serviceCode);
			mb.setBase(amount); // cantidad de consumo
			// mb.setGroupingCode(pro.getCadastralCode());

			mb.setOriginator(p);
			mb.setTimePeriod(entry.getTimePeriod());
			mb.calculateValue();
			System.out.println("........... el valor es:  " + mb.getValue());
			emissionOrder.add(mb);
		}

		return emissionOrder;

	}*/

	//ConsumptionState consumptionState;

	/*public String startPreEmission() {
		if (waterService == null)
			waterService = ServiceLocator.getInstance().findResource(WATER_SERVICE_NAME);
		try {
			EmissionOrder eo = createEmissionOrder();
			waterService.saveEmissionOrder(eo, true,findWaterSuppliesIds());

			if (eo != null) {
				// loadRecordReadings();
				String message = Interpolator.instance().interpolate("La Ruta " + this.getInstance().getName()
								+ " ha sido pre-emitida", new Object[0]);
				facesMessages.addToControl(
								"residentChooser",
								org.jboss.seam.international.StatusMessage.Severity.INFO,
								message);
				isPreEmited = true;
				return "complete";
			}
			isPreEmited = false;
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}*/

	public Boolean getShoudPreEmit() {
		return shoudPreEmit;
	}

	public void setShoudPreEmit(Boolean shoudPreEmit) {
		this.shoudPreEmit = shoudPreEmit;
	}

	public Date getHourReport() {
		if(this.hourReport == null)this.hourReport = new Date();
		return hourReport;
	}

	public void setHourReport(Date hourReport) {
		this.hourReport = hourReport;
	}

	public Boolean getHasEditionReadingRole() {
		if (hasEditionReadingRole != null)
			return hasEditionReadingRole;
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String role = systemParameterService.findParameter(UserSession.ROLE_NAME_EDITION_READING);
		if (role == null) {
			hasEditionReadingRole = false;
		} else {
			hasEditionReadingRole = userSession.getUser().hasRole(role);
		}
		return hasEditionReadingRole;
	}

	public void setHasEditionReadingRole(Boolean hasEditionReadingRole) {
		this.hasEditionReadingRole = hasEditionReadingRole;
	}

	public Integer getServicesNumber() {
		return servicesNumber;
	}

	public void setServicesNumber(Integer servicesNumber) {
		this.servicesNumber = servicesNumber;
	}

	public List<WaterMeterDTO> getWaterMeterDTOs() {
		return waterMeterDTOs;
	}

	public void setWaterMeterDTOs(List<WaterMeterDTO> waterMeterDTOs) {
		this.waterMeterDTOs = waterMeterDTOs;
	}

	public Integer getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(Integer serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public Boolean getReadyForPrint() {
		return readyForPrint;
	}

	public void setReadyForPrint(Boolean readyForPrint) {
		this.readyForPrint = readyForPrint;
	}
	
	@Logger
	Log logger;
	@In(create = true)
	ConsumptionHome consumptionHome;
	
	@In(create = true)
	EntityManager entityManager;

	////////////////////////////
	
	// para la generacion de consumos no en memoris sino ya en la base de datos
	// quitar loadRecordReadings -> este es el nuevo metodo
	public void generateConsumptions() {
		int yearInt = year;
		serviceNumber = null;
		countServicesNumber();// este metodo esta bien... solo hace un conteo
		shoudPreEmit = isRouteEmited();// metodo corregido
		System.out.println("ha sido pre-emitida..........................................." + shoudPreEmit);
		//aqui cambia todo
		// consulto los id's de consumos ya generados para el mes seleccionado
		List<Long> consumptionAlreadyGenerated = findConsumptionAlreadyGenerated();
		logger.info(">>>>>>>>>>>>>>>>>>>>>>los ya generados son "
				+ consumptionAlreadyGenerated.size() + " xxx " + year
				+ " - " + month.getMonthInt());
		// regreso un mes para comprobar los anteriores en caso de enero
		// regreso a diciembre del anterior anio
		int monthInt = month.getMonthInt() - 1;
		
		if (monthInt == 0) {
			yearInt = yearInt - 1;
			monthInt = 12;
		}
		//busco los consumos faltantes
		List<Long> missingConsumption;
		if (consumptionAlreadyGenerated.size() > 0) {
			missingConsumption = findMissingConsumptions(consumptionAlreadyGenerated, yearInt, monthInt);
			logger.info(">>>>>>>>> entra a cerooooooo");
		}else{
			missingConsumption = findAllMissingConsumption(yearInt, monthInt);
			logger.info(">>>>>>>>> entra a elseeeeeee");
		}
		//genero los consumos faltantes para la lista de missingConsumptions
		if (missingConsumption.size() > 0) {
			logger.info(">>>>>>>>>< va a generar "+missingConsumption.size()+ " consumos faltantes");
			generateNewRecords(findByIds(missingConsumption,yearInt, monthInt));
		}else{
			logger.info(">>>>>>>>>< no tiene nada que generarsssssssssss");
		}
	}
	
	/**
	 * genera y guarda los nuevos cosumos para el mes seleccionado 
	 * @param itemsOld
	 */
	public void generateNewRecords(List<Consumption> itemsOld) {
		Boolean registerEnabled = findWaterRegisterEnabled();
		Consumption conNew;
		consumptions=new ArrayList<Consumption>();
		
		int i=1;
		
//		ConsumptionControl cc = null;
		
		for (Consumption conOld : itemsOld) {
			
//			cc = new ConsumptionControl();
			
			conNew = new Consumption();
			conNew.setConsumptionState(conOld.getConsumptionState());
			conNew.setMonthType(month);
			conNew.setMonth(month.getMonthInt());
			conNew.setPreviousReading(conOld.getCurrentReading());
			conNew.setWaterMeterStatus(conOld.getWaterMeterStatus());				
			conNew.setWaterSupply(conOld.getWaterSupply());
			conNew.setYear(year);
			conNew.setConsumptionState(conOld.getConsumptionState());
			
			conNew.setApplyElderlyExemption(conOld.getWaterSupply().getApplyElderlyExemption());
			conNew.setApplySpecialExemption(conOld.getWaterSupply().getApplySpecialExemption());
//			conNew.setConsumptionControl(cc);
//			cc.setConsumption(conNew);
			
			//conNew.setPreviousAverage(findAverage(conOld.getWaterSupply()));
			if(registerEnabled != null) 
				conNew.setDisabled(registerEnabled);
			
			entityManager.persist(conNew);
			
			if ((i % 100) == 0) {
				entityManager.flush();
				entityManager.clear();
			}
			i++;
		}
		entityManager.flush();
		entityManager.clear();
	}
	
	@SuppressWarnings("unchecked")
	public List<Consumption> findByIds(List<Long> wsIds, int _year, int _month) {
		Query q = this.getEntityManager().createNamedQuery("Consumption.findConsumptionsByWaterSupply");
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setParameter("routeId", this.getInstance().getId());
		q.setParameter("isCanceled", Boolean.FALSE);
		q.setParameter("wsIds", wsIds);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findAllMissingConsumption(int _year, int _month) {
		Query q = this.getEntityManager().createNamedQuery(
				"Consumption.findAllMissingConsumptions");
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setParameter("routeId", this.getInstance().getId());
		return q.getResultList();
	}
	
	public List<Long> findMissingConsumptions(List<Long> consumptionAlreadyGenerated, int _year, int _month){
		Query query = this.getEntityManager().createNamedQuery("Consumption.findMissingConsumptions");
		query.setParameter("year", _year);
		query.setParameter("month", _month);
		query.setParameter("routeId", this.getInstance().getId());
		query.setParameter("consumptionAlreadyGenerated", consumptionAlreadyGenerated);
		return query.getResultList();
	}
	
	public List<Long> findConsumptionAlreadyGenerated(){
		Query query = this.getEntityManager().createNamedQuery("Consumption.findAlreadyGenerated");
		query.setParameter("year", year);
		query.setParameter("month", month.getMonthInt());
		query.setParameter("routeId", this.getInstance().getId());
		return query.getResultList();
	}
	
	public void loadConsumptionsToEmit(){
		countServicesNumber();
		waterMeterDTOs = loadWaterMeterByWaterSupplies();
		
		Query query=this.getEntityManager().createNamedQuery("Consumption.findByYearMonth");
		query.setParameter("year", year);
		query.setParameter("month", month.getMonthInt());
		query.setParameter("routeId", this.getInstance().getId());
		query.setParameter("isCanceled", Boolean.FALSE);
		//hacer un check de todos los consumos antes de presentarlos
		this.consumptions = checkConsumptionList(query.getResultList());
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> entra a load 222.0 jjjj");
		if (consumptions.size() > 0) newLoad = true;
	}
		
	public List<Consumption> checkConsumptionList(List<Consumption> items) {
		Boolean registerEnabled = findWaterRegisterEnabled();
		List<Consumption> newConsumptions = new ArrayList<Consumption>();
				
		for (Consumption cons : items) {
			checkCorrectReading(cons);
			if (registerEnabled != null)
				cons.setDisabled(registerEnabled);
			newConsumptions.add(cons);

		}		
		return newConsumptions;
	}
	
	private String observation;

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	

}
