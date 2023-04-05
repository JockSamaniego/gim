package org.gob.gim.common.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

@Name("residentChooserHome")
@Scope(ScopeType.CONVERSATION)
public class ResidentChooserHome extends EntityController{
	private static final long serialVersionUID = 1L;
	
	@Logger
	Log logger;

	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;

	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		logger.info("SEARCH BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void clearSearchPanel() {
		criteria = null;
		residents = null;
	}
	
	public void search() {
		logger.info("RESIDENT CHOOSER CRITERIA " + this.criteria);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.resident = resident;
			//findPendingBonds();
		} catch (Exception e) {
			e.printStackTrace();
			resident = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	public void residentSelectedListener(ActionEvent event) throws Exception {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.resident = resident;
		this.identificationNumber = resident.getIdentificationNumber();

		// findPendingBonds();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	public String getHandicapedPercentage(Resident resident){		
		return resident instanceof Person ? ((Person)resident).getHandicapedPercentage() != null ? ((Person)resident).getHandicapedPercentage().toString() : "" : "";
	}
	
	//para calcular la edad de los contribuyentes
	
	private String fontColor = "black";
	private Boolean resultExist = Boolean.FALSE;
	private Boolean residentIsDead = Boolean.FALSE;
	private int ageMonths;
	private int ageDays;
	
	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public Boolean getResultExist() {
		return resultExist;
	}

	public void setResultExist(Boolean resultExist) {
		this.resultExist = resultExist;
	}

	public Boolean getResidentIsDead() {
		return residentIsDead;
	}

	public void setResidentIsDead(Boolean residentIsDead) {
		this.residentIsDead = residentIsDead;
	}

	public int getAgeMonths() {
		return ageMonths;
	}

	public void setAgeMonths(int ageMonths) {
		this.ageMonths = ageMonths;
	}

	public int getAgeDays() {
		return ageDays;
	}

	public void setAgeDays(int ageDays) {
		this.ageDays = ageDays;
	}
	

	public String calculateAge(Resident resident){
		ageMonths = 0;
		ageDays = 0;
		resultExist = Boolean.FALSE;
		residentIsDead = Boolean.FALSE;
		fontColor = "black";
		if (resident == null){
			return null;
		}
		Person person = getEntityManager().find(Person.class, resident.getId());
		try {
			//Person person = (Person)resident;
			if(!person.getIsDead()){
				Date bornDate = person.getBirthday();
				Date currentDate = new Date();
				if(currentDate!=null && bornDate!=null){
					SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
				    int currentYear = Integer.parseInt(getYearFormat.format(currentDate));
				    int bornYear = Integer.parseInt(getYearFormat.format(bornDate));
				    int years = currentYear - (bornYear + 1);
				    
				    SimpleDateFormat getMonthFormat = new SimpleDateFormat("MM");
				    int currentMonth = Integer.parseInt(getMonthFormat.format(currentDate));
				    int bornMonth = Integer.parseInt(getMonthFormat.format(bornDate));
				    int months = currentMonth - bornMonth;
				    
				    SimpleDateFormat getDayFormat = new SimpleDateFormat("dd");
			        int currentDay = Integer.parseInt(getDayFormat.format(currentDate));
			        int bornDay = Integer.parseInt(getDayFormat.format(bornDate));
			        int days = currentDay - bornDay;
				    
				    ageMonths = months - 1;
				    ageDays = days;
				    if(months > 0){
				    	years = years + 1;
				    	if(days >= 0) {
				    		ageMonths = ageMonths + 1;
				        } else {
				        	ageDays = ageDays + 30;
				        }
				    } else if(months == 0){ 
				        if(days >= 0) {
				        	ageMonths = ageMonths + 1;
				        	years = years + 1;
				        } else {
				        	ageMonths = ageMonths + 12;
				        	ageDays = ageDays + 30;
				        }	    	
				    }  else if(months < 0){
				    	ageMonths = ageMonths + 13;
				    	if(days < 0){
				    		ageDays = ageDays + 30;
				    	}
				    }
				    
				    
				    if(years >= 65 && !person.getIsDead()){
				    	fontColor = "red";
				    	resident.setElderly(true);
				    } else if(person.getIsDead()){
				    	fontColor = "black";
				    	residentIsDead = Boolean.TRUE;
				    	resident.setElderly(false);
				    	//return String.valueOf(years) + " años (fallecido/a)";
				    }
				    resultExist = Boolean.TRUE;
				    return String.valueOf(years);
				}
		    	resident.setElderly(false);
				return "No disponible";
			} else {
				resultExist = Boolean.FALSE;
				residentIsDead = Boolean.TRUE;
				return "";
			}
		} catch (Exception e) {
	    	resident.setElderly(false);
			return "No disponible";
		}	 
		
	}

}
