package org.gob.gim.commercial.facade;

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

import ec.gob.gim.commercial.model.BusinessCatalog;
import ec.gob.gim.commercial.model.TouristLicenseEmission;
import ec.gob.gim.commercial.model.TouristLicenseItem;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.adjunct.TouristLicenseReference;

@Stateless(name = "TouristLicenseService")
public class TouristLicenseServiceBean implements TouristLicenseService {

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

	
	@Override
	public EmissionOrder generateEmissionOrder(BusinessCatalog bc,
			List<TouristLicenseItem> items, FiscalPeriod f, Person per,
			int year, String descriptionPart) throws Exception {
		
		return calculateEmissionOrderItmes(bc, items, f, per, year, descriptionPart);
	}

	private EmissionOrder calculateEmissionOrderItmes(BusinessCatalog bc,
			List<TouristLicenseItem> items, FiscalPeriod f, Person p,
			 int year, String descriptionPart)
			throws Exception {

		Date serviceDate = new Date();//createServiceDate(year, month);
		Date emisionPeriod = findEmisionPeriod();

		Entry entry = systemParameterService.materialize(Entry.class, "ENTRY_ID_TOURISM_LICENSE");

		EmissionOrder emissionOrder = new EmissionOrder();
		//
		// emissionOrder.setServiceDate(cServiceDate.getTime());
		emissionOrder.setServiceDate(serviceDate);

		emissionOrder.setDescription("Licencias de turismo: "
				+ bc.getName()
				+ ", "
				+ descriptionPart
				+ ", año: "
				+ year);
		emissionOrder.setEmisor(p);

		MunicipalBondStatus mbs = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		
		TouristLicenseReference tlr;
		String adisionalDescription;
		for (TouristLicenseItem tli : items) {
			
			//if (cp.getCurrentContract() != null) {
				EntryValueItem entryValueItem = new EntryValueItem();
				entryValueItem.setDescription(emissionOrder.getDescription());
				entryValueItem.setServiceDate(serviceDate);
				entryValueItem.setReference("");
			
				Resident person = tli.getLocal().getBusiness().getOwner();

				MunicipalBond mb = revenueService.createMunicipalBond(person,
						entry, f, entryValueItem, true, tli);
				
				String category=tli.getLocal().getLocalFeature().getFeatureCategory().getName();				
				
				tlr = new TouristLicenseReference();
				tlr.setCode(entry.getCode());
				tlr.setYear(year);
				tlr.setCategory(category);
				tlr.setBussinessName(tli.getLocal().getName());
				tlr.setLicenseItem(tli);
				
				adisionalDescription =tli.getAdisionalDescription();
				if (adisionalDescription != null && !adisionalDescription.equals("")) {
					adisionalDescription=", "+adisionalDescription;
				}else{
					adisionalDescription="";
				}
				
				mb.setAdjunct(tlr);
				//Referencia:	MEMORANDO 023-013-UT-GADML
				mb.setReference(descriptionPart);
				//Explicación:	POR 2013 AGENCIA DE VIAJES CATEGORIA DUALIDAD
				mb.setDescription("POR "+year+", "+ tli.getLocal().getName()+", CATEGORIA: "+category +adisionalDescription);
				mb.setBondAddress(tli.getLocal().getAddress().getStreet());
				mb.setServiceDate(serviceDate);
				mb.setCreationTime(new Date());
				
				mb.setGroupingCode(bc.getId()+ " - "+tli.getLocal().getLocalFeature().getComercialRegister());

				mb.setMunicipalBondStatus(mbs);

				mb.setBase(tli.getValue()); // valor a cobrar

				mb.setOriginator(p);
				mb.setTimePeriod(entry.getTimePeriod());
				mb.calculateValue();
				mb.setEmisionPeriod(emisionPeriod);
				emissionOrder.add(mb);
			//}
		}
		return emissionOrder;
	}

	private Date findEmisionPeriod() {
		Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		return calendar.getTime();

	}

	private Date createServiceDate(int year, int month) {
		Date today = new Date();
		/*
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(today);
		 * 
		 * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.set(Calendar.MONTH,
		 * 0);
		 * 
		 * return calendar.getTime();
		 */

		//System.out.println("------Fecha actual--------------");
		//System.out.println(new SimpleDateFormat("yyyy MMMM dd").format(today));

		Calendar todayC = Calendar.getInstance();
		todayC.setTime(today);
		int todayMonth = todayC.get(Calendar.DAY_OF_MONTH);
		System.out.println(todayMonth);

		Calendar serviceDate = Calendar.getInstance();
		serviceDate.setTime(today);
		serviceDate.set(Calendar.YEAR, year);
		serviceDate.set(Calendar.MONTH, month - 1);
		serviceDate.set(Calendar.DAY_OF_MONTH, 1);
		int serviceDateMaxDay = serviceDate
				.getActualMaximum(Calendar.DAY_OF_MONTH);
		//System.out.println("------Fecha sin revision de dias--------------");
		//System.out.println(new SimpleDateFormat("yyyy MMMM dd")				.format(serviceDate.getTime()));

		System.out.println(todayMonth + "    " + serviceDateMaxDay);

		if (todayMonth <= serviceDateMaxDay) {
			serviceDate.set(Calendar.DAY_OF_MONTH, todayMonth);
		} else {
			serviceDate.set(Calendar.DAY_OF_MONTH, serviceDateMaxDay);
		}
		//System.out.println("------Fecha final--------------");
		//System.out.println(new SimpleDateFormat("yyyy MMMM dd")				.format(serviceDate.getTime()));

		return serviceDate.getTime();
	}

	@Override
	public void saveEmissionOrder(EmissionOrder emissionOrder, TouristLicenseEmission tle, Boolean preEmit) {
		if (preEmit){
			//System.out.println("------Z>>>>>>>>>>>>>>ZZZZZ ya guardando");
			entityManager.persist(emissionOrder);
			entityManager.persist(tle);
			entityManager.flush();
			updateConcessionItems(emissionOrder);
		}
	}
	
	
	private void updateConcessionItems(EmissionOrder emissionOrder){
		String sql="";
		for(MunicipalBond mb:emissionOrder.getMunicipalBonds()){
			TouristLicenseReference tlr=(TouristLicenseReference) mb.getAdjunct();
			TouristLicenseItem tli = tlr.getLicenseItem();
			sql = sql+"update TouristLicenseItem set municipalbond_id = "+mb.getId()+" where id="+tli.getId()+";\n";
		}
		System.out.println(sql);
		Query q=entityManager.createNativeQuery(sql);
		q.executeUpdate();
		//System.out.println("termina ahi>>>>>>>>>>>>>>>><<");
	}

	

}