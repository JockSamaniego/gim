package ec.gob.gim.finances.model;

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
import ec.gob.gim.waterservice.model.WaterSupply;

@Audited
@Entity
@TableGenerator(name = "WriteOffRequestGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WriteOffRequest", initialValue = 1, allocationSize = 1)
public class WriteOffRequest {

	@Id
	@GeneratedValue(generator = "WriteOffRequestGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Boolean isActive;

	@Column(length = 50)
	private String internalProcessNumber;

	@Column(length = 100)
	private String detail;
	
	@Column(length = 100)
	private String detailEmission;
	
	@ManyToOne
	@JoinColumn(name = "writeofftype_id")
	private WriteOffType writeOffType;

	/**
	 * numero de secuencia a dar de baja
	 */
	@ManyToOne
	@JoinColumn(name = "sequencemanager_id")
	private SequenceManager sequenceManager;

	/**
	 * servicio de agua potable a dar de baja
	 */
	@ManyToOne
	@JoinColumn(name = "watersupply_id")
	private WaterSupply waterSupply;

	/**
	 * a quien se emite cuando sea necesario 
	 */
	@ManyToOne
	@JoinColumn(name = "issueto_id")
	private Resident issueTo;
	
	/**
	 * contribuyente
	 */
	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;

	/**
	 * usuario que alabora la baja
	 */
	@ManyToOne
	@JoinColumn(name = "madeby_id")
	private Resident madeBy;

	/**
	 * usuario que aprueba la baja
	 */
	@ManyToOne
	@JoinColumn(name = "approvedby_id")
	private Resident approvedBy;

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getInternalProcessNumber() {
		return internalProcessNumber;
	}

	public void setInternalProcessNumber(String internalProcessNumber) {
		this.internalProcessNumber = internalProcessNumber;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public SequenceManager getSequenceManager() {
		return sequenceManager;
	}

	public void setSequenceManager(SequenceManager sequenceManager) {
		this.sequenceManager = sequenceManager;
	}

	public WaterSupply getWaterSupply() {
		return waterSupply;
	}

	public void setWaterSupply(WaterSupply waterSupply) {
		this.waterSupply = waterSupply;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Resident getMadeBy() {
		return madeBy;
	}

	public void setMadeBy(Resident madeBy) {
		this.madeBy = madeBy;
	}

	public Resident getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Resident approvedBy) {
		this.approvedBy = approvedBy;
	}

	public WriteOffType getWriteOffType() {
		return writeOffType;
	}

	public void setWriteOffType(WriteOffType writeOffType) {
		this.writeOffType = writeOffType;
	}
	
	public String getDetailEmission() {
		return detailEmission;
	}

	public void setDetailEmission(String detailEmission) {
		this.detailEmission = detailEmission;
	}

	public Resident getIssueTo() {
		return issueTo;
	}

	public void setIssueTo(Resident issueTo) {
		this.issueTo = issueTo;
	}

}
