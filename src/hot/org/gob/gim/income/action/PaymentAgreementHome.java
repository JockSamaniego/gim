package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.income.view.AgreementSummaryDTO;
import org.gob.gim.income.view.MunicipalBondItem;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Alert;
import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.Dividend;
import ec.gob.gim.income.model.Payment;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
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
	BigInteger totalPayments  = BigInteger.ZERO;
	Boolean enterTOCalculate = Boolean.TRUE;
	
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

	public BigInteger getTotalPayments() {
		return totalPayments;
	}

	public void setTotalPayments(BigInteger totalPayments) {
		this.totalPayments = totalPayments;
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
	
//	@In(create=true)
//	GimService gimService;

	public void wire() {
		getInstance();
		if(getInstance().getId() != null)
			identificationNumber = getInstance().getResident().getIdentificationNumber(); 
		chargeControlMunicipalBondStates();
		//toActivePaymentAgreement();		
		
		if(getInstance().getId() != null && enterTOCalculate) {
			//@author macartuche
			//valor pagado
			calculateCanceledBonds();
			calculateBalanceForPay();
			calculatePayedValue();
			loadTotalDeposit();
			loadAgreementSummary();
		}
		
		// System.out.println("en el wire---------------------->>>>>");
	}
	 
	//@author macartuche
	//valor pagado y saldos
	@In(create=true)
	PaymentHome paymentHome;
	public void calculatePayedValue(){		
		if(getInstance().getId()!=null){
			 this.getInstance().getId();
 			 
			 Query q = getEntityManager().createNativeQuery("Select coalesce(SUM(d.value),0) "
			 			+ "from Deposit d join municipalbond m on m.id = d.municipalbond_id "
			 			+ "where m.paymentagreement_id=:id and d.status='VALID' "
			 			+ "and m.municipalbondstatus_id <> :canceled");
			 
			 q.setParameter("id", this.getInstance().getId());
			 q.setParameter("canceled", new Long(9));
			 
			 this.payed = (BigDecimal)q.getSingleResult();	
 
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
			Query q = getEntityManager().createNativeQuery("select coalesce(sum(m.value),0) from municipalbond m "
					+ "where m.paymentagreement_id=:agreement and m.municipalbondstatus_id=:canceled");
			q.setParameter("canceled", 9L); //anulados o dados de baja
			q.setParameter("agreement", getInstance().getId());
			this.canceledValue = (BigDecimal)q.getSingleResult();	 
		}
	}
		
	
	@SuppressWarnings("unchecked")
	public void loadTotalDeposit() {
		
		if(getInstance().getId() !=null ) {
			Query q = getEntityManager().createNativeQuery("select count(distinct(p.id)) "
					+ "from payment  p join deposit d on d.payment_id= p.id  "
					+ "join municipalbond m on m.id= d.municipalbond_id "
					+ "where m.paymentagreement_id=:id and p.status='VALID' and d.status='VALID' ");
			q.setParameter("id", getInstance().getId());
			
			List<Object> data = q.getResultList();
			if(data.size()>0) {
				totalPayments = (BigInteger)data.get(0);
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
			enterTOCalculate = Boolean.FALSE;
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
				mbi.calculateTotals(null,null,null);
			}
			searchAgreements(resident.getId());
			if (resident.getClass().getSimpleName().equalsIgnoreCase("Person")) {
	            Person person = (Person) resident;
	            personIsDead=person.getIsDead();
	        } 
			this.findResidentAlertsActive(resident);
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
			
			//rfam 2021-12-07
			findEntryDetail(selectedBondIds);
			
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
	private String activeMessage;
	private Boolean isFirstTime = Boolean.TRUE;

	public void toActivePaymentAgreement() {
		//this.setInstance(payAgree);
		getInstance();
		chargeControlMunicipalBondStates();
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
						this.activePaymentAgreement();
						toActive = Boolean.TRUE;
						activeMessage = "Info: El convenio se activo correctamente";
					} else {
						toActive = Boolean.FALSE;
						activeMessage = "Error: El convenio no tiene obligaciones pendientes";
					}
			} catch (Exception e) {
				toActive = Boolean.FALSE;
				activeMessage = "Error: No se pudo activar el convenio";
			}

		} else {
			toActive = Boolean.FALSE;
			activeMessage = "Error: El convenio ya se encuentra activado";
		}
	}

	public void activePaymentAgreement() {
		this.instance.setIsActive(Boolean.TRUE);
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		incomeService.updatePaymentAgreement(this.instance);
		toActive = Boolean.TRUE;
	}
	
	public void callWire(){
		if(isFirstTime){
			wire();
			isFirstTime = Boolean.FALSE;
		}
	}

	public Boolean getToActive() {
		return toActive;
	}

	public void setToActive(Boolean toActive) {
		this.toActive = toActive;
	}
	
	public String getActiveMessage() {
		return activeMessage;
	}

	public void setActiveMessage(String activeMessage) {
		this.activeMessage = activeMessage;
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
		//System.out.println("=----->"+this.getInstance().getId());
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
	
	public Boolean hasRole(String roleKey) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		String role = systemParameterService.findParameter(roleKey);
		if (role != null) {
			return userSession.getUser().hasRole(role);
		}
		return false;
	}
	
	/**
	 * @return 
	 * @macartuche
	 * REMISION activar para pago completo
	 */
	
	public void updatePA() { 
		try{
			Long id = getPaymentAgreementId();
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			PaymentAgreement paBD = getEntityManager().find(PaymentAgreement.class, id);
			paBD.setIsFullPayment(!paBD.getIsFullPayment());
			paBD.setUserUpdate(userSession.getUser().getId());		
			incomeService.update(paBD); 
		}catch (Exception e){
			e.printStackTrace();
		} 
	}
	
	public Long idPayment ;
	public Boolean remissionActive=Boolean.FALSE;
	public String messageRemission="";
	
	public boolean isfullPayment(Long paymentAgreementId) { 
		PaymentAgreement paBD = getEntityManager().find(PaymentAgreement.class, paymentAgreementId);
		return !paBD.getIsFullPayment(); 		
	}
	
	
	public void verifyForRemission() {
		if(this.getInstance()!=null) {			
			Boolean applyReferral = this.getInstance().getAgreementType().equals(AgreementType.REMISSION)? true: false;
			this.getInstance().setApplyReferral(applyReferral);
		}
	}

	public Long getIdPayment() {
		return idPayment;
	}

	public void setIdPayment(Long idPayment) {
		this.idPayment = idPayment;
	}

	public Boolean getRemissionActive() {
		return remissionActive;
	}

	public void setRemissionActive(Boolean remissionActive) {
		this.remissionActive = remissionActive;
	}

	public String getMessageRemission() {
		return messageRemission;
	}

	public void setMessageRemission(String messageRemission) {
		this.messageRemission = messageRemission;
	}
	
	//Para controlar la creacion de convenios a contribuyentes con alertas
	//Jock Samaniego
	//06-01-2020
	private Boolean residentHasAlerts = Boolean.FALSE;
	
	public Boolean getResidentHasAlerts() {
		return residentHasAlerts;
	}

	public void findResidentAlertsActive(Resident resident){
		List<Alert> alerts = new ArrayList<Alert>();
		Query query = getEntityManager().createNamedQuery("Alert.findPendingAlertsByResidentIdNum");
		query.setParameter("residentIdNum", resident.getIdentificationNumber());
		alerts = query.getResultList();
		if(alerts.size() > 0){
			residentHasAlerts = Boolean.TRUE;
		}else{
			residentHasAlerts = Boolean.FALSE;
		}
	}
	
	// para anular un convenio si el contribuyente no cumple con el primer pago
	// Jock Samaniego
	
	private String nullifiedMessage = "";
	
	public String getNullifiedMessage() {
		return nullifiedMessage;
	}

	public void setNullifiedMessage(String nullifiedMessage) {
		this.nullifiedMessage = nullifiedMessage;
	}

	public Boolean nullifiedControl(PaymentAgreement pa) throws ParseException{
		if(pa.getIsNullified() != null){
			if(pa.getIsNullified()){
				return Boolean.TRUE;
			}
		}	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateFormat = simpleDateFormat.format(new Date());
		Date currentDate = simpleDateFormat.parse(currentDateFormat);
		String paDateFormat = simpleDateFormat.format(pa.getFirstPaymentDate());
		Date paDate = simpleDateFormat.parse(paDateFormat);
		if (currentDate.after(paDate) || !pa.getIsActive()){
			return Boolean.TRUE;
		}	
		return Boolean.FALSE;
	}
	
		
	public void nullifiedPaymentAgreement(){
		try{
			this.nullifiedMessage = "El convenio ha sido anulado éxitosamente.";
			Boolean isAvailableNullified = Boolean.TRUE;
			Long id = getPaymentAgreementId();
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			PaymentAgreement paBD = getEntityManager().find(PaymentAgreement.class, id);
			for(MunicipalBond mb : paBD.getMunicipalBonds()){
				if (mb.getDeposits() != null && mb.getDeposits().size() > 0) {
					for(Deposit dp : mb.getDeposits()){
						if(dp.getValue().compareTo(BigDecimal.ZERO) > 0){
							this.nullifiedMessage = "No se puede anular, el convenio tiene pagos realizados.";
							isAvailableNullified = Boolean.FALSE;
						}
					}
				}
			}
			if(isAvailableNullified){
				paBD.setIsNullified(Boolean.TRUE);
				paBD.setIsActive(Boolean.FALSE);
				paBD.setNullifiedDate(new Date());
				paBD.setUserNullified(userSession.getUser().getId());
				for(MunicipalBond mb : paBD.getMunicipalBonds()){
					mb.setPaymentAgreement(null);
					mb.setMunicipalBondStatus(getEntityManager().find(MunicipalBondStatus.class, new Long(3)));
					getEntityManager().persist(mb);
				}
				incomeService.update(paBD); 
			}			
		}catch (Exception e){
			e.printStackTrace();
		} 
	}
	
	// Para cargar depositos realizados en la vista de cuotas establecidas
	// Jock Samaniego
	
	public void chargeDepositsByPaymentAgreement(PaymentAgreement payment){
		if(payment != null){
			this.setInstance(payment);
			loadDeposits();
			chargeValuesOfPaymentAgreement();
		}
	}
	
	private int pendingQuotas;
	private int expiredQuotas;
	private int paidQuotas;

	public int getPendingQuotas() {
		return pendingQuotas;
	}

	public void setPendingQuotas(int pendingQuotas) {
		this.pendingQuotas = pendingQuotas;
	}

	public int getExpiredQuotas() {
		return expiredQuotas;
	}

	public void setExpiredQuotas(int expiredQuotas) {
		this.expiredQuotas = expiredQuotas;
	}

	public int getPaidQuotas() {
		return paidQuotas;
	}

	public void setPaidQuotas(int paidQuotas) {
		this.paidQuotas = paidQuotas;
	}
	

	public void chargeValuesOfPaymentAgreement(){
		BigDecimal totalDeposit = BigDecimal.ZERO;
		BigDecimal totalQuotasValue = BigDecimal.ZERO;
		paidQuotas = 0;
		pendingQuotas = 0;
		expiredQuotas = 0;
		for (Payment pay : payments){
			totalDeposit = totalDeposit.add(pay.getValue());
		}
		for (Dividend div : this.instance.getDividends()){
			totalQuotasValue = totalQuotasValue.add(div.getAmount());
			if(totalQuotasValue.compareTo(totalDeposit)<=0){
				paidQuotas++;
			}else{
				if(div.getDate().compareTo(new Date()) < 0){
					expiredQuotas++;
				}else{
					pendingQuotas++;
				}
			}
		}
	}
	
	// @author rfam
	// @tag resumen de convenio
	// @date 2021-12-07
	private List<AgreementSummaryDTO> summaries;

	public void findEntryDetail(List<Long> selectedBondId) {
		String sql = "select " + "	e.code || ' - '|| e.name rubro, "
				+ "	min(mb.expirationdate) f_min, "
				+ "	max(mb.expirationdate) f_max, " + "	count(mb) cantidad, "
				+ "	sum(mb.value) sum_val, " + "	sum(mb.paidtotal) sum_total "
				+ "from municipalbond mb "
				+ "join paymentagreement pa on mb.paymentagreement_id = pa.id "
				+ "join entry e on mb.entry_id = e.id " +
				// "where pa.id = :agreementId "+
				" where mb.id in (:bondIds) " + "group by 1 " + "order by 1";
		List<Alert> alerts = new ArrayList<Alert>();
		Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("bondIds", selectedBondId);
		alerts = query.getResultList();

		this.summaries = NativeQueryResultsMapper.map(query.getResultList(),
				AgreementSummaryDTO.class);

	}

	public List<AgreementSummaryDTO> getSummaries() {
		return summaries;
	}

	public void setSummaries(List<AgreementSummaryDTO> summaries) {
		this.summaries = summaries;
	}
	
	public void loadAgreementSummary(){
		List<Long> selectedIds = new ArrayList<Long>();
		for(MunicipalBond mb : this.getInstance().getMunicipalBonds()){
			selectedIds.add(mb.getId());
		}
		findEntryDetail(selectedIds);
	}
	
}
