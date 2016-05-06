package org.gob.gim.retentioncode.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Section;
import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.complementvoucher.model.InstitutionService;
import ec.gob.gim.complementvoucher.model.RetentionCode;
import ec.gob.gim.market.model.ProductType;

@Name("retentionCodeHome")
public class RetentionCodeHome extends EntityHome<RetentionCode> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	Log logger; 
  

	public void setRetentionCodeId(Long id) {
		setId(id);
	}

	public Long getRetentionCodeId() {
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
	 
	public RetentionCode getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	 
}
