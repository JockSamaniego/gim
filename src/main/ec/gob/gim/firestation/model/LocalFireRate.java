package ec.gob.gim.firestation.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.commercial.model.FireRates;

@Audited
@Entity
@TableGenerator(name = "LocalFireRate", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "LocalFireRate", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "LocalFireRate.findAll", query = "SELECT f FROM LocalFireRate f order by f.id") })
public class LocalFireRate implements Comparable<LocalFireRate>{

	@Id
	@GeneratedValue(generator = "LocalFireRate", strategy = GenerationType.TABLE)
	private Long id;
	
	/**
	 * cantidad de emisión, por defecto 1
	 */
	private Integer amount;
	
	/**
	 * campor editable para poiner el nombre de la actividad
	 */
	private String activity;
	
	private BigDecimal value;
	
	private BigDecimal total;

	@ManyToOne
	private Business business;

	@ManyToOne
	private FireRates fireRates;

	@ManyToOne(fetch = FetchType.LAZY)
	private EmisionFireRate emisionFireRate;
	
	

	public LocalFireRate() {
		this.amount = 1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public FireRates getFireRates() {
		return fireRates;
	}

	public void setFireRates(FireRates fireRates) {
		this.fireRates = fireRates;
	}

	public EmisionFireRate getEmisionFireRate() {
		return emisionFireRate;
	}

	public void setEmisionFireRate(EmisionFireRate emisionFireRate) {
		this.emisionFireRate = emisionFireRate;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
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

	@Override
	public int compareTo(LocalFireRate o) {
		if(o != null){ 
			return this.fireRates.getId().intValue() - o.fireRates.getId().intValue();
		}
		return 0;
	}
	
	public void setSelectedFireRate(FireRates fireRates){
		this.fireRates = fireRates;
		this.activity = fireRates.getActivity();
		this.value = fireRates.getValue();
		this.total = this.value.multiply(BigDecimal.valueOf(this.amount));
	}
	
	public void updateTotal(){
		this.total = this.value.multiply(BigDecimal.valueOf(this.amount));
	}
	
	public void setData(LocalFireRate lfr){
		this.fireRates = lfr.getFireRates();
		this.activity = lfr.getActivity();
		this.value = lfr.getValue();
		this.total = lfr.getTotal();
		this.amount = lfr.getAmount();
	}

}
