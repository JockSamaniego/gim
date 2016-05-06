package org.gob.gim.concession.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.concession.facade.ConcessionGroupService;
import org.gob.gim.revenue.action.ContractHome;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.consession.model.ConcessionClasification;
import ec.gob.gim.consession.model.ConcessionGroup;
import ec.gob.gim.consession.model.ConcessionGroupEmission;
import ec.gob.gim.consession.model.ConcessionItem;
import ec.gob.gim.consession.model.ConcessionPlace;
import ec.gob.gim.consession.model.ConcessionPlaceType;
import ec.gob.gim.consession.model.ConcessionStatus;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.waterservice.model.MonthType;

@Name("concessionGroupHome")
public class ConcessionGroupHome extends EntityHome<ConcessionGroup> {

	@In(create = true)
	ConcessionClasificationHome concessionClasificationHome;

	@In(create = true)
	ConcessionPlaceHome concessionPlaceHome;

	@In(create = true)
	ConcessionItemHome concessionItemHome;

	@In(create = true)
	ContractHome contractHome;

	@In
	private FacesMessages facesMessages;

	@Logger
	Log logger;

	@In
	UserSession userSession;

	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;

	private Long residentId;

	private Integer year = -1;
	private MonthType month;
	private boolean newLoad = false;
	private Boolean isEditing = false;
	private Boolean readyForPrint;

	private List<ConcessionItem> items;
	private ConcessionPlaceType concessionPlaceType;

	private ConcessionPlace concessionPlace = new ConcessionPlace();

	private ConcessionItem concessionItem = new ConcessionItem();

	private ConcessionGroupService concessionGroupService;
	// private SystemParameterService systemParameterService;
	public static String CONCESSIONGROUP_SERVICE_NAME = "/gim/ConcessionGroupService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private EmissionOrder eo;
	
	private String generalConcept;
	
	private Date startDate = new Date();
	private Date endDate = new Date();
	private List<ConcessionGroupEmission> pemissions;
	private List<ConcessionPlaceType> cPlaceTypes; 

	public void setConcessionGroupId(Long id) {
		setId(id);
	}

	public Long getConcessionGroupId() {
		return (Long) getId();
	}

	@Override
	protected ConcessionGroup createInstance() {
		ConcessionGroup concessionGroup = new ConcessionGroup();
		return concessionGroup;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		ConcessionClasification clasification = concessionClasificationHome
				.getDefinedInstance();
		if (clasification != null) {
			getInstance().setClasification(clasification);
		}
		/*
		 * if (getResidentId() != null) {
		 * this.getInstance().getCurrentContract()
		 * .setSubscriber(searchResidentById()); this.resident =
		 * this.getInstance().getCurrentContract().getSubscriber();
		 * if(this.resident != null)this.identificationNumber =
		 * this.resident.getIdentificationNumber(); }
		 */
	}

	public boolean isWired() {
		return true;
	}

	public ConcessionGroup getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<ConcessionPlace> getPlaces() {
		return getInstance() == null ? null : new ArrayList<ConcessionPlace>(
				getInstance().getPlaces());
	}

	@Factory("concessionClasifications")
	@SuppressWarnings("unchecked")
	public List<ConcessionClasification> findConcessionClasification() {
		Query query = getPersistenceContext().createNamedQuery(
				"ConcessionClasification.findAll");
		return query.getResultList();
	}

	@Factory("concessionPlaceTypes")
	@SuppressWarnings("unchecked")
	public List<ConcessionPlaceType> findConcessionPlaceType() {
		Query query = getPersistenceContext().createNamedQuery(
				"ConcessionPlaceType.findAll");
		return query.getResultList();
	}

	public List<ConcessionPlaceType> findConcessionPlaceTypeByPlace() {
		Query query = this.getEntityManager().createNamedQuery(
				"ConcessionPlace.findPlaceTypeByGroup");
		query.setParameter("cgId", this.getInstance().getId());

		List<ConcessionPlaceType> placeTypes = query.getResultList();

		List<ConcessionPlaceType> placeTypesNew = new ArrayList<ConcessionPlaceType>();
		for (ConcessionPlaceType cpt : placeTypes) {
			if (!placeTypesNew.contains(cpt)) {
				placeTypesNew.add(cpt);
			}
		}
		this.cPlaceTypes = placeTypesNew;
		return placeTypesNew;
	}

	public ConcessionClasificationHome getConcessionClasificationHome() {
		return concessionClasificationHome;
	}

	public void setConcessionClasificationHome(
			ConcessionClasificationHome concessionClasificationHome) {
		this.concessionClasificationHome = concessionClasificationHome;
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

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}

	public void searchResident() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			this.resident = (Resident) query.getSingleResult();
			// this.getInstance().getCurrentContract().setSubscriber(resident);
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}
		} catch (Exception e) {
			this.resident = null;
			e.printStackTrace();
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		this.residents = null;
		this.resident = null;
		this.identificationNumber = null;
		this.concessionPlace = new ConcessionPlace();
	}

	public void clearNewConcessionItemPanel() {
		clearSearchResidentPanel();
		this.concessionPlace = new ConcessionPlace();
		this.concessionItemHome.setInstance(new ConcessionItem());
		this.generalConcept=null;
		isEditing = false;
	}

	public void residentSelectedListener(ActionEvent event) {
		// logger.info("select el resident");
		UIComponent component = event.getComponent();
		this.resident = (Resident) component.getAttributes().get("resident");
		if (resident != null)
			this.identificationNumber = resident.getIdentificationNumber();
		// this.getInstance().getCurrentContract().setSubscriber(resident);
	}

	public void addConcessionPlace() {
		if (this.resident != null) {
			Contract c = generateContract();

			this.concessionPlace.add(c);
			this.concessionPlace.setCurrentContract(c);
			this.concessionPlace.setResident(resident);
			this.getInstance().add(concessionPlace);
		}
		clearSearchResidentPanel();
		this.concessionPlace = new ConcessionPlace();
	}

	/**
	 * crear el nuevo place y persiste el item con un mes anterior
	 */
	public void persistPlaceItem() {
		int monthInt = month.getMonthInt() - 1;
		int yearInt = year;
		// en el caso q sea enero el año debe disminuir en 1 y el mes es 12
		if (monthInt == 0) {
			yearInt = yearInt - 1;
			monthInt = 12;
		}
		MonthType monthType = month.getMonthType(monthInt);

		if (this.resident != null) {
			Contract c = generateContract();

			this.concessionPlace.add(c);
			this.concessionPlace.setCurrentContract(c);
			this.concessionPlace.setResident(resident);
			this.getInstance().add(concessionPlace);
		}
		this.update();

		this.concessionItemHome.getInstance().setIsFirst(Boolean.TRUE);
		this.concessionItemHome.getInstance().setYear(yearInt);
		this.concessionItemHome.getInstance().setMonthType(monthType);
		this.concessionItemHome.getInstance().setMonth(monthInt);
		this.concessionItemHome.getInstance().setPlace(concessionPlace);
		this.concessionItemHome.persist();

		generateRecords(this.concessionPlaceType);
	}

	/**
	 * crear el nuevo place y persiste el item con un mes anterior
	 */
	public void updatePlacePersistItem() {
		int monthInt = month.getMonthInt() - 1;
		int yearInt = year;
		// en el caso q sea enero el año debe disminuir en 1 y el mes es 12
		if (monthInt == 0) {
			yearInt = yearInt - 1;
			monthInt = 12;
		}
		MonthType monthType = month.getMonthType(monthInt);
		
		if (this.resident != null) {

			System.out
					.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>entra a crear un contrato");

			Contract c = generateContract();

			this.concessionItem.setIsFirst(Boolean.TRUE);
			this.concessionItem.setYear(yearInt);
			this.concessionItem.setMonthType(monthType);
			this.concessionItem.setMonth(monthInt);

			//this.concessionPlaceHome.setInstance(concessionPlace);
			this.concessionPlaceHome.getInstance().add(c);
			this.concessionPlaceHome.getInstance().add(concessionItem);
			this.concessionPlaceHome.getInstance().setConcessionStatus(
					ConcessionStatus.RENTED);
			this.concessionPlaceHome.getInstance().setIsActive(Boolean.TRUE);
			this.concessionPlaceHome.getInstance().setCurrentContract(c);
			this.concessionPlaceHome.getInstance().setResident(resident);
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>el id "+this.concessionPlaceHome.getInstance().getId());
			this.concessionPlaceHome.update();
		}
		// this.concessionItemHome.getInstance().setPlace(this.concessionPlaceHome.getInstance());
		// this.concessionItemHome.persist();

		generateRecords(this.concessionPlaceType);
	}

	public void updatePlaceItem() {

		this.concessionPlaceHome.setInstance(concessionPlace);
		this.concessionPlaceHome.update();

		this.concessionItemHome.getInstance().setPlace(concessionPlace);
		this.concessionItemHome.update();

		generateRecords(this.concessionPlaceType);
	}

	public void noRenovateAction(ConcessionItem ci) {

		this.contractHome.setInstance(ci.getPlace().getCurrentContract());
		this.contractHome.getInstance().setExpirationDate(new Date());
		this.contractHome.update();

		this.concessionItemHome.setInstance(ci);
		this.concessionItemHome.getInstance().setIsOldItem(Boolean.TRUE);
		this.concessionItemHome.getInstance().getPreviousConcessionItem().setIsOldItem(Boolean.TRUE);
		this.concessionItemHome.update();
		logger.warn("entra  a ahcer el updae de item>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
		/*
		 * this.concessionItemHome.setInstance(ci);
		 * this.concessionItemHome.getInstance().set
		 */

		this.concessionPlaceHome.setInstance(ci.getPlace());
		this.concessionPlaceHome.getInstance().setConcessionStatus(
				ConcessionStatus.FREE);
		this.concessionPlaceHome.getInstance().setIsActive(Boolean.FALSE);
		this.concessionPlaceHome.getInstance().setCurrentContract(null);
		this.concessionPlaceHome.getInstance().setResident(null);
		this.concessionPlaceHome.update();

		generateRecords(this.concessionPlaceType);
	}

	public void editConcessionPlace(ConcessionPlace cp) {
		this.concessionPlace = cp;
		this.resident = cp.getResident();
		this.identificationNumber = resident.getIdentificationNumber();
	}

	public void editItemPlace(ConcessionItem ci) {

		this.concessionItemHome.setInstance(ci);
		this.concessionPlace = ci.getPlace();
		this.resident = concessionPlace.getResident();
		this.identificationNumber = resident.getIdentificationNumber();
	}

	public void useConcessionPlace(ConcessionPlace cp) {
		isEditing = true;
		//this.concessionPlace = cp;
		this.concessionPlaceHome.setInstance(cp);
		this.concessionItem = new ConcessionItem();
		// this.concessionItemHome.setInstance(new ConcessionItem());
	}

	private Contract generateContract() {
		Contract c = new Contract();
		c.setCreationDate(new Date());
		c.setDescription("Concesión: ");
		c.setSubscriber(resident);
		return c;
	}
	
	@In
	EntityManager entityManager;
	
	public void updateGeneralConcept(){
		List<Long> ids=new ArrayList<Long>();
		for (ConcessionItem ci : items) {
			ids.add(ci.getId());
		}
		String idss=ids.toString().replace("[", "").replace("]", "");
		String sql="update ConcessionItem set concept= '"+this.generalConcept+"' where id in ("+idss+");";
		// joinTransaction();
		Query q = entityManager.createNativeQuery(sql);
		q.executeUpdate();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> jeje\n " + sql);
		entityManager.flush();
		//generateRecords(this.concessionPlaceType);
	}

	public ConcessionPlace getConcessionPlace() {
		return concessionPlace;
	}

	public void setConcessionPlace(ConcessionPlace concessionPlace) {
		this.concessionPlace = concessionPlace;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public MonthType getMonth() {
		return month;
	}

	public void setMonth(MonthType month) {
		this.month = month;
	}

	public boolean isNewLoad() {
		return newLoad;
	}

	public void setNewLoad(boolean newLoad) {
		this.newLoad = newLoad;
	}

	public Boolean getReadyForPrint() {
		return readyForPrint;
	}

	public void setReadyForPrint(Boolean readyForPrint) {
		this.readyForPrint = readyForPrint;
	}

	public List<ConcessionItem> getItems() {
		return items;
	}

	public void setItems(List<ConcessionItem> items) {
		this.items = items;
	}

	public void cleanForPrint() {
		System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
	}

	public void generateInformationData() {

	}

	/**
	 * Busca los concession places por el concession placetype ya estan
	 * filtrados por el tipo de rubro que deseo
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findPlaces(ConcessionPlaceType cpt) {
		if (cpt == null)
			return new ArrayList<Long>();
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionPlace.findIdsByGroupAndPlaceType");
		q.setParameter("cgId", this.getInstance().getId());
		q.setParameter("isActive", Boolean.TRUE);
		q.setParameter("idCpt", cpt.getId());
		return q.getResultList();

	}

	/**
	 * Items para un rubro y las ubicaciones seleccionadas
	 * 
	 * @param places
	 * @param cpt
	 * @param _year
	 * @param _month
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findOldConcessionItemIds(List<Long> places, int _year,
			int _month) {
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionItem.findOldIdsByYearMonth");
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setParameter("isActive", Boolean.TRUE);
		q.setParameter("places", places);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Long> findConcessionItemIds(List<Long> places, int _year,
			int _month) {
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionItem.findIdsByYearMonth");
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setParameter("isActive", Boolean.TRUE);
		q.setParameter("places", places);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Long> findMissingItemIds(List<Long> places,
			List<Long> itemsAlredyGenerated, int _year, int _month) {
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionItem.findMissingItemsByYearMonth");
		q.setParameter("year", _year);
		q.setParameter("month", _month);
		q.setParameter("isActive", Boolean.TRUE);
		q.setParameter("places", places);
		q.setParameter("itemsAlreadyGenerated", itemsAlredyGenerated);
		return q.getResultList();
	}

	public void loadRecords(ConcessionPlaceType cpt) {
		this.concessionPlaceType = cpt;
		generateRecords(cpt);
	}

	public void generateRecords(ConcessionPlaceType cpt) {
		if (month != null) {
			int monthInt = month.getMonthInt();
			int yearInt = year;

			List<Long> placesIds = findPlaces(cpt);
			// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> los lugares son: "+placesIds);
			if (placesIds.size() > 0) {

				List<Long> itemsAlreadyGenerated = findOldConcessionItemIds(
						placesIds, year, monthInt);// sacar1
				// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> sacar1 : "+itemsAlreadyGenerated);
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> los ya generados son "
						+ itemsAlreadyGenerated.size()
						+ " xxx "
						+ yearInt
						+ " - " + monthInt);
				// regreso un mes para comprobar los anteriores en caso de enero
				// regreso a diciembre del anterior anio
				monthInt = monthInt - 1;
				if (monthInt == 0) {
					yearInt = yearInt - 1;
					monthInt = 12;
				}

				List<Long> missingItems;
				// regreso un mes atras para sacar los faltantes y generar los
				// nuevos
				if (itemsAlreadyGenerated.size() > 0) {
					missingItems = findMissingItemIds(placesIds,
							itemsAlreadyGenerated, yearInt, monthInt);// sacar2
					logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> el total faltente es "
							+ missingItems.size());
				} else {
					missingItems = findConcessionItemIds(placesIds, yearInt,
							monthInt);
					logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> No ha sido creado ninguna auns los nuevo son: "
							+ missingItems.size());
				}
				if (missingItems.size() > 0) {
					generateNewRecords(findByIds(missingItems));
				}
				// cargo los del mes seleccionado porq ya los creo anteriormente
				findItemsByPlacesIds(placesIds, year, month.getMonthInt());
			} else {
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> No existen ubicaciones "
						+ placesIds.size());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void findItemsByPlacesIds(List<Long> placesIds, int yearInt,
			int monthInt) {
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionItem.findByYearMonth");
		q.setParameter("year", yearInt);
		q.setParameter("month", monthInt);
		q.setParameter("isActive", Boolean.TRUE);
		q.setParameter("places", placesIds);
		this.items = q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ConcessionItem> findByIds(List<Long> itemIds) {
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionItem.findByIds");
		q.setParameter("itemIds", itemIds);
		return q.getResultList();
	}

	/*
	 * public void loadFinalRecords() { if (month != null) { int monthInt =
	 * month.getMonthInt() - 1; int yearInt = year; /*if (monthInt == 0) {
	 * yearInt = yearInt - 1; monthInt = 12; }
	 */
	// no consultar otra vez
	/*
	 * List<Long> placesId = findPlaces(this.concessionPlaceType); Query q =
	 * this.getEntityManager().createNamedQuery(
	 * "ConcessionItem.findByYearMonth"); q.setParameter("year", yearInt);
	 * q.setParameter("month", monthInt); q.setParameter("isActive",
	 * Boolean.TRUE); q.setParameter("places", placesId);
	 * q.setParameter("cptID", this.concessionPlaceType.getId());
	 * generateNewRecords(q.getResultList()); } }
	 */

	/**
	 * Genera los registros para el mes actual <br/>
	 * 1. la consulta debe ser de un mes atras para poder generar el registro
	 * actual <br/>
	 * 2. en el caso q sea enero el año debe disminuir en 1 y el mes es 12
	 */
	/*
	 * @SuppressWarnings("unchecked") public void
	 * loadRecords(ConcessionPlaceType cpt) { this.concessionPlaceType = cpt; if
	 * (month != null) { int monthInt = month.getMonthInt() - 1; int yearInt =
	 * year; if (monthInt == 0) { yearInt = yearInt - 1; monthInt = 12; }
	 * List<Long> placesId = findPlaces(cpt); Query q =
	 * this.getEntityManager().createNamedQuery(
	 * "ConcessionItem.findByYearMonth"); q.setParameter("year", yearInt);
	 * q.setParameter("month", monthInt); q.setParameter("isActive",
	 * Boolean.TRUE); q.setParameter("places", placesId);
	 * q.setParameter("cptID", cpt.getId());
	 * generateNewRecords(q.getResultList()); } }
	 */

	public void generateNewRecords(List<ConcessionItem> itemsOld) {
		ConcessionItem itemNew;
		for (ConcessionItem itemOld : itemsOld) {
			itemNew = new ConcessionItem();
			itemNew.setAddress(itemOld.getAddress());
			itemNew.setAmount(itemOld.getAmount());
			itemNew.setConcept(itemOld.getConcept());
			itemNew.setExplanation(itemOld.getExplanation());
			itemNew.setMonth(month.getMonthInt());
			itemNew.setMonthType(month);
			itemNew.setPlace(itemOld.getPlace());
			itemNew.setProductType(itemOld.getProductType());
			itemNew.setYear(year);
			itemNew.setPreviousConcessionItem(itemOld);
			this.concessionItemHome.setInstance(itemNew);
			this.concessionItemHome.persist();
		}
	}

	public ConcessionPlaceType getConcessionPlaceType() {
		return concessionPlaceType;
	}

	public void setConcessionPlaceType(ConcessionPlaceType concessionPlaceType) {
		this.concessionPlaceType = concessionPlaceType;
	}

	@SuppressWarnings("unchecked")
	public List<ConcessionPlace> findFreeConcessionPlaces() {
		if (this.concessionPlaceType == null)
			return null;
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionPlace.findByGroupAndPlaceType");
		q.setParameter("cgId", this.getInstance().getId());
		q.setParameter("isActive", Boolean.FALSE);
		q.setParameter("idCpt", this.concessionPlaceType.getId());
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> la nueva cantidad es de libre es = "
				+ q.getResultList().size());
		return q.getResultList();
	}

	public Boolean getIsEditing() {
		return isEditing;
	}

	public void setIsEditing(Boolean isEditing) {
		this.isEditing = isEditing;
	}

	public ConcessionItem getConcessionItem() {
		return concessionItem;
	}

	public void setConcessionItem(ConcessionItem concessionItem) {
		this.concessionItem = concessionItem;
	}

	private ConcessionGroupEmission cgEmission;
	
	public ConcessionGroupEmission initEmissionTrace(){
		cgEmission =new ConcessionGroupEmission();
		cgEmission.setConcessionGroup(this.getInstance());
		cgEmission.setConcessionPlaceType(concessionPlaceType);
		cgEmission.setDate(new Date());
		cgEmission.setExplanation("Tipo : "+concessionPlaceType.getEntry().getCode());
		cgEmission.setHasPreEmit(Boolean.TRUE);
		cgEmission.setMonth(month.getMonthInt());
		cgEmission.setMonthType(month);
		cgEmission.setYear(year);
		return cgEmission;
	}
	
	/**
	 * Comprueba que todas las cuentas esten acivas para poder emitir
	 * No se emite hasta q todas esten corregidas
	 * @return
	 */
	public boolean checkEntries(){
		boolean isChecked = true;
		for (ConcessionPlaceType cpt : cPlaceTypes) {
			Entry e = cpt.getEntry();
			if(!e.getIsActive()){
				isChecked=false;
				return isChecked;
			}
		}
		return isChecked;
	}
	
	public String emitConcessionGroup() {
		if (checkEntries()) {
			if (concessionGroupService == null)
				concessionGroupService = ServiceLocator.getInstance().findResource(CONCESSIONGROUP_SERVICE_NAME);
			try {
				eo = concessionGroupService.generateEmissionOrder(
						this.getInstance(), items, userSession.getFiscalPeriod(), userSession.getPerson(),
						year, month.getMonthInt(), "parte de la descripcion");
				if (eo != null) {
					concessionGroupService.saveEmissionOrder(eo,
							initEmissionTrace(), true);
					String message = Interpolator.instance().interpolate(
							"Concesion " + this.getInstance().getName()
									+ " ha sido emitido, ", new Object[0]);
					facesMessages
							.addToControl(
									"residentChooser",
									org.jboss.seam.international.StatusMessage.Severity.INFO,
									message);
					return "completed";
				}

				return null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String message = Interpolator.instance().interpolate(
						"#{messages['property.errorGenerateTax']}",
						new Object[0]);
				facesMessages
						.addToControl(
								"residentChooser",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
				return null;
			}
		} else {
			String message = Interpolator.instance().interpolate(
					"Existen cuentas inactivas", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
		// return "completed";
	}

	public String getGeneralConcept() {
		return generalConcept;
	}

	public void setGeneralConcept(String generalConcept) {
		if (generalConcept != null) {
			this.generalConcept = generalConcept.toUpperCase();	
		}
	}
	
	
	public void findPerformedEmission() {
		Query q = this.getEntityManager().createNamedQuery(
				"ConcessionGroupEmission.findByDate");
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		pemissions = q.getResultList();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<ConcessionGroupEmission> getPemissions() {
		return pemissions;
	}

	public void setPemissions(List<ConcessionGroupEmission> pemissions) {
		this.pemissions = pemissions;
	}

}
