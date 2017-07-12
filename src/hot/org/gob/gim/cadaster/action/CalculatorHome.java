package org.gob.gim.cadaster.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.joda.time.DateTime;
import org.joda.time.Period;

import ec.gob.gim.cadaster.model.CalculatorPlusV;
import ec.gob.gim.cadaster.model.CapitalGain;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.PassiveRate; 

@Name("calculatorHome")
public class CalculatorHome extends EntityHome<CapitalGain> {
	
	private static final long serialVersionUID = 1L;
	public BigDecimal SBU = new BigDecimal(375).multiply(new BigDecimal(24));
	public BigDecimal paidTotal = BigDecimal.ZERO;
	
	private BigDecimal acquisitionValue = BigDecimal.ZERO;
	private Integer yearsNumber;
	private Integer monthsNumber;
	private BigDecimal adjustmentPropertyValue= BigDecimal.ZERO;
	private BigDecimal exoneratedSBU; 	
	private String cadastralCode = "";
	private BigDecimal passiveRate =  BigDecimal.ZERO;
	private BigDecimal acquisitionAdjustmentValue = BigDecimal.ZERO;
	private BigDecimal adjustementFactor = BigDecimal.ZERO;
	private BigDecimal ordinaryProfit = BigDecimal.ZERO;
	private BigDecimal extraOrdinaryProfit = BigDecimal.ZERO;
	private BigDecimal taxable = BigDecimal.ZERO;
 
	@Logger
	Log logger;
	
	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;
	
	private CalculatorPlusV calculator;	
	private Property property;

	public void setPlusvaliaId(Long id) {
		setId(id);
	}

	public Long getPlusvaliaId() {
		return (Long) getId();
	}
	
	/**
	 * Persiste o actualiza el Block
	 * 
	 */
	public String save(){
		//this.getInstance().setSector(sector);
		if(isManaged()){
			return super.update();
		}else{
			return super.persist();
		}
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
		
		
	}

	public void wire() {
		
		System.out.println("FISCAL PERIOD: "+userSession.getFiscalPeriod());
		System.out.println("SBU: "+userSession.getFiscalPeriod().getBasicSalary());
		
		this.exoneratedSBU = userSession.getFiscalPeriod().getBasicSalary().
				multiply(new BigDecimal(24));
		getInstance();		
		if (this.instance.getId() == null) {
			calculator = new CalculatorPlusV();
			this.getInstance().setCreationDate(new Date());
			this.getInstance().setCreationTime(new Date());
		}else{
			//edicion de datos
			
		}
	}
	
	
	
	public void calculate(){
		
	}

	
	public void getAcquisitionCost(){
		if(this.getInstance().getProperty()!=null){
			this.getInstance().setAcquisitionValue( this.getInstance().getProperty()
					.getCurrentDomain().getCommercialAppraisal().
					add(this.getInstance().getBuildingImprovements()).
					add(this.getInstance().getAcquisitionEnhancements()).
					add(this.getInstance().getCem())
			);
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Property> searchActionByName(Object suggest) {
		String q = "SELECT p FROM Property p "
				+ "WHERE p.previousCadastralCode LIKE CONCAT(:suggest,'%') or "
				+ "      p.cadastralCode LIKE CONCAT(:suggest,'%')";				
		Query e = getEntityManager().createQuery(q);
		e.setParameter("suggest", (String) suggest);
		return (List<Property>) e.getResultList();
	}

	
	public void calculateData(){
		if(this.getInstance().getSaleDate()!=null && 
				this.getInstance().getPurchaseDate()!=null){
			
			//encontrar el promedio
			getPassiveRateBCE();
			
			//obtener el factor de ajuste
			Double one 		= 1d;
			BigDecimal i    = this.getInstance().getPassiveRate().getRate();
			
			double n = getMonths()/12;
			this.getInstance().setAdjustementFactor(new BigDecimal(Math.pow( (one + (i.doubleValue()/100)), n)));
			
			//valor de adquisicion ajustado
			this.getInstance().setAdjustedAcquisitionValue(this.getInstance().getAdjustementFactor().
					multiply(this.getInstance().getAcquisitionValue()));
			
			//GANANCIA ORDINARIA
			this.getInstance().setOrdinaryProfit( this.getInstance().getAdjustedAcquisitionValue().
					subtract(this.getInstance().getAcquisitionValue())); 
			System.out.println("ganancia ordinaria "+this.getInstance().getOrdinaryProfit());			
		}
	}
	
	
	@SuppressWarnings("unchecked")	
	private void getPassiveRateBCE(){
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(currentDate.getTime());
		EntityManager em = getEntityManager();
		Query q = em.createNamedQuery("PassiveRate.findAVG");
		q.setParameter("isActive",  Boolean.TRUE);
		
		List<PassiveRate> list = q.getResultList();		
		if(list.size()>0){
			this.getInstance().setPassiveRate(list.get(0));
		}		
	}
	
	
	private Double getMonths(){	
		DateTime dateStart = new DateTime(this.getInstance().getPurchaseDate());
		DateTime dateEnd = new DateTime(this.getInstance().getSaleDate());		
		Period $period = new Period(dateStart, dateEnd);		 
		this.yearsNumber = $period.getYears();
		this.monthsNumber = $period.getMonths(); 
		int aux =0;		
		if(this.yearsNumber >0){
			aux = this.yearsNumber *12;
		}			
		return (double)aux +this.monthsNumber;				
	}
	
	
	
	public void getextraordinaryProfit(){
		
		if(this.getInstance().getSalesValue() ==null || this.getInstance().getAdjustedAcquisitionValue() ==null){
			return;
		}
		
		if(this.getInstance().getSalesValue().compareTo(BigDecimal.ZERO)==1 && 
		   this.getInstance().getAdjustedAcquisitionValue().compareTo(BigDecimal.ZERO)==1){
			
			this.getInstance().setExtraOrdinaryProfit(this.getInstance().getSalesValue()
					.subtract(this.getInstance().getAdjustedAcquisitionValue()));
			
			//calcular la base imponible
			BigDecimal taxable = this.getInstance().getExtraOrdinaryProfit().subtract(exoneratedSBU);
			if(taxable.compareTo(BigDecimal.ZERO)<=0){
				taxable = BigDecimal.ZERO;
			}
			
			this.getInstance().setTaxable(taxable);			
			if(this.getInstance().getTaxable().compareTo(exoneratedSBU)>0){
				
				this.getInstance().setPaidTotal(this.getInstance().
						getTaxable().multiply(new BigDecimal(0.75)));
			}else{
				this.getInstance().setPaidTotal(BigDecimal.ZERO);
			}
			 
		}
	}
	
	public static int diferenciaEnDias(Date fechaMayor, Date fechaMenor) {
		long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();
		long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
		return (int) dias;
	}

	public boolean isWired() {
		return true;
	}

	public CalculatorPlusV getCalculator() {
		return calculator;
	}

	public void setCalculator(CalculatorPlusV calculator) {
		this.calculator = calculator;
	}

	public BigDecimal getSBU() {
		return SBU;
	}

	public void setSBU(BigDecimal sBU) {
		SBU = sBU;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {	
		this.instance.setProperty(property);
		getAcquisitionCost();
	}

	public BigDecimal getAcquisitionValue() {
		return acquisitionValue;
	}

	public void setAcquisitionValue(BigDecimal acquisitionValue) {
		this.acquisitionValue = acquisitionValue;
	}

	public Integer getYearsNumber() {
		return yearsNumber;
	}

	public void setYearsNumber(Integer yearsNumber) {
		this.yearsNumber = yearsNumber;
	}

	public Integer getMonthsNumber() {
		return monthsNumber;
	}

	public void setMonthsNumber(Integer monthsNumber) {
		this.monthsNumber = monthsNumber;
	}
 
	public BigDecimal getAdjustmentPropertyValue() {
		return adjustmentPropertyValue;
	}

	public void setAdjustmentPropertyValue(BigDecimal adjustmentPropertyValue) {
		this.adjustmentPropertyValue = adjustmentPropertyValue;
	}

	public BigDecimal getExoneratedSBU() {
		return exoneratedSBU;
	}

	public void setExoneratedSBU(BigDecimal exoneratedSBU) {
		this.exoneratedSBU = exoneratedSBU;
	}
	public BigDecimal getPassiveRate() {
		return passiveRate;
	}

	public void setPassiveRate(BigDecimal passiveRate) {
		this.passiveRate = passiveRate;
	}

	public BigDecimal getAcquisitionAdjustmentValue() {
		return acquisitionAdjustmentValue;
	}

	public void setAcquisitionAdjustmentValue(BigDecimal acquisitionAdjustmentValue) {
		this.acquisitionAdjustmentValue = acquisitionAdjustmentValue;
	}

	public BigDecimal getAdjustementFactor() {
		return adjustementFactor;
	}

	public void setAdjustementFactor(BigDecimal adjustementFactor) {
		this.adjustementFactor = adjustementFactor;
	}

	public BigDecimal getOrdinaryProfit() {
		return ordinaryProfit;
	}

	public void setOrdinaryProfit(BigDecimal ordinaryProfit) {
		this.ordinaryProfit = ordinaryProfit;
	}

	public BigDecimal getExtraOrdinaryProfit() {
		return extraOrdinaryProfit;
	}

	public void setExtraOrdinaryProfit(BigDecimal extraOrdinaryProfit) {
		this.extraOrdinaryProfit = extraOrdinaryProfit;
	}

	public BigDecimal getTaxable() {
		return taxable;
	}

	public void setTaxable(BigDecimal taxable) {
		this.taxable = taxable;
	}	
}