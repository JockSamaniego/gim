package org.gob.gim.cadaster.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Building;
import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.PropertyType;
import ec.gob.gim.cadaster.model.RightsTransfer;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;

@Name("rightsTransferHome")
public class RightsTransferHome extends EntityHome<RightsTransfer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Logger
	Log logger;
	
	@In(create = true)
	TerritorialDivisionHome territorialDivisionHome;
	
	private String criteria;
	private String criteriaProperty;
	private Resident seller;
	private Resident buyer;
	private String sellerIdentificationNumber;
	private String buyerIdentificationNumber;
	private SystemParameterService systemParameterService;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private Charge appraisalCharge;
	private Delegate appraisalDelegate;
	
	private TerritorialDivision parish;
	
	private List<Resident> residents;
	private List<Property> properties;
		
	public void setRightsTransferId(Long id) {
		setId(id);
	}

	public Long getRightsTransferId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
	
	public void wire() {
		getInstance();
		if(getInstance().getSeller() != null) setSellerIdentificationNumber(getInstance().getSeller().getIdentificationNumber());
		if(getInstance().getBuyer() != null) setBuyerIdentificationNumber(getInstance().getBuyer().getIdentificationNumber());	
		if(getInstance().getProperty() != null) loadValues();
	}
	
	/**
	 * Busca un resident en base al criterio de búsqueda
	 */
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}
	
	public BigDecimal calculateAliquotLotArea() {
		if (this.getInstance().getProperty() == null || this.getInstance().getProperty().getLotAliquot() == null) 	return new BigDecimal(0);

		return getInstance().getProperty().getArea().multiply(this.getInstance().getProperty().getLotAliquot()).divide(new BigDecimal(100));
	}
	
	private PropertyType urbanPropertyType;
	
	public void loadUrbanProperty(){
		if(urbanPropertyType != null) return;
		if (systemParameterService == null)	systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		urbanPropertyType = systemParameterService.materialize(PropertyType.class, "PROPERTY_TYPE_ID_URBAN");
	}
	
	private void updateTotalArea(Building building) {
		loadUrbanProperty();
		if (this.getInstance().getProperty().getPropertyType().equals(urbanPropertyType)) {
			building.setTotalArea(calculateTotalArea(building));
		} else {
			if (building.getArea() != null) {
				building.setTotalArea(building.getArea());
			} else {
				building.setTotalArea(BigDecimal.ZERO);
			}
		}

	}
	
	private BigDecimal calculateTotalArea(Building building) {
		if (building != null && building.getArea() != null && building.getFloorsNumber() != null)
			return building.getArea().multiply(BigDecimal.valueOf(building.getFloorsNumber()));
		return new BigDecimal(0);
	}
	
	private void calculateTotalAreaConstruction() {
		BigDecimal res = new BigDecimal(0);
		if (this.getInstance().getProperty() != null) {
			for (Building b : this.getInstance().getProperty().getBuildings()) {
				if (b.getTotalArea() == null) {
					updateTotalArea(b);
					res = res.add(b.getTotalArea());
				} else {
					res = res.add(b.getTotalArea());
				}
			}
		}

		this.getInstance().getProperty().getCurrentDomain().setTotalAreaConstruction(res);
	}
	
	public BigDecimal calculateAliquotConstructionArea() {

		if (this.getInstance().getProperty() == null || this.getInstance().getProperty().getBuildingAliquot() == null) return new BigDecimal(0);
		if (this.getInstance().getProperty().getCurrentDomain().getTotalAreaConstruction() == null) calculateTotalAreaConstruction();
		return this.getInstance().getProperty().getCurrentDomain().getTotalAreaConstruction()
				.multiply(this.getInstance().getProperty().getBuildingAliquot())
				.divide(new BigDecimal(100));
	}

	/**
	 * Busca un resident en base al número de identificación
	 */
	public Resident searchResident(String identificationNumber) {
		// logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			return resident;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Busca un resident en base al número de identificación
	 */
	public void searchSeller() {
		Resident resident = searchResident(sellerIdentificationNumber);		
		// resident.add(this.getInstance());
		this.getInstance().setSeller(resident);			

		if (resident == null || resident.getId() == null) {
			addFacesMessageFromResourceBundle("resident.notFound");
		}
		
	}
	
	public void searchBuyer() {
		Resident resident = searchResident(buyerIdentificationNumber);
		//resident.add(this.getInstance());
		this.getInstance().setBuyer(resident);			

		if (resident == null || resident.getId() == null) {
			addFacesMessageFromResourceBundle("resident.notFound");
		}
		
	}


	/**
	 * Selecciona el resident del modalPanel del residentChooser
	 * @param event
	 */
	public void sellerSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setSeller(resident);		
		this.setSellerIdentificationNumber(resident.getIdentificationNumber());
	}
	
	/**
	 * Selecciona el resident del modalPanel del residentChooser
	 * @param event
	 */
	public void buyerSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setBuyer(resident);		
		this.setBuyerIdentificationNumber(resident.getIdentificationNumber());
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
		this.instance.setProperty(property);
		if (property != null) loadValues();
	}
	
	private TerritorialDivision findTerritorialDivision(int x, int y, TerritorialDivision td) {

		if (this.getInstance().getProperty().getCadastralCode().length() < y)	return null;

		String code = this.getInstance().getProperty().getCadastralCode().substring(x, y);

		Query query = getPersistenceContext().createNamedQuery("TerritorialDivision.findByCodeAndParent");
		query.setParameter("code", code);
		query.setParameter("parent", td);
		List<?> result = query.getResultList();
		if (result != null && result.size() > 0)
			return (TerritorialDivision) result.get(0);

		return null;
	}
	
	public String generateReport(RightsTransfer rightsTransfer) {
		setInstance(rightsTransfer);
		loadCharge();
		if(getInstance().getProperty() != null) loadValues();
		return "/cadaster/report/RightsTransferReport.xhtml";
	}

	/**
	 * Se carga el encargado de catastros y de procuradoría
	 */
	public void loadCharge() {
		appraisalCharge = getCharge("DELEGATE_ID_CADASTER");
		if (appraisalCharge != null) {
			for (Delegate d : appraisalCharge.getDelegates()) {
				if (d.getIsActive())
					appraisalDelegate = d;
			}
		}
	}
	
	private Charge getCharge(String systemParameter) {
		if (systemParameterService == null)	systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,systemParameter);
		return charge;
	}
	
	public void loadValues() {
		
		if (this.getInstance().getProperty() != null  && this.getInstance().getProperty().getCadastralCode() == null)
			return;

		if (parish == null) parish = findTerritorialDivision(5, 9,territorialDivisionHome.findDefaultCanton());

	}
	
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		residents = null;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public Resident getSeller() {
		return seller;
	}

	public void setSeller(Resident seller) {
		this.seller = seller;
	}

	public Resident getBuyer() {
		return buyer;
	}

	public void setBuyer(Resident buyer) {
		this.buyer = buyer;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public String getSellerIdentificationNumber() {
		return sellerIdentificationNumber;
	}

	public void setSellerIdentificationNumber(String sellerIdentificationNumber) {
		this.sellerIdentificationNumber = sellerIdentificationNumber;
	}

	public String getBuyerIdentificationNumber() {
		return buyerIdentificationNumber;
	}

	public void setBuyerIdentificationNumber(String buyerIdentificationNumber) {
		this.buyerIdentificationNumber = buyerIdentificationNumber;
	}

	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public TerritorialDivision getParish() {
		return parish;
	}

	public void setParish(TerritorialDivision parish) {
		this.parish = parish;
	}

	public PropertyType getUrbanPropertyType() {
		return urbanPropertyType;
	}

	public void setUrbanPropertyType(PropertyType urbanPropertyType) {
		this.urbanPropertyType = urbanPropertyType;
	}

	public Charge getAppraisalCharge() {
		return appraisalCharge;
	}

	public void setAppraisalCharge(Charge appraisalCharge) {
		this.appraisalCharge = appraisalCharge;
	}

	public Delegate getAppraisalDelegate() {
		return appraisalDelegate;
	}

	public void setAppraisalDelegate(Delegate appraisalDelegate) {
		this.appraisalDelegate = appraisalDelegate;
	}

	
}
