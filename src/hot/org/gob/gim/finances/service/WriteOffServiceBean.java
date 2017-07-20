/**
 * 
 */
package org.gob.gim.finances.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;

import ec.gob.gim.finances.model.WriteOffRequest;
import ec.gob.gim.finances.model.DTO.ConsumptionPreviousDTO;
import ec.gob.gim.finances.model.DTO.WriteOffDetailDTO;
import ec.gob.gim.finances.model.DTO.WriteOffRequestDTO;

/**
 * @author rene
 *
 */
@Stateless(name = "WriteOffService")
public class WriteOffServiceBean implements WriteOffService {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<WriteOffDetailDTO> searchBondDetail(Long bond_number) {
		// TODO Auto-generated method stub

		Query query = this.entityManager
				.createNativeQuery("select 	mb.id as bond_id, "
						+ "mb.number as bond_number, "
						+ "con.month as mes, "
						+ "con.monthtype as mes_nombre, "
						+ "con.year as anio, "
						+ "con.previousreading as lectura_anterior, "
						+ "con.currentreading as lectura_actual, "
						+ "COALESCE(con.amount, -1) as consumo_m3, "
						+ "mb.value as valor, "
						+ "mb.adjunct_id "
						+ "FROM municipalbond mb "
						+ "LEFT JOIN waterservicereference wsr ON wsr.id = mb.adjunct_id "
						+ "LEFT JOIN consumption con ON con.id = wsr.consumption_id "
						+ "where mb.number =?1");
		query.setParameter(1, bond_number);

		List<WriteOffDetailDTO> retorno_bd = NativeQueryResultsMapper.map(
				query.getResultList(), WriteOffDetailDTO.class);

		return retorno_bd;
	}

	@Override
	public List<WriteOffRequestDTO> findByCriteria(
			String number_request_criteria,
			String identification_number_criteria,
			String name_resident_criteria, Integer firstRow,
			Integer numberOfRows) {
		Query query = this.entityManager
				.createNativeQuery("SELECT "
						+ "wor.id, "
						+ "wor.date, "
						+ "res.name AS resident_name, "
						+ "res.identificationnumber, "
						+ "was.ncalle as address, "
						+ "was.servicenumber, "
						+ "wme.serial, "
						+ "wrt.name AS _type, "
						+ "to_char(seq.code,'0000') number_code, "
						+ "EXTRACT(YEAR FROM wor.date) as _year, "
						+ "to_char(seq.code, '0000') || '-' || EXTRACT (YEAR FROM wor.date) AS request_number "
						+ "FROM "
						+ "writeoffrequest wor "
						+ "INNER JOIN resident res ON wor.resident_id = res.id "
						+ "INNER JOIN watermeter wme ON wor.watermeter_id = wme.id "
						+ "INNER JOIN watersupply was ON wme.watersupply_id = was.id "
						+ "INNER JOIN writeofftype wrt ON wor.writeofftype_id = wrt.id "
						+ "INNER JOIN sequencemanager seq ON wor.sequencemanager_id = seq.id "
						+ "WHERE (CAST(?1 AS text) = '' OR res.identificationnumber = CAST(?2 AS text)) "
						+ "AND (CAST(?3 AS text)= '' OR res.name LIKE CAST(?4 AS text)) "
						+ "AND (CAST(?5 AS text) = '' OR (to_char(seq.code, '0000') || '-' || EXTRACT (YEAR FROM wor.date)) LIKE CAST(?6 AS text)) "
						+ "ORDER BY request_number ASC");
		query.setParameter(1, identification_number_criteria == null ? ""
				: identification_number_criteria);
		query.setParameter(2, identification_number_criteria == null ? ""
				: identification_number_criteria);
		query.setParameter(3, name_resident_criteria == null ? "" : "%"
				+ name_resident_criteria + "%");
		query.setParameter(4, name_resident_criteria == null ? "" : "%"
				+ name_resident_criteria + "%");
		query.setParameter(5, number_request_criteria == null ? "" : "%"
				+ number_request_criteria + "%");
		query.setParameter(6, number_request_criteria == null ? "" : "%"
				+ number_request_criteria + "%");
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<WriteOffRequestDTO> retorno_bd = NativeQueryResultsMapper.map(
				query.getResultList(), WriteOffRequestDTO.class);

		return retorno_bd;
	}

	@Override
	public Long findWriteOffRequestsNumber(String number_request_criteria,
			String identification_number_criteria, String name_resident_criteria) {

		String stringQuery = "SELECT COUNT(wor.id) "
				+ "FROM "
				+ "writeoffrequest AS wor "
				+ "INNER JOIN resident res ON wor.resident_id = res.id "
				+ "INNER JOIN watermeter wme ON wor.watermeter_id = wme.id "
				+ "INNER JOIN watersupply was ON wme.watersupply_id = was.id "
				+ "INNER JOIN writeofftype wrt ON wor.writeofftype_id = wrt.id "
				+ "INNER JOIN sequencemanager seq ON wor.sequencemanager_id = seq.id "
				+ "WHERE (CAST(?1 AS text)= '' OR res.identificationnumber = CAST(?2 AS text)) "
				+ "AND (CAST(?3 AS text)= '' OR res.name LIKE CAST(?4 AS text)) "
				+ "AND (CAST(?5 AS text) = '' OR (to_char(seq.code, '0000') || '-' || EXTRACT (YEAR FROM wor.date)) LIKE CAST(?6 AS text))";

		Query query = entityManager.createNativeQuery(stringQuery);
		query.setParameter(1, identification_number_criteria == null ? ""
				: identification_number_criteria);
		query.setParameter(2, identification_number_criteria == null ? ""
				: identification_number_criteria);
		query.setParameter(3, name_resident_criteria == null ? "" : "%"
				+ name_resident_criteria + "%");
		query.setParameter(4, name_resident_criteria == null ? "" : "%"
				+ name_resident_criteria + "%");
		query.setParameter(5, number_request_criteria == null ? "" : "%"
				+ number_request_criteria + "%");
		query.setParameter(6, number_request_criteria == null ? "" : "%"
				+ number_request_criteria + "%");

		BigInteger size = (BigInteger) query.getSingleResult();

		return size.longValue();
	}

	@Override
	public WriteOffRequestDTO findById(Long writeOffRequestId) {
		Query query = this.entityManager
				.createNativeQuery("SELECT "
									+ "wor.id, "
									+ "wor.date, "
									+ "res.name AS resident_name, "
									+ "res.identificationnumber, "
									+ "was.ncalle as address, "
									+ "was.servicenumber, "
									+ "wme.serial, "
									+ "wrt.name AS _type, "
									+ "to_char(seq.code,'0000') number_code, "
									+ "EXTRACT(YEAR FROM wor.date) as _year, "
									+ "to_char(seq.code, '0000') || '-' || EXTRACT (YEAR FROM wor.date) AS request_number, "
									+ "wor.internalprocessnumber, "
									+ "wor.detail "
						+ "FROM "
						+ "writeoffrequest wor "
						+ "INNER JOIN resident res ON wor.resident_id = res.id "
						+ "INNER JOIN watermeter wme ON wor.watermeter_id = wme.id "
						+ "INNER JOIN watersupply was ON wme.watersupply_id = was.id "
						+ "INNER JOIN writeofftype wrt ON wor.writeofftype_id = wrt.id "
						+ "INNER JOIN sequencemanager seq ON wor.sequencemanager_id = seq.id "
						+ "WHERE wor.id = ?1");
		query.setParameter(1, writeOffRequestId);
		List<WriteOffRequestDTO> retorno_bd = NativeQueryResultsMapper.map(
				query.getResultList(), WriteOffRequestDTO.class);

		if(retorno_bd.size()>0){
			return retorno_bd.get(0);
		}
		return null;
	}

	@Override
	public List<ConsumptionPreviousDTO> findPreviousReading(
			Long watersupply_id, String _year, String _month) {
		Query query = this.entityManager
				.createNativeQuery("SELECT  con.year as anio, "
											+"to_char(to_date(con.year||'-'||con.month, 'YYYY-MM-DD'), 'TMMonth') as mes, "
											+"wme.serial as medidor, "
											+"wms.name as estado_consumo, "
											+"con.currentreading as lec_actual, "
											+"con.previousreading as lec_anterior, "
											+"con.amount as consumo, "
											+"mbs.name as est_pago, "
											+"mb.paidtotal as valor "
										+"FROM consumption con "
										+"LEFT JOIN watermeterstatus wms ON wms.id = con.watermeterstatus_id "
										+"LEFT JOIN watermeter wme ON wme.watersupply_id = con.watersupply_id "
										+"LEFT JOIN waterservicereference wsr ON con.id = wsr.consumption_id "
										+"LEFT JOIN municipalbond mb ON mb.adjunct_id = wsr.id "
										+"LEFT JOIN municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
										+"WHERE wme.id = ?1 "
										+"AND to_date(con.year||'-'||con.month, 'YYYY-MM-DD') < to_date(?2||'-'||?3, 'YYYY-MM-DD') "
										+"ORDER by con.year DESC, con.month DESC "
										+"LIMIT 4");
		query.setParameter(1, watersupply_id);
		query.setParameter(2, _year);
		query.setParameter(3, _month);
		
		List<ConsumptionPreviousDTO> retorno_bd = NativeQueryResultsMapper.map(
				query.getResultList(), ConsumptionPreviousDTO.class);

		return retorno_bd;
	}

}