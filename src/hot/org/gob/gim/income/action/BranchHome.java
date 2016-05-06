package org.gob.gim.income.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.ReceiptType;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.income.model.Till;

@Name("branchHome")
public class BranchHome extends EntityHome<Branch> {
	
	private static final long serialVersionUID = 1L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private SystemParameterService systemParameterService;
	
	private List<Person> cashiers;
	
	public List<Person> getCashiers() {
		return cashiers;
	}

	public void setCashiers(List<Person> cashiers) {
		this.cashiers = cashiers;
	}

	@In
	FacesMessages facesMessages;

	public void setBranchId(Long id) {
		setId(id);
	}

	public Long getBranchId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {		
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Branch getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public void addTill(){
		this.getInstance().add(new Till());
	}
	
	public void removeTill(Till t){
		this.getInstance().remove(t);
	}
	
	public String save(){
		
		if(existsDuplicateUsers()){
			//String message = Interpolator.instance().interpolate("#{messages['till.duplicateUsers']}", new Object[0]);
			//facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			addFacesMessageFromResourceBundle("till.duplicateUsers");
			return "failed";
		}
		
		if(existsDuplicateTillsNumber()){
			//String message = Interpolator.instance().interpolate("#{messages['till.duplicateNumbers']}", new Object[0]);
			//facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			addFacesMessageFromResourceBundle("till.duplicateNumbers");
			return "failed";
		}
		
		Branch b = getBranchByNumber();
		if( b != null && b.getId() != this.getInstance().getId() ){
			addFacesMessageFromResourceBundle("branch.duplicateNumber");
			return "failed";
		}
		
		String returnValue;
		if(isManaged()){
			returnValue = super.update();
		}else{
			returnValue = super.persist();
		}		
		createSequences();
		return returnValue;
	}
	
	public String findAdministratorRole(){
		systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		return systemParameterService.findParameter("ROLE_NAME_ADMINISTRATOR");
	}
	
	public boolean existsDuplicateUsers(){
		boolean aux = false;
		String rootRoleName = findAdministratorRole();
		for(int i = 0; i < this.getInstance().getTills().size(); i++){
			for(int j = i+1; j < this.getInstance().getTills().size(); j++){
				if(!this.getInstance().getTills().get(i).getPerson().getUser().hasRole(rootRoleName) && this.getInstance().getTills().get(i).getPerson().equals(this.getInstance().getTills().get(j).getPerson())){
					return true;
				}
			}
		}
		return aux;
	}
	
	
	
	public boolean existsDuplicateTillsNumber(){
		boolean aux = false;
		for(int i = 0; i < this.getInstance().getTills().size(); i++){
			for(int j = i+1; j < this.getInstance().getTills().size(); j++){
				if(this.getInstance().getTills().get(i).getNumber() == this.getInstance().getTills().get(j).getNumber()){
					return true;
				}
			}
		}
		return aux;
	}

	public List<Till> getTills() {		
		return getInstance() == null ? null : new ArrayList<Till>(getInstance()
				.getTills());
	}
	
	@SuppressWarnings("unchecked")
	public Branch getBranchByNumber(){		
		Query query = getPersistenceContext().createNamedQuery("Branch.findByNumber").setParameter("number", this.getInstance().getNumber());
		List<Branch> list = query.getResultList();		
		if(list != null && list.size() >0 ){
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> findCashiers(){		
		if(cashiers == null){			
			if(systemParameterService == null){
				systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			}
			String role_name = systemParameterService.findParameter("ROLE_NAME_CASHIER");			
			Query query = getPersistenceContext().createNamedQuery("Person.findByRoleName").setParameter("roleName", role_name);
			cashiers = query.getResultList();			
		}
		return cashiers != null ? cashiers : new ArrayList<Person>();
	}
		
	@SuppressWarnings("unchecked")
	public void createSequences(){
		Query query = getEntityManager().createNamedQuery("TaxpayerRecord.findAll");
		List<TaxpayerRecord> taxpayerRecords = query.getResultList();
		
		query = getEntityManager().createNamedQuery("ReceiptType.findAll");
		List<ReceiptType> receiptTypes = query.getResultList();
		
		for(TaxpayerRecord taxpayerRecord : taxpayerRecords){
			Long taxpayerRecordId = taxpayerRecord.getId(); 
			for(ReceiptType receiptType : receiptTypes){
				for(Till till : getInstance().getTills()){
					IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);						
					incomeService.createSequence(taxpayerRecordId , receiptType.getId(), till.getBranch().getNumber(), till.getNumber());
					System.out.println("LXGK-SRI -----> Sequence created ");
				}
			}
		}
	}
}
