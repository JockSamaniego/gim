package org.gob.gim.binnaclecrv.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.gob.gim.binnaclecrv.facade.BinnacleCRVService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.sun.xml.internal.ws.client.BindingProviderProperties;

import ec.gob.ant.DatosLicencia;
import ec.gob.ant.EntradaPersona;
import ec.gob.gim.binnaclecrv.model.AccidentPart;
import ec.gob.gim.binnaclecrv.model.AdmissionCategory;
import ec.gob.gim.binnaclecrv.model.BinnacleCRV;
import ec.gob.gim.binnaclecrv.model.DocumentTypeBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.Implicated;
import ec.gob.gim.binnaclecrv.model.InformativePart;
import ec.gob.gim.binnaclecrv.model.ObsBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.ObsTypeBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.PhotographicEvidence;
import ec.gob.gim.binnaclecrv.model.VehicleInventory;
import ec.gob.gim.binnaclecrv.model.VehicleInventoryBase;
import ec.gob.gim.binnaclecrv.model.VehicleItem;
import ec.gob.loja.middleapp.InfractionWSV2;
import ec.gob.loja.middleapp.InfractionWSV2Service;
import ec.gob.loja.middleapp.ResponseDatosLicencia;
import ec.gob.loja.middleapp.ResponseVehiculo;

@Name("binnacleCRVHome")
public class BinnacleCRVHome extends EntityHome<BinnacleCRV> {

	private static final long serialVersionUID = 1L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	@EJB
	BinnacleCRVService binnacleCRVService;

	@Logger
	Log logger;

	@In
	FacesMessages facesMessages;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private boolean isFirstTime = true;
	
	private String licenseCriteria;
	private String criteria;
	private String criteriaSearch;
	private boolean canEditLicensePlate = false;

	private VehicleItem vehicleItem = new VehicleItem();
	private ObsBinnacleCRV obsBinnacleCRV = new ObsBinnacleCRV();
	private InformativePart informativePart = new InformativePart();
	private AccidentPart accidentPart = new AccidentPart();
	private boolean hasJudicialDocument = false;
	private String partTimeString = "";
	private String retentionTimeString = "";
	private String accidentTimeString = "";
	private String notificationTimeString = "";
	private String arrivalTimeString = "";
    private DatosLicencia datosLicencia = new DatosLicencia();
    
	private Implicated implicated = new Implicated();

	public void setBinnacleCRVId(Long id) {
		setId(id);
	}

	public Long getBinnacleCRVId() {
		return (Long) getId();
	}

	/**
	 * @return the vehicleItem
	 */
	public VehicleItem getVehicleItem() {
		return vehicleItem;
	}

	/**
	 * @param vehicleItem the vehicleItem to set
	 */
	public void setVehicleItem(VehicleItem vehicleItem) {
		this.vehicleItem = vehicleItem;
	}

	/**
	 * @return the obsBinnacleCRV
	 */
	public ObsBinnacleCRV getObsBinnacleCRV() {
		return obsBinnacleCRV;
	}

	/**
	 * @param obsBinnacleCRV the obsBinnacleCRV to set
	 */
	public void setObsBinnacleCRV(ObsBinnacleCRV obsBinnacleCRV) {
		this.obsBinnacleCRV = obsBinnacleCRV;
	}

	/**
	 * @return the informativePart
	 */
	public InformativePart getInformativePart() {
		return informativePart;
	}

	/**
	 * @param informativePart the informativePart to set
	 */
	public void setInformativePart(InformativePart informativePart) {
		this.informativePart = informativePart;
	}

	/**
	 * @return the accidentPart
	 */
	public AccidentPart getAccidentPart() {
		return accidentPart;
	}

	/**
	 * @param accidentPart the accidentPart to set
	 */
	public void setAccidentPart(AccidentPart accidentPart) {
		this.accidentPart = accidentPart;
	}

	/**
	 * @return the implicated
	 */
	public Implicated getImplicated() {
		return implicated;
	}

	/**
	 * @param implicated the implicated to set
	 */
	public void setImplicated(Implicated implicated) {
		this.implicated = implicated;
	}

	/**
	 * @return the criteriaSearch
	 */
	public String getCriteriaSearch() {
		return criteriaSearch;
	}

	/**
	 * @param criteriaSearch the criteriaSearch to set
	 */
	public void setCriteriaSearch(String criteriaSearch) {
		this.criteriaSearch = criteriaSearch;
	}

	/**
	 * @return the canEditLicensePlate
	 */
	public boolean isCanEditLicensePlate() {
		return canEditLicensePlate;
	}

	/**
	 * @param canEditLicensePlate the canEditLicensePlate to set
	 */
	public void setCanEditLicensePlate(boolean canEditLicensePlate) {
		this.canEditLicensePlate = canEditLicensePlate;
	}

	public void setBusinessId(Long id) {
		setId(id);
	}

	public Long getBusinessId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if (!isFirstTime)
			return;
		isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	public BinnacleCRV getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public String getLicenseCriteria() {
		return licenseCriteria;
	}

	public void setLicenseCriteria(String licenseCriteria) {
		this.licenseCriteria = licenseCriteria;
	}

	/**
	 * @return the isFirstTime
	 */
	public boolean isFirstTime() {
		return isFirstTime;
	}

	/**
	 * @param isFirstTime the isFirstTime to set
	 */
	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	/**
	 * @return the hasJudicialDocument
	 */
	public boolean isHasJudicialDocument() {
		return hasJudicialDocument;
	}

	/**
	 * @param hasJudicialDocument the hasJudicialDocument to set
	 */
	public void setHasJudicialDocument(boolean hasJudicialDocument) {
		this.hasJudicialDocument = hasJudicialDocument;
	}

	/**
	 * @return the partTimeString
	 */
	public String getPartTimeString() {
		return partTimeString;
	}

	/**
	 * @param partTimeString the partTimeString to set
	 */
	public void setPartTimeString(String partTimeString) {
		this.partTimeString = partTimeString;
	}

	/**
	 * @return the retentionTimeString
	 */
	public String getRetentionTimeString() {
		return retentionTimeString;
	}

	/**
	 * @param retentionTimeString the retentionTimeString to set
	 */
	public void setRetentionTimeString(String retentionTimeString) {
		this.retentionTimeString = retentionTimeString;
	}

	/**
	 * @return the accidentTimeString
	 */
	public String getAccidentTimeString() {
		return accidentTimeString;
	}

	/**
	 * @param accidentTimeString the accidentTimeString to set
	 */
	public void setAccidentTimeString(String accidentTimeString) {
		this.accidentTimeString = accidentTimeString;
	}

	/**
	 * @return the notificationTimeString
	 */
	public String getNotificationTimeString() {
		return notificationTimeString;
	}

	/**
	 * @param notificationTimeString the notificationTimeString to set
	 */
	public void setNotificationTimeString(String notificationTimeString) {
		this.notificationTimeString = notificationTimeString;
	}

	/**
	 * @return the arrivalTimeString
	 */
	public String getArrivalTimeString() {
		return arrivalTimeString;
	}

	/**
	 * @param arrivalTimeString the arrivalTimeString to set
	 */
	public void setArrivalTimeString(String arrivalTimeString) {
		this.arrivalTimeString = arrivalTimeString;
	}

	/**
	 * @return the datosLicencia
	 */
	public DatosLicencia getDatosLicencia() {
		return datosLicencia;
	}

	/**
	 * @param datosLicencia the datosLicencia to set
	 */
	public void setDatosLicencia(DatosLicencia datosLicencia) {
		this.datosLicencia = datosLicencia;
	}

	/**
	 * @param messageLicense the messageLicense to set
	 */
	public void setMessageLicense(String messageLicense) {
		this.messageLicense = messageLicense;
	}

	private String messageLicense = "";

	public String getMessageLicense() {
		return messageLicense;
	}

	/* (non-Javadoc)
	 * @see org.jboss.seam.framework.EntityHome#persist()
	 */
	@Override
	public String persist() {
		if (!isIdDefined()) {
			for (VehicleInventory vi : instance.getVehicleInventories()) {
				vi.setBinnacleCRV(instance);
			}
			for (PhotographicEvidence pe : instance.getPhotographicEvidences()) {
				pe.setBinnacleCRV(instance);
			}
		}
		if (hasJudicialDocument) {
			if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.INFORMATIVE_PART){
				instance.setInformativePart(informativePart);
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ACCIDENT_PART){
				instance.setAccidentPart(accidentPart);
			}

		}
		return super.persist();
	}

	public void loadInventory() throws Exception {
		if (binnacleCRVService == null)
			binnacleCRVService = ServiceLocator.getInstance().findResource(BinnacleCRVService.LOCAL_NAME);
		if (!isIdDefined() && this.getInstance().getVehicleInventories().isEmpty()) {
			List<VehicleInventoryBase> result = binnacleCRVService.findVehicleInventoryBase();
			if (!result.isEmpty()) {
				for (VehicleInventoryBase vib : result) {
					VehicleInventory vi = new VehicleInventory();
					vi.setName(vib.getName());
					vi.setOrderColumn(vib.getOrderColumn());
					vi.setAmountVisible(vib.getAmountVisible());
					this.getInstance().getVehicleInventories().add(vi);
				}
			}
		}
	}
	
	public void addAllInventory() throws Exception {
		for (VehicleInventory vi : instance.getVehicleInventories()) {
			vi.setValue(true);
			vi.setAmount(0);
		}
	}

	public void clearAllInventory() {
		for (VehicleInventory vi : instance.getVehicleInventories()) {
			vi.setValue(false);
			vi.setAmount(0);
		}
	}

	public void createBinnacleCRV() {
		canEditLicensePlate = true;
		informativePart = null;
		accidentPart = null;
		hasJudicialDocument = false;
	}
	
	public void editBinnacleCRV() {
		canEditLicensePlate = false;
		if (!getInstance().isHasJudicialDocument()){
			if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.INFORMATIVE_PART){
				informativePart = new InformativePart();
				partTimeString = "";
				retentionTimeString = "";
				accidentTimeString = "";
				notificationTimeString = "";
				arrivalTimeString = "";
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ACCIDENT_PART){
				accidentPart = new AccidentPart();
				partTimeString = "";
				retentionTimeString = "";
				accidentTimeString = "";
				notificationTimeString = "";
				arrivalTimeString = "";
			}
			
		}
		else {
			if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.INFORMATIVE_PART){
				informativePart = instance.getInformativePart();
				partTimeString = informativePart.getPartTime().toString();
				retentionTimeString = informativePart.getRetentionTime().toString();
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ACCIDENT_PART){
				accidentPart = instance.getAccidentPart();
				partTimeString = accidentPart.getPartTime().toString();
				retentionTimeString = accidentPart.getDetentionTime().toString();
				accidentTimeString = accidentPart.getAccidentTime().toString();
				notificationTimeString = accidentPart.getNotificationTime().toString();
				arrivalTimeString = accidentPart.getArrivalTime().toString();
			}
		}
		hasJudicialDocument = instance.isHasJudicialDocument();
	}
	
	public void createVehicleItem() {
		this.vehicleItem = new VehicleItem();
	}
	
	public void editVehicleItem(VehicleItem vehicleItem) {
		this.vehicleItem = vehicleItem;
	}
	
	public void addVehicleItem() {
		instance.add(vehicleItem);
	}
	
	public void removeVehicleItem(VehicleItem vehicleItem) {
		if (vehicleItem != null)
			instance.remove(vehicleItem);
	}

	public void createImplicated() {
		this.implicated = new Implicated();
	}
	
	public void editImplicated(Implicated implicated) {
		this.implicated = implicated;
	}
	
	public void addImplicatedAccidentPart() {
		accidentPart.add(implicated);
	}
	
	public void removeImplicatedAccidentPart(Implicated implicated) {
		if (implicated != null)
			accidentPart.remove(implicated);
	}

	public void createObs(int tipo) {
		this.obsBinnacleCRV = new ObsBinnacleCRV();
		obsBinnacleCRV.setUser(userSession.getUser());
		obsBinnacleCRV.setObsDate(GregorianCalendar.getInstance().getTime());
		obsBinnacleCRV.setObsTime(GregorianCalendar.getInstance().getTime());
		if (tipo == 1){ //Ingreso o Admission
			obsBinnacleCRV.setObsTypeBinnacleCRV(ObsTypeBinnacleCRV.ADMISSION);
		} else {
			obsBinnacleCRV.setObsTypeBinnacleCRV(ObsTypeBinnacleCRV.DELIVERY);
		}
	}
	
	public void editObs(ObsBinnacleCRV obsBinnacleCRV) {
		this.obsBinnacleCRV = obsBinnacleCRV;
	}
	
	public void addObs() {
		instance.add(obsBinnacleCRV);
	}
	
	public void removeObs(ObsBinnacleCRV obsBinnacleCRV) {
		if (obsBinnacleCRV != null)
			instance.remove(obsBinnacleCRV);
	}

	public InfractionWSV2 getInfractionWSV2Instance() {
//        System.out.println("---Properties1:" + System.getProperties());
//        System.setProperty("com.sun.xml.internal.ws.connect.timeout", "10000");
//        System.setProperty("com.sun.xml.ws.connect.timeout", "10000");
//        System.setProperty("com.sun.xml.internal.ws.request.timeout", "5000");
//        System.setProperty("javax.xml.ws.client.receiveTimeout", "5000");
//        System.out.println("---Properties2:" + System.getProperties());
		InfractionWSV2Service service = new InfractionWSV2Service();
		InfractionWSV2 infractionWSV2 = service.getInfractionWSV2Port();
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
//        headers.put("username", Collections.singletonList("rgpaladinesc1"));
//        headers.put("password", Collections.singletonList("7I8w:NJA34"));
        headers.put("username", Collections.singletonList("rgpaladinesc"));
        headers.put("password", Collections.singletonList("w3fjdi$$*89ML"));
        Map<String, Object> req_ctx = ((BindingProvider)infractionWSV2).getRequestContext();
        req_ctx.put(BindingProviderProperties.REQUEST_TIMEOUT, 5000);
        req_ctx.put(BindingProviderProperties.CONNECT_TIMEOUT, 10000);
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        ((BindingProvider)infractionWSV2).getRequestContext().putAll(req_ctx);
        return infractionWSV2;
	}
	
	public void searchVehicle() {
		if (!canEditLicensePlate) return;
		try{
			InfractionWSV2 infractionWSV2 = getInfractionWSV2Instance();
			ResponseVehiculo responseVehiculo = infractionWSV2.consultarVehiculo(this.instance.getLicensePlate());
//			System.out.println("---responseVehicle---"+responseVehiculo);
//			System.out.println("---responseVehicle message---"+responseVehiculo.getMessage());
//			System.out.println("---responseVehicle vehicle----"+responseVehiculo.getVehicle());
//			System.out.println("---responseVehicle code: "+responseVehiculo.getCode());
//			System.out.println("---responseVehicle codeError: "+responseVehiculo.getVehicle().getResultado().getCodError());
			if (responseVehiculo.getCode() == 200){
				instance.setOwnerName(responseVehiculo.getVehicle().getPropietario());
				instance.setOwnerIdentification(responseVehiculo.getVehicle().getDocPropietario());
				instance.setOwnerAddress(responseVehiculo.getVehicle().getDireccion());
				instance.setOwnerEmail(responseVehiculo.getVehicle().getCorreo());
				instance.setChassis(responseVehiculo.getVehicle().getChasis());
				instance.setModel(responseVehiculo.getVehicle().getModeloDesc());
				instance.setMotor(responseVehiculo.getVehicle().getMotor());
				instance.setChassis(responseVehiculo.getVehicle().getChasis());
				instance.setService(responseVehiculo.getVehicle().getTipoServicio());
				instance.setChassis(responseVehiculo.getVehicle().getChasis());
				instance.setType(responseVehiculo.getVehicle().getTipoPeso());
				instance.setYear(responseVehiculo.getVehicle().getAnio() == null ? 0 : Integer.parseInt(responseVehiculo.getVehicle().getAnio()));
				if (responseVehiculo.getVehicle().getTonelaje() != null) 
					responseVehiculo.getVehicle().setTonelaje(responseVehiculo.getVehicle().getTonelaje().replaceAll(",", "."));
				instance.setTonnage(responseVehiculo.getVehicle().getTonelaje() == null ? 0 : Double.parseDouble("0"+responseVehiculo.getVehicle().getTonelaje()));
				if (responseVehiculo.getVehicle().getResultado().getCodError().compareToIgnoreCase("0") != 0) {
					instance.setLicensePlate("");
					facesMessages.add(responseVehiculo.getVehicle().getResultado().getMensaje());
				} else {
					facesMessages.add("Vehículo encontrado con placa: " + instance.getLicensePlate());
					canEditLicensePlate = false;
				}
			} else {
				instance.setLicensePlate("");
				facesMessages.add(responseVehiculo.getMessage());
			}
		} catch (WebServiceException e){
			e.printStackTrace();
			instance.setLicensePlate("");
			facesMessages.add("Error de conexión al Servicio de Datos de ANT. Comuníquese con el Administrador del Sistema");
		} catch (Exception e){
			instance.setLicensePlate("");
			facesMessages.add("Error desconocido. Comuníquese con el Administrador del Sistema");
			e.printStackTrace();
		}
		
	}
	
	public ResponseVehiculo searchResponseVehicle(String licensePlate) {
//		if (!canEditLicensePlate) return null;
		try{
			InfractionWSV2 infractionWSV2 = getInfractionWSV2Instance();
			ResponseVehiculo responseVehiculo = infractionWSV2.consultarVehiculo(licensePlate);

			if (responseVehiculo.getCode() == 200){
				return responseVehiculo;
			} else {
				facesMessages.add(responseVehiculo.getMessage());
				return null;
			}
		} catch (WebServiceException e){
			e.printStackTrace();
			facesMessages.add("Error de conexión al Servicio de Datos de ANT. Comuníquese con el Administrador del Sistema");
			return null;
		} catch (Exception e){
			facesMessages.add("Error desconocido. Comuníquese con el Administrador del Sistema");
			e.printStackTrace();
			return null;
		}
		
	}
	
	public ResponseDatosLicencia searchLicenseAccidentPart() {
//		if (!canEditLicensePlate) return null;
		try{
			InfractionWSV2 infractionWSV2 = getInfractionWSV2Instance();
			
			EntradaPersona datos = new EntradaPersona();
			datos.setIdIdentificacion("CED");
			datos.setIdentificacion(implicated.getIdentification());
			ResponseDatosLicencia responseLicense = infractionWSV2.consultarLicencia(datos);
//			System.out.println("---responseVehicle---"+responseVehiculo);
//			System.out.println("---responseVehicle message---"+responseVehiculo.getMessage());
//			System.out.println("---responseVehicle vehicle----"+responseVehiculo.getVehicle());
//			System.out.println("---responseVehicle code: "+responseVehiculo.getCode());
//			System.out.println("---responseVehicle codeError: "+responseVehiculo.getVehicle().getResultado().getCodError());
			if (responseLicense.getCode() == 200){
				return responseLicense;
			} else {
				implicated.setIdentification("");
				facesMessages.add(responseLicense.getMessage());
				return null;
			}
		} catch (WebServiceException e){
			e.printStackTrace();
			implicated.setIdentification("");
			facesMessages.add("Error de conexión al Servicio de Datos de ANT. Comuníquese con el Administrador del Sistema");
			return null;
		} catch (Exception e){
			implicated.setIdentification("");
			facesMessages.add("Error desconocido. Comuníquese con el Administrador del Sistema");
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void image1Listener(UploadEvent event) {
		PhotographicEvidence evidence = getInstance().getPhotographicEvidences().get(0);
		UploadItem item = event.getUploadItem();		
		if (item != null && item.getData() != null) {
			logger.info(item.getFileName() + ", size: " + item.getFileSize());
			if (item.getFileSize() > 1048576){
				facesMessages.add("La imagen ingresada es demasiado grande.");
				item = null;
			} else {
				if (item.getFileName().length() > 80)
					evidence.setName(item.getFileName().substring(0, 80));
				else
					evidence.setName(item.getFileName());
				evidence.setPhoto(item.getData());
			}
		}
	}

	public void clearImage1() {
		PhotographicEvidence evidence = getInstance().getPhotographicEvidences().get(0);
		evidence.setPhoto(null);
	}
	
	public void image2Listener(UploadEvent event) {
		PhotographicEvidence evidence = getInstance().getPhotographicEvidences().get(1);
		UploadItem item = event.getUploadItem();		
		if (item != null && item.getData() != null) {
			logger.info(item.getFileName() + ", size: " + item.getFileSize());
			if (item.getFileSize() > 1048576){
				facesMessages.add("La imagen ingresada es demasiado grande.");
				item = null;
			} else {
				if (item.getFileName().length() > 80)
					evidence.setName(item.getFileName().substring(0, 80));
				else
					evidence.setName(item.getFileName());
				evidence.setPhoto(item.getData());
			}
		}
	}

	public void clearImage2() {
		PhotographicEvidence evidence = getInstance().getPhotographicEvidences().get(1);
		evidence.setPhoto(null);
	}
	
	public List<AdmissionCategory> findAdmissionCategories(){
		List<AdmissionCategory> list = new ArrayList<AdmissionCategory>();
		if (instance == null) return list;
		if (instance.getAdmissionType() == null) return list;
		list = binnacleCRVService.findAdmissionCategories(instance.getAdmissionType());
		return list;
	}
	
	public void parseToInformativePartTime(String text) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(text);
			informativePart.setPartTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			informativePart.setPartTime(null);
		}
	}
	
	public void generateDocumentPart(){
		if (instance.getDocumentTypeBinnacleCRV() == null) return;
		hasJudicialDocument = true;
		instance.setHasJudicialDocument(true);
		if (instance.getDocumentTypeBinnacleCRV().name().equalsIgnoreCase(DocumentTypeBinnacleCRV.INFORMATIVE_PART.name())){
			System.out.println("--------cambia a informative part");
			informativePart = new InformativePart();
			instance.setInformativePart(informativePart);
		} else if (instance.getDocumentTypeBinnacleCRV().name().equalsIgnoreCase(DocumentTypeBinnacleCRV.ACCIDENT_PART.name())){
			System.out.println("--------cambia a accident part");
			accidentPart = new AccidentPart();
			instance.setAccidentPart(accidentPart);
		} else if (instance.getDocumentTypeBinnacleCRV().name().equalsIgnoreCase(DocumentTypeBinnacleCRV.ADMINISTRATIVE_PART.name())){
			instance.setHasJudicialDocument(false);
		} else if (instance.getDocumentTypeBinnacleCRV().name().equalsIgnoreCase(DocumentTypeBinnacleCRV.JUDICIAL_PART.name())){
			instance.setHasJudicialDocument(false);
		} else {
			instance.setHasJudicialDocument(false);
		}
	}
	
	public void validateInformativePartTime(){
		if (partTimeString  == null) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(partTimeString);
			informativePart.setPartTime(date);
			partTimeString = sdf.format(date);
		} catch (ParseException e) {
			partTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateInformativeRetentionTime(){
		if (retentionTimeString  == null) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(retentionTimeString);
			informativePart.setRetentionTime(date);
			retentionTimeString = sdf.format(date);
		} catch (ParseException e) {
			retentionTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateAccidentRetentionTime(){
		if (retentionTimeString  == null) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(retentionTimeString);
			accidentPart.setDetentionTime(date);
			retentionTimeString = sdf.format(date);
		} catch (ParseException e) {
			retentionTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateAccidentPartTime(){
		if (partTimeString  == null) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(partTimeString);
			accidentPart.setPartTime(date);
			partTimeString = sdf.format(date);
		} catch (ParseException e) {
			partTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateAccidentTime(){
		if (accidentTimeString  == null) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(accidentTimeString);
			accidentPart.setAccidentTime(date);
			accidentTimeString = sdf.format(date);
		} catch (ParseException e) {
			accidentTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateNotificationPartTime(){
		if (notificationTimeString  == null) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(notificationTimeString);
			accidentPart.setNotificationTime(date);
			notificationTimeString = sdf.format(date);
		} catch (ParseException e) {
			notificationTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateArrivalPartTime(){
		if (arrivalTimeString  == null) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(arrivalTimeString);
			accidentPart.setArrivalTime(date);
			arrivalTimeString = sdf.format(date);
		} catch (ParseException e) {
			arrivalTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void searchImplicated(){
		ResponseDatosLicencia responseLicense = searchLicenseAccidentPart();
		if (responseLicense == null) return;
		if (responseLicense.getLicenseData().getResultado().getExito().compareToIgnoreCase("N") == 0) {
			implicated.setIdentification("");
			facesMessages.add(responseLicense.getLicenseData().getResultado().getMensaje());
		} else {
			datosLicencia = responseLicense.getLicenseData();
			fillImplicated(datosLicencia);
			facesMessages.add("Licencia válida: " + datosLicencia.getNombreCompleto());
			canEditLicensePlate = false;
		}
	}
    
	private void fillImplicated(DatosLicencia datosLicencia) {
		implicated.setAccidentPart(accidentPart);
		implicated.setName(datosLicencia.getNombreCompleto());
		implicated.setOrigen(datosLicencia.getNacionalidad());
		if (!datosLicencia.getLicencias().isEmpty())
            implicated.setLicenseType(datosLicencia.getLicencias().get(0).getTipo());
		
	}
	
	public void searchVehicleImplicated(){
		ResponseVehiculo responseVehicle = searchResponseVehicle(implicated.getLicensePlate());
		if (responseVehicle == null) {
			implicated.setLicensePlate("");
			return;
		}
		if (responseVehicle.getVehicle().getResultado().getCodError().compareToIgnoreCase("0") != 0) {
			implicated.setLicensePlate("");
			facesMessages.add(responseVehicle.getVehicle().getResultado().getMensaje());
		} else {
			facesMessages.add("Vehículo válido: " + responseVehicle.getVehicle().getPlacaActual());
			fillImplicatedVehicle(responseVehicle);
			canEditLicensePlate = false;
		}
	}
    
	private void fillImplicatedVehicle(ResponseVehiculo responseVehicle) {
		String resume = responseVehicle.getVehicle().getModeloDesc() + " / " 
				+ responseVehicle.getVehicle().getColor() + " / " 
				+ responseVehicle.getVehicle().getPlacaActual();
		implicated.setResumeVehicle(resume);
	}
	
}