package org.gob.gim.cadaster.action;

import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.cadaster.model.*;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("streetHome")
public class StreetHome extends EntityHome<Street> {
	
	private Place place;
	
	@In(create=true)
	PlaceHome placeHome;

	private boolean correctPersist = false;
	
	public void setStreetId(Long id) {
		setId(id);
	}

	public Long getStreetId() {
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

	public Street getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<Place> findPlace(){			
		Query query = getPersistenceContext().createNamedQuery("Place.findAll");		
		return query.getResultList();			
	}
	
	public void createPlace(){		
		this.place = new Place();
		setCorrectPersist(false);
	}

	public void persistPlace(){
		placeHome.setInstance(place);		
		placeHome.persist();
		this.getInstance().setPlace(place);
		correctPersist = true;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}

	public void setCorrectPersist(boolean correctPersist) {
		this.correctPersist = correctPersist;
	}

	public boolean isCorrectPersist() {
		return correctPersist;
	}

}
