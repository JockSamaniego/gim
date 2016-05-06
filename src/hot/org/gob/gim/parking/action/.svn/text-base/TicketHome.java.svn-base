package org.gob.gim.parking.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.parking.model.Ticket;
import ec.gob.gim.parking.model.TimeUnit;

@Name("ticketHome")
public class TicketHome extends EntityHome<Ticket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3488019640553145387L;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	public static final String ROLE_NAME_PARKING_ADMINISTRATOR = "ROLE_NAME_PARKING_ADMINISTRATOR";

	@Logger
	Log logger;

	private Ticket ticket = null;
	
	private SystemParameterService systemParameterService;

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	private Long temporalId;

	public Long getTemporalId() {
		return temporalId;
	}

	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession; // Fuente del operator

	@In
	Conversation conversation;

	public void setTicketId(Long id) {
		setId(id);
	}

	public Long getTicketId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Ticket getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Date getTime() {
		return Calendar.getInstance().getTime();
	}
	
	public String update() {

		if (userSession.getJournal() == null) {
			addFacesMessageFromResourceBundle("Ticket.NoJournalActive");
			return "failed";
		} else {
			this.getInstance().setManager(userSession.getPerson());
			super.update();
			this.setTicketId(null);
			conversation.end();
		}
		return "updated";
	}
	
	public String onlyUpdate() {	
		return super.update();
	}


	public String persist() {
		if (userSession.getJournal() == null) {
			addFacesMessageFromResourceBundle("Ticket.NoJournalActive");
			return "failed";
		} else {
			Long n = findNumberOfBusySpacesInParkingLot(userSession.getJournal().getParkingLot().getId(), Calendar.getInstance().getTime());
			Long nc = findNumberOfBusySpacesForContractInParkingLot(userSession.getJournal().getParkingLot().getId(), Calendar.getInstance().getTime());
			if ((n + nc) >= userSession.getJournal().getParkingLot().getParkings()) {
				addFacesMessageFromResourceBundle("parkingLot.noEmptySpaces");
				return "failed";
			}
			this.getInstance().setJournal(userSession.getJournal());
			this.getInstance().setComing(this.getTime());
			this.getInstance().setTimeUnit(TimeUnit.HOUR); // Tarifa de hora por
															// defecto
			super.persist();
		}

		return "persisted";
	}

	public String confirmPrinting() {
		return "printed";
	}

	public void persistAndRedirect() {
		if (persist().equals("persisted")) {
			Redirect rd = Redirect.instance();
			rd.setViewId("/parking/report/Ticket.xhtml");
			rd.execute();
		}
		this.setId(null);
		this.getInstance();
	}

	public String generateNewTicket() {
		this.setTicketId(null);
		return "generated";
	}
	
	public Long findNumberOfBusySpacesInParkingLot(Long parkinLotId, Date d) {
		Query query = this.getEntityManager().createNamedQuery(
				"Ticket.findNumberOfBusySpacesInParkingLot");
		query.setParameter("id", parkinLotId);
		query.setParameter("comingDate", d);
		return (Long) query.getSingleResult();
	}

	public Long findNumberOfBusySpacesForContractInParkingLot(Long parkinLotId,
			Date d) {
		Query query = this.getEntityManager().createNamedQuery(
				"ParkingRent.findNumberOfContracsInParkingLotByDate");
		query.setParameter("id", parkinLotId);
		query.setParameter("comingDate", d);
		return (Long) query.getSingleResult();
	}

	public String receipt() {

		if (userSession.getJournal() == null) {
			addFacesMessageFromResourceBundle("Ticket.NoJournalActive");
			return "failed";
		} else if (this.getInstance().getCharge() != null) {
			this.getInstance().setManager(userSession.getPerson());
			super.update();
			// this.setTicketId(null);
			// conversation.end();
		} else {
			this.getInstance().setCashier(userSession.getPerson());
			this.getInstance().setManager(userSession.getPerson());
			this.getInstance().setGoing(this.getTime());
			this.getInstance().setCharge(calculateCharge());
			super.update();
			// this.setTicketId(null);
			// conversation.end();
		}

		return "updated";
	}
	
	public boolean hasParkingAdministratorRole(){
		return userSession.hasRole(ROLE_NAME_PARKING_ADMINISTRATOR);
	}

	BigDecimal value;
	
	public BigDecimal getValueForHour(){
		if(value != null ) return value;
		if (systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		}
		value = systemParameterService.findParameter("PARKING_VALUE_FOR_HOUR");
		return value;
	}
	
	private BigDecimal charge(long periodInMillis) {
		return getValueForHour().multiply(new BigDecimal(calculePeriod(periodInMillis)));
	}

	public BigDecimal calculateCharge() {
		BigDecimal res = BigDecimal.ZERO;
		if (this.getInstance().getTimeUnit().equals(TimeUnit.FREE)) return res;
		return charge(this.getInstance().getTimeTakenInMillis());
	}
	
	private Long extendedTime;
	
	public Long getExtendedTime(){
		if(extendedTime != null ) return extendedTime;
		if (systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		}
		extendedTime = systemParameterService.findParameter("PARKING_EXTENDED_TIME");
		return extendedTime;
	}
	
	//falta el extended time que es los minutos libres
	//falta el charge que es el valor por hora

	private long calculePeriod(long periodInMillis) {
		// modulo
		long periodInMin = java.util.concurrent.TimeUnit.MILLISECONDS
				.toMinutes(periodInMillis);
		long rest = periodInMin % 60;
		// residuo
		long _period = (periodInMin - rest) / 60; // obtiene la parte entera
														// siempre es un
														// multiplo

		// Si se exede el tiempo de gracia, sumar uno
		if (_period == 0 && periodInMin > getExtendedTime()) {
			_period = 1; // Hora o fracción
		} else if (rest > getExtendedTime()) {
			_period = _period + 1;
		}

		return _period;
	}

	public boolean existTicket() {
		Query query = getEntityManager().createNamedQuery(
				"Ticket.findByIdAndNoPaidAndNoInvalidated");
		query.setParameter("id", temporalId);
		if (query.getResultList().size() > 0)
			return true;
		return false;
	}

	public String print() {
		if (!existTicket()) {
			addFacesMessageFromResourceBundle("Ticket.canNotEmitReceipt");
			return "failed";
		}
		setId(temporalId);
		getInstance();
		return this.receipt();
	}

	@Factory("timeUnits")
	public List<TimeUnit> loadTimeUnits() {
		return Arrays.asList(TimeUnit.values());
	}

	public boolean isJournalEnabled() {
		return this.userSession.getJournal() != null;
	}
}
