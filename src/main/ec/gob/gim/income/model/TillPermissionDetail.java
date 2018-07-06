package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class TillPermissionDetail {

	@NativeQueryResultColumn(index = 0)
	private Date date;		
	
	private TillPermission tillPermission;
	
	@NativeQueryResultColumn(index = 1)
	private Integer tillNumber;
	
	private Integer servedPeople;
	
	@NativeQueryResultColumn(index = 2)
	private BigDecimal totalCollected;	
	
	@NativeQueryResultColumn(index = 3)
	private BigDecimal totalCompensationCollected;
	
	private BigDecimal totalCreditNoteCollected;
	
	@NativeQueryResultColumn(index = 4)
	private BigDecimal initialBalance;	
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal total;
	
	@NativeQueryResultColumn(index = 6)
	private String branch;	
	
	private Long transactionsNumber;	
	
	@NativeQueryResultColumn(index = 7)
	private String inChargeName;
	
	@NativeQueryResultColumn(index = 8)
	private boolean isOpened;
	
	private boolean isTillBank;
	
	public TillPermission getTillPermission() {
		return tillPermission;
	}

	public void setTillPermission(TillPermission tillPermission) {
		this.tillPermission = tillPermission;
	}
	
	public BigDecimal getTotalCreditNoteCollected() {
		return totalCreditNoteCollected;
	}

	public void setTotalCreditNoteCollected(BigDecimal totalCreditNoteCollected) {
		this.totalCreditNoteCollected = totalCreditNoteCollected;
	}

	public BigDecimal getTotalCompensationCollected() {
		return totalCompensationCollected;
	}

	public void setTotalCompensationCollected(BigDecimal totalCompensationCollected) {
		this.totalCompensationCollected = totalCompensationCollected;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public String getInChargeName() {
		return inChargeName;
	}

	public void setInChargeName(String inChargeName) {
		this.inChargeName = inChargeName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getTotalCollected() {
		return totalCollected;
	}

	public void setTotalCollected(BigDecimal totalCollected) {
		this.totalCollected = totalCollected;
	}

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	public void setServedPeople(Integer servedPeople) {
		this.servedPeople = servedPeople;
	}

	public Integer getServedPeople() {
		return servedPeople;
	}

	public void setTransactionsNumber(Long transactionsNumber) {
		this.transactionsNumber = transactionsNumber;
	}

	public Long getTransactionsNumber() {
		return transactionsNumber;
	}
	
	public Integer getTillNumber() {
		return tillNumber;
	}

	public void setTillNumber(Integer tillNumber) {
		this.tillNumber = tillNumber;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public boolean isTillBank() {
		return isTillBank;
	}

	public void setTillBank(boolean isTillBank) {
		this.isTillBank = isTillBank;
	}

}
