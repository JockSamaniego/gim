package org.gob.gim.ws.service;

import java.math.BigDecimal;

public class BondItemPrint {
	
	private Long id;
	private BigDecimal amount;
	private Boolean istaxable;
	private int ordernumber;
	private BigDecimal value;
	private BigDecimal total;
	private Long entry_id;
	private Long municipalbond_id;
	private String code;
	private String description;
	private String name;
	private String reason;
	private String datepattern;
	
	private BondEntryPrint entry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(int ordernumber) {
		this.ordernumber = ordernumber;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getEntry_id() {
		return entry_id;
	}

	public void setEntry_id(Long entry_id) {
		this.entry_id = entry_id;
	}

	public Long getMunicipalbond_id() {
		return municipalbond_id;
	}

	public void setMunicipalbond_id(Long municipalbond_id) {
		this.municipalbond_id = municipalbond_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDatepattern() {
		return datepattern;
	}

	public void setDatepattern(String datepattern) {
		this.datepattern = datepattern;
	}

	public Boolean getIstaxable() {
		return istaxable;
	}

	public void setIstaxable(Boolean istaxable) {
		this.istaxable = istaxable;
	}

	public BondEntryPrint getEntry() {
		return entry;
	}

	public void setEntry(BondEntryPrint entry) {
		this.entry = entry;
	}
	
}
