package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * Bitácora Digital: evidencia fotográfica, solamente admite dos fotografías con un máximo de 1MB cada una.
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 07-May-2021
 */
@Audited
@Entity
@TableGenerator(name = "PhotographicEvidenceGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "PhotographicEvidence", initialValue = 1, allocationSize = 1)
public class PhotographicEvidence implements Serializable {

	private static final long serialVersionUID = 6708594565109445983L;

	@Id
	@GeneratedValue(generator = "PhotographicEvidenceGenerator", strategy = GenerationType.TABLE)
	private Long id; 
    
	@Column(length = 80)
	private String name; //Nombre
	@Column(length = 2147483647)
	@Basic(fetch = FetchType.LAZY)
	private byte[] photo;
	
	@ManyToOne
	private BinnacleCRV binnacleCRV;

	public PhotographicEvidence() {

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the photo
	 */
	public byte[] getPhoto() {
		return photo;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(byte[] photo) {
		this.photo = photo;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(photo);
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
		PhotographicEvidence other = (PhotographicEvidence) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(photo, other.photo))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PhotographicEvidence [id=" + id + ", name=" + name + ", photo=" + Arrays.toString(photo) + "]";
	}

}
