package ec.gob.gim.waterservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

/**
 * @author richard
 * @version 1.0
 * @created 01-junio-2016 21:22:44 en esta clase se registran desde su
 *          implementacion los bloqueos de agua potable asi como tambien la
 *          re-emision de cobros indebidos
 */
@Audited
@Entity
@TableGenerator(name = "WaterBlockLogGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WaterBlockLog", initialValue = 1, allocationSize = 1)
public class WaterBlockLog {

	@Id
	@GeneratedValue(generator = "WaterBlockLogGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	/**
	 * razon de bloqueo y solicitud de baja
	 */
	@Column(length = 30)
	private String blockDetail;

	/**
	 * identifcar si es re-emision o un bloqueo, en el bloqueo es necesario
	 * solicitar el detalle de bloqueo
	 */
	private Boolean isReEmision;

	/**
	 * numero de tramite para un bloqueo
	 */
	@Column(length = 20)
	private String tramitNumber;

	/**
	 * la obligacion a dar de baja
	 */
	private Integer unsubscribeBond;
	
	/**
	 * no tiene clave foranea, solo guarda el id de la orden
	 */
	private Long emissionOrder_id;
	
	/**
	 * servicio a darse de baja y emitir
	 */
	private Integer serviceNumber;

	/**
	 * contribuyente al que se le emite nuevamente
	 */
	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;
	
	
	/**
	 * responsable de la lectura
	 */
	@ManyToOne
	@JoinColumn(name = "attendant_id")
	private Resident attendant;

	/**
	 * usuario que procede con la re-emision
	 */
	@ManyToOne
	@JoinColumn(name = "blocker_id")
	private Resident blocker;

	public WaterBlockLog() {
		this.isReEmision = Boolean.TRUE;
		this.date = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBlockDetail() {
		return blockDetail;
	}

	public void setBlockDetail(String blockDetail) {
		this.blockDetail = blockDetail;
	}

	public Boolean getIsReEmision() {
		return isReEmision;
	}

	public void setIsReEmision(Boolean isReEmision) {
		this.isReEmision = isReEmision;
	}

	public String getTramitNumber() {
		return tramitNumber;
	}

	public void setTramitNumber(String tramitNumber) {
		this.tramitNumber = tramitNumber;
	}

	public Integer getUnsubscribeBond() {
		return unsubscribeBond;
	}

	public void setUnsubscribeBond(Integer unsubscribeBond) {
		this.unsubscribeBond = unsubscribeBond;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Resident getBlocker() {
		return blocker;
	}

	public void setBlocker(Resident blocker) {
		this.blocker = blocker;
	}

	public Long getEmissionOrder_id() {
		return emissionOrder_id;
	}

	public void setEmissionOrder_id(Long emissionOrder_id) {
		this.emissionOrder_id = emissionOrder_id;
	}

	public Integer getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(Integer serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public Resident getAttendant() {
		return attendant;
	}

	public void setAttendant(Resident attendant) {
		this.attendant = attendant;
	}


}
