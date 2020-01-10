package org.gob.gim.photoMults.action;

import java.util.ArrayList;
import java.util.List;

import org.gob.gim.photoMults.dto.CriteriaReport;
import org.gob.gim.photoMults.dto.ReportPhotoMultsType;
import org.gob.gim.photoMults.pagination.PhotoMultsDataModel;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;

@Name("reportsHome")
@Scope(ScopeType.CONVERSATION)
public class ReportsHome extends EntityController {

	@In
	FacesMessages facesMessages;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CriteriaReport criteria = new CriteriaReport();

	public ReportsHome() {
	}

	public CriteriaReport getCriteria() {
		return criteria;
	}

	public void setCriteria(CriteriaReport criteria) {
		this.criteria = criteria;
	}

	@Factory("reportPhotoMultsTypes")
	public List<ReportPhotoMultsType> loadReportTypes() {
		List<ReportPhotoMultsType> listValues = new ArrayList<ReportPhotoMultsType>();

		for (ReportPhotoMultsType reportType : ReportPhotoMultsType.values()) {

			listValues.add(reportType);
		}
		return listValues;
	}

	public void buildReport() {
		
		getDataModel().setCriteria(this.criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
		
		System.out.println(this.criteria);
	}
	
	private PhotoMultsDataModel getDataModel() {

		PhotoMultsDataModel dataModel = (PhotoMultsDataModel) Component
				.getInstance(PhotoMultsDataModel.class, true);
		return dataModel;
	}

}