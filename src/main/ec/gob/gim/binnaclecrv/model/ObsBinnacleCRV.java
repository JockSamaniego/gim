package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.security.model.User;

/**
 * Bitácora Digital: observaciones ingresadas para el ingreso o salida del vehículo retenido.
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 07-May-2021
 */
@Audited
@Entity
@TableGenerator(name = "ObsBinnacleCRVGenerator", 
    table = "IdentityGenerator", 
    pkColumnName = "name", 
    valueColumnName = "value", 
    pkColumnValue = "ObsBinnacleCRV", 
    initialValue = 1, allocationSize = 1)

public class ObsBinnacleCRV implements Serializable {

	private static final long serialVersionUID = 6053009686265706471L;

	@Id
	@GeneratedValue(generator = "ObsBinnacleCRVGenerator", strategy = GenerationType.TABLE)
	private Long id; 
    
	@Temporal(TemporalType.DATE)
	private Date obsDate; //Fecha de la Observacion
	@Temporal(TemporalType.TIME)
	private Date obsTime; //hora de la Observacion
	@Column(columnDefinition = "TEXT")
	private String obs; //Observaciones
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	private ObsTypeBinnacleCRV obsTypeBinnacleCRV;// Tipo de Observacion
    
	@ManyToOne
	private BinnacleCRV binnacleCRV;
	@ManyToOne
	private User user;

	public ObsBinnacleCRV() {

	}

	public ObsBinnacleCRV(ObsTypeBinnacleCRV obsTypeBinnacleCRV) {
		this.obsTypeBinnacleCRV = obsTypeBinnacleCRV;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the obsDate
	 */
	public Date getObsDate() {
		return obsDate;
	}

	/**
	 * @param obsDate the obsDate to set
	 */
	public void setObsDate(Date obsDate) {
		this.obsDate = obsDate;
	}

	public Date getObsTime() {
		return obsTime;
	}

	public void setObsTime(Date obsTime) {
		this.obsTime = obsTime;
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
	 * @return the obsTypeBinnacleCRV
	 */
	public ObsTypeBinnacleCRV getObsTypeBinnacleCRV() {
		return obsTypeBinnacleCRV;
	}

	/**
	 * @param obsTypeBinnacleCRV the obsTypeBinnacleCRV to set
	 */
	public void setObsTypeBinnacleCRV(ObsTypeBinnacleCRV obsTypeBinnacleCRV) {
		this.obsTypeBinnacleCRV = obsTypeBinnacleCRV;
	}

	/**
	 * @return the binnacleCRV
	 */
	public BinnacleCRV getBinnacleCRV() {
		return binnacleCRV;
	}

	/**
	 * @param binnacleCRV the binnacleCRV to set
	 */
	public void setBinnacleCRV(BinnacleCRV binnacleCRV) {
		this.binnacleCRV = binnacleCRV;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((obs == null) ? 0 : obs.hashCode());
		result = prime * result + ((obsDate == null) ? 0 : obsDate.hashCode());
		result = prime * result + ((obsTypeBinnacleCRV == null) ? 0 : obsTypeBinnacleCRV.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		ObsBinnacleCRV other = (ObsBinnacleCRV) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (obs == null) {
			if (other.obs != null)
				return false;
		} else if (!obs.equals(other.obs))
			return false;
		if (obsDate == null) {
			if (other.obsDate != null)
				return false;
		} else if (!obsDate.equals(other.obsDate))
			return false;
		if (obsTypeBinnacleCRV != other.obsTypeBinnacleCRV)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ObsBinnacleCRV [id=" + id + ", obsDate=" + obsDate + ", obs=" + obs + "]";
	}

}
