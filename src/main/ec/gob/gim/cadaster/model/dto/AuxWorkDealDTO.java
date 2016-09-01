/**
 * 
 */
package ec.gob.gim.cadaster.model.dto;

import ec.gob.gim.common.model.ItemCatalog;

/**
 * @author rene
 *
 */
public class AuxWorkDealDTO {

	private WorkDealFull fraction;
	
	private ItemCatalog action;
	
	public AuxWorkDealDTO(WorkDealFull fraction, ItemCatalog action) {
		this.fraction = fraction;
		this.action = action;
	}

	public WorkDealFull getFraction() {
		return fraction;
	}
	
	public void setFraction(WorkDealFull fraction) {
		this.fraction = fraction;
	}
	
	public ItemCatalog getAction() {
		return action;
	}
	
	public void setAction(ItemCatalog action) {
		this.action = action;
	}
	
}