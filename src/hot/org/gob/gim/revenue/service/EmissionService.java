/**
 * 
 */
package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.ws.service.InfringementEmisionResponse;
import org.gob.loja.gim.ws.dto.InfringementEmisionDetail;
import org.gob.loja.gim.ws.dto.ant.PreemissionInfractionRequest;
import org.gob.loja.gim.ws.dto.preemission.AccountWithoutAdjunctRequest;
import org.gob.loja.gim.ws.dto.preemission.ApprovalPlansRequest;
import org.gob.loja.gim.ws.dto.preemission.BuildingPermitRequest;
import org.gob.loja.gim.ws.dto.preemission.PreemissionServiceResponse;
import org.gob.loja.gim.ws.dto.preemission.RuralPropertyRequest;
import org.gob.loja.gim.ws.dto.preemission.UrbanPropertyRequest;
import org.gob.loja.gim.ws.exception.AccountIsBlocked;
import org.gob.loja.gim.ws.exception.AccountIsNotActive;
import org.gob.loja.gim.ws.exception.EmissionOrderNotGenerate;
import org.gob.loja.gim.ws.exception.EmissionOrderNotSave;
import org.gob.loja.gim.ws.exception.EntryNotFound;
import org.gob.loja.gim.ws.exception.FiscalPeriodNotFound;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.TaxpayerNonUnique;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.revenue.model.DTO.ReportEmissionDTO;
import ec.gob.gim.revenue.model.criteria.ReportEmissionCriteria;
import ec.gob.gim.security.model.User;

/**
 * @author Rene Ortega
 * @Fecha 2017-02-02
 */
@Local
public interface EmissionService {
	public String LOCAL_NAME = "/gim/EmissionService/local";
	
	public List<ReportEmissionDTO> findEmissionReport(ReportEmissionCriteria criteria);
	
	public List<ReportEmissionDTO> findEmissionReportOtherDetails(ReportEmissionCriteria criteria);
	
	InfringementEmisionResponse generateANTEmissionInfringement(String username, PreemissionInfractionRequest params)
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
	
	PreemissionServiceResponse generateEmissionOrderWithoutAdjunctWS(AccountWithoutAdjunctRequest detailsEmission, User user)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	PreemissionServiceResponse generateEmissionOrderBuildingPermitWS(BuildingPermitRequest detailsEmission, Property property,User user)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	PreemissionServiceResponse generateEmissionOrderApprovalPlansWS(ApprovalPlansRequest detailsEmission, Property property,User user)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	PreemissionServiceResponse generateEmissionOrderUrbanPropertyWS(UrbanPropertyRequest detailsEmission, Property property,User user)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
	PreemissionServiceResponse generateEmissionOrderRuralPropertyWS(RuralPropertyRequest detailsEmission, Property property,User user)
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound,
			FiscalPeriodNotFound, EmissionOrderNotGenerate,
			EmissionOrderNotSave, InvalidUser, AccountIsNotActive,
			AccountIsBlocked;
	
}
