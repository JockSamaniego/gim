package org.gob.gim.emoney.action;

public enum EmoneyReportType {
	
	URBAN_PROPERTY("00056"),
    RUSTIC_PROPERTY("00057"),
    DRINKING_WATER("00076"),
    POLICE_FINE("00387"),
    RENT_MARKET("00027"), 
	ALL("");
	
	
	private String account;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccount() {
		return account;
	}
	
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	EmoneyReportType(String account){
		this.account = account;
	}
	
}
