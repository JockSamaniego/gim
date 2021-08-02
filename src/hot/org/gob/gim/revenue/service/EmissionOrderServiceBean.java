package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.DTO.EmissionOrderDTO;
import ec.gob.gim.revenue.model.criteria.EmissionOrderSearchCriteria;
import ec.gob.gim.security.model.User;

/**
 * 
 * @author wilman
 */
@Stateless(name = "EmissionOrderService")
public class EmissionOrderServiceBean implements EmissionOrderService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@EJB
	MunicipalBondService municipalBondService;

	@EJB
	SystemParameterService systemParameterService;

	@Override
	public EmissionOrder save(EmissionOrder emissionOrder) throws Exception {
		return crudService.create(emissionOrder);
	}

	@Override
	public EmissionOrder update(EmissionOrder emissionOrder) throws Exception {
		return crudService.update(emissionOrder);
	}

	@Override
	public EmissionOrder createEmissionOrder(MunicipalBond municipalBond, User u)
			throws Exception {
		EmissionOrder e = new EmissionOrder();
		e.add(municipalBond);
		e.setDescription(municipalBond.getDescription());
		e.setServiceDate(municipalBond.getServiceDate());
		e.setEmisor((Person) u.getResident());
		return save(e);
	}

	@Override
	public EmissionOrder createEmissionOrder(List<MunicipalBond> municipalBonds)
			throws EntryDefinitionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmissionOrder createEmissionOrder(EmissionOrder e) throws Exception {
		MunicipalBondStatus mbs = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		for (MunicipalBond m : e.getMunicipalBonds()) {
			m.setMunicipalBondStatus(mbs);
		}
		if (municipalBondService
				.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(e
						.getMunicipalBonds().get(0).getResident().getId(), e
						.getMunicipalBonds().get(0).getEntry().getId(), e
						.getMunicipalBonds().get(0).getServiceDate(), e
						.getMunicipalBonds().get(0).getGroupingCode()) != null)
			return null;
		save(e);
		return null;
	}

	@Override
	public List<EmissionOrderDTO> searchOrders(
			EmissionOrderSearchCriteria criteria) {
		String qry = "SELECT eor.id as numero, "
							+ "emi.name as emisor, "
							+ "eor.servicedate, "
							+ "res.name contribuyente, "
							+ "eor.description descripcion, "
							+ "count(distinct mb.id) as numero_bonds, "
							+ "string_agg(CAST(mb.id AS text), ',') as bonds "
				+ "FROM emissionorder eor "
				+ "INNER JOIN municipalbond mb ON mb.emissionorder_id = eor.id "
				+ "INNER JOIN entry ent ON mb.entry_id = ent.id "
				+ "INNER JOIN resident emi ON emi.id = mb.emitter_id "
				+ "INNER JOIN resident res ON res.id = mb.resident_id "
				+ "WHERE eor.isdispatched = FALSE "
				+ "AND mb.entry_id NOT IN (643, 644) "
				+ "AND (CAST(?1 AS text) = '' OR LOWER(res.name) LIKE ?1) "
				+ "AND (CAST(?2 AS text) = '' OR LOWER(res.identificationnumber) LIKE ?2) "
				+ "AND (CAST(?3 AS text) = '' OR ent.department = ?3) "
				+ "AND (CAST(?4 AS text) = '' OR LOWER(ent.code) LIKE ?4) "
				+ "GROUP BY eor.id, emi.name,eor.servicedate,res.name,eor.description";

		Query query = entityManager.createNativeQuery(qry);

		query.setParameter(1, criteria.getResident() == null ? "" : "%"+criteria.getResident().toLowerCase()+"%");
		query.setParameter(2, criteria.getIdentificationNumber() == null ? "" : "%"+criteria.getIdentificationNumber().toLowerCase()+"%");
		query.setParameter(3, criteria.getDepartment() == null ? "" : criteria.getDepartment());
		query.setParameter(4, criteria.getEntry() == null ? "" : "%"+criteria.getEntry().toLowerCase()+"%");

		List<EmissionOrderDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), EmissionOrderDTO.class);

		return retorno;
	}

	@Override
	public EmissionOrder findById(Long id) {
		Query query = entityManager.createNamedQuery("EmissionOrder.findById");
		query.setParameter("id", id);
		return (EmissionOrder) query.getSingleResult();
	}

}
