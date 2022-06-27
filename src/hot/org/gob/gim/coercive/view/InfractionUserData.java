package org.gob.gim.coercive.view;

import java.math.BigDecimal;

public class InfractionUserData {

	private String name;
	private String identification;
	private String mail;
	private String phone;
	private Long totalinfractions;
	private BigDecimal value;
	private BigDecimal interest;
	private BigDecimal total;

	public InfractionUserData() {
		this.identification = "";
		this.name = "";
		this.totalinfractions = 0L;
		this.value = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
	}

 
	public InfractionUserData(String name, String identification, String mail,
			String phone, Long totalinfractions, BigDecimal value, BigDecimal interest, BigDecimal total) {
		this.name = name;
		this.identification = identification;
		this.mail = mail;
		this.phone = phone;
		this.totalinfractions = totalinfractions;
		this.value = value;
		this.interest = interest;
		this.total = total;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getTotalinfractions() {
		return totalinfractions;
	}

	public void setTotalinfractions(Long totalinfractions) {
		this.totalinfractions = totalinfractions;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
