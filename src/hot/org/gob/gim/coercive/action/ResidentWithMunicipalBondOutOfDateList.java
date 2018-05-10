package org.gob.gim.coercive.action;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.coercive.view.ResidentItem;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.action.ReceiptPrintingManager;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessage.Severity;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;

@Name("residentWithMunicipalBondOutOfDateList")
@Scope(ScopeType.CONVERSATION)
public class ResidentWithMunicipalBondOutOfDateList extends
		EntityQuery<ResidentItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2904627074665502302L;

	private static final String EJBQL = "select NEW org.gob.gim.coercive.view.ResidentItem(resident.id,resident.identificationNumber,resident.name,count(mb),count(imp),sum(mb.value)) from Impugnment imp "
			+ "right join imp.municipalBond mb left join  mb.resident resident left join mb.entry entry";

	
	
	private static final String PENDING_BOND_STATUS_ID = "MUNICIPAL_BOND_STATUS_ID_PENDING";
	
	private MunicipalBondStatus municipalBondStatus = findPendingMunicipalBondStatus();
	
	private MunicipalBondType municipalBondType = MunicipalBondType.CREDIT_ORDER;
	
	private SystemParameterService systemParameterService;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private Charge revenueCharge;
	
	private Delegate revenueDelegate;
	
	private Charge finantialCharge;
	
	private Delegate finantialDelegate;
	
	private Long daysForOutOfDate = 1L;
	
	private BigDecimal amount = new BigDecimal(1);

	
	private Date expirationDate;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;
	
	@In(create = true)
	ReceiptPrintingManager receiptPrintingManager;

	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}
	
	private static final Pattern SUBJECT_PATTERN = Pattern.compile("^select\\s+(\\w+(?:\\s*\\.\\s*\\w+)*?)(?:\\s*,\\s*(\\w+(?:\\s*\\.\\s*\\w+)*?))*?\\s+from", Pattern.CASE_INSENSITIVE); 
	   private static final Pattern FROM_PATTERN = Pattern.compile("(^|\\s)(from)\\s",       Pattern.CASE_INSENSITIVE);
	   private static final Pattern WHERE_PATTERN = Pattern.compile("\\s(where)\\s",         Pattern.CASE_INSENSITIVE);
	   private static final Pattern ORDER_PATTERN = Pattern.compile("\\s(order)(\\s)+by\\s", Pattern.CASE_INSENSITIVE);
	   private static final Pattern GROUP_PATTERN = Pattern.compile("\\s(group)(\\s)+by\\s", Pattern.CASE_INSENSITIVE);
	
	@Override
	protected String getCountEjbql() {
		// TODO Auto-generated method stub
		String ejbql = getRenderedEjbql();
			      
	      Matcher fromMatcher = FROM_PATTERN.matcher(ejbql);
	      if ( !fromMatcher.find() )
	      {
	         throw new IllegalArgumentException("no from clause found in query");
	      }
	      int fromLoc = fromMatcher.start(2);
	      
	      // TODO can we just create a protected method that builds the query w/o the order by and group by clauses?
	      Matcher orderMatcher = ORDER_PATTERN.matcher(ejbql);
	      int orderLoc = orderMatcher.find() ? orderMatcher.start(1) : ejbql.length();

	      Matcher groupMatcher = GROUP_PATTERN.matcher(ejbql);
	      int groupLoc = groupMatcher.find() ? groupMatcher.start(1) : orderLoc;

	      Matcher whereMatcher = WHERE_PATTERN.matcher(ejbql);
	      int whereLoc = whereMatcher.find() ? whereMatcher.start(1) : groupLoc;

	      String subject;
	      if (getGroupBy() != null) {
	         subject = "distinct resident.id";
	      }
	      // to be JPA-compliant, we need to make this query like "select count(u) from User u"
	      // however, Hibernate produces queries some databases cannot run when the primary key is composite
	      else {
	          Matcher subjectMatcher = SUBJECT_PATTERN.matcher(ejbql);
	          if ( subjectMatcher.find() )
	          {
	             subject = subjectMatcher.group(1);
	          }
	          else
	          {
	             throw new IllegalStateException("invalid select clause for query");
	          }
	      }
	        
	      return new StringBuilder(ejbql.length() + 15).append("select count(").append(subject).append(") ").
	         append(ejbql.substring(fromLoc, whereLoc).replace("join fetch", "join")).
	         append(ejbql.substring(whereLoc, groupLoc)).toString().trim();
	}

	private static final String[] RESTRICTIONS = {
			"resident.id = #{residentWithMunicipalBondOutOfDateList.resident.id}",
			"entry.id = #{residentWithMunicipalBondOutOfDateList.entry.id}",				
			"mb.value >= #{residentWithMunicipalBondOutOfDateList.amount}",				
			"mb.municipalBondType = #{residentWithMunicipalBondOutOfDateList.municipalBondType}",			
			"mb.municipalBondStatus = #{residentWithMunicipalBondOutOfDateList.municipalBondStatus}",
			"mb.expirationDate <= #{residentWithMunicipalBondOutOfDateList.expirationDate} and mb.notification IS NULL",};

	public ResidentWithMunicipalBondOutOfDateList() {
		setEjbql(EJBQL);		
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		System.out.println("EJB:"+EJBQL);
		System.out.println("REstricciones"+RESTRICTIONS);
		setOrder("sum(mb.value) DESC");
		setGroupBy("resident.id,resident.identificationNumber,resident.name");		
		setMaxResults(25);
		Calendar now = Calendar.getInstance();
		if (this.expirationDate == null) {
			setExpirationDate(now.getTime());
		}
		if (!this.getDaysForOutOfDate().equals(0L)) {
			now.add(Calendar.DAY_OF_MONTH,(int) (daysForOutOfDate.longValue() * -1L));
			setExpirationDate(now.getTime());// la fecha menos los n días
		}		
	}
	
	
	private boolean rebuiltRequired = false;
	
	public boolean isRebuiltRequired() {
		return rebuiltRequired;
	}

	public void setRebuiltRequired(boolean rebuiltRequired) {
		this.rebuiltRequired = rebuiltRequired;	
	}
	
	public void cleanSelectedList(){
		selectedList = new ArrayList<ResidentItem>();
	}
	
	private List <ResidentItem> selectedList;
	
	public void addResidentItem(ResidentItem ri){		
		if(allResidentsSelected && !ri.isSelected()) allResidentsSelected = Boolean.FALSE;
		if(selectedList == null) selectedList = new ArrayList<ResidentItem>();			
		
		if(ri.isSelected()){
			selectedList.add(ri);			
		}else{
			selectedList.remove(ri);
		}
		
	}
	
	private void fillSelectedList(List<ResidentItem> list){
		if(selectedList == null) selectedList = new ArrayList<ResidentItem>();
		for(ResidentItem ri: list){
			selectedList.add(ri);
		}
	}
	
	public void selectAllResidentItems() {		
		if(allResidentsSelected)fillSelectedList(getResultList());
		
		for (ResidentItem ri : selectedList) {
			ri.setSelected(allResidentsSelected);
		}
		
		if(!allResidentsSelected) selectedList = new ArrayList<ResidentItem>();	
		
		setRebuiltRequired(!allResidentsSelected); // bloquear/desbloquear para
													// poder seleccionar
		addFacesMessageFromResourceBundle("selectItemsLocked");
	
	}
	
	private boolean allResidentsSelected = false;
	
	public void setAllResidentsSelected(boolean allResidentsSelected) {
		this.allResidentsSelected = allResidentsSelected;
	}

	public boolean isAllResidentsSelected() {
		return allResidentsSelected;
	}
	
	@In
	FacesMessages facesMessages;
	
	public void searchBonds(){
		isFirstTime = Boolean.TRUE;
		allResidentsSelected = Boolean.FALSE;
		getResultList();		
	}
			
	@Override
	@Transactional
	public List<ResidentItem> getResultList() {
		return super.getResultList();
	}
	
	private Entry entry;
	
	public Entry getEntry() {		
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	private List<MunicipalBond> municipalBonds;

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}
	
	private Long residentId;
	
	private Long entryId;

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {		
		this.entryId = entryId;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {		
		this.residentId = residentId;
	}
	
	private Boolean isFirstTime = Boolean.TRUE;
	
	private Boolean detailFromNotification = Boolean.FALSE;
	
	public Boolean getDetailFromNotification() {
		return detailFromNotification;
	}

	public void setDetailFromNotification(Boolean detailFromNotification) {
		this.detailFromNotification = detailFromNotification;
	}

	/**
	 * Recupera todos los municipalBonds pendientes por contribuyente y/o por rubro
	 */
	public void loadPendingBonds(){
		if(!isFirstTime) return;
		isFirstTime = Boolean.FALSE;
				
		if(entryId != null && entry == null){
			entry = findEntryById(entryId);
			entryCode = entry.getCode();
		}
		
		if(!detailFromNotification && residentId != null){		
			Query query = null;
			if(entry == null){
				query = getEntityManager().createNamedQuery("MunicipalBond.findExpiratedByResidentIdAndAmount");
			}else{
				query = getEntityManager().createNamedQuery("MunicipalBond.findExpiratedByResidentIdAndEntryIdAndAmount");
				query.setParameter("entryId", entry.getId());
			}		
			List<Long> ids = new ArrayList<Long>();
			ids.add(residentId);
			query.setParameter("residentIds", ids);
			query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
			query.setParameter("municipalBondStatusId", municipalBondStatus.getId());			
			query.setParameter("expirationDate", expirationDate);
			query.setParameter("value", amount);
			System.out.println("residentIds:"+ ids);
			System.out.println("municipalBondType:"+ MunicipalBondType.CREDIT_ORDER);
			System.out.println("municipalBondStatusId:"+ municipalBondStatus.getId());
			System.out.println("expirationDate:"+ expirationDate);
			System.out.println("value:"+ amount);
			municipalBonds = query.getResultList();
		}
			
		if(resident == null)resident = findResidentById(residentId);
		if(resident != null) identificationNumber = resident.getIdentificationNumber();
		
		List<MunicipalBond> bondsForCalculate = filterOnlyPendingBonds(municipalBonds);
		
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		try {
			incomeService.calculatePayment(bondsForCalculate, new Date(), true, true);
			incomeService.updateMunicipalBonds(bondsForCalculate);
			totalPending();
		} catch (EntryDefinitionNotFoundException e) {
			facesMessages.addToControl("savePaymentBtn", Severity.ERROR,"#{messages['entryDefinition.entryDefinitionNotFoundException']}");
			e.printStackTrace();
		}
	}
	
	private List<MunicipalBond> filterOnlyPendingBonds(List<MunicipalBond> municipalBonds){
		List<MunicipalBond> list = new ArrayList<MunicipalBond>();
		if(municipalBonds == null) return list;
		MunicipalBondStatus pending = findPendingMunicipalBondStatus();		
		for(MunicipalBond mb:municipalBonds){
			if(!list.contains(mb) && mb.getMunicipalBondStatus().equals(pending)) list.add(mb);
		}
		return list;
	}
	
	private Entry findEntryById(Long id){	
		Query query = getEntityManager().createNamedQuery("Entry.findById");
		query.setParameter("entryId", id);
		return (Entry) query.getSingleResult();
	}
	
	private Resident findResidentById(Long id){		
		if(id == null) return null;
		Query query = getEntityManager().createNamedQuery("Resident.findById");
		query.setParameter("id", id);		
		return (Resident) query.getSingleResult();
	}
	
	private BigDecimal total;
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	private void totalPending(){
		total = BigDecimal.ZERO;
		if(municipalBonds == null) return;
		MunicipalBondStatus pending = findPendingMunicipalBondStatus();		
		for(MunicipalBond mb :municipalBonds){
			if(mb.getMunicipalBondStatus().equals(pending))total = total.add(mb.getPaidTotal());
		}
	}

	private Resident resident;
	
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public MunicipalBondStatus findPendingMunicipalBondStatus(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		return systemParameterService.materialize(MunicipalBondStatus.class, PENDING_BOND_STATUS_ID);		
	}
	
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}
	
	private String identificationNumber;
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	private List<Resident> residents;
	
	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	
	private List<Entry> entries;

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
	}
	
	public void entrySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		if (entry.getAccount() == null) {
			setEntryCode(entry.getCode());
		} else {
			setEntryCode(entry.getAccount().getAccountCode());
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.NEVER)
	public void searchResidentByCriteria() {
		System.out.println("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}
	
	
	/**
	 * Imprime todos los municipalbond como títulos de crédito
	 * @return String
	 */
	public String printAll() {
		if(revenueCharge == null) loadCharges();
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		
		ArrayList<Deposit> depositList = new ArrayList<Deposit>();
		
		Long[] printings = {0L};
		
		for(MunicipalBond mb : municipalBonds){
			MunicipalBond municipalBond = incomeService.loadMunicipalBond(mb.getId());
			Set<Deposit> deposits = municipalBond.getDeposits();		
			
			Deposit depositToPrint = null;
			if(deposits.size() > 0){
				depositToPrint = (Deposit) Arrays.asList(deposits.toArray()).get(deposits.size() - 1);// deposits.get(deposits.size() - 1);
			}else{
				depositToPrint = new Deposit();
				depositToPrint.setBalance(BigDecimal.ZERO);
			}
			
			depositToPrint.setMunicipalBond(municipalBond);			
			depositList.add(depositToPrint);
		}			
		
		receiptPrintingManager.setPrintings(printings);
		receiptPrintingManager.setIsCertificate(true);
			
		String result = receiptPrintingManager.print(depositList);
						
		System.out.println("RESULTADO ----> " + result + " " + receiptPrintingManager);
		return result;
	}
		
	/**
	 * Imprime un municipalbond como título de crédito
	 * @return String
	 */
	public String print(Long municipalBondId) {
		if(revenueCharge == null) loadCharges();
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);		
		MunicipalBond municipalBond = incomeService.loadMunicipalBond(municipalBondId);
		Set<Deposit> deposits = municipalBond.getDeposits();		
		
		Deposit depositToPrint = null;
		if(deposits.size() > 0){
			depositToPrint = (Deposit) Arrays.asList(deposits.toArray()).get(deposits.size() - 1);
		}else{
			depositToPrint = new Deposit();
			depositToPrint.setBalance(BigDecimal.ZERO);
		}
		
		depositToPrint.setMunicipalBond(municipalBond);

		Long printingsNumber = new Long(municipalBond.getPrintingsNumber());		
		Long[] printings = { printingsNumber };
		receiptPrintingManager.setPrintings(printings);
		receiptPrintingManager.setIsCertificate(true);
			
		String result = receiptPrintingManager.print(depositToPrint);
						
		System.out.println("RESULTADO ----> " + result + " " + receiptPrintingManager);
		return result;
	}
	
	public void loadCharges() {
		
		finantialCharge = getCharge("DELEGATE_ID_FINANTIAL");
		if (finantialCharge != null) {
			for (Delegate d : finantialCharge.getDelegates()) {
				if (d.getIsActive()) finantialDelegate = d;
			}
		}
		
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive()) revenueDelegate = d;
			}
		}
	}
	
	public Charge getCharge(String systemParameter) {
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,systemParameter);
		return charge;
	}
	
	private String completeEntryCode(String code){
		StringBuffer codeBuffer = new StringBuffer(code.trim());
		while (codeBuffer.length() < 5){
			codeBuffer.insert(0,'0');
		}
		return codeBuffer.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.NEVER)
	public Entry findEntryByCode(String code) {
		code = this.completeEntryCode(code);
		Query query = getEntityManager().createNamedQuery("Entry.findByCode");
		query.setParameter("code", code);
		List<Entry> results = (List<Entry>)query.getResultList();
		if (! results.isEmpty())
			return results.get(0); 
		return null;
	}
	
	public void searchEntry() {		
		if (entryCode != null && entryCode.length() > 0) {
			Entry entry = findEntryByCode(entryCode);
			if (entry != null) {
				this.entry = entry;
				this.setEntry(entry);
				if (entry.getAccount() == null) {
					setEntryCode(entry.getCode());
				} else {
					setEntryCode(entry.getAccount().getAccountCode());
				}
			}
		}else{
			this.entry = null;			
		}
		
	}
	

	@Transactional(TransactionPropagationType.NEVER)
	public void searchResident() {
		System.out.println("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		if(identificationNumber == null || identificationNumber.trim().isEmpty()){
			resident = null;
			return;
		}
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			System.out.println("RESIDENT CHOOSER ACTION " + resident.getName());
			setResident(resident);
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void searchEntryByCriteria() {
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
			entries = findByCriteria(criteriaEntry);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(TransactionPropagationType.NEVER)
	public List<Entry> findByCriteria(String criteria) {
		Query query = getEntityManager().createNamedQuery("Entry.findByCriteria");
		query.setParameter("criteria", criteria);
		return query.getResultList();
	}
	
	protected String criteria;
	
	protected String entryCode;
	
	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	protected String criteriaEntry;
	
	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}
	
	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(String criteria) {		
		this.criteria = criteria;
	}

	/**
	 * @return the municipalBondType
	 */
	public MunicipalBondType getMunicipalBondType() {
		return municipalBondType;
	}

	/**
	 * @param municipalBondType
	 *            the municipalBondType to set
	 */
	public void setMunicipalBondType(MunicipalBondType municipalBondType) {
		this.municipalBondType = municipalBondType;
	}

	public void setDaysForOutOfDate(Long daysForOutOfDate) {
		this.daysForOutOfDate = daysForOutOfDate;
	}

	public Long getDaysForOutOfDate() {
		return daysForOutOfDate;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getExpirationDate() {

		return expirationDate;
	}

	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}

	public Charge getFinantialCharge() {
		return finantialCharge;
	}

	public void setFinantialCharge(Charge finantialCharge) {
		this.finantialCharge = finantialCharge;
	}

	public Delegate getFinantialDelegate() {
		return finantialDelegate;
	}

	public void setFinantialDelegate(Delegate finantialDelegate) {
		this.finantialDelegate = finantialDelegate;
	}

	public List<ResidentItem> getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(List<ResidentItem> selectedList) {
		this.selectedList = selectedList;
	}
	
	/**
	 * Crea las notificaciones para los items seleccionados
	 * @throws IOException 
	 */

}
