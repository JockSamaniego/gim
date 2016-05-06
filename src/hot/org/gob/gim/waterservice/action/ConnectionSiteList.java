package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("connectionSiteList")
public class ConnectionSiteList extends EntityQuery<ConnectionSite> {

	private static final String EJBQL = "select connectionSite from ConnectionSite connectionSite";

	private static final String[] RESTRICTIONS = {"lower(connectionSite.observations) like lower(concat(#{connectionSiteList.connectionSite.observations},'%'))",};

	private ConnectionSite connectionSite = new ConnectionSite();

	public ConnectionSiteList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ConnectionSite getConnectionSite() {
		return connectionSite;
	}
}
