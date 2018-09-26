package org.gob.gim.accounting.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.accounting.dto.AccountDetail;
import org.gob.gim.accounting.dto.AccountItem;
import org.gob.gim.accounting.dto.Criteria;
import org.gob.gim.accounting.dto.ReportFilter;
import org.gob.gim.accounting.dto.ReportType;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.GimUtils;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.income.model.Account;



@Stateless(name="FinantialService")
public class FinantialServiceBean implements FinantialService{	
	
	@PersistenceContext
	EntityManager entityManager;
	
	@EJB
	SystemParameterService systemParameterService;
	
	private final static String GENERAL_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         a.accountCode like :accountCode AND " +
			"         mb.emisionDate BETWEEN :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses)" +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String INCOME_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         a.accountCode like :accountCode AND " +
			"         mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"         mb.liquidationDate BETWEEN :startDate AND :endDate AND " +
			"         mb.paymentAgreement_id is null AND " +
			"         mb.municipalBondStatus_id in (:statuses)" +
			//rfam 2018-05-14 para eviatar el pago en abonos
			"		  AND (select count(maux) " + 
			"         		from gimprod.MunicipalbondAux maux " + 
			"				where maux.municipalbond_id = mb.id " + 
			"				AND maux.typepayment = 'SUBSCRIPTION' " + 
			"				AND maux.status = 'VALID') = 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String INCOME_QUERY_EMAALEP = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         a.accountCode like :accountCode AND " +
			"         mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"         mb.liquidationDate BETWEEN :startDate AND :endDate AND " +
			"         mb.paymentAgreement_id is null AND " +
			"         mb.municipalBondStatus_id in (:statuses) AND " +
			"         mb.entry_id in (:entriesList)" +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String SURCHARGES_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.surchargedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.emisionDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String REVENUE_TAXES_QUERY = 
			" SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(i.value) " +
			"    FROM taxitem i, municipalBond mb, tax t, account a " +
			"    WHERE i.municipalBond_id=mb.id AND " +
			"	       i.tax_id = t.id AND " +
			"          t.taxAccount_id = a.id AND " +
			"          mb.emisionDate BETWEEN :startDate AND :endDate AND " +			
			"          mb.municipalBondStatus_id in (:statuses) " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String DISCOUNTS_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, -sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.discountedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.emisionDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String INCOME_SURCHARGES_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.surchargedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"         mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.paymentAgreement_id is null AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			//rfam 2018-05-14 para eviatar el pago en abonos
			"		  AND (select count(maux) " + 
			"         		from gimprod.MunicipalbondAux maux " + 
			"				where maux.municipalbond_id = mb.id " + 
			"				AND maux.typepayment = 'SUBSCRIPTION' " + 
			"				AND maux.status = 'VALID') = 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String INCOME_DISCOUNTS_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, -sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.discountedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"         mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.paymentAgreement_id is null AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			//rfam 2018-05-14 para eviatar el pago en abonos
			"		  AND (select count(maux) " + 
			"         		from gimprod.MunicipalbondAux maux " + 
			"				where maux.municipalbond_id = mb.id " + 
			"				AND maux.typepayment = 'SUBSCRIPTION' " + 
			"				AND maux.status = 'VALID') = 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";	
	
	
	private final static String TAXES_QUERY = 
			" SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(i.value) " +
			"    FROM taxitem i, municipalBond mb, tax t, account a " +
			"    WHERE i.municipalBond_id=mb.id AND " +
			"	       i.tax_id = t.id AND " +
			"          t.taxAccount_id = a.id AND " +
			"          mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"          mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.paymentAgreement_id is null AND " +
			"          mb.municipalBondStatus_id in (:statuses) " +
			//rfam 2018-05-14 para eviatar el pago en abonos
			"		  AND (select count(maux) " + 
			"         		from gimprod.MunicipalbondAux maux " + 
			"				where maux.municipalbond_id = mb.id " + 
			"				AND maux.typepayment = 'SUBSCRIPTION' " + 
			"				AND maux.status = 'VALID') = 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String REVENUE_REPORT = "("+GENERAL_QUERY + ") UNION (" +
													 REVENUE_TAXES_QUERY + ")";	
	
	private final static String INCOME_REPORT  = "("+INCOME_QUERY + ") UNION (" +
												     INCOME_SURCHARGES_QUERY + ") UNION (" + 
												     INCOME_DISCOUNTS_QUERY + ") UNION (" + 
												     TAXES_QUERY+")";
	
//	private final static String INCOME_REPORT_EMAALEP  = "("+INCOME_QUERY + ") UNION (" +
//												     INCOME_SURCHARGES_QUERY + ") UNION (" + 
//												     INCOME_DISCOUNTS_QUERY + ") UNION (" + 
//												     TAXES_QUERY+")";
//
	private final static String INCOME_REPORT_EMAALEP  = "("+INCOME_QUERY_EMAALEP + ")";

	private final static String BALANCE_REPORT  = "("+GENERAL_QUERY + ") UNION (" +
													  REVENUE_TAXES_QUERY+")";

	private final static String VOID_REPORT  = "("+GENERAL_QUERY + ") UNION (" +		      
				REVENUE_TAXES_QUERY+")";

	private final static String DETAIL_QUERY =
			" SELECT mb.number, r.identificationNumber, r.name , sum(total), mb.expirationDate " +
			"     FROM MunicipalBond mb, item i, entry e, account a, resident r " +
			"     WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND" +
			"         e.account_id = a.id AND " +
			"         a.id = :accountId AND " +
			"         mb.resident_id = r.id AND " +
			"         mb.emisionDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"     GROUP BY mb.id, r.id " +
			"     ORDER BY r.name";
	
	private final static String INCOME_DETAIL_QUERY =
			" SELECT mb.number, r.identificationNumber, r.name , sum(total), mb.expirationDate " +
			"     FROM MunicipalBond mb, item i, entry e, account a, resident r " +
			"     WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND" +
			"         e.account_id = a.id AND " +
			"         a.id = :accountId AND " +
			"         mb.resident_id = r.id AND " +
			"         mb.emisionDate between :emissionStartDate AND :emissionEndDate AND " +
			"         mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.paymentAgreement_id is null AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"     GROUP BY mb.id, r.id " +
			"     ORDER BY r.name";
	
	private final static String BALANCE_DETAIL_QUERY =
			" SELECT mb.number, r.identificationNumber, r.name , sum(total), mb.expirationDate " +
			"     FROM gimprod.MunicipalBond mb, gimprod.item i, gimprod.entry e, gimprod.account a, gimprod.resident r " +
			"     WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND" +
			"         e.account_id = a.id AND " +
			"         a.id = :accountId AND " +
			"         mb.resident_id = r.id AND " +
			"         mb.emisionDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"     GROUP BY mb.id, r.id " +
			"     ORDER BY r.name";
	
	private final static String DUE_PORTFOLIO_DETAIL_QUERY = 
			" SELECT mb.number, r.identificationNumber, r.name , sum(paidTotal), mb.expirationDate " +
			"     FROM MunicipalBond mb, entry e, account a, resident r " +			
			"   WHERE " +			
			"         mb.entry_id = e.id AND" +
			"         e.account_id = a.id AND " +
			"         a.id = :accountId AND " +
			"         mb.resident_id = r.id AND " +
			"         mb.expirationDate BETWEEN :startDate AND :endDate AND " +			
			"         mb.municipalBondStatus_id in (:statuses) " +			
			"     GROUP BY mb.id, r.id " +
			"     ORDER BY r.name";
	
	private final static String QUOTAS_LIQUIDATION_DETAIL_QUERY = 
			" SELECT mb.number, r.identificationNumber, r.name , sum(total), mb.expirationDate " +
			"     FROM MunicipalBond mb, item i, entry e, account a, resident r " +			
			"     WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND" +
			"         e.account_id = a.id AND " +
			"         a.id = :accountId AND " +
			"         mb.resident_id = r.id AND " +
			"         mb.paymentAgreement_id IS NOT NULL AND " +
			"         mb.liquidationDate BETWEEN :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses)" +			
			"     GROUP BY mb.id, r.id " +
			"     ORDER BY r.name";		
			
	
	private final static String SUBSCRIPTION_DETAIL_QUERY = 
			" SELECT mb.number, r.identificationNumber, r.name , sum(total), mb.expirationDate " +
			"     FROM MunicipalBond mb, item i, entry e, account a, resident r " +			
			"     WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         a.id = :accountId AND " +
			"         mb.resident_id = r.id AND " +
			"         mb.liquidationDate BETWEEN :startDate AND :endDate " +
			"			AND (select count(*) " +
			"			from municipalbondaux maux " +
			"			where maux.municipalbond_id = mb.id " +
			"			AND maux.typepayment = 'SUBSCRIPTION' " +
			"			AND maux.status = 'VALID') > 0 AND " +
			"         mb.municipalBondStatus_id in (:statuses)" +			
			"     GROUP BY mb.id, r.id " +
			"     ORDER BY r.name";	
	
	/**
	 * En el query siguiente deberia manejarse por la fecha de liquidacion del MunicipalBond en lugar
	 * de la fecha del deposito?
	 */
	private final static String INCOME_INTEREST_QUERY =
			" SELECT sum(d.interest) " +
			"    FROM MunicipalBond mb, deposit d " +
			"    WHERE d.municipalBond_id = mb.id AND " +
			"          d.date between :startDate AND :endDate AND " +			
			"          d.status = 'VALID' AND " +
			"    	   mb.paymentAgreement_id IS NULL AND " +
			"          mb.municipalBondStatus_id in (:statuses) ";
    
	private final static String INCOME_COLLECTED_QUOTAS_QUERY =
			" SELECT sum(d.value) " +
			"    FROM MunicipalBond mb, deposit d " +
			"    WHERE d.municipalBond_id = mb.id AND " +
			"          d.date between :startDate AND :endDate AND " +
			"          mb.paymentAgreement_id is not null AND " +
			"          d.balance > 0 ";

	//@author macartuche
	//reporte x abonos != convenio de pago
	private final static String INCOME_COLLECTED_SUBSCRIPTION_QUERY =
			"SELECT sum(a.value) FROM ("+
				" SELECT d.value " +
				"    FROM MunicipalBond mb, deposit d, municipalbondaux mba " +
				"    WHERE d.municipalBond_id = mb.id  " +
				"       AND mba.municipalbond_id = mb.id " +
				"		AND mba.deposit_id = d.id" +
				"		AND mba.typepayment='SUBSCRIPTION' "+
				"		AND d.date between :startDate AND :endDate  "+
				"		AND d.status='VALID' "+
				"		AND d.balance > 0 "+
				"GROUP BY d.id) as a";
	//fin reporte x abono != convenio de pago
	
	private final static String INCOME_LIQUIDATED_QUOTAS_QUERY =
			" SELECT sum(d.value) " +
			"    FROM MunicipalBond mb, deposit d " +
			"    WHERE d.municipalBond_id = mb.id AND " +
			"          d.date between :startDate AND :endDate AND " +
			"          mb.paymentAgreement_id is not null AND" +
			"          d.balance = 0 ";	
	//@author macartuche
	//reporte x abonos != convenio de pago
	private final static String INCOME_LIQUIDATED_SUBSCRIPTION_QUERY =
			"SELECT sum(a.value) FROM ("+ 
				" SELECT d.value " +
				"    FROM MunicipalBond mb, deposit d, municipalbondaux mba " +
				"    WHERE d.municipalBond_id = mb.id  " +
				"       AND mba.municipalbond_id = mb.id " +
				"		AND mba.deposit_id = d.id" +
				"		AND mba.typepayment='SUBSCRIPTION' "+
				"		AND d.date between :startDate AND :endDate  "+
				"		AND d.status='VALID' "+
				"		AND d.balance = 0"+
				"GROUP BY d.id) as a";
	//fin reporte x abono != convenio de pago
	
	
	private final static String QUOTAS_LIQUIDATION_INCOME_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(i.total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         a.accountCode like :accountCode AND " +
			"         mb.paymentAgreement_id IS NOT NULL AND " +
			"         mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"         mb.liquidationDate BETWEEN :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses)" +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String QUOTAS_LIQUIDATION_SURCHARGES_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.surchargedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.paymentAgreement_id IS NOT NULL AND " +
			"         mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"         mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String QUOTAS_LIQUIDATION_DISCOUNTS_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, -sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.discountedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.paymentAgreement_id IS NOT NULL AND " +
			"         mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"         mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";	
	
	private final static String QUOTAS_LIQUIDATION_TAXES_QUERY = 
			" SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(i.value) " +
			"    FROM taxitem i, municipalBond mb, tax t, account a " +
			"    WHERE i.municipalBond_id=mb.id AND " +
			"	       i.tax_id = t.id AND " +
			"          t.taxAccount_id = a.id AND " +
			"          mb.paymentAgreement_id IS NOT NULL AND " +
			"          mb.emisionDate BETWEEN :emisionStartDate AND :emisionEndDate AND " +
			"          mb.liquidationDate between :startDate AND :endDate AND " +
			"          mb.municipalBondStatus_id in (:statuses) " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String QUOTAS_LIQUIDATION_INTEREST_QUERY =
			" SELECT sum(mb.interest) " +
			"    FROM MunicipalBond mb " +
			"    WHERE mb.liquidationDate between :startDate AND :endDate AND " +
			"    mb.paymentAgreement_id IS NOT NULL AND " +
			"          mb.municipalBondStatus_id in (:statuses) ";
	
	//@autor macartuche
	private final static String SUSCRIPTION_LIQUIDATION_INTEREST_QUERY =
	" select sum(a.interest) from ( " +
	"    SELECT " +
	"	 DISTINCT mb.interest, mb.id "+		
	"    FROM MunicipalBond mb  " +
	"    	join deposit d on d.municipalbond_id = mb.id   " +
	"    	join municipalbondaux mba on mba.deposit_id = d.id  " +
	"    WHERE mb.liquidationDate between :startDate AND :endDate AND " +
	"    	mba.municipalbond_id = mb.id AND	 " +
	"    	d.status='VALID' AND	 " +
	"    	mba.status='VALID' AND	 " +
	"       mb.municipalBondStatus_id in (:statuses) AND"+
	"       mba.typepayment='SUBSCRIPTION') as a";
	//interes para abonos pagos completos
	
	private final static String QUOTAS_LIQUIDATION_REPORT  = "("+QUOTAS_LIQUIDATION_INCOME_QUERY + ") UNION (" +
			QUOTAS_LIQUIDATION_SURCHARGES_QUERY + ") UNION (" + 
			QUOTAS_LIQUIDATION_DISCOUNTS_QUERY + ") UNION (" + 
			QUOTAS_LIQUIDATION_TAXES_QUERY+")";
	
	private final static String SUBSCRIPTION_INCOME_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(i.total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.municipalBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         a.accountCode like :accountCode AND " +
			"         mb.liquidationDate BETWEEN :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses)" +
			"			AND (select count(*) " +
			"			from municipalbondaux maux " +
			"			where maux.municipalbond_id = mb.id " +
			"			AND maux.typepayment = 'SUBSCRIPTION' " +
			"			AND maux.status = 'VALID') > 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String SUBSCRIPTION_SURCHARGES_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.surchargedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"			AND (select count(*) " +
			"			from municipalbondaux maux " +
			"			where maux.municipalbond_id = mb.id " +
			"			AND maux.typepayment = 'SUBSCRIPTION' " +
			"			AND maux.status = 'VALID') > 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String SUBSCRIPTION_DISCOUNTS_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, -sum(total) " +
			"   FROM item i, municipalBond mb, entry e, account a " +
			"   WHERE i.discountedBond_id=mb.id AND " +
			"         i.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         mb.liquidationDate between :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses) " +
			"			AND (select count(*) " +
			"			from municipalbondaux maux " +
			"			where maux.municipalbond_id = mb.id " +
			"			AND maux.typepayment = 'SUBSCRIPTION' " +
			"			AND maux.status = 'VALID') > 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";	
	
	private final static String SUBSCRIPTION_TAXES_QUERY = 
			" SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(i.value) " +
			"    FROM taxitem i, municipalBond mb, tax t, account a " +
			"    WHERE i.municipalBond_id=mb.id AND " +
			"	       i.tax_id = t.id AND " +
			"          t.taxAccount_id = a.id AND " +
			"          mb.liquidationDate between :startDate AND :endDate AND " +
			"          mb.municipalBondStatus_id in (:statuses) " +
			"			AND (select count(*) " +
			"			from municipalbondaux maux " +
			"			where maux.municipalbond_id = mb.id " +
			"			AND maux.typepayment = 'SUBSCRIPTION' " +
			"			AND maux.status = 'VALID') > 0 " +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String SUBSCRIPTION_REPORT  =  "("+SUBSCRIPTION_INCOME_QUERY + ") UNION (" +
			SUBSCRIPTION_SURCHARGES_QUERY + ") UNION (" + 
			SUBSCRIPTION_DISCOUNTS_QUERY + ") UNION (" + 
			SUBSCRIPTION_TAXES_QUERY+")";

	private final static String DUE_PORTFOLIO_QUERY = 
			"SELECT a.id, a.accountCode, a.previousYearsAccountCode, a.futureYearsAccountCode, a.budgetCertificateCode, a.accountName, sum(paidTotal) " +
			"   FROM municipalBond mb, entry e, account a " +
			"   WHERE " +
			"         mb.entry_id = e.id AND " +
			"         e.account_id = a.id AND " +
			"         a.accountCode like :accountCode AND " +
			"         mb.expirationDate BETWEEN :startDate AND :endDate AND " +
			"         mb.municipalBondStatus_id in (:statuses)" +
			"   GROUP BY a.id " +
			"   ORDER BY a.accountCode ";
	
	private final static String ENTRIES_EMAALEP_LIST = "ENTRIES_EMAALEP_LIST";
	
	private String findAccountNumber(Object[] row, ReportFilter filter){
		String accountNumber = (String)row[1];
		if(filter == ReportFilter.PREVIOUS){
			accountNumber = (String)row[2];
		} else {
			if(filter == ReportFilter.FUTURE){
				accountNumber = (String)row[3];
			} 
		}
		return accountNumber;
	}
	
	private AccountItem create(String accountNumber, Object[] row, ReportType reportType){
		AccountItem item = new AccountItem();		
		item.setAccountId(Long.parseLong(row[0].toString()));
		item.setAccountNumber(accountNumber);
		item.setBudgetAccountNumber((String)row[4]);
		item.setAccountName((String)row[5]);
		item.setCredit(BigDecimal.ZERO);
		item.setDebit(BigDecimal.ZERO);
		item.setReportType(reportType);
		item.setPreviousYearsAccountCode((String)row[2]);
		return item;
	}
		
	private void setValue(AccountItem accountItem, Object[] row, ReportType reportType){
		if(reportType == ReportType.INCOME || reportType == ReportType.QUOTAS_LIQUIDATION || reportType == ReportType.SUBSCRIPTION){
			accountItem.setCredit((BigDecimal)row[6]);
		} else {
			accountItem.setDebit((BigDecimal)row[6]);
		}		
		accountItem.calculateBalance();
	}

	private AccountItem buildInterestItem(Criteria criteria, Map<String, AccountItem> report){
		BigDecimal collectedInterest = findInterestValue(criteria);
		Account account = findAccountByDefinedParameter("INTEREST_ACCOUNT_ID");
		AccountItem interestItem = report.get(account.getAccountCode());
		if(interestItem == null){
			interestItem = new AccountItem();
			interestItem.setAccountId(account.getId());
			interestItem.setAccountNumber(account.getAccountCode());
			interestItem.setAccountName(account.getAccountName());
			interestItem.setBudgetAccountNumber(account.getBudgetCertificateCode());
			interestItem.setDebit(BigDecimal.ZERO);
			interestItem.setCredit(collectedInterest != null ? collectedInterest : BigDecimal.ZERO);
		} else {
			interestItem.setCredit(collectedInterest != null ? collectedInterest.add(interestItem.getCredit()) : interestItem.getCredit());
		}
		interestItem.setReportType(criteria.getReportType());
		interestItem.calculateBalance();
		
		return interestItem;
	}
	
	private AccountItem buildQuotasItem(Criteria criteria){
		BigDecimal collected = findLiquidatedQuotas(criteria); 
		BigDecimal liquidated = findCollectedQuotas(criteria);//*
		
		//@author macartuche
		//convenio o abono
		String accountParameter = "QUOTAS_ACCOUNT_ID";
		if(criteria.getReportType().equals(ReportType.SUBSCRIPTION)) {
			accountParameter = "QUOTAS_ACCOUNT_SUBSCRIPTION_ID";
		}//fin convenio o abono
		
		Account account = findAccountByDefinedParameter(accountParameter);
		AccountItem quotasItem = new AccountItem();
		quotasItem.setAccountId(account.getId());
		quotasItem.setAccountNumber(account.getAccountCode());
		quotasItem.setAccountName(account.getAccountName());
		quotasItem.setBudgetAccountNumber("");
		collected = collected != null ? collected : BigDecimal.ZERO;
		liquidated = liquidated != null ? liquidated : BigDecimal.ZERO;
		quotasItem.setDebit(collected.add(liquidated));
		quotasItem.setCredit(collected.add(liquidated));
		quotasItem.calculateBalance();
		quotasItem.setReportType(criteria.getReportType());
		return quotasItem;
	}
	
	
	@SuppressWarnings("unchecked")
	private BigDecimal findInterestValue(Criteria criteria){
		Query query = null;
		if(criteria.getReportType() == ReportType.QUOTAS_LIQUIDATION){
			query = entityManager.createNativeQuery(QUOTAS_LIQUIDATION_INTEREST_QUERY);
		}else if(criteria.getReportType() == ReportType.SUBSCRIPTION) {
			//@author macartuche
			query = entityManager.createNativeQuery(SUSCRIPTION_LIQUIDATION_INTEREST_QUERY);
			//fin --interes value for subscription
		}else{
			query = entityManager.createNativeQuery(INCOME_INTEREST_QUERY);
		}
		query.setParameter("startDate", criteria.getStartDate());
		query.setParameter("endDate", criteria.getEndDate());
		query.setParameter("statuses", getPaidStatuses());
		List<BigDecimal> result = query.getResultList();
		BigDecimal value = BigDecimal.ZERO;
		if(result.size() > 0){
			value = result.get(0);
		}
		return value;
	}
	
	
	@SuppressWarnings("unchecked")
	private BigDecimal findCollectedQuotas(Criteria criteria){
		String nativeQuery = INCOME_COLLECTED_QUOTAS_QUERY;
		if(criteria.getReportType()==ReportType.SUBSCRIPTION) {
			nativeQuery = INCOME_COLLECTED_SUBSCRIPTION_QUERY;
		}
		Query query = entityManager.createNativeQuery(nativeQuery);
		query.setParameter("startDate", criteria.getStartDate());
		query.setParameter("endDate", criteria.getEndDate());
		List<BigDecimal> result = query.getResultList();
		BigDecimal value = BigDecimal.ZERO;
		if(result.size() > 0){
			value = result.get(0);
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	private BigDecimal findLiquidatedQuotas(Criteria criteria){
		String nativeQuery = INCOME_LIQUIDATED_QUOTAS_QUERY;
		if(criteria.getReportType()==ReportType.SUBSCRIPTION) {
			nativeQuery = INCOME_LIQUIDATED_SUBSCRIPTION_QUERY;
		}
		
		Query query = entityManager.createNativeQuery(nativeQuery);
		query.setParameter("startDate", criteria.getStartDate());
		query.setParameter("endDate", criteria.getEndDate());
		List<BigDecimal> result = query.getResultList();
		BigDecimal value = BigDecimal.ZERO;
		if(result.size() > 0){
			value = result.get(0);
		}
		return value;
	}	
	
	
	@SuppressWarnings("unchecked")
	private void buildReport(Criteria criteria, Map<String, AccountItem> report, ReportType reportType, Date startDate, Date endDate, Date emisionStartDate, Date emisionEndDate, ReportFilter reportFilter){
		List<Long> statuses = getValidStatuses();
		Query query = entityManager.createNativeQuery(REVENUE_REPORT);
		if(reportType == ReportType.INCOME){
			query = entityManager.createNativeQuery(INCOME_REPORT);
			statuses = getPaidStatuses();
			query.setParameter("emisionStartDate", emisionStartDate);
			query.setParameter("emisionEndDate", emisionEndDate);
		} else {
			if(reportType == ReportType.VOID){
				query = entityManager.createNativeQuery(VOID_REPORT);
				statuses = getVoidStatuses();
			} else {
				if(reportType == ReportType.QUOTAS_LIQUIDATION){
					System.out.println(QUOTAS_LIQUIDATION_REPORT);
					query = entityManager.createNativeQuery(QUOTAS_LIQUIDATION_REPORT);
					statuses = getPaidStatuses();
					query.setParameter("emisionStartDate", emisionStartDate);
					query.setParameter("emisionEndDate", emisionEndDate);
				}
				else {
					if(reportType == ReportType.BALANCE){
						query = entityManager.createNativeQuery(BALANCE_REPORT);
						statuses = getBalancesStatuses();
						query.setParameter("startDate", emisionStartDate);
						query.setParameter("endDate", emisionEndDate);
					} else{
						if(reportType == ReportType.INCOME_EMAALEP){
							query = entityManager.createNativeQuery(INCOME_REPORT_EMAALEP);
							statuses = getPaidStatuses();
							String strListEmaalep = systemParameterService.findParameter(ENTRIES_EMAALEP_LIST);
							List<Long> entriesListLong = new ArrayList<Long>();
							entriesListLong = GimUtils.convertStringWithCommaToListLong(strListEmaalep);
//							List<String> entriesListStr = Arrays.asList(strListEmaalep.split(","));
//							List<Long> entriesListLong = new ArrayList<Long>();
//							for (int i = 0 ; i < entriesListStr.size() ; i++){
//								String str = entriesListStr.get(i);
//								System.out.println("RRRRRRRRRRRRRRRRRRRR cadena i: "+i+" valor: "+str);
//								entriesListLong.add(Long.parseLong(str));
//							}
//							System.out.println("String: "+entriesListStr);
//							System.out.println("size: "+entriesListStr.size());
//							System.out.println("Long: "+entriesListLong);
//							System.out.println("size: "+entriesListLong.size());
							query.setParameter("emisionStartDate", emisionStartDate);
							query.setParameter("emisionEndDate", emisionEndDate);
							query.setParameter("entriesList", entriesListLong);
						}else{
							if(reportType == ReportType.SUBSCRIPTION){
								query = entityManager.createNativeQuery(SUBSCRIPTION_REPORT);
								statuses = getSubscriptionStatuses();
							}
						}						
					}
						
				}
			}
		}
				
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("accountCode", criteria.getAccountCode()+"%");
		query.setParameter("statuses", statuses);
		List<Object[]> results = query.getResultList();
		
		for(Object[] row : results){
			String accountNumber = findAccountNumber(row, reportFilter);
			AccountItem accountItem = report.get(accountNumber);
			if(accountItem == null){
				accountItem = create(accountNumber, row, criteria.getReportType());
			}
			accountItem.setReportFilter(reportFilter);
			setValue(accountItem, row, reportType);
			report.put(accountNumber, accountItem);
		}
	}
	
//	@SuppressWarnings("unchecked")
//	private void buildReport(Criteria criteria, Map<String, AccountItem> report, Date startDate, Date endDate, Date emisionStartDate, Date emisionEndDate, ReportFilter reportFilter){
//		Query query = null;		
//		if(criteria.getReportType() == ReportType.INCOME) query = entityManager.createNativeQuery(INCOME_REPORT);
//		if(criteria.getReportType() == ReportType.QUOTAS_LIQUIDATION) query = entityManager.createNativeQuery(QUOTAS_LIQUIDATION_REPORT);
//		query.setParameter("startDate", startDate);
//		query.setParameter("endDate", endDate);
//		query.setParameter("accountCode", criteria.getAccountCode()+"%");
//		query.setParameter("statuses", getPaidStatuses());
//		query.setParameter("emisionStartDate", emisionStartDate);
//		query.setParameter("emisionEndDate", emisionEndDate);
//		
//		List<Object[]> results = query.getResultList();
//		
//		for(Object[] row : results){
//			String accountNumber = findAccountNumber(row, reportFilter);
//			AccountItem accountItem = report.get(accountNumber);
//			if(accountItem == null){
//				accountItem = create(accountNumber, row);
//			}
//			if(reportFilter == ReportFilter.CURRENT) accountItem.setShowDetail(Boolean.TRUE);
//			setValue(accountItem, row, criteria.getReportType());
//			report.put(accountNumber, accountItem);
//		}
//	}	
//	
	
	@SuppressWarnings("unchecked")	                                 
	private Map<Long, AccountDetail> findDetail(Criteria criteria, Long accountId, ReportType reportType, Map<Long, AccountDetail> detail, Date startDate, Date endDate, Date emisionStartDate, Date emisionEndDate){
		AccountDetail totalItem = detail.get(0L);
		List<Long> statuses = getValidStatuses();
		String sql = DETAIL_QUERY;		
		if(reportType == ReportType.INCOME){
			sql = INCOME_DETAIL_QUERY;
			statuses = getPaidStatuses();
		} else {
			if(reportType == ReportType.VOID){
				statuses = getVoidStatuses();
			}else{
				if(reportType == ReportType.DUE_PORTFOLIO){
					statuses = getEmittedStatuses();
					sql = DUE_PORTFOLIO_DETAIL_QUERY;
				}else{
					if(reportType == ReportType.QUOTAS_LIQUIDATION){
						statuses = getPaidStatuses();
						sql = QUOTAS_LIQUIDATION_DETAIL_QUERY;
					}else{
						if(reportType == ReportType.BALANCE){
							statuses = getBalancesStatuses();
							sql = BALANCE_DETAIL_QUERY;
						}else{
							if(reportType == ReportType.SUBSCRIPTION){
								statuses = getSubscriptionStatuses();
								sql = SUBSCRIPTION_DETAIL_QUERY;
							}
						}
					}
				}
			}
		}
		
		Query query = entityManager.createNativeQuery(sql);
		if(reportType == ReportType.INCOME){
			query.setParameter("emissionStartDate", emisionStartDate);
			query.setParameter("emissionEndDate", emisionEndDate);
		}
		query.setParameter("startDate", criteria.getStartDate());
		query.setParameter("endDate", criteria.getEndDate());
		query.setParameter("accountId", accountId);
		query.setParameter("statuses", statuses);
		List<Object[]> results = query.getResultList();
		
		for(Object[] row : results){
			Long accountNumber = ((BigInteger) row[0]).longValue();
			AccountDetail accountDetail = detail.get(accountNumber);
			if(accountDetail == null){
				accountDetail = createDetail(row);
			} 
			setValue(accountDetail, (BigDecimal)row[3], reportType, totalItem);
			detail.put(accountNumber, accountDetail);
		}
		return detail;
	}		

	
	@SuppressWarnings("unchecked")
	public Map<String, AccountItem> findDuePortfolioReport(Criteria criteria){
		Map<String, AccountItem> report = new HashMap<String, AccountItem>();
		List<Long> statuses = getEmittedStatuses();
	
		Query query = entityManager.createNativeQuery(DUE_PORTFOLIO_QUERY);
		query.setParameter("startDate", criteria.getStartDate());
		query.setParameter("endDate", criteria.getEndDate());
		query.setParameter("accountCode", criteria.getAccountCode()+"%");
		query.setParameter("statuses", statuses);
		List<Object[]> results = query.getResultList();
		
		for(Object[] row : results){
			BigDecimal value = (BigDecimal)row[6];
			if(value!=null && value.compareTo(BigDecimal.ZERO) > 0){
				String accountNumber = findAccountNumber(row, criteria.getReportFilter());
				AccountItem accountItem = report.get(accountNumber);
				if(accountItem == null){
					accountItem = create(accountNumber, row, criteria.getReportType());
				}
				accountItem.setReportFilter(ReportFilter.CURRENT);
				setValue(accountItem, row, criteria.getReportType());
				report.put(accountNumber, accountItem);
			}
		}
		return report;
	}	

	public Map<String, AccountItem> findBalanceReport(Criteria criteria){
		Map<String, AccountItem> report = new HashMap<String, AccountItem>();
		Query query = entityManager.createNamedQuery("FiscalPeriod.findById");
		query.setParameter("id", criteria.getFiscalPeriodId());
		FiscalPeriod fiscalPeriod = (FiscalPeriod) query.getSingleResult();
		
		Integer minimumYear = systemParameterService.findParameter("MINIMUM_BOND_YEAR");
		Date minimumBondDate = DateUtils.getDateInstance(minimumYear, Calendar.JANUARY, 01); 
		
		Calendar fiscalPeriodStartDate = DateUtils.getTruncatedInstance(fiscalPeriod.getStartDate());
		fiscalPeriodStartDate.add(Calendar.DAY_OF_YEAR, -1);
		
		Date previousYearsStartDate = criteria.getStartDate();
		Date previousYearsEndDate = fiscalPeriodStartDate.getTime();
		Date currentYearsStartDate = criteria.getStartDate().after(fiscalPeriod.getStartDate()) ? criteria.getStartDate() : fiscalPeriod.getStartDate();
		Date currentYearsEndDate = criteria.getEndDate();
				
		if(criteria.getReportType() == ReportType.VOID){
			if(criteria.getReportFilter() == ReportFilter.ALL || criteria.getReportFilter() == ReportFilter.PREVIOUS){
				buildReport(criteria, report, ReportType.VOID, previousYearsStartDate, previousYearsEndDate, null, null, ReportFilter.PREVIOUS);
			}
			
			if(criteria.getReportFilter() == ReportFilter.ALL || criteria.getReportFilter() == ReportFilter.CURRENT){
				buildReport(criteria, report, ReportType.VOID, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.CURRENT);
			}			
			
		} else {
			if(criteria.getReportFilter() == ReportFilter.ALL || criteria.getReportFilter() == ReportFilter.PREVIOUS){
				System.out.println("CALCULANDO AÑOS PREVIOS");
				if(criteria.getReportType() == ReportType.SUBSCRIPTION){
					buildReport(criteria, report, ReportType.SUBSCRIPTION, criteria.getStartDate(), criteria.getEndDate(), fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate(), ReportFilter.CURRENT);
				}
				if(criteria.getReportType() == ReportType.REVENUE || criteria.getReportType() == ReportType.COMBINED){
					buildReport(criteria, report, ReportType.REVENUE, previousYearsStartDate, previousYearsEndDate, null, null, ReportFilter.PREVIOUS);
				}
				
				if(criteria.getReportType() == ReportType.QUOTAS_LIQUIDATION){
					buildReport(criteria, report, ReportType.QUOTAS_LIQUIDATION, criteria.getStartDate(), criteria.getEndDate(), minimumBondDate, previousYearsEndDate, ReportFilter.PREVIOUS);
				}
				
				if(criteria.getReportType() == ReportType.INCOME || criteria.getReportType() == ReportType.COMBINED){
					buildReport(criteria, report, ReportType.INCOME, criteria.getStartDate(), criteria.getEndDate(), minimumBondDate, previousYearsEndDate ,ReportFilter.PREVIOUS);
				}

				if(criteria.getReportType() == ReportType.BALANCE){
					buildReport(criteria, report, ReportType.BALANCE, previousYearsStartDate, previousYearsEndDate, null, null,ReportFilter.PREVIOUS);
				}
				if(criteria.getReportType() == ReportType.REVENUE_EMAALEP){
					buildReport(criteria, report, ReportType.REVENUE_EMAALEP, previousYearsStartDate, previousYearsEndDate, null, null, ReportFilter.PREVIOUS);
				}

				if(criteria.getReportType() == ReportType.INCOME_EMAALEP){
					buildReport(criteria, report, ReportType.INCOME_EMAALEP, criteria.getStartDate(), criteria.getEndDate(), minimumBondDate, previousYearsEndDate ,ReportFilter.PREVIOUS);
				}

			}
			
			if(criteria.getReportFilter() == ReportFilter.ALL || criteria.getReportFilter() == ReportFilter.CURRENT){
				System.out.println("CALCULANDO AÑO ACTUAL");
				if(criteria.getReportType() == ReportType.SUBSCRIPTION){
					buildReport(criteria, report, ReportType.SUBSCRIPTION, criteria.getStartDate(), criteria.getEndDate(), fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate(), ReportFilter.CURRENT);
				}
				if(criteria.getReportType() == ReportType.QUOTAS_LIQUIDATION){
					buildReport(criteria, report, ReportType.QUOTAS_LIQUIDATION, criteria.getStartDate(), criteria.getEndDate(), fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate(), ReportFilter.CURRENT);
				}
				if(criteria.getReportType() == ReportType.REVENUE || criteria.getReportType() == ReportType.COMBINED){
					buildReport(criteria, report, ReportType.REVENUE, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.CURRENT);
				}
				if(criteria.getReportType() == ReportType.INCOME || criteria.getReportType() == ReportType.COMBINED){
					buildReport(criteria, report, ReportType.INCOME, criteria.getStartDate(), criteria.getEndDate(), fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate(), ReportFilter.CURRENT);
				}
				if(criteria.getReportType() == ReportType.BALANCE){
					buildReport(criteria, report, ReportType.BALANCE, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.CURRENT);
				}
				if(criteria.getReportType() == ReportType.REVENUE_EMAALEP){
					buildReport(criteria, report, ReportType.REVENUE_EMAALEP, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.CURRENT);
				}
				if(criteria.getReportType() == ReportType.INCOME_EMAALEP){
					buildReport(criteria, report, ReportType.INCOME_EMAALEP, criteria.getStartDate(), criteria.getEndDate(), fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate(), ReportFilter.CURRENT);
				}
			}
			//if(criteria.getReportFilter() == ReportFilter.ALL || criteria.getReportFilter() == ReportFilter.CURRENT){
			if(criteria.getReportFilter() == ReportFilter.FUTURE){
				System.out.println("CALCULANDO AÑOS FUTUROS");
				if(criteria.getReportType() == ReportType.REVENUE || criteria.getReportType() == ReportType.COMBINED){
					buildReport(criteria, report, ReportType.REVENUE, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.FUTURE);
				}
				if(criteria.getReportType() == ReportType.INCOME || criteria.getReportType() == ReportType.COMBINED){
					buildReport(criteria, report, ReportType.INCOME, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.FUTURE);
				}
				if(criteria.getReportType() == ReportType.REVENUE_EMAALEP){
					buildReport(criteria, report, ReportType.REVENUE_EMAALEP, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.FUTURE);
				}
				if(criteria.getReportType() == ReportType.INCOME_EMAALEP){
					buildReport(criteria, report, ReportType.INCOME_EMAALEP, currentYearsStartDate, currentYearsEndDate, null, null, ReportFilter.FUTURE);
				}
			}
			
			//&& criteria.getReportType() != ReportType.SUBSCRIPTION 
			if(report.size() > 0 && criteria.getReportType() != ReportType.REVENUE && criteria.getReportType() != ReportType.REVENUE_EMAALEP ){
				AccountItem interestItem = buildInterestItem(criteria, report);
				report.put(interestItem.getAccountNumber(), interestItem);
				if (criteria.getReportType() != ReportType.BALANCE){
					AccountItem quotasItem = buildQuotasItem(criteria); //AQUIIIIIIIIIIIIII
					report.put(quotasItem.getAccountNumber(), quotasItem);
				}
			}
		}
		return report;
	}
	
	
	public Map<Long, AccountDetail> findDetailReport(Criteria criteria, Long accountId, ReportFilter reportFilter){
		AccountDetail totalItem = new AccountDetail();
		Query query = entityManager.createNamedQuery("FiscalPeriod.findById");
		query.setParameter("id", criteria.getFiscalPeriodId());
		FiscalPeriod fiscalPeriod = (FiscalPeriod) query.getSingleResult();
		
		Integer minimumYear = systemParameterService.findParameter("MINIMUM_BOND_YEAR");
		Date minimumBondDate = DateUtils.getDateInstance(minimumYear, Calendar.JANUARY, 01); 
		
		Calendar fiscalPeriodStartDate = DateUtils.getTruncatedInstance(fiscalPeriod.getStartDate());
		fiscalPeriodStartDate.add(Calendar.DAY_OF_YEAR, -1);
		
		Date previousYearsStartDate = criteria.getStartDate();
		Date previousYearsEndDate = fiscalPeriodStartDate.getTime();
		Date currentYearsStartDate = criteria.getStartDate().after(fiscalPeriod.getStartDate()) ? criteria.getStartDate() : fiscalPeriod.getStartDate();
		Date currentYearsEndDate = criteria.getEndDate();
		
		totalItem.setDebit(BigDecimal.ZERO);
		totalItem.setCredit(BigDecimal.ZERO);
		Map<Long, AccountDetail> report = new HashMap<Long, AccountDetail>();
		report.put(0L, totalItem);
				
		
		if(criteria.getReportType() == ReportType.REVENUE){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.REVENUE, report, currentYearsStartDate, currentYearsEndDate, null, null);
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.REVENUE, report, minimumBondDate, previousYearsEndDate, null, null);
			}
			
		}
		if(criteria.getReportType() == ReportType.REVENUE_EMAALEP){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.REVENUE_EMAALEP, report, currentYearsStartDate, currentYearsEndDate, null, null);
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.REVENUE_EMAALEP, report, minimumBondDate, previousYearsEndDate, null, null);
			}
			
		}
		if(criteria.getReportType() == ReportType.INCOME){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.INCOME, report, currentYearsStartDate,currentYearsEndDate, fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate());
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.INCOME, report, currentYearsStartDate,currentYearsEndDate, minimumBondDate, previousYearsEndDate);
			}
		}
		if(criteria.getReportType() == ReportType.INCOME_EMAALEP){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.INCOME_EMAALEP, report, currentYearsStartDate,currentYearsEndDate, fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate());
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.INCOME_EMAALEP, report, currentYearsStartDate,currentYearsEndDate, minimumBondDate, previousYearsEndDate);
			}
		}
		if(criteria.getReportType() == ReportType.BALANCE){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.BALANCE, report, currentYearsStartDate,currentYearsEndDate, fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate());
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.BALANCE, report, minimumBondDate, previousYearsEndDate, null, null);
			}
		}
		if(criteria.getReportType() == ReportType.VOID){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.VOID, report, currentYearsStartDate, currentYearsEndDate, null, null);
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.VOID, report, minimumBondDate, previousYearsEndDate, null, null);
			}
		}
		if(criteria.getReportType() == ReportType.DUE_PORTFOLIO){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.DUE_PORTFOLIO, report,  currentYearsStartDate, currentYearsEndDate, null, null);
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.DUE_PORTFOLIO, report, minimumBondDate, previousYearsEndDate, null, null);
			}
		}
		if(criteria.getReportType() == ReportType.QUOTAS_LIQUIDATION){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.QUOTAS_LIQUIDATION, report, criteria.getStartDate(),criteria.getEndDate(), fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate());
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.QUOTAS_LIQUIDATION, report, criteria.getStartDate(),criteria.getEndDate(), minimumBondDate, previousYearsEndDate);
			}
		}
		if(criteria.getReportType() == ReportType.SUBSCRIPTION){
			if(reportFilter == ReportFilter.CURRENT){
				report = findDetail(criteria, accountId, ReportType.SUBSCRIPTION, report, criteria.getStartDate(),criteria.getEndDate(), fiscalPeriod.getStartDate(), fiscalPeriod.getEndDate());
			}
			
			if(reportFilter == ReportFilter.PREVIOUS){
				report = findDetail(criteria, accountId, ReportType.SUBSCRIPTION, report, criteria.getStartDate(),criteria.getEndDate(), minimumBondDate, previousYearsEndDate);
			}
		}
		return report;
	}
	
	private AccountDetail createDetail(Object[] row){
		AccountDetail detail = new AccountDetail();
		Long accountNumber = ((BigInteger) row[0]).longValue();
		detail.setBondNumber(accountNumber);
		detail.setIdentificationNumber((String)row[1]);
		detail.setResidentName((String)row[2]);
		detail.setExpirationDate((Date)row[4]);
		detail.setDebit(BigDecimal.ZERO);
		detail.setCredit(BigDecimal.ZERO);
		return detail;
	}
	
	private void setValue(AccountDetail detail, BigDecimal value, ReportType reportType, AccountDetail totalItem){
		if(reportType == ReportType.INCOME || reportType == ReportType.QUOTAS_LIQUIDATION || reportType == ReportType.INCOME_EMAALEP){
			detail.setCredit(value);
			totalItem.setCredit(totalItem.getCredit().add(value));
		} else {
			value = (value == null)? BigDecimal.ZERO : value;
			detail.setDebit(value);
			totalItem.setDebit(totalItem.getDebit().add(value));
		}
	}
	
	private List<Long> getEmittedStatuses(){
		List<Long> statuses = new ArrayList<Long>();
		statuses.add((Long)systemParameterService.findParameter(IncomeService.PENDING_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.BLOCKED_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.IN_PAYMENT_AGREEMENT_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.COMPENSATION_BOND_STATUS));
		//rfam 2018-05-15 para soportar pagos en abonos
		statuses.add((Long)systemParameterService.findParameter(IncomeService.SUBSCRIPTION_BOND_STATUS));
		return statuses; 
	}
	
	private List<Long> getValidStatuses(){
		List<Long> statuses = new ArrayList<Long>();
		statuses.addAll(getEmittedStatuses());
		statuses.addAll(getPaidStatuses());
		return statuses; 
	}
	
	
	private List<Long> getPaidStatuses(){
		List<Long> statuses = new ArrayList<Long>();
		statuses.add((Long)systemParameterService.findParameter(IncomeService.PAID_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.PAID_FROM_EXTERNAL_CHANNEL_BOND_STATUS));
		return statuses;
	}
	
	private List<Long> getSubscriptionStatuses(){
		List<Long> statuses = new ArrayList<Long>();
		statuses.add((Long)systemParameterService.findParameter(IncomeService.PAID_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.PAID_FROM_EXTERNAL_CHANNEL_BOND_STATUS));
		return statuses;
	}
	
	private List<Long> getVoidStatuses(){
		List<Long> statuses = new ArrayList<Long>();
		statuses.add((Long)systemParameterService.findParameter(IncomeService.REVERSED_BOND_STATUS));
		return statuses;
	}
	
	private Account findAccountByDefinedParameter(String parameterName){
		Long accountId = systemParameterService.findParameter(parameterName);
		Account account = entityManager.find(Account.class, accountId);
		return account;
	}
	
	private List<Long> getBalancesStatuses(){
		List<Long> statuses = new ArrayList<Long>();
		statuses.add((Long)systemParameterService.findParameter(IncomeService.PENDING_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.BLOCKED_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.IN_PAYMENT_AGREEMENT_BOND_STATUS));
		statuses.add((Long)systemParameterService.findParameter(IncomeService.COMPENSATION_BOND_STATUS));
		//rfam 2018-05-15 para soportar pagos en abonos
		statuses.add((Long)systemParameterService.findParameter(IncomeService.SUBSCRIPTION_BOND_STATUS));
		return statuses; 
	}
	
}
