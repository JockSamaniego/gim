package org.gob.gim.revenue.action.bankDebit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.Query;
import javax.transaction.SystemException;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.dto.BondAuxDTO;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.income.service.PaymentLocalService;
import org.gob.gim.revenue.action.bankDebit.pagination.BankDebitDataModel;
import org.gob.gim.revenue.service.BankDebitService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.gob.loja.gim.ws.dto.Payout;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.exception.HasNoObligations;
import org.gob.loja.gim.ws.exception.InvalidPayout;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.NotActiveWorkday;
import org.gob.loja.gim.ws.exception.NotOpenTill;
import org.gob.loja.gim.ws.exception.PayoutNotAllowed;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.framework.EntityHome;
import org.springframework.transaction.annotation.Transactional;

import ec.gob.gim.cadaster.model.Appraisal;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.dto.AffectationFactorDTO;
import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.PaymentMethod;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondForBankDebit;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.StatusChange;
import ec.gob.gim.revenue.model.bankDebit.BankDebit;
import ec.gob.gim.revenue.model.bankDebit.BankDebitForLiquidation;
import ec.gob.gim.revenue.model.bankDebit.criteria.BankDebitSearchCriteria;
import ec.gob.gim.revenue.model.bankDebit.dto.BankDebitReportDTO;
import ec.gob.gim.revenue.model.bankDebit.dto.MunicipalBondForBankDebitDTO;
import ec.gob.gim.revenue.model.bankDebit.dto.MunicipalBondLiquidationReportDTO;
import ec.gob.gim.security.model.User;
import ec.gob.gim.waterservice.model.WaterSupply;


@Name("bankDebitHome")
@Scope(ScopeType.CONVERSATION)
public class BankDebitHome extends EntityHome<MunicipalBondForBankDebit> {

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	@In
	FacesMessages facesMessages;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BankDebitService bankDebitService;

	private ItemCatalogService itemCatalogService;

	private PaymentLocalService paymentService;
	
	private MunicipalBondStatus pendingBondStatus;
	
	private MunicipalBondStatus paidBondStatus;

	/**
	 * criterios
	 * 
	 * @return
	 */

	// @In(create = true,required= false)
	// ImpugnmentDataModel dataModel;

	private BankDebitSearchCriteria criteria = new BankDebitSearchCriteria();

	private BankDebit debitSelected;

	private ItemCatalog typeAccount;

	private List<ItemCatalog> typesAccounts;

	private Integer serviceNumber;
	private WaterSupply waterSupplySelect;
	private String identificacionOwner;
	private String nameOwner;
	private String accountNumber;
	private String accountHolder;
	private Boolean active;

	private Boolean isRegister = Boolean.FALSE;
	private Boolean isEdit = Boolean.FALSE;
	
	private List<BankDebitReportDTO> dataReporte = new ArrayList<BankDebitReportDTO>();
	
	private boolean isFirstTime = true;
	
	@EJB
	private SystemParameterService systemParameterService;

	public BankDebitHome() {
		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		this.criteria = new BankDebitSearchCriteria();
		loadDebits();
	}

	public BankDebitSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(BankDebitSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public BankDebit getDebitSelected() {
		return debitSelected;
	}

	public void setDebitSelected(BankDebit debitSelected) {
		this.debitSelected = debitSelected;
	}

	public ItemCatalog getTypeAccount() {
		return typeAccount;
	}

	public void setTypeAccount(ItemCatalog typeAccount) {
		this.typeAccount = typeAccount;
	}

	public List<ItemCatalog> getTypesAccounts() {
		return typesAccounts;
	}

	public void setTypesAccounts(List<ItemCatalog> typesAccounts) {
		this.typesAccounts = typesAccounts;
	}

	public Integer getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(Integer serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getIdentificacionOwner() {
		return identificacionOwner;
	}

	public void setIdentificacionOwner(String identificacionOwner) {
		this.identificacionOwner = identificacionOwner;
	}

	public String getNameOwner() {
		return nameOwner;
	}

	public void setNameOwner(String nameOwner) {
		this.nameOwner = nameOwner;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public Boolean getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(Boolean isRegister) {
		this.isRegister = isRegister;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public List<BankDebitReportDTO> getDataReporte() {
		return dataReporte;
	}

	public void setDataReporte(List<BankDebitReportDTO> dataReporte) {
		this.dataReporte = dataReporte;
	}

	public void wire() {
		if (isFirstTime) {
			isFirstTime = Boolean.FALSE;
			this.initializeService();
			
			getDataModel().setCriteria(this.criteria);
			getDataModel().setRowCount(getDataModel().getObjectsNumber());
			findPendingLiquidations();
		}
	}

	private BankDebitDataModel getDataModel() {

		BankDebitDataModel dataModel = (BankDebitDataModel) Component
				.getInstance(BankDebitDataModel.class, true);
		return dataModel;
	}

	/*public void findDebits() {

		this.debits = this.bankDebitService.findDebitsForCriteria(criteria);
		System.out.println(this.debits);
	}*/

	public void loadDebits() {
		initializeService();
		this.criteria = new BankDebitSearchCriteria();
		//findDebits();

		typesAccounts = itemCatalogService
				.findItemsForCatalogCode(CatalogConstants.CATALOG_TYPES_BANK_ACCOUNT);

	}

	public void initializeService() {
		if (bankDebitService == null) {
			bankDebitService = ServiceLocator.getInstance().findResource(
					BankDebitService.LOCAL_NAME);
		}
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		
		if (paymentService == null) {
			paymentService = ServiceLocator.getInstance().findResource(
					PaymentLocalService.LOCAL_NAME);
		}
		
		/*
		 * if (systemParameterService == null) { systemParameterService =
		 * ServiceLocator.getInstance().findResource(
		 * SystemParameterService.LOCAL_NAME); }
		 */
	}

	public void searchDebits() {
		//this.debits = this.bankDebitService.findDebitsForCriteria(criteria);
		getDataModel().setCriteria(this.criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
	}

	public void preparaRegisterDebit() {

		this.isRegister = Boolean.TRUE;
		this.isEdit = Boolean.FALSE;

		this.identificacionOwner = null;
		this.nameOwner = null;
		this.waterSupplySelect = null;
		this.serviceNumber = null;
		this.typeAccount = null;
		this.accountNumber = null;
		this.accountHolder = null;
		//System.out.println("llega al preparaRegisterDebit");

	}
	
	public void generateReport() {
		//TODO llamar a servicio para calculo por persona y luego generar reporte con deudas de servicio de agua potable
		try {
			dataReporte = new ArrayList<BankDebitReportDTO>();
			List<Long> residentIds = this.bankDebitService.getBankDebitResidents();
			Boolean result = this.paymentService.calculateDebts(residentIds);
			//System.out.println(result);
			
			//Generar el reporte
			
			dataReporte = this.bankDebitService.getDataReport();
			createBankDebitsToFutureLiquidation();
		} catch (Exception e) {
			//System.out.println("Error al calcular los valores pendientes a pagar");
			e.printStackTrace();
			// return "";
		}
		
	}
	
	
	public void createBankDebitsToFutureLiquidation(){
		joinTransaction();
		try{
			Person user = userSession.getPerson();
				String query = "SELECT itsOK FROM gimprod.sp_mbs_from_bank_debits(?,?,?) ";
				Query q = getEntityManager().createNativeQuery(query);
				q.setParameter(1, user.getId());
				q.setParameter(2, new Date());
				q.setParameter(3, new Date());
				
				@SuppressWarnings("unchecked")
				Boolean itsOK = (Boolean) q.getSingleResult();
				if(itsOK){
					findPendingLiquidations();
					//getEntityManager().flush();
				}
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepareUpdateDebit(Long debitId) {
		this.isRegister = Boolean.FALSE;
		this.isEdit = Boolean.TRUE;
		this.debitSelected = bankDebitService.findById(debitId);
		if (this.debitSelected != null) {
			this.identificacionOwner = this.debitSelected.getWaterSupply()
					.getServiceOwner().getIdentificationNumber();
			this.nameOwner = this.debitSelected.getWaterSupply()
					.getServiceOwner().getName();
			this.waterSupplySelect = this.debitSelected.getWaterSupply();
			this.serviceNumber = this.debitSelected.getWaterSupply()
					.getServiceNumber();
			this.typeAccount = this.debitSelected.getAccountType();
			this.accountNumber = this.debitSelected.getAccountNumber();
			this.accountHolder = this.debitSelected.getAccountHolder();
			this.active = this.debitSelected.getActive();
		} else {
			this.identificacionOwner = null;
			this.nameOwner = null;
			this.waterSupplySelect = null;
			this.serviceNumber = null;
			this.typeAccount = null;
			this.accountNumber = null;
			this.accountHolder = null;
			this.active = null;
		}
	}

	public void searchWaterService() {
		//System.out.println("Llega al searchWaterService");
		if (this.serviceNumber != null) {
			WaterSupply ws = this.bankDebitService
					.findWaterSupplyByServiceNumber(this.serviceNumber);
			if (ws == null) {
				this.identificacionOwner = "";
				this.nameOwner = "";
				addFacesMessageFromResourceBundle(
						"revenue.bankDebit.notFoundWaterService",
						this.serviceNumber);
			} else {
				this.waterSupplySelect = ws;
				this.identificacionOwner = ws.getServiceOwner()
						.getIdentificationNumber();
				this.nameOwner = ws.getServiceOwner().getName();
			}

			/*
			 * MunicipalBond mb = impugnmentService
			 * .findMunicipalBondForImpugnment(this.impugnmentSelected
			 * .getNumberInfringement());
			 * 
			 * if (mb == null) { this.impugnmentSelected.setMunicipalBond(new
			 * MunicipalBond()); this.existObligationForInpugnmentNumber =
			 * Boolean.FALSE; addFacesMessageFromResourceBundle(
			 * "revenue.bankDebit.notFoundWaterService",
			 * this.impugnmentSelected.getNumberInfringement()); } else {
			 * this.existObligationForInpugnmentNumber = Boolean.TRUE;
			 * this.impugnmentSelected.setMunicipalBond(mb); }
			 */
		}
	}

	public void registerDebit() {
		if (this.waterSupplySelect == null || this.identificacionOwner == null
				|| this.nameOwner == null || this.typeAccount == null
				|| this.accountNumber == null || this.accountHolder == null) {
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Campos obligatorios vacios");
		} else {
			this.debitSelected = new BankDebit();
			this.debitSelected.setAccountHolder(accountHolder);
			this.debitSelected.setAccountNumber(accountNumber);
			this.debitSelected.setAccountType(typeAccount);
			this.debitSelected.setWaterSupply(waterSupplySelect);
			this.bankDebitService.save(debitSelected);
			/*getDataModel().setCriteria(this.criteria);
			getDataModel().setRowCount(getDataModel().getObjectsNumber());*/
		}

	}

	public void updateDebit() {
		if (this.waterSupplySelect == null || this.identificacionOwner == null
				|| this.nameOwner == null || this.typeAccount == null
				|| this.accountNumber == null || this.accountHolder == null) {
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Campos obligatorios vacios");
		} else {
			this.debitSelected.setAccountHolder(accountHolder);
			this.debitSelected.setAccountNumber(accountNumber);
			this.debitSelected.setAccountType(typeAccount);
			this.debitSelected.setWaterSupply(waterSupplySelect);
			this.debitSelected.setActive(this.active);
			this.bankDebitService.update(debitSelected);
			/*getDataModel().setCriteria(this.criteria);
			getDataModel().setRowCount(getDataModel().getObjectsNumber());*/
			this.debitSelected = null;
		}

	}
	
	private Boolean hasPendingLiquidations = Boolean.FALSE;
	List<BankDebitForLiquidation> previousForLiquidations = new ArrayList();
	List<BankDebitForLiquidation> bankDebitForLiquidations = new ArrayList();
	List<BankDebitForLiquidation> debitsNotSend = new ArrayList();
	List<BankDebitForLiquidation> debitsSuccessful = new ArrayList();
	List<BankDebitForLiquidation> debitsError = new ArrayList();

	public Boolean getHasPendingLiquidations() {
		return hasPendingLiquidations;
	}

	public void setHasPendingLiquidations(Boolean hasPendingLiquidations) {
		this.hasPendingLiquidations = hasPendingLiquidations;
	}
	
	public List<BankDebitForLiquidation> getBankDebitForLiquidations() {
		return bankDebitForLiquidations;
	}

	public void setBankDebitForLiquidations(
			List<BankDebitForLiquidation> bankDebitForLiquidations) {
		this.bankDebitForLiquidations = bankDebitForLiquidations;
	}
	
	public List<BankDebitForLiquidation> getPreviousForLiquidations() {
		return previousForLiquidations;
	}

	public void setPreviousForLiquidations(
			List<BankDebitForLiquidation> previousForLiquidations) {
		this.previousForLiquidations = previousForLiquidations;
	}
	
	public List<BankDebitForLiquidation> getDebitsNotSend() {
		return debitsNotSend;
	}

	public void setDebitsNotSend(List<BankDebitForLiquidation> debitsNotSend) {
		this.debitsNotSend = debitsNotSend;
	}

	public List<BankDebitForLiquidation> getDebitsSuccessful() {
		return debitsSuccessful;
	}

	public void setDebitsSuccessful(List<BankDebitForLiquidation> debitsSuccessful) {
		this.debitsSuccessful = debitsSuccessful;
	}

	public List<BankDebitForLiquidation> getDebitsError() {
		return debitsError;
	}

	public void setDebitsError(List<BankDebitForLiquidation> debitsError) {
		this.debitsError = debitsError;
	}

	public void findPendingLiquidations(){
		bankDebitForLiquidations = new ArrayList();
		previousForLiquidations = new ArrayList();
		hasPendingLiquidations = Boolean.FALSE;
		Query query = getEntityManager().createNamedQuery(
				"debitForLiquidation.findPending");
		previousForLiquidations = query.getResultList();
		if(previousForLiquidations.size() > 0){
			hasPendingLiquidations = Boolean.TRUE;
		}else{
			hasPendingLiquidations = Boolean.FALSE;
		}
		// checkMunicipalBondDebits();
	}
	
	public void selectAll(){
		bankDebitForLiquidations = new ArrayList();
		debitsNotSend = new ArrayList();
		for(BankDebitForLiquidation bankDebitForLiquidation :previousForLiquidations){
			if(!bankDebitForLiquidation.getHasProblem()){
				bankDebitForLiquidation.setIsSelected(Boolean.TRUE);
				bankDebitForLiquidations.add(bankDebitForLiquidation);
				bankDebitForLiquidation.setObservation("débito seleccionado");
			}else{
				debitsNotSend.add(bankDebitForLiquidation);
			}
		}
	}

	public void deselectAll(){
		bankDebitForLiquidations = new ArrayList();
		debitsNotSend = new ArrayList();
		for(BankDebitForLiquidation bankDebitForLiquidation :previousForLiquidations){
			if(!bankDebitForLiquidation.getHasProblem()){
				bankDebitForLiquidation.setObservation("débito No seleccionado");	
			}
			bankDebitForLiquidation.setIsSelected(Boolean.FALSE);	
			debitsNotSend.add(bankDebitForLiquidation);		
		}
	}
	
	public void selectedCheckBox(BankDebitForLiquidation bankDebitForLiquidation){
		if(!bankDebitForLiquidation.getIsSelected()){
			bankDebitForLiquidation.setIsSelected(Boolean.FALSE);
			if(bankDebitForLiquidations.contains(bankDebitForLiquidation)){
				bankDebitForLiquidations.remove(bankDebitForLiquidation);
			}
			if(!debitsNotSend.contains(bankDebitForLiquidation)){
				debitsNotSend.add(bankDebitForLiquidation);
			}
			bankDebitForLiquidation.setObservation("débito No seleccionado");
		}else{
			bankDebitForLiquidation.setIsSelected(Boolean.TRUE);
			if(!bankDebitForLiquidations.contains(bankDebitForLiquidation)){
				bankDebitForLiquidations.add(bankDebitForLiquidation);
			}
			if(debitsNotSend.contains(bankDebitForLiquidation)){
				debitsNotSend.remove(bankDebitForLiquidation);
			}
			bankDebitForLiquidation.setObservation("débito seleccionado");
		}
	}
	
	public void liquidateDebitsSelected(){
		pendingBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
		paidBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		debitsSuccessful = new ArrayList();
		debitsError = new ArrayList();
		joinTransaction();
		Person user = userSession.getPerson();
		for(BankDebitForLiquidation bankDebitForLiquidation :previousForLiquidations){
			
			if(bankDebitForLiquidations.contains(bankDebitForLiquidation)){
				// System.out.println(bankDebitForLiquidation.getResidentName());
				bankDebitForLiquidation.setIsLiquidated(Boolean.TRUE);
				bankDebitForLiquidation.setLiquidationDate(new Date());
				bankDebitForLiquidation.setLiquidationTime(new Date());
				bankDebitForLiquidation.setLiquidationResponsible(user);
				try{
				registerPaymentForLiquidation(bankDebitForLiquidation);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			bankDebitForLiquidation.setIsActive(Boolean.FALSE);
			getEntityManager().merge(bankDebitForLiquidation);
			
		}
		getEntityManager().flush();
	}
	
	public String viewReportExcel(){
		//createBankDebitsToFutureLiquidation();
		return "/revenue/bankDebits/BankDebitReportExcel.xhtml";
	}
	
	
	public void registerPaymentForLiquidation(BankDebitForLiquidation bankDebitForLiquidation)
			throws InvalidPayout, PayoutNotAllowed, TaxpayerNotFound,
			InvalidUser, NotActiveWorkday, NotOpenTill, HasNoObligations{
		List<Long> bondsIds = new ArrayList();
		for(MunicipalBondForBankDebit mb: bankDebitForLiquidation.getMbsForBankDebit()){
			bondsIds.add(mb.getMunicipalBondId());
		}
		Long tillId = userSession.getTillPermission().getTill().getId();
		 // Till till = findTill(bankDebitForLiquidation.getLiquidationResponsible().getId());
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		try {
			org.jboss.seam.transaction.Transaction.instance().setTransactionTimeout(1800);
			incomeService.saveForBankLiquidation(new Date(),
					bondsIds, bankDebitForLiquidation.getLiquidationResponsible(), tillId, null, PaymentMethod.NORMAL.name());
			bankDebitForLiquidation.setObservation("débito liquidado");
			debitsSuccessful.add(bankDebitForLiquidation);
			for(Long mbId : bondsIds){
				MunicipalBond mbStatus = getEntityManager().find(MunicipalBond.class, mbId);
				saveStatusChangeRecord("Pago de débito bancario en bloque", mbStatus, pendingBondStatus, paidBondStatus, userSession.getUser());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("========== no pagadoooo");
			bankDebitForLiquidation.setHasProblem(Boolean.TRUE);
			bankDebitForLiquidation.setIsLiquidated(Boolean.FALSE);
			bankDebitForLiquidation.setObservation("Error en la transacción de pago");
			debitsError.add(bankDebitForLiquidation);
			throw new InvalidPayout();
		}
		getEntityManager().flush();
	}
		
	public Boolean hasRole(String roleKey) {
		String role = systemParameterService.findParameter(roleKey);
		if (role != null) {
			return userSession.getUser().hasRole(role);
		}
		return false;
	}
	
	public void checkMunicipalBondDebits() {
		findPendingLiquidations();
		debitsNotSend = new ArrayList();
		List<Long> residentIds = new ArrayList();			
		for(BankDebitForLiquidation bdl : previousForLiquidations){
			residentIds.add(bdl.getResidentId());
		}
		try {
			org.jboss.seam.transaction.Transaction.instance().setTransactionTimeout(1800);
			Boolean result = this.paymentService.calculateDebts(residentIds);
			for(BankDebitForLiquidation bankDebitForLiquidation :previousForLiquidations){
				for(MunicipalBondForBankDebit mb : bankDebitForLiquidation.getMbsForBankDebit()){
					MunicipalBond municipalBond = getEntityManager().find(MunicipalBond.class, mb.getMunicipalBondId());
					if(checkPendingStatus(municipalBond)){
						bankDebitForLiquidation.setHasProblem(Boolean.TRUE);
						bankDebitForLiquidation.setObservation("Obligación no está pendiente de pago");
						debitsNotSend.add(bankDebitForLiquidation);
						break;
					}
					if(checkComparateValues(municipalBond, mb)){
						bankDebitForLiquidation.setHasProblem(Boolean.TRUE);
						bankDebitForLiquidation.setObservation("Los valores no coinciden");
						debitsNotSend.add(bankDebitForLiquidation);
						break;
					}
					
				}		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		deselectAll();
	}
	
	public Boolean checkPendingStatus(MunicipalBond mb){
		if(mb.getMunicipalBondStatus().getId() == 3){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public Boolean checkComparateValues(MunicipalBond municipalBond, MunicipalBondForBankDebit mb){
		if(municipalBond.getPaidTotal().compareTo(mb.getPaidTotal()) == 0){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	private void saveStatusChangeRecord(String explanation, MunicipalBond bond, MunicipalBondStatus previousStatus,
			MunicipalBondStatus status, User user) {
		StatusChange statusChange = new StatusChange();
		statusChange.setExplanation(explanation);
		statusChange.setMunicipalBond(bond);
		statusChange.setPreviousBondStatus(previousStatus);
		statusChange.setMunicipalBondStatus(status);
		statusChange.setUser(user);
		getEntityManager().persist(statusChange);
	}
}