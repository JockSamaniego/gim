package ec.gob.gim.propertyregister.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.security.model.User;

@Audited
@Entity
@TableGenerator(name = "PropertyRegisterItemGenerator", 
	table = "IdentityGenerator", 
	pkColumnName = "name", 
	valueColumnName = "value", 
	pkColumnValue = "PropertyRegisterItem", 
	initialValue = 1, allocationSize = 1)

public class PropertyRegisterItem {

	@Id
	@GeneratedValue(generator = "PropertyRegisterItemGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length=30)
	private String identificationNumber;
	
	@Column(length=50)
	private String calidad;
	
	@Column(length=200)
	private String intervenerName;
	
	@Column(length=200)
	private String obs;

	@ManyToOne
	private PropertyRegister propertyRegister;

	public PropertyRegisterItem() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	/**
	 * @return the calidad
	 */
	public String getCalidad() {
		return calidad;
	}

	/**
	 * @param calidad the calidad to set
	 */
	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}

	/**
	 * @return the intervenerName
	 */
	public String getIntervenerName() {
		return intervenerName;
	}

	/**
	 * @param intervenerName the intervenerName to set
	 */
	public void setIntervenerName(String intervenerName) {
		this.intervenerName = intervenerName;
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
	 * @return the propertyRegister
	 */
	public PropertyRegister getPropertyRegister() {
		return propertyRegister;
	}

	/**
	 * @param propertyRegister the propertyRegister to set
	 */
	public void setPropertyRegister(PropertyRegister propertyRegister) {
		this.propertyRegister = propertyRegister;
	}

}