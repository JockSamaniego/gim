package org.gob.gim.income.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.common.model.SystemParameter;
import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.ReceiptAuthorization;

@Scope(ScopeType.CONVERSATION)
@Name("receiptPrintingManager")
public class ReceiptPrintingManager{
	
	@In
	EntityManager entityManager;
	
//	private Long[] printings  = {0L, -1L};
	private Long[] printings  = {0L};
	
	
	
	
	public ReceiptPrintingManager() { 
	}

	private List<Deposit> depositsToPrint = new LinkedList<Deposit>();
	
	Map<Integer, Branch> branches;
	
	public Boolean isCertificate;
		
	public Boolean getIsCertificate() {
		return isCertificate;
	}

	public void setIsCertificate(Boolean isCertificate) {
		this.isCertificate = isCertificate;
	}

	@SuppressWarnings("unchecked")
	public Long[] getPrintings(){
		//System.out.println("INGRESO AQUIIIIIIIIIII ");
		Query q = entityManager.createNamedQuery("SystemParameter.findByName");
		q.setParameter("name", "PRINT_DOUBLE_PAPER");
		List<SystemParameter> list= q.getResultList();
		return (list.isEmpty())? printings : generateOriginalOrCopy(list.get(0).getValue());
	}
	
	/**
	 * @author macartuche
	 * @param value
	 * @return
	 */
	public Long[] generateOriginalOrCopy(String value){
		return (value.equals("false"))? new Long[]{0L} : new Long[]{0L, -1L};
	}
	
	public void setPrintings(Long[] printings){
		this.printings = printings;
	}
	
	public List<Deposit> getDepositsToPrint() {
		return depositsToPrint;
	}
	
	// JockSamaniego
	// Para control de impresiones de titulos (ORIGINAL-COPIA-CANJE)
	// 13-03-2020
	
	private int copiesNumber = 0;
	private Boolean isExchange = Boolean.FALSE;
	
	public int getCopiesNumber() {
		return copiesNumber;
	}

	public Boolean getIsExchange() {
		return isExchange;
	}
	
	public void controlReprintsCreditTitles(int copiesNumber, Boolean isExchange){
		this.copiesNumber = copiesNumber;
		this.isExchange = isExchange;
	}
	
	public String print(Deposit deposit){
		depositsToPrint.clear();
		//System.out.println("A IMPRIMIR DEPOSITO "+deposit);
		depositsToPrint.add(deposit);
		return "sentToPrint";
	}
	
	public String print(List<Deposit> deposits){
		depositsToPrint.clear();
		depositsToPrint.addAll(deposits);
		//System.out.println("A IMPRIMIR DEPOSITOS "+depositsToPrint.size());
		return "sentToPrint";
	}
	
	public Boolean getIsReceiptGenerationEnabled(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		return systemParameterService.findParameter("ENABLE_RECEIPT_GENERATION");
	}
	
	public Boolean showShield(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		return systemParameterService.findParameter("SHOW_SHIELD");
	}
	
	public Boolean getIsTaxable(Deposit deposit){
		return deposit.getMunicipalBond().getEntry().getIsTaxable();
	}
	
	public Branch getBranch(Integer branchNumber){
		loadBranches();
		return branches.get(branchNumber);
	}
	
	@SuppressWarnings("unchecked")
	public void loadBranches(){
		if(branches == null){
			branches = new HashMap<Integer, Branch>();
			Query query = entityManager.createNamedQuery("Branch.findAll");
			List<Branch> branchesList =  query.getResultList();
			for(Branch branch : branchesList){
				branches.put(branch.getNumber(), branch);
			}
		}
	}
	
	// TODO Esto se esta llamando varias veces desde el reporte
	public ReceiptAuthorization getReceiptAuthorization(String number){
		//System.out.println("Recovering ReceiptAuthorization NUMBER -----> "+number);
		Query query = entityManager.createNamedQuery("ReceiptAuthorization.findByNumber");
		query.setParameter("number", number);
		return (ReceiptAuthorization) query.getSingleResult();
	}

	public ReceiptAuthorization getReceiptAuthorization(Long id){
		//System.out.println("Recovering ReceiptAuthorization ID -----> "+id);
		Query query = entityManager.createNamedQuery("ReceiptAuthorization.findById");
		query.setParameter("id", id);
		return (ReceiptAuthorization) query.getSingleResult();
	}

}
