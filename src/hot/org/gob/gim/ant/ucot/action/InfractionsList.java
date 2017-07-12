package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("infractionsList")
public class InfractionsList extends EntityQuery<Infractions> {

	private static final String EJBQL = "select infractions from Infractions infractions";

	private static final String[] RESTRICTIONS = {
			"lower(infractions.article) like lower(concat(#{infractionsList.infractions.article},'%'))",
			"lower(infractions.identification) like lower(concat(#{infractionsList.infractions.identification},'%'))",
			"lower(infractions.licensePlate) like lower(concat(#{infractionsList.infractions.licensePlate},'%'))",
			"lower(infractions.numeral) like lower(concat(#{infractionsList.infractions.numeral},'%'))",};

	private Infractions infractions = new Infractions();

	public InfractionsList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Infractions getInfractions() {
		return infractions;
	}
}
