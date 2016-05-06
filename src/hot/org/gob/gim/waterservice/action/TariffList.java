package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tariffList")
public class TariffList extends EntityQuery<Tariff> {

	private static final String EJBQL = "select tariff from Tariff tariff";

	private static final String[] RESTRICTIONS = {};

	private Tariff tariff = new Tariff();

	public TariffList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tariff getTariff() {
		return tariff;
	}
}
