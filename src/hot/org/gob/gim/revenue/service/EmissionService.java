/**
 * 
 */
package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.revenue.model.DTO.ReportEmissionDTO;
import ec.gob.gim.revenue.model.criteria.ReportEmissionCriteria;

/**
 * @author Rene Ortega
 * @Fecha 2017-02-02
 */
@Local
public interface EmissionService {
	public String LOCAL_NAME = "/gim/EmissionService/local";
	
	public List<ReportEmissionDTO> findEmissionReport(ReportEmissionCriteria criteria);
	
	public List<ReportEmissionDTO> findEmissionReportOtherDetails(ReportEmissionCriteria criteria);
	
}
