package org.gob.gim.waterservice.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.CheckingRecordHome;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.action.MunicipalBondHome;
import org.gob.gim.waterservice.facade.WaterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.CheckingRecord;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.MonthType;
import ec.gob.gim.waterservice.model.RoutePreviewEmission;
import ec.gob.gim.waterservice.model.WaterMeterStatus;
import ec.gob.gim.waterservice.model.WaterSupply;
import ec.gob.gim.waterservice.model.WrongWaterEmission;

@Name("consumptionHome")
public class ConsumptionHome extends EntityHome<Consumption> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In(create = true)
	CheckingRecordHome checkingRecordHome;
	@In(create = true)
	ConsumptionStateHome consumptionStateHome;
	@In(create = true)
	MunicipalBondHome municipalBondHome;
	@In(create = true)
	WaterMeterStatusHome waterMeterStatusHome;
	@In(create = true)
	WaterSupplyHome waterSupplyHome;
	
	private List<RoutePreviewEmission> pemissions;
	private Date startDate = new Date();
	private Date endDate = new Date();
	// para reporte de emissiones mal realizadas
	private List<WrongWaterEmission> wrongWaterEmissions;
	
	// para las pre-emisiones
	public static String WATER_SERVICE_NAME = "/gim/WaterService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	// private SystemParameterService systemParameterService;
	private WaterService waterService;
	@In
	UserSession userSession;
	private EmissionOrder eo;
	
	@In EntityManager entityManager;
	
	private SystemParameterService systemParameterService;

	public void setConsumptionId(Long id) {
		setId(id);
	}

	public Long getConsumptionId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		CheckingRecord checkingRecord = checkingRecordHome.getDefinedInstance();
		/*if (checkingRecord != null) {
			getInstance().setCheckingRecord(checkingRecord);
		}*/
		ConsumptionState consumptionState = consumptionStateHome.getDefinedInstance();
		if (consumptionState != null) {
			getInstance().setConsumptionState(consumptionState);
		}
		MunicipalBond municipalBond = municipalBondHome.getDefinedInstance();
		if (municipalBond != null) {
			getInstance().setMunicipalBond(municipalBond);
		}
		WaterMeterStatus waterMeterStatus = waterMeterStatusHome.getDefinedInstance();
		if (waterMeterStatus != null) {
			getInstance().setWaterMeterStatus(waterMeterStatus);
		}
		WaterSupply waterSupply = waterSupplyHome.getDefinedInstance();
		if (waterSupply != null) {
			getInstance().setWaterSupply(waterSupply);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Consumption getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void findPerformedEmission() {
		Query q = this.getEntityManager().createNamedQuery("RoutePreviewEmission.findPerformEmissionByDate");
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		pemissions = q.getResultList();
	}

	public List<RoutePreviewEmission> getPemissions() {
		return pemissions;
	}

	public void setPemissions(List<RoutePreviewEmission> pemissions) {
		this.pemissions = pemissions;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	private Long waterSupplyEntryId;
	
	private Long paidStatus;
	private Long externalPayStatus;
	private Long blockedStatus;
	
	private List<Long> statusesIds;
	
	private List<Long> findExceptedMunicipalBondStatuses(){
		if(statusesIds != null) return statusesIds;
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		paidStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
		externalPayStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		blockedStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		statusesIds = new ArrayList<Long>();
		statusesIds.add(paidStatus);
		statusesIds.add(externalPayStatus);
		statusesIds.add(blockedStatus);
		return statusesIds;
	}
	
	private Long findWaterSupplyEntryId(Boolean currentAccount){
		if(!currentAccount) return 76L;		
		if(waterSupplyEntryId != null) return waterSupplyEntryId;
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		waterSupplyEntryId = systemParameterService.findParameter("ENTRY_ID_WATER_SERVICE_TAX");
		return waterSupplyEntryId;
	}

	public void findWrongEmission(Boolean currentAccount) {
		String sentece = "select NEW ec.gob.gim.waterservice.model.WrongWaterEmission(mb.id, mb.number, mb.serviceDate, resident.name, " 
				+ "resident.identificationNumber,mb.groupingCode,mb.paidTotal)  from MunicipalBond mb "
				+ "join mb.items i "
				+ "join mb.resident resident "
				+ "join mb.adjunct waterservicereference "
				+ "where mb.entry.id=:entryId and "
				+ "waterservicereference.waterSupplyCategory<>'CATEGORÍA 0' " 
				+ "and i.entry.id=:entryId and i.total=0 and mb.municipalBondStatus.id not in (:statusIds) "
				+ "order by mb.serviceDate ";
		Query q = this.getEntityManager().createQuery(sentece);
		q.setParameter("entryId", findWaterSupplyEntryId(currentAccount));
		q.setParameter("statusIds", findExceptedMunicipalBondStatuses());
		wrongWaterEmissions = q.getResultList();
		for(WrongWaterEmission wwe : wrongWaterEmissions){
			wwe.setConsumption(findConsumption(wwe));
		}
	}

	public Consumption findConsumption(WrongWaterEmission wwe) {
		Integer year=new Integer(new SimpleDateFormat("yyyy").format(wwe.getServiceDate()));
		Integer month=new Integer(new SimpleDateFormat("MM").format(wwe.getServiceDate()));
		String sentence = "SELECT consumption FROM Consumption consumption "
				+ "left join fetch consumption.waterSupply w "
				+ "left join fetch consumption.consumptionState "
				+ "WHERE w.serviceNumber  = :serviceNumber and consumption.year=:year and consumption.month=:month ";
		Query q=this.getEntityManager().createQuery(sentence);
		q.setParameter("serviceNumber",new Integer(wwe.getMunicipalBondGroupingcode()));
		q.setParameter("year", year);
		q.setParameter("month", month);
		if(q.getResultList().size()>0){
			return (Consumption) q.getSingleResult();
		}
		return null;
	}
	
	public void preEmiteConsumptions(){
		List<Consumption> consumptions=new ArrayList<Consumption>();
		
		if(wrongWaterEmissions == null) return;
		
		for(WrongWaterEmission wwe:wrongWaterEmissions){
			consumptions.add(wwe.getConsumption());
		}
		if (waterService == null)
			waterService = ServiceLocator.getInstance().findResource(WATER_SERVICE_NAME);
		try {
			eo = waterService.reCalculatePreEmissionOrder(consumptions, userSession.getFiscalPeriod(),userSession.getPerson());
			for (MunicipalBond mbnew : eo.getMunicipalBonds()) {
				for (WrongWaterEmission wwe : wrongWaterEmissions) {
					if(wwe.getMunicipalBondGroupingcode().equals(mbnew.getGroupingCode())){
						System.out.println("contri ant:.................. "+wwe.getResidentName());
						System.out.println("contri act:.................. "+mbnew.getResident().getName());
						System.out.println("el total ant:.................. "+wwe.getMunicipalBondPaidtotal());
						System.out.println("el total act:.................. "+mbnew.getPaidTotal());
						wwe.setOkMunicipalBond(mbnew);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processChange(){
		System.out.println("inicia el cambiossssssssssssssssssssssssssssssssssss");
		Query q;
		for (WrongWaterEmission wwe : wrongWaterEmissions) {
			q = this.getEntityManager().createNamedQuery("MunicipalBond.findById");
			q.setParameter("municipalBondId", wwe.getMunicipalBondId());
			MunicipalBond mb = (MunicipalBond) q.getSingleResult();
			for (Item itemBad : mb.getItems()) {
				for (Item itemOk : wwe.getOkMunicipalBond().getItems()) {
					if(itemBad.getEntry().getId().equals(itemOk.getEntry().getId())){
						itemBad.setValue(itemOk.getValue());
						itemBad.setTotal(itemOk.getTotal());
					}
				}
			}
			
			mb.setBalance(wwe.getOkMunicipalBond().getBalance());
			mb.setBase(wwe.getOkMunicipalBond().getBase());
			mb.setDiscount(wwe.getOkMunicipalBond().getDiscount());
			mb.setPaidTotal(wwe.getOkMunicipalBond().getPaidTotal());
			mb.setInterest(wwe.getOkMunicipalBond().getInterest());
			mb.setTaxableTotal(wwe.getOkMunicipalBond().getTaxableTotal());
			mb.setTaxesTotal(wwe.getOkMunicipalBond().getTaxesTotal());
			mb.setValue(wwe.getOkMunicipalBond().getValue());
			this.municipalBondHome.setInstance(mb);
			this.municipalBondHome.update();
			/*this.getEntityManager().refresh(mb);
			this.getEntityManager().flush();*/
		}
		//this.update();
		System.out.println(":::::::eeeeeeeeeeeeeeeeeeeeeeeee:::::::::::::");
	}
	
	//@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void processChangeOne(WrongWaterEmission wwe){
		System.out.println("inicia el cambiossssssssssssssssssssssssssssssssssss");		
		
		Query q = this.getEntityManager().createNamedQuery("MunicipalBond.findById");
		q.setParameter("municipalBondId", wwe.getMunicipalBondId());
		MunicipalBond mb = (MunicipalBond) q.getSingleResult();
		for (Item itemBad : mb.getItems()) {
			for (Item itemOk : wwe.getOkMunicipalBond().getItems()) {
				if (itemBad.getEntry().getId().equals(itemOk.getEntry().getId())) {
					System.out.println("entra al for grande    ...... "+itemBad.getEntry().getId());
					String jpqlUpdate = "update Item item set value = :value, total=:total where item.id = :itemId";
					int updatedEntities = entityManager.createQuery( jpqlUpdate )
					                            .setParameter( "value", itemOk.getValue() )
					                            .setParameter( "total", itemOk.getTotal() )
					                            .setParameter( "itemId", itemBad.getId() )
					                            .executeUpdate();					
					
					//itemBad.setValue(itemOk.getValue());
					//itemBad.setTotal(itemOk.getTotal());
				}
			}
		}
		
		String updateMb = "UPDATE MunicipalBond mb SET mb.balance = :balance, mb.base=:base, mb.discount= :discount, " +
				"mb.paidTotal=:paidTotal,mb.interest=:interest,mb.taxableTotal=:taxableTotal,mb.taxesTotal=:taxesTotal, " +
				"mb.value=:value " +
				"where mb.id = :mbId";
		
		int updatedEntities = entityManager.createQuery( updateMb )
        .setParameter( "balance", wwe.getOkMunicipalBond().getBalance() )
        .setParameter( "base", wwe.getOkMunicipalBond().getBase() )
        .setParameter( "discount", wwe.getOkMunicipalBond().getDiscount() )
        .setParameter( "paidTotal", wwe.getOkMunicipalBond().getPaidTotal() )
        .setParameter( "interest", wwe.getOkMunicipalBond().getInterest() )
        .setParameter( "taxableTotal", wwe.getOkMunicipalBond().getTaxableTotal() )
        .setParameter( "taxesTotal", wwe.getOkMunicipalBond().getTaxesTotal() )
        .setParameter( "value", wwe.getOkMunicipalBond().getValue() )
        .setParameter( "mbId", mb.getId() )
        .executeUpdate();
		
		mb.setBalance(wwe.getOkMunicipalBond().getBalance());
		mb.setBase(wwe.getOkMunicipalBond().getBase());
		mb.setDiscount(wwe.getOkMunicipalBond().getDiscount());
		mb.setPaidTotal(wwe.getOkMunicipalBond().getPaidTotal());
		mb.setInterest(wwe.getOkMunicipalBond().getInterest());
		mb.setTaxableTotal(wwe.getOkMunicipalBond().getTaxableTotal());
		mb.setTaxesTotal(wwe.getOkMunicipalBond().getTaxesTotal());
		mb.setValue(wwe.getOkMunicipalBond().getValue());
		//entityManager.merge(mb);
		//entityManager.flush();
		//this.municipalBondHome.setInstance(mb);
		//this.municipalBondHome.update();
		/*
		 * this.getEntityManager().refresh(mb); this.getEntityManager().flush();
		 */
		
		//this.update();
		System.out.println(":::::::eeeeeeeeeeeeeeeeeeeeeeeee::::::::::::: "+updatedEntities);
	}
	

	public List<WrongWaterEmission> getWrongWaterEmissions() {
		return wrongWaterEmissions;
	}

	public void setWrongWaterEmissions(
			List<WrongWaterEmission> wrongWaterEmissions) {
		this.wrongWaterEmissions = wrongWaterEmissions;
	}

	public List<Long> getStatusesIds() {
		return statusesIds;
	}

	public void setStatusesIds(List<Long> statusesIds) {
		this.statusesIds = statusesIds;
	}
		
}
