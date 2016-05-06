package org.gob.gim.waterservice.action;

import org.gob.gim.common.action.PersonHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.waterservice.model.Route;
import ec.gob.gim.waterservice.model.RoutePeriod;

@Name("routePeriodHome")
public class RoutePeriodHome extends EntityHome<RoutePeriod> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In(create = true)
	PersonHome personHome;
	@In(create = true)
	RouteHome routeHome;

	public void setRoutePeriodId(Long id) {
		setId(id);
	}

	public Long getRoutePeriodId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Person readingMan = personHome.getDefinedInstance();
		if (readingMan != null) {
			getInstance().setReadingMan(readingMan);
		}
		Route route = routeHome.getDefinedInstance();
		if (route != null) {
			getInstance().setRoute(route);
		}
	}

	public boolean isWired() {
		return true;
	}

	public RoutePeriod getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
