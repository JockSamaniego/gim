package org.gob.gim.ant.ucot.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.Query;

import ec.gob.gim.ant.ucot.model.*;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.gob.gim.common.NativeQueryResultsMapper;
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
	
	private Date registerDate;

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public void wire() {
		registerDate = new Date();
		getInstance();
		Bulletin bulletin = bulletinHome.getDefinedInstance();
		if (bulletin != null) {
			getInstance().setBulletin(bulletin);
		}
		if(this.instance.getPhotoFine() == null){
			this.instance.setPhotoFine(Boolean.FALSE);
		}
		/*ItemCatalog status = itemCatalogHome.getDefinedInstance();
		if (status != null) {
			getInstance().setStatus(status);
		}
		ItemCatalog type = itemCatalogHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}*/
		findSalaryBasic();
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
	private String detail;
	
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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
	
	public void findResidentName(){
		if(this.instance.getIdentification() != null && this.instance.getIdentification() != ""){
			List<String> names = new ArrayList<String>();
			Query query = getEntityManager().createNamedQuery(
					"infractions.findResidentNameByIdent");
			query.setParameter("identNum", this.instance.getIdentification());
			names = query.getResultList();
			if(names.size()>0){
				this.instance.setName(names.get(0));
			}else{
				this.instance.setName("No registrado");	
			}
		}else{
			this.instance.setName(null);
		}	
	}
	
	public List<String> chargeArticles(){
		List<String> articles = new ArrayList();
			if(!this.instance.getPhotoFine()){
				String query = "SELECT DISTINCT co.article FROM gimprod.coip co WHERE co.isphotofine = false ORDER BY co.article ASC ";
				Query q = this.getEntityManager().createNativeQuery(query);
				articles = q.getResultList();
			}else{
				String query = "SELECT DISTINCT co.article FROM gimprod.coip co WHERE co.isphotofine = true ORDER BY co.article ASC ";
				Query q = this.getEntityManager().createNativeQuery(query);
				articles = q.getResultList();
			}
		resetValues();
		System.out.println("articles size "+articles.size());
		return articles;
	}
	
	public List<String> findNumeralByArticle(String article){
		List<String> numerals = new ArrayList<String>();
		if(!this.instance.getPhotoFine()){
			String query = "SELECT co.numeral FROM gimprod.coip co WHERE co.article = '"+this.instance.getArticle()+"' and co.isphotofine = false ORDER BY co.numeral ASC";
			Query q = this.getEntityManager().createNativeQuery(query);
			numerals = q.getResultList();
		}else{
			String query = "SELECT co.numeral FROM gimprod.coip co WHERE co.article = '"+this.instance.getArticle()+"' and co.isphotofine = true ORDER BY co.numeral ASC";
			Query q = this.getEntityManager().createNativeQuery(query);
			numerals = q.getResultList();
		}
		System.out.println("numerals size "+numerals.size());
		return numerals;
	}
	
	public void chargeValues(){
		String query = "SELECT co.points AS points, co.percentaje AS percentaje, co.detail AS detail FROM gimprod.coip co WHERE co.article = '"+this.instance.getArticle()+"' And co.numeral = '"+this.instance.getNumeral()+"'";
		Query q = this.getEntityManager().createNativeQuery(query);
		List<CoipDTO> coipList = NativeQueryResultsMapper.map(q.getResultList(), CoipDTO.class);
		if(!coipList.isEmpty()){
			this.instance.setPoints(coipList.get(0).getPoints());
			this.porcentage = coipList.get(0).getPercetaje();
			this.detail = coipList.get(0).getDetail();
			calculateValue();
		}else{
			resetValues();
		}	
	}
	
	public void findSalaryBasic(){
		if(this.salary == null){
			Date date = new Date();
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			String today = dt.format(date);
			String query = "SELECT fp.basicsalaryunifiedforrevenue FROM gimprod.fiscalperiod fp WHERE fp.startdate <= '"+today+"' AND fp.enddate >= '"+today+"'";
			Query q = this.getEntityManager().createNativeQuery(query);
			this.salary = (BigDecimal) q.getResultList().get(0);
		}
		
	}
	
	public void resetValues(){
		if(!this.infractionToEdit){
			this.instance.setNumeral(null);
			this.instance.setPartNumber(null);
			this.instance.setPoints(null);
			this.porcentage = null;
			this.instance.setValue(null);	
			this.detail = null;
		}
		this.infractionToEdit = Boolean.FALSE;
	}
	
	private Boolean infractionToEdit = Boolean.FALSE;
	
	public Boolean getInfractionToEdit() {
		return infractionToEdit;
	}

	public void setInfractionToEdit(Boolean infractionToEdit) {
		this.infractionToEdit = infractionToEdit;
	}

	public void prepareToEdit(){
		infractionToEdit = Boolean.TRUE;
	}
	
	private static Boolean photoFineFR;


	
	public static Boolean getPhotoFineFR() {
		return photoFineFR;
	}

	public static void setPhotoFineFR(Boolean photoFineFR) {
		InfractionsHome.photoFineFR = photoFineFR;
	}

	public void isPhotoFineFR(){
		InfractionsHome.photoFineFR = Boolean.TRUE;
	}
	
	public void isNotPhotoFineFR(){
		InfractionsHome.photoFineFR = Boolean.FALSE;
	}
	
	public void prepareToCreateAndEditPhotoFine(){
		wire();
		this.instance.setPhotoFine(Boolean.TRUE);
		this.instance.setFixedRadar(Boolean.TRUE);
	}

	public String savePhotoFineFR(){

		//validaciones
				message=null;

				
				boolean exist  = radarCodeExist(this.instance.getRadarCode());
				if(exist){
					message="El código de radar ya existe";
					return "/ant/ucot/PhotoFineFREdit.xhtml";
				}

				//guardado en BD
				this.getInstance().setCreationDate(new Date());				 
		        this.getInstance().setResponsible_user(userSession.getUser().getResident().getName());	 
		        this.getInstance().setResponsible(userSession.getPerson());

		
		persist();
		return "/ant/ucot/PhotoFineFRList.xhtml";
	}
	
	public Boolean radarCodeExist(String radarCode){
		List<Infractions> infractions = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"infractions.findByRadarCode");
		query.setParameter("radarCode", radarCode);
		infractions = query.getResultList();
		
		if(infractions.size()>0){
			return true;
		}
		return false;
		
	}
}
