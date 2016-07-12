package ec.gob.gim.revenue.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;


@Audited
@Entity
@TableGenerator(name="ExemptionForPropertyGenerator",
				pkColumnName="name",
				pkColumnValue="ExemptionForProperty",
				table="IdentityGenerator",
				valueColumnName="value",
				allocationSize = 1, initialValue = 1)
public class ExemptionForProperty {
	
	@Id
	@GeneratedValue(generator="ExemptionForPropertyGenerator", strategy=GenerationType.TABLE)
	private Long id;

	@ManyToOne
	@JoinColumn(name="exemption_id")
	private Exemption exemption;
	
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;
	
	private BigDecimal amountCreditYear1;
	
	private BigDecimal amountCreditYear2;
	
	private BigDecimal amountCreditYear3;
	
	private BigDecimal discountPercentage;
	
	@Column(length=150)
	private String nameHistoryResident;
	
	public ExemptionForProperty() {
		this.amountCreditYear1 = BigDecimal.ZERO;
		this.amountCreditYear2 = BigDecimal.ZERO;
		this.amountCreditYear3 = BigDecimal.ZERO;
		this.discountPercentage = new BigDecimal(100);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Exemption getExemption() {
		return exemption;
	}

	public void setExemption(Exemption exemption) {
		this.exemption = exemption;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public BigDecimal getAmountCreditYear1() {
		return amountCreditYear1;
	}

	public void setAmountCreditYear1(BigDecimal amountCreditYear1) {
		this.amountCreditYear1 = amountCreditYear1;
	}

	public BigDecimal getAmountCreditYear2() {
		return amountCreditYear2;
	}

	public void setAmountCreditYear2(BigDecimal amountCreditYear2) {
		this.amountCreditYear2 = amountCreditYear2;
	}

	public BigDecimal getAmountCreditYear3() {
		return amountCreditYear3;
	}

	public void setAmountCreditYear3(BigDecimal amountCreditYear3) {
		this.amountCreditYear3 = amountCreditYear3;
	}

	public String getNameHistoryResident() {
		return nameHistoryResident;
	}

	public void setNameHistoryResident(String nameHistoryResident) {
		this.nameHistoryResident = nameHistoryResident;
	}

	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	
}