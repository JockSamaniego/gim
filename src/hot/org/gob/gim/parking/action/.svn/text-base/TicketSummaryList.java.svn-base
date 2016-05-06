package org.gob.gim.parking.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.Tax;
import ec.gob.gim.income.model.TaxRate;
import ec.gob.gim.parking.model.Journal;
import ec.gob.gim.parking.model.ParkingLot;
import ec.gob.gim.parking.model.Ticket;
import ec.gob.gim.parking.model.TicketSummary;

@Name("ticketSummaryList")
public class TicketSummaryList extends EntityController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6099097065159199722L;

	private static final String SQL = "SELECT date_part('year', going) as year, date_part('month', going) as month, date_part('day', going) as day, count(*), sum(charge) "
			+ "FROM ticket t LEFT JOIN journal j ON (j.id=t.journal_id) "
			+ "where t.going >= ? and t.going <= ? "
			+ "and j.parkinglot_id = ? "
			+ "and t.invalidated = FALSE "
			+ "and not t.charge is null "
			+ "and not t.emissionorder_id is null "
			+ "and t.id in (:ids)"
			+ "group by year, month, day " + "order by year, month, day";

	private boolean isOnlyReport = true;

	private String ids = "";

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public boolean isOnlyReport() {
		return isOnlyReport;
	}

	public void setOnlyReport(boolean isOnlyReport) {
		this.isOnlyReport = isOnlyReport;
	}

	private static final String REPORTSQL = "SELECT date_part('year', going) as year, date_part('month', going) as month, date_part('day', going) as day, count(*), sum(charge) "
			+ "FROM ticket t LEFT JOIN journal j ON (j.id=t.journal_id) "
			+ "where t.going >= ? and t.going <= ? "
			+ "and j.parkinglot_id = ? "
			+ "and t.invalidated = FALSE "
			+ "group by year, month, day " + "order by year, month, day";

	private static final String SQL_INVALIDATED = "SELECT t.id, p.name, t.coming "
			+ "FROM ticket t LEFT JOIN journal j ON (j.id=t.journal_id) 	LEFT JOIN resident p ON (p.id = j.operator_id)"
			+ "where t.coming >= ? and t.coming <= ? "
			+ "and j.parkinglot_id = ? " + "and t.invalidated = TRUE " +
			// "and not t.charge is null " +
			"order by t.id";
	private Date start;
	private Date end;
	private ParkingLot parkingLot;
	private Charge administrationCharge;
	private Delegate administrationDelegate;

	public Charge getAdministrationCharge() {
		return administrationCharge;
	}

	public void setAdministrationCharge(Charge administrationCharge) {
		this.administrationCharge = administrationCharge;
	}

	public Delegate getAdministrationDelegate() {
		return administrationDelegate;
	}

	public void setAdministrationDelegate(Delegate administrationDelegate) {
		this.administrationDelegate = administrationDelegate;
	}

	private EntityManager entityManager;

	private List<TicketSummary> resulList = new ArrayList<TicketSummary>();

	public EntityManager getEntityManager() {

		if (this.entityManager == null) {
			this.entityManager = (EntityManager) Component
					.getInstance("entityManager");
		}
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<TicketSummary> getResultList() {
		if (this.resulList.isEmpty()) {
			buildTicketSummaryResultList();
		}		
		return this.resulList;
	}

	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (TicketSummary ts : this.getResultList()) {
			total = total.add(new BigDecimal(ts.getTotal()));
		}
		return total;
	}
	
	public BigDecimal getTotalTickets() {
		BigDecimal total = BigDecimal.ZERO;
		for (Ticket ts : tickets) {
			total = total.add(ts.getCharge());
		}
		return total;
	}
	
	public void generateSummary(){
		summaryCollectedTickets = new ArrayList<TicketSummary>();
		
		List<Person> operators = new ArrayList<Person>();
		for(Ticket t : tickets){
			if(!operators.contains(t.getCashier())) operators.add(t.getCashier());
		}
		
		for(Person p : operators){
			BigDecimal n = BigDecimal.ZERO;
			for(Ticket t : tickets){
				if(t.getCashier().equals(p)){
					n = n.add(t.getCharge());
				}
			}
			TicketSummary t = new TicketSummary();
			t.setTotal(n.doubleValue());
			t.setOperatorName(p.getName());
			summaryCollectedTickets.add(t);
		}
	}

	private List<Ticket> tickets;
	
	private List<Ticket> invalidatedTickets;
	
	private List<TicketSummary> summaryCollectedTickets;

	public List<TicketSummary> getSummaryCollectedTickets() {
		return summaryCollectedTickets;
	}

	public void setSummaryCollectedTickets(
			List<TicketSummary> summaryCollectedTickets) {
		this.summaryCollectedTickets = summaryCollectedTickets;
	}

	public List<Ticket> getInvalidatedTickets() {
		return invalidatedTickets;
	}

	public void setInvalidatedTickets(List<Ticket> invalidatedTickets) {
		this.invalidatedTickets = invalidatedTickets;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	private void findCollectedTickets(){
		Query query = this.getEntityManager().createNamedQuery("Ticket.findCollectedTicketsInParkingLot");
		query.setParameter("startDate", start);
		query.setParameter("endDate", end);
		query.setParameter("id", parkingLot.getId());
		tickets = query.getResultList();
	}

	public void findTickets() {
		findCollectedTickets();		
		findInvalidatedTickets();	
		generateSummary();
	}
	
	private void findInvalidatedTickets() {
		Query query = this.getEntityManager().createNamedQuery("Ticket.findInvalidatedTicketsInParkingLot");		
		query.setParameter("startDate", start);
		query.setParameter("endDate", end);
		query.setParameter("id", parkingLot.getId());
		invalidatedTickets = query.getResultList();		
	}

	private void buildTicketSummaryResultList() {		
		this.resulList.clear();
		if (isOnlyReport()) {
			this.resulList = this.getNativeResultList(TicketSummaryList.REPORTSQL);
		} else {
			if(ids.trim().isEmpty()){
				this.resulList = new ArrayList<TicketSummary>();
			}else{
				String sql = TicketSummaryList.SQL.replace(":ids", ids);
				this.resulList = this.getNativeResultList(sql);
			}			
		}
	}

	public static Connection getConnection(String jndiName)
			throws NamingException, SQLException {
		InitialContext ic = new InitialContext();
		DataSource ds = (DataSource) ic.lookup(jndiName);
		return ds.getConnection();
	}

	public void changeDatesTime() {
		this.getStart().setHours(0);
		this.getStart().setMinutes(0);
		this.getStart().setSeconds(0);
		this.getEnd().setHours(23);
		this.getEnd().setMinutes(59);
		this.getEnd().setSeconds(59);
	}
	
	public BigDecimal totalForIVADiscount(Double value){
		if(systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		}
		Tax discountTax =  systemParameterService.materialize(Tax.class, "PARKING_TAX_DISCOUNTED");
		BigDecimal res = new BigDecimal(value);
		TaxRate tr = findActiveTaxRate(discountTax);
		if(tr != null){
			res = res.divide(new BigDecimal(1).add(tr.getRate()),4,RoundingMode.HALF_EVEN);				
			return res;
		}
		
		return res;
	}
	
	public TaxRate findActiveTaxRate(Tax t) {
		Query query = getEntityManager().createNamedQuery("TaxRate.findActiveByTaxIdAndPaymentDate");
		query.setParameter("taxId", t.getId());
		query.setParameter("paymentDate", new Date());
		List<TaxRate> list = query.getResultList();
		if(list.size() > 0) return list.get(0);
		return null;		
	}
	
	public BigDecimal getTotalForIVA(){
		if(systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		}		
		Tax discountTax =  systemParameterService.materialize(Tax.class, "PARKING_TAX_DISCOUNTED");		
		BigDecimal total = getTotal();
		TaxRate tr = findActiveTaxRate(discountTax);
		if(tr != null){
			total = total.divide(new BigDecimal(1).add(tr.getRate()),4,RoundingMode.HALF_EVEN);				
			return total;
		}
		return total;
	}
	
	public BigDecimal getTotalSummary(){
		return getTotal().add(getTotalForIVA());		
	}

	private List<TicketSummary> getNativeResultList(String sql) {
		String jndiName = "gimDatasource";
		List<TicketSummary> buffer = new ArrayList<TicketSummary>();
		Connection conn;
		ResultSet rs = null;
		TicketSummary ts = null;
		
		
		try {
			conn = TicketSummaryList.getConnection(jndiName);
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				changeDatesTime();
				st.setTimestamp(1, new Timestamp(this.getStart().getTime()));
				st.setTimestamp(2, new Timestamp(this.getEnd().getTime()));
				st.setLong(3, this.getParkingLot().getId());

				for (rs = st.executeQuery(); rs.next();) {
					ts = new TicketSummary();
					ts.setYear(rs.getInt(1));
					ts.setMonth(rs.getInt(2));
					ts.setDay(rs.getInt(3));
					ts.setCount(rs.getInt(4));
					ts.setTotal(totalForIVADiscount(rs.getDouble(5)).doubleValue());
					buffer.add(ts);
				}

			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
			conn.close();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} catch (NamingException nex) {
			System.err.println(nex.getMessage());
		}
		return buffer;
	}

	public void loadCharge() {
		if (administrationCharge != null)
			return;
		administrationCharge = getCharge("DELEGATE_ID_ADMINISTRATION");

		if (administrationCharge != null) {
			for (Delegate d : administrationCharge.getDelegates()) {
				if (d.getIsActive())
					administrationDelegate = d;
			}
		}
		
	}

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private SystemParameterService systemParameterService;

	public Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,
				systemParameter);
		return charge;
	}

	public List<Ticket> getInvalidatedTicketResulList() {

		String jndiName = "gimDatasource";
		List<Ticket> buffer = new ArrayList<Ticket>();
		Connection conn;
		ResultSet rs = null;
		Ticket t = null;
		Journal j = null;
		Person p = null;
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getDefault());
		try {
			conn = TicketSummaryList.getConnection(jndiName);
			try {
				PreparedStatement st = conn.prepareStatement(SQL_INVALIDATED);
				changeDatesTime();
				st.setTimestamp(1, new Timestamp(this.getStart().getTime()));
				st.setTimestamp(2, new Timestamp(this.getEnd().getTime()));
				st.setLong(3, this.getParkingLot().getId());

				for (rs = st.executeQuery(); rs.next();) {
					t = new Ticket();
					p = new Person();
					j = new Journal();
					t.setId(rs.getLong(1));
					p.setName(rs.getString(2));
					t.setComing(rs.getTimestamp(3, c));

					j.setOperator(p);
					t.setJournal(j);
					buffer.add(t);
				}

			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
			conn.close();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} catch (NamingException nex) {
			System.err.println(nex.getMessage());
		}
		return buffer;
	}

	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}

	public ParkingLot getParkingLot() {
		return parkingLot;
	}
}
