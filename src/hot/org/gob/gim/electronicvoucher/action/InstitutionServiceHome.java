package org.gob.gim.electronicvoucher.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.LegalEntityType;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.complementvoucher.model.ComplementVoucher;
import ec.gob.gim.complementvoucher.model.ComplementVoucherType;
import ec.gob.gim.complementvoucher.model.ElectronicVoucher;
import ec.gob.gim.complementvoucher.model.ElectronicVoucherDetail;
import ec.gob.gim.complementvoucher.model.InstitutionService;
import ec.gob.gim.complementvoucher.model.RetentionCode;
import ec.gob.gim.complementvoucher.model.RetentionTax;
import ec.gob.gim.complementvoucher.model.TypeEmissionPoint;

@Name("institutionServiceHome")
public class InstitutionServiceHome extends EntityHome<InstitutionService>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;

	private ComplementVoucher complement;
	private List<ComplementVoucherType> types;
	private Boolean branch=false;

	public void setInstitutionServiceId(Long id) {
		setId(id);
	}

	public Long getInstitutionServiceId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		this.instance.setAccountingRecords(Boolean.FALSE);
		this.instance.setIsMatrix(Boolean.TRUE); 
	}

	public boolean isWired() {
		return true;
	}

	public InstitutionService getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void newComplementVoucher() {
		this.complement = new ComplementVoucher(); 
	}

	public void addComplementVoucher() {

		if (this.complement != null) {
			this.complement.setInstitutionService(this.getInstance());
			this.complement.setCurrentValue(1L);
			List<TypeEmissionPoint> voucherBypoints = new ArrayList<TypeEmissionPoint>();
			for(ComplementVoucherType type : this.types){
				TypeEmissionPoint tep = new TypeEmissionPoint();
				tep.setComplementVoucher(this.complement);
				tep.setComplementVoucherType(type);
				tep.setCurrentValue(1L);
				voucherBypoints.add(tep);
			}
			this.complement.setTypesemiEmissionPointsList(voucherBypoints);
			this.getInstance().getComplementVoucherList().add(this.complement);
			this.complement = null;
			
		}
	}
 
	
	public void removeComplementVoucher(ComplementVoucher complement) {
		this.getInstance().getComplementVoucherList().remove(complement);
	}

	@Factory("typesEmission")
	@SuppressWarnings("unchecked")
	public List<ComplementVoucherType> findTypes() {
		Query query = getPersistenceContext().createNamedQuery(
				"ComplementVoucherType.findAll"); 
		return query.getResultList();
	}

	public ComplementVoucher getComplement() {
		return complement;
	}

	public void setComplement(ComplementVoucher complement) {
		this.complement = complement;
	}
	
	@Factory("institutionParent")
	@SuppressWarnings("unchecked")
	public List<InstitutionService> findMatrix() {
		Query query = getPersistenceContext().createNamedQuery("InstitutionService.findMatrix");
		query.setParameter("isMatrix", Boolean.TRUE);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> searchResident(Object suggest){ 
		String queryString = "Select p from Person p where p.identificationNumber like :identification or"
				+ " lower(p.name) like :name";
		String suggestion = (String)suggest;
		if(suggestion.length() > 6){
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("identification", suggestion.toLowerCase()+"%");
			query.setParameter("name", suggestion.toLowerCase()+"%");
			return (List<Person>)query.getResultList();
		}else{
			return new ArrayList<Person>();
		}
		
	}

	
	public void addResident(Person p){
		if(this.complement!=null){
			this.complement.setPerson(p);
		}
	}
	
	public Boolean getBranch() {
		return branch;
	}

	public void setBranch(Boolean branch) {
		this.branch = branch;
	}

	public void fijarBranch(){
		this.branch = (this.branch)? false: true;
		System.out.println("=>"+this.branch);
	}

	public List<ComplementVoucherType> getTypes() {
		return types;
	}

	public void setTypes(List<ComplementVoucherType>  types) {
		this.types = types;
	}
}

