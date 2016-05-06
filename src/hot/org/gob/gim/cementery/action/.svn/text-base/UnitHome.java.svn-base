package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.cementery.model.UnitType;

@Name("unitHome")
public class UnitHome extends EntityHome<Unit> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Log logger;
	
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

	public Unit getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setUnitId(Long id) {
		setId(id);
	}

	public Long getUnitId() {
		return (Long) getId();
	}

}
