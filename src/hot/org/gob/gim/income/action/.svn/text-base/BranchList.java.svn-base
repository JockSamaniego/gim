package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("branchList")
public class BranchList extends EntityQuery<Branch> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select branch from Branch branch";

	private static final String[] RESTRICTIONS = {"lower(branch.name) like lower(concat(#{branchList.branch.name},'%'))",
												  "branch.number = #{branchList.branch.number}",};

	private Branch branch = new Branch();

	public BranchList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Branch getBranch() {
		return branch;
	}
	
	@Override
	public String getOrder() {
		return "branch.number";
	}
}
