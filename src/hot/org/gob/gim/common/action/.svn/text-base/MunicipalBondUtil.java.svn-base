package org.gob.gim.common.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.gob.gim.income.view.MunicipalBondItem;

import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;

public class MunicipalBondUtil {
	
	private static MunicipalBondStatus municipalBondStatus;
	
	private static MunicipalBondStatus inAgreementStatus;
	
	public static MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public static void setMunicipalBondStatus(
			MunicipalBondStatus municipalBondStatus) {
		MunicipalBondUtil.municipalBondStatus = municipalBondStatus;
	}

	public static List<MunicipalBondItem> fillMunicipalBondItems(List<MunicipalBond> municipalBonds){		
		
		MunicipalBondItem root = new MunicipalBondItem(null);
		for (MunicipalBond municipalBond : municipalBonds) {
			String entryId = municipalBond.getEntry().getId().toString();
			MunicipalBondItem item = root.findNode(entryId, municipalBond);
	
			String groupingCode = municipalBond.getGroupingCode();
			MunicipalBondItem groupingItem = item.findNode(groupingCode,municipalBond);
			if(municipalBond.getMunicipalBondStatus().equals(municipalBondStatus)) municipalBond.setTotalCancelled(BigDecimal.ZERO);
			if(municipalBond.getMunicipalBondStatus().equals(inAgreementStatus)){				
				municipalBond.setTotalCancelled(sumTotalCancelled(municipalBond));
			}
			MunicipalBondItem mbi = new MunicipalBondItem(municipalBond);
			groupingItem.add(mbi);
		}
				
		for (MunicipalBondItem mbi : root.getMunicipalBondItems()) {			
			mbi.calculateTotals(municipalBondStatus, inAgreementStatus);	
			
		}
		return root.getMunicipalBondItems();
	}
	
	private static BigDecimal sumTotalCancelled(MunicipalBond mb){
		BigDecimal res = BigDecimal.ZERO;
		List<Long> ids = new ArrayList<Long>();
		for(Deposit d: mb.getDeposits()){
			if(!ids.contains(d.getId())){
				res = res.add(d.getValue());
				ids.add(d.getId());
			}
		}		
		return res;
	}

	public static MunicipalBondStatus getInAgreementStatus() {
		return inAgreementStatus;
	}

	public static void setInAgreementStatus(MunicipalBondStatus inAgreementStatus) {
		MunicipalBondUtil.inAgreementStatus = inAgreementStatus;
	}


}
