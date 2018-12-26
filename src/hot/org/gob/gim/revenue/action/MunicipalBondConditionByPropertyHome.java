package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.MunicipalBondUtil;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.view.MunicipalBondItem;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.SolvencyHistory;
import ec.gob.gim.revenue.model.SolvencyHistoryType;
import ec.gob.gim.revenue.model.SolvencyReportType;

@Name("municipalBondConditionByProperty")
public class MunicipalBondConditionByPropertyHome extends EntityHome<MunicipalBond> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In
	UserSession userSession;
	
	@In(create=true)
	SolvencyHistoryHome solvencyHistoryHome;
	
	private String criteriaProperty;
	private String motivation;
	private List<Property> properties;
	private Property property;
	
	private Resident resident;
	
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}


	@In
	FacesMessages facesMessages;	
	
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
	
	private Charge revenueCharge;
	private Delegate revenueDelegate;
	
	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}
	
	private SystemParameterService systemParameterService;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
		
	public Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,systemParameter);
		return charge;
	}
	
	public void loadCharge() {
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}
		}		
	}
	
	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes().get("property");
		setProperty(property);
		setResident(property.getCurrentDomain().getResident());
		municipalBondItemsResult = new ArrayList<MunicipalBondItem>();
		result = new ArrayList<MunicipalBond>();
		isReadyForPrint = false;
	}
	
	
	private List<MunicipalBond> result;
	
	public List<MunicipalBond> getResult() {
		return result;
	}

	public void setResult(List<MunicipalBond> result) {
		this.result = result;
	}

	public void chargeResults() throws Exception{
		try{
			if(revenueCharge == null) loadCharge();
			municipalBondItemsResult = getMunicipalBondItems();		
			if(result.size() == 0 && property != null){
				isReadyForPrint = true;
			}else{
				isReadyForPrint = false;
				String message = Interpolator.instance().interpolate("#{messages['Error no se puede generar']}", new Object[0]);
				facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);			
			}
		} catch (Exception e) {
			String message = Interpolator.instance().interpolate("#{messages['entryDefinition.entryDefinitionNotFoundException']}", new Object[0]);
			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);			
		}
	}
	
	public String generateSolvencyCertificate(){
		if(resident == null){
			addFacesMessageFromResourceBundle("resident.empty");
			return "failed";
		}
		if(motivation.trim().length() <= 0){
			addFacesMessageFromResourceBundle("motivation.empty");
			return "failed";
		}

		SolvencyHistory solvencyHistory = new SolvencyHistory(resident, motivation, SolvencyHistoryType.DOMAIN_TRANSFER, null, property, userSession.getPerson(),null, null, null, null);
		solvencyHistoryHome.setInstance(solvencyHistory);
		
		if (solvencyHistoryHome.save() != "persisted"){
			addFacesMessageFromResourceBundle("history.persisted");
			return "failed";
		}
		
		return "print";
	}
	
	private Boolean isReadyForPrint;
	
	List<MunicipalBondItem> municipalBondItemsResult;
	
	public List<MunicipalBondItem> getMunicipalBondItemsResult() {
		return municipalBondItemsResult;
	}
	
	private BigDecimal total;
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal calculateTotal(){
		total = BigDecimal.ZERO;
		for(MunicipalBondItem mbi: municipalBondItemsResult){
			total = total.add(mbi.getMunicipalBond().getPaidTotal());
		}
		total = total.setScale(2, RoundingMode.HALF_UP);
		return total;
	}

	public void setMunicipalBondItemsResult(
			List<MunicipalBondItem> municipalBondItemsResult) {
		this.municipalBondItemsResult = municipalBondItemsResult;
	}


	
	public List<MunicipalBondItem> getMunicipalBondItems() throws Exception{		
		result = new LinkedList<MunicipalBond>();		
		if(property != null){
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			MunicipalBondService municipalBondService = ServiceLocator.getInstance().findResource(MunicipalBondService.LOCAL_NAME);			
			//List<MunicipalBond> mbs = incomeService.findOnlyPendingBonds(133359L);	
			List<MunicipalBond> mbs = municipalBondService.findPendingsByGroupingCode(property.getCadastralCode());
			incomeService.calculatePayment(mbs, new Date(), true, true, false);			
			result.addAll(mbs);
		}		
		MunicipalBondUtil.setMunicipalBondStatus(findPendingStatus());		
		return MunicipalBondUtil.fillMunicipalBondItems(result);
	}
	
	private MunicipalBondStatus findPendingStatus(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		return systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
	}
	
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
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

	public Boolean getIsReadyForPrint() {
		return isReadyForPrint;
	}

	public void setIsReadyForPrint(Boolean isReadyForPrint) {
		this.isReadyForPrint = isReadyForPrint;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	
}
