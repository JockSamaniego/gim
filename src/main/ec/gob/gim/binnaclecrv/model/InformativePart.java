package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * Bitácora Digital: datos del Parte Informativo
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 31-May-2021
 */
@Audited
@Entity
@TableGenerator(name = "InformativePartGenerator", 
    table = "IdentityGenerator", 
    pkColumnName = "name", 
    valueColumnName = "value", 
    pkColumnValue = "InformativePart", 
    initialValue = 1, allocationSize = 1)

public class InformativePart implements Serializable {

	private static final long serialVersionUID = 4657397264624650535L;

	@Id
	@GeneratedValue(generator = "InformativePartGenerator", strategy = GenerationType.TABLE)
	private Long id; 
    
	@Column(length = 20)
	private String partNumber; //Número de Parte
	@Column(length = 100)
	private String agentName; //Nombre de Agente
	@Column(length = 10)
	private String code; //Código
	@Column(length = 100)
	private String cause; //Causa
	@Column(length = 100)
	private String place; //Lugar
	@Column(columnDefinition = "TEXT")
	private String circumstances; //Circunstancias
	@Temporal(TemporalType.DATE)
	private Date partDate; //Fecha del parte
	@Temporal(TemporalType.TIME)
	private Date partTime; //Hora del Parte
	@Temporal(TemporalType.DATE)
	private Date retentionDate; //Fecha de retención
	@Temporal(TemporalType.TIME)
	private Date retentionTime; //Hora de retención
    
	public InformativePart() {

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
	 * @return the cause
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * @param cause the cause to set
	 */
	public void setCause(String cause) {
		this.cause = cause;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((agentName == null) ? 0 : agentName.hashCode());
		result = prime * result + ((cause == null) ? 0 : cause.hashCode());
		result = prime * result
				+ ((circumstances == null) ? 0 : circumstances.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((partDate == null) ? 0 : partDate.hashCode());
		result = prime * result
				+ ((partTime == null) ? 0 : partTime.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result
				+ ((retentionDate == null) ? 0 : retentionDate.hashCode());
		result = prime * result
				+ ((retentionTime == null) ? 0 : retentionTime.hashCode());
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
		InformativePart other = (InformativePart) obj;
		if (partNumber == null) {
			if (other.partNumber != null)
				return false;
		} else if (!partNumber.equals(other.partNumber))
			return false;
		if (agentName == null) {
			if (other.agentName != null)
				return false;
		} else if (!agentName.equals(other.agentName))
			return false;
		if (cause == null) {
			if (other.cause != null)
				return false;
		} else if (!cause.equals(other.cause))
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (partDate == null) {
			if (other.partDate != null)
				return false;
		} else if (!partDate.equals(other.partDate))
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
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InformativePart [id=" + id + ", partNumber=" + partNumber 
				+ ", agentName=" + agentName
				+ ", code=" + code + ", cause=" + cause + ", place=" + place
				+ ", circumstances=" + circumstances + ", partDate=" + partDate
				+ ", partTime=" + partTime + ", retentionDate=" + retentionDate
				+ ", retentionTime=" + retentionTime  + "]";
	}

}