package org.gob.gim.contravention.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.action.PersonHome;
import org.gob.gim.common.action.ResidentHome;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.revenue.action.MunicipalBondHome;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.contravention.model.Contravention;
import ec.gob.gim.contravention.model.Provenance;
import ec.gob.gim.contravention.model.SanctionType;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("contraventionHome")
public class ContraventionHome extends EntityHome<Contravention> {

	@In(create = true)
	MunicipalBondHome municipalBondHome;
	@In(create = true)
	PersonHome personHome;
	@In(create = true)
	ProvenanceHome provenanceHome;
	@In(create = true)
	ResidentHome residentHome;
	@In(create = true)
	SanctionTypeHome sanctionTypeHome;

	private String criteria;
	private List<Resident> residents;
	private String identificationNumber;
	@Logger
	Log logger;

	@In
	UserSession userSession;
	@In
	private FacesMessages facesMessages;

	public void setContraventionId(Long id) {
		setId(id);
	}

	public Long getContraventionId() {
		return (Long) getId();
	}

	@Override
	protected Contravention createInstance() {
		Contravention contravention = new Contravention();
		return contravention;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		MunicipalBond municipalBond = municipalBondHome.getDefinedInstance();
		if (municipalBond != null) {
			getInstance().setMunicipalBond(municipalBond);
		}
		Person originator = personHome.getDefinedInstance();
		if (originator != null) {
			getInstance().setOriginator(originator);
		}
		Provenance provenance = provenanceHome.getDefinedInstance();
		if (provenance != null) {
			getInstance().setProvenance(provenance);
		}
		Resident resident = residentHome.getDefinedInstance();
		if (resident != null) {
			getInstance().setResident(resident);
		}
		SanctionType sanctionType = sanctionTypeHome.getDefinedInstance();
		if (sanctionType != null) {
			getInstance().setSanctionType(sanctionType);
		}
		finContraventionNumber();
	}

	public boolean isWired() {
		return true;
	}

	public Contravention getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + criteria);
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void searchResident() {
		logger.info("RESIDENT CHOOSER CRITERIA... " + this.identificationNumber);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.getInstance().setResident(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	@Override
	public String persist() {
		this.getInstance().setOriginator(userSession.getPerson());
		return super.persist();
	}

	@SuppressWarnings("unchecked")
	@Factory("provenances")
	public List<Provenance> loadProvenances() {
		Query query = this.getEntityManager().createNamedQuery(
				"Provenance.findAll");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Factory("sanctionTypes")
	public List<SanctionType> loadSanctionTypes() {
		Query query = this.getEntityManager().createNamedQuery(
				"SanctionType.findAll");
		return query.getResultList();
	}

	public void finContraventionNumber() {
		if (this.getInstance().getNumber() == null) {
			Integer year = Integer.parseInt(new SimpleDateFormat("yyyy")
					.format(new Date()));
			Query query = this.getEntityManager().createNamedQuery(
					"Contravention.findMaxNumber");
			query.setParameter("year", year);
			Object o = query.getSingleResult();
			if (o != null) {
				int code = Integer.parseInt(o.toString());
				this.getInstance().setNumber(code + 1);
			} else {
				this.getInstance().setNumber(1);
			}

			this.getInstance().setYear(year);
		}
	}

	public void updateInactiveContravention() {
		this.getInstance().setIsActive(Boolean.FALSE);
		super.update();
		//return "inactived";
	}
}