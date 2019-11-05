package org.gob.gim.commercial.action;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.commercial.facade.TouristLicenseService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.commercial.model.BusinessCatalog;
import ec.gob.gim.commercial.model.Local;
import ec.gob.gim.commercial.model.LocalFeature;
import ec.gob.gim.commercial.model.TouristLicenseEmission;
import ec.gob.gim.commercial.model.TouristLicenseItem;
import ec.gob.gim.revenue.model.EmissionOrder;

@Name("localFeatureHome")
public class LocalFeatureHome extends EntityHome<LocalFeature> {

	@In(create = true)
	TouristLicenseItemHome touristLicenseItemHome;

	private TouristLicenseItem touristLicenseItem;

	private Integer year = -1;
	private List<TouristLicenseItem> items;

	private BusinessCatalog businessCatalog;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	@In
	FacesMessages facesMessages;

	private TouristLicenseService touristLicenseService;
	//private SystemParameterService systemParameterService;
	public static String TOURISTLICENSE_SERVICE_NAME = "/gim/TouristLicenseService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private EmissionOrder eo;
	private String emissionReference;

	public void setLocalFeatureId(Long id) {
		setId(id);
	}

	public Long getLocalFeatureId() {
		return (Long) getId();
	}

	@Override
	protected LocalFeature createInstance() {
		LocalFeature localFeature = new LocalFeature();
		return localFeature;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public LocalFeature getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public BusinessCatalog getBusinessCatalog() {
		return businessCatalog;
	}

	public void setBusinessCatalog(BusinessCatalog businessCatalog) {
		this.businessCatalog = businessCatalog;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void generateRecords() {
		if (businessCatalog != null && year > 2000) {
			// buscar los items ya generados
			List<Long> localAlreadyGenerated = findItemsAlreadyGenerated();
			//System.out.println("buscar los items ya generados "					+ localAlreadyGenerated.size());
			// buscar los locales q aun no han sido generados
			List<Local> missingLocals;
			if (localAlreadyGenerated.size() > 0) {
				missingLocals = findMissingLocals(localAlreadyGenerated);
			} else {
				missingLocals = findLocalsByCatalog();
			}
			if (missingLocals.size() > 0) {
				insertNewRecords(missingLocals);
				System.out
						.println("buscar los locales q aun no han sido generados "
								+ missingLocals.size());
			} else {
				System.out.println("no hay locales pendientes ");
			}
			finItems();
		}
	}

	public void insertNewRecords(List<Local> missingLocals) {
		TouristLicenseItem tli;
		for (Local l : missingLocals) {
			tli = new TouristLicenseItem();
			tli.setBusinessCatalog(businessCatalog);
			tli.setLocal(l);
			tli.setOriginator(userSession.getPerson());
			tli.setRegisterdate(new Date());
			tli.setRegisterTime(new Date());
			tli.setYear(year);
			this.touristLicenseItemHome.setInstance(tli);
			this.touristLicenseItemHome.persist();
		}
	}

	public void finItems() {
		Query q = this.getEntityManager().createNamedQuery(
				"TouristLicenseItem.findByCatalogAndYear");
		q.setParameter("bcId", this.businessCatalog.getId());
		q.setParameter("year", this.year);
		items = q.getResultList();
	}

	public List<Local> findLocalsByCatalog() {
		Query q = this.getEntityManager().createNamedQuery(
				"Local.findLocalsByCatalog");
		q.setParameter("bcId", this.businessCatalog.getId());
		return q.getResultList();
	}

	public List<Local> findMissingLocals(List<Long> localAlreadyGenerated) {
		Query q = this.getEntityManager().createNamedQuery(
				"Local.findMissingLocals");
		q.setParameter("bcId", this.businessCatalog.getId());
		q.setParameter("localsAlreadyGenerated", localAlreadyGenerated);
		return q.getResultList();
	}

	public List<Long> findItemsAlreadyGenerated() {
		Query q = this.getEntityManager().createNamedQuery(
				"TouristLicenseItem.findLocalIdsByCatalogAndYear");
		q.setParameter("bcId", this.businessCatalog.getId());
		q.setParameter("year", this.year);
		return q.getResultList();
	}

	public List<TouristLicenseItem> getItems() {
		return items;
	}

	public void setItems(List<TouristLicenseItem> items) {
		this.items = items;
	}

	public TouristLicenseItem getTouristLicenseItem() {
		return touristLicenseItem;
	}

	public void setTouristLicenseItem(TouristLicenseItem touristLicenseItem) {
		this.touristLicenseItem = touristLicenseItem;
	}

	public void selectInfoItem(TouristLicenseItem tli) {
		this.touristLicenseItem = tli;
	}

	public void updateTourisItem(TouristLicenseItem tli) {
		this.touristLicenseItemHome.setInstance(tli);
		this.touristLicenseItemHome.update();

	}

	public String startEmissionTourismLicense() {
		if (touristLicenseService == null)
			touristLicenseService = ServiceLocator.getInstance()
					.findResource(TOURISTLICENSE_SERVICE_NAME);
		try {
			if (checkFields()) {
				eo = touristLicenseService.generateEmissionOrder(
						this.businessCatalog, items,
						userSession.getFiscalPeriod(), userSession.getPerson(),
						year, this.emissionReference);
								
				if (eo != null) {
					TouristLicenseEmission tle =new TouristLicenseEmission();
					tle.setBusinessCatalog(businessCatalog);
					tle.setDate(new Date());
					tle.setExplanation(this.emissionReference);
					tle.setHasPreEmit(true);
					tle.setYear(year);
					
					//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> >>>>>>>>ZZZZZZ entra a guardar");
					touristLicenseService.saveEmissionOrder(eo, tle, true);
					String message = Interpolator.instance().interpolate(
							"Licencia de turismo: "
									+ this.businessCatalog.getName()
									+ " han sido emitidas, ", new Object[0]);
					facesMessages
							.addToControl(
									"residentChooser",
									org.jboss.seam.international.StatusMessage.Severity.INFO,
									message);
					return "completed";
				}
			} else {
				String message = Interpolator.instance().interpolate(
						"Revise los campos antes de emitir", new Object[0]);
				facesMessages.addToControl("residentChooser",
						org.jboss.seam.international.StatusMessage.Severity.ERROR,
						message);
				//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+message);
				return null;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			String message = Interpolator.instance().interpolate(
					"#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return null;
		}
	}

	public boolean checkFields() {
		if (this.emissionReference != null) {
			return true;
		} else {
			return false;
		}
	}

	public String getEmissionReference() {
		return emissionReference;
	}

	public void setEmissionReference(String emissionReference) {
		this.emissionReference = emissionReference;
	}
	
	//Autor: Jock Samaniego M.
	//Para desactivar local turistico desde la tabla de permiso anual de turismo
		public void disableLocalTourist(TouristLicenseItem itemTourist){
			touristLicenseItem = itemTourist;
			touristLicenseItem.getLocal().setIsActive(false);	 
			persist();
			generateRecords();
		}
		
//Autor: Jock Samaniego M.
//Para obtener lista de locales desactivados desde el TourismLicenseEmission.
		public List<TouristLicenseItem> searchLocalTouristDisabled() {
			List<TouristLicenseItem> itemsDisabled;
			Query q = this.getEntityManager().createNamedQuery(
					"TouristLicenseItem.findByCatalogAndYearDisabled");
			q.setParameter("bcId", this.businessCatalog.getId());
			q.setParameter("year", this.year);
			itemsDisabled = q.getResultList();
			return itemsDisabled;
		}
		
//Autor: Jock Samaniego M.
//Para reactivar el local desactivado desde el TourismLicenseEmission.
		public void enableLocalTourist(TouristLicenseItem itemTourist){
			touristLicenseItem = itemTourist;
			touristLicenseItem.getLocal().setIsActive(true);	 
			persist();
			generateRecords();
		}
		
	private boolean tableLocalTourist=Boolean.FALSE;

	public boolean isTableLocalTourist() {
		return tableLocalTourist;
	}

	public void setTableLocalTourist(boolean tableLocalTourist) {
		this.tableLocalTourist = tableLocalTourist;
	}
	
	public void viewTableTourist(){
		tableLocalTourist=Boolean.TRUE;
	}
	public void viewTableLocals(){
		tableLocalTourist=Boolean.FALSE;
	}
}