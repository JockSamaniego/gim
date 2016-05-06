package org.gob.gim.appraisal.facade;

import java.util.List;

import javax.ejb.Local;


import ec.gob.gim.appraisal.model.AppraisalPeriod;
/**
 * @author GADM-Loja
 * 
 **/
import ec.gob.gim.cadaster.model.Property;

@Local
public interface AppraisalService {
	
	public List<Property> calculateUrbanAppraisal(AppraisalPeriod appraisalPeriod, int anioAppraisal, List<Property> properties, boolean temporalValues);
	public void saveValueBySquareMeter(List<Property> properties, boolean temporalValues) throws Exception;
	public void saveAppraisals(List<Property> properties, boolean temporalValues) throws Exception;

}
