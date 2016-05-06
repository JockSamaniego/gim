package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("blockLimitList")
public class BlockLimitList extends EntityQuery<BlockLimit> {

	private static final String EJBQL = "select blockLimit from BlockLimit blockLimit";

	private static final String[] RESTRICTIONS = {"lower(blockLimit.street.name) like lower(concat(#{blockLimitList.blockLimit.street.name},'%'))",};

	private BlockLimit blockLimit = new BlockLimit();

	public BlockLimitList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public BlockLimit getBlockLimit() {
		return blockLimit;
	}
}
