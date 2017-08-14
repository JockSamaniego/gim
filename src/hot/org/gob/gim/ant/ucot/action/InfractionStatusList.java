package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("infractionStatusList")
public class InfractionStatusList extends EntityQuery<InfractionStatus> {

	private static final String EJBQL = "select infractionStatus from InfractionStatus infractionStatus";

	private static final String[] RESTRICTIONS = {};

	private InfractionStatus infractionStatus = new InfractionStatus();

	public InfractionStatusList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public InfractionStatus getInfractionStatus() {
		return infractionStatus;
	}
}
