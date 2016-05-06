package org.gob.gim.concession.facade;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
import ec.gob.gim.consession.model.ConcessionGroup;
import ec.gob.gim.consession.model.ConcessionGroupEmission;
import ec.gob.gim.consession.model.ConcessionItem;
import ec.gob.gim.consession.model.ConcessionPlace;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.adjunct.ConcessionGroupReference;

@Stateless(name = "ConcessionGroupService")
public class ConcessionGroupServiceBean implements ConcessionGroupService {

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

	
	
	public EmissionOrder generateEmissionOrder(ConcessionGroup cg,
			List<ConcessionItem> items, FiscalPeriod f, Person per, int year, int month, String descriptionPart) throws Exception {
		return calculateEmissionOrderItmes(cg, items, f, per, true, year, month, descriptionPart);
	}
	
	private EmissionOrder calculateEmissionOrderItmes(ConcessionGroup cg, List<ConcessionItem> items,
			FiscalPeriod f, Person p, boolean preEmit, int year, int month,String descriptionPart) throws Exception {
		
		//Calendar cServiceDate = Calendar.getInstance();
		//cServiceDate.set(year, (month-1), 01);
		
		Date serviceDate=createServiceDate(year, month);
		Date emisionPeriod = findEmisionPeriod();
		
		/*Entry entry = null;
		entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_WATER_SERVICE_TAX");*/

		EmissionOrder emissionOrder = new EmissionOrder();
		//
		//emissionOrder.setServiceDate(cServiceDate.getTime());
		emissionOrder.setServiceDate(serviceDate);
		
		emissionOrder.setDescription("Concesiones: "+cg.getName()+ ", "+descriptionPart + ", año/mes: " + year + "-" + new SimpleDateFormat("MMMM").format(serviceDate).toUpperCase());
		emissionOrder.setEmisor(p);		
		
		
		MunicipalBondStatus mbs = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");		
		ConcessionGroupReference cgr;		
		for(ConcessionItem ci: items){
			ConcessionPlace cp = ci.getPlace();
			if (cp.getCurrentContract() != null) {
								
				EntryValueItem entryValueItem = new EntryValueItem();
				entryValueItem.setDescription(emissionOrder.getDescription());
				//entryValueItem.setMainValue(ci.getAmount());
				entryValueItem.setServiceDate(serviceDate);
				entryValueItem.setReference("");
				
				Entry entry = cp.getConsessionPlaceType().getEntry();
				
				Resident person = cp.getCurrentContract().getSubscriber();
				
				MunicipalBond mb = revenueService.createMunicipalBond(person, entry, f, entryValueItem, true, ci );
				
				cgr = new ConcessionGroupReference();
				//cgr.setCode("Concesión");
				cgr.setConcessionGroupName(cp.getConcessionGroup().getName());
				cgr.setConcessionItem(ci);
				cgr.setConcessionPlace(cp);
				cgr.setConcessionPlaceNumber(cp.getName());				
				cgr.setConcessionPlaceOrder(cp.getInternalOrder());
				cgr.setConcessionPlaceType(cp.getConsessionPlaceType().getName());
				cgr.setProductType(ci.getProductType().getName());
				
				mb.setAdjunct(cgr);
				// end Adjunt
				mb.setReference(ci.getConcept());
				mb.setDescription(ci.getExplanation());
				//mb.setAddress(ci.getAddress());
				mb.setBondAddress(ci.getAddress());
				mb.setServiceDate(serviceDate);
				mb.setCreationTime(new Date());
				//arriendo-id_mercado-id_local
				mb.setGroupingCode(cp.getId()+" - "+ci.getId());
				
				mb.setMunicipalBondStatus(mbs);
	
				mb.setBase(ci.getAmount()); // cantidad de consumo
				// mb.setGroupingCode(pro.getCadastralCode());
				
				mb.setOriginator(p);
				mb.setTimePeriod(entry.getTimePeriod());
				mb.calculateValue();
				//mb.setBondAddress(address);
				mb.setEmisionPeriod(emisionPeriod);
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
	
	private Date createServiceDate(int year, int month){
		Date today = new Date();
		/*
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		return calendar.getTime();*/
		
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
	
	public void saveEmissionOrder(EmissionOrder emissionOrder, ConcessionGroupEmission cgEmission, Boolean preEmit) {
		if (preEmit){
			entityManager.persist(emissionOrder);
			entityManager.persist(cgEmission);
			entityManager.flush();
			updateConcessionItems(emissionOrder);
		}
	}
	/**
	 * 
	 * @param emissionOrder
	 */
	private void updateConcessionItems(EmissionOrder emissionOrder){
		String sql="";
		for(MunicipalBond mb:emissionOrder.getMunicipalBonds()){
			ConcessionGroupReference cgr=(ConcessionGroupReference) mb.getAdjunct();
			ConcessionItem ci = cgr.getConcessionItem();
			sql = sql+"update ConcessionItem set municipalbond_id = "+mb.getId()+" where id="+ci.getId()+";\n";
		}
		System.out.println(sql);
		Query q=entityManager.createNativeQuery(sql);
		q.executeUpdate();
		System.out.println("termina ahi>>>>>>>>>>>>>>>><<");
	}
}
