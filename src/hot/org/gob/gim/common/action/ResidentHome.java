package org.gob.gim.common.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.dto.HistoryChangeResident;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.common.model.Address;
import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.common.model.LegalEntity;
import ec.gob.gim.common.model.LegalEntityType;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.loja.client.clients.UserClient;
import ec.gob.loja.client.model.Message;
import ec.gob.loja.client.model.UserWS;
import javax.faces.application.FacesMessage;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

@Name("residentHome")
public class ResidentHome extends EntityHome<Resident> {

    private static final long serialVersionUID = 1L;
    private String residentType = "Person";
    public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
    private SystemParameterService systemParameterService;

    @Logger
    Log log;

    private String country;

    private List<HistoryChangeResident> historyChangeResidentList = new ArrayList<HistoryChangeResident>();

    @In
    FacesMessages facesMessages;

    @In(scope = ScopeType.SESSION, value = "userSession")
    UserSession userSession;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setResidentId(Long id) {
        setId(id);
    }

    public Long getResidentId() {
        return (Long) getId();
    }

    public String changeResidentType() {
        EntityManager em = getEntityManager();
        String sqlSentence = "UPDATE Resident "
                + "SET residentType = :residentType, "
                + "    lastName = :lastName, "
                + "    identificationType = :identificationType "
                + "  WHERE id = :id";
        Query query = em.createNativeQuery(sqlSentence);
        query.setParameter("id", instance.getId());

        if (instance.getClass().getSimpleName().equalsIgnoreCase("Person")) {
            Person person = (Person) instance;
            query.setParameter("residentType", "J");
            query.setParameter("lastName", person.getLastName());
            query.setParameter("identificationType", "TAXPAYER_DOCUMENT");
        } else {
            LegalEntity person = (LegalEntity) instance;
            query.setParameter("residentType", "N");
            query.setParameter("lastName", person.getName());
            query.setParameter("identificationType", "NATIONAL_IDENTITY_DOCUMENT");
        }
        query.executeUpdate();
        return "updated";
    }

    public boolean isPassport() {

        if (this.getInstance().getIdentificationType() != null
                && this.getInstance().getIdentificationType().equals(IdentificationType.PASSPORT)) {
            return true;
        }

        return false;
    }

    @Override
    protected Resident createInstance() {
        Resident resident;
        if (residentType.equalsIgnoreCase("Person")) {
            resident = new Person();
            resident.setIdentificationType(IdentificationType.NATIONAL_IDENTITY_DOCUMENT);
        } else {
            resident = new LegalEntity();
            resident.setIdentificationType(IdentificationType.TAXPAYER_DOCUMENT);
        }
        // disableAddressControls = false;
        return resident;
    }

    public List<String> findCountries(Object suggestion) {
        List<String> countries = new ArrayList<String>();
        String countryName = suggestion.toString();
        if (countryName.length() < 3) {
            return countries;
        }

        java.util.Locale[] locales = java.util.Locale.getAvailableLocales();
        for (java.util.Locale locale : locales) {
            String name = locale.getDisplayCountry();
            if (!countries.contains(name) && name.toLowerCase().startsWith(countryName.toLowerCase())) {
                countries.add(name);
            }
        }

        Collections.sort(countries);
        return countries;
    }

    public void beforeAutcomplete(String c) {
        if (c == null) {
            country = null;
        } else {
            country = c;
        }
    }

    public boolean existIdentification() {
        Resident r = findResident(this.getInstance().getIdentificationNumber());
        if (r == null) {
            return false;
        }
        if (r != null && r.getId() != getInstance().getId()) {
            return true;
        }
        return false;
    }

    private boolean isPublicEntityCodeValid() {
        Resident resident = instance;
        if (resident.getClass() == LegalEntity.class) {
            LegalEntity legalEntity = (LegalEntity) resident;
            System.out.println("LEADING YOU A CHOICE: entityType " + legalEntity.getLegalEntityType());
            if (legalEntity.getLegalEntityType() == LegalEntityType.PUBLIC) {
                String code = (String) legalEntity.getCode();
                System.out.println("LEADING YOU A CHOICE: code " + code);
                if (code == null || code.isEmpty()) {
                    String message = ResourceBundle.instance().getString("InvalidPublicEntityCodeException");
                    InvalidValue iv = new InvalidValue(message, LegalEntityType.class, "code", null, legalEntity);
                    facesMessages.addToControl("legalEntityCode", iv);
                    return false;
                }
            }
        }
        return true;
    }

    public String save() {
        String outcome = null;
        try {
            if (!isPublicEntityCodeValid()) {
                return null;
            }

            if (residentType.equalsIgnoreCase("Person")) {
                instance.setName(instance.toString());
                ((Person) getInstance()).setCountry(country);
            }

            ResidentService residentService = ServiceLocator.getInstance().findResource(ResidentService.LOCAL_NAME);
            residentService.save(getInstance());
            if (this.getInstance().getId() != null) {
                outcome = "updated";
            } else {
                outcome = "persisted";
            }
            try {
                UserWS userws = new UserWS();
                if (residentType.equalsIgnoreCase("Person")) {

                    userws.setIdentification(((Person) instance).getIdentificationNumber());
                    userws.setName(((Person) instance).getFirstName());
                    userws.setSurname(((Person) instance).getLastName());
                    userws.setActive(Boolean.TRUE);
                    userws.setEmail(((Person) instance).getEmail());
                    userws.setPassword(((Person) instance).getIdentificationNumber());
                    try {
                        userws.setPhone(((Person) instance).getCurrentAddress().getPhoneNumber());
                    } catch (Exception ex) {
                        System.out.println(" setPhone sri >>> error >>>>> <<<<<<");
                        ex.printStackTrace();
                        userws.setPhone("");
                    }
                } else {

                    userws.setIdentification(((LegalEntity) instance).getIdentificationNumber());
                    userws.setName(((LegalEntity) instance).getName());
                    userws.setSurname("");
                    userws.setActive(Boolean.TRUE);
                    userws.setEmail(((LegalEntity) instance).getEmail());
                    userws.setPassword(((LegalEntity) instance).getIdentificationNumber());
                    try {
                        userws.setPhone(((LegalEntity) instance).getCurrentAddress().getPhoneNumber());
                    } catch (Exception ex) {
                        System.out.println(" setPhone sri >>> error >>>>> <<<<<<");
                        ex.printStackTrace();
                        userws.setPhone("");
                    }
                }
                this.sendToService(userws);
                addFacesMessageFromResourceBundle("update.mail.sri");
            } catch (Exception e) {
                System.out.println("save sri >>> error >>>>> " + e.getStackTrace().toString());
                e.printStackTrace();
                getFacesMessages().addFromResourceBundle(FacesMessage.SEVERITY_ERROR, "noUpdate.mail.sri");
            }
        } catch (Exception e) {
            e.printStackTrace();
            addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
        }
        return outcome;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();

        if (getInstance().getCurrentAddress() == null) {
            Address address = new Address();
            getInstance().setCurrentAddress(address);
            getInstance().add(address);
            if (getInstance() instanceof Person) {
                country = ((Person) getInstance()).getCountry();
            }
        }
    }

    private UserWS sendToService(UserWS input) throws Exception {
        log.info("BASE_URI_SRI >>>>> " + ResourceBundle.instance().getString("BASE_URI_SRI"));
        String BASE_URI = ResourceBundle.instance().getString("BASE_URI_SRI");
        UserClient client = new UserClient(BASE_URI);
        log.info("UserClient client >>>>> <<<<<<");
        UserWS response;
        response = client.saveUser_XML(input, UserWS.class);
        System.out.println("Estado >>>>>>>>>> " + response.getState());

        if (response.getMessageList() != null) {
            List<Message> mensajes = response.getMessageList();
            for (Message mensaje : mensajes) {
                System.out.println(mensaje.getType() + "\t" + mensaje.getIdentifier() + "\t" + mensaje.getMessage() + "\t" + mensaje.getAdditionalInformation());
            }
        }
        return response;
    }

    public boolean isWired() {
        return true;
    }

    public Resident getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    /*
     public String findLastValueForIdentification(String nid, String initialvalue){
     Long initial = Long.parseLong(initialvalue);
     List<String> result = getEntityManager().createNamedQuery("Resident.findIdentificationNumber").setParameter("identificationNumber", nid).getResultList();
     if (result != null && !result.isEmpty()) {			
     for(String a: result){
     Long aux = Long.parseLong(a) + 1;
     if(aux > initial){
     initial = aux;
     }				
     }			
     return initial.toString();
     }else{
     return null;
     }
     }
     */
    /*
     public boolean existLegalEntity = false;
	
     public boolean isExistLegalEntity() {
     return existLegalEntity;
     }

     public void setExistLegalEntity(boolean existLegalEntity) {
     this.existLegalEntity = existLegalEntity;
     }
     */
    @SuppressWarnings("unchecked")
    public Resident findResident(String nid) {
        List<Resident> result = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber").setParameter("identificationNumber", nid).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public List<Address> getAddresses() {
        return getInstance() == null ? null : new ArrayList<Address>(
                getInstance().getAddresses());
    }

    public List<Business> getBusinesses() {
        return getInstance() == null ? null : new ArrayList<Business>(
                getInstance().getBusinesses());
    }

    public List<Contract> getContracts() {
        return getInstance() == null ? null : new ArrayList<Contract>(
                getInstance().getContracts());
    }

    public List<MunicipalBond> getMunicipalBonds() {
        return getInstance() == null ? null : new ArrayList<MunicipalBond>(
                getInstance().getMunicipalBonds());
    }

    public List<Domain> getDomains() {
        return getInstance() == null ? null : new ArrayList<Domain>(
                getInstance().getDomains());
    }

    public String getResidentType() {
        return residentType;
    }

    public void setResidentType(String residentType) {
        this.residentType = residentType;
    }

    public List<HistoryChangeResident> getHistoryChangeResidentList() {
        return historyChangeResidentList;
    }

    public void setHistoryChangeResidentList(
            List<HistoryChangeResident> historyChangeResidentList) {
        this.historyChangeResidentList = historyChangeResidentList;
    }

    public List<IdentificationType> getIdentificationTypes() {
        List<IdentificationType> identificationTypes = new ArrayList<IdentificationType>();
        if (residentType.equalsIgnoreCase("Person")) {
            identificationTypes.add(IdentificationType.TAXPAYER_DOCUMENT);
            identificationTypes.add(IdentificationType.NATIONAL_IDENTITY_DOCUMENT);
            identificationTypes.add(IdentificationType.PASSPORT);
        } else {
            identificationTypes.add(IdentificationType.TAXPAYER_DOCUMENT);
            identificationTypes.add(IdentificationType.GENERATED_NUMBER);
        }
        return identificationTypes;
    }

    public void addAddress() {
        Address address = new Address();
        this.getInstance().add(address);
        this.getInstance().setCurrentAddress(address);
        // disableAddressControls = false;
    }

    /*
     * public Boolean getDisableAddressControls() { return
     * disableAddressControls; }
     * 
     * public void setDisableAddressControls(Boolean disableAddressControls) {
     * this.disableAddressControls = disableAddressControls; }
     */
    @SuppressWarnings("unchecked")
    public void loadHistoryChangeForResident(Long id) {
        historyChangeResidentList.clear();
        String sql = "select rv.id, rv.timestamp, rv.username, r.name, ra.revtype "
                + "from gimaudit.resident_aud ra inner join revision rv "
                + "on ra.rev = rv.id "
                + "inner join _user u on rv.username = u.name "
                + "inner join resident r on u.id = r.user_id "
                + "where ra.id = " + id
                + "order by rv.timestamp desc";

        List<Object[]> results = getEntityManager().createNativeQuery(sql).getResultList();
        for (Object[] row : results) {
            HistoryChangeResident hcr = new HistoryChangeResident((BigInteger) row[0], (Date) row[1], row[2].toString(), row[3].toString(), (Short) row[4]);
            historyChangeResidentList.add(hcr);
        }
    }

    public String getTransactionTypeKey(Integer value) {
        return "TT" + value;
    }

    public Boolean hasRole(String roleKey) {
        if (systemParameterService == null) {
            systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
        }
        String role = systemParameterService.findParameter(roleKey);
        if (role != null) {
            return userSession.getUser().hasRole(role);
        }
        return false;
    }

}
