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
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.security.model.User;

/**
 * @author René
 * @date 2022-06-20
 */
@Audited
@Entity
@TableGenerator(name = "HistoryStatusNotificationGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "HistoryStatusInfraction", initialValue = 1, allocationSize = 1)
@Table(name = "historystatusnotification", schema = "infracciones")
public class HistoryStatusNotification {

	@Id
	@GeneratedValue(generator = "HistoryStatusNotificationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private Date date;

	@ManyToOne
	@JoinColumn(name="status_id")
	private ItemCatalog status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "responsible_id")
	private Resident responsible;

	private String observation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notification_id")
	private NotificationInfractions notification;
	
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
	 * @return the responsible
	 */
	public Resident getResponsible() {
		return responsible;
	}

	/**
	 * @param responsible the responsible to set
	 */
	public void setResponsible(Resident responsible) {
		this.responsible = responsible;
	}

	/**
	 * @return the notification
	 */
	public NotificationInfractions getNotification() {
		return notification;
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(NotificationInfractions notification) {
		this.notification = notification;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HistoryStatusNotification [id=" + id + ", date=" + date
				+ ", status=" + status + ", user=" + user + ", responsible="
				+ responsible + ", observation=" + observation
				+ ", notification=" + notification + ", version=" + version
				+ "]";
	}

}
