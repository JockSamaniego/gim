package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.commercial.action.BusinessHome;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.InvalidEmissionException;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.gim.revenue.view.EntryValueItem;
import org.gob.loja.gim.ws.dto.ObligationsHistoryFotoMulta;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import ec.gob.gim.commercial.model.FireNames;
import ec.gob.gim.commercial.model.FireRates;
import ec.gob.gim.common.model.Alert;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryType;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.security.model.Role;
//macartuche
//antclient
//import ec.gob.loja.antclient.DatosMatricula;
//import ec.gob.loja.antclient.MetodosProxy;

@Name("municipalBondHome")
public class MunicipalBondHome extends EntityHome<MunicipalBond> {

	private static final long serialVersionUID = 1L;

	public static String ADJUNCT_PREFIX = "/revenue/adjunct/";
	public static String ADJUNCT_SUFIX = ".xhtml";
	public static String EMPTY_ADJUNCT_URI = ADJUNCT_PREFIX + "Empty" + ADJUNCT_SUFIX;

	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	public static String ROLE_NAME_EMISOR = "ROLE_NAME_EMISOR";
	public static String MUNICIPALBOND_SERVICE_NAME = "/gim/MunicipalBondService/local";

	private List<Long> bondsNumbers;
	private Long bondNumber;

	private MunicipalBondService municipalBondService;

	private String entryCode;
	private String groupingCode;
	private String concept;
	private Entry entry;
	private String adjunctUri;
	private String criteria;
	private String criteriaEntry;
	private String identificationNumber;
	private SystemParameterService systemParameterService;
	private Resident resident;
	private Person emitter;
	private boolean isForReverse = false;
	private boolean isFromUrbanProperty = false;
	private boolean isFirstTime = true;
	private Boolean allBondsSelected;

	private List<Resident> residents;
	private List<Entry> entries;
	private List<EntryValueItem> entryValueItems;
	private List<MunicipalBond> municipalBonds;
	private List<Integer> emissionYearList;

	private String emisorRole;
	private MunicipalBondStatus pendingBondStatus;
	private MunicipalBondStatus blockedBondStatus;
	private MunicipalBondStatus canceledBondStatus;
	private MunicipalBondStatus paidBondStatus;
	private Date maximumServiceDate;
	private Date minimumServiceDate;

	@In
	FacesMessages facesMessages;

	@Logger
	Log logger;

	@In(create = true)
	AdjunctHome adjunctHome;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	public MunicipalBondHome() {
		adjunctUri = EMPTY_ADJUNCT_URI;
		entryValueItems = new ArrayList<EntryValueItem>();
		municipalBonds = new ArrayList<MunicipalBond>();
		// loadBondStatus();
	}

	private void cleanList() {
		entryValueItems.clear();
		municipalBonds.clear();
	}

	public Boolean getIsTaxSectionRendered() {
		if (entry != null) {
			return entry.getEntryType() == EntryType.TAX;
		}
		return Boolean.FALSE;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getAdjunctURI() {
		return adjunctUri;
	}

	public void setMunicipalBondId(Long id) {
		setId(id);
	}

	public Long getMunicipalBondId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public void changeToEmptyDate(EntryValueItem entryValueItem) {
		entryValueItem.setExpirationDate(null);
	}

	private MunicipalBond findByNumber(Long number) {
		Query q = getEntityManager().createNamedQuery(
				"MunicipalBond.findByNumberAndGroupingCodeNull");
		q.setParameter("number", number);
		List<MunicipalBond> list = q.getResultList();
		return list.size() == 1 ? list.get(0) : null;
	}

	public String updateGroupingCode() {
		if (municipalBondService == null)
			municipalBondService = ServiceLocator.getInstance().findResource(
					MUNICIPALBOND_SERVICE_NAME);
		try {
			municipalBondService.updateGroupingCode(bondsNumbers, groupingCode);
		} catch (Exception e) {
			logger.error(":::::::::::::::::::::::::::::exception:::::::::::::::::::::::: "
					+ e.getMessage());
			addFacesMessageFromResourceBundle("common.updateError");
			return "error";
		}
		return "ready";

	}

	public void addBond() {
		if (bondNumber == null)
			return;
		if (municipalBonds == null || bondsNumbers == null) {
			municipalBonds = new ArrayList<MunicipalBond>();
			bondsNumbers = new ArrayList<Long>();
		}
		MunicipalBond aux = findByNumber(bondNumber);

		if (aux != null && !municipalBonds.contains(aux)) {
			municipalBonds.add(aux);
			bondsNumbers.add(bondNumber);
			bondNumber = null;
		}
	}

	public void removeBond(MunicipalBond municipalBond) {
		municipalBonds.remove(municipalBond);
		bondsNumbers.remove(municipalBond.getNumber());
	}

	public void search() {
		logger.info("RESIDENT CHOOSER CRITERIA " + this.criteria);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			findPendingAlerts(resident.getId());
			this.setResident(resident);
		} catch (Exception e) {
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		logger.info("SEARCH BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void selectAllBonds() {
		for (MunicipalBond mb : municipalBonds) {
			mb.setIsSelected(allBondsSelected);
		}
	}

	public void synchronizeAllBondsSelected(Boolean isSelected) {
		if (!isSelected) {
			allBondsSelected = Boolean.FALSE;
		}
	}

	public List<MunicipalBond> getSelectedMunicipalBonds() {
		List<MunicipalBond> selected = new ArrayList<MunicipalBond>();
		if (municipalBonds != null) {
			for (MunicipalBond mb : municipalBonds) {
				if (mb.getIsSelected() != null && mb.getIsSelected()) {
					selected.add(mb);
				}
			}
		}
		return selected;
	}

	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
	}

	public void resetDialog() {
		this.concept = null;
	}

	public void findCurrentEmisions() {
		if (resident == null || emitter == null) {
			municipalBonds.clear();
			return;
		}
		RevenueService revenueService = ServiceLocator.getInstance()
				.findResource(RevenueService.LOCAL_NAME);
		municipalBonds = revenueService.findMunicipalBondsForReverseByEmitter(
				resident.getId(), emitter.getId(), new Date());
	}

	public void onlyResidentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
		findPendingAlerts(resident.getId());
	}

	public void buildMunicipalBonds() {
		FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();
		RevenueService revenueService = ServiceLocator.getInstance()
				.findResource(REVENUE_SERVICE_NAME);

		if (!municipalBonds.isEmpty()) {
			municipalBonds.clear();
		}

		for (EntryValueItem entryValueItem : entryValueItems) {
			try {
				if (entry.getIsValueEditable()) {
					if (entryValueItem.getMainValue() == null
							|| BigDecimal.ZERO.compareTo(entryValueItem
									.getMainValue()) > 0) {
						addFacesMessageFromResourceBundle("revenue.taxableBaseIsNegative");
						municipalBonds.clear();
						return;
					}
					if(entry.getId()==654){
						int compare=entryValueItem.getMainValue().compareTo(valueControl);
						if (compare!=0) {
							addFacesMessageFromResourceBundle("revenue.mainValueControl");
							municipalBonds.clear();
							return;
						}
					}
				}
				if (entry.getIsAmountEditable()) {
					if (entryValueItem.getAmount() == null
							|| BigDecimal.ZERO.compareTo(entryValueItem
									.getAmount()) > 0) {
						addFacesMessageFromResourceBundle("revenue.amountIsNegative");
						municipalBonds.clear();
						return;
					}
				}
				logger.info("====> VALOR INGRESADO: #0, con fecha: #1 ",
						entryValueItem.getMainValue(),
						entryValueItem.getServiceDate());
				entryValueItem.setExempt(this.getInstance().getExempt());
				entryValueItem.setInternalTramit(this.getInstance()
						.getInternalTramit());
				entryValueItem.setNoPasiveSubject(this.getInstance()
						.getIsNoPasiveSubject());
				String groupingCode = identificationNumber;
				Adjunct adjunct = null;
				// logger.info("EMITIENDO -----> "+entry.getAdjunctClassName()
				// != null && !entry.getAdjunctClassName().trim().isEmpty());
				if (entry.getAdjunctClassName() != null
						&& !entry.getAdjunctClassName().trim().isEmpty()) {
					adjunct = adjunctHome.getInstance();
					// logger.info("EMITIENDO -----> "+adjunct);
					if (adjunct != null) {
						groupingCode = adjunct != null ? adjunct.getCode()
								: groupingCode;
					}
					logger.info("EMITIENDO -----> GroupingCode: "
							+ groupingCode);
				}
				MunicipalBond mb = revenueService.createMunicipalBond(
						instance.getResident(), entry, fiscalPeriod,
						entryValueItem, true, adjunct);
				if (mb.getGroupingCode() == null)
					mb.setGroupingCode(groupingCode);
				mb.setAdjunct(adjunct);
				mb.setEmisionPeriod(mb.getEmisionDate());
				mb.setBondAddress(this.getInstance().getBondAddress());
				municipalBonds.add(mb);				
			} catch (EntryDefinitionNotFoundException e) {
				e.printStackTrace();
				StatusMessages.instance().addFromResourceBundleOrDefault(
						Severity.ERROR, e.getClass().getSimpleName(),
						e.getMessage(), entry.getName(),
						entryValueItem.getServiceDate());
			} catch (Exception e) {
				e.printStackTrace();
				StatusMessages.instance().addFromResourceBundleOrDefault(
						Severity.ERROR, e.getClass().getSimpleName(),
						e.getMessage());
			}
		}
		logger.info("====> MunicipalBonds Size: #0 ", municipalBonds.size());		
	}

	public List<Long> getSelectedBondsIds() {
		List<Long> selected = new ArrayList<Long>();
		for (MunicipalBond d : municipalBonds) {
			if (d.getIsSelected()) {
				selected.add(d.getId());
			}
		}
		return selected;
	}

	@Override
	public String persist() {
		Boolean emissionValid = isEmissionValid();
		if (!emissionValid) {
			return null;
		}

		Person emitter = userSession.getPerson();

		for (MunicipalBond mb : municipalBonds) {
			mb.setEmitter(emitter);
			mb.setOriginator(emitter);
			if (mb.getResident().getCurrentAddress() != null) {
				mb.setAddress(mb.getResident().getCurrentAddress().getStreet());
			}
		}

		String persisted = null;
		RevenueService revenueService = ServiceLocator.getInstance()
				.findResource(REVENUE_SERVICE_NAME);
		try {
			revenueService.emit(municipalBonds, userSession.getUser());
			setInstance(null);
			setIdentificationNumber(null);
			setEntryCode(null);
			Conversation.instance().leave();
			logger.info("VALORES FIJADOS A NULL");
			addFacesMessageFromResourceBundle("municipalBond.persited");
			this.cleanList();
			persisted = "persisted";

		} catch (InvalidEmissionException iee) {
			addFacesMessageFromResourceBundle(iee.getClass().getSimpleName());
		} catch (Exception e) {
			StatusMessages.instance().add(Severity.ERROR,
					"ERROR al persitir el(los) titulo(s) de credito(s): #0",
					e.getMessage());
			logger.error(
					"===> ERROR AL PERSISTIR EL TITULO DE CREDITO :( == #0", e);
			e.printStackTrace();
		}
		return persisted;

	}

	public void searchResident() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			findPendingAlerts(resident.getId());
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.getInstance().setResident(resident);
			cleanList();
			setEntry(null);
			setEntryCode(null);
			adjunctUri = EMPTY_ADJUNCT_URI;

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setResident(null);
			cleanList();
			setEntry(null);
			setEntryCode(null);
			adjunctUri = EMPTY_ADJUNCT_URI;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public List<Long> findIdRoles() {
		List<Long> list = new ArrayList<Long>();
		for (Role r : userSession.getUser().getRoles()) {
			list.add(r.getId());
		}

		return list;
	}

	public void findEmisorRole() {
		if (emisorRole == null) {
			if (systemParameterService == null) {
				systemParameterService = ServiceLocator.getInstance()
						.findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			}
			emisorRole = systemParameterService.findParameter(ROLE_NAME_EMISOR);
		}
	}

	@SuppressWarnings("unchecked")
	public void searchEntry() {
		if (entryCode != null) {
			entryCode = completeEntryCode(entryCode);

			findEmisorRole();

			Query query = null;
			if (userSession.getUser().hasRole(emisorRole)) {
				query = getEntityManager().createNamedQuery(
						"Entry.findByCodeAndDirectEmissionAndActive");
			} else {
				List<Long> roles = findIdRoles();
				if (roles.size() > 0) {
					query = getEntityManager()
							.createNamedQuery(
									"Entry.findByCodeAndDirectEmissionAndActiveAndRole");
					query.setParameter("roleIds", roles);
				}
			}

			List<Entry> entries = null;
			if (query != null) {
				query.setParameter("code", entryCode);
				entries = query.getResultList();
			} else {
				entries = new ArrayList<Entry>();
			}

			Entry entry = null;
			if (entries.size() > 0) {
				entry = entries.get(0);
			}

			if (entry != null) {
				logger.info("Entry found... " + entry.getName());
				cleanList();
				this.entry = entry;
				this.instance.setEntry(entry);
				if (entry.getAccount() == null) {
					setEntryCode(entry.getCode());
				} else {
					setEntryCode(entry.getAccount().getAccountCode());
				}

				Adjunct adjunct = createAdjunct(entry);
				if (adjunct != null) {
					adjunctHome.setId(adjunct.getId());
					adjunctHome.setInstance(adjunct);
				}

				entryValueItems.clear();
				EntryValueItem entryValueItem = new EntryValueItem(
						this.getRenderedCalendarFull());
				entryValueItem.setDescription(entry.getDescription());
				entryValueItems.add(entryValueItem);
			} else {
				String message = Interpolator
						.instance()
						.interpolate(
								"#{messages['revenue.incorrectCodeOrNoHaveDirectEmission']}",
								new Object[0]);
				facesMessages
						.addToControl(
								"entryChooser",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);

				adjunctUri = EMPTY_ADJUNCT_URI;
				cleanList();
				this.entry = null;
				this.instance.setEntry(entry);
			}
		}
	}

	private Adjunct createAdjunct(Entry entry) {
		try {
			System.out.println("CREATE ADJUNCT ----> "
					+ entry.getAdjunctClassName());
			if (entry.getAdjunctClassName() != null
					&& !entry.getAdjunctClassName().trim().isEmpty()) {
				Class<?> klass = Class.forName(entry.getAdjunctClassName());
				Adjunct adjunct = (Adjunct) klass.newInstance();
				System.out.println("ADJUNCT CREATED ----> "
						+ adjunct.getClass().getSimpleName());
				this.adjunctUri = ADJUNCT_PREFIX
						+ adjunct.getClass().getSimpleName() + ADJUNCT_SUFIX;
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

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	private String completeEntryCode(String code) {
		StringBuffer codeBuffer = new StringBuffer(code.trim());
		while (codeBuffer.length() < 5) {
			codeBuffer.insert(0, '0');
		}
		return codeBuffer.toString();
	}

	@SuppressWarnings("unchecked")
	public void searchEntryByCriteria() {
		logger.info("SEARCH Entry BY CRITERIA " + this.criteriaEntry);
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {

			findEmisorRole();

			Query query = null;
			if (userSession.getUser().hasRole(emisorRole)) {
				query = getEntityManager().createNamedQuery(
						"Entry.findByCriteriaAndDirectEmission");
			} else {
				List<Long> roles = findIdRoles();
				if (roles.size() > 0) {
					query = getEntityManager().createNamedQuery(
							"Entry.findByCriteriaAndDirectEmissionAndRole");
					query.setParameter("roleIds", roles);
				}
			}

			if (query != null) {
				query.setParameter("criteria", this.criteriaEntry);
				entries = query.getResultList();
			} else {
				entries = new ArrayList<Entry>();
			}

		}
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		residents = null;
	}

	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
		entryValueItems.clear();
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
		findPendingAlerts(resident.getId());
		cleanList();
		setEntry(null);
		setEntryCode(null);
		adjunctUri = EMPTY_ADJUNCT_URI;
		clearEntryValues();
	}

	public void entrySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		cleanList();
		this.instance.setEntry(entry);
		clearEntryValues();
		if (entry.getAccount() == null) {
			setEntryCode(entry.getCode());
		} else {
			setEntryCode(entry.getAccount().getAccountCode());
		}

		Adjunct adjunct = createAdjunct(entry);
		if (adjunct != null) {
			adjunctHome.setId(adjunct.getId());
			adjunctHome.setInstance(adjunct);
		}

		entryValueItems.clear();
		EntryValueItem entryValueItem = new EntryValueItem(
				this.getRenderedCalendarFull());
		entryValueItem.setDescription(entry.getDescription());
		entryValueItems.add(entryValueItem);

	}

	private void clearEntryValues() {
		entryValueItems.clear();
		EntryValueItem entryValueItem = new EntryValueItem();
		if (entry != null)
			entryValueItem.setDescription(entry.getDescription());
		entryValueItems.add(entryValueItem);
	}

	public void remove(EntryValueItem entryValueItem) {
		entryValueItems.remove(entryValueItem);
	}

	public void addEntryValueItem() {
		EntryValueItem entryValueItem = new EntryValueItem(
				this.getRenderedCalendarFull());
		entryValueItem.setDescription(entry.getDescription());
		entryValueItems.add(entryValueItem);
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if (!isFirstTime)
			return;
		logger.info("...... Ingreso al wire");
		if (getInstance().getId() != null || isFromUrbanProperty) {
			if (getInstance().getResident() != null)
				setIdentificationNumber(getInstance().getResident()
						.getIdentificationNumber());
			this.cleanList();
			municipalBonds.add(getInstance());
		}
		isFirstTime = false;
		BusinessHome.myId = null;
		bondIsWire = Boolean.TRUE;
	}

	public boolean isWired() {
		return true;
	}

	public MunicipalBond getDefinedInstance() {
		// return isIdDefined() ? getInstance() : null;
		this.cleanList();
		if (isIdDefined()) {
			municipalBonds.add(getInstance());
			return getInstance();
		} else {
			return null;
		}
	}

	public String cancelEmit() {
		return "cancelEmit";
	}

	private Date findMaximumServiceDate() {
		Query query = this.getEntityManager().createNamedQuery(
				"FiscalPeriod.findMaxDate");
		return (Date) query.getSingleResult();
	}

	private Date findMinimumServiceDate() {
		Query query = this.getEntityManager().createNamedQuery(
				"FiscalPeriod.findMinDate");
		return (Date) query.getSingleResult();
	}

	private Integer getYearMaxServiceDate() {
		Calendar date = Calendar.getInstance();
		date.setTime(getMaximumServiceDate());
		return date.get(Calendar.YEAR);
	}

	private void buildEmissionYearList() {
		this.emissionYearList = new ArrayList<Integer>();
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Integer minimumEmissionYear = systemParameterService
				.findParameter("MINIMUM_EMISSION_YEAR");
		Integer maximumEmissionYear = getYearMaxServiceDate();
		while (minimumEmissionYear <= maximumEmissionYear) {
			this.emissionYearList.add(minimumEmissionYear);
			minimumEmissionYear++;
		}
	}

	public List<Integer> getEmissionYearList() {
		if (this.emissionYearList == null) {
			buildEmissionYearList();
		}
		return this.emissionYearList;
	}

	public Boolean getRenderedCalendar() {
		if (entry != null) {
			return !entry.getTimePeriod().getName().equalsIgnoreCase("Anual");
		}
		return Boolean.FALSE;
	}

	public Boolean getRenderedCalendarFull() {
		if (getRenderedCalendar()) {
			return !entry.getTimePeriod().getName().equalsIgnoreCase("Mensual");
		}
		return Boolean.FALSE;
	}

	public Date getMaximumServiceDate() {
		if (maximumServiceDate == null)
			maximumServiceDate = this.findMaximumServiceDate();
		return maximumServiceDate;
	}

	public Date getMinumumServiceDate() {
		if (minimumServiceDate == null)
			minimumServiceDate = this.findMinimumServiceDate();
		return minimumServiceDate;
	}

	public Boolean isEmissionValid() {
		for (MunicipalBond municipalBond : municipalBonds) {
			for (Item item : municipalBond.getItems()) {
				if (BigDecimal.ZERO.compareTo(item.getTotal()) > 0) {
					addFacesMessageFromResourceBundle("InvalidEmissionException");
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	public Person getEmitter() {
		return emitter;
	}

	public void setEmitter(Person emitter) {
		this.emitter = emitter;
	}

	public boolean isForReverse() {
		return isForReverse;
	}

	public void setForReverse(boolean isForReverse) {
		this.isForReverse = isForReverse;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public MunicipalBondStatus getPendingBondStatus() {
		return pendingBondStatus;
	}

	public void setPendingBondStatus(MunicipalBondStatus pendingBondStatus) {
		this.pendingBondStatus = pendingBondStatus;
	}

	public MunicipalBondStatus getBlockedBondStatus() {
		return blockedBondStatus;
	}

	public void setBlockedBondStatus(MunicipalBondStatus blockedBondStatus) {
		this.blockedBondStatus = blockedBondStatus;
	}

	public List<Item> getItems() {
		return getInstance() == null ? null : getInstance().getItems();
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public Boolean getAllBondsSelected() {
		return allBondsSelected;
	}

	public void setAllBondsSelected(Boolean allBondsSelected) {
		this.allBondsSelected = allBondsSelected;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public boolean isFromUrbanProperty() {
		return isFromUrbanProperty;
	}

	public void setFromUrbanProperty(boolean isFromUrbanProperty) {
		this.isFromUrbanProperty = isFromUrbanProperty;
	}

	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public List<EntryValueItem> getEntryValueItems() {
		return entryValueItems;
	}

	public void setEntryValueItems(List<EntryValueItem> entryValueItems) {
		this.entryValueItems = entryValueItems;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public MunicipalBondStatus getCanceledBondStatus() {
		return canceledBondStatus;
	}

	public void setCanceledBondStatus(MunicipalBondStatus canceledBondStatus) {
		this.canceledBondStatus = canceledBondStatus;
	}

	public MunicipalBondStatus getPaidBondStatus() {
		return paidBondStatus;
	}

	public void setPaidBondStatus(MunicipalBondStatus paidBondStatus) {
		this.paidBondStatus = paidBondStatus;
	}

	public String getEmisorRole() {
		return emisorRole;
	}

	public void setEmisorRole(String emisorRole) {
		this.emisorRole = emisorRole;
	}

	public String getGroupingCode() {
		return groupingCode;
	}

	public void setGroupingCode(String groupingCode) {
		this.groupingCode = groupingCode;
	}

	public List<Long> getBondsNumbers() {
		return bondsNumbers;
	}

	public void setBondsNumbers(List<Long> bondsNumbers) {
		this.bondsNumbers = bondsNumbers;
	}

	public Long getBondNumber() {
		return bondNumber;
	}

	public void setBondNumber(Long bondNumber) {
		this.bondNumber = bondNumber;
	}

	public MunicipalBond loadMunicipalBondById(Long municipalBondId) {
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.findById");
		query.setParameter("municipalBondId", municipalBondId);
		MunicipalBond mb = (MunicipalBond) query.getSingleResult();
		return mb;
	}
	
//Autor: Jock Samaniego M.
//Para controlar el cierre de actividad de un negocio desde la patente y los activos...
	
	public static String message="";
	

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		MunicipalBondHome.message = message;
	}

	public void finishedActivity1(boolean isActive){
		if(isActive==false){
			entryValueItems.get(0).setDescription("Ejercicio Económico: Actividad Liquidada"+"\n");
			message="Liquidar también la actividad en IMPUESTO A LOS ACTIVOS TOTALES";
		}else{
			entryValueItems.get(0).setDescription("Ejercicio Económico: ");
			message="";
		}
		
		//System.out.println("-------"+isActive+"------");
	}
	
	public void finishedActivity2(boolean isActive){
		if(isActive==false){
			entryValueItems.get(0).setDescription("Ejercicio Económico: Actividad Liquidada"+"\n");
			message="Liquidar también la actividad en PATENTE MUNICIPAL";
		}else{
			entryValueItems.get(0).setDescription("Ejercicio Económico: ");
			message="";
		}
		
		//System.out.println("-------"+isActive+"------");
	}
	
	//Autor: Jock Samaniego M.
	//Para emitir el rubro directamente desde la emision del certificado de solvencia...
	private String certificateNumberSolvency;
	private Resident residentSolvency;
    private List<MunicipalBond> municipalBondsSolvency;
    private Entry entrySolvency;
    private String codeSolvency;
    private String motivationSolvency;
    private String copiesNumberSolvency;
    private BigDecimal totalSolvency;
    private Delegate revenueDelegateSolvency;
    private Charge revenueChargeSolvency;	
    private Delegate incomeDelegateSolvency;
	private Charge incomeChargeSolvency;
	private boolean buttonGenerateSolvency;
	private Resident applicantSolvency;
	private List<String> copiesEnumerate;
	private Long number;
	private String observationsSolvency;
	
	public String getCertificateNumberSolvency() {
		return certificateNumberSolvency;
	}
	public void setCertificateNumberSolvency(String certificateNumberSolvency) {
		this.certificateNumberSolvency = certificateNumberSolvency;
	}
	public Resident getResidentSolvency() {
		return residentSolvency;
	}
	public void setResidentSolvency(Resident residentSolvency) {
		this.residentSolvency = residentSolvency;
	}
	public List<MunicipalBond> getMunicipalBondsSolvency() {
		return municipalBondsSolvency;
	}
	public void setMunicipalBondsSolvency(List<MunicipalBond> municipalBondsSolvency) {
		this.municipalBondsSolvency = municipalBondsSolvency;
	}
	public Entry getEntrySolvency() {
		return entrySolvency;
	}
	public void setEntrySolvency(Entry entrySolvency) {
		this.entrySolvency = entrySolvency;
	}
	public String getCodeSolvency() {
		return codeSolvency;
	}
	public void setCodeSolvency(String codeSolvency) {
		this.codeSolvency = codeSolvency;
	}
	public String getMotivationSolvency() {
		return motivationSolvency;
	}
	public void setMotivationSolvency(String motivationSolvency) {
		this.motivationSolvency = motivationSolvency;
	}
	public String getCopiesNumberSolvency() {
		return copiesNumberSolvency;
	}
	public void setCopiesNumberSolvency(String copiesNumberSolvency) {
		this.copiesNumberSolvency = copiesNumberSolvency;
	}
	public BigDecimal getTotalSolvency() {
		return totalSolvency;
	}
	public void setTotalSolvency(BigDecimal totalSolvency) {
		this.totalSolvency = totalSolvency;
	}
	public Delegate getRevenueDelegateSolvency() {
		return revenueDelegateSolvency;
	}
	public void setRevenueDelegateSolvency(Delegate revenueDelegateSolvency) {
		this.revenueDelegateSolvency = revenueDelegateSolvency;
	}
	public Charge getRevenueChargeSolvency() {
		return revenueChargeSolvency;
	}
	public void setRevenueChargeSolvency(Charge revenueChargeSolvency) {
		this.revenueChargeSolvency = revenueChargeSolvency;
	}
	public Delegate getIncomeDelegateSolvency() {
		return incomeDelegateSolvency;
	}
	public void setIncomeDelegateSolvency(Delegate incomeDelegateSolvency) {
		this.incomeDelegateSolvency = incomeDelegateSolvency;
	}
	public Charge getIncomeChargeSolvency() {
		return incomeChargeSolvency;
	}
	public void setIncomeChargeSolvency(Charge incomeChargeSolvency) {
		this.incomeChargeSolvency = incomeChargeSolvency;
	}
	public boolean isButtonGenerateSolvency() {
		return buttonGenerateSolvency;
	}
	public void setButtonGenerateSolvency(boolean buttonGenerateSolvency) {
		this.buttonGenerateSolvency = buttonGenerateSolvency;
	}
	public Resident getApplicantSolvency() {
		return applicantSolvency;
	}
	public void setApplicantSolvency(Resident applicantSolvency) {
		this.applicantSolvency = applicantSolvency;
	}
	public List<String> getCopiesEnumerate() {
		return copiesEnumerate;
	}
	public void setCopiesEnumerate(List<String> copiesEnumerate) {
		this.copiesEnumerate = copiesEnumerate;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getObservationsSolvency() {
		return observationsSolvency;
	}
	public void setObservationsSolvency(String observationsSolvency) {
		this.observationsSolvency = observationsSolvency;
	}

	public String buildDirectMunicipalBonds() {
		SolvencyReportHome SRHome = (SolvencyReportHome) Contexts.getConversationContext().get(SolvencyReportHome.class);
		String result=SRHome.generateSolvencyCertificate();	
		buttonGenerateSolvency=SRHome.isButtonGenerate();
		if(result==null){
    		 SRHome.disableButton();
    		return "/revenue/SolvencyCertificate.xhtml";
    	}else if(result=="failed"){
    		SRHome.disableButton();
    		return "/revenue/SolvencyCertificate.xhtml";
    	}else{
			certificateNumberSolvency = SRHome.getCertificateNumber();
			residentSolvency = SRHome.getResident();
			//municipalBondsSolvency = SRHome.getMunicipalBonds();
			//entrySolvency = SRHome.getEntry();
			codeSolvency = SRHome.getCode();
			motivationSolvency = SRHome.getMotivation();
			copiesNumberSolvency = SRHome.getCopiesNumber();
			totalSolvency = SRHome.getTotal();
			revenueDelegateSolvency = SRHome.getRevenueDelegate();
			revenueChargeSolvency = SRHome.getRevenueCharge();
			incomeDelegateSolvency = SRHome.getIncomeDelegate();
			incomeChargeSolvency = SRHome.getIncomeCharge();
			applicantSolvency = SRHome.getApplicant();
			observationsSolvency = SRHome.getObservation();
			copiesEnumerate = new ArrayList<String>();
    		for(int i=1;i<=Integer.parseInt(copiesNumberSolvency);i++){
    			copiesEnumerate.add(Integer.toString(i));
    		}
    		/*FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();
			RevenueService revenueService = ServiceLocator.getInstance().findResource(REVENUE_SERVICE_NAME);
				try {
					Resident finalResident=null;
					if(applicantSolvency!=null){
						finalResident=applicantSolvency;
					}else{
						finalResident=residentSolvency;
					}
					String groupingCode = finalResident.getIdentificationNumber();
					Adjunct adjunct = null;
					Query query = getEntityManager().createNamedQuery("Entry.findById");
					query.setParameter("entryId", (long)73);
					Entry entry73 = (Entry) query.getSingleResult();
								
					EntryValueItem EVI = new EntryValueItem();
					//EVI.setDescription("Solicitud de Certificado de Solvencia\nC.I: "+residentSolvency.getIdentificationNumber());
					EVI.setDescription("Certificado de Solvencia, "+motivationSolvency);
					EVI.setReference("Contribuyente: "+residentSolvency.getIdentificationNumber()+",\nSolicitante: "+finalResident.getIdentificationNumber());
					EVI.setPreviousPayment(BigDecimal.ZERO);
					int totalValue=(Integer.parseInt(copiesNumberSolvency)*2);
					EVI.setMainValue(new BigDecimal(totalValue));
					
					
					MunicipalBond mbSolvency = revenueService.createMunicipalBond(finalResident, entry73, fiscalPeriod,
							EVI, true, adjunct);
					if (mbSolvency.getGroupingCode() == null)
						mbSolvency.setGroupingCode(groupingCode);
					mbSolvency.setAdjunct(adjunct);
					mbSolvency.setEmisionPeriod(mbSolvency.getEmisionDate());
					mbSolvency.setBondAddress(finalResident.getCurrentAddress().getStreet());
					municipalBonds.add(mbSolvency);
				} catch (EntryDefinitionNotFoundException e) {
					e.printStackTrace();
					StatusMessages.instance().addFromResourceBundleOrDefault(
							Severity.ERROR, e.getClass().getSimpleName(),
							e.getMessage(), entry.getName());
				} catch (Exception e) {
					e.printStackTrace();
					StatusMessages.instance().addFromResourceBundleOrDefault(
							Severity.ERROR, e.getClass().getSimpleName(),
							e.getMessage());
				}
			//persist();
			//number=findNumber();
			 */
			if(result=="resident"){
				SRHome.disableButton();
				return "/revenue/report/SolvencyReportByResident.xhtml";
        	}else if(result=="entry"){
        		SRHome.disableButton();
        		return "/revenue/report/SolvencyReportByEntry.xhtml";
        	}  			
    	}
    	SRHome.disableButton();
    	return "/revenue/SolvencyCertificate.xhtml";    	
	}
	// Autor:Jock Samaniego
	//para obtener el numero de la obligacion de pago
		/*public Long findNumber(){
			Query query2 = getEntityManager().createNamedQuery("MunicipalBond.findLastId");
			Long n=(Long)query2.getSingleResult();
			Query query = getEntityManager().createNamedQuery("MunicipalBond.findLastNumber");
			query.setParameter("id", n);
			Long number=(Long) query.getSingleResult();
			return number;
		}*/	
	
	// Autor:Jock Samaniego
	//para emitir la obligacion de bomberos........
		private FireNames fireRateName;
		private FireRates fireRate;	
		private List<FireRates> fireRates;
		private String fireMessage;
		private String amountRate="1";
		private boolean variableAmount=Boolean.TRUE;
		private BigDecimal valueControl;
		

		public FireNames getFireRateName() {
			return fireRateName;
		}

		public void setFireRateName(FireNames fireRateName) {
			this.fireRateName = fireRateName;
		}

		public FireRates getFireRate() {
			return fireRate;
		}

		public void setFireRate(FireRates fireRate) {
			this.fireRate = fireRate;
		}

		public List<FireRates> getFireRates() {
			return fireRates;
		}

		public void setFireRates(List<FireRates> fireRates) {
			this.fireRates = fireRates;
		}
		

		public String getFireMessage() {
			return fireMessage;
		}

		public void setFireMessage(String fireMessage) {
			this.fireMessage = fireMessage;
		}	

		public String getAmountRate() {
			return amountRate;
		}

		public void setAmountRate(String amountRate) {
			this.amountRate = amountRate;
		}

		public boolean isVariableAmount() {
			return variableAmount;
		}

		public void setVariableAmount(boolean variableAmount) {
			this.variableAmount = variableAmount;
		}
		
		public BigDecimal getValueControl() {
			return valueControl;
		}

		public void setValueControl(BigDecimal valueControl) {
			this.valueControl = valueControl;
		}

		public void findFireRates(){
			fireRates = new ArrayList<FireRates>();
			Query query = getEntityManager().createNamedQuery("FireRates.findForTypes");
			query.setParameter("name", fireRateName.getCodeName());
			fireRates= query.getResultList();
			fireMessage="";
		}
		
		public void findFireRateAndAmount(){
			String code=fireRate.getCode();
			if(code.equals("11.05")||code.equals("12.02")||code.equals("12.03")||code.equals("16.01")||code.equals("16.02")||code.equals("30.03")||code.equals("33.02")||code.equals("34.01")){
				variableAmount=Boolean.FALSE;
			}else{
				variableAmount=Boolean.TRUE;
				amountRate="1";
			}
			BigDecimal mainValue=fireRate.getValue();
			fireMessage=mainValue.toString();
			entryValueItems.get(0).setMainValue(mainValue);
			valueControl=mainValue;
		}
		
		
		public void calculateAmountRate(String ar){
			BigDecimal mainValue=fireRate.getValue().multiply(new BigDecimal(ar));		
			fireMessage=mainValue.toString();
			entryValueItems.get(0).setMainValue(mainValue);
			valueControl=mainValue;
		}
		
		// Autor:Jock Samaniego 08/07/2016
		//para permitir las consultas de historial de obligaciones
		
		private String obligationsRadioButton;
		private int reportType = 0;
		private String obligationsHistoryCriteria;
		private List<ObligationsHistoryFotoMulta> obligationsHistoryResult;
		EntityManager em = getEntityManager();
		
		public void cleanReport(){
			reportType = 0;
			obligationsHistoryResult = new ArrayList<ObligationsHistoryFotoMulta>();
			obligationsHistoryCriteria = null;
		}
		
		public void searchObligationsHistory(){
			obligationsHistoryResult = new ArrayList<ObligationsHistoryFotoMulta>();
			if(obligationsRadioButton.equals("Normal")){
				reportType = 1;
				String qryResult = "SELECT mb.id, re.identificationnumber, re.name, "
						+ "mb.number, mb.emisiondate, mb.expirationdate, mb.value, mb.paidtotal, mb.description, mb.reference, mb.groupingcode, "
						+ "mbs.name status, en.name entName "
						+ "FROM gimprod.municipalbond mb "
						+ "INNER JOIN gimprod.resident re on mb.resident_id = re.id "
						+ "inner join municipalbondstatus mbs on mb.municipalbondstatus_id = mbs.id "
						+ "inner join entry en on mb.entry_id = en.id "
						+ "WHERE lower(mb.description) like lower('"+obligationsHistoryCriteria+"%') or "
						+ "lower(re.identificationNumber) like lower('"+obligationsHistoryCriteria+"%') or "
						+ "lower(mb.reference) like lower('"+obligationsHistoryCriteria+"%') or lower(mb.groupingcode) like lower('%"+obligationsHistoryCriteria+"%') "
						+ "ORDER BY emisiondate;";
				Query queryResult = this.getEntityManager().createNativeQuery(qryResult);
				List<Object[]> result = queryResult.getResultList();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				ObligationsHistoryFotoMulta reg;
				for (Object[] row : result) {
					reg = new ObligationsHistoryFotoMulta();
					try {
						reg.setId(row[0] == null ? 0 : Long.parseLong(row[0].toString()));
						reg.setIdentificationNumber(row[1] == null ? "" : row[1].toString());
						reg.setName(row[2] == null ? "" : row[2].toString());
						reg.setNumber(row[3] == null ? 0 : Long.parseLong(row[3].toString()));
						reg.setEmisiondate(row[4] == null ? sdf.parse("") : sdf.parse(row[4].toString()));
						reg.setExpirationdate(row[5] == null ? sdf.parse("") : sdf.parse(row[5].toString()));
						reg.setValue(row[6] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Double.valueOf(row[6].toString())));
						reg.setPaidtotal(row[7] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Double.valueOf(row[7].toString())));
						reg.setDescription(row[8] == null ? "" : row[8].toString());
						reg.setReference(row[9] == null ? "" : row[9].toString());
						reg.setGroupingcode(row[10] == null ? "" : row[10].toString());
						reg.setStatus(row[11] == null ? "" : row[11].toString());
						reg.setEntryName(row[12] == null ? "" : row[12].toString());
						obligationsHistoryResult.add(reg);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}else if(obligationsRadioButton.equals("FotoMulta")){
				reportType = 2;
				String qryResult = "SELECT mb.id, re.identificationnumber, re.name, "
						+ "mb.number, mb.emisiondate, mb.expirationdate, mb.value, mb.paidtotal, mb.description, mb.reference, mb.groupingcode, "
						+ "ant.numberplate, ant.contraventionNumber, ant.speeding, ant.citationdate, mbs.name  status, en.name entName "
						+ "FROM gimprod.municipalbond mb "
						+ "INNER JOIN gimprod.resident re on mb.resident_id = re.id "
						+ "inner join municipalbondstatus mbs on mb.municipalbondstatus_id = mbs.id "
						+ "inner join entry en on mb.entry_id = en.id "
						+ "INNER JOIN gimprod.antreference ant on mb.adjunct_id = ant.id "
						+ "WHERE lower(mb.description) like lower('%"+obligationsHistoryCriteria+"%') or "
						//+ "lower(mb.reference) like lower('%"+obligationsHistoryCriteria+"%') or lower(mb.groupingcode) like lower('%"+obligationsHistoryCriteria+"%') "
						//+ "ORDER BY emisiondate;";
						+ "ant.contraventionNumber = '"+obligationsHistoryCriteria+"' "
						+ "ORDER BY emisiondate;";
				Query queryResult = this.getEntityManager().createNativeQuery(qryResult);
				List<Object[]> result = queryResult.getResultList();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				ObligationsHistoryFotoMulta reg;
				for (Object[] row : result) {
				reg = new ObligationsHistoryFotoMulta();
					try {
						reg.setId(row[0] == null ? 0 : Long.parseLong(row[0].toString()));
						reg.setIdentificationNumber(row[1] == null ? "" : row[1].toString());
						reg.setName(row[2] == null ? "" : row[2].toString());
						reg.setNumber(row[3] == null ? 0 : Long.parseLong(row[3].toString()));
						reg.setEmisiondate(row[4] == null ? sdf.parse("") : sdf.parse(row[4].toString()));
						reg.setExpirationdate(row[5] == null ? sdf.parse("") : sdf.parse(row[5].toString()));
						reg.setValue(row[6] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Double.valueOf(row[6].toString())));
						reg.setPaidtotal(row[7] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Double.valueOf(row[7].toString())));
						reg.setDescription(row[8] == null ? "" : row[8].toString());
						reg.setReference(row[9] == null ? "" : row[9].toString());
						reg.setGroupingcode(row[10] == null ? "" : row[10].toString());
						reg.setNumberPlate(row[11] == null ? "" : row[11].toString());
						reg.setAntNumber(row[12] == null ? "" : row[12].toString());
						reg.setSpeeding(row[13] == null ? BigDecimal.ZERO : BigDecimal.valueOf(Double.valueOf(row[13].toString())));
						reg.setCitationDate(row[14] == null ? null : sdf.parse(row[14].toString()));
						reg.setStatus(row[15] == null ? "" : row[15].toString());
						reg.setEntryName(row[16] == null ? "" : row[16].toString());
						obligationsHistoryResult.add(reg);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}else if(obligationsRadioButton.equals("Ant")){
				//macartuche
				//antclient
//				solicitaMatricula(obligationsHistoryCriteria);
			}
		}
		
		//macartuche
		//antclient
//		private void solicitaMatricula(String placa) {
//			MetodosProxy mp=new MetodosProxy();
//			try {
//				DatosMatricula dm=mp.solicita_Matricula(placa, "WEB", "TESTUSER");
//				System.out.println("------------ "+dm.getAnio());
//				System.out.println("------------ "+dm.getApellido1());
//				System.out.println("------------ "+dm.getApellido2());
//			} catch (RemoteException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		public String getObligationsRadioButton() {
			return obligationsRadioButton;
		}

		public void setObligationsRadioButton(String obligationsRadioButton) {
			this.obligationsRadioButton = obligationsRadioButton;
		}

		public String getObligationsHistoryCriteria() {
			return obligationsHistoryCriteria;
		}

		public void setObligationsHistoryCriteria(String obligationsHistoryCriteria) {
			this.obligationsHistoryCriteria = obligationsHistoryCriteria;
		}

		public List<ObligationsHistoryFotoMulta> getObligationsHistoryResult() {
			return obligationsHistoryResult;
		}

		public void setObligationsHistoryResult(
				List<ObligationsHistoryFotoMulta> obligationsHistoryResult) {
			this.obligationsHistoryResult = obligationsHistoryResult;
		}

		public int getReportType() {
			return reportType;
		}

		public void setReportType(int reportType) {
			this.reportType = reportType;
		}
		
		//Jock Samaniego
		//Para bloquear emisión
		
		private Boolean bondIsWire=Boolean.FALSE;
		private List<Alert> pendingAlerts = new ArrayList<Alert>();
		private Boolean isBlocketToEmit = Boolean.FALSE;
		private String blocketMessage;
		private String colorMessage;

		public Boolean getIsBlocketToEmit() {
			return isBlocketToEmit;
		}

		public List<Alert> getPendingAlerts() {
			return pendingAlerts;
		}

		public String getBlocketMessage() {
			return blocketMessage;
		}

		public String getColorMessage() {
			return colorMessage;
		}

		public Boolean getBondIsWire() {
			return bondIsWire;
		}

		public void setBondIsWire(Boolean bondIsWire) {
			this.bondIsWire = bondIsWire;
		}

		@SuppressWarnings("unchecked")
		private void findPendingAlerts(Long residentId) {
			blocketMessage="";
			pendingAlerts.clear();
			isBlocketToEmit = Boolean.FALSE;
			colorMessage = "blue";
			Query query = getEntityManager().createNamedQuery("Alert.findPendingAlertsByResidentId");
			query.setParameter("residentId", residentId);
			pendingAlerts = query.getResultList();
			if (pendingAlerts.size()>0){
				blocketMessage=pendingAlerts.get(0).getOpenDetail();			
			}
			for (Alert alert : pendingAlerts) {
				//if (alert.getPriority() == AlertPriority.HIGH) {
					//paymentBlocked = true;
				//}
				if(alert.getAlertType().getIsToEmit()){
					isBlocketToEmit = Boolean.TRUE;
					colorMessage = "red";
					blocketMessage=alert.getOpenDetail();
				}
			}
		}
		
		/**
		 * Permite buscar un numero de notificacion de simert en la bd
		 * para evitar la duplicidad de multas de simert
		 * rfam 2018-03-26
		 * @param code
		 * @return
		 */
		@SuppressWarnings("unused")
		public Boolean findSimertfine(String code) {
			Query q = getEntityManager().createNativeQuery("select count(*) from gimprod.vehicularfinereference vfr "
					+ "inner join gimprod.municipalbond mb on vfr.id = mb.adjunct_id "
					+ "where vfr.notificationnumber like concat('%', :criteria,'%') "
					+ "and mb.municipalbondstatus_id in (3,4,5,6,7,10)");
			q.setParameter("criteria", code);
			BigInteger quantity = (BigInteger) q.getSingleResult();
			if (quantity.intValue() <= 0) {
				return false;
			}else {
				return true;	
			}
		}

}
