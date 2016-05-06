package ec.gob.gim.coercive.model;

import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;

/**
 * Negocio: Tarea de Notificación de vencimiento de obligación
 * @author jlgranda
 * @version 1.0
 * @created 23-Nov-2011 12:48:27
 */
@Audited
@Entity
@TableGenerator(
	 name="NotificationTaskGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="NotificationTask",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
                @NamedQuery(name = "Notification.countCreatedBetweenDatesAndWithoutNotifitifier", 
				query =   "SELECT count(nt.id), pf.name, pf.identificationNumber "
                                        + "FROM NotificationTask nt "
                                        + "JOIN nt.notifier pf "                                         
					+ "WHERE cast(nt.creationDate as date) BETWEEN :startDate AND :endDate "                                         
                                        + "GROUP BY pf.name, pf.identificationNumber"),
                @NamedQuery(name = "Notification.countCreatedBetweenDatesAndNotifitifier", 
				query =   "SELECT count(nt.id), pf.name, pf.identificationNumber "
                                        + "FROM NotificationTask nt "
                                        + "JOIN nt.notifier pf "                                         
					+ "WHERE nt.notifier.id = :notifierV AND " 
                                        + "cast(nt.creationDate as date) BETWEEN :startDate AND :endDate "
                                        + "GROUP BY pf.name, pf.identificationNumber"),
		@NamedQuery(name = "Notificationtask.countForTypeBetweenDates", 
				query =   "SELECT count(nt.id) FROM NotificationTask nt " 
					+ "WHERE nt.creationDate BETWEEN :startDate AND :endDate and nt.notificationTaskType.id = :typeId")
})

public class NotificationTask {

	@Id
	@GeneratedValue(generator="NotificationTaskGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column
	private String razon;

	/**
	 * <tt>Person</tt> que impulsa la notificación
	 */
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="actuary_id")
	private Person actuary;
	
	/**
	 * <tt>Person</tt> que notifica
	 */
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="notifier_id")
	private Person notifier;
	
	/**
	 * <tt>Person</tt> que inicia juicio
	 */
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="judgmental_id")
	private Person judgmental;
	
	@Column
	private String numberJudgment;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@Temporal(TemporalType.DATE)
	private Date notifiedDate;
	
	@OneToOne
	private NotificationTaskType notificationTaskType; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazon() {
		return razon;
	}

	public void setRazon(String razon) {
		this.razon = razon;
	}

	public Person getActuary() {
		return actuary;
	}

	public void setActuary(Person actuary) {
		this.actuary = actuary;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getNotifiedDate() {
		return notifiedDate;
	}

	public void setNotifiedDate(Date notifiedDate) {
		this.notifiedDate = notifiedDate;
	}

	public void setNotificationTaskType(NotificationTaskType notificationTaskType) {
		this.notificationTaskType = notificationTaskType;
	}

	public NotificationTaskType getNotificationTaskType() {
		return notificationTaskType;
	}
	
	
	/**
	 * @return the notifier
	 */
	public Person getNotifier() {
		return notifier;
	}

	/**
	 * @param notifier the notifier to set
	 */
	public void setNotifier(Person notifier) {
		this.notifier = notifier;
	}

	/**
	 * @return the judgmental
	 */
	public Person getJudgmental() {
		return judgmental;
	}

	/**
	 * @param judgmental the judgmental to set
	 */
	public void setJudgmental(Person judgmental) {
		this.judgmental = judgmental;
	}

	/**
	 * @return the numberJudgment
	 */
	public String getNumberJudgment() {
		return numberJudgment;
	}

	/**
	 * @param numberJudgment the numberJudgment to set
	 */
	public void setNumberJudgment(String numberJudgment) {
		this.numberJudgment = numberJudgment;
	}

	@Transient
	public boolean isEditable(){
		return this.getNotificationTaskType().getSequence() != NotificationTaskType.FIRST;
	}
	
}
