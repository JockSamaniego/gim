package ec.gob.gim.finances.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.waterservice.model.WaterMeter;

@Audited
@Entity
@TableGenerator(name = "WriteOffRequestGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WriteOffRequest", initialValue = 1, allocationSize = 1)
public class WriteOffRequest {

	@Id
	@GeneratedValue(generator = "WriteOffRequestGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Boolean isActive = Boolean.TRUE;

	@Column(length = 50)
	private String internalProcessNumber;

	@Column(length = 100)
	private String detail;

	@ManyToOne
	@JoinColumn(name = "writeofftype_id")
	private WriteOffType writeOffType;

	/**
	 * numero de secuencia a dar de baja
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private SequenceManager sequenceManager;

	/**
	 * Medidor a dar de baja
	 */
	@ManyToOne
	@JoinColumn(name = "watermeter_id")
	private WaterMeter waterMeter;

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
	 * usuario que elabora la baja
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

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name = "writeoffrequest_id")
	@OrderBy("id asc")
	private List<WriteOffDetail> details = new ArrayList<WriteOffDetail>();
	
	@Column(length = 100)
	private String observations;

	public WriteOffRequest() {
		super();
		// TODO Auto-generated constructor stub
		this.date = new Date();
		this.waterMeter = new WaterMeter();
		this.writeOffType = null;
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

	public Resident getIssueTo() {
		return issueTo;
	}

	public void setIssueTo(Resident issueTo) {
		this.issueTo = issueTo;
	}

	public WaterMeter getWaterMeter() {
		return waterMeter;
	}

	public void setWaterMeter(WaterMeter waterMeter) {
		this.waterMeter = waterMeter;
	}

	public List<WriteOffDetail> getDetails() {
		return details;
	}

	public void setDetails(List<WriteOffDetail> details) {
		this.details = details;
	}

	public void addDetail(WriteOffDetail detail) {
		detail.setWriteOffRequest(this);
		this.details.add(detail);
	}

	public String sequenceNumberComplete() {
		
		DecimalFormat numFormat = new DecimalFormat("0000");
		//System.out.println("Code format: "+numFormat.format(this.sequenceManager.getCode()));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(numFormat.format(this.sequenceManager.getCode()));
		stringBuilder.append(" - ");
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.getDate()); 
		stringBuilder.append(cal.get(Calendar.YEAR));
		return stringBuilder.toString();
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}
	
}