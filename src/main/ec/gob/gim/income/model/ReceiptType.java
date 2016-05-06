package ec.gob.gim.income.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
		 name="ReceiptTypeGenerator",
		 table="IdentityGenerator",
		 pkColumnName="name",
		 valueColumnName="value",
		 pkColumnValue="ReceiptType",
		 initialValue=1, allocationSize=1
	 )

@NamedQuery(name="ReceiptType.findAll", query="select r from ReceiptType r")
public class ReceiptType {
	
	@Id
	@GeneratedValue(generator="ReceiptTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private Integer code;
	
	@Column(length=30)
	private String name;

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

	public Integer getCode() {
		return code;
	}

	public String getSriCode() {
		return String.format("%02d", code != null ? code : 0);
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}
