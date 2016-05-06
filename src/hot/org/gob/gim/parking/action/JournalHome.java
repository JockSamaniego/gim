package org.gob.gim.parking.action;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.parking.model.Journal;

@Name("journalHome")
public class JournalHome extends EntityHome<Journal> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3488019640553145387L;

	@Logger 
	Log logger;
	
	private Journal journal = null;
	
	@In(scope=ScopeType.SESSION, value="userSession")
	UserSession userSession; //Fuente del operator
	

	public void setParkingLotId(Long id) {
		setId(id);
	}

	public Long getParkingLotId() {
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

	public Journal getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public Journal getJournal() {
		return journal;
	}

}
