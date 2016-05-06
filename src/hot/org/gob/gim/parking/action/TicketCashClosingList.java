package org.gob.gim.parking.action;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.rental.facade.RentalService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.parking.model.ParkingLot;
import ec.gob.gim.parking.model.Ticket;
import ec.gob.gim.revenue.model.EmissionOrder;


@Name("ticketCashClosingList")
@Scope(ScopeType.CONVERSATION)
public class TicketCashClosingList extends EntityQuery<Ticket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7829139376689892812L;

	private static final String EJBQL = "select ticket from Ticket ticket ";

	private static final String[] RESTRICTIONS = {
		"((lower(ticket.journal.parkingLot.name) like lower(concat(#{ticketCashClosingList.criteria},'%'))) or " +
		" (lower(ticket.journal.operator.name) like lower(concat(:el1,'%'))) or " +
		" (lower(ticket.numberPlate) like lower(concat(:el1,'%'))))",
		"ticket.journal.parkingLot = #{ticketCashClosingList.parkingLot}",
		"ticket.invalidated = #{ticketCashClosingList.invalidated} and ticket.emissionOrder is null and not ticket.charge is null",
		"ticket.going >= #{ticketCashClosingList.coming}",
		"ticket.going <= #{ticketCashClosingList.going}"
		};
	
	private String criteria;
	
	private boolean userReport = false;
	
	public boolean isUserReport() {
		return userReport;
	}

	public void setUserReport(boolean userReport) {
		this.userReport = userReport;
	}


	private Date coming;

	private Date going;
	
	private boolean invalidated;

	private Ticket ticket = new Ticket();
	
	private ParkingLot parkingLot;
	
	private RentalService rentalService;
	

	@In(scope=ScopeType.SESSION, value="userSession")
	UserSession userSession; //Fuente del suscriber
	

	public TicketCashClosingList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
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
	
	
	/* Cash closing for ticket list
	 * 
	 */
	@In (create=true)
	TicketHome ticketHome;

	public void changeDatesTime(){
		this.getComing().setHours(0);
		this.getComing().setMinutes(0);
		this.getComing().setSeconds(0);
		this.getGoing().setHours(23);
		this.getGoing().setMinutes(59);
		this.getGoing().setSeconds(59);
	}
	
	private boolean isOnlyReport = true;
		
	public boolean isOnlyReport() {		
		return isOnlyReport;
	}

	public void setOnlyReport(boolean isOnlyReport) {		
		this.isOnlyReport = isOnlyReport;
	}
	
	private String ids="";

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@In
	FacesMessages facesMessages;

	public String cashClosing(){		
		if(!isOnlyReport){
			Calendar ca = Calendar.getInstance();			
			if(going.after(ca.getTime())){
				String message = Interpolator.instance().interpolate(
						"#{messages['parkingLot.noValidGoingDate']}", new Object[0]);
				facesMessages.addToControl("",
						org.jboss.seam.international.StatusMessage.Severity.ERROR,
						message);
				return "failed";
			}
			EmissionOrder emissionOrder = null;
			changeDatesTime();
						
			try {
				emissionOrder = this.getRentalService().preEmitCashClosingParkingRent(this, userSession.getFiscalPeriod(), userSession.getPerson());				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			if(emissionOrder != null && emissionOrder.getId() != null){
				ids = "";
				for (Ticket t : this.getResultList()){
					if (t.getCharge() != null){
						t.setEmissionOrder(emissionOrder);
						ticketHome.setInstance(t);
						ticketHome.onlyUpdate();
						ids = ids + "," + t.getId();
					}
				}
				ids = ids.substring(1);
			}
			
		}
		if(userReport) return "reportByUser";
		
		return "closed";
	}
			
	public void setRentalService(RentalService rentalService) {
		this.rentalService = rentalService;
	}


	public RentalService getRentalService() {
		if (rentalService == null)
			rentalService = ServiceLocator.getInstance().findResource(
					ParkingRentHome.RENTAL_SERVICE_NAME);
		return rentalService;
	}

	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}

	public ParkingLot getParkingLot() {
		return parkingLot;
	}
	
}
