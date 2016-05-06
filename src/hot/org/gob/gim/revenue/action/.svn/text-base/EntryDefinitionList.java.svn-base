package org.gob.gim.revenue.action;

import ec.gob.gim.revenue.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("entryDefinitionList")
public class EntryDefinitionList extends EntityQuery<EntryDefinition> {

	private static final String EJBQL = "select entryDefinition from EntryDefinition entryDefinition";

	private static final String[] RESTRICTIONS = {
			"lower(entryDefinition.dateFormat) like lower(concat(#{entryDefinitionList.entryDefinition.dateFormat},'%'))",
			"lower(entryDefinition.ruleName) like lower(concat(#{entryDefinitionList.entryDefinition.ruleName},'%'))",};

	private EntryDefinition entryDefinition = new EntryDefinition();

	public EntryDefinitionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public EntryDefinition getEntryDefinition() {
		return entryDefinition;
	}
}
