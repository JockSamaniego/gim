package org.gob.gim.revenue.action;

import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.revenue.model.Adjunct;

@Name("adjunctHome")
public class AdjunctHome extends EntityHome<Adjunct> {

	private static final long serialVersionUID = 5480310161334213615L;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private SystemParameterService systemParameterService;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	@SuppressWarnings("unchecked")
	public void findByCode(){
		Query query = getEntityManager().createNamedQuery("Adjunct.findByCode");
		query.setParameter("code", getInstance().getCode());
		try{
			
			List<Adjunct> adjuncts = query.getResultList();
		 
			if(adjuncts != null && adjuncts.size() == 1){
				setInstance(adjuncts.get(0));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public Boolean hasRole(String roleKey) {
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String role = systemParameterService.findParameter(roleKey);
		if(role != null){
			return userSession.getUser().hasRole(role);
		}
		return false;
	}

}