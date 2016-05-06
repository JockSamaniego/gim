package ec.gob.gim.complementvoucher.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond; 

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:36
 */
@Audited
@Entity
@TableGenerator(
		name = "ElectronicItemGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "ElectronicItem", 
		initialValue = 1, allocationSize = 1
)
public class ElectronicItem implements Comparable<ElectronicItem>{

	@Id
	@GeneratedValue(generator = "ElectronicItemGenerator", strategy = GenerationType.TABLE)
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
	private ElectronicVoucher electronicVoucher;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond surchargedBond;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond discountedBond;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Entry targetEntry;	
	
	// Hacer referencia al item del cual se toma el dato
	@OneToOne(fetch=FetchType.LAZY)
	private Item item;
	
	@OneToMany(mappedBy = "item", fetch= FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private  List<AditionalDetail> details;

	public ElectronicItem() {		
		value = BigDecimal.ZERO;
		amount = BigDecimal.ONE;
		total = BigDecimal.ZERO;
		orderNumber = 0;
		isTaxable = Boolean.FALSE;
		details = new ArrayList<AditionalDetail>();
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
	
	public ElectronicVoucher getElectronicVoucher() {
		return electronicVoucher;
	}

	public void setElectronicVoucher(ElectronicVoucher electronicVoucher) {
		this.electronicVoucher = electronicVoucher;
	}

	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof ElectronicItem)) {
			return false;
		}
		ElectronicItem other = (ElectronicItem) object;
					
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
	public int compareTo(ElectronicItem o) {
		if(o != null){ 
			return this.orderNumber.intValue() - o.orderNumber.intValue();
		}
		return 0;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<AditionalDetail> getDetails() {
		return details;
	}

	public void setDetails(List<AditionalDetail> details) {
		this.details = details;
	}
}// end Item