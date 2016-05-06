package ec.gob.gim.consession.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.market.model.ProductType;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.waterservice.model.MonthType;

/**
 * @author richard
 * @version 1.0
 * @created 02-ago-2013 17:18:01
 */

@Audited
@Entity
@TableGenerator(name = "ConcessionItemGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConcessionItem", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "ConcessionItem.findByYearMonth", query = "SELECT ci FROM ConcessionItem ci "
				+ "left join fetch ci.place place "
				+ "left join fetch place.resident resident "
				+ "left join fetch place.currentContract contract "
				+ "left join fetch place.consessionPlaceType cpt "
				+ "WHERE ci.year = :year and ci.month = :month and place.isActive = :isActive "
				// + "and cpt.id = :cptID "
				+ "and ci.isOldItem is FALSE "
				+ "and place.id in(:places) order by place.internalOrder"),
		@NamedQuery(name = "ConcessionItem.findIdsByYearMonth", query = "SELECT ci.id FROM ConcessionItem ci "
				+ "left join ci.place place "
				+ "left join place.resident resident "
				+ "left join place.currentContract contract "
				+ "left join place.consessionPlaceType cpt "
				+ "WHERE ci.year = :year and ci.month = :month and place.isActive = :isActive "
				// + "and cpt.id = :cptID "
				+ "and ci.isOldItem is FALSE "
				+ "and place.id in(:places)"),
		@NamedQuery(name = "ConcessionItem.findOldIdsByYearMonth", query = "SELECT pci.id FROM ConcessionItem ci "
				+ "left join ci.place place "
				+ "left join ci.previousConcessionItem pci "
				//+ "left join place.resident resident "
				//+ "left join place.currentContract contract "
				//+ "left join place.consessionPlaceType cpt "
				+ "WHERE ci.year = :year and ci.month = :month and place.isActive = :isActive "
				+ "and place.id in(:places) and pci.id <> null "),
		@NamedQuery(name = "ConcessionItem.findMissingItemsByYearMonth", query = "SELECT ci.id FROM ConcessionItem ci "
				+ "left join ci.place place "
				//+ "left join place.resident resident "
				//+ "left join place.currentContract contract "
				//+ "left join place.consessionPlaceType cpt "
				+ "WHERE ci.year = :year and ci.month = :month and place.isActive = :isActive "
				+ "and place.id in(:places) "
				+ "and ci.isOldItem is FALSE "
				+ "and ci.id not in (:itemsAlreadyGenerated)"),
		@NamedQuery(name = "ConcessionItem.findByIds", query = "SELECT ci FROM ConcessionItem ci "
				+ "left join fetch ci.place place "
				+ "left join fetch place.resident resident "
				+ "left join fetch place.currentContract contract "
				+ "left join fetch place.consessionPlaceType cpt "
				+ "WHERE ci.id in(:itemIds) order by place.internalOrder")

})
public class ConcessionItem {

	@Id
	@GeneratedValue(generator = "ConcessionItemGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private MonthType monthType;

	private BigDecimal amount;

	@Column(length = 80, nullable = false)
	private String concept;

	@Column(length = 80)
	private String address;

	@Column(length = 80, nullable = false)
	private String explanation;

	private Integer month;

	@Temporal(TemporalType.DATE)
	private Date registerDate;
	
	/**
	 * cuando no renovo la concession queda en true, para identiicar hasta cuel llege
	 * con un concessionPlace
	 */
	private Boolean isOldItem;

	/**
	 * para determinar si es la primera lectura registrada para reportes no se
	 * la debe tomar
	 */
	private Boolean isFirst;

	private Integer year;

	//@JoinColumn(name = "place_id")
	@ManyToOne
	private ConcessionPlace place;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalBond_id")
	private MunicipalBond municipalBond;

	@ManyToOne
	@JoinColumn(name = "productType_id")
	private ProductType productType;

	@ManyToOne
	@JoinColumn(name = "previousci_id")
	private ConcessionItem previousConcessionItem;

	public ConcessionItem() {
		this.registerDate = new Date();
		this.isFirst = Boolean.FALSE;
		this.isOldItem = Boolean.FALSE;
	}

	public void finalize() throws Throwable {

	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept.toUpperCase();
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation.toUpperCase();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public ConcessionPlace getPlace() {
		return place;
	}

	public void setPlace(ConcessionPlace place) {
		this.place = place;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public MonthType getMonthType() {
		return monthType;
	}

	public void setMonthType(MonthType monthType) {
		this.monthType = monthType;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if(address!=null){
			this.address = address.toUpperCase();	
		}
	}

	public Boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}

	public ConcessionItem getPreviousConcessionItem() {
		return previousConcessionItem;
	}

	public void setPreviousConcessionItem(ConcessionItem previousConcessionItem) {
		this.previousConcessionItem = previousConcessionItem;
	}

	public Boolean getIsOldItem() {
		return isOldItem;
	}

	public void setIsOldItem(Boolean isOldItem) {
		this.isOldItem = isOldItem;
	}

}// end ConcessionItem