package org.gob.gim.parking.action;

import java.util.Arrays;
import java.util.Date;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.parking.model.ParkingLot;
import ec.gob.gim.parking.model.Ticket;


@Name("ticketList")
public class TicketList extends EntityQuery<Ticket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7829139376689892812L;

	private static final String EJBQL = "select ticket from Ticket ticket ";

	private static final String[] RESTRICTIONS = {
		"((lower(ticket.journal.parkingLot.name) like lower(concat(#{ticketList.criteria},'%'))) or " +
		" (lower(ticket.journal.operator.name) like lower(concat(:el1,'%'))) or " +
		" (lower(ticket.numberPlate) like lower(concat(:el1,'%'))))",
		"ticket.id = #{ticketList.id}",
		"ticket.journal.parkingLot = #{ticketList.parkingLot}",
		//"ticket.invalidated = #{ticketList.invalidated} and not ticket.charge is null",
		"ticket.invalidated = #{ticketList.invalidated}",
		"ticket.coming > #{ticketList.coming}",
		"ticket.going < #{ticketList.going}",
		};
	
	private String criteria;
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Date coming;

	private Date going;
	
	private boolean invalidated;

	private Ticket ticket = new Ticket();
	
	private ParkingLot parkingLot;
	

	@In(scope=ScopeType.SESSION, value="userSession")
	UserSession userSession; //Fuente del suscriber
	

	public TicketList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("ticket.coming");
		setOrderDirection("DESC");
		setMaxResults(25);
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		if (this.criteria == null){
			setCriteria("");
		}
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public Date getComing() {
		return coming;
	}

	public void setComing(Date coming) {
		this.coming = coming;
	}

	public Date getGoing() {
		return going;
	}

	public void setGoing(Date going) {
		this.going = going;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public void setInvalidated(boolean invalidated) {
		this.invalidated = invalidated;
	}

	public boolean isInvalidated() {
		return invalidated;
	}
	
	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}

	public ParkingLot getParkingLot() {
		return parkingLot;
	}
	
}
