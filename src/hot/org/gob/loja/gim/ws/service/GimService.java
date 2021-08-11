package org.gob.loja.gim.ws.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.gob.gim.ws.service.EmisionResponse;
import org.gob.gim.ws.service.GeneralResponse;
import org.gob.gim.ws.service.InfringementEmisionResponse;
import org.gob.gim.ws.service.UserResponse;
import org.gob.loja.gim.ws.dto.EmisionDetail;
import org.gob.loja.gim.ws.dto.InfringementEmisionDetail;
import org.gob.loja.gim.ws.dto.RealEstate;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.StatementReport;
import org.gob.loja.gim.ws.dto.Taxpayer;
import org.gob.gim.ws.service.BondPrintRequest;
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

import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.security.model.User;

@Local
public interface GimService {
	
	public String LOCAL_NAME = "/gim/GimService/local";

	Taxpayer findTaxpayer(ServiceRequest request) 
		throws TaxpayerNotFound, TaxpayerNonUnique, InvalidUser, AccountIsNotActive, AccountIsBlocked;
	Map<String, Object> findTaxpayer(String name, String password, String identificationNumber) 
		throws TaxpayerNotFound, TaxpayerNonUnique, InvalidUser, AccountIsNotActive, AccountIsBlocked;
	Boolean saveTaxpayer(ServiceRequest request, Taxpayer taxpayer) 
			throws InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNotSaved;
	Boolean saveTaxpayer(String name, String password,
			Map<String, Object> taxpayerAsMap) throws InvalidUser,
			AccountIsNotActive, AccountIsBlocked, TaxpayerNotSaved;
	
	RealEstate findRealEstate(ServiceRequest request, String cadastralCode)
			throws RealEstateNotFound, TaxpayerNotFound, TaxpayerNonUnique, InvalidUser,
			AccountIsNotActive, AccountIsBlocked;
	Map<String, Object> findRealEstate(String name, String password,
			String cadastralCode) throws RealEstateNotFound, TaxpayerNotFound, TaxpayerNonUnique,
			InvalidUser, AccountIsNotActive, AccountIsBlocked;
	
	Boolean generateEmissionOrder(String name, String password,
			String identificationNumber, String accountCode, String pplessUser)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	EmisionResponse generateEmissionOrder(String name, String password,
			String identificationNumber, String accountCode, EmisionDetail emisionDetail)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	Boolean generateEmissionOrder(String name, String password,
			String identificationNumber, String accountCode, String pplessUser, Integer quantity)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	StatementReport buildReport(ServiceRequest request, Date startDate, Date endDate, String reportType, Long entryId)
			throws InvalidUser;
	
	List<RealEstate> findProperties(ServiceRequest request);
	
	/**
	 * @author macartuche
	 * @param identificationNumber
	 * @param username
	 * @param password
	 * @return
	 */
	UserResponse  saveUser(ServiceRequest request, String username, String password);
	UserResponse login(String username, String password);
	
	/**
	 * CRTV - TURNOS
	 * @author macartuche
	 * @param identification
	 * @return
	 */
	boolean searchDueDebts(ServiceRequest request);
	
	InfringementEmisionResponse generateANTEmissionInfringement(String name, String password,
			String identificationNumber, String accountCode, InfringementEmisionDetail emisionDetail)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	InfringementEmisionResponse confirmANTEmissionInfringement(String name, String password,
			InfringementEmisionDetail emisionDetail)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked, Exception;
	
	GeneralResponse bondsByExternalPayment(ServiceRequest request);
	
	GeneralResponse updateBondPrinterNumber(ServiceRequest request, BondPrintRequest bonds);
		
}
