package ec.gob.gim.revenue.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.complementvoucher.model.ElectronicItem;

/**
 * @author mac
 * @version 1.0
 * @created 2022-01-06
 */ 
@Entity
@TableGenerator(
		name = "BondMailInformationGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "BondMailInformation", 
		initialValue = 1, allocationSize = 1
)
public class BondMailInformation{

	@Id
	@GeneratedValue(generator = "BondMailInformationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	
	private String emailSendit;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendmailDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond bond;
	

	public BondMailInformation() {		 
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}
  
	public MunicipalBond getBond() {
		return bond;
	}

	public void setBond(MunicipalBond bond) {
		this.bond = bond;
	}

	public String getEmailSendit() {
		return emailSendit;
	}

	public void setEmailSendit(String emailSendit) {
		this.emailSendit = emailSendit;
	}

	public Date getSendmailDate() {
		return sendmailDate;
	}

	public void setSendmailDate(Date sendmailDate) {
		this.sendmailDate = sendmailDate;
	} 
}