package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.commercial.action.BusinessHome;
import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.cadaster.model.Building;
import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.commercial.model.Local;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.CurrencyDevaluation;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.adjunct.BusinessLocalReference;
import ec.gob.gim.revenue.model.adjunct.DomainTransfer;
import ec.gob.gim.revenue.model.adjunct.PropertyAppraisal;
import ec.gob.gim.revenue.model.adjunct.PropertyReference;
import ec.gob.gim.revenue.model.adjunct.PropertyReferenceOptional;
import ec.gob.gim.revenue.model.adjunct.detail.EarlyTransferDiscount;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleType;

@Name("adjunctAction")
public class AdjunctAction extends EntityController{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public void loadPendingDomainTransfers(){
		DomainTransfer domainTransfer = findCurrentAdjunct();
		if(domainTransfer.getProperty() != null){
			Long propertyId = domainTransfer.getProperty().getId();
			if(propertyId != null){
				Query query = getEntityManager().createNamedQuery("Domain.findPendingTransfersByPropertyId");
				query.setParameter("propertyId", propertyId);
				List<Domain> domainTransfers = query.getResultList();
				domainTransfer.setDomainTransfers(domainTransfers);
				domainTransfer.setDomain(null);
			}
		}
	}
	
	@Factory("earlyTransferDiscounts")
	public List<EarlyTransferDiscount> loadEarlyTransferDiscounts(){
		return Arrays.asList(EarlyTransferDiscount.values());
	}	
	
	public Boolean getIsAlcabalaTax(){
		MunicipalBond municipalBond = findMunicipalBond();
		String name = municipalBond.getEntry().getName();
		return name != null && name.toLowerCase().contains("alcabala");
	}
		
	public void updatePropertyAppraisal(){
		PropertyAppraisal propertyAppraisal = findCurrentAdjunct();
		if(propertyAppraisal != null){
			Property property = propertyAppraisal.getProperty();
			Domain currentDomain = property.getCurrentDomain();
			
			propertyAppraisal.setCadastralCode(property.getCadastralCode());
			propertyAppraisal.setPreviousCadastralCode(property.getPreviousCadastralCode());
			propertyAppraisal.setLotAppraisal(currentDomain.getLotAppraisal());
			propertyAppraisal.setBuildingAppraisal(currentDomain.getBuildingAppraisal());
			propertyAppraisal.setRealLotAppraisal(currentDomain.getLotAppraisal());
			propertyAppraisal.setRealBuildingAppraisal(currentDomain.getBuildingAppraisal());
			propertyAppraisal.setCommercialAppraisal(currentDomain.getCommercialAppraisal());
			
			//macartuche 
			//2019-03-13 16:07
			BigDecimal constructionArea = calculateTotalAreaConstruction(property);
			propertyAppraisal.setLotArea(property.getArea());
			propertyAppraisal.setConstructionArea(constructionArea);
//			this.setAddressAdjunct(property.getAddress());
			changePropertyTaxableBase();
			//propertyAppraisal.setCode(propertyAppraisal.getPreviousCadastralCode()+" - "+propertyAppraisal.getCadastralCode());
			//property.getBuildings()
			//rfam 2017-12-15 aprobacion de ordenanza
			//propertyAppraisal.setLotArea(BigDecimal.ONE);
			//propertyAppraisal.setConstructionArea(BigDecimal.ONE);
		}
	}
	
	/***/
	private BigDecimal calculateTotalAreaConstruction(Property _property) {
		BigDecimal res = new BigDecimal(0);
		if (_property != null) {
			for (Building b : _property.getBuildings()) {
				if (b.getTotalArea() == null) {
					updateTotalArea(b, _property);
					res = res.add(b.getTotalArea());
				} else {
					res = res.add(b.getTotalArea());
				}
			}
		}

		return res;		
	}
	
	private void updateTotalArea(Building building, Property _property) {
		if (_property.getPropertyType().getId().equals(1L)) { //urbano
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
	
	public void changePropertyTaxableBase(){
		PropertyAppraisal propertyAppraisal = findCurrentAdjunct();
		if(propertyAppraisal != null){
			BigDecimal taxableBase = BigDecimal.ZERO;
			if (propertyAppraisal.getChangeAppraisals()){
				taxableBase = propertyAppraisal.getLotAppraisal().add(propertyAppraisal.getBuildingAppraisal());
				taxableBase = taxableBase.setScale(2, RoundingMode.HALF_UP);
				propertyAppraisal.setCommercialAppraisal(taxableBase);
			}
			else{
				taxableBase = propertyAppraisal.getCommercialAppraisal();
			}
			MunicipalBondHome municipalBondHome = findMunicipalBondHome();
			municipalBondHome.getEntryValueItems().get(0).setMainValue(taxableBase);
			Property property = propertyAppraisal.getProperty();
			if (property != null)
				municipalBondHome.getInstance().setBondAddress(property.getAddress());
			else
				municipalBondHome.getInstance().setBondAddress("");
			municipalBondHome.getInstance().setBase(taxableBase);
		}
	}
	
	
	public void updateDomainCode(){
		DomainTransfer domainTransfer = findCurrentAdjunct();
		if(domainTransfer != null){
			Domain domain = domainTransfer.getDomain();
			Property property = domainTransfer.getProperty();
			Domain currentDomain = property.getCurrentDomain();
			
			Resident buyer = domain.getResident();
			Resident seller = property.getCurrentDomain().getResident(); 
			
			domainTransfer.setSeller(seller.getName());
			domainTransfer.setBuyer(buyer.getName());
			domainTransfer.setAddress(property.getAddress());
			domainTransfer.setLotArea(property.getArea());
			domainTransfer.setBuildingAppraisal(currentDomain.getBuildingAppraisal());
			domainTransfer.setLotAppraisal(currentDomain.getLotAppraisal());
			domainTransfer.setCommercialAppraisal(currentDomain.getCommercialAppraisal());
			domainTransfer.setTransactionValue(domain.getValueTransaction());
			domainTransfer.setBuildingArea(currentDomain.getTotalAreaConstruction());
			domainTransfer.setCadastralCode(property.getCadastralCode());
			domainTransfer.setPreviousCadastralCode(property.getPreviousCadastralCode());
			domainTransfer.setBuyingTransactionValue(currentDomain.getValueTransaction());
			domainTransfer.setBuyingDate(currentDomain.getDate());
			domainTransfer.setTransactionDate(domain.getCreationDate());
			domainTransfer.setImprovementsContribution(domain.getSpecialContribution());
			domainTransfer.setNewBuildingValue(domain.getNewBuildingValue());
			
			MunicipalBondHome municipalBondHome = findMunicipalBondHome();
			BigDecimal taxableBase = calculateTaxableBase(municipalBondHome);			
			municipalBondHome.getEntryValueItems().get(0).setMainValue(taxableBase);
			
			//domainTransfer.setCode(domainTransfer.getPreviousCadastralCode()+" - "+domainTransfer.getCadastralCode());
		}
	}
	
	public void resetPropertyValues(){
		PropertyAppraisal appraisal = findCurrentAdjunct();
		appraisal.setProperty(null);
	}

	public void resetPropertyValuesOfChangeAppraisals(){
		PropertyAppraisal appraisal = findCurrentAdjunct();
		if (!appraisal.getChangeAppraisals()){
			appraisal.setProperty(null);
			appraisal.setBuildingAppraisal(BigDecimal.ZERO);
			appraisal.setCadastralCode("");
			appraisal.setCode("");
			appraisal.setCommercialAppraisal(BigDecimal.ZERO);
			appraisal.setLotAppraisal(BigDecimal.ZERO);
			appraisal.setPreviousCadastralCode("");
			appraisal.setRealBuildingAppraisal(BigDecimal.ZERO);
			appraisal.setRealLotAppraisal(BigDecimal.ZERO);
			changePropertyTaxableBase();
		}
	}
	
	public void resetValues(){
		DomainTransfer domainTransfer = findCurrentAdjunct();
		domainTransfer.setProperty(null);
		domainTransfer.setDomain(null);
	}
	
	private BigDecimal calculateTaxableBase(MunicipalBondHome municipalBondHome){
		DomainTransfer domainTransfer = findCurrentAdjunct();
		BigDecimal taxableBase = domainTransfer.getTaxableBase();
		if(!getIsAlcabalaTax()){
			Integer years = domainTransfer.getYearsFromLastTransfer(domainTransfer.getBuyingDate(), domainTransfer.getTransactionDate());
			if(years < 20){
				Double yearsFactor = years * 0.05;
				
				taxableBase = domainTransfer.getTransactionValue().subtract(domainTransfer.getBuyingTransactionValue());
				taxableBase = taxableBase.subtract(domainTransfer.getImprovementsContribution());
				taxableBase = taxableBase.subtract(domainTransfer.getNewBuildingValue().multiply(getFactorNewBuildingValue(municipalBondHome)));
				taxableBase = taxableBase.subtract(taxableBase.multiply(new BigDecimal(yearsFactor)));
				
				Integer year = DateUtils.getTruncatedInstance(domainTransfer.getBuyingDate()).get(Calendar.YEAR);
				BigDecimal devaluationFactor = findCurrencyDevaluationFactor(year);
				taxableBase = taxableBase.subtract(taxableBase.multiply(devaluationFactor));
			} else {
				taxableBase = BigDecimal.ZERO;
			}
		}
		return taxableBase.setScale(2, RoundingMode.HALF_UP);
	}
		
/*
 * se deberán evaluar primero las fechas más nuevas
 * para retornar el factor del porcentaje de descuento 
 * en obra nueva
 */
	public BigDecimal getFactorNewBuildingValue(MunicipalBondHome municipalBondHome){
		BigDecimal factor = new BigDecimal(0.8); // es para todas las emisiones antes del 01 de sept de 2013 80%(0.8)
		Calendar limitDate01 = new GregorianCalendar(2013, 7, 29); // entra en vigencia a partir del 01 de septiembre de 2013 100%(1)
		if (municipalBondHome.getEntryValueItems().get(0).getServiceDate().after(limitDate01.getTime())){
			return BigDecimal.ONE;
		}else{
			return factor;
		}
	}
	
	public void changeTaxableBase(){
		MunicipalBondHome municipalBondHome = findMunicipalBondHome();
		BigDecimal taxableBase = calculateTaxableBase(municipalBondHome);
		municipalBondHome.getEntryValueItems().get(0).setMainValue(taxableBase);
	}
	
	@SuppressWarnings("unchecked")
	public List<Property> findPropertiesByResidentId(){
		Long residentId = findSelectedResidentId();
		if(residentId != null){
			Query query = getEntityManager().createNamedQuery("Property.findByResidentIdForEmission");
			query.setParameter("residentId", residentId);
			return query.getResultList();
		}
		return new ArrayList<Property>();
	}
	
	public void updatePropertyCode(){
		PropertyReference reference = findCurrentAdjunct();
		if(reference != null){
			//System.out.println("UPDATING PROPERTY CODE ----> Cadastral code set on code property!!");
			reference.setCode(reference.getProperty().getCadastralCode());
			reference.setOwner(reference.getProperty().getCurrentDomain().getResident().getName());
			if(reference.getProperty().getLocation() != null){
				reference.setLocation(reference.getProperty().getLocation().getMainBlockLimit().getStreet().getName());
			}
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<Local> findLocalesByResidentId(){
		Long residentId = findSelectedResidentId();
		if(residentId != null){
			Query query = getEntityManager().createNamedQuery("Local.findByOwnerId");
			query.setParameter("ownerId", residentId);
			return query.getResultList();
		}
		return new ArrayList<Local>();
	}
	
	@SuppressWarnings("unchecked")
	public List<VehicleType> findVehicleTypeForSimert() {
		Query query = getEntityManager().createNamedQuery("VehicleType.findForSimert");
		return query.getResultList();
	}
	
	public void updateLocalCode(Long id){
		BusinessLocalReference reference = findCurrentAdjunct();
		if(reference != null && reference.getLocal() != null){
			//System.out.println("UPDATING LOCAL CODE ----> Local id set on code property!!");
			reference.setCode(reference.getLocal().getId().toString());
			reference.setOwner(reference.getLocal().getBusiness().getOwner().getName());
			if (reference.getLocal().getBusiness().getManager() == null)
				reference.setManager(reference.getLocal().getBusiness().getOwner().getName());
			else
				reference.setManager(reference.getLocal().getBusiness().getManager().getName());
			if (reference.getLocal().getAddress() != null)
				reference.setLocation(reference.getLocal().getAddress().toString());
			if (reference.getLocal().getBusiness().getName() != null)
				reference.setBusinessName(reference.getLocal().getBusiness().getName());
			if (reference.getLocal().getName() != null)
				reference.setLocalName(reference.getLocal().getName());
		}
		 
		BusinessHome.myId = id;
		BusinessHome home = (BusinessHome) Contexts.getConversationContext().get(BusinessHome.class);
		home.isActive();
		MunicipalBondHome.message="";
	}
	
	private Long findSelectedResidentId(){
		Long residentId = null;
		
		MunicipalBondHome home = (MunicipalBondHome) Contexts.getConversationContext().get(MunicipalBondHome.class);
		if(home != null){
			if(home.getInstance() != null && home.getInstance().getResident() != null){
				residentId = home.getInstance().getResident().getId();
			}
		}else{
			DeferredMunicipalBondAction defMBAction = (DeferredMunicipalBondAction) Contexts.getConversationContext().get(DeferredMunicipalBondAction.class);
			if(defMBAction != null){
				if(defMBAction.getResident() != null){
					residentId = defMBAction.getResident().getId();
				}
			}			
		}
		return residentId;
	}
	
	private MunicipalBondHome findMunicipalBondHome(){
		MunicipalBondHome home = (MunicipalBondHome) Contexts.getConversationContext().get(MunicipalBondHome.class);
		return home;
	}
	
	private MunicipalBond findMunicipalBond(){
		MunicipalBond municipalBond = null;
		MunicipalBondHome home = findMunicipalBondHome();
		if(home != null){
			municipalBond = home.getInstance(); 
		}
		return municipalBond;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Adjunct> T findCurrentAdjunct(){
		AdjunctHome home = (AdjunctHome) Contexts.getConversationContext().get(AdjunctHome.class);
		if(home != null){
			//System.out.println("UPDATING PROPERTY CODE ----> Home found!!"+home);
			T currentAdjunct = (T) home.getInstance();
			return currentAdjunct;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private BigDecimal findCurrencyDevaluationFactor(Integer year){
		Query query = getEntityManager().createNamedQuery("CurrencyDevaluation.findByYear");
		query.setParameter("year", year);		
		List<CurrencyDevaluation> currencyDevaluations = query.getResultList();
		if(currencyDevaluations.size() > 0){
			return currencyDevaluations.get(0).getValue(); 
		}
		return BigDecimal.ZERO;
		
	}
	
	
//Autor: Jock Samaniego M.
//Para obtener lista de locales desactivados desde el MunicipalBondEdit.
	private List<Local> desactiveLocals=null;
	
	@SuppressWarnings("unchecked")
	public List<Local> desactiveLocals(){
		Long residentId = findSelectedResidentId();
		if(residentId != null){
			Query query = getEntityManager().createNamedQuery("Local.findByResidentAndNoActive");
			query.setParameter("ownerId", residentId);
			desactiveLocals = query.getResultList();
			return desactiveLocals;
		}
		return new ArrayList<Local>();
	}

	public List<Local> getDesactiveLocals() {
		return desactiveLocals;
	}

	public void setDesactiveLocals(List<Local> desactiveLocals) {
		this.desactiveLocals = desactiveLocals;
	}
	
	//Jock Samaniego
	//Para obtener lista de tipos de domainTransfer
	
	private List<ItemCatalog> transferTypes;
	private ItemCatalogService itemCatalogService;
	
	public List<ItemCatalog> domainTransferTypes(){
		initializeService();
		transferTypes = new ArrayList<ItemCatalog>();
		transferTypes = itemCatalogService.findItemsForCatalogCode(
				CatalogConstants.CATALOG_TYPES_DOMAIN_TRANSFER);
		
		return transferTypes;
	}
	
	public void initializeService() {
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
	}
	
	//Para seleccion de propiedad opcional
	//Jock Samaniego
	//29-11-2018
	
	public void updatePropertyOptionalCode(){
		PropertyReferenceOptional reference = findCurrentAdjunct();
		if(reference != null){
			//System.out.println("UPDATING PROPERTY CODE ----> Cadastral code set on code property!!");
			reference.setCode(reference.getProperty().getCadastralCode());
			reference.setOwner(reference.getProperty().getCurrentDomain().getResident().getName());
			if(reference.getProperty().getLocation() != null){
				reference.setLocation(reference.getProperty().getLocation().getMainBlockLimit().getStreet().getName());
			}
		}
	}
	
	public void resetPropertyOptionalValues(){
		PropertyReferenceOptional reference = findCurrentAdjunct();
		reference.setProperty(null);
	}
	
	public List<ItemCatalog> findFineOrnamentCategory(){
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		
		List<ItemCatalog> items = new ArrayList<ItemCatalog>();
		
		items =  itemCatalogService.findItemsForCatalogCode(
				CatalogConstants.CATALOG_TYPES_CONSUME_ALCOHOLIC_BEVERAGES);
		
		return items;
	}
	
	public void updateOrnamentCategory(){
		
		PropertyReferenceOptional reference = findCurrentAdjunct();
		// ItemCatalog category = 
		
		/* if(reference != null){
			//System.out.println("UPDATING PROPERTY CODE ----> Cadastral code set on code property!!");
			reference.setCode(reference.getProperty().getCadastralCode());
			reference.setOwner(reference.getProperty().getCurrentDomain().getResident().getName());
			if(reference.getProperty().getLocation() != null){
				reference.setLocation(reference.getProperty().getLocation().getMainBlockLimit().getStreet().getName());
			}
		}*/
	}
	
}
