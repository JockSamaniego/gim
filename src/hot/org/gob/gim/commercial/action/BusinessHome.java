package org.gob.gim.commercial.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.action.MunicipalBondHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.commercial.model.BusinessCatalog;
import ec.gob.gim.commercial.model.EconomicActivity;
import ec.gob.gim.commercial.model.Local;
import ec.gob.gim.commercial.model.LocalFeature;
import ec.gob.gim.commercial.model.OperatingLicense;
import ec.gob.gim.common.model.Address;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

@Name("businessHome")
public class BusinessHome extends EntityHome<Business> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log logger;

	private String criteria;

	private List<Resident> residents;

	@In(create = true)
	EconomicActivityHome economicActivityHome;

	@In(create = true)
	LocalFeatureHome localFeatureHome;

	// private MonthType month;
	/*
	 * @In(create = true) LocalHome localHome;
	 */

	private String ownerIdentificationNumber;

	private LocalFeature localFeature;

	public String getOwnerIdentificationNumber() {
		return ownerIdentificationNumber;
	}

	public void setOwnerIdentificationNumber(String ownerIdentificationNumber) {
		this.ownerIdentificationNumber = ownerIdentificationNumber;
	}

	public String getManagerIdentificationNumber() {
		return managerIdentificationNumber;
	}

	public void setManagerIdentificationNumber(String managerIdentificationNumber) {
		this.managerIdentificationNumber = managerIdentificationNumber;
	}

	private String managerIdentificationNumber;

	public void setBusinessId(Long id) {
		setId(id);
	}

	public Long getBusinessId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	private boolean isFirstTime = true;

	public void wire() {
		if (!isFirstTime)
			return;

		if (getInstance() != null && getInstance().getOwner() != null)
			setOwnerIdentificationNumber(getInstance().getOwner().getIdentificationNumber());

		if (getInstance() != null && getInstance().getManager() != null)
			setManagerIdentificationNumber(getInstance().getManager().getIdentificationNumber());

		isFirstTime = false;
		businessTourist = Boolean.FALSE;
	}

	public void addLocal() {
		this.getInstance().add(this.local);
	}

	public void beforeEditLocal(Local l) {
		this.local = l;
		dates();
	}

	public boolean isWired() {
		return true;
	}

	private Charge sanitationCharge;

	public Charge getSanitationCharge() {
		return sanitationCharge;
	}

	public void setSanitationCharge(Charge sanitationCharge) {
		this.sanitationCharge = sanitationCharge;
	}

	public Delegate getSanitationDelegate() {
		return sanitationDelegate;
	}

	public void setSanitationDelegate(Delegate sanitationDelegate) {
		this.sanitationDelegate = sanitationDelegate;
	}

	private Delegate sanitationDelegate;

	public void loadCharge() {
		sanitationCharge = getCharge("DELEGATE_ID_SANITATION");
		if (sanitationCharge != null) {
			for (Delegate d : sanitationCharge.getDelegates()) {
				if (d.getIsActive())
					sanitationDelegate = d;
			}
		}
	}

	public Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class, systemParameter);
		return charge;
	}

	private Local local;

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	private String defaultCity;

	public String getDefaultCity() {
		return defaultCity;
	}

	public void setDefaultCity(String defaultCity) {
		this.defaultCity = defaultCity;
	}

	public String getDefaultCountry() {
		return defaultCountry;
	}

	public void setDefaultCountry(String defaultCountry) {
		this.defaultCountry = defaultCountry;
	}

	private String defaultCountry;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private SystemParameterService systemParameterService;

	public void loadCityAndCountry() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		}
		defaultCity = systemParameterService.findParameter("DEFAULT_CITY");
		defaultCountry = systemParameterService.findParameter("DEFAULT_COUNTRY");
	}

	private FiscalPeriod fiscalPeriod;

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public void createLocal() {
		this.local = new Local();
		if (defaultCity == null || defaultCountry == null)
			loadCityAndCountry();
		Address address = new Address();
		address.setCountry(defaultCountry);
		address.setCity(defaultCity);
		this.local.setAddress(address);
	}

	private String economicActivity;
	private String businessCatalog;

	public String getEconomicActivity() {
		return economicActivity;
	}

	public void setEconomicActivity(String economicActivity) {
		this.economicActivity = economicActivity;
	}

	public Business getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EconomicActivity> getEconomicActivities() {
		return getInstance() == null ? null : new ArrayList<EconomicActivity>(getInstance().getEconomicActivities());
	}

	public List<Local> getLocales() {
		return getInstance() == null ? null : new ArrayList<Local>(getInstance().getLocales());
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public void searchOwner() {
		logger.info("looking for............ {0}", getOwnerIdentificationNumber());
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", getOwnerIdentificationNumber());
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			// resident.add(this.getInstance());
			this.getInstance().setOwner(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			} else {
				setOwnerIdentificationNumber(this.getInstance().getOwner().getIdentificationNumber());
			}

		} catch (Exception e) {
			this.getInstance().setOwner(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void searchManager() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", getManagerIdentificationNumber());
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			// resident.add(this.getInstance());
			this.getInstance().setManager((Person) resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			} else {
				setManagerIdentificationNumber(this.getInstance().getManager().getIdentificationNumber());
			}

		} catch (Exception e) {
			this.getInstance().setManager(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
	}

	public void ownerSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setOwner(resident);
		if (resident != null) {
			setOwnerIdentificationNumber(resident.getIdentificationNumber());
		}
	}

	public void managerSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setManager((Person) resident);
		if (resident != null) {
			setManagerIdentificationNumber(resident.getIdentificationNumber());
		}
	}
	//
	// public void addEconomicActivity(){
	// this.instance.add(economicActivityHome.getInstance());
	// }

	public void addEconomicActivity(EconomicActivity economicActivity) {
		this.instance.add(economicActivity);
	}

	public void removeEconomicActivity(EconomicActivity economicActivity) {
		this.instance.remove(economicActivity);
	}

	public void removeLocal(Local local) {
		this.instance.remove(local);
	}

	public void addBusinessCatalog(BusinessCatalog businessCatalog) {
		this.instance.add(businessCatalog);
	}

	public void removeBusinessCatalog(BusinessCatalog businessCatalog) {
		this.instance.remove(businessCatalog);
	}

	public void selectLocalFeature(Local l) {
		this.local = l;
		if (l.getLocalFeature() == null) {
			localFeature = new LocalFeature();
		} else {
			localFeature = l.getLocalFeature();
		}
	}

	public LocalFeature getLocalFeature() {
		return localFeature;
	}

	public void setLocalFeature(LocalFeature localFeature) {
		this.localFeature = localFeature;
	}

	public void saveOrUpdateLocalFeature() {
		localFeatureHome.setInstance(localFeature);
		localFeatureHome.getInstance().setLocal(local);
		if (this.localFeature.getId() == null) {
			localFeatureHome.persist();
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< esta en null ");
		} else {
			localFeatureHome.update();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>< si existe " + localFeature.getId());
		}
		local.setLocalFeature(localFeature);
		addLocal();
	}

	public String getBusinessCatalog() {
		return businessCatalog;
	}

	public void setBusinessCatalog(String businessCatalog) {
		this.businessCatalog = businessCatalog;
	}

	@SuppressWarnings("unchecked")
	public List<BusinessCatalog> findBusinessCatalog(Object suggest) {
		String pref = (String) suggest;
		Query query = this.getEntityManager().createNamedQuery("BusinessCatalog.findAllActiveByCriteria");
		query.setParameter("criteria", pref);
		return query.getResultList();
	}

	public String getCurrentMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
		return sdf.format(new Date()).toUpperCase();
	}

	// Autor: Jock Samaniego M.
	// Para desactivar los locales desde la vista de obligaciones municipales
	// mediante patente municipal y activos totales.
	public boolean activeLocal = false;
	public static Long myId;

	public boolean isActiveLocal() {
		return activeLocal;
	}

	public void setActiveLocal(boolean activeLocal) {
		this.activeLocal = activeLocal;
	}

	public void isActive() {
		// System.out.println("=>"+activeLocal+"---"+myId+"----active");
		if (myId != null) {
			Query query = getEntityManager().createNamedQuery("Local.findById");
			query.setParameter("id", myId);
			this.local = (Local) query.getSingleResult();
			activeLocal = this.local.getIsActive();
		} else {
			activeLocal = false;
		}
	}

	private static boolean settle1 = Boolean.TRUE;
	private static boolean settle2 = Boolean.TRUE;

	public void settle() {
		if (myId != null) {
			settle1 = activeLocal;
			// System.out.println("-----settle1---"+settle1+"-------");
			// System.out.println("-----settle2---"+settle2+"-------");
			if (settle1 == false && settle2 == false) {
				Query query = getEntityManager().createNamedQuery("Local.findById");
				query.setParameter("id", myId);
				this.local = (Local) query.getSingleResult();
				this.local.setIsActive(activeLocal);
				persist();
				activeLocal = false;
				myId = null;
				settle1 = Boolean.TRUE;
				settle2 = Boolean.TRUE;
			}
		}
		MunicipalBondHome.message = "";
	}

	public void settle2() {
		if (myId != null) {
			settle2 = activeLocal;
			// System.out.println("-----settle2---"+settle2+"-------");
			// System.out.println("-----settle1---"+settle1+"-------");
			if (settle1 == false && settle2 == false) {
				Query query = getEntityManager().createNamedQuery("Local.findById");
				query.setParameter("id", myId);
				this.local = (Local) query.getSingleResult();
				this.local.setIsActive(activeLocal);
				persist();
				activeLocal = false;
				myId = null;
				settle1 = Boolean.TRUE;
				settle2 = Boolean.TRUE;
			}
		}
		MunicipalBondHome.message = "";
	}

	// Autor: Jock Samaniego M.
	// Para activar los locales desactivados desde la vista de obligaciones
	// municipales.
	public void activation(Local l) {
		l.setIsActive(true);
		persist();
	}

	// Autor: Jock Samaniego M.
	// Para crear nuevos locales desde la vista de obligaciones municipales.
	private String navegation;

	public String getNavegation() {
		return navegation;
	}

	public void setNavegation(String navegation) {
		this.navegation = navegation;
	}

	public void navegation(String page) {
		navegation = page;
	}

	public String goPage() {
		persist();
		return navegation;
	}

	// Autor: Jock Samaniego M.
	// Para la emision de permisos de funcionamiento.
	private Date emissionDate;
	private Date validityDate;
	Calendar cal4 = Calendar.getInstance();
	private String fiscalYear = Integer.toString(cal4.get(Calendar.YEAR));
	private String year1 = fiscalYear;
	private String year2 = Integer.toString(Integer.parseInt(fiscalYear) - 1);
	private String year3 = Integer.toString(Integer.parseInt(fiscalYear) - 2);
	private String year4 = Integer.toString(Integer.parseInt(fiscalYear) - 3);
	private String year5 = Integer.toString(Integer.parseInt(fiscalYear) - 4);

	public Date getEmissionDate() {
		return emissionDate;
	}

	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public String getYear1() {
		return year1;
	}

	public void setYear1(String year1) {
		this.year1 = year1;
	}

	public String getYear2() {
		return year2;
	}

	public void setYear2(String year2) {
		this.year2 = year2;
	}

	public String getYear3() {
		return year3;
	}

	public void setYear3(String year3) {
		this.year3 = year3;
	}

	public String getYear4() {
		return year4;
	}

	public void setYear4(String year4) {
		this.year4 = year4;
	}

	public String getYear5() {
		return year5;
	}

	public void setYear5(String year5) {
		this.year5 = year5;
	}

	public void dates() {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(fiscalYear), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		emissionDate = cal.getTime();
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Integer.parseInt(fiscalYear), cal2.getActualMaximum(Calendar.MONTH),
				cal2.getMaximum(Calendar.DAY_OF_MONTH));
		validityDate = cal2.getTime();
	}

	// Autor: Jock Samaniego M.
	// Para controlar el numero de especie en la emision de permisos de
	// funcionamiento.
	// public String printLicense(){
	public String confirmPrintLicense() {
		addLocal();
		addLicense();
		localLicense();
		persist();
		return "false";
	}

	public void printLicense() {
		loadCharge();
		OperatingLicense lic;
		Query query = getEntityManager().createNamedQuery("Local.findByPaperCode");
		query.setParameter("code", codePaper);
		try {
			lic = (OperatingLicense) query.getSingleResult();
		} catch (Exception e) {
			lic = null;
		}
		if (lic == null) {
			validCodePaper = Boolean.TRUE;
		} else {
			/*
			 * message="el número de especie ya existe"; return null;
			 */
			validCodePaper = Boolean.FALSE;
			message = "El número de especie ya ha sido utilizado anteriormente";
		}
	}

	private Boolean validCodePaper;
	private String codePaper;
	private String message;

	public String getCodePaper() {
		return codePaper;
	}

	public void setCodePaper(String codePaper) {
		this.codePaper = codePaper;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getValidCodePaper() {
		return validCodePaper;
	}

	public void setValidCodePaper(Boolean validCodePaper) {
		this.validCodePaper = validCodePaper;
	}

	private OperatingLicense license;

	public OperatingLicense getLicense() {
		return license;
	}

	public void setLicense(OperatingLicense license) {
		this.license = license;
	}

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	// Autor: Jock Samaniego M.
	// Para guardar en la base los permisos de funcionamiento que se van
	// emitiendo.
	public void addLicense() {
		license = new OperatingLicense();
		license.setDate_validity(validityDate);
		license.setDate_emission(emissionDate);
		license.setLocal_code(this.local.getCode());
		license.setLocal_ruc(this.local.getBusiness().getCedruc());
		license.setPaper_code(codePaper);
		license.setResponsible_user(userSession.getUser().getResident().getName());
		license.setResponsible(userSession.getPerson());
		license.setEconomic_activity(economic_Activity.toUpperCase());
		license.setNullified(Boolean.FALSE); 
		license.setResponsible_delegate(this.sanitationDelegate.getName()); 

		EntityManager em = getEntityManager();
		em.persist(license);
		// em.merge(license);
	}

	// Autor: Jock Samaniego M.
	// Para generar los reportes de los locales con y sin permiso de
	// funcionamiento.
	public void localLicense() {
		String actualLicense = this.local.getLicenseyear();
		if (actualLicense == null) {
			this.local.setLicenseyear(fiscalYear);
		} else {
			if (Integer.parseInt(actualLicense) < Integer.parseInt(fiscalYear)) {
				this.local.setLicenseyear(fiscalYear);
			}
		}
	}

	private String licenseCriteria;

	public String getLicenseCriteria() {
		return licenseCriteria;
	}

	public void setLicenseCriteria(String licenseCriteria) {
		this.licenseCriteria = licenseCriteria;
	}

	private String messageLicense = "";

	public String getMessageLicense() {
		return messageLicense;
	}

	public void setMessageLicense(String messageLicense) {
		this.messageLicense = messageLicense;
	}

	private int licenseState = 4;

	public int getLicenseState() {
		return licenseState;
	}

	public void setLicenseState(int licenseState) {
		this.licenseState = licenseState;
	}

	public void licenseSelected() {
		if (licenseCriteria.equals("con permiso actual")) {
			licenseState = 1;
		} else if (licenseCriteria.equals("sin permiso actual")) {
			licenseState = 2;
		} else if (licenseCriteria.equals("inactivos")) {
			licenseState = 0;
		} else {
			licenseState = 4;
		}
		messageLicense = "Total de locales " + licenseCriteria;
	}

	// Autor: Jock Samaniego M.
	// Para clasificar el businessCatalog en turistico y comercial general.
	private boolean businessTourist = Boolean.FALSE;

	public boolean isBusinessTourist() {
		return businessTourist;
	}

	public void setBusinessTourist(boolean businessTourist) {
		this.businessTourist = businessTourist;
	}

	public void businessIsTourist(String businessname) {
		if (businessname.equals("COMERCIAL GENERAL")) {
			businessTourist = Boolean.FALSE;
		} else {
			businessTourist = Boolean.TRUE;
		}

	}

	// Autor: Jock Samaniego M.
	// Para crear nuevas actividades comerciales.
	private String addActivity;
	private String securityKey;
	private String activityMessage = "";
	private BusinessCatalog commercialActivity;
	private boolean tourist = Boolean.FALSE;

	public String getAddActivity() {
		return addActivity;
	}

	public void setAddActivity(String addActivity) {
		this.addActivity = addActivity;
	}

	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}

	public String getActivityMessage() {
		return activityMessage;
	}

	public void setActivityMessage(String activityMessage) {
		this.activityMessage = activityMessage;
	}

	public BusinessCatalog getCommercialActivity() {
		return commercialActivity;
	}

	public void setCommercialActivity(BusinessCatalog commercialActivity) {
		this.commercialActivity = commercialActivity;
	}

	public boolean isTourist() {
		return tourist;
	}

	public void setTourist(boolean tourist) {
		this.tourist = tourist;
	}

	public void createdEconomicActivity() {
		if (addActivity == null || addActivity == "") {
			activityMessage = "falló el proceso. Debe ingresar el nombre de la actividad";
		} else {
			Query query = getEntityManager().createNamedQuery("BusinessCatalog.findByName");
			query.setParameter("name", addActivity.toUpperCase());
			List<String> rate = new ArrayList<String>();
			rate = query.getResultList();
			// System.out.println("------------------"+rate.size());
			if (rate.size() > 0) {
				activityMessage = "falló el proceso. La actividad comercial ya existe";
			} else if (securityKey == null || securityKey == "") {
				activityMessage = "falló el proceso. Debe ingresar la clave de seguridad";
			} else if (securityKey.equals("p2016")) {
				// System.out.println("------------------------------------"+addActivity);
				commercialActivity = new BusinessCatalog();
				commercialActivity.setIsActive(true);
				commercialActivity.setIsInFeature(true);
				commercialActivity.setName(addActivity.toUpperCase());
				commercialActivity.setIsTourist(tourist);
				EntityManager em = getEntityManager();
				em.persist(commercialActivity);
				persist();
				activityMessage = "actividad comercial creada con éxito.";
			} else {
				activityMessage = "falló el proceso. Clave incorrecta";
			}

		}
	}

	// Autor: Jock Samaniego M.
	// Para uso de parroquias por defecto en permisos de funcionamiento

	private List<String> parishNames;
	private String economic_Activity;

	public List<String> getParishNames() {
		return parishNames;
	}

	public void setParishNames(List<String> parishNames) {
		this.parishNames = parishNames;
	}

	public String getEconomic_Activity() {
		return economic_Activity;
	}

	public void setEconomic_Activity(String economic_Activity) {
		this.economic_Activity = economic_Activity;
	}

	public List<String> addParish() {
		parishNames = new ArrayList<String>(Arrays.asList("EL SAGRARIO", "SAN SEBASTIAN", "EL VALLE", "SUCRE",
				"PUNZARA", "CARIGAN", "CHANTACO", "CHIQUIRIBAMBA", "EL CISNE", "GUALEL", "JIMBILLA", "MALACATOS",
				"QUINARA", "SAN LUCAS", "SAN PEDRO DE VILCABAMBA", "SANTIAGO", "TAQUIL", "VILCABAMBA", "YANGANA"));
		Collections.sort(parishNames);
		return parishNames;
	}
}