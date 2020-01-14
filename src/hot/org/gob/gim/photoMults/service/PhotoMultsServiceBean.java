package org.gob.gim.photoMults.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.photoMults.dto.CriteriaReport;
import org.gob.gim.photoMults.dto.ReportPhotoMultsDTO;

/**
 * * @author René Ortega * @Fecha 2020-01-09
 */

@Stateless(name = "PhotoMultsService")
public class PhotoMultsServiceBean implements PhotoMultsService {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<ReportPhotoMultsDTO> findPhotoMults(CriteriaReport criteria,
			Integer firstRow, Integer numberOfRows) {

		Query query = null;

		switch (criteria.getReportType()) {
		case PHOTO_MULTS_EMIT: {

			query = entityManager
					.createNativeQuery("SELECT mb.number, "
							+ "mb.emisiondate, "
							+ "mbs.name as status, "
							+ "ent. name as entry, "
							+ "coalesce(mb.base, 0.00) as value, "
							+ "res.identificationnumber, "
							+ "res.name, "
							+ "ant.citationdate, "
							+ "ant.contraventionnumber, "
							+ "ant.numberplate, "
							+ "ant.physicalinfractionnumber, "
							+ "ant.servicetype, "
							+ "coalesce(ant.speeding, 0.00), "
							+ "ant.supportdocumenturl, "
							+ "ant.vehicletype, "
							+ "mb.creationdate "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (3,4,5,6,7,9,11,13,14) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.emisiondate BETWEEN ?1 AND ?2 "
							+ "ORDER BY mb.emisiondate, mb.number asc");
			break;
		}
		case PHOTO_MULTS_PRE_EMIT: {
			query = entityManager
					.createNativeQuery("SELECT mb.number, "
							+ "mb.creationdate, "
							+ "mbs.name as status, "
							+ "ent. name as entry, "
							+ "coalesce(mb.base, 0.00) as value, "
							+ "res.identificationnumber, "
							+ "res.name, "
							+ "ant.citationdate, "
							+ "ant.contraventionnumber, "
							+ "ant.numberplate, "
							+ "ant.physicalinfractionnumber, "
							+ "ant.servicetype, "
							+ "coalesce(ant.speeding, 0.00), "
							+ "ant.supportdocumenturl, "
							+ "ant.vehicletype, "
							+ "mb.creationdate "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (2) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.creationdate BETWEEN ?1 AND ?2 "
							+ "ORDER BY mb.creationdate, mb.number asc");
			break;
		}
		case PHOTO_MULTS_LOW: {
			query = entityManager
					.createNativeQuery("SELECT mb.number, "
							+ "mb.reverseddate, "
							+ "mbs.name as status, "
							+ "ent. name as entry, "
							+ "coalesce(mb.base, 0.00) as value, "
							+ "res.identificationnumber, "
							+ "res.name, "
							+ "ant.citationdate, "
							+ "ant.contraventionnumber, "
							+ "ant.numberplate, "
							+ "ant.physicalinfractionnumber, "
							+ "ant.servicetype, "
							+ "coalesce(ant.speeding, 0.00), "
							+ "ant.supportdocumenturl, "
							+ "ant.vehicletype, "
							+ "mb.creationdate "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (9) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.reverseddate BETWEEN ?1 AND ?2 "
							+ "ORDER BY mb.reverseddate, mb.number asc");
			break;
		}
		case PHOTO_MULTS_PAID: {
			query = entityManager
					.createNativeQuery("SELECT mb.number, "
							+ "mb.liquidationdate, "
							+ "mbs.name as status, "
							+ "ent. name as entry, "
							+ "coalesce(mb.base, 0.00) as value, "
							+ "res.identificationnumber, "
							+ "res.name, "
							+ "ant.citationdate, "
							+ "ant.contraventionnumber, "
							+ "ant.numberplate, "
							+ "ant.physicalinfractionnumber, "
							+ "ant.servicetype, "
							+ "coalesce(ant.speeding, 0.00), "
							+ "ant.supportdocumenturl, "
							+ "ant.vehicletype, "
							+ "mb.creationdate "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (6,11) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.liquidationdate BETWEEN ?1 AND ?2 "
							+ "ORDER BY mb.liquidationdate, mb.number asc");
			
			break;
		}
		case PHOTO_MULTS_EXTEMPORARY: {
			query = entityManager
					.createNativeQuery("SELECT 	mb.number,  "
							+ "mb.creationdate,  "
							+ "mbs.name as status,  "
							+ "ent. name as entry,  "
							+ "coalesce(mb.base, 0.00) as value,  "
							+ "res.identificationnumber,  "
							+ "res.name,  "
							+ "ant.citationdate,  "
							+ "ant.contraventionnumber,  "
							+ "ant.numberplate,  "
							+ "ant.physicalinfractionnumber,  "
							+ "ant.servicetype,  "
							+ "coalesce(ant.speeding, 0.00),  "
							+ "ant.supportdocumenturl,  "
							+ "ant.vehicletype,  "
							+ "mb.creationdate "
							+ "FROM municipalbond mb  "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id  "
							+ "INNER JOIN resident res ON res.id = mb.resident_id  "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id  "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id  "
							+ "where mb.creationdate > obtener_fecha_tope_ingreso_fm(DATE(ant.citationdate)) "
							+ "AND ant.citationdate is not null  "
							+ "AND mb.entry_id IN (643,644)  "
							+ "AND mb.creationdate BETWEEN ?1 AND ?2 "
							+ "and mbs.id not in (8,9,10) "
							+ "ORDER BY ant.citationdate, mb.number asc ");
			break;
		}

		default:
			break;
		}
		query.setParameter(1, criteria.getStartDate(), TemporalType.DATE);
		query.setParameter(2, criteria.getEndDate(), TemporalType.DATE);
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<ReportPhotoMultsDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), ReportPhotoMultsDTO.class);

		return retorno;

	}

	@Override
	public Long findPhotoMultsNumber(CriteriaReport criteria) {
		Query query = null;
		
		switch (criteria.getReportType()) {
		case PHOTO_MULTS_EMIT: {
			query = entityManager
					.createNativeQuery("SELECT COUNT(mb.id) "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (3,4,5,6,7,9,11,13,14) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.emisiondate BETWEEN ?1 AND ?2 ");
			break;
		}
		case PHOTO_MULTS_PRE_EMIT: {
			query = entityManager
					.createNativeQuery("SELECT COUNT(mb.id) "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (2) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.creationdate BETWEEN ?1 AND ?2 ");
			break;
		}
		case PHOTO_MULTS_LOW: {
			query = entityManager
					.createNativeQuery("SELECT COUNT(mb.id) "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (9) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.reverseddate BETWEEN ?1 AND ?2 ");
			break;
		}
		case PHOTO_MULTS_PAID: {
			query = entityManager
					.createNativeQuery("SELECT COUNT(mb.id) "
							+ "FROM municipalbond mb "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
							+ "INNER JOIN resident res ON res.id = mb.resident_id "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
							+ "WHERE mb.municipalbondstatus_id IN (6,11) "
							+ "AND mb.entry_id IN (643,644) "
							+ "AND mb.liquidationdate BETWEEN ?1 AND ?2 ");
			
			break;
		}
		case PHOTO_MULTS_EXTEMPORARY: {
			query = entityManager
					.createNativeQuery("SELECT COUNT(mb.id) "
							+ "FROM municipalbond mb  "
							+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id  "
							+ "INNER JOIN resident res ON res.id = mb.resident_id  "
							+ "INNER JOIN entry ent ON ent.id = mb.entry_id  "
							+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id  "
							+ "where mb.creationdate > obtener_fecha_tope_ingreso_fm(DATE(ant.citationdate)) "
							+ "AND ant.citationdate is not null  "
							+ "AND mb.entry_id IN (643,644)  "
							+ "AND mb.creationdate BETWEEN ?1 AND ?2 "
							+ "and mbs.id not in (8,9,10)");
			break;
		}

		default:
			break;
		}
		query.setParameter(1, criteria.getStartDate(), TemporalType.DATE);
		query.setParameter(2, criteria.getEndDate(), TemporalType.DATE);

		BigInteger size = (BigInteger) query.getSingleResult();

		return size.longValue();
	}

	/*
	 * @Override public List<ImpugnmentDTO>
	 * findImpugnmentsForCriteria(ImpugnmentSearchCriteria criteria) { Query
	 * query = entityManager.createNativeQuery("SELECT imp.id AS id, "
	 * +"imp.creationdate, " +"imp.numberinfringement, "
	 * +"imp.numberprosecution, " +"imp.numbertramit, " +"imp.observation, "
	 * +"imp.updatedate, " +"mb.id AS municipalbond_id, "
	 * +"itm.id AS status_id, " +"imp.userregister_id, " +"imp.userupdate_id, "
	 * +"res.identificationnumber, " +"res.name AS resident_name, "
	 * +"itm.name AS statusname, " +"mb.number AS municipalbond_number, "
	 * +"mb.value AS municipalbond_value, " +"itm.code AS statuscode "
	 * +"FROM Impugnment imp "
	 * +"INNER JOIN municipalBond mb ON (mb.id = imp.municipalbond_id)"
	 * +"INNER JOIN resident res ON (res.id = mb.resident_id) "
	 * +"INNER JOIN itemcatalog itm ON (itm.id = imp.status_itm_id) " +
	 * "WHERE (CAST(?1 AS text) = '' OR imp.numberInfringement = CAST(?1 AS text)) "
	 * +
	 * "AND (CAST(?2 AS text) = '' OR imp.numberProsecution = CAST(?2 AS text)) "
	 * +
	 * "AND (CAST(?3 AS text) = '' OR res.identificationNumber = CAST(?3 AS text)) "
	 * +"AND (?4 = 0 OR itm.id = ?4) " +"ORDER BY imp.id DESC");
	 * query.setParameter(1, criteria.getNumberInfringement() == null ? ""
	 * :criteria.getNumberInfringement()); query.setParameter(2,
	 * criteria.getNumberProsecution() ==null ? ""
	 * :criteria.getNumberProsecution()); query.setParameter(3,
	 * criteria.getIdentificationNumber() == null ? ""
	 * :criteria.getIdentificationNumber()); query.setParameter(4,
	 * criteria.getState() == null ? BigInteger.ZERO
	 * :criteria.getState().getId());
	 * 
	 * List<ImpugnmentDTO> retorno =
	 * NativeQueryResultsMapper.map(query.getResultList(), ImpugnmentDTO.class);
	 * 
	 * return retorno; }
	 */

}
