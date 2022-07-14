/**
 * 
 */
package ec.gob.gim.coercive.model.infractions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.security.model.User;

/**
 * @author Ronald Paladines
 * @date 2022-06-30
 */
@Audited
@Entity
@TableGenerator(name = "InfringementAgreementGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "InfringementAgreement", initialValue = 1, allocationSize = 1)
@Table(name = "infringementAgreement", schema = "infracciones")
@NamedQueries(value = {
	@NamedQuery(name = "InfringementAgreement.findByIdentification", 
		query = "SELECT i FROM InfringementAgreement i " +
			"WHERE i.infractorIdentification = :infractorIdentification ")
	})

public class InfringementAgreement implements Serializable{

	private static final long serialVersionUID = -4200321422266450146L;

	@Id
	@GeneratedValue(generator = "InfringementAgreementGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private BigDecimal total;
	private BigDecimal initialFee;
	private int percentage;
	private int months;
	private String responsableName;
	private String responsableCharge;
	private String infractorIdentification;
	private String infractorName;
	private boolean active;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date systemDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date liquidationDate;
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="creator_id")
	private User creator;
	
	@OneToMany(mappedBy = "infringementAgreement", fetch = FetchType.EAGER)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(value = "id desc")
	private List<Datainfraction> infractions;
	
	/**
	 * 
	 */
	public InfringementAgreement() {
		super();
		this.date = new Date();
		active = true;
		infractions = new ArrayList<Datainfraction>();
		total = BigDecimal.ZERO;
		initialFee = BigDecimal.ZERO;
		percentage = 30;
		months = 1;
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
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the initialFee
	 */
	public BigDecimal getInitialFee() {
		return initialFee;
	}

	/**
	 * @param initialFee the initialFee to set
	 */
	public void setInitialFee(BigDecimal initialFee) {
		this.initialFee = initialFee;
	}

	/**
	 * @return the percentage
	 */
	public int getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the months
	 */
	public int getMonths() {
		return months;
	}

	/**
	 * @param months the months to set
	 */
	public void setMonths(int months) {
		this.months = months;
	}

	/**
	 * @return the responsableName
	 */
	public String getResponsableName() {
		return responsableName;
	}

	/**
	 * @param responsableName the responsableName to set
	 */
	public void setResponsableName(String responsableName) {
		this.responsableName = responsableName;
	}

	/**
	 * @return the responsableCharge
	 */
	public String getResponsableCharge() {
		return responsableCharge;
	}

	/**
	 * @param responsableCharge the responsableCharge to set
	 */
	public void setResponsableCharge(String responsableCharge) {
		this.responsableCharge = responsableCharge;
	}

	/**
	 * @return the infractorIdentification
	 */
	public String getInfractorIdentification() {
		return infractorIdentification;
	}

	/**
	 * @param infractorIdentification the infractorIdentification to set
	 */
	public void setInfractorIdentification(String infractorIdentification) {
		this.infractorIdentification = infractorIdentification;
	}

	/**
	 * @return the infractorName
	 */
	public String getInfractorName() {
		return infractorName;
	}

	/**
	 * @param infractorName the infractorName to set
	 */
	public void setInfractorName(String infractorName) {
		this.infractorName = infractorName;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the systemDate
	 */
	public Date getSystemDate() {
		return systemDate;
	}

	/**
	 * @param systemDate the systemDate to set
	 */
	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}

	/**
	 * @return the liquidationDate
	 */
	public Date getLiquidationDate() {
		return liquidationDate;
	}

	/**
	 * @param liquidationDate the liquidationDate to set
	 */
	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return the infractions
	 */
	public List<Datainfraction> getInfractions() {
		return infractions;
	}

	/**
	 * @param infractions the infractions to set
	 */
	public void setInfractions(List<Datainfraction> infractions) {
		this.infractions = infractions;
	}

	public void add(Datainfraction datainfraction){
		if (!this.infractions.contains(datainfraction)){
			this.infractions.add(datainfraction);
			datainfraction.setInfringementAgreement(this);
		}
	}
	
	public void remove(Datainfraction datainfraction){
		if (this.infractions.contains(datainfraction)){
			this.infractions.remove(datainfraction);
			datainfraction.setInfringementAgreement(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((infractorIdentification == null) ? 0
						: infractorIdentification.hashCode());
		result = prime * result
				+ ((infractorName == null) ? 0 : infractorName.hashCode());
		result = prime * result
				+ ((initialFee == null) ? 0 : initialFee.hashCode());
		result = prime * result + months;
		result = prime * result + percentage;
		result = prime
				* result
				+ ((responsableCharge == null) ? 0 : responsableCharge
						.hashCode());
		result = prime * result
				+ ((responsableName == null) ? 0 : responsableName.hashCode());
		result = prime * result
				+ ((systemDate == null) ? 0 : systemDate.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
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
		InfringementAgreement other = (InfringementAgreement) obj;
		if (active != other.active)
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (infractorIdentification == null) {
			if (other.infractorIdentification != null)
				return false;
		} else if (!infractorIdentification
				.equals(other.infractorIdentification))
			return false;
		if (infractorName == null) {
			if (other.infractorName != null)
				return false;
		} else if (!infractorName.equals(other.infractorName))
			return false;
		if (initialFee == null) {
			if (other.initialFee != null)
				return false;
		} else if (!initialFee.equals(other.initialFee))
			return false;
		if (months != other.months)
			return false;
		if (percentage != other.percentage)
			return false;
		if (responsableCharge == null) {
			if (other.responsableCharge != null)
				return false;
		} else if (!responsableCharge.equals(other.responsableCharge))
			return false;
		if (responsableName == null) {
			if (other.responsableName != null)
				return false;
		} else if (!responsableName.equals(other.responsableName))
			return false;
		if (systemDate == null) {
			if (other.systemDate != null)
				return false;
		} else if (!systemDate.equals(other.systemDate))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfringementAgreement [id=" + id + ", total=" + total
				+ ", initialFee=" + initialFee + ", percentage=" + percentage
				+ ", months=" + months + ", responsableName=" + responsableName
				+ ", responsableCharge=" + responsableCharge
				+ ", infractorIdentification=" + infractorIdentification
				+ ", infractorName=" + infractorName + ", active=" + active
				+ ", date=" + date + ", systemDate=" + systemDate
				+ ", creator=" + creator + "]";
	}

}
