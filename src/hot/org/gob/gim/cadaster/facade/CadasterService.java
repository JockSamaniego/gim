package org.gob.gim.cadaster.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.LocationPropertySinat;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.cadaster.model.UnbuiltLot;
import ec.gob.gim.cadaster.model.WorkDealFraction;
import ec.gob.gim.cadaster.model.dto.AppraisalsPropertyDTO;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;

@Local
public interface CadasterService {
	
	public String LOCAL_NAME = "/gim/CadasterService/local";
	
	void preEmitUtilityTax(Property p, Domain d, FiscalPeriod f, Person per) throws Exception;
	void preEmitAlcabalaTax(Property p,Domain d, FiscalPeriod f, Person per, boolean exoneration, String exonerationReason) throws Exception;	
	List<MunicipalBond> onlyCalculatePreEmissionOrderPropertyTax(EmissionOrder eo, Entry e, List<Property> properties, FiscalPeriod f, Person p,boolean isUrban, boolean isSpecial) throws Exception;
	List<MunicipalBond> onlyCalculatePreEmissionOrderUnbuiltLotTax(EmissionOrder eo, Entry e, List<UnbuiltLot> unbuiltLots, FiscalPeriod f, Person p) throws Exception;
	List<Property> findPropertyByCadastralCodeAndType(String cadastralCode, String systemParameterName, List<Long> noEmitFor);
	TerritorialDivision findTerritorialDivision(String cadastralCode, int x, int y, TerritorialDivision td);
	TerritorialDivision findTerritorialDivision(String code, long territorialDivisionTypeId);
	TerritorialDivision findTerritorialDivision(String code, long territorialDivisionTypeId, TerritorialDivision parent);
	TerritorialDivision findDefaultCanton();
	TerritorialDivision findDefaultProvince();
	void calculateExemptions(Long fiscalPeriodId, Date fromDate, Date untilDate);	
	void createCopyOfUnbuiltLots(List<UnbuiltLot> unbuiltLots, FiscalPeriod fiscalPeriod);
	Domain update(Domain domain) throws Exception;
	
	/*
	 * Rene Ortega
	 * 2016-08-16
	 * metodo que devuelve los avaluos de x propiedad
	 * 
	 */
	List<AppraisalsPropertyDTO> findAppraisalsForProperty(Long property_id);
	WorkDealFraction verifyCheckAlreadyAddedPropertyIntoWorkDeal(Long workDeal_id, Long property_id);
	
	/*
	 * Rene Ortega
	 * 2017-02-21
	 * metodo que devuelve la localizacion de propiedades rusticas
	 * 
	 */
	LocationPropertySinat findLocationPropertySinat(Long property_id);
	
	//Jock Samaniego
	//actualizar deleted predios rusticos 23/09/2016
		public void updateRusticProperty(Property property);
}
