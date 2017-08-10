package org.gob.loja.gim.ws.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.gob.gim.ws.service.UserResponse;
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
	
	String generateEmissionOrder(String name, String password,
			String identificationNumber, String accountCode, EmisionDetail emisionDetail)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	StatementReport buildReport(ServiceRequest request, Date startDate, Date endDate, String reportType, Long entryId)
			throws InvalidUser;
	
	List<RealEstate> findProperties(ServiceRequest request);
	
	Map<String, String>  saveUser(String identificationNumber, String username, String password);
	
}
