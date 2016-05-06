package org.gob.gim.revenue.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.gob.gim.common.exception.NonUniqueIdentificationNumberException;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryStructure;
import ec.gob.gim.revenue.model.EntryStructureType;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.security.model.User;

@Local
public interface RevenueService {
	
	public static final String LOCAL_NAME = "/gim/RevenueService/local";
	
	Resident findResidentById(Long residentId);
	Resident findResidentByIdentificationNumber(String identificationNumber) throws NonUniqueIdentificationNumberException;
	List<Resident> findResidentByCriteria(String Criteria);
	
	FiscalPeriod findFiscalPeriodById(Long fiscalPeriodId);
	List<FiscalPeriod> findFiscalPeriodCurrent(Date currentDate);
	
	Entry findEntryByCode(String code);
	Entry findEntryById(Long entryId);
	List<Entry> findEntryByCriteria(String criteria);
	List<MunicipalBond> findMunicipalBondsForReverseByEmitter(Long residentId, Long emitterId, Date date);
	void update(List<Long> bondIds, Long municipalBondStatusId, Long userId, String explanation);
	//void updateReversedDescriptionAndValue(List<Long> bondIds, String observation);
			
	MunicipalBond emit(MunicipalBond municipalBond, User user) throws Exception;
	void emit(List<MunicipalBond> municipalBonds, User user) throws Exception;
	void emitFuture(List<MunicipalBond> municipalBonds, User user) throws Exception;
	//void emit(Long emissionOrderId,Long userId) throws Exception;
	void emit(Long emissionOrderId, Long userId, Date startDate) throws Exception;
	void changeFutureToPendign(List<MunicipalBond> municipalBonds, User user, Person person, String explanation) throws Exception;
	

	public MunicipalBond createMunicipalBond(
			Resident resident, 
			Entry entry,
			FiscalPeriod fiscalPeriod, 
			EntryValueItem entryValueItem,
			boolean isEmission,
			Object ... facts) throws EntryDefinitionNotFoundException;

	/*
	public MunicipalBond createDeferredMunicipalBond(
			Resident resident,
			Entry entry,
			FiscalPeriod fiscalPeriod,
			BigDecimal base, 
			Date serviceDate, 
			Date expirationDate, 
			String reference, 
			String description,
			Object ... facts ) throws EntryDefinitionNotFoundException;
	*/
	/*
	MunicipalBond createMunicipalBond(Long residentId, Long fiscalPeriodId,
			Long entryId, BigDecimal mainEntryValue, Date serviceDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Long residentId, Long fiscalPeriodId,
			Long entryId, BigDecimal mainEntryValue, Date serviceDate, String reference,
			MunicipalBondType municipalBondType, Object ... facts) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident, FiscalPeriod fiscalPeriod, Entry entryMain, 
			BigDecimal mainEntryValue, Date serviceDate, MunicipalBondType municipalBondType) 
			throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, String reference,
			MunicipalBondType municipalBondType, Object ... facts) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident,
			Long fiscalPeriodId, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident,
			Long fiscalPeriodId, Long entryMainId,
			BigDecimal mainEntryValue, Date serviceDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Long residentId,
			Long fiscalPeriodId, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	
	MunicipalBond createMunicipalBond(Long residentId, Long fiscalPeriodId,
			Long entryId, BigDecimal mainEntryValue, Date serviceDate, Date expirationDate,
			String reference, MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident,
			Long fiscalPeriodId, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident,
			Long fiscalPeriodId, Long entryMainId,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Long residentId,
			Long fiscalPeriodId, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	*/

	/*
	MunicipalBond createDeferredMunicipalBond(Long residentId,
			Long fiscalPeriodId, Long entryMainId,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createDeferredMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createDeferredMunicipalBond(Resident resident,
			Long fiscalPeriodId, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createDeferredMunicipalBond(Long residentId,
			Long fiscalPeriodId, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createDeferredMunicipalBond(Resident resident,
			Long fiscalPeriodId, Long entryMainId,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createDeferredMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType) throws EntryDefinitionNotFoundException;
	MunicipalBond createDeferredMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entryMain,
			BigDecimal mainEntryValue, Date serviceDate, Date expirationDate, String reference,
			MunicipalBondType municipalBondType, Object ... facts) throws EntryDefinitionNotFoundException;
	*/
	
	List<MunicipalBond> findMunicipalBondByResidentIdAndTypeAndStatus(
			Long residentId, MunicipalBondType municipalBondType,
			Long municipalBondStatusId);	
	
	List<EntryStructure> findEntryStructureChildren(Long entryParentId);
	List<EntryStructure> findEntryStructureChildrenByType(Long entryParentId,
			EntryStructureType entryStructureType);
	Entry findByAccountCode(String accountCode);

	/*
	void createItemsToMunicipalBond(MunicipalBond municipalBond, 
			BigDecimal mainEntryValue, Date serviceDate) throws EntryDefinitionNotFoundException;
	void createItemsToDeferredMunicipalBond(MunicipalBond municipalBond,
			BigDecimal mainEntryValue, Date serviceDate) throws EntryDefinitionNotFoundException;
	*/
	
	//void calculeMunicipalBond(MunicipalBond mb);
	
}
