package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ec.gob.gim.commercial.model.Local;
import ec.gob.gim.revenue.model.Adjunct;

@Entity
@DiscriminatorValue("BLO")
public class BusinessLocalReference extends Adjunct{
	@ManyToOne
	private Local local;
	
	private Boolean lostFiscalYear;
	
	@Column(length=150)
	private String manager;

	@Column(length=150)
	private String owner;

	@Column(length=100)
	private String location;

	@Column(length=120)
	private String businessName;

	@Column(length=120)
	private String localName;

	//@tag descuentoEmprendimiento
	//@author macartuche
	//@date 2016-09-23T12:04
	private Boolean hasDiscountByEntrepreneurship;
	
	public BusinessLocalReference(){
		lostFiscalYear = Boolean.FALSE;
	}

	public Boolean getLostFiscalYear() {
		return lostFiscalYear;
	}

	public void setLostFiscalYear(Boolean lostFiscalYear) {
		this.lostFiscalYear = lostFiscalYear;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}
	
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	@Override
	public String toString() {
		if(local == null || local.getBusiness() == null) return "";
		if(location == null || location.trim().length() == 0) 
			return local.getBusiness().getName() + " - " + local.getAddress().toString();
		else
			return local.getBusiness().getName() + " - " + location;
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
		if(local == null) return details;
		ValuePair pair = null;
		if ((businessName != null) && (businessName.trim().length() > 0)){
			pair = new ValuePair("Negocio", businessName);
			details.add(pair);
		} else {
			pair = new ValuePair("Negocio", local.getBusiness().getName());
			details.add(pair);
		}
		if ((localName != null) && (localName.trim().length() > 0)){
			pair = new ValuePair("Local", localName);
			details.add(pair);
		} else {
			pair = new ValuePair("Local", local.getName());
			details.add(pair);
		}
		if ((manager != null) && (manager.trim().length() > 0)){
			pair = new ValuePair("Rep. Legal", manager.trim());
			details.add(pair);
		}
		if ((location != null) && (location.trim().length() > 0)){
			pair = new ValuePair("Ubicación", location);
			details.add(pair);
		} else {
			pair = new ValuePair("Ubicación", local.getAddress().toString());
			details.add(pair);			
		}
		return details;
	}

	//@tag descuentoEmprendimiento
	//@author macartuche
	//@date 2016-09-23T12:04
	public Boolean getHasDiscountByEntrepreneurship() {
		return hasDiscountByEntrepreneurship;
	}

	public void setHasDiscountByEntrepreneurship(Boolean hasDiscountByEntrepreneurship) {
		this.hasDiscountByEntrepreneurship = hasDiscountByEntrepreneurship;
	}	
}
