package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * Bitácora Digital: datos de la Boleta.
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 25-Jun-2021
 */
@Audited
@Entity
@TableGenerator(name = "BallotPartGenerator", 
    table = "IdentityGenerator", 
    pkColumnName = "name", 
    valueColumnName = "value", 
    pkColumnValue = "BallotPart", 
    initialValue = 1, allocationSize = 1)

public class BallotPart implements Serializable {

	private static final long serialVersionUID = -6857566110454087705L;

	@Id
    @GeneratedValue(generator = "BallotPartGenerator", strategy = GenerationType.TABLE)
    private Long id; 
    
    @Enumerated(EnumType.STRING)
    @Column(length=20)
    private BallotType ballotType; // Tipo de Boleta
	@Column(length = 20)
	private String partNumber; // Número de Parte
	private String tipology; // Tipología
	private String place; // Lugar
	private String prevention; // Prevención
	private String detachment; // Destacamento
	@Temporal(TemporalType.DATE)
	private Date notificationDate; // Fecha de Citación
	@Temporal(TemporalType.TIME)
	private Date notificationTime; // Hora de Citación
	@Temporal(TemporalType.DATE)
	private Date detentionDate; // Fecha de Detención
	@Temporal(TemporalType.TIME)
	private Date detentionTime; // Hora de Detención
	@Temporal(TemporalType.DATE)
	private Date retentionDate; // Fecha de Retención
	@Temporal(TemporalType.TIME)
	private Date retentionTime; // Hora de Retención
	@Temporal(TemporalType.DATE)
	private Date partDate; // Fecha del Parte
	@Temporal(TemporalType.TIME)
	private Date partTime; // Hora del Parte
	private String agentName; // Agente Grado-Codigo-Nombres-CI
	private String code; // Código
	@Column(columnDefinition = "TEXT")
	private String circumstances; // Circunstancias
	@Column(columnDefinition = "TEXT")
	private String obs; // Observaciones
	@Column(columnDefinition = "TEXT")
	private String documents; // Documentos

    @OneToMany(mappedBy = "ballotPart", cascade = CascadeType.ALL)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Implicated> implicateds; //Implicados en la boleta inscrita
    
    public BallotPart() {
    	implicateds = new ArrayList<Implicated>();
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ballotType
	 */
	public BallotType getBallotType() {
		return ballotType;
	}

	/**
	 * @param ballotType the ballotType to set
	 */
	public void setBallotType(BallotType ballotType) {
		this.ballotType = ballotType;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the tipology
	 */
	public String getTipology() {
		return tipology;
	}

	/**
	 * @param tipology the tipology to set
	 */
	public void setTipology(String tipology) {
		this.tipology = tipology;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the prevention
	 */
	public String getPrevention() {
		return prevention;
	}

	/**
	 * @param prevention the prevention to set
	 */
	public void setPrevention(String prevention) {
		this.prevention = prevention;
	}

	/**
	 * @return the detachment
	 */
	public String getDetachment() {
		return detachment;
	}

	/**
	 * @param detachment the detachment to set
	 */
	public void setDetachment(String detachment) {
		this.detachment = detachment;
	}

	/**
	 * @return the notificationDate
	 */
	public Date getNotificationDate() {
		return notificationDate;
	}

	/**
	 * @param notificationDate the notificationDate to set
	 */
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	/**
	 * @return the notificationTime
	 */
	public Date getNotificationTime() {
		return notificationTime;
	}

	/**
	 * @param notificationTime the notificationTime to set
	 */
	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	/**
	 * @return the detentionDate
	 */
	public Date getDetentionDate() {
		return detentionDate;
	}

	/**
	 * @param detentionDate the detentionDate to set
	 */
	public void setDetentionDate(Date detentionDate) {
		this.detentionDate = detentionDate;
	}

	/**
	 * @return the detentionTime
	 */
	public Date getDetentionTime() {
		return detentionTime;
	}

	/**
	 * @param detentionTime the detentionTime to set
	 */
	public void setDetentionTime(Date detentionTime) {
		this.detentionTime = detentionTime;
	}

	/**
	 * @return the retentionDate
	 */
	public Date getRetentionDate() {
		return retentionDate;
	}

	/**
	 * @param retentionDate the retentionDate to set
	 */
	public void setRetentionDate(Date retentionDate) {
		this.retentionDate = retentionDate;
	}

	/**
	 * @return the retentionTime
	 */
	public Date getRetentionTime() {
		return retentionTime;
	}

	/**
	 * @param retentionTime the retentionTime to set
	 */
	public void setRetentionTime(Date retentionTime) {
		this.retentionTime = retentionTime;
	}

	/**
	 * @return the partDate
	 */
	public Date getPartDate() {
		return partDate;
	}

	/**
	 * @param partDate the partDate to set
	 */
	public void setPartDate(Date partDate) {
		this.partDate = partDate;
	}

	/**
	 * @return the partTime
	 */
	public Date getPartTime() {
		return partTime;
	}

	/**
	 * @param partTime the partTime to set
	 */
	public void setPartTime(Date partTime) {
		this.partTime = partTime;
	}

	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the circumstances
	 */
	public String getCircumstances() {
		return circumstances;
	}

	/**
	 * @param circumstances the circumstances to set
	 */
	public void setCircumstances(String circumstances) {
		this.circumstances = circumstances;
	}

	/**
	 * @return the obs
	 */
	public String getObs() {
		return obs;
	}

	/**
	 * @param obs the obs to set
	 */
	public void setObs(String obs) {
		this.obs = obs;
	}

	/**
	 * @return the documents
	 */
	public String getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(String documents) {
		this.documents = documents;
	}

	/**
	 * @return the implicateds
	 */
	public List<Implicated> getImplicateds() {
		return implicateds;
	}

	/**
	 * @param implicateds the implicateds to set
	 */
	public void setImplicateds(List<Implicated> implicateds) {
		this.implicateds = implicateds;
	}

	public void add(Implicated implicated) {
		if (!implicateds.contains(implicated)) {
			implicateds.add(implicated);
			implicated.setBallotPart(this);
		}
	}
	
	public void remove(Implicated implicated) {
		if (implicateds.contains(implicated)) {
			implicateds.remove(implicated);
			implicated.setBallotPart(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((agentName == null) ? 0 : agentName.hashCode());
		result = prime * result
				+ ((ballotType == null) ? 0 : ballotType.hashCode());
		result = prime * result
				+ ((circumstances == null) ? 0 : circumstances.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((detachment == null) ? 0 : detachment.hashCode());
		result = prime * result
				+ ((detentionDate == null) ? 0 : detentionDate.hashCode());
		result = prime * result
				+ ((detentionTime == null) ? 0 : detentionTime.hashCode());
		result = prime * result
				+ ((documents == null) ? 0 : documents.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((notificationDate == null) ? 0 : notificationDate.hashCode());
		result = prime
				* result
				+ ((notificationTime == null) ? 0 : notificationTime.hashCode());
		result = prime * result + ((obs == null) ? 0 : obs.hashCode());
		result = prime * result
				+ ((partDate == null) ? 0 : partDate.hashCode());
		result = prime * result
				+ ((partNumber == null) ? 0 : partNumber.hashCode());
		result = prime * result
				+ ((partTime == null) ? 0 : partTime.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result
				+ ((prevention == null) ? 0 : prevention.hashCode());
		result = prime * result
				+ ((retentionDate == null) ? 0 : retentionDate.hashCode());
		result = prime * result
				+ ((retentionTime == null) ? 0 : retentionTime.hashCode());
		result = prime * result
				+ ((tipology == null) ? 0 : tipology.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BallotPart other = (BallotPart) obj;
		if (agentName == null) {
			if (other.agentName != null)
				return false;
		} else if (!agentName.equals(other.agentName))
			return false;
		if (ballotType != other.ballotType)
			return false;
		if (circumstances == null) {
			if (other.circumstances != null)
				return false;
		} else if (!circumstances.equals(other.circumstances))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (detachment == null) {
			if (other.detachment != null)
				return false;
		} else if (!detachment.equals(other.detachment))
			return false;
		if (detentionDate == null) {
			if (other.detentionDate != null)
				return false;
		} else if (!detentionDate.equals(other.detentionDate))
			return false;
		if (detentionTime == null) {
			if (other.detentionTime != null)
				return false;
		} else if (!detentionTime.equals(other.detentionTime))
			return false;
		if (documents == null) {
			if (other.documents != null)
				return false;
		} else if (!documents.equals(other.documents))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (notificationDate == null) {
			if (other.notificationDate != null)
				return false;
		} else if (!notificationDate.equals(other.notificationDate))
			return false;
		if (notificationTime == null) {
			if (other.notificationTime != null)
				return false;
		} else if (!notificationTime.equals(other.notificationTime))
			return false;
		if (obs == null) {
			if (other.obs != null)
				return false;
		} else if (!obs.equals(other.obs))
			return false;
		if (partDate == null) {
			if (other.partDate != null)
				return false;
		} else if (!partDate.equals(other.partDate))
			return false;
		if (partNumber == null) {
			if (other.partNumber != null)
				return false;
		} else if (!partNumber.equals(other.partNumber))
			return false;
		if (partTime == null) {
			if (other.partTime != null)
				return false;
		} else if (!partTime.equals(other.partTime))
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		if (prevention == null) {
			if (other.prevention != null)
				return false;
		} else if (!prevention.equals(other.prevention))
			return false;
		if (retentionDate == null) {
			if (other.retentionDate != null)
				return false;
		} else if (!retentionDate.equals(other.retentionDate))
			return false;
		if (retentionTime == null) {
			if (other.retentionTime != null)
				return false;
		} else if (!retentionTime.equals(other.retentionTime))
			return false;
		if (tipology == null) {
			if (other.tipology != null)
				return false;
		} else if (!tipology.equals(other.tipology))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BallotPart [id=" + id + ", ballotType=" + ballotType
				+ ", partNumber=" + partNumber + ", tipology=" + tipology
				+ ", place=" + place + ", prevention=" + prevention
				+ ", detachment=" + detachment + ", notificationDate="
				+ notificationDate + ", notificationTime=" + notificationTime
				+ ", detentionDate=" + detentionDate + ", detentionTime="
				+ detentionTime + ", retentionDate=" + retentionDate
				+ ", retentionTime=" + retentionTime + ", partDate=" + partDate
				+ ", partTime=" + partTime + ", agentName=" + agentName
				+ ", code=" + code + ", circumstances=" + circumstances
				+ ", obs=" + obs + ", documents=" + documents + "]";
	}

}
