package ec.gob.gim.income.model;

import javax.persistence.*;


import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:47
 * Impuesto
 */
@Audited
@Entity
@TableGenerator(
	 name="TaxGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Tax",
	 initialValue=1, allocationSize=1
)
 
@NamedQueries(value = {
		@NamedQuery(name="Tax.findById", 
				query="select tax from Tax tax " +
						"where "+
						"tax.id = :taxId"),
		@NamedQuery(name="Tax.findAll", 
				query="select tax from Tax tax "),
		@NamedQuery(name="Tax.findByCriteria", 
				query="select tax from Tax tax " +
						"where "+
						"lower(tax.name) like lower(concat(:criteria, '%'))" +						
						"order by tax.name"),
		@NamedQuery(name="Tax.findByEntryId", 
				query="select tax from Entry entry join entry.taxes tax " +
					"where "+
					"entry.id = :entryId " +						
					"order by tax.name"),
		@NamedQuery(name="Tax.findByEntryNotNull", 
					query="select tax from Entry entry join entry.taxes tax " +
					"where "+
					"entry.id != null")						
		}
)
public class Tax {

	@Id
	@GeneratedValue(generator="TaxGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=50, nullable=true)	
	private String name;
	
	private String description;
	
	@Column(length=2, nullable=true)	
	private String sriCode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Account taxAccount;

	@OneToMany(mappedBy = "tax",cascade=CascadeType.ALL)
	//@JoinColumn(name="tax_id")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("startDate")
	private java.util.List<TaxRate> taxRates;

	public Tax(){
		taxRates = new java.util.ArrayList<TaxRate>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.util.List<TaxRate> getTaxRates() {
		return taxRates;
	}

	public void setTaxRates(java.util.List<TaxRate> taxRates) {
		this.taxRates = taxRates;
	}
	
	public void add(TaxRate taxRate){
		if (!this.taxRates.contains(taxRate)){
			this.taxRates.add(taxRate);
			taxRate.setTax(this);
		}
	}
	
	public void remove(TaxRate taxRate){
		boolean removed = this.taxRates.remove(taxRate);
		if(removed) taxRate.setTax(null);
	}
	
	public Account getTaxAccount() {
		return taxAccount;
	}

	public void setTaxAccount(Account taxAccount) {
		this.taxAccount = taxAccount;
	}

	public String getSriCode() {
		return sriCode;
	}

	public void setSriCode(String sriCode) {
		this.sriCode = sriCode;
	}
	
}//end Tax