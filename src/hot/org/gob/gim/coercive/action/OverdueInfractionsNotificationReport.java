/**
 * 
 */
package org.gob.gim.coercive.action;

import java.util.List;

import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

/**
 * @author René
 *
 */
@Name("overdueInfractionsNotificationReport")
public class OverdueInfractionsNotificationReport extends EntityController{
	
	private DatainfractionService datainfractionService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String selectedItems = "";
	
	private boolean isFirstTime = true;
	
	public void initialize(){
		if (isFirstTime) {
			if (datainfractionService == null) {
				datainfractionService = ServiceLocator.getInstance().findResource(
						datainfractionService.LOCAL_NAME);
			}
			
			System.out.println(this.getSelectedItemAsList());
		}
		
	}

	/**
	 * @return the selectedItems
	 */
	public String getSelectedItems() {
		return selectedItems;
	}

	/**
	 * @param selectedItems the selectedItems to set
	 */
	public void setSelectedItems(String selectedItems) {
		this.selectedItems = selectedItems;
	}
	
	public List<Long> getSelectedItemAsList() {
		return org.gob.gim.common.Util.splitArrayString(this.getSelectedItems());
	}


}
