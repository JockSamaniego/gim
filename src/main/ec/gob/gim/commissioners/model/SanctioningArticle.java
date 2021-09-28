package ec.gob.gim.commissioners.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;


@Audited
@Entity
@TableGenerator(name = "SanctioningArticleGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "SanctioningArticle", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "SanctioningArticle.findAll", query = "SELECT sa FROM SanctioningArticle sa order by sa.creationDate"),
						@NamedQuery(name = "SanctioningArticle.findByType", query = "SELECT sa FROM SanctioningArticle sa where sa.commissionerBallotType.code =:commissionerType and (sa.isActive = true or sa.isActive is null) ORDER BY sa.article,sa.numeral ASC "),
						@NamedQuery(name = "SanctioningArticle.findResidentNameByIdent", query = "Select r.name from Resident r where r.identificationNumber = :identNum"),
					  })
public class SanctioningArticle {

	@Id
	@GeneratedValue(generator = "SanctioningArticleGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(nullable = false)
	private Date creationDate;
	
	private String article;
	
	private String numeral;
	
	private String ordinance;
	
	private String detail;
	
	private String sanction;
	
	private Boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_id") 
	private Person responsible;
	   
	@JoinColumn(name = "responsible_user")	 
	@Column(length = 100)	 
	private String responsible_user;
	
	@ManyToOne
	@JoinColumn(name="itemcatalog_id")
	private ItemCatalog commissionerBallotType;
	
	public SanctioningArticle(){
		isActive = Boolean.TRUE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getNumeral() {
		return numeral;
	}

	public void setNumeral(String numeral) {
		this.numeral = numeral;
	}

	public String getOrdinance() {
		return ordinance;
	}

	public void setOrdinance(String ordinance) {
		this.ordinance = ordinance;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Person getResponsible() {
		return responsible;
	}

	public void setResponsible(Person responsible) {
		this.responsible = responsible;
	}

	public String getResponsible_user() {
		return responsible_user;
	}

	public void setResponsible_user(String responsible_user) {
		this.responsible_user = responsible_user;
	}

	public ItemCatalog getCommissionerBallotType() {
		return commissionerBallotType;
	}

	public void setCommissionerBallotType(ItemCatalog commissionerBallotType) {
		this.commissionerBallotType = commissionerBallotType;
	}

	public String getSanction() {
		return sanction;
	}

	public void setSanction(String sanction) {
		this.sanction = sanction;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	

}
