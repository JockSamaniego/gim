package org.gob.gim.cadaster.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.RightsTransfer;

@Name("rightsTransferList")
public class RightsTransferList extends EntityQuery<RightsTransfer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String seller;
	private String buyer;

	//private static final String EJBQL = "select entry from Entry entry left join fetch entry.parent";
	private static final String EJBQL = "select rt from RightsTransfer rt left join rt.property ";
	
	private static final String[] RESTRICTIONS= {
		"lower(rt.seller.name) like lower(concat(#{rightsTransferList.seller},'%'))",
		"lower(rt.buyer.name) like lower(concat(#{rightsTransferList.buyer},'%'))",};

	
	public RightsTransferList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrder("rt.id");
	}


	public String getSeller() {
		return seller;
	}


	public void setSeller(String seller) {
		this.seller = seller;
	}


	public String getBuyer() {
		return buyer;
	}


	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}	
	
}
