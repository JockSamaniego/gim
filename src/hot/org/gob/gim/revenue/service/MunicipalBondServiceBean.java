package org.gob.gim.revenue.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.InterestPublicInstitutionUtils;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.FiscalPeriodService;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.service.TaxRateService;
import org.gob.gim.income.service.TaxService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.LegalEntity;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.CompensationReceipt;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.InterestRate;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.gim.income.model.Tax;
import ec.gob.gim.income.model.TaxItem;
import ec.gob.gim.income.model.TaxRate;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.revenue.model.AuxCreateItem;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryDefinition;
import ec.gob.gim.revenue.model.EntryDefinitionType;
import ec.gob.gim.revenue.model.EntryStructure;
import ec.gob.gim.revenue.model.EntryStructureType;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.TimeToCalculate;
import ec.gob.gim.revenue.model.adjunct.PropertyAppraisal;
import ec.gob.gim.security.model.MunicipalbondAux;

/**
 * 
 * @author wilman y GADM-L
 */
@Stateless(name = "MunicipalBondService")
public class MunicipalBondServiceBean implements MunicipalBondService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@EJB
	SystemParameterService systemParameterService;

	@EJB
	EntryService entryService;

	@EJB
	FiscalPeriodService fiscalPeriodService;

	@EJB
	TaxRateService taxRateService;

	@EJB
	TaxService taxService;

	@EJB
	ResidentService residentService;

	public static final String PENDING_BOND_STATUS = "MUNICIPAL_BOND_STATUS_ID_PENDING";

	// @author macartuche
	// @date 2016-06-27 11:42
	// @tag InteresCeroInstPub
	// No realizar el calculo de interes para instituciones publicas
	public static final String COLLECT_INTEREST_PUBLIC_COMPANY = "INTEREST_PUBLIC_COMPANY_EQ_CERO";

	private static final String COUNT_EJBQL = "SELECT count(municipalBond) "
			+ "FROM MunicipalBond municipalBond " + " WHERE ";

	private static final String EJBQL = "SELECT municipalBond "
			+ "FROM MunicipalBond municipalBond "
			+ "LEFT JOIN FETCH municipalBond.resident resident "
			+ "LEFT JOIN FETCH municipalBond.municipalBondStatus municipalBondStatus "
			+ "LEFT JOIN FETCH municipalBond.entry entry "
			+ "LEFT JOIN FETCH municipalBond.receipt receipt " + " WHERE ";

	private static String JOIN_CLAUSE = " AND ";

	@SuppressWarnings("unused")
	private List<Long> getEntryIds(List<Entry> entries) {
		List<Long> longs = new ArrayList<Long>();
		for (Entry entry : entries)
			longs.add(entry.getId());
		return longs;
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findPendingsByGroupingCode(String groupingCode) {
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		Long PENDING_STATUS_ID = systemParameterService
				.findParameter(PENDING_BOND_STATUS);
		parameters.put("groupingCode", groupingCode);
		parameters.put("statusId", PENDING_STATUS_ID);
		List<MunicipalBond> list = crudService.findWithNamedQuery(
				"MunicipalBond.findByStatusIdAndGroupingCode", parameters);
		return list;
	}

	public MunicipalBond findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(
			Long residentId, Long entryId, Date serviceDate, String groupingCode) {
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("residentId", residentId);
		parameters.put("entryId", entryId);
		parameters.put("serviceDate", serviceDate);
		parameters.put("groupingCode", groupingCode);
		List<?> list = crudService
				.findWithNamedQuery(
						"MunicipalBond.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode",
						parameters);
		if (list != null && list.size() > 0)
			return (MunicipalBond) list.get(0);
		return null;
	}

	private Date calculateExpirationDate(Date date, Integer daysNumber) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DATE, daysNumber);
		return now.getTime();
	}

	public MunicipalBond createInstance(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entry, BigDecimal base,
			Date creationDate, Date emissionDate, Date serviceDate,
			Date expirationDate, String reference, String description,
			TaxpayerRecord institution) {

		if (institution == null)
			institution = findTaxpayerRecord(entry.getId());

		MunicipalBond municipalBond = new MunicipalBond();

		municipalBond.setInstitution(institution);
		municipalBond.setResident(resident);
		municipalBond.setFiscalPeriod(fiscalPeriod);
		municipalBond.setEntry(entry);
		municipalBond.setCreationDate(creationDate);
		municipalBond.setCreationTime(creationDate);
		municipalBond.setEmisionDate(emissionDate);
		municipalBond.setEmisionTime(emissionDate);
		municipalBond.setServiceDate(serviceDate);
		municipalBond.setExpirationDate(expirationDate);
		municipalBond.setReference(reference);
		municipalBond.setDescription(description);

		municipalBond.setBase(base);

		municipalBond.setTimePeriod(entry.getTimePeriod());

		return municipalBond;
	}

	public MunicipalBond createMunicipalBond(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entry,
			EntryValueItem entryValueItem, boolean isEmission,
			boolean applyDiscounts, Object... facts)
			throws EntryDefinitionNotFoundException {

		Date now = Calendar.getInstance().getTime();
		Date creationDate = now;
		Date emissionDate = now;

		Date serviceDate = entryValueItem.getServiceDate();
		String reference = entryValueItem.getReference();
		String description = entryValueItem.getDescription();
		BigDecimal base = entryValueItem.getMainValue();
		Date expirationDate = null;
		boolean aux = false;
		if (entryValueItem.getExpirationDate() == null) {
			expirationDate = calculateExpirationDate(serviceDate, entry
					.getTimePeriod().getDaysNumber());
		} else {
			aux = true;
			expirationDate = entryValueItem.getExpirationDate();
		}

		MunicipalBond municipalBond = createInstance(resident, fiscalPeriod,
				entry, base, creationDate, emissionDate, serviceDate,
				expirationDate, reference, description, null);

		municipalBond.setIsExpirationDateDefined(aux);
		municipalBond.setExempt(entryValueItem.isExempt());
		municipalBond.setInternalTramit(entryValueItem.getInternalTramit());
		municipalBond.setPreviousPayment(entryValueItem.getPreviousPayment());
		municipalBond.setIsNoPasiveSubject(entryValueItem.isNoPasiveSubject());
		municipalBond
				.setPreviousPayment(entryValueItem.getPreviousPayment() != null ? entryValueItem
						.getPreviousPayment() : BigDecimal.ZERO);
		createItemsToMunicipalBond(municipalBond, entryValueItem, true,
				isEmission, facts);
		if (aux)
			municipalBond.setExpirationDate(expirationDate);
		calculatePayment(municipalBond, now, null, true, isEmission,
				applyDiscounts, null, facts);
		return municipalBond;
	}

	@SuppressWarnings("unchecked")
	@Override
	// iva12%
	public void calculatePayment(MunicipalBond municipalBond, Date paymentDate,
			Deposit deposit, boolean isNew, boolean isEmission,
			boolean applyDiscounts, List<TaxRate> taxRatesActives,
			Object... facts) throws EntryDefinitionNotFoundException {

		// System.out.println("<<<R>>>calculatePayment: \n\n\n\n\n");
		if (!isNew) {
			EntryValueItem entryValueItem = new EntryValueItem();
			entryValueItem.setMainValue(municipalBond.getBase());
			entryValueItem.setServiceDate(municipalBond.getServiceDate());
			entryValueItem.setAmount(municipalBond.getItems().get(0)
					.getAmount());
			entryValueItem.setInternalTramit(municipalBond.getInternalTramit());
			createItemsToMunicipalBond(municipalBond, entryValueItem, isNew,
					isEmission, facts);
		}
		createTaxItems(municipalBond, paymentDate, taxRatesActives);
		municipalBond.calculateValue();
		if (municipalBond.getId() == null) {
			municipalBond.setBalance(municipalBond.getValue());
		}
		if (deposit == null) {
			municipalBond.setBalance(municipalBond.getValue());
		}
		// municipalBond.setBalance(municipalBond.getBalance().add(municipalBond.getSurcharge()).subtract(municipalBond.getDiscount()));
		calculateInterest(municipalBond, deposit, paymentDate);// luego de
																// pruebas de
																// coactivas y
																// de interes
																// cero remover
																// ******

		// @author macartuche
		// @date 2016-06-27 10:33
		// @tag InteresCeroInstPub
		// No realizar el calculo de interes para instituciones publicas
		/*
		 * Boolean interestCero_publiCompany =
		 * systemParameterService.findParameter
		 * (COLLECT_INTEREST_PUBLIC_COMPANY); if(interestCero_publiCompany){
		 * boolean isPublic =
		 * InterestPublicInstitutionUtils.isPublicInstitution(
		 * municipalBond.getResident().getId()); if(isPublic){
		 * System.out.println("Number: "+municipalBond.getNumber()+" Service: "+
		 * municipalBond.getGroupingCode()+ " Interes anterior calculado "+
		 * municipalBond.getInterest()+ " nuevo interes: 0.00");
		 * municipalBond.setInterest(BigDecimal.ZERO);
		 * //if(municipalBond.getReceipt()!=null){ // Receipt receipt =
		 * municipalBond.getReceipt(); // Query compensationQuery =
		 * entityManager.createQuery(
		 * "Select cr from CompensationReceipt cr where cr.receipt.id=:receiptid"
		 * ); // compensationQuery.setParameter("receiptid", receipt.getId());
		 * // List<CompensationReceipt> list =
		 * compensationQuery.getResultList(); // if(!list.isEmpty()){ //
		 * CompensationReceipt cr = list.get(0); //
		 * System.out.println("Interes CompensationReceipt "+cr.getInterest());
		 * // if(cr.getInterest().compareTo(BigDecimal.ZERO)==1){ //
		 * municipalBond.setInterest(cr.getInterest()); //
		 * System.out.println("=======================>"
		 * +receipt.toString()+" interest CR: "+cr.getInterest()); // } // } //}
		 * }else{ calculateInterest(municipalBond, deposit, paymentDate); }
		 * }else{ calculateInterest(municipalBond, deposit, paymentDate); }
		 */

		roundItems(municipalBond);
		// /ya tiene los 48.10
		BigDecimal paidTotal = municipalBond.getBalance();
		paidTotal = paidTotal.add(municipalBond.getSurcharge());

		// @tag recaudacionCoativas
		if (!applyDiscounts) {
			// if (applyDiscounts) {
			municipalBond.setDiscount(BigDecimal.ZERO);
		} else {
			paidTotal = paidTotal.subtract(municipalBond.getDiscount());
		}
		paidTotal = paidTotal.add(municipalBond.getInterest());
		paidTotal = paidTotal.add(municipalBond.getTaxesTotal());
		// paidTotal =
		// paidTotal.subtract(municipalBond.getPreviousPayment()!=null ?
		// municipalBond.getPreviousPayment() : BigDecimal.ZERO);
		municipalBond.setPaidTotal(paidTotal);
	}

	private List<AuxCreateItem> findByStructuresType(MunicipalBond mb,
			Date serviceDate, EntryStructureType entryStructureType,
			List<byte[]> rulesToApply, boolean isEmission)
			throws EntryDefinitionNotFoundException {

		List<AuxCreateItem> list = new ArrayList<AuxCreateItem>();

		List<EntryStructure> entryStructureChildren = this.entryService
				.findEntryStructureChildrenByType(mb.getEntry().getId(),
						entryStructureType);

		for (EntryStructure e : entryStructureChildren) {
			if (e.getChild().getTimeToCalculate() == null
					|| (e.getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_EMISSION) && isEmission)
					|| (e.getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_PAYMENT) && !isEmission)) {
				AuxCreateItem aux = new AuxCreateItem();
				aux.setEntryStructure(e);
				EntryDefinition edchild = this.entryService
						.findEntryDefinitionCurrentToEntry(
								e.getChild().getId(), serviceDate);
				aux.setCurrentEntryDefinition(edchild);
				aux.setEntryStructureType(entryStructureType);
				if (edchild != null
						&& edchild.getEntryDefinitionType() == EntryDefinitionType.RULE) {
					rulesToApply.add(edchild.getRule().getBytes());
				}
				list.add(aux);
			}
		}
		return list;
	}

	private void addOnlyChildrenItems(MunicipalBond mb, Date serviceDate,
			List<AuxCreateItem> aux, List<Item> itemFacts, boolean isEmission,
			Boolean internalTramit) throws EntryDefinitionNotFoundException {

		for (AuxCreateItem ac : aux) {
			if (ac.getEntryStructure().getChild().getTimeToCalculate() == null
					|| (ac.getEntryStructure().getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_EMISSION) && isEmission)
					|| (ac.getEntryStructure().getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_PAYMENT) && !isEmission)) {

				EntryDefinition edchild = ac.getCurrentEntryDefinition();
				internalTramit = (internalTramit == null) ? false
						: internalTramit;
				Boolean emitOnInternal = (edchild.getEntry()
						.getEmitOnInternal() == null ? false : edchild
						.getEntry().getEmitOnInternal());

				if (!internalTramit || (internalTramit && emitOnInternal)) {
					Item saved = containItem(mb, edchild.getEntry());
					saved = buildItem(edchild, new EntryValueItem(), ac
							.getEntryStructure().getChild().getIsTaxable(), ac
							.getEntryStructure().getTargetEntry(), null,
							itemFacts, saved, false);
					saved.setEntry(edchild.getEntry());
					addItem(mb, saved, ac.getEntryStructureType());
					// System.out.println("LXGK -----> ITEM ADDED");
				}
			}
		}
	}

	private void addChildrenItems(MunicipalBond municipalBond,
			Date serviceDate, EntryStructureType entryStructureType,
			List<byte[]> rulesToApply, List<Item> itemFacts,
			boolean isEmission, Boolean internalTramit)
			throws EntryDefinitionNotFoundException {
		// System.out.println("LXGK -----> "+entryStructureType);
		List<EntryStructure> entryStructureChildren = this.entryService
				.findEntryStructureChildrenByType(municipalBond.getEntry()
						.getId(), entryStructureType);
		// System.out.println("LXGK -----> "+(entryStructureChildren!=null ?
		// entryStructureChildren.size() : "Null"));
		for (EntryStructure e : entryStructureChildren) {
			if (e.getChild().getTimeToCalculate() == null
					|| (e.getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_EMISSION) && isEmission)
					|| (e.getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_PAYMENT) && !isEmission)) {
				EntryDefinition edchild = this.entryService
						.findEntryDefinitionCurrentToEntry(
								e.getChild().getId(), serviceDate);
				internalTramit = (internalTramit == null) ? false
						: internalTramit;
				Boolean emitOnInternal = (edchild.getEntry()
						.getEmitOnInternal() == null ? false : edchild
						.getEntry().getEmitOnInternal());
				if (!internalTramit || (internalTramit && emitOnInternal)) {
					Item saved = containItem(municipalBond, edchild.getEntry());
					saved = buildItem(edchild, new EntryValueItem(), e
							.getChild().getIsTaxable(), e.getTargetEntry(),
							rulesToApply, itemFacts, saved, true);
					saved.setEntry(edchild.getEntry());
					addItem(municipalBond, saved, entryStructureType);
					// System.out.println("LXGK -----> ITEM ADDED");
				}
			}
		}
	}

	public MunicipalBond addChildrenItem(MunicipalBond municipalBond,
			Date serviceDate, EntryStructureType entryStructureType, boolean isEmission, Boolean internalTramit, BigDecimal value)
			throws EntryDefinitionNotFoundException {
		// System.out.println("LXGK -----> "+entryStructureType);
		List<EntryStructure> entryStructureChildren = this.entryService
				.findEntryStructureChildrenByType(municipalBond.getEntry()
						.getId(), entryStructureType);
		// System.out.println("LXGK -----> "+(entryStructureChildren!=null ?
		// entryStructureChildren.size() : "Null"));
		for (EntryStructure e : entryStructureChildren) {
			if (e.getChild().getTimeToCalculate() == null
					|| (e.getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_EMISSION) && isEmission)
					|| (e.getChild().getTimeToCalculate()
							.equals(TimeToCalculate.IN_PAYMENT) && !isEmission)) {
				EntryDefinition edchild = this.entryService
						.findEntryDefinitionCurrentToEntry(
								e.getChild().getId(), serviceDate);
				internalTramit = (internalTramit == null) ? false
						: internalTramit;
				Boolean emitOnInternal = (edchild.getEntry()
						.getEmitOnInternal() == null ? false : edchild
						.getEntry().getEmitOnInternal());
				if (!internalTramit || (internalTramit && emitOnInternal)) {
					Item saved = containItem(municipalBond, edchild.getEntry());

					/*
					 * EntryDefinition entryDefinition, EntryValueItem
					 * entryValueItem, Boolean isTaxable, Entry targetEntry,
					 * List<Item> itemFacts, Item item
					 */

					saved = buildItemWithoutExecutingRules(edchild,
							new EntryValueItem(), e.getChild().getIsTaxable(),
							e.getTargetEntry(), saved, value, municipalBond);
					saved.setEntry(edchild.getEntry());
					addItem(municipalBond, saved, entryStructureType);
					// System.out.println("LXGK -----> ITEM ADDED");
				}
			}
		}

		return municipalBond;
	}

	private void addItem(MunicipalBond municipalBond, Item item,
			EntryStructureType entryStructureType) {
		// System.out.println("MunicipalBondService: Added item of type
		// "+entryStructureType.name());
		if (entryStructureType == EntryStructureType.NORMAL) {
			// System.out.println("MunicipalBondService: NORMAL ADDED");
			if (item.getId() == null) {
				Integer orderNumber = municipalBond.getItems().size() + 1;
				item.setOrderNumber(orderNumber);
			}
			municipalBond.add(item);

		} else if (entryStructureType == EntryStructureType.DISCOUNT) {
			// System.out.println("MunicipalBondService: DISCOUNT ADDED");
			if (item.getId() == null) {
				Integer orderNumber = municipalBond.getDiscountItems().size() + 1;
				item.setOrderNumber(orderNumber);
			}
			municipalBond.addDiscountItem(item);

		} else {
			// System.out.println("MunicipalBondService: SURCHARGE ADDED");
			if (item.getId() == null) {
				Integer orderNumber = municipalBond.getSurchargeItems().size() + 1;
				item.setOrderNumber(orderNumber);
			}
			municipalBond.addSurchargeItem(item);
		}
	}

	private Item containItem(MunicipalBond mb, Entry itemEntry) {
		for (Item i : mb.getItems()) {
			if (i.getEntry().equals(itemEntry))
				return i;
		}

		for (Item i : mb.getSurchargeItems()) {
			if (i.getEntry().equals(itemEntry))
				return i;
		}

		for (Item i : mb.getDiscountItems()) {
			if (i.getEntry().equals(itemEntry))
				return i;
		}

		return null;
	}

	public void createItemsToMunicipalBond(List<MunicipalBond> bonds,
			BigDecimal amount, boolean isNew, boolean isEmission,
			boolean internalTramit) throws EntryDefinitionNotFoundException {

		if (bonds == null || bonds.size() == 0)
			return;
		MunicipalBond municipalBond = bonds.get(0);
		Date now = Calendar.getInstance().getTime();
		if (municipalBond.getEntry() != null) {
			List<byte[]> rulesToApply = new ArrayList<byte[]>();

			Entry entry = municipalBond.getEntry();
			Long entryId = entry.getId();
			EntryDefinition entryDefinition = this.entryService
					.findEntryDefinitionCurrentToEntry(entryId,
							municipalBond.getServiceDate());

			loadRuleMainItem(entryDefinition, rulesToApply);

			List<AuxCreateItem> normal = findByStructuresType(municipalBond,
					municipalBond.getServiceDate(), EntryStructureType.NORMAL,
					rulesToApply, isEmission);
			List<AuxCreateItem> discount = findByStructuresType(municipalBond,
					municipalBond.getServiceDate(),
					EntryStructureType.DISCOUNT, rulesToApply, isEmission);
			List<AuxCreateItem> surcharge = findByStructuresType(municipalBond,
					municipalBond.getServiceDate(),
					EntryStructureType.SURCHARGE, rulesToApply, isEmission);

			List<TaxRate> activeTaxRates = findActiveTaxRates(municipalBond,
					now);

			List<MunicipalBond> bondsWithValueZero = new ArrayList<MunicipalBond>();

			for (MunicipalBond mb : bonds) {
				List<Item> itemFacts = new ArrayList<Item>();
				mb.setApplyInterest(entryDefinition.getApplyInterest());

				if (isNew) {
					EntryValueItem entryValueItem = new EntryValueItem();
					entryValueItem.setAmount(amount);
					entryValueItem.setMainValue(mb.getBase());
					Item mainItem = buildItem(entryDefinition, entryValueItem,
							entry.getIsTaxable(), null, rulesToApply,
							itemFacts, null, false);
					mainItem.setEntry(entry);
					mainItem.setOrderNumber(1);
					mb.add(mainItem);
				}

				addOnlyChildrenItems(mb, mb.getServiceDate(), normal,
						itemFacts, isEmission, internalTramit);
				addOnlyChildrenItems(mb, mb.getServiceDate(), discount,
						itemFacts, isEmission, internalTramit);
				addOnlyChildrenItems(mb, mb.getServiceDate(), surcharge,
						itemFacts, isEmission, internalTramit);

				LegalEntity legalEntity = null;

				Resident resident = mb.getResident();

				if (resident instanceof LegalEntity)
					legalEntity = ((LegalEntity) resident);

				invokeRules(mb, rulesToApply, itemFacts, mb.getAdjunct(),
						legalEntity);
				roundItems(mb);
				mb.setDiscount(calculateDiscount(mb));
				mb.setSurcharge(calculateSurcharge(mb));
				mb.calculateValue();
				if (isNew
						&& mb.getPreviousPayment() != null
						&& mb.getPreviousPayment().compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal cienBd = new BigDecimal(100);
					for (Item i : mb.getItems()) {
						BigDecimal percent = new BigDecimal(0.0);
						percent = i.getTotal().multiply(cienBd)
								.divide(mb.getValue(), RoundingMode.HALF_EVEN);
						BigDecimal difference = new BigDecimal(0.0);
						difference = mb.getPreviousPayment().multiply(percent)
								.divide(cienBd)
								.setScale(2, RoundingMode.HALF_EVEN);
						if (difference.compareTo(i.getTotal()) <= 0) {
							i.setTotal(i.getTotal().subtract(difference));
						}
					}

					// Item mainItem = mb.getItems().get(0);
					// mainItem.setTotal(mainItem.getTotal().subtract(mb.getPreviousPayment()));
				}

				calculatePayment(mb, now, null, true, isEmission, true,
						activeTaxRates, itemFacts);

				if (mb.getPaidTotal().compareTo(BigDecimal.ZERO) < 1) {
					bondsWithValueZero.add(mb);
				}

			}

			bonds.removeAll(bondsWithValueZero);
		}
	}

	/**
	 * Emitir las recaudadoras venta de humus
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Exemption> findExemptionsByFiscalPeriod(Long fiscalPeriodId,
			Date fromDate, Date untilDate) {
		Query query = entityManager
				.createNamedQuery("Exemption.findByFiscalPeriodAndActiveAndDate");
		query.setParameter("fiscalPeriodId", fiscalPeriodId);
		query.setParameter("start", fromDate);
		query.setParameter("end", untilDate);
		return query.getResultList();
	}

	public void changeExemptionInMunicipalBond(MunicipalBond municipalBond,
			boolean isNew, boolean isEmission, Object... facts)
			throws EntryDefinitionNotFoundException {
		if (municipalBond.getEntry() != null) {

			List<byte[]> rulesToApply = new ArrayList<byte[]>();
			// System.out.println("=======> OBTIENE LOS ITEMS");
			List<Item> itemFacts = municipalBond.getItems();

			Date serviceDate = municipalBond.getServiceDate();
			// System.out.println("=======>serviceDate: " + serviceDate);

			// Crea item principal
			Entry entry = municipalBond.getEntry();
			Long entryId = entry.getId();
			EntryDefinition entryDefinition = this.entryService
					.findEntryDefinitionCurrentToEntry(entryId, serviceDate);
			rulesToApply.add(entryDefinition.getRule().getBytes());

			invokeRules(municipalBond, rulesToApply, itemFacts, facts);
			roundItems(municipalBond);
			municipalBond.setDiscount(calculateDiscount(municipalBond));
			municipalBond.setSurcharge(calculateSurcharge(municipalBond));
			calculatePayment(municipalBond, municipalBond.getEmisionDate(),
					null, false, isEmission, true, null, facts);

			// if(isNew && municipalBond.getPreviousPayment() != null &&
			// municipalBond.getPreviousPayment().compareTo(BigDecimal.ZERO) > 0
			// ){
			// BigDecimal cienBd = new BigDecimal(100);
			// for (Item i : municipalBond.getItems()){
			// BigDecimal percent = new BigDecimal(0.0);
			// percent =
			// i.getTotal().multiply(cienBd).divide(municipalBond.getValue(),
			// RoundingMode.HALF_EVEN);
			// BigDecimal difference = new BigDecimal(0.0);
			// difference =
			// municipalBond.getPreviousPayment().multiply(percent).divide(cienBd).setScale(2,
			// RoundingMode.HALF_EVEN);
			// i.setTotal(i.getTotal().subtract(difference));
			// }
			// }
		}
	}

	public void createItemsToMunicipalBond(MunicipalBond municipalBond,
			EntryValueItem entryValueItem, boolean isNew, boolean isEmission,
			Object... facts) throws EntryDefinitionNotFoundException {
		if (municipalBond.getEntry() != null) {

			Date serviceDate = entryValueItem.getServiceDate();
			Boolean internalTramit = entryValueItem.getInternalTramit();

			List<byte[]> rulesToApply = new ArrayList<byte[]>();
			List<Item> itemFacts = new ArrayList<Item>();

			// Crea item principal
			Entry entry = municipalBond.getEntry();
			Long entryId = entry.getId();
			EntryDefinition entryDefinition = this.entryService
					.findEntryDefinitionCurrentToEntry(entryId, serviceDate);
			municipalBond.setApplyInterest(entryDefinition.getApplyInterest());

			if (isNew) {
				Item mainItem = buildItem(entryDefinition, entryValueItem,
						entry.getIsTaxable(), null, rulesToApply, itemFacts,
						null, true);
				mainItem.setEntry(entry);
				mainItem.setOrderNumber(1);
				municipalBond.add(mainItem);
			}

			addChildrenItems(municipalBond, serviceDate,
					EntryStructureType.NORMAL, rulesToApply, itemFacts,
					isEmission, internalTramit);
			addChildrenItems(municipalBond, serviceDate,
					EntryStructureType.DISCOUNT, rulesToApply, itemFacts,
					isEmission, internalTramit);
			addChildrenItems(municipalBond, serviceDate,
					EntryStructureType.SURCHARGE, rulesToApply, itemFacts,
					isEmission, internalTramit);

			// Aplica las reglas
			invokeRules(municipalBond, rulesToApply, itemFacts, facts);
			roundItems(municipalBond);
			municipalBond.setDiscount(calculateDiscount(municipalBond));
			municipalBond.setSurcharge(calculateSurcharge(municipalBond));
			municipalBond.calculateValue();
			if (isNew
					&& municipalBond.getPreviousPayment() != null
					&& municipalBond.getPreviousPayment().compareTo(
							BigDecimal.ZERO) > 0) {
				BigDecimal cienBd = new BigDecimal(100);
				for (Item i : municipalBond.getItems()) {
					BigDecimal percent = new BigDecimal(0.0);
					percent = i
							.getTotal()
							.multiply(cienBd)
							.divide(municipalBond.getValue(),
									RoundingMode.HALF_EVEN);
					BigDecimal difference = new BigDecimal(0.0);
					difference = municipalBond.getPreviousPayment()
							.multiply(percent).divide(cienBd)
							.setScale(2, RoundingMode.HALF_EVEN);
					if (difference.compareTo(i.getTotal()) <= 0) {
						i.setTotal(i.getTotal().subtract(difference));
					}
				}

				// Item mainItem = municipalBond.getItems().get(0);
				// mainItem.setTotal(mainItem.getTotal().subtract(municipalBond.getPreviousPayment()));
			}
		}
	}

	/*
	 * public void createItemToMunicipalBond(MunicipalBond municipalBond,
	 * EntryValueItem entryValueItem, boolean isNew, boolean isEmission,
	 * Object... facts) throws EntryDefinitionNotFoundException { if
	 * (municipalBond.getEntry() != null) {
	 * 
	 * Date serviceDate = entryValueItem.getServiceDate(); Boolean
	 * internalTramit = entryValueItem.getInternalTramit();
	 * 
	 * List<Item> itemFacts = new ArrayList<Item>();
	 * 
	 * // Crea item principal Entry entry = municipalBond.getEntry(); Long
	 * entryId = entry.getId(); EntryDefinition entryDefinition =
	 * this.entryService.findEntryDefinitionCurrentToEntry(entryId,
	 * serviceDate);
	 * municipalBond.setApplyInterest(entryDefinition.getApplyInterest());
	 * 
	 * if (isNew) { Item mainItem = buildItem(entryDefinition, entryValueItem,
	 * entry.getIsTaxable(), null, rulesToApply, itemFacts, null, true);
	 * mainItem.setEntry(entry); mainItem.setOrderNumber(1);
	 * municipalBond.add(mainItem); }
	 * 
	 * addChildrenItems(municipalBond, serviceDate,
	 * EntryStructureType.SURCHARGE, rulesToApply, itemFacts, isEmission,
	 * internalTramit);
	 * 
	 * // Aplica las reglas invokeRules(municipalBond, rulesToApply, itemFacts,
	 * facts); roundItems(municipalBond);
	 * municipalBond.setDiscount(calculateDiscount(municipalBond));
	 * municipalBond.setSurcharge(calculateSurcharge(municipalBond));
	 * municipalBond.calculateValue(); if (isNew &&
	 * municipalBond.getPreviousPayment() != null &&
	 * municipalBond.getPreviousPayment().compareTo(BigDecimal.ZERO) > 0) {
	 * BigDecimal cienBd = new BigDecimal(100); for (Item i :
	 * municipalBond.getItems()) { BigDecimal percent = new BigDecimal(0.0);
	 * percent = i.getTotal().multiply(cienBd).divide(municipalBond.getValue(),
	 * RoundingMode.HALF_EVEN); BigDecimal difference = new BigDecimal(0.0);
	 * difference =
	 * municipalBond.getPreviousPayment().multiply(percent).divide(cienBd
	 * ).setScale(2, RoundingMode.HALF_EVEN); if
	 * (difference.compareTo(i.getTotal()) <= 0) {
	 * i.setTotal(i.getTotal().subtract(difference)); } }
	 * 
	 * // Item mainItem = municipalBond.getItems().get(0); //
	 * mainItem.setTotal(mainItem
	 * .getTotal().subtract(municipalBond.getPreviousPayment())); } } }
	 */

	public void roundItems(MunicipalBond municipalBond) {
		for (Item i : municipalBond.getItems()) {
			i.setTotal(i.getTotal().setScale(2, RoundingMode.HALF_UP));
		}
	}

	private void loadRuleMainItem(EntryDefinition entryDefinition,
			List<byte[]> rulesToApply) {
		if (entryDefinition != null
				&& entryDefinition.getEntryDefinitionType() == EntryDefinitionType.RULE) {
			rulesToApply.add(entryDefinition.getRule().getBytes());
		}
	}

	/**
	 * Se considera que existira un unico item de cada tipo en el grupo
	 * 
	 * @param ed
	 * @param amount
	 * @param value
	 * @param isTaxable
	 * @param targetEntry
	 * @return
	 */
	private Item buildItem(EntryDefinition entryDefinition,
			EntryValueItem entryValueItem, Boolean isTaxable,
			Entry targetEntry, List<byte[]> rulesToApply, List<Item> itemFacts,
			Item item, boolean addRules) {
		Entry entry = entryDefinition.getEntry();
		BigDecimal amount = entryValueItem.getAmount();
		BigDecimal value = entryValueItem.getMainValue();
		if (entryValueItem.getResetValue()) {
			if (entry.getIsValueEditable() != null
					&& entry.getIsValueEditable()) {
				amount = BigDecimal.ONE;
			} else {
				if (entry.getIsAmountEditable()) {
					value = BigDecimal.ONE;
				} else {
					// Permitir que el valor se coloque directamente de acuerdo
					// a lo definido en EntryDefinition
					amount = BigDecimal.ONE;
					value = BigDecimal.ONE;
				}
			}

			if (amount == null) {
				amount = BigDecimal.ONE;
			}

			if (value == null) {
				value = BigDecimal.ONE;
			}

			if (item == null) {
				item = new Item();
				item.setEntry(entry);
				item.setTargetEntry(targetEntry);
			}

			item.setAmount(amount);
			item.setValue(value);

			if (isTaxable != null) {
				item.setIsTaxable(isTaxable);
			}

			if (entryDefinition != null
					&& entryDefinition.getEntryDefinitionType() == EntryDefinitionType.RULE) {
				if (addRules)
					rulesToApply.add(entryDefinition.getRule().getBytes());
				itemFacts.add(item);
				// System.out.println("RULES DEBUG -----> ADDED FACT " +
				// item.getEntry().getName());
			} else {
				BigDecimal entryDefinitionValue = entryDefinition.getValue();
				BigDecimal factor = new BigDecimal(entryDefinition.getFactor());
				BigDecimal calculatedValue = value.multiply(
						entryDefinitionValue).divide(factor);
				BigDecimal total = amount.multiply(calculatedValue);
				item.setTotal(total.setScale(2, RoundingMode.HALF_UP));
				item.setValue(calculatedValue.setScale(2, RoundingMode.HALF_UP));
				// System.out.println("RULES DEBUG -----> VALUE APPLIED " +
				// item.getEntry().getName());
			}

		} else {
			if (item == null) {
				item = new Item();
				item.setEntry(entry);
				item.setTargetEntry(targetEntry);
			}

			item.setAmount(amount);
			item.setValue(value);

			if (isTaxable != null) {
				item.setIsTaxable(isTaxable);
			}

			item.setTotal(entryValueItem.getMainValue().setScale(2,
					RoundingMode.HALF_UP));
			item.setValue(entryValueItem.getMainValue().setScale(2,
					RoundingMode.HALF_UP));
		}

		return item;
	}

	private Item buildItemWithoutExecutingRules(
			EntryDefinition entryDefinition, EntryValueItem entryValueItem,
			Boolean isTaxable, Entry targetEntry, Item item, BigDecimal _value, MunicipalBond municipalBond) {
		Entry entry = entryDefinition.getEntry();
		BigDecimal amount = entryValueItem.getAmount();
		BigDecimal value = _value;
		if (entryValueItem.getResetValue()) {
			if (entry.getIsValueEditable() != null
					&& entry.getIsValueEditable()) {
				amount = BigDecimal.ONE;
			} else {
				if (entry.getIsAmountEditable()) {
					value = BigDecimal.ONE;
				} else {
					// Permitir que el valor se coloque directamente de acuerdo
					// a lo definido en EntryDefinition
					amount = BigDecimal.ONE;
					value = BigDecimal.ONE;
				}
			}

			if (amount == null) {
				amount = BigDecimal.ONE;
			}

			if (value == null) {
				value = BigDecimal.ONE;
			}

			if (item == null) {
				item = new Item();
				item.setEntry(entry);
				item.setTargetEntry(targetEntry);
			}

			item.setAmount(amount);
			item.setValue(value);

			if (isTaxable != null) {
				item.setIsTaxable(isTaxable);
			}
			
			BigDecimal total = amount.multiply(_value);

			item.setTotal(total.setScale(2,
					RoundingMode.HALF_UP));
			item.setValue(value.setScale(2,
					RoundingMode.HALF_UP));
			
			if (item.getId() == null) {
				Integer orderNumber = municipalBond.getSurchargeItems().size() + 1;
				item.setOrderNumber(orderNumber);
			}

		} else {
			if (item == null) {
				item = new Item();
				item.setEntry(entry);
				item.setTargetEntry(targetEntry);
			}

			item.setAmount(amount);
			//item.setValue(value);

			if (isTaxable != null) {
				item.setIsTaxable(isTaxable);
			}

			BigDecimal total = amount.multiply(value);
			item.setTotal(total.setScale(2,
					RoundingMode.HALF_UP));
			item.setValue(value.setScale(2,
					RoundingMode.HALF_UP));
			
			if (item.getId() == null) {
				Integer orderNumber = municipalBond.getSurchargeItems().size() + 1;
				item.setOrderNumber(orderNumber);
			}
		}
		
		/*if (item == null) {
			item = new Item();
			item.setEntry(entry);
			item.setTargetEntry(targetEntry);
		}
		
		if (item.getId() == null) {
			Integer orderNumber = municipalBond.getSurchargeItems().size() + 1;
			item.setOrderNumber(orderNumber);
		}
		
		item.setAmount(amount);
		item.setValue(value);

		if (isTaxable != null) {
			item.setIsTaxable(isTaxable);
		}

		item.setTotal(value.setScale(2,
				RoundingMode.HALF_UP));
		item.setValue(value.setScale(2,
				RoundingMode.HALF_UP));*/

		return item;
	}

	private void addFact(Object object,
			org.drools.runtime.StatefulKnowledgeSession session) {
		FactHandle factHandle = session.getFactHandle(object);
		if (factHandle == null) {
			// if(object !=
			// null)System.out.println("RULES DEBUG -----> Inserting fact
			// "+object.getClass().getName());
			factHandle = session.insert(object);
		} else {
			// if(object !=
			// null)System.out.println("RULES DEBUG -----> Updating
			// fact"+object.getClass().getName());
			session.update(factHandle, object);
		}
	}

	public void updateGroupingCode(List<Long> bondsNumbers, String groupingCode) {
		Query q = entityManager
				.createNamedQuery("MunicipalBond.setGroupingCodeByBondNumber");
		q.setParameter("groupingCode", groupingCode);
		q.setParameter("bondsNumbers", bondsNumbers);
		q.executeUpdate();
	}

	private void invokeRules(MunicipalBond municipalBond,
			List<byte[]> rulesToApply, List<Item> itemFacts,
			Object... externalFacts) {
		// System.out.println("RULES DEBUG -----> INVOKE RULES STARTS");

		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		// System.out.println("RULES DEBUG -----> KnowledgeBase
		// "+knowledgeBase);

		StatefulKnowledgeSession session = knowledgeBase
				.newStatefulKnowledgeSession();
		// System.out.println("RULES DEBUG -----> Session "+session);
		// KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);

		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		// System.out.println("RULES DEBUG -----> KnowledgeBuilder
		// "+knowledgeBuilder);

		if (itemFacts != null && itemFacts.size() > 0) {

			// System.out.println("RULES DEBUG -----> itemFacts "+itemFacts+
			// " size "+ itemFacts.size());
			// System.out.println("RULES DEBUG -----> rulesToApply
			// "+rulesToApply+
			// " size "+ rulesToApply.size());

			// Se agregan reglas a la base de conocimiento
			for (byte[] rule : rulesToApply) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					bos.write(rule);
					// System.out.println(bos.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}

				InputStream is = new ByteArrayInputStream(bos.toByteArray());
				knowledgeBuilder.add(
						ResourceFactory.newInputStreamResource(is),
						ResourceType.DRL);
				if (knowledgeBuilder.hasErrors()) {
					// System.out.println("RULES DEBUG -----> WARNING: Added Rule has errors");
					KnowledgeBuilderErrors errors = knowledgeBuilder
							.getErrors();
					Iterator<KnowledgeBuilderError> it = errors.iterator();
					while (it.hasNext()) {
						KnowledgeBuilderError error = it.next();
						// System.out.println("RULES DEBUG -----> error" +
						// error.getMessage() + " " + error.getLines() );
					}
				}
			}
			// System.out.println("RULES DEBUG -----> rulesToApply
			// "+rulesToApply+
			// " size "+ rulesToApply.size());
			knowledgeBase.addKnowledgePackages(knowledgeBuilder
					.getKnowledgePackages());

			// Se recolecta todos los hechos
			List<Object> facts = new ArrayList<Object>();
			facts.addAll(itemFacts);
			if (externalFacts != null)
				facts.addAll(Arrays.asList(externalFacts));
			facts.add(municipalBond);
			if (municipalBond.getAdjunct() != null) {
				facts.add(municipalBond.getAdjunct());
			}
			// System.out.println("RULES DEBUG -----> facts to add "+facts+
			// " size "+ facts.size());
			for (Object fact : facts) {
				addFact(fact, session);
			}

			Collection<FactHandle> factHandles = session.getFactHandles();
			// System.out.println("RULES DEBUG -----> factHandles to apply
			// "+factHandles.size());

			if (factHandles.size() > 0) {
				// System.out.println("RULES DEBUG -----> Firing all rules ");
				int firedRules = session.fireAllRules();
				// System.out.println("RULES DEBUG -----> Rules applied " +
				// firedRules);
			}

			for (FactHandle factHandle : factHandles) {
				session.retract(factHandle);
			}
		}
	}

	public BigDecimal calculateDiscount(MunicipalBond municipalBond) {
		BigDecimal discount = BigDecimal.ZERO;
		for (Item i : municipalBond.getDiscountItems()) {
			i.setTotal(i.getTotal().setScale(2, RoundingMode.HALF_UP));
			BigDecimal aux = i.getTotal();
			discount = discount.add(aux);
		}
		return discount;
	}

	public BigDecimal calculateSurcharge(MunicipalBond municipalBond) {
		BigDecimal surcharge = BigDecimal.ZERO;
		for (Item i : municipalBond.getSurchargeItems()) {
			/*
			 * System.out.println("item: " + i);
			 * System.out.println("item total: " + i.getTotal());
			 * System.out.println("item total: " +
			 * i.getEntry().getCompleteName());
			 */
			i.setTotal(i.getTotal().setScale(2, RoundingMode.HALF_UP));
			BigDecimal aux = i.getTotal();
			surcharge = surcharge.add(aux);
		}
		return surcharge;
	}

	@Override
	public MunicipalBond save(MunicipalBond municipalBond) throws Exception {
		return crudService.create(municipalBond);
	}

	@Override
	public MunicipalBond update(MunicipalBond municipalBond) throws Exception {
		return crudService.update(municipalBond);
	}

	@Override
	public PropertyAppraisal update(PropertyAppraisal propertyAppraisalAdjunct)
			throws Exception {
		return crudService.update(propertyAppraisalAdjunct);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MunicipalBond> findByResidentIdAndTypeAndStatus(
			Long residentId, MunicipalBondType municipalBondType,
			Long... municipalBondStatusIds) {

		List<Long> statuses = Arrays.asList(municipalBondStatusIds);

		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("residentId", residentId);
		parameters.put("municipalBondType", municipalBondType);
		parameters.put("municipalBondStatusIds", statuses);

		return crudService.findWithNamedQuery(
				"MunicipalBond.findByResidentIdAndTypeAndStatus", parameters);
	}

	public TaxpayerRecord findTaxpayerRecord(Long entryId) {
		Query query = entityManager
				.createNamedQuery("Issuer.findActiveTaxpayerRecordByEntryId");
		query.setParameter("entryId", entryId);
		TaxpayerRecord taxpayerRecord = null;
		try {
			taxpayerRecord = (TaxpayerRecord) query.getSingleResult();
		} catch (Exception e) {
			System.out
					.println("WARNING: There is no or there is more than one active taxpayer record for entry "
							+ entryId + " selecting default issuer");
		}

		if (taxpayerRecord == null) {
			taxpayerRecord = findDefaultInstitution();
		}
		return taxpayerRecord;
	}

	public TaxpayerRecord findDefaultInstitution() {
		Query query = entityManager
				.createNamedQuery("TaxpayerRecord.findDefaultTaxpayerRecord");
		TaxpayerRecord institution = (TaxpayerRecord) query.getSingleResult();
		return institution;
	}

	private List<TaxRate> findActiveTaxRates(MunicipalBond municipalBond,
			Date paymentDate) {
		List<Tax> taxes = taxService.findByEntryId(municipalBond.getEntry()
				.getId());
		List<TaxRate> taxRatesActives = new ArrayList<TaxRate>();

		for (Tax tax : taxes) {
			taxRatesActives.addAll(taxRateService
					.findActiveByTaxIdAndPaymentDate(tax.getId(), paymentDate));
		}

		return taxRatesActives;
	}

	private void createTaxItems(MunicipalBond municipalBond, Date paymentDate,
			List<TaxRate> taxRatesActives) {

		// System.out.println(":)============== municipalBond.getItems() ==>" +
		// municipalBond.getItems().size() + " subtotal antes: " +
		// municipalBond.getTaxableTotal());
		municipalBond.calculateTaxableTotal();
		List<Tax> taxes = null;
		// iva 14% a iva12%
		if (taxRatesActives == null) {
			if (municipalBond.getId() == null)
				taxes = taxService.findByEntryId(municipalBond.getEntry()
						.getId());
			else
				taxes = taxService.findByMunicipalBondId(municipalBond.getId());
		}

		// System.out.println(":)============== taxes ==>" + taxes.size() +
		// " subtotal: " + municipalBond.getTaxableTotal());
		if (taxRatesActives == null) {
			for (Tax tax : taxes) {
				taxRatesActives = taxRateService
						.findActiveByTaxIdAndPaymentDate(tax.getId(),
								paymentDate);
				addTaxItems(municipalBond, taxRatesActives);
			}
		} else {
			addTaxItems(municipalBond, taxRatesActives);
		}

		municipalBond.calculateTaxesTotal();

	}

	private void addTaxItems(MunicipalBond municipalBond,
			List<TaxRate> taxRatesActives) {
		for (TaxRate taxRate : taxRatesActives) {
			TaxItem taxItem = getTaxItem(municipalBond, taxRate.getTax());
			taxItem.setAppliedRate(taxRate.getRate());
			taxItem.setAppliedTaxRate(taxRate);
			taxItem.setValue(municipalBond.getTaxableTotal().multiply(
					taxRate.getRate()));
			taxItem.setValue(taxItem.getValue().setScale(2,
					RoundingMode.HALF_UP));
			taxItem.setTax(taxRate.getTax());
			municipalBond.add(taxItem);
		}
	}

	private TaxItem getTaxItem(MunicipalBond municipalBond, Tax tax) {
		for (TaxItem ti : municipalBond.getTaxItems()) {
			if (ti.getTax().getId() == tax.getId()) {
				return ti;
			}
		}
		return new TaxItem();
	}

	private List<Date> findDatesForPaidInterest(Date paymentDate,
			Date expirationDate) {
		// System.out.println("INTERESTS -----> Calculating INTEREST DATES");
		Date truncatedPaymentDate = DateUtils.truncate(paymentDate);
		List<Date> dates = new ArrayList<Date>();
		if (truncatedPaymentDate.after(expirationDate)) {
			Calendar temp = DateUtils.getTruncatedInstance(expirationDate);
			temp.set(Calendar.DAY_OF_MONTH, 1);
			while (truncatedPaymentDate.after(temp.getTime())
					|| truncatedPaymentDate.equals(temp.getTime())) {
				// System.out.println("INTERESTS -----> ADDED DATE
				// "+temp.getTime());
				dates.add(temp.getTime());
				temp.add(Calendar.MONTH, 1);
			}
		}
		// System.out.println("INTERESTS -----> Calculating for "+dates.size());
		/*
		 * Integer indexDelete = null; Date deleteDate= null; if
		 * (!dates.isEmpty()) { Calendar cal = Calendar.getInstance();
		 * cal.setTime(expirationDate); int maxDayExpirationDate =
		 * cal.get(Calendar.DAY_OF_MONTH); int monthExpiration =
		 * cal.get(Calendar.MONTH); for (int i = 0; i < dates.size(); i++) {
		 * 
		 * Calendar aux = Calendar.getInstance(); aux.setTime(dates.get(i)); int
		 * maxDayDate = aux.getActualMaximum(Calendar.DAY_OF_MONTH); int
		 * monthDate = cal.get(Calendar.MONTH); if(maxDayDate ==
		 * maxDayExpirationDate && monthDate == monthExpiration){ indexDelete =
		 * i; deleteDate = dates.get(i); break; }
		 * 
		 * } }
		 * 
		 * if(indexDelete!=null){ dates.remove(deleteDate); }
		 */
		return dates;
	}

	@SuppressWarnings("unchecked")
	private List<InterestRate> findInterestRateByExpirationDate(Date endDate) {
		Map<String, Date> parameters = new HashMap<String, Date>();
		parameters.put("endDate", endDate);
		return crudService.findWithNamedQuery(
				"InterestRate.findByEndDateGreaterThan", parameters);
	}

	@SuppressWarnings("unchecked")
	private void calculateInterest(MunicipalBond municipalBond,
			Deposit lastDeposit, Date paymentDate) {
		// System.out.println("INTERESTS -----> BEGINS=============>>>>>>>>>>>>>1234");
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal balance = municipalBond.getBalance().subtract(
				municipalBond.getDiscount());

		// System.out.println("INTERESTS -----> Apply interest?
		// "+municipalBond.getApplyInterest());
		if (municipalBond.getApplyInterest() != null
				&& municipalBond.getApplyInterest()) {

			Date expirationDate = DateUtils.truncate(municipalBond
					.getExpirationDate());

			if (lastDeposit != null
					&& lastDeposit.getDate().after(expirationDate)) {
				expirationDate = DateUtils.truncate(lastDeposit.getDate());
			}

			// rfam 2018-07-03 se controla la creacion de mb porq no tienen
			// estado inicial
			if (municipalBond.getMunicipalBondStatus() != null) {
				// poner condicion de si tiene convenio de pago
				if (municipalBond.getPaymentAgreement() != null
						|| municipalBond.getMunicipalBondStatus().getId()
								.intValue() == 14) {
					// @author macartuche
					// @date 2016-06-20T16:600:00
					// @tag recaudacionCoactivas
					Query qaux = entityManager
							.createQuery("Select ma from MunicipalbondAux ma where ma.municipalbond.id =:id and "
									+ "ma.status=:status and ma.type=:type order by ma.liquidationDate ");
					qaux.setParameter("id", municipalBond.getId());
					qaux.setParameter("status", "VALID");
					qaux.setParameter("type", "I");

					List<MunicipalbondAux> datalist = qaux.getResultList();
					if (!datalist.isEmpty()) {
						// tomar la ultima instancia almacenada
						int lastIndex = datalist.size() - 1;
						MunicipalbondAux aux = datalist.get(lastIndex);
						if (aux.getCoveritem() != null && !aux.getCoveritem()) {
							expirationDate = DateUtils.truncate(municipalBond
									.getExpirationDate());
							// verficar si ha habido pagos de todo
							Query queryNewInterest = entityManager
									.createQuery("Select ma from MunicipalbondAux ma where ma.municipalbond.id =:id and "
											+ "ma.status=:status and ma.type=:type and ma.coveritem=:coveritem order by ma.liquidationDate desc ");
							queryNewInterest.setParameter("id",
									municipalBond.getId());
							queryNewInterest.setParameter("status", "VALID");
							queryNewInterest.setParameter("type", "I");
							queryNewInterest.setParameter("coveritem", true);

							List<MunicipalbondAux> oldInterest = queryNewInterest
									.getResultList();
							if (!oldInterest.isEmpty()) {
								MunicipalbondAux bondAux = oldInterest.get(0);
								expirationDate = DateUtils.truncate(bondAux
										.getLiquidationDate());
							}

						}
					}
					if (municipalBond.getExpirationDate().after(expirationDate)) {
						expirationDate = municipalBond.getExpirationDate();
					}
				}

			}

			// @author macartuche
			// @date 2016-07-21T16:41
			// @tag InteresCeroInstPub
			Boolean interestCero_publiCompany = systemParameterService
					.findParameter(COLLECT_INTEREST_PUBLIC_COMPANY);
			if (interestCero_publiCompany) {
				/*
				 * @author Rene
				 * 
				 * @date 2016-08-24
				 */
				boolean isPublic = residentService
						.isPublicInstitution(municipalBond.getResident()
								.getId());
				if (isPublic) {
					Calendar transitionDate = InterestPublicInstitutionUtils.transitionDate;
					Calendar paymentCalendar = Calendar.getInstance();
					paymentCalendar.setTime(paymentDate);
					if (paymentCalendar.after(transitionDate)) {
						// /se realiza el cambio de fecha de pago
						// solo para calculo de interes, no para fecha real de
						// pago
						paymentDate = transitionDate.getTime();
					}
					checkCompensationReceipt(municipalBond);
				}
			}
			// fin @tag InteresCeroInstPub

			paymentDate = DateUtils.truncate(paymentDate);
			// System.out.println("INTERESTS -----> Expiration date?
			// "+expirationDate);
			// System.out.println("INTERESTS -----> Payment date?
			// "+expirationDate);

			List<Date> datesForApplyInterest = findDatesForPaidInterest(
					paymentDate, expirationDate);

			if (expirationDate.before(paymentDate)) {
				List<InterestRate> interestRateList = findInterestRateByExpirationDate(expirationDate);
				// System.out.println("INTERESTS -----> TABLE");
				for (InterestRate ir : interestRateList) {
					BigDecimal appliedRate = ir
							.getValue()
							.divide(new BigDecimal(12), new MathContext(10))
							.divide(new BigDecimal(ir.getFactor()),
									new MathContext(12));

					for (Date d : datesForApplyInterest) {

						if ((ir.getEndDate().after(d) && ir.getBeginDate()
								.before(d))
								|| ir.getEndDate().equals(d)
								|| ir.getBeginDate().equals(d)) {
							// System.out.println("INTERESTS -----> RATE FOR
							// "+ir.getBeginDate()+" AND "+ir.getEndDate()+" IS
							// "+appliedRate+" CHECKING DATE "+d);
							BigDecimal interestValue = balance
									.multiply(appliedRate);
							interest = interest.add(interestValue);
							// System.out.println("Added "+interest);
						}
					}
					// System.out.println("INTERESTS -----> AFTER
					// FOR\n\n\n\n\n\n");
				}

				// System.out.println("Status=============>>>>>>>>>>>>> "+municipalBond.getMunicipalBondStatus().getId());
				// verificar si la obligacion tiene compensacion
				MunicipalBondStatus mbs = systemParameterService.materialize(
						MunicipalBondStatus.class,
						"MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
				Long statusId = mbs.getId();
				System.out.println("MBS ID " + statusId);
				if (municipalBond.getMunicipalBondStatus() != null
						&& statusId != 0) {
					if (municipalBond.getMunicipalBondStatus().getId()
							.longValue() == statusId.longValue()) {
						// macartuche
						// verificar si ya se ha registrado la factura
						// electronica

						Receipt receipt = municipalBond.getReceipt();

					}
				}

				// fin modificacion macartuche

				// interest =
				// this.calculateInterestForInterestRateOutDate(interestRateList,
				// paymentDate, balance, interest);
			}
		}

		interest = interest.setScale(2, RoundingMode.HALF_UP);
		municipalBond.setInterest(interest);

		// @tag interesFactElec
		// aumentar campo de interesfactura
		if (municipalBond.getInterestVoucher() != null) {
			municipalBond.setInterest(municipalBond.getInterestVoucher());
		}
		// fin @tag interesFactElec
		// System.out.println("INTERESTS -----> ENDS With value "+interest);
	}

	// @author macartuche
	// 2016-07-21T16:51
	// @tag InteresCeroInstPub
	// chequear que la deuda tenga el mismo interes que la factura electronica
	@SuppressWarnings("unchecked")
	private void checkCompensationReceipt(MunicipalBond municipalBond) {
		if (municipalBond.getReceipt() != null) {
			Receipt receipt = municipalBond.getReceipt();
			Query compensationQuery = entityManager
					.createQuery("Select cr from CompensationReceipt cr where cr.receipt.id=:receiptid");
			compensationQuery.setParameter("receiptid", receipt.getId());
			List<CompensationReceipt> list = compensationQuery.getResultList();
			if (!list.isEmpty()) {
				CompensationReceipt cr = list.get(0);
				System.out.println("Interes CompensationReceipt "
						+ cr.getInterest());
				if (cr.getInterest().compareTo(BigDecimal.ZERO) == 1) {
					municipalBond.setInterest(cr.getInterest());
					System.out.println("=======================>"
							+ receipt.toString() + " interest CR: "
							+ cr.getInterest());
				}
			}
		}
	}

	/*
	 * private BigDecimal
	 * calculateInterestForInterestRateOutDate(List<InterestRate>
	 * interestRateList, Date paymentDate, BigDecimal balance, BigDecimal
	 * interest){ if (!interestRateList.isEmpty() && interestRateList.size() >
	 * 0){ InterestRate interestRateLast =
	 * interestRateList.get(interestRateList.size()-1); Calendar dateLast =
	 * Calendar.getInstance(); dateLast.setTime(interestRateLast.getEndDate());
	 * while (dateLast.before(paymentDate)){ java.math.BigDecimal interestValue
	 * = balance.multiply(interestRateLast.getMonthlyRate()); interest =
	 * interest.add(interestValue); } } return interest; }
	 */

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findMunicipalBonds(Long residentId,
			Long entryId, Date startDate, Date endDate,
			Long municipalBondStatusId, Integer firstRow, Integer numberOfRows,
			Long municipalBondNumber) {

		if (residentId == null && entryId == null && startDate == null
				&& endDate == null && municipalBondStatusId == null
				&& municipalBondNumber == 0) {
			return new ArrayList<MunicipalBond>();
		}

		StringBuilder queryBuilder = new StringBuilder(EJBQL);

		if (residentId != null) {
			queryBuilder.append("resident.id = " + residentId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (startDate != null && endDate != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			queryBuilder
					.append("emisionDate >= '" + df.format(startDate) + "'");
			queryBuilder.append(JOIN_CLAUSE);
			queryBuilder.append("emisionDate <= '" + df.format(endDate) + "'");
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (entryId != null) {
			queryBuilder.append("entry.id = " + entryId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (municipalBondStatusId != null) {
			queryBuilder.append("municipalBond.municipalBondStatus.id = "
					+ municipalBondStatusId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (municipalBondNumber > 0) {
			queryBuilder
					.append("municipalBond.number = " + municipalBondNumber);
			queryBuilder.append(JOIN_CLAUSE);
		}

		int start = queryBuilder.lastIndexOf(JOIN_CLAUSE);

		queryBuilder = queryBuilder.delete(start, queryBuilder.length());

		queryBuilder
				.append(" ORDER BY municipalBond.entry, municipalBond.id desc");

		// System.out.println("QUERY GENERADO findMunicipalBonds---->
		// \n\n\n"+queryBuilder.toString());

		String stringQuery = queryBuilder.toString();

		// Integer maxResults =
		// systemParameterService.findParameter("MAX_QUERY_RESULTS");
		Query query = entityManager.createQuery(stringQuery);
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<MunicipalBond> mbs = query.getResultList();

		return mbs;
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findMunicipalBonds(Long residentId,
			Long entryId, Long municipalBondStatusId, Integer firstRow,
			Integer numberOfRows) {

		if (residentId == null && entryId == null
				&& municipalBondStatusId == null) {
			return new ArrayList<MunicipalBond>();
		}

		StringBuilder queryBuilder = new StringBuilder(EJBQL);

		if (residentId != null) {
			queryBuilder.append("resident.id = " + residentId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (entryId != null) {
			queryBuilder.append("entry.id = " + entryId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (municipalBondStatusId != null) {
			queryBuilder.append("municipalBond.municipalBondStatus.id = "
					+ municipalBondStatusId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		int start = queryBuilder.lastIndexOf(JOIN_CLAUSE);

		queryBuilder = queryBuilder.delete(start, queryBuilder.length());

		queryBuilder
				.append(" ORDER BY municipalBond.entry, municipalBond.id desc");

		// System.out.println("QUERY GENERADO findMunicipalBonds---->
		// \n\n\n"+queryBuilder.toString());

		String stringQuery = queryBuilder.toString();

		// Integer maxResults =
		// systemParameterService.findParameter("MAX_QUERY_RESULTS");
		Query query = entityManager.createQuery(stringQuery);
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<MunicipalBond> mbs = query.getResultList();

		return mbs;
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<MunicipalBond> findMunicipalBondsFormalizer(
			Long municipalBondStatusId, Date now, Integer firstRow,
			Integer numberOfRows) {
		// TODO imolementar aqui logica buscar emisiones formalizar
		StringBuilder queryBuilder = new StringBuilder(EJBQL);

		if (municipalBondStatusId != null) {
			queryBuilder.append("municipalBond.municipalBondStatus.id = "
					+ municipalBondStatusId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (now != null) {

			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String nowString = formatter.format(now);

			queryBuilder.append(":now>= municipalBond.emisionDate ");
			queryBuilder.append(JOIN_CLAUSE);
		}

		int start = queryBuilder.lastIndexOf(JOIN_CLAUSE);

		queryBuilder = queryBuilder.delete(start, queryBuilder.length());

		queryBuilder
				.append(" ORDER BY municipalBond.entry, municipalBond.id desc");

		// System.out.println("QUERY GENERADO findMunicipalBonds---->
		// \n\n\n"+queryBuilder.toString());

		String stringQuery = queryBuilder.toString();

		// Integer maxResults =
		// systemParameterService.findParameter("MAX_QUERY_RESULTS");
		Query query = entityManager.createQuery(stringQuery);
		query.setParameter("now", now, TemporalType.DATE);
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<MunicipalBond> mbs = query.getResultList();

		return mbs;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long findMunicipalBondsNumber(Long residentId, Long entryId,
			Date startDate, Date endDate, Long municipalBondStatusId,
			Long municipalBondNumber) {

		if (residentId == null && entryId == null && startDate == null
				&& endDate == null && municipalBondStatusId == null
				&& municipalBondNumber == 0) {
			return 0L;
		}

		StringBuilder queryBuilder = new StringBuilder(COUNT_EJBQL);

		if (residentId != null) {
			queryBuilder.append("municipalBond.resident.id = " + residentId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (startDate != null && endDate != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			queryBuilder.append("municipalBond.emisionDate >= '"
					+ df.format(startDate) + "'");
			queryBuilder.append(JOIN_CLAUSE);
			queryBuilder.append("municipalBond.emisionDate <= '"
					+ df.format(endDate) + "'");
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (entryId != null) {
			queryBuilder.append("municipalBond.entry.id = " + entryId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (municipalBondStatusId != null) {
			queryBuilder.append("municipalBond.municipalBondStatus.id = "
					+ municipalBondStatusId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (municipalBondNumber > 0) {
			queryBuilder
					.append("municipalBond.number = " + municipalBondNumber);
			queryBuilder.append(JOIN_CLAUSE);
		}

		int start = queryBuilder.lastIndexOf(JOIN_CLAUSE);

		queryBuilder = queryBuilder.delete(start, queryBuilder.length());

		// System.out.println("QUERY GENERADO findMunicipalBondsNumber---->
		// \n\n\n"+queryBuilder.toString());
		//

		String stringQuery = queryBuilder.toString();

		Query query = entityManager.createQuery(stringQuery);
		Long size = (Long) query.getSingleResult();

		// System.out.println("CLASE RETORNADA " + size.getClass() +
		// " CURRENT SIZE = " + size);

		return size;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long findMunicipalBondsNumber(Long residentId, Long entryId,
			Long municipalBondStatusId) {

		if (residentId == null && entryId == null
				&& municipalBondStatusId == null) {
			return 0L;
		}

		StringBuilder queryBuilder = new StringBuilder(COUNT_EJBQL);

		if (residentId != null) {
			queryBuilder.append("municipalBond.resident.id = " + residentId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (entryId != null) {
			queryBuilder.append("municipalBond.entry.id = " + entryId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (municipalBondStatusId != null) {
			queryBuilder.append("municipalBond.municipalBondStatus.id = "
					+ municipalBondStatusId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		int start = queryBuilder.lastIndexOf(JOIN_CLAUSE);

		queryBuilder = queryBuilder.delete(start, queryBuilder.length());

		// System.out.println("QUERY GENERADO findMunicipalBondsNumber---->
		// \n\n\n"+queryBuilder.toString());
		//

		String stringQuery = queryBuilder.toString();

		Query query = entityManager.createQuery(stringQuery);
		Long size = (Long) query.getSingleResult();

		// System.out.println("CLASE RETORNADA emisiones futuras: " +
		// size.getClass() + " CURRENT SIZE = " + size);
		return size;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long findMunicipalBondsNumberFormalizer(Long municipalBondStatusId,
			Date now) {
		// TODO implementar aqui logica sacer numero de obligacione spor
		// formalizar

		StringBuilder queryBuilder = new StringBuilder(COUNT_EJBQL);

		if (municipalBondStatusId != null) {
			queryBuilder.append("municipalBond.municipalBondStatus.id = "
					+ municipalBondStatusId);
			queryBuilder.append(JOIN_CLAUSE);
		}

		if (now != null) {

			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String nowString = formatter.format(now);

			queryBuilder.append(":now>= municipalBond.emisionDate ");
			queryBuilder.append(JOIN_CLAUSE);
		}

		int start = queryBuilder.lastIndexOf(JOIN_CLAUSE);

		queryBuilder = queryBuilder.delete(start, queryBuilder.length());

		// System.out.println("QUERY GENERADO findMunicipalBondsNumber---->
		// \n\n\n"+queryBuilder.toString());
		//

		String stringQuery = queryBuilder.toString();

		Query query = entityManager.createQuery(stringQuery);
		query.setParameter("now", now, TemporalType.DATE);
		Long size = (Long) query.getSingleResult();

		/*
		 * System.out.println(
		 * "numero de obligaciones normalizar que devuelve el model: " +
		 * size.getClass() + " CURRENT SIZE = " + size);
		 */
		return size;

	}
}