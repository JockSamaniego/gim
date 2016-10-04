package ec.gob.gim.revenue.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.complementvoucher.model.ElectronicItem;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:36
 */
@Audited
@Entity
@TableGenerator(
		name = "ItemGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Item", 
		initialValue = 1, allocationSize = 1
)
public class Item implements Comparable<Item>{

	@Id
	@GeneratedValue(generator = "ItemGenerator", strategy = GenerationType.TABLE)
	private Long id;

	// Cantidad
	private BigDecimal amount;

	//@Column(columnDefinition="numeric(19,4)")
	// Lo que cueste el servicio, impuesto o producto
	private BigDecimal value;
	
	// Factor resultante de la operacion value * amount 
	private BigDecimal total;
	
	private Integer orderNumber;
	
	private Boolean isTaxable;
	
	private String observations;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "entry_id")
	private Entry entry;

	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond municipalBond;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond surchargedBond;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond discountedBond;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Entry targetEntry;	
	
	/** para notas de credito electronicas **/
	//@author macartuche
	//@date 2015/06/11 09:56
//	@OneToOne(mappedBy="item", fetch = FetchType.LAZY)
//	private ElectronicItem electronicItem;
	
	public Item() {		
		value = BigDecimal.ZERO;
		amount = BigDecimal.ONE;
		total = BigDecimal.ZERO;
		orderNumber = 0;
		isTaxable = Boolean.FALSE;
	}

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

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return the accumulateSubtotal
	 */
	public Boolean getIsTaxable() {
		return isTaxable;
	}

	/**
	 * @param accumulateSubtotal the accumulateSubtotal to set
	 */
	public void setIsTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public void setTotal(BigDecimal total){
		this.total = total;
	}
	
	public BigDecimal getTotal(){
		return total;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Entry getTargetEntry() {
		return targetEntry;
	}
	
	public void setTargetEntry(Entry targetEntry) {
		this.targetEntry = targetEntry;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof Item)) {
			return false;
		}
		Item other = (Item) object;
					
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		
		if(this.getEntry().getId() != other.getEntry().getId()){
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	public MunicipalBond getSurchargedBond() {
		return surchargedBond;
	}

	public void setSurchargedBond(MunicipalBond surchargedBond) {
		this.surchargedBond = surchargedBond;
	}

	public MunicipalBond getDiscountedBond() {
		return discountedBond;
	}

	public void setDiscountedBond(MunicipalBond discountedBond) {
		this.discountedBond = discountedBond;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public int compareTo(Item o) {
		if(o != null){ 
			return this.orderNumber.intValue() - o.orderNumber.intValue();
		}
		return 0;
	}

	
	/** para notas de credito electronicas **/
	//@author macartuche
	//@date 2015/06/11 09:56
//	public ElectronicItem getElectronicItem() {
//		return electronicItem;
//	}

//	public void setElectronicItem(ElectronicItem electronicItem) {
//		this.electronicItem = electronicItem;
//	}
}// end Item