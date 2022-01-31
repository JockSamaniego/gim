package org.gob.gim.factoryline.action;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.factoryline.FactoryLine;
import ec.gob.gim.factoryline.FactoryLineRoad;
import ec.gob.gim.factoryline.SectorType;
import ec.gob.gim.factoryline.TerritorialPolygon;


@Name("factoryLineHome")
@Scope(ScopeType.CONVERSATION)
public class FactoryLineHome extends EntityHome<FactoryLine> {

	private static final long serialVersionUID = -1983240040776377384L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	@Logger
	Log logger;

	@In
	FacesMessages facesMessages;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private boolean isFirstTime = true;
	
	private SystemParameterService systemParameterService;

	private String criteria;
	private String criteriaSearch;

	private Property property;
	private List<Property> properties;

	private String criteriaProperty;
	private Resident resident = null;

	private FactoryLine factoryLineRep = new FactoryLine();
	
	private FactoryLineRoad road = new FactoryLineRoad();

	private List<TerritorialPolygon> territorialPolygons = new ArrayList<TerritorialPolygon>();
	private List<FactoryLineRoad> roadsHome = new ArrayList<FactoryLineRoad>();

   private boolean duplicate;
   
	public void setFactoryLineId(Long id) {
		setId(id);
	}

	public Long getFactoryLineId() {
		return (Long) getId();
	}

	/**
	 * @return the criteriaSearch
	 */
	public String getCriteriaSearch() {
		return criteriaSearch;
	}

	/**
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	/**
	 * @return the criteriaProperty
	 */
	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	/**
	 * @param criteriaProperty the criteriaProperty to set
	 */
	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	/**
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
	}

	/**
	 * @param criteriaSearch the criteriaSearch to set
	 */
	public void setCriteriaSearch(String criteriaSearch) {
		this.criteriaSearch = criteriaSearch;
	}

	public void searchProperty() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}
	
	public void searchPropertyByCriteria() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}
		
	public void clearSearchPropertyPanel() {
		this.setCriteriaProperty(null);
		properties = null;
	}
	
	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes().get("property");
		setProperty(property);
		setResident(property.getCurrentDomain().getResident());
		fillDataCadaster(property);
	}
	
	private void fillDataCadaster(Property property) {
		instance.setCadastralCode(property.getCadastralCode());
		instance.setOwner(property.getCurrentDomain().getResident().getName());
		instance.setIdentification(property.getCurrentDomain().getResident().getIdentificationNumber());
		instance.setNeighborhood(property.getLocation().getNeighborhood().getName());
		instance.setBlock(property.getBlock().getCode());
		instance.setPropertyNumber(property.getLocation().getHouseNumber());
		instance.setArea(property.getArea().doubleValue());
		instance.setFront(property.getFront().doubleValue());
		instance.setLotBackground(property.getSide().doubleValue());
		instance.setLotPosition(property.getLotPosition());
		instance.setStreet(property.getLocation().getMainBlockLimit().getStreet().getName());
		instance.setStreetBetween("x");
		instance.setStreetBetweenTwo("x");
		instance.setParish(property.getBlock().getSector().getParent().getParent().getName());
	}
	
	public void clearDataInstance(){
		instance.setCadastralCode("");
		instance.setOwner("");
		instance.setIdentification("");
		instance.setNeighborhood("");
		instance.setBlock("");
		instance.setPropertyNumber("");
		instance.setArea(null);
		instance.setFront(null);
		instance.setLotBackground(null);
		instance.setLotPosition(null);
		instance.setStreet("");
		instance.setStreetBetween("");
		instance.setStreetBetweenTwo("");
		instance.setParish("");
		instance.setTerritorialPolygon(null);
		setProperty(null);
		setResident(null);
	}
	
	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
		loadPolygons();
//		factoryLine = new FactoryLine();
	}

	public void wire() {
		getDefinedInstance();
		if (!isFirstTime)
			return;
		if ((!isFirstTime) && (duplicate)){
			duplicateFactoryLine(instance);
		}
		isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	public FactoryLine getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	/**
	 * @return the isFirstTime
	 */
	public boolean isFirstTime() {
		return isFirstTime;
	}

	/**
	 * @param isFirstTime the isFirstTime to set
	 */
	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	/**
	 * @return the road
	 */
	public FactoryLineRoad getRoad() {
		return road;
	}

	/**
	 * @param road the road to set
	 */
	public void setRoad(FactoryLineRoad road) {
		this.road = road;
	}

	/* (non-Javadoc)
	 * @see org.jboss.seam.framework.EntityHome#persist()
	 */
	@Override
	public String persist() {
		if (!isIdDefined()) {
			instance.setUser(userSession.getUser());
			instance.setCreationDate(new GregorianCalendar().getTime());
//			instance.setExpiratedDate(instance.getCreationDate() + );
		}
		return super.persist();
	}

	public void createFactoryLine() {
		territorialPolygons.clear();
	}
	
	public void editFactoryLine() {
		getInstance();
		TerritorialPolygon polygon = getInstance().getTerritorialPolygon();
		TerritorialPolygon polygon1 = getInstance().getTerritorialPolygon1();
		TerritorialPolygon polygon2 = getInstance().getTerritorialPolygon2();
		loadPolygons();
		loadRoads();
		getInstance().setTerritorialPolygon(polygon);
		getInstance().setTerritorialPolygon1(polygon1);
		getInstance().setTerritorialPolygon2(polygon2);
	}
	
	@Override
	@Transactional
	public String update() {
		return super.update();
	}
	
	public Boolean hasRole(String roleKey) {
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String role = systemParameterService.findParameter(roleKey);
		if (role != null) {
			return userSession.getUser().hasRole(role);
		}
		return false;
	}
	
	public void createRoad(){
		this.road = new FactoryLineRoad();
	}
	
	public void editRoad(FactoryLineRoad road){
		this.road = road;
	}
	
	public void addRoad(FactoryLineRoad road){
		if (road == null) return;
		road.getFactoryLines().add(instance);
		this.instance.getRoads().add(road);
		this.road = null;
		loadRoads();
	}
	
	public void removeRoad(FactoryLineRoad road){
		this.instance.getRoads().remove(road);
		road.getFactoryLines().remove(instance);
		loadRoads();
	}
	
	/**
	 * @return the roads
	 */
	public List<FactoryLineRoad> getRoadsHome() {
		return roadsHome;
	}

	/**
	 * @param roads the roads to set
	 */
	public void setRoadsHome(List<FactoryLineRoad> roadsHome) {
		this.roadsHome = roadsHome;
	}

	/**
	 * @return the duplicate
	 */
	public boolean isDuplicate() {
		return duplicate;
	}

	/**
	 * @param duplicate the duplicate to set
	 */
	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	private void loadRoads(){
		if (getInstance() == null) return;
		roadsHome.clear();
		roadsHome.addAll(getInstance().getRoads());
	}
	
	public void generateReport(FactoryLine factoryLine) {
		this.factoryLineRep = factoryLine;
		loadRoads();
	}

	/**
	 * @return the factoryLine
	 */
	public FactoryLine getFactoryLineRep() {
		return factoryLineRep;
	}

	/**
	 * @param factoryLine the factoryLine to set
	 */
	public void setFactoryLineRep(FactoryLine factoryLineRep) {
		this.factoryLineRep = factoryLineRep;
	}
	
	public String getAllObservations(){
		if (instance == null) return "";
		else {
			StringBuilder str = new StringBuilder();
			if ((instance.getTerritorialPolygon().getObs1() != null) && (instance.getTerritorialPolygon().getObs1().length() > 5))
				str = str.append("- " + instance.getTerritorialPolygon().getObs1() + "\n");
			if ((instance.getTerritorialPolygon().getObs2() != null) && (instance.getTerritorialPolygon().getObs2().length() > 5))
				str = str.append("- " + instance.getTerritorialPolygon().getObs2() + "\n");
			if ((instance.getTerritorialPolygon().getObs3() != null) && (instance.getTerritorialPolygon().getObs3().length() > 5))
				str = str.append("- " + instance.getTerritorialPolygon().getObs3() + "\n");
			if ((instance.getTerritorialPolygon().getObs4() != null) && (instance.getTerritorialPolygon().getObs4().length() > 5))
				str = str.append("- " + instance.getTerritorialPolygon().getObs4());
			if ((instance.getSpecificRemark() != null) && (instance.getSpecificRemark().length() > 5))
				str = str.append("- " + instance.getSpecificRemark());
			return str.toString();
		}
	}
	
	public void loadPolygons(){
		territorialPolygons.clear();
		getInstance().setTerritorialPolygon(null);
		if (getInstance().getSectorType() == null){
			return;
		} else {
			Query query = getEntityManager()
					.createNamedQuery("TerritorialPolygon.findAllActiveBySectorType");
			query.setParameter("sectorType", instance.getSectorType());
			territorialPolygons = query.getResultList();
		}
	}

	/**
	 * @return the territorialPolygons
	 */
	public List<TerritorialPolygon> getTerritorialPolygons() {
		return territorialPolygons;
	}

	/**
	 * @param territorialPolygons the territorialPolygons to set
	 */
	public void setTerritorialPolygons(List<TerritorialPolygon> territorialPolygons) {
		this.territorialPolygons = territorialPolygons;
	}
	
	public void duplicateFactoryLine(FactoryLine factoryLine){
		FactoryLine newFactoryLine = new FactoryLine();
		newFactoryLine.setArea(this.getInstance().getArea());
		newFactoryLine.setBlock(this.getInstance().getBlock());
		newFactoryLine.setCadastralCode(this.getInstance().getCadastralCode());
		newFactoryLine.setCreationDate(this.getInstance().getCreationDate());
		newFactoryLine.setDepartment(this.getInstance().getDepartment());
		newFactoryLine.setExpiratedDate(null);
		newFactoryLine.setFactoryLineNumber("");
		newFactoryLine.setFront(this.getInstance().getFront());
		newFactoryLine.setHasSewer(this.getInstance().getHasSewer());
		newFactoryLine.setHasWater(this.getInstance().getHasWater());
		newFactoryLine.setIdentification(this.getInstance().getIdentification());
		newFactoryLine.setLotBackground(this.getInstance().getLotBackground());
		newFactoryLine.setLotPosition(this.getInstance().getLotPosition());
		newFactoryLine.setMitigable(this.getInstance().getMitigable());
		newFactoryLine.setMitigableDescription(this.getInstance().getMitigable());
		newFactoryLine.setMitigableTreatment(this.getInstance().getMitigableTreatment());
		newFactoryLine.setNeighborhood(this.getInstance().getNeighborhood());
		newFactoryLine.setNotMitigable(this.getInstance().getNotMitigable());
		newFactoryLine.setNotMitigableDescription(this.getInstance().getNotMitigable());
		newFactoryLine.setNotMitigableTreatment(this.getInstance().getNotMitigableTreatment());
		newFactoryLine.setOwner(this.getInstance().getOwner());
		newFactoryLine.setParish(this.getInstance().getParish());
		newFactoryLine.setPropertyNumber(this.getInstance().getPropertyNumber());
		newFactoryLine.setResident(this.getInstance().getResident());
		for (FactoryLineRoad road : this.getInstance().getRoads()) {
			newFactoryLine.add(road);
		}
		newFactoryLine.setSectorType(this.getInstance().getSectorType());
		newFactoryLine.setSpecificRemark(this.getInstance().getSpecificRemark());
		newFactoryLine.setStreet(this.getInstance().getStreet());
		newFactoryLine.setStreetBetween(this.getInstance().getStreetBetween());
		newFactoryLine.setStreetBetweenTwo(this.getInstance().getStreetBetweenTwo());
		newFactoryLine.setTerritorialPolygon(this.getInstance().getTerritorialPolygon());
		newFactoryLine.setTerritorialPolygon1(this.getInstance().getTerritorialPolygon1());
		newFactoryLine.setTerritorialPolygon2(this.getInstance().getTerritorialPolygon2());
		newFactoryLine.setWithoutCadastralInfo(this.getInstance().getWithoutCadastralInfo());
		newFactoryLine.setUser(userSession.getUser());
		
		this.setInstance(newFactoryLine);
		TerritorialPolygon polygon = getInstance().getTerritorialPolygon();
		TerritorialPolygon polygon1 = getInstance().getTerritorialPolygon1();
		TerritorialPolygon polygon2 = getInstance().getTerritorialPolygon2();
		loadPolygons();
		loadRoads();
		getInstance().setTerritorialPolygon(polygon);
		getInstance().setTerritorialPolygon1(polygon1);
		getInstance().setTerritorialPolygon2(polygon2);
	}
	
}