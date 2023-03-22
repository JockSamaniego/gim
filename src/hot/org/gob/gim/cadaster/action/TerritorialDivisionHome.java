package org.gob.gim.cadaster.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Block;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.cadaster.model.TerritorialDivisionType;

@Name("territorialDivisionHome")
public class TerritorialDivisionHome extends EntityHome<TerritorialDivision> {

	private static final long serialVersionUID = 7830466345215560335L;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	@In(create = true)
	TerritorialDivisionHome territorialDivisionHome;
	@In(create = true)
	TerritorialDivisionTypeHome territorialDivisionTypeHome;
	@Logger
	private Log logger;
	
	private SystemParameterService systemParameterService;
	
	/*
	@In(scope=ScopeType.SESSION, required=false)
	@Out(scope=ScopeType.SESSION, required=false)
	*/
	private TerritorialDivisionType childrenTerritorialDivisionType;

	public void setTerritorialDivisionId(Long id) {
		setId(id);
	}

	public Long getTerritorialDivisionId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
	
	public void wire() {
		getInstance();
		if(!isManaged()){
			getInstance().setTerritorialDivisionType(getDefaultTerritorialDivisionType());
		}
		TerritorialDivisionType territorialDivisionType = territorialDivisionTypeHome.getDefinedInstance();
		if (territorialDivisionType != null) {
			getInstance().setTerritorialDivisionType(territorialDivisionType);
		}
	}

	public boolean isWired() {
		return true;
	}

	public TerritorialDivision getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Block> getBlocks() {
		return getInstance() == null ? null : new ArrayList<Block>(getInstance().getBlocks());
	}
	public List<TerritorialDivision> getTerritorialDivisions() {
		return getInstance() == null ? null : new ArrayList<TerritorialDivision>(getInstance().getTerritorialDivisions());
	}

	//@Observer("org.jboss.seam.postCreate.territorialDivisionList")
	private TerritorialDivisionType getDefaultTerritorialDivisionType(){
		List<TerritorialDivisionType> territorialDivisionTypes = (List<TerritorialDivisionType>) getSessionContext().get("territorialDivisionTypes");
		return territorialDivisionTypes.get(0);
		/*
		Query query = getEntityManager().createNamedQuery("TerritorialDivisionType.findAll");
		List<TerritorialDivisionType> territorialDivisionTypes = query.getResultList();
		return territorialDivisionTypes.get(0);
		*/
	}
	
	@Factory("childrenTerritorialDivisionType")
	public TerritorialDivisionType getChildrenTerritorialDivisionType(){
		if(childrenTerritorialDivisionType==null){
			TerritorialDivisionType currentTerritorialDivisionType = getInstance().getTerritorialDivisionType();
			List<TerritorialDivisionType> territorialDivisionTypes = (List<TerritorialDivisionType>) getSessionContext().get("territorialDivisionTypes");
//			logger.info("LOXAGEEK 2011-08-28 ---> NULL?: "+territorialDivisionTypes==null);
			if(territorialDivisionTypes!=null){
//				logger.info("LOXAGEEK 2011-08-28 ---> Tamaño: "+territorialDivisionTypes.size());
				for(int i = 0; i<territorialDivisionTypes.size(); i++){
					if(currentTerritorialDivisionType.equals(territorialDivisionTypes.get(i))){
						if(i < (territorialDivisionTypes.size() - 1)){
							return territorialDivisionTypes.get(i+1);
						}
					}
				}
			}		
			return null;
		}
		return childrenTerritorialDivisionType;
	}
	
	public void add(){
//		logger.info("Children of type "+getChildrenTerritorialDivisionType().getName()+" added ");
		TerritorialDivision child = new TerritorialDivision();
		child.setTerritorialDivisionType(getChildrenTerritorialDivisionType());
		getInstance().add(child);
	}
	
	public void remove(TerritorialDivision node){
		if(node.getTerritorialDivisions().size() > 0 || node.getBlocks().size() > 0){
			addFacesMessageFromResourceBundle("territorialDivision.canNotDeleteTerritorialDivision");
			return;
		}
		getInstance().remove(node);
	}
	
	@Factory("defaultProvince")
	public TerritorialDivision findDefaultProvince(){	
		if (systemParameterService == null)systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String code = systemParameterService.findParameter("TERRITORIAL_DIVISION_CODE_PROVINCE");
		Long territorialDivisionTypeId = systemParameterService.findParameter("TERRITORIAL_DIVISION_TYPE_ID_PROVINCE");
		return findTerritorialDivision(code, territorialDivisionTypeId);
	}

	@Factory("defaultCanton")
	public TerritorialDivision findDefaultCanton(){
		TerritorialDivision province = findDefaultProvince(); 
		if (systemParameterService == null)systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
				
		String code = systemParameterService.findParameter("TERRITORIAL_DIVISION_CODE_CANTON");
		Long territorialDivisionTypeId = systemParameterService.findParameter("TERRITORIAL_DIVISION_TYPE_ID_CANTON");
		return findTerritorialDivision(code, territorialDivisionTypeId, province);
	}
	
	private TerritorialDivision findTerritorialDivision(String code, long territorialDivisionTypeId, TerritorialDivision parent){
		Query query = getEntityManager().createNamedQuery("TerritorialDivision.findByTypeAndCodeAndParent");
		query.setParameter("code", code);
		query.setParameter("territorialDivisionTypeId", territorialDivisionTypeId);
		query.setParameter("parent", parent);
		TerritorialDivision territorialDivision = (TerritorialDivision) query.getSingleResult();
		return territorialDivision;
	}
	
	private TerritorialDivision findTerritorialDivision(String code, long territorialDivisionTypeId){
		Query query = getEntityManager().createNamedQuery("TerritorialDivision.findByTypeAndCode");
		query.setParameter("code", code);
		query.setParameter("territorialDivisionTypeId", territorialDivisionTypeId);
		TerritorialDivision territorialDivision = (TerritorialDivision) query.getSingleResult();
		return territorialDivision;
	}

	public void setSystemParameterService(SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

}
