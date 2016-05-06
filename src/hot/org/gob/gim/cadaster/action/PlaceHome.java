package org.gob.gim.cadaster.action;

import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.cadaster.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("placeHome")
public class PlaceHome extends EntityHome<Place> {
	
	public void setPlaceId(Long id) {
		setId(id);
	}

	public Long getPlaceId() {
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

	public Place getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	private List<Place> streetRelations(){			
		Query query = getPersistenceContext().createQuery("Select street.id from Street street where street.place.id = :id");
		query.setParameter("id",this.getInstance().getId());
		return query.getResultList();			
    }
	
	private List<Place> neighborhoodRelations(){			
		Query query = getPersistenceContext().createQuery("Select n.id from Neighborhood n where n.place.id = :id");
		query.setParameter("id",this.getInstance().getId());
		return query.getResultList();			
   }
	
	public boolean canBeRemoved(){
		List<Place> streetsInAPlace = streetRelations();
		List<Place> neighborhoodInAPlace = neighborhoodRelations();
		if((streetsInAPlace == null || streetsInAPlace.size() == 0) && (neighborhoodInAPlace == null || neighborhoodInAPlace.size() == 0)){			
			return true;
		}
		return false;
	}
	
	

}
