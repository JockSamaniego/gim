package org.gob.gim.waterservice.facade;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.FiscalPeriodService;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.EntryService;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.CompensationReceipt;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.adjunct.WaterServiceReference;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.MaintenanceEntryDTO;
import ec.gob.gim.waterservice.model.Route;
import ec.gob.gim.waterservice.model.RoutePreviewEmission;
import ec.gob.gim.waterservice.model.WaterBlockLog;

@Stateless(name = "WaterService")
public class WaterServiceBean implements WaterService {

	@EJB
	MunicipalBondService municipalBondService;

	@EJB
	EntryService entryService;

	@EJB
	ResidentService residentService;

	@EJB
	FiscalPeriodService fiscalPeriodService;

	@EJB
	RevenueService revenueService;

	@EJB
	SystemParameterService systemParameterService;

	@EJB
	CrudService crudService;

	@PersistenceContext
	EntityManager entityManager;
	
	public List<String> serviceToUpdate;

	private List<MaintenanceEntryDTO> findTotalMaintenanceEntryByWaterSupply(
			List<Long> waterSuppliesIds) {
		Query query = entityManager
				.createNamedQuery("MaintenanceEntry.SumValuesByWaterSupply");
		query.setParameter("waterSuppliesIds", waterSuppliesIds);
		return query.getResultList();
	}

	private MaintenanceEntryDTO findMaintenanceEntryDTOByWaterSupplyId(
			Long waterSupplyId, List<MaintenanceEntryDTO> maintenanceEntryDTOs) {
		for (MaintenanceEntryDTO m : maintenanceEntryDTOs) {
			if (m.getId().equals(waterSupplyId))
				return m;
		}
		return null;
	}

	private Map<String, Exemption> convertToMap(List<Exemption> exemptions) {
		Map<String, Exemption> results = new HashMap<String, Exemption>();

		for (Exemption ex : exemptions) {
			if (ex.getResident() != null)
				results.put(ex.getResident().getIdentificationNumber(), ex);
		}

		return results;
	}

	List<Consumption> consumptionsToUpdate;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmissionOrder buildEmissionOrder(List<Consumption> consumptions,
			FiscalPeriod f, Person p, boolean preEmit, int year, int month, String observation)
			throws Exception {
		consumptionsToUpdate = new ArrayList<Consumption>();

		Calendar cServiceDate = Calendar.getInstance();
		cServiceDate.set(year, (month - 1), 01);

		Route r = consumptions.get(0).getWaterSupply().getRoute();

		Date emisionPeriod = findEmisionPeriod();

		Entry entry = null;
		entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_WATER_SERVICE_TAX");

		TaxpayerRecord institution = municipalBondService
				.findTaxpayerRecord(entry.getId());

		// List<FiscalPeriod> fiscalPeriods =
		// fiscalPeriodService.findCurrent(cServiceDate.getTime());

		// List<Exemption> exemptions =
		// municipalBondService.findExemptionsByFiscalPeriod(fiscalPeriods.get(0).getId());

		// Map<String,Exemption> exempMap = convertToMap(exemptions);

		String monthYear = new SimpleDateFormat("MMMM").format(
				cServiceDate.getTime()).toUpperCase()
				+ "-" + year;

		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(cServiceDate.getTime());
		emissionOrder.setDescription(entry.getName()
				+ ":  Ruta "
				+ r.getName()
				+ ", "
				+ "año/mes: "
				+ year
				+ "-"
				+ new SimpleDateFormat("MMMM").format(cServiceDate.getTime())
						.toUpperCase());
		emissionOrder.setEmisor(p);

		WaterServiceReference wsr;
		BigDecimal amount;
		String code = "";
		String serviceCode = "";
		String waterMeter = "";
		String address = "";

		MunicipalBondStatus mbs = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");

		List<Long> waterSupplies = new ArrayList<Long>();

		ConsumptionState preEmitted = findPreEmittedConsumptionState();
		Date now = Calendar.getInstance().getTime();
		
		 //@author macartuche 
	    //@date 2016-06-27 11:42 
	    //@tag InteresCeroInstPub 
	    //No realizar el calculo de interes para instituciones publicas     
	    //Map<String, BigDecimal> interestByServiceCode = new HashMap<String, BigDecimal>(); 

		for (Consumption c : consumptions) {
			waterSupplies.add(c.getWaterSupply().getId());
			String serviceNumber = c.getWaterSupply().getServiceNumber()
					.toString();
			
			//@author macartuche 
		      //@date 2016-06-27 11:42 
		      //@tag InteresCeroInstPub 
		      //No realizar el calculo de interes para instituciones publicas 
		      //BigDecimal interestSUM = findInterestByServiceCode(serviceNumber); 
		      //interestByServiceCode.put(serviceNumber, interestSUM); 
		} 

		List<MaintenanceEntryDTO> maintenanceEntryDTOs = findTotalMaintenanceEntryByWaterSupply(waterSupplies);

		// sacar todos los interes por el servicio => fijarse en watersupplies
		Resident person;
		serviceToUpdate = new ArrayList<String>();

		for (Consumption c : consumptions) {

			if (!c.getWaterSupply().getWaterSupplyCategory().getName()
					.equals("CATEGORÍA 0")) {

				if (consumptions.size() == 1) {
					String service = c.getWaterSupply().getServiceNumber()
							.toString();
					emissionOrder.setDescription(entry.getName()
							+ ":  Servicio " + service + ", " + "año/mes: "
							+ year + "/" + month);
				}

				amount = c.getAmount();
				serviceCode = c.getWaterSupply().getServiceNumber().toString();
				waterMeter = c.getWaterSupply().getWaterMeters().get(0)
						.getSerial();
				code = serviceCode + " - " + waterMeter;
				address = c.getWaterSupply().getNcalle() + " "
						+ c.getWaterSupply().getNcasa();

				c.setHasPreEmit(new Boolean(true));
				c.setConsumptionState(preEmitted);

				EntryValueItem entryValueItem = new EntryValueItem();
				entryValueItem.setDescription(emissionOrder.getDescription());
				entryValueItem.setMainValue(amount);
				entryValueItem.setServiceDate(cServiceDate.getTime());
				entryValueItem.setReference("");

				// todo verifcar el tiempo de vigencia de la emision de agua
				// potable
				entryValueItem.setExpirationDate(calculateExpirationDate(now,
						60));

				// Resident person =
				// c.getWaterSupply().getContract().getSubscriber();
				person = c.getWaterSupply().getRecipeOwner();

				MaintenanceEntryDTO maintenanceEntryDTO = findMaintenanceEntryDTOByWaterSupplyId(
						c.getWaterSupply().getId(), maintenanceEntryDTOs);

				// start Adjunt
				wsr = new WaterServiceReference();
				wsr.setMaintenanceEntryDTO(maintenanceEntryDTO);
				// wsr.setExemption(exempMap.get(person.getIdentificationNumber()));
				wsr.setCode(code);

				// wsr.setServiceNumber(serviceCode+" / "+monthYear);
				// wsr.setServiceAddress(c.getWaterSupply().getNcalle());

				wsr.setConsumptionAmount(amount);
				wsr.setConsumptionCurrentReading(c.getCurrentReading());
				wsr.setConsumptionPreviousReading(c.getPreviousReading());
				wsr.setWaterMeterNumber(waterMeter);
				wsr.setWaterMeterStatus(c.getWaterMeterStatus().getName());
				wsr.setWaterSupplyCategory(c.getWaterSupply()
						.getWaterSupplyCategory().getName());
				wsr.setRouteName(r.getName() + " - "
						+ c.getWaterSupply().getRouteOrder());
				wsr.setConsumption(c);
				wsr.setRoute(r);

				/********************/
				// macartuche ->pasando el interes acumulado					
				//@author macartuche 
		        //@date 2016-06-27 11:42 
		        //@tag InteresCeroInstPub 
		        //No realizar el calculo de interes para instituciones publicas 
		         
		        //BigDecimal interestList = interestByServiceCode.get(serviceCode); 
		        //if(interestList!=null){ 
		        //  if (interestList.compareTo(BigDecimal.ZERO) == 1) { 
		        //    serviceToUpdate.add(serviceCode); 
		        //  } 
		        //  wsr.setAccruedInterest(interestList); 
		        //} 
			

				if (c.getWaterSupply().getApplyElderlyExemption()) {
					wsr.setExemptionType("TERCERA EDAD");
					c.setApplyElderlyExemption(c.getWaterSupply()
							.getApplyElderlyExemption());
				} else if (c.getWaterSupply().getApplySpecialExemption()) {
					wsr.setExemptionType("ESPECIAL");
					c.setApplySpecialExemption(c.getWaterSupply()
							.getApplySpecialExemption());
				} else {
					wsr.setExemptionType("NINGUNO");
				}

//				 MunicipalBond mb = revenueService.createMunicipalBond(person,
//				 entry, f, entryValueItem, true, c, wsr);

				MunicipalBond mb = municipalBondService.createInstance(person,
						f, entry, amount, now, now,
						entryValueItem.getServiceDate(),
						entryValueItem.getExpirationDate(),
						entryValueItem.getReference(),
						emissionOrder.getDescription(), institution);

				mb.setAdjunct(wsr);
				// end Adjunt

				mb.setServiceDate(cServiceDate.getTime());
				System.out.println("buildEmisionOrder---> "+observation);
				if(observation!=null && !observation.equals("")){
					System.out.println("buildEmisionOrder--->!null:"+observation);
					mb.setDescription(wsr.toString()+", Número Obligación: "+observation);
				}
				else{
					System.out.println("buildEmisionOrder--->else:"+observation);
					mb.setDescription(wsr.toString());
				}				
				
				mb.setCreationTime(now);
				mb.setGroupingCode(serviceCode);

				mb.setMunicipalBondStatus(mbs);
				mb.setBase(amount); // cantidad de consumo
				// mb.setGroupingCode(pro.getCadastralCode());

				mb.setOriginator(p);
				mb.setTimePeriod(entry.getTimePeriod());
				// mb.calculateValue();
				mb.setBondAddress(address);
				mb.setEmisionPeriod(emisionPeriod);
				c.setMunicipalBond(mb);

				consumptionsToUpdate.add(c);

				emissionOrder.add(mb);

			}
		}


		if (emissionOrder.getMunicipalBonds() != null
				&& emissionOrder.getMunicipalBonds().size() > 0) {
			municipalBondService.createItemsToMunicipalBond(
					emissionOrder.getMunicipalBonds(), null, true, true, false);
		}

		return emissionOrder;
	}

	/**
	 * @macartuche Permite encontrar la suma de los intereses almacenados en
	 *             CompensationReceipt
	 * 
	 * @param serviceCode
	 * @return
	 */
	//@author macartuche 
	//@date 2016-06-27 17:51 
	//@tag InteresCeroInstPub 
	//No realizar el calculo de interes para instituciones publicas 
//  private BigDecimal findInterestByServiceCode(String serviceCode) { 
//	    BigDecimal interestSUM = BigDecimal.ZERO; 
//	    Query query = entityManager 
//	        .createQuery("Select SUM(cr.residualInterest) from CompensationReceipt cr " 
//	            + "where cr.groupingCode=:serviceCode and cr.available=:available and cr.isPaid=:isPayed"); 
//	    query.setParameter("serviceCode", serviceCode); 
//	    query.setParameter("available", Boolean.TRUE); 
//	    //agregar campo para buscar solo los pagados 
//	    //macartuche 
//	    //2016-02-20 09:45 
//	    query.setParameter("isPayed", Boolean.TRUE); 
// 
//	    try { 
//	      interestSUM = (BigDecimal) query.getSingleResult(); 
//	    } catch (NoResultException e) { 
//	      System.out.println("No hay intereses para el serviceCode " 
//	          + serviceCode); 
//	    } 
// 
//	    return interestSUM; 
//  } 


	private Date findEmisionPeriod() {
		Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		return calendar.getTime();

	}

	private Date calculateExpirationDate(Date date, Integer daysNumber) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DATE, daysNumber);
		return now.getTime();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveEmissionOrder(EmissionOrder emissionOrder, Boolean preEmit,
			List<Long> waterSupplyIds) {
		if (preEmit) {
			entityManager.persist(emissionOrder);
			updateToEmittedStatus(waterSupplyIds);
			//macartuche
			
			//@author macartuche 
		    //@date 2016-06-27 11:42 
		    //@tag InteresCeroInstPub 
		    //No realizar el calculo de interes para instituciones publicas 
		       
		    //updateCompensationReceipt();
		}
		

		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveEmissionOrder(EmissionOrder emissionOrder, Boolean preEmit) {
		if (preEmit) {
			entityManager.persist(emissionOrder);
		}
	}

	/**
	 * El metodo saveEmissionOrder tiene una anotacion
	 * 
	 * @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) el cuale es
	 *                                                              necesario
	 *                                                              para la
	 *                                                              emsion de
	 *                                                              agua, pero
	 *                                                              para la
	 *                                                              emision de
	 *                                                              presupuesto
	 *                                                              no
	 */
	public void saveEmissionOrderBudget(EmissionOrder emissionOrder,
			Boolean preEmit) {
		if (preEmit) {
			entityManager.persist(emissionOrder);
		}
	}
	
	@Override
	public void saveWaterBlockLog(WaterBlockLog waterBlockLog) {
		entityManager.persist(waterBlockLog);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveEmissionOrderMerchandising(EmissionOrder emissionOrder,
			Boolean preEmit, RoutePreviewEmission rpe, List<Long> waterSupplyIds) {
		if (preEmit) {
			System.out
					.println("el tamanio total es>>>>>>>>>>>>>>>>>>>>>>>>>>>< "
							+ emissionOrder.getMunicipalBonds().size());
			entityManager.persist(emissionOrder);
			entityManager.persist(rpe);
			updateToEmittedStatus(waterSupplyIds);
			// updateConsumption();			
			//@author macartuche 
		    //@date 2016-06-27 11:42 
		    //@tag InteresCeroInstPub 
		    //No realizar el calculo de interes para instituciones publicas 
		       
		    //updateCompensationReceipt(); 
		}
	}

	
	//@author macartuche 
	//@date 2016-06-27 11:42 
	//@tag InteresCeroInstPub 
	//No realizar el calculo de interes para instituciones publicas 
//	private void updateCompensationReceipt(){
//		//macartuche
//		//actualizar todos los codigos de servicio
//		for (String serviceUpdate : serviceToUpdate) {
//			Query q = entityManager
//					.createQuery("Update CompensationReceipt cr "
//							+ "SET cr.available = false "
//							+ "WHERE cr.groupingCode=:groupingCode and cr.isPaid=:isPaid ");
//			q.setParameter("groupingCode", serviceUpdate);
//			//macartuche
//			//2016-02-20 11:48:45
//			//poner no disponibles los que ya se han pagado
//			q.setParameter("isPaid", Boolean.TRUE);
//			q.executeUpdate();
//		}
//	}
	
	private void updateToEmittedStatus(List<Long> waterSupplies) {
		if (waterSupplies == null || waterSupplies.size() == 0)
			return;
		Query query = entityManager
				.createNamedQuery("MaintenanceEntry.setToEmittedStatus");
		query.setParameter("watersupplyIds", waterSupplies);
		query.executeUpdate();
	}

	/*
	 * private void updateConsumption(){ if (consumptionsToUpdate.size() ==
	 * 0)return; //entityManager.getTransaction().begin(); int i=0; for
	 * (Consumption cc : consumptionsToUpdate) { entityManager.merge(cc); if ((i
	 * % 100) == 0) { entityManager.flush(); entityManager.clear(); } i++; }
	 * //entityManager.getTransaction().commit(); }
	 */

	/*
	 * @SuppressWarnings("unchecked") public List<Consumption>
	 * findConsumption(Route r, int year, int month) { Map<String, Object>
	 * parameters = new HashMap<String, Object>(); parameters.put("year", year);
	 * parameters.put("month", month); parameters.put("routeId", r.getId());
	 * List<Consumption> consumptions = crudService.findWithNamedQuery(
	 * "Consumption.findByYearMonth", parameters); if (consumptions != null) {
	 * return consumptions; } return new ArrayList<Consumption>(); }
	 */

	/*
	 * public void savePreEmissionOrder(EmissionOrder e) {
	 * entityManager.create(e); }
	 */

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmissionOrder calculatePreEmissionOrderWaterConsumption(
			List<Consumption> consumptions, FiscalPeriod f, Person p, int year,
			int month, String observation) throws Exception {
		return buildEmissionOrder(consumptions, f, p, true, year, month, observation);
	}

	@SuppressWarnings("unchecked")
	private ConsumptionState findPreEmittedConsumptionState() {
		// List<ConsumptionState> consumptionStates =
		// crudService.findWithNamedQuery("ConsumptionState.findAll");
		return systemParameterService.materialize(ConsumptionState.class,
				"CONSUMPTIONSTATE_ID_PREEMITTED");
	}

	public EmissionOrder reCalculatePreEmissionOrder(
			List<Consumption> consumptions, FiscalPeriod f, Person p)
			throws Exception {
		return calculatePreEmissionOrder(consumptions, f, p, true);
	}

	private EmissionOrder calculatePreEmissionOrder(
			List<Consumption> consumptions, FiscalPeriod f, Person p,
			boolean preEmit) throws Exception {

		Date emisionPeriod = findEmisionPeriod();

		Entry entry = null;
		entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_WATER_SERVICE_TAX");

		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(new Date());

		emissionOrder.setEmisor(p);

		WaterServiceReference wsr;
		String service = "No service";
		String address = "";

		MunicipalBondStatus mbs = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");

		ConsumptionState preEmitted = findPreEmittedConsumptionState();
		Resident person;

		Calendar cServiceDate = Calendar.getInstance();

		// macartuche
		// 2015-09-29		
		//@author macartuche 
	    //@date 2016-06-27 18:00 
	    //@tag InteresCeroInstPub 
	    //No realizar el calculo de interes para instituciones publicas 
	     
	    //Map<String, BigDecimal> interestByServiceCode = new HashMap<String, BigDecimal>(); 
	    //for (Consumption c : consumptions) { 
	    //  String serviceNumber = c.getWaterSupply().getServiceNumber() 
	    //      .toString(); 
	    //  BigDecimal interestSUM = findInterestByServiceCode(serviceNumber); 
	    //  interestByServiceCode.put(serviceNumber, interestSUM); 
	    //}

		serviceToUpdate = new ArrayList<String>();
		for (Consumption c : consumptions) {

			// Calendar cServiceDate = Calendar.getInstance();
			cServiceDate.set(c.getYear(), (c.getMonth() - 1), 01);
			String monthYear = new SimpleDateFormat("MMMM").format(
					cServiceDate.getTime()).toUpperCase()
					+ "-" + c.getYear();

			service = c.getWaterSupply().getServiceNumber().toString();
			c.setHasPreEmit(new Boolean(true));
			c.setConsumptionState(preEmitted);
			address = c.getWaterSupply().getNcalle() + " "
					+ c.getWaterSupply().getNcasa();

			emissionOrder.setDescription(entry.getName() + ":  Servicio "
					+ service + ", " + "año/mes: ");

			EntryValueItem entryValueItem = new EntryValueItem();
			entryValueItem.setDescription(emissionOrder.getDescription());
			entryValueItem.setMainValue(c.getAmount());
			entryValueItem.setServiceDate(cServiceDate.getTime());
			entryValueItem.setReference("");

			// todo verifcar el tiempo de vigencia de la emision de agua potable
			entryValueItem.setExpirationDate(calculateExpirationDate(
					new Date(), 60));

			person = c.getWaterSupply().getRecipeOwner();

			// start Adjunt
			wsr = new WaterServiceReference();
			wsr.setCode(c.getWaterSupply().getServiceNumber() + " - "
					+ c.getWaterSupply().getWaterMeters().get(0).getSerial());
			// wsr.setServiceNumber(c.getWaterSupply().getServiceNumber().toString()+" / "+monthYear);
			// wsr.setServiceAddress(c.getWaterSupply().getNcalle());

			wsr.setConsumptionAmount(c.getAmount());
			wsr.setConsumptionCurrentReading(c.getCurrentReading());
			wsr.setConsumptionPreviousReading(c.getPreviousReading());
			wsr.setWaterMeterNumber(c.getWaterSupply().getWaterMeters().get(0)
					.getSerial());
			wsr.setWaterMeterStatus(c.getWaterMeterStatus().getName());
			wsr.setWaterSupplyCategory(c.getWaterSupply()
					.getWaterSupplyCategory().getName());
			wsr.setRouteName(c.getWaterSupply().getRoute().getName() + " - "
					+ c.getWaterSupply().getRouteOrder());
			wsr.setConsumption(c);
			wsr.setRoute(c.getWaterSupply().getRoute());
//
//			/********************/
//			// macartuche ->pasando el interes acumulado
			
			//@author macartuche 
		    //@date 2016-06-27 18:02 
		    //@tag InteresCeroInstPub 
		    //No realizar el calculo de interes para instituciones publicas 
		       
//		    String serviceCode = c.getWaterSupply().getServiceNumber().toString(); 
//		    BigDecimal interestList = interestByServiceCode.get(serviceCode); 
			
//			System.out.println("Interest una sola emision: "+interestList+" "+serviceCode);
//			//valor mayor a cero
//			if(interestList!=null){
//				if (interestList.compareTo(BigDecimal.ZERO) == 1) {
//					serviceToUpdate.add(serviceCode);
//				}
//				wsr.setAccruedInterest(interestList);
//			}
		
			
			if (c.getWaterSupply().getApplyElderlyExemption()) {
				wsr.setExemptionType("TERCERA EDAD");
				c.setApplyElderlyExemption(c.getWaterSupply()
						.getApplyElderlyExemption());
			} else if (c.getWaterSupply().getApplySpecialExemption()) {
				wsr.setExemptionType("ESPECIAL");
				c.setApplySpecialExemption(c.getWaterSupply()
						.getApplySpecialExemption());
			} else {
				wsr.setExemptionType("NINGUNO");
			}
			// end Adjunt
			// start mb

			MunicipalBond mb = revenueService.createMunicipalBond(person,
					entry, f, entryValueItem, true, c, wsr);		
			
			mb.setDescription(wsr.toString());
			mb.setCreationTime(new Date());
			mb.setGroupingCode(c.getWaterSupply().getServiceNumber().toString());

			mb.setMunicipalBondStatus(mbs);

			mb.setBase(c.getAmount()); // cantidad de consumo
			mb.setServiceDate(cServiceDate.getTime());
			mb.setOriginator(p);
			mb.setTimePeriod(entry.getTimePeriod());
			mb.setBondAddress(address);
			mb.setEmisionPeriod(emisionPeriod);

			// mb.calculateValue();

			mb.setAdjunct(wsr);

			c.setMunicipalBond(mb);

			consumptionsToUpdate.add(c);

			emissionOrder.add(mb);
		}
		emissionOrder.setDescription(entry.getName() + ":  Servicio " + service
				+ ", " + "año/mes: ");

		return emissionOrder;

	}

	public List<Consumption> getConsumptionsToUpdate() {
		return consumptionsToUpdate;
	}

	

}
