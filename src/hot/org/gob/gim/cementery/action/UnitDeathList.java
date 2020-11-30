package org.gob.gim.cementery.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Section;
import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;

@Name("unitDeathList")
public class UnitDeathList extends EntityQuery<Unit> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select unit from Unit unit "
			+ "left join fetch unit.currentDeath currentDeath "
			+ "left join currentDeath.deceased deceased ";
	
	private static final String[] RESTRICTIONS = {
			"lower(unit.section.name) = lower(#{unitDeathList.sectionName})",
			"lower(unit.section.cementery.name) like lower(#{unitDeathList.cementeryName})",
			"unit.currentDeath.dateOfDeath = #{unitDeathList.dateOfDeath}",
			"unit.unitStatus = #{unitDeathList.unitStatus}",
			"lower(unit.unitType.name) = lower(#{unitDeathList.unitType})",
			"lower(unit.currentDeath.deathName) like lower(concat(#{unitDeathList.deceasedCriteria},'%'))",
			"lower(unit.currentDeath.currentContract.subscriber.name) like lower(concat(#{unitDeathList.debtorCriteria},'%'))"
	};
	private String cementeryName;
	private String sectionName;
	private String deceasedCriteria;
	private String debtorCriteria;
	private Date dateOfDeath;
	private UnitStatus unitStatus;
	private String unitType;

	public UnitDeathList() {
		setOrder("unit.section.cementery.name, unit.section.name, unit.code");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	public String getCementeryName() {
		return cementeryName;
	}

	public void setCementeryName(String cementeryName) {
		this.cementeryName = cementeryName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getDeceasedCriteria() {
		return deceasedCriteria;
	}

	public void setDeceasedCriteria(String deceasedCriteria) {
		this.deceasedCriteria = deceasedCriteria;
	}

	public String getDebtorCriteria() {
		return debtorCriteria;
	}

	public void setDebtorCriteria(String debtorCriteria) {
		this.debtorCriteria = debtorCriteria;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public UnitStatus getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(UnitStatus unitStatus) {
			this.unitStatus = unitStatus;
	}
	
	public void setUnitStatusNull(){
		this.unitStatus = null;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getUnitType() {
		return unitType;
	}
	
	// para obtener secciones por cementerio
	
	private List<String> sectionsByCementery;

	public List<String> getSectionsByCementery() {
		return sectionsByCementery;
	}

	public void setSectionsByCementery(List<String> sectionsByCementery) {
		this.sectionsByCementery = sectionsByCementery;
	}

	public List<String> findSectionsByCementery(){
		sectionsByCementery = new ArrayList();
		if(this.cementeryName != null && this.cementeryName != ""){
			Query q2 = getEntityManager().createNamedQuery("Cementery.findByName");
			q2.setParameter("name", this.cementeryName);
			Cementery cementery = (Cementery) q2.getSingleResult();
			if(cementery != null){
				Query q = getEntityManager().createNamedQuery("Section.findByCementery");
				q.setParameter("cementery", cementery);
				sectionsByCementery = q.getResultList();
			}				
		}
		return sectionsByCementery;
	}

}
