package ec.gob.gim.coercive.model.infractions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;

@Audited
@Entity
@TableGenerator(name = "NotificationInfractionGenerator", table = "IdentityGenerator", 
				pkColumnName = "name", valueColumnName = "value", 
				pkColumnValue = "NotificationInfraction", 
				initialValue = 1, allocationSize = 1)
@Table(name = "notificationinfraction", schema = "infracciones", uniqueConstraints={ @UniqueConstraint(columnNames={"year","number"})})

public class NotificationInfractions {

	@Id
	@GeneratedValue(generator = "NotificationInfractionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	/**
	 * Detalle de <tt>MunicipalBond</tt> afectados por esta
	 * <tt>Notification</tt>
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "notification_id")
	@OrderBy("id asc")
	private List<Datainfraction> infractions;


	@Temporal(TemporalType.DATE)
	private Date creationTimeStamp;

	@Column
	private int year;

	@Column
	private int number;

	public NotificationInfractions() {
		infractions = new ArrayList<Datainfraction>(); 
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
  
	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	public List<Datainfraction> getInfractions() {
		return infractions;
	}

	public void setInfractions(List<Datainfraction> infractions) {
		this.infractions = infractions;
	}

	@Transient
	public BigDecimal getTotal() {
		BigDecimal sum = new BigDecimal(0);
		for (Datainfraction di : this.infractions) {
				sum = sum.add(di.getTotalValue()); 
		}
		return sum;
	}

	@Transient
	public String getYearNumber() {
		return String.valueOf(this.getYear()) + "-"
				+ String.valueOf(this.getNumber());
	}

	public void add(Datainfraction infraction) {
		if (!infractions.contains(infraction) && infraction != null) {
			infractions.add(infraction);
			//infraction.setNotification(this);
		}
	}

	public void remove(Datainfraction infraction) {
		if (infractions.contains(infraction)) {
			infractions.remove(infraction);
			//infraction.setNotification(null);
		}
	}

}
