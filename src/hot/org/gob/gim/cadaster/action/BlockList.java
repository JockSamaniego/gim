package org.gob.gim.cadaster.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.Block;
import ec.gob.gim.cadaster.model.TerritorialDivision;

@Name("blockList")
public class BlockList extends EntityQuery<Block> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select block from Block block left join fetch block.limits limit left join fetch limit.street";

	private static final String[] RESTRICTIONS = {
			"lower(block.cadastralCode) like lower(concat('%',#{blockList.block.cadastralCode},'%'))",
			"lower(block.sector.parent.parent.name) like lower(concat(#{blockList.parishName},'%'))",
			"lower(limit.street.name) like lower(concat(#{blockList.streetName},'%'))"};

	private String streetName;
	private String parishName;
	private Block block = new Block();
	
	
	
	private TerritorialDivision territorialDivision;

	public BlockList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("block.cadastralCode");
		setOrderDirection("asc");
		setMaxResults(25);
	}

	public Block getBlock() {
		return block;
	}

	public TerritorialDivision getTerritorialDivision() {
		return territorialDivision;
	}

	public void setTerritorialDivision(TerritorialDivision territorialDivision) {
		this.territorialDivision = territorialDivision;
	}
	
	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getParishName() {
		return parishName;
	}

	public void setParishName(String parishName) {
		this.parishName = parishName;
	}
}
