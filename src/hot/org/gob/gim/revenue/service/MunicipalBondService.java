package org.gob.gim.revenue.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.TaxRate;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryStructureType;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.adjunct.PropertyAppraisal;

@Local
public interface MunicipalBondService {

	public String LOCAL_NAME = "/gim/MunicipalBondService/local";

	public MunicipalBond createMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entry,
			EntryValueItem entryValueItem, boolean isEmission,
			boolean applyDiscounts, 
			Object... facts)
			throws EntryDefinitionNotFoundException;

	public void calculatePayment(MunicipalBond municipalBond, Date paymentDate,
			Deposit deposit, boolean isNew, boolean isEmission,
			boolean applyDiscounts, 
			List<TaxRate> taxRatesActives,
			Object... facts) throws EntryDefinitionNotFoundException;  //1 desde vista cajero

	MunicipalBond findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(
			Long residentId, Long entryId, Date serviceDate, String groupingCode);

	public void createItemsToMunicipalBond(List<MunicipalBond> bonds,
			BigDecimal amount, boolean isNew, boolean isEmission,
			boolean internalTramit) throws EntryDefinitionNotFoundException;

	public void createItemsToMunicipalBond(MunicipalBond municipalBond,
			EntryValueItem entryValueItem, boolean isNew, boolean isEmission,
			Object... facts) throws EntryDefinitionNotFoundException;

	public void changeExemptionInMunicipalBond(MunicipalBond municipalBond,
			boolean isNew, boolean isEmission, 
			Object... facts)
			throws EntryDefinitionNotFoundException;

	List<MunicipalBond> findPendingsByGroupingCode(String groupingCode);

	MunicipalBond save(MunicipalBond municipalBond) throws Exception;

	MunicipalBond update(MunicipalBond municipalBond) throws Exception;

	PropertyAppraisal update(PropertyAppraisal propertyAppraisal)
			throws Exception;

	public void updateGroupingCode(List<Long> bondsNumbers, String groupingCode)
			throws Exception;

	List<MunicipalBond> findByResidentIdAndTypeAndStatus(Long residentId,
			MunicipalBondType municipalBondType, Long... municipalBondStatusIds);

	// void calculate(MunicipalBond mb);
	//macartuche
	//agregado la fecha de creacion
	public List<Exemption> findExemptionsByFiscalPeriod(Long fiscalPeriodId, Date fromDate, Date untilDate);

	public TaxpayerRecord findTaxpayerRecord(Long entryId);

	public TaxpayerRecord findDefaultInstitution();

	public MunicipalBond createInstance(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entry, BigDecimal base,
			Date creationDate, Date emissionDate, Date serviceDate,
			Date expirationDate, String reference, String description,
			TaxpayerRecord institution);

	/*
	 * public void createPaymentItems(MunicipalBond municipalBond, Date
	 * serviceDate, Object... facts) throws EntryDefinitionNotFoundException;
	 */
	/*
	 * public MunicipalBond createDeferredMunicipalBond(Resident resident,
	 * FiscalPeriod fiscalPeriod, Entry entry, BigDecimal base, Date
	 * serviceDate, Date expirationDate, String reference, String description,
	 * Object... facts) throws EntryDefinitionNotFoundException;
	 */

	public List<MunicipalBond> findMunicipalBonds(Long residentId,
			Long entryId, Date startDate, Date endDate,
			Long municipalBondStatusId, Integer firstRow, Integer numberOfRows,
			Long municipalBondNumber);

	public List<MunicipalBond> findMunicipalBonds(Long residentId,
			Long entryId, Long municipalBondStatusId, Integer firstRow,
			Integer numberOfRows);
	
	public List<MunicipalBond> findMunicipalBondsFormalizer(Long municipalBondStatusId, Date now, Integer firstRow,
			Integer numberOfRows);

	public Long findMunicipalBondsNumber(Long residentId, Long entryId,
			Date startDate, Date endDate, Long municipalBondStatusId,
			Long municipalBondNumber);
	
	public Long findMunicipalBondsNumber(Long residentId, Long entryId,
		   Long municipalBondStatusId);
	
	public Long findMunicipalBondsNumberFormalizer(Long municipalBondStatusId, Date now);
	
	//macartuche
	//para abonos
	public BigDecimal calculateDiscount(MunicipalBond municipalBond);
	
	MunicipalBond addChildrenItem(MunicipalBond municipalBond, Date serviceDate,
			EntryStructureType entryStructureType,
			boolean isEmission, Boolean internalTramit, BigDecimal value) throws EntryDefinitionNotFoundException;
}
