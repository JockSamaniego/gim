package org.gob.gim.market.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.market.facade.MarketService;
import org.gob.gim.revenue.action.EmissionOrderHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.market.model.Market;
import ec.gob.gim.market.model.Stand;
import ec.gob.gim.market.model.StandStatus;
import ec.gob.gim.market.model.StandType;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.ContractType;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("standHome")
public class StandHome extends EntityHome<Stand> {

	public static String MARKET_SERVICE_NAME = "/gim/MarketService/local";
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

	private EmissionOrder emissionOrder;

	private MarketService marketService;

	private SystemParameterService systemParameterService;

	@In(create = true)
	MarketHome marketHome;
	@In(create = true)
	StandTypeHome standTypeHome;

	// para el resident chooser
	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;

	private Long residentId;

	// para poder preemitir los localaes en el caso q tenga mas de uno
	private List<Stand> preEmitStands;

	private String operationType = "Rent";
	private Boolean activePanelResident = new Boolean(true);

	public void setStandId(Long id) {
		setId(id);
	}

	public Long getStandId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Market market = marketHome.getDefinedInstance();
		if (market != null) {
			getInstance().setMarket(market);
		}
		StandType standType = standTypeHome.getDefinedInstance();
		if (standType != null) {
			getInstance().setStandType(standType);
		}
		if (this.getInstance().getCurrentContract() == null) {
			getInstance().setCurrentContract(new Contract());
			getInstance().getCurrentContract().setCreationDate(new Date());
		}
		if (getResidentId() != null) {
			this.getInstance().getCurrentContract().setSubscriber(searchResidentById());
			this.resident = this.getInstance().getCurrentContract().getSubscriber();
			if(this.resident != null)this.identificationNumber = this.resident.getIdentificationNumber(); 
		}

		if (operationType.equals("Rent")) {
			activePanelResident = new Boolean(true);
		} else {
			activePanelResident = new Boolean(false);
		}

		if (marketService == null)
			marketService = ServiceLocator.getInstance().findResource(MARKET_SERVICE_NAME);
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
	}

	@SuppressWarnings("unchecked")
	public Resident searchResidentById() {
		if (this.getResidentId() != null) {
			Query query = getEntityManager().createNamedQuery("Resident.findById");
			query.setParameter("id", this.getResidentId());
			List<?> list = query.getResultList();
			return (list != null ? (Resident) list.get(0) : null);
		}
		return null;
	}

	public boolean isWired() {
		return true;
	}

	public Stand getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Contract> getContracts() {
		return getInstance() == null ? null : new ArrayList<Contract>(getInstance().getContracts());
	}

	/*public List<ProductType> getSellProducts() {
		return getInstance() == null ? null : new ArrayList<ProductType>(getInstance().getSellProducts());
	}*/

	// panel de busque de contribuyente

	public void searchResident() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.getInstance().getCurrentContract().setSubscriber(resident);
			
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			//this.getInstance().getCurrentContract().setSubscriber(null);
			e.printStackTrace();
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		logger.info("ingresa el search by criteria");
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
		this.resident = null;

		//this.getInstance().getCurrentContract().setSubscriber(resident);
	}
	
	
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		residents = null;
		this.resident = null;

		//this.getInstance().getCurrentContract().setSubscriber(resident);
	}

	public void search() {
		logger.info("ingresa el search");
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident1 = (Resident) query.getSingleResult();
			this.getInstance().getCurrentContract().setSubscriber(resident1);
			this.resident = resident1;
			if(resident != null) this.identificationNumber = resident.getIdentificationNumber();
		} catch (Exception e) {
			this.getInstance().getCurrentContract().setSubscriber(null);
			this.resident = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		logger.info("select el resident");
		UIComponent component = event.getComponent();
		this.resident = (Resident) component.getAttributes().get("resident");
		if(resident != null) this.identificationNumber = resident.getIdentificationNumber();
		this.getInstance().getCurrentContract().setSubscriber(resident);
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

	public void initSystemParameter() {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
	}

	public String rentStand() throws Exception {
		initSystemParameter();
		try {

			this.getInstance().setStandStatus(StandStatus.RENTED);
			this.getInstance().getCurrentContract()
					.setSubscriber(this.resident);
			this.getInstance().add(this.getInstance().getCurrentContract());
			ContractType contractType = systemParameterService.materialize(
					ContractType.class, "CONTRACT_TYPE_ID_STAND");
			getInstance().getCurrentContract().setContractType(contractType);
			this.update();
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

	public String rentStand1() throws Exception {
		initSystemParameter();
		try {
			this.getInstance()
					.getCurrentContract()
					.setDescription(
							"Area del Local: " + this.getInstance().getArea()
									+ " m2");

			this.getInstance().setStandStatus(StandStatus.RENTED);
			this.getInstance().getCurrentContract()
					.setSubscriber(this.resident);
			this.getInstance().add(this.getInstance().getCurrentContract());
			ContractType contractType = systemParameterService.materialize(
					ContractType.class, "CONTRACT_TYPE_ID_STAND");
			getInstance().getCurrentContract().setContractType(contractType);
			this.getInstance().setLastPreEmission(new Date());
			this.update();
			return preEmitStand();
		} catch (Exception e) {
			//System.out.println("=====>>>> error 1 " + e);
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}

	public String preEmitStand() {
		//System.out.println("entra a preemitir");
		if (getInstance().getCurrentContract().getSubscriber() == null) {
			String message = Interpolator.instance().interpolate(
					"#{messages['resident.required']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			//System.out.println("no es nulo el contribuyente");
			return "";
		}

		try {
			emissionOrder = marketService.calculatePreEmissionRentStand(this
					.getInstance().getCurrentContract(), this.getInstance(),
					userSession.getFiscalPeriod(), userSession.getPerson());
			calculateTotal(emissionOrder);
		} catch (Exception e) {
			//System.out.println("=====>>>> error 2 " + e);
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
		}

		try {
			marketService.savePreEmissionOrder(emissionOrder);
		} catch (Exception e) {
			//System.out.println(e.toString());
			e.printStackTrace();
		}
		this.getInstance().setHasPreEmit(false);
		this.update();

		emissionOrderHome.setInstance(emissionOrder);
		emissionOrderHome.update();

		return "complete";
	}

	public EmissionOrder calculateTotal(EmissionOrder eOrder) {
		if (eOrder == null)
			eOrder = new EmissionOrder();
		eOrder.setAmount(new BigDecimal(0));
		for (MunicipalBond m : eOrder.getMunicipalBonds()) {
			eOrder.setAmount(eOrder.getAmount().add(m.getValue()));
		}
		return eOrder;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}

	public EmissionOrder getEmissionOrder() {
		return emissionOrder;
	}

	public void setEmissionOrder(EmissionOrder emissionOrder) {
		this.emissionOrder = emissionOrder;
	}

	public EmissionOrderHome getEmissionOrderHome() {
		return emissionOrderHome;
	}

	public void setEmissionOrderHome(EmissionOrderHome emissionOrderHome) {
		this.emissionOrderHome = emissionOrderHome;
	}

	public MarketService getMarketService() {
		return marketService;
	}

	public void setMarketService(MarketService marketService) {
		this.marketService = marketService;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public void wirePreEmitStand() throws Exception {
		getInstance();

		Query q = this.getEntityManager().createNamedQuery(
				"Stand.findByResidentMarket");
		q.setParameter("market", this.getInstance().getMarket());
		q.setParameter("subscriber", this.getInstance().getCurrentContract()
				.getSubscriber());
		q.setParameter("standType", this.getInstance().getStandType());

		preEmitStands = q.getResultList();

		// en el caso de tener mas de un stand se calcula el valor de los demas
		// por el valor del primero
		int i = 1;
		for (Stand s : preEmitStands) {
			if (i == 1) {
				startValuesPreEmission(s);
				s.setEmissionOrder(emissionOrder);
			} else {
				s.setEmissionOrder(generateEmissionOtherStands(s, i));
			}
			i++;
		}
	}

	public EmissionOrder generateEmissionOtherStands(Stand stand, int multiply) {
		//System.out				.println("entra a realizar el calculo de: " + stand.getName());
		if (stand.getCurrentContract().getSubscriber() == null) {
			String message = Interpolator.instance().interpolate(
					"#{messages['resident.required']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			//System.out.println("no es nulo el contribuyente");
			return null;
		}
		try {
			BigDecimal value = this.emissionOrder.getAmount().multiply(
					new BigDecimal(multiply));
			//System.out.println("el valuees::::................. " + value);
			EmissionOrder eoLocal = marketService.generatePreEmissionRentStand(
					stand.getCurrentContract(), stand,
					userSession.getFiscalPeriod(), userSession.getPerson(),
					value);

			return calculateTotal(eoLocal);
		} catch (Exception e) {
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			//System.out.println("error de generate................. " + e);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}

	public void startValuesPreEmission(Stand stand) throws Exception {
		this.getInstance()
				.getCurrentContract()
				.setTotalMonths(
						monthsBetween(this.getInstance().getCurrentContract()
								.getExpirationDate(), this.getInstance()
								.getCurrentContract().getSubscriptionDate()));

		if (marketService == null)
			marketService = ServiceLocator.getInstance().findResource(
					MARKET_SERVICE_NAME);
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);

		calculateTotalMarketStandEmissionOrder(stand);
	}

	public static Long monthsBetween(Date minuend, Date subtrahend) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(minuend);
		int minuendMonth = cal.get(Calendar.MONTH);
		int minuendYear = cal.get(Calendar.YEAR);
		cal.setTime(subtrahend);
		int subtrahendMonth = cal.get(Calendar.MONTH);
		int subtrahendYear = cal.get(Calendar.YEAR);
		return Long
				.parseLong(((minuendYear - subtrahendYear) * cal
						.getMaximum(Calendar.MONTH))
						+ (minuendMonth - subtrahendMonth + (1 * (minuendYear - subtrahendYear)))
						+ "");
	}

	public void calculateTotalMarketStandEmissionOrder(Stand stand)
			throws Exception {

		//System.out.println("entra a realizar el calculo de: ");
		if (stand.getCurrentContract().getSubscriber() == null) {
			String message = Interpolator.instance().interpolate(
					"#{messages['resident.required']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			//System.out.println("no es nulo el contribuyente");
			return;
		}
		try {
			emissionOrder = marketService.calculatePreEmissionRentStand(
					stand.getCurrentContract(), this.getInstance(),
					userSession.getFiscalPeriod(), userSession.getPerson());
			emissionOrder = calculateTotal(emissionOrder);
		} catch (Exception e) {
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
		}
	}

	public String preEmitMarketStand() throws Exception {
		try {
			for (Stand s : preEmitStands) {
				EmissionOrder eOrderLocal = s.getEmissionOrder();
				//System.out.println("el valor generado es: "						+ eOrderLocal.getAmount());
				eOrderLocal
						.getMunicipalBonds()
						.get(0)
						.setGroupingCode(
								this.getInstance().getCurrentContract().getId()
										.toString());
				marketService.savePreEmissionOrder(eOrderLocal);
				s.setLastPreEmission(new Date());
				marketService.updateStand(s);
			}
			String message = Interpolator.instance().interpolate(
					"Pre-Emision Generada", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.INFO,
					message);
		} catch (Exception e) {
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}

		/*
		 * try { emissionOrder .getMunicipalBonds() .get(0) .setGroupingCode(
		 * this.getInstance().getCurrentContract().getId() .toString());
		 * marketService.savePreEmissionOrder(emissionOrder);
		 * this.getInstance().setStandStatus(StandStatus.RENTED);
		 * this.getInstance().setHasPreEmit(false); this.update(); if
		 * (emissionOrder.getMunicipalBonds() != null &&
		 * emissionOrder.getMunicipalBonds().size() > 0) { emissionOrder
		 * .getMunicipalBonds() .get(0) .setGroupingCode(
		 * this.getInstance().getCurrentContract().getId() .toString()); }
		 * emissionOrderHome.setInstance(emissionOrder);
		 * emissionOrderHome.update(); } catch (Exception e) { // TODO
		 * Auto-generated catch block String message =
		 * Interpolator.instance().interpolate(
		 * "#{messages['property.errorGenerateTax']}", new Object[0]);
		 * facesMessages.addToControl("residentChooser",
		 * org.jboss.seam.international.StatusMessage.Severity.ERROR, message);
		 * return "failed"; }
		 */
		return "complete";
	}

	public List<Stand> getPreEmitStands() {
		return preEmitStands;
	}

	public void setPreEmitStands(List<Stand> preEmitStands) {
		this.preEmitStands = preEmitStands;
	}

	/**
	 * Comprobar si ya ha pasado por lo menos un mes para poder pre-emitir
	 * 
	 * @param actual
	 * @param comparationDate
	 * @return
	 */
	public Boolean verifyDatePreEmit(Date actual, Date comparationDate) {
		if (comparationDate != null) {
			int mesActual = Integer.parseInt(new SimpleDateFormat("MM").format(actual));
			int anoActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(actual));

			int mesComparation = Integer.parseInt(new SimpleDateFormat("MM").format(comparationDate));
			int anoComparation = Integer.parseInt(new SimpleDateFormat("yyyy").format(comparationDate));
			
			if (anoActual > anoComparation) {
				return new Boolean(true);
			} else {
				if (anoActual == anoComparation) {
					if (mesActual > mesComparation) {
						return new Boolean(true);
					} else {
						return new Boolean(false);
					}
				} else {
					return new Boolean(false);
				}
			}
		} else {
			return false;
		}

	}

	public Boolean getActivePanelResident() {
		return activePanelResident;
	}

	public void setActivePanelResident(Boolean activePanelResident) {
		this.activePanelResident = activePanelResident;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public void calculateStandValue(){
		/*System.out.println("1--------------- "+this.getInstance().getIsCalculated());
		System.out.println("2--------------- "+this.getInstance().getValueSquareMeter());
		System.out.println("3--------------- "+this.getInstance().getArea());*/
		if(this.getInstance().getIsCalculated()){
			if (this.getInstance().getValueSquareMeter() != null) {
				this.getInstance().setValue(this.getInstance().
						getValueSquareMeter().multiply(new BigDecimal(this.getInstance().getArea())).
						setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		}
	}	

}
