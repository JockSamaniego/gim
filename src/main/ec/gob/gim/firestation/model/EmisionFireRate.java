package ec.gob.gim.firestation.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.commercial.model.Business;

@Audited
@Entity
@TableGenerator(name = "EmisionFireRate", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "EmisionFireRate", initialValue = 1, allocationSize = 1)
public class EmisionFireRate {

	@Id
	@GeneratedValue(generator = "EmisionFireRate", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date asignationDate;
	
	@ManyToOne
	private Business business;

	@OneToMany(mappedBy = "emisionFireRate", cascade = { CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<LocalFireRate> rates;

	public EmisionFireRate() {
		rates = new TreeSet<LocalFireRate>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAsignationDate() {
		return asignationDate;
	}

	public void setAsignationDate(Date asignationDate) {
		this.asignationDate = asignationDate;
	}

	public Set<LocalFireRate> getRates() {
		return rates;
	}

	public void setRates(Set<LocalFireRate> rates) {
		this.rates = rates;
	}

	public void add(LocalFireRate item) {
		if (!this.rates.contains(item)) {
			this.rates.add(item);
			item.setEmisionFireRate(this);
		}
	}

	public void remove(LocalFireRate item) {
		boolean removed = this.rates.remove(item);
		if (removed) {
			item.setEmisionFireRate((EmisionFireRate) null);
		}
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}
	
	

}
