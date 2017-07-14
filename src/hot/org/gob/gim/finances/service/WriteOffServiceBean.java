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
						+ "AND (CAST(?5 AS text) = '' OR (to_char(seq.code, '0000') || '-' || EXTRACT (YEAR FROM wor.date)) LIKE CAST(?6 AS text))");
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
									+ "to_char(seq.code, '0000') || '-' || EXTRACT (YEAR FROM wor.date) AS request_number "
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

}