package org.gob.gim.income.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.dto.BondAuxDTO;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.income.model.PaymentMethod;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.security.model.MunicipalbondAux;

@Name("paymentAgreementProcessHome")
@Scope(ScopeType.CONVERSATION)
public class PaymentAgreementProcessHome extends EntityHome<MunicipalbondAux> implements Serializable {

	private static final long serialVersionUID = 1L;

	@In
	FacesMessages facesMessages;

	@Logger
	Log logger;

	private Resident resident;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private PaymentAgreement paymentAgreementSelected;

	private SystemParameterService systemParameterService;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private String criteria;

	private String identificationNumber;

	private List<Resident> residents;
	
	private List<PaymentAgreement> paymentAgreements;
	
	private List<MunicipalbondAux> auxList;
	
	private List<MunicipalbondAux> bdList;
	
	private Boolean changeType = false;
	private Boolean changeLowerPercentage=false;
	
	public void initialize(){
		
	}

	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		logger.info("SEARCH BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public void residentSelectedListener(ActionEvent event) throws Exception {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());

		
	}
	
	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
		this.changeLowerPercentage=false;
		this.changeType=false;
	}
	
	public void search() {
		logger.info("RESIDENT identificationNumber " + this.identificationNumber);
		if (this.identificationNumber == null || this.identificationNumber.length() == 0) {
			addFacesMessageFromResourceBundle("resident.enterValidIdentifier");
			return;
		}

		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setResident(resident);
			
			findPendingPaymentsAgreements();
		} catch (Exception e) {
			// e.printStackTrace();
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void setValues(){
		if(this.paymentAgreementSelected!=null) {
			this.changeType = (this.paymentAgreementSelected.getAgreementType().toString().equals("COERCIVEJUDGEMENT"))? true:false;
			this.changeLowerPercentage = (this.paymentAgreementSelected.getLowerPercentage()!=null && this.paymentAgreementSelected.getLowerPercentage())? true: false;
			
		}
	}
	public void callSP(){
		if(this.paymentAgreementSelected != null){
			String query = "SELECT * FROM sp_paymentagreement_lower_percentege( "+this.paymentAgreementSelected.getId()+") ";
			System.out.println(query);
			Query q = getEntityManager().createNativeQuery(query);
//			q.setParameter(0, this.paymentAgreementSelected.getId());
			
			@SuppressWarnings("unchecked")
			List<BondAuxDTO> list = NativeQueryResultsMapper.map(q.getResultList(), BondAuxDTO.class);
			this.auxList = new ArrayList<MunicipalbondAux>(); 
			MunicipalBond bond = null;
			
			//cambiar el tipo 
			
			for (BondAuxDTO bondAuxDTO : list) {
				
				MunicipalbondAux mba = new MunicipalbondAux();
				mba.setLiquidationDate(bondAuxDTO.getLiquidationDate());
				mba.setLiquidationTime(bondAuxDTO.getLiquidationTime());
				mba.setPayValue(bondAuxDTO.getPayvalue());
				mba.setStatus(bondAuxDTO.getStatus());
				bond = getMunicipalBond(bondAuxDTO.getMunicipalbondid());
				mba.setMunicipalbond(bond);
				mba.setDeposit(getDeposit(bondAuxDTO.getDepositid()));
				mba.setType(bondAuxDTO.getType());
				
				//@author macartuche
				//@date 2018-05-04
				//abonos
				mba.setTypepayment(PaymentMethod.AGREEMENT.name());
				//fin abonos
				
				this.auxList.add(mba);
			}
			
			//obtener los registros para el bond de todos los tipos
			query = "Select mba from MunicipalbondAux mba where mba.municipalbond=:municipalbond "
					+ " and  mba.status=:status order by mba.liquidationDate desc";
			Query q1 = getEntityManager().createQuery(query);
			q1.setParameter("municipalbond", bond);
			q1.setParameter("status", "VALID");
			this.bdList = q1.getResultList();
			 
			
		}
	}
	
	//@Transactional
	public void saveBondsAux(){
		
		joinTransaction();
		if(this.paymentAgreementSelected != null){
			if(this.changeType) {
				this.paymentAgreementSelected.setAgreementType(AgreementType.COERCIVEJUDGEMENT);
				getEntityManager().merge(this.paymentAgreementSelected);
				getEntityManager().flush();
			}
			
			if(this.changeLowerPercentage) {
				this.paymentAgreementSelected.setLowerPercentage(Boolean.TRUE);
				getEntityManager().merge(this.paymentAgreementSelected);
				getEntityManager().flush();
			}
		}
		
		//actualizar valores existentes en la bd

		for (MunicipalbondAux municipalbondAux : this.bdList) {
			System.out.println(municipalbondAux.getId()+" | "+municipalbondAux.getBalance()+" | "+
								municipalbondAux.getCoveritem()+" | "+
								municipalbondAux.getLiquidationDate()+" | "+ municipalbondAux.getLiquidationTime()+" | "+
								municipalbondAux.getPayValue()+" | "+municipalbondAux.getDeposit().getId()+" | "+
								municipalbondAux.getMunicipalbond().getId()+" | "+municipalbondAux.getAnotherItem());
			getEntityManager().merge(municipalbondAux);
			getEntityManager().flush();
		}
		
		//System.out.println("----------------------------------");
		//insertar valores nuevos
		for (MunicipalbondAux municipalbondAux : auxList) {
			System.out.println(municipalbondAux.getId()+" | "+municipalbondAux.getBalance()+" | "+
					municipalbondAux.getCoveritem()+" | "+
					municipalbondAux.getLiquidationDate()+" | "+ municipalbondAux.getLiquidationTime()+" | "+
					municipalbondAux.getPayValue()+" | "+municipalbondAux.getDeposit().getId()+" | "+
					municipalbondAux.getMunicipalbond().getId()+" | "+municipalbondAux.getAnotherItem());
			getEntityManager().persist(municipalbondAux);
			getEntityManager().flush();
	 
		}
		

		//clearSearchPanel();
	}
	
	@SuppressWarnings("unused")
	private void equalsRowsByType(MunicipalBond municipalbond, String type){
		String query = "Select mba from MunicipalbondAux mba where mba.municipalbond=:municipalbond "
				+ "where mba.status=:status and mba.type:=type order by mba.liquidationDate";
		Query q = getEntityManager().createQuery(query);
		q.setParameter("municipalbond", municipalbond);
		q.setParameter("status", "VALID");
		q.setParameter("type", type);
		
		@SuppressWarnings("unchecked")
		List<MunicipalbondAux> list = q.getResultList();
		for (MunicipalbondAux bondAux: list) {
			
			if(!bondAux.getCoveritem())
				continue;

			
			bondAux.setAnotherItem(true);
			getEntityManager().merge(bondAux);
				
			//actualizar anotherItem=true al resto de bondsaux anteriores al bondaux actual
			String updateSQL ="Update municipalbondaux set anotheritem=true "
					+ "	where municipalbond_id="+municipalbond.getId()+" "
					+ " and id < "+bondAux.getId();
					
			
		}
	}
	
	private MunicipalBond getMunicipalBond(Long id){
		return getEntityManager().find(MunicipalBond.class, id);
	}
	
	private Deposit getDeposit(Long id){
		return getEntityManager().find(Deposit.class, id);
	}
	
	private void findPendingPaymentsAgreements(){
		if(this.resident!=null){
			Query q = getEntityManager().createQuery("Select pa from PaymentAgreement pa "
					+ "where pa.resident=:resident and pa.isActive=:isactive ");
			q.setParameter("resident", this.resident);
			q.setParameter("isactive", true);
			this.paymentAgreements = (List<PaymentAgreement>)q.getResultList();
		}
	}
	

	public List<PaymentAgreement> getPaymentAgreements() {
		return paymentAgreements;
	}

	public void setPaymentAgreements(List<PaymentAgreement> paymentAgreements) {
		this.paymentAgreements = paymentAgreements;
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

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public PaymentAgreement getPaymentAgreementSelected() {
		return paymentAgreementSelected;
	}

	public void setPaymentAgreementSelected(PaymentAgreement paymentAgreementSelected) {
		this.paymentAgreementSelected = paymentAgreementSelected;
	}

	public List<MunicipalbondAux> getAuxList() {
		return auxList;
	}

	public void setAuxList(List<MunicipalbondAux> auxList) {
		this.auxList = auxList;
	}

	public List<MunicipalbondAux> getBdList() {
		return bdList;
	}

	public void setBdList(List<MunicipalbondAux> bdList) {
		this.bdList = bdList;
	}

	public Boolean getChangeType() {
		return changeType;
	}

	public void setChangeType(Boolean changeType) {
		this.changeType = changeType;
	}

	public Boolean getChangeLowerPercentage() {
		return changeLowerPercentage;
	}

	public void setChangeLowerPercentage(Boolean changeLowerPercentage) {
		this.changeLowerPercentage = changeLowerPercentage;
	}
	
	
}