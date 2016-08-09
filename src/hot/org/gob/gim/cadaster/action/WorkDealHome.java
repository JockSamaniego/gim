package org.gob.gim.cadaster.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.WorkDeal;
import ec.gob.gim.cadaster.model.WorkDealFraction;
import ec.gob.gim.cadaster.model.WorkDealFractionComparator;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;


@Name("workDealHome")
public class WorkDealHome extends EntityHome<WorkDeal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log logger;

	private SystemParameterService systemParameterService;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private String criteriaProperty;
	private Charge appraisalCharge;
	private Delegate appraisalDelegate;

	private Boolean isFirstTime = Boolean.TRUE;

	private WorkDealFraction workDealFraction;

	private List<Property> properties;

	public void setWorkDealId(Long id) {
		setId(id);
	}

	public Long getWorkDealId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if (isFirstTime)
			calculate();
		isFirstTime = Boolean.FALSE;
	}

	public void createWorkDealFraction() {
		this.workDealFraction = new WorkDealFraction();
	}

	public void editWorkDealFraction(WorkDealFraction workDealFraction) {
		this.workDealFraction = workDealFraction;
	}

	/**
	 * Agrega un building a la propiedad
	 */
	public void addWorkDealFraction() {
		this.getInstance().add(this.workDealFraction);
	}

	public void removeWorkDealFraction(WorkDealFraction workDealFraction) {
		if (workDealFraction != null)
			this.getInstance().remove(workDealFraction);
	}

	public void searchProperty() {
		if (this.criteriaProperty == null)
			return;
		Query query = getEntityManager().createNamedQuery("Property.findByCadastralCode");
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}

	public void searchPropertyByCriteria() {
		if (this.criteriaProperty == null)
			return;
		Query query = getEntityManager().createNamedQuery("Property.findByCadastralCode");
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}

	public void clearSearchPropertyPanel() {
		this.setCriteriaProperty(null);
		properties = null;
	}

	public void calculateContributionFront() {
		this.workDealFraction.setContributionFront(
				this.workDealFraction.getFrontLength().multiply(this.workDealFraction.getProperty().getLotAliquot())
						.divide(new BigDecimal(100), RoundingMode.HALF_UP));
	}

	private BigDecimal totalContributionFront;
	private BigDecimal totalAppraisal;
	private BigDecimal totalSharedValue;
	private BigDecimal totalDifferentiatedValue;
	private BigDecimal total;

	/**
	 * @author macartuche
	 * @date 2015-07-28
	 */
	private BigDecimal totalWater;
	private BigDecimal totalSewerage;
	
	//@author macartuche
	//@tag cambioCalculoCEM
	List<WorkDealFraction> listWDF = new ArrayList<WorkDealFraction>();		

	public void calculate() {
		if (this.getInstance().getCollectFactor().compareTo(BigDecimal.ZERO) == -1
				|| this.getInstance().getCollectFactor().compareTo(BigDecimal.ONE) == 1) {
			addFacesMessageFromResourceBundle("workDeal.collectFactorInvalid");
			return;
		}
		totalContributionFront = BigDecimal.ZERO;
		totalAppraisal = BigDecimal.ZERO;
		totalSharedValue = BigDecimal.ZERO;
		totalDifferentiatedValue = BigDecimal.ZERO;
		// agregado mack
		totalWater = BigDecimal.ZERO;
		totalSewerage = BigDecimal.ZERO;
		////
		total = BigDecimal.ZERO;
		calculateValues();
		
		//@author macartuche
		//@tag cambioCalculoCEM
		listWDF = this.getInstance().getWorkDealFractions();
		for (WorkDealFraction wdf : listWDF) {
			wdf.setCommercialAppraisal(getTotalAppraisalBYWorkeDealFractionSQL(wdf.getId()));
		}
		//fin @tag cambioCalculoCEM
		
		for (WorkDealFraction wf : listWDF) {
			calculateSharedValue(wf);
			calculateDifferentitatedValue(wf);

			wf.setTotal(wf.getSharedValue().add(wf.getDifferentiatedValue()));

			totalSharedValue = totalSharedValue.add(wf.getSharedValue());
			totalDifferentiatedValue = totalDifferentiatedValue.add(wf.getDifferentiatedValue());
			total = total.add(wf.getTotal());
		}
		this.getInstance().setTotalContributionFront(totalContributionFront);
		this.getInstance().setTotalAppraisal(totalAppraisal);
		adjustTotal();


		//
		//total de comercialappraisal de todos los workdealfraction de un workdeal
		BigDecimal totalComercialAppraisal_WDF = getTotalAppraisalBYWorkeDealSQL();
				
		for (WorkDealFraction wf : listWDF) {
			BigDecimal aux = BigDecimal.ZERO;
			if (this.getInstance().getDrinkingWater() != null && this.getInstance().getDrinkingWater()) {
				//aux = calculateValuesWS(wf, this.getInstance().getWaterValue());
				//@tag cambioCalculoCEM
				aux = calculateValuesWS(wf, this.getInstance().getWaterValue(), totalComercialAppraisal_WDF);
				aux = aux.setScale(2, RoundingMode.HALF_UP);
				wf.setWaterValue(aux);
			}

			if (this.getInstance().getSewerage() != null && this.getInstance().getSewerage()) {
				//@tag cambioCalculoCEM
				//aux = calculateValuesWS(wf, this.getInstance().getSewerageValue());
				aux = calculateValuesWS(wf, this.getInstance().getSewerageValue(), totalComercialAppraisal_WDF);
				aux = aux.setScale(2, RoundingMode.HALF_UP);
				System.out.println("Sin redondeo==>" + aux);
				wf.setSewerageValue(aux);
			}

			// agregado mack
			if (wf.getSewerageValue() != null) {
				totalSewerage = totalSewerage.add(wf.getSewerageValue());
			}
			if (wf.getWaterValue() != null) {
				totalWater = totalWater.add(wf.getWaterValue());
			}

		}

		// water
		if (this.getInstance().getDrinkingWater()!=null && 
				this.getInstance().getDrinkingWater()) {
			compareCases(totalWater, this.getInstance().getWaterValue(), "water");
		}

		// sewerage-alcantarillado
		if (this.getInstance().getSewerage()!=null && 
				this.getInstance().getSewerage()) {
			compareCases(totalSewerage, this.getInstance().getSewerageValue(), "sewerage");
		}
		
	}

	/**
	 * 
	 * @param totalSum
	 * @param value
	 */
	private void compareCases(BigDecimal totalSum, BigDecimal valueCompare, String waterOrSewerage) {

		// System.out.println("sumatoria==>" + totalSum + "- valor: " + value);
		int roundSeg = totalSum.compareTo(valueCompare);
		// System.out.println("Compare==>" + roundSeg);
		int i = 0;
		int limit = this.instance.getWorkDealFractions().size();
		while (roundSeg != 0) {
			switch (roundSeg) {
			case -1:
				roundSeg = addOrSubstractToRow(i, 1, waterOrSewerage, valueCompare);
				break;
			case 1:
				roundSeg = addOrSubstractToRow(i, -1, waterOrSewerage, valueCompare);
				break;
			}
			i++;
			if (i == limit) {
				i = 0;
			}
		}
	}

	/**
	 * 
	 * @param position
	 * @return
	 */
	private int addOrSubstractToRow(int position, int typeOperation, String waterOrSewerage, BigDecimal valueCompare) {

		BigDecimal aux = getValueDetail(waterOrSewerage, position);
		aux = makeOperation(aux, typeOperation);
		setValueColumn(aux, waterOrSewerage, position);

		System.out.println("POS=>" + position + " - " + aux);

		// sumar y obtener el total de agua o alcantarillado
		BigDecimal sumValue = BigDecimal.ZERO;
		BigDecimal value = BigDecimal.ZERO;
		//@author macartuche
		//@tag cambioCalculoCEM
		//for (int i = 0; i < this.instance.getWorkDealFractions().size(); i++) {
		for (int i = 0; i < listWDF.size(); i++) {
			value = getValueDetail(waterOrSewerage, i);
			sumValue = sumValue.add(value);
		}

		updateTotals(sumValue, waterOrSewerage);
		return sumValue.compareTo(valueCompare);
	}

	/**
	 * 
	 * @param sumValue
	 * @param waterOrSewerage
	 */
	private void updateTotals(BigDecimal sumValue, String waterOrSewerage) {
		if (waterOrSewerage.equals("water")) {
			totalWater = sumValue;
		} else {
			totalSewerage = sumValue;
		}
	}

	/**
	 * 
	 * @param waterOrSewerage
	 * @param position
	 * @return
	 */
	private BigDecimal getValueDetail(String waterOrSewerage, int position) {
		//WorkDealFraction aux = this.instance.getWorkDealFractions().get(position);
		//@author macartuche
		//@tag cambioCalculoCEM
		WorkDealFraction aux = listWDF.get(position);
		return (waterOrSewerage.equals("water")) ? aux.getWaterValue() : aux.getSewerageValue();
	}

	/**
	 * 
	 * @param aux
	 * @param typeOperation
	 * @return
	 */
	private BigDecimal makeOperation(BigDecimal aux, int typeOperation) {
		BigDecimal cents = new BigDecimal("0.01");
		return (typeOperation == 1) ? aux.add(cents) : aux.subtract(cents);
	}

	/**
	 * 
	 * @param aux
	 * @param waterOrSewerage
	 * @param position
	 */
	private void setValueColumn(BigDecimal aux, String waterOrSewerage, int position) {
		if (waterOrSewerage.equals("water")) {
			this.instance.getWorkDealFractions().get(position).setWaterValue(aux);
		} else {
			this.instance.getWorkDealFractions().get(position).setSewerageValue(aux);
		}
	}

	private void adjustTotal() {
		BigDecimal workDealValue = this.getInstance().getWorkDealValue().multiply(this.getInstance().getCollectFactor())
				.setScale(2, RoundingMode.HALF_UP);
		if (total.compareTo(workDealValue) != 0) {
			BigDecimal difference = workDealValue.subtract(total);
			//this.getInstance().getWorkDealFractions().get(this.getInstance().getWorkDealFractions().size() - 1)
			//		.setSharedValue(this.getInstance().getWorkDealFractions()
			//				.get(this.getInstance().getWorkDealFractions().size() - 1).getSharedValue()
			//				.add(difference));
			//this.getInstance().getWorkDealFractions().get(this.getInstance().getWorkDealFractions().size() - 1)
			//		.setTotal(this.getInstance().getWorkDealFractions()
			//				.get(this.getInstance().getWorkDealFractions().size() - 1).getTotal().add(difference));
			//@tag cambioCalculoCEM
			listWDF.get(listWDF.size() - 1)
					.setSharedValue(listWDF
							.get(listWDF.size() - 1).getSharedValue()
							.add(difference));
			listWDF.get(listWDF.size() - 1)
					.setTotal(listWDF
							.get(listWDF.size() - 1).getTotal().add(difference));			
			totalSharedValue = BigDecimal.ZERO;
			total = BigDecimal.ZERO;
			
			
			/*
			for (WorkDealFraction wf : listWDF) {
				totalSharedValue = totalSharedValue.add(wf.getSharedValue());
				total = total.add(wf.getTotal());
			}
			*/
			totalSharedValue = getSharedValueByWorkDeal();
			total = getTotalWorkDealFraction();
		}
	}

	private BigDecimal getTotalAppraisalBYWorkeDealSQL(){
		Query qTotalAprraisal = getEntityManager().createNativeQuery("select sum(d.commercialappraisal) "
				+ "from workdealfraction wdf "
				+ "join property p on  wdf.property_id = p.id "
				+ "join domain d on p.id=d.property_id "
				+ "where d.currentproperty_id is not null  "
				+ "and wdf.workdeal_id="+getInstance().getId());
		return (BigDecimal)qTotalAprraisal.getSingleResult();
	}
	

	private BigDecimal getTotalAppraisalBYWorkeDealFractionSQL(Long wdfId){
		Query qTotalAprraisal = getEntityManager().createNativeQuery("select sum(d.commercialappraisal) "
				+ "from workdealfraction wdf "
				+ "join property p on  wdf.property_id = p.id "
				+ "join domain d on p.id=d.property_id "
				+ "where d.currentproperty_id is not null  "
				+ "and wdf.id="+wdfId);
		return (BigDecimal)qTotalAprraisal.getSingleResult();
	}
	
	private BigDecimal getTotalContributionSQL(){
		Query qTotalContributionFront = getEntityManager().createNativeQuery("select sum(contributionfront) "
				+ "from workdealfraction where workdeal_id="+getInstance().getId());
		return (BigDecimal)qTotalContributionFront.getSingleResult();	
	}
	
	
	public void calculateValues() {
		totalContributionFront = totalContributionFront.add(getTotalContributionSQL());
		totalAppraisal = totalAppraisal.add(getTotalAppraisalBYWorkeDealSQL());
		/*
		for (WorkDealFraction wf : this.getInstance().getWorkDealFractions()) {
			totalContributionFront = totalContributionFront.add(wf.getContributionFront());
			totalAppraisal = totalAppraisal.add(wf.getProperty().getCurrentDomain().getCommercialAppraisal());
		}*/		
		
		if (totalContributionFront.equals(BigDecimal.ZERO))
			totalContributionFront = BigDecimal.ONE;
		if (totalAppraisal.equals(BigDecimal.ZERO))
			totalAppraisal = BigDecimal.ONE;
		this.getInstance().setTotalSharedValue(
				this.getInstance().getWorkDealValue().multiply(new BigDecimal(0.4)).setScale(2, RoundingMode.HALF_UP));
		this.getInstance().setTotalDifferentiatedValue(
				this.getInstance().getWorkDealValue().multiply(new BigDecimal(0.6)).setScale(2, RoundingMode.HALF_UP));
		if (totalContributionFront.compareTo(BigDecimal.ZERO) == 1)
			this.getInstance().setFrontFactor(this.getInstance().getWorkDealValue().multiply(new BigDecimal(0.4))
					.divide(totalContributionFront, 12, RoundingMode.HALF_UP));
		if (totalAppraisal.compareTo(BigDecimal.ZERO) == 1)
			this.getInstance().setAppraisalFactor(this.getInstance().getWorkDealValue().multiply(new BigDecimal(0.6))
					.divide(totalAppraisal, 12, RoundingMode.HALF_UP));
	}

	public void calculateTotals() {
		totalContributionFront = BigDecimal.ZERO;
		totalAppraisal = BigDecimal.ZERO;
		totalSharedValue = BigDecimal.ZERO;
		totalDifferentiatedValue = BigDecimal.ZERO;
		totalWater = BigDecimal.ZERO;
		totalSewerage = BigDecimal.ZERO;
		total = BigDecimal.ZERO;
		
		
		//@author
		//@tag cambioCalculoCEM
		totalContributionFront = totalContributionFront.add(getTotalContributionSQL());
		totalSharedValue = totalSharedValue.add(getSharedValueByWorkDeal());
		totalDifferentiatedValue = totalDifferentiatedValue.add(getDifferentiatedValue());
		totalAppraisal = totalAppraisal.add(getTotalAppraisalBYWorkeDealSQL());
		total = total.add(getTotalWorkDealFraction());
		
		totalSewerage = totalSewerage.add(getTotalSewerageWDF());
		totalWater = totalWater.add(getTotalWaterWDF());
		
		//fin cambioCalculoCEM
		//for (WorkDealFraction wf : this.getInstance().getWorkDealFractions()) {
		//	totalContributionFront = totalContributionFront.add(wf.getContributionFront());
		//	totalSharedValue = totalSharedValue.add(wf.getSharedValue());
		//	totalDifferentiatedValue = totalDifferentiatedValue.add(wf.getDifferentiatedValue());
		//	totalAppraisal = totalAppraisal.add(wf.getProperty().getCurrentDomain().getCommercialAppraisal());
		//	total = total.add(wf.getTotal());
		// macartuche
		//  if (wf.getSewerageValue() != null) {
		//		wf.setSewerageValue(wf.getSewerageValue().setScale(2, RoundingMode.HALF_UP));
		//		totalSewerage = totalSewerage.add(wf.getSewerageValue());
		//  }

		//	if (wf.getWaterValue() != null) {
		//		wf.setWaterValue(wf.getWaterValue().setScale(2, RoundingMode.HALF_UP));
		//		totalWater = totalWater.add(wf.getWaterValue());
		//	}
		//}

		totalSewerage = totalSewerage.setScale(2, RoundingMode.HALF_UP);
		totalWater = totalWater.setScale(2, RoundingMode.HALF_UP);

		// ordenar por nro catastro
		/*
		 * for(WorkDealFraction wf: this.getInstance().getWorkDealFractions()){
		 * wf.getProperty().getCurrentDomain(). }
		 */
	}
	
	private BigDecimal getSharedValueByWorkDeal(){
		Query qTotalSharedValue = getEntityManager().createNativeQuery("select sum(sharedvalue) "
				+ "from workdealfraction where workdeal_id="+getInstance().getId());
		return (BigDecimal)qTotalSharedValue.getSingleResult();	
	}
	
	private BigDecimal getDifferentiatedValue(){
		Query qDifferentiatedValue = getEntityManager().createNativeQuery("select sum(differentiatedvalue) "
				+ "from workdealfraction where workdeal_id="+getInstance().getId());
		return (BigDecimal)qDifferentiatedValue.getSingleResult();	
	}
	
	private BigDecimal getTotalWorkDealFraction(){
		Query qtotal = getEntityManager().createNativeQuery("select sum(total) "
				+ "from workdealfraction where workdeal_id="+getInstance().getId());
		return (BigDecimal)qtotal.getSingleResult();	
	}
	
	
	private BigDecimal getTotalSewerageWDF(){
//		BigDecimal totalSewerage = BigDecimal.ZERO;
		Query qTotalSewerage = getEntityManager().createNativeQuery("select round(sum(seweragevalue)::DECIMAL,2)"
				+ " from workdealfraction "
				+ " where workdeal_id="+getInstance().getId()
				+ " and seweragevalue is not null");
		return (BigDecimal)qTotalSewerage.getSingleResult();
	}
	
	private BigDecimal getTotalWaterWDF(){
//		BigDecimal totalWater = BigDecimal.ZERO;
		Query qTotalWater = getEntityManager().createNativeQuery("select round(sum(watervalue)::DECIMAL,2) "
				+ " from workdealfraction "
				+ " where workdeal_id="+getInstance().getId()
				+ " and seweragevalue is not null");
		return (BigDecimal)qTotalWater.getSingleResult();
	}
	
	
	private void calculateSharedValue(WorkDealFraction workDealFraction) {
		BigDecimal aux = BigDecimal.ONE;
		// Frente Contribucion x Factor Frente x factor Cobro
		aux = aux.multiply(workDealFraction.getContributionFront().multiply(this.getInstance().getFrontFactor())
				.multiply(this.getInstance().getCollectFactor()));
		aux = aux.setScale(2, RoundingMode.HALF_UP);
		workDealFraction.setSharedValue(aux);
	}

	private void calculateDifferentitatedValue(WorkDealFraction workDealFraction) {
		BigDecimal aux = BigDecimal.ONE;
		// Avaluo Comercial x Factor AvaluoFrente x factor Cobro
		 
		//aux = aux.multiply(workDealFraction.getProperty().getCurrentDomain().getCommercialAppraisal()
		//		.multiply(this.getInstance().getAppraisalFactor()).multiply(this.getInstance().getCollectFactor()));
		
		//macartuche
		//@tag cambioCalculoCEM
		aux = aux.multiply(workDealFraction.getCommercialAppraisal()
						.multiply(this.getInstance().getAppraisalFactor()).multiply(this.getInstance().getCollectFactor()));
				
		aux = aux.setScale(2, RoundingMode.HALF_UP);
		workDealFraction.setDifferentiatedValue(aux);
	}

	private BigDecimal calculateValuesWS(WorkDealFraction workDealFraction, BigDecimal value, 
			BigDecimal totalComercialAppraisal) {
		BigDecimal aux = BigDecimal.ONE;
		//BigDecimal sum = BigDecimal.ZERO;
		BigDecimal avg = BigDecimal.ZERO;

		/*
		for (WorkDealFraction wf : this.getInstance().getWorkDealFractions()) {
			sum = sum.add(wf.getProperty().getCurrentDomain().getCommercialAppraisal());
		}*/
		BigDecimal sum = totalComercialAppraisal;		
		avg = value.divide(sum, 12, RoundingMode.HALF_UP);
				
		//@tag cambioCalculoCEM
		//aux = workDealFraction.getProperty().getCurrentDomain().getCommercialAppraisal().multiply(avg);
		aux = workDealFraction.getCommercialAppraisal().multiply(avg);
		aux = aux.setScale(10, RoundingMode.HALF_UP);
		return aux;
	}

	/**
	 * Get the number of columns reports
	 * 
	 * @return
	 */
	public int getColumnsReport() {
		int columns = 12;

		if (this.getInstance().getDrinkingWater() != null && this.getInstance().getDrinkingWater()) {
			columns++;
		}

		if (this.getInstance().getSewerage() != null && this.getInstance().getSewerage()) {
			columns++;
		}

		return columns;
	}

	public String getWidthsReport() {
		String widths = "3.5 3.5 2 6 3 1 1.8 1.8 2 1.8 1.8 1.8";
		if (this.getInstance().getDrinkingWater() != null && this.getInstance().getDrinkingWater()) {
			widths += " 1.8";
		}

		if (this.getInstance().getSewerage() != null && this.getInstance().getSewerage()) {
			widths += " 1.8";
		}
		return widths;
	}

	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes().get("property");
		this.workDealFraction.setProperty(property);

		// agregado macartuche
		try {
			String direction = property.getLocation().getHouseNumber() + " "
					+ property.getLocation().getMainBlockLimit().getStreet().getName();
			
			System.out.println("====>"+direction);
			workDealFraction.setAddress(direction);
		} catch (Exception e) {

		}

	}

	/**
	 * Se carga el encargado de catastros
	 */
	public void loadCharge() {
		appraisalCharge = getCharge("DELEGATE_ID_CADASTER");
		if (appraisalCharge != null) {
			for (Delegate d : appraisalCharge.getDelegates()) {
				if (d.getIsActive())
					appraisalDelegate = d;
			}
		}
	}

	
	public List<WorkDealFraction> orderByAddresAndCadastralCode(List<WorkDealFraction> workDealFractions) {
		Collections.sort(workDealFractions, new WorkDealFractionComparator());
		return workDealFractions; 
	}

	public String generateReport(WorkDeal workDeal) {
		setInstance(workDeal);
		if (getInstance().getReportDate() == null) {
			getInstance().setReportDate(new Date());
			update();
		}
		calculateTotals();
		loadCharge();
		workDeal.setWorkDealFractions(orderByAddresAndCadastralCode(workDeal.getWorkDealFractions()));
		return "/cadaster/report/WorkDealReport.xhtml";
	}

	@Override
	@Transactional
	public String persist() {
		if (this.getInstance().getCollectFactor().compareTo(BigDecimal.ZERO) == -1
				|| this.getInstance().getCollectFactor().compareTo(BigDecimal.ONE) == 1) {
			addFacesMessageFromResourceBundle("workDeal.collectFactorInvalid");
			return "failed";
		}
		calculate();
		return super.persist();
	}

	@Override
	@Transactional
	public String update() {
		if (this.getInstance().getCollectFactor().compareTo(BigDecimal.ZERO) == -1
				|| this.getInstance().getCollectFactor().compareTo(BigDecimal.ONE) == 1) {
			addFacesMessageFromResourceBundle("workDeal.collectFactorInvalid");
			return "failed";
		}
		calculate();
		return super.update();
	}

	private Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class, systemParameter);
		return charge;
	}

	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	public Charge getAppraisalCharge() {
		return appraisalCharge;
	}

	public void setAppraisalCharge(Charge appraisalCharge) {
		this.appraisalCharge = appraisalCharge;
	}

	public Delegate getAppraisalDelegate() {
		return appraisalDelegate;
	}

	public void setAppraisalDelegate(Delegate appraisalDelegate) {
		this.appraisalDelegate = appraisalDelegate;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public WorkDealFraction getWorkDealFraction() {
		return workDealFraction;
	}

	public void setWorkDealFraction(WorkDealFraction workDealFraction) {
		this.workDealFraction = workDealFraction;
	}

	public BigDecimal getTotalAppraisal() {
		return totalAppraisal;
	}

	public void setTotalAppraisal(BigDecimal totalAppraisal) {
		this.totalAppraisal = totalAppraisal;
	}

	public BigDecimal getTotalSharedValue() {
		return totalSharedValue;
	}

	public void setTotalSharedValue(BigDecimal totalSharedValue) {
		this.totalSharedValue = totalSharedValue;
	}

	public BigDecimal getTotalDifferentiatedValue() {
		return totalDifferentiatedValue;
	}

	public void setTotalDifferentiatedValue(BigDecimal totalDifferentiatedValue) {
		this.totalDifferentiatedValue = totalDifferentiatedValue;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public BigDecimal getTotalContributionFront() {
		return totalContributionFront;
	}

	public void setTotalContributionFront(BigDecimal totalContributionFront) {
		this.totalContributionFront = totalContributionFront;
	}

	/**
	 * @author macartuche
	 * @date 2015-07-28
	 */

	public BigDecimal getTotalWater() {
		return totalWater;
	}

	public void setTotalWater(BigDecimal totalWater) {
		this.totalWater = totalWater;
	}

	public BigDecimal getTotalSewerage() {
		return totalSewerage;
	}

	public void setTotalSewerage(BigDecimal totalSewerage) {
		this.totalSewerage = totalSewerage;
	}
}
