package org.gob.gim.revenue.action;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.revenue.model.adjunct.detail.VehicleMaker;

@Name("vehicleMakerHome")
public class VehicleMakerHome extends EntityHome<VehicleMaker> {

	private static final long serialVersionUID = 1L;

	@In
	FacesMessages facesMessages;
	
	public void setVehicleMakerId(Long id) {
		setId(id);
	}

	public Long getVehicleMakerId() {
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

	public VehicleMaker getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	@Override
	public String persist() {
		// comprobar q no exista el nombre
		Query q = this.getEntityManager().createNamedQuery("VehicleMaker.findByName");
		q.setParameter("name", this.getInstance().getName());
		if (q.getResultList().size() == 0) {
			return super.persist();
		} else {
			facesMessages.addToControl("resident",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"#{messages['vehicleMaker.nameExist']}");
			return null;
		}
	}
}
