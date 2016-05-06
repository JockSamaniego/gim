package ec.gob.gim.waterservice.model;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:28
 */

@Audited
@Entity
@TableGenerator(name = "ConsumptionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Consumption", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "Consumption.findByYearMonth", query = "SELECT consumption FROM Consumption consumption "
				+ "left join fetch consumption.waterSupply waterSupply "
				+ "left join fetch waterSupply.waterMeters wm "
				+ "left join fetch waterSupply.route route "
				+ "left join fetch waterSupply.street street "
				+ "left join fetch street.place place "
				+ "left join fetch consumption.consumptionState cs "
				+ "left join fetch waterSupply.waterSupplyCategory waterSupplyCategory "
				//+ "left join fetch waterSupply.contract contract "
				//+ "left join fetch contract.subscriber subscriber "
				//+ "left join fetch contract.contractType contractType "
				//+ "left join fetch contractType.entry entry "
				+ "left join fetch waterSupply.serviceOwner serviceOwner "
				+ "left join fetch waterSupply.recipeOwner recipeOwner "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and route.id = :routeId and waterSupply.isCanceled = :isCanceled "
				+ "and wm.isActive = TRUE ORDER by waterSupply.routeOrder"),
				//+ "ORDER by waterSupply.routeOrder"),
				
		@NamedQuery(name = "Consumption.findByYearMonthNotMunicipal", query = "SELECT consumption FROM Consumption consumption "
				+ "left join fetch consumption.waterSupply waterSupply "
				+ "left join fetch waterSupply.waterMeters wm "
				+ "left join fetch waterSupply.route route "
				+ "left join fetch waterSupply.street street "
				+ "left join fetch street.place place "
				+ "left join fetch consumption.consumptionState cs "
				+ "left join fetch waterSupply.waterSupplyCategory waterSupplyCategory " 
				+ "left join fetch waterSupply.serviceOwner serviceOwner "
				+ "left join fetch waterSupply.recipeOwner recipeOwner "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and route.id = :routeId and waterSupply.isCanceled = :isCanceled "
				+ "and waterSupplyCategory.id <> 8 "
				+ "and wm.isActive = TRUE ORDER by waterSupply.routeOrder"),

		@NamedQuery(name = "Consumption.findByYearMonthwaterSupplyCategory", query = "SELECT consumption FROM Consumption consumption "
				+ "left join fetch consumption.waterSupply waterSupply "
				+ "left join fetch consumption.consumptionState cs "
				+ "left join fetch waterSupply.waterSupplyCategory waterSupplyCategory "
				//+ "left join fetch waterSupply.contract contract "
				//+ "left join fetch contract.contractType contractType "
				//+ "left join fetch contractType.entry entry "
				+ "left join fetch waterSupply.serviceOwner serviceOwner "
				+ "left join fetch waterSupply.recipeOwner recipeOwner "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and waterSupply.route.id = :routeId " +
						"and waterSupplyCategory.id = :waterSupplyCategoryId " +
						"ORDER by waterSupply.routeOrder"),

		@NamedQuery(name = "Consumption.findByYearMonthWaterSupply", query = "SELECT consumption FROM Consumption consumption "
				+ "WHERE consumption.year = :year and "
				+ "consumption.month = :month and "
				+ "consumption.waterSupply.id = :waterSupply"),

		@NamedQuery(name = "Consumption.amountAverage", query = "SELECT AVG(consumption.currentReading - consumption.previousReading) FROM Consumption consumption "
				+ "where consumption.year = :year and "
				+ "consumption.waterSupply.id = :waterSupply and "
				+ "consumption.month BETWEEN :monthStart and :monthEnd"),

		@NamedQuery(name = "Consumption.amountAverageByWaterSupply", query = "SELECT NEW ec.gob.gim.waterservice.model.WaterSupplyAverage(ws.id,AVG(consumption.currentReading - consumption.previousReading)) FROM Consumption consumption join consumption.waterSupply ws "
				+ "where consumption.year = :year and "
				+ "consumption.month BETWEEN :monthStart and :monthEnd and "
				+ "ws.id in (:waterSupplyIds) GROUP BY ws.id"),

		@NamedQuery(name = "Consumption.findByWaterSupply", query = "SELECT consumption FROM Consumption consumption left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState "
				+ "left join fetch consumption.waterMeterStatus "
				+ "WHERE w.id  = :waterSupplyId ORDER by consumption.year ASC, consumption.month ASC"),

		@NamedQuery(name = "Consumption.countByYearMonth", query = "SELECT consumption FROM Consumption consumption left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState cs "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and "
				+ "consumption.month = :month and "
				+ "consumption.amount <> null and "
				+ "w.route.id = :routeId ORDER by w.routeOrder"),

		@NamedQuery(name = "Consumption.countByYearMonthShouldEmit", query = "SELECT COUNT(consumption) FROM Consumption consumption "
				+ "left join consumption.waterSupply waterSupply "
				+ "WHERE consumption.year = :year and "
				+ "consumption.month = :month and "
				+ "waterSupply.route.id = :routeId ORDER by waterSupply.routeOrder"),

		@NamedQuery(name = "Consumption.totalByYearMonthEmited", query = "SELECT consumption FROM Consumption consumption left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState cs "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and w.route.id = :routeId and consumption.hasPreEmit = TRUE"),

		@NamedQuery(name = "Consumption.isEmitedByYearMonth", query = "SELECT consumption FROM Consumption consumption left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState cs "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and w.route.id = :routeId and consumption.hasPreEmit = FALSE ORDER by w.routeOrder"),

		@NamedQuery(name = "Consumption.findByWaterSupplyServiceNumber", query = "SELECT consumption FROM Consumption consumption " 
				+ "left join fetch consumption.waterSupply w "
				//+ "LEFT JOIN FETCH w.contract cont "
				//+ "LEFT JOIN FETCH cont.contractType ct "
				//+ "LEFT JOIN FETCH ct.entry "
				+ "LEFT JOIN FETCH w.serviceOwner "
				+ "left join fetch w.recipeOwner recipeOwner "
				+ "LEFT JOIN FETCH w.street s "
				+ "LEFT JOIN FETCH s.place "
				+ "LEFT JOIN FETCH w.waterMeters wm "
				+ "LEFT JOIN FETCH wm.waterMeterStatus wms "
				+ "LEFT JOIN FETCH w.waterSupplyCategory "
				+ "LEFT JOIN FETCH w.route route "
				+ "left join fetch consumption.consumptionState cs "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and w.serviceNumber = :wsServiceNumber "
				+ "and wm.isActive = TRUE "
				+ "ORDER by w.routeOrder"),
		@NamedQuery(name = "Consumption.findOver", query = "SELECT consumption FROM Consumption consumption left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState cs "
				+ "left join fetch consumption.waterSupply waterSupply "
				+ "left join fetch waterSupply.waterSupplyCategory waterSupplyCategory "
				//+ "left join fetch waterSupply.contract contract "
				//+ "left join fetch contract.contractType contractType "
				+ "LEFT JOIN FETCH w.waterMeters wm "
				//+ "left join fetch contractType.entry entry "
				+ "left join fetch waterSupply.serviceOwner serviceOwner "
				+ "left join fetch waterSupply.recipeOwner recipeOwner "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and w.route.id = :routeId and consumption.amount > :overAmount "
				+ "and wm.isActive = TRUE "
				+ "ORDER by w.routeOrder"),
		@NamedQuery(name = "Consumption.findLessThan", query = "SELECT consumption FROM Consumption consumption left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState cs "
				+ "left join fetch consumption.waterSupply waterSupply "
				+ "left join fetch waterSupply.waterSupplyCategory waterSupplyCategory "
				//+ "left join fetch waterSupply.contract contract "
				//+ "left join fetch contract.contractType contractType "
				+ "LEFT JOIN FETCH w.waterMeters wm "
				//+ "left join fetch contractType.entry entry "
				+ "left join fetch waterSupply.serviceOwner serviceOwner "
				+ "left join fetch waterSupply.recipeOwner recipeOwner "
				//+ "left join fetch consumption.checkingRecord cr "
				+ "left join fetch consumption.waterMeterStatus ws "
				+ "WHERE consumption.year = :year and consumption.month = :month and w.route.id = :routeId and consumption.amount < :overAmount "
				+ "and wm.isActive = TRUE "
				+ "ORDER by w.routeOrder"),
		@NamedQuery(name = "Consumption.findByWaterSupplyBetweenMonth", query = "SELECT consumption FROM Consumption consumption left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState "
				+ "left join fetch consumption.waterMeterStatus "
				+ "WHERE w.id  = :waterSupplyId and  consumption.year = :year and consumption.month between :monthEnd and :monthStart ORDER by consumption.month ASC"),
		@NamedQuery(name = "Consumption.updateHasPreEmit", query = "update Consumption c set c.hasPreEmit = :hasPreEmit "
				+ "WHERE c.year = :year and c.month = :month and "
				+ "exists (select ws from WaterSupply ws where ws.route.id=:routeId and ws.id=c.waterSupply.id))"),
				
				//desde aqui las mejoras
		@NamedQuery(name = "Consumption.findAlreadyGenerated", query = "SELECT ws.id FROM Consumption c "
				+ "left join c.waterSupply ws "
				+ "left join ws.route route "
				+ "WHERE c.year = :year and c.month = :month and route.id = :routeId"),
		@NamedQuery(name = "Consumption.findMissingConsumptions", query = "SELECT ws.id FROM Consumption c "
				+ "left join c.waterSupply ws "
				+ "left join ws.route route "
				+ "WHERE c.year = :year and c.month = :month and route.id = :routeId and ws.id not in (:consumptionAlreadyGenerated)"),
		@NamedQuery(name = "Consumption.findAllMissingConsumptions", query = "SELECT ws.id FROM Consumption c "
				+ "left join c.waterSupply ws "
				+ "left join ws.route route "
				+ "WHERE c.year = :year and c.month = :month and route.id = :routeId"),
		@NamedQuery(name = "Consumption.findConsumptionsByWaterSupply", query = "SELECT c FROM Consumption c "
				+ "left join fetch c.waterSupply ws "
				+ "left join fetch ws.waterMeters wm "
				+ "left join fetch ws.route route "
				+ "left join fetch ws.street street "
				+ "left join fetch street.place place "
				+ "left join fetch c.consumptionState cs "
				+ "left join fetch ws.waterSupplyCategory wsc "
				+ "left join fetch ws.serviceOwner serviceOwner "
				+ "left join fetch ws.recipeOwner recipeOwner "
				+ "left join fetch c.waterMeterStatus wms "
				+ "WHERE c.year = :year and c.month = :month and route.id = :routeId and ws.isCanceled = :isCanceled "
				+ "and ws.id in (:wsIds)"
				+ "and wm.isActive = TRUE ORDER by ws.routeOrder"),
		@NamedQuery(name = "Consumption.findConsumptionsByIds", query = "SELECT c "
				+ "FROM Consumption c "
				+ "WHERE c.id in (:ids) "),
				
		@NamedQuery(name = "Consumption.findDtoConsumptionsByRoute", query = "SELECT NEW ec.gob.gim.wsrest.dto.DtoConsumption("
				+ "c.id, c.month, c.year, ws.serviceNumber, c.previousReading, c.currentReading, "
				+ "wm.serial, wms.name, cs.name, ws.routeOrder, ws.ncasa, wsc.name, serviceOwner.name, c.observations, "
				+ "c.latitude, c.longitude, c.transferred, c.received) "
				+ "FROM Consumption c "
				+ "left join c.consumptionState cs "
				+ "left join c.waterSupply ws "
				+ "left join ws.waterMeters wm "
				+ "left join ws.waterSupplyCategory wsc "
				+ "left join wm.waterMeterStatus wms "
				+ "left join ws.route route "
				+ "left join ws.serviceOwner serviceOwner "
				+ "WHERE c.year = :year and c.month = :month and route.id = :routeId  "
				+ "and wm.isActive = TRUE ORDER by ws.routeOrder"),
		@NamedQuery(name = "Consumption.findAverageByServiceCode", query = "SELECT avg(c.amount) "
				+ "FROM Consumption c "
				+ "left join c.waterSupply ws "
				+ "WHERE ws.serviceNumber = :serviceCode ")
				
				
})
public class Consumption {

	@Id
	@GeneratedValue(generator = "ConsumptionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private BigDecimal amount;
	private Long currentReading;

	private Integer month;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private MonthType monthType;

	private Long previousReading;
	@Temporal(TemporalType.DATE)
	private Date readingDate;

	private Integer year;

	private boolean hasPreEmit;
	
	private boolean applySpecialExemption;
	private boolean applyElderlyExemption;
	
	private BigDecimal exemptionValue;
	
	private String observations;

	private Double latitude;
	private Double longitude;

	private boolean received;
	private boolean transferred;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateReceived;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTransferred;

	@ManyToOne
	@JoinColumn(name = "consumptionState_id")
	public ConsumptionState consumptionState;

	//todo que hace esto, no lo estoy usando
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checkingRecord_id")
	private CheckingRecord checkingRecord;*/

	@ManyToOne
	@JoinColumn(name = "waterSupply_id")
	private WaterSupply waterSupply;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalBond_id")
	private MunicipalBond municipalBond;

	@ManyToOne
	@JoinColumn(name = "waterMeterStatus_id")
	private WaterMeterStatus waterMeterStatus;

//	@OneToOne(cascade={CascadeType.PERSIST}, mappedBy="consumption")
//	private ConsumptionControl consumptionControl;
//	
	@Transient
	private Boolean isValidReading;
	@Transient
	private BigDecimal previousAverage;
	@Transient
	private Long differenceInReading;
	@Transient
	private String errorMessage;
	@Transient
	private Boolean disabled;
	
	@Transient
	private Integer notPayMonths;
	
	@Transient
	private BigDecimal totalDebt;
	
	@Transient
	private BigDecimal paidTotal;
	
	@Transient
	private String municipalBondState;
		

	public BigDecimal getTotalDebt() {
		return totalDebt;
	}

	public void setTotalDebt(BigDecimal totalDebt) {
		this.totalDebt = totalDebt;
	}
	
	public Consumption() {
		hasPreEmit = false;
		disabled = false;
		exemptionValue = BigDecimal.ZERO;
		
		this.latitude = new Double(0);
		this.longitude = new Double(0);
		this.transferred = false;
		this.received = false;
		this.applyElderlyExemption = false;
		this.applySpecialExemption = false;
	
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getCurrentReading() {
		return currentReading;
	}

	public void setCurrentReading(Long currentReading) {
		this.currentReading = currentReading;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Long getPreviousReading() {
		return previousReading;
	}

	public void setPreviousReading(Long previousReading) {
		this.previousReading = previousReading;
	}

	public Date getReadingDate() {
		return readingDate;
	}

	public void setReadingDate(Date readingDate) {
		this.readingDate = readingDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public ConsumptionState getConsumptionState() {
		return consumptionState;
	}

	public void setConsumptionState(ConsumptionState consumptionState) {
		this.consumptionState = consumptionState;
	}

	/*public CheckingRecord getCheckingRecord() {
		return checkingRecord;
	}

	public void setCheckingRecord(CheckingRecord checkingRecord) {
		this.checkingRecord = checkingRecord;
	}*/

	public WaterSupply getWaterSupply() {
		return waterSupply;
	}

	public void setWaterSupply(WaterSupply waterSupply) {
		this.waterSupply = waterSupply;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	/**
	 * @return the waterMeterStatus
	 */
	public WaterMeterStatus getWaterMeterStatus() {
		return waterMeterStatus;
	}

	/**
	 * @param waterMeterStatus
	 *            the waterMeterStatus to set
	 */
	public void setWaterMeterStatus(WaterMeterStatus waterMeterStatus) {
		this.waterMeterStatus = waterMeterStatus;
	}

	public Boolean getIsValidReading() {
		return isValidReading;
	}

	public void setIsValidReading(Boolean isValidReading) {
		this.isValidReading = isValidReading;
	}

	public BigDecimal getPreviousAverage() {
		return previousAverage;
	}

	public void setPreviousAverage(BigDecimal previousAverage) {
		this.previousAverage = previousAverage;
	}

	public Long getDifferenceInReading() {
		return differenceInReading;
	}

	public void setDifferenceInReading(Long differenceInReading) {
		this.differenceInReading = differenceInReading;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean getHasPreEmit() {
		return hasPreEmit;
	}

	public void setHasPreEmit(Boolean hasPreEmit) {
		this.hasPreEmit = hasPreEmit;
	}

	public MonthType getMonthType() {
		return monthType;
	}

	public void setMonthType(MonthType monthType) {
		this.monthType = monthType;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getNotPayMonths() {
		return notPayMonths;
	}

	public void setNotPayMonths(Integer notPayMonths) {
		this.notPayMonths = notPayMonths;
	}
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof Consumption)) {
			return false;
		}
		Consumption other = (Consumption) object;
					
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public boolean getApplySpecialExemption() {
		return applySpecialExemption;
	}

	public void setApplySpecialExemption(boolean applySpecialExemption) {
		this.applySpecialExemption = applySpecialExemption;
	}

	public boolean getApplyElderlyExemption() {
		return applyElderlyExemption;
	}

	public void setApplyElderlyExemption(boolean applyElderlyExemption) {
		this.applyElderlyExemption = applyElderlyExemption;
	}

	public BigDecimal getExemptionValue() {
		return exemptionValue;
	}

	public void setExemptionValue(BigDecimal exemptionValue) {
		this.exemptionValue = exemptionValue;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

//	public ConsumptionControl getConsumptionControl() {
//		return consumptionControl;
//	}
//
//	public void setConsumptionControl(ConsumptionControl consumptionControl) {
//		this.consumptionControl = consumptionControl;
//	}
//
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public boolean isTransferred() {
		return transferred;
	}

	public void setTransferred(boolean transferred) {
		this.transferred = transferred;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public Date getDateTransferred() {
		return dateTransferred;
	}

	public void setDateTransferred(Date dateTransferred) {
		this.dateTransferred = dateTransferred;
	}

	public String getMunicipalBondState() {
		return municipalBondState;
	}

	public void setMunicipalBondState(String municipalBondState) {
		this.municipalBondState = municipalBondState;
	}

}
