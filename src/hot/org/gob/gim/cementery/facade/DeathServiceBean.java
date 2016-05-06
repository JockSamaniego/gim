package org.gob.gim.cementery.facade;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.FiscalPeriodService;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.EntryService;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;

/**
 * @author IML
 *
 */
@Stateless(name="DeathService")
public class DeathServiceBean implements DeathService {
	
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
	
	private EmissionOrder savePreEmissionOrderDeathUnit(Contract c, Unit unit, FiscalPeriod f, Person p, boolean preEmit) throws Exception {
		// TODO Auto-generated method stub
		
		Date currentDate = new Date();
		Entry entry =  systemParameterService.materialize(Entry.class, "ENTRY_ID_RENT_DEATH");		
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(unit.getCurrentDeath().getCurrentContract().getSubscriptionDate());		
		emissionOrder.setDescription(entry.getName() + ": " + unit.getCurrentDeath().getCurrentContract().getSubscriber().getName());
		emissionOrder.setEmisor(p);	
		
		EntryValueItem entryValueItem = new EntryValueItem();  
		entryValueItem.setDescription(emissionOrder.getDescription());
		//entryValueItem.setMainValue(unit.getCurrentDeath().getCurrentContract().);
		entryValueItem.setServiceDate(currentDate);
		entryValueItem.setReference("");
		
		MunicipalBond mb = revenueService.createMunicipalBond(unit.getCurrentDeath().getCurrentContract().getSubscriber(), entry, f, entryValueItem, true, unit);

		
		//MunicipalBond mb = revenueService.createMunicipalBond(d.getCurrentDeath().getCurrentContract().getSubscriber(), f, entry, null, currentDate, null, MunicipalBondType.EMISSION_ORDER, d);
		
		mb.setCreationDate(currentDate);	
		mb.setCreationTime(currentDate);
		mb.setEntry(entry);
		mb.setFiscalPeriod(f);		
		mb.setAddress(unit.getCurrentDeath().getCurrentContract().getSubscriber().getAddresses().toString());
		mb.setOriginator(p);
		mb.setResident(unit.getCurrentDeath().getCurrentContract().getSubscriber());
		mb.setServiceDate(unit.getCurrentDeath().getCurrentContract().getSubscriptionDate());
		mb.setTimePeriod(entry.getTimePeriod());		
		emissionOrder.add(mb);		
			
		if(preEmit){
			mb.setGroupingCode(c.getId().toString());
			savePreEmissionOrder(emissionOrder);
		}
				
		return emissionOrder;

	}
	
	public void savePreEmissionOrder(EmissionOrder e){
		MunicipalBondStatus mbs = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		for(MunicipalBond m : e.getMunicipalBonds()){
			m.setMunicipalBondStatus(mbs);
		}
		if(municipalBondService.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(e.getMunicipalBonds().get(0).getResident().getId(), 
				e.getMunicipalBonds().get(0).getEntry().getId(), e.getMunicipalBonds().get(0).getServiceDate(),e.getMunicipalBonds().get(0).getGroupingCode()) != null) return;
		crudService.create(e);
	}


	public void preEmitDeathUnit(Contract c, Unit d, FiscalPeriod f, Person p)
			throws Exception {
		// TODO Auto-generated method stub
		savePreEmissionOrderDeathUnit(c, d, f, p, true);
		
	}
	
	@Override
	public EmissionOrder calculatePreEmissionDeathUnit(Contract c, Unit d,
			FiscalPeriod f, Person p) throws Exception {
		// TODO Auto-generated method stub
		return savePreEmissionOrderDeathUnit(c, d, f, p, false);
	}
	

}
