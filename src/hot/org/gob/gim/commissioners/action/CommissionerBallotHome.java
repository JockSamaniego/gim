package org.gob.gim.commissioners.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import org.buni.meldware.mail.util.DateUtil;
import org.gob.gim.accounting.dto.AccountItem;
import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.Infractions;
import ec.gob.gim.commissioners.model.CommissionerBallot;
import ec.gob.gim.commissioners.model.CommissionerBallotStatus;
import ec.gob.gim.commissioners.model.CommissionerBulletin;
import ec.gob.gim.commissioners.model.CommissionerInspector;
import ec.gob.gim.commissioners.model.SanctioningArticle;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Resident;

@Name("commissionerBallotHome")
public class CommissionerBallotHome extends EntityHome<CommissionerBallot> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	
	private List<ItemCatalog> hygieneBallotStatus;
	private ItemCatalogService itemCatalogService;
	private Boolean isFirstTime = Boolean.TRUE;
	
	
	public List<ItemCatalog> getHygieneBallotStatus() {
		return hygieneBallotStatus;
	}

	public void setHygieneBallotStatus(List<ItemCatalog> hygieneBallotStatus) {
		this.hygieneBallotStatus = hygieneBallotStatus;
	}

	public ItemCatalogService getItemCatalogService() {
		return itemCatalogService;
	}

	public void setItemCatalogService(ItemCatalogService itemCatalogService) {
		this.itemCatalogService = itemCatalogService;
	}

	public void setInfractionsId(Long id) {
		setId(id);
	}

	public Long getInfractionsId() {
		return (Long) getId();
	}

	@Override
	protected CommissionerBallot createInstance() {
		CommissionerBallot commissionerBallot = new CommissionerBallot();
		return commissionerBallot;
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
		initializeService();
		if(isFirstTime){
			if(this.instance.getId() == null){
				createCommissionerBallotStatus();
				this.commissionerBallotStatus.setObservations("INICIO DE PROCESO");
					this.commissionerBallotStatus.setStatusName(itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONER_BALLOT_STATUS, "INITIATED"));
				addStatus();
			}
			loadCharge();
		}

		isFirstTime = Boolean.FALSE;
	}

	public boolean isWired() {
		return true;
	}
	
	public void initializeService() {
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		chargeStatusForSelect();
	}

	public CommissionerBallot getDefinedInstance() {
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
	 
	 
	public String saveCommissionerBallot(){
		//validaciones
				message=null;

				BigInteger ballotNumber = this.instance.getBallotNumber();
				

				boolean exist  = serialExist(ballotNumber);
				if(exist){
					message="El número de boleta ya existe";
					if(commissionerType.getCode().equals("COMMISSIONER_TRANSIT")){
						return "/commissioners/TransitBallotsEdit.xhtml";
					} else if (commissionerType.getCode().equals("COMMISSIONER_HYGIENE")){
						return "/commissioners/HygieneBallotsEdit.xhtml";
					} else if (commissionerType.getCode().equals("COMMISSIONER_ORNAMENT")){
						return "/commissioners/OrnamentBallotsEdit.xhtml";
					} else if (commissionerType.getCode().equals("COMMISSIONER_AMBIENT")){
						return "/commissioners/AmbientBallotsEdit.xhtml";
					} else if (commissionerType.getCode().equals("LEADERSHIP_HYGIENE")){
						return "/commissioners/LeaderShipBallotsEdit.xhtml";
					} else {
						return null;
					}
					
				}

				//guardado en BD
				this.getInstance().setCreationDate(new Date());				 
		        this.getInstance().setResponsible_user(userSession.getUser().getResident().getName());	 
		        this.getInstance().setResponsible(userSession.getPerson());
		        this.getInstance().setCommissionerBallotType(commissionerType);

		
		persist();
		if(commissionerType.getCode().equals("COMMISSIONER_TRANSIT")){
			return "/commissioners/TransitBallotsList.xhtml";
		} else if (commissionerType.getCode().equals("COMMISSIONER_HYGIENE")){
			return "/commissioners/HygieneBallotsList.xhtml";
		} else if (commissionerType.getCode().equals("COMMISSIONER_ORNAMENT")){
			return "/commissioners/OrnamentBallotsList.xhtml";
		} else if (commissionerType.getCode().equals("COMMISSIONER_AMBIENT")){
			return "/commissioners/AmbientBallotsList.xhtml";
		} else if (commissionerType.getCode().equals("LEADERSHIP_HYGIENE")){
			return "/commissioners/LeaderShipBallotsList.xhtml";
		} else {
			return null;
		}
	}
	

	public Boolean serialExist(BigInteger ballotNumber){
		List<CommissionerBallot> commissionerBallots = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"commissionerBallot.findByNumberAndType");
		query.setParameter("ballotNumber", ballotNumber);
		query.setParameter("commissionerType", commissionerType.getCode());
		commissionerBallots = query.getResultList();
		
		if(commissionerBallots.size()>0){
			return true;
		}
		return false;
		
	}
	
	public void findInfractorName(){
		if(this.instance.getInfractorIdentification() != null && this.instance.getInfractorIdentification() != ""){
			List<Resident> res = new ArrayList<Resident>();
			Query query = getEntityManager().createNamedQuery(
					"commissionerBallot.findResidentByIdent");
			query.setParameter("identNum", this.instance.getInfractorIdentification());
			res = query.getResultList();
			if(res.size()>0){
				this.instance.setInfractorName(res.get(0).getName());
				this.instance.setInfractorAddress(res.get(0).getCurrentAddress().getStreet());
				this.instance.setInfractorPhone(res.get(0).getCurrentAddress().getPhoneNumber());
				this.instance.setInfractorEmail(res.get(0).getEmail());
			}else{
				this.instance.setInfractorName("No registrado");
				this.instance.setInfractorAddress("No registrado");
				this.instance.setInfractorPhone("No registrado");
				this.instance.setInfractorEmail("No registrado");
			}
		}else{
			this.instance.setInfractorName(null);
			this.instance.setInfractorAddress(null);
			this.instance.setInfractorPhone(null);
			this.instance.setInfractorEmail(null);
		}	
	}
	
	public void findInspectorName(){
		if(this.commissionerInspector.getNumberIdentification() != null && this.commissionerInspector.getNumberIdentification() != ""){
			List<String> names = new ArrayList<String>();
			Query query = getEntityManager().createNamedQuery(
					"commissionerBallot.findResidentNameByIdent");
			query.setParameter("identNum", this.commissionerInspector.getNumberIdentification());
			names = query.getResultList();
			if(names.size()>0){
				this.commissionerInspector.setName(names.get(0));
			}else{
				this.commissionerInspector.setName("");	
			}
		}else{
			this.commissionerInspector.setName(null);
		}	
	}
	
	
	public List<SanctioningArticle> chargeArticles(){
		List<SanctioningArticle> articles = new ArrayList();
				String query = "SanctioningArticle.findByType";
				Query q = this.getEntityManager().createNamedQuery(query);
				q.setParameter("commissionerType", commissionerType.getCode());
				articles = q.getResultList();
		return articles;
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
	
	private CommissionerBallotStatus commissionerBallotStatus;
	
	private SanctioningArticle sanctioningArticle;
	
	private CommissionerBulletin commissionerBulletin;
	
	private CommissionerInspector commissionerInspector;
	
	public CommissionerBallotStatus getCommissionerBallotStatus() {
		return commissionerBallotStatus;
	}
	public void setCommissionerBallotStatus(
			CommissionerBallotStatus commissionerBallotStatus) {
		this.commissionerBallotStatus = commissionerBallotStatus;
	}
	
	public SanctioningArticle getSanctioningArticle() {
		return sanctioningArticle;
	}

	public void setSanctioningArticle(SanctioningArticle sanctioningArticle) {
		this.sanctioningArticle = sanctioningArticle;
	}
	
	public CommissionerBulletin getCommissionerBulletin() {
		return commissionerBulletin;
	}

	public void setCommissionerBulletin(CommissionerBulletin commissionerBulletin) {
		this.commissionerBulletin = commissionerBulletin;
	}

	public CommissionerInspector getCommissionerInspector() {
		return commissionerInspector;
	}

	public void setCommissionerInspector(CommissionerInspector commissionerInspector) {
		this.commissionerInspector = commissionerInspector;
	}

	//creates
	
	public void createCommissionerBallotStatus() {
		this.commissionerBallotStatus = new CommissionerBallotStatus();
		
	}
	
	public void createSanctioningArticle() {
		this.sanctioningArticle = new SanctioningArticle();
	
	}
	
	public void createCommissionerBulletin() {
		this.commissionerBulletin = new CommissionerBulletin();
	
	}
	
	public void createCommissionerInspector() {
		this.commissionerInspector = new CommissionerInspector();
	
	}
	
	//updates
	
	public void updateSanctioningArticle(SanctioningArticle sanctioningArticle) {
		this.sanctioningArticle = sanctioningArticle;
	
	}
	
	public void updateCommissionerBulletin(CommissionerBulletin commissionerBulletin) {
		this.commissionerBulletin = commissionerBulletin;
	
	}
	
	public void updateCommissionerInspector(CommissionerInspector commissionerInspector) {
		this.commissionerInspector = commissionerInspector;
	
	}
	
	
	
	public void removeStatus(CommissionerBallotStatus status) {
		this.instance.remove(status);
		if(status.getId()!=null){
			this.getInstance().setCurrentStatus(findLastStatusForBallotExceptId(status.getId()));
		}else{
			this.getInstance().setCurrentStatus(findLastStatusForBallot());	
		}
	}
	
	public void addStatus() {
		this.commissionerBallotStatus.setCreationDate(new Date());
		this.commissionerBallotStatus.setResponsible(userSession.getPerson());
		this.commissionerBallotStatus.setResponsible_user(userSession.getUser().getResident().getName());
		this.getInstance().add(this.commissionerBallotStatus);
		this.getInstance().setCurrentStatus(this.commissionerBallotStatus);
	}

	
	// commissioners
	
	private static ItemCatalog commissionerType;
	List<SanctioningArticle> sanctioningArticles = new ArrayList();
	List<CommissionerInspector> commissionerInspectors = new ArrayList();
	List<CommissionerBulletin> commissionerBulletins = new ArrayList();


	public static ItemCatalog getCommissionerType() {
		return commissionerType;
	}

	public static void setCommissionerType(ItemCatalog commissionerType) {
		CommissionerBallotHome.commissionerType = commissionerType;
	}

	public List<SanctioningArticle> getSanctioningArticles() {
		return sanctioningArticles;
	}

	public void setSanctioningArticles(List<SanctioningArticle> sanctioningArticles) {
		this.sanctioningArticles = sanctioningArticles;
	}

	public List<CommissionerInspector> getCommissionerInspectors() {
		return commissionerInspectors;
	}

	public void setCommissionerInspectors(
			List<CommissionerInspector> commissionerInspectors) {
		this.commissionerInspectors = commissionerInspectors;
	}

	public List<CommissionerBulletin> getCommissionerBulletins() {
		return commissionerBulletins;
	}

	public void setCommissionerBulletins(
			List<CommissionerBulletin> commissionerBulletins) {
		this.commissionerBulletins = commissionerBulletins;
	}
	
	

	public Long commissionerTypeId(){
		return commissionerType.getId();
	}
	
	public String commissionerTypeCode(){
		return commissionerType.getCode();
	}
	
	

	public void setHygieneCommissioner(){
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		commissionerType = itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONERS_TYPES, "COMMISSIONER_HYGIENE");
		wire();
	}
	
	public void setTransitCommissioner(){
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		commissionerType = itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONERS_TYPES, "COMMISSIONER_TRANSIT");
		wire();
	}
	
	public void setOrnamentCommissioner(){
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		commissionerType = itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONERS_TYPES, "COMMISSIONER_ORNAMENT");
		wire();
	}
	
	public void setAmbientCommissioner(){
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		commissionerType = itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONERS_TYPES, "COMMISSIONER_AMBIENT");
		wire();
	}
	
	public void setHygieneLeaderShep(){
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		commissionerType = itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONERS_TYPES, "LEADERSHIP_HYGIENE");
		wire();
	}
	
	public void findArticlesByCommissioner(){
		sanctioningArticles = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"SanctioningArticle.findByType");
		query.setParameter("commissionerType", commissionerType.getCode());
		sanctioningArticles = query.getResultList();
	}
	
	public void findInspectorsByCommissioner(){
		commissionerInspectors = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerInspector.findByType");
		query.setParameter("commissionerType", commissionerType.getCode());
		commissionerInspectors = query.getResultList();
	}
	
	public void findBulletinsByCommissioner(){
		commissionerBulletins = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBulletin.findByType");
		query.setParameter("commissionerType", commissionerType.getCode());
		commissionerBulletins = query.getResultList();
	}
	
	public void saveSanctioningArticle(){
		joinTransaction();
		this.sanctioningArticle.setCreationDate(new Date());	
		this.sanctioningArticle.setResponsible(userSession.getPerson());
		this.sanctioningArticle.setResponsible_user(userSession.getUser().getResident().getName());
		this.sanctioningArticle.setCommissionerBallotType(commissionerType);
		getEntityManager().merge(this.sanctioningArticle);
		getEntityManager().flush();
		findArticlesByCommissioner();
	}
	
	public void saveCommissionerBulletin(){
		joinTransaction();
		this.commissionerBulletin.setCreationDate(new Date());	
		this.commissionerBulletin.setResponsible(userSession.getPerson());
		this.commissionerBulletin.setResponsible_user(userSession.getUser().getResident().getName());
		this.commissionerBulletin.setCommissionerBallotType(commissionerType);
		if(this.commissionerBulletin.getInspector() != null){
			getEntityManager().merge(this.commissionerBulletin);
			getEntityManager().flush();
		}
		findBulletinsByCommissioner();
	}
	
	public void saveCommissionerInspector(){
		joinTransaction();
		this.commissionerInspector.setCreationDate(new Date());	
		this.commissionerInspector.setResponsible(userSession.getPerson());
		this.commissionerInspector.setResponsible_user(userSession.getUser().getResident().getName());
		this.commissionerInspector.setCommissionerBallotType(commissionerType);
		List<CommissionerInspector> inspectors = new ArrayList<CommissionerInspector>();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerInspector.findByIdentificationNumberAndType");
		query.setParameter("identNum", this.commissionerInspector.getNumberIdentification());
		query.setParameter("commissionerType", commissionerType.getCode());
		inspectors = query.getResultList();
		if(inspectors.size()>0){
			addFacesMessageFromResourceBundle("EL INSPECTOR YA ESTÁ REGISTRADO");
		}else{
			getEntityManager().merge(this.commissionerInspector);
			getEntityManager().flush();
		}
		findInspectorsByCommissioner();
	}
	
	
	
	public Boolean isTransitBallot(){
		if(commissionerType.getCode().equals("COMMISSIONER_TRANSIT")){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private List<ItemCatalog> statusForCommissioners;
	private List<ItemCatalog> statusCompleteForCommissioners;
	private List<ItemCatalog> statusForSentCommissioners;
	
	public List<ItemCatalog> getStatusForCommissioners() {
		return statusForCommissioners;
	}

	public void setStatusForCommissioners(List<ItemCatalog> statusForCommissioners) {
		this.statusForCommissioners = statusForCommissioners;
	}

	public List<ItemCatalog> getStatusCompleteForCommissioners() {
		return statusCompleteForCommissioners;
	}

	public void setStatusCompleteForCommissioners(
			List<ItemCatalog> statusCompleteForCommissioners) {
		this.statusCompleteForCommissioners = statusCompleteForCommissioners;
	}

	public List<ItemCatalog> getStatusForSentCommissioners() {
		return statusForSentCommissioners;
	}

	public void setStatusForSentCommissioners(
			List<ItemCatalog> statusForSentCommissioners) {
		this.statusForSentCommissioners = statusForSentCommissioners;
	}

	public List<ItemCatalog> chargeStatusForSelect(){
		statusForCommissioners = new ArrayList<ItemCatalog>();
		statusCompleteForCommissioners = new ArrayList<ItemCatalog>();
		statusForSentCommissioners = new ArrayList<ItemCatalog>();
			statusForCommissioners = itemCatalogService.findItemsForCatalogCodeOrderById(
					CatalogConstants.CATALOG_COMMISSIONER_BALLOT_STATUS);
			statusCompleteForCommissioners = itemCatalogService.findCompleteItemsForCatalogCodeOrderById(
					CatalogConstants.CATALOG_COMMISSIONER_BALLOT_STATUS);
			statusForSentCommissioners = itemCatalogService.findItemsForSentStatus(
					CatalogConstants.CATALOG_COMMISSIONER_BALLOT_STATUS);
		return statusForCommissioners;
	}
	
	public CommissionerBallotStatus findLastStatusForBallot(){
		List<CommissionerBallotStatus> status = new ArrayList();
		String query = "CommissionerBallotStatus.findLastStatusForBallot";
		Query q = getEntityManager().createNamedQuery(query);
		q.setParameter("commissionerBallotId", this.instance.getId());
		status = q.getResultList();
		if(status.size() > 0){
			return status.get(0);
		}else {
			return null;
		}
	}
	
	public CommissionerBallotStatus findLastStatusForBallotExceptId(Long statusId){
		List<CommissionerBallotStatus> status = new ArrayList();
		String query = "CommissionerBallotStatus.findLastStatusForBallotExceptId";
		Query q = getEntityManager().createNamedQuery(query);
		q.setParameter("commissionerBallotId", this.instance.getId());
		q.setParameter("statusId", statusId);
		status = q.getResultList();
		if(status.size() > 0){
			return status.get(0);
		}else {
			return null;
		}
	}
	
	public void addAppearanceInBallot(CommissionerBallot ballot){
		this.setInstance(ballot);
		if(this.getInstance().getResponsibleAppearance() == null){
			this.getInstance().setResponsibleAppearance_user(userSession.getUser().getResident().getName());	 
	        this.getInstance().setResponsibleAppearance(userSession.getPerson());
		}
	}
	
	public void addNullifiedInBallot(CommissionerBallot ballot){
		this.setInstance(ballot);
		if(!this.getInstance().getIsNullified()){
			this.getInstance().setResponsibleNullified_user(userSession.getUser().getResident().getName());	 
			this.getInstance().setNullifiedDate(new Date());
		}
	}
	
	public void addFileNumberInBallot(CommissionerBallot ballot){
		this.setInstance(ballot);		
	}
	
	public String UpdateCommissionerBallot(){
		super.persist();
		if(this.commissionerTypeCode().equals("COMMISSIONER_HYGIENE")){
			return "/commissioners/HygieneBallotsList.xhtml";
		} else if(this.commissionerTypeCode().equals("COMMISSIONER_TRANSIT")){
			return "/commissioners/TransitBallotsList.xhtml";
		} else if(this.commissionerTypeCode().equals("COMMISSIONER_ORNAMENT")){
			return "/commissioners/OrnamentBallotsList.xhtml";
		} else if(this.commissionerTypeCode().equals("COMMISSIONER_AMBIENT")){
			return "/commissioners/AmbientBallotsList.xhtml";
		}
		return null;
	}
	
	
	private String inspectorIdentification;
	private String inspectorName;
	
	
	public String getInspectorIdentification() {
		return inspectorIdentification;
	}

	public void setInspectorIdentification(String inspectorIdentification) {
		this.inspectorIdentification = inspectorIdentification;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	
	public void findInspectorObject(){
		if(this.inspectorIdentification != null && this.inspectorIdentification != ""){
			List<CommissionerInspector> inspectors = new ArrayList<CommissionerInspector>();
			Query query = getEntityManager().createNamedQuery(
					"CommissionerInspector.findByIdentificationNumberAndType");
			query.setParameter("identNum", this.inspectorIdentification);
			query.setParameter("commissionerType", commissionerType.getCode());
			inspectors = query.getResultList();
			if(inspectors.size()>0){
				if(inspectors.get(0).getIsActive()){
					this.commissionerBulletin.setInspector(inspectors.get(0));
					this.inspectorName = inspectors.get(0).getName();
				} else {
					this.commissionerBulletin.setInspector(null);
					this.inspectorName = null;
					addFacesMessageFromResourceBundle("DEBE ACTIVAR AL INSPECTOR");
				}
			}else{
				this.commissionerBulletin.setInspector(null);
				this.inspectorName = null;
				addFacesMessageFromResourceBundle("DEBE REGISTRAR AL INSPECTOR");
			}
		}else{
			this.commissionerBulletin.setInspector(null);
		}	
	}
	
	public void findInspectorObjectForBallot(){
		if(this.instance.getInspectorIdentification() != null && this.instance.getInspectorIdentification() != ""){
			List<CommissionerInspector> inspectors = new ArrayList<CommissionerInspector>();
			Query query = getEntityManager().createNamedQuery(
					"CommissionerInspector.findByIdentificationNumberAndType");
			query.setParameter("identNum", this.instance.getInspectorIdentification());
			query.setParameter("commissionerType", commissionerType.getCode());
			inspectors = query.getResultList();
			if(inspectors.size()>0){
				if(inspectors.get(0).getIsActive()){
					this.instance.setInspectorName(inspectors.get(0).getName());
					findBulletinsByInspector();
					if(bulletinsByInspector.size() == 0){
						bulletinsByInspector = new ArrayList<CommissionerBulletin>();
						addFacesMessageFromResourceBundle("EL INSPECTOR NO TIENE LIBRETINES ASIGNADOS");
					}
				} else {
					this.instance.setInspectorName("EL INSPECTOR NO ESTA ACTIVO");
					bulletinsByInspector = new ArrayList<CommissionerBulletin>();
					addFacesMessageFromResourceBundle("DEBE ACTIVAR AL INSPECTOR");
				}
			}else{
				bulletinsByInspector = new ArrayList<CommissionerBulletin>();
				this.instance.setInspectorName("EL INSPECTOR NO ESTA REGISTRADO");
				addFacesMessageFromResourceBundle("DEBE REGISTRAR AL INSPECTOR");
			}
		}else{
			bulletinsByInspector = new ArrayList<CommissionerBulletin>();
		}	
	}
	
	List<CommissionerBulletin> bulletinsByInspector = new ArrayList<CommissionerBulletin>();
	
	public List<CommissionerBulletin> getBulletinsByInspector() {
		return bulletinsByInspector;
	}

	public void setBulletinsByInspector(
			List<CommissionerBulletin> bulletinsByInspector) {
		this.bulletinsByInspector = bulletinsByInspector;
	}

	
	public void findBulletinsByInspector(){
		bulletinsByInspector = new ArrayList<CommissionerBulletin>();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBulletin.findByInspector");
		query.setParameter("identNum", this.instance.getInspectorIdentification());
		query.setParameter("commissionerType", commissionerType.getCode());
		bulletinsByInspector = query.getResultList();
	}
	
	public void findBulletinAndInspectorByBullet(){
		List<CommissionerBulletin> bulletins = new ArrayList<CommissionerBulletin>();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBulletin.findBySerial");
		query.setParameter("ballotNumber", this.instance.getBallotNumber());
		query.setParameter("commissionerType", commissionerType.getCode());
		bulletins = query.getResultList();
		if(bulletins.size() > 0){
			this.bulletinsByInspector = bulletins;
			this.instance.setBulletin(bulletins.get(0));
			this.instance.setInspectorIdentification(bulletins.get(0).getInspector().getNumberIdentification());
			this.instance.setInspectorName(bulletins.get(0).getInspector().getName());
		} else {
			addFacesMessageFromResourceBundle("NO EXISTE UN LIBRETIN CON ESA SERIE");
		}
	}
	
	public void bulletinCreatedControls(){
		if(this.commissionerBulletin.getBulletinNumber() != null){
			List<CommissionerBulletin> bulletins = new ArrayList<CommissionerBulletin>();
			Query query = getEntityManager().createNamedQuery(
					"CommissionerBulletin.findByNumber");
			query.setParameter("number", this.commissionerBulletin.getBulletinNumber());
			query.setParameter("commissionerType", commissionerType.getCode());
			bulletins = query.getResultList();
			if(bulletins.size() > 0){
				this.commissionerBulletin.setBulletinNumber(null);
				addFacesMessageFromResourceBundle("YA EXISTE UN LIBRETIN CON ESE NUMERO");
			}
		}
		if(this.commissionerBulletin.getStartNumber() != null){
			List<CommissionerBulletin> bulletins = new ArrayList<CommissionerBulletin>();
			Query query = getEntityManager().createNamedQuery(
					"CommissionerBulletin.findByStartSerial");
			query.setParameter("startNumber", this.commissionerBulletin.getStartNumber());
			query.setParameter("commissionerType", commissionerType.getCode());
			bulletins = query.getResultList();
			if(bulletins.size() > 0){
				this.commissionerBulletin.setStartNumber(null);
				addFacesMessageFromResourceBundle("LA SERIE SE ENCUENTRA DENTRO DEL RANGO DE OTRO LIBRETIN");
			}
		}
		if(this.commissionerBulletin.getEndNumber() != null){
			List<CommissionerBulletin> bulletins = new ArrayList<CommissionerBulletin>();
			Query query = getEntityManager().createNamedQuery(
					"CommissionerBulletin.findByEndSerial");
			query.setParameter("endNumber", this.commissionerBulletin.getEndNumber());
			query.setParameter("commissionerType", commissionerType.getCode());
			bulletins = query.getResultList();
			if(bulletins.size() > 0){
				this.commissionerBulletin.setEndNumber(null);
				addFacesMessageFromResourceBundle("LA SERIE SE ENCUENTRA DENTRO DEL RANGO DE OTRO LIBRETIN");
			}
		}
	}
	
	//para reporte de boletas
	
	private Date fromDateReport;
	private Date untilDateReport;
	private List<CommissionerBallot> ballotsForReport = new ArrayList<CommissionerBallot>();
	private String inspectorIdentificationReport;
	private Long bulletinIdReport;
	private List<CommissionerBulletin> bulletinsForReport = new ArrayList<CommissionerBulletin>();
	private BigDecimal totalValueReport;

	public Date getFromDateReport() {
		return fromDateReport;
	}

	public void setFromDateReport(Date fromDateReport) {
		this.fromDateReport = fromDateReport;
	}

	public Date getUntilDateReport() {
		return untilDateReport;
	}

	public void setUntilDateReport(Date untilDateReport) {
		this.untilDateReport = untilDateReport;
	}
	
	public List<CommissionerBallot> getBallotsForReport() {
		return ballotsForReport;
	}

	public void setBallotsForReport(List<CommissionerBallot> ballotsForReport) {
		this.ballotsForReport = ballotsForReport;
	}

	public String getInspectorIdentificationReport() {
		return inspectorIdentificationReport;
	}

	public void setInspectorIdentificationReport(
			String inspectorIdentificationReport) {
		this.inspectorIdentificationReport = inspectorIdentificationReport;
	}

	public Long getBulletinIdReport() {
		return bulletinIdReport;
	}

	public void setBulletinIdReport(Long bulletinIdReport) {
		this.bulletinIdReport = bulletinIdReport;
	}

	public List<CommissionerBulletin> getBulletinsForReport() {
		return bulletinsForReport;
	}

	public void setBulletinsForReport(List<CommissionerBulletin> bulletinsForReport) {
		this.bulletinsForReport = bulletinsForReport;
	}

	public BigDecimal getTotalValueReport() {
		return totalValueReport;
	}

	public void setTotalValueReport(BigDecimal totalValueReport) {
		this.totalValueReport = totalValueReport;
	}

	public void generalBallotsReport(){
		ballotsForReport = new ArrayList<CommissionerBallot>();
		Calendar cal = Calendar.getInstance();
        cal.setTime(this.untilDateReport);
        cal.add(Calendar.DATE, 1);
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBallot.findGeneralReport");
		query.setParameter("startDate", this.fromDateReport);
		query.setParameter("endDate", cal.getTime());
		query.setParameter("commissionerType", commissionerType.getCode());
		ballotsForReport = query.getResultList();
		calculateTotalValueReport();
	}
	
	public void generalBallotsReportInfractionDate(){
		ballotsForReport = new ArrayList<CommissionerBallot>();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBallot.findGeneralReportInfractionDate");
		query.setParameter("startDate", this.fromDateReport);
		query.setParameter("endDate", this.untilDateReport);
		query.setParameter("commissionerType", commissionerType.getCode());
		ballotsForReport = query.getResultList();
		calculateTotalValueReport();
	}
	
	public void generalBallotsReportNullified(){
		ballotsForReport = new ArrayList<CommissionerBallot>();
		Calendar cal = Calendar.getInstance();
        cal.setTime(this.untilDateReport);
        cal.add(Calendar.DATE, 1);
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBallot.findGeneralReportNullified");
		query.setParameter("startDate", this.fromDateReport);
		query.setParameter("endDate", cal.getTime());
		query.setParameter("commissionerType", commissionerType.getCode());
		ballotsForReport = query.getResultList();
	}
	
	public void findBulletinsByInspectorReport(){
		bulletinsForReport = new ArrayList<CommissionerBulletin>();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBulletin.findByInspector");
		query.setParameter("identNum", inspectorIdentificationReport);
		query.setParameter("commissionerType", commissionerType.getCode());
		bulletinsForReport = query.getResultList();
	}
	
	
	public void ballotsByBulletinReport(){
		ballotsForReport = new ArrayList<CommissionerBallot>();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBallot.findByBulletin");
		query.setParameter("bulletinId", this.bulletinIdReport);
		query.setParameter("commissionerType", commissionerType.getCode());
		ballotsForReport = query.getResultList();
		// calculateTotalValueReport();
	}
	
	public void ballotsForEmissionReport(){
		findInspectorsByCommissioner();
		ballotsForReport = new ArrayList<CommissionerBallot>();
		Query query = getEntityManager().createNamedQuery(
				"CommissionerBallot.findForEmission");
		query.setParameter("commissionerType", commissionerType.getCode());
		ballotsForReport = query.getResultList();
	}
	
	public void printReportByEmission(){
		for(CommissionerBallot cb : ballotsForReport){
			CommissionerBallotStatus sent = createSentStatus(cb);
			this.setInstance(cb);
			this.getInstance().setCurrentStatus(sent);
			this.getInstance().getStatus().add(sent);
			persist();
		}
	}
	
	public CommissionerBallotStatus createSentStatus(CommissionerBallot cb){
		CommissionerBallotStatus sent = new CommissionerBallotStatus();
		sent.setCreationDate(new Date());
		sent.setCommissionerBallot(cb);
		sent.setResponsible_user(userSession.getUser().getResident().getName());	 
		sent.setResponsible(userSession.getPerson());
		sent.setObservations("ENVIADA EN REPORTE PARA EMISION Y ARCHIVADA");
			sent.setStatusName(itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONER_BALLOT_STATUS, "SENT_ISSUE"));
		return sent;
	}
	
	public void calculateTotalValueReport(){
		totalValueReport = BigDecimal.ZERO;
		for(CommissionerBallot ballot : this.ballotsForReport){
			if (ballot.getSanctionValue() !=null ){
				totalValueReport = totalValueReport.add(ballot.getSanctionValue());
			}
		}
	}
	
	public String findResidentAddress(String identNum){
		Query query = getEntityManager().createNamedQuery(
				"commissionerBallot.findAddressByIdent");
		query.setParameter("identNum", identNum);
		return (String) query.getSingleResult();
	}
	
	public String findResidentPhone(String identNum){
		Query query = getEntityManager().createNamedQuery(
				"commissionerBallot.findPhoneByIdent");
		query.setParameter("identNum", identNum);
		return (String) query.getSingleResult();
	}
	
	public String findResidentEmail(String identNum){
		Query query = getEntityManager().createNamedQuery(
				"commissionerBallot.findEmailByIdent");
		query.setParameter("identNum", identNum);
		return (String) query.getSingleResult();
	}
	
	public String returnDateFormat(Date infractionDate){
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy");
		return dt1.format(infractionDate);
	}
	
	public void createdNewBallot(){
		this.setInstance(new CommissionerBallot());
	}
	
	public void saveNullifiedAndStatus(){
		if(!this.getInstance().getIsNullified()){ 
			this.getInstance().setIsNullified(Boolean.TRUE);
		}
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		
		if(this.instance.getId() != null){
			createCommissionerBallotStatus();
			this.commissionerBallotStatus.setObservations("BOLETA ANULADA Y ARCHIVADA");
				this.commissionerBallotStatus.setStatusName(itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_COMMISSIONER_BALLOT_STATUS, "NULLIFIED_ARCHIVED"));
			addStatus();
		}
		
		UpdateCommissionerBallot();
	}
	
	public List<CommissionerBallotStatus> staturOrderByDate(){
		List<CommissionerBallotStatus> status = new LinkedList<CommissionerBallotStatus>();
		status.addAll(this.getInstance().getStatus());
		Collections.sort(status);
		return status;
	}
	
	private Charge revenueCharge;	
	private Delegate revenueDelegate;
	
	
	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}
	
	
	private SystemParameterService systemParameterService;
	
	public Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,systemParameter);
		return charge;
	}

	public void loadCharge() {
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}
		}	
	}
	
	public String printEmissionOrdenByBallot(CommissionerBallot ballot){
		this.ballotsForReport = new ArrayList<CommissionerBallot>();
		this.ballotsForReport.add(ballot);
		printReportByEmission();
		return "/commissioners/report/CommissionerBallotsReportByEmissionPDF.xhtml";
	}
}

