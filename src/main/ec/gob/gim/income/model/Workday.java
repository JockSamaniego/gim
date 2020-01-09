package ec.gob.gim.income.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:50
 */
//@Audited
@Entity
@TableGenerator(
	 name="WorkdayGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Workday",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {@NamedQuery(name="Workday.findByDate", query="select workday from Workday workday where workday.date = :date"),
					   @NamedQuery(name="Workday.findByIncomeOpeningAndDate", query="select workday from Workday workday where workday.date = :date and (workday.isIncomeOpening = true or workday.isIncomeOpening = false)"),
					   @NamedQuery(name="Workday.findByRevenueOpeningAndDate", query="select workday from Workday workday where workday.date = :date and (workday.isRevenueOpening = true or workday.isRevenueOpening = false)"),
					   @NamedQuery(name="Workday.findByCharge", query="select workday from Workday workday where workday.charge = :charge"),
					   @NamedQuery(name="Workday.findById", query="select workday from Workday workday where workday.id = :id"),
                                           @NamedQuery(name="Workday.findLastCharge", query="select max(workday.charge) from Workday workday"),
					   @NamedQuery(name="Workday.countOpenedWorkday", query="select count(workday.id) from Workday workday where workday.closingWorkdayDate is null"),
					   @NamedQuery(name="Workday.findCurrentWorkday", query="select workday from Workday workday where workday.isIncomeOpening = true"),
                                           @NamedQuery(name = "Workday.changeStatus", query = 
                                                   "UPDATE Workday ws\n" +
                                                           "  SET" +
                                                           "  ws.incomeObservations = :incomeobservations,   \n" +
                                                           "  ws.closingWorkdayDate = :closingworkdaydate,   \n" +
                                                           "  ws.isIncomeOpening = :isincomeopening, \n" +
                                                           "  ws.closingTime = :closingtime\n" +
                                                           "  WHERE ws.id IN (:id)")})
public class Workday {
	
	@Id
	@GeneratedValue(generator="WorkdayGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(unique=true)
	private Long charge;	
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.DATE)
	private Date closingWorkdayDate;

	private String incomeObservations;	
	
	private String revenueObservations;

	private Boolean isRevenueOpening;
	
	private Boolean isIncomeOpening;

	@Temporal(TemporalType.TIMESTAMP)
	private Date openingTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date closingTime;
	
	@OneToMany(mappedBy = "workday", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<TillPermission> tillPermissions;	

	public Workday(){
		tillPermissions = new ArrayList<TillPermission>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCharge() {
		return charge;
	}

	public void setCharge(Long charge) {
		this.charge = charge;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public List<TillPermission> getTillPermissions() {
		Collections.sort(tillPermissions);
		return tillPermissions;
	}

	public void setTillPermissions(List<TillPermission> tillPermissions) {
		this.tillPermissions = tillPermissions;
	}

	public void add(TillPermission t){
		if(t != null && !tillPermissions.contains(t)){
			tillPermissions.add(t);
			t.setWorkday(this);
		}
	}
	
	public void remove(TillPermission t){
		if(tillPermissions.contains(t)){
			tillPermissions.remove(t);
			t.setWorkday(null);
		}
	}
	
	public Boolean getIsRevenueOpening() {
		return isRevenueOpening;
	}

	public void setIsRevenueOpening(Boolean isRevenueOpening) {
		this.isRevenueOpening = isRevenueOpening;
	}

	public Boolean getIsIncomeOpening() {
		return isIncomeOpening;
	}

	public void setIsIncomeOpening(Boolean isIncomeOpening) {
		this.isIncomeOpening = isIncomeOpening;
	}

	public void setIncomeObservations(String incomeObservations) {
		this.incomeObservations = incomeObservations;
	}

	public String getIncomeObservations() {
		return incomeObservations;
	}
	
	public String getRevenueObservations() {
		return revenueObservations;
	}

	public void setRevenueObservations(String revenueObservations) {
		this.revenueObservations = revenueObservations;
	}
	public Date getClosingWorkdayDate() {
		return closingWorkdayDate;
	}

	public void setClosingWorkdayDate(Date closingWorkdayDate) {
		this.closingWorkdayDate = closingWorkdayDate;
	}

	public Date getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(Date openingTime) {
		this.openingTime = openingTime;
	}

	public Date getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}
	
}//end Workday