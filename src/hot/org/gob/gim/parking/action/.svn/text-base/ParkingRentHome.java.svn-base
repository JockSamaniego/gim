package org.gob.gim.parking.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.ResidentHome;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.rental.facade.RentalService;
import org.gob.gim.revenue.action.ContractHome;
import org.gob.gim.util.GimPrettyTime;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.parking.model.ParkingLot;
import ec.gob.gim.parking.model.ParkingRent;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.ContractType;

@Name("parkingRentHome")
public class ParkingRentHome extends EntityHome<ParkingRent> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3488019640553145387L;
	
	public static String RENTAL_SERVICE_NAME = "/gim/RentalService/local";

	@Logger 
	Log logger;
	
	private String criteria;
	
	private String identificationNumber;
	
	private List<Resident> residents;
	
	@In(create = true)
	private ResidentHome residentHome;
	
	private Resident subscriber;
	
	private ParkingLot parkingLot;
	
	private ParkingRent parkingRent = null;
	
	private Long payments;
	
	@In(scope=ScopeType.SESSION, value="userSession")
	UserSession userSession; //Fuente del suscriber
	
	private RentalService rentalService;
	
	public ParkingRentHome() {
		
		this.payments = 0L;
	}
	

	public void setParkingRentId(Long id) {
		setId(id);
	}

	public Long getParkingRentId() {
		return (Long) getId();
	}

	
	
	@Override
	protected ParkingRent createInstance() {
		ParkingRent parkingRent = new ParkingRent();
		//Completar información del contrato		
		Contract contract = new Contract();		
		parkingRent.setContract(contract);		
		return parkingRent;
	}

	private ContractType getContractType() {
		Query query = getEntityManager().createNamedQuery("ContractType.findByEntry");
		query.setParameter("entry", this.getInstance().getParkingLot().getEntry());
		return (ContractType) query.getResultList().get(0);
	}


	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if(getInstance() != null 
				&& getInstance().getContract() != null 
				&& getInstance().getContract().getSubscriber() != null) identificationNumber= getInstance().getContract().getSubscriber().getIdentificationNumber();
	
	}

	public boolean isWired() {
		return true;
	}

	public ParkingRent getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public ParkingRent getParkingRent() {
		return parkingRent;
	}

	public void setParkingRent(ParkingRent parkingRent) {
		this.parkingRent = parkingRent;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}


	public String getCriteria() {
		return criteria;
	}


	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}


	public String getIdentificationNumber() {
		return identificationNumber;
	}
	
	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	
	public void searchOwner() {
		logger.info("looking for............ {0}", this.getIdentificationNumber());
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.getIdentificationNumber());
		try{
			Person resident = (Person) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			
			this.getInstance().getContract().setSubscriber(resident);
			
			if (resident.getId() == null){
				addFacesMessageFromResourceBundle("resident.notFound");
			}
			
			identificationNumber = resident.getIdentificationNumber();
			
		}
		catch(Exception e){
			this.getInstance().getContract().setSubscriber(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	
	@SuppressWarnings("unchecked")
	public void searchOperatorByCriteria(){
		if (this.criteria != null && !this.criteria.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public void clearSearchPanel(){
		this.setCriteria(null);
		residents = null;
	}
	
	public void operatorSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Person resident = (Person) component.getAttributes().get("resident");
		this.getInstance().getContract().setSubscriber(resident);
		identificationNumber = resident.getIdentificationNumber();
	}


	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
		if (!isManaged()){
			this.getInstance().setParkingLot(this.parkingLot);
		}
	}


	public ParkingLot getParkingLot() {
		return parkingLot;
	}
	
	@In(create=true)
	ContractHome contractHome;
	
	private Date findLastDateOfMonth(Date d){		
		Calendar ca = Calendar.getInstance();	
		ca.setTime(d);
		int lastDate = ca.getActualMaximum(Calendar.DATE);		
		ca.set(Calendar.DATE, lastDate);
		return ca.getTime();
	}
	
	@Override
	public String persist(){
		
		if(this.getInstance().getContract().getSubscriber() == null){
			addFacesMessageFromResourceBundle("resident.required");
			return "failed";
		}
		
		//Completar la entidad ParkingRent
		this.getInstance().setManager(userSession.getPerson());
		//Completar información del contrato
		Calendar now = Calendar.getInstance();
		this.getInstance().getContract().setContractType(getContractType());
		this.getInstance().getContract().setCreationDate(now.getTime());
		this.getInstance().getContract().setExpirationDate(findLastDateOfMonth(this.getInstance().getContract().getExpirationDate()));
		this.getInstance().getContract().setDescription("Arriendo de parqueadero: " + this.getInstance().getParkingLot().getName() + ",  " + this.getInstance().getContract().getSubscriber().getName());	
		
		contractHome.setInstance(this.getInstance().getContract());
		contractHome.persist();
		
		super.persist();
					
		try {
			this.getRentalService().preEmitParkingRent(this.getInstance(), userSession.getFiscalPeriod(), this.getPayments());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "persisted";
	}


	public void setRentalService(RentalService rentalService) {
		this.rentalService = rentalService;
	}


	public RentalService getRentalService() {
		if (rentalService == null)
			rentalService = ServiceLocator.getInstance().findResource(
					RENTAL_SERVICE_NAME);
		return rentalService;
	}


	public void setSubscriber(Resident subscriber) {
		this.subscriber = subscriber;
	}


	public Resident getSubscriber() {
		return subscriber;
	}


	public void setPayments(Long payments) {
		this.payments = payments;
	}
	
	public void calculateExpirationDate() {
		if(this.payments.intValue() == 0){
			this.getInstance().getContract().setExpirationDate(null);
			return;
		}
		if (this.getInstance().getContract().getSubscriptionDate() == null
				|| this.getPayments() == null)
			return;
		Calendar ca = Calendar.getInstance();
		ca.setTime(this.getInstance().getContract().getSubscriptionDate());
		ca.add(Calendar.MONTH, this.payments.intValue());		
		ca.add(Calendar.DAY_OF_MONTH, -1);
		this.getInstance().getContract().setExpirationDate(ca.getTime());
		
	}

	public Long getPayments() {
		return payments;
	}
	
	public void presetPayments(){
		if (this.getInstance().getContract().getExpirationDate() != null){
			GregorianCalendar  g1 = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar  g2 = (GregorianCalendar) GregorianCalendar.getInstance();
			g1.setTime(this.getInstance().getContract().getSubscriptionDate());
			g2.setTime(this.getInstance().getContract().getExpirationDate());			
			this.setPayments(Long.valueOf(GimPrettyTime.getMonths(g1, g2)));
		}
	}

}
