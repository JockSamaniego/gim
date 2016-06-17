package org.gob.gim.waterservice.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.MonthType;
import ec.gob.gim.waterservice.model.Route;
import ec.gob.gim.waterservice.model.RoutePeriod;
import ec.gob.gim.waterservice.model.WaterBlockLog;
import ec.gob.gim.waterservice.model.WaterMeter;
import ec.gob.gim.waterservice.model.WaterSupply;
import ec.gob.gim.waterservice.model.WaterSupplyAverage;

@Name("recordReadingHome")
public class RecordReadingHome extends EntityHome<Route> {

	@In(create = true)
	RoutePeriodHome routePeriodHome;
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
	List<Consumption> consumptions;
	MonthType month;

	// para el resident chooser
	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;
	List<ConsumptionState> consumptionStates;

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

	private Consumption actualConsumption;
	private Consumption infoConsumption;
	//
	private int actualReadingPosition;
	private Integer changeIn;
	private Integer startYear;
	private SystemParameterService systemParameterService;

	// ////in
	// private String searchType = "ruta";
	private Integer waterSupplyServiceNumber;
	private Boolean isPreEmited = new Boolean(false);
	
	@In(create = true)
	EntityManager entityManager;

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

	private boolean isFirstTime = true;

	public void wire() {
		if (!isFirstTime)
			return;
		getInstance();
		readingMan = new Person();
		routePeriod = new RoutePeriod();
		loadConsumptionStates();
		isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	public String addRoutePeriod() {
		routePeriod.setReadingMan(readingMan);
		this.getInstance().add(routePeriod);
		// crear nuevos objetos
		routePeriod = new RoutePeriod();
		readingMan = new Person();
		this.resident = null;
		return null;
	}

	public String editRoutePeriod(RoutePeriod rp) {
		routePeriod = rp;
		readingMan = rp.getReadingMan();
		return null;
	}

	public void saveRecordReading(Consumption conNew) {
		// int pos = consumptions.indexOf(conNew);
		if (checkCorrectReading(conNew)) {
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
			// addAvarageNewConsumption();
		}
	}

	@Override
	public String persist() {
		String stringReturn = "";
		Query query = this.getEntityManager().createNamedQuery(
				"Route.countByName");
		query.setParameter("routeName", this.getInstance().getName());
		int count = Integer.parseInt(query.getSingleResult().toString());
		if (count == 0) {
			stringReturn = super.persist();
		} else {
			String message = Interpolator.instance()
					.interpolate("#{messages['route.routeNameAlreadyExist']}",
							new Object[0]);
			facesMessages.addFromResourceBundle(Severity.ERROR, message,
					new Object());
			stringReturn = "";
		}
		return stringReturn;
	}

	public List<Person> autoComplete(Object o) {
		Query query = this.getEntityManager().createNamedQuery(
				"Person.findReadingMan");
		query.setParameter("criteria", o.toString());
		return (List<Person>) query.getResultList();
	}

	/*
	 * para autocompletado en cuadro de texto escribiendo el nombre
	 */
	public List<Route> autoCompleteRouteByName(Object o) {
		Query query = this.getEntityManager().createNamedQuery(
				"Route.findByName");
		query.setParameter("routeName", o.toString());
		return (List<Route>) query.getResultList();
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
		} else {
			System.out.println("es nulo no entiendo pos? " + startYear);
		}

		return years;
	}

	/*
	 * @Factory("monthTypes") public List<MonthType> loadMonthTypes() { return
	 * Arrays.asList(MonthType.values()); }
	 */

	/*
	 * eventos orden de lectura
	 */

	public void loadWaterSupplyes() {
		Query query = this.getEntityManager().createNamedQuery(
				"WaterSupply.findByRoute");
		query.setParameter("routeId", this.getInstance().getId());
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

	/**
	 * Updates all reading orders, depending on the parameter "changeIn"
	 */
	public void updateServiceOrderAll() {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		changeIn = systemParameterService
				.findParameter("AMOUNT_BETWEEN_WATER_ROUTE");
		System.out.println("========> changeIn " + changeIn);
		if (changeIn != null) {
			if (waterSupplies != null) {
				int routeOrderActual = 0;
				int i = 0;
				joinTransaction();
				for (WaterSupply ws : waterSupplies) {
					if (i == 0) {
						routeOrderActual = ws.getRouteOrder();
					} else {
						routeOrderActual = routeOrderActual + changeIn + 1;
						ws.setRouteOrder(routeOrderActual);
						getEntityManager().merge(ws);
					}
					i++;
				}
				getEntityManager().flush();
				loadWaterSupplyes();
			} else {
				String message = Interpolator.instance()
						.interpolate("#{messages['route.routeLoadServices']}",
								new Object[0]);
				facesMessages.addFromResourceBundle(Severity.ERROR, message,
						new Object());
			}
		} else {
			String message = Interpolator.instance().interpolate(
					"#{messages['route.parameterNoRegistred']}", new Object[0]);
			facesMessages.addFromResourceBundle(Severity.ERROR, message,
					new Object());
		}

	}

	private boolean newLoad = false;

	public boolean isNewLoad() {
		return newLoad;
	}

	public void setNewLoad(boolean newLoad) {
		this.newLoad = newLoad;
	}

	/*
	 * public void cleanList(){ consumptions = new ArrayList<Consumption>();
	 * newLoad = false; }
	 */

	/*
	 * eventos registro de lecturas
	 */
	public void loadRecordReadings() {
		System.out.println("entra a cargar::::!!!!!!!!!!!!!!!!! ");
		// la coinsulta debe ser de un mes atras para poder cargar la lectura
		// anterior
		int monthInt = month.getMonthInt() - 1;
		int yearInt = year;
		// en el caso q sea enero el año debe disminuir en 1 y el mes es 12
		if (monthInt == 0) {
			yearInt = yearInt - 1;
			monthInt = 12;
		}
		findSelectMonthRecordReadings();
		Query query = this.getEntityManager().createNamedQuery(
				"Consumption.findByYearMonth");
		query.setParameter("year", yearInt);
		query.setParameter("month", monthInt);
		query.setParameter("routeId", this.getInstance().getId());
		consumptions = loadNewConsumtionList(query.getResultList());
		if (consumptions.size() > 0)
			newLoad = true;
	}

	/**
	 * Create a new Consumption's list, if a Consumption exist is added to the
	 * new list
	 * 
	 * @param oldList
	 * @return
	 */
	public List<Consumption> loadNewConsumtionList(List<Consumption> oldList) {
		avaragePreviousReading = new BigDecimal(0);
		if (consumptionStates == null)
			loadConsumptionStates();
		List<Consumption> newConsumptions = new ArrayList<Consumption>();
		Consumption conNew;

		List<Long> waterSupplies = new ArrayList<Long>();

		for (Consumption conOld : oldList) {
			waterSupplies.add(conOld.getWaterSupply().getId());
		}

		generateAverageConsumption(waterSupplies);

		for (Consumption conOld : oldList) {
			conNew = findIfExist(conOld);
			if (conNew == null) {
				conNew = new Consumption();
				//conNew.setCheckingRecord(conOld.getCheckingRecord());
				conNew.setConsumptionState(conOld.getConsumptionState());
				conNew.setMonth(month.getMonthInt());
				conNew.setMunicipalBond(conOld.getMunicipalBond());
				conNew.setPreviousReading(conOld.getCurrentReading());
				conNew.setReadingDate(new Date());
				conNew.setWaterMeterStatus(conOld.getWaterMeterStatus());
				conNew.setWaterSupply(conOld.getWaterSupply());
				conNew.setYear(year);
				conNew.setConsumptionState(this.consumptionStates.get(0));
				conNew.setPreviousAverage(findAverage(conOld.getWaterSupply()));
				
				conNew.setApplyElderlyExemption(conOld.getApplyElderlyExemption());
				conNew.setApplySpecialExemption(conOld.getApplySpecialExemption());
				newConsumptions.add(conNew);
				/*
				 * this.avaragePreviousReading = avaragePreviousReading .add(new
				 * BigDecimal(conOld.getCurrentReading()));
				 */
			} else {
				conNew.setPreviousAverage(findAverage(conNew.getWaterSupply()));
				checkCorrectReading(conNew);
				newConsumptions.add(conNew);
			}
		}
		return newConsumptions;
	}

	public BigDecimal findAverage(WaterSupply ws) {
		for (WaterSupplyAverage wsa : waterSupplyAverages) {
			if (wsa.getWaterSupplyId() == ws.getId()) {
				return wsa.getAmount();
			}
		}
		return BigDecimal.ZERO;
	}

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

	// /////////////////////
	/*
	 * eventos registro de lecturas
	 */
	public void loadRecordReadingsPanel() {
		if (consumptions == null)
			return;
		whereStartRecordReading();
	}

	// /////////////////

	/**
	 * cuando se lanza el panel de registro de lecturas determina donde se quedo
	 * el ultimo registro inrgesado
	 */
	public void whereStartRecordReading() {
		actualReadingPosition = 0;
		for (int i = 0; i < consumptions.size(); i++) {
			if (consumptions.get(i).getCurrentReading() != null) {
				actualReadingPosition++;
			} else {
				i = consumptions.size() + 10;
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

	}

	public void nextConsumption() {
		actualReadingPosition++;
		if (actualReadingPosition <= consumptions.size()) {
			actualConsumption = consumptions.get(actualReadingPosition);
		} else {
			actualReadingPosition--;
		}
	}

	public void previousConsumption() {
		actualReadingPosition--;
		if (actualReadingPosition >= 0) {
			actualConsumption = consumptions.get(actualReadingPosition);
		} else {
			actualReadingPosition++;
		}

	}

	public void loadConsumptionStates() {
		Query query = getPersistenceContext().createNamedQuery(
				"ConsumptionState.findAll");
		consumptionStates = query.getResultList();
	}

	/**
	 * generates the average consumption of the last 3 months
	 * 
	 * @param ws
	 * @return
	 */
	// public BigDecimal generateAverageConsumption(WaterSupply ws) {
	// BigDecimal amount = BigDecimal.valueOf(0);
	// int monthEnd = month.getMonthInt() - 1;
	// int monthStart = monthEnd - 2;
	// int yearAux = year;
	// BigDecimal amountLastYear = BigDecimal.valueOf(0);
	// boolean lastYear = false;
	// if (monthEnd == 0) {
	// yearAux = yearAux - 1;
	// monthEnd = 12;
	// monthStart = monthEnd - 2;
	// } else {
	// // es necesario obtener los datos del año pasado octubre, noviembre
	// // y/o diciembre
	// lastYear = true;
	// if (monthEnd == 1) {
	// amountLastYear = runAverageQuery(ws, yearAux - 1, 11, 12);
	// }
	// if (monthEnd == 2) {
	// amountLastYear = runAverageQuery(ws, yearAux - 1, 12, 12);
	// }
	// }
	// amount = runAverageQuery(ws, yearAux, monthStart, monthEnd);
	// if (lastYear) {
	// amount = amount.add(amountLastYear).divide(BigDecimal.valueOf(2));
	// }
	// return amount;
	// }
	//

	public List<WaterSupplyAverage> runAverageQuery(List<Long> ws, int year,
			int monthStart, int monthEnd) {
		if (ws.size() == 0)
			return new ArrayList<WaterSupplyAverage>();
		if (monthStart == -1 || monthStart == 0) {
			monthStart = 1;
		}
		Query query = this.getEntityManager().createNamedQuery(
				"Consumption.amountAverageByWaterSupply");
		query.setParameter("year", year);
		query.setParameter("monthStart", monthStart);
		query.setParameter("monthEnd", monthEnd);
		query.setParameter("waterSupplyIds", ws);
		return query.getResultList();
	}

	List<Consumption> currentsConsumptions;

	public void findSelectMonthRecordReadings() {
		Query query = this.getEntityManager().createNamedQuery(
				"Consumption.findByYearMonth");
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
					// consumptions.set(pos, conNew);
				} else {
					joinTransaction();
					getEntityManager().flush();
					raiseAfterTransactionSuccessEvent();
				}
				// addAvarageNewConsumption();
			}
		}
		return "ready";
	}

	/**
	 * checks if the current reading is correct
	 * 
	 * @param conNew
	 * @return
	 */
	public Boolean checkCorrectReading(Consumption conNew) {
		Boolean isChecked = false;
		WaterMeter actualWaterMeter = findWaterMeter(conNew.getWaterSupply());

		if (conNew.getCurrentReading() != null) {
			if (conNew.getCurrentReading() > -1) {
				if (consumptionStates == null)
					loadConsumptionStates();
				
				if (conNew.getPreviousReading() > conNew.getCurrentReading()) {

					int verificacion = checkLowerConsumption(actualWaterMeter,
							conNew);

					
					System.out
							.println(":::::::::::::==========>>>> la verificacion es "
									+ verificacion);
					if (verificacion > 0) {
						conNew.setDifferenceInReading(verificacion
								+ conNew.getCurrentReading());
						conNew.setAmount(new BigDecimal(conNew
								.getDifferenceInReading()));
						conNew.setConsumptionState(this.consumptionStates
								.get(2));
						conNew.setIsValidReading(true);
						isChecked = true;
						conNew.setErrorMessage("");
					} else {
						conNew.setIsValidReading(false);
						conNew.setErrorMessage("Revisar el consumo, no debe ser menor");
						conNew.setDifferenceInReading(null);
						conNew.setAmount(null);
					}
				} else {

					System.out.println("ZZZZZZZZZZ "
							+ conNew.getCurrentReading());

					conNew.setDifferenceInReading(conNew.getCurrentReading()
							- conNew.getPreviousReading());
					conNew.setAmount(new BigDecimal(conNew
							.getDifferenceInReading()));
					conNew.setConsumptionState(this.consumptionStates.get(2));
					conNew.setIsValidReading(true);
					isChecked = true;
					conNew.setErrorMessage("");
				}

			} else {
				conNew.setIsValidReading(false);
				conNew.setErrorMessage("No números negativos");
				conNew.setDifferenceInReading(null);
				conNew.setAmount(null);
			}
		} else {
			conNew.setIsValidReading(false);
			conNew.setErrorMessage("Debe ingresar un valor");
			conNew.setDifferenceInReading(null);
			conNew.setAmount(null);
		}

		return isChecked;
	}

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
	public int checkLowerConsumption(WaterMeter actualWaterMeter,
			Consumption conNew) {
		int number = 0;
		if (actualWaterMeter != null) {
			String val = "";
			for (int i = 0; i < actualWaterMeter.getDigitsNumber(); i++) {
				val = val + "9";
			}
			number = Integer.parseInt(val);
			int resultado = number - conNew.getPreviousReading().intValue();
			if (resultado <= 200) {
				return resultado;
			}
			return 0;
		} else {
			return 0;
		}
	}

	public WaterMeter findWaterMeter(WaterSupply ws) {
		Query q = this.getEntityManager().createNamedQuery(
				"WaterMeter.findByWaterSupplyActualMeter");
		q.setParameter("active", new Boolean(true));
		q.setParameter("wsId", ws.getId());
		if (q.getResultList().size() > 0) {
			WaterMeter wm=(WaterMeter) q.getResultList().get(0);
			System.out.println(".......................... "+wm.getSerial()+" ....... "+q.getResultList().size());
			return (WaterMeter) q.getResultList().get(0);
		} else {
			return null;
		}
	}

	// //////////////////////////////////////
	/**
	 * saves the new reading
	 */
	public void saveRecordReadingPanel() {
		Consumption conNew = actualConsumption;
		// int pos = consumptions.indexOf(conNew);
		if (checkCorrectReadingPanel(conNew)) {
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
			nextConsumption();
			// addAvarageNewConsumption();
		}
	}

	/**
	 * checks if the current reading is correct
	 * 
	 * @param conNew
	 * @return
	 */
	public Boolean checkCorrectReadingPanel(Consumption conNew) {
		conNew = actualConsumption;
		Boolean isChecked = false;
		// falta verificar el numero dedigitos del medidor.. no existen aun
		// medidores
		if (conNew.getCurrentReading() != null) {
			if (conNew.getCurrentReading() > -1) {
				conNew.setDifferenceInReading(conNew.getCurrentReading()
						- conNew.getPreviousReading());
				conNew.setAmount(new BigDecimal(conNew.getDifferenceInReading()));
				conNew.setConsumptionState(this.consumptionStates.get(2));
				conNew.setIsValidReading(true);
				isChecked = true;
				conNew.setErrorMessage("");
			} else {
				conNew.setIsValidReading(false);
				conNew.setErrorMessage("No números negativos");
			}
		} else {
			conNew.setIsValidReading(false);
			conNew.setErrorMessage("Debe ingresar un valor");
		}
		/**
		 * consumptions.remove(position); consumptions.add(position, conNew);
		 */
		return isChecked;
	}

	// /////////////////////////////////////

	public Route getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setInstaceReadingMan(Person person) {
		this.readingMan = person;
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
		this.readingMan = null;
	}

	public void search() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident1 = (Resident) query.getSingleResult();
			this.readingMan = (Person) resident1;
			// this.concession.setResident();
			this.resident = resident1;
		} catch (Exception e) {
			this.readingMan = null;
			this.resident = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {

		UIComponent component = event.getComponent();
		Resident resident1 = (Resident) component.getAttributes().get(
				"resident");
		this.readingMan = (Person) resident1;
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
	 */
	public String preEmiteWater() {
		if (shouldEmit()) {
			if (waterService == null) waterService = ServiceLocator.getInstance().findResource(WATER_SERVICE_NAME);
			try {
				eo = waterService.calculatePreEmissionOrderWaterConsumption(consumptions, userSession.getFiscalPeriod(),
						userSession.getPerson(), year, month.getMonthInt(), observation);
				if (eo != null) {
					// loadRecordReadings();
					String message = Interpolator.instance().interpolate(
							"La Ruta " + this.getInstance().getName()
									+ " ha sido pre-emitida", new Object[0]);
					facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.INFO,message);
					System.out.println("..................... completo");
					return "complete";
				}
				System.out.println("en el try ..................... nulo");
				return null;
			} catch (Exception e) {
				String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}",new Object[0]);
				facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
				return null;
			}
		} else {
			String message = Interpolator.instance().interpolate("No se han registrado todas las lecturas para la ruta seleccionada",new Object[0]);
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
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
				if (actualConsumptions >= intConsumptions) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

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

	/*
	 * public String getSearchType() { return searchType; }
	 * 
	 * public void setSearchType(String searchType) { this.searchType =
	 * searchType; }
	 * 
	 * public void changeSearchPanel() { newLoad = false; consumptions = null; }
	 */

	public void searchConsumtionByWaterSupply() {

		int monthInt = month.getMonthInt();		
		int yearInt = year;
		System.out.println("water supply>>>>>>>>>ServiceNumber:" + waterSupplyServiceNumber + " year:" + year + " month:" + monthInt + " observacionValor: "+observation);
		Query query = this.getEntityManager().createNamedQuery("Consumption.findByWaterSupplyServiceNumber");
		query.setParameter("year", yearInt);
		query.setParameter("month", monthInt);
		query.setParameter("wsServiceNumber", waterSupplyServiceNumber);
		// consumptions = loadNewConsumtionList(query.getResultList());
		consumptions = query.getResultList();
	}

	public Integer getWaterSupplyServiceNumber() {
		return waterSupplyServiceNumber;
	}

	public void setWaterSupplyServiceNumber(Integer waterSupplyServiceNumber) {
		this.waterSupplyServiceNumber = waterSupplyServiceNumber;
	}
	
	public String preEmiteOneWaterService() {

		if (waterService == null)
			waterService = ServiceLocator.getInstance().findResource(WATER_SERVICE_NAME);
		try {
			System.out.println("preEmiteOneWaterService--->ObservacionValor:"+this.observation);
			eo = waterService.calculatePreEmissionOrderWaterConsumption(
					this.consumptions, userSession.getFiscalPeriod(),
					userSession.getPerson(), year, month.getMonthInt(), observation);
			waterService.saveEmissionOrder(eo, true,findWaterSuppliesIds());
			if (eo != null) {
				// loadRecordReadings();
				//rfarmijosm 2016-06-02
				//guadar el detalle de bloqueo para luego dar la baja
				WaterBlockLog wbl = initializeBlock();
				wbl.setEmissionOrder_id(eo.getId());
				wbl.setResident(eo.getMunicipalBonds().get(0).getResident());
				System.out.println("-------------------->>>>>>>>> "+eo.getMunicipalBonds().get(0).getNumber());
				
				waterService.saveWaterBlockLog(wbl);
				
				
				String message = Interpolator.instance().interpolate(
						"El servicio " + this.waterSupplyServiceNumber
								+ " ha sido pre-emitido", new Object[0]);
				facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.INFO,message);
				isPreEmited = true;
				
				//updateConsumption(waterService.getConsumptionsToUpdate());
				
				return "complete";
			}
			isPreEmited = false;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			isPreEmited = false;
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			return null;
		}
	}
	
	/**
	 * se inicializa con los datos ingresados 
	 * @return
	 */
	public WaterBlockLog initializeBlock(){
		
		WaterBlockLog wbl=new WaterBlockLog();
		wbl.setBlockDetail(this.blockDetail);
		wbl.setBlocker(userSession.getPerson());
		wbl.setIsReEmision(this.isReEmision);
		wbl.setResident(resident);
		wbl.setTramitNumber(this.tramitNumber);
		wbl.setServiceNumber(waterSupplyServiceNumber);
		if(observation!=null){
			wbl.setUnsubscribeBond(Integer.parseInt(observation));	
		}
		return wbl;
	}
	
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

	public Boolean getIsPreEmited() {
		return isPreEmited;
	}

	public void setIsPreEmited(Boolean isPreEmited) {
		this.isPreEmited = isPreEmited;
	}

	public boolean isFirstTime() {
		return isFirstTime;
	}

	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}
	
	/*
	 * Manuel U.
	 */
	
	private String observation = "";

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	/**
	 * rfarmijosm 2016-05-01
	 */
		
	private String blockDetail;
	
	private String tramitNumber;

	/**
	 * identifcar si es re-emision o un bloqueo, en el bloqueo es necesario
	 * solicitar el detalle de bloqueo
	 */
	private Boolean isReEmision = Boolean.TRUE;
	
	public String getBlockDetail() {
		return blockDetail;
	}

	public void setBlockDetail(String blockDetail) {
		this.blockDetail = blockDetail;
	}

	public Boolean getIsReEmision() {
		return isReEmision;
	}

	public void setIsReEmision(Boolean isReEmision) {
		this.isReEmision = isReEmision;
	}

	public String getTramitNumber() {
		return tramitNumber;
	}

	public void setTramitNumber(String tramitNumber) {
		this.tramitNumber = tramitNumber;
	}	
	
	
}
