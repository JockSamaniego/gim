package ec.gob.gim.revenue.model.impugnment;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.security.model.User;

/**
 * @author rene
 * @date 2016-07-13
 * @summary Clase para Impugnaciones
 */
@Audited
@Entity
@TableGenerator(name = "ImpugnmentGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Impugnment", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "Impugnment.findByCriteria", query = "select i from Impugnment i "
																+ "join fetch i.municipalBond mb "
																+ "join fetch mb.resident res "
																+ "join fetch i.status sta "
																+ "where (:numberInfringement='' OR i.numberInfringement=:numberInfringement) "
																+ "AND (:numberProsecution = 0 OR i.numberProsecution=:numberProsecution) "
																+ "AND (:identificationNumber = '' OR res.identificationNumber=:identificationNumber) "
																+ "AND (:statusId = 0 OR sta.id=:statusId) "
																+ "ORDER BY i.id DESC"),
		@NamedQuery(name = "Impugnment.findById", query = "select i from Impugnment i join fetch i.municipalBond mb join fetch mb.resident res where i.id=:impugnmentId"),
		@NamedQuery(name = "Impugnment.findByMunicipalBond", query = "select i from Impugnment i where (i.municipalBond.id =:municipalBond_id and i.status.code =:code)") })
public class Impugnment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ImpugnmentGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	
	private String numberProsecution;

	private String numberInfringement;

	private String observation;

	private String numberTramit;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_itm_id", nullable = false, referencedColumnName = "id")
	private ItemCatalog type;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status_itm_id", nullable = false, referencedColumnName = "id")
	private ItemCatalog status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "municipalBond_id", nullable = false, referencedColumnName = "id")
	private MunicipalBond municipalBond;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userRegister_id", nullable = false, referencedColumnName = "id")
	private User userRegister;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userUpdate_id", nullable = true, referencedColumnName = "id")
	private User userUpdate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Version
	private Long version = 0L;

	public Impugnment() {
		//2016-09-18 rfarmijosm se esta guardando null en la bd
		this.creationDate = new Date();
		this.userUpdate = null;
		this.updateDate = null;
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

	public String getNumberProsecution() {
		return numberProsecution;
	}

	public void setNumberProsecution(String numberProsecution) {
		this.numberProsecution = numberProsecution;
	}

	public String getNumberInfringement() {
		return numberInfringement;
	}

	public void setNumberInfringement(String numberInfringement) {
		this.numberInfringement = numberInfringement;
	}

	public ItemCatalog getType() {
		return type;
	}

	public void setType(ItemCatalog type) {
		this.type = type;
	}

	public ItemCatalog getStatus() {
		return status;
	}

	public void setStatus(ItemCatalog status) {
		this.status = status;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public User getUserRegister() {
		return userRegister;
	}

	public void setUserRegister(User userRegister) {
		this.userRegister = userRegister;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public User getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(User userUpdate) {
		this.userUpdate = userUpdate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getNumberTramit() {
		return numberTramit;
	}

	public void setNumberTramit(String numberTramit) {
		this.numberTramit = numberTramit;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Impugnment)) {
			return false;
		}
		Impugnment other = (Impugnment) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		if (id == null) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Impugnment [id=" + id + ", creationDate=" + creationDate
				+ ", numberProsecution="
				+ numberProsecution + ", numberInfringement="
				+ numberInfringement + ", type=" + type + ", status=" + status
				+ ", municipalBond=" + municipalBond + ", userRegister="
				+ userRegister + ", version=" + version + "]";
	}

}