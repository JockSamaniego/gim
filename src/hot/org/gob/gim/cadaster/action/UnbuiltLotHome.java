package org.gob.gim.cadaster.action;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.cadaster.facade.CadasterService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.UnbuiltLot;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.FiscalPeriod;

@Name("unbuiltLotHome")
public class UnbuiltLotHome extends EntityHome<UnbuiltLot> {
	
	@In
	FacesMessages facesMessages;
	
	public static String CADASTER_SERVICE_NAME = "/gim/CadasterService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private SystemParameterService systemParameterService;	
	
	private CadasterService cadasterService;
	
	private String criteriaProperty;
	
	private Charge appraisalCharge;
	private Delegate appraisalDelegate;
	
	private List<Property> properties;		
	
	public void setUnbuiltLotId(Long id) {
		setId(id);
	}

	public Long getUnbuiltLotId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public boolean isFirsTime = true;
	
	public void wire() {		
		getInstance();	
	}

	public boolean isWired() {
		return true;
	}

	public UnbuiltLot getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String save(){
		UnbuiltLot exist = existUnbuiltLot();
		if(exist != null && 
				( this.isManaged() && !exist.getId().equals(this.getInstance().getId())) || (exist != null && !this.isManaged()) ){
			addFacesMessageFromResourceBundle("unbuiltLot.existUnBuiltLot");
			return "failed";
		}
		
//		if(this.getInstance().getFiscalPeriod().getStartDate().getYear() < Calendar.getInstance().getTime().getYear()){
//			addFacesMessageFromResourceBundle("unbuiltLot.fiscalPeriodInvalid");
//			return "failed";
//		}
		
		if(this.getInstance().getProperty() == null){
			addFacesMessageFromResourceBundle("unbuiltLot.emptyProperty");
			return "failed";
		}
						
		if(this.isManaged()) return super.update();
		
		return super.persist();
	}
	
	private FiscalPeriod previousFiscalPeriod;
	
	private FiscalPeriod currentFiscalPeriod;
	
	private List<UnbuiltLot> findByFiscalPeriod(Long fiscalPeriodId){
		Query query = getEntityManager().createNamedQuery("UnbuiltLot.findByFiscalPeriod");		
		query.setParameter("fiscalPeriodId", fiscalPeriodId);
		return query.getResultList();		
	}
	
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
	
	public String createCopy(){
		
		if(previousFiscalPeriod.getStartDate().after(currentFiscalPeriod.getStartDate()) || previousFiscalPeriod.getStartDate().equals(currentFiscalPeriod.getStartDate())){
			addFacesMessageFromResourceBundle("unbuiltLot.previousFiscalPeriodInvalid");
			return "failed";
		}
		
		List<UnbuiltLot> list = findByFiscalPeriod(previousFiscalPeriod.getId());
		
		if(list.size() == 0){
			addFacesMessageFromResourceBundle("unbuiltLot.emptyUnbuiltLots");
			return "failed";
		}
		
		List<UnbuiltLot> list1 = findByFiscalPeriod(currentFiscalPeriod.getId());
		
		if(list1.size() > 0){
			addFacesMessageFromResourceBundle("unbuiltLot.existUnbuiltLotsByFiscalPeriod");
			return "failed";
		}
		
		if (cadasterService == null) cadasterService = ServiceLocator.getInstance().findResource(CADASTER_SERVICE_NAME);
		
		
		
		cadasterService.createCopyOfUnbuiltLots(list, currentFiscalPeriod);
		
		return "ready";
	}
	
	public UnbuiltLot existUnbuiltLot() {
		if(this.getInstance().getProperty() == null || this.getInstance().getFiscalPeriod() == null) return null;
		Query query = getEntityManager().createNamedQuery("UnbuiltLot.findByFiscalPeriodAndProperty");
		query.setParameter("propertyId", this.getInstance().getProperty().getId());
		query.setParameter("fiscalPeriodId", this.getInstance().getFiscalPeriod().getId());
		List<UnbuiltLot> list = query.getResultList();
		if(list.size() > 0){
			return list.get(0);						
		}
		return null;
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
		this.getInstance().setProperty(property);
		this.getInstance().setPropertyUse(property.getCurrentDomain().getPropertyUse());
	}
	
	public String removeUnbuiltLot(UnbuiltLot unbuiltLot){
		this.setInstance(unbuiltLot);
		super.remove();		
		return "/cadaster/UnbuiltLotList.xhtml";
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

	public FiscalPeriod getPreviousFiscalPeriod() {
		return previousFiscalPeriod;
	}

	public void setPreviousFiscalPeriod(FiscalPeriod previousFiscalPeriod) {
		this.previousFiscalPeriod = previousFiscalPeriod;
	}

	public FiscalPeriod getCurrentFiscalPeriod() {
		return currentFiscalPeriod;
	}

	public void setCurrentFiscalPeriod(FiscalPeriod currentFiscalPeriod) {
		this.currentFiscalPeriod = currentFiscalPeriod;
	}

	public CadasterService getCadasterService() {
		return cadasterService;
	}

	public void setCadasterService(CadasterService cadasterService) {
		this.cadasterService = cadasterService;
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
