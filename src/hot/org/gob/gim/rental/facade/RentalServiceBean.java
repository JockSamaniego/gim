/**
 * 
 */
package org.gob.gim.rental.facade;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.gob.gim.parking.action.TicketCashClosingList;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.EntryService;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.Tax;
import ec.gob.gim.income.model.TaxRate;
import ec.gob.gim.parking.model.ParkingRent;
import ec.gob.gim.parking.model.Ticket;
import ec.gob.gim.rental.model.Space;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryDefinition;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;

/**
 * @author wilman
 *
 */
@Stateless(name="RentalService")
public class RentalServiceBean implements RentalService {
	
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
	
	private EmissionOrder savePreEmissionOrderRentSpace(Contract c,Space s, FiscalPeriod f, Person p, boolean preEmit) throws Exception {
		// TODO Auto-generated method stub
		
		Date currentDate = new Date();
		Entry entry = null;
		
		if(s.getSpaceType().getName().equalsIgnoreCase("ANTENA")){
			entry =  systemParameterService.materialize(Entry.class, "ENTRY_ID_RENT_ANTENNA");
		}
		
		if(s.getSpaceType().getName().equalsIgnoreCase("VALLA")){
			entry =  systemParameterService.materialize(Entry.class, "ENTRY_ID_RENT_FENCE");
		}
		
		if(s.getSpaceType().getName().equalsIgnoreCase("ROTULO") || s.getSpaceType().getName().equalsIgnoreCase("PALETA")){
			entry =  systemParameterService.materialize(Entry.class, "ENTRY_ID_RENT_LABEL_PALETTE");
		}
		
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(s.getCurrentContract().getSubscriptionDate());		
		emissionOrder.setDescription(entry.getName() + ": " + s.getAddress());
		emissionOrder.setEmisor(p);	
		
		EntryValueItem entryValueItem = new EntryValueItem(); 
		entryValueItem.setDescription(emissionOrder.getDescription());
		//entryValueItem.setMainValue();
		entryValueItem.setServiceDate(currentDate);
		entryValueItem.setReference("");
		
		MunicipalBond mb = revenueService.createMunicipalBond(s.getCurrentContract().getSubscriber(), entry, f, entryValueItem,true, s);
		
		
		//MunicipalBond mb = revenueService.createMunicipalBond(s.getCurrentContract().getSubscriber(), f, entry, null, currentDate, null, MunicipalBondType.EMISSION_ORDER, s);
		
		mb.setCreationDate(currentDate);	
		mb.setCreationTime(currentDate);
		mb.setEntry(entry);
		mb.setFiscalPeriod(f);		
		mb.setAddress(s.getAddress());
		mb.setOriginator(p);
		mb.setResident(s.getCurrentContract().getSubscriber());
		mb.setServiceDate(s.getCurrentContract().getSubscriptionDate());
		mb.setTimePeriod(entry.getTimePeriod());		
		emissionOrder.add(mb);		
			
		if(preEmit){
			mb.setGroupingCode(c.getId().toString());
			savePreEmissionOrder(emissionOrder);
		}
		return emissionOrder;

	}
	
	public String getMonthName(Date d){
		return new DateFormatSymbols().getMonths()[d.getMonth()];		
	}
	
	private EmissionOrder savePreEmissionOrderParkingRent(ParkingRent parkingRent, FiscalPeriod f, boolean preEmit, Long payments) throws Exception {
		
		Date currentDate = new Date();
		Entry entry =  parkingRent.getParkingLot().getEntry(); 		
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(parkingRent.getContract().getSubscriptionDate());		
		emissionOrder.setDescription(entry.getName());
		emissionOrder.setEmisor((Person)parkingRent.getManager());	
		
		
		MunicipalBond mb = null;		
		Date aux = parkingRent.getContract().getSubscriptionDate();
		GregorianCalendar g1 = (GregorianCalendar) GregorianCalendar.getInstance();
		g1.setTime(parkingRent.getContract().getSubscriptionDate());
		GregorianCalendar g2 = (GregorianCalendar) GregorianCalendar.getInstance();
		g2.setTime(parkingRent.getContract().getExpirationDate());
		
		
		double base = 0;
		
		Calendar ca = Calendar.getInstance();
		
		for (int i = 0; i < payments; i++){
			EntryValueItem entryValueItem = new EntryValueItem(); 
			entryValueItem.setDescription(emissionOrder.getDescription());
			entryValueItem.setMainValue(new BigDecimal(base));
			entryValueItem.setServiceDate(currentDate);
			entryValueItem.setReference(parkingRent.getReference());
			
			mb = revenueService.createMunicipalBond(parkingRent.getContract().getSubscriber(), entry, f, entryValueItem, true, parkingRent);
			
			//mb = revenueService.createMunicipalBond(parkingRent.getContract().getSubscriber(), f, entry, new BigDecimal(base), currentDate,parkingRent.getReference(), MunicipalBondType.EMISSION_ORDER, parkingRent);			
			mb.setCreationDate(currentDate);	
			mb.setCreationTime(currentDate);
			mb.setEntry(entry);
			mb.setFiscalPeriod(f);		
			mb.setAddress(parkingRent.getParkingLot().getDescription());
			mb.setOriginator((Person) parkingRent.getManager());
			mb.setResident(parkingRent.getContract().getSubscriber());
			mb.setServiceDate(parkingRent.getContract().getSubscriptionDate());
			mb.setTimePeriod(entry.getTimePeriod());
			
			//mb.setBase(new BigDecimal(base)); //Cada pago corresponde a la fracción del parqueadero en función del número de pagos
			
			if(mb.getDescription() == null) mb.setDescription("");
			mb.setDescription(mb.getDescription() +" EN EL ESTACIONAMIENTO: " + parkingRent.getParkingLot().getName() + ": POR EL MES DE " + getMonthName(aux).toUpperCase() + ", CON REFERENCIA: " + parkingRent.getReference());			
			
			mb.setExpirationDate(currentDate);
						
			emissionOrder.add(mb);
			ca.setTime(aux);
			ca.add(Calendar.MONTH, 1);
			aux = ca.getTime();
		}
			
		if(preEmit){
			for(MunicipalBond m: emissionOrder.getMunicipalBonds()){
				m.setGroupingCode(parkingRent.getContract().getId().toString());
			}			
			savePreEmissionOrder(emissionOrder);
		}
				
		return emissionOrder;

	}
	/*
	private  Date calculateNextDate(Date startDate){
		Calendar cstartDate = Calendar.getInstance();
		cstartDate.setTime(startDate);		
		return cstartDate.getTime(); 
	}
	*/
	
	private TaxRate findActiveTaxRate(Tax t) {
		Query query = entityManager.createNamedQuery("TaxRate.findActiveByTaxIdAndPaymentDate");
		query.setParameter("taxId", t.getId());
		query.setParameter("paymentDate", new Date());
		List<TaxRate> list = query.getResultList();
		if(list.size() > 0) return list.get(0);
		return null;
	}
	
	private EmissionOrder savePreEmissionOrderCashClosingParkingRent(TicketCashClosingList ticketList, FiscalPeriod f, Person person,  boolean preEmit) throws Exception {
		
		Date currentDate = new Date();
		Entry entry =  ticketList.getParkingLot().getCashClosingEntry();		
		Entry discountEntry =  systemParameterService.materialize(Entry.class, "PARKING_ENTRY_DISCOUNTED");
		Tax discountTax =  systemParameterService.materialize(Tax.class, "PARKING_TAX_DISCOUNTED");
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(ticketList.getGoing());		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy/MM/dd");
		String description = ticketList.getParkingLot().getName() + ": Cierre de tickets del [" + sdf.format(ticketList.getComing()) + " al " + sdf.format(ticketList.getGoing()) + "]";
		emissionOrder.setDescription(description);
		emissionOrder.setEmisor(ticketList.getParkingLot().getManager());
		
		EntryDefinition entryDefinition = null;
		for(EntryDefinition edef: discountEntry.getEntryDefinitions()){
			if(edef.getIsCurrent()) entryDefinition = edef;
		}
		
		TaxRate taxRate = findActiveTaxRate(discountTax);
		
		MunicipalBond mb = null; 
		//Date serviceDate = currentDate;
		Date expirationDate = currentDate;
		BigDecimal value = new BigDecimal(0);	
		
		if (ticketList.getResultList().isEmpty()){
			return null;
		} 
		
		Ticket auxTicket= null;
		for (Ticket ticket : ticketList.getResultList()){
			if(auxTicket == null) {
				auxTicket = ticket;
				auxTicket.setIsTotalCollected(true);
			}
			value = value.add(ticket.getCharge());
		}

		// Crear un MunicipalBond para el valor total
		EntryValueItem entryValueItem = new EntryValueItem(); 
		entryValueItem.setDescription(emissionOrder.getDescription());
		entryValueItem.setMainValue(value);
		entryValueItem.setServiceDate(currentDate);
		entryValueItem.setReference("");
		
		if(taxRate == null || entryDefinition == null){
			mb = revenueService.createMunicipalBond(person, entry, f, entryValueItem, true, auxTicket);
			//mb = revenueService.createMunicipalBond(person, f, entry, value, currentDate, null, MunicipalBondType.EMISSION_ORDER,auxTicket);
		}else{
			mb = revenueService.createMunicipalBond(person, entry, f, entryValueItem, true, auxTicket, taxRate, entryDefinition);
			//mb = revenueService.createMunicipalBond(person, f, entry, value, currentDate, null, MunicipalBondType.EMISSION_ORDER,auxTicket,taxRate,entryDefinition);
		}
		
		mb.setCreationDate(currentDate);
		mb.setCreationTime(currentDate);
		mb.setEntry(entry);
		mb.setFiscalPeriod(f);
		mb.setAddress(description);
		mb.setOriginator(ticketList.getParkingLot().getManager());
		mb.setResident(person);
		mb.setServiceDate(emissionOrder.getServiceDate());
		mb.setTimePeriod(entry.getTimePeriod());

		mb.setExpirationDate(expirationDate);
		//serviceDate = expirationDate;

		emissionOrder.add(mb);
		
		if(preEmit){			
			mb.setGroupingCode(ticketList.getParkingLot().getId().toString());
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


	@Override
	public void preEmitRentSpace(Contract c,Space s, FiscalPeriod f, Person p)
			throws Exception {
		// TODO Auto-generated method stub
		savePreEmissionOrderRentSpace(c,s, f, p, true);
		
	}
	
	@Override
	public void preEmitParkingRent(ParkingRent parkingRent, FiscalPeriod f, Long payments)
			throws Exception {
		savePreEmissionOrderParkingRent(parkingRent, f, true, payments);
		
	}
	
	@Override
	public EmissionOrder preEmitCashClosingParkingRent(TicketCashClosingList ticketList, FiscalPeriod f, Person p)
			throws Exception {
		return savePreEmissionOrderCashClosingParkingRent(ticketList, f, p, true);
	}


	@Override
	public EmissionOrder calculatePreEmissionRentSpace(Contract c, Space s,
			FiscalPeriod f, Person p) throws Exception {
		return savePreEmissionOrderRentSpace(c, s, f, p, false);
	}
	

}
