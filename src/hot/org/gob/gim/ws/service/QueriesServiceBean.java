/**
 * 
 */
package org.gob.gim.ws.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.loja.gim.ws.dto.queries.EntryDTO;
import org.gob.loja.gim.ws.dto.queries.LocalDTO;
import org.gob.loja.gim.ws.dto.queries.OperatingPermitDTO;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;

import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author René
 *
 */
@Stateless(name = "QueriesService")
public class QueriesServiceBean implements QueriesService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	MunicipalBondService municipalBondService;

	@Override
	public BondDTO findBondById(Long bondId) {

		MunicipalBond municipalBond = this.municipalBondService
				.findById(bondId);
		BondDTO dto = new BondDTO(municipalBond);
		return dto;
	}

	@Override
	public List<OperatingPermitDTO> findOperatingPermits(String ruc) {
		String qry = "SELECT "
				+ "op.id, "
				+ "ad.parish AS parroquia, "
				+ "op.date_emission AS fecha_emision, "
				+ "op.date_validity AS validez_hasta, "
				+ "op.local_ruc AS ruc_local, "
				+ "bu.name AS nombre_local, "
				+ "op.paper_code AS codigo_formulario, "
				+ "op.local_code AS codigo_local, "
				+ "REPLACE(REPLACE(op.economic_activity,chr(10), ''),chr(13), '') AS actividad, "
				+ "ad.street AS direccion, "
				+ "ad.phonenumber AS telefono_movil, "
				+ "ad.mobilenumber AS telefono_fijo "
				+ "FROM operatinglicense op "
				+ "INNER JOIN local lo ON op.local_code = lo.code "
				+ "LEFT JOIN business bu ON lo.business_id = bu.id "
				+ "LEFT JOIN address ad ON lo.address_id = ad.id " + "WHERE "
				+ "op.nullified = FALSE " + "AND op.local_ruc = ?1 "
				+ "ORDER BY 2, 3, 6";

		Query query = entityManager.createNativeQuery(qry);

		query.setParameter(1, ruc);

		List<OperatingPermitDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), OperatingPermitDTO.class);

		return retorno;
	}

	@Override
	public List<LocalDTO> findLocals(String cedRuc) {
		String qry = "SELECT " + "lo.id, " + "lo.name, " + "lo.reference, "
				+ "ad.street, " + "lo.code, " + "but.name as businesstype, "
				+ "res.name as owner, " + "bu.cedruc " + "FROM local lo "
				+ "LEFT JOIN business bu ON lo.business_id = bu.id "
				+ "LEFT JOIN address ad ON lo.address_id = ad.id "
				+ "INNER JOIN businesstype but ON but.id = bu.businesstype_id "
				+ "INNER JOIN resident res ON res.id = bu.owner_id "
				+ "WHERE  " + "bu.cedruc = ?1 " + "AND lo.isactive = true";
		Query query = entityManager.createNativeQuery(qry);

		query.setParameter(1, cedRuc);

		List<LocalDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), LocalDTO.class);

		return retorno;
	}

	@Override
	public EntryDTO findEntry(String code) {
		String qry = "SELECT  ent.id, "
							+"ent.code, "
							+"ent.department, "
							+"ent.description, "
							+"ent.name, "
							+"ent.reason, "
							+"ent.completename, "
							+"acc.accountcode, "
							+"acc.accountname "
					+"FROM entry ent "
					+"INNER JOIN account acc ON acc.id = ent.account_id "
					+"WHERE ent.code = ?"; 
		Query query = entityManager.createNativeQuery(qry);

		query.setParameter(1, code);

		List<EntryDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), EntryDTO.class);
		
		if(retorno.size() > 0 ){
			return retorno.get(0);
		}

		return null;
	}

}
