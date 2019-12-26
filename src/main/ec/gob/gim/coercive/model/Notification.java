package ec.gob.gim.coercive.model;

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

/**
 * Negocio: Notificación de vencimiento de obligación
 * 
 * @author jlgranda
 * @version 1.0
 * @created 23-Nov-2011 11:57:27
 */
@Audited
@Entity
@TableGenerator(name = "NotificationGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Notification", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "Notification.countCreatedBetweenDates", query = "SELECT count(n.id) FROM Notification n "
				+ "WHERE cast(n.creationTimeStamp as date) BETWEEN :startDate AND :endDate"),
		@NamedQuery(name = "Notification.sumBondValueBetweenDates", query = "SELECT sum(mb.value) FROM Notification n "
				+ "JOIN n.municipalBonds mb "
				+ "WHERE cast(n.creationTimeStamp as date) BETWEEN :startDate AND :endDate"),
		@NamedQuery(name = "Notification.sumBondPaidTotalBetweenDatesAndBondStatus", query = "SELECT sum(mb.paidTotal) FROM Notification n "
				+ "JOIN n.municipalBonds mb "
				+ "WHERE cast(n.creationTimeStamp as date) BETWEEN :startDate AND :endDate "
				+ "AND mb.municipalBondStatus.id = :statusId"),
		@NamedQuery(name = "Notification.actualNumber", query = "SELECT max(n.number) FROM Notification n "
				+ "WHERE n.year = :year") 
})
@Table(uniqueConstraints={ @UniqueConstraint(columnNames={"year","number"})})
public class Notification {

	@Id
	@GeneratedValue(generator = "NotificationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "notification_id")
	private List<NotificationTask> notificationTasks;

	/**
	 * <tt>Person</tt> sujeta de notificación, podría ser distinta al
	 * <tt>Resident</tt> implicado en el <tt>MunicipalBond</tt>
	 */
	@ManyToOne
	private Resident notified;

	/**
	 * Detalle de <tt>MunicipalBond</tt> afectados por esta
	 * <tt>Notification</tt>
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "notification_id")
	@OrderBy("id asc")
	private List<MunicipalBond> municipalBonds;

	/**
	 * Grupo de <tt>Entry</tt> al que correcponde la notificación
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Entry entry;

	@Temporal(TemporalType.DATE)
	private Date creationTimeStamp;

	@Column
	private int year;

	@Column
	private int number;

	public Notification() {
		municipalBonds = new ArrayList<MunicipalBond>();
		notificationTasks = new ArrayList<NotificationTask>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<NotificationTask> getNotificationTasks() {
		return notificationTasks;
	}

	public void setNotificationTasks(List<NotificationTask> notificationTasks) {
		this.notificationTasks = notificationTasks;
	}

	public Resident getNotified() {
		return notified;
	}

	public void setNotified(Resident notified) {
		this.notified = notified;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
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

	public void add(NotificationTask notificationTask) {
		if (!notificationTasks.contains(notificationTask)
				&& notificationTask != null) {
			notificationTasks.add(notificationTask);
		}
	}

	public void remove(NotificationTask notificationTask) {
		if (notificationTasks.contains(notificationTask)) {
			notificationTasks.remove(notificationTask);
		}
	}

	@Transient
	public BigDecimal getTotal() {
		BigDecimal sum = new BigDecimal(0);
		//listado de status payed
		//macartuche	
		ArrayList<Long> status = new ArrayList<Long>();
		status.add(6L);
		status.add(11L);
		
		for (MunicipalBond mb : this.getMunicipalBonds()) {
			//macartuche 2019-11-27
			//solo tomar en consideracion las pendientes/convenio
			if( !status.contains(mb.getMunicipalBondStatus().getId())) {
				sum = sum.add(mb.getValue()); 
			}
			
		}
		return sum;
	}

	@Transient
	public String getYearNumber() {
		return String.valueOf(this.getYear()) + "-"
				+ String.valueOf(this.getNumber());
	}

	public void add(MunicipalBond m) {
		if (!municipalBonds.contains(m) && m != null) {
			municipalBonds.add(m);
			m.setNotification(this);
		}
	}

	public void remove(MunicipalBond m) {
		if (municipalBonds.contains(m)) {
			municipalBonds.remove(m);
			m.setNotification(null);
		}
	}

}
