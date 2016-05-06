package org.gob.gim.cadaster.action;

import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.cadaster.model.*;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("neighborhoodHome")
public class NeighborhoodHome extends EntityHome<Neighborhood> {
	
	@In(create=true)
	PlaceHome placeHome;

	private Place place;
	
	private boolean correctPersist = false;

	public void setNeighborhoodId(Long id) {
		setId(id);
	}

	public Long getNeighborhoodId() {
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

	public Neighborhood getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	/**
	 * Busca todos los Place
	 * @return List<Place>
	 */
	public List<Place> findPlace() {
		Query query = getPersistenceContext().createNamedQuery("Place.findAll");
		return query.getResultList();
	}
	
	/**
	 * Guarda objeto Place
	 */
	public void persistPlace(){
		placeHome.setInstance(place);		
		placeHome.persist();
		this.getInstance().setPlace(place);
		correctPersist = true;	
	}
	
	/**
	 * Crea un nuevo objeto Place
	 */
	public void createPlace(){		
		this.place = new Place();
		setCorrectPersist(false);
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
