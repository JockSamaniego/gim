package org.gob.gim.cadaster.action;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.cadaster.model.TerritorialDivisionType;

@Name("territorialDivisionList")
public class TerritorialDivisionList extends EntityQuery<TerritorialDivision> {

	private static final long serialVersionUID = 5963616265599473018L;

	private static final String EJBQL = "select territorialDivision from TerritorialDivision territorialDivision";

	private static final String[] RESTRICTIONS = {		
			"territorialDivision.territorialDivisionType = #{territorialDivisionList.territorialDivisionType}",
			"lower(territorialDivision.code) like lower(concat(#{territorialDivisionList.criteria},'%')) or "+
			"lower(territorialDivision.name) like lower(concat(:el2,'%'))",};
	
	private String criteria;
	
	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private TerritorialDivisionType territorialDivisionType;
	
	@Logger
	private Log logger;

	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private TerritorialDivisionType defaultTerritorialDivisionType;
	
	public TerritorialDivisionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrder("territorialDivision.territorialDivisionType.priority");
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public TerritorialDivisionType getTerritorialDivisionType() {
		return territorialDivisionType;
	}

	public void setTerritorialDivisionType(TerritorialDivisionType territorialDivisionType) {
		this.territorialDivisionType = territorialDivisionType;
	}
	
	public TerritorialDivisionType getDefaultTerritorialDivisionType() {
		return defaultTerritorialDivisionType;
	}

	@Factory(value="territorialDivisionTypes",scope=ScopeType.SESSION)
	public List<TerritorialDivisionType> findTerritorialDivisionTypes(){
		Query query = getEntityManager().createNamedQuery("TerritorialDivisionType.findAll");
		List<TerritorialDivisionType> territorialDivisionTypes = query.getResultList();
		territorialDivisionType = territorialDivisionTypes.get(0);
		defaultTerritorialDivisionType = territorialDivisionTypes.get(0);
		return territorialDivisionTypes;
	}
	

}
