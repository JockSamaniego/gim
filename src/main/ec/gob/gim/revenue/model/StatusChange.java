package ec.gob.gim.revenue.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.security.model.User;

@Audited
@Entity
@TableGenerator(name = "StatusChangeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "StatusChange", initialValue = 1, allocationSize = 1)

//@NamedNativeQueries(value = {
//	@NamedNativeQuery(name="StatusChange.findDownBondsWithAccountByResolution",
//		query = "select ac.accountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc " +
//				"left join gimprod.municipalbond mb " +
//				"on mb.id=sc.municipalbond_id " +
//				"left join gimprod.item i " +
//				"on i.municipalbond_id=sc.municipalbond_id " +
//				"left join gimprod.entry e " +
//				"on e.id = i.entry_id " +
//				"left join gimprod.account ac " +
//				"on ac.id = e.account_id " +
//				"where sc.municipalbondstatus_id = 9 " +
//				"and sc.explanation = ':resolutionNumber'" +
//				"and mb.emisionDate >= '2012-01-01'" +
//				"group by ac.accountcode, ac.accountname " +
//				"order by ac.accountCode")
//	})
//
@NamedQueries(value = {
		// Enlista todas las resoluciones de baja agrupadas
		@NamedQuery(name = "StatusChange.findAllGrouped",
//		query = "select NEW org.gob.gim.income.dto.BondDownStatus(sc.explanation, sum(sc.municipalBond.value+sc.municipalBond.taxesTotal-sc.municipalBond.discount)) " +
				query = "select NEW org.gob.gim.income.dto.BondDownStatus(sc.explanation, sum(sc.municipalBond.value+sc.municipalBond.taxesTotal)) "
						+ "from StatusChange sc " + "where sc.municipalBondStatus.id=9 "
						+ "and sc.date between :startDateFiscalPeriod and :endDateFiscalPeriod "
						+ "group by sc.explanation " + "order by sc.explanation"),
		@NamedQuery(name = "StatusChange.findAllGroupedByResolution",
//		query = "select NEW org.gob.gim.income.dto.BondDownStatus(sc.explanation, sum(sc.municipalBond.value+sc.municipalBond.taxesTotal-sc.municipalBond.discount)) " +
				query = "select NEW org.gob.gim.income.dto.BondDownStatus(sc.explanation, sum(sc.municipalBond.value+sc.municipalBond.taxesTotal)) "
						+ "from StatusChange sc " + "where sc.municipalBondStatus.id=9 "
						+ "and sc.date between :startDateFiscalPeriod and :endDateFiscalPeriod "
						+ "and lower(sc.explanation) like lower(concat('%',:resolution,'%')) "
						+ "group by sc.explanation " + "order by sc.explanation"),
		@NamedQuery(name = "StatusChange.findAllGroupedByDates",
//			query = "select NEW org.gob.gim.income.dto.BondDownStatus(sc.explanation, sum(sc.municipalBond.value+sc.municipalBond.taxesTotal-sc.municipalBond.discount)) " +
				query = "select NEW org.gob.gim.income.dto.BondDownStatus(sc.explanation, sum(sc.municipalBond.value+sc.municipalBond.taxesTotal)) "
						+ "from StatusChange sc " + "where sc.municipalBondStatus.id=9 "
						+ "and sc.date between :startDate and :endDate " + "group by sc.explanation "
						+ "order by sc.explanation"),
		@NamedQuery(name = "StatusChange.findByResolution", query = "select NEW org.gob.gim.income.dto.BondDownStatus(sc.explanation, sc.date, sum(sc.municipalBond.value+sc.municipalBond.taxesTotal)) "
				+ "from StatusChange sc " + "where sc.municipalBondStatus.id=9 "
				+ "and lower(sc.explanation) like lower(concat('%',:resolution,'%')) " + "group by sc.explanation "
				+ "order by sc.date desc, sc.explanation"),
		@NamedQuery(name = "StatusChange.findBondsDownStatusByResolution", query = "select mb from StatusChange sc "
				+ "left join sc.municipalBond mb " + "left join fetch mb.receipt receipt "
				+ "left join fetch mb.resident resident " + "where sc.municipalBondStatus.id=9 "
				+ "and sc.explanation = :resolution " + "and sc.date = mb.reversedDate " + "order by mb.emisionDate"),
		@NamedQuery(name = "StatusChange.findBondsDownStatusByDates", query = "select mb from StatusChange sc "
				+ "left join sc.municipalBond mb " + "left join fetch mb.receipt receipt "
				+ "left join fetch mb.resident resident " + "where sc.municipalBondStatus.id=9 "
				+ "and sc.date between :startDate and :endDate " + "and sc.date = mb.reversedDate "
				+ "order by mb.reversedResolution, mb.reversedDate, mb.emisionDate"),

		@NamedQuery(name = "StatusChange.findBondsByStatusAndDate", query = "select distinct(mb) from StatusChange sc "
				+ "left join sc.municipalBond mb " + "left join fetch mb.receipt receipt "
				+ "left join fetch mb.resident resident " + "where sc.municipalBondStatus.id=:statusId "
				+ "and sc.date between :startDate and :endDate "
				+ "order by mb.reversedResolution, mb.reversedDate, mb.emisionDate"),

		@NamedQuery(name = "StatusChange.findBondsByStatusAndDateAndUser", query = "select distinct(mb) from StatusChange sc "
				+ "left join sc.municipalBond mb " + "left join fetch mb.receipt receipt "
				+ "left join fetch mb.resident resident " + "where sc.municipalBondStatus.id=:statusId "
				+ "and sc.date between :startDate and :endDate " + "and sc.user.id = :userId "
				+ "order by mb.reversedResolution, mb.reversedDate, mb.emisionDate"),

		@NamedQuery(name = "StatusChange.findDownBondsWithAccountByResolutionOriginal", query = "select NEW org.gob.gim.income.dto.BondDownStatus(ac.accountCode, ac.accountName, sum(i.total)) from StatusChange sc "
				+ "left join sc.municipalBond mb " + "left join mb.items i " + "left join i.entry e "
				+ "left join e.account ac " + "where sc.municipalBondStatus.id=9 " + "and sc.explanation = :resolution "
				+ "and sc.date = mb.reversedDate " + "group by ac.accountCode, ac.accountName "
				+ "order by ac.accountCode"),
		@NamedQuery(name = "StatusChange.findDownBondsWithAccountByResolution", query = "select NEW org.gob.gim.income.dto.BondDownStatus(ac.accountCode, ac.accountName, sum(i.total)) from StatusChange sc "
				+ "left join sc.municipalBond.items i " + "left join i.entry.account ac "
				+ "where sc.municipalBondStatus.id=9 " + "and sc.explanation = :resolution "
				+ "group by ac.accountCode, ac.accountName " + "order by ac.accountCode"),
		@NamedQuery(name = "StatusChange.findDownBondsWithAccountByDates", query = "select NEW org.gob.gim.income.dto.BondDownStatus(ac.accountCode, ac.accountName, sum(i.total)) from StatusChange sc "
				+ "left join sc.municipalBond.items i " + "left join i.entry.account ac "
				+ "where sc.municipalBondStatus.id=9 " + "and sc.date between :startDate and :endDate "
				+ "group by ac.accountCode, ac.accountName " + "order by ac.accountCode"),
		@NamedQuery(name = "StatusChange.findDownBondsWithAccountByDates", query = "select NEW org.gob.gim.income.dto.BondDownStatus(ac.accountCode, ac.accountName, sum(i.total)) from StatusChange sc "
				+ "left join sc.municipalBond.items i " + "left join i.entry.account ac "
				+ "where sc.municipalBondStatus.id=9 " + "and sc.date between :startDate and :endDate "
				+ "group by ac.accountCode, ac.accountName " + "order by ac.accountCode"),

		@NamedQuery(name = "StatusChange.findLastChangeByMunicipalBond", query = "select sc from StatusChange sc "
				+ "where sc.municipalBond = :municipalBond " + "order by sc.date+sc.time desc limit 1")

})

public class StatusChange {

	@Id
	@GeneratedValue(generator = "StatusChangeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Temporal(TemporalType.TIME)
	private Date time;

	private String explanation;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "previousBondStatus_id")
	private MunicipalBondStatus previousBondStatus;

	@ManyToOne
	@JoinColumn(name = "municipalBondStatus_id")
	private MunicipalBondStatus municipalBondStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalBond_id")
	private MunicipalBond municipalBond;

	@Transient
	private BigDecimal totalForResolution;

	@Transient
	private Boolean changeResident;

	// rfam 2020-02-13 ML-UMTTTSV-L-FM-050-2020
	private String judicialProcessNumber;

	public StatusChange() {
		Date now = new Date();
		setDate(now);
		setTime(now);
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public MunicipalBondStatus getPreviousBondStatus() {
		return previousBondStatus;
	}

	public void setPreviousBondStatus(MunicipalBondStatus previousBondStatus) {
		this.previousBondStatus = previousBondStatus;
	}

	public BigDecimal getTotalForResolution() {
		return totalForResolution;
	}

	public void setTotalForResolution(BigDecimal totalForResolution) {
		this.totalForResolution = totalForResolution;
	}

	public Boolean getChangeResident() {
		return changeResident;
	}

	public void setChangeResident(Boolean changeResident) {
		this.changeResident = changeResident;
	}

	public StatusChange cloneNew(StatusChange scOld, String explanation, User user) {

		StatusChange scNew = new StatusChange();
		scNew.setExplanation("SC_EXPL: " + explanation);
		scNew.setUser(user);
		scNew.setPreviousBondStatus(scOld.getMunicipalBond().getMunicipalBondStatus());
		scNew.setMunicipalBondStatus(scOld.getMunicipalBond().getMunicipalBondStatus());
		scNew.setMunicipalBond(scOld.getMunicipalBond());
		return scNew;

	}

	@Override
	public String toString() {
		return "StatusChange [id=" + id + ", date=" + date + ", time=" + time + ", explanation=" + explanation
				+ ", user=" + user + ", previousBondStatus=" + previousBondStatus + ", municipalBondStatus="
				+ municipalBondStatus + ", municipalBond=" + municipalBond + ", totalForResolution="
				+ totalForResolution + ", changeResident=" + changeResident + "]";
	}

	public String getJudicialProcessNumber() {
		return judicialProcessNumber;
	}

	public void setJudicialProcessNumber(String judicialProcessNumber) {
		this.judicialProcessNumber = judicialProcessNumber;
	}

}