package org.gob.gim.waterservice.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.ConsumptionRange;
import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.MonthType;
import ec.gob.gim.waterservice.model.Route;
import ec.gob.gim.waterservice.model.WaterConsumptionIndicator;
import ec.gob.gim.waterservice.model.WaterMeter;
import ec.gob.gim.waterservice.model.WaterMeterStatus;
import ec.gob.gim.waterservice.model.WaterSupply;
import ec.gob.gim.waterservice.model.WaterSupplyCategory;

@Name("routeReport")
public class RouteReport extends EntityHome<Route> {

	@In(create = true)
	RoutePeriodHome routePeriodHome;
	@In(create = true)
	WaterSupplyHome waterSupplyHome;
	@In
	private FacesMessages facesMessages;

	// registrar Lecturas
	private Integer year = -1;
	private List<Consumption> consumptions;
	MonthType month;

	List<ConsumptionState> consumptionStates;

	// para las pre-emisiones
	public static String WATER_SERVICE_NAME = "/gim/WaterService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	@In
	UserSession userSession;
	//
	private Integer startYear;
	private SystemParameterService systemParameterService;

	// para panel luego de preemitido
	List<Consumption> consumptionsReport;

	private Integer monthsNumber = 3;
	List<MunicipalBond> municipalBondsNotPayed;

	// para reporte de indices por rango de rutas
	private int routeStart;
	private int routeEnd;
	private List<Route> routes;
	private List<WaterSupplyCategory> categories;
	private BigDecimal totalOwedByRoute;
	private int rowIndex = 0; 
	
	//para consultas por  numero de servicio
	private String serviceNumber;

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
		getInstance();
		loadConsumptionStates();
		isFirstTime = false;
		if (meterStatus == null) {
			loadWaterMeterStatus();
		}

		if (categories == null) {
			loadWaterSupplyCategory();
		}
	}
	
	public void initDates(){
		if(!isFirstTime) return;
		isFirstTime = false;
		if (startDate == null) {
			FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();
			startDate = fiscalPeriod.getStartDate();
		}
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
		newLoad = false;
	}

	public List<Integer> getYears() {
		List<Integer> years = new ArrayList<Integer>();
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		startYear = systemParameterService
				.findParameter("MINIMUM_EMISSION_YEAR");

		if (startYear != null) {
			int iStart = startYear;
			int iActual = Integer.parseInt(new SimpleDateFormat("yyyy")
					.format(new Date()));
			int amount = (iActual - iStart) + 1;
			for (int i = 0; i < amount; i++) {
				years.add(iActual);
				iActual--;
			}
		}
		return years;
	}

	// ///////////////////////////////////
	/*public void loadRecordReadingsRecalculate() {
		System.out.println("entra a cargar::::!!!!!!!!!!!!!!!!! recalculate");
		// se obtiene el mes actual... los consumos ya debe estar generados
		int monthInt = month.getMonthInt();
		int yearInt = year;

		// findSelectMonthRecordReadings();
		Query query = this.getEntityManager().createNamedQuery("Consumption.findByYearMonthNotMunicipal");
		query.setParameter("year", yearInt);
		query.setParameter("month", monthInt);
		query.setParameter("routeId", this.getInstance().getId());
		query.setParameter("isCanceled", new Boolean(false));

		consumptions = loadNewConsumtionList(query.getResultList());

		if (consumptions.size() > 0)
			newLoad = true;
	}*/
	
	private List<WaterSupply> pendingSupplies; 
	
	public void findPendingByRoute(){
		String sQuery="SELECT waterSupply FROM WaterSupply waterSupply "
				+ "LEFT JOIN waterSupply.serviceOwner "
				+ "LEFT JOIN waterSupply.route route "
				+ "LEFT JOIN waterSupply.waterSupplyCategory waterSupplyCategory "
				+ "LEFT JOIN waterSupply.waterMeters wm "
				+ "WHERE route.id = :routeId and waterSupply.isCanceled = :isCanceled "
				+ "and waterSupplyCategory.id <> 8 "
				+ "and wm.isActive = TRUE "
				+ "order by waterSupply.routeOrder";
		Query query = this.getEntityManager().createQuery(sQuery);
		query.setParameter("routeId", this.getInstance().getId());
		query.setParameter("isCanceled", new Boolean(false));
		pendingSupplies=listPendingBonds(query.getResultList());
		if (pendingSupplies.size() > 0)
			newLoad = true;
	}
	
	public List<WaterSupply> listPendingBonds(List<WaterSupply> waterServices) {
		List<WaterSupply> newConsumptions = new ArrayList<WaterSupply>();		
		startConsumptionDates();
		loadPendingStatus();
		if(getGroupingCodes(waterServices).size()>0){
			List<Object[]> list = countMunicipalBond(getGroupingCodes(waterServices),getResidentIds(waterServices));
			totalOwedByRoute=BigDecimal.ZERO;
			for(Object[] row : list){
				setTotalOwedByRoute(totalOwedByRoute.add((BigDecimal)row[3]));
			}
			//System.out.println("el totallllllllllllllllllllllll "+totalOwedByRoute);
			int sequence=1;
			for (WaterSupply ws : waterServices) {
				ws.setNotPayMonths(findMunicipalBondsCounted(list, ws.getServiceNumber().toString(),null));
				ws.setTotalDebt(findMunicipalBondsTotalDebt(list, ws.getServiceNumber().toString()));
				if (ws.getNotPayMonths() >= monthsNumber) {
					ws.setSequence(sequence);
					newConsumptions.add(ws);
					sequence++;
				}
			}	
		}else{
			
		}
		return newConsumptions;
	}
	
	public List<String> getGroupingCodes(List<WaterSupply> waterServices){
		List<String> aux = new ArrayList<String>();
		for(WaterSupply ws: waterServices){
			aux.add(ws.getServiceNumber().toString());
		}
		return aux;
	}
	
	public List<Long> getResidentIds(List<WaterSupply> waterServices){
		List<Long> aux = new ArrayList<Long>();
		for(WaterSupply ws: waterServices){
			aux.add(ws.getServiceOwner().getId());
		}
		return aux;
	}
	
	
	/**
	 * Create a new Consumption's list, if a Consumption exist is added to the
	 * new list
	 * 
	 * @param oldList
	 * @return
	 */
	/*public List<Consumption> loadNewConsumtionList(List<Consumption> oldList) {
		List<Consumption> newConsumptions = new ArrayList<Consumption>();		
		startConsumptionDates();
		loadPendingStatus();
		if(getGroupingCodes(oldList).size()>0){
			List<Object[]> list = countMunicipalBond(getGroupingCodes(oldList),getResidentIds(oldList));
			totalOwedByRoute=BigDecimal.ZERO;
			for(Object[] row : list){
				//Long counted = (Long)row[0];
				//String groupingCode = (String)row[1];
				//Long resident=(Long)row[2];
				setTotalOwedByRoute(totalOwedByRoute.add((BigDecimal)row[3]));
				//System.out.println("los datos son:............. "+counted+" "+groupingCode+" "+ " "+resident+"    "+row[3]);
			}
			System.out.println("el totallllllllllllllllllllllll "+totalOwedByRoute);
			
			for (Consumption conOld : oldList) {
				conOld.setNotPayMonths(findMunicipalBondsCounted(list, conOld.getWaterSupply().getServiceNumber().toString(),null));
				conOld.setTotalDebt(findMunicipalBondsTotalDebt(list, conOld.getWaterSupply().getServiceNumber().toString()));
				if (conOld.getNotPayMonths() >= monthsNumber) {
					newConsumptions.add(conOld);	
				}
			}	
		}else{
			
		}
		return newConsumptions;
	}*/
	
	/*public List<String> getGroupingCodes(List<Consumption> list){
		List<String> aux = new ArrayList<String>();
		for(Consumption c: list){
			aux.add(c.getWaterSupply().getServiceNumber().toString());
		}
		return aux;
	}
	
	public List<Long> getResidentIds(List<Consumption> list){
		List<Long> aux = new ArrayList<Long>();
		for(Consumption c: list){
			aux.add(c.getWaterSupply().getServiceOwner().getId());
		}
		return aux;
	}*/
	
	private Long pendingStatus;
	
	
	public void loadPendingStatus() {
		if(pendingStatus != null) return;
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		pendingStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");	
	}
	
	public void startConsumptionDates(){
		int actualMonth;
		int year;
		
		actualMonth = month.getMonthInt() - 1;
		year = this.year;

		Calendar startDateLocal = Calendar.getInstance();
		startDateLocal.set(year, actualMonth, 1);

		int endDayOfMonth = startDateLocal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		startDateLocal.set(year, actualMonth, endDayOfMonth);

		Calendar endDateLocal = Calendar.getInstance();
		endDateLocal.set(year, actualMonth, 1);
		endDateLocal.add(Calendar.MONTH, -monthsNumber);
		
		this.startDate = startDateLocal.getTime();
		this.endDate = endDateLocal.getTime();
	}


	/**
	 * encontrar el municipalbond para un determinado consumo emitido
	 * 
	 * @param c
	 * @return
	 */
	private List<Object[]> countMunicipalBond(List<String> groupingCodes, List<Long> rsdntIds) {
		
		int monthInt = month.getMonthInt() - 1;
		
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.MONTH, monthInt);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		
		//System.out.println("la fehca es..................... "+c.getTime());
		
		String sentence = "SELECT mb.groupingCode, COUNT(mb), res.id, SUM(mb.value) FROM MunicipalBond mb "
				+ "LEFT JOIN mb.municipalBondStatus mbs "
				+ "LEFT JOIN mb.resident res "
				+ "WHERE mb.groupingCode in (:codes) and "
				+ "res.id in (:rsdntIds) and "
				+ "mb.expirationDate < :currentDate and "
				+ "mbs.id = :municipalBondStatusId GROUP BY mb.groupingCode,res.id "
				+ "HAVING COUNT(mb)>=3 "
				+ "order by mb.groupingCode";

		Query q = this.getEntityManager().createQuery(sentence);		
		q.setParameter("codes", groupingCodes);
		q.setParameter("currentDate", c.getTime());
		q.setParameter("municipalBondStatusId", pendingStatus);
		q.setParameter("rsdntIds", rsdntIds);
		
		return q.getResultList();
	}
	
	private Integer findMunicipalBondsCounted(List<Object[]> results, String serviceNumber,Long resident_id){
		for(Object[] row : results){
			String groupingCode = (String)row[0];
			Long counted = (Long)row[1];
			if(groupingCode.equals(serviceNumber)){
				return counted.intValue();
			}
		}
		return 0;
	}
	
	private BigDecimal findMunicipalBondsTotalDebt(List<Object[]> results, String serviceNumber){
		for(Object[] row : results){
			//Long counted = (Long)row[0];
			String groupingCode = (String)row[0];
			BigDecimal total = (BigDecimal)row[3];
			if(groupingCode.equals(serviceNumber)){
				return total;
			}
		}
		return new BigDecimal(0);
	}

	public void findMunicipalBondList(WaterSupply ws) {
		loadPendingStatus();
		String sentence = "SELECT mb FROM MunicipalBond mb "
				+ "LEFT JOIN mb.municipalBondStatus mbs "
				+ "WHERE "
				+ "mb.expirationDate < :currentDate and "
				+ "mb.groupingCode = :code and "
				+ "mbs.id = :municipalBondStatusId order by mb.serviceDate";

		String code = ws.getServiceNumber().toString();

		int monthInt = month.getMonthInt() - 1;
		
		Calendar ca=Calendar.getInstance();
		ca.clear();
		ca.set(Calendar.MONTH, monthInt);
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.DATE, ca.getActualMaximum(Calendar.DATE));
		
		Query q = this.getEntityManager().createQuery(sentence);		
		q.setParameter("currentDate", ca.getTime());
		q.setParameter("code", code);
		q.setParameter("municipalBondStatusId", pendingStatus);

		municipalBondsNotPayed = q.getResultList();
	}
	
		
	@SuppressWarnings("unchecked")
	public void findMunicipalBondByServiceNumber() {
		if (serviceNumber != null) {
			String sentence = "SELECT mb FROM MunicipalBond mb "
					+ "LEFT JOIN mb.municipalBondStatus mbs "
					+ "LEFT JOIN mb.resident res "
					+ "LEFT JOIN mb.adjunct "
					+ "WHERE "
					+ "mb.emisionDate between :startDate and :currentDate and "
					+ "mb.groupingCode = :code "
					+ "order by mb.serviceDate";
				
			Query q = this.getEntityManager().createQuery(sentence);		
			q.setParameter("code", serviceNumber);
			q.setParameter("startDate", startDate);
			q.setParameter("currentDate", new Date());

			municipalBondsNotPayed = q.getResultList();
		}else{
			String message = "Ingrese el número de servicio";
			InvalidValue iv = new InvalidValue(message, MunicipalBond.class, "code", null, this);
			facesMessages.addToControl("legalEntityCode", iv);	
		}
	}

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

	List<Consumption> currentsConsumptions;

	public void findSelectMonthRecordReadings() {

		Query query = this.getEntityManager().createNamedQuery("Consumption.findByYearMonth");
		query.setParameter("year", year);
		query.setParameter("month", month.getMonthInt());
		query.setParameter("routeId", this.getInstance().getId());
		currentsConsumptions = query.getResultList();
	}

	public Consumption findIfExist(Consumption conOld) {
		for (Consumption cs : currentsConsumptions) {
			if (cs.getWaterSupply().getId() == conOld.getWaterSupply().getId()) {
				return cs;
			}
		}
		return null;
	}

	public WaterMeter findWaterMeter(WaterSupply ws) {
		Query q = this.getEntityManager().createNamedQuery("WaterMeter.findByWaterSupplyActualMeter");
		q.setParameter("active", new Boolean(true));
		q.setParameter("wsId", ws.getId());
		if (q.getResultList().size() > 0) {
			return (WaterMeter) q.getResultList().get(0);
		} else {
			return null;
		}
	}

	public Route getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
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

	public List<Consumption> getConsumptionsReport() {
		return consumptionsReport;
	}

	public void setConsumptionsReport(List<Consumption> consumptionsReport) {
		this.consumptionsReport = consumptionsReport;
	}

	public Integer getMonthsNumber() {
		return monthsNumber;
	}

	public void setMonthsNumber(Integer monthsNumber) {
		this.monthsNumber = monthsNumber;
	}

	public List<MunicipalBond> getMunicipalBondsNotPayed() {
		return municipalBondsNotPayed;
	}

	public void setMunicipalBondsNotPayed(
			List<MunicipalBond> municipalBondsNotPayed) {
		this.municipalBondsNotPayed = municipalBondsNotPayed;
	}

	// para reporte de indicadores

	public int getRouteStart() {
		return routeStart;
	}

	public void setRouteStart(int routeStart) {
		this.routeStart = routeStart;
	}

	public int getRouteEnd() {
		return routeEnd;
	}

	public void setRouteEnd(int routeEnd) {
		this.routeEnd = routeEnd;
	}

	private List<Long> routeIds;

	public void findRoutes() {
		String sentence = "Select r from Route r where "
				+ "r.number between :rStart and :rEnd order by r.number";
		Query q = this.getEntityManager().createQuery(sentence);
		q.setParameter("rStart", routeStart);
		q.setParameter("rEnd", routeEnd);
		routes = q.getResultList();

		routeIds = new ArrayList<Long>();
		for (Route r : routes) {
			routeIds.add(r.getId());
		}
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	
	private WaterSupplyCategory residentialWaterSupplyCategory;	
	private WaterSupplyCategory commercialWaterSupplyCategory;
	private WaterSupplyCategory officialWaterSupplyCategory;
	private WaterSupplyCategory halfOfficialWaterSupplyCategory;
	private WaterSupplyCategory industrialWaterSupplyCategory;
	private WaterSupplyCategory oldPeopleWaterSupplyCategory; 
	private WaterSupplyCategory zeroCategoryWaterSupplyCategory;

	public void loadWaterSupplyCategory(){
		this.categories = new ArrayList<WaterSupplyCategory>();
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		 		 
		 residentialWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_RESIDENTIAL");	
		 commercialWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_COMMERCIAL");
		 officialWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_OFFICIAL");
		 halfOfficialWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_HALF_OFFICIAL");
		 industrialWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_INDUSTRIAL");
		 //oldPeopleWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_OLD_PEOPLE");		 
		 zeroCategoryWaterSupplyCategory = systemParameterService.materialize(WaterSupplyCategory.class, "WATER_SUPPLY_CATEGORY_ID_ZERO_CATEGORY");
						
		 this.categories.add(residentialWaterSupplyCategory);
		 this.categories.add(commercialWaterSupplyCategory);		 
		 this.categories.add(officialWaterSupplyCategory);
		 this.categories.add(halfOfficialWaterSupplyCategory);
		 this.categories.add(industrialWaterSupplyCategory);		 
		 //this.categories.add(oldPeopleWaterSupplyCategory);
		 this.categories.add(zeroCategoryWaterSupplyCategory);
	}

	

	private List<WaterMeterStatus> meterStatus;
	private List<WaterConsumptionIndicator> consumptionIndicators;
	
	
	private WaterMeterStatus workingMeterStatus;	
	private WaterMeterStatus withoutMeterStatus;
	private WaterMeterStatus damagedMeterStatus;

	public void loadWaterMeterStatus() {
		
		this.meterStatus = new ArrayList<WaterMeterStatus>();
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		 		 
		 workingMeterStatus = systemParameterService.materialize(WaterMeterStatus.class, "WATER_METER_STATUS_ID_WORKING");
		 withoutMeterStatus = systemParameterService.materialize(WaterMeterStatus.class, "WATER_METER_STATUS_ID_WITHOUT");
		 damagedMeterStatus = systemParameterService.materialize(WaterMeterStatus.class, "WATER_METER_STATUS_ID_DAMAGED");
		 
						
		 this.meterStatus.add(workingMeterStatus);
		 this.meterStatus.add(withoutMeterStatus);		 
		 this.meterStatus.add(damagedMeterStatus);
	}
	
	public void findMunicipalBond(){
		Date today = new Date();
		
		Calendar calendarStart = Calendar.getInstance();
		calendarStart.setTime(today);
		calendarStart.set(Calendar.DAY_OF_MONTH, 1);
		calendarStart.set(Calendar.MONTH, 11);
		calendarStart.set(Calendar.YEAR, 2012);
		
		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTime(today);
		calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
		calendarEnd.set(Calendar.MONTH, 11);
		calendarEnd.set(Calendar.YEAR, 2012);
		
		/*String sentencia = "select mb from MunicipalBond mb "
				+ "left join mb.adjunct adjunct "
				+ "where mb.entry.id = :entryId and "
				+ "mb.serviceDate between :starDate and :endDate "
				+ "and adjunct.consumptionamount between :startRange and :endRange "
				+ "and adjunct.waterMeterStatus = :waterMeterStatus and "
				+ "and adjunct.waterSupplyCategory=:waterSupplyCategory ";
						/*"and "
				+ "EXIST (select wsr WaterServiceReference wsr where wsr.consumptionamount between :startRange and :endRange "
				+ "and wsr.waterMeterStatus = :waterMeterStatus and "
				+ "and wsr.waterSupplyCategory=:waterSupplyCategory and wsr.id = adjunct.id )";
				/*+ "and wsr.consumptionamount between :startRange and :endRange "
				+ "and wsr.waterMeterStatus = :waterMeterStatus and "
				+ "and wsr.waterSupplyCategory=:waterSupplyCategory ";*/
		
		String sentencia="select SUM(municipalBond.paidTotal) from municipalBond " +
				"inner join adjunct on municipalBond.adjunct_id =adjunct.id " +
				"inner join WaterServiceReference on WaterServiceReference.id =adjunct.id " +
				"where entry_id= :entryId " +
				"and serviceDate between :starDate and :endDate " +
				"and WaterServiceReference.consumptionamount between :startRange and :endRange " +
				"and WaterServiceReference.waterMeterStatus = :waterMeterStatus " +
				"and WaterServiceReference.waterSupplyCategory=:waterSupplyCategory " +
				"and WaterServiceReference.route_id IN (:routeIds) ";
		Query q=this.getEntityManager().createNativeQuery(sentencia);
		q.setParameter("entryId", findWaterSupplyEntryId());
		q.setParameter("starDate", calendarStart.getTime());
		q.setParameter("endDate", calendarEnd.getTime());
		q.setParameter("startRange", new BigDecimal(0));
		q.setParameter("endRange", new BigDecimal(10));
		
		q.setParameter("waterMeterStatus", "Funcionando");
		q.setParameter("waterSupplyCategory", "RESIDENCIAL");
		q.setParameter("routeIds", routeIds);
		
		/*System.out.println("fechas inicio:::::::::::::::::::::        "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(calendarStart.getTime()));
		System.out.println("fechas fin  ::::::::::::::::::::::        "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(calendarEnd.getTime()));
		System.out.println("el valor es::::::::::::::::::::::: tamaño "+q.getResultList().size());
		System.out.println("el valor es::::::::::::::::::::::: valor  "+q.getSingleResult());
		System.out.println("el valor es::::::::::::::::::::::: rutas  "+routeIds.size());*/
		
	}
	
	private Date startDate;
	private Date endDate;
	
	public void startDates(){
		int monthInt = month.getMonthInt();
		int yearInt = this.year;
		
		Calendar cstartDate = Calendar.getInstance();
		cstartDate.set(yearInt, (monthInt-1), 01);
		
		int endDayOfMonth = cstartDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		Calendar cendDate = Calendar.getInstance();
		cendDate.set(year, (monthInt - 1), endDayOfMonth);
		
		startDate = cstartDate.getTime();
		endDate = cendDate.getTime();
		
		//System.out.println("start "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(startDate.getTime()));
		//System.out.println("end   "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(endDate.getTime()));
	}
	
	Query qwe;
	
	/*public BigDecimal totalBy(WaterConsumptionIndicator consumptionIndicator, WaterSupplyCategory wsc, ConsumptionRange cr, Long itemEntryId){
		if (startDate == null) {
			startDates();
		}
		String sentencia = null;
		if(itemEntryId.equals(new Long(531))){
			sentencia = "select SUM(item.total) from gimprod.item as item "
				+ "inner join gimprod.municipalBond as mb on item.municipalbond_id = mb.id "
				+ "inner join gimprod.WaterServiceReference as wsr on mb.adjunct_id = wsr.id "
				+ "where "
				+ "item.entry_id in ( :itemEntryID, 76 )"
				+ "and mb.municipalbondstatus_id not in (1,5,8,9,10) "
				+ "and mb.entry_id in ( 531,76) "
				+ "and mb.serviceDate between :startDate and :endDate "
				+ "and wsr.consumptionamount >= :startRange and wsr.consumptionamount <= :endRange "
				+ "and wsr.waterSupplyCategory = :wsc "
					+ "and wsr.route_id IN (:routeIds)";
			
		}else{
			sentencia = "select SUM(item.total) from item as item "
				+ "inner join municipalBond as mb on item.municipalbond_id = mb.id "
				//+ "inner join adjunct as adj on mb.adjunct_id = adj.id "
				//+ "inner join WaterServiceReference as wsr on wsr.id = adj.id "
				+ "inner join WaterServiceReference as wsr on mb.adjunct_id = wsr.id "
				+ "where "
				+ "item.entry_id= :itemEntryID "
				+ "and mb.municipalbondstatus_id not in (1,5,8,9,10) "
				+ "and mb.entry_id= 531 "
				+ "and mb.serviceDate between :startDate and :endDate "
				//+ "and wsr.consumptionamount between :startRange and :endRange "
				+ "and wsr.consumptionamount >= :startRange and wsr.consumptionamount <= :endRange "
				//+ "and wsr.waterMeterStatus = :wms "
				+ "and wsr.waterSupplyCategory = :wsc "
					+ "and wsr.route_id IN (:routeIds)";	
		}
				
		qwe=this.entityManager.createNativeQuery(sentencia);
		qwe.setParameter("itemEntryID", itemEntryId);
		qwe.setParameter("startDate", startDate);
		qwe.setParameter("endDate", endDate);
		qwe.setParameter("startRange", new BigDecimal(cr.getFrom()));
		qwe.setParameter("endRange", new BigDecimal(cr.getTo()));
		//qwe.setParameter("wms", wms.getName());
		qwe.setParameter("wsc", wsc.getName());
		qwe.setParameter("routeIds", routeIds);
				
		if (qwe.getResultList().size() > 0) {
			if(qwe.getSingleResult()!=null){
				return new BigDecimal(qwe.getSingleResult().toString());	
			}else{
				return new BigDecimal(0);
			}
		}else{
			return new BigDecimal(0);
		}
	}*/
	
	/*public void findValues(WaterConsumptionIndicator consumptionIndicator, WaterSupplyCategory wsc, ConsumptionRange cr){
		loadEntries();		
		//itemEntryIds.add(new Long(76));		
		for (Long entryId : entryIds) {
			if(entryId.equals(waterSupplyEntryId))
				consumptionIndicator.setAgua(totalBy(consumptionIndicator, wsc, cr,  entryId));
			if(entryId.equals(sewerageEntryId))
				consumptionIndicator.setAlcantarillado(totalBy(consumptionIndicator, wsc, cr, entryId));
			if(entryId.equals(trashEntryId))
				consumptionIndicator.setBasura(totalBy(consumptionIndicator, wsc, cr, entryId));
			if(entryId.equals(masterPlanEntryId))
				consumptionIndicator.setP_maestro(totalBy(consumptionIndicator, wsc, cr, entryId));
			if(entryId.equals(securityEntryId))
				consumptionIndicator.setS_ciudadana(totalBy(consumptionIndicator, wsc, cr, entryId));
			if(entryId.equals(microWaterShedsEntryId))
				consumptionIndicator.setM_cuencas(totalBy(consumptionIndicator, wsc, cr, entryId));
		}
	}*/
	
	
	/*public void findValuesFor(WaterConsumptionIndicator consumptionIndicator, WaterSupplyCategory wsc, ConsumptionRange cr, Long entryId){
		loadEntries();
		if (entryId.equals(waterSupplyEntryId))
			consumptionIndicator.setAgua(totalBy(consumptionIndicator, wsc, cr, entryId));
		if(entryId.equals(sewerageEntryId))
			consumptionIndicator.setAlcantarillado(totalBy(consumptionIndicator, wsc, cr, entryId));
		if(entryId.equals(trashEntryId))
			consumptionIndicator.setBasura(totalBy(consumptionIndicator, wsc, cr,entryId));
		if(entryId.equals(masterPlanEntryId))
			consumptionIndicator.setP_maestro(totalBy(consumptionIndicator, wsc, cr,entryId));
		if(entryId.equals(securityEntryId))
			consumptionIndicator.setS_ciudadana(totalBy(consumptionIndicator, wsc, cr, entryId));
		if(entryId.equals(microWaterShedsEntryId))
			consumptionIndicator.setM_cuencas(totalBy(consumptionIndicator, wsc, cr, entryId));
	}*/
	
	@In
	EntityManager entityManager;
	Query qq;
	
	
	/*public BigDecimal totalFootersBy(WaterSupplyCategory wsc, Long itemEntryId){
		System.out.println("....................................name.. "+wsc.getName()+"--------------------el id "+itemEntryId);
		if (startDate == null) {
			startDates();
		}
		String sentencia=null;
		if(itemEntryId.equals(new Long(531))){
			sentencia= "select SUM(item.total) from gimprod.item as item "
				+ "inner join gimprod.municipalBond as mb on item.municipalbond_id = mb.id "
				+ "inner join gimprod.WaterServiceReference as wsr on mb.adjunct_id = wsr.id "
				+ "where "
				+ "item.entry_id in (:itemEntryID,76 ) "
				+ "and mb.entry_id in ( 531,76 ) "
				+ "and mb.municipalbondstatus_id not in (1,5,8,9,10) "
				+ "and mb.serviceDate between :startDate and :endDate "
				+ "and wsr.waterSupplyCategory = :wsc "
				+ "and wsr.route_id IN (:routeIds)";
		}else{
			sentencia= "select SUM(item.total) from gimprod.item as item "
				+ "inner join gimprod.municipalBond as mb on item.municipalbond_id = mb.id "
				//+ "inner join adjunct as adj on mb.adjunct_id = adj.id "
				//+ "inner join WaterServiceReference as wsr on wsr.id = adj.id "
				+ "inner join gimprod.WaterServiceReference as wsr on mb.adjunct_id = wsr.id "
				+ "where "
				+ "item.entry_id= :itemEntryID "
				+ "and mb.entry_id= 76 "
				+ "and mb.municipalbondstatus_id not in (1,5,8,9,10) "
				+ "and mb.serviceDate between :startDate and :endDate "
				+ "and wsr.waterSupplyCategory = :wsc "
				+ "and wsr.route_id IN (:routeIds)";	
		}
		//Query q=this.getEntityManager().createNativeQuery(sentencia);
		qq=this.entityManager.createNativeQuery(sentencia);
		qq.setParameter("itemEntryID", itemEntryId);
		qq.setParameter("startDate", startDate);
		qq.setParameter("endDate", endDate);
		qq.setParameter("wsc", wsc.getName());
		qq.setParameter("routeIds", routeIds);
				
		if (qq.getResultList().size() > 0) {
			if(qq.getSingleResult()!=null){
				System.out.println("el valor encontrado para "+wsc.getName()+"   -------------------- "+qq.getSingleResult().toString());
				return new BigDecimal(qq.getSingleResult().toString());	
			}else{
				return new BigDecimal(0);
			}
		}else{
			return new BigDecimal(0);
		}
	}*/
	
	private Long waterSupplyEntryId;
	
	private Long findWaterSupplyEntryId(){
		if(waterSupplyEntryId != null) return waterSupplyEntryId;
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		waterSupplyEntryId = systemParameterService.findParameter("ENTRY_ID_WATER_SERVICE_TAX"); 
		return waterSupplyEntryId;
	}
	
	private List<Long> entryIds;
	
	private Long sewerageEntryId; // alcantarillado
	private Long masterPlanEntryId; // plan maestro
	private Long securityEntryId; // seguridad
	private Long microWaterShedsEntryId; // microcuencas
	private Long basicCostEntryId; // costo basico
	private Long trashEntryId; // basura
	
	private void loadEntriesIds() {
		if(sewerageEntryId != null) return;
		if(waterSupplyEntryId == null) findWaterSupplyEntryId();
		if(systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		sewerageEntryId = systemParameterService.findParameter("ENTRY_ID_SEWERAGE");
		masterPlanEntryId = systemParameterService.findParameter("ENTRY_ID_MASTER_PLAN");
		securityEntryId = systemParameterService.findParameter("ENTRY_ID_SECURITY");
		microWaterShedsEntryId = systemParameterService.findParameter("ENTRY_ID_MICRO_WATERSHEDS");
		basicCostEntryId = systemParameterService.findParameter("ENTRY_ID_BASIC_COST");
		trashEntryId = systemParameterService.findParameter("ENTRY_ID_TRASH");		
	}
		
	public void loadEntries(){
		if(entryIds == null || entryIds.size() == 0){
			loadEntriesIds();
			entryIds=new ArrayList<Long>();
			entryIds.add(waterSupplyEntryId);//agua
			entryIds.add(sewerageEntryId);//alcantarillado
			entryIds.add(masterPlanEntryId);//plan maestro
			entryIds.add(securityEntryId);//seguridad
			entryIds.add(microWaterShedsEntryId);//micro cuencas		
			entryIds.add(trashEntryId);//basura
		}
		
	}
	
	/*public void findFooterValues(WaterSupplyCategory wsc){		
		loadEntries();		
		//itemEntryIds.add(new Long(76));		
		if(wsc.getId()!=8){
			for (Long entryId : entryIds) {
				if (entryId.equals(waterSupplyEntryId)){
					wsc.setAgua_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getAgua_total()));
				}
				if (entryId.equals(sewerageEntryId)){
					wsc.setAlcantarillado_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getAlcantarillado_total()));
				}
				if (entryId.equals(trashEntryId)){
					wsc.setBasura_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getBasura_total()));
				}
				if (entryId.equals(masterPlanEntryId)){
					wsc.setP_maestro_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getP_maestro_total()));
				}
				if (entryId.equals(securityEntryId)){
					wsc.setS_ciudadana_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getS_ciudadana_total()));
				}
				if (entryId.equals(microWaterShedsEntryId)){
					wsc.setM_cuencas_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getM_cuencas_total()));
				}
			}	
		}		
	}*/
	
	public void findFooterValuesBestWay(){		
		//loadEntries();
		//WaterSupplyCategory wsc
		if (startDate == null) {
			startDates();
		}
		/*String sentencia = "select SUM(item.total) from gimprod.item as item "
				+ "inner join gimprod.municipalBond as mb on item.municipalbond_id = mb.id "
				//+ "inner join adjunct as adj on mb.adjunct_id = adj.id "
				//+ "inner join WaterServiceReference as wsr on wsr.id = adj.id "
				+ "inner join gimprod.WaterServiceReference as wsr on mb.adjunct_id = wsr.id "
				+ "where "
				+ "item.entry_id= :itemEntryID "
				+ "and mb.entry_id= 76 "
				+ "and mb.municipalbondstatus_id not in (1,5,8,9,10) "
				+ "and mb.serviceDate between :startDate and :endDate "
				+ "and wsr.waterSupplyCategory = :wsc "
				+ "and wsr.route_id IN (:routeIds)";*/
		String sentencia = "select item.entry_id, wsr.waterSupplyCategory, SUM(item.total) from gimprod.item as item "
				+ "inner join gimprod.municipalBond as mb on item.municipalbond_id = mb.id "
				+ "inner join gimprod.WaterServiceReference as wsr on mb.adjunct_id = wsr.id "
				+ "where "
				+ "item.entry_id in (76,459,460,464,450,43) "
				+ "and mb.municipalbondstatus_id in (3,4,6,7,11) "
				+ "and mb.serviceDate between :startDate and :endDate "
				+ "and wsr.waterSupplyCategory IN ( 'RESIDENCIAL', 'COMERCIAL','OFICIAL', 'OFICIAL MEDIO', 'INDUSTRIAL','CATEGORIA') "
				+ "and wsr.route_id IN (:routeIds) "
				+ "GROUP BY item.entry_id, wsr.waterSupplyCategory "
				+ "order by wsr.waterSupplyCategory,item.entry_id";
		
		qq=this.entityManager.createNativeQuery(sentencia);
		//qq.setParameter("itemEntryID", itemEntryId);
		qq.setParameter("startDate", startDate);
		qq.setParameter("endDate", endDate);
		//qq.setParameter("wsc", wsc.getName());
		qq.setParameter("routeIds", routeIds);
				
		List<Object[]> footerValues = qq.getResultList();
		//System.out.println("el tamaño de la consulta es::::::::: "+footerValues.size());
		
		for (WaterSupplyCategory wsc : categories) {
			for (Object[] ob : footerValues) {
				String entry_id = ob[0].toString();
				String category = ob[1].toString();
				BigDecimal subTotal=new BigDecimal(ob[2].toString());
				
				//System.out.println(":::::::::::::datos "+entry_id+"    "+category+"   "+subTotal+" se compara con : "+wsc.getName());
				
				if(wsc.getName().equals(category)){
					if(entry_id.equals("43")){
						wsc.setBasura_total(subTotal);
					}
					if(entry_id.equals("76")){
						wsc.setAgua_total(subTotal);
					}
					if(entry_id.equals("450")){
						wsc.setM_cuencas_total(subTotal);
					}
					if(entry_id.equals("459")){
						wsc.setAlcantarillado_total(subTotal);
					}
					if(entry_id.equals("460")){
						wsc.setP_maestro_total(subTotal);
					}
					if(entry_id.equals("464")){
						wsc.setS_ciudadana_total(subTotal);
					}
				}
			}
		}
		
		//itemEntryIds.add(new Long(76));		
		/*if(wsc.getId()!=8){
			for (Long entryId : entryIds) {
				if (entryId.equals(waterSupplyEntryId)){
					wsc.setAgua_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getAgua_total()));
				}
				if (entryId.equals(sewerageEntryId)){
					wsc.setAlcantarillado_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getAlcantarillado_total()));
				}
				if (entryId.equals(trashEntryId)){
					wsc.setBasura_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getBasura_total()));
				}
				if (entryId.equals(masterPlanEntryId)){
					wsc.setP_maestro_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getP_maestro_total()));
				}
				if (entryId.equals(securityEntryId)){
					wsc.setS_ciudadana_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getS_ciudadana_total()));
				}
				if (entryId.equals(microWaterShedsEntryId)){
					wsc.setM_cuencas_total(totalFootersBy(wsc, entryId));
					wsc.setPaidTotal_total(wsc.getPaidTotal_total().add(wsc.getM_cuencas_total()));
				}
			}	
		}*/		
	}
	

	/*public BigDecimal amountBy(WaterSupplyCategory wsc,WaterMeterStatus wms, ConsumptionRange cr, boolean isGoodConsumption) {
		//amount
		System.out.println("::::::::::::::::::::::::::: consulta Amount: "+wsc.getName()+" .... "+wms.getName()+" ..  "+cr.getFrom()+" . . . ."+cr.getTo() +" q es "+isGoodConsumption);
		int monthInt = month.getMonthInt();
		int yearInt = this.year;
		//
		String sentencia = null;
		if (isGoodConsumption) {
			sentencia = "select SUM(c.amount) from Consumption c "
				+ "left join c.waterMeterStatus wms "
				+ "left join c.waterSupply ws "
				+ "left join ws.waterSupplyCategory wsc "
				+ "left join ws.route route "
				+ "where c.month = :month and c.year = :year "
				+ "and route.id in (:routeIds) and wsc = :wsc and ( wms=:wms or wms=4 ) and c.amount between :startRange and :endRange";
		}else{
		sentencia = "select SUM(c.amount) from Consumption c "
				+ "left join c.waterMeterStatus wms "
				+ "left join c.waterSupply ws "
				+ "left join ws.waterSupplyCategory wsc "
				+ "left join ws.route route "
				+ "where c.month = :month and c.year = :year "
				+ "and route.id in (:routeIds) and wsc = :wsc and wms=:wms and c.amount between :startRange and :endRange";
		}
		Query q=this.getEntityManager().createQuery(sentencia);
		q.setParameter("month", monthInt);
		q.setParameter("year", yearInt);
		q.setParameter("routeIds", routeIds);
		q.setParameter("wsc", wsc);
		q.setParameter("wms", wms);
		q.setParameter("startRange", new BigDecimal(cr.getFrom()));
		q.setParameter("endRange", new BigDecimal(cr.getTo()));
		BigDecimal l = (BigDecimal)q.getSingleResult(); 
		return l == null ? BigDecimal.ZERO : l;	
	}*/
	
	public List<Object[]> subscribersNumber(WaterSupplyCategory wsc, WaterMeterStatus wms, ConsumptionRange cr, boolean isGoodConsumption) {
		//amount
		//System.out.println("::::::::::::::::::::::::::: consulta number: "+wsc.getName()+" .... "+wms.getName()+" ..  "+cr.getFrom()+" . . . ."+cr.getTo()+" q es "+isGoodConsumption);
		int monthInt = month.getMonthInt();
		int yearInt = this.year;
		//SUM(c.amount)
		String sentencia = null;
		if (isGoodConsumption) {
			sentencia = "select COUNT(c), SUM(c.amount) from Consumption c "
				+ "left join c.waterMeterStatus wms "
				+ "left join c.waterSupply ws "
				+ "left join ws.waterSupplyCategory wsc "
				+ "left join ws.route route "
				+ "where c.month = :month and c.year = :year "
				+ "and route.id in (:routeIds) and wsc = :wsc and (wms=:wms OR wms.id= 4) and c.amount between :startRange and :endRange";
			
		} else {
			sentencia = "select COUNT(c),SUM(c.amount) from Consumption c "
					+ "left join c.waterMeterStatus wms "
					+ "left join c.waterSupply ws "
					+ "left join ws.waterSupplyCategory wsc "
					+ "left join ws.route route "
					+ "where c.month = :month and c.year = :year "
					+ "and route.id in (:routeIds) and wsc = :wsc and wms=:wms and c.amount between :startRange and :endRange";
		}
		Query q=this.getEntityManager().createQuery(sentencia);
		q.setParameter("month", monthInt);
		q.setParameter("year", yearInt);
		q.setParameter("routeIds", routeIds);
		q.setParameter("wsc", wsc);
		q.setParameter("wms", wms);
		q.setParameter("startRange", new BigDecimal(cr.getFrom()));
		q.setParameter("endRange", new BigDecimal(cr.getTo()));
		
		//Long l = (Long)q.getSingleResult(); 
		//return l == null ? 0L : l;
		return q.getResultList();
	
	}

	public List<WaterMeterStatus> getMeterStatus() {
		return meterStatus;
	}

	public void setMeterStatus(List<WaterMeterStatus> meterStatus) {
		this.meterStatus = meterStatus;
	}

	private List<ConsumptionRange> residencialConsumption;
	private List<ConsumptionRange> comercialConsumption;

	public void initConsumptionRangeResidencial() {
		residencialConsumption = new ArrayList<ConsumptionRange>();
		residencialConsumption.add(new ConsumptionRange(new Long(0), new Long(10)));
		residencialConsumption.add(new ConsumptionRange(new Long(11), new Long(20)));
		residencialConsumption.add(new ConsumptionRange(new Long(21), new Long(50)));
		residencialConsumption.add(new ConsumptionRange(new Long(51), new Long(70)));
		residencialConsumption.add(new ConsumptionRange(new Long(71), new Long(90)));
		residencialConsumption.add(new ConsumptionRange(new Long(91), new Long(100)));
		residencialConsumption.add(new ConsumptionRange(new Long(101),new Long(10000)));
	}

	public void initConsumptionRangeComercial() {
		comercialConsumption = new ArrayList<ConsumptionRange>();
		comercialConsumption.add(new ConsumptionRange(new Long(0), new Long(10)));
		comercialConsumption.add(new ConsumptionRange(new Long(11),new Long(20)));
		comercialConsumption.add(new ConsumptionRange(new Long(21),new Long(50)));
		comercialConsumption.add(new ConsumptionRange(new Long(51), new Long(100)));
		comercialConsumption.add(new ConsumptionRange(new Long(101), new Long(10000)));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void loadRecordReadings() {
		newLoad=true;
		if (routeIds != null) {
			initConsumptionRangeComercial();
			initConsumptionRangeResidencial();
			startSubscriberAndConsumption();	
		}else{
			facesMessages.addToControl("resident",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Por vafor ingresar el rango de rutas");
		}
	}

	public void startSubscriberAndConsumption() {
		WaterConsumptionIndicator consumptionIndicator;
		for (WaterSupplyCategory wsc : categories) {
			wsc.waxingValues();
			consumptionIndicators = new ArrayList<WaterConsumptionIndicator>();
			if (wsc.equals(residentialWaterSupplyCategory) || wsc.equals(oldPeopleWaterSupplyCategory)) {
				for (ConsumptionRange cr : residencialConsumption) {
					consumptionIndicator = new WaterConsumptionIndicator();
					consumptionIndicator.setConsumptionRange(cr);
					for (WaterMeterStatus wms : meterStatus) {
						if(wms.equals(workingMeterStatus)){
							consumptionIndicator.setWms(wms);
							List<Object[]> data = subscribersNumber(wsc, wms, cr,true);
							if (data.size() > 0) {
								Object[] data_ob = data.get(0);
								//System.out.println("estos son los datosssssssssssssssssss "+data_ob[0]+"    "+data_ob[1]);
								if (data_ob[0] != null)
									consumptionIndicator.setSubscriber_good(Long.parseLong(data_ob[0].toString()));
								else
									consumptionIndicator.setSubscriber_good(Long.parseLong("0"));
								if (data_ob[1] != null)
									consumptionIndicator.setConsumption_good(new BigDecimal(data_ob[1].toString()));
								else
									consumptionIndicator.setConsumption_good(BigDecimal.ZERO);
							}else{
								consumptionIndicator.setSubscriber_good(Long.parseLong("0"));
								consumptionIndicator.setConsumption_good(BigDecimal.ZERO);
							}
							//sumas para  los footers
							wsc.setSubscriber_good_total(wsc.getSubscriber_good_total() + consumptionIndicator.getSubscriber_good());
							wsc.setConsumption_good_total(wsc.getConsumption_good_total().add(consumptionIndicator.getConsumption_good()));
							//findValues(consumptionIndicator, wsc, wms, cr, true);
						}
						if(wms.equals(withoutMeterStatus)){
							consumptionIndicator.setWms(wms);
							List<Object[]> data = subscribersNumber(wsc, wms, cr, false);
							if (data.size() > 0) {
								Object[] data_ob = data.get(0);
								//System.out.println("estos son los datosssssssssssssssssss "+data_ob[0]+"    "+data_ob[1]);
								if (data_ob[0] != null)
									consumptionIndicator.setSubscriber_unmetered(Long.parseLong(data_ob[0].toString()));
								else
									consumptionIndicator.setSubscriber_unmetered(Long.parseLong("0"));
								if (data_ob[1] != null)
									consumptionIndicator.setConsumption_unmetered(new BigDecimal(data_ob[1].toString()));
								else
									consumptionIndicator.setConsumption_unmetered(BigDecimal.ZERO);
							}else{
								
							}
							//consumptionIndicator.setSubscriber_unmetered(subscribersNumber(wsc, wms, cr, false));
							//consumptionIndicator.setConsumption_unmetered(amountBy(wsc, wms, cr, false));
							//sumas para  los footers
							wsc.setSubscriber_unmetered_total(wsc.getSubscriber_unmetered_total() + consumptionIndicator.getSubscriber_unmetered());
							wsc.setConsumption_unmetered_total(wsc.getConsumption_unmetered_total().add(consumptionIndicator.getConsumption_unmetered()));
							//findValues(consumptionIndicator, wsc, wms, cr, false);
						}
						if(wms.equals(damagedMeterStatus)){
							consumptionIndicator.setWms(wms);
							List<Object[]> data = subscribersNumber(wsc, wms, cr, false);
							if (data.size() > 0) {
								Object[] data_ob = data.get(0);
								//System.out.println("estos son los datosssssssssssssssssss "+data_ob[0]+"    "+data_ob[1]);
								if (data_ob[0] != null)
									consumptionIndicator.setSubscriber_damaged(Long.parseLong(data_ob[0].toString()));
								else
									consumptionIndicator.setSubscriber_damaged(Long.parseLong("0"));
								if (data_ob[1] != null)
									consumptionIndicator.setConsumption_damaged(new BigDecimal(data_ob[1].toString()));
								else
									consumptionIndicator.setConsumption_damaged(BigDecimal.ZERO);
							}
							//consumptionIndicator.setSubscriber_damaged(subscribersNumber(wsc, wms, cr, false));
							//consumptionIndicator.setConsumption_damaged(amountBy(wsc, wms, cr, false));
							//sumas para  los footers
							wsc.setSubscriber_damaged_total(wsc.getSubscriber_damaged_total() + consumptionIndicator.getSubscriber_damaged());
							wsc.setConsumption_damaged_total(wsc.getConsumption_damaged_total().add(consumptionIndicator.getConsumption_damaged()));
							//findValues(consumptionIndicator, wsc, wms, cr, false);
						}
					}
					consumptionIndicators.add(consumptionIndicator);
				}
				//findFooterValues(wsc );
				wsc.setConsumptionIndicators(consumptionIndicators);
			} else {
				for (ConsumptionRange cr : comercialConsumption) {
					consumptionIndicator = new WaterConsumptionIndicator();
					consumptionIndicator.setConsumptionRange(cr);
					for (WaterMeterStatus wms : meterStatus) {
						if(wms.equals(workingMeterStatus)){
							consumptionIndicator.setWms(wms);
							
							List<Object[]> data = subscribersNumber(wsc, wms, cr,true);
							if (data.size() > 0) {
								Object[] data_ob = data.get(0);
								//System.out.println("estos son los datosssssssssssssssssss "+data_ob[0]+"    "+data_ob[1]);
								//consumptionIndicator.setSubscriber_good(Long.parseLong(data_ob[0].toString()));
								//consumptionIndicator.setConsumption_good(new BigDecimal(data_ob[1].toString()));
								if (data_ob[0] != null)
									consumptionIndicator.setSubscriber_good(Long.parseLong(data_ob[0].toString()));
								else
									consumptionIndicator.setSubscriber_good(Long.parseLong("0"));
								if (data_ob[1] != null)
									consumptionIndicator.setConsumption_good(new BigDecimal(data_ob[1].toString()));
								else
									consumptionIndicator.setConsumption_good(BigDecimal.ZERO);
							}
							
							//consumptionIndicator.setSubscriber_good(subscribersNumber(wsc, wms, cr, true));
							//consumptionIndicator.setConsumption_good(amountBy(wsc, wms, cr, true));
							//sumas para  los footers
							wsc.setSubscriber_good_total(wsc.getSubscriber_good_total() + consumptionIndicator.getSubscriber_good());
							wsc.setConsumption_good_total(wsc.getConsumption_good_total().add(consumptionIndicator.getConsumption_good()));
							
							//findValues(consumptionIndicator, wsc, wms, cr, true);
						}
						if(wms.equals(withoutMeterStatus)){
							consumptionIndicator.setWms(wms);
							List<Object[]> data = subscribersNumber(wsc, wms, cr,false);
							if (data.size() > 0) {
								Object[] data_ob = data.get(0);
								//System.out.println("estos son los datosssssssssssssssssss "+data_ob[0]+"    "+data_ob[1]);
								//consumptionIndicator.setSubscriber_good(Long.parseLong(data_ob[0].toString()));
								//consumptionIndicator.setConsumption_good(new BigDecimal(data_ob[1].toString()));
								if (data_ob[0] != null)
									consumptionIndicator.setSubscriber_unmetered(Long.parseLong(data_ob[0].toString()));
								else
									consumptionIndicator.setSubscriber_unmetered(Long.parseLong("0"));
								if (data_ob[1] != null)
									consumptionIndicator.setConsumption_unmetered(new BigDecimal(data_ob[1].toString()));
								else
									consumptionIndicator.setConsumption_unmetered(BigDecimal.ZERO);
							}
							//consumptionIndicator.setSubscriber_unmetered(subscribersNumber(wsc, wms, cr, false));
							//consumptionIndicator.setConsumption_unmetered(amountBy(wsc, wms, cr, false));
							//sumas para  los footers
							wsc.setSubscriber_unmetered_total(wsc.getSubscriber_unmetered_total() + consumptionIndicator.getSubscriber_unmetered());
							wsc.setConsumption_unmetered_total(wsc.getConsumption_unmetered_total().add(consumptionIndicator.getConsumption_unmetered()));
							
							//findValues(consumptionIndicator, wsc, wms, cr, false);
						}
						if(wms.equals(damagedMeterStatus)){
							consumptionIndicator.setWms(wms);
							List<Object[]> data = subscribersNumber(wsc, wms, cr,false);
							if (data.size() > 0) {
								Object[] data_ob = data.get(0);
								//System.out.println("estos son los datosssssssssssssssssss "+data_ob[0]+"    "+data_ob[1]);
								//consumptionIndicator.setSubscriber_good(Long.parseLong(data_ob[0].toString()));
								//consumptionIndicator.setConsumption_good(new BigDecimal(data_ob[1].toString()));
								if (data_ob[0] != null)
									consumptionIndicator.setSubscriber_damaged(Long.parseLong(data_ob[0].toString()));
								else
									consumptionIndicator.setSubscriber_damaged(Long.parseLong("0"));
								if (data_ob[1] != null)
									consumptionIndicator.setConsumption_damaged(new BigDecimal(data_ob[1].toString()));
								else
									consumptionIndicator.setConsumption_damaged(BigDecimal.ZERO);
							}
							//consumptionIndicator.setSubscriber_damaged(subscribersNumber(wsc, wms, cr, false));
							//consumptionIndicator.setConsumption_damaged(amountBy(wsc, wms, cr, false));
							//sumas para  los footers
							wsc.setSubscriber_damaged_total(wsc.getSubscriber_damaged_total() + consumptionIndicator.getSubscriber_damaged());
							wsc.setConsumption_damaged_total(wsc.getConsumption_damaged_total().add(consumptionIndicator.getConsumption_damaged()));
							
							//findValues(consumptionIndicator, wsc, wms, cr, false);
						}
					}					
					consumptionIndicators.add(consumptionIndicator);
				}				
				//findFooterValues(wsc );
				wsc.setConsumptionIndicators(consumptionIndicators);
			}
		}
		//findFooterValues(wsc);
		findFooterValuesBestWay();
	}
	
	/*public void loadWaterValues() {
		for (WaterSupplyCategory wsc : categories) {
			if(!wsc.getId().equals(zeroCategoryWaterSupplyCategory.getId())){
				for (WaterConsumptionIndicator wci : wsc.getConsumptionIndicators()) {
					findValuesFor(wci, wsc, wci.getConsumptionRange(), waterSupplyEntryId);
				}	
			}
		}
	}*/
	
	/*public void loadSewerageValues() {
		for (WaterSupplyCategory wsc : categories) {
			if(!wsc.getId().equals(zeroCategoryWaterSupplyCategory.getId())){
				for (WaterConsumptionIndicator wci : wsc.getConsumptionIndicators()) {
					findValuesFor(wci, wsc, wci.getConsumptionRange(), sewerageEntryId);
				}	
			}
		}
	}*/

	public List<ConsumptionRange> getResidencialConsumption() {
		return residencialConsumption;
	}

	public void setResidencialConsumption(
			List<ConsumptionRange> residencialConsumption) {
		this.residencialConsumption = residencialConsumption;
	}

	public List<ConsumptionRange> getComercialConsumption() {
		return comercialConsumption;
	}

	public void setComercialConsumption(List<ConsumptionRange> comercialConsumption) {
		this.comercialConsumption = comercialConsumption;
	}

	public List<WaterSupplyCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<WaterSupplyCategory> categories) {
		this.categories = categories;
	}

	public List<WaterConsumptionIndicator> getConsumptionIndicators() {
		return consumptionIndicators;
	}

	public void setConsumptionIndicators(
			List<WaterConsumptionIndicator> consumptionIndicators) {
		this.consumptionIndicators = consumptionIndicators;
	}

	public ConsumptionState getGeneratedConsumptionState() {
		return generatedConsumptionState;
	}

	public void setGeneratedConsumptionState(
			ConsumptionState generatedConsumptionState) {
		this.generatedConsumptionState = generatedConsumptionState;
	}

	public ConsumptionState getEnteredConsumptionState() {
		return enteredConsumptionState;
	}

	public void setEnteredConsumptionState(ConsumptionState enteredConsumptionState) {
		this.enteredConsumptionState = enteredConsumptionState;
	}

	public ConsumptionState getCheckedConsumptionState() {
		return checkedConsumptionState;
	}

	public void setCheckedConsumptionState(ConsumptionState checkedConsumptionState) {
		this.checkedConsumptionState = checkedConsumptionState;
	}

	public ConsumptionState getPreEmittedConsumptionState() {
		return preEmittedConsumptionState;
	}

	public void setPreEmittedConsumptionState(
			ConsumptionState preEmittedConsumptionState) {
		this.preEmittedConsumptionState = preEmittedConsumptionState;
	}

	public Long getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(Long pendingStatus) {
		this.pendingStatus = pendingStatus;
	}

	public WaterMeterStatus getWorkingMeterStatus() {
		return workingMeterStatus;
	}

	public void setWorkingMeterStatus(WaterMeterStatus workingMeterStatus) {
		this.workingMeterStatus = workingMeterStatus;
	}

	public WaterMeterStatus getWithoutMeterStatus() {
		return withoutMeterStatus;
	}

	public void setWithoutMeterStatus(WaterMeterStatus withoutMeterStatus) {
		this.withoutMeterStatus = withoutMeterStatus;
	}

	public WaterMeterStatus getDamagedMeterStatus() {
		return damagedMeterStatus;
	}

	public void setDamagedMeterStatus(WaterMeterStatus damagedMeterStatus) {
		this.damagedMeterStatus = damagedMeterStatus;
	}

	public BigDecimal getTotalOwedByRoute() {
		return totalOwedByRoute;
	}

	public void setTotalOwedByRoute(BigDecimal totalOwedByRoute) {
		this.totalOwedByRoute = totalOwedByRoute;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<WaterSupply> getPendingSupplies() {
		return pendingSupplies;
	}

	public void setPendingSupplies(List<WaterSupply> pendingSupplies) {
		this.pendingSupplies = pendingSupplies;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}


}