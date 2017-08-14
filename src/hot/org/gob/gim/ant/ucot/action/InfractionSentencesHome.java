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

@Name("infractionSentencesHome")
public class InfractionSentencesHome extends EntityHome<InfractionSentences> {
	
	private List<ItemCatalog> typesSentence;
	private ItemCatalogService itemCatalogService;
	
	@In(create = true)
	InfractionsHome infractionsHome;

	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setInfractionSentencesId(Long id) {
		setId(id);
	}

	public Long getInfractionSentencesId() {
		return (Long) getId();
	}

	@Override
	protected InfractionSentences createInstance() {
		InfractionSentences infractionSentences = new InfractionSentences();
		return infractionSentences;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		/*ItemCatalog type = itemCatalogHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}*/
		Infractions infraction = infractionsHome.getDefinedInstance();
		if (infraction != null) {
			getInstance().setInfraction(infraction);
		}
		initializeService();
		typesSentence = new ArrayList<ItemCatalog>();
		typesSentence = itemCatalogService.findItemsForCatalogCode(
						CatalogConstants.CATALOG_TYPES_SENTENCE);
		
	}

	public boolean isWired() {
		return true;
	}
	
	public void initializeService() {
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
	}

	public InfractionSentences getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<ItemCatalog> getTypesSentence() {
		return typesSentence;
	}

	public void setTypesSentence(List<ItemCatalog> typesSentence) {
		this.typesSentence = typesSentence;
	}

	private List<InfractionSentences> infractionSentences;

	public List<InfractionSentences> getInfractionSentences() {
		return infractionSentences;
	}

	public void setInfractionSentences(List<InfractionSentences> infractionSentences) {
		this.infractionSentences = infractionSentences;
	}
	
	private Long infraction_id;
	

	public Long getInfraction_id() {
		return infraction_id;
	}

	public void setInfraction_id(Long infraction_id) {
		this.infraction_id = infraction_id;
	}

	public void searchSentences(){
		Infractions infraction = infractionsHome.getDefinedInstance();
		infractionSentences = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"sentence.findByInfractionId");
		query.setParameter("infractionId", infraction.getId());
		infractionSentences = query.getResultList();
	}
	
}
