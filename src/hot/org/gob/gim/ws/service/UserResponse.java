package org.gob.gim.ws.service;

import org.gob.loja.gim.ws.dto.Taxpayer;

public class UserResponse {

	private String message;
	private String name;
	private String status; 
	private Taxpayer taxpayer;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Taxpayer getTaxpayer() {
		return taxpayer;
	}
	public void setTaxpayer(Taxpayer taxpayer) {
		this.taxpayer = taxpayer;
	}	
}
