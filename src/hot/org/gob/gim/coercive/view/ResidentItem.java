package org.gob.gim.coercive.view;

import java.math.BigDecimal;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;

public class ResidentItem {

	private boolean isSelected;
	private Resident resident;
	private Entry entry;
	Long account;
	Long impAccount;
	BigDecimal total;
		
	public ResidentItem() {
		resident = (Resident) new Person();
		entry = new Entry();
		account = 0L;
		impAccount = 0L;
		total = null;
	}
		
	public ResidentItem(Long id, String identificationNumber,String name,Long account,Long impAccount, BigDecimal value){		
		resident = (Resident) new Person();
		this.resident.setId(id);
		this.resident.setIdentificationNumber(identificationNumber);
		this.resident.setName(name);
		this.account = account;
		this.impAccount = impAccount;
		this.total = value;		
	}
	
	public ResidentItem(Long id, String identificationNumber,String name,Long account, BigDecimal value){		
		resident = (Resident) new Person();
		this.resident.setId(id);
		this.resident.setIdentificationNumber(identificationNumber);
		this.resident.setName(name);
		this.account = account;
		this.impAccount = 0L;
		this.total = value;		
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Resident getResident() {
		return resident;
	}
	public void setResident(Resident resident) {
		this.resident = resident;
	}
	public Long getAccount() {
		return account;
	}
	public void setAccount(Long account) {
		this.account = account;
	}
	
	public Long getImpAccount() {
		return impAccount;
	}

	public void setImpAccount(Long impAccount) {
		this.impAccount = impAccount;
	}

	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Entry getEntry() {
		return entry;
	}
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof ResidentItem)) {
			return false;
		}
		ResidentItem other = (ResidentItem) object;
					
		if ((this.resident.getId() != null && !this.resident.getId().equals(other.getResident().getId()))) {
			return false;
		}
		return true;
	}	
	
}

