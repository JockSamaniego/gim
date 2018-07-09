package ec.gob.gim.revenue.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.CreditNote;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.LegalStatus;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.TaxItem;
import ec.gob.gim.income.model.TaxpayerRecord;

/**
 * @author gerson
 * @version 1.0
	 * @created 04-Ago-2011 16:30:30
 */
@Audited
@Entity
@TableGenerator(name = "MunicipalBondGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "MunicipalBond", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "MunicipalBond.findByResidentIdAndTypeAndStatus", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry e "
				//rfam 2018-05-10 este dato se obtine desde la vista Gim.java
				//+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				// rarmijos 2015-12-19
//				+ "LEFT JOIN FETCH it.electronicItem "
				//
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				// rarmijos 2015-12-19
//				+ "LEFT JOIN FETCH di.electronicItem  "
				//
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				// rarmijos 2015-12-19
//				+ "LEFT JOIN FETCH si.electronicItem "
				//
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "LEFT JOIN FETCH mb.adjunct "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mbs.id IN (:municipalBondStatusIds) "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.expirationDate, mb.id, mb.municipalBondStatus.id"),

		@NamedQuery(name = "MunicipalBond.findByFiscalPeriodResidentsEntryAndStatus", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry e "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "LEFT JOIN FETCH mb.adjunct "
				+ "WHERE mb.resident.id in (:residentIds) AND "
				+ "mb.fiscalPeriod.id =:fiscalPeriodId AND "
				+ "e.id in (:entryIds) AND "
				+ "mbs.id = :municipalBondStatusId "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.expirationDate, mb.id, mb.municipalBondStatus.id"),

		@NamedQuery(name = "MunicipalBond.findMunicipalBondByResidentIdAndEmitterAndDate", query = "SELECT mb FROM MunicipalBond mb "
				+ " LEFT JOIN FETCH mb.entry e"
				+ " LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ " LEFT JOIN FETCH mb.receipt re"
				+ " LEFT JOIN FETCH mb.resident r"
				+ " LEFT JOIN FETCH mb.emitter e"
				+ " WHERE r.id = :residentId AND e.id=:emitterId AND"
				+ " mbs.id = :statusId AND"
				+ " mb.emisionDate = :date"
				+ " ORDER BY mb.id"),

		@NamedQuery(name = "MunicipalBond.findInPaymentAgreementByStatusAndDates", query = "SELECT distinct mb.id FROM MunicipalBond mb "
				+ " JOIN mb.deposits d"
				+ " WHERE d.date BETWEEN :startDate AND :endDate AND mb.municipalBondStatus.id = :statusId"
				+ " AND mb.paymentAgreement is not null"),

		@NamedQuery(name = "MunicipalBond.findInPaymentAgreementAndStatusIdAndDates", query = "SELECT distinct mb FROM MunicipalBond mb "
				+ " left join fetch mb.receipt"
				+ " left join fetch mb.deposits"
				+ " WHERE mb.liquidationDate BETWEEN :startDate AND :endDate AND mb.municipalBondStatus.id in(:statusIds)"
				+ " AND mb.paymentAgreement is not null"),

		@NamedQuery(name = "MunicipalBond.findPaidInPaymentAgreementBetweenDates", query = "SELECT distinct mb.id FROM MunicipalBond mb "
				+ " JOIN mb.deposits d"
				+ " WHERE d.date BETWEEN :startDate AND :endDate "
				+ " AND mb.paymentAgreement is not null"),

		@NamedQuery(name = "MunicipalBond.findByIdsToChangeStatus", query = "SELECT mb FROM MunicipalBond mb "
				+ " LEFT JOIN FETCH mb.receipt re"
				+ " WHERE mb.id IN (:municipalBondIds)"),

		@NamedQuery(name = "MunicipalBond.findByIds", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry e "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "LEFT JOIN FETCH mb.adjunct "
				+ "WHERE mb.id IN (:municipalBondIds)" + "ORDER BY mb.id"),

		@NamedQuery(name = "MunicipalBond.findByIdsPaymentPlatform", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry e "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "WHERE mb.id IN (:municipalBondIds)" + "ORDER BY mb.id"),

		@NamedQuery(name = "MunicipalBond.findByResidentIdAndTypeAndStatusAndEntryAndGroupingCode", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry entry "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id IN (:municipalBondStatusIds) AND "
				+ "deposit is null AND entry.id = :entryId "
				+ "AND lower(mb.groupingCode) like lower(concat('%',:code,'%')) "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.serviceDate, mb.id"),

		@NamedQuery(name = "MunicipalBond.findByResidentIdAndTypeAndStatusBetweenDates", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.receipt r "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.emisionDate BETWEEN :startDate AND :endDate AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id IN (:municipalBondStatusIds) "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.expirationDate, mb.municipalBondStatus.id"),

		@NamedQuery(name = "MunicipalBond.findByResidentIdEntryIdAndTypeAndStatusBetweenDates", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.receipt r "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "LEFT JOIN FETCH mb.adjunct "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.entry.id=:entryId AND "
				+ "mb.emisionDate BETWEEN :startDate AND :endDate AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id IN (:municipalBondStatusIds) "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.expirationDate, mb.municipalBondStatus.id"),

		@NamedQuery(name = "MunicipalBond.findByResidentIdEntryIdAndTypeAndStatus", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.receipt r "
				+ "LEFT JOIN FETCH mb.resident resident "
				+ "LEFT JOIN FETCH resident.currentAddress "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "LEFT JOIN FETCH mb.adjunct "
				+ "WHERE resident.id=:residentId AND "
				+ "mb.entry.id=:entryId AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mbs.id IN (:municipalBondStatusIds) "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.expirationDate, mb.municipalBondStatus.id"),

		@NamedQuery(name = "MunicipalBond.findAccountsByDeposits", query = "SELECT distinct ac FROM MunicipalBond mb "
				+ "JOIN mb.entry entry "
				+ "JOIN entry.account ac "
				+ "JOIN mb.deposits dep "
				+ "where dep.date Between :startDate and :endDate "
				+ "AND mb.municipalBondStatus.id = :municipalBondStatusId"),

		@NamedQuery(name = "MunicipalBond.findAccountsByDepositsAndItem", query = "SELECT distinct ac FROM MunicipalBond mb "
				+ "JOIN mb.items i "
				+ "JOIN i.entry entry "
				+ "JOIN entry.account ac "
				+ "JOIN mb.deposits dep "
				+ "where dep.date Between :startDate and :endDate "
				+ "AND mb.municipalBondStatus.id = :municipalBondStatusId"),

		@NamedQuery(name = "MunicipalBond.findAccountsByDepositsAndSurchargeItem", query = "SELECT distinct ac FROM MunicipalBond mb "
				+ "JOIN mb.surchargeItems i "
				+ "JOIN i.entry entry "
				+ "JOIN entry.account ac "
				+ "JOIN mb.deposits dep "
				+ "where dep.date Between :startDate and :endDate "
				+ "AND mb.municipalBondStatus.id = :municipalBondStatusId"),

		@NamedQuery(name = "MunicipalBond.findAccountsByDepositsAndDiscountItem", query = "SELECT distinct ac FROM MunicipalBond mb "
				+ "JOIN mb.discountItems i "
				+ "JOIN i.entry entry "
				+ "JOIN entry.account ac "
				+ "JOIN mb.deposits dep "
				+ "where dep.date Between :startDate and :endDate "
				+ "AND mb.municipalBondStatus.id = :municipalBondStatusId"),

		@NamedQuery(name = "MunicipalBond.findAccountsByDepositsAndTaxItem", query = "SELECT distinct ac FROM MunicipalBond mb "
				+ "JOIN mb.taxItems i "
				+ "join i.tax t "
				+ "join t.taxAccount ac "
				+ "JOIN mb.deposits dep "
				+ "where dep.date Between :startDate and :endDate "
				+ "AND mb.municipalBondStatus.id = :municipalBondStatusId"),

		// @NamedQuery(name = "MunicipalBond.findAccountsByDepositsAndItem",
		// query = "SELECT distinct ac FROM MunicipalBond mb "
		// + "JOIN mb.items i "
		// + "JOIN i.entry entry "
		// + "JOIN entry.account ac "
		// + "JOIN mb.deposits dep "
		// + "WHERE dep.id in (:depositsIds)" +
		// "select distinct (d) from MunicipalBond m " +
		// "join m.deposits d " +
		// "where d.date Between :startDate and :endDate " +
		// "AND m.municipalBondStatus.id = :municipalBondStatusId"),

		@NamedQuery(name = "MunicipalBond.findById", query = "SELECT DISTINCT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry "
				+ "LEFT JOIN FETCH mb.municipalBondStatus "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "LEFT JOIN FETCH res.currentAddress "
				+ "LEFT JOIN FETCH mb.deposits deposits "
				+ "LEFT JOIN FETCH deposits.payment payment "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.receipt r "
				+ "LEFT JOIN FETCH r.receiptType "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "LEFT JOIN FETCH mb.adjunct adjunct "
				+ "LEFT JOIN FETCH mb.emitter emitter "
				+ "LEFT JOIN FETCH payment.cashier cashier "
				+ "WHERE mb.id = :municipalBondId"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByPerson", query = "SELECT NEW org.gob.gim.revenue.view.ReportView(mb.emitter.id,mb.emitter.name, count(mb.id),sum(mb.paidTotal)) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.emitter is not null and mb.emitter.id = :personId "
				+ "GROUP BY mb.emitter.name, mb.emitter.id"),

		@NamedQuery(name = "MunicipalBond.findDuePortfolioDTO", query = "SELECT NEW ec.gob.gim.coercive.model.DuePortfolioDTO(mb.number, e.department, r.name, r.identificationNumber, mb.address, "
				+ "e.name, mb.description, mb.emisionDate, mb.expirationDate, mb.paidTotal ) from MunicipalBond mb "
				+ "JOIN mb.entry e "
				+ "JOIN mb.resident r "
				+ "JOIN mb.municipalBondStatus mbs "
				+ "where mb.expirationDate Between :startDate and :endDate and "
				+ "mbs.id in (:statusIds) " + "ORDER BY mb.expirationDate"),

		@NamedQuery(name = "MunicipalBond.SumTotalNullifiedByPerson", query = "SELECT NEW org.gob.gim.revenue.view.ReportView(mb.emitter.id,mb.emitter.name, count(mb.id),sum(mb.paidTotal)) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.emitter is not null and mb.emitter.id = :personId and mb.municipalBondStatus.id = :statusId  "
				+ "GROUP BY mb.emitter.name, mb.emitter.id"),

		@NamedQuery(name = "MunicipalBond.findEmittedByDatesAndEmitter", query = "SELECT mb from MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.receipt r "
				+ "JOIN FETCH mb.resident resident "
				+ "JOIN FETCH mb.emitter emitter "
				+ "where mb.emisionDate Between :startDate and :endDate "
				//+ "and mb.emitter is not null "
				+ "and emitter.id = :personId "
				+ "GROUP BY emitter.name,emitter.id, mb.id,resident.id, r.id"),
				//+ "GROUP BY emitter.name,emitter.id, mb.id,resident.id "),

		@NamedQuery(name = "MunicipalBond.findMunicipalBondViewBetweenDates", query = "SELECT NEW ec.gob.gim.revenue.model.MunicipalBondView(mb.entry.id,mb.entry.name,mb.resident.name, mb.number, mb.address, sum(mb.value + mb.taxesTotal)) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate AND "
				+ "mb.municipalBondStatus.id in (:statusIds) "
				+ "GROUP BY mb.entry.id, mb.entry.name,mb.resident.name, mb.id, mb.address "
				+ "order by mb.id"),

		@NamedQuery(name = "MunicipalBond.findMunicipalBondViewBetweenDatesByEntry", query = "SELECT NEW ec.gob.gim.revenue.model.MunicipalBondView(mb.entry.id,mb.entry.name,mb.resident.name, mb.id, mb.address, sum(mb.value + mb.taxesTotal)) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.municipalBondStatus.id in (:statusId) and mb.entry.id = :entryId order by mb.id"),

		@NamedQuery(name = "MunicipalBond.findMunicipalBondViewBetweenDatesByMunicipalBondStatusAndEntry", query = "SELECT NEW ec.gob.gim.revenue.model.MunicipalBondView(mb.entry.id,mb.entry.name,mb.resident.name, mb.id, mb.address, mb.value) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.entry.id = :entryId AND mb.municipalBondStatus.id = :statusId order by mb.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmitted", query = "SELECT NEW org.gob.gim.revenue.view.ReportView(mb.emitter.id,mb.emitter.name, count(mb.id),sum(mb.paidTotal)) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.emitter is not null "
				+ "GROUP BY mb.emitter.name, mb.emitter.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalNullified", query = "SELECT NEW org.gob.gim.revenue.view.ReportView(mb.emitter.id, mb.emitter.name, count(mb.id),sum(mb.paidTotal)) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.emitter is not null and mb.municipalBondStatus.id = :statusId "
				+ "GROUP BY mb.emitter.name,mb.emitter.id"),

		@NamedQuery(name = "MunicipalBond.findEmittedByDates", query = "SELECT mb from MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.receipt r "
				+ "LEFT JOIN FETCH mb.resident resident "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.emitter is not null "
				+ "GROUP BY mb.emitter.name,mb.emitter.id, mb.id, resident.id, r.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryPreviousYears", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,SUM(m.value)) from MunicipalBond m join m.deposits d join d.payment p "
				+ "join m.entry e where m.emisionPeriod < :periodDate AND m.municipalBondStatus.id = :municipalBondStatusId AND p.date between :startDate AND :endDate"
				+ " GROUP BY e.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryPreviousYearsByItems", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,SUM(i.total)) from MunicipalBond m join m.items i "
				+ "join i.entry e where m.emisionPeriod < :periodDate AND m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)"
				+ " GROUP BY e.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryPreviousYearsByDiscountItems", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,SUM(i.total)) from MunicipalBond m join m.discountItems i "
				+ "join i.entry e where m.emisionPeriod < :periodDate AND m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)"
				+ " GROUP BY e.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryPreviousYearsBySurchargeItems", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,SUM(i.total)) from MunicipalBond m join m.surchargeItems i "
				+ "join i.entry e where m.emisionPeriod < :periodDate AND m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)"
				+ " GROUP BY e.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryPreviousYearsByTaxItems", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id,SUM(i.value)) from MunicipalBond m join m.taxItems i "
				+ "join i.tax t join t.taxAccount ac where m.emisionPeriod < :periodDate AND m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)"
				+ " GROUP BY ac.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryNextYears", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,SUM(m.value)) from MunicipalBond m join m.deposits d join d.payment p "
				+ "join m.entry e where m.emisionPeriod > :periodDate AND m.municipalBondStatus.id = :municipalBondStatusId AND p.date between :startDate AND :endDate"
				+ " GROUP BY e.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryPreviousYearsInPaymentAgreement", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,SUM(d.value)) from MunicipalBond m join m.deposits d "
				+ "join m.entry e where m.emisionPeriod < :startDate AND m.municipalBondStatus.id = :municipalBondStatusId"
				+ " GROUP BY e.id"),

		@NamedQuery(name = "MunicipalBond.SumTotalValuesByEntryNextYearsInPaymentAgreement", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,SUM(d.value)) from MunicipalBond m join m.deposits d "
				+ "join m.entry e where m.emisionPeriod > :endDate AND m.municipalBondStatus.id = :municipalBondStatusId"
				+ " GROUP BY e.id"),

		@NamedQuery(name = "MunicipalBond.findByNumberAndStatusAndResidentId", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.entry LEFT JOIN FETCH mb.creditNote "
				+ "WHERE "
				+ "mb.resident.id = :residentId AND "
				+ "mb.number = :municipalBondNumber AND "
				+ "mb.municipalBondStatus.id IN (:municipalBondStatusIds) "),

		@NamedQuery(name = "MunicipalBond.findByNumberAndGroupingCodeNull", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.entry LEFT JOIN FETCH mb.creditNote "
				+ "WHERE "
				+ "(mb.groupingCode is null OR TRIM (BOTH mb.groupingCode) = '' ) AND "
				+ "mb.number = :number "),

		@NamedQuery(name = "MunicipalBond.findOverdueByResidentIdAndTypeAndStatus", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.entry "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id = :municipalBondStatusId "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.expirationDate,mb.serviceDate"),

		@NamedQuery(name = "MunicipalBond.findByResidentIdAndTypeAndStatusAndDaysOutOfDateAndAmount", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.entry "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id=:municipalBondStatusId AND "
				+ "(lower(mb.entry.name) like lower(concat(:municipalEntryName,'%'))) AND "
				+ "mb.paidTotal>=:municipalBondPaymentTotal AND "
				+ "mb.fiscalPeriod=:municipalBondFiscalPeriod AND "
				+ "mb.expirationDate<=:municipalBondExpirationDate "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.serviceDate"),
		@NamedQuery(name = "MunicipalBond.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.entry "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.serviceDate=:serviceDate AND "
				+ "mb.entry.id=:entryId AND "
				+ "mb.groupingCode=:groupingCode " + "ORDER BY mb.entry.id"),

		@NamedQuery(name = "MunicipalBond.findByStatusIdAndGroupingCode", query = "SELECT distinct mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.entry e "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.municipalBondStatus mbs "
				+ "LEFT JOIN FETCH mb.receipt "
				+ "LEFT JOIN FETCH mb.resident "
				+ "LEFT JOIN FETCH mb.items it "
				+ "LEFT JOIN FETCH it.entry "
				+ "LEFT JOIN FETCH mb.discountItems di "
				+ "LEFT JOIN FETCH di.entry "
				+ "LEFT JOIN FETCH mb.deposits deposit "
				+ "LEFT JOIN FETCH mb.surchargeItems si "
				+ "LEFT JOIN FETCH si.entry "
				+ "LEFT JOIN FETCH mb.taxItems ti "
				+ "LEFT JOIN FETCH ti.tax "
				+ "LEFT JOIN FETCH mb.adjunct "
				+ "WHERE mb.municipalBondStatus.id=:statusId AND "
				+ "(mb.groupingCode like lower(concat('%',:groupingCode,'%'))) "
				+ "ORDER BY mb.entry.id"),

		@NamedQuery(name = "MunicipalBond.findExpiratedByResidentIdAndAmount", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.entry "
				+ "WHERE "
				+ "mb.resident.id in (:residentIds) AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id=:municipalBondStatusId AND "
				+ "mb.expirationDate <= :expirationDate AND mb.notification IS NULL AND mb.value >= :value "
				+ "ORDER BY mb.entry.id"),

		@NamedQuery(name = "MunicipalBond.findExpiratedByResidentIdAndEntryIdAndAmount", query = "SELECT mb FROM MunicipalBond mb "
				+ "WHERE "
				+ "mb.entry.id = :entryId AND "
				+ "mb.resident.id in (:residentIds) AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id=:municipalBondStatusId AND "
				+ "mb.expirationDate <= :expirationDate AND mb.notification IS NULL AND mb.value >= :value "
				+ "ORDER BY mb.entry.id"),

		@NamedQuery(name = "MunicipalBond.findByResidentIdAndEntryId", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.entry "
				+ "WHERE mb.resident.id=:residentId AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id=:municipalBondStatusId AND "
				+ "mb.entry.id=:entryId "
				+ "ORDER BY mb.entry.id, mb.groupingCode, mb.serviceDate"),

		@NamedQuery(name = "MunicipalBond.findByPaymentAgreementIdAndStatusId", query = "SELECT mb FROM MunicipalBond mb LEFT JOIN FETCH mb.institution "
				+ "WHERE mb.paymentAgreement.id=:paymentAgreementId AND "
				+ "mb.municipalBondStatus.id=:municipalBondStatusId "
				+ "ORDER BY expirationDate"),
		//rfam 2018-05-09 consulta de obligaciones en abono
		@NamedQuery(name = "MunicipalBond.findBySubscriptionStatusId", query = "SELECT mb FROM MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.institution "
				+ "LEFT JOIN FETCH mb.resident res "
				+ "WHERE mb.municipalBondStatus.id=:municipalBondStatusId "
				+ "and mb.resident.id=:residentId "
				+ "ORDER BY expirationDate"),

		@NamedQuery(name = "MunicipalBond.findByStatusAndCashierAndDate", query = "SELECT mb FROM MunicipalBond mb "
				+ "WHERE mb.municipalBondStatus.id=:municipalBondStatusId AND "
				+ "mb.municipalBondStatus.id=:municipalBondStatusId ORDER BY expirationDate"),

		@NamedQuery(name = "MunicipalBond.SumTotalsEmmitedByEntry", query = "select SUM(m.value) from MunicipalBond m join m.entry e left join e.account ac where m.emisionDate Between :startDate and :endDate "
				+ "and e.id =:entryId GROUP BY e.code, ac.budgetCertificateCode ORDER BY ac.budgetCertificateCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedBetweenDates", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(m.value)) from MunicipalBond m join m.entry e left join e.account ac where m.emisionDate Between :startDate and :endDate"
				+ " AND m.municipalBondStatus.id not in (:municipalBondStatusIds) GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalReversedBetweenDates", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(m.value)) from MunicipalBond m join m.entry e left join e.account ac where m.emisionDate Between :startDate and :endDate"
				+ " AND m.municipalBondStatus.id in (:municipalBondStatusIds) GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalReversedBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate and i.total > 0"
				+ " AND m.municipalBondStatus.id in (:municipalBondStatusIds) GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalFutureBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total), ac.parameterFutureEmission) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.creationDate Between :startDate and :endDate and i.total > 0"
				+ " AND m.municipalBondStatus.id =:municipalBondStatusId GROUP BY e.id, e.name,ac.accountCode,ac.parameterFutureEmission ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalFutureBetweenDatesByItemAndEntry", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total), ac.parameterFutureEmission) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.creationDate Between :startDate and :endDate and i.total > 0 and e.id=:entry_id "
				+ " AND m.municipalBondStatus.id =:municipalBondStatusId GROUP BY e.id, e.name,ac.accountCode,ac.parameterFutureEmission ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalPrepaidBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total),ac.parameterFutureEmission) "
				+ "from StatusChange sch "
				+ "join sch.municipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate and i.total > 0"
				+ " AND sch.explanation=:explanation GROUP BY e.id, e.name,ac.accountCode,ac.parameterFutureEmission ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalReversedBetweenDatesByTaxItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value)) from MunicipalBond m "
				+ "join m.taxItems ti left join ti.tax t left join t.taxAccount ac "
				+ "where m.emisionDate Between :startDate and :endDate and ti.value > 0"
				+ " AND m.municipalBondStatus.id in (:municipalBondStatusIds) GROUP BY t.id, t.name,ac.accountCode ORDER BY ac.accountCode"),
		// //
		@NamedQuery(name = "MunicipalBond.SumTotalFutureBetweenDatesByTaxItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value),  ac.parameterFutureEmission) from MunicipalBond m "
				+ "join m.taxItems ti left join ti.tax t left join t.taxAccount ac "
				+ "where m.creationDate Between :startDate and :endDate and ti.value > 0"
				+ " AND m.municipalBondStatus.id =:municipalBondStatusId GROUP BY t.id, t.name,ac.accountCode, ac.parameterFutureEmission ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalPrepaidBetweenDatesByTaxItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value),ac.parameterFutureEmission) "
				+ "from StatusChange sch "
				+ "join sch.municipalBond m "
				+ "join m.taxItems ti "
				+ "left join ti.tax t "
				+ "left join t.taxAccount ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and ti.value > 0 "
				+ " AND sch.explanation=:explanation"
				+ " GROUP BY t.id, t.name,ac.accountCode,ac.parameterFutureEmission ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedAndPendingBetweenDates", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(m.value)) from MunicipalBond m join m.entry e left join e.account ac where m.emisionDate Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id = :statusId"
				+ " GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedAndPendingBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id = :statusId and i.total > 0"
				+ " GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedBetweenDatesCurrentPeriodByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id, ac.accountName, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "AND m.emisionPeriod =  :emisionPeriod "
				+ "AND m.municipalBondStatus.id in (:statusIds) and i.total > 0"
				+ " GROUP BY ac.id, ac.accountName, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedBetweenDatesPreviousPeriodsByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id, ac.accountName, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "AND m.emisionPeriod <  :emisionPeriod "
				+ "AND m.municipalBondStatus.id in (:statusIds) and i.total > 0"
				+ " GROUP BY ac.id, ac.accountName, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedBetweenDatesFuturePeriodsByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected"
				+ "(ac.id, "
				+ "ac.accountName, "
				+ "ac.accountCode, "
				+ "SUM(i.total)) "
				+ "from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.municipalBondStatus.id =:statusId and i.total > 0"
				+ " and m.creationDate Between :startDate and :endDate"
				+ " GROUP BY ac.id, ac.accountName, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalTaxesEmittedAndPendingBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value)) from MunicipalBond m "
				+ "join m.taxItems ti left join ti.tax t left join t.taxAccount ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id = :statusId and ti.value > 0"
				+ " GROUP BY t.id, t.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalTaxesEmittedByPeriodBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value)) from MunicipalBond m "
				+ "join m.taxItems ti "
				+ "left join ti.tax t "
				+ "left join t.taxAccount ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "AND m.emisionPeriod = :emisionPeriod "
				+ "AND m.municipalBondStatus.id in (:statusIds) " +
				// "and ti.value > 0" +
				" GROUP BY t.id, t.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalTaxesEmittedByPreviousPeriodsBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value)) from MunicipalBond m "
				+ "LEFT JOIN m.taxItems ti "
				+ "LEFT JOIN ti.tax t "
				+ "LEFT JOIN t.taxAccount ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "AND m.emisionPeriod < :emisionPeriod "
				+ "AND m.municipalBondStatus.id in (:statusIds) " +
				// "and ti.value > 0" +
				" GROUP BY t.id, t.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalTaxesEmittedByFuturePeriodsBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value)) from MunicipalBond m "
				+ "INNER JOIN m.taxItems ti "
				+ "LEFT JOIN ti.tax t "
				+ "LEFT JOIN t.taxAccount ac "
				+ "where m.creationDate Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id =:statusId "
				+ " GROUP BY t.id, t.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate and i.total > 0"
				+ " AND m.municipalBondStatus.id not in (:municipalBondStatusIds) GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedBetweenDatesAndEmisionPeriodByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and i.total > 0 "
				+ "and m.emisionPeriod = :emisionPeriod "
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) "
				+ "GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),

		/*
		 * nunca se la ha usado--------------------- para reporte de emision
		 * 
		 * @NamedQuery(name=
		 * "MunicipalBond.SumTotalEmittedBetweenDatesPreviousPeriodsByItem",
		 * query=
		 * "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
		 * + "join m.items i join i.entry e left join e.account ac " +
		 * "where m.emisionDate Between :startDate and :endDate " +
		 * "and i.total > 0 " + "and m.emisionPeriod = :emisionPeriod " +
		 * "AND m.municipalBondStatus.id in (:municipalBondStatusIds) " +
		 * "GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),
		 */

		@NamedQuery(name = "MunicipalBond.SumTotalTaxesEmittedBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(t.id, t.name, ac.accountCode, SUM(ti.value)) from MunicipalBond m "
				+ "join m.taxItems ti join ti.tax t join t.taxAccount ac "
				+ "where m.emisionDate Between :startDate and :endDate and ti.value > 0"
				+ " AND m.municipalBondStatus.id not in (:municipalBondStatusIds) GROUP BY t.id, t.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDates", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(m.value)) from MunicipalBond m join m.entry e left join e.account ac where m.emisionDate Between :startDate and :endDate "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id not in (:municipalBondStatusIds)"
				+ " GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id not in (:municipalBondStatusIds)"
				+ " GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalReversedByEntryBetweenDates", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(m.value)) from MunicipalBond m join m.entry e left join e.account ac where m.emisionDate Between :startDate and :endDate "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:municipalBondStatusIds)"
				+ " GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalReversedByEntryBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:municipalBondStatusIds) and i.value > 0"
				+ " GROUP BY e.id, e.name, ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedAndPendingByEntryBetweenDates", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(m.value)) from MunicipalBond m join m.entry e left join e.account ac where m.emisionDate Between :startDate and :endDate "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id = :statusId"
				+ " GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedAndPendingByEntryBetweenDatesByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id = :statusId"
				+ " GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndCurrrentPeriodByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and m.emisionPeriod = :emisionPeriod "
				+ "and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:statusIds)"
				+ " GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndCurrrentPeriodBySurchargeItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.surchargeItems i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and m.emisionPeriod = :emisionPeriod "
				+ "and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:statusIds)"
				+ " GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndCurrrentPeriodByDiscountItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.discountItems i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and m.emisionPeriod = :emisionPeriod "
				+ "and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:statusIds)"
				+ " GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndPreviousPeriodsByItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.items i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and m.emisionPeriod < :emisionPeriod "
				+ "and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:statusIds) "
				+ "GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndPreviousPeriodsBySurchargeItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.surchargeItems i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and m.emisionPeriod < :emisionPeriod "
				+ "and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:statusIds) "
				+ "GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndPreviousPeriodsByDiscountItem", query = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name, ac.accountCode, SUM(i.total)) from MunicipalBond m "
				+ "join m.discountItems i join i.entry e left join e.account ac "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "and m.emisionPeriod < :emisionPeriod "
				+ "and i.total > 0 "
				+ "AND e.id =:entryId AND m.municipalBondStatus.id in (:statusIds) "
				+ "GROUP BY e.id, e.name,ac.accountCode ORDER BY ac.accountCode"),

		@NamedQuery(name = "MunicipalBond.findByStatusAndDate", query = "select m from MunicipalBond m where m.emisionDate Between :startDate and :endDate and m.municipalBondStatus.id=:municipalBondStatusId ORDER BY m.id"),

		@NamedQuery(name = "MunicipalBond.findBetweenEmissionDates", query = "select distinct(mb) from MunicipalBond mb "
				+ "LEFT JOIN FETCH mb.receipt r "
				+ "LEFT JOIN FETCH mb.items i "
				+ "where mb.emisionDate Between :startDate and :endDate"),

		@NamedQuery(name = "MunicipalBond.findEntriesBetweenEmissionDates", query = "select distinct(mb.entry) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate and mb.municipalBondStatus.id in (:statusId)"),

		@NamedQuery(name = "MunicipalBond.findEntriesBetweenEmissionDatesAndMunicipalBondStatus", query = "select distinct(mb.entry) from MunicipalBond mb "
				+ "where mb.emisionDate Between :startDate and :endDate AND mb.municipalBondStatus.id = :statusId"),

		@NamedQuery(name = "MunicipalBond.findAccountBetweenEmissionDates", query = "select distinct(ac) from MunicipalBond mb left join mb.entry e left join e.account ac "
				+ "where mb.emisionDate Between :startDate and :endDate AND ac is not null"),

		@NamedQuery(name = "MunicipalBond.findAccountBetweenEmissionDatesByItem", query = "select distinct(ac) from MunicipalBond mb "
				+ "left join mb.items i left join i.entry e left join e.account ac "
				+ "where mb.emisionDate Between :startDate and :endDate AND ac is not null"),

		@NamedQuery(name = "MunicipalBond.findATaxAccountsBetweenEmissionDatesByItem", query = "select distinct(ac) from MunicipalBond mb "
				+ "left join mb.taxItems ti left join ti.tax ta left join ta.taxAccount ac "
				+ "where mb.emisionDate Between :startDate and :endDate AND ac is not null"),

		@NamedQuery(name = "MunicipalBond.findAccountBetweenEmissionDatesByEntry", query = "select distinct(ac) from MunicipalBond mb left join mb.entry e left join e.account ac "
				+ "where mb.emisionDate Between :startDate and :endDate AND ac is not null AND e.id = :entryId"),

		@NamedQuery(name = "MunicipalBond.findAccountBetweenEmissionDatesByEntryByItem", query = "select distinct(ac) from MunicipalBond mb "
				+ "left join mb.items i left join i.entry e left join e.account ac "
				+ "where mb.emisionDate Between :startDate and :endDate AND ac is not null AND e.id = :entryId"),

		@NamedQuery(name = "MunicipalBond.findTotalBetweenEmissionDates", query = "select sum(m.value) from MunicipalBond m where m.emisionDate Between :startDate and :endDate"),

		@NamedQuery(name = "MunicipalBond.findTotalByEntriesBetweenEmissionDates", query = "select e.id,sum(m.value) from MunicipalBond m left join m.entry e where m.emisionDate Between :startDate and :endDate GROUP BY e.id"),
		@NamedQuery(name = "MunicipalBond.findEntrieBetweenEmissionDates", query = "select distinct (e) from MunicipalBond m left join m.entry e where m.emisionDate Between :startDate and :endDate ORDER BY e.code"),

		@NamedQuery(name = "MunicipalBond.findMunicipalBonds", query = "select m from MunicipalBond m where m.emisionDate Between :startDate and :endDate ORDER BY m.id"),

		@NamedQuery(name = "MunicipalBond.setPaidStatus", query = "UPDATE MunicipalBond mb "
				+ "SET mb.balance = :balance, "
				+ "    mb.paidTotal = :paidTotal, "
				+ "    mb.interest = :interest, "
				+ "    mb.surcharge = :surcharge, "
				+ "    mb.discount = :discount, "
				+ "    mb.taxesTotal = :taxesTotal, "
				+ "    mb.taxableTotal = :taxableTotal, "
				+ "    mb.nonTaxableTotal = :nonTaxableTotal, "
				+ "    mb.printingsNumber = :printingsNumber, "
				+ "    mb.municipalBondStatus.id = :municipalBondStatusId "
				+ "WHERE mb.id IN (:municipalBondId)"),

		@NamedQuery(name = "MunicipalBond.setGroupingCodeByBondNumber", query = "UPDATE MunicipalBond mb "
				+ "SET mb.groupingCode = :groupingCode "
				+ "WHERE mb.number IN (:bondsNumbers)"),

		@NamedQuery(name = "MunicipalBond.changeStatus", query = "UPDATE MunicipalBond mb "
				+ " SET "
				+ "    mb.municipalBondStatus.id = :municipalBondStatusId "
				+ " WHERE mb.id IN (:municipalBondIds)"),

		@NamedQuery(name = "MunicipalBond.updateReverseDescriptionAndValue", query = "UPDATE MunicipalBond mb "
				+ " SET "
				+ "    mb.description = concat(mb.description,:observation), mb.value = mb.value * (-1)"
				+ " WHERE mb.id IN (:municipalBondIds)"),

		@NamedQuery(name = "MunicipalBond.updateReprintings", query = "UPDATE MunicipalBond mb "
				+ " SET "
				+ "    mb.printingsNumber = (mb.printingsNumber + 1) "
				+ " WHERE mb.id = :municipalBondId"),

		@NamedQuery(name = "MunicipalBond.updateLegalStatus", query = "UPDATE MunicipalBond mb "
				+ " SET "
				+ "    mb.legalStatus = :legalStatus, "
				+ "    mb.creditNote.id = :creditNoteId "
				+ " WHERE mb.id IN (:municipalBondIds)"),
		@NamedQuery(name = "MunicipalBond.updatePaymentAgreement", query = "UPDATE MunicipalBond mb "
				+ " SET "
				+ "    mb.municipalBondStatus.id = :municipalBondStatusId, "
				+ "    mb.paymentAgreement.id = :paymentAgreementId "
				+ " WHERE mb.id IN (:municipalBondIds)"),

		@NamedQuery(name = "Bond.findByStatusAndResidentId", query = "SELECT NEW org.gob.loja.gim.ws.dto.Bond("
				+ "    mb.id, mb.number, e.name, mb.groupingCode, mb.paidTotal, mb.serviceDate, mb.expirationDate, "
				+ "  mb.interest, mb.surcharge, mb.taxesTotal, mb.discount )"
				+ "  FROM "
				+ "    MunicipalBond mb "
				+ "    JOIN mb.entry e "
				+ "  WHERE "
				+ "    mb.resident.id=:residentId AND "
				+ "    mb.municipalBondType=:municipalBondType AND "
				+ "    mb.municipalBondStatus.id = :pendingBondStatusId"),

		@NamedQuery(name = "BondDetail.findBondDetailById", query = "SELECT NEW org.gob.loja.gim.ws.dto.BondDetail("
				+ "    mb.id, a.budgetCertificateName, i.entry.name, i.total)"
				+ "  FROM "
				+ "    MunicipalBond mb "
				+ "    JOIN mb.items i "
				+ "    JOIN i.entry e "
				+ "    JOIN e.subLineAccount a"
				+ "  WHERE " + "    mb.id=:bondId"),

		@NamedQuery(name = "BondDetail.findBondDetailByIds", query = "SELECT NEW org.gob.loja.gim.ws.dto.BondDetail("
				+ "    mb.id, a.budgetCertificateName, i.entry.name, i.total)"
				+ "  FROM "
				+ "    MunicipalBond mb "
				+ "    JOIN mb.items i "
				+ "    JOIN i.entry e "
				+ "    JOIN e.subLineAccount a"
				+ "  WHERE " + "    mb.id in (:bondIds) order by i.orderNumber"),

		@NamedQuery(name = "Bond.findIdsByStatusAndResidentId", query = "SELECT mb.id  FROM "
				+ "    MunicipalBond mb "
				+ "    JOIN mb.entry e "
				+ "  WHERE "
				+ "    mb.resident.id=:residentId AND "
				+ "    mb.municipalBondType=:municipalBondType AND "
				+ "    mb.municipalBondStatus.id = :pendingBondStatusId"),

		@NamedQuery(name = "Bond.countByStatusAndResidentId", query = "SELECT COUNT(mb)"
				+ "  FROM "
				+ "    MunicipalBond mb "
				+ "  WHERE "
				+ "    mb.resident.id=:residentId AND "
				+ "    mb.municipalBondType=:municipalBondType AND "
				+ "    mb.municipalBondStatus.id = :pendingBondStatusId"),
		@NamedQuery(name = "Bond.findTotalToPay", query = "SELECT SUM(mb.paidTotal)"
				+ "  FROM "
				+ "    MunicipalBond mb "
				+ "  WHERE "
				+ "    mb.resident.id=:residentId AND "
				+ "    mb.municipalBondType=:municipalBondType AND "
				+ "    mb.municipalBondStatus.id = :pendingBondStatusId AND"
				+ "    mb.id IN (:selectedBonds)"),

		@NamedQuery(name = "MunicipalBond.findSimpleByIds", query = "SELECT mb"
				+ "  FROM " + "    MunicipalBond mb " + "  WHERE "
				+ "    mb.id IN (:municipalBondIds)"),

		@NamedQuery(name = "MunicipalBond.findBondsCadastralByResidentAndGroupingCode", query = "SELECT mb FROM "
				+ "    MunicipalBond mb "
				+ "    JOIN mb.entry e "
				+ "  WHERE "
				+ "    mb.resident.id=:residentId AND "
				+ "    mb.municipalBondStatus.id = 3 AND "
				+ "    lower(mb.groupingCode) like lower(concat('%',:groupingCode,'%')) AND "
				+ "    mb.entry.id in (56,57,61) "
				+ "  ORDER BY mb.entry.id, mb.expirationDate desc"),

		@NamedQuery(name = "MunicipalBond.changeBondOfResident", query = "UPDATE MunicipalBond mb "
				+ "  SET mb.resident.id = :residentId "
				+ "  WHERE mb.id IN (:bondIds) "
				+ "  AND mb.municipalBondStatus.id=3"),
		@NamedQuery(name = "MunicipalBond.overduePortafolio", query = "SELECT NEW org.gob.gim.income.view.OverduePortfolio(account.accountCode,account.accountName,sum(item.total)) FROM Item item "
				+ "JOIN item.entry entry "
				+ "JOIN entry.account account "
				+ "where item.municipalBond.id in ("
				+ "select mb.id from MunicipalBond mb where mb.expirationDate < :date and mb.municipalBondStatus.id in (3)) "
				+ "GROUP BY account.accountCode,account.accountName "
				+ "ORDER BY account.accountCode"),
		@NamedQuery(name = "MunicipalBond.countMunicipalsBondsPendingFormalize", query = "SELECT count(*) "
				+ "from MunicipalBond mb "
				+ "join mb.municipalBondStatus mbs "
				+ "where mbs.id=:futureStatusId "
				+ "and :now >= mb.emisionDate"),
		@NamedQuery(name = "MunicipalBond.findLastId", query = "select max(municipalBond.id) from MunicipalBond municipalBond"),
		
		@NamedQuery(name = "MunicipalBond.findLastNumber", query = "select municipalBond.number from MunicipalBond municipalBond WHERE "
				+ "municipalBond.id = :id"),
		
		@NamedQuery(name = "Bond.findFutureByStatusAndResidentId", query = "SELECT NEW org.gob.loja.gim.ws.dto.FutureBond( "
				+ "e.name, mb.groupingCode, count(mb), sum(mb.paidTotal), sum(mb.interest), sum(mb.surcharge), sum(mb.taxesTotal), sum(mb.discount) ) "
				+ "FROM MunicipalBond mb "
				+ "JOIN mb.entry e "
				+ "WHERE "
				+ "mb.resident.id=:residentId AND "
				+ "mb.municipalBondType=:municipalBondType AND "
				+ "mb.municipalBondStatus.id = :pendingBondStatusId "
				+ "group by e.name, mb.groupingCode "
				+ "order by e.name, mb.groupingCode ")
})
//
public class MunicipalBond implements Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = 18386387333339876L;

	@Id
	@GeneratedValue(generator = "MunicipalBondGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Version
	private Long version = 0L;

	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Temporal(TemporalType.TIME)
	private Date creationTime;

	@Temporal(TemporalType.DATE)
	private Date liquidationDate;

	@Temporal(TemporalType.TIME)
	private Date liquidationTime;

	@Temporal(TemporalType.DATE)
	private Date reversedDate;

	@Temporal(TemporalType.TIME)
	private Date reversedTime;

	@Temporal(TemporalType.DATE)
	private Date emisionDate;

	@Temporal(TemporalType.TIME)
	private Date emisionTime;

	@Temporal(TemporalType.DATE)
	private Date expirationDate;

	@Temporal(TemporalType.DATE)
	private Date serviceDate;

	@Column(unique = true)
	private Long number;

	private String description;

	@Transient
	private Boolean isExpirationDateDefined;

	private String reference;

	@Temporal(TemporalType.DATE)
	private Date emisionPeriod;

	private Integer printingsNumber;

	@Column(length = 60)
	private String groupingCode;

	private String address;

	private String bondAddress;

	private Boolean exempt;

	private Boolean internalTramit;

	private Boolean isNoPasiveSubject;

	private Boolean applyInterest;

	private String reversedResolution;

	@Transient
	private Boolean isSelected;

	/**
	 * El valor base o cantidad ingresada para el calculo del rubro Por ejemplo
	 * Numero de tachos de basura o base imponible o avaluo del vehículo
	 */
	private BigDecimal base;

	private BigDecimal previousPayment;

	/**
	 * Valor emitido de la obligacion municipal
	 */
	private BigDecimal value;

	private BigDecimal interest;

	private BigDecimal discount;

	private BigDecimal surcharge;

	private BigDecimal balance;

	private BigDecimal taxesTotal;

	private BigDecimal paidTotal;

	@Transient
	private BigDecimal totalCancelled;

	private BigDecimal taxableTotal;

	private BigDecimal nonTaxableTotal;

	/**
	 * Relationships
	 */

	@OneToOne(mappedBy = "municipalBond", fetch = FetchType.LAZY)
	private Receipt receipt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notification_id")
	private Notification notification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "institution_id")
	private TaxpayerRecord institution;

	@ManyToOne(fetch = FetchType.LAZY)
	private PaymentAgreement paymentAgreement;

	@ManyToOne(fetch = FetchType.LAZY)
	private CreditNote creditNote;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private LegalStatus legalStatus;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private MunicipalBondType municipalBondType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalBondStatus_id")
	private MunicipalBondStatus municipalBondStatus;

	@OneToMany(mappedBy = "municipalBond", cascade = { CascadeType.MERGE,
			CascadeType.PERSIST })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<Item> items;

	@OneToMany(mappedBy = "discountedBond", cascade = { CascadeType.MERGE,
			CascadeType.PERSIST })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<Item> discountItems;

	@OneToMany(mappedBy = "surchargedBond", cascade = { CascadeType.MERGE,
			CascadeType.PERSIST })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<Item> surchargeItems;

	@ManyToOne(fetch = FetchType.LAZY)
	private Entry entry;

	@OneToMany(mappedBy = "municipalBond", fetch = FetchType.LAZY)
	@OrderBy("date, time asc")
	private Set<Deposit> deposits;

	@OneToMany(mappedBy = "municipalBond", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<TaxItem> taxItems;

	@NotAudited
	@ManyToOne(fetch = FetchType.LAZY)
	private Resident resident;

	@NotAudited
	@ManyToOne(fetch = FetchType.LAZY)
	private FiscalPeriod fiscalPeriod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "timePeriod_id")
	private TimePeriod timePeriod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emitter_id")
	private Person emitter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "originator_id")
	private Person originator;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinColumn(name = "adjunct_id")
	private Adjunct adjunct;
	
	//@author macartuche
	//@date 2016-08-11
	//@tag interesFactElec
	//aumentar campo de interesfactura
	private BigDecimal interestVoucher;
	private BigDecimal surchargeVoucher;

	public MunicipalBond() {
		// creationDate = Calendar.getInstance().getTime();
		// emisionDate = Calendar.getInstance().getTime();
		expirationDate = Calendar.getInstance().getTime();
		exempt = Boolean.FALSE;
		isNoPasiveSubject = Boolean.FALSE;
		interest = BigDecimal.ZERO;
		previousPayment = BigDecimal.ZERO;
		discount = BigDecimal.ZERO;
		surcharge = BigDecimal.ZERO;
		taxesTotal = BigDecimal.ZERO;
		taxableTotal = BigDecimal.ZERO;
		nonTaxableTotal = BigDecimal.ZERO;
		paidTotal = BigDecimal.ZERO;
		balance = BigDecimal.ZERO;
		legalStatus = LegalStatus.ACCEPTED;
		items = new TreeSet<Item>();
		discountItems = new TreeSet<Item>();
		surchargeItems = new TreeSet<Item>();
		deposits = new HashSet<Deposit>();
		taxItems = new TreeSet<TaxItem>();
		printingsNumber = 0;
		internalTramit = Boolean.FALSE;
	}

	public MunicipalBond(Long id, BigDecimal taxes, BigDecimal value) {
		/*
		 * exempt = Boolean.FALSE; isNoPasiveSubject = Boolean.FALSE; interest =
		 * BigDecimal.ZERO; previousPayment = BigDecimal.ZERO; discount =
		 * BigDecimal.ZERO; surcharge = BigDecimal.ZERO; taxesTotal =
		 * BigDecimal.ZERO; taxableTotal = BigDecimal.ZERO; nonTaxableTotal =
		 * BigDecimal.ZERO; paidTotal = BigDecimal.ZERO; balance =
		 * BigDecimal.ZERO; legalStatus = LegalStatus.ACCEPTED; items = new
		 * TreeSet<Item>(); discountItems = new TreeSet<Item>(); surchargeItems
		 * = new TreeSet<Item>(); deposits = new LinkedList<Deposit>(); taxItems
		 * = new TreeSet<TaxItem>(); printingsNumber = 0; internalTramit =
		 * Boolean.FALSE;
		 */
		this.id = id;
		this.value = value;
		this.taxesTotal = taxes;
	}

	public void calculateTaxableTotal() {
		taxableTotal = BigDecimal.ZERO;
		nonTaxableTotal = BigDecimal.ZERO;
		for (Item i : this.getItems()) {
			if (i.getIsTaxable()) {
				taxableTotal = taxableTotal.add(i.getTotal());
			} else {
				nonTaxableTotal = nonTaxableTotal.add(i.getTotal());
			}
		}
	}

	public BigDecimal getInterestTotal() {
		BigDecimal interestTotal = BigDecimal.ZERO;
		for (Deposit d : this.getDeposits()) {
			if (d.getStatus() == FinancialStatus.VALID) {
				interestTotal = interestTotal.add(d.getInterest());
			}
		}
		return interestTotal;
	}

	public BigDecimal findPaidTotal() {
		BigDecimal paidTotal = BigDecimal.ZERO;
		Set<Deposit> deps = new HashSet<Deposit>();
		
		for (Deposit d : this.getDeposits()) {
			deps.add(d);			
			paidTotal = paidTotal.add(d.getValue());
		}
		System.out.println(deps);
		return paidTotal;
	}

	public BigDecimal getSubTotal() {
		BigDecimal subTotal = BigDecimal.ZERO;
		for (Item item : this.getItems()) {
			subTotal = subTotal.add(item.getTotal());
		}
		return subTotal;
	}

	// TODO Revisar el calculo y cambiar de capas
	/*
	 * public BigDecimal getTotal() { BigDecimal total = BigDecimal.ZERO; total
	 * = total.add(getSubTotal()); total = total.add(getTaxesTotal()); total =
	 * total.add(getInterestTotal()); total = total.add(getSurcharge()); total =
	 * total.subtract(getDiscount()); return total; }
	 */

	public void calculateTaxesTotal() {
		taxesTotal = BigDecimal.ZERO;
		for (TaxItem taxItem : this.getTaxItems()) {
			taxesTotal = taxesTotal.add(taxItem.getValue());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getEmisionDate() {
		return emisionDate;
	}

	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getTransformedServiceDate() {
		String serviceDateStr = null;
		if (getEntry() != null && getEntry().getDatePattern() != null) {
			if (serviceDate != null) {
				DateFormat df = new SimpleDateFormat(getEntry()
						.getDatePattern());
				serviceDateStr = df.format(serviceDate);
			}
		}
		return serviceDateStr;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getGroupingCode() {
		return groupingCode;
	}

	public void setGroupingCode(String groupingCode) {
		this.groupingCode = groupingCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public MunicipalBondType getMunicipalBondType() {
		return municipalBondType;
	}

	public void setMunicipalBondType(MunicipalBondType municipalBondType) {
		this.municipalBondType = municipalBondType;
	}

	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public List<Item> getItems() {
		List<Item> itemsList = new ArrayList<Item>();
		itemsList.addAll(items);
		Collections.sort(itemsList);
		return itemsList;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void add(Item item) {
		if (!this.items.contains(item)) {
			this.items.add(item);
			item.setMunicipalBond(this);
		} else {
			System.out.println("MunicipalBond: Item was not added");
		}
	}

	public void remove(Item item) {
		boolean removed = this.items.remove(item);
		if (removed) {
			item.setMunicipalBond((MunicipalBond) null);
		}
	}

	public void calculateValue() {
		BigDecimal value = BigDecimal.ZERO;
		for (Item item : items) {
			BigDecimal aux = item.getTotal();
			value = value.add(aux);
		}
		this.setValue(value);
	}

	public BigDecimal getTaxableTotal() {
		return taxableTotal;
	}

	public void setTaxableTotal(BigDecimal taxableTotal) {
		this.taxableTotal = taxableTotal;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Boolean getExempt() {
		return exempt;
	}

	public void setExempt(Boolean exempt) {
		this.exempt = exempt;
	}

	public Boolean getIsNoPasiveSubject() {
		return isNoPasiveSubject;
	}

	public void setIsNoPasiveSubject(Boolean isNoPasiveSubject) {
		this.isNoPasiveSubject = isNoPasiveSubject;
	}

	public BigDecimal getBase() {
		return base;
	}

	public void setBase(BigDecimal base) {
		this.base = base;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}

	public Person getEmitter() {
		return emitter;
	}

	public void setEmitter(Person emitter) {
		this.emitter = emitter;
	}

	public Person getOriginator() {
		return originator;
	}

	public void setOriginator(Person originator) {
		this.originator = originator;
	}

	public Adjunct getAdjunct() {
		return adjunct;
	}

	public void setAdjunct(Adjunct adjunct) {
		this.adjunct = adjunct;
	}

	public String getAdjunctName() {
		if (entry != null) {
			String adjunctName = entry.getAdjunctClassName();
			int index = adjunctName.lastIndexOf('.');
			adjunctName = adjunctName.substring(index + 1);
			System.out.println("TAKE THE TIME -----> " + adjunctName);
			return adjunctName;
		}
		return "Empty";
	}

	public List<Item> getDiscountItems() {
		List<Item> itemsList = new ArrayList<Item>();
		itemsList.addAll(discountItems);
		Collections.sort(itemsList);
		return itemsList;
	}

	public void setDiscountItems(Set<Item> discountItems) {
		this.discountItems = discountItems;
	}

	public List<Item> getSurchargeItems() {
		List<Item> itemsList = new ArrayList<Item>();
		itemsList.addAll(surchargeItems);
		Collections.sort(itemsList);
		return itemsList;
	}

	public void setSurchargeItems(Set<Item> surchargeItems) {
		this.surchargeItems = surchargeItems;
	}

	public void addDiscountItem(Item item) {
		if (!this.discountItems.contains(item)) {
			this.discountItems.add(item);
			item.setDiscountedBond(this);
		}
	}

	public void removeDiscountItem(Item item) {
		boolean removed = this.discountItems.remove(item);
		if (removed) {
			item.setDiscountedBond((MunicipalBond) null);
		}
	}

	public void addSurchargeItem(Item item) {
		if (!this.surchargeItems.contains(item)) {
			this.surchargeItems.add(item);
			item.setSurchargedBond(this);
		}
	}

	public void removeSurchargeItem(Item item) {
		boolean removed = this.surchargeItems.remove(item);
		if (removed) {
			item.setSurchargedBond((MunicipalBond) null);
		}
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public Notification getNotification() {
		return notification;
	}

	@Transient
	public boolean isNotificationBeGiven() {
		return this.notification != null;
	}

	public PaymentAgreement getPaymentAgreement() {
		return paymentAgreement;
	}

	public void setPaymentAgreement(PaymentAgreement paymentAgreement) {
		this.paymentAgreement = paymentAgreement;
	}

	public Boolean getApplyInterest() {
		return applyInterest;
	}

	public void setApplyInterest(Boolean applyInterest) {
		this.applyInterest = applyInterest;
	}

	public String getReversedResolution() {
		return reversedResolution;
	}

	public void setReversedResolution(String reversedResolution) {
		this.reversedResolution = reversedResolution;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Set<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	public void add(Deposit deposit) {
		if (!this.deposits.contains(deposit)) {
			this.deposits.add(deposit);
			deposit.setMunicipalBond(this);
		}
	}

	public void remove(Deposit deposit) {
		boolean removed = this.deposits.remove(deposit);
		if (removed)
			deposit.setMunicipalBond(null);
	}

	public Boolean getIsCanceled() {
		System.out.println("BALANCE ---> " + balance);
		return balance != BigDecimal.ZERO;
	}

	public List<TaxItem> getTaxItems() {
		List<TaxItem> itemsList = new ArrayList<TaxItem>();
		itemsList.addAll(taxItems);
		return itemsList;
	}

	public void setTaxItems(Set<TaxItem> taxItems) {
		this.taxItems = taxItems;
	}

	public void add(TaxItem taxItem) {
		if (!this.taxItems.contains(taxItem)) {
			this.taxItems.add(taxItem);
			taxItem.setMunicipalBond(this);
		}
	}

	public void remove(TaxItem taxItem) {
		boolean removed = this.taxItems.remove(taxItem);
		if (removed)
			taxItem.setMunicipalBond(null);
	}

	public BigDecimal getTaxesTotal() {
		return taxesTotal;
	}

	public void setTaxesTotal(BigDecimal taxesTotal) {
		this.taxesTotal = taxesTotal;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public CreditNote getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(CreditNote creditNote) {
		this.creditNote = creditNote;
	}

	public LegalStatus getLegalStatus() {
		return legalStatus;
	}

	public void setLegalStatus(LegalStatus legalStatus) {
		this.legalStatus = legalStatus;
	}

	public Integer getPrintingsNumber() {
		return printingsNumber;
	}

	public void setPrintingsNumber(Integer printingsNumber) {
		this.printingsNumber = printingsNumber;
	}

	public Date getEmisionPeriod() {
		return emisionPeriod;
	}

	public void setEmisionPeriod(Date emisionPeriod) {
		this.emisionPeriod = emisionPeriod;
	}

	public TaxpayerRecord getInstitution() {
		return institution;
	}

	public void setInstitution(TaxpayerRecord institution) {
		this.institution = institution;
	}

	public Boolean getIsExpirationDateDefined() {
		return isExpirationDateDefined;
	}

	public void setIsExpirationDateDefined(Boolean isExpirationDateDefined) {
		this.isExpirationDateDefined = isExpirationDateDefined;
	}

	public BigDecimal getNonTaxableTotal() {
		return nonTaxableTotal;
	}

	public void setNonTaxableTotal(BigDecimal nonTaxableTotal) {
		this.nonTaxableTotal = nonTaxableTotal;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getEmisionTime() {
		return emisionTime;
	}

	public void setEmisionTime(Date emisionTime) {
		this.emisionTime = emisionTime;
	}

	public String getBondAddress() {
		return bondAddress;
	}

	public void setBondAddress(String bondAddress) {
		this.bondAddress = bondAddress;
	}

	public Boolean getInternalTramit() {
		return internalTramit;
	}

	public void setInternalTramit(Boolean internalTramit) {
		this.internalTramit = internalTramit;
	}

	public BigDecimal getPreviousPayment() {
		return previousPayment;
	}

	public void setPreviousPayment(BigDecimal previousPayment) {
		this.previousPayment = previousPayment;
	}

	public Date getLiquidationDate() {
		return liquidationDate;
	}

	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	public Date getLiquidationTime() {
		return liquidationTime;
	}

	public void setLiquidationTime(Date liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	public BigDecimal getTotalCancelled() {
		return totalCancelled;
	}

	public void setTotalCancelled(BigDecimal totalCancelled) {
		this.totalCancelled = totalCancelled;
	}

	public Date getReversedDate() {
		return reversedDate;
	}

	public void setReversedDate(Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	public Date getReversedTime() {
		return reversedTime;
	}

	public void setReversedTime(Date reversedTime) {
		this.reversedTime = reversedTime;
	}
	
	//@author macartuche
	//@date 2016-08-11
	//@tag interesFactElec
	//aumentar campo de interesfactura
	public BigDecimal getInterestVoucher() {
		return interestVoucher;
	}

	public void setInterestVoucher(BigDecimal interestVoucher) {
		this.interestVoucher = interestVoucher;
	}

	public BigDecimal getSurchargeVoucher() {
		return surchargeVoucher;
	}

	public void setSurchargeVoucher(BigDecimal surchargeVoucher) {
		this.surchargeVoucher = surchargeVoucher;
	}
	
	public List<Deposit> getDepositsList() {
		List<Deposit> list = new ArrayList<Deposit>(this.deposits);
		return list;
	}

}// end MunicipalBond
