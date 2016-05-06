package ec.gob.gim.cementery.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

/**
 * @author GADM-Loja Ronald Paladines
 * @version 1.0
 * @created 20-Feb-2014
 */
@Audited
@Entity
@TableGenerator(
	name="CremationGenerator",
	table="IdentityGenerator",
	pkColumnName="name",
	valueColumnName="value",
	pkColumnValue="Cremation",
	initialValue=1, allocationSize=1
)

@NamedQueries(value = { 
		 @NamedQuery(name = "Cremation.findAll", 
				 	query = "SELECT cremation FROM Cremation cremation " +
				 			"order by cremation.date desc"), 

		 @NamedQuery(name = "Cremation.findByCrematoriumAndDates", 
		 			query = "SELECT cremation FROM Cremation cremation " +
		 					"where cremation.crematorium = :crematorium and cremation.date between :startDate and :endDate order by cremation.date")
})


public class Cremation {

	@Id
	@GeneratedValue(generator="CremationGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private BodyType bodyType;
	@Column(length = 60)
	private String name;
	private int cremationOrder;
	private int incomeOrder;
	private Double value;
	private Double rateValue;
	@Temporal(TemporalType.DATE)
	private Date date;
	@Temporal(TemporalType.DATE)
	private Date dateOfDeath;
	@Column(length = 20)
	private String kinship;//parentesco 
	private String observations;
	private Boolean freeDay = false;

	
	@ManyToOne(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "resident_id")
	private Resident bloodRelation; //deudo

	@ManyToOne
	@JoinColumn(name = "crematorium_id")
	private Crematorium crematorium;

	public Cremation(){
		value = 0.00;
		rateValue = 0.00;
		freeDay = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public int getCremationOrder() {
		return cremationOrder;
	}

	public void setCremationOrder(int cremationOrder) {
		this.cremationOrder = cremationOrder;
	}

	public int getIncomeOrder() {
		return incomeOrder;
	}

	public void setIncomeOrder(int incomeOrder) {
		this.incomeOrder = incomeOrder;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getRateValue() {
		return rateValue;
	}

	public void setRateValue(Double rateValue) {
		this.rateValue = rateValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public String getKinship() {
		return kinship;
	}

	public void setKinship(String kinship) {
		this.kinship = kinship.toUpperCase();
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public Resident getBloodRelation() {
		return bloodRelation;
	}

	public void setBloodRelation(Resident bloodRelation) {
		this.bloodRelation = bloodRelation;
	}

	public Crematorium getCrematorium() {
		return crematorium;
	}

	public void setCrematorium(Crematorium crematorium) {
		this.crematorium = crematorium;
	}

	public Boolean getFreeDay() {
		return freeDay;
	}

	public void setFreeDay(Boolean freeDay) {
		this.freeDay = freeDay;
	}

}