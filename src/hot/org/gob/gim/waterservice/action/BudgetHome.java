package org.gob.gim.waterservice.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.view.DeferredMunicipalBondItem;
import org.gob.gim.revenue.view.EntryValueItem;
import org.gob.gim.waterservice.facade.WaterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.TimePeriod;
import ec.gob.gim.revenue.model.adjunct.BudgetReference;
import ec.gob.gim.waterservice.model.Budget;
import ec.gob.gim.waterservice.model.BudgetDTO;
import ec.gob.gim.waterservice.model.BudgetEntry;
import ec.gob.gim.waterservice.model.BudgetItem;
import ec.gob.gim.waterservice.model.BudgetItemGrouped;
import ec.gob.gim.waterservice.model.BudgetItemResult;

@Name("budgetHome")
@Scope(ScopeType.CONVERSATION)
public class BudgetHome extends EntityHome<Budget> implements Serializable {

	@In
	private FacesMessages facesMessages;

	private static final long serialVersionUID = 1L;
	/**
	 * @In(create = true) MunicipalBondHome municipalBondHome;
	 */
	// @In(create = true)
	// PropertyHome propertyHome;
	// @In(create = true)
	// ResidentHome residentHome;

	/**
	 * mi codigo
	 */
	List<BudgetEntry> budgetEntries;
	String criteriaBudgetEntry;
	List<Property> properties;
	String criteriaProperty;
	BigDecimal total;
	Long tax;
	BigDecimal subTotal;
	String criteria;
	private BigDecimal subTotalTax;
	private BigDecimal subTotalNoTax;

	private List<Budget> budgetItemOrdenables = new ArrayList<Budget>();
	// borrar al anterior
	// @DataModel
	// private List<BudgetItemOrdenable> listBudgetItem = new
	// ArrayList<BudgetItemOrdenable>();

	// @DataModelSelection
	// @Out(required = false)
	// BudgetItemOrdenable bioSelected;

	private Boolean isPanelPropertyEnable = new Boolean(true);

	// busqueda del resident
	private Long residentId;
	private List<Resident> residents;

	private String criteriaEntry;
	private String identificationNumber;
	private String identificationNumberDoneBy;
	// datos para pre-emitir
	List<Entry> preEmissionEntries;
	public static String WATER_SERVICE_NAME = "/gim/WaterService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	// private SystemParameterService systemParameterService;
	private WaterService waterService;

	@In
	UserSession userSession;

	List<BudgetItem> preEmissionBudgetItem;

	private List<BudgetItemGrouped> budgetItemsGrouped;
		
	private SystemParameterService systemParameterService;
	
	
	private List<MunicipalBond> municipalBondsReport;
	

	public void setBudgetId(Long id) {
		setId(id);
	}

	public Long getBudgetId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	boolean isFirsttime = true;

	public void wire() {
		if (!isFirsttime) return;
		getInstance();
				
		if (this.getInstance().getResident() != null) {
			identificationNumber = this.getInstance().getResident().getIdentificationNumber();
		}

		if (this.getInstance().getDoneBy() != null) {
			identificationNumberDoneBy = this.getInstance().getDoneBy().getIdentificationNumber();
		}

		if (this.getInstance().getProperty() == null && this.getInstance().getResident() != null) {
			isPanelPropertyEnable = new Boolean(false);
		}
		calculateTotalBudget();
		isFirsttime = false;
		generateCode();
	}

	public void generateCode() {
		if (this.getInstance().getCode() == null) {
			int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
			Query q = this.getEntityManager().createNamedQuery("Budget.findMaxCodeByYear");
			q.setParameter("year", year);
			Object o = q.getSingleResult();			
			if (o != null) {
				int code = Integer.parseInt(o.toString());
				this.getInstance().setCode(code + 1);
			} else {
				this.getInstance().setCode(1);
			}
			
			this.getInstance().setYear(year);
		}
	}

	public String wireReport(Long budgetId) {
		setBudgetId(budgetId);
		getInstance();

		calculateTotalBudget();		

		// para el reporte
		if (this.getInstance().getId() != null) {			
			List<Entry> entries = new ArrayList<Entry>();
			
			for(BudgetItem b: this.getInstance().getBudgetItems()){
				Entry aux = b.getBudgetEntry().getEntry();
				if(!entries.contains(aux)) entries.add(aux);
			}
			
			this.budgetItemsGrouped = new ArrayList<BudgetItemGrouped>();
			BigDecimal subTotal;			
			
			List<BudgetItem> budgetItems;
			
			for (Entry e : entries) {
				
				budgetItems= new ArrayList<BudgetItem>();
				
				subTotal = BigDecimal.ZERO;			
				
				for(BudgetItem bi: getInstance().getBudgetItems()){
					if(e.equals(bi.getBudgetEntry().getEntry()) && !budgetItems.contains(bi)){
						if(bi.getTotal() != null){
							subTotal = subTotal.add(bi.getTotal());
							budgetItems.add(bi);
						}						
					}
				}
				
				if(subTotal.compareTo(BigDecimal.ZERO) == 1){
					BudgetItemGrouped budgetItemGrouped = new BudgetItemGrouped();	
					budgetItemGrouped.setBudgetItems(budgetItems);
					budgetItemGrouped.setSubTotal(subTotal);
					budgetItemsGrouped.add(budgetItemGrouped);
				}

			}
			
		}		
		
		return "/waterservice/report/BudgetReport.xhtml";
	}
	public boolean isWired() {
		return true;
	}

	public Budget getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<BudgetItem> getBudgetItems() {
		return getInstance() == null ? null : new ArrayList<BudgetItem>(
				getInstance().getBudgetItems());
	}

	public void searchBudgetEntry() {
		Query query = getEntityManager().createNamedQuery("BudgetEntry.findByName");
		query.setParameter("criteria", criteriaBudgetEntry);
		this.budgetEntries = query.getResultList();
	}

	public void add(BudgetEntry budgetEntry) {
		boolean alreadyInList=false;
		for(BudgetItem aux:this.getInstance().getBudgetItems()){
			if(aux.getBudgetEntry().equals(budgetEntry)){
				alreadyInList=true;
				break;
			}
		}
		if(!alreadyInList){
			BudgetItem bi = new BudgetItem();
			bi.setBudgetEntry(budgetEntry);
			bi.setValue(budgetEntry.getValue());
			this.getInstance().add(bi);	
		}else{
			System.out.println("ya existe en la lista");
		}
		
	}

	public void removeBudgetItem(BudgetItem budgetItem) {
		this.getInstance().getBudgetItems().remove(budgetItem);
		calculateTotalBudget();
	}

	public void calculateTotalBudgetItem(BudgetItem budgetItem) {
		if (budgetItem != null) {
			if (budgetItem.getQuantity() != null) {
				BigDecimal value = budgetItem.getValue();
				//budgetItem.setValue(value);
				budgetItem.setTotal(value.multiply(budgetItem.getQuantity()));
			}
		}
		calculateTotalBudget();
	}

	public void calculateTotalBudget() {

		total = new BigDecimal(0);
		subTotalNoTax = new BigDecimal(0);
		subTotalTax = new BigDecimal(0);

		// subTotal = new BigDecimal(0);
		//tax = new Long(12);
		tax = new Long(14);
		for (BudgetItem bi : this.getInstance().getBudgetItems()) {
			if (bi != null) {
				if (bi.getTotal() != null) {
					if (bi.getBudgetEntry().getIsTaxable()) {
						subTotalTax = subTotalTax.add(bi.getTotal());
					} else {
						subTotalNoTax = subTotalNoTax.add(bi.getTotal());
					}
				}
			}
		}
		this.getInstance().setSubTotalNoTax(subTotalNoTax);
		this.getInstance().setSubTotalTax(subTotalTax);
		/*
		 * BigDecimal taxTotal = subTotal.multiply(new BigDecimal(tax)).divide(
		 * new BigDecimal(100));
		 */
		//iva a 14 rfarmijos 2016-05-29
		total = total.add(subTotalNoTax).add(subTotalTax.add(subTotalTax.multiply(new BigDecimal(0.14))));
		this.total = this.total.setScale(2, RoundingMode.HALF_UP);
		this.getInstance().setTotal(total);
	}

	public void selectProperty(Property property) {
		this.getInstance().setProperty(property);
		this.getInstance().setResident(
				property.getCurrentDomain().getResident());
	}

	public String getCriteriaBudgetEntry() {
		return criteriaBudgetEntry;
	}

	public void setCriteriaBudgetEntry(String criteriaBudgetEntry) {
		this.criteriaBudgetEntry = criteriaBudgetEntry;
	}

	public List<BudgetEntry> getBudgetEntries() {
		return budgetEntries;
	}

	public void setBudgetEntries(List<BudgetEntry> budgetEntries) {
		this.budgetEntries = budgetEntries;
	}

	public Long getTax() {
		return tax;
	}

	public void setTax(Long tax) {
		this.tax = tax;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	public List<Budget> getBudgetItemOrdenables() {
		return budgetItemOrdenables;
	}

	public void setBudgetItemOrdenables(List<Budget> budgetItemOrdenables) {
		this.budgetItemOrdenables = budgetItemOrdenables;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	// eventos para el componente de busqueda de propiedades
	// metodos busqueda de propiedades del contribuyente
	public void searchProperty() {
		Query query = getEntityManager().createNamedQuery(
				"Property.findByCadastralCode");
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}

	public void searchPropertyByCriteria() {
		if (criteriaProperty != null) {

			if (criteriaProperty.length() >= 8) {
				Query query = getEntityManager().createNamedQuery("Property.findByCadastralCode");
				query.setParameter("criteria", this.criteriaProperty);
				properties = query.getResultList();				
			}
		}
	}

	public void clearSearchPropertyPanel() {
		this.setCriteriaProperty(null);
		properties = null;
	}

	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes().get("property");
		this.getInstance().setProperty(property);
		this.getInstance().setResident(property.getCurrentDomain().getResident());
		this.getInstance().setCadastralCode(property.getCadastralCode());
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public Boolean getIsPanelPropertyEnable() {
		return isPanelPropertyEnable;
	}

	public void setIsPanelPropertyEnable(Boolean isPanelPropertyEnable) {
		this.isPanelPropertyEnable = isPanelPropertyEnable;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
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

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	/*
	 * public void cleanValues() { if (this.getInstance().getSpaceType() == null
	 * || this.getInstance().getSpaceType().getName()
	 * .equalsIgnoreCase("ANTENA")) { this.getInstance().setCompassPoint(null);
	 * this.getInstance().setWidth(0); this.getInstance().setHeight(0);
	 * isAntenna = true; } else { isAntenna = false; }
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
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

	public void searchResident() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.getInstance().setResident(resident);
			
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void searchResidentDoneBy() {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);		
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumberDoneBy);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.getInstance().setDoneBy(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setDoneBy(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void doneBySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setDoneBy(resident);
		this.setIdentificationNumberDoneBy(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	/*
	 * Inicio de pre-emisiones
	 */

	public void wirePreEmission() {
		if (!isFirsttime) return;
		getInstance();

		if (this.getInstance().getProperty() == null && this.getInstance().getResident() != null) {
			isPanelPropertyEnable = Boolean.FALSE;
		}

		if (this.getInstance().getResident() != null) {
			identificationNumber = this.getInstance().getResident().getIdentificationNumber();
		}		
		calculatePreEmissionValues();		
		isFirsttime = false;
	}

	/**
	 * Carga los items del presupuesto agurupados por el rubro
	 */
	public void loadBudgetItemsGrouped() {
		Query q = this.getEntityManager().createNamedQuery("BudgetItem.findByBudgetGroupedByEntry");
		q.setParameter("budgetId", this.getInstance().getId());
		this.preEmissionEntries = q.getResultList();
	}

	public List<Long> findEntriesIds(List<Entry> entries) {
		List<Long> list = new ArrayList<Long>();
		for (Entry e : entries) {
			if (!list.contains(e.getId()))
				list.add(e.getId());
		}
		return list;
	}

	public Entry findEntry(List<Entry> entries, Long entryId) {
		Entry entry = null;
		for (Entry e : entries) {
			if (e.getId().equals(entryId)) {
				entry = e;
			}
		}
		return entry;
	}

	public List<BudgetItem> calculatePreEmissionValues() {
		if (preEmissionEntries == null) loadBudgetItemsGrouped();

		List<BudgetItem> values = new ArrayList<BudgetItem>();

		Query q = this.getEntityManager().createNamedQuery("BudgetItem.addByBudgetEntries");
		q.setParameter("budgetId", this.getInstance().getId());
		q.setParameter("entriesId", findEntriesIds(preEmissionEntries));
		List<BudgetItemResult> lista = q.getResultList();

		for (BudgetItemResult ir : lista) {
			if (ir.getTotal() != null) {
				BudgetItem bi = new BudgetItem();
				bi.setQuantity(new BigDecimal(1));
				bi.setTotal(ir.getTotal());
				bi.setBudgetEntry(new BudgetEntry());
				bi.getBudgetEntry().setEntry(findEntry(preEmissionEntries, ir.getEntryId()));
				values.add(bi);
			}
		}

		preEmissionBudgetItem = values;
		return values;
	}

	public DeferredMunicipalBondItem populateEntry(Entry entry, BudgetItem bi) {
		DeferredMunicipalBondItem deferredMunicipalBondItem = new DeferredMunicipalBondItem(
				entry, bi.getTotal(), bi.getQuantity().intValue(), new Date(),
				new Date());
		return deferredMunicipalBondItem;
	}
	
	private Date findEmisionPeriod(){
		 Date today = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		return calendar.getTime();
		
	}

	private EmissionOrder createEmissionOrder(List<BudgetItem> items) throws Exception {
		
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		
		Calendar ca = Calendar.getInstance();

		Date currentDate = ca.getTime();

		FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();

		Person p = userSession.getPerson();

		Resident resident = this.getInstance().getResident();

		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(currentDate);
		emissionOrder.setDescription("Presupuesto N°: "+this.getInstance().getCode()+" - "+this.getInstance().getYear()+", Agua Potable");
		emissionOrder.setEmisor(p);
		
		Date emisionPeriod = findEmisionPeriod();
		
		MunicipalBondStatus mbs = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");

		Entry entry = null;

		RevenueService revenueService = ServiceLocator.getInstance().findResource("/gim/RevenueService/local");
		
		BudgetReference budgr;

		for (BudgetItem bi : items) {
			budgr =new BudgetReference();
			budgr.setCode(this.getInstance().getCode().toString());
			budgr.setBudgetCode(this.getInstance().getCode()+" - "+this.getInstance().getYear());
			budgr.setCadastralCode(this.getInstance().getCadastralCode());
			
			System.out.println("............ " + bi.getQuantity());
			System.out.println("------------ " + bi.getTotal());

			entry = bi.getBudgetEntry().getEntry();

			// emision normal
			Long quantity=bi.getQuantity().longValue();
			if (quantity == 1) {
				
				EntryValueItem entryValueItem = new EntryValueItem();
				entryValueItem.setDescription(emissionOrder.getDescription());
				entryValueItem.setMainValue(bi.getTotal());
				entryValueItem.setServiceDate(currentDate);
				//entryValueItem.setExpirationDate(expirationDate);
				entryValueItem.setResetValue(Boolean.FALSE);
				entryValueItem.setReference("");
				entryValueItem.setServiceDate(currentDate);

				MunicipalBond mb = revenueService.createMunicipalBond(resident, entry, fiscalPeriod, entryValueItem, true);

				mb.setAdjunct(budgr);
				//mb.setExpirationDate(expirationDate);
				mb.setServiceDate(currentDate);
				mb.setEmisionPeriod(currentDate);
				mb.setEmitter(p);
				mb.setOriginator(p);
				
				mb.setTimePeriod(entry.getTimePeriod());
				mb.setCreationTime(new Date());
				//mb.setPaidTotal(bi.getTotal());
				mb.setMunicipalBondStatus(mbs);
				mb.calculateValue();
				mb.setEmisionPeriod(emisionPeriod);
				//mb.setAddress(resident.getCurrentAddress() != null ? resident.getCurrentAddress().toString() : "");
				
				emissionOrder.add(mb);
			}

			// emision fee
			
			if (quantity > 1) {

				BigDecimal value = bi.getTotal().divide(bi.getQuantity(), 3,RoundingMode.HALF_EVEN);

				Date serviceDate = currentDate;

				System.out.println(".... el value " + value + " ------------ "+ serviceDate);
				System.out.println(".... el feeee " + bi.getQuantity()+ " ------------ " + serviceDate);

				for (int i = 1; i <= quantity; i++) {
					try {
						Date expirationDate = calculateNextDate(serviceDate,entry.getTimePeriod());
						EntryValueItem entryValueItem = new EntryValueItem();
						entryValueItem.setDescription(emissionOrder.getDescription());
						entryValueItem.setMainValue(value);
						entryValueItem.setServiceDate(serviceDate);
						entryValueItem.setExpirationDate(expirationDate);
						entryValueItem.setResetValue(Boolean.FALSE);
						entryValueItem.setReference("");
						

						MunicipalBond mb = revenueService.createMunicipalBond(resident, entry, fiscalPeriod, entryValueItem, true);

						mb.setAdjunct(budgr);
						mb.setExpirationDate(expirationDate);
						mb.setServiceDate(serviceDate);
						mb.setEmisionPeriod(serviceDate);
						mb.setEmitter(p);
						mb.setOriginator(p);
						
						mb.setTimePeriod(entry.getTimePeriod());
						mb.setCreationTime(new Date());
						//mb.setPaidTotal(bi.getTotal());
						mb.setMunicipalBondStatus(mbs);
						mb.calculateValue();
						mb.setEmisionPeriod(emisionPeriod);
						//mb.setAddress(resident.getCurrentAddress() != null ? resident.getCurrentAddress().toString() : "");
						
						serviceDate = expirationDate;
						
						emissionOrder.add(mb);
						
					} catch (Exception e) {
						String messages = ResourceBundle.instance().getString(e.getMessage());
						StatusMessages.instance().add(Severity.ERROR, messages, entry.getName(), serviceDate);
						System.out.println(messages + "\n" + entry.getName() + "\n" + serviceDate);
					}
				}
			}
		}
		return emissionOrder;
	}

	public Date calculateNextDate(Date startDate, TimePeriod timePeriod) {
		Calendar cstartDate = Calendar.getInstance();
		cstartDate.setTime(startDate);
		if (timePeriod != null)
			//cstartDate.add(Calendar.DATE, timePeriod.getDaysNumber());
			cstartDate.add(Calendar.DATE, 30);
		return cstartDate.getTime(); 
	}

	public String startPreEmission() {
		if (waterService == null)
			waterService = ServiceLocator.getInstance().findResource(WATER_SERVICE_NAME);
		try {

			EmissionOrder eo = createEmissionOrder(preEmissionBudgetItem);
			waterService.saveEmissionOrderBudget(eo, true);
			this.getInstance().setIsServiceOrderGenerate(new Boolean(true));
			this.update();

			String message = Interpolator.instance().interpolate(
					"El presupuesto " + this.getInstance().getCode() + " - "
							+ this.getInstance().getYear()
							+ " ha sido pre-emitido", new Object[0]);
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.INFO,message);
					
			return "preEmited";
		} catch (Exception e) {
			e.printStackTrace();
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}
	

	public List<Entry> getPreEmissionEntries() {
		return preEmissionEntries;
	}

	public void setPreEmissionEntries(List<Entry> preEmissionEntries) {
		this.preEmissionEntries = preEmissionEntries;
	}

	public List<BudgetItem> getPreEmissionBudgetItem() {
		return preEmissionBudgetItem;
	}

	public void setPreEmissionBudgetItem(List<BudgetItem> preEmissionBudgetItem) {
		this.preEmissionBudgetItem = preEmissionBudgetItem;
	}

	public List<BudgetItemGrouped> getBudgetItemsGrouped() {
		return budgetItemsGrouped;
	}

	public void setBudgetItemsGrouped(List<BudgetItemGrouped> budgetItemsGrouped) {
		this.budgetItemsGrouped = budgetItemsGrouped;
	}

	@Override
	public String persist() {
		// TODO Auto-generated method stub
		this.getInstance().setRegister(userSession.getPerson());
		return super.persist();
	}

	@Override
	public String update() {
		this.getInstance().setRegister(userSession.getPerson());
		return super.update();
	}

	public String getIdentificationNumberDoneBy() {
		return identificationNumberDoneBy;
	}

	public void setIdentificationNumberDoneBy(String identificationNumberDoneBy) {
		this.identificationNumberDoneBy = identificationNumberDoneBy;
	}
	
	
	public void deleteBudget(){
		this.remove();
	}

	
	// para el detalle del presupuesto
	
	public void budgetDetail(BudgetDTO budget){
		String sentence = "SELECT mb FROM MunicipalBond mb "
				+ "JOIN mb.adjunct adjunct "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "WHERE "
				+ "mb.serviceDate between :firstDayMonth and :lastDayMonth "
				+ "and res.id=:residentId and "
				+ "adjunct.code like concat(:code,'%')";
		
		String code = budget.getCode().toString();
		
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX code " + budget.getCode());
		System.out.println("start "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(budget.getDate()));
		
		int actualMonth;
		int year;
		
		actualMonth = Integer.parseInt(new SimpleDateFormat("MM").format(budget.getDate())) ;
		year = Integer.parseInt(new SimpleDateFormat("yyyy").format(budget.getDate()));
								
		Calendar startDate = Calendar.getInstance();
		startDate.set(year, (actualMonth - 1), 1);

		int endDayOfMonth = startDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		
		Calendar endDate = Calendar.getInstance();
		endDate.set(year, (actualMonth - 1), endDayOfMonth);
		
		System.out.println("start "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(startDate.getTime()));
		System.out.println("end   "+new SimpleDateFormat("yyyy-MM-dd EEEE").format(endDate.getTime()));
		
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX start date " + startDate.getTime());
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX end date " + endDate.getTime());
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX resident id " + budget.getResidentId());
		
		Query q = this.getEntityManager().createQuery(sentence);
		q.setParameter("firstDayMonth", startDate.getTime());
		q.setParameter("lastDayMonth", endDate.getTime());
		q.setParameter("code", code);
		q.setParameter("residentId", budget.getResidentId());
		
		this.municipalBondsReport = q.getResultList();
		
	}

	public List<MunicipalBond> getMunicipalBondsReport() {
		return municipalBondsReport;
	}

	public void setMunicipalBondsReport(List<MunicipalBond> municipalBondsReport) {
		this.municipalBondsReport = municipalBondsReport;
	}

}
