/**
 * 
 */
package org.gob.gim.cadaster.facade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.FiscalPeriodService;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.EntryService;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.LocationPropertySinat;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.PropertyUse;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.cadaster.model.UnbuiltLot;
import ec.gob.gim.cadaster.model.WorkDealFraction;
import ec.gob.gim.cadaster.model.dto.AppraisalsPropertyDTO;
import ec.gob.gim.cadaster.model.dto.ExemptionDTO;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.ExemptionForProperty;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.adjunct.DomainTransfer;
import ec.gob.gim.revenue.model.adjunct.PropertyAppraisal;

/**
 * @author wilman
 *
 */
@Stateless(name = "CadasterService")
public class CadasterServiceBean implements CadasterService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	MunicipalBondService municipalBondService;

	@EJB
	EntryService entryService;

	@EJB
	ResidentService residentService;

	@EJB
	FiscalPeriodService fiscalPeriodService;

	@EJB
	RevenueService revenueService;

	@EJB
	SystemParameterService systemParameterService;

	@EJB
	CrudService crudService;

	/**
	 * PreEmisiÃ³n de la Utilidad por venta de predio
	 * 
	 * @param p
	 *            Property a la que se le va a emitir la Utilidad
	 * @param d
	 *            Domain nuevo dominio que se estÃ¡ creando con el traspaso
	 * @param f
	 *            FiscalPeriod para el que se va a emitir
	 * @param per
	 *            Persona que realiza la preEmisiÃ³n
	 */
	@Override
	public void preEmitUtilityTax(Property p, Domain d, FiscalPeriod f,
			Person per) throws Exception {
		// TODO Auto-generated method stub
		Date currentDate = new Date();
		Entry entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_UTILITY");
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(currentDate);
		emissionOrder.setDescription(entry.getName() + ": "
				+ p.getCadastralCode());
		emissionOrder.setEmisor(per);

		EntryValueItem entryValueItem = new EntryValueItem();
		entryValueItem.setDescription(emissionOrder.getDescription());
		entryValueItem.setMainValue(d.getCommercialAppraisal());
		entryValueItem.setServiceDate(currentDate);
		entryValueItem
				.setReference("Anterior: " + p.getPreviousCadastralCode() != null ? p
						.getPreviousCadastralCode() : "" + " - Nuevo: "
						+ p.getCadastralCode());

		MunicipalBond mb = revenueService.createMunicipalBond(p
				.getCurrentDomain().getResident(), entry, f, entryValueItem,
				true, p, d);

		mb.setCreationDate(currentDate);
		mb.setCreationTime(currentDate);
		mb.setEntry(entry);
		mb.setFiscalPeriod(f);
		mb.setOriginator(per);
		mb.setBase(d.getValueForCalculate());
		mb.setDescription("UTILIDAD");
		mb.setGroupingCode(p.getCadastralCode());
		mb.setAddress(p.getLocation().getMainBlockLimit().getStreet().getName());
		mb.setServiceDate(currentDate);
		mb.setTimePeriod(entry.getTimePeriod());

		mb.setAdjunct(createAdjunct(p, d));

		if (municipalBondService
				.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(d
						.getResident().getId(), entry.getId(), mb
						.getServiceDate(), mb.getGroupingCode()) != null)
			return;
		mb.calculateValue();
		if (mb.getValue().compareTo(new BigDecimal(0)) == -1)
			mb.setValue(new BigDecimal(0));
		emissionOrder.add(mb);

		savePreEmissionOrder(emissionOrder);
	}

	private DomainTransfer createAdjunct(Property pro, Domain domain) {
		DomainTransfer adj = new DomainTransfer();
		adj.setBuyer(domain.getResident().getName());
		adj.setSeller(pro.getCurrentDomain().getResident().getName());
		adj.setBuildingAppraisal(pro.getCurrentDomain().getBuildingAppraisal());
		adj.setLotAppraisal(pro.getCurrentDomain().getLotAppraisal());
		adj.setCommercialAppraisal(pro.getCurrentDomain()
				.getCommercialAppraisal());
		adj.setTransactionValue(domain.getValueTransaction());
		adj.setLotArea(pro.getArea());
		adj.setBuildingArea(pro.getCurrentDomain().getTotalAreaConstruction());
		return adj;
	}

	/**
	 * PreEmisiÃ³n de la Alcabala por venta de predio
	 * 
	 * @param pro
	 *            Property a la que se le va a emitir la Alcabala
	 * @param d
	 *            Domain nuevo dominio que se estÃ¡ creando con el traspaso
	 * @param f
	 *            FiscalPeriod para el que se va a emitir
	 * @param per
	 *            Persona que realiza la preEmisiÃ³n
	 * @param exoneration
	 *            Boolean se le emite o no la Alcabala
	 * @param exonerationReason
	 *            RazÃ³n de la exoneraciÃ³n
	 */
	@Override
	public void preEmitAlcabalaTax(Property pro, Domain d, FiscalPeriod f,
			Person p, boolean exoneration, String exonerationReason)
			throws Exception {
		// TODO Auto-generated method stub
		Date currentDate = new Date();
		Entry entry = systemParameterService.materialize(Entry.class,
				"ENTRY_ID_ALCABALA");
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(currentDate);
		emissionOrder.setDescription(entry.getName() + ": "
				+ pro.getCadastralCode());
		emissionOrder.setEmisor(p);

		EntryValueItem entryValueItem = new EntryValueItem();
		entryValueItem.setExempt(exoneration);
		entryValueItem.setDescription(emissionOrder.getDescription());
		entryValueItem.setMainValue(d.getCommercialAppraisal());
		entryValueItem.setServiceDate(currentDate);
		entryValueItem.setReference("Anterior: "
				+ pro.getPreviousCadastralCode() != null ? pro
				.getPreviousCadastralCode() : "" + " - Nuevo: "
				+ pro.getCadastralCode());

		MunicipalBond mb = revenueService.createMunicipalBond(pro
				.getCurrentDomain().getResident(), entry, f, entryValueItem,
				true, pro, d);
		// MunicipalBond mb =
		// revenueService.createMunicipalBond(d.getResident(), f, entry, null,
		// currentDate, null, MunicipalBondType.EMISSION_ORDER, d,pro);

		mb.setCreationDate(currentDate);
		mb.setCreationTime(currentDate);
		mb.setEntry(entry);
		mb.setDescription("ALCABALA");
		if (exoneration)
			mb.setDescription(mb.getDescription() == null ? "" : mb
					.getDescription() + " - " + exonerationReason);
		mb.setFiscalPeriod(f);
		mb.setOriginator(p);
		mb.setGroupingCode(pro.getCadastralCode());
		mb.setBase(d.getValueForCalculate());

		mb.setAdjunct(createAdjunct(pro, d));

		if (pro.isUrban()) {
			mb.setAddress(pro.getLocation().getMainBlockLimit().getStreet()
					.getName());
		} else {
			mb.setAddress(getAddressForRusticProperty(pro));
		}
		mb.setServiceDate(currentDate);

		mb.setTimePeriod(entry.getTimePeriod());
		if (municipalBondService
				.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(d
						.getResident().getId(), entry.getId(), mb
						.getServiceDate(), mb.getGroupingCode()) != null)
			return;
		mb.calculateValue();
		if (mb.getValue().compareTo(new BigDecimal(0)) == -1)
			mb.setValue(new BigDecimal(0));

		emissionOrder.add(mb);

		savePreEmissionOrder(emissionOrder);

	}

	/**
	 * Buscar TerritorialDivision
	 * 
	 * @param cadastralCode
	 *            Clave catastral de la que se toma el subString
	 * @param x
	 *            PosiciÃ³n inicial para el subString
	 * @param y
	 *            PosiciÃ³n final para el subString
	 * @param td
	 *            TerritorialDivision padre
	 * @return TerritorialDivision
	 */
	@Override
	public TerritorialDivision findTerritorialDivision(String cadastralCode,
			int x, int y, TerritorialDivision td) {

		if (cadastralCode.length() < y)
			return null;

		String code = cadastralCode.substring(x, y);

		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("code", code);
		parameters.put("parent", td);
		List<?> list = crudService.findWithNamedQuery(
				"TerritorialDivision.findByCodeAndParent", parameters);
		if (list != null && list.size() > 0)
			return (TerritorialDivision) list.get(0);
		return null;
	}

	/**
	 * Buscar TerritorialDivision
	 * 
	 * @param code
	 *            CÃ³digo del TerritorialDivision
	 * @param territorialDivisionTypeId
	 *            Tipo de TerritorialDivision
	 * @return TerritorialDivision
	 */
	@Override
	public TerritorialDivision findTerritorialDivision(String code,
			long territorialDivisionTypeId) {
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("code", code);
		parameters.put("territorialDivisionTypeId", territorialDivisionTypeId);
		List<?> list = crudService.findWithNamedQuery(
				"TerritorialDivision.findByTypeAndCode", parameters);
		if (list != null && list.size() > 0)
			return (TerritorialDivision) list.get(0);
		return null;
	}

	/**
	 * @param code
	 *            CÃ³digo del TerritorialDivision
	 * @param territorialDivisionTypeId
	 *            Tipo de TerritorialDivision
	 * @param parent
	 *            TerritorialDivision padre
	 * @return TerritorialDivision
	 */
	@Override
	public TerritorialDivision findTerritorialDivision(String code,
			long territorialDivisionTypeId, TerritorialDivision parent) {
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("code", code);
		parameters.put("territorialDivisionTypeId", territorialDivisionTypeId);
		parameters.put("parent", parent);
		List<?> list = crudService.findWithNamedQuery(
				"TerritorialDivision.findByTypeAndCodeAndParent", parameters);
		if (list != null && list.size() > 0)
			return (TerritorialDivision) list.get(0);
		return null;
	}

	@Override
	public TerritorialDivision findDefaultCanton() {
		TerritorialDivision province = findDefaultProvince();
		String code = systemParameterService
				.findParameter("TERRITORIAL_DIVISION_CODE_CANTON");
		Long territorialDivisionTypeId = systemParameterService
				.findParameter("TERRITORIAL_DIVISION_TYPE_ID_CANTON");
		return findTerritorialDivision(code, territorialDivisionTypeId,
				province);
	}

	@Override
	public TerritorialDivision findDefaultProvince() {
		String code = systemParameterService
				.findParameter("TERRITORIAL_DIVISION_CODE_PROVINCE");
		Long territorialDivisionTypeId = systemParameterService
				.findParameter("TERRITORIAL_DIVISION_TYPE_ID_PROVINCE");
		return findTerritorialDivision(code, territorialDivisionTypeId);
	}

	private TerritorialDivision findProvince(String code) {
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("code", code);
		List<?> list = crudService.findWithNamedQuery(
				"TerritorialDivision.findProvinceByCode", parameters);
		if (list != null && list.size() > 0)
			return (TerritorialDivision) list.get(0);
		return null;
	}

	private Date calculateExpirationDate(Date date, Integer daysNumber) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DATE, daysNumber);
		return now.getTime();
	}

	private MunicipalBond createInstance(Resident resident,
			FiscalPeriod fiscalPeriod, Entry entry, BigDecimal base,
			Date creationDate, Date emissionDate, Date serviceDate,
			Date expirationDate, String reference, String description,
			TaxpayerRecord institution) {

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

		Adjunct adjunct = createAdjunct(entry);

		municipalBond.setAdjunct(adjunct);

		municipalBond.setTimePeriod(entry.getTimePeriod());

		return municipalBond;
	}

	private Adjunct createAdjunct(Entry entry) {
		try {
			System.out.println("CREATE ADJUNCT ----> "
					+ entry.getAdjunctClassName());
			if (entry.getAdjunctClassName() != null
					&& !entry.getAdjunctClassName().trim().isEmpty()) {
				Class<?> klass = Class.forName(entry.getAdjunctClassName());
				Adjunct adjunct = (Adjunct) klass.newInstance();
				System.out.println("ADJUNCT CREATED ----> "
						+ adjunct.getClass().getSimpleName());
				return adjunct;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Crea los MunicipalBond del impuesto por solares no edificados
	 * 
	 * @param eo
	 *            EmissionOrder sobre la que se agregan los MunicipalBond
	 * @param entry
	 *            Rubro que se va a emitir
	 * @param unbuiltLots
	 *            Lista de Solares no edificadors a las que se les va a emitir
	 *            el impuesto predial
	 * @param fiscalperiod
	 *            Periodo fiscal para el que se realiza la emisiÃ³n
	 * @param p
	 *            Persona que realiza la preEmisiÃ³n
	 * @return List<MunicipalBond>
	 */
	public List<MunicipalBond> onlyCalculatePreEmissionOrderUnbuiltLotTax(
			EmissionOrder eo, Entry entry, List<UnbuiltLot> unbuiltLots,
			FiscalPeriod fiscalPeriod, Person p) throws Exception {

		if (unbuiltLots == null)
			return null;
		Date now = Calendar.getInstance().getTime();
		List<MunicipalBond> bonds = new ArrayList<MunicipalBond>();
		MunicipalBondStatus mbs = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");

		TaxpayerRecord institution = municipalBondService
				.findTaxpayerRecord(entry.getId());

		for (UnbuiltLot ubl : unbuiltLots) {

			Date creationDate = now;
			Date emissionDate = now;

			Date serviceDate = eo.getServiceDate();
			String reference = ("Anterior: "
					+ ubl.getProperty().getPreviousCadastralCode() != null ? ubl
					.getProperty().getPreviousCadastralCode() : ""
					+ " - Nuevo: " + ubl.getProperty().getCadastralCode());
			String description = eo.getDescription();
			BigDecimal base = ubl.getProperty().getCurrentDomain()
					.getLotAppraisal();
			Date expirationDate = calculateExpirationDate(serviceDate, entry
					.getTimePeriod().getDaysNumber());

			Resident resident = ubl.getProperty().getCurrentDomain()
					.getResident();

			MunicipalBond municipalBond = createInstance(resident,
					fiscalPeriod, entry, base, creationDate, emissionDate,
					serviceDate, expirationDate, reference, description,
					institution);

			municipalBond.setIsExpirationDateDefined(Boolean.FALSE);
			municipalBond.setExempt(false);
			municipalBond.setInternalTramit(false);
			municipalBond.setIsNoPasiveSubject(false);
			municipalBond.setPreviousPayment(BigDecimal.ZERO);
			municipalBond.setGroupingCode(ubl.getProperty().getCadastralCode());
			municipalBond.setMunicipalBondStatus(mbs);
			municipalBond.setEmisionPeriod(fiscalPeriodService.findCurrent(now)
					.get(0).getStartDate());

			fillPropertyAppraisal(
					(PropertyAppraisal) municipalBond.getAdjunct(), ubl
							.getProperty().getCurrentDomain());

			municipalBond.setBondAddress(ubl.getProperty().getAddress());

			if (ubl.getProperty().getLocation() != null
					&& ubl.getProperty().getLocation().getMainBlockLimit() != null
					&& ubl.getProperty().getLocation().getMainBlockLimit()
							.getStreet() != null) {
				municipalBond.setAddress(ubl.getProperty().getLocation()
						.getMainBlockLimit().getStreet().getName());
			} else {
				municipalBond.setAddress(getAddressForRusticProperty(ubl
						.getProperty()));
			}

			if (ubl.getProperty().getAddressReference() != null) {
				municipalBond.setAddress(municipalBond.getAddress() + " "
						+ ubl.getProperty().getAddressReference());
				municipalBond.setBondAddress(municipalBond.getBondAddress()
						+ " " + ubl.getProperty().getAddressReference());
			}

			municipalBond.setDescription(entry.getName());

			municipalBond.setOriginator(p);
			municipalBond.calculateValue();
			if (municipalBond.getValue().compareTo(new BigDecimal(0)) == -1)
				municipalBond.setValue(new BigDecimal(0));

			bonds.add(municipalBond);

		}

		if (bonds != null && bonds.size() > 0)
			municipalBondService.createItemsToMunicipalBond(bonds, null, true,
					true, false);

		return bonds;

	}

	/**
	 * Crea los MunicipalBond del impuesto predial urbano o rÃºstico para todas
	 * las propiedades enviadas como parÃ¡metro
	 * 
	 * @param eo
	 *            EmissionOrder sobre la que se agregan los MunicipalBond
	 * @param entry
	 *            Rubro que se va a emitir: Predio urbano o rÃºstico
	 * @param properties
	 *            Lista de Propiedades a las que se les va a emitir el impuesto
	 *            predial
	 * @param fiscalperiod
	 *            Periodo fiscal para el que se realiza la emisiÃ³n
	 * @param p
	 *            Persona que realiza la preEmisiÃ³n
	 * @param isUrban
	 *            Boolean indica si es urbano o en caso contrario es rÃºstico
	 * @return List<MunicipalBond>
	 */
	public List<MunicipalBond> onlyCalculatePreEmissionOrderPropertyTax(
			EmissionOrder eo, Entry entry, List<Property> properties,
			FiscalPeriod fiscalPeriod, Person p, boolean isUrban, boolean IsSpecial)
			throws Exception {

		if (properties == null)
			return null;
		Date now = Calendar.getInstance().getTime();
		List<MunicipalBond> bonds = new ArrayList<MunicipalBond>();
		MunicipalBondStatus mbs = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");

		TaxpayerRecord institution = municipalBondService
				.findTaxpayerRecord(entry.getId());
		String desconocido = "DESCONOCIDO";
		BigDecimal exemptValueForMinAppraisal = BigDecimal.ZERO;
		if (isUrban) {
			// exemptValueForMinAppraisal =
			// fiscalPeriod.getBasicSalaryUnifiedForRevenue().multiply(new
			// BigDecimal(25));
			// no se fijan los 25 SBU por que no es posible determinar Los
			// predios unifamiliares urbano-marginales con avalÃºos de hasta
			// veinticinco remuneraciones
			// bÃ¡sicas unificadas del trabajador en general
			exemptValueForMinAppraisal = BigDecimal.ZERO;
		} else
			exemptValueForMinAppraisal = fiscalPeriod
					.getBasicSalaryUnifiedForRevenue().multiply(
							new BigDecimal(15));

		for (Property pro : properties) {
			System.out.println("===========>> contribuyente: "
					+ pro.getCurrentDomain().getResident().getName());
			if ((pro.getCurrentDomain() != null)
					&& (pro.getCurrentDomain().getResident().getName()
							.toUpperCase().indexOf(desconocido) == -1)
					&& (!pro.getCurrentDomain().getPropertyUse()
							.equals(PropertyUse.MUNICIPAL))) {
				Date creationDate = now;
				// Date emissionDate = now;
				Date emissionDate = eo.getServiceDate();

				Date serviceDate = eo.getServiceDate();
				String reference = ("Anterior: "
						+ pro.getPreviousCadastralCode() != null ? pro
						.getPreviousCadastralCode() : "" + " - Nuevo: "
						+ pro.getCadastralCode());
				String description = eo.getDescription();
				BigDecimal base = pro.getCurrentDomain()
						.getCommercialAppraisal();
				Date expirationDate = null;
				Date expiration = new Date(eo.getServiceDate().getYear(), 11,
						31);
				boolean aux = false;
				if (expiration == null) {
					expirationDate = calculateExpirationDate(serviceDate, entry
							.getTimePeriod().getDaysNumber());
				} else {
					aux = true;
					expirationDate = expiration;
				}

				Resident resident = pro.getCurrentDomain().getResident();

				MunicipalBond municipalBond = createInstance(resident,
						fiscalPeriod, entry, base, creationDate, emissionDate,
						serviceDate, expirationDate, reference, description,
						institution);

				municipalBond.setIsExpirationDateDefined(aux);
				municipalBond.setExempt(false);
				municipalBond.setInternalTramit(false);
				municipalBond.setIsNoPasiveSubject(false);
				municipalBond.setPreviousPayment(BigDecimal.ZERO);
				municipalBond.setGroupingCode(pro.getCadastralCode());
				municipalBond.setMunicipalBondStatus(mbs);

				fillPropertyAppraisal(
						(PropertyAppraisal) municipalBond.getAdjunct(),
						pro.getCurrentDomain());

				if (isUrban) {
					if (pro.getLocation() != null
							&& pro.getLocation().getMainBlockLimit() != null
							&& pro.getLocation().getMainBlockLimit()
									.getStreet() != null)
						municipalBond.setAddress(pro.getLocation()
								.getMainBlockLimit().getStreet().getName());

					// ****
					String parameterDescription = systemParameterService
							.findParameter("URBAN_PROPERTY_TAX_DESCRIPTION_EMISSION");
					municipalBond
							.setDescription(String.format(parameterDescription,
									fiscalPeriod.getFiscalYear()));
				} else {
					// municipalBond.setAddress(getAddressForRusticProperty(pro));
					municipalBond.setAddress(pro.getCurrentDomain()
							.getDescription());
					
					municipalBond.setBondAddress(pro.getCurrentDomain()
							.getDescription());
					String parameterDescription = systemParameterService
							.findParameter("RUSTIC_PROPERTY_TAX_DESCRIPTION_EMISSION");
					municipalBond
							.setDescription(String.format(parameterDescription,
									fiscalPeriod.getFiscalYear()));
					

				}

				if (pro.getAddressReference() != null) {
					municipalBond.setAddress(municipalBond.getAddress() + " "
							+ pro.getAddressReference());
					municipalBond.setBondAddress(municipalBond.getBondAddress()
							+ " " + pro.getAddressReference());
				}
				
				//@tag predioColoma
				if(IsSpecial){
					municipalBond.setAddress(pro.getAddressReference());
					municipalBond.setBondAddress(pro.getAddressReference());
				}

				if (exemptValueForMinAppraisal.compareTo(base) >= 0) {
					ExonerateForPredialTax(
							(PropertyAppraisal) municipalBond.getAdjunct(),
							base);
				}

				municipalBond.setOriginator(p);
				municipalBond.calculateValue();
				if (municipalBond.getValue().compareTo(new BigDecimal(0)) == -1)
					municipalBond.setValue(new BigDecimal(0));

				bonds.add(municipalBond);
			}

		}

		if (bonds != null && bonds.size() > 0)
			municipalBondService.createItemsToMunicipalBond(bonds, null, true,
					true, false);

		return bonds;

	}

	private void ExonerateForPredialTax(PropertyAppraisal adjunct,
			BigDecimal exemptionValue) {
		adjunct.setExemptionValue(exemptionValue);
	}

	private void fillPropertyAppraisal(PropertyAppraisal propertyAppraisal,
			Domain d) {
		propertyAppraisal.setEmitWithoutProperty(false);
		propertyAppraisal.setBuildingAppraisal(d.getBuildingAppraisal());
		propertyAppraisal.setCadastralCode(d.getProperty().getCadastralCode());
		propertyAppraisal.setPreviousCadastralCode(d.getProperty()
				.getPreviousCadastralCode());
		propertyAppraisal.setCadastralCode(d.getProperty().getCadastralCode());
		propertyAppraisal.setCommercialAppraisal(d.getCommercialAppraisal());
		propertyAppraisal.setLotAppraisal(d.getLotAppraisal());
		propertyAppraisal.setProperty(d.getProperty());
		propertyAppraisal.setCode(propertyAppraisal.getPreviousCadastralCode()
				+ " - " + propertyAppraisal.getCadastralCode());
	}

	private List<Long> loadEntriesIds() {
		List<Long> entriesIds = new ArrayList<Long>();
		entriesIds.add((Long) systemParameterService
				.findParameter("ENTRY_ID_URBAN_PROPERTY"));
		entriesIds.add((Long) systemParameterService
				.findParameter("ENTRY_ID_RUSTIC_PROPERTY"));
		return entriesIds;
	}

	private Long loadMunicipalBondStatus() {
		return (Long) systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_PENDING");
	}

	private void calculateDiscountPercentage(Exemption ex) {
		if (ex.getExemptionType().getId() == 1 || ex.getExemptionType().getId() == 2 ) { // 1 por tercera edad
			BigDecimal cienBD = new BigDecimal(100);
			BigDecimal base = ex.getFiscalPeriod()//500 salarios basicos
					.getBasicSalaryUnifiedForRevenue()
					.multiply(ex.getFiscalPeriod().getSalariesNumber());
			System.out.println("<<<RR>>> base: " + base);
			System.out.println("<<<RR>>> patrimonio: " + ex.getPatrimony());
			base = base.multiply(ex.getExemptionPercentage().divide(cienBD));
			System.out
					.println("<<<RR>>> base aplicando porcentaje de exencion: "
							+ base);
			BigDecimal discount = BigDecimal.ZERO;
			if (ex.getPatrimony().compareTo(BigDecimal.ZERO) == 1)
				discount = base.multiply(cienBD).divide(ex.getPatrimony(),
						RoundingMode.HALF_UP);
			System.out.println("<<<RR>>> discount: " + discount);
			if (discount.compareTo(cienBD) > 0)
				ex.setDiscountPercentage(cienBD);
			else
				ex.setDiscountPercentage(discount);
		}  else {
			ex.setDiscountPercentage(ex.getExemptionPercentage());
		}

		// BigDecimal base =
		// ex.getFiscalPeriod().getBasicSalary().multiply(ex.getFiscalPeriod().getSalariesNumber());
		// base = base.multiply(ex.getExemptionPercentage().divide(new
		// BigDecimal(100)));
		// BigDecimal excedent = ex.getPatrimony().subtract(base);
		// BigDecimal discount = new BigDecimal(100);
		// if(excedent.compareTo(base) == 1){
		// discount = BigDecimal.ZERO;
		// }else{
		// if(excedent.compareTo(BigDecimal.ZERO) == 1){
		// if(ex.getPropertiesAppraisal().compareTo(BigDecimal.ZERO) == 1)
		// discount = new BigDecimal(100).subtract(excedent.multiply(new
		// BigDecimal(100)).divide(ex.getPropertiesAppraisal(),
		// RoundingMode.HALF_UP));
		// }
		// }
		// ex.setDiscountPercentage(discount);
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBond> findMunicipalBondsByFiscalPeriodResidentAndEntry(
			Long fiscalPeriodId, List<Long> residentIds, List<Long> entryIds,
			Long statusId) {
		Query query = entityManager
				.createNamedQuery("MunicipalBond.findByFiscalPeriodResidentsEntryAndStatus");
		query.setParameter("fiscalPeriodId", fiscalPeriodId);
		query.setParameter("residentIds", residentIds);
		query.setParameter("entryIds", entryIds);
		query.setParameter("municipalBondStatusId", statusId);
		return query.getResultList();
	}

	private Exemption findExemption(List<Exemption> exemptions, Long residentId) {
		for (Exemption ex : exemptions) {
			if (ex.getResident().getId() == residentId)
				return ex;
		}
		return null;
	}

	/**
	 * Crea el item de exenciÃ³n por tercera edad para todos los
	 * municipalBonds(Predio urbano y rÃºstico) existentes en un periodo fiscal
	 * determinado
	 * 
	 * Calcula el total de avalÃºo de las propiedades, calcula el patrimonio y el
	 * valor de exenciÃ³n para los contribuyentes y establece el valor de
	 * exenciÃ³n para el descuento y vuelve a calcular el valor a pagar por cada
	 * obligaciÃ³n del contribuyente.
	 * 
	 * @param fiscalPeriodId
	 *            id del periodo fiscal
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void calculateExemptions(Long fiscalPeriodId, Date fromDate,
			Date untilDate) {
		List<Long> entriesIds = loadEntriesIds();
		Long pendingStatus = loadMunicipalBondStatus();
		List<Exemption> exemptions = municipalBondService
				.findExemptionsByFiscalPeriod(fiscalPeriodId, fromDate,
						untilDate); //cambiado

		System.out.println("Total exoneraciones: " + exemptions.size());
		List<Long> residentsIds = new ArrayList<Long>();
		BigDecimal appraisalForProperties;
		List<MunicipalBond> bonds = new ArrayList<MunicipalBond>();
		int count = 0;
		int countTotal = exemptions.size();
		for (Exemption ex : exemptions) {
			System.out.println("\n\n\n\n=======> resident in exemption: "
					+ ex.getResident().getName());
			System.out.println("======>>Exemption ID: " + ex.getId());
			count++;
			System.out.println("======>>Exemption nro: " + count + " de "
					+ countTotal);
			appraisalForProperties = BigDecimal.ZERO;
			
			ExemptionDTO valuesExemption; 
			
			if (ex.getExemptionType().getId() == 1 || ex.getExemptionType().getId() == 2){//por tercera edad y discapacidad
				//@tag exoneraciones2017
				if(ex.getPropertiesInExemption().isEmpty())
					valuesExemption = calculateAppraisalGeneral(ex);
				else
					valuesExemption = calculateAppraisalForProperties(ex);
				//fin @tag exoneraciones2017
			}else{
				valuesExemption = calculateAppraisalForProperties(ex);//todo arregkar metodo
				//appraisalForProperties = calculateAppraisalForProperties(ex);
			}
			
			//@tag exoneraciones2017
			switch (ex.getExemptionType().getId().intValue()) {
				case 1: case 3: //tercera edad y rebaja hipotecaria
					appraisalForProperties = valuesExemption.getCommercialAppraisalResident().
					add(valuesExemption.getCommercialAppraisalPattern()).
					subtract(valuesExemption.getCommercialAppraisalSpecial());
					break;
	
				case 2: //discapacidad
					appraisalForProperties = valuesExemption.getCommercialAppraisalTable().
											subtract(valuesExemption.getCommercialAppraisalSpecial());
					break;
				case 4: //especial
					appraisalForProperties = BigDecimal.ZERO;
					break;		 
			}
			//fin @tag exoneraciones2017
			
			ex.setPropertiesAppraisal(appraisalForProperties);
			ex.calculatePatrimony(appraisalForProperties);
			calculateDiscountPercentage(ex);
			residentsIds.clear();
			residentsIds.add(ex.getResident().getId());
			if (ex.getPartner() != null)
				residentsIds.add(ex.getPartner().getId());

			bonds.clear();
			bonds = findMunicipalBondsByFiscalPeriodResidentAndEntry(
					fiscalPeriodId, residentsIds, entriesIds, pendingStatus);//todo
			for (MunicipalBond mb : bonds) { //TODO

				System.out.println("Clave catastral "+mb.getGroupingCode());
				PropertyAppraisal propertyAppraisal = (PropertyAppraisal) mb
						.getAdjunct();
				BigDecimal exemptionValue = BigDecimal.ZERO;
				exemptionValue = propertyAppraisal.getCommercialAppraisal()
						.multiply(ex.getDiscountPercentage())
						.divide(new BigDecimal(100));
				exemptionValue.setScale(2, RoundingMode.HALF_UP);
				if (ex.getExemptionType().getId() == 3) { // Por Rebaja
															// Hipotecaria
					exemptionValue = ex.getFiscalPeriod().getMortgageRate();
				}
				if (exemptionValue.compareTo(mb.getBase()) == 1) {
					exemptionValue = mb.getBase();
				}
				try {
					if (ex.getExemptionType().getId() == 1 || ex.getExemptionType().getId() == 2) { // tercera edad  y discap
						//@tag exoneraciones2017
						boolean flag=false;
						for(ExemptionForProperty efp: ex.getPropertiesInExemption()){
							if(efp.getProperty().getId().intValue() == propertyAppraisal.getProperty().getId().intValue()){			 
								BigDecimal percentage = (efp.getDiscountPercentage()==null)? ex.getDiscountPercentage() : efp.getDiscountPercentage();
								exemptionValue = percentage.multiply(propertyAppraisal.getCommercialAppraisal()).divide(new BigDecimal(100)); 
								propertyAppraisal.setExemptionValue(exemptionValue);
								//remision x defecto se pasa false porque no es pago, no implica calculo ni nada
								municipalBondService.changeExemptionInMunicipalBond(mb,false, true, Boolean.FALSE, propertyAppraisal);
								flag=true;
							}
						}
						//fin @tag exoneraciones2017
						
						if(flag)
							continue;
						//Solo aplica para tercera edad
						if(ex.getExemptionType().getId() == 1){
							propertyAppraisal.setExemptionValue(exemptionValue);
							// mb.setReference(mb.getReference() + " " +
							// ex.getReference());
							//remision x defecto se pasa false porque no es pago, no implica calculo ni nada
							municipalBondService.changeExemptionInMunicipalBond(mb,
									false, true, Boolean.FALSE, propertyAppraisal);
						}
					} else { // las demas exenciones por propiedad
						List<ExemptionForProperty> exForProperties = ex
								.getPropertiesInExemption();
						for (ExemptionForProperty exemptionForProperty : exForProperties) {
							// if
							// (exemptionForProperty.getProperty().getCadastralCode().matches(mb.getGroupingCode())){
							boolean flag=false;
//							if (exemptionForProperty.getProperty()
//									.getCadastralCode()
//									.indexOf(mb.getGroupingCode()) != -1) {
							System.out.println("comparación: ÏdPropiedad: "+ 
										exemptionForProperty.getProperty().getId()+" ID BOND: "+
									propertyAppraisal.getProperty().getId().intValue());
							if(exemptionForProperty.getProperty().getId().intValue() == 
									propertyAppraisal.getProperty().getId().intValue()){			 
								if (exemptionForProperty.getProperty()
										.getCurrentDomain().getPropertyUse() == PropertyUse.PARTICULAR) {
//									propertyAppraisal
//											.setExemptionValue(exemptionValue);
									
//									for(ExemptionForProperty efp: ex.getPropertiesInExemption()){
//										if(efp.getProperty().getId().intValue() == propertyAppraisal.getProperty().getId().intValue()){			 
											BigDecimal percentage = (exemptionForProperty.getDiscountPercentage()==null)? ex.getDiscountPercentage() : exemptionForProperty.getDiscountPercentage();
											exemptionValue = percentage.multiply(propertyAppraisal.getCommercialAppraisal()).divide(new BigDecimal(100)); 
											propertyAppraisal.setExemptionValue(exemptionValue);
											//remision x defecto se pasa false porque no es pago, no implica calculo ni nada
											municipalBondService.changeExemptionInMunicipalBond(mb,false, true, Boolean.FALSE, propertyAppraisal);
//											flag=true;
//										}
//									}
									
									continue;
								} else {
									propertyAppraisal
											.setExemptionValue(propertyAppraisal
													.getCommercialAppraisal());
									//remision x defecto se pasa false porque no es pago, no implica calculo ni nada
									municipalBondService
									.changeExemptionInMunicipalBond(mb,
											false, true, Boolean.FALSE, propertyAppraisal);
								}
//								System.out
//										.println("======>>Exencion por propiedad: "
//												+ ex.getId());
//								System.out
//										.println("======>>Exesi ncion por cadastralcode: "
//												+ exemptionForProperty
//														.getProperty()
//														.getCadastralCode());
//								
//								
//									
								// mb.setReference(mb.getReference() + " " +
								// ex.getReference());
								
							}
							
						}
					}
				} catch (EntryDefinitionNotFoundException e) {
					e.printStackTrace();
				}
			}

		}
		/*
		 * List<MunicipalBond> bonds =
		 * findMunicipalBondsByFiscalPeriodResidentAndEntry(fiscalPeriodId,
		 * residentsIds, entriesIds, pendingStatus);
		 * 
		 * for(MunicipalBond mb : bonds){ try { EntryValueItem entryValueItem =
		 * new EntryValueItem();
		 * entryValueItem.setServiceDate(mb.getServiceDate());
		 * entryValueItem.setMainValue(mb.getBase());
		 * entryValueItem.setInternalTramit(Boolean.FALSE);
		 * municipalBondService.changeExemptionInMunicipalBond(mb, false, true,
		 * findExemption(exemptions, mb.getResident().getId())); //
		 * municipalBondService.createItemsToMunicipalBond(mb, entryValueItem,
		 * false, true, findExemption(exemptions, mb.getResident().getId())); }
		 * catch (EntryDefinitionNotFoundException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } }
		 */
	}

	@SuppressWarnings("unchecked")
	public ExemptionDTO calculateAppraisalGeneral(Exemption exemption) {
		
		//controlar para los de tratamiento especial
		
//		Query query = entityManager
//				.createNamedQuery("Property.findByResidentIdNotDeleted");
//		query.setParameter("residentId", exemption.getResident().getId());
//		List<Property> properties = query.getResultList();
		BigDecimal appraisalForProperties = BigDecimal.ZERO;
		
//		String query1 = "select result.total_apraisal_resident + result.total_apraisal_parent "
//				+ "from  (SELECT COALESCE(sum(d.commercialappraisal),0.00) as total_apraisal_resident, "
//				+ "(SELECT COALESCE(sum(d.commercialappraisal),0.00) "
//				+ " FROM property p INNER JOIN domain d on (d.currentproperty_id = p.id) "
//				+ "WHERE d.resident_id = ?0 and p.deleted = false) as total_apraisal_parent "
//				+ "FROM property p "
//				+ "INNER JOIN domain d on (d.currentproperty_id = p.id) "
//				+ "WHERE d.resident_id = ?1 and p.deleted = false) result";
		
		String query1 = "SELECT COALESCE(sum(d.commercialappraisal),0.00) as total_apraisal_resident,"
				+ "(SELECT COALESCE(sum(d.commercialappraisal),0.00) "
				+ "FROM property p "
				+ "INNER JOIN domain d on (d.currentproperty_id = p.id) "
				+ "WHERE d.resident_id = ?0 "
				+ "and p.deleted = false) as total_apraisal_parent, "
				+ "(select coalesce(sum(dom.commercialappraisal),0.00)  "
				+ "from exemptionforproperty efp "
				+ "inner join property pro on (efp.property_id = pro.id) "
				+ "inner join domain dom ON (dom.currentproperty_id = pro.id)  "
				+ "where efp.treatmenttype_itm_id is not null "
				+ "and efp.exemption_id = ?2 and pro.deleted = false) as total_apraisal_special "
				+ "FROM property p "
				+ "INNER JOIN domain d on (d.currentproperty_id = p.id) "
				+ "WHERE d.resident_id = ?1 "
				+ "and p.deleted = false ";
	
	
		Query q = entityManager.createNativeQuery(query1);
		q.setParameter(0, exemption.getPartner()==null? (new BigInteger("-9999")) : exemption.getPartner().getId());
		q.setParameter(1, exemption.getResident().getId());
		q.setParameter(2, exemption.getId());
		
		List<ExemptionDTO> list = NativeQueryResultsMapper.map(q.getResultList(), ExemptionDTO.class);
		
		return (list.isEmpty())? null : list.get(0);
//		appraisalForProperties = q.getResultList();
		
//		for (Property property : properties) {
//			System.out.println("=======> calculateAppraisalGeneral: "
//					+ property.getCadastralCode());
//			System.out.println("=======> resident: "
//					+ exemption.getResident().getName());
//			System.out.println("=======> property code: "
//					+ property.getCadastralCode());
//			System.out.println("=======> property appraisal: "
//					+ property.getCurrentDomain().getCommercialAppraisal());
//			appraisalForProperties = appraisalForProperties.add(property
//					.getCurrentDomain().getCommercialAppraisal());
//		}
//		if (exemption.getPartner() != null) {
//			query = entityManager
//					.createNamedQuery("Property.findByResidentIdNotDeleted");
//			query.setParameter("residentId", exemption.getPartner().getId());
//			properties.clear();
//			properties = query.getResultList();
//			for (Property property : properties) {
//				appraisalForProperties = appraisalForProperties.add(property
//						.getCurrentDomain().getCommercialAppraisal());
//			}
//		}
//		return appraisalForProperties;
	}
	

	public ExemptionDTO calculateAppraisalForProperties(Exemption exemption) {
		
		String query1 = "select (SELECT COALESCE(sum(d4.commercialappraisal),0.00) "
									+ "FROM property p4 "
									+ "	INNER JOIN domain d4 on (d4.currentproperty_id = p4.id) "
									+ " WHERE d4.resident_id =  ?0"
									+ "		and p4.deleted = false) as total_resident, "
						+ "		(SELECT COALESCE(sum(d5.commercialappraisal),0.00) "
									+ "		FROM property p5 "
									+ "		INNER JOIN domain d5 on (d5.currentproperty_id = p5.id) "
									+ "		WHERE d5.resident_id =  ?1 "
									+ "		and p5.deleted = false) as total_pattern,"
							+ "	(select 	COALESCE(sum(dom5.commercialappraisal),0.00) "
									+ "		from exemptionforproperty efp5 "
									+ "		inner join property pro5 on pro5.id = efp5.property_id "
									+ "		inner join domain dom5 on dom5.currentproperty_id = pro5.id "
									+ "		inner join exemption exe5 on (exe5.id = efp5.exemption_id) "
									+ "		where efp5.exemption_id = ?2 "
									+ "		and pro5.deleted = false "
									+ "		and efp5.treatmentType_itm_id is not null ) as total_special ,"
						+ "		COALESCE(sum(dom.commercialappraisal),0.00) as total_fractions "
				+ "		from exemptionforproperty efp "
				+ "		inner join property pro on pro.id = efp.property_id "
				+ "		inner join domain dom on dom.currentproperty_id = pro.id  "
				+ "		inner join exemption exe on (exe.id = efp.exemption_id) "
				+ "		where efp.exemption_id = ?2 "
				+ "		and pro.deleted = false";

		Query q = entityManager.createNativeQuery(query1);
	
		q.setParameter(0, exemption.getResident().getId());
		q.setParameter(1, ((exemption.getPartner()==null)? new BigInteger("-9999"): exemption.getPartner().getId()));
		q.setParameter(2, exemption.getId());
		
		List<ExemptionDTO> list = NativeQueryResultsMapper.map(q.getResultList(), ExemptionDTO.class);
		
		return (list.isEmpty())? null : list.get(0);
		
		
		
		/*List<ExemptionForProperty> propertiesInEx = exemption
				.getPropertiesInExemption();
		
	
		BigDecimal appraisalForProperties = BigDecimal.ZERO;
		for (ExemptionForProperty exForProperty : propertiesInEx) {
			 
			
			if ((exForProperty.getProperty().getCurrentDomain().getResident()
					.getId() == exemption.getResident().getId())
					&& (!exForProperty.getProperty().getDeleted()))
				System.out.println("=======> calculateAppraisalForProperties: "
						+ exForProperty.getProperty().getCadastralCode());
			System.out.println("=======> resident: "
					+ exemption.getResident().getName());
			System.out.println("=======> property code: "
					+ exForProperty.getProperty().getCadastralCode());
			System.out.println("=======> property appraisal: "
					+ exForProperty.getProperty().getCurrentDomain()
							.getCommercialAppraisal());
			appraisalForProperties = appraisalForProperties.add(exForProperty
					.getProperty().getCurrentDomain().getCommercialAppraisal());
			
			
			
		}
		return appraisalForProperties;*/
	}

	// private EmissionOrder savePreEmissionOrderPropertyTax(String
	// cadastralCode, FiscalPeriod f, Person p, boolean preEmit,boolean isUrban)
	// throws Exception {
	// // TODO Auto-generated method stub
	//
	// Date currentDate = new Date();
	// Entry entry = null;
	// String systemParameterPropertyType = "";
	// if(isUrban){
	// entry = systemParameterService.materialize(Entry.class,
	// "ENTRY_ID_URBAN_PROPERTY");
	// systemParameterPropertyType = "PROPERTY_TYPE_ID_URBAN";
	// }else{
	// entry = systemParameterService.materialize(Entry.class,
	// "ENTRY_ID_RUSTIC_PROPERTY");
	// systemParameterPropertyType = "PROPERTY_TYPE_ID_RUSTIC";
	// }
	//
	// EmissionOrder emissionOrder = new EmissionOrder();
	// emissionOrder.setServiceDate(f.getStartDate());
	// emissionOrder.setDescription(entry.getName() + ": " + cadastralCode);
	// emissionOrder.setEmisor(p);
	//
	// for(Property pro
	// :findPropertyByCadastralCodeAndType(cadastralCode,systemParameterPropertyType)){
	// //calculateCommercialAppraisal(pro);
	//
	// EntryValueItem entryValueItem = new EntryValueItem();
	// entryValueItem.setDescription(emissionOrder.getDescription());
	// entryValueItem.setMainValue(pro.getCurrentDomain().getCommercialAppraisal());
	// entryValueItem.setServiceDate(currentDate);
	// entryValueItem.setReference("Anterior: " + pro.getPreviousCadastralCode()
	// != null ? pro.getPreviousCadastralCode() : "" + " - Nuevo: " +
	// pro.getCadastralCode());
	//
	// MunicipalBond mb =
	// revenueService.createMunicipalBond(pro.getCurrentDomain().getResident(),
	// entry, f, entryValueItem, true, pro.getCurrentDomain(),f);
	//
	// //MunicipalBond mb =
	// revenueService.createMunicipalBond(pro.getCurrentDomain().getResident(),
	// f, entry, null, currentDate, null, MunicipalBondType.EMISSION_ORDER,
	// pro.getCurrentDomain());
	//
	// mb.setCreationDate(currentDate);
	// mb.setCreationTime(currentDate);
	// mb.setEntry(entry);
	// mb.setDescription(entry.getDescription());
	// mb.setFiscalPeriod(f);
	// mb.setBase(pro.getCurrentDomain().getCommercialAppraisal());
	// mb.setGroupingCode(pro.getCadastralCode());
	// if(isUrban){
	// if(pro.getLocation() != null && pro.getLocation().getMainBlockLimit() !=
	// null && pro.getLocation().getMainBlockLimit().getStreet() !=
	// null)mb.setAddress(pro.getLocation().getMainBlockLimit().getStreet().getName());
	// mb.setDescription("IMPUESTO PREDIAL URBANO");
	// }else{
	// mb.setAddress(getAddressForRusticProperty(pro));
	// mb.setDescription("IMPUESTO PREDIAL RUSTICO");
	// }
	//
	// mb.setOriginator(p);
	// mb.setServiceDate(f.getStartDate());
	// mb.setTimePeriod(entry.getTimePeriod());
	// mb.calculateValue();
	// if(mb.getValue().compareTo(new BigDecimal(0)) == -1) mb.setValue(new
	// BigDecimal(0));
	// if(municipalBondService.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(pro.getCurrentDomain().getResident().getId(),
	// entry.getId(), mb.getServiceDate(),mb.getGroupingCode()) == null){
	// emissionOrder.add(mb);
	// }
	// }
	// if(preEmit)savePreEmissionOrder(emissionOrder);
	//
	// return emissionOrder;
	//
	// }

	/**
	 * DirecciÃ³n es el nombre de la parroquia
	 * 
	 * @param pro
	 *            propiedad para la que se va a buscar la direcciÃ³n
	 * @return String
	 */
	private String getAddressForRusticProperty(Property pro) {
		String code = systemParameterService
				.findParameter("TERRITORIAL_DIVISION_CODE_PROVINCE");
		TerritorialDivision province = findProvince(code);
		TerritorialDivision canton = findTerritorialDivision(
				pro.getCadastralCode(), 2, 5, province);
		TerritorialDivision parish = findTerritorialDivision(
				pro.getCadastralCode(), 5, 9, canton);
		return parish.getName();
	}

	/**
	 * Buscar propiedades en base a un tipode propiedad, coincidencias con la
	 * clave catastral, excepto para ciertas propiedades noEmitFor
	 * 
	 * @param cadastralCode
	 *            clave catastral
	 * @param systemParameterName
	 *            parametro para el tipo de propiedad: urbana o rÃºstica
	 * @param noEmitFor
	 *            ids de las propiedades que no se va a tomar en cuenta
	 * @return List<Property>
	 */
	public List<Property> findPropertyByCadastralCodeAndType(
			String cadastralCode, String systemParameterName,
			List<Long> noEmitFor) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Long idType = systemParameterService.findParameter(systemParameterName);
		parameters.put("cadastralCode", cadastralCode);
		parameters.put("idType", idType);
		String sql = "";
		if (noEmitFor == null || noEmitFor.size() == 0) {
			sql = "Property.findByCadastralCodeAndType";
		} else {
			sql = "Property.findByCadastralCodeAndTypeExceptFromSomeOwnerIds";
			parameters.put("ownersIds", noEmitFor);
		}
		List<?> list = crudService.findWithNamedQuery(sql, parameters);
		if (list != null) {
			return (List<Property>) list;
		}
		return new ArrayList<Property>();
	}

	private void savePreEmissionOrder(EmissionOrder e) {
		MunicipalBondStatus mbs = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		for (MunicipalBond m : e.getMunicipalBonds()) {
			m.setMunicipalBondStatus(mbs);
		}
		crudService.create(e);
	}

	private UnbuiltLot copyUnbuiltLot(UnbuiltLot unbuiltLot,
			FiscalPeriod fiscalPeriod) {
		UnbuiltLot aux = new UnbuiltLot();
		aux.setFiscalPeriod(fiscalPeriod);
		aux.setObservation(unbuiltLot.getObservation());
		aux.setProperty(unbuiltLot.getProperty());
		aux.setPropertyUse(unbuiltLot.getPropertyUse());
		aux.setEmited(false);
		return aux;
	}

	@Override
	public void createCopyOfUnbuiltLots(List<UnbuiltLot> unbuiltLots,
			FiscalPeriod fiscalPeriod) {
		for (UnbuiltLot ul : unbuiltLots) {
			UnbuiltLot aux = copyUnbuiltLot(ul, fiscalPeriod);
			crudService.create(aux);
		}
	}

	public Domain update(Domain domain) throws Exception {
		return crudService.update(domain);
	}

	/*
	 * Rene
	 * 2016-08-16
	 * Metodo para obtener los avaluos de la propiedad
	 */
	@Override
	public List<AppraisalsPropertyDTO> findAppraisalsForProperty(
			Long property_id) {
		Query query = entityManager
				.createNativeQuery("select CAST(extract(YEAR from app.date) as INTEGER) AS year_appraisal, "
						+ "app.commercialappraisal AS commercial_appraisal "
						+ "from appraisal app "
						+ "inner JOIN property pro ON (app.property_id = pro.id) "
						+ "where pro.id=?1 "
						+ "order by app.date asc");
		query.setParameter(1, property_id);
		
		List<AppraisalsPropertyDTO> retorno = NativeQueryResultsMapper.map(query.getResultList(), AppraisalsPropertyDTO.class);
		
		return retorno;
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.cadaster.facade.CadasterService#verifyCheckAlreadyAddedPropertyIntoWorkDeal(java.lang.Long, java.lang.Long)
	 */
	@Override
	public WorkDealFraction verifyCheckAlreadyAddedPropertyIntoWorkDeal(Long workDeal_id, Long property_id) {
		// TODO Auto-generated method stub
		//Query query = this.entityManager.createNativeQuery("select count(*) from gimprod.workdealfraction where workdeal_id=?1 and property_id =?2");
		Query query = this.entityManager.createQuery("select wf from WorkDealFraction wf where wf.workDeal.id=:workDeal_id and wf.property.id=:property_id");
		query.setParameter("workDeal_id", workDeal_id);
		query.setParameter("property_id", property_id);
		
		List<WorkDealFraction> result = query.getResultList();
		if(result.isEmpty()){
			return null;
		}
		return result.get(0);
	}
	
	//Jock Samaniego.. 23/09/2016
	public void updateRusticProperty(Property property){
		entityManager.merge(property);		
	}

	@Override
	public LocationPropertySinat findLocationPropertySinat(Long property_id) {
		// TODO Auto-generated method stub
		Query query = entityManager
				.createNamedQuery("LocationPropertySinat.findByProperty");
		query.setParameter("property_id", property_id);
		List<LocationPropertySinat> result = query.getResultList();
		if(result.size()>0){
			return result.get(0);
		}
		
		return null;
	}
		
}