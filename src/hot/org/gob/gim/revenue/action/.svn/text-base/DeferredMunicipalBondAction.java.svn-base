package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.view.DeferredMunicipalBondItem;
import org.gob.gim.revenue.view.DeferredMunicipalBondList;
import org.gob.gim.revenue.view.EntryValueItem;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;

@Name("deferredMunicipalBond")
@Scope(ScopeType.CONVERSATION)
public class DeferredMunicipalBondAction extends EntityController{
	
	private static final long serialVersionUID = 1394436339054364341L;
	private String criteriaResident;
	private String criteriaEntry;
	private String identificationNumber;
	private String entryCode; 
	
	private List<Resident> residents;
	private List<Entry> entries;
	
	private List<DeferredMunicipalBondItem> deferredMunicipalBondItems;
	
	private Resident resident;
	
	private Date serviceCalculationDate;
	private Boolean isDateEditable;
	
	private Entry entry;
	private String adjunctUri;
	
	private DeferredMunicipalBondList deferredMunicipalBondList;
	
	public static String ADJUNCT_PREFIX = "/revenue/adjunct/";
	public static String ADJUNCT_SUFIX = ".xhtml";
	public static String EMPTY_ADJUNCT_URI = ADJUNCT_PREFIX + "Empty" + ADJUNCT_SUFIX;

	@Logger 
	Log logger;
	
	@In(scope=ScopeType.SESSION, value="userSession")
	UserSession userSession;
	
	@In(create = true)
	AdjunctHome adjunctHome;

	public Boolean futureIssuance = Boolean.FALSE;
	
	public DeferredMunicipalBondAction() {
		super();
		adjunctUri = EMPTY_ADJUNCT_URI;
		serviceCalculationDate = Calendar.getInstance().getTime();
		isDateEditable = Boolean.FALSE;
		entry = new Entry();
		deferredMunicipalBondItems = new ArrayList<DeferredMunicipalBondItem>();
	}

	public void searchResident() {
		//logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try{
			//Resident resident = (Resident) query.getSingleResult();
			resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			
			//this.resident = resident;
			deferredMunicipalBondItems.clear();
			setEntry(null);
			setEntryCode(null);
			adjunctUri = EMPTY_ADJUNCT_URI;
			
			if (resident.getId() == null){
				addFacesMessageFromResourceBundle("resident.notFound");
			}
			
		}
		catch(Exception e){
			deferredMunicipalBondItems.clear();
			setEntry(null);
			setEntryCode(null);
			this.setResident(null);
			adjunctUri = EMPTY_ADJUNCT_URI;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	
	private String completeEntryCode(String code){
		StringBuffer codeBuffer = new StringBuffer(code.trim());
		while (codeBuffer.length() < 5){
			codeBuffer.insert(0,'0');
		}
		return codeBuffer.toString();
	}
	
	public void searchEntry() {
		//logger.info("Entry CHOOSER CODE... "+ entryCode);
		if (entryCode != null){
			entryCode = completeEntryCode(entryCode);	
			try {
				Query query = getEntityManager().createNamedQuery("Entry.findByCodeAndDirectEmissionAndActive");
//				RevenueService revenueService = ServiceLocator.getInstance().findResource("/gim/RevenueService/local"); 
//				Entry entry = revenueService.findEntryByCode(entryCode);
				
				query.setParameter("code", entryCode);
				
				List<Entry> entries = query.getResultList();
				
				Entry entry = null;
				if(entries.size()>0){
					entry = entries.get(0);
				}
				
				if (entry != null){
					logger.info("Entry found... "+ entry.getName());
					this.entry = entry;
					if(entry.getAccount() == null){
						setEntryCode(entry.getCode());
					}else{
						setEntryCode(entry.getAccount().getAccountCode());
					}
				}else{
					String message = Interpolator.instance().interpolate(
								"#{messages['revenue.incorrectCodeOrNoHaveDirectEmission']}", new Object[0]);
						facesMessages.addToControl("entryChooser",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
					
					this.entry = null;
					adjunctUri = EMPTY_ADJUNCT_URI;
				}
			
			}
			catch(Exception e){
				entry = new Entry();
				adjunctUri = EMPTY_ADJUNCT_URI;
				//entryCode = "";
				addFacesMessageFromResourceBundle("entry.notFound");
			}
		}
	}
	
	@In
	FacesMessages facesMessages;
	
	
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria(){
		logger.info("SEARCH RESIDENT BY CRITERIA "+this.criteriaResident);
		if (this.criteriaResident != null && !this.criteriaResident.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteriaResident);
			residents = query.getResultList();
		}
	}
	
	public void searchEntryByCriteria(){
		logger.info("SEARCH Entry BY CRITERIA "+this.criteriaEntry);
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Entry.findByCriteriaAndDirectEmission");
			query.setParameter("criteria", this.criteriaEntry);			
			entries = query.getResultList();
		}
	}

	
	public void clearSearchResidentPanel(){
		this.setCriteriaResident(null);
		residents = null;
	}
	
	public void clearSearchEntryPanel(){
		this.setCriteriaEntry(null);
		entries = null;
	}
	
	public void residentSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
		deferredMunicipalBondItems.clear();
		setEntry(null);
		setEntryCode(null);
		adjunctUri = EMPTY_ADJUNCT_URI;
	}
	
	public void entrySelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		setEntry(entry);
		if(entry.getAccount() == null){
			setEntryCode(entry.getCode());
		}else{
			setEntryCode(entry.getAccount().getAccountCode());
		}
	}
	
	public void populateEntry(Entry entry){
		logger.info("=====> Ingreso a fijar entry #0", entry.getCode());
		
		DeferredMunicipalBondItem  deferredMunicipalBondItem = 
			new DeferredMunicipalBondItem(entry, new BigDecimal(0.0), 1, this.serviceCalculationDate, this.serviceCalculationDate);
		
		this.deferredMunicipalBondItems.add(deferredMunicipalBondItem);
		Adjunct adjunct = createAdjunct(entry);
		if (adjunct != null) {
			adjunctHome.setId(adjunct.getId());
			adjunctHome.setInstance(adjunct);
		}
		
		logger.info("=====> deferredMunicipalBondItems.size #0", deferredMunicipalBondItems.size());
		
	}
	
	public void addEntryAction(){
		logger.info("In add entry....");
		populateEntry(entry);
		entry = new Entry();
		setEntryCode(null);		
	}
	
	public void remove(DeferredMunicipalBondItem deferredMunicipalBondItem){
		logger.info("In remove DeferredMunicipalBondItem....");
		this.deferredMunicipalBondItems.remove(deferredMunicipalBondItem);
	}
		
	/*private MunicipalBond buildMunicipalBond(Entry entry, List<EntryDefinition> entryDefinitionList, 
			BigDecimal value, TimePeriod timePeriod, Date serviceDate, Date expirationDate,
			FiscalPeriod fiscalPeriod){
		
		Date now = Calendar.getInstance().getTime();
		
		MunicipalBond municipalBond = new MunicipalBond();
		municipalBond.setResident(resident);
		municipalBond.setEntry(entry);
		municipalBond.setTimePeriod(timePeriod);
		municipalBond.setCreationDate(now);
		municipalBond.setEmisionDate(now);
		municipalBond.setServiceDate(serviceDate);
		municipalBond.setExpirationDate(expirationDate);
		municipalBond.setFiscalPeriod(fiscalPeriod);
		municipalBond.setMunicipalBondType(MunicipalBondType.CREDIT_ORDER);
		
		buildItems(entryDefinitionList, municipalBond);
		
		Item item = municipalBond.getItems().get(0);
		item.setAmount(new BigDecimal(1));
		item.setValue(value);
		item.calculateTotal();
		
		logger.info("Item a padre.... nombre: #0, cantidad: #1, value: #2, total: #2 ", item.getEntry().getName(), item.getAmount(), item.getValue(), item.getTotal() );
		
		municipalBond.setSubTotal(item.getTotal());
		
		this.fireRules(municipalBond);
		
		return municipalBond; 
	}*/
	
	
	public void previewEmitAction(){	
		
		System.out.println("...................... el valor es: "+futureIssuance);
		futureIssuance = Boolean.FALSE;
		/*if(futureIssuance){
			deferredFutureEmission();
		}else{*/
			deferredNormalEmission();
		//}
	}
	
	public void previewFutureEmitAction(){	
		
		System.out.println("...................... el valor es: "+futureIssuance);
		futureIssuance = Boolean.TRUE;
		//if(futureIssuance){
			deferredFutureEmission();
		/*}else{
			deferredNormalEmission();
		}*/
	}
	
	//private SystemParameterService systemParameterService;
	//public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	/**
	 * emision para anios futuros
	 */
	public void deferredFutureEmission(){
		
		/*if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		}
		
		MunicipalBondStatus mbs = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_FUTURE");
		*/		
		
		this.deferredMunicipalBondList = new DeferredMunicipalBondList();

		FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();
		Person emitter = userSession.getPerson();

		for (DeferredMunicipalBondItem dmbi : this.deferredMunicipalBondItems) {
			Entry entry = dmbi.getEntry();
			BigDecimal value = null;
			RevenueService revenueService = ServiceLocator.getInstance().findResource("/gim/RevenueService/local");
			if (entry.getIsValueEditable())
				value = dmbi.getValue().divide(new BigDecimal(dmbi.getFee()), 3, RoundingMode.HALF_EVEN);
			if (entry.getIsAmountEditable()) {
				EntryValueItem auxEntryItem = new EntryValueItem();
				auxEntryItem.setDescription(dmbi.getDescription());
				auxEntryItem.setReference(dmbi.getReference());
				auxEntryItem.setServiceDate(serviceCalculationDate);
				auxEntryItem.setMainValue(value);
				auxEntryItem.setAmount(dmbi.getAmount());
				MunicipalBond aux = null;
				try {
					aux = revenueService.createMunicipalBond(resident, entry, fiscalPeriod, auxEntryItem, true);
				} catch (EntryDefinitionNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (aux != null) {
					value = aux.getValue().divide( new BigDecimal(dmbi.getFee()), 3, RoundingMode.HALF_EVEN);
				}

			}
			Date serviceDate = dmbi.getStartDate();

			for (int i = 1; i <= dmbi.getFee(); i++) {
				try {
					logger.info("Creando municipal Bond ..... #0 ", i);
					Date expirationDate = dmbi.calculateNextDate(serviceDate);
					EntryValueItem entryItem = new EntryValueItem();
					entryItem.setDescription(dmbi.getDescription());
					entryItem.setReference(dmbi.getReference());
					entryItem.setExpirationDate(expirationDate);
					entryItem.setServiceDate(serviceCalculationDate);
					entryItem.setMainValue(value);
					entryItem.setResetValue(Boolean.FALSE);
					// MunicipalBond municipalBond =
					// revenueService.createDeferredMunicipalBond(resident,
					// fiscalPeriod.getId(), entry, value,
					// serviceCalculationDate, expirationDate,
					// MunicipalBondType.EMISSION_ORDER);
					MunicipalBond municipalBond = revenueService .createMunicipalBond(resident, entry, fiscalPeriod, entryItem, true);
															
					municipalBond.setExpirationDate(expirationDate);
					municipalBond.setServiceDate(serviceDate);
					municipalBond.setEmitter(emitter);
					municipalBond.setOriginator(emitter);
					municipalBond.setAddress(resident.getCurrentAddress() != null ? resident.getCurrentAddress().toString() : "");
					municipalBond.setEmisionPeriod(serviceDate);
					municipalBond.setBondAddress(dmbi.getBondAddress());
					if (entry.getAdjunctClassName() != null && entry.getAdjunctClassName().trim().length() > 0) {
						Adjunct adjunct = null;
						System.out.println("revisando -----> " + entry.getAdjunctClassName());
						if (adjunctHome.getInstance() != null) {
							System.out.println("EMITIENDO -----> " + entry.getAdjunctClassName());
							adjunct = adjunctHome.getInstance();
							municipalBond.setGroupingCode(adjunct.getCode());
						}
						municipalBond.setAdjunct(adjunct);
					}
					serviceDate = expirationDate; // TODO Ver el funcionamiento de las fechas

					deferredMunicipalBondList.add(municipalBond);
				} catch (Exception e) {
					addFacesMessageFromResourceBundle(e.getMessage());
				}
			}
		}
	}
	
	
	/**
	 * emision normal, no se ha modificado nada
	 */
	public void deferredNormalEmission(){
		this.deferredMunicipalBondList = new DeferredMunicipalBondList();

		FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();
		Person emitter = userSession.getPerson();

		for (DeferredMunicipalBondItem dmbi : this.deferredMunicipalBondItems) {
			Entry entry = dmbi.getEntry();
			BigDecimal value = null;
			RevenueService revenueService = ServiceLocator.getInstance()
					.findResource("/gim/RevenueService/local");
			if (entry.getIsValueEditable())
				value = dmbi.getValue().divide(new BigDecimal(dmbi.getFee()),
						3, RoundingMode.HALF_EVEN);
			if (entry.getIsAmountEditable()) {
				EntryValueItem auxEntryItem = new EntryValueItem();
				auxEntryItem.setDescription(dmbi.getDescription());
				auxEntryItem.setReference(dmbi.getReference());
				auxEntryItem.setServiceDate(serviceCalculationDate);
				auxEntryItem.setMainValue(value);
				auxEntryItem.setAmount(dmbi.getAmount());
				MunicipalBond aux = null;
				try {
					aux = revenueService.createMunicipalBond(resident, entry,
							fiscalPeriod, auxEntryItem, true);
				} catch (EntryDefinitionNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (aux != null) {
					value = aux.getValue().divide(
							new BigDecimal(dmbi.getFee()), 3,
							RoundingMode.HALF_EVEN);
				}

			}
			Date serviceDate = dmbi.getStartDate();

			for (int i = 1; i <= dmbi.getFee(); i++) {
				try {
					logger.info("Creando municipal Bond ..... #0 ", i);
					Date expirationDate = dmbi.calculateNextDate(serviceDate);
					EntryValueItem entryItem = new EntryValueItem();
					entryItem.setDescription(dmbi.getDescription());
					entryItem.setReference(dmbi.getReference());
					entryItem.setExpirationDate(expirationDate);
					entryItem.setServiceDate(serviceCalculationDate);
					entryItem.setMainValue(value);
					entryItem.setResetValue(Boolean.FALSE);
					// MunicipalBond municipalBond =
					// revenueService.createDeferredMunicipalBond(resident,
					// fiscalPeriod.getId(), entry, value,
					// serviceCalculationDate, expirationDate,
					// MunicipalBondType.EMISSION_ORDER);
					MunicipalBond municipalBond = revenueService
							.createMunicipalBond(resident, entry, fiscalPeriod,
									entryItem, true);
					municipalBond.setExpirationDate(expirationDate);
					municipalBond.setServiceDate(serviceDate);
					municipalBond.setEmitter(emitter);
					municipalBond.setOriginator(emitter);
					municipalBond.setAddress(resident.getCurrentAddress() != null ? resident
									.getCurrentAddress().toString() : "");
					municipalBond.setEmisionPeriod(serviceDate);
					municipalBond.setBondAddress(dmbi.getBondAddress());
					if (entry.getAdjunctClassName() != null && entry.getAdjunctClassName().trim().length() > 0) {
						Adjunct adjunct = null;
						System.out.println("revisando -----> " + entry.getAdjunctClassName());
						if (adjunctHome.getInstance() != null) {
							System.out.println("EMITIENDO -----> " + entry.getAdjunctClassName());
							adjunct = adjunctHome.getInstance();
							municipalBond.setGroupingCode(adjunct.getCode());
						}
						municipalBond.setAdjunct(adjunct);
					}
					serviceDate = expirationDate; // TODO Ver el funcionamiento de las fechas

					deferredMunicipalBondList.add(municipalBond);
				} catch (Exception e) {
					addFacesMessageFromResourceBundle(e.getMessage());
				}
			}
		}
	}
	
	/*public void previewEmitAction(){		
		this.deferredMunicipalBondList = new DeferredMunicipalBondList();
		
		//FiscalPeriod fiscalPeriod = findCurrentFiscalPeriod();
		FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();
		
		for (DeferredMunicipalBondItem dmbi : this.deferredMunicipalBondItems){
			Entry entry = dmbi.getEntry();
			
			EntryDefinition entryDefinitionParent = findEntryDefinition(entry);
			
			List<EntryDefinition> entryDefinitionList = findEntryDefinitionsFromParent(entry);
			entryDefinitionList.add(0, entryDefinitionParent);
			
			BigDecimal value = dmbi.getAmount().divide(new BigDecimal(dmbi.getFee()));
			//BigDecimal value = new BigDecimal(dmbi.getAmount());
			//value = value.divide(new BigDecimal(dmbi.getFee()), 3, RoundingMode.HALF_EVEN);
			
			Date serviceDate = dmbi.getStartDate(); 
			
			for(int i=1; i<=dmbi.getFee(); i++){
				logger.info("Creando municipal Bond ..... #0 ", i);
				Date expirationDate = dmbi.calculateNextDate(serviceDate);
				MunicipalBond municipalBond = buildMunicipalBond(entry, entryDefinitionList, value, 
						dmbi.getTimePeriod(), serviceDate, expirationDate, fiscalPeriod);
				serviceDate = expirationDate;				
				deferredMunicipalBondList.add(municipalBond);
			}
			
			
		}
	}*/
	
	
	public String emit(){
		logger.info("Saving MunicipalBond... #0", deferredMunicipalBondList.getMunicipalBonds().size());
		System.out.println("...................... el vvvvvvvvvvalor es: "+futureIssuance);
		try{
			RevenueService revenueService = ServiceLocator.getInstance().findResource("/gim/RevenueService/local");
			if(futureIssuance){
				revenueService.emitFuture(deferredMunicipalBondList.getMunicipalBonds(), userSession.getUser());
			}else{
				revenueService.emit(deferredMunicipalBondList.getMunicipalBonds(), userSession.getUser());
			}
			
			setEntryCode(null);
			setIdentificationNumber(null);
			adjunctUri = EMPTY_ADJUNCT_URI;
			Conversation.instance().leave();
			logger.info("VALORES FIJADOS A NULL");
			addFacesMessageFromResourceBundle("deferredMunicipalBond.persited", deferredMunicipalBondList.getMunicipalBonds().size());
			return "persisted";
		}catch(Exception e){
			this.addFacesMessage("Error de tipo: #0", e.getMessage());
			addFacesMessageFromResourceBundle("municipalBond.errorSave",  e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * @return the criteriaResident
	 */
	public String getCriteriaResident() {
		return criteriaResident;
	}

	/**
	 * @param criteriaResident the criteriaResident to set
	 */
	public void setCriteriaResident(String criteriaResident) {
		this.criteriaResident = criteriaResident;
	}

	/**
	 * @return the criteriaEntry
	 */
	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	/**
	 * @param criteriaEntry the criteriaEntry to set
	 */
	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	/**
	 * @return the residents
	 */
	public List<Resident> getResidents() {
		return residents;
	}

	/**
	 * @param residents the residents to set
	 */
	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	/**
	 * @return the entries
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	/**
	 * @return the deferredMunicipalBondItems
	 */
	public List<DeferredMunicipalBondItem> getDeferredMunicipalBondItems() {
		return deferredMunicipalBondItems;
	}

	/**
	 * @param deferredMunicipalBondItems the deferredMunicipalBondItems to set
	 */
	public void setDeferredMunicipalBondItems(
			List<DeferredMunicipalBondItem> deferredMunicipalBondItems) {
		this.deferredMunicipalBondItems = deferredMunicipalBondItems;
	}

	/**
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
	}

	/**
	 * @return the serviceCalculationDate
	 */
	public Date getServiceCalculationDate() {
		return serviceCalculationDate;
	}

	/**
	 * @param serviceCalculationDate the serviceCalculationDate to set
	 */
	public void setServiceCalculationDate(Date serviceCalculationDate) {
		this.serviceCalculationDate = serviceCalculationDate;
	}

	/**
	 * @return the isDateEditable
	 */
	public Boolean getIsDateEditable() {
		return isDateEditable;
	}

	/**
	 * @param isDateEditable the isDateEditable to set
	 */
	public void setIsDateEditable(Boolean isDateEditable) {
		this.isDateEditable = isDateEditable;
	}

	/**
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	/**
	 * @return the deferredMunicipalBondList
	 */
	public DeferredMunicipalBondList getDeferredMunicipalBondList() {
		return deferredMunicipalBondList;
	}

	/**
	 * @param deferredMunicipalBondList the deferredMunicipalBondList to set
	 */
	public void setDeferredMunicipalBondList(
			DeferredMunicipalBondList deferredMunicipalBondList) {
		this.deferredMunicipalBondList = deferredMunicipalBondList;
	}

	/**
	 * @return the entryCode
	 */
	public String getEntryCode() {
		return entryCode;
	}

	/**
	 * @param entryCode the entryCode to set
	 */
	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getAdjunctUri() {
		return adjunctUri;
	}

	public void setAdjunctUri(String adjunctUri) {
		this.adjunctUri = adjunctUri;
	}
	
	private Adjunct createAdjunct(Entry entry) {
		try {
			System.out.println("CREATE ADJUNCT ----> " + entry.getAdjunctClassName());
			if (entry.getAdjunctClassName() != null && !entry.getAdjunctClassName().trim().isEmpty()) {
				Class<?> klass = Class.forName(entry.getAdjunctClassName());
				Adjunct adjunct = (Adjunct) klass.newInstance();
				System.out.println("ADJUNCT CREATED ----> "	+ adjunct.getClass().getSimpleName());
				this.adjunctUri = ADJUNCT_PREFIX + adjunct.getClass().getSimpleName() + ADJUNCT_SUFIX;
				return adjunct;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		this.adjunctUri = EMPTY_ADJUNCT_URI;
		return null;
	}

	public String cancelEmit() {
		return "cancelEmit";
	}
	
	public Boolean getFutureIssuance() {
		return futureIssuance;
	}

	public void setFutureIssuance(Boolean futureIssuance) {
		this.futureIssuance = futureIssuance;
	}

	/*public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}*/

}
