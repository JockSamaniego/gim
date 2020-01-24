package org.gob.gim.photoMults.dto;

import java.util.ArrayList;
import java.util.List;

public class SummaryGroupDTO {
	private Long year;
	private List<ReportPhotoMultsSummaryDTO> childrens = new ArrayList<ReportPhotoMultsSummaryDTO>();
	
	public SummaryGroupDTO(Long year, List<ReportPhotoMultsSummaryDTO> childrens) {
		super();
		this.year = year;
		this.childrens = childrens;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public List<ReportPhotoMultsSummaryDTO> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<ReportPhotoMultsSummaryDTO> childrens) {
		this.childrens = childrens;
	}

}
