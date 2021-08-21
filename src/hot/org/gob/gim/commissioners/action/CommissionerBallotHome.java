package org.gob.gim.commissioners.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
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
import ec.gob.gim.common.model.ItemCatalog;

@Name("commissionerBallotHome")
public class CommissionerBallotHome extends EntityHome<CommissionerBallot> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
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
				this.commissionerBallotStatus.setObservations("Inicio de Proceso");
				this.commissionerBallotStatus.setStatusName(itemCatalogService.findItemByCodeAndCodeCatalog(CatalogConstants.CATALOG_HYGIENE_BALLOT_STATUS, "INITIATED"));
				addStatus();
			}
		}
//		hygieneBallotStatus = new ArrayList<ItemCatalog>();
//		hygieneBallotStatus = itemCatalogService.findItemsForCatalogCode(
//						CatalogConstants.CATALOG_HYGIENE_BALLOT_STATUS);
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
	 
	public String saveHygieneBallot(){
		//validaciones
				message=null;

				BigInteger ballotNumber = this.instance.getBallotNumber();
				

				boolean exist  = serialExist(ballotNumber);
				if(exist){
					message="El número de boleta ya existe";
					return "/commissioners/HygieneBallotsEdit.xhtml";
				}

				//guardado en BD
				this.getInstance().setCreationDate(new Date());				 
		        this.getInstance().setResponsible_user(userSession.getUser().getResident().getName());	 
		        this.getInstance().setResponsible(userSession.getPerson());
		        this.getInstance().setCommissionerBallotType(commissionerType);

		
		persist();
		return "/commissioners/HygieneBallotsList.xhtml";
	}
	
	public String saveTransitBallot(){
		//validaciones
				message=null;

				BigInteger ballotNumber = this.instance.getBallotNumber();
				

				boolean exist  = serialExist(ballotNumber);
				if(exist){
					message="El número de boleta ya existe";
					return "/commissioners/TransitBallotsEdit.xhtml";
				}

				//guardado en BD
				this.getInstance().setCreationDate(new Date());				 
		        this.getInstance().setResponsible_user(userSession.getUser().getResident().getName());	 
		        this.getInstance().setResponsible(userSession.getPerson());
		        this.getInstance().setCommissionerBallotType(commissionerType);

		
		persist();
		return "/commissioners/TransitBallotsList.xhtml";
	}
	
	public Boolean serialExist(BigInteger ballotNumber){
		List<CommissionerBallot> commissionerBallots = new ArrayList();
		Query query = getEntityManager().createNamedQuery(
				"commissionerBallot.findByNumberAndType");
		query.setParameter("ballotNumber", ballotNumber);
		commissionerBallots = query.getResultList();
		
		if(commissionerBallots.size()>0){
			return true;
		}
		return false;
		
	}
	
	public void findInfractorName(){
		if(this.instance.getInfractorIdentification() != null && this.instance.getInfractorIdentification() != ""){
			List<String> names = new ArrayList<String>();
			Query query = getEntityManager().createNamedQuery(
					"commissionerBallot.findResidentNameByIdent");
			query.setParameter("identNum", this.instance.getInfractorIdentification());
			names = query.getResultList();
			if(names.size()>0){
				this.instance.setInfractorName(names.get(0));
			}else{
				this.instance.setInfractorName("No registrado");	
			}
		}else{
			this.instance.setInfractorName(null);
		}	
	}
	
	public void findInspectorName(){
		if(this.instance.getInspectorIdentification() != null && this.instance.getInspectorIdentification() != ""){
			List<String> names = new ArrayList<String>();
			Query query = getEntityManager().createNamedQuery(
					"commissionerBallot.findResidentNameByIdent");
			query.setParameter("identNum", this.instance.getInspectorIdentification());
			names = query.getResultList();
			if(names.size()>0){
				this.instance.setInspectorName(names.get(0));
			}else{
				this.instance.setInspectorName("No registrado");	
			}
		}else{
			this.instance.setInspectorName(null);
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
	
	//para reporte de boletas
	private List<Infractions> infracts = new ArrayList<Infractions>();
	
	public List<Infractions> getInfracts() {
		return infracts;
	}

	public void setInfracts(List<Infractions> infracts) {
		this.infracts = infracts;
	}

	public void setInfractionsInNewArray(List<Infractions> infractions){
		this.infracts = new ArrayList<Infractions>();
		this.infracts = infractions;
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
	
	public Boolean isTransitBallot(){
		if(commissionerType.getCode().equals("COMMISSIONER_TRANSIT")){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private List<ItemCatalog> statusForCommissioners;
	
	public List<ItemCatalog> getStatusForCommissioners() {
		return statusForCommissioners;
	}

	public void setStatusForCommissioners(List<ItemCatalog> statusForCommissioners) {
		this.statusForCommissioners = statusForCommissioners;
	}

	public List<ItemCatalog> chargeStatusForSelect(){
		statusForCommissioners = new ArrayList<ItemCatalog>();
		if(commissionerType.getCode().equals("COMMISSIONER_HYGIENE")){
			statusForCommissioners = itemCatalogService.findItemsForCatalogCodeOrderById(
					CatalogConstants.CATALOG_HYGIENE_BALLOT_STATUS);
		}else if (commissionerType.getCode().equals("COMMISSIONER_TRANSIT")){
			statusForCommissioners = itemCatalogService.findItemsForCatalogCodeOrderById(
					CatalogConstants.CATALOG_TRANSIT_BALLOT_STATUS);
		}
		
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
	
	public String UpdateCommissionerBallot(){
		update();
		return "/commissioners/HygieneBallotsList.xhtml";
	}
	
}
