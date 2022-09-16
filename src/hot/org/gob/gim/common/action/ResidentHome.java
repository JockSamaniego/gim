package org.gob.gim.common.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
//import org.glassfish.jersey.client.ClientConfig;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.dto.HistoryChangeResident;
import org.gob.gim.common.dto.UserWS;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
//import org.hibernate.validator.InvalidValue;
import javax.validation.constraints.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.common.dto.DinardapResident;
import ec.gob.gim.common.model.Address;
import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.common.model.LegalEntity;
import ec.gob.gim.common.model.LegalEntityType;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.loja.dinardap.dinardap.cliente.DINARDAPServiceProvider;
import ec.gob.loja.dinardap.dinardap.cliente.ResponseContribuyente;

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
    
    @In(create = true)
	private Renderer renderer;

    @In(scope = ScopeType.SESSION, value = "userSession")
    UserSession userSession;
    
    @Logger
	Log logger;
    
    //consutal dinardap
    private String identificationNumber;
    private String dinardapMessage;
    private DinardapResident dinardapResident = new DinardapResident();

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
            //System.out.println("LEADING YOU A CHOICE: entityType " + legalEntity.getLegalEntityType());
            if (legalEntity.getLegalEntityType() == LegalEntityType.PUBLIC) {
                String code = (String) legalEntity.getCode();
                //System.out.println("LEADING YOU A CHOICE: code " + code);
                if (code == null || code.isEmpty()) {
                    String message = ResourceBundle.instance().getString("InvalidPublicEntityCodeException");
                    //ConstraintViolation constraintViolation = (ConstraintViolation) new ConstraintViolationException(null);
                    //InvalidValue iv = new InvalidValue(message, LegalEntityType.class, "code", null, legalEntity);
                    //facesMessages.addToControl("legalEntityCode", iv);
                    return false;
                }
            }
        }
        return true;
    }

    public String save() {	
        String outcome = null; 
        
        //2018-08-08 rfam para el control de cuenta unica
        if(this.getInstance().getGenerateUniqueAccountt()) {
        	if(this.getInstance().getEmail()==null || this.getInstance().getEmail().equals("")) {
        		getFacesMessages().addFromResourceBundle(FacesMessage.SEVERITY_ERROR, "Correo es necesario para cuenta única");
        		return null;
        	}
        }
        
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
            //rfam 2018-08-08 creacion de cuenta unica
            if(this.getInstance().getGenerateUniqueAccountt()) {
            	//createUniqueAccounttUser(this.getInstance().getIdentificationNumber());	
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
                        //System.out.println(" setPhone sri >>> error >>>>> <<<<<<");
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
                        //System.out.println(" setPhone sri >>> error >>>>> <<<<<<");
                        ex.printStackTrace();
                        userws.setPhone("");
                    }
                }
                residentService.updateUserIntoEBilling(userws);
                addFacesMessageFromResourceBundle("update.mail.sri");
            } catch (Exception e) {
                //System.out.println("save sri >>> error >>>>> " + e.getStackTrace().toString());
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

    public boolean isWired() {
        return true;
    }

    public Resident getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

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

	private String messageUniqueAccount;
	private String checkingURL;
	private String auth_username = "usrBA-AR";
	private String auth_password = "ua.ws@Sistem2";
	private String serviceUniqueAccountURL = "http://192.168.1.185:8080/services/gadloja";	
    
    public String getMessageUniqueAccount() {
		return messageUniqueAccount;
	}

	public void setMessageUniqueAccount(String messageUniqueAccount) {
		this.messageUniqueAccount = messageUniqueAccount;
	}


	public String getCheckingURL() {
		return checkingURL;
	}

	public void setCheckingURL(String checkingURL) {
		this.checkingURL = checkingURL;
	}

	public void createUniqueAccounttUser(String identificationNumber) {
		//System.out.println("--------------------cuenta unica "+identificationNumber);
		Form form = new Form();
		form.param("identificationNumber", identificationNumber);

		Client client = javax.ws.rs.client.ClientBuilder.newClient();
		WebTarget webTarget = client.target(serviceUniqueAccountURL).path("restapi");
		webTarget.register(new org.glassfish.jersey.client.filter.HttpBasicAuthFilter(auth_username, auth_password));
		Response response = webTarget.path("createUserInComplaintBD").request()
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
		String responseDetail = response.readEntity(String.class);
		try {
			JSONObject jo = new JSONObject(responseDetail);
			String status = jo.getString("status");
			if (status.equals("ok")) {
				this.messageUniqueAccount = jo.getString("message");
				this.checkingURL = jo.getString("url");
				sendMail(renderer);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Asynchronous
	private void sendMail(Renderer r){
		try {
			logger.info("Renderer "+r);
			r.render("/common/email/NewUserUniqueAccount.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	
	public String getDinardapMessage() {
		return dinardapMessage;
	}

	public void setDinardapMessage(String dinardapMessage) {
		this.dinardapMessage = dinardapMessage;
	}
	
	public DinardapResident getDinardapResident() {
		return dinardapResident;
	}

	public void setDinardapResident(DinardapResident dinardapResident) {
		this.dinardapResident = dinardapResident;
	}

	public void dinardapSearch() {
		if (this.identificationNumber != null && this.identificationNumber != "") {
			DINARDAPServiceProvider dinardap = new DINARDAPServiceProvider();
			ResponseContribuyente result = dinardap.informacionDemografica(this.identificationNumber);
			this.dinardapMessage = "Demográfico"+result.getMessage();
			this.dinardapResident = new DinardapResident();
			this.dinardapResident.setData(result);
			
			ResponseContribuyente resultBio = dinardap.informacionBiometrica(this.identificationNumber);
			this.dinardapMessage = this.dinardapMessage + "\nBiométrico: "+resultBio.getMessage();
			
			this.dinardapResident.setDiometricData(resultBio);
			
			
		} else {
			this.dinardapResident = new DinardapResident();
			this.dinardapMessage = "Ingrese número de indentificación";
		}
	}
    
}
