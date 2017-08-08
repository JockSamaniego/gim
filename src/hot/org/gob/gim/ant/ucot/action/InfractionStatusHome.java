package org.gob.gim.ant.ucot.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.ant.ucot.model.*;
import ec.gob.gim.common.model.ItemCatalog;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("infractionStatusHome")
public class InfractionStatusHome extends EntityHome<InfractionStatus> {

	private List<ItemCatalog> typesStatus;
	private ItemCatalogService itemCatalogService;
	
	@In(create = true)
	InfractionsHome infractionsHome;
	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setInfractionStatusId(Long id) {
		setId(id);
	}

	public Long getInfractionStatusId() {
		return (Long) getId();
	}

	@Override
	protected InfractionStatus createInstance() {
		InfractionStatus infractionStatus = new InfractionStatus();
		return infractionStatus;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Infractions infraction = infractionsHome.getDefinedInstance();
		if (infraction != null) {
			getInstance().setInfraction(infraction);
		}
		/*ItemCatalog status = itemCatalogHome.getDefinedInstance();
		if (status != null) {
			getInstance().setStatus(status);
		}*/
		
		initializeService();
		typesStatus = new ArrayList<ItemCatalog>();
		typesStatus = itemCatalogService.findItemsForCatalogCode(
						CatalogConstants.CATALOG_TYPES_STATUS);
	}

	public boolean isWired() {
		return true;
	}

	public InfractionStatus getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public void initializeService() {
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
	}

	public List<ItemCatalog> getTypesStatus() {
		return typesStatus;
	}

	public void setTypesStatus(List<ItemCatalog> typesStatus) {
		this.typesStatus = typesStatus;
	}
	
	private List<InfractionStatus> infractionStatus;

	
	public List<InfractionStatus> getInfractionStatus() {
		return infractionStatus;
	}

	public void setInfractionStatus(List<InfractionStatus> infractionStatus) {
		this.infractionStatus = infractionStatus;
	}

	private Long infraction_id;
	

	public Long getInfraction_id() {
		return infraction_id;
	}

	public void setInfraction_id(Long infraction_id) {
		this.infraction_id = infraction_id;
	}

	public void searchStatus(){
		Infractions infraction = infractionsHome.getDefinedInstance();
		infractionStatus = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"status.findByInfractionId");
		query.setParameter("infractionId", infraction.getId());
		infractionStatus = query.getResultList();
	}

}
