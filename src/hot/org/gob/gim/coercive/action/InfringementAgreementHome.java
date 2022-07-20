package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.DividendInfraction;
import ec.gob.gim.coercive.model.infractions.InfringementAgreement;

@Name("infringementAgreementHome")
public class InfringementAgreementHome extends EntityHome<InfringementAgreement> {

	private static final long serialVersionUID = 62094782221905843L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	@Logger
	Log logger;

	@In
	FacesMessages facesMessages;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private SystemParameterService systemParameterService;

	private boolean isFirstTime = true;
	
	private String criteria;
	private String identificationSearch;
	
	private List<Datainfraction> infractionsList = new ArrayList<Datainfraction>();
	private Datainfraction infractionSelected;

	private List<Integer> percentages = new ArrayList<Integer>();
	
	private InfringementAgreement infringementAgreementRep;
	private DatainfractionService datainfractionService;
	private List<Long> statusPending = new ArrayList<Long>();
	
	public BigDecimal minFee = BigDecimal.ZERO;
	
	public void setInfringementAgreementId(Long id) {
		setId(id);
	}

	public Long getInfringementAgreementId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if (!isFirstTime)
			return;
		if (systemParameterService == null) 
			systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
		identificationSearch = "";
		isFirstTime = false;
		for (int i = 20; i <= 30; i++) {
			percentages.add(i);
		}
	}

	public boolean isWired() {
		return true;
	}

	public InfringementAgreement getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	/**
	 * @return the identificationSearch
	 */
	public String getIdentificationSearch() {
		return identificationSearch;
	}

	/**
	 * @param identificationSearch the identificationSearch to set
	 */
	public void setIdentificationSearch(String identificationSearch) {
		this.identificationSearch = identificationSearch;
	}

	/**
	 * @return the infractionsList
	 */
	public List<Datainfraction> getInfractionsList() {
		return infractionsList;
	}

	/**
	 * @param infractionsList the infractionsList to set
	 */
	public void setInfractionsList(List<Datainfraction> infractionsList) {
		this.infractionsList = infractionsList;
	}

	/**
	 * @return the infractionSelected
	 */
	public Datainfraction getInfractionSelected() {
		return infractionSelected;
	}

	/**
	 * @param infractionSelected the infractionSelected to set
	 */
	public void setInfractionSelected(Datainfraction infractionSelected) {
		this.infractionSelected = infractionSelected;
	}

	/**
	 * @return the percentages
	 */
	public List<Integer> getPercentages() {
		return percentages;
	}

	/**
	 * @param percentages the percentages to set
	 */
	public void setPercentages(List<Integer> percentages) {
		this.percentages = percentages;
	}

	/**
	 * @return the infringementAgreementRep
	 */
	public InfringementAgreement getInfringementAgreementRep() {
		return infringementAgreementRep;
	}

	/**
	 * @param infringementAgreementRep the infringementAgreementRep to set
	 */
	public void setInfringementAgreementRep(
			InfringementAgreement infringementAgreementRep) {
		this.infringementAgreementRep = infringementAgreementRep;
	}

	/**
	 * @return the minFee
	 */
	public BigDecimal getMinFee() {
		return minFee;
	}

	/**
	 * @param minFee the minFee to set
	 */
	public void setMinFee(BigDecimal minFee) {
		this.minFee = minFee;
	}

	/**
	 * @return the isFirstTime
	 */
	public boolean isFirstTime() {
		return isFirstTime;
	}

	/**
	 * @param isFirstTime the isFirstTime to set
	 */
	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	/* (non-Javadoc)
	 * @see org.jboss.seam.framework.EntityHome#persist()
	 */
	@Override
	public String persist() {
		if (!isIdDefined()) {
			if (instance.getInitialFee().compareTo(minFee) == -1){
				facesMessages.add("El valor de la cuota inicial no puede ser menor a: " + minFee.doubleValue());
				return null;
			}
			instance.setCreator(userSession.getUser());
			generateDividends();
		}
		return super.persist();
	}

	public void createInfringementAgreement() {
	}
	
	public void editInfringementAgreement() {
	}
	
	@Override
	@Transactional
	public String update() {
		return super.update();
	}
	
	public void searchByIdentification(){
		statusPending = systemParameterService.findListIds("PAYMENT_INFRACTIONS_STATUS_IDS");
		clearInstance();
		Query query = getEntityManager().createNamedQuery("Datainfraction.findByIdentification");
		query.setParameter("identification", identificationSearch);
		query.setParameter("statusPending", statusPending);
		infractionsList = query.getResultList();
		if ((infractionsList != null) && (infractionsList.size() > 0)){
			Datainfraction inf = infractionsList.get(0);
			instance.setInfractorIdentification(inf.getIdentification());
			instance.setInfractorName(inf.getName());
			instance.getInfractions().addAll(infractionsList);
			for (Datainfraction datainfraction : instance.getInfractions()) {
				datainfraction.setInAgreement(true);
				datainfraction.setInfringementAgreement(instance);
			}
			calculateAgreement();
		} else {
			facesMessages.add("No se encontró infracciones para " + identificationSearch);
			instance = new InfringementAgreement();
		}
	}
	
	public void removeInfraction(Datainfraction datainfraction){
		if (datainfraction != null){
			instance.getInfractions().remove(datainfraction);
			datainfraction.setInAgreement(false);
			datainfraction.setInfringementAgreement(null);
			calculateAgreement();
		}
	}
	
	public void calculateAgreement(){
		MathContext context = new MathContext(2);
		instance.setTotal(BigDecimal.ZERO);
		for (Datainfraction datainfraction : instance.getInfractions()) {
			instance.setTotal(instance.getTotal().add(datainfraction.getTotalValue()));
		}
		BigDecimal percentage = new BigDecimal(instance.getPercentage());
		instance.setInitialFee(instance.getTotal().multiply(percentage).divide(new BigDecimal(100)).round(context));
		minFee = instance.getInitialFee();
	}
	
	public void generateReport(InfringementAgreement infringementAgreement) {
		this.infringementAgreementRep = infringementAgreement;
	}

	public void clearInstance(){
		instance = new InfringementAgreement();
		infractionsList.clear();
	}
	
	public void generateDividends(){
		MathContext context = new MathContext(3);
		
		Calendar calendar = DateUtils.getTruncatedInstance(new Date());
		
		if(instance.getMonths() > 0) {
			DividendInfraction dividend = createDividend(this.getInstance().getTotal(), this.getInstance().getInitialFee(), calendar.getTime());
			BigDecimal share = this.getInstance().getTotal().subtract(instance.getInitialFee()).divide(new BigDecimal(instance.getMonths()), context);
			for(Integer dividendNumber = 1; dividendNumber <= instance.getMonths() ; dividendNumber++){
				calendar.add(Calendar.MONTH, 1);
				BigDecimal balance = dividend.getBalance().subtract(dividend.getAmount());
				if(dividendNumber < instance.getMonths()){
					dividend = createDividend(balance, share, calendar.getTime());
				} else {
					dividend = createDividend(balance, balance, calendar.getTime());
				}
				instance.getDividends().add(dividend);
			}
		}
	}

	private DividendInfraction createDividend(BigDecimal balance, BigDecimal amount, Date date){
		DividendInfraction dividend = new DividendInfraction();
		dividend.setDate(date);
		dividend.setBalance(balance);
		dividend.setAmount(amount);
		return dividend;
	}
	
}