package org.gob.gim.binnaclecrv.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.gob.gim.binnaclecrv.facade.BinnacleCRVService;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

// import com.sun.xml.internal.ws.client.BindingProviderProperties;

import ec.gob.ant.DatosLicencia;
import ec.gob.ant.EntradaPersona;
import ec.gob.gim.binnaclecrv.model.AccidentPart;
import ec.gob.gim.binnaclecrv.model.AdmissionCategory;
import ec.gob.gim.binnaclecrv.model.ArrivalHistoryBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.BallotPart;
import ec.gob.gim.binnaclecrv.model.BinnacleCRV;
import ec.gob.gim.binnaclecrv.model.DocumentTypeBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.ExitTypeBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.Implicated;
import ec.gob.gim.binnaclecrv.model.InformativePart;
import ec.gob.gim.binnaclecrv.model.ObsBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.ObsTypeBinnacleCRV;
import ec.gob.gim.binnaclecrv.model.PhotographicEvidence;
import ec.gob.gim.binnaclecrv.model.VehicleInventory;
import ec.gob.gim.binnaclecrv.model.VehicleInventoryBase;
import ec.gob.gim.binnaclecrv.model.VehicleItem;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.adjunct.BinnacleCRVReference;
// import ec.gob.gim.revenue.model.adjunct.BinnacleCRVReference;
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
	
	private SystemParameterService systemParameterService;

	private String licenseCriteria;
	private String criteria;
	private String criteriaSearch;
	private boolean canEditLicensePlate = false;

	private VehicleItem vehicleItem = new VehicleItem();
	private ObsBinnacleCRV obsBinnacleCRV = new ObsBinnacleCRV();
	private InformativePart informativePart = new InformativePart();
	private AccidentPart accidentPart = new AccidentPart();
	private BallotPart ballotPart = new BallotPart();
	private boolean hasJudicialDocument = false;
	private String partTimeString = "";
	private String admissionTimeString = "";
	private String retentionTimeString = "";
	private String detentionTimeString = "";
	private String accidentTimeString = "";
	private String notificationTimeString = "";
	private String arrivalTimeString = "";
    private DatosLicencia datosLicencia = new DatosLicencia();
    
	private Implicated implicated = new Implicated();

	private Resident resident = null;

	ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV;
	
	private boolean canSaveExitVehicle;
	private boolean manualExit;

	private boolean hasRoleUCOT;
	private boolean hasRoleCRV;
	
	private File fileNameOfJudicialPart;
	private byte[] byteNameOfJudicialPart;
	private boolean isNewFileNameOfJudicialPart;
	
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
	 * @return the ballotPart
	 */
	public BallotPart getBallotPart() {
		return ballotPart;
	}

	/**
	 * @param ballotPart the ballotPart to set
	 */
	public void setBallotPart(BallotPart ballotPart) {
		this.ballotPart = ballotPart;
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
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
	}

	/**
	 * @return the arrivalHistoryBinnacleCRV
	 */
	public ArrivalHistoryBinnacleCRV getArrivalHistoryBinnacleCRV() {
		return arrivalHistoryBinnacleCRV;
	}

	/**
	 * @param arrivalHistoryBinnacleCRV the arrivalHistoryBinnacleCRV to set
	 */
	public void setArrivalHistoryBinnacleCRV(
			ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV) {
		this.arrivalHistoryBinnacleCRV = arrivalHistoryBinnacleCRV;
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
		findRoleCRV();
		findRoleUCOT();
	}

	public void wire() {
		if (!isFirstTime)
			return;
		findRoleCRV();
		findRoleUCOT();
		isFirstTime = false;
	}

	public void wireArrival() {
		if (!isFirstTime)
			return;
		findRoleCRV();
		findRoleUCOT();
		isFirstTime = false;
	}

	public void wireExit() {
		if (!isFirstTime)
			return;
		findRoleCRV();
		findRoleUCOT();
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
	 * @return the admissionTimeString
	 */
	public String getAdmissionTimeString() {
		return admissionTimeString;
	}

	/**
	 * @param admissionTimeString the admissionTimeString to set
	 */
	public void setAdmissionTimeString(String admissionTimeString) {
		this.admissionTimeString = admissionTimeString;
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
	 * @return the detentionTimeString
	 */
	public String getDetentionTimeString() {
		return detentionTimeString;
	}

	/**
	 * @param detentionTimeString the detentionTimeString to set
	 */
	public void setDetentionTimeString(String detentionTimeString) {
		this.detentionTimeString = detentionTimeString;
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

	/**
	 * @return the canSaveExitVehicle
	 */
	public boolean isCanSaveExitVehicle() {
		return canSaveExitVehicle;
	}

	/**
	 * @param canSaveExitVehicle the canSaveExitVehicle to set
	 */
	public void setCanSaveExitVehicle(boolean canSaveExitVehicle) {
		this.canSaveExitVehicle = canSaveExitVehicle;
	}

	/**
	 * @return the manualExit
	 */
	public boolean isManualExit() {
		return manualExit;
	}

	/**
	 * @param manualExit the manualExit to set
	 */
	public void setManualExit(boolean manualExit) {
		this.manualExit = manualExit;
	}

	/**
	 * @return the hasRoleUCOT
	 */
	public boolean isHasRoleUCOT() {
		return hasRoleUCOT;
	}

	/**
	 * @param hasRoleUCOT the hasRoleUCOT to set
	 */
	public void setHasRoleUCOT(boolean hasRoleUCOT) {
		this.hasRoleUCOT = hasRoleUCOT;
	}

	/**
	 * @return the hasRoleCRV
	 */
	public boolean getHasRoleCRV() {
		return hasRoleCRV;
	}

	/**
	 * @param hasRoleCRV the hasRoleCRV to set
	 */
	public void setHasRoleCRV(boolean hasRoleCRV) {
		this.hasRoleCRV = hasRoleCRV;
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
			ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV = new ArrivalHistoryBinnacleCRV();
			arrivalHistoryBinnacleCRV.setAdmissionType(instance.getAdmissionType());
			arrivalHistoryBinnacleCRV.setArrivalDate(instance.getAdmissionDate());
			arrivalHistoryBinnacleCRV.setHasCraneService(instance.getBinnacleCRVCrane().getGenerateTaxes());
			arrivalHistoryBinnacleCRV.setUserArrival(userSession.getUser());
			arrivalHistoryBinnacleCRV.setKm(instance.getKm());
			arrivalHistoryBinnacleCRV.setBinnacleCRV(instance);
			instance.add(arrivalHistoryBinnacleCRV);
		}
		if (hasJudicialDocument) {
			if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.INFORMATIVE_PART){
				instance.setInformativePart(informativePart);
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ADMINISTRATIVE_PART){
				instance.setInformativePart(informativePart);
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ACCIDENT_PART){
				instance.setAccidentPart(accidentPart);
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.JUDICIAL_PART){
				instance.setBallotPart(ballotPart);
			}
			chargeFile();
		}
		return super.persist();
	}

	private void chargeFile(){
		if (isNewFileNameOfJudicialPart){
			if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			String dirBase = systemParameterService.findParameter(SystemParameterService.PATH_FILES_BINNACLE_CRV);
			if (byteNameOfJudicialPart != null){
				String fileName = dirBase + File.separator + DateUtils.formatDate(instance.getAdmissionDate());
				File f = new File(fileName);
				if ((!f.exists()) || (!f.isDirectory()))
					f.mkdir();
				fileName = fileName + File.separator + instance.getId() + ".pdf";
				saveFile(fileName, byteNameOfJudicialPart);
			}
		}
	}
	
	private void saveFile(String fileName, byte[] file){
        BufferedOutputStream bos = null;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos); 
            bos.write(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bos != null) {
                try  {
                    bos.flush();
                    bos.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
	}
	
	private void saveFile(String fileName, File file){
        BufferedOutputStream bos = null;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
            byte[] b = getBytesFromFile(file);
            bos = new BufferedOutputStream(fos); 
            bos.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bos != null) {
                try  {
                    bos.flush();
                    bos.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
	}
	
    public byte[] getBytesFromFile(File file) throws IOException {
        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
			facesMessages.add("El archivo es demasiado grande para procesar.");
            throw new IOException("El archivo es demasiado grande para procesar.");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
			facesMessages.add("No se pudo leer el archivo completamente. Archivo " + file.getName());
            throw new IOException("Could not completely read file " + file.getName());
        }
        return bytes;
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
		ballotPart = null;
		hasJudicialDocument = false;
		admissionTimeString = "";
		fileNameOfJudicialPart = null;
		byteNameOfJudicialPart = null;
		isNewFileNameOfJudicialPart = false;
	}
	
	public void editBinnacleCRV() {
		canEditLicensePlate = false;
		if (getInstance().getAdmissionTime() != null)
			admissionTimeString = instance.getAdmissionTime().toString();
		if (!getInstance().isHasJudicialDocument()){
			if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.INFORMATIVE_PART){
				informativePart = new InformativePart();
				partTimeString = "";
				retentionTimeString = "";
				detentionTimeString = "";
				accidentTimeString = "";
				notificationTimeString = "";
				arrivalTimeString = "";
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ADMINISTRATIVE_PART){
				informativePart = new InformativePart();
				partTimeString = "";
				retentionTimeString = "";
				detentionTimeString = "";
				accidentTimeString = "";
				notificationTimeString = "";
				arrivalTimeString = "";
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ACCIDENT_PART){
				accidentPart = new AccidentPart();
				partTimeString = "";
				retentionTimeString = "";
				detentionTimeString = "";
				accidentTimeString = "";
				notificationTimeString = "";
				arrivalTimeString = "";
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.JUDICIAL_PART){
				ballotPart = new BallotPart();
				partTimeString = "";
				retentionTimeString = "";
				detentionTimeString = "";
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
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.ADMINISTRATIVE_PART){
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
			} else if (instance.getDocumentTypeBinnacleCRV() == DocumentTypeBinnacleCRV.JUDICIAL_PART){
				ballotPart = instance.getBallotPart();
				partTimeString = ballotPart.getPartTime().toString();
				retentionTimeString = ballotPart.getRetentionTime().toString();
				detentionTimeString = ballotPart.getDetentionTime().toString();
				notificationTimeString = ballotPart.getNotificationTime().toString();
			}
		}
		hasJudicialDocument = instance.isHasJudicialDocument();
		isNewFileNameOfJudicialPart = false;
		fileNameOfJudicialPart = null;
		byteNameOfJudicialPart = null;
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

	public void addImplicatedBallotPart() {
		ballotPart.add(implicated);
	}
	
	public void removeImplicatedBallotPart(Implicated implicated) {
		if (implicated != null)
			ballotPart.remove(implicated);
	}

	public void createObs(int tipo) {
		this.obsBinnacleCRV = new ObsBinnacleCRV();
		obsBinnacleCRV.setUser(userSession.getUser());
		obsBinnacleCRV.setObsDate(GregorianCalendar.getInstance().getTime());
		obsBinnacleCRV.setObsTime(GregorianCalendar.getInstance().getTime());
		if (tipo == 1){ //Ingreso o Admission
			obsBinnacleCRV.setObsTypeBinnacleCRV(ObsTypeBinnacleCRV.ADMISSION);
		} else if (tipo == 2){ //Salida
			obsBinnacleCRV.setObsTypeBinnacleCRV(ObsTypeBinnacleCRV.DELIVERY);
		} else if (tipo == 3){ //Parte por Accidente
			obsBinnacleCRV.setObsTypeBinnacleCRV(ObsTypeBinnacleCRV.ACCIDENT_PART);
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
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		InfractionWSV2Service service = new InfractionWSV2Service();
		InfractionWSV2 infractionWSV2 = service.getInfractionWSV2Port();
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		String usernameANT = systemParameterService.findParameter(SystemParameterService.USER_NAME_ANT_SERVICE);
		String passwordANT = systemParameterService.findParameter(SystemParameterService.PASSWORD_ANT_SERVICE);
//        headers.put("username", Collections.singletonList("rgpaladinesc"));
//        headers.put("password", Collections.singletonList("w3fjdi$$*89ML"));
        headers.put("username", Collections.singletonList(usernameANT));
        headers.put("password", Collections.singletonList(passwordANT));
        Map<String, Object> req_ctx = ((BindingProvider)infractionWSV2).getRequestContext();
        //req_ctx.put(BindingProviderProperties.REQUEST_TIMEOUT, 5000);
        //req_ctx.put(BindingProviderProperties.CONNECT_TIMEOUT, 10000);
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        ((BindingProvider)infractionWSV2).getRequestContext().putAll(req_ctx);
        return infractionWSV2;
	}
	
	public void searchVehicle() {
		if (!canEditLicensePlate) return;
		try{
			InfractionWSV2 infractionWSV2 = getInfractionWSV2Instance();
			ResponseVehiculo responseVehiculo = infractionWSV2.consultarVehiculo(this.instance.getLicensePlate());
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
				if (responseVehiculo.getVehicle().getClaseVehiculo().equalsIgnoreCase("G") == true)
					instance.setType("MOTO");
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
	
	public ResponseDatosLicencia searchLicenseAccidentPart(String identification) {
		try{
			InfractionWSV2 infractionWSV2 = getInfractionWSV2Instance();
			
			EntradaPersona datos = new EntradaPersona();
			datos.setIdIdentificacion("CED");
			datos.setIdentificacion(identification);
			ResponseDatosLicencia responseLicense = infractionWSV2.consultarLicencia(datos);
			if (responseLicense.getCode() == 200){
				return responseLicense;
			} else {
				facesMessages.add(responseLicense.getMessage());
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
		int tamanio = 1048576;
		PhotographicEvidence evidence = getInstance().getPhotographicEvidences().get(1);
		UploadItem item = event.getUploadItem();		
		if (item != null && item.getData() != null) {
			logger.info(item.getFileName() + ", size: " + item.getFileSize());
			if (item.getFileSize() > tamanio){
				facesMessages.add("La imagen ingresada es demasiado grande. Máximo permitido " + tamanio + " bytes.");
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
	
	public void fileListener(UploadEvent event) {
		int tamanio = 5767168; //5.5MB
		UploadItem item = event.getUploadItem();		
		if (item != null && item.getData() != null) {
			logger.info(item.getFileName() + ", size: " + item.getFileSize());
			if (item.getFileSize() > tamanio){ 
				facesMessages.add("El archivo ingresado es demasiado grande. Máximo permitido " + tamanio + " bytes.");
				item = null;
			} else {
				if (item.getFileName().length() > 100)
					instance.setNameOfJudicialPart(item.getFileName().substring(0, 100));
				else
					instance.setNameOfJudicialPart(item.getFileName());
				fileNameOfJudicialPart = item.getFile();
				byteNameOfJudicialPart = item.getData();
				isNewFileNameOfJudicialPart = true;
			}
		}
	}

	public void clearFile() {
		instance.setNameOfJudicialPart("");
		fileNameOfJudicialPart = null;
		byteNameOfJudicialPart = null;
		isNewFileNameOfJudicialPart = false;
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
			informativePart = new InformativePart();
			instance.setInformativePart(informativePart);
		} else if (instance.getDocumentTypeBinnacleCRV().name().equalsIgnoreCase(DocumentTypeBinnacleCRV.ACCIDENT_PART.name())){
			accidentPart = new AccidentPart();
			instance.setAccidentPart(accidentPart);
		} else if (instance.getDocumentTypeBinnacleCRV().name().equalsIgnoreCase(DocumentTypeBinnacleCRV.ADMINISTRATIVE_PART.name())){
			instance.setHasJudicialDocument(false);
		} else if (instance.getDocumentTypeBinnacleCRV().name().equalsIgnoreCase(DocumentTypeBinnacleCRV.JUDICIAL_PART.name())){
			instance.setHasJudicialDocument(true);
			ballotPart = new BallotPart();
			instance.setBallotPart(ballotPart);
		} else {
			instance.setHasJudicialDocument(false);
		}
	}
	
	public void validateAdmissionTime(){
		if (admissionTimeString == null) return;
		if (admissionTimeString == "") return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(admissionTimeString);
			instance.setAdmissionTime(date);
			admissionTimeString = sdf.format(date);
		} catch (ParseException e) {
			admissionTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateInformativePartTime(){
		if (partTimeString == null) return;
		if (partTimeString == "") return;
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
		if (retentionTimeString == null) return;
		if (retentionTimeString == "") return;
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
		if (retentionTimeString == null) return;
		if (retentionTimeString == "") return;
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
		if (partTimeString == null) return;
		if (partTimeString == "") return;
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
		if (accidentTimeString == null) return;
		if (accidentTimeString == "") return;
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
		if (notificationTimeString == null) return;
		if (notificationTimeString == "") return;
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
		if (arrivalTimeString == null) return;
		if (arrivalTimeString == "") return;
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
	
	public void validateBallotRetentionTime(){
		if (retentionTimeString == null) return;
		if (retentionTimeString == "") return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(retentionTimeString);
			ballotPart.setRetentionTime(date);
			retentionTimeString = sdf.format(date);
		} catch (ParseException e) {
			retentionTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateBallotDetentionTime(){
		if (detentionTimeString == null) return;
		if (detentionTimeString == "") return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(detentionTimeString);
			ballotPart.setDetentionTime(date);
			detentionTimeString = sdf.format(date);
		} catch (ParseException e) {
			detentionTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateBallotPartTime(){
		if (partTimeString == null) return;
		if (partTimeString == "") return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(partTimeString);
			ballotPart.setPartTime(date);
			partTimeString = sdf.format(date);
		} catch (ParseException e) {
			partTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void validateBallotNotificationTime(){
		if (notificationTimeString == null) return;
		if (notificationTimeString == "") return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = sdf.parse(notificationTimeString);
			ballotPart.setNotificationTime(date);
			notificationTimeString = sdf.format(date);
		} catch (ParseException e) {
			notificationTimeString = "";
			e.printStackTrace();
		}
	}
	
	public void searchImplicated(){
		ResponseDatosLicencia responseLicense = searchLicenseAccidentPart(implicated.getIdentification());
		if (responseLicense == null) {
			implicated.setIdentification("");
			return;
		}
		if (responseLicense.getLicenseData().getResultado().getExito().compareToIgnoreCase("N") == 0) {
			implicated.setIdentification("");
			facesMessages.add(responseLicense.getLicenseData().getResultado().getMensaje());
		} else {
			datosLicencia = responseLicense.getLicenseData();
			fillImplicated(datosLicencia);
			facesMessages.add("Persona encontrada: " + datosLicencia.getNombreCompleto());
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
	
	public boolean canVehicleExit(BinnacleCRV binnacleCRV){
		if (binnacleCRV.getLastArrivalHistoryBinnacleCRV().getExitDate() == null)
			return true;
		return false;
	}
	
	public boolean canVehicleReArrival(BinnacleCRV binnacleCRV){
		if (binnacleCRV.getLastArrivalHistoryBinnacleCRV().getExitTypeBinnacleCRV() == null) return false; 
		if (binnacleCRV.getLastArrivalHistoryBinnacleCRV().getExitTypeBinnacleCRV().equals(ExitTypeBinnacleCRV.PROVISIONAL))
			return true;
		return false;
	}
	
	public void captureBinnacleCRV(){
		if (getInstance().getLastArrivalHistoryBinnacleCRV().getExitDate() != null)
		    arrivalHistoryBinnacleCRV = new ArrivalHistoryBinnacleCRV();
		else
			arrivalHistoryBinnacleCRV = getInstance().getLastArrivalHistoryBinnacleCRV();
	}

	@Override
	@Transactional
	public String update() {
		chargeFile();
		return super.update();
	}
	
	public String updateReArrival() {
		ArrivalHistoryBinnacleCRV lastArrival = instance.getLastArrivalHistoryBinnacleCRV();
		if (lastArrival.getExitDate().after(arrivalHistoryBinnacleCRV.getArrivalDate())){
			facesMessages.add("La fecha de reingreso no puede ser menor a " + lastArrival.getExitDate());
			return null;
		}
		Date today = new GregorianCalendar().getTime();
		if (today.before(arrivalHistoryBinnacleCRV.getArrivalDate())){
			facesMessages.add("La fecha de reingreso no puede ser mayor a " + today);
			return null;
		}
		arrivalHistoryBinnacleCRV.setUserArrival(userSession.getUser());
		arrivalHistoryBinnacleCRV.setBinnacleCRV(instance);
		instance.add(arrivalHistoryBinnacleCRV);
		return this.update();
	}
	
	public String updateExitArrival() {
		Date today = new GregorianCalendar().getTime();
		if (arrivalHistoryBinnacleCRV.getExitDate().after(today)){
			facesMessages.add("La fecha de salida no puede ser posterior a " + today);
			return null;
		}
		arrivalHistoryBinnacleCRV.setUserExit(userSession.getUser());
		return this.update();
	}
	
	public void reviewResident(){
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", getInstance().getOwnerIdentification());
		try {
			resident = (Resident) query.getSingleResult();
		} catch (Exception e) {
			resident = null;
			facesMessages.add("Es necesario ingresar los datos del contribuyente propietario del vehículo para poder continuar.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private BinnacleCRVReference findBinnacleCRVReference(ArrivalHistoryBinnacleCRV arrivalHistoryBinnacleCRV, Long entryId){
		Query query = getEntityManager().createNamedQuery("BinnacleCRVReference.findByArrivalAndEntryId");
		query.setParameter("arrivalHistoryBinnacleCRV", arrivalHistoryBinnacleCRV);
		query.setParameter("entryId", entryId);
		List<BinnacleCRVReference> list = query.getResultList();
		if (list.size() > 0){
			BinnacleCRVReference binnacleCRVReference = list.get(0);
			query = null;
			query = getEntityManager().createNamedQuery("Bond.findByAdjunctId");
			query.setParameter("adjunctId", binnacleCRVReference.getId());
			MunicipalBond bond = (MunicipalBond) query.getSingleResult();
			if ((bond.getMunicipalBondStatus().getId() == 6) || (bond.getMunicipalBondStatus().getId() == 11))
				return binnacleCRVReference;
			else
				return null;
		}
		return null;
	}
	
	public void reviewExitVehicle(){
		canSaveExitVehicle = true;
		boolean isEmmitedGaraje = false;
		boolean isEmmitedCrane = true;
		ArrivalHistoryBinnacleCRV arrival = getInstance().getLastArrivalHistoryBinnacleCRV();
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Long entryGarajeId = systemParameterService.findParameter(SystemParameterService.ENTRY_ID_GARAJE_SERVICE_UMTTT);
		BinnacleCRVReference binnacleCRVReference = findBinnacleCRVReference(arrival, entryGarajeId);
		if (binnacleCRVReference == null){
			if (!manualExit) facesMessages.add("Es necesario emitir y/o cancelar los títulos por el Servicio de Garaje.");
		} else{
			arrival.setExitDate(binnacleCRVReference.getExitDate());
			if (!arrival.isHasCraneService()) arrival.setExitDateCrane(binnacleCRVReference.getExitDate());
			arrival.setExitTypeBinnacleCRV(binnacleCRVReference.getExitTypeBinnacleCRV());
			arrival.setBondGaraje(binnacleCRVReference.getBond());
			isEmmitedGaraje = true;
		}
		if (arrival.isHasCraneService()) {
			Long entryCraneId = systemParameterService.findParameter(SystemParameterService.ENTRY_ID_GRUA_SERVICE_UMTTT);
			binnacleCRVReference = findBinnacleCRVReference(arrival, entryCraneId);
			if (binnacleCRVReference == null){
				if (!manualExit) facesMessages.add("Es necesario emitir y/o cancelar los títulos por el Servicio de Grúa.");
				isEmmitedCrane = false;
			} else {
				arrival.setExitDateCrane(binnacleCRVReference.getExitDate());
				arrival.setBondCrane(binnacleCRVReference.getBond());
			}
		}
		if ((!isEmmitedGaraje) || (!isEmmitedCrane))
			canSaveExitVehicle = false;
		if (manualExit) canSaveExitVehicle = true;
		this.arrivalHistoryBinnacleCRV = arrival;
	}
	
	public void clearArrival(){
		this.arrivalHistoryBinnacleCRV = null;
	}
	
	public Boolean hasRole(String roleKey) {
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String role = systemParameterService.findParameter(roleKey);
		if (role != null) {
			return userSession.getUser().hasRole(role);
		}
		return false;
	}
	
	public void findRoleUCOT(){
		hasRoleUCOT = hasRole(SystemParameterService.ROLE_NAME_UCOT_SYSTEM);
	}
	
	public void findRoleCRV(){
		hasRoleCRV = hasRole(SystemParameterService.ROLE_NAME_CRV_SYSTEM);
	}
	
	public void downloadPart(){
		if (instance.getNameOfJudicialPart().length() == 0){
			return;
		} else {
			if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			String dirBase = systemParameterService.findParameter(SystemParameterService.PATH_FILES_BINNACLE_CRV);
//			String dirBase = "d:\\catastroimg";
			String fileName = dirBase + File.separator + DateUtils.formatDate(instance.getAdmissionDate());
			fileName = fileName + File.separator + instance.getId() + ".pdf";
			File file = new File(fileName);
			downloadFile(file);
		}
	}
	
	public void downloadFile(File file) {   
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	    response.setHeader("Content-Type", "application/pdf");
	    response.setHeader("Content-Disposition", "attachment;filename=\"" + instance.getNameOfJudicialPart() + "\"");
	    response.setContentLength((int) file.length());
	    FileInputStream input= null;
	    try {
	        int i= 0;
	        input = new FileInputStream(file);  
	        byte[] buffer = new byte[1024];
	        while ((i = input.read(buffer)) != -1) {  
	            response.getOutputStream().write(buffer);  
	            response.getOutputStream().flush();  
	        }
	        facesContext.responseComplete();
	        facesContext.renderResponse();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if(input != null) {
	                input.close();
	            }
	        } catch(IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
}