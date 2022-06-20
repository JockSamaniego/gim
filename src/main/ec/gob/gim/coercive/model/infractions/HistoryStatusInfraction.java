/**
 * 
 */
package ec.gob.gim.coercive.model.infractions;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.security.model.User;

/**
 * @author René
 * @date 2022-06-20
 */
@Audited
@Entity
@TableGenerator(name = "HistoryStatusInfractionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "HistoryStatusInfraction", initialValue = 1, allocationSize = 1)
@Table(name = "historystatusinfraction", schema = "infracciones")
public class HistoryStatusInfraction {

	@Id
	@GeneratedValue(generator = "HistoryStatusInfractionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private Date date;

	@ManyToOne
	@JoinColumn(name="status_id")
	private ItemCatalog status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String observation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "infraction_id")
	private Datainfraction infraction;
	
	@Version
	private Long version = 0L;

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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the status
	 */
	public ItemCatalog getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(ItemCatalog status) {
		this.status = status;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param observation
	 *            the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the infraction
	 */
	public Datainfraction getInfraction() {
		return infraction;
	}

	/**
	 * @param infraction
	 *            the infraction to set
	 */
	public void setInfraction(Datainfraction infraction) {
		this.infraction = infraction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HistoryStatusInfraction [id=" + id + ", date=" + date
				+ ", status=" + status + ", user=" + user + ", observation="
				+ observation + ", infraction=" + infraction + "]";
	}

}
