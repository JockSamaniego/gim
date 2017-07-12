package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class BondReport {

	@NativeQueryResultColumn(index = 0)
	private Long id;
	
	@NativeQueryResultColumn(index = 1)
	private Long number;
	
	@NativeQueryResultColumn(index = 2)
	private String account;
	
	@NativeQueryResultColumn(index = 3)
	private String serviceCode;
	
	@NativeQueryResultColumn(index = 4)
	private String serviceDate;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal total;
	
	@NativeQueryResultColumn(index = 6)
	private String paymentDate;
	
	@NativeQueryResultColumn(index = 7)
	private String antNumber;
	
	@NativeQueryResultColumn(index = 8)
	private String numberPlate;
	
	

	public BondReport(Long id, Long number, String account, String serviceCode, String serviceDate, BigDecimal total,
			String paymentDate, String antNumber, String numberPlate) {
		super();
		this.id = id;
		this.number = number;
		this.account = account;
		this.serviceCode = serviceCode;
		this.serviceDate = serviceDate;
		this.total = total;
		this.paymentDate = paymentDate;
		this.antNumber = antNumber;
		this.numberPlate = numberPlate;
	}

	/*public BondReport(Long id, Long number, String account, String serviceCode, String serviceDate, BigDecimal total,
			String paymentDate, Long antNumber, String numberPlate) {
		this.id = id;
		this.number = number;
		this.account = account;
		this.serviceCode = serviceCode;
		this.serviceDate = serviceDate;
		this.total = total;
		this.paymentDate = paymentDate;
		this.antNumber = antNumber;
		this.numberPlate = numberPlate;
	}*/
		
	public BondReport() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	/*public Long getAntNumber() {
		return antNumber;
	}

	public void setAntNumber(Long antNumber) {
		this.antNumber = antNumber;
	}*/

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getAntNumber() {
		return antNumber;
	}

	public void setAntNumber(String antNumber) {
		this.antNumber = antNumber;
	}

}
