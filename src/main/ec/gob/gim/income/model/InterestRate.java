package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gob.gim.common.DateUtils;
import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
		name = "InterestRateGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "InterestRate", 
		initialValue = 1, allocationSize = 1
)

@NamedQueries(value={
		@NamedQuery(name="InterestRate.findById",
			    query="SELECT ir FROM InterestRate ir " +
				    		"WHERE ir.id = :id " +
							"ORDER BY ir.beginDate, ir.endDate"),
		@NamedQuery(name="InterestRate.findByEndDateGreaterThan",
				    query="SELECT ir FROM InterestRate ir " +
					    	"WHERE ir.endDate >= :endDate " +
							"ORDER BY ir.beginDate, ir.endDate"),
		@NamedQuery(name="InterestRate.findLastEndDate",
				    query="SELECT max (ir.endDate) FROM InterestRate ir "),
		@NamedQuery(name="InterestRate.findActive",
				    query="SELECT ir FROM InterestRate ir where :date BETWEEN ir.beginDate AND ir.endDate")
})

public class InterestRate {

	public static final String YEAR = "YEAR";
	public static final String MONTH = "MONTH";
	public static final String DATE = "DATE";
	
	@Id
	@GeneratedValue(generator = "InterestRateGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date beginDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	private BigDecimal value;
	
	private Integer factor;
	
	public InterestRate() {
		setBeginDate(Calendar.getInstance().getTime());
		setEndDate(Calendar.getInstance().getTime());
		
		value = BigDecimal.ZERO;
		factor = new Integer(100);
	}
	
	public BigDecimal getRate(){
		return value.divide(new BigDecimal(factor));
	}
	
	public BigDecimal getMonthlyRate(){
		return value.divide(new BigDecimal(12), new MathContext(10)).divide(new BigDecimal(factor), new MathContext(12));
	}
	
	
	public Map<String, Integer> getDateLimit(Date expirationDate, Date current){
		
		Calendar firstDate = Calendar.getInstance();
		Calendar lastDate = Calendar.getInstance();
		
		if ((endDate.after(expirationDate) && beginDate.before(expirationDate))){
			if (this.endDate.before(current)){
				firstDate.setTime(expirationDate);
				lastDate.setTime(endDate);
			}else{
				firstDate.setTime(expirationDate);
				lastDate.setTime(current);
			}
		}else{
			if (this.endDate.before(current)){
				firstDate.setTime(beginDate);
				lastDate.setTime(endDate);
			}else{
				firstDate.setTime(beginDate);
				lastDate.setTime(current);
			}
		}
				
		if (firstDate.after(lastDate))
			return null;
		
		Integer year = lastDate.get(Calendar.YEAR);
	    Integer month = lastDate.get(Calendar.MONTH);
	    Integer day = lastDate.get(Calendar.DATE);
	        
	 // PARA LOS DIAS
	    if (day < firstDate.get(Calendar.DATE)){
	    	int ndays = lastDate.getActualMaximum(Calendar.DAY_OF_MONTH);
	        day = (day + ndays) - firstDate.get(Calendar.DATE);
	        // como pedi un mes dias tengo que devolver al mes actual
	        month--;
	    }else
	    	day = day - firstDate.get(Calendar.DATE);
	        
	    if (month < firstDate.get(Calendar.MONTH)){
	    	month = (month + 12) - firstDate.get(Calendar.MONTH);
	        // como pedi un anio en meses tengo que devolver al anio actual
	        year--;
	    }else
	    	month = month - firstDate.get(Calendar.MONTH);
	      
	    if (year > firstDate.get(Calendar.YEAR)){
	        //now.add(Calendar.YEAR, -(birthDate.get(Calendar.YEAR)));
	        year = year - firstDate.get(Calendar.YEAR);
	    }else{
	    	year = 0;
	    }

	    Map<String, Integer> map = new HashMap<String, Integer>();
	    map.put(YEAR, year);
	    map.put(MONTH, month);
	    map.put(DATE, day);
	    
	    return map;
	}
	
	public Integer getMonthsToApply(Date expirationDate, Date current){		
		Map<String, Integer> limit = this.getDateLimit(expirationDate, current);
		
		if(limit == null) return null;
		
		if (limit.get(DATE)  > 0)
			return limit.get(MONTH)+1;
		else{
			return limit.get(MONTH);
		}
		
	}
		
	public List<BigDecimal> findRatesToApply(Date expirationDate, Date current){
		Integer countMonths = getMonthsToApply(expirationDate, current);
		
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		
		if (countMonths != null){
			for (int i=0; i < countMonths.intValue(); i++){
				list.add(getRate());
			}
		}
		
		return list;
	}
	
	public List<BigDecimal> findRatesMonthsToApply(Date expirationDate, Date current){
		Integer countMonths = getMonthsToApply(expirationDate, current);
		
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		
		if (countMonths != null){
			for (int i=0; i < countMonths.intValue(); i++){
				list.add(getMonthlyRate());
			}
		}
		
		return list;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = DateUtils.truncate(beginDate);
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		Calendar calendar = DateUtils.getTruncatedInstance(endDate);
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		this.endDate = calendar.getTime();
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the factor
	 */
	public Integer getFactor() {
		return factor;
	}

	/**
	 * @param factor the factor to set
	 */
	public void setFactor(Integer factor) {
		this.factor = factor;
	}
	
	/*public static void main (String[] args){
		Calendar firstDate = new GregorianCalendar(2011,04,1);
		Calendar lastDate = new GregorianCalendar(2011,06,30);
		
		InterestRate ir = new InterestRate();
		ir.setBeginDate(firstDate.getTime());
		ir.setEndDate(lastDate.getTime());
		ir.setValue(new BigDecimal(12.98));
				
		System.out.println("RATE: " + ir.getRate());
		System.out.println("RATEMONTHS: " + ir.getRateMonths());
		
		Date current = Calendar.getInstance().getTime();
		Calendar c = new GregorianCalendar(2011,8,2);
		//Date current = c.getTime();
		
		Map<String, Integer> c1 = ir.getDateLimit(current, Calendar.getInstance().getTime()); 
		
		System.out.println("YEAR: " + c1.get("YEAR"));
		System.out.println("Meses: " + c1.get("MONTH"));
		System.out.println("DIAS: " + c1.get("DATE"));
		
		System.out.println("ESTA DENTRO DEL LIMITE: " + ir.isInTime(current));
		
		System.out.println("Numero de Meses: " + ir.getMonthsToApply(current, Calendar.getInstance().getTime()));
		
		List<BigDecimal> rates = ir.findRatesToApply(current, Calendar.getInstance().getTime());
		
		System.out.println("Rates size: " + rates.size());
		for (BigDecimal bd : rates ){
			System.out.println("Rate : " + bd.doubleValue());
		}
		
	}
	*/
}
