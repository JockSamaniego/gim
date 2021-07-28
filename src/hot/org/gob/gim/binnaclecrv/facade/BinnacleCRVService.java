package org.gob.gim.binnaclecrv.facade;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.binnaclecrv.model.AdmissionCategory;
import ec.gob.gim.binnaclecrv.model.AdmissionType;
import ec.gob.gim.binnaclecrv.model.VehicleInventoryBase;


/**
 * @author Ronald Paladines C
 *
 */
@Local
public interface BinnacleCRVService {

	public String LOCAL_NAME = "/gim/BinnacleCRVService/local";
	
	List<VehicleInventoryBase> findVehicleInventoryBase() throws Exception;
	List<AdmissionCategory> findAdmissionCategories(AdmissionType admissionType);
	
}
