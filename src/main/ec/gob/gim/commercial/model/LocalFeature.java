package ec.gob.gim.commercial.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

/**
 * @author richard
 * @version 1.0
 * @created 05-nov-2013 08:36:22
 */
@Audited
@Entity
@TableGenerator(name = "LocalFeatureGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "LocalNice", initialValue = 1, allocationSize = 1)
public class LocalFeature {

	@Id
	@GeneratedValue(generator = "LocalFeatureGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 20)
	private String comercialRegister;

	private Integer habitation;
	private Integer person;
	private Integer personPlaza;
	private Integer _table;
	private Integer tablePlaza;
	private Integer vehicle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "featureCategory_id")
	private FeatureCategory featureCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "businessCatalog_id")
	private BusinessCatalog businessCatalog;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "localFeature", cascade = CascadeType.ALL)
	private Local local;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComercialRegister() {
		return comercialRegister;
	}

	public void setComercialRegister(String comercialRegister) {
		this.comercialRegister = comercialRegister;
	}

	public Integer getHabitation() {
		return habitation;
	}

	public void setHabitation(Integer habitation) {
		this.habitation = habitation;
	}

	public Integer getPerson() {
		return person;
	}

	public void setPerson(Integer person) {
		this.person = person;
	}

	public Integer getPersonPlaza() {
		return personPlaza;
	}

	public void setPersonPlaza(Integer personPlaza) {
		this.personPlaza = personPlaza;
	}

	public Integer getTablePlaza() {
		return tablePlaza;
	}

	public void setTablePlaza(Integer tablePlaza) {
		this.tablePlaza = tablePlaza;
	}

	public Integer getVehicle() {
		return vehicle;
	}

	public void setVehicle(Integer vehicle) {
		this.vehicle = vehicle;
	}

	public FeatureCategory getFeatureCategory() {
		return featureCategory;
	}

	public void setFeatureCategory(FeatureCategory featureCategory) {
		this.featureCategory = featureCategory;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Integer get_table() {
		return _table;
	}

	public void set_table(Integer _table) {
		this._table = _table;
	}

	public BusinessCatalog getBusinessCatalog() {
		return businessCatalog;
	}

	public void setBusinessCatalog(BusinessCatalog businessCatalog) {
		this.businessCatalog = businessCatalog;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<table width='100%'>");
		stringBuilder.append("<tr>");
		stringBuilder.append("<td width='25%'>");
		stringBuilder.append("Habitaciones:");
		stringBuilder.append("</td>");
		stringBuilder.append("<td>");
		stringBuilder.append(habitation != null ? habitation : "-");
		stringBuilder.append("</td>");
		stringBuilder.append("</tr>");

		stringBuilder.append("<tr>");
		stringBuilder.append("<td>");
		stringBuilder.append("Personas:	");
		stringBuilder.append("</td>");
		stringBuilder.append("<td>");
		stringBuilder.append(person != null ? person : "-");
		stringBuilder.append("</td>");
		stringBuilder.append("</tr>");

		stringBuilder.append("<tr>");
		stringBuilder.append("<td>");
		stringBuilder.append("Plazas:	");
		stringBuilder.append("</td>");
		stringBuilder.append("<td>");
		stringBuilder.append(personPlaza != null ? personPlaza : "-");
		stringBuilder.append("</td>");
		stringBuilder.append("</tr>");

		stringBuilder.append("<tr>");
		stringBuilder.append("<td>");
		stringBuilder.append("Mesas:	");
		stringBuilder.append("</td>");
		stringBuilder.append("<td>");
		stringBuilder.append(_table != null ? _table : "-");
		stringBuilder.append("</td>");
		stringBuilder.append("</tr>");

		stringBuilder.append("<tr>");
		stringBuilder.append("<td>");
		stringBuilder.append("Plazas:	");
		stringBuilder.append("</td>");
		stringBuilder.append("<td>");

		stringBuilder.append(tablePlaza != null ? tablePlaza : "-");
		stringBuilder.append("</td>");
		stringBuilder.append("</tr>");

		stringBuilder.append("<tr>");
		stringBuilder.append("<td>");
		stringBuilder.append("Vehículos:	");
		stringBuilder.append("</td>");
		stringBuilder.append("<td>");
		stringBuilder.append(vehicle != null ? vehicle : "-");
		stringBuilder.append("</td>");
		stringBuilder.append("</tr>");
		
		stringBuilder.append("</table>");
		return stringBuilder.toString();
	}
	/*
	 * @ManyToOne(cascade=CascadeType.ALL)
	 * 
	 * @JoinColumn(name="address_id")
	 * 
	 * @Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	 * private Address address;
	 * 
	 * @ManyToOne private Business business;
	 */

	/*
	 * @ManyToOne(cascade=CascadeType.ALL)
	 * 
	 * @JoinColumn(name="localFeature_id")
	 * 
	 * @Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	 * private LocalFeature localFeature;
	 */

	/*
	 * @OneToOne(fetch=FetchType.LAZY)
	 * 
	 * @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN) private
	 * LocalFeature localFeature;
	 */
}// end Local