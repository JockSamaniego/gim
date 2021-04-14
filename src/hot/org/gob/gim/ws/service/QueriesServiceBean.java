/**
 * 
 */
package org.gob.gim.ws.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.gob.gim.revenue.service.MunicipalBondService;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;

import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author René
 *
 */
@Stateless(name = "QueriesService")
public class QueriesServiceBean implements QueriesService {

	@EJB
	MunicipalBondService municipalBondService;

	@Override
	public BondDTO findBondById(Long bondId) {

		MunicipalBond municipalBond = this.municipalBondService
				.findById(bondId);
		BondDTO dto = new BondDTO(municipalBond);
		return dto;
	}

}
