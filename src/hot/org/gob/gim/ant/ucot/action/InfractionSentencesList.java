package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("infractionSentencesList")
public class InfractionSentencesList extends EntityQuery<InfractionSentences> {

	private static final String EJBQL = "select infractionSentences from InfractionSentences infractionSentences ORDER BY infractionSentences.id ASC";

	private static final String[] RESTRICTIONS = {
			"lower(infractionSentences.description) like lower(concat(#{infractionSentencesList.infractionSentences.description},'%'))",
			"lower(infractionSentences.judge) like lower(concat(#{infractionSentencesList.infractionSentences.judge},'%'))",
			"lower(infractionSentences.processNumber) like lower(concat(#{infractionSentencesList.infractionSentences.processNumber},'%'))",};

	private InfractionSentences infractionSentences = new InfractionSentences();

	public InfractionSentencesList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public InfractionSentences getInfractionSentences() {
		return infractionSentences;
	}
}
