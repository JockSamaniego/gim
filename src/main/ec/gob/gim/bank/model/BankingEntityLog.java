package ec.gob.gim.bank.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;


@Entity
@TableGenerator(name = "BankingEntityLogGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "BankingEntityLog", initialValue = 1, allocationSize = 1)

public class BankingEntityLog {

	@Id
	@GeneratedValue(generator = "BankingEntityLogGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column
	private String methodUsed;
	
	@Column
	private Date dateTransaction;
	
	@Column
	private String transactionId;
	
	@Column
	private String bankUsername;
	
	@Column
	private Boolean methodCompleted;
	
	@Column
	private String codeError;
	
	
	public BankingEntityLog() {

	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getMethodUsed() {
		return methodUsed;
	}


	public void setMethodUsed(String methodUsed) {
		this.methodUsed = methodUsed;
	}


	public Date getDateTransaction() {
		return dateTransaction;
	}


	public void setDateTransaction(Date dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getBankUsername() {
		return bankUsername;
	}

	public void setBankUsername(String bankUsername) {
		this.bankUsername = bankUsername;
	}


	public Boolean getMethodCompleted() {
		return methodCompleted;
	}


	public void setMethodCompleted(Boolean methodCompleted) {
		this.methodCompleted = methodCompleted;
	}


	public String getCodeError() {
		return codeError;
	}

	public void setCodeError(String codeError) {
		this.codeError = codeError;
	}

	
}