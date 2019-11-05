package org.gob.gim.common.action;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.security.model.Action;

@Name("actionHome")
public class ActionHome extends EntityHome<Action> {
	
	

	public void setActionId(Long id) {
		setId(id);
	}

	public Long getActionId() {
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

	public Action getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Action> searchActionByName(Object suggest) {
		//System.out.println("entra a consultar????????????????????????????????????????????");
		String q = "SELECT a FROM Action a "
				+ "WHERE LOWER(a.name) LIKE CONCAT(LOWER(:suggest),'%')";
		Query e = this.getEntityManager().createQuery(q);
		e.setParameter("suggest", (String) suggest);
		return (List<Action>) e.getResultList();
	}
	
	public void addAction(Action act) {
		this.getInstance().add(act);
	}
	
	public void removeAction(Action act) {
		this.getInstance().remove(act);
	}
	
	
}
