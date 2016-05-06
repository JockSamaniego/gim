package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;

@Audited
@Entity
@TableGenerator(
	 name="TillPermissionGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="TillPermission",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name="TillPermission.findByPersonIdAndWorkdayDate", 
				query="select t from TillPermission t " +
						"where "+
						"t.person.id=:personId and t.workday.date = :date)"),
		@NamedQuery(name="TillPermission.findByBranchTillBetweenDates", 
				query="select t from TillPermission t left join t.till till left join till.branch b " +
						"where "+
						"till.id=:tillId and t.workday.date BETWEEN :startDate AND :endDate and b.id=:branchId ORDER BY t.workday.date DESC)"),
		@NamedQuery(name="TillPermission.findByBranchBetweenDates", 
				query="select t from TillPermission t left join t.till till left join till.branch b " +
						"where "+
						"t.workday.date BETWEEN :startDate AND :endDate and b.id=:branchId ORDER BY t.workday.date DESC)"),
		
		@NamedQuery(name="TillPermission.findBetweenDates", 
				query="select t from TillPermission t left join t.till till left join till.branch b " +
					"where "+
					"t.workday.date BETWEEN :startDate AND :endDate ORDER BY t.workday.date, b.id "),
                
		@NamedQuery(name="TillPermission.findWorkId", 
				query="select t from TillPermission t " +
					"where "+
					"t.workday.id = :workday AND t.till.id = :till"),                
						
		@NamedQuery(name="TillPermission.findCashiersByWorkdayDate", 
				query="select t.person from TillPermission t " +
						"where "+
						"t.workday.date = :date"),
						
		@NamedQuery(name="TillPermission.findByCashierAndWorkdayDate", 
				query="select t from TillPermission t " +
						"where "+
						"t.workday.date = :date AND t.person.id=:cashierId"),
		@NamedQuery(name="TillPermission.findNumberOpenTillsByWorkday", 
				query="select count(t.id) from TillPermission t where "+
						"t.workday.id = :workdayId and (t.openingTime is not null and t.closingTime is null) "),
		@NamedQuery(name="TillPermission.findNumberOpenTillsByDate", 
				query="select count(t.id) from TillPermission t where "+
						"cast(t.openingTime as date) = :date AND t.closingTime is null")}
)
public class TillPermission implements Comparable<TillPermission>{
	
	@Id
	@GeneratedValue(generator="TillPermissionGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date openingTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date closingTime;
	
	private BigDecimal initialBalance;
	
	private boolean isEnabled;
	
	private String observation;
	
	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	@ManyToOne
	@JoinColumn(name="till_id")
	private Till till;
	
	@ManyToOne
	@JoinColumn(name="person_id")
	private Person person;
	
	@ManyToOne
	private Workday workday;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<CashRecord> cashRecord;    
	
	public TillPermission(){
		cashRecord = new ArrayList<CashRecord>();
	}

	public List<CashRecord> getCashRecord() {
		return cashRecord;
	}

	public void setCashRecord(List<CashRecord> cashRecord) {
		this.cashRecord = cashRecord;
	}

	public Workday getWorkday() {
		return workday;
	}

	public void setWorkday(Workday workday) {
		this.workday = workday;
	}

	public Till getTill() {
		return till;
	}

	public void setTill(Till till) {
		this.till = till;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public void add(CashRecord c){
		if(!cashRecord.contains(c) && c != null){
			cashRecord.add(c);			
		}
	}
	
	public void remove(CashRecord c){
		if(cashRecord.contains(c)){
			cashRecord.remove(c);			
		}
	}

	@Override
	public int compareTo(TillPermission o) {
		if(o != null){
			return this.till.getNumber().compareTo(o.getTill().getNumber());
		}
		return 0;
	}

}
