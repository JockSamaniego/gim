package org.gob.gim.binnaclecrv.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.binnaclecrv.model.AdmissionCategory;
import ec.gob.gim.binnaclecrv.model.AdmissionType;
import ec.gob.gim.binnaclecrv.model.VehicleInventoryBase;

@Stateless(name = "BinnacleCRVService")
public class BinnacleCRVServiceBean implements BinnacleCRVService{
	
	@EJB
	CrudService crudService;
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<VehicleInventoryBase> findVehicleInventoryBase() throws Exception {
		List<VehicleInventoryBase> findByNativeQuery = this.crudService.findWithNamedQuery("VehicleInventoryBase.findAllActive");
		List<VehicleInventoryBase> result = findByNativeQuery;
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdmissionCategory> findAdmissionCategories(AdmissionType admissionType) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("admissionType", admissionType);
		List<AdmissionCategory> findByNativeQuery = this.crudService.
				findWithNamedQuery("AdmissionCategory.findAllByAdmissionType", parameters);
		List<AdmissionCategory> result = findByNativeQuery;
		return result;
	}

}
