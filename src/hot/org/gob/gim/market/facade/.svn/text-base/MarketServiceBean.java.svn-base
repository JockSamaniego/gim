package org.gob.gim.market.facade;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import ec.gob.gim.market.model.Market;
import ec.gob.gim.market.model.MarketEmission;
import ec.gob.gim.market.model.Stand;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.adjunct.MarketReference;

@Stateless(name = "MarketService")
public class MarketServiceBean implements MarketService {
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
	
	private EmissionOrder calculateEmissionOrderStand(Market market, List<Stand> stands,
			FiscalPeriod f, Person p, boolean preEmit, int year, int month,String descriptionPart) throws Exception {
		
		/*Calendar cServiceDate = Calendar.getInstance();
		cServiceDate.set(year, (month-1), 01);*/
		Date serviceDate = createServiceDate(year, month);
		
		Date emisionPeriod = findEmisionPeriod();
		
		/*Entry entry = null;
		entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_WATER_SERVICE_TAX");*/

		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(serviceDate);
		emissionOrder.setDescription("Arriendos: "+market.getName()+ ", "+descriptionPart + ", año/mes: " + year + "-" + new SimpleDateFormat("MMMM").format(serviceDate).toUpperCase());
		emissionOrder.setEmisor(p);
		
		String monthYear=new SimpleDateFormat("MMMM").format(serviceDate).toUpperCase()+"-"+year;
			
		MunicipalBondStatus mbs = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		MarketReference mr;
		for (Stand s : stands) {
			if (s.getCurrentContract() != null) {
								
				EntryValueItem entryValueItem = new EntryValueItem();
				entryValueItem.setDescription(emissionOrder.getDescription());
				//entryValueItem.setMainValue(amount);
				entryValueItem.setServiceDate(serviceDate);
				entryValueItem.setReference("");
				//entryValueItem.setExpirationDate(calculateExpirationDate(new Date(),90));
				Entry entry =s.getStandType().getEntry();
				
				Resident person = s.getCurrentContract().getSubscriber();
				
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx>>>>>>>>>>>>>>>>>>>>><< "+person.getName());
				
				MunicipalBond mb = revenueService.createMunicipalBond(person, entry, f, entryValueItem, true, s );
				
				// start Adjunt
				mr = new MarketReference();
//				mr.setServiceDetail(monthYear);
				mr.setArea(new BigDecimal(s.getArea()));
				mr.setMarket(market);
				mr.setMarketName(market.getName());
				mr.setStand(s);
				mr.setStandName(s.getName());
				mr.setStandNumber(s.getNumber());
				mr.setStandType(s.getStandType().getName());
				mr.setProductType(s.getProductType().getName());
				//mr.setExplanation(s.getExplicacion());
				//mr.setReference(s.getConcepto());
				mb.setAdjunct(mr);
				// end Adjunt
				mb.setReference(s.getConcepto());
				mb.setDescription(s.getExplicacion());
				
				mb.setServiceDate(serviceDate);
				//mb.setDescription(mr.toString());
				mb.setCreationTime(new Date());
				//arriendo-id_mercado-id_local
				mb.setGroupingCode("ARRIENDO-"+s.getMarket().getId()+"-"+s.getId());
				
				mb.setMunicipalBondStatus(mbs);
	
				mb.setBase(s.getValue()); // cantidad de consumo
				// mb.setGroupingCode(pro.getCadastralCode());
				
				mb.setOriginator(p);
				mb.setTimePeriod(entry.getTimePeriod());
				mb.calculateValue();
				//mb.setBondAddress(address);
				mb.setEmisionPeriod(emisionPeriod);
				System.out.println("........... el valor es:  " + mb.getValue());
				emissionOrder.add(mb);
			}
		}
		
		return emissionOrder;
	}
	
	
	private Date findEmisionPeriod(){
		 Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		return calendar.getTime();
		
	}

	private EmissionOrder savePreEmissionOrderRentStand(Contract c, Stand s,
			FiscalPeriod f, Person p, boolean preEmit) throws Exception {
		// TODO Auto-generated method stub

		Date currentDate = new Date();
		Entry entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_RENT_STAND");
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(s.getCurrentContract().getSubscriptionDate());		
		emissionOrder.setDescription(entry.getName() + " - Puesto: " + s.getName() + ", Area: "
				+ s.getArea());
		emissionOrder.setEmisor(p);

		
		EntryValueItem entryValueItem = new EntryValueItem();  
		entryValueItem.setDescription(emissionOrder.getDescription());
		entryValueItem.setMainValue(s.getValue());
		entryValueItem.setServiceDate(currentDate);
		entryValueItem.setReference("");
		
		MunicipalBond mb = revenueService.createMunicipalBond(s.getCurrentContract().getSubscriber(), entry, f, entryValueItem, true, s);
		
		/*
		MunicipalBond mb = revenueService.createMunicipalBond(s
				.getCurrentContract().getSubscriber(), f, entry, null,
				currentDate, null, MunicipalBondType.EMISSION_ORDER, s);
*/
		mb.setCreationDate(currentDate);
		mb.setCreationTime(currentDate);
		mb.setEntry(entry);
		mb.setFiscalPeriod(f);
		// mb.setAddress(s.getAddress());
		mb.setOriginator(p);
		mb.setResident(s.getCurrentContract().getSubscriber());
		mb.setServiceDate(s.getCurrentContract().getSubscriptionDate());
		mb.setTimePeriod(entry.getTimePeriod());
		mb.calculateValue();
		emissionOrder.add(mb);

		if (preEmit) {
			mb.setGroupingCode(c.getId().toString());
			savePreEmissionOrder(emissionOrder);
		}

		return emissionOrder;

	}

	//
	private EmissionOrder generatePreEmissionOrderRentStand(Contract c,
			Stand s, FiscalPeriod f, Person p, boolean preEmit, BigDecimal value)
			throws Exception {
		// TODO Auto-generated method stub

		Date currentDate = new Date();
		Entry entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_RENT_STAND");
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(s.getCurrentContract().getSubscriptionDate());		
		emissionOrder.setDescription(entry.getName() + " - Puesto: " + s.getName() + ", Area: "
				+ s.getArea());
		emissionOrder.setEmisor(p);

		/*
		 * MunicipalBond mb = revenueService.createMunicipalBond(s
		 * .getCurrentContract().getSubscriber(), f, entry, null, currentDate,
		 * null, MunicipalBondType.EMISSION_ORDER, s);
		 */
		EntryValueItem entryValueItem = new EntryValueItem();  
		entryValueItem.setDescription(emissionOrder.getDescription());
		entryValueItem.setMainValue(s.getValue());
		entryValueItem.setServiceDate(currentDate);
		entryValueItem.setReference("");
		
		MunicipalBond mb = revenueService.createMunicipalBond(s.getCurrentContract().getSubscriber(), entry, f, entryValueItem, true);		
		/*
		MunicipalBond mb = revenueService.createMunicipalBond(s
				.getCurrentContract().getSubscriber(), f, entry, value,
				currentDate, MunicipalBondType.EMISSION_ORDER);
		*/
		mb.setCreationDate(currentDate);
		mb.setCreationTime(currentDate);
		mb.setEntry(entry);
		mb.setFiscalPeriod(f);
		// mb.setAddress(s.getAddress());
		mb.setOriginator(p);
		mb.setResident(s.getCurrentContract().getSubscriber());
		mb.setServiceDate(s.getCurrentContract().getSubscriptionDate());
		mb.setTimePeriod(entry.getTimePeriod());
		emissionOrder.add(mb);

		if (preEmit) {
			mb.setGroupingCode(c.getId().toString());
			savePreEmissionOrder(emissionOrder);
		}

		return emissionOrder;

	}
	
	private Date createServiceDate(int year, int month){
		Date today = new Date();		
		System.out.println("------Fecha actual--------------");
        System.out.println(new SimpleDateFormat("yyyy MMMM dd").format(today));

        Calendar todayC = Calendar.getInstance();
        todayC.setTime(today);
        int todayMonth=todayC.get(Calendar.DAY_OF_MONTH);
        System.out.println(todayMonth);
                        
        Calendar serviceDate = Calendar.getInstance();
        serviceDate.setTime(today);
        serviceDate.set(Calendar.YEAR, year);
        serviceDate.set(Calendar.MONTH, month-1);
        serviceDate.set(Calendar.DAY_OF_MONTH, 1);
        int serviceDateMaxDay=serviceDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("------Fecha sin revision de dias--------------");
        System.out.println(new SimpleDateFormat("yyyy MMMM dd").format(serviceDate.getTime()));
        
        System.out.println(todayMonth +"    "+serviceDateMaxDay);
                
        if (todayMonth <= serviceDateMaxDay) {
            serviceDate.set(Calendar.DAY_OF_MONTH, todayMonth);
        }else{
            serviceDate.set(Calendar.DAY_OF_MONTH, serviceDateMaxDay);
        }
        System.out.println("------Fecha final--------------");
        System.out.println(new SimpleDateFormat("yyyy MMMM dd").format(serviceDate.getTime()));

        return serviceDate.getTime();
	}

	public void savePreEmissionOrder(EmissionOrder e) {
		System.out.println("Resident: "
				+ e.getMunicipalBonds().get(0).getResident().getName());
		System.out.println("ServiceDate: "
				+ e.getMunicipalBonds().get(0).getServiceDate());
		crudService.create(e);
	}
	
	@Override
	public void updateStand(Stand e) throws Exception {
		crudService.update(e);		
	}

	@Override
	public void preEmitRentStand(Contract c, Stand s, FiscalPeriod f, Person p)
			throws Exception {
		savePreEmissionOrderRentStand(c, s, f, p, true);

	}

	@Override
	public EmissionOrder calculatePreEmissionRentStand(Contract c, Stand s,
			FiscalPeriod f, Person p) throws Exception {
		return savePreEmissionOrderRentStand(c, s, f, p, false);
	}

	@Override
	public EmissionOrder generatePreEmissionRentStand(Contract c, Stand s,
			FiscalPeriod f, Person p, BigDecimal value) throws Exception {
		return generatePreEmissionOrderRentStand(c, s, f, p, false, value);
	}


	@Override
	public EmissionOrder generateEmissionOrder(Market market,
			List<Stand> stands, FiscalPeriod f, Person per, int year, int month, String descriptionPart) throws Exception {
		return calculateEmissionOrderStand(market, stands, f, per, true, year, month, descriptionPart);
	}
	
	public void saveEmissionOrder(EmissionOrder emissionOrder, Boolean preEmit, MarketEmission me) {
		if (preEmit){
			entityManager.persist(emissionOrder);
			entityManager.persist(me);
		}
	}
	

	/*@Override
	public void emitStands(Market market, List<Stand> stands, FiscalPeriod f,
			Person per) throws Exception {
		calculatePreEmissionOrderRentStand
		
	}*/

	

}
