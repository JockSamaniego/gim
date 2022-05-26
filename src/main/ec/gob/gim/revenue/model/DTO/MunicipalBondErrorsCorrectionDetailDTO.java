package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;


@NativeQueryResultEntity
public class MunicipalBondErrorsCorrectionDetailDTO {
	
	@NativeQueryResultColumn(index = 0)
	private int number;
		
	@NativeQueryResultColumn(index = 1)
	private String entryName;
	
	@NativeQueryResultColumn(index = 2)
	private BigDecimal value;
	
	@NativeQueryResultColumn(index = 3)
	private String accountCode;
	
	@NativeQueryResultColumn(index = 4)
	private String accountName;
	
	@NativeQueryResultColumn(index = 5)
	private int itemOrder;
	

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

}
