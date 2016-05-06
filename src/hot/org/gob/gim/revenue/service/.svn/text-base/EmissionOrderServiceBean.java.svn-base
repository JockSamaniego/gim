package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.security.model.User;

/**
 * 
 * @author wilman
 */
@Stateless(name = "EmissionOrderService")
public class EmissionOrderServiceBean implements EmissionOrderService {

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
	public EmissionOrder createEmissionOrder(MunicipalBond municipalBond, User u) throws Exception {
		EmissionOrder e = new EmissionOrder();
		e.add(municipalBond);
		e.setDescription(municipalBond.getDescription());
		e.setServiceDate(municipalBond.getServiceDate());		
		e.setEmisor((Person)u.getResident());
		return save(e);		
	}

	@Override
	public EmissionOrder createEmissionOrder(List<MunicipalBond> municipalBonds) throws EntryDefinitionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmissionOrder createEmissionOrder(EmissionOrder e) throws Exception {
		MunicipalBondStatus mbs = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PREEMIT");
		for(MunicipalBond m : e.getMunicipalBonds()){
			m.setMunicipalBondStatus(mbs);
		}
		if(municipalBondService.findByResidentIdAndEntryIdAndServiceDateAndGroupingCode(e.getMunicipalBonds().get(0).getResident().getId(), 
				e.getMunicipalBonds().get(0).getEntry().getId(), e.getMunicipalBonds().get(0).getServiceDate(),e.getMunicipalBonds().get(0).getGroupingCode()) != null) return null;
		save(e);
		return null;
	}


}
