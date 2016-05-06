package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.appraisal.facade.AppraisalService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.gob.gim.exception.ReverseNotAllowedException;
import org.gob.gim.income.facade.ElectronicService;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.ReceiptType;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.gim.income.model.TillPermission;

@Name("electronicDocumentHome")
public class ElectronicDocumentHome extends EntityHome<Receipt> {

	private static final long serialVersionUID = 1L;
	
	@Logger
	Log logger;

	private List<Receipt> receipts = new ArrayList<Receipt>();
	
	private ReceiptType receiptType;
	
	private Date beginDate;
	
	private Date endDate;
	
	private StatusElectronicReceipt statusElectronicReceipt;
	
	private String criteria;
	
	private int limitRecords = 0;
	
	public void setReceiptId(Long id) {
		setId(id);
	}

	public Long getReceiptId() {
		return (Long) getId();
	}

	public boolean isWired() {
		return true;
	}

	public Receipt getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@Override
	public String persist() {
		try{
			return "persisted";
		} catch (Exception e){
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
			return null;
		}
	}

	@Override
	public String update() {
		System.out.println("UPDATE START");
		ElectronicService electronicService = ServiceLocator.getInstance().findResource(ElectronicService.LOCAL_NAME);
		try{
			org.jboss.seam.transaction.Transaction.instance().setTransactionTimeout(7200);
			electronicService.authorizedElectronicReceipts(receipts);
			System.out.println("RECEIPTS UPDATED");
			return "updated";
		} catch(Exception e){
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
			e.printStackTrace();
			return null;
		} 
	}

	public List<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}

	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public StatusElectronicReceipt getStatusElectronicReceipt() {
		return statusElectronicReceipt;
	}

	public void setStatusElectronicReceipt(
			StatusElectronicReceipt statusElectronicReceipt) {
		this.statusElectronicReceipt = statusElectronicReceipt;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public void findDocuments(){
		ElectronicService electronicService = ServiceLocator.getInstance().findResource(ElectronicService.LOCAL_NAME);
		receipts.clear();
		receipts = electronicService.findReceipts(beginDate, endDate, statusElectronicReceipt, limitRecords);
	}
	
	public void AuthorizedElectronicReceipts(){
		update();
//		ElectronicService electronicService = ServiceLocator.getInstance().findResource(ElectronicService.LOCAL_NAME);
//		electronicService.authorizedElectronicReceipts(receipts);
////		for (Receipt receipt : receipts) {
////			electronicService.authorizedElectronicReceipt(receipt);
////		}
		resetAll();
//		findDocuments();
	}

	public void ConsultElectronicReceipts(){
		System.out.println("START CONSULT");
		ElectronicService electronicService = ServiceLocator.getInstance().findResource(ElectronicService.LOCAL_NAME);
		try{
			electronicService.consultElectronicReceipts(receipts);
			System.out.println("END CONSULT");
		} catch(Exception e){
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
			e.printStackTrace();
		} 
		resetAll();
	}

	public int getLimitRecords() {
		return limitRecords;
	}

	public void setLimitRecords(int limitRecords) {
		this.limitRecords = limitRecords;
	}
	
	public void resetAll() {
		receipts.clear();
	}

}