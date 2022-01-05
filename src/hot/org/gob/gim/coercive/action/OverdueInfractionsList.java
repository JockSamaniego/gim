package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.coercive.view.InfractionItem;
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

import ec.gob.gim.cadaster.model.TransferDomainComplement;
import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;

@Name("overdueInfractionsList")
@Scope(ScopeType.CONVERSATION)
public class OverdueInfractionsList extends EntityQuery<InfractionItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2904627074665502302L;

	private static final String EJBQL = "select NEW org.gob.gim.coercive.view.InfractionItem(di.identification, di.name, count(di), sum(di.value), sum(di.interest), sum(di.totalValue)) "+
										"from Datainfraction di ";

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private String identification;
	private String name;
	private String licensePlate;
	private Date emisionFrom;
	private Date emisionUntil;
	private Date expirationFrom;
	private Date expirationUntil;
	private String ticket;
	private String article;
	
	//
	private String identificationNumber;
	private Boolean isFirstTime = Boolean.TRUE;
	private Boolean detailFromNotification = Boolean.FALSE;
	private BigDecimal total = BigDecimal.ZERO;
	
	private static final Pattern SUBJECT_PATTERN = Pattern.compile(
			"^select\\s+(\\w+(?:\\s*\\.\\s*\\w+)*?)(?:\\s*,\\s*(\\w+(?:\\s*\\.\\s*\\w+)*?))*?\\s+from",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern FROM_PATTERN = Pattern.compile("(^|\\s)(from)\\s", Pattern.CASE_INSENSITIVE);
	private static final Pattern WHERE_PATTERN = Pattern.compile("\\s(where)\\s", Pattern.CASE_INSENSITIVE);
	private static final Pattern ORDER_PATTERN = Pattern.compile("\\s(order)(\\s)+by\\s", Pattern.CASE_INSENSITIVE);
	private static final Pattern GROUP_PATTERN = Pattern.compile("\\s(group)(\\s)+by\\s", Pattern.CASE_INSENSITIVE);

	@Override
	protected String getCountEjbql() {
		// TODO Auto-generated method stub
		String ejbql = getRenderedEjbql();

		Matcher fromMatcher = FROM_PATTERN.matcher(ejbql);
		if (!fromMatcher.find()) {
			throw new IllegalArgumentException("no from clause found in query");
		}
		int fromLoc = fromMatcher.start(2);

		// TODO can we just create a protected method that builds the query w/o the
		// order by and group by clauses?
		Matcher orderMatcher = ORDER_PATTERN.matcher(ejbql);
		int orderLoc = orderMatcher.find() ? orderMatcher.start(1) : ejbql.length();

		Matcher groupMatcher = GROUP_PATTERN.matcher(ejbql);
		int groupLoc = groupMatcher.find() ? groupMatcher.start(1) : orderLoc;

		Matcher whereMatcher = WHERE_PATTERN.matcher(ejbql);
		int whereLoc = whereMatcher.find() ? whereMatcher.start(1) : groupLoc;

		String subject;
		if (getGroupBy() != null) {
			subject = "distinct di.identification";
		}
		// to be JPA-compliant, we need to make this query like "select count(u) from
		// User u"
		// however, Hibernate produces queries some databases cannot run when the
		// primary key is composite
		else {
			Matcher subjectMatcher = SUBJECT_PATTERN.matcher(ejbql);
			if (subjectMatcher.find()) {
				subject = subjectMatcher.group(1);
			} else {
				throw new IllegalStateException("invalid select clause for query");
			}
		}

		return new StringBuilder(ejbql.length() + 15).append("select count(").append(subject).append(") ")
				.append(ejbql.substring(fromLoc, whereLoc).replace("join fetch", "join"))
				.append(ejbql.substring(whereLoc, groupLoc)).toString().trim();
	}

	private static final String[] RESTRICTIONS = { 		
			"di.identification = #{overdueInfractionsList.identification}",
			"di.name = #{overdueInfractionsList.name}", 
			/*"di.licensePlate = #{overdueInfractionsList.licensePlate}",
			"di.article = #{overdueInfractionsList.article}",			
			"di.ticket = #{overdueInfractionsList.ticket}",
			"di.emision >= #{overdueInfractionsList.emisionFrom} ",
			"di.emision <= #{overdueInfractionsList.emisionUntil} ", 
			"di.expiration >= #{overdueInfractionsList.expirationFrom}",
			"di.expiration <= #{overdueInfractionsList.expirationUntil}",*/
	};

	public OverdueInfractionsList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

		// System.out.println(getRestrictionExpressionStrings());
		// System.out.println("EJB:"+EJBQL);
		// System.out.println("REstricciones"+RESTRICTIONS);
		setOrder("sum(di.totalValue) DESC");
		//setGroupBy("di.resident.id,resident.identificationNumber,resident.name");
		setGroupBy("di.identification, di.name");
		setMaxResults(25);
		Calendar now = Calendar.getInstance();

		if (this.expirationFrom == null) {
			setExpirationFrom(now.getTime());
		}

		if (this.expirationUntil == null) {
			setExpirationUntil(now.getTime());
		}

		if (this.emisionFrom == null) {
			setEmisionFrom(now.getTime());
		}

		if (this.emisionUntil == null) {
			setEmisionUntil(now.getTime());
		}

	}

	private boolean rebuiltRequired = false;

	public boolean isRebuiltRequired() {
		return rebuiltRequired;
	}

	public void setRebuiltRequired(boolean rebuiltRequired) {
		this.rebuiltRequired = rebuiltRequired;
	}

	public void cleanSelectedList() {
		selectedList = new ArrayList<ResidentItem>();
	}

	private List<ResidentItem> selectedList;

	public void addResidentItem(ResidentItem ri) {
		if (allResidentsSelected && !ri.isSelected())
			allResidentsSelected = Boolean.FALSE;
		if (selectedList == null)
			selectedList = new ArrayList<ResidentItem>();

		if (ri.isSelected()) {
			selectedList.add(ri);
		} else {
			selectedList.remove(ri);
		}

	}

	private void fillSelectedList(List<ResidentItem> list) {
		if (selectedList == null)
			selectedList = new ArrayList<ResidentItem>();
		for (ResidentItem ri : list) {
			selectedList.add(ri);
		}
	}

	public void selectAllResidentItems() {

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

	public void searchBonds() {
		isFirstTime = Boolean.TRUE;
		allResidentsSelected = Boolean.FALSE;
		getResultList();
	}

	private Entry entry;

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	private List<Datainfraction> infractions;

	 

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


	public Boolean getDetailFromNotification() {
		return detailFromNotification;
	}

	public void setDetailFromNotification(Boolean detailFromNotification) {
		this.detailFromNotification = detailFromNotification;
	}

	/**
	 * Recupera todos las infracciones x identificacion 
	 */
	@SuppressWarnings("unchecked")
	public void loadPendingInfractions() {
		if (!isFirstTime)
			return;
		isFirstTime = Boolean.FALSE;

		 
		if(!detailFromNotification && identificationNumber != null){		
			Query query = getEntityManager().createQuery("Select di from Datainfraction di where di.identification=:identificationNumber");					
			query.setParameter("identificationNumber", identificationNumber);
		 
			infractions = query.getResultList();
			
			totalPending();
		}
	}
	
	
	private void totalPending(){
		total = BigDecimal.ZERO;
		if(infractions == null) return;
		 
		for(Datainfraction infraction :infractions){
			// TO DO verificar a futuro los estados
			//if(mb.getMunicipalBondStatus().equals(pending) || mb.getMunicipalBondStatus().equals(agreement)){
				total = total.add(infraction.getTotalValue());
			//}
		}
	}


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
		// System.out.println("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	/**
	 * Imprime todos los municipalbond como títulos de crédito
	 * 
	 * @return String
	 */
	public String printAll() {
		/*
		 * if(revenueCharge == null) loadCharges(); IncomeService incomeService =
		 * ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		 * 
		 * ArrayList<Deposit> depositList = new ArrayList<Deposit>();
		 * 
		 * Long[] printings = {0L};
		 * 
		 * for(MunicipalBond mb : municipalBonds){ MunicipalBond municipalBond =
		 * incomeService.loadMunicipalBond(mb.getId()); Set<Deposit> deposits =
		 * municipalBond.getDeposits();
		 * 
		 * Deposit depositToPrint = null; if(deposits.size() > 0){ depositToPrint =
		 * (Deposit) Arrays.asList(deposits.toArray()).get(deposits.size() - 1);//
		 * deposits.get(deposits.size() - 1); }else{ depositToPrint = new Deposit();
		 * depositToPrint.setBalance(BigDecimal.ZERO); }
		 * 
		 * depositToPrint.setMunicipalBond(municipalBond);
		 * depositList.add(depositToPrint); }
		 * 
		 * receiptPrintingManager.setPrintings(printings);
		 * receiptPrintingManager.setIsCertificate(true);
		 * 
		 * String result = receiptPrintingManager.print(depositList);
		 * 
		 * //System.out.println("RESULTADO ----> " + result + " " +
		 * receiptPrintingManager); return result;
		 */
		return null;
	}

	/**
	 * Imprime un municipalbond como título de crédito
	 * 
	 * @return String
	 */
	public String print(Long municipalBondId) {
		/*
		 * if(revenueCharge == null) loadCharges(); IncomeService incomeService =
		 * ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		 * MunicipalBond municipalBond =
		 * incomeService.loadMunicipalBond(municipalBondId); Set<Deposit> deposits =
		 * municipalBond.getDeposits();
		 * 
		 * Deposit depositToPrint = null; if(deposits.size() > 0){ depositToPrint =
		 * (Deposit) Arrays.asList(deposits.toArray()).get(deposits.size() - 1); }else{
		 * depositToPrint = new Deposit(); depositToPrint.setBalance(BigDecimal.ZERO); }
		 * 
		 * depositToPrint.setMunicipalBond(municipalBond);
		 * 
		 * Long printingsNumber = new Long(municipalBond.getPrintingsNumber()); Long[]
		 * printings = { printingsNumber };
		 * receiptPrintingManager.setPrintings(printings);
		 * receiptPrintingManager.setIsCertificate(true);
		 * 
		 * String result = receiptPrintingManager.print(depositToPrint);
		 * 
		 * //System.out.println("RESULTADO ----> " + result + " " +
		 * receiptPrintingManager); return result;
		 */
		return null;
	}

	protected String criteria;

	protected String entryCode;

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	/**
	 * Crea las notificaciones para los items seleccionados
	 * 
	 * @throws IOException
	 */

	// para impresión directa de titulos de credito desde el listado de
	// notificaciones
	// Jock Samaniego

	public String loadBondsForPrintReport(Long notificationId) {
		Notification not = getEntityManager().find(Notification.class, notificationId);
		//this.setMunicipalBonds(not.getMunicipalBonds());
		this.loadPendingInfractions();
		this.printAll();
		return "/income/report/CreditTitleForNotification.xhtml";
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Date getEmisionFrom() {
		return emisionFrom;
	}

	public void setEmisionFrom(Date emisionFrom) {
		this.emisionFrom = emisionFrom;
	}

	public Date getEmisionUntil() {
		return emisionUntil;
	}

	public void setEmisionUntil(Date emisionUntil) {
		this.emisionUntil = emisionUntil;
	}

	public Date getExpirationFrom() {
		return expirationFrom;
	}

	public void setExpirationFrom(Date expirationFrom) {
		this.expirationFrom = expirationFrom;
	}

	public Date getExpirationUntil() {
		return expirationUntil;
	}

	public void setExpirationUntil(Date expirationUntil) {
		this.expirationUntil = expirationUntil;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public List<Datainfraction> getInfractions() {
		return infractions;
	}

	public void setInfractions(List<Datainfraction> infractions) {
		this.infractions = infractions;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	 
	
	
}
