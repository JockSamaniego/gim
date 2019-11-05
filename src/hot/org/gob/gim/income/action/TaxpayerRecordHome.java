package org.gob.gim.income.action;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.DateUtils;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.income.model.ReceiptAuthorization;
import ec.gob.gim.income.model.TaxpayerRecord;

@Name("taxpayerRecordHome")
public class TaxpayerRecordHome extends EntityHome<TaxpayerRecord> {
	
	private static final long serialVersionUID = 18937028370234L;
	
	@Logger
	Log logger;
		
	private Charge charge;
	
	private Delegate delegate;	
	
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
	
	public String save() {
		if(!isTaxpayerInformationValid()){
			return null;
		}
		//System.out.println("IS DEFAULT "+this.getInstance().getIsDefault());
		if(this.getInstance().getIsDefault()){ 
			disableDefaultTaxpayerRecord();
		}
		if(isManaged()){
			super.update();
		} else {
			super.persist();
		}

		//createSequences();
		return "saved";
	}
	
	/*
	@SuppressWarnings("unchecked")
	public void createSequences(){
		
		Query query = getEntityManager().createNamedQuery("ReceiptType.findAll");
		List<ReceiptType> receiptTypes = query.getResultList();

		query = getEntityManager().createNamedQuery("Till.findActive");
		List<Till> tills = query.getResultList();
		
		for(ReceiptAuthorization receiptAuthorization : instance.getReceiptAuthorizations()){
			for(ReceiptType receiptType : receiptTypes){
				for(Till till : tills){

						// TODO Colocarlo como un named native query con parametros?
						/*
						String sequenceName = "AUTH_"+receiptAuthorization.getId()+"_"+receiptType.getId()+"_"+till.getBranch().getNumber()+"_"+till.getNumber();
						System.out.println("LXGK-SRI -----> Sequence CHECKIN sequence "+sequenceName);
						String createSentence = "CREATE SEQUENCE "+sequenceName+" START "+initialReceiptNumber+" MINVALUE "+initialReceiptNumber+" MAXVALUE "+finalReceiptNumber;
						query = entityManager.createNativeQuery(createSentence);
						query.executeUpdate();
						*-/
						IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);						
						incomeService.createSequence(receiptAuthorization, receiptType.getId(), till.getBranch().getNumber(), till.getNumber());
						System.out.println("LXGK-SRI -----> Sequence created ");
				}
			}
		}
	}
	*/

	
	public void calculateDates(ReceiptAuthorization authorization){
		
		Calendar calendar = DateUtils.getTruncatedInstance(authorization.getStartDate());
		calendar.setLenient(true);		
		authorization.setStartDate(calendar.getTime());		
		calendar.add(Calendar.YEAR, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		authorization.setEndDate(calendar.getTime());
	}
	
	
	private Boolean isTaxpayerInformationValid(){
		List<ReceiptAuthorization> authorizations = instance.getReceiptAuthorizations();
		Integer size = authorizations.size();
		
		TaxpayerRecord taxpayer = getInstance();
		
		if(taxpayer.getIsSpecialTaxpayer()){
			if (taxpayer.getSpecialTaxpayerDate() == null || 
				taxpayer.getSpecialTaxpayerResolution() == null || 
				taxpayer.getSpecialTaxpayerResolution().length() == 0){
				addFacesMessage("taxpayerRecord.emptySpecialTaxpayerFields");
				return Boolean.FALSE;
			} 
		}
		
		// Check authorization number size
		try{
			for(ReceiptAuthorization authorization : authorizations){
				if(authorization.getAuthorizationNumber() == null){
					addFacesMessage("receiptAuthorization.numberCantBeNull");
					return Boolean.FALSE;
				}
				
				if(authorization.getAuthorizationNumber().length() == 10){
					Long.parseLong(authorization.getAuthorizationNumber());
				} else {
					addFacesMessage("receiptAuthorization.invalidNumberLength");
					return Boolean.FALSE;
				}
			}
		} catch(Exception e){
			addFacesMessage("receiptAuthorization.numberMustContainOnlyDigits");
			return Boolean.FALSE;
		}
		
		// Check dates and numbers
		for(int i=0; i < size - 1 ; i++){
			 ReceiptAuthorization base = authorizations.get(i);
			 for(int j=i+1; j < size ; j++){
				 ReceiptAuthorization current = authorizations.get(j);
				 if(base.getAuthorizationNumber().equalsIgnoreCase(current.getAuthorizationNumber())){
					 addFacesMessage("receiptAuthorization.authorizationNumberPreviouslyRegistered");
					 return Boolean.FALSE;
				 }
				 
				 //System.out.println("Fecha actual: "+current.getStartDate()+"\n\n");
				 Long baseStartTime = base.getStartDate().getTime();
				 Long baseEndTime = base.getEndDate().getTime();
				 Long currentStartTime = current.getStartDate().getTime();
				 Long currentEndTime = current.getEndDate().getTime();
				 
				 if((currentStartTime >= baseStartTime && currentStartTime <= baseEndTime) ||
					(currentEndTime >= baseStartTime && currentEndTime <= baseEndTime)){
					 addFacesMessage("receiptAuthorization.invalidDates");
					 return Boolean.FALSE;
				 }
			}
		}
		return Boolean.TRUE;
	}
	
	/*
	private void createReceiptAuthorizationSequence(List<ReceiptAuthorization> receiptAuthorizations){
		for(ReceiptAuthorization r : receiptAuthorizations){
			String template = ResourceBundle.instance().getString("config.createSequence");			
			String sql = Interpolator.instance().interpolate(template, r.getId(), r.getStartNumber().toString());			
			getPersistenceContext().createNativeQuery(sql).executeUpdate();
		}
	}
	*/
	
	
	/*
	private void separateReceiptAuthorizationWithId(){
		receiptAuthorizationsSaved  = new ArrayList<ReceiptAuthorization>();
		for(ReceiptAuthorization r : this.getInstance().getReceiptAuthorizations()){
			if(r.getId() != null){
				receiptAuthorizationsSaved.add(r);
			}
		}
	}
	*/
	
	
	public void logoListener(UploadEvent event) {
		UploadItem item = event.getUploadItem();		
		if (item != null && item.getData() != null) {
			logger.info(item.getFileName() + ", size: " + item.getFileSize());			
			getInstance().setLogo(item.getData());			
		}
	}

	public void clearLogo() {
		getInstance().setLogo(null);
	}
	
	public void clearFlag() {
		getInstance().setFlag(null);
	}

	public void flagListener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		logger.info("===> Sketch listener executed...  Data: " + item.getData());
		if (item != null && item.getData() != null) {
			logger.info(item.getFileName() + ", size: " + item.getFileSize());
			getInstance().setFlag(item.getData());			
		}
	}
	
	private void disableDefaultTaxpayerRecord(){
		Query query = getPersistenceContext().createNamedQuery("TaxpayerRecord.disableDefaultTaxpayerRecord");
		query.setParameter("id", instance.getId());
		query.executeUpdate();
		/*
		List<TaxpayerRecord> list = query.getResultList();
		if(list != null && list.size() > 0)	{
			TaxpayerRecord t = list.get(0);
			if(t.getId() != this.getInstance().getId()){
				// ERROR NO SE PUEDE PONER ASI LA INSTANCIA
				t.setIsDefault(false);
				this.setInstance(t);
				this.update();
			}
		}
		*/
	}
	
	/*
	public List<ReceiptAuthorization> separateNewReceiptAuthorizations(){
		List<ReceiptAuthorization> receiptAuthorizations = new ArrayList<ReceiptAuthorization>();
		for(ReceiptAuthorization r: this.getInstance().getReceiptAuthorizations()){
			if(!receiptAuthorizationsSaved.contains(r)){
				receiptAuthorizations.add(r);
			}
		}
		return receiptAuthorizations;
	}
	*/
		
	/*
	public Long countTaxpayerRecord(){
		Query query = getPersistenceContext().createNamedQuery("TaxpayerRecord.countTaxPayerRecord");
		Long n = Long.parseLong(query.getSingleResult().toString());		
		return n;
	}
	*/


	public TaxpayerRecord getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void addReceiptAuthorization() {
		Calendar now = Calendar.getInstance();
		ReceiptAuthorization ra = new ReceiptAuthorization();
		ra.setStartDate(now.getTime());
		this.getInstance().add(ra);		
	}
	
	/*
	private ReceiptAuthorization buildReceiptAuthorization(Date currentDate){
		return ra;
	}
	*/

	public void removeReceiptAuthorization(ReceiptAuthorization r) {
		this.getInstance().remove(r);
		/*
		List<ReceiptAuthorization> receiptAuthorizationList = this.getInstance().getReceiptAuthorizations();
		if (!receiptAuthorizationList.isEmpty() || receiptAuthorizationList.size() > 0){		
			ReceiptAuthorization receiptAuthorizationLast = receiptAuthorizationList.get(receiptAuthorizationList.size()-1);
			receiptAuthorizationLast.setEndDate(null);
		}
		Calendar now = Calendar.getInstance();		
		//updateEndDateRemoveReceiptAuthorizationLast(now, this.getInstance().getReceiptAuthorizations());
		 * *
		 */
	}
	
	/*
	public void reloadEndDateLast(Date current) {
		Calendar now = Calendar.getInstance();
		now.setTime(current);
		List<ReceiptAuthorization> receiptAuthorizations = getInstance()
				.getReceiptAuthorizations();						
		updateEndDateReceiptAuthorizationLast(now, receiptAuthorizations);
	}
	*/

	/*
	private void updateEndDateReceiptAuthorizationLast(Calendar now,
			List<ReceiptAuthorization> receiptAuthorizations) {
		if (!receiptAuthorizations.isEmpty()
				&& receiptAuthorizations.size() > 1) {
			now.add(Calendar.DATE, -1);
			ReceiptAuthorization receiptAuthorization = receiptAuthorizations
					.get(receiptAuthorizations.size() - 2);
			receiptAuthorization.setEndDate(now.getTime());
		}
	}
	*/
	
	/*
	private void updateEndDateRemoveReceiptAuthorizationLast(Calendar now,
			List<ReceiptAuthorization> receiptAuthorizations) {
		if (!receiptAuthorizations.isEmpty()
				&& receiptAuthorizations.size() > 1) {
			for(int i = receiptAuthorizations.size()-1; i > 0 ; i--){
				now.setTime(receiptAuthorizations.get(i).getStartDate());
				now.add(Calendar.DATE, -1);
				receiptAuthorizations.get(i-1).setEndDate(now.getTime());
			}
		}
	}
	*/

	public void addCharge() {		
		getInstance().add(new Charge());
	}
	
	public void editDelegate(Charge c){	
		delegate = getActiveDelegate(c);		
		charge = c;
	}
	
	public void saveAddDelegate(){
		desactiveDelegates(charge);
		delegate.setIsActive(true);
		charge.add(delegate);
	}
	
	public void changeCharge(Charge c){
		charge = c;
	}
	
	public void desactiveDelegates(Charge c){
		for(Delegate d: charge.getDelegates()){
			d.setIsActive(false);
		}
		
	}
	
	public Delegate getActiveDelegate(Charge c){
		for(Delegate d: c.getDelegates()){
			if(d.getIsActive())return d;
		}
		return null;
	}
	
	public void addDelegate(Charge c){
		delegate = new Delegate();		
		charge = c;
	}

	public Charge getCharge() {
		return charge;
	}

	public void setCharge(Charge charge) {
		this.charge = charge;
	}

	public Delegate getDelegate() {
		return delegate;
	}

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public void setTaxpayerRecordId(Long id) {
		setId(id);
	}

	public Long getTaxpayerRecordId() {
		return (Long) getId();
	}
	
	/*
	@Override
	protected TaxpayerRecord createInstance() {
		TaxpayerRecord taxpayerRecord = new TaxpayerRecord();
		return taxpayerRecord;
	}
	*/	
	
}
