package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * Bitácora Digital: datos del vehículo y persona implicada
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 08-Jun-2021
 */
@Audited
@Entity
@TableGenerator(name = "ImplicatedGenerator", 
    table = "IdentityGenerator", 
    pkColumnName = "name", 
    valueColumnName = "value", 
    pkColumnValue = "Implicated", 
    initialValue = 1, allocationSize = 1)

public class Implicated implements Serializable {

    private static final long serialVersionUID = 3440094890711222057L;

    @Id
    @GeneratedValue(generator = "ImplicatedGenerator", strategy = GenerationType.TABLE)
    private Long id;

	@Column(length = 20)
    private String licensePlate; // Placa/RAMV/CPN
	@Column(length = 200)
    private String Name; // Nombres
	@Column(length = 15) 
    private String identification; // Cédula RUC
	@Column(length = 20)
    private String licenseType; // L. Tipo
	@Column(length = 20)
    private String origen; // Origen
    private int age; // Edad
    private boolean alcoholicInfluence; // Influencia Alcohólica
    private boolean personArrested; // Detenido
	@Column(length = 200)
    private String resumeVehicle; // Tipo y placa
	@Column(length = 20)
    private String service; // Servicio
	@Column(length = 200)
    private String street; // Calle o Carretera
    private boolean vehicleArrested; // Veh. detenido
	@Column(columnDefinition = "TEXT")
    private String freeInterview; // Entrevista Libre y Voluntaria
    
    @ManyToOne
    AccidentPart accidentPart;

    @ManyToOne
    BallotPart ballotPart;

	public Implicated() {
        
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
	 * @return the licensePlate
	 */
	public String getLicensePlate() {
		return licensePlate;
	}

	/**
	 * @param licensePlate the licensePlate to set
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the identification
	 */
	public String getIdentification() {
		return identification;
	}

	/**
	 * @param identification the identification to set
	 */
	public void setIdentification(String identification) {
		this.identification = identification;
	}

	/**
	 * @return the licenseType
	 */
	public String getLicenseType() {
		return licenseType;
	}

	/**
	 * @param licenseType the licenseType to set
	 */
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	/**
	 * @return the origen
	 */
	public String getOrigen() {
		return origen;
	}

	/**
	 * @param origen the origen to set
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the alcoholicInfluence
	 */
	public boolean isAlcoholicInfluence() {
		return alcoholicInfluence;
	}

	/**
	 * @param alcoholicInfluence the alcoholicInfluence to set
	 */
	public void setAlcoholicInfluence(boolean alcoholicInfluence) {
		this.alcoholicInfluence = alcoholicInfluence;
	}

	/**
	 * @return the personArrested
	 */
	public boolean isPersonArrested() {
		return personArrested;
	}

	/**
	 * @param personArrested the personArrested to set
	 */
	public void setPersonArrested(boolean personArrested) {
		this.personArrested = personArrested;
	}

	/**
	 * @return the resumeVehicle
	 */
	public String getResumeVehicle() {
		return resumeVehicle;
	}

	/**
	 * @param resumeVehicle the resumeVehicle to set
	 */
	public void setResumeVehicle(String resumeVehicle) {
		this.resumeVehicle = resumeVehicle;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the vehicleArrested
	 */
	public boolean isVehicleArrested() {
		return vehicleArrested;
	}

	/**
	 * @param vehicleArrested the vehicleArrested to set
	 */
	public void setVehicleArrested(boolean vehicleArrested) {
		this.vehicleArrested = vehicleArrested;
	}

	/**
	 * @return the freeInterview
	 */
	public String getFreeInterview() {
		return freeInterview;
	}

	/**
	 * @param freeInterview the freeInterview to set
	 */
	public void setFreeInterview(String freeInterview) {
		this.freeInterview = freeInterview;
	}

	/**
	 * @return the accidentPart
	 */
	public AccidentPart getAccidentPart() {
		return accidentPart;
	}

	/**
	 * @param accidentPart the accidentPart to set
	 */
	public void setAccidentPart(AccidentPart accidentPart) {
		this.accidentPart = accidentPart;
	}

	/**
	 * @return the ballotPart
	 */
	public BallotPart getBallotPart() {
		return ballotPart;
	}

	/**
	 * @param ballotPart the ballotPart to set
	 */
	public void setBallotPart(BallotPart ballotPart) {
		this.ballotPart = ballotPart;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + age;
		result = prime * result + (alcoholicInfluence ? 1231 : 1237);
		result = prime * result
				+ ((freeInterview == null) ? 0 : freeInterview.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((identification == null) ? 0 : identification.hashCode());
		result = prime * result
				+ ((licensePlate == null) ? 0 : licensePlate.hashCode());
		result = prime * result
				+ ((licenseType == null) ? 0 : licenseType.hashCode());
		result = prime * result + ((origen == null) ? 0 : origen.hashCode());
		result = prime * result + (personArrested ? 1231 : 1237);
		result = prime * result
				+ ((resumeVehicle == null) ? 0 : resumeVehicle.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + (vehicleArrested ? 1231 : 1237);
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
		Implicated other = (Implicated) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		if (age != other.age)
			return false;
		if (alcoholicInfluence != other.alcoholicInfluence)
			return false;
		if (freeInterview == null) {
			if (other.freeInterview != null)
				return false;
		} else if (!freeInterview.equals(other.freeInterview))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (identification == null) {
			if (other.identification != null)
				return false;
		} else if (!identification.equals(other.identification))
			return false;
		if (licensePlate == null) {
			if (other.licensePlate != null)
				return false;
		} else if (!licensePlate.equals(other.licensePlate))
			return false;
		if (licenseType == null) {
			if (other.licenseType != null)
				return false;
		} else if (!licenseType.equals(other.licenseType))
			return false;
		if (origen == null) {
			if (other.origen != null)
				return false;
		} else if (!origen.equals(other.origen))
			return false;
		if (personArrested != other.personArrested)
			return false;
		if (resumeVehicle == null) {
			if (other.resumeVehicle != null)
				return false;
		} else if (!resumeVehicle.equals(other.resumeVehicle))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (vehicleArrested != other.vehicleArrested)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Implicated [id=" + id + ", licensePlate=" + licensePlate
				+ ", Name=" + Name + ", identification=" + identification
				+ ", licenseType=" + licenseType + ", origen=" + origen
				+ ", age=" + age + ", alcoholicInfluence=" + alcoholicInfluence
				+ ", personArrested=" + personArrested + ", resumeVehicle="
				+ resumeVehicle + ", service=" + service + ", street=" + street
				+ ", vehicleArrested=" + vehicleArrested + ", freeInterview="
				+ freeInterview + "]";
	} 
    
}
