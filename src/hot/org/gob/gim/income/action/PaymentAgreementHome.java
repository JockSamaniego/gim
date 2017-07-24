package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.income.view.MunicipalBondItem;
import org.gob.loja.gim.ws.service.PaymentService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.Dividend;
import ec.gob.gim.income.model.Payment;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondType;

@Name("paymentAgreementHome")
public class PaymentAgreementHome extends EntityHome<PaymentAgreement> {

	private static final long serialVersionUID = 1L;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	@Logger
	Log logger;
	
	@In
	FacesMessages facesMessages;
	
	private List<MunicipalBondItem> municipalBondItems;
	
	private BigDecimal initialDividend;
	
	private Boolean allBondsSelected;
		
	private String criteria;
	
	private String identificationNumber;
	
	private List<Resident> residents;
	
	private Boolean readyForPrint = Boolean.FALSE;

	private SystemParameterService systemParameterService;
	
	BigDecimal paymentAgreementTotal = BigDecimal.ZERO;
	
	@In(scope=ScopeType.SESSION, value="userSession")
	UserSession userSession;

	//@macartuche 
	//para saber lo pagado hasta el momento
	BigDecimal payed = BigDecimal.ZERO;
	BigDecimal balanceForPay = BigDecimal.ZERO;
	BigDecimal canceledValue = BigDecimal.ZERO;
	List<Payment> payments = new ArrayList<Payment>();
	
	
	public BigDecimal getPayed() {
		return payed;
	}

	public void setPayed(BigDecimal payed) {
		this.payed = payed;
	}
	
	public BigDecimal getBalanceForPay() {
		return balanceForPay;
	}

	public void setBalanceForPay(BigDecimal balanceForPay) {
		this.balanceForPay = balanceForPay;
	}
	 

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public BigDecimal getCanceledValue() {
		return canceledValue;
	}

	public void setCanceledValue(BigDecimal canceledValue) {
		this.canceledValue = canceledValue;
	}

	//fin valor pagado
	



	public void setPaymentAgreementId(Long id) {
		setId(id);
	}

	public Long getPaymentAgreementId() {
		return (Long) getId();
	}	
	
	public Boolean getReadyForPrint() {
		return readyForPrint;
	}

	public void setReadyForPrint(Boolean readyForPrint) {
		this.readyForPrint = readyForPrint;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
		
	}

	public void wire() {
		getInstance();
		if(getInstance().getId() != null)
			identificationNumber = getInstance().getResident().getIdentificationNumber(); 
		chargeControlMunicipalBondStates();
		toActivePaymentAgreement();		
		
		if(getInstance().getId() != null) {
			//@author macartuche
			//valor pagado
			calculateCanceledBonds();
			calculateBalanceForPay();
			calculatePayedValue();
		}
	}
	
	
	//@author macartuche
	//valor pagado y saldos
	@In(create=true)
	PaymentHome paymentHome;
	public void calculatePayedValue(){		
		if(getInstance().getId()!=null){
			 this.getInstance().getId();
			 Query q = getEntityManager().createQuery("Select SUM(d.value) from Deposit d "
			 		+ "where d.municipalBond.paymentAgreement.id=:id and d.status=:status "
			 		+ "and d.municipalBond.municipalBondStatus.id != :canceled");
			 q.setParameter("id", this.getInstance().getId());
			 q.setParameter("status", FinancialStatus.VALID);
			 q.setParameter("canceled", new Long(9));
			 
			 this.payed = (BigDecimal)q.getSingleResult();	
			 if(this.payed==null) {
				 this.payed= BigDecimal.ZERO;
			 }
		}
	}
	
	public void calculateBalanceForPay() {
		balanceForPay = BigDecimal.ZERO;
		paymentHome.setPaymentAgreement(this.getInstance());
		paymentHome.calculateTotals();
		for(MunicipalBond bond: paymentHome.getMunicipalBonds()) {
			balanceForPay = balanceForPay.add(bond.getPaidTotal());
		}
	}
	
	public void calculateCanceledBonds() {
		if(getInstance().getId()!=null) {
			Query q = getEntityManager().createQuery("Select SUM(m.value) from MunicipalBond m "
					+ "where m.municipalBondStatus.id=:canceled and m.paymentAgreement.id=:agreement");
			q.setParameter("canceled", 9L); //anulados o dados de baja
			q.setParameter("agreement", getInstance().getId());
			this.canceledValue = (BigDecimal)q.getSingleResult();	
			 if(this.canceledValue==null) {
				 this.canceledValue= BigDecimal.ZERO;
			 }
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadDeposits() {
		if(getInstance().getId()!=null) {
			 
			Query q = getEntityManager().createQuery("Select DISTINCT(p) from Payment p "
					+ "JOIN p.deposits d "
					+ "JOIN d.municipalBond m "
					+ "WHERE m.paymentAgreement.id=:id and d.status=:status "
					+ "and m.municipalBondStatus.id !=:canceled "
					+ "ORDER by p.date, p.time ");
			q.setParameter("id", getInstance().getId());
			q.setParameter("status", FinancialStatus.VALID);
			q.setParameter("canceled", new Long(9));
			payments = (List<Payment>)q.getResultList();
		}
	}
	//fin abonos y saldos @macartuche

	public boolean isWired() {
		return true;
	}

	public PaymentAgreement getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public void search() {
		logger.info("RESIDENT CHOOSER CRITERIA "+this.criteria);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try{
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			this.getInstance().setResident(resident);
			this.municipalBondItems = findPendingDueMunicipalBondItems(resident.getId());
			for(MunicipalBondItem mbi : municipalBondItems){
				mbi.calculateTotals(null,null);
			}
			searchAgreements(resident.getId());
			if (resident.getClass().getSimpleName().equalsIgnoreCase("Person")) {
	            Person person = (Person) resident;
	            personIsDead=person.getIsDead();
	        } 
		}
		catch(Exception e){
			e.printStackTrace();
			this.getInstance().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	private List<MunicipalBondItem> findPendingDueMunicipalBondItems(Long residentId){
		if (systemParameterService == null)systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Long  pendingMunicipalBondStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
		
		Query query = getEntityManager().createNamedQuery("MunicipalBond.findOverdueByResidentIdAndTypeAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType",MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusId", pendingMunicipalBondStatusId);
		
		logger.info("PARAMETERS -----> residentId "+residentId);
		logger.info("PARAMETERS -----> municipalBondType "+MunicipalBondType.CREDIT_ORDER.name());
		logger.info("PARAMETERS -----> municipalBondStatusId "+pendingMunicipalBondStatusId);
		List<MunicipalBond> mbs = query.getResultList();
		
		MunicipalBondItem root = new MunicipalBondItem(null);
		try{
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			//@tag recaudacionCoactivas
			incomeService.calculatePayment(mbs, new Date(), true, true);
			
		} catch(Exception e){
			addFacesMessageFromResourceBundle(e.getMessage());
		}
		
		for(MunicipalBond municipalBond : mbs){
			MunicipalBondItem mbi = new MunicipalBondItem(municipalBond);
			
			String entryId = municipalBond.getEntry().getId().toString();
			MunicipalBondItem item = root.findNode(entryId, municipalBond);
			
			String groupingCode = municipalBond.getGroupingCode();
			MunicipalBondItem groupingItem = item.findNode(groupingCode, municipalBond);
			
			groupingItem.add(mbi);
		}
		
		return root.getMunicipalBondItems();
	}

	
	public void calculatePaymentAgreementTotal(){
		paymentAgreementTotal = BigDecimal.ZERO;
		if(municipalBondItems != null){
			for(MunicipalBondItem mbi : municipalBondItems){
				paymentAgreementTotal = paymentAgreementTotal.add(mbi.calculatePaymentTotal());
			}
		}
		this.getInstance().setValue(paymentAgreementTotal);
	}
	
	public BigDecimal getPaymentAgreementTotal(){
		return paymentAgreementTotal;
	}
	
	@SuppressWarnings("unchecked")
	public void searchByCriteria(){
		logger.info("SEARCH BY CRITERIA "+this.criteria);
		if(this.criteria != null && !this.criteria.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public void clearSearchPanel(){
		this.setCriteria(null);
		residents = null;
	}
	
	public List<Resident> getResidents(){
		return residents;
	}
	
	public String getCriteria() {
		return criteria;
	}
	
	public void selectAllItems(){
		for(MunicipalBondItem mbi : municipalBondItems){
			mbi.setIsSelected(allBondsSelected);
		}
	}
	
	private List<MunicipalBond> getSelected(){
		List<MunicipalBond> selectedBonds = new ArrayList<MunicipalBond>();
		for(MunicipalBondItem mbi : municipalBondItems){
			mbi.fillSelected(selectedBonds);
		}
		return selectedBonds;
	}

	
	private List<Long> getSelectedBondIds(){
		List<MunicipalBond> selectedBonds = getSelected();
		List<Long> selectedIds = new ArrayList<Long>();
		for(MunicipalBond mb : selectedBonds){
			selectedIds.add(mb.getId());
		}
		return selectedIds;
	}
	
	@Override
	public String persist() {
		String outcome = null;		
		if(initialDividend == null || initialValidDividend == null) return null;
		if(initialDividend.compareTo(initialValidDividend) == -1){
			if(!this.getInstance().getLowerPercentage()){
			//if(!this.getInstance().getResident().getId().equals(Long.parseLong("161345"))){
				facesMessages.addToControl("savePaymentBtn",org.jboss.seam.international.StatusMessage.Severity.ERROR,
						"#{messages['paymentAgreement.invalidInitialDividend']}");
				return outcome;
			//}
			}
		}
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(new Date());
			this.getInstance().setDescription(super.getMessages().get("paymentAgreement.descriptionTitle")+ " " +strDate);
			
			List<Long> selectedBondIds = getSelectedBondIds();
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			incomeService.save(getInstance(), selectedBondIds);
			outcome = "persisted";
			readyForPrint = Boolean.TRUE;
		} catch (Exception e){
			e.printStackTrace();
		}

		return outcome;
	}
	
	public void changeSelectedItems(){
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

	public void residentSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
		this.municipalBondItems = findPendingDueMunicipalBondItems(resident.getId());
	}
	
	public List<MunicipalBondItem> getMunicipalBondItems() {
		return municipalBondItems;
	}

	public void setMunicipalBondItems(List<MunicipalBondItem> municipalBondItems) {
		this.municipalBondItems = municipalBondItems;
	}

	public Boolean getAllBondsSelected() {
		return allBondsSelected;
	}

	public void setAllBondsSelected(Boolean allBondsSelected) {
		this.allBondsSelected = allBondsSelected;
	}
	
	
	public Boolean getIsSaveButtonDisabled(){
		BigDecimal paymentAgreementValue = instance.getValue(); 
		if(paymentAgreementValue == null || BigDecimal.ZERO.equals(paymentAgreementValue)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private BigDecimal initialValidDividend;
	
	public BigDecimal getInitialValidDividend() {
		return initialValidDividend;
	}

	public void setInitialValidDividend(BigDecimal initialValidDividend) {
		this.initialValidDividend = initialValidDividend;
	}

	public BigDecimal calcultaInitialDividend(){
		initialValidDividend = BigDecimal.ZERO;
		if (systemParameterService == null)systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		BigDecimal  porcent = systemParameterService.findParameter("CREDIT_NOTE_INITIAL_PORCENT");
		if(this.getInstance().getValue() == null)return initialValidDividend;		
		initialValidDividend = this.getInstance().getValue().multiply(porcent).multiply(new BigDecimal(0.01));		
		initialValidDividend = initialValidDividend.setScale(2, BigDecimal.ROUND_HALF_UP);		
		return initialValidDividend;
	}
	
	public void clearValues(){
		instance.getDividends().clear();
		setInitialDividend(calcultaInitialDividend());		
		calculateBalance();
		instance.setDividendsNumber(null);
		instance.setFirstPaymentDate(null);
	}
	
	public void calculateProjectedDividends(){
		MathContext context = new MathContext(3);
		
		Integer dividendsNumber = this.getInstance().getDividendsNumber(); 
		Date firstPaymentDate = this.getInstance().getFirstPaymentDate();
		BigDecimal initialShare = this.getInstance().getInitialDividend();
		
		if(	dividendsNumber != null 
			&& dividendsNumber > 0 
			&& firstPaymentDate != null	
			&& initialShare != null 
			&& initialShare.compareTo(BigDecimal.ZERO) > 0 ){
		
			List<Dividend> projectedDividends = this.getInstance().getDividends();
			projectedDividends.clear();
	
			Calendar calendar = DateUtils.getTruncatedInstance(firstPaymentDate);
			firstPaymentDate = calendar.getTime();
			
			Dividend dividend = createDividend(this.getInstance().getValue(), initialShare, calendar.getTime());		
			projectedDividends.add(0,dividend);
			
			BigDecimal share = this.getInstance().getBalance().divide(new BigDecimal(dividendsNumber), context);
			
			if(dividendsNumber != null && dividendsNumber > 0) {
				for(Integer dividendNumber = 1; dividendNumber <= dividendsNumber ; dividendNumber++){
					calendar.add(Calendar.MONTH, 1);
					BigDecimal balance = dividend.getBalance().subtract(dividend.getAmount());
					if(dividendNumber < dividendsNumber){
						dividend = createDividend(balance, share, calendar.getTime());
					} else {
						dividend = createDividend(balance, balance, calendar.getTime());
					}
					projectedDividends.add(dividendNumber, dividend);
				}
			}
		} else {
			instance.getDividends().clear();
			addFacesMessageFromResourceBundle("paymentAgreement.inputAllRequiredValuesFirst");
		}
	}
	
	private Dividend createDividend(BigDecimal balance, BigDecimal amount, Date date){
		Dividend dividend = new Dividend();
		dividend.setDate(date);
		dividend.setBalance(balance);
		dividend.setAmount(amount);
		return dividend;
	}
	
	public void calculateBalance(){
		PaymentAgreement agreement = this.getInstance();		
		if(agreement.getValue().doubleValue() > initialDividend.doubleValue()){
			if(initialDividend != null){
				BigDecimal balance = agreement.getValue().subtract(initialDividend);
				agreement.setBalance(balance);
				agreement.setInitialDividend(initialDividend);
			}
		} else {
			addFacesMessageFromResourceBundle("paymentAgreement.initialDividendGreaterThanTotalValue");
			agreement.setBalance(null);
		}
		
	}

	public BigDecimal getInitialDividend() {
		return initialDividend;
	}

	public void setInitialDividend(BigDecimal initialDividend) {
		this.initialDividend = initialDividend;
	}

	public void setSystemParameterService(SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}
	
	List<PaymentAgreement> agreemetsExist;
	
	
	public List<PaymentAgreement> getAgreemetsExist() {
		return agreemetsExist;
	}

	public void setAgreemetsExist(List<PaymentAgreement> agreemetsExist) {
		this.agreemetsExist = agreemetsExist;
	}

	public void searchAgreements(Long residentId){
		Query query = getEntityManager().createNamedQuery("PaymentAgreement.findByResidentIdAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("isActive", Boolean.TRUE);

		logger.info("PARAMETERS -----> residentId " + residentId);
		agreemetsExist = query.getResultList();
	}
	
	private boolean personIsDead=Boolean.FALSE;

	public boolean isPersonIsDead() {
		return personIsDead;
	}

	public void setPersonIsDead(boolean personIsDead) {
		this.personIsDead = personIsDead;
	}


	//2016-07-19T11:08
	//@author macartuche
	//@tag recaudacionCoactiva
	@Factory("agreementTypes")
	public List<AgreementType> loadReportTypes() {
		
		List<AgreementType> list = new ArrayList<AgreementType>();
		
		for (AgreementType type : AgreementType.values()) {
			list.add(type);
		}
		return list;
	}
	
	//Jock Samaniego
	//20/09/2016
	//Control para activacion de convenios

	private Boolean toActive = Boolean.FALSE;
	private String[] statesString;
	private List<Long> statesLong = new ArrayList<Long>();

	public void toActivePaymentAgreement() {
		if (!this.instance.getIsActive()) {
			try {
					String qryResult = "SELECT count(*) "
							+ "FROM gimprod.municipalbond mb "
							+ "WHERE mb.paymentagreement_id =:id and mb.municipalbondstatus_id in (:state_id)";
					Query queryResult = this.getEntityManager()
							.createNativeQuery(qryResult);
					queryResult.setParameter("id", this.instance.getId());
					queryResult.setParameter("state_id", statesLong);
					String result = queryResult.getSingleResult().toString();
					if (Integer.parseInt(result) >= 1) {
						toActive = Boolean.TRUE;
					} else {
						toActive = Boolean.FALSE;
					}
			} catch (Exception e) {
				toActive = Boolean.FALSE;
			}

		} else {
			toActive = Boolean.FALSE;
		}
	}

	public void activePaymentAgreement() {
		this.instance.setIsActive(Boolean.TRUE);
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		incomeService.updatePaymentAgreement(this.instance);
		toActive = Boolean.TRUE;
	}

	public Boolean getToActive() {
		return toActive;
	}

	public void setToActive(Boolean toActive) {
		this.toActive = toActive;
	}
	
	public void chargeControlMunicipalBondStates(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		String controlStates = systemParameterService.findParameter("STATES_MUNICIPALBOND_CONTROL_PAYMENTAGREEMENT");
		statesString = controlStates.trim().split(",");
		for(int i=0;i<statesString.length;i++){
			statesLong.add(Long.parseLong(statesString[i]));
		}
	}

	
	//@tag conveniosPagos
	//@date 2016-10-28T11:46
	private List<Payment> paymentsList;
	

	private List<MunicipalBond> municipalBonds;
	

	public List<Payment> getPaymentsList() {
		return paymentsList;
	}

	public void setPaymentsList(List<Payment> paymentsList) {
		this.paymentsList = paymentsList;
	}
	//fin @tag conveniosPagos
	
	//@tag conveniosPagos
	//@author macartuche
	//@date 2016-10-31T08:23
	
	private List<MunicipalBond> municipalbondList;
	
	public void loadMunicipalBondPayments(){
		municipalBonds = findAgreementMunicipalBonds(this.getInstance().getId()); 
	}
	
	
	public boolean checkAvailable(){
		System.out.println("=----->"+this.getInstance().getId());
		return (this.getInstance().getId()==null)? true : false;
	}

	
	@SuppressWarnings("unchecked")
	private List<MunicipalBond> findAgreementMunicipalBonds(Long paymentId) {

		SystemParameterService systemParameterService = ServiceLocator.getInstance()
					.findResource(SystemParameterService.LOCAL_NAME);
		Long inAgreementMunicipalBondStatusId = systemParameterService
					.findParameter(IncomeServiceBean.IN_PAYMENT_AGREEMENT_BOND_STATUS);
		Query query = getEntityManager().createNamedQuery("MunicipalBond.findByPaymentAgreementIdAndStatusId");
		query.setParameter("municipalBondStatusId", inAgreementMunicipalBondStatusId);
		query.setParameter("paymentAgreementId", paymentId);
		return query.getResultList();
	}
	
	public List<MunicipalBond> getMunicipalbondList() {
		return municipalbondList;
	}

	public void setMunicipalbondList(List<MunicipalBond> municipalbondList) {
		this.municipalbondList = municipalbondList;
	}
	

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}
}
