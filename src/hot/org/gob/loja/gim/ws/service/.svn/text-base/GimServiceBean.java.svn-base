package org.gob.loja.gim.ws.service;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.cadaster.facade.CadasterService;
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
import org.gob.loja.gim.ws.dto.BondReport;
import org.gob.loja.gim.ws.dto.EmisionDetail;
import org.gob.loja.gim.ws.dto.RealEstate;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.StatementReport;
import org.gob.loja.gim.ws.dto.Taxpayer;
import org.gob.loja.gim.ws.exception.AccountIsBlocked;
import org.gob.loja.gim.ws.exception.AccountIsNotActive;
import org.gob.loja.gim.ws.exception.EmissionOrderNotGenerate;
import org.gob.loja.gim.ws.exception.EmissionOrderNotSave;
import org.gob.loja.gim.ws.exception.EntryNotFound;
import org.gob.loja.gim.ws.exception.FiscalPeriodNotFound;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.RealEstateNotFound;
import org.gob.loja.gim.ws.exception.TaxpayerNonUnique;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;
import org.gob.loja.gim.ws.exception.TaxpayerNotSaved;
import org.loxageek.common.ws.ReflectionUtil;

import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.common.model.Address;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.LegalEntity;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.adjunct.ANTReference;
import ec.gob.gim.security.model.User;

@Stateless(name="GimService")
@Interceptors({SecurityInterceptor.class})
public class GimServiceBean implements GimService{

	@PersistenceContext
    private EntityManager em;
	
	@EJB
	ResidentService residentService;
	
	@EJB
	CadasterService cadasterService;
	
	@EJB
	RevenueService revenueService;
	
	@EJB
	SystemParameterService systemParameterService;
	
	@Override
	public Taxpayer findTaxpayer(ServiceRequest request)
			throws TaxpayerNotFound, TaxpayerNonUnique, InvalidUser, AccountIsNotActive, AccountIsBlocked {
		String identificationNumber = request.getIdentificationNumber();
		Taxpayer taxpayer = findTaxpayer(identificationNumber);
		System.out.println("::::::findTaxpayer Found: " + taxpayer != null ? taxpayer.getId() : "NULL");
		return taxpayer;
	}
	
	@Override
	public Map<String, Object> findTaxpayer(String name, String password, String identificationNumber) 
		throws TaxpayerNotFound, TaxpayerNonUnique, InvalidUser, AccountIsNotActive, AccountIsBlocked{
		Taxpayer taxpayer = findTaxpayer(identificationNumber);
		System.out.println("::::::findTaxpayer Found: " + taxpayer != null ? taxpayer.getId() : "NULL");
		return ReflectionUtil.getAsMap(taxpayer); 
	}

	@Override
	public Boolean saveTaxpayer(ServiceRequest request, Taxpayer taxpayer)
			throws InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNotSaved {
		Resident resident = getInstanceResident(taxpayer); 
		try{
			save(resident);
			System.out.println("::::::saveTaxpayer: SAVED OK!!");
			return Boolean.TRUE;
		}catch(Exception e){
			System.out.println("------------------------------");
			e.printStackTrace();
			throw new TaxpayerNotSaved();
			//return Boolean.FALSE;
		}		
	}
	
	@Override
	public Boolean saveTaxpayer(String name, String password, Map<String, Object> taxpayerAsMap)
			throws InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNotSaved {
		
		Taxpayer taxpayer = (Taxpayer)ReflectionUtil.getFromMap(taxpayerAsMap, Taxpayer.class);	
		Resident resident = getInstanceResident(taxpayer); 
		try{
			save(resident);
			System.out.println("::::::saveTaxpayer: SAVED OK!!");
			return Boolean.TRUE;
		}catch(Exception e){
			throw new TaxpayerNotSaved();
			//return Boolean.FALSE;
		}		
	}
	
	@Override
	public RealEstate findRealEstate(ServiceRequest request,  String cadastralCode) 
			throws RealEstateNotFound, TaxpayerNotFound, TaxpayerNonUnique, InvalidUser, AccountIsNotActive, AccountIsBlocked{
		RealEstate realEstate = findRealEstate(cadastralCode);
		System.out.println(":::::: findRealEstate FOUND: " + realEstate != null ? realEstate.getId() : "null");
		return realEstate;
	}
	
	@Override
	public Map<String, Object> findRealEstate(String name, String password, String cadastralCode) 
			throws RealEstateNotFound, TaxpayerNotFound, TaxpayerNonUnique, InvalidUser, AccountIsNotActive, AccountIsBlocked{
		RealEstate realEstate = findRealEstate(cadastralCode);
		System.out.println(":::::: findRealEstate FOUND: " + realEstate != null ? realEstate.getId() : "null");
		return ReflectionUtil.getAsMap(realEstate); 
	}
	
	@Override
	public Boolean generateEmissionOrder(String name, String password, String identificationNumber, 
			String accountCode, String pplessUser) throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound, FiscalPeriodNotFound, 
			EmissionOrderNotGenerate, EmissionOrderNotSave, InvalidUser, AccountIsNotActive, AccountIsBlocked{
		
		try{
			Resident resident = residentService.find(identificationNumber);
			if (resident == null){
				throw new TaxpayerNotFound();
			}
			//Entry entry = revenueService.findByAccountCode(accountCode);
			Entry entry = revenueService.findEntryByCode(accountCode);
			if (entry == null){
				throw new EntryNotFound();
			}
			
			Date currentDate = java.util.Calendar.getInstance().getTime();
			List<FiscalPeriod> fiscalPeriods = revenueService.findFiscalPeriodCurrent(currentDate);
			
			FiscalPeriod fiscalPeriodCurrent = fiscalPeriods != null && !fiscalPeriods.isEmpty() ? fiscalPeriods.get(0) : null; 
			
			if (fiscalPeriodCurrent == null){
				throw new FiscalPeriodNotFound();
			}
			
			EntryValueItem entryValueItem = new EntryValueItem();
			entryValueItem.setDescription(entry.getName());
			entryValueItem.setServiceDate(currentDate);
			entryValueItem.setAmount(BigDecimal.ONE);
			//entryValueItem.setMainValue(mainValue)
							
			MunicipalBond mb = revenueService.createMunicipalBond(resident, entry, fiscalPeriodCurrent, entryValueItem, true);
			mb.setGroupingCode(identificationNumber);
			if (mb.getResident().getCurrentAddress() != null) {
				mb.setAddress(mb.getResident().getCurrentAddress().getStreet());
			}
			User user = findUser(name, password);
			if (user == null){
				throw new InvalidUser();
			}
			Person emitter = findPersonFromUser(user.getId());
			mb.setEmitter(emitter);
			mb.setOriginator(emitter);
			mb.setDescription(name + ": " + pplessUser);
			
			///
			revenueService.emit(mb, user);
			///
			System.out.println("=========> MUNICIPAL BOND: ");
			System.out.println("===> RESIDENT: " + mb.getResident().getName());
			System.out.println("===> DIRECCION: " + mb.getAddress());
			System.out.println("===> DESCRIPCION: " + mb.getDescription());
			System.out.println("===> BASE: " + mb.getBase());
			System.out.println("===> CREATIONDATE: " + mb.getCreationDate());
			System.out.println("===> EMISSIONDATE: " + mb.getEmisionDate());
			System.out.println("===> EMITTER: " + mb.getEmitter().getName());
			System.out.println("===> ORIGINATOR: " + mb.getOriginator().getName());
			System.out.println("===> EXPIRATIONDATE: " + mb.getExpirationDate());
			System.out.println("===> EMISSIONPERIOD: " + mb.getEmisionPeriod());
			System.out.println("===> ENTRY: " + mb.getEntry().getName() + " - " + mb.getEntry().getCode());
			System.out.println("===> FISCALPERIOD: " + mb.getFiscalPeriod().getName());
			System.out.println("===> ITEMS: " + mb.getItems().size());
			System.out.println("===> TOTAL: " + mb.getPaidTotal());
			System.out.println("===> VALUE: " + mb.getValue());
			System.out.println("===> NUMBER: " + mb.getNumber());
			System.out.println("===> MUNICIPALBONDSTATUS: " + (mb.getMunicipalBondStatus() != null ? mb.getMunicipalBondStatus().getName():null));
			System.out.println("===> MUNICIPALBONDTYPE: " + (mb.getMunicipalBondType() != null ? mb.getMunicipalBondType():null));
			System.out.println("===> MUNICIPALBONDLEGALSTATUS: " + (mb.getLegalStatus() != null ? mb.getLegalStatus().name() : null));
			
			System.out.println("::::::generateEmissionOrder: SAVED OK!!");
			return Boolean.TRUE;			
		}catch(NonUniqueIdentificationNumberException e){
			throw new TaxpayerNonUnique();
		} catch (EntryDefinitionNotFoundException e) {
			throw new EmissionOrderNotGenerate();
		} catch (Exception e) {
			throw new EmissionOrderNotSave();
		}
		//return Boolean.FALSE;
	}
	
	private Resident getInstanceResident(Taxpayer taxpayer) throws TaxpayerNotSaved{
		String identificationNumber = taxpayer.getIdentificationNumber();
		Resident resident = null;
		if (taxpayer.getId() == null){
			if (identificationNumber.length() <= 10){
				resident = Person.createInstance(taxpayer);			
			}else{
				resident = LegalEntity.createInstance(taxpayer);
			}
		}else{
			resident = residentService.find(taxpayer.getId());
			if (resident == null){
				throw new TaxpayerNotSaved();
			}
			resident.setEmail(taxpayer.getEmail());
			resident.setIdentificationNumber(identificationNumber);
			if (resident.getCurrentAddress() == null){
				Address currentAddress = new Address(); 
				currentAddress.setCity("Loja");
				currentAddress.setCountry("Ecuador");
				resident.add(currentAddress);
				resident.setCurrentAddress(currentAddress);
			}
			resident.getCurrentAddress().setStreet(taxpayer.getStreet());
			resident.getCurrentAddress().setPhoneNumber(taxpayer.getPhoneNumber());
			
			if (resident instanceof Person){
				((Person)resident).setFirstName(taxpayer.getFirstName());
				((Person)resident).setLastName(taxpayer.getLastName());
				resident.setName(((Person)resident).toString());
			}else{
				resident.setName(taxpayer.getFirstName());
			}
			
		}
		return resident;
	}
	
	private void save(Resident resident) throws 
		IdentificationNumberSizeException, IdentificationNumberWrongException, 
		InvalidIdentificationNumberException, InvalidIdentificationNumberFinishedException, IdentificationNumberExistsException{
		resident.isIdentificationNumberValid(resident.getIdentificationNumber());
		//IdentificationNumberUtil.validateIdentificationNumber(resident.getIdentificationType(), resident.getIdentificationNumber());
		residentService.save(resident);
	}
	
	private Taxpayer findTaxpayer(String identificationNumber) throws TaxpayerNotFound, TaxpayerNonUnique{
		Query query;
		
		if (identificationNumber.length() <= 10)
			query = em.createNamedQuery("Taxpayer.findPersonByIdentification");
		else  
			query = em.createNamedQuery("Taxpayer.findByIdentificationNumber");//query = em.createNamedQuery("Taxpayer.findLegalEntityFullByIdentification");
		query.setParameter("identificationNumber", identificationNumber);
		try{
			Taxpayer taxpayer = (Taxpayer) query.getSingleResult();
			if(taxpayer != null){
				Address currentAddress = findCurrentAddressByTaxpayerId(taxpayer.getId());
				if (currentAddress != null){
					taxpayer.setStreet(currentAddress.getStreet());
					taxpayer.setPhoneNumber(currentAddress.getPhoneNumber());
				}
				return taxpayer;
			}else{
				throw new TaxpayerNotFound();
			}
		}catch(NoResultException e){
			throw new TaxpayerNotFound();
		}catch(NonUniqueResultException e){
			throw new TaxpayerNonUnique();
		}catch(IllegalStateException e){
			throw new TaxpayerNotFound();
		}
	}
	
	private RealEstate findRealEstate(String cadastralCode) throws RealEstateNotFound, TaxpayerNotFound, TaxpayerNonUnique{
		try{
			Query query = em.createNamedQuery("RealEstate.findByCadastralCode");
			query.setParameter("cadastralCode", cadastralCode);
			RealEstate realEstate = (RealEstate)query.getSingleResult();
			if (realEstate != null){
				// Busqueda de datos del propietarios
				Taxpayer owner = findTaxpayer(realEstate.getIdentificationNumber());
				realEstate.setOwner(owner);
				// Busqueda de la parroquia a donde perteneces el predio			
				TerritorialDivision defaultCanton = cadasterService.findDefaultCanton();
				TerritorialDivision parish = cadasterService.findTerritorialDivision(realEstate.getCode(), 5, 9, defaultCanton);
				realEstate.setNameParish(parish != null ? parish.getName() : null);
				
				// Busqueda de las calles de la manzana
				/*query = em.createNamedQuery("BlockLimit.findByBlockId");
				query.setParameter("blockId", realEstate.getIdBlock());
				List<BlockLimit> blockLimits= (List<BlockLimit>)query.getResultList();
				if (blockLimits != null && !blockLimits.isEmpty()){
					StringBuffer limits = new StringBuffer();
					for (BlockLimit bl : blockLimits){
						limits = limits.append(bl.getStreet().getName());
						limits = limits.append(", ");
					}
					limits = limits.deleteCharAt(limits.length()-2);//(limits.length()-2, limits.length());
					realEstate.setStreetBlockLimits(limits.toString().trim());
				}*/
				query = em.createNamedQuery("Street.findByBlockId");
				query.setParameter("blockId", realEstate.getIdBlock());
				List<Street> blockLimits= (List<Street>)query.getResultList();
				if (blockLimits != null && !blockLimits.isEmpty()){
					StringBuffer limits = new StringBuffer();
					for (Street bl : blockLimits){
						limits = limits.append(bl.getName());
						limits = limits.append(", ");
					}
					limits = limits.deleteCharAt(limits.length()-2);//(limits.length()-2, limits.length());
					realEstate.setStreetBlockLimits(limits.toString().trim());
				}
				return realEstate;
			}
			throw new RealEstateNotFound();
		}catch(NoResultException e){
			throw new RealEstateNotFound();
		}catch(NonUniqueResultException e){
			throw new RealEstateNotFound();
		}catch(IllegalStateException e){
			throw new RealEstateNotFound();
		}
	}
	
	private Address findCurrentAddressByTaxpayerId(Long TaxpayerId){
		Query query = em.createNamedQuery("Address.findCurrentAddressByResidentId");
		query.setParameter("residentId", TaxpayerId);
		Address currentAddress = (Address) query.getSingleResult();
		return currentAddress; 
	}

	private User findUser(String name, String password){
		String hashPassword = hash(password);		
		Query query = em.createNamedQuery("User.findByUsernameAndPassword");
		query.setParameter("name", name);
		query.setParameter("password", hashPassword);
		User user = null;
		@SuppressWarnings("unchecked")
		List<User> users = query.getResultList(); 
		if(users != null && users.size() == 1){
			user = users.get(0);
		}
		return user;
	}
	
	private String hash(String plainTextPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(plainTextPassword.getBytes("UTF-8"));
			byte[] rawHash = digest.digest();
			return new String(org.jboss.seam.util.Hex.encodeHex(rawHash));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Person findPersonFromUser(Long userId) throws InvalidUser {
		try{
			Query query = em.createNamedQuery("User.findResidentByUserId");
			query.setParameter("userId", userId);
			Person person = null;
			person = (Person) query.getSingleResult();
			return person;
		}catch(NoResultException e){
			throw new InvalidUser();
		}catch(NonUniqueResultException e){
			throw new InvalidUser();
		}
	}

	@Override
	public Boolean generateEmissionOrder(String name, String password,
			String identificationNumber, String accountCode,
			EmisionDetail emisionDetail) throws TaxpayerNotFound,
			TaxpayerNonUnique, EntryNotFound, FiscalPeriodNotFound,
			EmissionOrderNotGenerate, EmissionOrderNotSave, InvalidUser,
			AccountIsNotActive, AccountIsBlocked {
		// TODO Auto-generated method stub
		
		try{
			Resident resident = residentService.find(identificationNumber);
			if (resident == null){
				throw new TaxpayerNotFound();
			}
			//Entry entry = revenueService.findByAccountCode(accountCode);
			Entry entry = revenueService.findEntryByCode(accountCode);
			if (entry == null){
				throw new EntryNotFound();
			}
			
			Date currentDate = java.util.Calendar.getInstance().getTime();
			List<FiscalPeriod> fiscalPeriods = revenueService.findFiscalPeriodCurrent(currentDate);
			
			FiscalPeriod fiscalPeriodCurrent = fiscalPeriods != null && !fiscalPeriods.isEmpty() ? fiscalPeriods.get(0) : null; 
			
			if (fiscalPeriodCurrent == null){
				throw new FiscalPeriodNotFound();
			}
			
			EntryValueItem entryValueItem = new EntryValueItem();
			entryValueItem.setDescription(entry.getName());
			entryValueItem.setServiceDate(currentDate);
			entryValueItem.setAmount(BigDecimal.ONE);
			entryValueItem.setMainValue(emisionDetail.getTotal());
			
			MunicipalBondStatus preEmitBondStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
							
			MunicipalBond mb = revenueService.createMunicipalBond(resident, entry, fiscalPeriodCurrent, entryValueItem, true);
			//mb.setGroupingCode(identificationNumber);
			if (mb.getResident().getCurrentAddress() != null) {
				mb.setAddress(mb.getResident().getCurrentAddress().getStreet());
			}
			User user = findUser(name, password);
			if (user == null){
				throw new InvalidUser();
			}
			Person emitter = findPersonFromUser(user.getId());
			
			mb.setEmitter(emitter);
			mb.setOriginator(emitter);
			
			// start Adjunt
			ANTReference ant = new ANTReference();
			ant.setNumberPlate(emisionDetail.getNumberPlate());
			ant.setAntNumber(emisionDetail.getAntNumber());
			ant.setSpeeding(emisionDetail.getSpeeding());
			ant.setVehicleType(emisionDetail.getVehicleType());
			ant.setServiceType(emisionDetail.getServiceType());
			ant.setCitationDate(emisionDetail.getCitationDate());
			mb.setAdjunct(ant);
			// end Adjunt
			
			mb.setReference(emisionDetail.getReference());
			mb.setDescription(emisionDetail.getDescription());
			mb.setBondAddress(emisionDetail.getAddress());
			
			mb.setServiceDate(new Date());
			mb.setCreationTime(new Date());
			mb.setGroupingCode(emisionDetail.getNumberPlate());

			mb.setBase(emisionDetail.getTotal()); // cantidad de consumo
			
			mb.setTimePeriod(entry.getTimePeriod());
			mb.calculateValue();
			mb.setMunicipalBondStatus(preEmitBondStatus);
			mb.setMunicipalBondType(MunicipalBondType.EMISSION_ORDER);
			//mb.setBondAddress(address);
			mb.setEmisionPeriod(findEmisionPeriod());
			
			///
			//revenueService.emit(mb, user);
			///
			System.out.println("=========> MUNICIPAL BOND: ");
			System.out.println("===> RESIDENT: " + mb.getResident().getName());
			System.out.println("===> DIRECCION: " + mb.getAddress());
			System.out.println("===> DESCRIPCION: " + mb.getDescription());
			System.out.println("===> BASE: " + mb.getBase());
			System.out.println("===> CREATIONDATE: " + mb.getCreationDate());
			System.out.println("===> EMISSIONDATE: " + mb.getEmisionDate());
			System.out.println("===> EMITTER: " + mb.getEmitter().getName());
			System.out.println("===> ORIGINATOR: " + mb.getOriginator().getName());
			System.out.println("===> EXPIRATIONDATE: " + mb.getExpirationDate());
			System.out.println("===> EMISSIONPERIOD: " + mb.getEmisionPeriod());
			System.out.println("===> ENTRY: " + mb.getEntry().getName() + " - " + mb.getEntry().getCode());
			System.out.println("===> FISCALPERIOD: " + mb.getFiscalPeriod().getName());
			System.out.println("===> ITEMS: " + mb.getItems().size());
			System.out.println("===> TOTAL: " + mb.getPaidTotal());
			System.out.println("===> VALUE: " + mb.getValue());
			System.out.println("===> NUMBER: " + mb.getNumber());
			System.out.println("===> MUNICIPALBONDSTATUS: " + (mb.getMunicipalBondStatus() != null ? mb.getMunicipalBondStatus().getName():null));
			System.out.println("===> MUNICIPALBONDTYPE: " + (mb.getMunicipalBondType() != null ? mb.getMunicipalBondType():null));
			System.out.println("===> MUNICIPALBONDLEGALSTATUS: " + (mb.getLegalStatus() != null ? mb.getLegalStatus().name() : null));
			
			System.out.println("::::::generateEmissionOrder: SAVED OK!!");
			
			
			EmissionOrder eo = createEmisionOrder(emitter, "Multas ANT - Rubro: " + entry.getCode());
			eo.add(mb);
			
			em.persist(eo);
			
			return Boolean.TRUE;			
		}catch(NonUniqueIdentificationNumberException e){
			throw new TaxpayerNonUnique();
		} catch (EntryDefinitionNotFoundException e) {
			throw new EmissionOrderNotGenerate();
		} catch (Exception e) {
			throw new EmissionOrderNotSave();
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
	
	private EmissionOrder createEmisionOrder(Person emisor, String description){
		EmissionOrder emissionOrder = new EmissionOrder();
		emissionOrder.setServiceDate(new Date());
		emissionOrder.setDescription(description);
		emissionOrder.setEmisor(emisor);
		return emissionOrder;
	}

	/**
	 * Genera el reporte de obligaciones por tipo (estado de obligaciones)
	 * 
	 */
	@Override
	public StatementReport buildReport(ServiceRequest request, Date startDate, Date endDate, String reportType, Long entryId) throws InvalidUser {
		System.out.println("   desde "+startDate+"     hasta "+endDate);
		//controla q safety solo obtenga los rubros de fotomultas
		if(request.getUsername().equals("usuario_sesvial") &&
				(entryId.equals(Long.parseLong("643")) || entryId.equals(Long.parseLong("644")) )){
			if (reportType.equals("PENDING")) {
				return findPending(startDate, endDate, reportType, entryId);
			}
			if (reportType.equals("PAYED")) {
				return findPayed(startDate, endDate, reportType, entryId);
			}	
		}		
		return null;
	}
	
	private StatementReport findPending(Date startDate, Date endDate, String reportType, Long entryId){
		
		List<Long> pendingStatus=new ArrayList<Long>();
		pendingStatus.add(new Long("3"));
		pendingStatus.add(new Long("4"));
		pendingStatus.add(new Long("5"));
		
		
		/*String sqlPending=
				"SELECT NEW org.gob.loja.gim.ws.dto.BondReport("
				+ "  mb.id, mb.number, e.name, mb.groupingCode, mb.serviceDate, mb.paidTotal, mb.emisionDate, "
				+ "  mb.id, mb.groupingCode)"
				+ "  FROM "
				+ "    MunicipalBond mb "
				+ "    JOIN mb.entry e "
				+ "  WHERE "
				+ "	   e.id = :entryId and "
				+ "    mb.municipalBondStatus.id in (:statusId) and "
				+ "    mb.creationDate between :startDate and :endDate ";
		
		//Long pendingBondStatusId = systemParameterService.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = em.createQuery(sqlPending);
		query.setParameter("entryId", entryId);
		query.setParameter("statusId", pendingStatus);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		StatementReport statementReport = new StatementReport();
		statementReport.setStartDate(startDate);
		statementReport.setEndDate(endDate);
		statementReport.setBondReports(query.getResultList());
		System.out.println("--------------------------el tamnio es "+query.getResultList().size());*/
		
		String sqlPending="select mb.id, mb.number, e.name, mb.groupingCode, mb.serviceDate, "
				+ "mb.base, mb.emisionDate, ant.antNumber, ant.numberplate "
				+ "from municipalbond mb "
				+ "inner join resident on mb.resident_id=resident.id "
				+ "inner join ANTReference ant on mb.adjunct_Id=ant.id "
				+ "inner join entry e on mb.entry_id = e.id "
				+ "where entry_id in (:entryId) "
				+ "and mb.municipalbondstatus_id in (:statusId) "
				+ "and mb.creationdate between :startDate and :endDate "
				+ "order by mb.serviceDate ";
					
		Query query = em.createNativeQuery(sqlPending);
					
		query.setParameter("entryId", entryId);
		query.setParameter("statusId", pendingStatus);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
				
		StatementReport statementReport = new StatementReport();
		statementReport.setStartDate(startDate);
		statementReport.setEndDate(endDate);
		statementReport.setBondReports(NativeQueryResultsMapper.map(query.getResultList(), BondReport.class));
		System.out.println("--------------------------pendientes "+query.getResultList().size());
		
		return statementReport;
	}
	
	private StatementReport findPayed(Date startDate, Date endDate, String reportType, Long entryId){
		/*String sqlPending=
				"SELECT NEW org.gob.loja.gim.ws.dto.BondReport("
				+ "  mb.id, mb.number, e.name, mb.groupingCode, mb.serviceDate, mb.paidTotal, mb.emisionDate, "
				+ "  mb.id, mb.groupingCode)"
				+ "  FROM "
				+ "    MunicipalBond mb "
				+ "    JOIN mb.entry e "
				+ "  WHERE "
				+ "	   e.id = :entryId and "
				+ "    mb.municipalBondStatus.id in ( :statusId ) and "
				+ "    mb.creationDate between :startDate and :endDate ";*/
		List<Long> paymentStatus=new ArrayList<Long>();
		paymentStatus.add(new Long("6"));
		paymentStatus.add(new Long("11"));
		
		String sqlPending="select mb.id, mb.number, e.name, mb.groupingCode, mb.serviceDate, "
				+ "mb.base, mb.liquidationDate, ant.antNumber, ant.numberplate "
				+ "from municipalbond mb "
				+ "inner join resident on mb.resident_id=resident.id "
				+ "inner join ANTReference ant on mb.adjunct_Id=ant.id "
				+ "inner join entry e on mb.entry_id = e.id "
				+ "where entry_id in (:entryId) "
				+ "and mb.municipalbondstatus_id in (:statusId) "
				+ "and mb.creationdate >= '2016-01-01' "
				+ "and mb.liquidationdate between :startDate and :endDate "
				+ "order by mb.serviceDate ";
					
		Query query = em.createNativeQuery(sqlPending);
					
		query.setParameter("entryId", entryId);
		query.setParameter("statusId", paymentStatus);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
				
		StatementReport statementReport = new StatementReport();
		statementReport.setStartDate(startDate);
		statementReport.setEndDate(endDate);
		statementReport.setBondReports(NativeQueryResultsMapper.map(query.getResultList(), BondReport.class));
		System.out.println("--------------------------pagos "+query.getResultList().size());
		return statementReport;
	}
	
}