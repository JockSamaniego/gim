package org.gob.gim.propertyregister.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.common.model.CheckingRecord;
import ec.gob.gim.common.model.CheckingRecordType;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.propertyregister.model.PropertyRegister;

@Name("propertyRegisterHome")
@Scope(ScopeType.CONVERSATION)
public class PropertyRegisterHome extends EntityHome<PropertyRegister> {

    private static final long serialVersionUID = -2586577925050672565L;
    private static String messageObs = "Se registra el Traspaso de Dominio según información del Registro de la Propiedad";
    private Resident resident;
    private String criteria;
    private String identificationNumber;
    private String partnerIdentificationNumber;

    private Property property;
    private String criteriaProperty;

    private List<Resident> residents;

    private SystemParameterService systemParameterService;

    public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

    private Domain newDomain;
    
    @Logger
    Log logger;

    @In
    FacesMessages facesMessages;

    @In(scope = ScopeType.SESSION, value = "userSession")
    UserSession userSession;

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public void init() {
        property = null;
        newDomain = new Domain();
        Query query = getEntityManager().createQuery("SELECT t FROM TerritorialDivision t where t.id = 11");
        TerritorialDivision notarysProvince = (TerritorialDivision) query.getSingleResult();
        newDomain.setNotarysProvince(notarysProvince);
        query = getEntityManager().createQuery("SELECT t FROM TerritorialDivision t where t.id = 20784");
        TerritorialDivision notarysCity = (TerritorialDivision) query.getSingleResult();
        newDomain.setNotarysCity(notarysCity);
        resident = null;
        if (systemParameterService == null) {
            systemParameterService = ServiceLocator.getInstance().findResource(
                    SYSTEM_PARAMETER_SERVICE_NAME);
        }
    }

    public void searchResident() {
        Query query = getEntityManager().createNamedQuery(
                "Resident.findByIdentificationNumber");
        query.setParameter("identificationNumber", this.identificationNumber);
        try {
            Resident resident = (Resident) query.getSingleResult();
            logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

//            this.getInstance().setResident(resident);
//            this.getInstance().getPropertiesInExemption().clear();
//            reCalculateValues();

            if (resident.getId() == null) {
                addFacesMessageFromResourceBundle("resident.notFound");
            }

        } catch (Exception e) {
//            this.getInstance().setResident(null);
//            reCalculateValues();
            addFacesMessageFromResourceBundle("resident.notFound");
        }
    }

    @SuppressWarnings("unchecked")
    public void searchResidentByCriteria() {
        logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
        if (this.criteria != null && !this.criteria.isEmpty()) {
            Query query = getEntityManager().createNamedQuery(
                    "Resident.findByCriteria");
            query.setParameter("criteria", this.criteria);
            residents = query.getResultList();
        }
    }

    public void clearSearchResidentPanel() {
        this.setCriteria(null);
        residents = null;
    }

    public void residentSelectedListener(ActionEvent event) {
        UIComponent component = event.getComponent();
        Resident resident = (Resident) component.getAttributes()
                .get("resident");
        clearSearchResidentPanel();
    }

    public void setPropertyRegisterId(Long id) {
        setId(id);
    }

    public Long getPropertyRegisterId() {
        return (Long) getId();
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public boolean isFirsTime = true;

    public void wire() {
        if (!isFirsTime)
            return;
        getInstance();
        isFirsTime = false;
        init();
    }

    public boolean isWired() {
        return true;
    }

    public PropertyRegister getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    public void setResidents(List<Resident> residents) {
        this.residents = residents;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getPartnerIdentificationNumber() {
        return partnerIdentificationNumber;
    }

    public void setPartnerIdentificationNumber(
            String partnerIdentificationNumber) {
        this.partnerIdentificationNumber = partnerIdentificationNumber;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getCriteriaProperty() {
        return criteriaProperty;
    }

    public void setCriteriaProperty(String criteriaProperty) {
        this.criteriaProperty = criteriaProperty;
    }

    public boolean isFirsTime() {
        return isFirsTime;
    }

    public void setFirsTime(boolean isFirsTime) {
        this.isFirsTime = isFirsTime;
    }

    public void prepareEdit(PropertyRegister propertyRegister){
        this.setInstance(propertyRegister);
    }

    public void searchData(){
        Query query = getEntityManager().createNamedQuery(
                "Resident.findByIdentificationNumber");
        query.setParameter("identificationNumber", instance.getIdentificationNumber());
        try {
            resident = (Resident) query.getSingleResult();
        } catch (Exception e) {
            facesMessages.add(Severity.ERROR, "No existe el nuevo contribuyente registrado en el Sistema.");
            e.printStackTrace();
        }
        
        query = getEntityManager().createNamedQuery(
                "Property.findByCadastralCode1");
        query.setParameter("cadastralCode", instance.getCadastralCode());
        try {
            property = (Property) query.getSingleResult();
            if (property == null){
                facesMessages.add(Severity.ERROR, "No existe la propiedad en el Sistema.");
            }
        } catch (Exception e) {
            facesMessages.add(Severity.ERROR, "No existe la propiedad en el Sistema.");
            e.printStackTrace();
        }

    }

    public String updatePrevious() {
        instance.setDateUpdate(new Date());
        instance.setResponsableUpdate(userSession.getUser());
        instance.setRegisteredChange("PREVIAMENTE");
        instance.setDomain(null);
        return super.update();
    }

    @Override
    public String update() {
        if ((property == null) || (resident == null)){
            facesMessages.add(Severity.ERROR, "La Información está incompleta, no se puede registrar.");
            return "";
        }
        
        chargeDomain();
        instance.setDateUpdate(new Date());
        instance.setResponsableUpdate(userSession.getUser());
        instance.setRegisteredChange("SI");
        instance.setDomain(newDomain);
        CheckingRecord cr = new CheckingRecord();
        cr.setChecker(userSession.getPerson());
        cr.setCheckingRecordType(CheckingRecordType.DOMAIN_TRANSFER);
        cr.setDate(new Date());
        cr.setTime(new Date());
        cr.setObservation(messageObs);
        this.instance.getDomain().getCurrentProperty().add(cr);
        return super.update();
    }

    private void chargeDomain(){
        newDomain.setAcquisitionDate(instance.getNotaryDate());
        newDomain.setRealStateNumber(String.valueOf(instance.getInscriptionNumber()));
        newDomain.setBeneficiaries(instance.getIntervenerName());
        newDomain.setBoundaries(null);
        newDomain.setCommercialAppraisal(property.getCurrentDomain().getCommercialAppraisal());
        newDomain.setCommercialAppraisalTmp(property.getCurrentDomain().getCommercialAppraisalTmp());
        newDomain.setBuildingAppraisal(property.getCurrentDomain().getBuildingAppraisal());
        newDomain.setBuildingAppraisalTmp(property.getCurrentDomain().getBuildingAppraisalTmp());
        newDomain.setLotAppraisal(property.getCurrentDomain().getLotAppraisal());
        newDomain.setLotAppraisalTmp(property.getCurrentDomain().getLotAppraisalTmp());
        newDomain.setChangeOwnerConfirmed(true);
        newDomain.setUserRegister(userSession.getUser());
        newDomain.setPropertyUse(property.getCurrentDomain().getPropertyUse());
        newDomain.setCreationDate(new Date());
        newDomain.setCreationTime(new Date());
        newDomain.setDate(instance.getNotaryDate());
        newDomain.setIsActive(true);
        newDomain.setValueBySquareMeter(property.getCurrentDomain().getValueBySquareMeter());
        newDomain.setValueBySquareMeterTmp(property.getCurrentDomain().getValueBySquareMeterTmp());
        newDomain.setValueTransaction(new BigDecimal(instance.getAmount()));
        newDomain.setProperty(property);
        newDomain.setResident(resident);
        newDomain.setBuildingAreaTransfer(property.getCurrentDomain().getBuildingAreaTransfer());
        newDomain.setTotalAreaConstruction(property.getCurrentDomain().getTotalAreaConstruction());
        newDomain.setLotAreaTransfer(property.getCurrentDomain().getLotAreaTransfer());
        newDomain.setObservations(messageObs);
        newDomain.setPreviousDomain(property.getCurrentDomain());
        property.getCurrentDomain().setIsActive(false);
        property.getCurrentDomain().setCurrentProperty(null);
        property.setCurrentDomain(null);
        property.setCurrentDomain(newDomain);
    }

    /**
     * @return the newDomain
     */
    public Domain getNewDomain() {
        return newDomain;
    }

    /**
     * @param newDomain the newDomain to set
     */
    public void setNewDomain(Domain newDomain) {
        this.newDomain = newDomain;
    }

    @SuppressWarnings("unchecked")
    public List<TerritorialDivision> getCantons() {
        return showCantons(newDomain);
    }

    /**
     * Devuelve los cantones que pertenecen a la provincia de la notaría donde
     * se realizó el traspaso o se registró el dominio
     * 
     * @return List<TerritorialDivision>
     */
    @SuppressWarnings("unchecked")
    private List<TerritorialDivision> showCantons(Domain d) {
        if (d.getNotarysProvince() != null) {
            Query query = getPersistenceContext().createNamedQuery(
                    "TerritorialDivision.findByParent");
            query.setParameter("parentId", d.getNotarysProvince().getId());
            return query.getResultList();
        } else {
            return new ArrayList<TerritorialDivision>();
        }
    }

}
