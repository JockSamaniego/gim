package org.gob.gim.income.view;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;

import ec.gob.gim.income.model.PaymentRestriction;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;

public class MunicipalBondItem {
	private Boolean isSelected;
	private MunicipalBond municipalBond;

	private Map<String, MunicipalBondItem> children;
	private MunicipalBondItem parent;	

	private SystemParameterService systemParameterService;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private Boolean enableCheckBox = true;
	
	public MunicipalBondItem(MunicipalBond municipalBond) {
		isSelected = Boolean.FALSE;
		this.municipalBond = municipalBond;
		children = new LinkedHashMap<String, MunicipalBondItem>();
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String paymentRestrictionValue = systemParameterService.findParameter("PAYMENT_RESTRICTION_VALUE");
		if ((paymentRestrictionValue.equals(PaymentRestriction.ALL_EXPIRED.toString())) && (municipalBond != null)){
			if ((municipalBond.getExpirationDate().before(GregorianCalendar.getInstance().getTime())) ||
			(municipalBond.getExpirationDate().equals(GregorianCalendar.getInstance().getTime()))){
				if (municipalBond.getResident() != null){ 
					if (!municipalBond.getResident().isEnabledIndividualPayment()) {
						this.enableCheckBox = false;
					}
					else{
						this.enableCheckBox = true;						
					}
				} else
					this.enableCheckBox = false;
			}
			else
				this.enableCheckBox = true;
			
		} else
			this.enableCheckBox = true;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
		if (children.values() != null && children.values().size() > 0) {
			for (MunicipalBondItem mbi : children.values()) {
				mbi.setIsSelected(isSelected);
			}
		}
		else {
			List<MunicipalBondItem> partners = this.getParent().getMunicipalBondItems();
			if (isSelected) {
				for (MunicipalBondItem mbi : partners) {
					if (this == mbi) {
						break;
					}
					mbi.changeSelection(isSelected);
				}
			} else {
				Boolean change = Boolean.FALSE;
				for (MunicipalBondItem mbi : partners) {
					if (change) {
						mbi.changeSelection(isSelected);
					}
					if (this == mbi) {
						change = Boolean.TRUE;
					}
				}
				
			}
		}
	}

	private void changeSelection(Boolean newSelection){
		this.isSelected = newSelection;
	}
	
	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public void add(MunicipalBondItem municipalBondItem) {
		children.put(municipalBondItem.toString(), municipalBondItem);
		municipalBondItem.setParent(this);
	}

	public MunicipalBondItem findNode(String code, MunicipalBond municipalBond) {
		MunicipalBondItem item = children.get(code);
		if (item == null) {
			MunicipalBond clone = new MunicipalBond();
			clone.setEntry(municipalBond.getEntry());
			clone.setGroupingCode(municipalBond.getGroupingCode());
			clone.setMunicipalBondStatus(municipalBond.getMunicipalBondStatus());
			clone.setAdjunct(municipalBond.getAdjunct());
			item = new MunicipalBondItem(clone);
			children.put(code, item);
		}
		return item;
	}

	public List<MunicipalBondItem> getMunicipalBondItems() {
		List<MunicipalBondItem> list = new LinkedList<MunicipalBondItem>();
		list.addAll(children.values());
		return list;
	}

	public BigDecimal calculatePaymentTotal() {
		BigDecimal paymentTotal = BigDecimal.ZERO;
		if (municipalBond != null) {
			if (children.size() > 0) {
				for (MunicipalBondItem mbi : children.values()) {
					paymentTotal = paymentTotal.add(mbi.calculatePaymentTotal());
				}
			} else {
				if (isSelected) {
					System.out.println("TOTAL AGREGADO ----> " + municipalBond.getEntry().getName() + " - " + municipalBond.getValue());
					return municipalBond.getPaidTotal();
				}
			}
		}
		return paymentTotal;
	}
	
	public Long parentsNumber(MunicipalBondItem mbi){
		Long aux = 0L;
		MunicipalBondItem parent = mbi.getParent();
		while(parent != null){
			aux++;
			parent = parent.getParent();
		}
		return aux;
	}

	public void calculateTotals(MunicipalBondStatus municipalBondStatus, MunicipalBondStatus inAgreementBondStatus) {
		if (municipalBond != null) {
			if (children.size() > 0) {
				BigDecimal value = BigDecimal.ZERO;
				BigDecimal interest = BigDecimal.ZERO;
				BigDecimal discount = BigDecimal.ZERO;
				BigDecimal surcharge = BigDecimal.ZERO;
				BigDecimal paidTotal = BigDecimal.ZERO;
				BigDecimal taxesTotal = BigDecimal.ZERO;
				BigDecimal totalCancelled = BigDecimal.ZERO;

				for (MunicipalBondItem mbi : children.values()) {				
					mbi.calculateTotals(municipalBondStatus, inAgreementBondStatus);					
					if(municipalBondStatus == null || 
							mbi.getMunicipalBond().getMunicipalBondStatus().getId().equals(municipalBondStatus.getId()) || 
							parentsNumber(mbi) == 0 ||
							mbi.getMunicipalBond().getMunicipalBondStatus().getId().equals(inAgreementBondStatus.getId())){
						if(inAgreementBondStatus!= null && mbi.getMunicipalBond().getMunicipalBondStatus().getId().equals(inAgreementBondStatus.getId())){
							value = value.add(mbi.getMunicipalBond().getPaidTotal().subtract(mbi.getMunicipalBond().getInterest()).subtract(mbi.getMunicipalBond().getSurcharge()).add(mbi.getMunicipalBond().getDiscount()));
						}else{
							value = value.add(mbi.getMunicipalBond().getValue());							
						}					
						paidTotal = paidTotal.add(mbi.getMunicipalBond().getPaidTotal());
						interest = interest.add(mbi.getMunicipalBond().getInterest());
						discount = discount.add(mbi.getMunicipalBond().getDiscount());
						surcharge = surcharge.add(mbi.getMunicipalBond().getSurcharge());
						taxesTotal = taxesTotal.add(mbi.getMunicipalBond().getTaxesTotal());
						totalCancelled = totalCancelled.add(mbi.getMunicipalBond().getTotalCancelled() == null ? BigDecimal.ZERO : mbi.getMunicipalBond().getTotalCancelled());
					}					
				}
				
				municipalBond.setValue(value);
				municipalBond.setTotalCancelled(totalCancelled);
				municipalBond.setPaidTotal(paidTotal);
				municipalBond.setInterest(interest);
				municipalBond.setDiscount(discount);
				municipalBond.setSurcharge(surcharge);
				municipalBond.setTaxesTotal(taxesTotal);				
				System.out.println("TOTAL CALCULADO ----> "	+ municipalBond.getEntry().getName() + " - " + municipalBond.getValue().floatValue());
			}

		}
		// return paymentTotal;
	}

	public String getServiceDate() {
		if (municipalBond != null) {
			return municipalBond.getTransformedServiceDate();
		}
		return "";
	}

	public void fillSelected(List<MunicipalBond> selectedMunicipalBonds) {
		if (municipalBond != null) {
			if (children.size() > 0) {
				for (MunicipalBondItem mbi : children.values()) {
					mbi.fillSelected(selectedMunicipalBonds);
				}
			} else {
				if (isSelected) {
					selectedMunicipalBonds.add(municipalBond);
				}
			}
		}
	}

	public MunicipalBondItem getParent() {
		return parent;
	}

	public void setParent(MunicipalBondItem parent) {
		this.parent = parent;
	}
	/*
	 * @Override public String toString() { return
	 * municipalBond.getEntry().getId()+"-"+municipalBond.getGroupingCode(); }
	 */

	public Boolean getEnableCheckBox() {
		return enableCheckBox;
	}

	public void setEnableCheckBox(Boolean enableCheckBox) {
		this.enableCheckBox = enableCheckBox;
	}

}
