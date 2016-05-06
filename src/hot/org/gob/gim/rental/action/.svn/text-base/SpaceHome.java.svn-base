package org.gob.gim.rental.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.rental.facade.RentalService;
import org.gob.gim.revenue.action.EmissionOrderHome;
import org.gob.gim.revenue.service.EmissionOrderService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.rental.model.Space;
import ec.gob.gim.rental.model.SpaceStatus;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.ContractType;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("spaceHome")
public class SpaceHome extends EntityHome<Space> {

	public static String RENTAL_SERVICE_NAME = "/gim/RentalService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	@Logger
	Log logger;

	@In
	Gim gim;

	@In
	FacesMessages facesMessages;

	@In
	UserSession userSession;
	
	@In(create = true)
	private EmissionOrderHome emissionOrderHome;

	private List<Resident> residents;

	private boolean isFirstTime = true;
	private boolean isAntenna = true;

	private Long residentId;	
	private Space space;
	private String criteria;
	private String criteriaEntry;
	private String identificationNumber;
	private EmissionOrder emissionOrder;
	private Charge regulationCharge;	
	private Delegate regulationDelegate;

	private RentalService rentalService;	
	private SystemParameterService systemParameterService;
	
	private ArrayList<Contract> contracts;

	public void setSpaceId(Long id) {
		setId(id);
	}

	public Long getSpaceId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if (getInstance().getSpaceType() != null && getInstance().getSpaceType().getName().equalsIgnoreCase("ANTENA")) {
			isAntenna = true;
		} else {
			isAntenna = false;
		}
		if(this.getInstance().getCurrentContract() != null) this.getInstance().getCurrentContract().setTotalMonths(monthsBetween(this.getInstance().getCurrentContract().getExpirationDate(),this.getInstance().getCurrentContract().getSubscriptionDate()));
		this.getInstance().setTotalArea(this.getInstance().getWidth() * this.getInstance().getHeight());
	}
	
	@Override
	public String persist() {
		// TODO Auto-generated method stub
		getInstance().setSpaceStatus(SpaceStatus.FREE);
		return super.persist();
	}
	
	
	public String rentUpdate() {
		// TODO Auto-generated method stub
		if(this.getInstance().getSpaceType().getName().equals("ANTENA")){
			this.getInstance().getCurrentContract().setDescription("Alto de la antena: " + this.getInstance().getAntennaHeight() +" m");
		}else{
			this.getInstance().getCurrentContract().setDescription("Ancho: " + this.getInstance().getWidth() + " m, " + 
																	"Alto: " + this.getInstance().getHeight() + " m, " +
																	"Area: " + this.getInstance().getTotalArea() +" m2");
		}
		this.getInstance().setHasPreEmit(true);
		this.getInstance().add(this.getInstance().getCurrentContract());		
		return super.update();
	}
	
	public void outOfService(Space s){
		s.setSpaceStatus(SpaceStatus.OUT_SERVICE);
	}

	public static Long monthsBetween(Date minuend, Date subtrahend) {
		Calendar cal = Calendar.getInstance();		
		cal.setTime(minuend);		
		int minuendMonth = cal.get(Calendar.MONTH);
		int minuendYear = cal.get(Calendar.YEAR);
		cal.setTime(subtrahend);
		int subtrahendMonth = cal.get(Calendar.MONTH);
		int subtrahendYear = cal.get(Calendar.YEAR);		
		return Long.parseLong(((minuendYear - subtrahendYear) * cal.getMaximum(Calendar.MONTH))+ (minuendMonth - subtrahendMonth + (1*(minuendYear - subtrahendYear))) + "");
	}

	public ArrayList<Contract> orderByDateAsc(List<Contract> cont){
		ArrayList<Contract> contracts = new ArrayList<Contract>();
		ArrayList<Date> dates = new ArrayList<Date>();
		for(Contract c:cont){
			dates.add(c.getExpirationDate());
		}
		Collections.sort(dates);
		ArrayList<Date> temp = new ArrayList<Date>();
		for(int i=dates.size()-1 ; i>=0 ; i--){
			temp.add(dates.get(i));
		}
		for(Date d: temp){
			for(Contract c:cont){
				if(c.getExpirationDate().equals(d) && !contracts.contains(c)) contracts.add(c);
			}
		}
		return contracts;
	}
	
	public void wireRent() {
		if (!isFirstTime)
			return;
		getInstance();
		Date lastExpirationDate = null;
		double lastAntennaSize = 0;
		if (getInstance().getCurrentContract() != null) {
			lastExpirationDate = getInstance().getCurrentContract()
					.getExpirationDate();			
		}
		if (getInstance().getAntennaHeight() != 0) {
			lastAntennaSize = getInstance().getAntennaHeight();	
		}
		getInstance().setCurrentContract(new Contract());
		getInstance().getCurrentContract().setCreationDate(new Date());
		getInstance().setTotalArea(
				getInstance().getWidth() * getInstance().getHeight());
		if (getResidentId() != null) {
			this.getInstance().getCurrentContract()
					.setSubscriber(searchResidentById());
			this.getInstance().getCurrentContract()
					.setSubscriptionDate(lastExpirationDate);
		}
		if (getInstance().getSpaceType().getName().equalsIgnoreCase("ANTENA")) {
			getInstance().setAntennaHeight(lastAntennaSize);			
			isAntenna = true;
		} else {
			isAntenna = false;
		}
		isFirstTime = false;
	}
	
	public void wirePreEmitRent() throws Exception {
		if (!isFirstTime) return;
		
		getInstance().setTotalArea(getInstance().getWidth() * getInstance().getHeight());
		
		if (getInstance().getSpaceType().getName().equalsIgnoreCase("ANTENA")) {					
			isAntenna = true;
		} else {
			isAntenna = false;
		}
		
		this.getInstance().getCurrentContract().setTotalMonths(monthsBetween(this.getInstance().getCurrentContract().getExpirationDate(), this.getInstance().getCurrentContract().getSubscriptionDate()));
		
		calculateTotalRentSpaceEmissionOrder();
		
		if(this.getInstance().getCurrentContract().getSubscriber() != null) this.setIdentificationNumber(this.getInstance().getCurrentContract().getSubscriber().getIdentificationNumber());
		
		isFirstTime = false;
	}
	
	
	public void changeSpace(Space s){
		space = s;
		this.setContracts(orderByDateAsc(s.getContracts()));
	}

	public boolean isWired() {
		return true;
	}

	public Space getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void cleanValues() {
		if (this.getInstance().getSpaceType() == null
				|| this.getInstance().getSpaceType().getName()
						.equalsIgnoreCase("ANTENA")) {
			this.getInstance().setCompassPoint(null);
			this.getInstance().setWidth(0);
			this.getInstance().setHeight(0);
			isAntenna = true;
		} else {
			isAntenna = false;
		}

	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	@SuppressWarnings("unchecked")
	public Resident searchResidentById() {
		if (this.getResidentId() != null) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findById");
			query.setParameter("id", this.getResidentId());
			List<?> list = query.getResultList();
			return (list != null ? (Resident) list.get(0) : null);
		}
		return null;
	}

	public void searchResident() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			// resident.add(this.getInstance());
			this.getInstance().getCurrentContract().setSubscriber(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().getCurrentContract().setSubscriber(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void calculateTotal(EmissionOrder emissionOrder) {
		if (emissionOrder == null)
			emissionOrder = new EmissionOrder();
		emissionOrder.setAmount(new BigDecimal(0));
		for (MunicipalBond m : emissionOrder.getMunicipalBonds()) {
			emissionOrder.setAmount(emissionOrder.getAmount().add(m.getValue()));
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().getCurrentContract().setSubscriber(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	public void calculateExpirationDate() {
		if (this.getInstance().getCurrentContract().getSubscriptionDate() == null
				|| this.getInstance().getCurrentContract().getTotalMonths() == null)
			return;
		Calendar ca = Calendar.getInstance();
		ca.setTime(this.getInstance().getCurrentContract()
				.getSubscriptionDate());
		ca.add(Calendar.MONTH, this.getInstance().getCurrentContract()
				.getTotalMonths().intValue());
		this.getInstance().getCurrentContract().setExpirationDate(ca.getTime());
	}
	
	public String rentSpace() throws Exception {
		try {
					
			if(emissionOrder.getMunicipalBonds()!= null && emissionOrder.getMunicipalBonds().size() > 0){
				emissionOrder.getMunicipalBonds().get(0).setGroupingCode(this.getInstance().getCurrentContract().getId().toString());
			}
			
			EmissionOrderService emissionOrderService = ServiceLocator.getInstance().findResource(EmissionOrderService.LOCAL_NAME);
			
			emissionOrder.setEmisor((Person)userSession.getUser().getResident());
			
			emissionOrderService.createEmissionOrder(emissionOrder);
						
			//rentalService.savePreEmissionOrder(emissionOrder);
			
			this.getInstance().setSpaceStatus(SpaceStatus.RENTED);
			this.getInstance().setHasPreEmit(false);
			this.update();
			
			emissionOrderHome.setInstance(emissionOrder);
			emissionOrderHome.update();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}
		return "complete";
	}

	public void noRenovate(Space s) throws Exception {
		this.setInstance(s);
		if (this.getInstance().getSpaceType().getName()
				.equalsIgnoreCase("ANTENA"))
			this.getInstance().setAntennaHeight(0);
		this.getInstance().setSpaceStatus(SpaceStatus.FREE);
		this.getInstance().setCurrentContract(null);
		this.update();
	}

	public void calculateTotalRentSpaceEmissionOrder() throws Exception {
		if (getInstance().getCurrentContract().getSubscriber() == null) {
			String message = Interpolator.instance().interpolate(
					"#{messages['resident.required']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return;
		}
		if (rentalService == null)
			rentalService = ServiceLocator.getInstance().findResource(
					RENTAL_SERVICE_NAME);
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		try {
			emissionOrder = rentalService.calculatePreEmissionRentSpace(this.getInstance().getCurrentContract(),
					this.getInstance(), userSession.getFiscalPeriod(),
					userSession.getPerson());
			
			calculateTotal(emissionOrder);
			ContractType contractType = systemParameterService.materialize(ContractType.class, "CONTRACT_TYPE_ID_RENTAL");
			getInstance().getCurrentContract().setContractType(contractType);
		} catch (Exception e) {
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']} " + e, new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
		}
	}
	
	public void loadCharge(){
		regulationCharge = getCharge("DELEGATE_ID_REGULATION_AND_CONTROL");
		if(regulationCharge != null){
			for(Delegate d: regulationCharge.getDelegates()){
				if(d.getIsActive())regulationDelegate = d;			
			}			
		}
	}
	
	public Charge getCharge(String systemParameter){
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class, systemParameter);
		return charge;
	}

	public void setEmissionOrder(EmissionOrder emissionOrder) {
		this.emissionOrder = emissionOrder;
	}

	public EmissionOrder getEmissionOrder() {
		return emissionOrder;
	}

	public void setRentalService(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	public RentalService getRentalService() {
		return rentalService;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setAntenna(boolean isAntenna) {
		this.isAntenna = isAntenna;
	}

	public boolean isAntenna() {
		return isAntenna;
	}

	public void setSystemParameterService(SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public Space getSpace() {
		return space;
	}

	public void setContracts(ArrayList<Contract> contracts) {
		this.contracts = contracts;
	}

	public ArrayList<Contract> getContracts() {
		return contracts;
	}

	public void setEmissionOrderHome(EmissionOrderHome emissionOrderHome) {
		this.emissionOrderHome = emissionOrderHome;
	}

	public EmissionOrderHome getEmissionOrderHome() {
		return emissionOrderHome;
	}
	
	public Charge getRegulationCharge() {
		return regulationCharge;
	}

	public void setRegulationCharge(Charge regulationCharge) {
		this.regulationCharge = regulationCharge;
	}

	public Delegate getRegulationDelegate() {
		return regulationDelegate;
	}

	public void setRegulationDelegate(Delegate regulationDelegate) {
		this.regulationDelegate = regulationDelegate;
	}
	
	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

}
