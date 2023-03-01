/**
 * 
 */
package org.gob.gim.revenue.facade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.exception.NonUniqueIdentificationNumberException;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.FiscalPeriodService;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.InvalidEmissionException;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.exception.UserCanNotEmitException;
import org.gob.gim.revenue.service.EmissionOrderService;
import org.gob.gim.revenue.service.EntryService;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.gim.revenue.view.EntryValueItem;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryStructure;
import ec.gob.gim.revenue.model.EntryStructureType;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.StatusChange;
//import ec.gob.gim.revenue.model.adjunct.BinnacleCRVReference;
import ec.gob.gim.security.model.User;

/**
 * @author wilman
 *
 */
@Stateless(name = "RevenueService")
public class RevenueServiceBean implements RevenueService {

	@EJB
	MunicipalBondService municipalBondService;

	@EJB
	EmissionOrderService emissionOrderService;

	@EJB
	CrudService crudService;

	@EJB
	EntryService entryService;

	@EJB
	ResidentService residentService;

	@EJB
	FiscalPeriodService fiscalPeriodService;

	@EJB
	SystemParameterService systemParameterService;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Resident findResidentById(Long residentId) {
		return residentService.find(residentId);
	}

	@Override
	public Resident findResidentByIdentificationNumber(String identificationNumber)
			throws NonUniqueIdentificationNumberException {
		return residentService.find(identificationNumber);
	}

	@Override
	public List<Resident> findResidentByCriteria(String criteria) {
		return residentService.findByCriteria(criteria);
	}

	@Override
	public Entry findEntryById(Long entryId) {
		return entryService.find(entryId);
	}

	@Override
	public Entry findEntryByCode(String code) {
		return entryService.find(code);
	}

	@Override
	public List<Entry> findEntryByCriteria(String criteria) {
		return entryService.findByCriteria(criteria);
	}

	@Override
	public FiscalPeriod findFiscalPeriodById(Long fiscalPeriodId) {
		return fiscalPeriodService.find(fiscalPeriodId);
	}

	@Override
	public List<FiscalPeriod> findFiscalPeriodCurrent(Date currentDate) {
		return fiscalPeriodService.findCurrent(currentDate);
	}

	public MunicipalBond createMunicipalBond(Resident resident, Entry entry, FiscalPeriod fiscalPeriod,
			EntryValueItem entryValueItem, boolean isEmission, Object... facts)
			throws EntryDefinitionNotFoundException {

		return municipalBondService.createMunicipalBond(resident, fiscalPeriod, entry, entryValueItem, isEmission, true,
				facts);
	}

	/*
	 * public MunicipalBond createDeferredMunicipalBond(Resident resident,
	 * FiscalPeriod fiscalPeriod, Entry entry, BigDecimal base, Date serviceDate,
	 * Date expirationDate, String reference, String description, Object ... facts)
	 * throws EntryDefinitionNotFoundException{
	 * 
	 * return municipalBondService.createDeferredMunicipalBond(resident,
	 * fiscalPeriod, entry, base, serviceDate, expirationDate, reference,
	 * description, facts); }
	 */

	private Boolean canUserEmit(User user) {
		String emisorRoleName = systemParameterService.findParameter("ROLE_NAME_EMISOR");
		String preemisorRoleName = systemParameterService.findParameter("ROLE_NAME_PREEMISOR");
		if (user.hasRole(emisorRoleName) || user.hasRole(preemisorRoleName)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private MunicipalBondType findMunicipalBondTypeByRole(User user) {
		String emisorRoleName = systemParameterService.findParameter("ROLE_NAME_EMISOR");
		String preemisorRoleName = systemParameterService.findParameter("ROLE_NAME_PREEMISOR");
		String simertRoleName = systemParameterService.findParameter("ROLE_NAME_SIMERT");
		String urbanRoleName = systemParameterService.findParameter("ROLE_NAME_URBAN_EMISOR");
		String ruralRoleName = systemParameterService.findParameter("ROLE_NAME_RURAL_EMISOR");
		String solarRoleName = systemParameterService.findParameter("ROLE_NAME_SOLAR_EMISOR");
		if ((user.hasRole(emisorRoleName)) || (user.hasRole(simertRoleName)) || (user.hasRole(urbanRoleName))
				|| (user.hasRole(ruralRoleName)) || (user.hasRole(solarRoleName))) {
			return MunicipalBondType.CREDIT_ORDER;
		}
		if (user.hasRole(preemisorRoleName)) {
			return MunicipalBondType.EMISSION_ORDER;
		}
		return null;
	}

	public void checkEmission(MunicipalBond municipalBond) throws InvalidEmissionException {
		for (Item item : municipalBond.getItems()) {
			if (BigDecimal.ZERO.compareTo(item.getTotal()) > 0) {
				throw new InvalidEmissionException();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.revenue.facade.RevenueService#emit(ec.gob.gim.revenue.model
	 * .MunicipalBond)
	 */
	@Override
	public MunicipalBond emit(MunicipalBond municipalBond, User user) throws Exception {
		checkEmission(municipalBond);
		if (canUserEmit(user)) {
			MunicipalBondStatus pendingBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,
					"MUNICIPAL_BOND_STATUS_ID_PENDING");

			MunicipalBondType mt = findMunicipalBondTypeByRole(user);
			if (mt == null) {
				throw new UserCanNotEmitException();
			}

			Date now = Calendar.getInstance().getTime();

			municipalBond.setMunicipalBondType(mt);
			MunicipalBondStatus previousStatus = municipalBond.getMunicipalBondStatus();
			municipalBond.setMunicipalBondStatus(pendingBondStatus);
			municipalBond.setNumber(findNextMunicipalBondValue());
			municipalBond.setEmisionDate(now);
			municipalBond.setEmisionTime(now);
			municipalBond.setEmisionPeriod(fiscalPeriodService.findCurrent(now).get(0).getStartDate());
			municipalBond.setEmitter((Person) user.getResident());
			
			// para separar multas del simert u otros rubros
			// Jock Samaniego
			// 05-01-2020
			String simertCodes = (String) systemParameterService.findParameter("SEPARATE_SIMERT_BONDS_CODES");
			if (simertCodes.indexOf(municipalBond.getEntry().getCode()) != -1) {
				String newGroupingCode = "Obligaci\u00F3n " + municipalBond.getNumber();
				municipalBond.setGroupingCode(newGroupingCode);
			}

			if (municipalBond.getId() != null) {
				municipalBondService.update(municipalBond);
				saveStatusChangeRecord(null, municipalBond, previousStatus, pendingBondStatus, user);
			} else {
				if (municipalBond.getAdjunct() != null) {
					/*
					 * System.out.println("EMITIENDO CON ADJUNTO " + municipalBond.getAdjunct() +
					 * " CODIGO ADJUNTO " + municipalBond.getAdjunct().getCode());
					 */
					Adjunct adjunct = null;
					adjunct = municipalBond.getAdjunct();
					//Ronald Paladines Celi
					//Módulo de Bitácora Digital CRV 
					/*if ((adjunct != null) && (adjunct.getClass() == BinnacleCRVReference.class)){
						BinnacleCRVReference binnacleCRVReference = (BinnacleCRVReference) municipalBond.getAdjunct();
						if (!binnacleCRVReference.getEmitWithoutBinnacle()){
							binnacleCRVReference.setBond(municipalBond);
						}
					}*/ 
					if (municipalBond.getAdjunct().getId() != null) {
						adjunct = entityManager.merge(municipalBond.getAdjunct());
						municipalBond.setAdjunct(adjunct);
						municipalBond.setGroupingCode(adjunct.getCode());
					}
				}
				
				// rfam 2021-08-30 no dejar el groupingcode vacio
				if (municipalBond.getGroupingCode() == null) {
					if(municipalBond.getResident()
							.getIdentificationNumber()!=null){
						municipalBond.setGroupingCode(municipalBond.getResident()
								.getIdentificationNumber());	
					}else{
						municipalBond.setGroupingCode("ID:"+municipalBond.getResident().getId());
					}
					
				}
				
				municipalBondService.save(municipalBond);

				if (mt == MunicipalBondType.EMISSION_ORDER) {
					emissionOrderService.createEmissionOrder(municipalBond, user);
				}
			}

			return municipalBond;

		} else {
			throw new UserCanNotEmitException();
		}
	}

	// public void emit(Long emissionOrderId, Long userId, Date startDate) {
	public void emit(Long emissionOrderId, Person emitter, Date startDate) {
		EmissionOrder eo = findEmissionOrder(emissionOrderId);
		// User user = findUser(userId);
		MunicipalBondStatus pendingBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_PENDING");
		Date now = Calendar.getInstance().getTime();
		for (MunicipalBond municipalBond : loadMunicipalBonds(emissionOrderId)) {
			municipalBond.setMunicipalBondType(MunicipalBondType.CREDIT_ORDER);
			municipalBond.setMunicipalBondStatus(pendingBondStatus);
			municipalBond.setNumber(findNextMunicipalBondValue());
			municipalBond.setEmisionDate(now);
			municipalBond.setEmisionTime(now);
			municipalBond.setEmitter(emitter);
			municipalBond.setEmisionPeriod(startDate);
			// rfarmijosm 2016-01-30 se comenta no es necesario buscar cada rato
			// q se emite
			// municipalBond.setEmisionPeriod(fiscalPeriodService.findCurrent(now).get(0).getStartDate());
		}
		eo.setIsDispatched(true);
		crudService.update(eo);
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBond> loadMunicipalBonds(Long id) {
		Query query = entityManager.createNamedQuery("EmissionOrder.findMunicipalBondsByEmissionOrderId");
		query.setParameter("id", id);
		return query.getResultList();
	}

	private EmissionOrder findEmissionOrder(Long id) {
		Query query = entityManager.createNamedQuery("EmissionOrder.findById");
		query.setParameter("id", id);
		return (EmissionOrder) query.getSingleResult();
	}

	private User findUser(Long id) {
		Query query = entityManager.createNamedQuery("User.findByUserId");
		query.setParameter("userId", id);
		return (User) query.getSingleResult();
	}

	private Long findNextMunicipalBondValue() {
		Query query = entityManager.createNativeQuery("SELECT nextval('gimprod.municipalBondNumber')");
		return ((BigInteger) query.getResultList().get(0)).longValue();
	}

	@Override
	public void emit(List<MunicipalBond> municipalBonds, User user) throws Exception {
		for (MunicipalBond mb : municipalBonds) {
			mb = this.emit(mb, user);
		}
	}

	@Override
	public List<MunicipalBond> findMunicipalBondByResidentIdAndTypeAndStatus(Long residentId,
			MunicipalBondType municipalBondType, Long municipalBondStatusId) {
		return municipalBondService.findByResidentIdAndTypeAndStatus(residentId, municipalBondType,
				municipalBondStatusId);
	}

	@Override
	public List<EntryStructure> findEntryStructureChildrenByType(Long entryParentId,
			EntryStructureType entryStructureType) {
		return entryService.findEntryStructureChildrenByType(entryParentId, entryStructureType);
	}

	@Override
	public List<EntryStructure> findEntryStructureChildren(Long entryParentId) {
		return entryService.findEntryStructureChildren(entryParentId);
	}

	/*
	 * @Override public void calculeMunicipalBond(MunicipalBond mb) {
	 * municipalBondService.calculate(mb); }
	 */

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(List<Long> municipalBondIds, Long municipalBondStatusId, Long userId, String explanation,
			String judicialProcessNumber) {
		// System.out .println("GZ -----> Update in RevenueServiceBean, changing status
		// to id " + municipalBondStatusId);
		User user = entityManager.getReference(User.class, userId);
		MunicipalBondStatus status = entityManager.getReference(MunicipalBondStatus.class, municipalBondStatusId);
		Long reversedStatusId = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_REVERSED");

		Query query = entityManager.createNamedQuery("MunicipalBond.findByIdsToChangeStatus");
		query.setParameter("municipalBondIds", municipalBondIds);
		List<MunicipalBond> bonds = query.getResultList();

		for (MunicipalBond bond : bonds) {
			// System.out.println("GZ -----> Update in RevenueServiceBean");
			MunicipalBondStatus previousStatus = bond.getMunicipalBondStatus();
			bond.setMunicipalBondStatus(status);
			if (reversedStatusId.equals(municipalBondStatusId)) {
				Date now = Calendar.getInstance().getTime();
				bond.setReversedDate(now);
				bond.setReversedTime(now);
				bond.setReversedResolution(explanation);
			}
			if (judicialProcessNumber != null) {
				saveStatusChangeRecord(explanation, judicialProcessNumber, bond, previousStatus, status, user);
			} else {
				saveStatusChangeRecord(explanation, bond, previousStatus, status, user);
			}
		}

		/*
		 * Query query = entityManager.createNamedQuery("MunicipalBond.changeStatus");
		 * query.setParameter("municipalBondIds", municipalBondIds);
		 * query.setParameter("municipalBondStatusId", municipalBondStatusId);
		 * query.executeUpdate();
		 */
	}

	private void saveStatusChangeRecord(String explanation, MunicipalBond bond, MunicipalBondStatus previousStatus,
			MunicipalBondStatus status, User user) {
		StatusChange statusChange = new StatusChange();
		statusChange.setExplanation(explanation);
		statusChange.setMunicipalBond(bond);
		statusChange.setPreviousBondStatus(previousStatus);
		statusChange.setMunicipalBondStatus(status);
		statusChange.setUser(user);
		entityManager.persist(statusChange);
	}

	@SuppressWarnings("unused")
	private void saveStatusChangeRecord(String explanation, String judicialProcessNumber, MunicipalBond bond,
			MunicipalBondStatus previousStatus, MunicipalBondStatus status, User user) {
		StatusChange statusChange = new StatusChange();
		statusChange.setExplanation(explanation);
		statusChange.setMunicipalBond(bond);
		statusChange.setPreviousBondStatus(previousStatus);
		statusChange.setMunicipalBondStatus(status);
		statusChange.setUser(user);
		statusChange.setJudicialProcessNumber(judicialProcessNumber);
		entityManager.persist(statusChange);
	}

	/*
	 * @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) public void
	 * updateReversedDescriptionAndValue(List<Long> selected, String observation) {
	 * Query query = entityManager.createNamedQuery(
	 * "MunicipalBond.updateReverseDescriptionAndValue");
	 * query.setParameter("municipalBondIds", selected);
	 * query.setParameter("observation", observation); query.executeUpdate(); }
	 */

	@SuppressWarnings("unchecked")
	public List<MunicipalBond> findMunicipalBondsForReverseByEmitter(Long residentId, Long emitterId, Date date) {
		if (residentId != null) {
			MunicipalBondStatus pendingBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,
					"MUNICIPAL_BOND_STATUS_ID_PENDING");

			Query query = entityManager
					.createNamedQuery("MunicipalBond.findMunicipalBondByResidentIdAndEmitterAndDate");
			query.setParameter("residentId", residentId);
			query.setParameter("emitterId", emitterId);
			query.setParameter("date", date);
			query.setParameter("statusId", pendingBondStatus.getId());
			return query.getResultList();
		}
		return null;
	}

	@Override
	public Entry findByAccountCode(String accountCode) {
		return entryService.findByAccountCode(accountCode);
	}

	@Override
	/**
	 * @author richard armijos Emitir años futuros, la primer obligacion pertenece a
	 *         este anio, las otras quedan en el aire, hasta pasarlas a la fecha q
	 *         les corresponden
	 */
	public void emitFuture(List<MunicipalBond> municipalBonds, User user) throws Exception {
		MunicipalBondStatus futureBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_FUTURE_EMISION");
		MunicipalBondStatus pendingBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_PENDING");

		// int i=0;
		// la ultima emision para compara con la siguiente
		// para idnetificar si pasa al siguiente anio
		// Date lastDate = new Date();
		Date now = Calendar.getInstance().getTime();
		for (MunicipalBond municipalBond : municipalBonds) {
			checkEmission(municipalBond);
			if (canUserEmit(user)) {

				MunicipalBondType mt = findMunicipalBondTypeByRole(user);
				if (mt == null) {
					throw new UserCanNotEmitException();
				}

				// Date now = Calendar.getInstance().getTime();

				if (yearOfDate(municipalBond.getServiceDate()) > yearOfDate(now)) {
					// comparar si sigo en el mismo años sino pasa al futuro
					// System.out.println("las siguientes ........ "+ i);
					// System.out.println("entra a poner la fecha de emision........:
					// "+municipalBond.getServiceDate());
					// lastDate = municipalBond.getServiceDate();
					// if(isNexYear(lastDate, now)){
					// System.out.println("pasa al siguiente anio "+ i);
					municipalBond.setEmisionDate(municipalBond.getServiceDate());
					municipalBond.setEmisionTime(municipalBond.getServiceDate());
					municipalBond.setMunicipalBondStatus(futureBondStatus);
					/*
					 * }else{ //System.out.println("se mantiene normal "+ i);
					 * municipalBond.setEmisionDate(now); municipalBond.setEmisionTime(now);
					 * municipalBond.setMunicipalBondStatus(pendingBondStatus); //TODO identificar q
					 * numero va a tener sino enviar null y generar la proxima vez
					 * municipalBond.setNumber(findNextMunicipalBondValue());
					 * municipalBond.setEmisionPeriod(fiscalPeriodService.findCurrent(now).get(0).
					 * getStartDate()); }
					 */
				} else {
					// System.out.println("la primer vez ........ "+ i);
					municipalBond.setEmisionDate(now);
					municipalBond.setEmisionTime(now);
					municipalBond.setMunicipalBondStatus(pendingBondStatus);
					// TODO identificar q numero va a tener sino enviar null y generar la proxima
					// vez
					municipalBond.setNumber(findNextMunicipalBondValue());
					municipalBond.setEmisionPeriod(fiscalPeriodService.findCurrent(now).get(0).getStartDate());
					// lastDate = municipalBond.getServiceDate();
				}

				municipalBond.setMunicipalBondType(mt);
				MunicipalBondStatus previousStatus = municipalBond.getMunicipalBondStatus();

				municipalBond.setEmitter((Person) user.getResident());

				if (municipalBond.getId() != null) {
					municipalBondService.update(municipalBond);
					saveStatusChangeRecord(null, municipalBond, previousStatus, pendingBondStatus, user);
				} else {
					if (municipalBond.getAdjunct() != null) {
						/*
						 * System.out.println("EMITIENDO CON ADJUNTO " + municipalBond.getAdjunct() +
						 * " CODIGO ADJUNTO " + municipalBond.getAdjunct().getCode());
						 */
						if (municipalBond.getAdjunct().getId() != null) {
							Adjunct adjunct = entityManager.merge(municipalBond.getAdjunct());
							municipalBond.setAdjunct(adjunct);
							municipalBond.setGroupingCode(adjunct.getCode());
						}
					}

					municipalBondService.save(municipalBond);

					if (mt == MunicipalBondType.EMISSION_ORDER) {
						emissionOrderService.createEmissionOrder(municipalBond, user);
					}
				}

			} else {
				throw new UserCanNotEmitException();
			}
			// i++;
		}
	}

	public int yearOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public boolean isNexYear(Date last, Date now) {
		Calendar cLast = Calendar.getInstance();
		Calendar cNow = Calendar.getInstance();

		cLast.setTime(last);
		cNow.setTime(now);

		int yearLast = cLast.get(Calendar.YEAR);
		int yearNow = cNow.get(Calendar.YEAR);

		// System.out.println("yearLast "+yearLast+" - yearNow "+yearNow);
		if (yearLast == yearNow) {
			return false;
		} else if (yearNow < yearLast) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * @author Rene Ortega Cambiar de estado Municipal Bonds de anios futuros a
	 *         Pendientes para poder cobrarlas
	 */
	public void changeFutureToPendign(List<MunicipalBond> municipalBonds, User user, Person person, String explanation)
			throws Exception {
		// TODO Auto-generated method stub
		/*
		 * System.out.println("Lo que llega al service revenue****---------");
		 * System.out.println("Municipal Bonds:" + municipalBonds);
		 * System.out.println("User:" + user);
		 */
		for (MunicipalBond municipalBond : municipalBonds) {
			checkEmission(municipalBond);
			// System.out.println("Paso de check Emision");
			if (canUserEmit(user)) {
				// System.out.println("Si puede emitir");
				MunicipalBondStatus pendingBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,
						"MUNICIPAL_BOND_STATUS_ID_PENDING");
				MunicipalBondType mt = findMunicipalBondTypeByRole(user);
				if (mt == null) {
					// System.out.println("Usuario no puede emitir MT");
					throw new UserCanNotEmitException();
				}

				// System.out.println("Iniciando a setear valores");

				Date now = Calendar.getInstance().getTime();

				MunicipalBondStatus previous = municipalBond.getMunicipalBondStatus();

				municipalBond.setMunicipalBondStatus(pendingBondStatus);
				/*
				 * municipalBond
				 * .setExpirationDate(getExpirationDateChangeFutureStatusToPendingStatus( now,
				 * municipalBond.getEmisionDate(), municipalBond.getExpirationDate()));
				 */
				municipalBond.setNumber(findNextMunicipalBondValue());
				municipalBond.setEmisionDate(now);
				municipalBond.setEmisionPeriod(fiscalPeriodService.findCurrent(now).get(0).getStartDate());
				// municipalBond.setEmitter(person);
				// System.out.println("Antes de guardar municipal bond");
				municipalBondService.update(municipalBond);
				// System.out.println("Antes de guardar status change");
				saveStatusChangeRecord(explanation, municipalBond, previous, pendingBondStatus, user);

			} else {
				// System.out.println("No puede emitir");
				throw new UserCanNotEmitException();
			}

		}

	}

	public static Date getExpirationDateChangeFutureStatusToPendingStatus(Date now, Date emissionDate,
			Date expirationDate) {
		Calendar firstDate = Calendar.getInstance();
		firstDate.setTime(emissionDate);
		firstDate.set(Calendar.HOUR, 0);
		firstDate.set(Calendar.MINUTE, 0);
		firstDate.set(Calendar.SECOND, 0);
		firstDate.set(Calendar.MILLISECOND, 0);

		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(expirationDate);
		lastDate.set(Calendar.HOUR, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		lastDate.set(Calendar.MILLISECOND, 0);

		Calendar present = Calendar.getInstance();
		present.setTime(now);
		present.set(Calendar.HOUR, 0);
		present.set(Calendar.MINUTE, 0);
		present.set(Calendar.SECOND, 0);
		present.set(Calendar.MILLISECOND, 0);

		Calendar diferenceDays = Calendar.getInstance();
		diferenceDays.setTimeInMillis(lastDate.getTimeInMillis() - firstDate.getTimeInMillis());

		int days = diferenceDays.get(Calendar.DAY_OF_YEAR);
		// System.out.println("Diferencia = " + days);

		present.add(Calendar.DATE, days);
		return present.getTime();
	}

	@Override
	public MunicipalBond emit(MunicipalBond municipalBond, User user, MunicipalBondStatus status) throws Exception {
		checkEmission(municipalBond);
		if (canUserEmit(user)) {
			/*
			 * MunicipalBondStatus pendingBondStatus = systemParameterService
			 * .materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
			 */

			MunicipalBondType mt = findMunicipalBondTypeByRole(user);
			if (mt == null) {
				throw new UserCanNotEmitException();
			}

			Date now = Calendar.getInstance().getTime();

			municipalBond.setMunicipalBondType(mt);
			MunicipalBondStatus previousStatus = municipalBond.getMunicipalBondStatus();
			municipalBond.setMunicipalBondStatus(status);
			municipalBond.setNumber(findNextMunicipalBondValue());
			municipalBond.setEmisionDate(now);
			municipalBond.setEmisionTime(now);
			municipalBond.setEmisionPeriod(fiscalPeriodService.findCurrent(now).get(0).getStartDate());
			municipalBond.setEmitter((Person) user.getResident());

			// para separar multas del simert u otros rubros
			// Jock Samaniego
			// 05-01-2020
			String simertCodes = (String) systemParameterService.findParameter("SEPARATE_SIMERT_BONDS_CODES");
			if (simertCodes.indexOf(municipalBond.getEntry().getCode()) != -1) {
				String newGroupingCode = "Obligación " + municipalBond.getNumber();
				municipalBond.setGroupingCode(newGroupingCode);
			}

			if (municipalBond.getId() != null) {
				municipalBondService.update(municipalBond);
				saveStatusChangeRecord(null, municipalBond, previousStatus, status, user);
			} else {
				if (municipalBond.getAdjunct() != null) {
					/*
					 * System.out.println("EMITIENDO CON ADJUNTO " + municipalBond.getAdjunct() +
					 * " CODIGO ADJUNTO " + municipalBond.getAdjunct().getCode());
					 */
					if (municipalBond.getAdjunct().getId() != null) {
						Adjunct adjunct = entityManager.merge(municipalBond.getAdjunct());
						municipalBond.setAdjunct(adjunct);
						municipalBond.setGroupingCode(adjunct.getCode());
					}
				}

				municipalBondService.save(municipalBond);

				if (mt == MunicipalBondType.EMISSION_ORDER) {
					emissionOrderService.createEmissionOrder(municipalBond, user);
				}
			}

			return municipalBond;

		} else {
			throw new UserCanNotEmitException();
		}
	}

	public MunicipalBond update(MunicipalBond municipalBond, MunicipalBondStatus previousStatus,
			MunicipalBondStatus currentStatus, User user, String explanation) throws Exception {

		if (municipalBond.getId() != null) {
			MunicipalBond updatedBond = municipalBondService.update(municipalBond);
			saveStatusChangeRecord(explanation, updatedBond, previousStatus, currentStatus, user);
			return updatedBond;
		}
		return null;
	}
	
	public MunicipalBond pullApartMunicipalBond(MunicipalBond municipalBond) throws Exception{
		String newGroupingCode = municipalBond.getGroupingCode()+ " - " + municipalBond.getNumber();
		municipalBond.setGroupingCode(newGroupingCode);
		MunicipalBond updatedBond = municipalBondService.update(municipalBond);
		return updatedBond;
	}

}
