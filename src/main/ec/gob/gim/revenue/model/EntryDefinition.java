package ec.gob.gim.revenue.model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * Definicion de rubro: Permite definir el valor y la forma de calculo o regla
 * aplicada para obtener el valor a cobrar por el Rubro correspondiente
 * 
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:33
 */
@Audited
@Entity
@TableGenerator(
		name = "EntryDefinitionGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "EntryDefinition", 
		initialValue = 1, allocationSize = 1
)

@NamedQueries(value = {
		@NamedQuery(name="EntryDefinition.findByEntryParentAndEntryChildAndMaxStarDate", 
				query="select entryDefinition from EntryDefinition entryDefinition " +
							"left join fetch entryDefinition.entry entry " +							 
						"where " +
							"entryDefinition.id in " +
								"(Select max(ed.id) from EntryDefinition ed " +
									"join ed.entry.parents parents " +
									//"where (ed.entry = :entry or ed.entry.parents = :entry) and " +
									"where (ed.entry = :entry or parents.parent = :entry) and " +
										"ed.startDate <= :startDate " + 
								"group by ed.entry)"
			),
		@NamedQuery(name="EntryDefinition.findByEntryAndMaxStarDate", 
					query="select entryDefinition from EntryDefinition entryDefinition " +
								"left join fetch entryDefinition.entry entry " + 
							"where " +
								"entryDefinition.id in " +
									"(Select max(ed.id) from EntryDefinition ed " +
										"where (ed.entry = :entry) and " +
											"ed.startDate <= :startDate )" 
			),		
		@NamedQuery(name="EntryDefinition.findByEntryParentAndMaxStarDate", 
				query="select entryDefinition from EntryDefinition entryDefinition " +
							"left join fetch entryDefinition.entry entry " + 
						"where " +
							"entryDefinition.id in " +
								"(Select max(ed.id) from EntryDefinition ed " +
									//"join ed.entry entry join entry.parents parents " +
									//"where parents = :entry and " +
									//"join ed.entry entry " +
									//"where :entry member of entry.parents and " +
									"join ed.entry entry1 join entry1.parents parents " +
									"where parents.parent = :entry and " +
										"ed.startDate <= :startDate " + 
								"group by ed.entry)" 
		),
		@NamedQuery(name="EntryDefinition.findMaxIdByEntryAndServiceDate", 
				query="Select max(ed.id) from EntryDefinition ed " +
						"where ed.entry.id in (:entryIds) and " +
							"ed.startDate <= :serviceDate " + 
						"group by ed.entry)" 
		),
		/// Esta consulta obtiene los entryDefinitions de acuerdo al orden de los ids
		// Pero se requiere a veces que sea ordenada
		@NamedQuery(name="EntryDefinition.findByIds", 
				query="select entryDefinition from EntryDefinition entryDefinition " +
							"left join fetch entryDefinition.entry entry " + 
						"where " +
							"entryDefinition.id in (:ids)"					 
		),
		@NamedQuery(name="EntryDefinition.findById", 
				query="select entryDefinition from EntryDefinition entryDefinition " +
							"left join fetch entryDefinition.entry entry " + 
						"where " +
							"entryDefinition.id = :entryDefinitionId)"					 
		)
		
}
)
public class EntryDefinition {

	@Id
	@GeneratedValue(generator = "EntryDefinitionGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private Boolean isCurrent;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	private BigDecimal value;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private EntryDefinitionType entryDefinitionType;
	
	private Boolean isAutoNullable;
	
	private Integer factor;
	
	private Boolean applyInterest;
	
	@Column(length = 2147483647, columnDefinition="TEXT")
	private String rule;
	
	@ManyToOne
	private Entry entry;
	
	private String reason;

	public EntryDefinition() {
		this.startDate = java.util.Calendar.getInstance().getTime();
		factor = new Integer(1);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public EntryDefinitionType getEntryDefinitionType() {
		return entryDefinitionType;
	}

	public void setEntryDefinitionType(EntryDefinitionType entryDefinitionType) {
		this.entryDefinitionType = entryDefinitionType;
	}
	
	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	
	/**
	 * @return the isAutoNullable
	 */
	public Boolean getIsAutoNullable() {
		return isAutoNullable;
	}

	/**
	 * @param isAutoNullable the isAutoNullable to set
	 */
	public void setIsAutoNullable(Boolean isAutoNullable) {
		this.isAutoNullable = isAutoNullable;
	}

	/**
	 * @return the factor
	 */
	public Integer getFactor() {
		return factor;
	}

	/**
	 * @param factor the factor to set
	 */
	public void setFactor(Integer factor) {
		this.factor = factor;
	}
			
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	/*
	public String getRuleText(){
		if (rule != null){
			return new String(rule);
		}
		return "";
	}
	
	public void setRuleText(String ruleText){	
		if (ruleText != null){
			rule = ruleText.getBytes();
			return;
		} 
		rule = null;
	}*/

	/**
	 * @return the applyInterest
	 */
	public Boolean getApplyInterest() {
		return applyInterest;
	}

	/**
	 * @param applyInterest the applyInterest to set
	 */
	public void setApplyInterest(Boolean applyInterest) {
		this.applyInterest = applyInterest;
	}
	
	public static void printBytes(byte[] array, String name) {
	    for (int k = 0; k < array.length; k++) {
	        //System.out.println(name + "[" + k + "] = " );
	    }
	}
	
	
	public static void main(String[] args){
		try {
			String original = new String("A" + "\u00ea" + "\u00f1" + "\u00fc" + "C");
		    byte[] utf8Bytes = original.getBytes("UTF8");
		    byte[] defaultBytes = original.getBytes();

		    String roundTrip = new String(utf8Bytes, "UTF8");
		    /*System.out.println("defaultBytes = " + defaultBytes);
		    System.out.println("utf8 = " + utf8Bytes);
		    System.out.println("roundTrip = " + roundTrip);
		    System.out.println();*/
		    printBytes(utf8Bytes, "utf8Bytes");
		    //System.out.println();
		    printBytes(defaultBytes, "defaultBytes");
		} 
		catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}

	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}// end EntryDefinition