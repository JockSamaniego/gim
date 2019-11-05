package org.gob.gim.cadaster.action;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.cadaster.facade.CadasterService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.action.AdjunctAction;
import org.gob.gim.revenue.action.AdjunctHome;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.adjunct.PropertyAppraisal;

@Name("changePropertyAndBondsHome")
public class ChangePropertyAndBondsHome extends EntityHome<MunicipalBond> {

	/**
	 * 
	 */
	@In(create = true)
	AdjunctAction adjunctAction;
	
	@In(create = true)
	AdjunctHome adjunctHome;
	
	private static final long serialVersionUID = 1L;
	
	private List<Resident> residents = new ArrayList<Resident>();
	private Resident residentLast;
	private String identificationResidentLast;
	private String criteriaResidentLast;
	private Resident residentNew;
	private String identificationResidentNew;
	private String criteriaResidentNew;
	private String groupingCode;
	private List<Property> properties = new ArrayList<Property>();
	private List<MunicipalBond> bonds = new ArrayList<MunicipalBond>();
	

	public static String MUNICIPALBOND_SERVICE_NAME = "/gim/MunicipalBondService/local";
	public static String CADASTER_SERVICE_NAME = "/gim/CadasterService/local";

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

	public MunicipalBond getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public Resident getResidentLast() {
		return residentLast;
	}

	public void setResidentLast(Resident residentLast) {
		this.residentLast = residentLast;
	}

	public String getIdentificationResidentLast() {
		return identificationResidentLast;
	}

	public void setIdentificationResidentLast(String identificationResidentLast) {
		this.identificationResidentLast = identificationResidentLast;
	}

	public String getCriteriaResidentLast() {
		return criteriaResidentLast;
	}

	public void setCriteriaResidentLast(String criteriaResidentLast) {
		this.criteriaResidentLast = criteriaResidentLast;
	}

	public Resident getResidentNew() {
		return residentNew;
	}

	public void setResidentNew(Resident residentNew) {
		this.residentNew = residentNew;
	}

	public String getIdentificationResidentNew() {
		return identificationResidentNew;
	}

	public void setIdentificationResidentNew(String identificationResidentNew) {
		this.identificationResidentNew = identificationResidentNew;
	}

	public String getCriteriaResidentNew() {
		return criteriaResidentNew;
	}

	public void setCriteriaResidentNew(String criteriaResidentNew) {
		this.criteriaResidentNew = criteriaResidentNew;
	}

	public String getGroupingCode() {
		return groupingCode;
	}

	public void setGroupingCode(String groupingCode) {
		this.groupingCode = groupingCode;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<MunicipalBond> getBonds() {
		return bonds;
	}

	public void setBonds(List<MunicipalBond> bonds) {
		this.bonds = bonds;
	}

	@SuppressWarnings("unchecked")
	public void findBondsByResidentAndGroupingCode(){
		bonds.clear();
		properties.clear();
		this.setResidentNew(null);
		if (this.residentLast != null && this.groupingCode != null && !this.groupingCode.isEmpty()) {
			Query query = this.getEntityManager().createNamedQuery(
			"MunicipalBond.findBondsCadastralByResidentAndGroupingCode");
			query.setParameter("residentId", residentLast.getId());
			query.setParameter("groupingCode", groupingCode);
			bonds = query.getResultList();
			
			query = this.getEntityManager().createNamedQuery(
			"Property.findByCadastralCode");
			query.setParameter("criteria", groupingCode);
			properties = query.getResultList();
		}
	}
	
	public void searchResidentLast() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationResidentLast);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setResidentLast(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.setResidentLast(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	@SuppressWarnings("unchecked")
	public void searchResidentLastByCriteria() {
		if (this.criteriaResidentLast != null && !this.criteriaResidentLast.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteriaResidentLast);
			setResidents(query.getResultList());
		}
	}

	public void residentLastSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.setResidentLast(resident);
		this.setIdentificationResidentLast(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setResidentLast(null);
		this.setResidentNew(null);
		this.setResidents(null);
		this.identificationResidentLast = "";
		this.identificationResidentNew = "";
		this.groupingCode = "";
		this.properties.clear();
		bonds.clear();
	}

	public void searchResidentNew() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationResidentNew);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setResidentNew(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.setResidentNew(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	@SuppressWarnings("unchecked")
	public void searchResidentNewByCriteria() {
		if (this.criteriaResidentNew != null && !this.criteriaResidentNew.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteriaResidentNew);
			setResidents(query.getResultList());
		}
	}

	public void residentNewSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.setResidentNew(resident);
		this.setIdentificationResidentNew(resident.getIdentificationNumber());
	}

	public void updateBonds(){
		persist();
		clearSearchResidentPanel();
	}
	
	@Override
	public String persist() {
		if (!bonds.isEmpty() && residentNew != null && properties.size() <= 1){
			String actualCadastralCode = "";
			String newPreviousCadastralCode = "";

			CadasterService cadasterService = ServiceLocator.getInstance().findResource(CADASTER_SERVICE_NAME);
			for (Property p: properties) {
				Domain d = p.getCurrentDomain();
				d.setResident(residentNew);
				actualCadastralCode = p.getCadastralCode();
				newPreviousCadastralCode = p.getPreviousCadastralCode();
				try {
//					cadasterService.update(d);
				} catch (Exception e) {
					addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
					e.printStackTrace();
				}
			}

			MunicipalBondService mbService = ServiceLocator.getInstance().findResource(MUNICIPALBOND_SERVICE_NAME);
			for (MunicipalBond bond : bonds) {
				bond.setResident(residentNew);
				PropertyAppraisal pAppraisal;
				if (bond.getAdjunct() != null){
					Query query = getEntityManager().createNamedQuery(
							"PropertyAppraisal.findById");
					query.setParameter("idPropertyAppraisal", bond.getAdjunct().getId());
					pAppraisal =(PropertyAppraisal) (query.getSingleResult());
				}
				else
					pAppraisal = null;
				try {
					if (pAppraisal != null){
						String lastPreviousCode = pAppraisal.getPreviousCadastralCode();
						if (bond.getGroupingCode().indexOf(actualCadastralCode) != -1){
							pAppraisal.setPreviousCadastralCode(newPreviousCadastralCode);
							bond.setReference(bond.getReference().replace(lastPreviousCode, newPreviousCadastralCode));
						}
						mbService.update(pAppraisal);
					}
					mbService.update(bond);
				} catch (Exception e) {
					addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
					e.printStackTrace();
					return null;
				}
			}
			
			return "persisted";
		} else {
			addFacesMessageFromResourceBundle("No se pudo Actualizar la Informacion");
			return null;
		}
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
	
}
