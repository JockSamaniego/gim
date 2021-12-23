package ec.gob.gim.coercive.model.infractions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * Negocio: Notificación de vencimiento de obligación
 * 
 * @author jlgranda
 * @version 1.0
 * @created 23-Nov-2011 11:57:27
 */
@Audited
@Entity
@Table(name = "data_excel", schema = "infracciones")
public class Datainfraction {
	
	@Id
	@GeneratedValue(generator="infracciones.datainfraction_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name = "infracciones.datainfraction_seq", allocationSize = 1)
	private Long id;

	@Column(name="tipo_id")
	private String typeId;

	@Column(name="identificacion")
	private String identification;	

	@Column(name="nombre")
	private String name;
	 
	@Column(name="placa")
	private String licensePlate;
	

	@Column(name="tipo_deudor")
	private String debtType;
	
	@Column(name="boleta")
	private String ticket;
	 
	//estado pasado en el excel por el consorcio
	@Column(name="estado")
	private String stateConsortium;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="emision")
	private Date emision;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registro")
	private Date register;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="vencimiento")
	private Date expiration;
	
	@Column(name="articulo")
	private String article;
	
	@Column(name="articulo_description")
	private String articleDescription;
	
	@Column(name="valor")
	private BigDecimal value;
	
	@Column(name="interes")
	private BigDecimal interest;
	
	@Column(name="valor_total")
	private BigDecimal totalValue;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_datos")
	private Date dataDate;
	
	@Column(name="mejor_gestion")
	private String betterManagement;
	
	@Column(name="contacto_general")
	private String contactGeneral;
	
	@Column(name="gestion_telefonica")
	private String telephoneManagement;
	
	@Column(name="telefono_marcado")
	private String dialedPhone;
		
	@Column(name="intensidad_telefonica")	
	private Integer telephoneIntensity;
	
	@Column(name="email")	
	private String emailIntensity;
	
	@Column(name="correo")	
	private String email;
	
	@Column(name="ivr")		
	
	private Integer ivr;
	@Column(name="telefono_ivr")
	private String phoneivr;
	
	@Column(name="sms")
	private Integer sms;
	
	@Column(name="telefono_sms")
	private String phoneSms;
	
	@Column(name="observacion")
	private String observation;
	
	@Column(name="intensidad_general")
	private String generalIntensity;
	
	@Column(name="direccion")
	private String direction;
	
	@Column(name="ubicabilidad_direccion")
	private String locability;
	
	@Column(name="provincia_direccion_ubicable")	
	private String locabilityProvince;
	
	@Column(name="a_migrar")		
	private Boolean toMigrate;
	
	@Column(name="userid")		
	private Long userid;
	
	@Column(name="fecha_migracion")		
	private Date migrationDate;
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="estado_id")
	private ItemCatalog state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notificacion_id")
	private NotificationInfractions notification;

 
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getDebtType() {
		return debtType;
	}

	public void setDebtType(String debtType) {
		this.debtType = debtType;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getStateConsortium() {
		return stateConsortium;
	}

	public void setStateConsortium(String stateConsortium) {
		this.stateConsortium = stateConsortium;
	}

	public Date getEmision() {
		return emision;
	}

	public void setEmision(Date emision) {
		this.emision = emision;
	}

	public Date getRegister() {
		return register;
	}

	public void setRegister(Date register) {
		this.register = register;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getArticleDescription() {
		return articleDescription;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}

	public String getBetterManagement() {
		return betterManagement;
	}

	public void setBetterManagement(String betterManagement) {
		this.betterManagement = betterManagement;
	}

	public String getContactGeneral() {
		return contactGeneral;
	}

	public void setContactGeneral(String contactGeneral) {
		this.contactGeneral = contactGeneral;
	}

	public String getTelephoneManagement() {
		return telephoneManagement;
	}

	public void setTelephoneManagement(String telephoneManagement) {
		this.telephoneManagement = telephoneManagement;
	}

	public String getDialedPhone() {
		return dialedPhone;
	}

	public void setDialedPhone(String dialedPhone) {
		this.dialedPhone = dialedPhone;
	}

	public Integer getTelephoneIntensity() {
		return telephoneIntensity;
	}

	public void setTelephoneIntensity(Integer telephoneIntensity) {
		this.telephoneIntensity = telephoneIntensity;
	}

	public String getEmailIntensity() {
		return emailIntensity;
	}

	public void setEmailIntensity(String emailIntensity) {
		this.emailIntensity = emailIntensity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIvr() {
		return ivr;
	}

	public void setIvr(Integer ivr) {
		this.ivr = ivr;
	}

	public String getPhoneivr() {
		return phoneivr;
	}

	public void setPhoneivr(String phoneivr) {
		this.phoneivr = phoneivr;
	}

	public Integer getSms() {
		return sms;
	}

	public void setSms(Integer sms) {
		this.sms = sms;
	}

	public String getPhoneSms() {
		return phoneSms;
	}

	public void setPhoneSms(String phoneSms) {
		this.phoneSms = phoneSms;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getGeneralIntensity() {
		return generalIntensity;
	}

	public void setGeneralIntensity(String generalIntensity) {
		this.generalIntensity = generalIntensity;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getLocability() {
		return locability;
	}

	public void setLocability(String locability) {
		this.locability = locability;
	}

	public String getLocabilityProvince() {
		return locabilityProvince;
	}

	public void setLocabilityProvince(String locabilityProvince) {
		this.locabilityProvince = locabilityProvince;
	}

	public Boolean getToMigrate() {
		return toMigrate;
	}

	public void setToMigrate(Boolean toMigrate) {
		this.toMigrate = toMigrate;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Date getMigrationDate() {
		return migrationDate;
	}

	public void setMigrationDate(Date migrationDate) {
		this.migrationDate = migrationDate;
	}

	public ItemCatalog getState() {
		return state;
	}

	public void setState(ItemCatalog state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NotificationInfractions getNotification() {
		return notification;
	}

	public void setNotification(NotificationInfractions notification) {
		this.notification = notification;
	}

	@Override
	public String toString() {
		return "Datainfraction [id=" + id + ", typeId=" + typeId + ", identification=" + identification + ", name="
				+ name + ", licensePlate=" + licensePlate + ", debtType=" + debtType + ", ticket=" + ticket
				+ ", stateConsortium=" + stateConsortium + ", emision=" + emision + ", register=" + register
				+ ", expiration=" + expiration + ", article=" + article + ", articleDescription=" + articleDescription
				+ ", value=" + value + ", interest=" + interest + ", totalValue=" + totalValue + ", dataDate="
				+ dataDate + ", betterManagement=" + betterManagement + ", contactGeneral=" + contactGeneral
				+ ", telephoneManagement=" + telephoneManagement + ", dialedPhone=" + dialedPhone
				+ ", telephoneIntensity=" + telephoneIntensity + ", emailIntensity=" + emailIntensity + ", email="
				+ email + ", ivr=" + ivr + ", phoneivr=" + phoneivr + ", sms=" + sms + ", phoneSms=" + phoneSms
				+ ", observation=" + observation + ", generalIntensity=" + generalIntensity + ", direction=" + direction
				+ ", locability=" + locability + ", locabilityProvince=" + locabilityProvince + ", toMigrate="
				+ toMigrate + ", userid=" + userid + ", migrationDate=" + migrationDate + ", state=" + state
				+ ", notification=" + notification + "]";
	}
}
