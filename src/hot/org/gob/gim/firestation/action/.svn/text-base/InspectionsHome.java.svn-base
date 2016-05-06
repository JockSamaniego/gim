package org.gob.gim.firestation.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.commercial.model.Local;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.firestation.model.InspectionObservation;
import ec.gob.gim.firestation.model.Inspections;
import ec.gob.gim.firestation.model.InspectionsState;
import ec.gob.gim.firestation.model.StateFireInstallation;
import ec.gob.gim.firestation.model.StateGasInstallation;
import ec.gob.gim.firestation.model.StateSystemEvacuationEscape;
import ec.gob.gim.firestation.model.StateSystemVentilationContamination;
import ec.gob.gim.firestation.model.TechnicalInformation;
import ec.gob.gim.firestation.model.TransportTechnicalInformation;
import ec.gob.gim.firestation.model.TypeInformation;
import ec.gob.gim.firestation.model.TypeInspections;


@Name("inspectionsHome")
public class InspectionsHome extends EntityHome<Inspections> {
		
	private static final long serialVersionUID = 1L;
	
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	@Logger 
	Log logger;
	
	@In(create = true)
	TechnicalInformationHome technicalInformationHome;
	
	@In(create = true)
	TransportTechnicalInformationHome transportTechnicalInformationHome;

	
	private String criteria;
	
	private List<Resident> residents;
	private String ownerIdentificationNumber;
	private String inspectorIdentificationNumber;
	private String fireManPhoneNumber;
	private Resident resident;
	private Local local;
	private Date inspectionsDate;
	//private String parish;
	//private String codeTechnicalInformation;
	private InspectionsState inspectionsState;
	private TypeInformation typeInformation;
	private TypeInspections typeInspections;
	private TechnicalInformation technicalInformation=new TechnicalInformation();
	private TransportTechnicalInformation transportTechnicalInformation=new TransportTechnicalInformation();
	private StateFireInstallation stateFireInstallation;
	private StateGasInstallation stateGasInstallation;
	private StateSystemEvacuationEscape stateSystemEvacuationEscape;
	private StateSystemVentilationContamination stateSystemVentilationContamination;
	//private TerritorialDivision parish;
	private List<Integer> emissionYearList;	
	private Date maximumServiceDate;
	private Date minimumServiceDate;	
		
	public void setInspectionsId(Long id) {
		setId(id);
	}

	public Long getInspectionsId() {
		return (Long) getId();
	}

	@Override
	protected Inspections createInstance() {
		Inspections inspections = new Inspections();
		return inspections;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
				
		if(!isFirstTime) return;
		if(getInstance() != null && getInstance().getInspector() != null) setInspectorIdentificationNumber(getInstance().getInspector().getIdentificationNumber());
		if(getInstance() != null && getInstance().getLocal() != null) setOwnerIdentificationNumber(getInstance().getLocal().getBusiness().getOwner().getIdentificationNumber());
		if(getInstance() != null && getInstance().getLocal() != null) setResident(getInstance().getLocal().getBusiness().getOwner());
		//if(getInstance() != null && getInstance().getLocal() != null) setResident(getInstance().getLocal().getBusiness().getOwner());
	//	if(getInstance() != null && getInstance().getLocal() != null) setLocal(getInstance().getLocal());
		
		/*Resident inspector = residentHome.getDefinedInstance();
		if (inspector != null) {
			getInstance().setInspector(inspector);
		}
		Local local = localHome.getDefinedInstance();
		if (local != null) {
			getInstance().setLocal(local);
		}*/
	}

	public boolean isWired() {
		return true;
	}

	public Inspections getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	private boolean isFirstTime = true;
	/*public void wire() {
		getInstance();
		
		if(!isFirstTime) return;
		if(getInstance() != null && getInstance().getInspector() != null) setInspectorIdentificationNumber(getInstance().getInspector().getIdentificationNumber());
		if(getInstance() != null && getInstance().getLocal() != null) setOwnerIdentificationNumber(getInstance().getLocal().getBusiness().getOwner().getIdentificationNumber());
		if(getInstance() != null && getInstance().getLocal() != null) setResident(getInstance().getLocal().getBusiness().getOwner());
		//if(getInstance() != null && getInstance().getLocal() != null) setResident(getInstance().getLocal().getBusiness().getOwner());
	//	if(getInstance() != null && getInstance().getLocal() != null) setLocal(getInstance().getLocal());
		
		//System.out.println("Instancia: "+getInstance().getLocal().getBusiness().getOwner());
	
		//System.out.println("Instancia: "+getInstance().getInspector());
		//System.out.println("Instancia: "+getInstance().getResident());
		//	System.out.println("Instancia: "+getInstance().local.getResident());
		//System.out.println("Instancia: "+getInstance().getLocal.getOwner());
	  	//System.out.println("llega a qui y produce error");
	  	//if(!isFirstTime) return;			
		//if(getInstance() != null && getInstance().getLocal() != null) setLocal(getInstance().getLocal());
	  	
		//if(getInstance() != null && getInstance().getInspector() != null) setInspectorIdentificationNumber(getInstance().getInspector().getIdentificationNumber());
		//if(getInstance() != null && getInstance().getLocal().getBusiness().getOwner() != null) setOwnerIdentificationNumber(getInstance().getLocal().getBusiness().getOwner().getIdentificationNumber());
		//if(getInstance() != null && getInstance().getLocal() != null) setLocal(getInstance().getLocal());
//		if(getInstance() != null && getInstance().getLocal() != null) setLocal(getInstance().getLocal());
		//if(getInstance() != null );
	//	getInstance();
		isFirstTime = false;
				
	}*/
	
	public void loadTechnicalInformation(){
		System.out.println("ddddddddddddddddddddd");
		if (this.getInspectionsId() != null) {
			System.out.println("ddddddddddddddddddddd "+this.getInspectionsId());
			Query q=this.getEntityManager().createNamedQuery("TechnicalInformation.findByInspection");
			q.setParameter("idInspection", getInspectionsId());
			if(q.getResultList().size()>0){
				this.technicalInformation=(TechnicalInformation) q.getResultList().get(0);
			}
		}else{
			System.out.println("///////////////////////////");
		}
	}

	public void loadTransportTechnicalInformation(){
		System.out.println("cccccccccccccccccccccccccccccccccccc");
		if (this.getInspectionsId() != null) {
			System.out.println("ccccccccccccccccccccccccccccc "+this.getInspectionsId());
			Query q=this.getEntityManager().createNamedQuery("TransportTechnicalInformation.findByInspection");
			q.setParameter("idInspection", getInspectionsId());
			if(q.getResultList().size()>0){
				this.transportTechnicalInformation=(TransportTechnicalInformation) q.getResultList().get(0);
			}
		}else{
			System.out.println("///////////////////////////");
		}
	}


	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	
	public void searchResident() {
		logger.info("looking for............holaaaaaa {0}", getOwnerIdentificationNumber());
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", getOwnerIdentificationNumber());
		try{
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			logger.info("RESIDENT CHOOSER ACTION "+resident.getId());
			
			//resident.add(this.getInstance());			
			this.setResident(resident);
			
			if (resident.getId() == null){
				addFacesMessageFromResourceBundle("resident.notFound");
			}else{
				//setOwnerIdentificationNumber(this.getInstance().getOwner().getIdentificationNumber());
			}
		}
		catch(Exception e){
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	public void searchInspector() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", getInspectorIdentificationNumber());
		try{
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			
			//resident.add(this.getInstance());			
			this.getInstance().setInspector((Person) resident);
			
			if (resident.getId() == null){
				addFacesMessageFromResourceBundle("resident.notFound");
			}else{
				setInspectorIdentificationNumber(this.getInstance().getInspector().getIdentificationNumber());
			}
		}
		catch(Exception e){
			this.getInstance().setInspector(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria(){
		logger.info("SEARCH RESIDENT BY CRITERIA "+this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public String getOwnerIdentificationNumber() {
		return ownerIdentificationNumber;
	}

	public void setOwnerIdentificationNumber(String ownerIdentificationNumber) {
		this.ownerIdentificationNumber = ownerIdentificationNumber;
	}

	public String getFireManPhoneNumber() {
		return fireManPhoneNumber;
	}

	public void setFireManPhoneNumber(String fireManPhoneNumber) {
		this.fireManPhoneNumber = fireManPhoneNumber;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	@SuppressWarnings("unchecked")
	public List<Local> findLocalesByResidentId(){
		System.out.println("lleaga alear localesssssssss acctionsssssssss");
		if(resident != null){
			Query query = getEntityManager().createNamedQuery("Local.findByOwnerId");
			query.setParameter("ownerId", resident.getId());
			return query.getResultList();
		}
		return new ArrayList<Local>();
	}
	
	public void clearSearchPanel(){
		this.setCriteria(null);
		residents = null;
	}
	
	public void ownerSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		if(resident != null){
			setOwnerIdentificationNumber(resident.getIdentificationNumber());
		}
	}
	
	public void inspectorSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		//this.setResident(resident);
		this.getInstance().setInspector((Person)resident);
		if(resident != null){
			setInspectorIdentificationNumber(resident.getIdentificationNumber());
		}
	}

	
	public Date getInspectionsDate() {
		return inspectionsDate;
	}
	public void setInspectionsDate(Date inspectionsDate) {
		this.inspectionsDate = inspectionsDate;
	} 

	@Factory("inspectionsStates")
	public List<InspectionsState> loadInspectionsStates() {
		return Arrays.asList(InspectionsState.values());
	}
	
	public InspectionsState getInspectionsState() {
		return inspectionsState;
	}

	public void setInspectionsState(InspectionsState inspectionsState) {
		this.inspectionsState = inspectionsState;
	}
	
	@Factory("typesInformation")
	public List<TypeInformation> loadTypesInformation() {
		return Arrays.asList(TypeInformation.values());
	}
	
	public TypeInformation getTypeInformation() {
		return typeInformation;
	}

	public void setTypeInformation(TypeInformation typeInformation) {
		this.typeInformation = typeInformation;
	}

	@Factory("statesFireInstallation")
	public List<StateFireInstallation> loadStatesFireInstallation() {
		return Arrays.asList(StateFireInstallation.values());
	}
	
	public StateFireInstallation getStateFireInstallation() {
		return stateFireInstallation;
	}

	public void setStateFireInstallation(StateFireInstallation stateFireInstallation) {
		this.stateFireInstallation = stateFireInstallation;
	}

	@Factory("statesGasInstallation")
	public List<StateGasInstallation> loadStatesGasInstallation() {
		return Arrays.asList(StateGasInstallation.values());
	}
	
	public StateGasInstallation getStateGasInstallation() {
		return stateGasInstallation;
	}

	public void setStateGasInstallation(StateGasInstallation stateGasInstallation) {
		this.stateGasInstallation = stateGasInstallation;
	}

	@Factory("statesSystemEvacuationEscape")
	public List<StateSystemEvacuationEscape> loadStatesSystemEvacuationEscape() {
		return Arrays.asList(StateSystemEvacuationEscape.values());
	}
	
	public StateSystemEvacuationEscape getStateSystemEvacuationEscape() {
		return stateSystemEvacuationEscape;
	}

	public void setStateSystemEvacuationEscape(StateSystemEvacuationEscape stateSystemEvacuationEscape) {
		this.stateSystemEvacuationEscape = stateSystemEvacuationEscape;
	}

	@Factory("statesSystemVentilationContamination")
	public List<StateSystemVentilationContamination> loadStatesSystemVentilationContamination() {
		return Arrays.asList(StateSystemVentilationContamination.values());
	}
	
	public StateSystemVentilationContamination getStateSystemVentilationContamination() {
		return stateSystemVentilationContamination;
	}

	public void setStateSystemVentilationContamination(StateSystemVentilationContamination stateSystemVentilationContamination) {
		this.stateSystemVentilationContamination = stateSystemVentilationContamination;
	}
	
	public TechnicalInformation getTechnicalInformation() {
		return technicalInformation;
	}

	public void setTechnicalInformation(TechnicalInformation technicalInformation) {
		this.technicalInformation = technicalInformation;
	}

	public Local getLocal() {
		return local;
	}
	
	public void setLocal(Local local) {
		this.local = local;
	}

	public String saveOrUpdateTechnicalInfomation(){
		System.out.println("llega al metodo de guardar....................");		
		
		if (this.technicalInformation.getId() == null) {
			this.technicalInformation.setInspections(this.getInstance());
			technicalInformationHome.setInstance(this.technicalInformation);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< esta en null ");
			technicalInformationHome.persist();
			return "updated";
		}else{
			technicalInformationHome.setInstance(this.technicalInformation);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>< si existe "+technicalInformation.getId());
			technicalInformationHome.update();
			return "updated";
		}
		//this.setTechnicalInformation(technicalInformation);
		//addLocal();
	}

	public String saveOrUpdateTransportTechnicalInfomation(){
		System.out.println("llega al metodo de guardar....................");		
		
		if (this.transportTechnicalInformation.getId() == null) {
			this.transportTechnicalInformation.setInspections(this.getInstance());
			transportTechnicalInformationHome.setInstance(this.transportTechnicalInformation);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< esta en null ");
			transportTechnicalInformationHome.persist();
			return "updated0";
		}else{
			transportTechnicalInformationHome.setInstance(this.transportTechnicalInformation);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>< si existe "+technicalInformation.getId());
			transportTechnicalInformationHome.update();
			return "updated0";
		}
		//this.setTechnicalInformation(technicalInformation);
		//addLocal();
	}

	public String getInspectorIdentificationNumber() {
		return inspectorIdentificationNumber;
	}

	public void setInspectorIdentificationNumber(String inspectorIdentificationNumber) {
		this.inspectorIdentificationNumber = inspectorIdentificationNumber;
	}

	public TechnicalInformationHome getTechnicalInformationHome() {
		return technicalInformationHome;
	}

	public void setTechnicalInformationHome(
			TechnicalInformationHome technicalInformationHome) {
		this.technicalInformationHome = technicalInformationHome;
	}

	public TransportTechnicalInformationHome getTransportTechnicalInformationHome() {
		return transportTechnicalInformationHome;
	}

	public void setTransportTechnicalInformationHome(
			TransportTechnicalInformationHome transportTechnicalInformationHome) {
		this.transportTechnicalInformationHome = transportTechnicalInformationHome;
	}
	/// transport
	@Factory("typesInspections")
	public List<TypeInspections> loadTypesInspections() {
		return Arrays.asList(TypeInspections.values());
	}
	
	public TypeInspections getTypeInspections() {
		return typeInspections;
	}

	public void setTypeInspections(TypeInspections typeInspections) {
		this.typeInspections = typeInspections;
	}

	public TransportTechnicalInformation getTransportTechnicalInformation() {
		return transportTechnicalInformation;
	}

	public void setTransportTechnicalInformation(
			TransportTechnicalInformation transportTechnicalInformation) {
		this.transportTechnicalInformation = transportTechnicalInformation;
	}

	public void addObservation(){
		this.technicalInformation.add(new InspectionObservation());
	}
	
	public void removeObservation(InspectionObservation io){
		this.technicalInformation.remove(io);
	}
	
	public void addObservationTransport(){
		this.transportTechnicalInformation.add(new InspectionObservation());
	}
	
	public void removeObservationTransport(InspectionObservation io){
		this.transportTechnicalInformation.remove(io);
	}

}
