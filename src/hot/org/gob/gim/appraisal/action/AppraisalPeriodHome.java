package org.gob.gim.appraisal.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.appraisal.model.AppraisalItemBase;
import ec.gob.gim.appraisal.model.AppraisalItemExternal;
import ec.gob.gim.appraisal.model.AppraisalItemRoof;
import ec.gob.gim.appraisal.model.AppraisalItemStructure;
import ec.gob.gim.appraisal.model.AppraisalItemWall;
import ec.gob.gim.appraisal.model.AppraisalPeriod;
import ec.gob.gim.appraisal.model.AppraisalTotalExternal;
import ec.gob.gim.appraisal.model.AppraisalTotalRoof;
import ec.gob.gim.appraisal.model.AppraisalTotalStructure;
import ec.gob.gim.appraisal.model.AppraisalTotalWall;

@Name("appraisalPeriodHome")
public class AppraisalPeriodHome extends EntityHome<AppraisalPeriod> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	AppraisalTotalStructureHome appraisalTotalStructureHome;
	
	Log logger;
	private String criteria;
	private String typeItem;

	AppraisalPeriod copy;

	private AppraisalItemStructure appraisalItemStructure;
	private AppraisalItemWall appraisalItemWall;
	private AppraisalItemRoof appraisalItemRoof;
	private AppraisalItemExternal appraisalItemExternal;
	
	private List<AppraisalItemStructure> appraisalItemsStructure;
	private List<AppraisalItemWall> appraisalItemsWall;
	private List<AppraisalItemRoof> appraisalItemsRoof;
	private List<AppraisalItemExternal> appraisalItemsExternal;
	
	private AppraisalTotalStructure appraisalTotalStructure;
	private AppraisalTotalWall appraisalTotalWall;
	private AppraisalTotalRoof appraisalTotalRoof;
	private AppraisalTotalExternal appraisalTotalExternal;
	
	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		//System.out.println("============> wire periodo actual");
		//System.out.println(this.getInstance().getName());
	}

	public boolean isWired() {
		return true;
	}

	public AppraisalPeriod getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@Override
	public String update() {
		calculateTotalStructure();
		calculateTotalWall();
		calculateTotalRoof();
		calculateTotalExternal();
		return super.update();
	}
	
	@Override
	public String persist() {
//		calculateTotalStructure();
//		calculateTotalWall();
//		calculateTotalRoof();
//		calculateTotalExternal();
		return super.persist();
	}
	
	public void changePeriod(AppraisalPeriod appraisalPeriod) {
		//System.out.println("============> Se instancia el periodo actual");
		this.setInstance(appraisalPeriod);
	}

	public void setAppraisalPeriodId(Long id) {
		setId(id);
	}

	public Long getAppraisalPeriodId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getTypeItem() {
		return typeItem;
	}

	public void setTypeItem(String typeItem) {
		this.typeItem = typeItem;
	}

	public void changeTab(ValueChangeEvent e) {
		this.setTypeItem(e.getNewValue().toString());
	}
	
	public AppraisalItemStructure getAppraisalItemStructure() {
		return appraisalItemStructure;
	}

	public void setAppraisalItemStructure(
			AppraisalItemStructure appraisalItemStructure) {
		this.appraisalItemStructure = appraisalItemStructure;
	}

	public AppraisalItemWall getAppraisalItemWall() {
		return appraisalItemWall;
	}

	public void setAppraisalItemWall(AppraisalItemWall appraisalItemWall) {
		this.appraisalItemWall = appraisalItemWall;
	}

	public AppraisalItemRoof getAppraisalItemRoof() {
		return appraisalItemRoof;
	}

	public void setAppraisalItemRoof(AppraisalItemRoof appraisalItemRoof) {
		this.appraisalItemRoof = appraisalItemRoof;
	}

	public AppraisalItemExternal getAppraisalItemExternal() {
		return appraisalItemExternal;
	}

	public void setAppraisalItemExternal(AppraisalItemExternal appraisalItemExternal) {
		this.appraisalItemExternal = appraisalItemExternal;
	}

	public List<AppraisalItemStructure> getAppraisalItemsStructure() {
		return appraisalItemsStructure;
	}

	public void setAppraisalItemsStructure(
			List<AppraisalItemStructure> appraisalItemsStructure) {
		this.appraisalItemsStructure = appraisalItemsStructure;
	}

	public List<AppraisalItemWall> getAppraisalItemsWall() {
		return appraisalItemsWall;
	}

	public void setAppraisalItemsWall(List<AppraisalItemWall> appraisalItemsWall) {
		this.appraisalItemsWall = appraisalItemsWall;
	}

	public List<AppraisalItemRoof> getAppraisalItemsRoof() {
		return appraisalItemsRoof;
	}

	public void setAppraisalItemsRoof(List<AppraisalItemRoof> appraisalItemsRoof) {
		this.appraisalItemsRoof = appraisalItemsRoof;
	}

	public List<AppraisalItemExternal> getAppraisalItemsExternal() {
		return appraisalItemsExternal;
	}

	public void setAppraisalItemsExternal(
			List<AppraisalItemExternal> appraisalItemsExternal) {
		this.appraisalItemsExternal = appraisalItemsExternal;
	}
	
	public void createAppraisalItemStructure() {
		this.appraisalItemStructure = new AppraisalItemStructure();
		this.appraisalItemStructure.setAppraisalTotalStructure(appraisalTotalStructure);
	}

	public void editAppraisalItemStructure(AppraisalItemStructure appraisalItemStructure) {
		this.appraisalItemStructure = appraisalItemStructure;
	}

	public void createAppraisalItemWall() {
		this.appraisalItemWall = new AppraisalItemWall();
		this.appraisalItemWall.setAppraisalTotalWall(appraisalTotalWall);
	}

	public void editAppraisalItemWall(AppraisalItemWall appraisalItemWall) {
		this.appraisalItemWall = appraisalItemWall;
	}

	public void createAppraisalItemRoof() {
		this.appraisalItemRoof = new AppraisalItemRoof();
		this.appraisalItemRoof.setAppraisalTotalRoof(appraisalTotalRoof);
	}

	public void editAppraisalItemRoof(AppraisalItemRoof appraisalItemRoof) {
		this.appraisalItemRoof = appraisalItemRoof;
	}

	public void createAppraisalItemExternal() {
		this.appraisalItemExternal = new AppraisalItemExternal();
		this.appraisalItemExternal.setAppraisalTotalExternal(appraisalTotalExternal);
	}

	public void editAppraisalItemExternal(AppraisalItemExternal appraisalItemExternal) {
		this.appraisalItemExternal = appraisalItemExternal;
	}

	public AppraisalTotalStructure getAppraisalTotalStructure() {
		return appraisalTotalStructure;
	}

	public void setAppraisalTotalStructure(
			AppraisalTotalStructure appraisalTotalStructure) {
		this.appraisalTotalStructure = appraisalTotalStructure;
	}

	public void createAppraisalTotalStructure() {
		this.appraisalTotalStructure = new AppraisalTotalStructure();
	}

	public void editAppraisalTotalStructure(AppraisalTotalStructure appraisalTotalStructure) {
		this.appraisalTotalStructure = appraisalTotalStructure;
	}
	
	public AppraisalTotalWall getAppraisalTotalWall() {
		return appraisalTotalWall;
	}

	public void setAppraisalTotalWall(
			AppraisalTotalWall appraisalTotalWall) {
		this.appraisalTotalWall = appraisalTotalWall;
	}

	public void createAppraisalTotalWall() {
		this.appraisalTotalWall = new AppraisalTotalWall();
	}

	public void editAppraisalTotalWall(AppraisalTotalWall appraisalTotalWall) {
		this.appraisalTotalWall = appraisalTotalWall;
	}

	public AppraisalTotalRoof getAppraisalTotalRoof() {
		return appraisalTotalRoof;
	}

	public void setAppraisalTotalRoof(
			AppraisalTotalRoof appraisalTotalRoof) {
		this.appraisalTotalRoof = appraisalTotalRoof;
	}

	public void createAppraisalTotalRoof() {
		this.appraisalTotalRoof = new AppraisalTotalRoof();
	}

	public void editAppraisalTotalRoof(AppraisalTotalRoof appraisalTotalRoof) {
		this.appraisalTotalRoof = appraisalTotalRoof;
	}

	public AppraisalTotalExternal getAppraisalTotalExternal() {
		return appraisalTotalExternal;
	}

	public void setAppraisalTotalExternal(
			AppraisalTotalExternal appraisalTotalExternal) {
		this.appraisalTotalExternal = appraisalTotalExternal;
	}

	public void createAppraisalTotalExternal() {
		this.appraisalTotalExternal = new AppraisalTotalExternal();
	}

	public void editAppraisalTotalExternal(AppraisalTotalExternal appraisalTotalExternal) {
		this.appraisalTotalExternal = appraisalTotalExternal;
	}

	@SuppressWarnings("unchecked")
	public List<AppraisalItemBase> findAppraisalItemsBaseByName(Object suggest) {
		Query query = this.getEntityManager().createNamedQuery(
				"AppraisalItemBase.findAll");
		query.setParameter("aIBaseName", suggest.toString());
		return (List<AppraisalItemBase>) query.getResultList();
	}

	public void calculateTotalStructure(){
		if(this.getInstance() != null){
			for (AppraisalTotalStructure aTStructure : this.getInstance().getAppraisalTotalStructure()){
				BigDecimal total = BigDecimal.ZERO;
				for (AppraisalItemStructure aIStructure : aTStructure.getAppraisalItemsStructure()){
					total = total.add(aIStructure.getSubtotal());
				}
				if (aTStructure.getTotal().compareTo(total) != 0) aTStructure.setTotal(total);
			}
		}
	}
	
	public void calculateTotalWall(){
		if(this.getInstance() != null){
			for (AppraisalTotalWall aTWall : this.getInstance().getAppraisalTotalWall()){
				BigDecimal total = BigDecimal.ZERO;
				for (AppraisalItemWall aIWall : aTWall.getAppraisalItemsWall()){
					total = total.add(aIWall.getSubtotal());
				}
				if (aTWall.getTotal().compareTo(total) != 0) aTWall.setTotal(total);
			}
		}
	}
	
	public void calculateTotalRoof(){
		if(this.getInstance() != null){
			for (AppraisalTotalRoof aTRoof : this.getInstance().getAppraisalTotalRoof()){
				BigDecimal total = BigDecimal.ZERO;
				for (AppraisalItemRoof aIRoof : aTRoof.getAppraisalItemsRoof()){
					total = total.add(aIRoof.getSubtotal());
				}
				if (aTRoof.getTotal().compareTo(total) != 0) aTRoof.setTotal(total);
			}
		}
	}
	
	public void calculateTotalExternal(){
		if(this.getInstance() != null){
			for (AppraisalTotalExternal aTExternal : this.getInstance().getAppraisalTotalExternal()){
				BigDecimal total = BigDecimal.ZERO;
				for (AppraisalItemExternal aIExternal : aTExternal.getAppraisalItemsExternal()){
					total = total.add(aIExternal.getSubtotal());
				}
				if (aTExternal.getTotal().compareTo(total) != 0) aTExternal.setTotal(total);
			}
		}
	}

	public void updateCostes(AppraisalPeriod appraisalPeriod){
		this.setInstance(appraisalPeriod);
		updateCostesStructure();
		updateCostesWall();
		updateCostesRoof();
		updateCostesExternal();
		update();
	}
	
	public void updateCostesStructure(){
		if(this.getInstance() != null){
			for (AppraisalTotalStructure aTStructure : this.getInstance().getAppraisalTotalStructure()){
				BigDecimal subTotal = BigDecimal.ZERO;
				for (AppraisalItemStructure aIStructure : aTStructure.getAppraisalItemsStructure()){
					aIStructure.setCoste(aIStructure.getAppraisalItemBase().getCoste());
					subTotal = aIStructure.getAppraisalItemBase().getCoste().multiply(aIStructure.getCoeficiente()).setScale(2, RoundingMode.HALF_UP);
					aIStructure.setSubtotal(subTotal);
				}
			}
		}		
	}
	
	public void updateCostesWall(){
		if(this.getInstance() != null){
			for (AppraisalTotalWall aTWall : this.getInstance().getAppraisalTotalWall()){
				BigDecimal subTotal = BigDecimal.ZERO;
				for (AppraisalItemWall aIWall : aTWall.getAppraisalItemsWall()){
					aIWall.setCoste(aIWall.getAppraisalItemBase().getCoste());
					subTotal = aIWall.getAppraisalItemBase().getCoste().multiply(aIWall.getCoeficiente()).setScale(2, RoundingMode.HALF_UP);
					aIWall.setSubtotal(subTotal);
				}
			}
		}		
	}
	
	public void updateCostesRoof(){
		if(this.getInstance() != null){
			for (AppraisalTotalRoof aTRoof : this.instance.getAppraisalTotalRoof()){
				BigDecimal subTotal = BigDecimal.ZERO;
				for (AppraisalItemRoof aIRoof : aTRoof.getAppraisalItemsRoof()){
					aIRoof.setCoste(aIRoof.getAppraisalItemBase().getCoste());
					subTotal = aIRoof.getAppraisalItemBase().getCoste().multiply(aIRoof.getCoeficiente()).setScale(2, RoundingMode.HALF_UP);
					aIRoof.setSubtotal(subTotal);
				}
			}
		}		
	}
	
	public void updateCostesExternal(){
		if(this.getInstance() != null){
			for (AppraisalTotalExternal aTExternal : this.getInstance().getAppraisalTotalExternal()){
				BigDecimal subTotal = BigDecimal.ZERO;
				for (AppraisalItemExternal aIExternal : aTExternal.getAppraisalItemsExternal()){
					aIExternal.setCoste(aIExternal.getAppraisalItemBase().getCoste());
					subTotal = aIExternal.getAppraisalItemBase().getCoste().multiply(aIExternal.getCoeficiente()).setScale(2, RoundingMode.HALF_UP);
					aIExternal.setSubtotal(subTotal);
				}
			}
		}		
	}
	
	public void copyPeriod(AppraisalPeriod appraisalPeriod){
		copy = new AppraisalPeriod();
		copy.setCode("copy"+appraisalPeriod.getCode());
		copy.setName("copy"+appraisalPeriod.getName());
		copy.setFactorHasEquipment(appraisalPeriod.getFactorHasEquipment());
		copy.setFactorHasntEquipment(appraisalPeriod.getFactorHasntEquipment());
		copy.setFactorHasWater(appraisalPeriod.getFactorHasWater());
		copy.setFactorHasntWater(appraisalPeriod.getFactorHasntWater());
		copy.setFactorHasSewerage(appraisalPeriod.getFactorHasSewerage());
		copy.setFactorHasntSewerage(appraisalPeriod.getFactorHasntSewerage());
		copy.setFactorHasEnergy(appraisalPeriod.getFactorHasEnergy());
		copy.setFactorHasntEnergy(appraisalPeriod.getFactorHasntEnergy());
		copy.setOpen(true);
		BigDecimal cero = BigDecimal.ZERO;
		
		for (AppraisalTotalStructure aTStructureOrigen : appraisalPeriod.getAppraisalTotalStructure()){
			AppraisalTotalStructure aTStructureCopy = new AppraisalTotalStructure();
			aTStructureCopy.setStructureMaterial(aTStructureOrigen.getStructureMaterial());
			aTStructureCopy.setTotal(cero);
			for (AppraisalItemStructure aIStructureOrigen : aTStructureOrigen.getAppraisalItemsStructure()){
				AppraisalItemStructure aIStructureCopy = new AppraisalItemStructure();
				aIStructureCopy.setAppraisalItemBase(aIStructureOrigen.getAppraisalItemBase());
				aIStructureCopy.setCoeficiente(aIStructureOrigen.getCoeficiente());
				aIStructureCopy.setCoste(cero);
				aIStructureCopy.setSubtotal(cero);
				aTStructureCopy.add(aIStructureCopy);
			}
			copy.add(aTStructureCopy);
		}
		
		for (AppraisalTotalWall aTWallOrigen : appraisalPeriod.getAppraisalTotalWall()){
			AppraisalTotalWall aTWallCopy = new AppraisalTotalWall();
			aTWallCopy.setWallMaterial(aTWallOrigen.getWallMaterial());
			aTWallCopy.setTotal(cero);
			for (AppraisalItemWall aIWallOrigen : aTWallOrigen.getAppraisalItemsWall()){
				AppraisalItemWall aIWallCopy = new AppraisalItemWall();
				aIWallCopy.setAppraisalItemBase(aIWallOrigen.getAppraisalItemBase());
				aIWallCopy.setCoeficiente(aIWallOrigen.getCoeficiente());
				aIWallCopy.setCoste(cero);
				aIWallCopy.setSubtotal(cero);
				aTWallCopy.add(aIWallCopy);
			}
			copy.add(aTWallCopy);
		}
		
		for (AppraisalTotalRoof aTRoofOrigen : appraisalPeriod.getAppraisalTotalRoof()){
			AppraisalTotalRoof aTRoofCopy = new AppraisalTotalRoof();
			aTRoofCopy.setRoofMaterial(aTRoofOrigen.getRoofMaterial());
			aTRoofCopy.setTotal(cero);
			for (AppraisalItemRoof aIRoofOrigen : aTRoofOrigen.getAppraisalItemsRoof()){
				AppraisalItemRoof aIRoofCopy = new AppraisalItemRoof();
				aIRoofCopy.setAppraisalItemBase(aIRoofOrigen.getAppraisalItemBase());
				aIRoofCopy.setCoeficiente(aIRoofOrigen.getCoeficiente());
				aIRoofCopy.setCoste(cero);
				aIRoofCopy.setSubtotal(cero);
				aTRoofCopy.add(aIRoofCopy);
			}
			copy.add(aTRoofCopy);
		}
		
		for (AppraisalTotalExternal aTExternalOrigen : appraisalPeriod.getAppraisalTotalExternal()){
			AppraisalTotalExternal aTExternalCopy = new AppraisalTotalExternal();
			aTExternalCopy.setExternalFinishing(aTExternalOrigen.getExternalFinishing());
			aTExternalCopy.setTotal(cero);
			for (AppraisalItemExternal aIExternalOrigen : aTExternalOrigen.getAppraisalItemsExternal()){
				AppraisalItemExternal aIExternalCopy = new AppraisalItemExternal();
				aIExternalCopy.setAppraisalItemBase(aIExternalOrigen.getAppraisalItemBase());
				aIExternalCopy.setCoeficiente(aIExternalOrigen.getCoeficiente());
				aIExternalCopy.setCoste(cero);
				aIExternalCopy.setSubtotal(cero);
				aTExternalCopy.add(aIExternalCopy);
			}
			copy.add(aTExternalCopy);
		}
		
		this.setInstance(copy);
		this.wire();
		persist();
	}
	
}
