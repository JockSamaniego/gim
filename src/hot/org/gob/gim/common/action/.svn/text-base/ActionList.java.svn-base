package org.gob.gim.common.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.security.model.Action;

@Name("actionList")
public class ActionList extends EntityQuery<Action> {

	private static final String EJBQL = "select action from Action action";

	private static final String[] RESTRICTIONS = {
			"lower(action.name) like lower(concat(#{actionList.action.name},'%'))",};

	private Action action = new Action();

	public ActionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Action getAction() {
		return action;
	}
}
