package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("bulletinList")
public class BulletinList extends EntityQuery<Bulletin> {

	private static final String EJBQL = "select bulletin from Bulletin bulletin";

	private static final String[] RESTRICTIONS = {};

	private Bulletin bulletin = new Bulletin();

	public BulletinList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Bulletin getBulletin() {
		return bulletin;
	}
}
