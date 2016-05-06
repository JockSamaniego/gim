package org.gob.gim.commercial.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.commercial.model.EconomicActivity;
import ec.gob.gim.commercial.model.EconomicActivityType;

@Name("economicActivityList")
public class EconomicActivityList extends EntityQuery<EconomicActivity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select economicActivity from EconomicActivity economicActivity left join fetch economicActivity.parent";

	private static final String[] RESTRICTIONS = {
			"economicActivity.economicActivityType = #{economicActivityList.economicActivityType}",
			"(lower(economicActivity.code) like lower(concat(#{economicActivityList.criteria},'%')) or " +
			"lower(economicActivity.name) like lower(concat(:el2,'%')))",
			};
	
	private EconomicActivityType economicActivityType = null;
		
	private String criteria;
	

	public EconomicActivityList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

		
	@Factory("economicActivityTypes")
	public List<EconomicActivityType> loadEconomicActivityTypes(){
		return Arrays.asList(EconomicActivityType.values());
	}

	/**
	 * @return the economicActivityType
	 */
	public EconomicActivityType getEconomicActivityType() {
		return economicActivityType;		
	}

	/**
	 * @param economicActivityType the economicActivityType to set
	 */
	public void setEconomicActivityType(EconomicActivityType economicActivityType) {
		this.economicActivityType = economicActivityType;
	}

	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	List<EconomicActivityType> economicActivityTypes;
	
	@SuppressWarnings("unchecked")
	public List<EconomicActivity> findEconomicActivities(Object suggest){
		if(economicActivityTypes == null){
			economicActivityTypes = new ArrayList<EconomicActivityType>();
			economicActivityTypes.add(EconomicActivityType.ACTIVITY);
			economicActivityTypes.add(EconomicActivityType.SUBACTIVITY);
		}
		String pref = (String)suggest;
		Query query = this.getEntityManager().createNamedQuery("EconomicActivity.findChildsByType");
		query.setParameter("criteria", pref);
		query.setParameter("types", economicActivityTypes);		
		List<EconomicActivity> lista = query.getResultList();		
		return lista;
	}
		
}