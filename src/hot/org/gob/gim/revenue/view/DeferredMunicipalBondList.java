package org.gob.gim.revenue.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.gob.gim.revenue.model.MunicipalBond;

public class DeferredMunicipalBondList {

	private List<MunicipalBond> municipalBonds;
	private BigDecimal total;
	

	public DeferredMunicipalBondList() {
		// TODO Auto-generated constructor stub
		municipalBonds = new ArrayList<MunicipalBond>();
		total = new BigDecimal(0);
	}
	
	public void add(MunicipalBond municipalBond){
		municipalBonds.add(municipalBond);
		total = total.add(municipalBond.getPaidTotal());
	}
	
	public void remove(MunicipalBond municipalBond){
		municipalBonds.remove(municipalBond);
		total = total.subtract(municipalBond.getPaidTotal());
	}

	/**
	 * @return the municipalBonds
	 */
	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	/**
	 * @param municipalBonds the municipalBonds to set
	 */
	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	 
}
