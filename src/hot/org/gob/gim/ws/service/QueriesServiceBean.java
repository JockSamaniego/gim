/**
 * 
 */
package org.gob.gim.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.loja.gim.ws.dto.BondDetail;
import org.gob.loja.gim.ws.dto.BondWS;
import org.gob.loja.gim.ws.dto.Taxpayer;
import org.gob.loja.gim.ws.dto.queries.DebtsDTO;
import org.gob.loja.gim.ws.dto.queries.EntryDTO;
import org.gob.loja.gim.ws.dto.queries.LocalDTO;
import org.gob.loja.gim.ws.dto.queries.OperatingPermitDTO;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;

import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondType;

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

	@EJB
	SystemParameterService systemParameterService;

	@EJB
	private IncomeService incomeService;

	public final String ACCOUNT_CODE_FOR_INTEREST = "ACCOUNT_CODE_FOR_INTEREST";

	public final String ACCOUNT_CODE_FOR_SURCHARGE = "ACCOUNT_CODE_FOR_SURCHARGE";

	public final String ACCOUNT_CODE_FOR_TAX = "ACCOUNT_CODE_FOR_TAX";

	public final String ACCOUNT_CODE_FOR_DISCOUNT = "ACCOUNT_CODE_FOR_DISCOUNT";

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
		String qry = "SELECT  ent.id, " + "ent.code, " + "ent.department, "
				+ "ent.description, " + "ent.name, " + "ent.reason, "
				+ "ent.completename, " + "acc.accountcode, "
				+ "acc.accountname " + "FROM entry ent "
				+ "INNER JOIN account acc ON acc.id = ent.account_id "
				+ "WHERE ent.code = ?";
		Query query = entityManager.createNativeQuery(qry);

		query.setParameter(1, code);

		List<EntryDTO> retorno = NativeQueryResultsMapper.map(
				query.getResultList(), EntryDTO.class);

		if (retorno.size() > 0) {
			return retorno.get(0);
		}

		return null;
	}

	@Override
	public DebtsDTO findDebts(String identification) {
		Taxpayer taxpayer = findTaxpayer(identification);
		DebtsDTO result = new DebtsDTO();
		result.setTaxpayer(taxpayer);

		List<Long> statusPermit = systemParameterService
				.findListIds("STATUS_IDS_REST_DEBTS");

		List<Long> bondIds = pendingBonds(taxpayer.getId(), statusPermit);
		List<BondWS> bonds = new ArrayList<BondWS>();
		if (bondIds.size() > 0) {
			try {
				incomeService.calculatePayment(new Date(), bondIds, true, true);
				bonds = findBonds(bondIds);
				loadBondsDetail(bonds);
				result.setBonds(bonds);
			} catch (EntryDefinitionNotFoundException e) {
				e.printStackTrace();

				// statement.setMessage("Pago no permitido, error en calculo");
				// statement.setCode("ML.FS.7005");
				// persisBankingEntityLog(false, statement.toString());
				return null;
			}
		}
		return result;
	}

	private Taxpayer findTaxpayer(String identificationNumber) {
		Query query = entityManager
				.createNamedQuery("Taxpayer.findResidentFullByIdentification");
		query.setParameter("identificationNumber", identificationNumber);
		try {
			Taxpayer taxpayer = (Taxpayer) query.getSingleResult();
			if (taxpayer == null || taxpayer.getId() == null) {
				return null;
			}
			return taxpayer;
		} catch (Exception e) {
			return null;
		}
	}

	private List<Long> pendingBonds(Long taxpayerId, List<Long> statuses) {
		Long pendingBondStatusId = systemParameterService
				.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = entityManager
				.createNamedQuery("Bond.findIdsByStatusesAndResidentId");
		query.setParameter("residentId", taxpayerId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("statusIds", pendingBondStatusId);
		List<Long> ids = query.getResultList();
		return ids;
	}

	private List<BondWS> findBonds(List<Long> ids) {
		Query query = entityManager.createNamedQuery("Bond.findByListIds");
		query.setParameter("ids", ids);
		// query.setParameter("pendingBondStatusId", pendingBondStatusId);
		List<BondWS> bonds = query.getResultList();
		return bonds;

	}

	private void loadBondsDetail(List<BondWS> bonds) {
		String accountCodeDiscount = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_DISCOUNT);
		String accountCodeInterest = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_INTEREST);
		String accountCodeSurcharge = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_SURCHARGE);
		String accountCodeTax = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_TAX);
		List<Long> bondIds = new ArrayList<Long>();
		for (BondWS bond : bonds) {
			bondIds.add(bond.getId());
		}
		Query query = entityManager
				.createNamedQuery("BondDetail.findBondDetailByIds");
		query.setParameter("bondIds", bondIds);
		List<BondDetail> bondDetails = new ArrayList<BondDetail>();
		bondDetails = query.getResultList();
		for (BondWS bond : bonds) {
			List<BondDetail> bdAux = new ArrayList<BondDetail>();
			for (BondDetail bd : bondDetails) {
				if (bd.getBondId().equals(bond.getId())) {
					bdAux.add(bd);
				}
			}
			bond.setBondsDetail(bdAux);
			BondDetail bondDetail = new BondDetail(bond.getId(),
					accountCodeInterest, "intereses", bond.getInterests());
			bond.getBondsDetail().add(bondDetail);
			bondDetail = new BondDetail(bond.getId(), accountCodeSurcharge,
					"recargos", bond.getSurcharges());
			bond.getBondsDetail().add(bondDetail);
			bondDetail = new BondDetail(bond.getId(), accountCodeTax,
					"impuestos", bond.getTaxes());
			bond.getBondsDetail().add(bondDetail);
			bondDetail = new BondDetail(bond.getId(), accountCodeDiscount,
					"descuentos", bond.getDiscounts().multiply(
							BigDecimal.valueOf(-1)));
			bond.getBondsDetail().add(bondDetail);
		}
	}

}
