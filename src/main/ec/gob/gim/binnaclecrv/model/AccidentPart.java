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
 * Bitácora Digital: datos del Parte por Accidente.
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 08-Jun-2021
 */
@Audited
@Entity
@TableGenerator(name = "AccidentPartGenerator", 
    table = "IdentityGenerator", 
    pkColumnName = "name", 
    valueColumnName = "value", 
    pkColumnValue = "AccidentPart", 
    initialValue = 1, allocationSize = 1)

public class AccidentPart implements Serializable {

    private static final long serialVersionUID = 3342067669514478439L;

    @Id
    @GeneratedValue(generator = "AccidentPartGenerator", strategy = GenerationType.TABLE)
    private Long id; 
    
    private String partNumber; // Número de Parte
    private String agentName; // Nombre de Agente
    private String code; // Código
    private String tipology; // Tipología
    private String place; // Lugar
    private String referencePoint; // Punto de referencia
    private String latitude; // Latitud
    private String longitude; // Longitud
	@Temporal(TemporalType.DATE)
    private Date accidentDate; // Fecha del Accidente
	@Temporal(TemporalType.TIME)
    private Date accidentTime; // Hora del Accidente
	@Temporal(TemporalType.DATE)
    private Date partDate; // Fecha del Parte
	@Temporal(TemporalType.TIME)
    private Date partTime; // Hora del Parte
	@Temporal(TemporalType.DATE)
    private Date notificationDate; // Fecha del Aviso
	@Temporal(TemporalType.TIME)
    private Date notificationTime; // Hora del Aviso
	@Temporal(TemporalType.TIME)
    private Date arrivalTime; // Hora de Llegada a la Escena
	@Temporal(TemporalType.DATE)
    private Date detentionDate; // Fecha de Detención
	@Temporal(TemporalType.TIME)
    private Date detentionTime; // Hora de Detención
    @Enumerated(EnumType.STRING)
    @Column(length=15)
    private String road; // Calzada
    @Enumerated(EnumType.STRING)
    @Column(length=15)
    private String semaphores; // Semáforos
    @Enumerated(EnumType.STRING)
    @Column(length=15)
    private String sky; // Cielo
    private String circumstances; // Circunstancias
    private String obs; // Observaciones
    private String documents; // Documentos
    
    @OneToMany(mappedBy = "accidentPart", cascade = CascadeType.ALL)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Implicated> implicateds; //Implicados en un accidente
    
    public AccidentPart() {
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
	 * @return the referencePoint
	 */
	public String getReferencePoint() {
		return referencePoint;
	}

	/**
	 * @param referencePoint the referencePoint to set
	 */
	public void setReferencePoint(String referencePoint) {
		this.referencePoint = referencePoint;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the accidentDate
	 */
	public Date getAccidentDate() {
		return accidentDate;
	}

	/**
	 * @param accidentDate the accidentDate to set
	 */
	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}

	/**
	 * @return the accidentTime
	 */
	public Date getAccidentTime() {
		return accidentTime;
	}

	/**
	 * @param accidentTime the accidentTime to set
	 */
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
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
	 * @return the arrivalTime
	 */
	public Date getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param arrivalTime the arrivalTime to set
	 */
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
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
	 * @return the road
	 */
	public String getRoad() {
		return road;
	}

	/**
	 * @param road the road to set
	 */
	public void setRoad(String road) {
		this.road = road;
	}

	/**
	 * @return the semaphores
	 */
	public String getSemaphores() {
		return semaphores;
	}

	/**
	 * @param semaphores the semaphores to set
	 */
	public void setSemaphores(String semaphores) {
		this.semaphores = semaphores;
	}

	/**
	 * @return the sky
	 */
	public String getSky() {
		return sky;
	}

	/**
	 * @param sky the sky to set
	 */
	public void setSky(String sky) {
		this.sky = sky;
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
			implicated.setAccidentPart(this);
		}
	}
	
	public void remove(Implicated implicated) {
		if (implicateds.contains(implicated)) {
			implicateds.remove(implicated);
			implicated.setAccidentPart(null);
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
				+ ((accidentDate == null) ? 0 : accidentDate.hashCode());
		result = prime * result
				+ ((accidentTime == null) ? 0 : accidentTime.hashCode());
		result = prime * result
				+ ((agentName == null) ? 0 : agentName.hashCode());
		result = prime * result
				+ ((circumstances == null) ? 0 : circumstances.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((detentionDate == null) ? 0 : detentionDate.hashCode());
		result = prime * result
				+ ((detentionTime == null) ? 0 : detentionTime.hashCode());
		result = prime * result
				+ ((documents == null) ? 0 : documents.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
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
				+ ((referencePoint == null) ? 0 : referencePoint.hashCode());
		result = prime * result + ((road == null) ? 0 : road.hashCode());
		result = prime * result
				+ ((semaphores == null) ? 0 : semaphores.hashCode());
		result = prime * result + ((sky == null) ? 0 : sky.hashCode());
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
		AccidentPart other = (AccidentPart) obj;
		if (accidentDate == null) {
			if (other.accidentDate != null)
				return false;
		} else if (!accidentDate.equals(other.accidentDate))
			return false;
		if (accidentTime == null) {
			if (other.accidentTime != null)
				return false;
		} else if (!accidentTime.equals(other.accidentTime))
			return false;
		if (agentName == null) {
			if (other.agentName != null)
				return false;
		} else if (!agentName.equals(other.agentName))
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
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
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
		if (referencePoint == null) {
			if (other.referencePoint != null)
				return false;
		} else if (!referencePoint.equals(other.referencePoint))
			return false;
		if (road == null) {
			if (other.road != null)
				return false;
		} else if (!road.equals(other.road))
			return false;
		if (semaphores == null) {
			if (other.semaphores != null)
				return false;
		} else if (!semaphores.equals(other.semaphores))
			return false;
		if (sky == null) {
			if (other.sky != null)
				return false;
		} else if (!sky.equals(other.sky))
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
		return "AccidentPart [id=" + id + ", partNumber=" + partNumber
				+ ", agentName=" + agentName + ", code=" + code + ", tipology="
				+ tipology + ", place=" + place + ", referencePoint="
				+ referencePoint + ", latitude=" + latitude + ", longitude="
				+ longitude + ", accidentDate=" + accidentDate
				+ ", accidentTime=" + accidentTime + ", partDate=" + partDate
				+ ", partTime=" + partTime + ", notificationDate="
				+ notificationDate + ", notificationTime=" + notificationTime
				+ ", detentionDate=" + detentionDate + ", detentionTime="
				+ detentionTime + ", road=" + road + ", semaphores="
				+ semaphores + ", sky=" + sky + ", circumstances="
				+ circumstances + ", obs=" + obs + ", documents=" + documents
				+ "]";
	}
    
}
