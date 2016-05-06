package org.gob.gim.common.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.security.model.Action;
import ec.gob.gim.security.model.Permission;
import ec.gob.gim.security.model.Role;
import ec.gob.gim.security.model.User;

@Name("roleHome")
public class RoleHome extends EntityHome<Role> {
	
	private static final long serialVersionUID = 1566469317930163860L;
	
	
	public void setRoleId(Long id) {
		setId(id);
	}

	public Long getRoleId() {
		return (Long) getId();
	}

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

	public Role getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void addUser(User user) {
		getInstance().add(user);
	}
	
	public boolean hasAction(Action a){
		for(Permission p : this.getInstance().getPermissions()){
			if(p.getAction().equals(a)){
				return true;
			}
		}
		return false;
	}	
	
	public void addPermission(Action a) {		
		if(!hasAction(a)){
			Permission per = new Permission();
			per.setAction(a);
			getInstance().add(per);
		}
	}
	
	public void removeUser(User user) {
		getInstance().remove(user);
	}
	
	public void removePermission(Permission per) {
		getInstance().remove(per);
	}
	
	public List<User> getUsers(){
		return getInstance() == null ? null : new ArrayList<User>(
				getInstance().getUsers()); 
	}
	
	
	@SuppressWarnings("unchecked")
	public List<User> searchUserByNameOrIdentifierOrResidentName(Object suggest) {

		String q = "SELECT user FROM User user "
				+ "WHERE LOWER(user.name)LIKE CONCAT(LOWER(:suggest),'%') "
				+ " OR LOWER(user.resident.name) LIKE CONCAT(lower(:suggest),'%') "
				+ " OR LOWER(user.resident.identificationNumber) LIKE CONCAT(lower(:suggest),'%'))";
		Query e = getEntityManager().createQuery(q);
		e.setParameter("suggest", (String) suggest);
		return (List<User>) e.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Action> searchActionByName(Object suggest) {
		String q = "SELECT action FROM Action action "
				+ "WHERE LOWER(action.name)LIKE CONCAT(LOWER(:suggest),'%') ";				
		Query e = getEntityManager().createQuery(q);
		e.setParameter("suggest", (String) suggest);
		return (List<Action>) e.getResultList();
	}



}
