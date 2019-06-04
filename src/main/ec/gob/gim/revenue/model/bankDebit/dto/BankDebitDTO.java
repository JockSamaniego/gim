package ec.gob.gim.revenue.model.bankDebit.dto;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class BankDebitDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Long id;
	
	@NativeQueryResultColumn(index = 1)
    private String identificationNumber;
	
	@NativeQueryResultColumn(index = 2)
    private String residentName;
	
	@NativeQueryResultColumn(index = 3)
    private String accountType;
	
	@NativeQueryResultColumn(index = 4)
    private String accountNumber;
	
	@NativeQueryResultColumn(index = 5)
    private String accountHolder;
	
	@NativeQueryResultColumn(index = 6)
    private Integer servicenumber;
	
	@NativeQueryResultColumn(index = 7)
    private Boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

	public Integer getServicenumber() {
		return servicenumber;
	}

	public void setServicenumber(Integer servicenumber) {
		this.servicenumber = servicenumber;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}