package org.gob.gim.market.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.market.facade.MarketService;
import org.gob.gim.market.view.MarketReportView;
import org.gob.gim.revenue.action.ContractHome;
import org.gob.gim.revenue.action.EmissionOrderHome;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.market.model.Concession;
import ec.gob.gim.market.model.Market;
import ec.gob.gim.market.model.MarketEmission;
import ec.gob.gim.market.model.ProductType;
import ec.gob.gim.market.model.Stand;
import ec.gob.gim.market.model.StandStatus;
import ec.gob.gim.market.model.StandType;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.waterservice.model.MonthType;

@Name("marketHome")
public class MarketHome extends EntityHome<Market> {

	private Stand stand = new Stand();
	private ProductType productType = new ProductType();
	private Concession concession = new Concession();

	@Logger
	Log logger;
	// para el resident chooser
	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;

	// para arendar el stand
	private String marketRentOperation;
	private Boolean enabledToRent = new Boolean(false);

	// //
	private Stand standHistory;

	@In(create = true)
	StandHome standHome;

	@In(create = true)
	ContractHome contractHome;

	private MarketEmission marketEmission = new MarketEmission();
	@In(create = true)
	private EmissionOrderHome emissionOrderHome;

	private MarketService marketService;
	//private SystemParameterService systemParameterService;
	public static String MARKET_SERVICE_NAME = "/gim/MarketService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private EmissionOrder eo;
	@In
	private FacesMessages facesMessages;
	@In
	UserSession userSession;

	private List<Stand> standsEmission;

	private StandType standType;
	private MonthType monthType;
	
	private Boolean isLoadAllStands = new Boolean(false);

	// / para el reporte de emisiones realizadas
	private Date startDate = new Date();
	private Date endDate = new Date();
	private List<MarketEmission> pemissions;
	private List<StandType> standTypesCheck;
	
	// para reporte de mercados
//	private List<Market> markets;
	private Market market;
	private Integer year = -1;
	private MonthType month;

	public void setMarketId(Long id) {
		setId(id);
	}

	public Long getMarketId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if (this.getInstance().getCurrentConcession() != null) {
			this.concession = this.getInstance().getCurrentConcession();
			this.resident = this.concession.getResident();
			if (this.resident != null)
				this.identificationNumber = this.resident
						.getIdentificationNumber();
		}
		if (this.stand.getStandStatus() == null) {
			this.stand.setStandStatus(StandStatus.FREE);
		}

		/*
		 * if (marketRentOperation.equals("toRent")) { enabledToRent = new
		 * Boolean(true); } else { enabledToRent = new Boolean(false); }
		 */
	}

	public List<StandType> findStandTypeByMarket() {
		Query query = this.getEntityManager().createNamedQuery(
				"Stand.findStandTypeByMarket");
		query.setParameter("market", this.getInstance());
		List<StandType> standTypes = query.getResultList();
		List<StandType> standTypesNew = new ArrayList<StandType>();
		for (StandType s : standTypes) {
			if (!standTypesNew.contains(s)) {
				standTypesNew.add(s);
			}
		}
		this.standTypesCheck = standTypesNew;
		return standTypesNew;
	}

	public boolean isWired() {
		return true;
	}

	public Market getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Concession> getConcessions() {
		return getInstance() == null ? null : new ArrayList<Concession>(
				getInstance().getConcessions());
	}

	public List<Stand> getStands() {
		return getInstance() == null ? null : new ArrayList<Stand>(
				getInstance().getStands());
	}

	public Stand getStand() {
		return stand;
	}

	public void setStand(Stand stand) {
		this.stand = stand;
	}

	/*
	 * public void addNewMarket() {
	 * 
	 * }
	 */

	// / para los stands
	public List<StandStatus> getStandStatuses() {

		return Arrays.asList(StandStatus.values());
	}

	public List<StandType> getStandTypes() {
		Query q = this.getEntityManager().createNamedQuery(
				"StandType.findByName");
		return q.getResultList();
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public List<ProductType> autoComplete(Object o) {
		Query query = this.getEntityManager().createNamedQuery(
				"ProductType.findByName");
		query.setParameter("criteria", o.toString());
		return (List<ProductType>) query.getResultList();
	}

	public void selectProductType(ProductType pt) {
		if (pt != null) {
			this.productType = pt;
		}
	}

	public void enviarNameProducType(String cc) {
		System.out.println("::::::::::::::::::::::::: " + cc
				+ productType.getName());
		// this.productType.setName(cc);
	}

	/*
	 * public void addProductType() {
	 * 
	 * ProductType pt = this.productType;
	 * System.out.println("tipo de producto ::::::::::: " +
	 * this.productType.getName()); pt.setIsActive(new Boolean(true));
	 * this.stand.add(pt); this.productType = new ProductType();
	 * 
	 * }
	 */

	/*
	 * public void removeProductType(ProductType productType) { if
	 * (this.stand.getId() != null) { this.stand.remove(productType); } }
	 */

	public void addStand() {
		if (stand.getId() != null) {
			this.update();
		} else {
			this.getInstance().add(this.stand);
		}
	}

	public void removeStand(Stand stand) {
		this.getInstance().remove(stand);
	}

	public void selectStand(Stand stand) {
		this.stand = stand;
	}

	public void addNewStand() {
		this.stand = new Stand();
	}

	// / metodos para las concesiones

	public Concession getConcession() {
		return concession;
	}

	public void setConcession(Concession concession) {
		this.concession = concession;
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

	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		logger.info("ingresa el search by criteria");
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
		this.resident = null;
		this.concession.setResident(null);
	}

	public void search() {
		logger.info("ingresa el search");
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident1 = (Resident) query.getSingleResult();
			this.concession.setResident(resident1);
			this.resident = resident1;
		} catch (Exception e) {
			this.concession.setResident(null);
			this.resident = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		logger.info("select el resident");
		UIComponent component = event.getComponent();
		Resident resident1 = (Resident) component.getAttributes().get(
				"resident");
		this.concession.setResident(resident);
		this.resident = resident1;
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void addConcession() {
		if (resident != null) {
			System.out
					.println("=============>>>>>>>>>>>>>>>>>>> entra a agregar concession");
			this.concession.setResident(resident);
			this.getInstance().setCurrentConcession(this.concession);
			this.getInstance().add(this.concession);
			// this.update();
		}
	}

	public void newConcession() {
		this.concession = new Concession();
		this.resident = null;
		this.getInstance().setCurrentConcession(null);
		this.identificationNumber = null;
	}

	public void selectStandHistory(Stand stand) {
		this.standHistory = stand;
	}

	public void noRenovateCancelAction() {
		standHome.setInstance(new Stand());
	}

	public void removeConcession(Concession conc) {
		this.getInstance().remove(conc);
		if (this.getInstance().getCurrentConcession() != null
				&& this.getInstance().getCurrentConcession().equals(conc))
			this.getInstance().setCurrentConcession(null);
	}

	public Boolean getEnabledToRent() {
		return enabledToRent;
	}

	public void setEnabledToRent(Boolean enabledToRent) {
		this.enabledToRent = enabledToRent;
	}

	public String getMarketRentOperation() {
		return marketRentOperation;
	}

	public void setMarketRentOperation(String marketRentOperation) {
		this.marketRentOperation = marketRentOperation;
	}

	public Stand getStandHistory() {
		return standHistory;
	}

	public void setStandHistory(Stand standHistory) {
		this.standHistory = standHistory;
	}

	public void contractInformation(Stand stand) {
		this.standHome.setInstance(stand);
	}

	public void noRenovateAction(Stand stand) {
		this.contractHome.setInstance(stand.getCurrentContract());
		this.contractHome.getInstance().setExpirationDate(new Date());
		this.contractHome.update();

		this.standHome.setInstance(stand);
		this.standHome.getInstance().setStandStatus(StandStatus.FREE);
		// this.standHome.getInstance().remove(this.standHome.getInstance().getCurrentContract());
		this.standHome.getInstance().setCurrentContract(null);

		this.standHome.update();

		loadStandsByType(this.standType);
	}

	// para la busqueda de la street
	public List<Street> searchStreetByName(Object suggest) {
		Query e = this.getEntityManager().createNamedQuery("Street.findByName");
		e.setParameter("name", (String) suggest);
		// e.setParameter("suggest", (String) suggest);
		return (List<Street>) e.getResultList();
	}

	public void selectStreet(Street s) {
		if (s != null) {
			this.stand.setStreet(s);
		}
	}

	// para cambio de la Dr. parra
	public void standToChange(Stand stand) {
		Contract c = new Contract();
		c.setCreationDate(new Date());
		c.setDescription("Local: " + stand.getName() + " - Area: "
				+ stand.getArea() + " m2");
		stand.add(c);
		stand.setCurrentContract(c);
		this.standHome.setInstance(stand);
	}

	public void standToEdit(Stand stand) {
		/*
		 * Contract c = new Contract(); c.setCreationDate(new Date());
		 * c.setDescription("Local: " + stand.getName() + " - Area: " +
		 * stand.getArea() + " m2"); stand.add(c); stand.setCurrentContract(c);
		 */
		this.standHome.setInstance(stand);
	}

	public void updateStandContract() {
		this.standHome.getInstance().setIsActive(true);
		this.standHome.getInstance().setIsCalculated(false);
		this.standHome.getInstance().setStandStatus(StandStatus.RENTED);
		this.standHome.update();
		// this.update();
		loadStandsByType(this.standType);
	}

	public void persistStandContract() {
		this.standHome.getInstance().setIsActive(true);
		this.standHome.getInstance().setIsCalculated(false);
		this.standHome.getInstance().setMarket(this.getInstance());
		this.standHome.getInstance().setStandStatus(StandStatus.RENTED);
		this.standHome.persist();
		// this.update();
		loadStandsByType(this.standType);

	}

	public void updateStand() {
		// this.standHome.getInstance().setStandStatus(StandStatus.RENTED);
		this.standHome.update();
		// this.update();
		loadStandsByType(this.standType);
	}

	public void updateStandValue(Stand stand) {
		this.standHome.setInstance(stand);
		this.standHome.update();
	}

	public void newStandPanel() {
		this.standHome.setInstance(new Stand());
	}

	public MarketEmission getMarketEmission() {
		return marketEmission;
	}

	public void setMarketEmission(MarketEmission marketEmission) {
		this.marketEmission = marketEmission;
	}
	/**
	 * Comprueba que todas las cuentas esten acivas para poder emitir
	 * No se emite hasta q todas esten corregidas
	 * @return
	 */
	public boolean checkEntries(){
		boolean isChecked = true;
		for (StandType stc : standTypesCheck) {
			Entry e = stc.getEntry();
			if(!e.getIsActive()){
				isChecked=false;
				return isChecked;
			}
		}
		return isChecked;
	}
	
	
	public String emitMarket() {

		if (checkEntries()) {
			if (marketService == null)
				marketService = ServiceLocator.getInstance().findResource(MARKET_SERVICE_NAME);
			try {
				marketEmission.setDate(new Date());
				marketEmission.setHasPreEmit(true);
				marketEmission.setMonth(this.monthType.getMonthInt());
				marketEmission.setMarket(this.getInstance());
				if (!isLoadAllStands) {
					marketEmission.setStandType(standType);
					marketEmission.setExplanation("Tipo de Local: "
							+ standType.getEntry().getCode() + " - "
							+ standType.getName());
				} else {
					marketEmission.setExplanation("Tipo de Local: Todos");
				}

				eo = marketService.generateEmissionOrder(this.getInstance(),
						standsEmission, userSession.getFiscalPeriod(),
						userSession.getPerson(), marketEmission.getYear(),
						this.monthType.getMonthInt(),
						marketEmission.getExplanation());
				marketService.saveEmissionOrder(eo, true, marketEmission);
				if (eo != null) {
					String message = Interpolator.instance().interpolate(
							"Mercado " + this.getInstance().getName()+ " ha sido emitido, " + marketEmission.getExplanation(),
							new Object[0]);
					facesMessages.addToControl(
									"residentChooser",
									org.jboss.seam.international.StatusMessage.Severity.INFO,
									message);
					return "completed";
				}
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				String message = Interpolator.instance().interpolate(
						"#{messages['property.errorGenerateTax']}",
						new Object[0]);
				facesMessages
						.addToControl(
								"residentChooser",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
				return null;
			}
		} else {
			String message = Interpolator.instance().interpolate(
					"Existen cuentas inactivas", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}

	/**
	 * Determina si el mercado ha sido emitido
	 * 
	 * @return
	 */
	public boolean isMarketEmited() {
		Query query = this.getEntityManager().createNamedQuery(
				"MarketEmission.finEmission");
		query.setParameter("year", this.marketEmission.getYear());
		query.setParameter("month", this.marketEmission.getMonthType()
				.getMonthInt());
		query.setParameter("marketId", this.getInstance().getId());
		List<MarketEmission> totalConsumptionEmited = query.getResultList();
		if (totalConsumptionEmited.size() > 0) {
			return true;
		} else {
			return false;
		}

		/*
		 * Query query = this.getEntityManager().createNamedQuery(
		 * "Consumption.totalByYearMonthEmited"); query.setParameter("year",
		 * year); query.setParameter("month", month.getMonthInt());
		 * query.setParameter("routeId", this.getInstance().getId()); int
		 * totalConsumptionEmited = 0; if (query.getResultList().size() > 0) {
		 * totalConsumptionEmited = query.getResultList().size(); } if
		 * (consumptions != null) { int intConsumptions = consumptions.size();
		 * if (totalConsumptionEmited == intConsumptions) { // ///falta
		 * controlar que ya este emitido return true; } else { return false; } }
		 * else { return false; }
		 */
	}

	public void loadStandsByType(StandType standType) {
		this.standType = standType;
		Query query = this.getEntityManager().createNamedQuery(
				"Stand.findStandByType");
		query.setParameter("market", this.getInstance());
		query.setParameter("standType", standType);
		query.setParameter("standStatus", StandStatus.RENTED);
		this.standsEmission = query.getResultList();
		isLoadAllStands = new Boolean(false);
	}

	public void loadAllStands() {
		Query query = this.getEntityManager().createNamedQuery(
				"Stand.findStandByMarket");
		query.setParameter("market", this.getInstance());
		query.setParameter("standStatus", StandStatus.RENTED);
		this.standsEmission = query.getResultList();
		isLoadAllStands = new Boolean(true);
	}

	public List<Stand> getStandsEmission() {
		return standsEmission;
	}

	public void setStandsEmission(List<Stand> standsEmission) {
		this.standsEmission = standsEmission;
	}

	public StandType getStandType() {
		return standType;
	}

	public void setStandType(StandType standType) {
		this.standType = standType;
	}

	public MonthType getMonthType() {
		return monthType;
	}

	public void setMonthType(MonthType monthType) {
		this.monthType = monthType;
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

	public List<MarketEmission> getPemissions() {
		return pemissions;
	}

	public void setPemissions(List<MarketEmission> pemissions) {
		this.pemissions = pemissions;
	}

	public void findPerformedEmission() {
		Query q = this.getEntityManager().createNamedQuery(
				"MarketEmission.findByDate");
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		pemissions = q.getResultList();
	}

	public List<Stand> freeStands() {
		Query query = this.getEntityManager().createNamedQuery(
				"Stand.findStandByType");
		query.setParameter("market", this.getInstance());
		query.setParameter("standType", standType);
		query.setParameter("standStatus", StandStatus.FREE);
		return query.getResultList();
	}

	public Boolean getIsLoadAllStands() {
		return isLoadAllStands;
	}

	public void setIsLoadAllStands(Boolean isLoadAllStands) {
		this.isLoadAllStands = isLoadAllStands;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public MonthType getMonth() {
		return month;
	}

	public void setMonth(MonthType month) {
		this.month = month;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}
	
	public void startDates(){
		int monthInt = month.getMonthInt();
		int yearInt = this.year;
		Calendar cstartDate = Calendar.getInstance();
		cstartDate.set(yearInt, (monthInt-1), 01);
		int endDayOfMonth = cstartDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		Calendar cendDate = Calendar.getInstance();
		cendDate.set(year, (monthInt - 1), endDayOfMonth);
		this.startDate = cstartDate.getTime();
		this.endDate = cendDate.getTime();
		System.out.println("start "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(startDate.getTime()));
		System.out.println("end   "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(endDate.getTime()));
	}
	
	@Factory("marketsActive")
	public List<Market> loadActiveMarkets() {
		Query q=this.getEntityManager().createNamedQuery("Market.findAllActive");
		return q.getResultList();
	}
	
	@Factory("mBStatuses")
	public List<String> loadMunicipalBondStatuses() {
		List<String> statuses=new  ArrayList<String>();
		statuses.add("TODOS");
		statuses.add("PENDIENTE");
		statuses.add("PAGADA");
		statuses.add("EN CONVENIO");
		return statuses;
	}
	
	private List<MarketReportView> marketReportViews;
	private String status;
	//private List<String> municipalBondStatuses;
	
	public void findAwardeeByMarket() {
		startDates();		
		String sentence = "select stand.number standNumber, stand.name standName, entry.code entryCode, entry.name entryName, "
				+ "resident.identificationnumber identificationnumber, resident.name residentName, municipalbondstatus.name municipalbondstatus, "
				+ "municipalbond.paidtotal paidtotal, marketreference.producttype productType "
				+ "from municipalbond "
				+ "inner join marketreference on municipalbond.adjunct_id = marketreference.id "
				+ "inner join resident on municipalbond.resident_id= resident.id "
				+ "inner join municipalbondstatus on  municipalbond.municipalbondstatus_id= municipalbondstatus.id "
				+ "inner join market on marketreference.market_id=market.id "
				+ "inner join stand on marketreference.stand_id=stand.id "
				+ "inner join entry on municipalbond.entry_id = entry.id "
				+ "where entry_id in (23,27,33,236,30,85,26,34,88,51) "
				+ "and servicedate between :startDate and :endDate "
				+ "and municipalbondstatus.id in (:statuses) AND market.id= :marketId "
				+ "order by marketreference.producttype,stand.number,entry_id";
		Query q = this.getEntityManager().createNativeQuery(sentence);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		q.setParameter("marketId", this.market.getId());
		q.setParameter("statuses", getMunicipalBondStatuses());
		//marketReportViews = q.getResultList();
		List<Object[]> data=q.getResultList();
		marketReportViews = new ArrayList<MarketReportView>();
		MarketReportView mrv;
		for (Object[] d : data) {
			Integer sNumber = Integer.parseInt(d[0].toString());
			String sName=d[1].toString();
			String eCode=d[2].toString();
			String eName=d[3].toString();
			String rIden=d[4].toString();
			String rName=d[5].toString();
			String mbs=d[6].toString();
			BigDecimal pt = new BigDecimal(d[7].toString());
			String pType=d[8].toString();
			mrv = new MarketReportView(sNumber, sName, eCode, eName, rIden, rName, mbs, pt, pType);
			
			//mrv=new MarketReportView(, , d[2].toString(), d[3].toString(), d[4].toString(), d[5].toString(), d[6].toString(), new BigDecimal(d[7].toString()), d[8].toString());
			marketReportViews.add(mrv);
		}
		System.out.println("los dato sonssssssssssssssss "+marketReportViews.size());
	}
	
	public List<Long> getMunicipalBondStatuses(){
		List<Long> ids=new ArrayList<Long>();
		if(status.equals("TODOS")){
			ids.add(new Long(3));
			ids.add(new Long(4));
			ids.add(new Long(6));
		}else
		if(status.equals("PENDIENTE")){
			ids.add(new Long(3));
		}else
		if(status.equals("PAGADA")){
			ids.add(new Long(6));
		}else
		if(status.equals("EN CONVENIO")){
			ids.add(new Long(4));
		}else{
			//cargo por defecto todos los valores
			ids.add(new Long(3));
			ids.add(new Long(4));
			ids.add(new Long(6));
		}
			
		return ids;
	}

	public List<MarketReportView> getMarketReportViews() {
		return marketReportViews;
	}

	public void setMarketReportViews(List<MarketReportView> marketReportViews) {
		this.marketReportViews = marketReportViews;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
