package ec.gob.gim.income.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
	 name="CreditNoteTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="CreditNoteType",
	 initialValue=1, allocationSize=1
 )
 @NamedQueries({
	 @NamedQuery(name="CreditNoteType.findAll", query="SELECT o FROM CreditNoteType o")
 })
public class CreditNoteType {
	@Id
	@GeneratedValue(generator="CreditNoteTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
		
	private String name;
	
	private String account;
	
	public CreditNoteType() {
		
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	

}
