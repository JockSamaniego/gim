package org.gob.gim.ant.ucot.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.ant.ucot.model.*;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Resident;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("infractionsHome")
public class InfractionsHome extends EntityHome<Infractions> {

	@In(create = true)
	BulletinHome bulletinHome;
	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setInfractionsId(Long id) {
		setId(id);
	}

	public Long getInfractionsId() {
		return (Long) getId();
	}

	@Override
	protected Infractions createInstance() {
		Infractions infractions = new Infractions();
		return infractions;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Bulletin bulletin = bulletinHome.getDefinedInstance();
		if (bulletin != null) {
			getInstance().setBulletin(bulletin);
		}
		/*ItemCatalog status = itemCatalogHome.getDefinedInstance();
		if (status != null) {
			getInstance().setStatus(status);
		}
		ItemCatalog type = itemCatalogHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}*/
	}

	public boolean isWired() {
		return true;
	}

	public Infractions getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	private String message;
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

	public String saveInfraction(Bulletin bulletin){
		getInstance().setBulletin(bulletin);
		//validaciones
				message=null;

				BigInteger serial = this.instance.getSerial();
				boolean outRank  = serialOutRank(serial, bulletin);
				if(outRank){
					message="El serial esta fuera del rango del boletin";
					return "/ant/ucot/InfractionsEdit.xhtml";
				}
				
				boolean exist  = serialExist(serial);
				if(exist){
					message="El serial ya existe";
					return "/ant/ucot/InfractionsEdit.xhtml";
				}

				//guardado en BD
		
		persist();
		return "/ant/ucot/InfractionsList.xhtml";
	}
	
	public Boolean serialOutRank(BigInteger serial, Bulletin bulletin){
		if(serial.compareTo(bulletin.getStartNumber())>=0 && serial.compareTo(bulletin.getEndNumber())<=0){
			return false;
			
		}
		return true;
	}
	
	public Boolean serialExist(BigInteger serial){
		List<Infractions> infractions = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"infractions.findBySerial");
		query.setParameter("serial", serial);
		infractions = query.getResultList();
		
		if(infractions.size()>0){
			return true;
		}
		return false;
		
	}

}
