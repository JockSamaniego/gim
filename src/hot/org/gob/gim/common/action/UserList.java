package org.gob.gim.common.action;

import java.util.Arrays;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.common.service.UserService;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.security.model.User;

@Name("userList")
public class UserList extends EntityQuery<User> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "SELECT u FROM User u ";

	private static final String[] RESTRICTIONS = {
		"lower(u.resident.identificationNumber) like lower(concat(#{userList.criteria},'%'))",
		"lower(u.resident.name) like lower(concat(#{userList.criteria},'%'))",
		"lower(u.name) like lower(concat(#{userList.criteria},'%'))"
	};
	
	private String criteria;

	public UserList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	@Override
	public String getOrder() {
		return "u.resident.name";
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public String lockAll(){
		UserService userService = ServiceLocator.getInstance().findResource(UserService.LOCAL_NAME);
		userService.lockAll();		
		addFacesMessageFromResourceBundle("security.allUsersLocked");
		return "locked";
	}
	
	public String unlockAll(){
		UserService userService = ServiceLocator.getInstance().findResource(UserService.LOCAL_NAME);
		userService.unlockAll();
		addFacesMessageFromResourceBundle("security.allUsersUnlocked");
		return "unlocked";
	}
	
	@Factory("rootRole")
	public String getRootRoleName(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);		
		String rootRoleName = systemParameterService.findParameter("ROLE_NAME_ADMINISTRATOR");
		return rootRoleName;
	}
}

