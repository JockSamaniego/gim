package ec.gob.gim.revenue.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Domain;

@Audited
@Entity
@TableGenerator(
		name = "RevisionEmissionOrderFMGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "RevisionEmissionOrderFM", 
		initialValue = 1, allocationSize = 1
)
public class RevisionEmissionOrderFM {

	@Id
	@GeneratedValue(generator = "RevisionEmissionOrderFMGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	private EmissionOrder emissionOrder;
	
	private Long reviserUserId;
	
	private String reviserName;
	
	private Date revisionDate;
	
	private String status;
	
	private Boolean ownerIdentification;
	private Boolean infractionNumber;
	private Boolean offenderPlate;
	private Boolean infractionDate;
	private Boolean infractionPlace;
	private Boolean speedLimitDetected;
	private Boolean establishedSpeedLimit;
	private Boolean equipmentSerialNumber;
	private Boolean offenderService;
	private Boolean infractionAmount;
	private Boolean ballotGenerationDate;
	private Boolean threePhotographs;
	private Boolean detailsMotivation;
	private Boolean ballotNotification;
	private Boolean approvalCertificate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public EmissionOrder getEmissionOrder() {
		return emissionOrder;
	}
	public void setEmissionOrder(EmissionOrder emissionOrder) {
		this.emissionOrder = emissionOrder;
	}
	public Long getReviserUserId() {
		return reviserUserId;
	}
	public void setReviserUserId(Long reviserUserId) {
		this.reviserUserId = reviserUserId;
	}
	public Date getRevisionDate() {
		return revisionDate;
	}
	public void setRevisionDate(Date revisionDate) {
		this.revisionDate = revisionDate;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getOwnerIdentification() {
		return ownerIdentification;
	}
	public void setOwnerIdentification(Boolean ownerIdentification) {
		this.ownerIdentification = ownerIdentification;
	}
	public Boolean getInfractionNumber() {
		return infractionNumber;
	}
	public void setInfractionNumber(Boolean infractionNumber) {
		this.infractionNumber = infractionNumber;
	}
	public Boolean getOffenderPlate() {
		return offenderPlate;
	}
	public void setOffenderPlate(Boolean offenderPlate) {
		this.offenderPlate = offenderPlate;
	}
	public Boolean getInfractionDate() {
		return infractionDate;
	}
	public void setInfractionDate(Boolean infractionDate) {
		this.infractionDate = infractionDate;
	}
	public Boolean getInfractionPlace() {
		return infractionPlace;
	}
	public void setInfractionPlace(Boolean infractionPlace) {
		this.infractionPlace = infractionPlace;
	}
	public Boolean getSpeedLimitDetected() {
		return speedLimitDetected;
	}
	public void setSpeedLimitDetected(Boolean speedLimitDetected) {
		this.speedLimitDetected = speedLimitDetected;
	}
	public Boolean getEstablishedSpeedLimit() {
		return establishedSpeedLimit;
	}
	public void setEstablishedSpeedLimit(Boolean establishedSpeedLimit) {
		this.establishedSpeedLimit = establishedSpeedLimit;
	}
	public Boolean getEquipmentSerialNumber() {
		return equipmentSerialNumber;
	}
	public void setEquipmentSerialNumber(Boolean equipmentSerialNumber) {
		this.equipmentSerialNumber = equipmentSerialNumber;
	}
	public Boolean getOffenderService() {
		return offenderService;
	}
	public void setOffenderService(Boolean offenderService) {
		this.offenderService = offenderService;
	}
	public Boolean getInfractionAmount() {
		return infractionAmount;
	}
	public void setInfractionAmount(Boolean infractionAmount) {
		this.infractionAmount = infractionAmount;
	}
	public Boolean getBallotGenerationDate() {
		return ballotGenerationDate;
	}
	public void setBallotGenerationDate(Boolean ballotGenerationDate) {
		this.ballotGenerationDate = ballotGenerationDate;
	}
	public Boolean getThreePhotographs() {
		return threePhotographs;
	}
	public void setThreePhotographs(Boolean threePhotographs) {
		this.threePhotographs = threePhotographs;
	}
	public Boolean getDetailsMotivation() {
		return detailsMotivation;
	}
	public void setDetailsMotivation(Boolean detailsMotivation) {
		this.detailsMotivation = detailsMotivation;
	}
	public Boolean getBallotNotification() {
		return ballotNotification;
	}
	public void setBallotNotification(Boolean ballotNotification) {
		this.ballotNotification = ballotNotification;
	}
	public Boolean getApprovalCertificate() {
		return approvalCertificate;
	}
	public void setApprovalCertificate(Boolean approvalCertificate) {
		this.approvalCertificate = approvalCertificate;
	}
	public String getReviserName() {
		return reviserName;
	}
	public void setReviserName(String reviserName) {
		this.reviserName = reviserName;
	}
		
}
