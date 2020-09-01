package org.gob.gim.photoMults.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.photoMults.dto.CriteriaReport;
import org.gob.gim.photoMults.dto.ReportPhotoMultsDTO;
import org.gob.gim.photoMults.dto.ReportPhotoMultsSummaryDTO;

import ec.gob.gim.revenue.model.DTO.ReportEmissionDTO;

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

			String qry = "SELECT mb.number, "
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
					+ "mb.creationdate, "
					+ "ant.id, "
					+ "ant.documentVisualizationsNumber "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (3,4,5,6,7,9,11,13,14) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.emisiondate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			qry += "ORDER BY mb.emisiondate, mb.number asc";

			query = entityManager.createNativeQuery(qry);
			break;
		}
		case PHOTO_MULTS_PRE_EMIT: {
			String qry = "SELECT mb.number, "
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
					+ "mb.creationdate, "
					+ "ant.id, "
					+ "ant.documentVisualizationsNumber "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (2) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.creationdate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			qry += "ORDER BY mb.creationdate, mb.number asc";

			query = entityManager.createNativeQuery(qry);
			break;
		}
		case PHOTO_MULTS_LOW: {
			String qry = "SELECT mb.number, "
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
					+ "mb.creationdate, "
					+ "ant.id, "
					+ "ant.documentVisualizationsNumber "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (9) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.reverseddate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			qry += "ORDER BY mb.reverseddate, mb.number asc";

			query = entityManager.createNativeQuery(qry);
			break;
		}
		case PHOTO_MULTS_PAID: {
			String qry = "SELECT mb.number, "
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
					+ "mb.creationdate, "
					+ "ant.id, "
					+ "ant.documentVisualizationsNumber "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (6,11) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.liquidationdate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			qry += "ORDER BY mb.liquidationdate, mb.number asc";

			query = entityManager.createNativeQuery(qry);

			break;
		}
		case PHOTO_MULTS_EXTEMPORARY: {
			String qry = "SELECT 	mb.number,  "
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
					+ "mb.creationdate, "
					+ "ant.id, "
					+ "ant.documentVisualizationsNumber "
					+ "FROM municipalbond mb  "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id  "
					+ "INNER JOIN resident res ON res.id = mb.resident_id  "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id  "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id  "
					+ "where mb.creationdate > obtener_fecha_tope_ingreso_fm(DATE(ant.citationdate)) "
					+ "AND ant.citationdate is not null  "
					+ "AND mb.entry_id IN (643,644)  "
					+ "AND mb.creationdate BETWEEN ?1 AND ?2 "
					+ "and mbs.id not in (8,9,10) ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			qry += "ORDER BY ant.citationdate, mb.number asc ";
			query = entityManager.createNativeQuery(qry);
			break;
		}

		default:
			break;
		}
		query.setParameter(1, criteria.getStartDate(), TemporalType.DATE);
		query.setParameter(2, criteria.getEndDate(), TemporalType.DATE);

		if (criteria.getIdentificationNumber() != null) {
			if (criteria.getIdentificationNumber().trim().length() > 0)
				query.setParameter(3, criteria.getIdentificationNumber().trim());
		}

		if (criteria.getResidentName() != null) {
			if (criteria.getResidentName().trim().length() > 0)
				query.setParameter(4, criteria.getResidentName().trim());
		}

		if (criteria.getInfractionNumber() != null) {
			if (criteria.getInfractionNumber().trim().length() > 0)
				query.setParameter(5, criteria.getInfractionNumber().trim());
		}

		if (criteria.getPlate() != null) {
			if (criteria.getPlate().trim().length() > 0)
				query.setParameter(6, criteria.getPlate().trim());
		}

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
			String qry = "SELECT COUNT(mb.id) "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (3,4,5,6,7,9,11,13,14) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.emisiondate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			query = entityManager.createNativeQuery(qry);

			break;
		}
		case PHOTO_MULTS_PRE_EMIT: {

			String qry = "SELECT COUNT(mb.id) "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (2) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.creationdate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			query = entityManager.createNativeQuery(qry);

			break;
		}
		case PHOTO_MULTS_LOW: {

			String qry = "SELECT COUNT(mb.id) "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (9) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.reverseddate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			query = entityManager.createNativeQuery(qry);

			break;
		}
		case PHOTO_MULTS_PAID: {

			String qry = "SELECT COUNT(mb.id) "
					+ "FROM municipalbond mb "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id "
					+ "INNER JOIN resident res ON res.id = mb.resident_id "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
					+ "WHERE mb.municipalbondstatus_id IN (6,11) "
					+ "AND mb.entry_id IN (643,644) "
					+ "AND mb.liquidationdate BETWEEN ?1 AND ?2 ";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			query = entityManager.createNativeQuery(qry);

			break;
		}
		case PHOTO_MULTS_EXTEMPORARY: {

			String qry = "SELECT COUNT(mb.id) "
					+ "FROM municipalbond mb  "
					+ "INNER JOIN antreference ant ON ant.id = mb.adjunct_id  "
					+ "INNER JOIN resident res ON res.id = mb.resident_id  "
					+ "INNER JOIN entry ent ON ent.id = mb.entry_id  "
					+ "INNER JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id  "
					+ "where mb.creationdate > obtener_fecha_tope_ingreso_fm(DATE(ant.citationdate)) "
					+ "AND ant.citationdate is not null  "
					+ "AND mb.entry_id IN (643,644)  "
					+ "AND mb.creationdate BETWEEN ?1 AND ?2 "
					+ "and mbs.id not in (8,9,10)";

			if (criteria.getIdentificationNumber() != null) {
				if (criteria.getIdentificationNumber().trim().length() > 0)
					qry += "AND res.identificationnumber = ?3 ";
			}

			if (criteria.getResidentName() != null) {
				if (criteria.getResidentName().trim().length() > 0)
					qry += "AND res.name ilike %?4% ";
			}

			if (criteria.getInfractionNumber() != null) {
				if (criteria.getInfractionNumber().trim().length() > 0)
					qry += "AND ant.contraventionnumber = ?5 ";

			}

			if (criteria.getPlate() != null) {
				if (criteria.getPlate().trim().length() > 0)
					qry += "AND ant.numberplate = ?6 ";
			}

			query = entityManager.createNativeQuery(qry);

			break;
		}

		default:
			break;
		}
		query.setParameter(1, criteria.getStartDate(), TemporalType.DATE);
		query.setParameter(2, criteria.getEndDate(), TemporalType.DATE);

		if (criteria.getIdentificationNumber() != null) {
			if (criteria.getIdentificationNumber().trim().length() > 0)
				query.setParameter(3, criteria.getIdentificationNumber().trim());
		}

		if (criteria.getResidentName() != null) {
			if (criteria.getResidentName().trim().length() > 0)
				query.setParameter(4, criteria.getResidentName().trim());
		}

		if (criteria.getInfractionNumber() != null) {
			if (criteria.getInfractionNumber().trim().length() > 0)
				query.setParameter(5, criteria.getInfractionNumber().trim());
		}

		if (criteria.getPlate() != null) {
			if (criteria.getPlate().trim().length() > 0)
				query.setParameter(6, criteria.getPlate().trim());

		}

		BigInteger size = (BigInteger) query.getSingleResult();

		return size.longValue();

	}

	@Override
	public List<ReportPhotoMultsSummaryDTO> reportSummary(Date startDate,
			Date endDate) {
		try {

			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			Query query = entityManager
					.createNativeQuery("select * from reports.sp_reporte_resumen_foto_multas(?1, ?2)");
			query.setParameter(1, dt1.format(startDate));
			query.setParameter(2, dt1.format(endDate));

			List<ReportPhotoMultsSummaryDTO> retorno = NativeQueryResultsMapper.map(
					query.getResultList(), ReportPhotoMultsSummaryDTO.class);

			return retorno;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
