package org.gob.gim.accounting.dto;

public enum ReportType {
	REVENUE(new String("GAD")),
	INCOME(new String("GAD")),
	VOID(new String("GAD")),
	DUE_PORTFOLIO(new String("GAD")),
	QUOTAS_LIQUIDATION(new String("GAD")),
	COMBINED(new String("GAD")),
	BALANCE(new String("GAD")),
	SUBSCRIPTION(new String("GAD")),
	REVENUE_EMAALEP(new String("EMAALEP")),
	INCOME_EMAALEP(new String("EMAALEP"));
	
	String roleSystem;

	private ReportType(String roleSystem){
		this.roleSystem = roleSystem;
	}
	
	public String getRoleSystem() {
		return roleSystem;
	}

	public void setRoleSystem(String roleSystem) {
		this.roleSystem = roleSystem;
	}
	
}
