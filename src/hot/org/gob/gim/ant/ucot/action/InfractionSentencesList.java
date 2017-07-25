package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

@Name("infractionSentencesList")
public class InfractionSentencesList extends EntityQuery<InfractionSentences> {

	private static final String EJBQL = "select infractionSentences from InfractionSentences infractionSentences ORDER BY infractionSentences.id ASC";

	private static final String[] RESTRICTIONS = {};

	private InfractionSentences infractionSentences = new InfractionSentences();

	public InfractionSentencesList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public InfractionSentences getInfractionSentences() {
		return infractionSentences;
	}

	public void setInfractionSentences(InfractionSentences infractionSentences) {
		this.infractionSentences = infractionSentences;
	}
	
}
