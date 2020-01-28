package org.gob.gim.photoMults.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.gob.gim.photoMults.dto.CriteriaReport;
import org.gob.gim.photoMults.dto.ReportPhotoMultsDTO;
import org.gob.gim.photoMults.dto.ReportPhotoMultsSummaryDTO;

/**
 *  * @author René Ortega
 *  * @Fecha 2020-01-09
 */

@Local
public interface PhotoMultsService {

	public String LOCAL_NAME = "/gim/PhotoMultsService/local";

	public List<ReportPhotoMultsDTO> findPhotoMults(CriteriaReport criteria, Integer firstRow,
			Integer numberOfRows);
	
	public Long findPhotoMultsNumber(CriteriaReport criteria);
	
	public List<ReportPhotoMultsSummaryDTO> reportSummary(Date startDate, Date endDate);
	
	
}
