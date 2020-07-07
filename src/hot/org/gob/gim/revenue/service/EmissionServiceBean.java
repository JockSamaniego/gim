/**
 * 
 */
package org.gob.gim.revenue.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.exception.IdentificationNumberExistsException;
import org.gob.gim.common.exception.IdentificationNumberSizeException;
import org.gob.gim.common.exception.IdentificationNumberWrongException;
import org.gob.gim.common.exception.InvalidIdentificationNumberException;
import org.gob.gim.common.exception.InvalidIdentificationNumberFinishedException;
import org.gob.gim.common.exception.NonUniqueIdentificationNumberException;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.view.EntryValueItem;
import org.gob.gim.ws.service.InfringementEmisionResponse;
import org.gob.loja.gim.ws.dto.InfringementEmisionDetail;
import org.gob.loja.gim.ws.dto.ant.BondDTO;
import org.gob.loja.gim.ws.dto.ant.PreemissionInfractionRequest;
import org.gob.loja.gim.ws.exception.AccountIsBlocked;
import org.gob.loja.gim.ws.exception.AccountIsNotActive;
import org.gob.loja.gim.ws.exception.EmissionOrderNotGenerate;
import org.gob.loja.gim.ws.exception.EmissionOrderNotSave;
import org.gob.loja.gim.ws.exception.EntryNotFound;
import org.gob.loja.gim.ws.exception.FiscalPeriodNotFound;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.TaxpayerNonUnique;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;

import ec.gob.gim.common.model.Address;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Gender;
import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.DTO.ReportEmissionDTO;
import ec.gob.gim.revenue.model.adjunct.InfringementANTReference;
import ec.gob.gim.revenue.model.criteria.ReportEmissionCriteria;
import ec.gob.gim.security.model.User;

/**
 * @author Rene Ortega
 * @Fecha 2017-02-02
 */
@Stateless(name = "EmissionService")
public class EmissionServiceBean implements EmissionService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	MunicipalBondService municipalBondService;

	@EJB
	RevenueService revenueService;

	@EJB
	SystemParameterService systemParameterService;

	@EJB
	ResidentService residentService;

	@Override
	public List<ReportEmissionDTO> findEmissionReport(
			ReportEmissionCriteria criteria) {
		// TODO Auto-generated method stub
		try {

			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			// System.out.println(dt1.format(criteria.getStartDate()));
			Query query = entityManager
					.createNativeQuery("select * from reports.sp_reporte_emision(?1, ?2, ?3, ?4)");
			query.setParameter(1, dt1.format(criteria.getStartDate()));
			query.setParameter(2, dt1.format(criteria.getEndDate()));
			query.setParameter(3, criteria.getStatus_ids());
			query.setParameter(4, criteria.getAccount_id());

			List<ReportEmissionDTO> retorno = NativeQueryResultsMapper.map(
					query.getResultList(), ReportEmissionDTO.class);

			return retorno;
		} catch (Exception e) {
			// System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ReportEmissionDTO> findEmissionReportOtherDetails(
			ReportEmissionCriteria criteria) {
		// TODO Auto-generated method stub
		try {

			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			// System.out.println(dt1.format(criteria.getStartDate()));
			Query query = entityManager
					.createNativeQuery("select * from reports.sp_reporte_emision_other_details(?1, ?2, ?3, ?4)");
			query.setParameter(1, dt1.format(criteria.getStartDate()));
			query.setParameter(2, dt1.format(criteria.getEndDate()));
			query.setParameter(3, criteria.getStatus_ids());
			query.setParameter(4, criteria.getAccount_id());

			List<ReportEmissionDTO> retorno = NativeQueryResultsMapper.map(
					query.getResultList(), ReportEmissionDTO.class);

			return retorno;
		} catch (Exception e) {
			// System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public InfringementEmisionResponse generateANTEmissionInfringement(
			String username, PreemissionInfractionRequest params)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked {
		InfringementEmisionResponse response = new InfringementEmisionResponse();
		response.setStatus("ERROR");

		response.setDetail("Contribuyente Identificacion: "
				+ params.getInfractor().getIdentificationNumber());

		User user = findUser(username);

		if (countANTInfringementOnReference(
				params.getInfraction().getCitationNumber()).intValue() <= 0) {

			try {

				if (params.getInfraction().getInfractionDate() == null) {
					response.setMessage("Emisión fallida. Fecha de infracción faltante");
					return response;
				}

				if (params.getInfraction().getCitationNumber() == null) {
					response.setMessage("Emisión fallida. Nro de citación faltante");
					return response;
				}

				// infractor
				Resident resident = residentService.find(params.getInfractor()
						.getIdentificationNumber());
				if (resident == null) {
					// TODO crear infractor
					Person person = new Person();
					if (params.getInfractor().getIdentificationType() == "PAS") {
						person.setIdentificationType(IdentificationType.PASSPORT);
					} else if (params.getInfractor().getIdentificationType() == "CED") {
						person.setIdentificationType(IdentificationType.NATIONAL_IDENTITY_DOCUMENT);
					} else if (params.getInfractor().getIdentificationType() == "RUC") {
						person.setIdentificationType(IdentificationType.TAXPAYER_DOCUMENT);
					} else {
						person.setIdentificationType(IdentificationType.PASSPORT);
					}
					person.setFirstName(params.getInfractor().getFirstName());
					person.setLastName(params.getInfractor().getLastName());
					person.setEmail(params.getInfractor().getEmail());
					person.setIdentificationNumber(params.getInfractor()
							.getIdentificationNumber());

					if (params.getInfractor().getGender() != null) {
						if (params.getInfractor().getGender() == "CODE_MASC") {
							person.setGender(Gender.MALE);
						} else if (params.getInfractor().getGender() == "CODE_FEME") {
							person.setGender(Gender.FEMALE);
						} else {
							person.setGender(Gender.MALE);
						}
					} else {
						person.setGender(Gender.MALE);
					}

					person.setName(params.getInfractor().getName());
					person.setCountry(params.getInfractor().getCountry());
					person.setAddresses(new ArrayList<Address>());
					person.setCurrentAddress(new Address());
					// Direccion Actual
					person.getCurrentAddress().setStreet(
							params.getInfractor().getAddress());
					person.getCurrentAddress().setPhoneNumber(
							params.getInfractor().getPhoneNumber());
					person.getCurrentAddress().setPhoneNumber(
							params.getInfractor().getMobileNumber());
					person.getCurrentAddress().setCity("Loja");
					person.getCurrentAddress().setCountry(params.getInfractor().getCountry());

					resident = saveResident(person);

					if (resident == null) {
						response.setMessage("Emisión fallida. Contribuyente No Encontrado");
						return response;
					}

				}

				Entry entry = revenueService.findEntryByCode(params
						.getAccountCode());
				if (entry == null) {
					response.setMessage("Emisión fallida. Rubro No Encontrado");
					return response;
				}

				Date currentDate = java.util.Calendar.getInstance().getTime();
				List<FiscalPeriod> fiscalPeriods = revenueService
						.findFiscalPeriodCurrent(currentDate);

				FiscalPeriod fiscalPeriodCurrent = fiscalPeriods != null
						&& !fiscalPeriods.isEmpty() ? fiscalPeriods.get(0)
						: null;

				if (fiscalPeriodCurrent == null) {
					response.setMessage("Emisión fallida. Periódo Fiscal No Encontrado");
					return response;
				}

				if (user == null) {
					response.setMessage("Emisión fallida. Usuario Inválido");
					return response;
				}

				if (!checkUserRole(user.getId())) {
					response.setMessage("Emisión fallida. Permisos insuficientes para el usuario");
					return response;
				}

				Person emitter = findPersonFromUser(user.getId());
				if (emitter == null) {
					response.setMessage("Emisión fallida. Emisor no existe");
					return response;
				}

				EntryValueItem entryValueItem = new EntryValueItem();
				entryValueItem.setDescription(entry.getName());
				entryValueItem.setServiceDate(currentDate);
				entryValueItem.setAmount(BigDecimal.ONE);
				// TODO sacar en base a %
				BigDecimal value = (params.getInfraction().getPercentSBU()
						.multiply(fiscalPeriodCurrent
								.getBasicSalaryUnifiedForRevenue()))
						.divide(BigDecimal.valueOf(100.0));
				entryValueItem.setMainValue(value);

				// infraccion a confirmar resto de datos devueltos por ANT
				MunicipalBondStatus toConfirmBondStatus = systemParameterService
						.materialize(MunicipalBondStatus.class,
								"MUNICIPAL_BOND_STATUS_ID_TO_CONFIRM");

				MunicipalBond mb = revenueService.createMunicipalBond(resident,
						entry, fiscalPeriodCurrent, entryValueItem, true);
				if (mb.getResident().getCurrentAddress() != null) {
					mb.setAddress(mb.getResident().getCurrentAddress()
							.getStreet());
				}

				mb.setEmitter(emitter);
				mb.setOriginator(emitter);

				// start Adjunt
				InfringementANTReference ant = new InfringementANTReference();
				ant.setCitationNumber(params.getInfraction()
						.getCitationNumber());
				ant.setInfringementDate(params.getInfraction()
						.getInfractionDate());
				ant.setInfringementType(params.getInfraction().getType());
				ant.setLostPoints(params.getInfraction().getPoints());
				ant.setNumberPlate(params.getInfraction().getPlate());
				ant.setOrigin(params.getInfraction().getOrigin());
				ant.setTransitAgent(findResidentByIdentificationNumber(params
						.getInfraction().getIdentificationAgent()));
				mb.setAdjunct(ant);
				// end Adjunt

				mb.setReference(params.getInfraction().getLiteralDescription());
				mb.setDescription(params.getInfraction().getObservation());
				mb.setBondAddress(params.getInfraction().getPlace());

				mb.setServiceDate(new Date());
				mb.setCreationTime(new Date());
				// TODO ver porque no siempre existe placa
				mb.setGroupingCode(params.getInfraction().getPlate() == null ? params
						.getInfractor().getIdentificationNumber() : params
						.getInfraction().getPlate());
				// TODO sacar en base al % de SBU
				mb.setBase(value); // cantidad de consumo

				mb.setTimePeriod(entry.getTimePeriod());
				mb.calculateValue();
				mb.setMunicipalBondStatus(toConfirmBondStatus);
				mb.setMunicipalBondType(MunicipalBondType.EMISSION_ORDER);
				mb.setEmisionPeriod(findEmisionPeriod());

				MunicipalBond createdBond = revenueService.emit(mb, user,
						toConfirmBondStatus);

				response.setMunicipalBondId(createdBond.getId());
				response.setMunicipalBondNumber(createdBond.getNumber());
				response.setBond(mapMuniciBondToBond(createdBond));
				response.setMessage("Foto-multa emitida con éxito");
				response.setStatus("OK");

			} catch (NonUniqueIdentificationNumberException e) {
				response.setMessage("Emisión fallida. Número de identificación No Único");
				response.setDetail(e.toString());
			} catch (EntryDefinitionNotFoundException e) {
				response.setMessage("Emisión fallida. Orden de emisión No Generada");
				response.setDetail(e.toString());
			} catch (Exception e) {
				response.setMessage("Emisión fallida. Orden de emisión No Guardada");
				response.setDetail(e.toString());
				e.printStackTrace();
			}
		} else {
			response.setMessage("Emisión fallida. La foto multa ya consta registrada en el sistema");
		}

		return response;

	}

	@Override
	public InfringementEmisionResponse confirmANTEmissionInfringement(
			String name, String password,
			InfringementEmisionDetail emisionDetail) throws TaxpayerNotFound,
			TaxpayerNonUnique, EntryNotFound, FiscalPeriodNotFound,
			EmissionOrderNotGenerate, EmissionOrderNotSave, InvalidUser,
			AccountIsNotActive, AccountIsBlocked, Exception {
		InfringementEmisionResponse response = new InfringementEmisionResponse();
		response.setStatus("ERROR");

		if (emisionDetail.getExpirationDate() == null) {
			response.setMessage("Emisión fallida. Fecha de expiración falante");
			return response;
		}

		User user = findUser(name);

		if (user == null) {
			response.setMessage("Emisión fallida. Usuario Inválido");
			return response;
		}

		if (!checkUserRole(user.getId())) {
			response.setMessage("Emisión fallida. Permisos insuficientes para el usuario");
			return response;
		}

		MunicipalBondStatus pendingBondStatus = systemParameterService
				.materialize(MunicipalBondStatus.class,
						"MUNICIPAL_BOND_STATUS_ID_PENDING");

		MunicipalBond bond = municipalBondService
				.findMunicipalBondByNumber(emisionDetail
						.getMunicipalBondNumber());
		MunicipalBondStatus previousBondStatus = bond.getMunicipalBondStatus();
		bond.setMunicipalBondStatus(pendingBondStatus);
		bond.setExpirationDate(emisionDetail.getExpirationDate());

		MunicipalBond updateBond = revenueService.update(bond,
				previousBondStatus, pendingBondStatus, user,
				"Confirmacion de infraccion ANT");

		response.setMunicipalBondId(updateBond.getId());
		response.setMunicipalBondNumber(updateBond.getNumber());
		response.setMessage("Infracción confirmada");
		response.setStatus("OK");

		return response;

	}

	private Long countANTInfringementOnReference(String citationNumber) {
		Query query = entityManager
				.createNamedQuery("InfringementANTReference.countInfringementByCitationNumber");
		return (Long) query.setParameter("citationNumber",
				citationNumber.trim()).getSingleResult();
	}

	private Resident findResidentByIdentificationNumber(String identificacion)
			throws InvalidUser {
		try {
			Query query = entityManager
					.createNamedQuery("Resident.findByIdentificationNumber");
			query.setParameter("identificationNumber", identificacion);
			return (Resident) query.getSingleResult();
		} catch (NoResultException e) {
			throw new InvalidUser();
		} catch (NonUniqueResultException e) {
			throw new InvalidUser();
		}
	}

	/**
	 * comprobar que el usuario tenga el rol emisor
	 * 
	 * @author rfam
	 * @since 2019-10-25
	 * @return
	 */
	private Boolean checkUserRole(Long userId) {
		Long roleId = systemParameterService
				.findParameter("FOTO_MULTA_PREEMISOR");
		String sql = "select count(*) from role__user as ru where ru.users_id = :userId and ru.roles_id = :roleId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);
		BigInteger quantity = (BigInteger) query.getSingleResult();
		return quantity.intValue() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	private User findUser(String name) {
		Query query = entityManager.createNamedQuery("User.findByUsername");
		query.setParameter("name", name);
		User user = null;

		@SuppressWarnings("unchecked")
		List<User> users = query.getResultList();
		if (users != null && users.size() == 1) {
			user = users.get(0);
		}
		return user;
	}

	private Person findPersonFromUser(Long userId) throws InvalidUser {
		try {
			Query query = entityManager
					.createNamedQuery("User.findResidentByUserId");
			query.setParameter("userId", userId);
			Person person = null;
			person = (Person) query.getSingleResult();
			return person;
		} catch (NoResultException e) {
			throw new InvalidUser();
		} catch (NonUniqueResultException e) {
			throw new InvalidUser();
		}
	}

	private Date findEmisionPeriod() {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);
		return calendar.getTime();

	}

	@SuppressWarnings("unused")
	private Resident saveResident(Resident resident)
			throws IdentificationNumberWrongException {
		try {
			if (resident.isIdentificationNumberValid(resident
					.getIdentificationNumber())) {
				return residentService.saveResident(resident);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	private BondDTO mapMuniciBondToBond(MunicipalBond mb){
		final String patternDate = "dd/MM/yyyy";
		final String patternDateTime = "dd/MM/yyyy' 'HH:mm:ss";
		final String patternTime = "HH:mm:ss";
				BondDTO bond = new BondDTO();
		bond.setAddress(mb.getAddress());
		bond.setApplyInterest(mb.getApplyInterest());
		bond.setBalance(mb.getBalance());
		bond.setBase(mb.getBase());
		bond.setBondAddress(mb.getBondAddress());
		bond.setCreationDate(DateUtils.format(mb.getCreationDate(), patternDate));
		bond.setCreationTime(DateUtils.format(mb.getCreationTime(), patternTime));
		bond.setDescription(mb.getDescription());
		bond.setDiscount(mb.getDiscount());
		bond.setEmisionDate(DateUtils.format(mb.getEmisionDate(),patternDate));
		bond.setEmisionPeriod(DateUtils.format(mb.getEmisionPeriod(), patternDate));
		bond.setEmisionTime(DateUtils.format(mb.getEmisionTime(), patternTime));
		bond.setExempt(mb.getExempt());
		bond.setExpirationDate(DateUtils.format(mb.getExpirationDate(), patternDate));
		bond.setGroupingCode(mb.getGroupingCode());
		bond.setId(mb.getId());
		bond.setInterest(mb.getInterest());
		bond.setInternalTramit(mb.getInternalTramit());
		bond.setIsExpirationDateDefined(mb.getIsExpirationDateDefined());
		bond.setIsNoPasiveSubject(mb.getIsNoPasiveSubject());
		bond.setLiquidationDate(DateUtils.format(mb.getLiquidationDate(), patternDate));
		bond.setLiquidationTime(DateUtils.format(mb.getLiquidationTime(), patternTime));
		bond.setNonTaxableTotal(mb.getNonTaxableTotal());
		bond.setNumber(mb.getNumber());
		bond.setPaidTotal(mb.getPaidTotal());
		bond.setPreviousPayment(mb.getPreviousPayment());
		bond.setPrintingsNumber(mb.getPrintingsNumber());
		bond.setReference(mb.getReference());
		bond.setReversedDate(DateUtils.format(mb.getReversedDate(), patternDate));
		bond.setReversedResolution(mb.getReversedResolution());
		bond.setReversedTime(DateUtils.format(mb.getReversedTime(), patternTime));
		bond.setServiceDate(DateUtils.format(mb.getServiceDate(), patternDate));
		bond.setSurcharge(mb.getSurcharge());
		bond.setTaxableTotal(mb.getTaxableTotal());
		bond.setTaxesTotal(mb.getTaxesTotal());
		bond.setValue(mb.getValue());
		
		return bond;
	}

}