package org.gob.gim.revenue.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.revenue.model.TimePeriod;

@Name("timePeriodHome")
public class TimePeriodHome extends EntityHome<TimePeriod> {

	private static final long serialVersionUID = 1L;

	public void setTimePeriodId(Long id) {
		setId(id);
	}

	public Long getTimePeriodId() {
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

	public TimePeriod getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
