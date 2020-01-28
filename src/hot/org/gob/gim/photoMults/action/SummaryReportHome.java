package org.gob.gim.photoMults.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.photoMults.dto.ReportPhotoMultsSummaryDTO;
import org.gob.gim.photoMults.dto.SummaryGroupDTO;
import org.gob.gim.photoMults.service.PhotoMultsService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;

@Name("summaryReportHome")
@Scope(ScopeType.CONVERSATION)
public class SummaryReportHome extends EntityController {

	@In
	FacesMessages facesMessages;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PhotoMultsService photoMultsService;

	/*
	 * CRITERIA
	 */
	private Date startDate;
	private Date endDate;

	List<ReportPhotoMultsSummaryDTO> results = new ArrayList<ReportPhotoMultsSummaryDTO>();
	List<SummaryGroupDTO> resultsGrouped = new ArrayList<SummaryGroupDTO>();

	public SummaryReportHome() {
		this.initializeService();
		this.startDate = new Date();
		this.endDate = new Date();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<ReportPhotoMultsSummaryDTO> getResults() {
		return results;
	}

	public void setResults(List<ReportPhotoMultsSummaryDTO> results) {
		this.results = results;
	}
	
	public List<SummaryGroupDTO> getResultsGrouped() {
		return resultsGrouped;
	}

	public void setResultsGrouped(List<SummaryGroupDTO> resultsGrouped) {
		this.resultsGrouped = resultsGrouped;
	}

	public void initializeService() {
		if (photoMultsService == null) {
			photoMultsService = ServiceLocator.getInstance().findResource(
					PhotoMultsService.LOCAL_NAME);
		}
	}

	public void buildReport() {
		this.results = new ArrayList<ReportPhotoMultsSummaryDTO>();
		this.resultsGrouped = new ArrayList<SummaryGroupDTO>();

		List<ReportPhotoMultsSummaryDTO> _result = this.photoMultsService
				.reportSummary(startDate, endDate);
		this.results = _result;
		this.resultsGrouped = this.buildListGrouped(results);
		
	}
	
	private List<SummaryGroupDTO> buildListGrouped(List<ReportPhotoMultsSummaryDTO> _list){
		List<SummaryGroupDTO> _resultGrouped = new ArrayList<SummaryGroupDTO>();

		Map<Long, List<ReportPhotoMultsSummaryDTO>> groupedByAnio = new HashMap<Long, List<ReportPhotoMultsSummaryDTO>>();
		for (ReportPhotoMultsSummaryDTO row : _list) {
			Long key = row.getYear();
			if (groupedByAnio.get(key) == null) {
				groupedByAnio.put(key,
						new ArrayList<ReportPhotoMultsSummaryDTO>());
			}
			groupedByAnio.get(key).add(row);
		}

		Set<Long> _groupedStudentsKeySet = groupedByAnio.keySet();
		TreeSet<Long> groupedStudentsKeySet = new TreeSet<Long>();
		groupedStudentsKeySet.addAll(_groupedStudentsKeySet);
		for (Long _year : groupedStudentsKeySet) {
			List<ReportPhotoMultsSummaryDTO> _chidrens = groupedByAnio.get(_year);
			_resultGrouped.add(new SummaryGroupDTO(_year, _chidrens));
			
		}
		
		return _resultGrouped;
	}

}