package org.gob.gim.cadaster.action;

import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.cadaster.model.*;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

@Name("buildingMaterialValueHome")
public class BuildingMaterialValueHome extends EntityHome<BuildingMaterialValue> {
	
	@In
	FacesMessages facesMessages;

	public void setBuildingMaterialValueId(Long id) {
		setId(id);
	}

	public Long getBuildingMaterialValueId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}
	
	public BuildingMaterialValue getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	/**
	 * Persiste o actualiza un BuildingMaterialValue
	 * @return String 'persisted' en caso de guardar, 'updated' en caso de actualizar o 'failed' en caso de error 
	 */
	public String save(){
		if (existsBuildingMaterialValue()) {
			String message = Interpolator.instance().interpolate("#{messages['existsBuildingMaterialValue']}", new Object[0]);
			facesMessages.addToControl("structureMaterial", org.jboss.seam.international.StatusMessage.Severity.ERROR, message);
			return "failed";
		}
		
		if(isManaged()){
			return super.update();
		}else{
			return super.persist();
		}
	}
		
	private boolean existsBuildingMaterialValue(){
		List<BuildingMaterialValue> values = findBuildingMaterialValues();
		if(values.size() > 0 && !this.isManaged()){
			return true;
		}
		
		if(findBuildingMaterialValues().size() == 1 && this.isManaged() && !values.get(0).getId().equals(this.instance.getId())){
			return true;
		}
		return false;
	}
	
	private List<BuildingMaterialValue> findBuildingMaterialValues() {		
		Query query = this.getEntityManager().createNamedQuery("BuildingMaterialValue.findByStructureMaterialAndFiscalPeriod");
		query.setParameter("structureMaterial", this.getInstance().getStructureMaterial());
		query.setParameter("fiscalPeriodId", this.getInstance().getFiscalPeriod().getId());
		return (List<BuildingMaterialValue>) query.getResultList();
	}

}
