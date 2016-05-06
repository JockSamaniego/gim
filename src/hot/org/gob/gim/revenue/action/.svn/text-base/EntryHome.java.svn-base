package org.gob.gim.revenue.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.income.action.IssuerHome;
import org.gob.gim.revenue.facade.RevenueService;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import ec.gob.gim.income.model.Account;
import ec.gob.gim.income.model.Issuer;
import ec.gob.gim.income.model.Tax;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryDefinition;
import ec.gob.gim.revenue.model.EntryDefinitionType;
import ec.gob.gim.revenue.model.EntryStructure;
import ec.gob.gim.revenue.model.EntryStructureType;
import ec.gob.gim.revenue.model.EntryType;
import ec.gob.gim.revenue.model.PreissuerPermission;

@Name("entryHome")
public class EntryHome extends EntityHome<Entry> {

	private static final long serialVersionUID = 1L;
	
	protected String criteriaEntryNormal;
	protected String criteriaEntryDiscount;
	protected String criteriaEntryInterest;
	protected String criteriaEntrySurcharge;	
	protected String criteriaTax;
	private String entryCode;	
	private String criteriaEntry;
	private String accountCode;
	private Entry entry;
	private boolean isFirstTime = true;
	private Account currentAccount;
	protected Entry entryChildNormal;
	protected Entry entryChildDiscount;
	protected Entry entryChildInterest;
	protected Entry entryChildSurcharge;	
	protected Tax tax;
	protected EntryDefinition entryDefinition;
	protected EntryStructure entryStructuredSelected;
	protected Tax taxSelected;	
	protected Boolean isLoaded;
	protected Boolean editCode = false;
	
	private RevenueService revenueService;

	private List<Entry> entries;
	protected List<EntryStructure> normals; 
	protected List<EntryStructure> discounts;
	protected List<EntryStructure> surcharges;	
	protected List<Entry> entriesOnApply;	
	private List<Issuer> issuers = new ArrayList<Issuer>();	
	private List<Issuer> deletedIssuers = new ArrayList<Issuer>();
	
	@In(create = true)
	IssuerHome issuerHome;	
	
	@Logger
	Log logger;
	
	@In
	Gim gim;
	
	@In
	FacesMessages facesMessages;

	public EntryHome() {		
		normals = new ArrayList<EntryStructure>();
		discounts = new ArrayList<EntryStructure>();
		surcharges = new ArrayList<EntryStructure>();		
		entriesOnApply = new ArrayList<Entry>();		
		isLoaded = Boolean.FALSE;
	}
	
	public void cleanRule(){
		this.getEntryDefinition().setRule(null);
		this.getEntryDefinition().setValue(null);
		this.ruleValidated = Boolean.FALSE;
	}
	
	public void beforeAutcompleteEntryStructureChildNormal() {			
		if (this.entryChildNormal != null){
			this.logger.info("::::))) beforeAutcompleteEntryStructureChildNormal(): #0, #1", entryChildNormal.getName(), entryChildNormal.getId());
			this.criteriaEntryNormal = entryChildNormal.getName();
		}else{
			this.criteriaEntryNormal = null;
		}						
	}	
	
	public void addEntryStructureChildNormal(){
		this.logger.info("::::))) Add this.entryChildNormal: #0, #1", entryChildNormal.getName(), entryChildNormal.getId());		
		if (this.entryChildNormal != null){
			EntryStructure es = new EntryStructure();
			es.setEntryStructureType(EntryStructureType.NORMAL);
			//es.setOrder(this.instance.getChildren().size() + 1);
			es.setOrder(getNormals().size() + 1);
			es.setChild(entryChildNormal);
			this.getInstance().addChild(es);
			//
			if(!this.getNormals().contains(es)) this.getNormals().add(es);
			setEntryChildNormal(null);
			this.criteriaEntryNormal = null;
			//
		}
	}
	
	public Long countActivePreissuerPermissions(){
		Long aux = 0L;
		for(PreissuerPermission p : getInstance().getPreissuerPermissions()){
			if(p.getIsActive()){
				aux++;
			}
		}
		return aux;
	}
	
	public void updateEditableFields(){
		if(getInstance().getIsAmountEditable()){
			getInstance().setIsValueEditable(false);
		}
		
		if(getInstance().getIsValueEditable()){
			getInstance().setIsAmountEditable(false);
		}
	}
	
	public void changeReceiptType(){
		if(!getInstance().getIsTaxable()) getInstance().setReceiptType(null);
	}

	public void removeEntryStructureChildNormal(){
		this.logger.info("::::))) Remove this.EntryStructureChildNormal: #0", entryStructuredSelected.getChild().getName());
		if (this.getEntryStructuredSelected() != null){
			this.getInstance().removeChild(entryStructuredSelected);
			//
			this.getNormals().remove(entryStructuredSelected);
			entryStructuredSelected = null;
			//
		}
	}
	
	public void beforeAutcompleteEntryStructureChildDiscount() {			
		this.logger.info("::::))) beforeAutcompleteEntryStructureChildDiscount(): #0, #1", entryChildDiscount.getName(), entryChildDiscount.getId());
		if (this.entryChildDiscount != null){
			this.criteriaEntryDiscount = entryChildDiscount.getName();
		}else{
			this.criteriaEntryDiscount = null;
		}
						
	}
	
	public void beforeAutcomplete(Account a) {		
		this.getInstance().setAccount(a);		
		if(a == null){
			accountCode = null;
		}else{
			//a.setEntry(this.getInstance());
			accountCode = a.getAccountCode() + " - " + a.getAccountName();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Account> findAccounts(String criteria){		
		Query query = this.getEntityManager().createNamedQuery("Account.findChildsAccountByCriteria");
		query.setParameter("criteria", criteria);
		return query.getResultList();		
	}
	
	public List<Account> findAvailableAccounts(Object suggestion){		
		String accountCode = suggestion.toString();
		List<Account> list = findAccounts(accountCode);		
		if(this.isManaged() && this.getInstance().getAccount() != null && this.getInstance().getAccount().getAccountCode().startsWith(accountCode)
				&& getCurrentAccount() != null && !list.contains(getCurrentAccount())){
			list.add(this.getCurrentAccount());
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> findSubLineAccounts(String criteria){
		Query query = this.getEntityManager().createNamedQuery("Account.findChildsSubLineAccountByCriteria");
		query.setParameter("criteria", criteria);
		return query.getResultList();		
	}
	
	public List<Account> findAvailableSubLineAccounts(Object suggestion){		
		String accountCode = suggestion.toString();
		List<Account> list = findSubLineAccounts(accountCode);
		if(this.isManaged() && this.getInstance().getAccount() != null && this.getInstance().getAccount().getAccountCode().startsWith(accountCode)
				&& getCurrentAccount() != null && !list.contains(getCurrentAccount())){
			list.add(this.getCurrentAccount());
		}
		return list;
	}
	
	public void addIssuer(){		
		Calendar now = Calendar.getInstance();
		updateActivesIssuers(issuers);
		issuers.add(buildIssuer(now.getTime()));
		reloadEndDateLast(issuers.get(issuers.size()-1));		
	}

	
	public void reloadEndDateLast(Issuer i) {
		Calendar now = Calendar.getInstance();
		now.setTime(i.getStartDate());					
		updateEndDateIssuerLast(now, issuers, i);
	}
	
	
	@Override
	public String update() {		
		String aux = super.update();
		if(aux.equals("updated")){
			//if(issuers.size() == 0) createDefaultIssuer();
			for(Issuer i :issuers){				
				if(i.getId() == null){
					i.setEntry(this.getInstance());					
					issuerHome.setInstance(i);
					issuerHome.persist();					
				}else{					
					issuerHome.setInstance(i);
					issuerHome.update();
				}
			}			
			for(Issuer i :deletedIssuers){
				if(i.getId() != null){					
					issuerHome.setInstance(i);
					issuerHome.remove();
				}
			}
		}		
		return aux;
	}

	
	@Override
	public String persist() {
		String aux = super.persist();
		if(aux.equals("persisted")){
			//if(issuers.size() == 0) createDefaultIssuer();
			for(Issuer i :issuers){
				i.setEntry(this.getInstance());	
				issuerHome.setInstance(i);
				issuerHome.persist();	
			}
		}		
		return aux;
	}
	
	/*
	private void createDefaultIssuer(){
		TaxpayerRecord taxpayerRecord = taxpayerRecordHome.findDefaultTaxpayerRecord();
		if(taxpayerRecord != null){
			Calendar now = Calendar.getInstance();
			Issuer issuer = buildIssuer(now.getTime());
			issuer.setTaxpayerRecord(taxpayerRecord);
			issuer.setEntry(this.getInstance());
			issuerHome.setInstance(issuer);
			issuerHome.persist();
		}
	}
	*/
			
	private void updateEndDateIssuerLast(Calendar now,
			List<Issuer> issuers, Issuer i) {		
		if (!issuers.isEmpty()
				&& issuers.size() > 1) {
			now.add(Calendar.DATE, -1);
			int position = issuers.indexOf(i);
			if(position <= 0) return;
			Issuer issuer = issuers
					.get(position - 1);
			issuer.setEndDate(now.getTime());
			issuer.setIsActive(false);
		}
	}
	
	private void updateEndDateRemoveIssuer(Calendar now,
			List<Issuer> issuers) {		
		if (!issuers.isEmpty()
				&& issuers.size() > 1) {
			for(int i = issuers.size()-1; i > 0 ; i--){
				now.setTime(issuers.get(i).getStartDate());
				now.add(Calendar.DATE, -1);
				issuers.get(i-1).setEndDate(now.getTime());
			}
		}
	}
	
	public void updateActivesIssuers(List<Issuer> issuers) {
		for (Issuer i : issuers) {
			i.setIsActive(false);
		}
	}

	
	private Issuer buildIssuer(Date currentDate){
		Issuer i = new Issuer();
		i.setStartDate(currentDate);
		i.setIsActive(true);		
		return i;
	}
	
	public void removeIssuer(Issuer i){
		issuers.remove(i);
		if(i.getId() != null) deletedIssuers.add(i);
		updateRemoveIssuer(issuers);
		updateEndDateRemoveIssuer(Calendar.getInstance(), issuers);
	}
	
	public void addPreissuerPermission(){
		PreissuerPermission p = new PreissuerPermission();
		p.setIsActive(true);
		getInstance().add(p);		
	}
	
	public void removePreissuer(PreissuerPermission p){
		if(p.getId() == null){
			getInstance().remove(p);
		}else{
			p.setIsActive(false);
		}
	}
	
	private void updateRemoveIssuer(List<Issuer> issuers){
		if (!issuers.isEmpty() || issuers.size() > 0){		
			Issuer issuer = issuers.get(issuers.size()-1);
			issuer.setIsActive(true);
			issuer.setEndDate(null);
		}
	}
	
	public void addEntryStructureChildDiscount(){
		this.logger.info("::::))) Add this.entryChildDiscount: #0", entryChildDiscount.getName());
		if (this.entryChildDiscount != null){
			EntryStructure es = new EntryStructure();
			es.setEntryStructureType(EntryStructureType.DISCOUNT);
			//es.setOrder(this.instance.getChildren().size() + 1);
			es.setOrder(getDiscounts().size() + 1);
			es.setChild(entryChildDiscount);
			this.getInstance().addChild(es);
			//
			if(!this.getDiscounts().contains(es))this.getDiscounts().add(es);
			setEntryChildDiscount(null);
			this.criteriaEntryDiscount=null;
			//
		}
	}
	
	public void removeEntryStructureChildDiscount(){
		this.logger.info("::::))) Remove this.EntryStructureChildDiscount: #0", entryStructuredSelected.getChild().getName());
		if (this.getEntryStructuredSelected() != null){
			this.getInstance().removeChild(entryStructuredSelected);
			//
			this.getDiscounts().remove(entryStructuredSelected);
			entryStructuredSelected = null;
			//
		}
	}
	
	public void beforeAutcompleteEntryStructureChildSurcharge() {			
		this.logger.info("::::))) beforeAutcompleteEntryStructureChildSurcharge(): #0, #1", entryChildSurcharge.getName(), entryChildSurcharge.getId());
		if (this.entryChildSurcharge != null){
			this.criteriaEntrySurcharge = entryChildSurcharge.getName();
		}else{
			this.criteriaEntrySurcharge = null;
		}					
	}
	
	public void addEntryStructureChildSurcharge(){
		this.logger.info("::::))) Add this.entryChildSurcharge: #0", entryChildSurcharge.getName());
		//if (this.entryChildSurcharge.getId() != null){
		if (this.entryChildSurcharge != null){
			EntryStructure es = new EntryStructure();
			es.setEntryStructureType(EntryStructureType.SURCHARGE);
			//es.setOrder(this.instance.getChildren().size() + 1);
			es.setOrder(getSurcharges().size() + 1);
			es.setChild(entryChildSurcharge);
			this.getInstance().addChild(es);
			//
			if(!this.getSurcharges().contains(es))this.getSurcharges().add(es);
			setEntryChildSurcharge(null);
			criteriaEntrySurcharge = null;
			//
		}
	}
	
	public void removeEntryStructureChildSurcharge(){
		this.logger.info("::::))) Remove this.EntryStructureChildSurcharge: #0", entryStructuredSelected.getChild().getName());
		if (this.getEntryStructuredSelected() != null){
			this.getInstance().removeChild(entryStructuredSelected);
			//
			this.getSurcharges().remove(entryStructuredSelected);
			entryStructuredSelected = null;
			//
		}
	}
	
	public void addTax(){
		this.logger.info("::::))) Add this.Tax: #0", tax.getName());
		if (this.tax != null){
			this.getInstance().add(tax);
			//
			setTax(null);
			criteriaTax = null;
			//
		}
	}
	
	public void removeTax(){
		this.logger.info("::::))) Remove this.taxSelected: #0", taxSelected.getName());
		if (this.getTaxSelected() != null){
			this.getInstance().remove(taxSelected);
			//
			entryStructuredSelected = null;
			//
		}
	}
	
	public void beforeAutcompleteTax() {			
		if (this.tax != null){
			this.logger.info("::::))) beforeAutcompleteTax(): #0, #1", tax.getName(), tax.getId());
			this.criteriaTax= tax.getName();
		}else{
			this.criteriaTax= null;
		}
						
	}
	
	public void saveEntryDefinition(){		
		if(this.entryDefinition.getEntryDefinitionType().equals(EntryDefinitionType.RULE)
				&& this.entryDefinition.getRule() == null){
			facesMessages.addToControl("", org.jboss.seam.international.StatusMessage.Severity.ERROR, "#{messages['entry.undefinedRule']}");			
			return;
		}
		
		if(this.entryDefinition.getEntryDefinitionType().equals(EntryDefinitionType.VALUE)
				&& (this.entryDefinition.getValue() == null || this.entryDefinition.getFactor() == null)){
			facesMessages.addToControl("", org.jboss.seam.international.StatusMessage.Severity.ERROR, "#{messages['common.emptyRequiredField']}");			
			return;
		}
		
		if(this.entryDefinition.getEntryDefinitionType().equals(EntryDefinitionType.RULE)){
			this.entryDefinition.setValue(null);		
		}else{
			this.entryDefinition.setRule(null);
		}
		
		if (this.entryDefinition.getId() == null){
			if (this.entryDefinition.getIsCurrent()){				
				getInstance().updateAllIsCurrentToEntryDefinitions(Boolean.FALSE);				
			}			
			getInstance().add(entryDefinition);		
		}		
	}
	
	public void remove(EntryDefinition entryDefinition){	
		this.getInstance().remove(entryDefinition);
	}

	private String lastRule; 
	
	public void exitEditDefinitionPanel(){
		this.entryDefinition.setRule(lastRule);
	}

	public void ruleListener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		logger.info("Rule listener executed...  Data: #0", item.getData().length);
	    if(item != null && item.getData() != null) {
	    	logger.info("File uploading " + item.getFileName()+", "+item.getFileSize());    
	    	lastRule = getEntryDefinition().getRule();
	    	getEntryDefinition().setRule(new String(item.getData()));	    	
	    	StatusMessages.instance().add(Severity.INFO, "Archivo upload:::: ");
	    	ruleValidated = Boolean.FALSE;
	    }	    
	}
	
	private Boolean ruleValidated = Boolean.FALSE;
	
	public void ruleValidator(){
		logger.info("Loading ruleValidator(): #0", getEntryDefinition().getRule());
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		InputStream is = new ByteArrayInputStream(getEntryDefinition().getRule().getBytes());
		builder.add(ResourceFactory.newInputStreamResource(is), ResourceType.DRL);
		
		if (builder.hasErrors()){
			Exception e = new Exception(builder.getErrors().toString());
					
			String message = "Error en archivo de reglas de tipo:\n "+ e.getMessage();
			getEntryDefinition().setRule(null);
					
			StatusMessages.instance().add(Severity.ERROR, message);
			StatusMessages.instance().add(Severity.ERROR, "Archivo no se ha guardado");
			//this.addFacesMessage(message, this);
			
			logger.error(message);
			logger.error("Archivo no se ha guardado");
        }else{
        	StatusMessages.instance().add(Severity.INFO, "Archivo compilado con exito");
        	logger.info("Archivo compilado con exito");
        	ruleValidated = Boolean.TRUE;
        }
		
	}
	
	public void setEntryId(Long id) {
		setId(id);
	}

	public Long getEntryId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
	
	/*private void initializeEntryStructureDivisions(){
		// Solo para la primera vez		
		RevenueService revenueService = ServiceLocator.getInstance().findResource(REVENUE_SERVICE_NAME);
		//getInstance().setChildren(revenueService.findEntryStructureChildren(this.getInstance().getId()));
		
		
		//if (this.normals.isEmpty())
			this.normals = revenueService.findEntryStructureChildrenByType(this.getInstance().getId(), EntryStructureType.NORMAL);
		//if (this.discounts.isEmpty()) 
			this.discounts = revenueService.findEntryStructureChildrenByType(this.getInstance().getId(), EntryStructureType.DISCOUNT);
		//if (this.interests.isEmpty())
			this.interests = revenueService.findEntryStructureChildrenByType(this.getInstance().getId(), EntryStructureType.INTEREST);
		//if (this.surcharges.isEmpty())
			this.surcharges = revenueService.findEntryStructureChildrenByType(this.getInstance().getId(), EntryStructureType.SURCHARGE);
		
		isLoaded = Boolean.TRUE;
	}*/
	
	private void initializeEntryStructureDivisions(){
		// Solo para la primera vez
		
		for (EntryStructure es : getInstance().getChildren()){
			if (es.getEntryStructureType().equals(EntryStructureType.NORMAL)){
				this.normals.add(es);
			}else if (es.getEntryStructureType().equals(EntryStructureType.DISCOUNT)){
				this.discounts.add(es);
			}else{
				this.surcharges.add(es);
			}
		}		
		isLoaded = Boolean.TRUE;
	}
	
	private void loadEntriesOnApply(){
		// Solo para la primera vez
		if (!entriesOnApply.contains(instance))
			entriesOnApply.add(instance);
		for (EntryStructure es : this.instance.getChildren()){
			if (!entriesOnApply.contains(es.getChild()))
				entriesOnApply.add(es.getChild());
		}
	}
	
	public void wire() {
		logger.info("...... Ingreso al wire");		
		//getInstance();
		
		if (!isLoaded){
			getInstance();		
			initializeEntryStructureDivisions();
			if(getInstance().getId() != null) issuers = findIssuersByEntry();
		}		
		loadEntriesOnApply();
		if(!this.isManaged()){
			this.getInstance().setCode(nextEntryCode());
		}else{			
			if(this.getInstance().getAccount() != null && isFirstTime) {
				setAccountCode(this.getInstance().getAccount().getAccountCode() + " - " +  this.getInstance().getAccount().getAccountName());				
				setCurrentAccount(this.getInstance().getAccount());				
				isFirstTime = false;
			}
		}		
	}
	
	public void searchEntry() {		
		if (entryCode != null) {
			if(revenueService == null) revenueService = (RevenueService)ServiceLocator.getInstance().findResource(RevenueService.LOCAL_NAME);
			Entry entry = revenueService.findEntryByCode(entryCode);
			if (entry != null){
				this.entry = entry;
				this.setEntry(entry);
				setEntryCode(entry.getCode());
				
			}
		}
	}
	
	public void searchEntryByCriteria(){
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()){
			if(revenueService == null) revenueService = (RevenueService)ServiceLocator.getInstance().findResource(RevenueService.LOCAL_NAME); 
			entries = revenueService.findEntryByCriteria(criteriaEntry);			
		}
	}	
	
	public void clearSearchEntryPanel(){
		this.setCriteriaEntry(null);
		entries = null;
	}
	
	public void entrySelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		this.setEntryCode(entry.getCode());
	}
	
	@SuppressWarnings("unchecked")
	public String nextEntryCode(){
		List<String> list = getPersistenceContext().createNamedQuery("Entry.findLastCode").getResultList();
		if(list != null && list.size() > 0 && list.get(0) != null){
			try{
				Long res = Long.parseLong(list.get(0).toString());
				res ++;
				return getNumberFormat().format(res);
			} catch(Exception e){
				editCode = true;
				
			}
		}
		return "";
	}
	
	private java.text.NumberFormat getNumberFormat() {
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("00000");
		numberFormat.setMaximumIntegerDigits(5);
		return numberFormat;
	}
	

	@SuppressWarnings("unchecked")
	public List<Issuer> findIssuersByEntry(){
		Query query = getPersistenceContext().createNamedQuery("Issuer.findByEntry").setParameter("id", this.getInstance().getId());
		return query.getResultList();
	}
	
	public boolean isWired() {
		return true;
	}

	public Entry getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EntryStructure> getChildren() {
		return getInstance() == null ? null : new ArrayList<EntryStructure>(getInstance().getChildren());
	}
	
	public List<EntryDefinition> getEntryDefinitions() {
		return getInstance() == null ? null : new ArrayList<EntryDefinition>(
				getInstance().getEntryDefinitions());
	}
	
	@Factory("entryTypes")
	public List<EntryType> loadEntryTypes(){
		return Arrays.asList(EntryType.values());
	}
	
	@Factory("entryDefinitionTypes")
	public List<EntryDefinitionType> loadEntryDefinitionTypes(){
		return Arrays.asList(EntryDefinitionType.values());
	}
	
	@SuppressWarnings("unchecked")
	public List<Entry> findEntries(Object suggest){
		String pref = (String)suggest;
		Query query = this.getEntityManager().createNamedQuery("Entry.findByCriteria");
		query.setParameter("criteria", pref);
		return query.getResultList();	    
	}
	
	@SuppressWarnings("unchecked")
	public List<Tax> findTaxes(Object suggest){
		String pref = (String)suggest;
		Query query = this.getEntityManager().createNamedQuery("Tax.findByCriteria");
		query.setParameter("criteria", pref);
		return query.getResultList();	    
	}
	
	/**
	 * @return the entryChildNormal
	 */
	public Entry getEntryChildNormal() {
		return entryChildNormal;
	}

	/**
	 * @param entryChild the entryChild to set
	 */
	public void setEntryChildNormal(Entry entryChildNormal) {
		this.entryChildNormal = entryChildNormal;
	}

	/**
	 * @return the entryChildDiscount
	 */
	public Entry getEntryChildDiscount() {
		return entryChildDiscount;
	}

	/**
	 * @param entryChildDiscount the entryChildDiscount to set
	 */
	public void setEntryChildDiscount(Entry entryChildDiscount) {
		this.entryChildDiscount = entryChildDiscount;
	}

	/**
	 * @return the entryChildInterest
	 */
	public Entry getEntryChildInterest() {
		return entryChildInterest;
	}

	/**
	 * @param entryChildInterest the entryChildInterest to set
	 */
	public void setEntryChildInterest(Entry entryChildInterest) {
		this.entryChildInterest = entryChildInterest;
	}

	/**
	 * @return the entryChildSurcharge
	 */
	public Entry getEntryChildSurcharge() {
		return entryChildSurcharge;
	}

	/**
	 * @param entryChildSurcharge the entryChildSurcharge to set
	 */
	public void setEntryChildSurcharge(Entry entryChildSurcharge) {
		this.entryChildSurcharge = entryChildSurcharge;
	}

	/**
	 * @return the normals
	 */
	public List<EntryStructure> getNormals() {
		return normals;
	}

	/**
	 * @param normals the normals to set
	 */
	public void setNormals(List<EntryStructure> normals) {
		this.normals = normals;
	}

	/**
	 * @return the discounts
	 */
	public List<EntryStructure> getDiscounts() {
		return discounts;
	}

	/**
	 * @param discounts the discounts to set
	 */
	public void setDiscounts(List<EntryStructure> discounts) {
		this.discounts = discounts;
	}
	
	/**
	 * @return the surcharges
	 */
	public List<EntryStructure> getSurcharges() {
		return surcharges;
	}

	/**
	 * @param surcharges the surcharges to set
	 */
	public void setSurcharges(List<EntryStructure> surcharges) {
		this.surcharges = surcharges;
	}

	/**
	 * @return the entryStructuredSelected
	 */
	public EntryStructure getEntryStructuredSelected() {
		return entryStructuredSelected;
	}

	/**
	 * @param entryStructuredSelected the entryStructuredSelected to set
	 */
	public void setEntryStructuredSelected(EntryStructure entryStructuredSelected) {
		this.entryStructuredSelected = entryStructuredSelected;
	}

	/**
	 * @return the entryDefinition
	 */
	public EntryDefinition getEntryDefinition() {
		if (entryDefinition == null) entryDefinition = new EntryDefinition();
		return entryDefinition;
	}
	
	public void createDefinition(){
		this.entryDefinition = new EntryDefinition();
	}

	/**
	 * @param entryDefinition the entryDefinition to set
	 */
	public void setEntryDefinition(EntryDefinition entryDefinition) {
		this.entryDefinition = entryDefinition;
	}

	/**
	 * @return the criteriaTax
	 */
	public String getCriteriaTax() {
		return criteriaTax;
	}

	/**
	 * @param criteriaTax the criteriaTax to set
	 */
	public void setCriteriaTax(String criteriaTax) {
		this.criteriaTax = criteriaTax;
	}

	/**
	 * @return the tax
	 */
	public Tax getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(Tax tax) {
		this.tax = tax;
	}

	/**
	 * @return the taxSelected
	 */
	public Tax getTaxSelected() {
		return taxSelected;
	}

	/**
	 * @param taxSelected the taxSelected to set
	 */
	public void setTaxSelected(Tax taxSelected) {
		this.taxSelected = taxSelected;
	}

	public List<Tax> getTaxes() {
		return getInstance() == null ? null : new ArrayList<Tax>(
				getInstance().getTaxes());
	}

	public void setIssuers(List<Issuer> issuers) {
		this.issuers = issuers;
	}

	public List<Issuer> getIssuers() {
		return issuers;
	}
	
	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	
	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}
	
	public List<Issuer> getDeletedIssuers() {
		return deletedIssuers;
	}

	public void setDeletedIssuers(List<Issuer> deletedIssuers) {
		this.deletedIssuers = deletedIssuers;
	}
	
	public List<Entry> getEntriesOnApply(){	
		return entriesOnApply;
	}
	

	/**
	 * @return the criteriaEntryNormal
	 */
	public String getCriteriaEntryNormal() {
		return criteriaEntryNormal;
	}

	/**
	 * @param criteriaEntryNormal the criteriaEntryNormal to set
	 */
	public void setCriteriaEntryNormal(String criteriaEntryNormal) {
		this.criteriaEntryNormal = criteriaEntryNormal;
	}

	/**
	 * @return the criteriaEntryDiscount
	 */
	public String getCriteriaEntryDiscount() {
		return criteriaEntryDiscount;
	}

	/**
	 * @param criteriaEntryDiscount the criteriaEntryDiscount to set
	 */
	public void setCriteriaEntryDiscount(String criteriaEntryDiscount) {
		this.criteriaEntryDiscount = criteriaEntryDiscount;
	}

	/**
	 * @return the criteriaEntryInterest
	 */
	public String getCriteriaEntryInterest() {
		return criteriaEntryInterest;
	}

	/**
	 * @param criteriaEntryInterest the criteriaEntryInterest to set
	 */
	public void setCriteriaEntryInterest(String criteriaEntryInterest) {
		this.criteriaEntryInterest = criteriaEntryInterest;
	}

	/**
	 * @return the criteriaEntrySurcharge
	 */
	public String getCriteriaEntrySurcharge() {
		return criteriaEntrySurcharge;
	}

	/**
	 * @param criteriaEntrySurcharge the criteriaEntrySurcharge to set
	 */
	public void setCriteriaEntrySurcharge(String criteriaEntrySurcharge) {
		this.criteriaEntrySurcharge = criteriaEntrySurcharge;
	}

	public Boolean getRuleValidated() {
		return ruleValidated;
	}

	public void setRuleValidated(Boolean ruleValidated) {
		this.ruleValidated = ruleValidated;
	}

	public Boolean getEditCode() {
		return editCode;
	}

	public void setEditCode(Boolean editCode) {
		this.editCode = editCode;
	}
	
}