package org.gob.gim.ant.ucot.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import ec.gob.gim.ant.ucot.model.*;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Resident;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
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
	
	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

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
				this.getInstance().setCreationDate(new Date());
				this.getInstance().setResponsible_user(userSession.getUser().getResident().getName());
				this.getInstance().setResponsible(userSession.getPerson());
				
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
	
	private BigDecimal salary;
	private BigDecimal porcentage;
	
	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public BigDecimal getPorcentage() {
		return porcentage;
	}

	public void setPorcentage(BigDecimal porcentage) {
		this.porcentage = porcentage;
	}

	public void calculateValue(){
		if(salary != null && porcentage != null){
			BigDecimal resp;
			resp = salary.multiply(porcentage);
			resp = resp.divide(new BigDecimal(100));
			this.getInstance().setValue(resp);
		}else{
			this.getInstance().setValue(new BigDecimal(0));
		}
	}
}
