package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.cementery.facade.DeathService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.action.EmissionOrderHome;
import org.gob.gim.revenue.action.SolvencyReportHome;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Death;
import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.ContractType;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("deathHome")
public class DeathHome extends EntityHome<Death> implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	public static String DEATH_SERVICE_NAME = "/gim/DeathService/local";

	@In
	Gim gim;

	@In
	FacesMessages facesMessages;

	@In
	UserSession userSession;
	
	@In(create = true)
	private EmissionOrderHome emissionOrderHome;

	@In(create = true)
	UnitDeathHome unitDeathHome;

	Log logger;

	private SystemParameterService systemParameterService;
	private DeathService deathService;
	private EmissionOrder emissionOrder;

	private String deceasedIdentification;
	private String deceasedCriteria;
	private String debtorIdentification;
	private String debtorCriteria;

	private Person deceased;
	private Resident deceasedResident;
	private Resident debtor;

	private List<Resident> residents;

	private boolean registered = true;

	private boolean isFirstTime = true;
	private boolean renewContract;

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if (isFirstTime){
			if (unitDeathHome.isIdDefined()){
				System.out.println("================Ingresa a unitDeathHome en wire");
				if (unitDeathHome.getInstance().getCurrentDeath() == null){
					this.setRegistered(false);
					this.setDeceasedCriteria(null);
				} else {
					if (unitDeathHome.getInstance().getCurrentDeath().getDeceased() == null){
						this.setRegistered(false);
						this.setDeceasedCriteria(null);
					} else {
						this.setRegistered(true);
						this.setDeceasedIdentification(unitDeathHome.getInstance().getCurrentDeath().getDeceased().getIdentificationNumber());
//						this.setDeceased(unitDeathHome.getInstance().getCurrentDeath().getDeceased());
						searchPersonDeceased();
					}
				}
				unitDeathHome.getInstance().setCurrentDeath(instance);
				this.instance.setUnit(unitDeathHome.getInstance());
			}
			if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			ContractType contractType = systemParameterService.materialize(ContractType.class, "CONTRACT_TYPE_ID_DEATH");
			if (isRenewContract()) {
				Contract lastContract = null;
				lastContract = this.instance.getCurrentContract();
				if (lastContract != null){
					this.instance.setCurrentContract(new Contract());
					this.instance.getCurrentContract().setSubscriptionDate(lastContract.getExpirationDate());
					this.instance.getCurrentContract().setSubscriber(lastContract.getSubscriber());
					
				} 
				
			}
			if (this.instance.getCurrentContract() == null){
				this.instance.setCurrentContract(new Contract());
				this.instance.getCurrentContract().setSubscriptionDate(new Date());
			}
			this.instance.getCurrentContract().setCreationDate(new Date());
			this.instance.getCurrentContract().setExpirationDate(new Date());
			this.instance.getCurrentContract().setContractType(contractType);
			this.instance.getCurrentContract().setDeath(this.instance);
			this.instance.getCurrentContract().setTotalMonths(new Long(0));
			if(this.getInstance().getCurrentContract() != null)this.setDebtor(this.getInstance().getCurrentContract().getSubscriber());
			if(this.getDebtor() != null)this.setDebtorIdentification(this.getDebtor().getIdentificationNumber());
			if(this.getInstance().getDeceased() != null)this.setDeceasedResident(this.getInstance().getDeceased());
			if(this.getDeceasedResident() != null)this.setDeceasedIdentification(this.getDeceasedResident().getIdentificationNumber());

		}
		isFirstTime = false;
	}

	public void calculateTotal(EmissionOrder emissionOrder) {
		if (emissionOrder == null) emissionOrder = new EmissionOrder();
		emissionOrder.setAmount(new BigDecimal(0));
		for (MunicipalBond m : emissionOrder.getMunicipalBonds()) {
			emissionOrder.setAmount(emissionOrder.getAmount().add(m.getValue()));
		}
	}

	public void calculateRentDeathEmissionOrder() throws Exception {
		if (getInstance().getCurrentContract().getSubscriber() == null) {
			String message = Interpolator.instance().interpolate("#{messages['resident.required']}", new Object[0]);
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			return;
		}
		if (deathService == null)
			deathService = ServiceLocator.getInstance().findResource(DEATH_SERVICE_NAME);
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		try {
			emissionOrder = deathService.calculatePreEmissionDeathUnit(this.getInstance().getCurrentContract(),
					this.getInstance().getUnit(), userSession.getFiscalPeriod(),userSession.getPerson());

			calculateTotal(emissionOrder);
//			ContractType contractType = systemParameterService.materialize(ContractType.class, "RENTAL_CONTRACT_TYPE_ID");
//			getInstance().getCurrentContract().setContractType(contractType);
		} catch (Exception e) {
			String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Death getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setDeathId(Long id) {
		setId(id);
	}

	public Long getDeathId() {
		return (Long) getId();
	}

	@Override
	public String persist() {
		addCurrentContract();
		if (unitDeathHome.isIdDefined()){
			System.out.println("ingresa a unitdeath is iddefined");
			this.getInstance().setUnit(unitDeathHome.getInstance());
			unitDeathHome.getInstance().setDeath(instance);
			unitDeathHome.getInstance().setUnitStatus(UnitStatus.RENTED);
		} else {
			System.out.println("==================unitDeathHome es null");
			this.instance.setUnit(null);
			unitDeathHome.setInstance(null);
		}
		
		if (this.isRegistered()){
			this.getInstance().setDeathName(this.deceasedResident.getName());
		}

		if (super.persist() == "failed") return "failed";
/*
		if (emissionOrder != null){
			try {
				if (rentUnitDeath() == "failed") return "failed";
			} catch (Exception e) {
				String message = Interpolator.instance().interpolate(
						"#{messages['property.errorGenerateTax']}", new Object[0]);
				facesMessages.addToControl("residentChooser",
						org.jboss.seam.international.StatusMessage.Severity.ERROR,
						message);
				return "failed";
			}
		}
*/
		return "persisted";
	}

	public String rentUnitDeath() throws Exception {
		try {
			emissionOrder.getMunicipalBonds().get(0).setGroupingCode(this.getInstance().getCurrentContract().getId().toString());
			deathService.savePreEmissionOrder(emissionOrder);
			this.update();
			if(emissionOrder.getMunicipalBonds()!= null && emissionOrder.getMunicipalBonds().size() > 0){
				emissionOrder.getMunicipalBonds().get(0).setGroupingCode(this.getInstance().getCurrentContract().getId().toString());
			}
			emissionOrderHome.setInstance(emissionOrder);
			emissionOrderHome.update();
		} catch (Exception e) {
			String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}
		return "complete";
	}

	public List<Contract> getContracts() {
		return getInstance() == null ? null : new ArrayList<Contract>(
				getInstance().getContracts());
	}

	public void calculateExpirationDate() {
		if (this.getInstance().getCurrentContract().getSubscriptionDate() == null
				|| this.getInstance().getCurrentContract().getTotalMonths() == null)
			return;
		Calendar ca = Calendar.getInstance();
		ca.setTime(this.getInstance().getCurrentContract().getSubscriptionDate());
		ca.add(Calendar.MONTH, this.getInstance().getCurrentContract().getTotalMonths().intValue());
		this.getInstance().getCurrentContract().setExpirationDate(ca.getTime());

		try {
			calculateRentDeathEmissionOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCurrentContract(){
		System.out.println("ingresa a addcontract");

		if (deceasedResident != null) this.getInstance().setDeathName(deceasedResident.getName());
		
//		this.instance.getCurrentContract().setSubscriber(this.debtor);

		this.instance.setCurrent(true);
		this.instance.setRenovationNumber(0);
		if (unitDeathHome.isIdDefined()) this.instance.setCementery(unitDeathHome.getInstance().getSection().getCementery());
		System.out.println("sale de addcontract");
			
	}
	
	public String getDeceasedIdentification() {
		return deceasedIdentification;
	}

	public void setDeceasedIdentification(String deceasedIdentification) {
		this.deceasedIdentification = deceasedIdentification;
	}

	public String getDeceasedCriteria() {
		return deceasedCriteria;
	}

	public void setDeceasedCriteria(String deceasedCriteria) {
		this.deceasedCriteria = deceasedCriteria;
	}

	public String getDebtorIdentification() {
		return debtorIdentification;
	}

	public void setDebtorIdentification(String debtorIdentification) {
		this.debtorIdentification = debtorIdentification;
	}

	public String getDebtorCriteria() {
		return debtorCriteria;
	}

	public void setDebtorCriteria(String debtorCriteria) {
		this.debtorCriteria = debtorCriteria;
	}

	public Person getDeceased() {
		return deceased;
	}

	public void setDeceased(Person deceased) {
		this.deceased = deceased;
	}

	public Resident getDeceasedResident() {
		return deceasedResident;
	}

	public void setDeceasedResident(Resident deceasedResident) {
		this.deceasedResident = deceasedResident;
	}

	public Resident getDebtor() {
		return debtor;
	}

	public void setDebtor(Resident debtor) {
		this.debtor = debtor;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public boolean isRegistered() {
		return this.registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isRenewContract() {
		return renewContract;
	}

	public void setRenewContract(boolean renewContract) {
		this.renewContract = renewContract;
	}	

	public UnitDeathHome getUnitDeathHome() {
		return unitDeathHome;
	}

	public void setUnitDeathHome(UnitDeathHome unitDeathHome) {
		this.unitDeathHome = unitDeathHome;
	}

	public void setNullDeceased() {
		if (!isIdDefined()){
			if (!this.registered) {
				this.setDeceasedIdentification(null);
				this.setDeceased(null);
				setDeceasedResident(null);
				clearSearchResidentPanel();
			}
		}
	}


	@SuppressWarnings("unchecked")
	public void searchDeceasedByCriteria() {
		if (this.deceasedCriteria != null && !this.deceasedCriteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.deceasedCriteria);
			setResidents(query.getResultList());
		}
	}

	@SuppressWarnings("unchecked")
	public void searchDebtorByCriteria() {
		if (this.debtorCriteria != null && !this.debtorCriteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.debtorCriteria);
			setResidents(query.getResultList());
		}
	}

	public void searchPersonDeceased() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.deceasedIdentification);
		try {
			Resident resident = (Resident) query.getSingleResult();

			this.getInstance().setDeceased((Person) resident);
			this.setDeceasedResident(resident);
			this.setDeceasedIdentification(resident.getIdentificationNumber());

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setDeceased(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void searchResidentDebtor() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.debtorIdentification);
		try {
			Resident resident = (Resident) query.getSingleResult();

//			this.getInstance().getCurrentDomain().setResident(resident);
			this.setDebtor(resident);
			if (this.instance.getCurrentContract() != null)
				this.instance.getCurrentContract().setSubscriber(resident);
			this.setDebtorIdentification(resident.getIdentificationNumber());

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
//			this.getInstance().getCurrentDomain().setResident(null);
			this.setDebtor(null);
			this.instance.getCurrentContract().setSubscriber(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void deceasedSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
//		this.getInstance().getCurrentDomain().setResident(resident);
		this.getInstance().setDeceased((Person) resident);
		this.setDeceasedResident(resident);
		this.setDeceasedIdentification(resident.getIdentificationNumber());
	}

	public void debtorSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
//		this.getInstance().getCurrentDomain().setResident(resident);
		this.setDebtor(resident);
		this.instance.getCurrentContract().setSubscriber(resident);
		this.setDebtorIdentification(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setDeceasedCriteria(null);
		this.setDebtorCriteria(null);
		setResidents(null);
	}

	public EmissionOrder getEmissionOrder() {
		return emissionOrder;
	}

	public void setEmissionOrder(EmissionOrder emissionOrder) {
		this.emissionOrder = emissionOrder;
	}
}
