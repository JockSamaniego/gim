/**
 * 
 */
package org.gob.gim.income.facade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.gob.gim.common.service.SystemParameterService;

import ec.common.sridocuments.v110.factura.Factura;
import ec.common.sridocuments.v110.factura.XmlTransform;
import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.loja.client.clients.ElectronicClient;
import ec.gob.loja.client.model.DataWS;
import ec.gob.loja.client.utility.FileUtilities;

/**
 * 
 * @author RonaldPC
 *
 */
@Stateless(name="ElectronicService")
public class ElectronicServiceBean implements ElectronicService {
	
	@EJB
	SystemParameterService systemParameterService;
	
	@PersistenceContext
	EntityManager entityManager;
	
	private Map<Long, Branch> mapBranch = new HashMap<Long, Branch>();
	
	private DataWS sendElectronicReceipt(Receipt receipt) throws Exception{
		String sriEnvironment = systemParameterService.findParameter(ELECTRONIC_INVOICE_ENVIRONMENT);
		JAXBContext jaxbContext = JAXBContext.newInstance(Factura.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String xml_dir = systemParameterService.findParameter(ELECTRONIC_INVOICE_XML_DIR);
        String dirName = System.getProperty("user.home")+File.separator+xml_dir;
//        System.out.println("Output sent to "+dirName);
        new File(dirName).mkdirs();
        
    	String xmlFileName = dirName+File.separator+receipt.toString()+".xml";
//    	System.out.println("File Saved: "+xmlFileName);
    	File output = new File(xmlFileName);
    	Factura factura = XmlTransform.transform(receipt, sriEnvironment, mapBranch);
    	marshaller.marshal(factura, output);
		
        byte[] document;
        DataWS dataWS = new DataWS();
        try {
            document = FileUtilities.convertir_file_to_ByteArray(output);
            String checksum = FileUtilities.checkSumApacheCommons(xmlFileName);
            dataWS.setXmlFile(document);
            dataWS.setRucCompany(receipt.getMunicipalBond().getInstitution().getNumber());
            //System.out.println("cliente checksum ");
            dataWS.setCheksum(checksum);
            //System.out.println("enviar al servicio del SRI");
            dataWS = sendToSRIService(dataWS);
//            new File(xmlFileName).delete();
        } catch (IOException ex) {
            Logger.getLogger(IncomeServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    	return dataWS;
	}
	
	private DataWS sendToSRIService(DataWS input) {
		String uriElectronicService = systemParameterService.findParameter(ELECTRONIC_INVOICE_BASE_URI);
	    ElectronicClient client = new ElectronicClient(uriElectronicService);
	    DataWS response;
	    response = client.reception_XML(input, DataWS.class);
	    /*System.out.println("Access key " + response.getAccesKey() + " Autorizado en SRI: " + response.getAuthorized()
	            + " Ambiente Contingencia: " + response.getContingencyEnvironment());*/
	    return response;
	}

	private DataWS sendConsultElectronicReceipt(Receipt receipt) throws Exception{
        DataWS dataWS = new DataWS();
        dataWS.setRucCompany(receipt.getMunicipalBond().getInstitution().getNumber());
        dataWS.setVoucherNumber(receipt.getReceiptNumber());
        //System.out.println("enviar consulta al servicio de Facturacion Electronica");
        dataWS = sendConsultToSRIService(dataWS);
    	return dataWS;
	}

	private DataWS sendConsultToSRIService(DataWS input) {
		String uriElectronicService = systemParameterService.findParameter(ELECTRONIC_INVOICE_BASE_URI);
	    ElectronicClient client = new ElectronicClient(uriElectronicService);
	    DataWS response;
	    response = client.consultingVoucher_XML(input, DataWS.class);
	    /*System.out.println("Access key " + response.getAccesKey() + " Autorizado en SRI: " + response.getAuthorized()
	            + " Ambiente Contingencia: " + response.getContingencyEnvironment());*/
	    return response;
	}

	@SuppressWarnings("unchecked")
	public List<Receipt> findReceipts(Date beginDate, Date endDate, StatusElectronicReceipt statusElectronicReceipt, int limitRecords) {
		Query query = entityManager.createNamedQuery("Receipt.findBetweenDatesAndStatusElectronicReceipt");
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		cal.add(Calendar.SECOND, 1);
		//System.out.println("<<<R>>> Start Date: "+cal.getTime());
		query.setParameter("startDate", cal.getTime());
		cal.setTime(endDate);
		cal.add(Calendar.HOUR, 24);
		cal.add(Calendar.SECOND, -1);
		//System.out.println("<<<R>>>   End Date: "+cal.getTime());
		query.setParameter("endDate", cal.getTime());
//		query.setParameter("startDate", beginDate);
//		query.setParameter("endDate", endDate);
		query.setParameter("statusElectronicReceipt", statusElectronicReceipt);
		if (limitRecords > 0) query.setMaxResults(limitRecords);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public boolean authorizedElectronicReceipts(List<Receipt> receipts){
		//System.out.println("\n\n\n\n\n<<<R>>> Hora inicio: "+GregorianCalendar.getInstance().getTime());
		List<Receipt> receiptsFull = new ArrayList<Receipt>();
		List<Long> ids = new ArrayList<Long>();
		int i = 0;
		for (Receipt r : receipts) {
//			i++;
//			System.out.println("<<<R>>> receipt add "+ i +" id: "+r.getId());
			ids.add(r.getId());
		}
		Query query = entityManager.createNamedQuery("Receipt.findFullByIds");
		query.setParameter("receiptIds", ids);
		receiptsFull = query.getResultList();
		i = 0;
		for (Receipt receipt : receiptsFull) {
			i++;
			//System.out.println("\n\n<<<R>>> receipt "+ i +" id: "+receipt.getId());
			authorizedElectronicReceipt(receipt);
		}
		//System.out.println("\n\n\n\n\n<<<R>>> Hora final: "+GregorianCalendar.getInstance().getTime());
		return true;
	}

	@Override
	public boolean authorizedElectronicReceipt(Receipt receipt) {
		if (mapBranch.isEmpty()) fillMapBranch();
		StatusElectronicReceipt statusElectronicReceipt;
		if ((receipt.getStatusElectronicReceipt().name().equals(StatusElectronicReceipt.PENDING.name()))
				|| (receipt.getStatusElectronicReceipt().name().equals(StatusElectronicReceipt.CONTINGENCY.name()))){
			//System.out.println("<<<R>>> StatusElectronicReceipt: "+receipt.getStatusElectronicReceipt().name());
			try {
				DataWS signReceipt = sendElectronicReceipt(receipt);
				if ((signReceipt.getAuthorized() != null) && (signReceipt.getAuthorized())){
					//System.out.println("<<<R>>> SriAuthorizationNumber: "+signReceipt.getAuthorizationNumber());
					statusElectronicReceipt = StatusElectronicReceipt.AUTHORIZED;
					updateReceipt(receipt, signReceipt.getAccesKey(), signReceipt.getAuthorizationNumber(), 
							signReceipt.getAuthorizationDate(), signReceipt.getContingencyEnvironment(), statusElectronicReceipt);
				}
				else{
					if ((signReceipt.getContingencyEnvironment() != null) && (signReceipt.getContingencyEnvironment())){
						//System.out.println("<<<R>>> En Contingencia: "+signReceipt.getAccesKey());
						statusElectronicReceipt = StatusElectronicReceipt.CONTINGENCY;
						updateReceipt(receipt, signReceipt.getAccesKey(), "", 
								receipt.getDate(), signReceipt.getContingencyEnvironment(), statusElectronicReceipt);
					} else{
						if (signReceipt.getReceived()){
							//System.out.println("<<<R>>> SOLO RECEPCION: "+signReceipt.getReceived());
							statusElectronicReceipt = StatusElectronicReceipt.RECEPTION;
							updateReceipt(receipt, receipt.getSriAccessKey(), receipt.getAuthorizationNumber(), 
									receipt.getDate(), receipt.isSriContingencyEnvironment(), statusElectronicReceipt);
						} else {
							//System.out.println("<<<R>>> ERROR: "+signReceipt.getMessageList().toString());
							statusElectronicReceipt = StatusElectronicReceipt.ERROR;
							updateReceipt(receipt, receipt.getSriAccessKey(), receipt.getAuthorizationNumber(), 
									receipt.getDate(), receipt.isSriContingencyEnvironment(), statusElectronicReceipt);
						}
					}
				}
			} catch (Exception e) {
				//System.out.println(e);
				return false;
			}
		}
		return true;
	}
	
	public void updateReceipt(Receipt receipt, String sriAccessKey, String authorizationNumber, Date sriAuthorizationDate, 
			boolean sriContingencyEnvironment, StatusElectronicReceipt statusElectronicReceipt){
		Query query = entityManager.createNamedQuery("Receipt.updateElectronicReceipt");
		query.setParameter("sriAccessKey", sriAccessKey);
		query.setParameter("authorizationNumber", authorizationNumber);
		query.setParameter("sriAuthorizationDate", sriAuthorizationDate);
		query.setParameter("sriContingencyEnvironment", sriContingencyEnvironment);
		query.setParameter("statusElectronicReceipt", statusElectronicReceipt);
		query.setParameter("receiptId", receipt.getId());
		query.executeUpdate();
	}

	public boolean consultElectronicReceipts(List<Receipt> receipts){
		//System.out.println("\n\n\n\n\n<<<R>>> Hora inicio: "+GregorianCalendar.getInstance().getTime());
		int i = 0;
		for (Receipt receipt : receipts) {
			i++;
			//System.out.println("\n\n<<<R>>> receipt "+ i +" id: "+receipt.getId());
			consultElectronicReceipt(receipt);
		}
		//System.out.println("\n\n\n\n\n<<<R>>> Hora final: "+GregorianCalendar.getInstance().getTime());
		return true;
	}

	@SuppressWarnings("unchecked")
	public void fillMapBranch(){
//		System.out.println("\n<<<R>>> branch: ");
		mapBranch.clear();
		//System.out.println("\n<<<R>>> branch: " + entityManager);
		Query query = entityManager.createNamedQuery("Branch.findAll");
		List<Branch> list = new ArrayList<Branch>();
		list = query.getResultList();
		for (Branch branch : list) {
//			System.out.println("\n<<<R>>> branch: " + branch.getId());
			mapBranch.put(branch.getId(), branch);
		}
	}
	
	public boolean consultElectronicReceipt(Receipt receipt) {
		if (mapBranch.isEmpty()) fillMapBranch();
		StatusElectronicReceipt statusElectronicReceipt;
		if ((receipt.getStatusElectronicReceipt().name().equals(StatusElectronicReceipt.RECEPTION.name()))
				|| (receipt.getStatusElectronicReceipt().name().equals(StatusElectronicReceipt.CONTINGENCY.name()))){
			//System.out.println("<<<R>>> StatusElectronicReceipt: "+receipt.getStatusElectronicReceipt().name());
			try {
				DataWS signReceipt = sendConsultElectronicReceipt(receipt);
				if ((signReceipt.getAuthorized() != null) && (signReceipt.getAuthorized())){
					//System.out.println("<<<R>>> SriAuthorizationNumber: "+signReceipt.getAuthorizationNumber());
					statusElectronicReceipt = StatusElectronicReceipt.AUTHORIZED;
					updateReceipt(receipt, signReceipt.getAccesKey(), signReceipt.getAuthorizationNumber(), 
							signReceipt.getAuthorizationDate(), signReceipt.getContingencyEnvironment(), statusElectronicReceipt);
				}
				else{
					if ((signReceipt.getContingencyEnvironment() != null) && (signReceipt.getContingencyEnvironment())){
						//System.out.println("<<<R>>> En Contingencia: "+signReceipt.getAccesKey());
						statusElectronicReceipt = StatusElectronicReceipt.CONTINGENCY;
						updateReceipt(receipt, signReceipt.getAccesKey(), "", 
								receipt.getDate(), signReceipt.getContingencyEnvironment(), statusElectronicReceipt);
					} else{
						if (signReceipt.getReceived()){
							//System.out.println("<<<R>>> SOLO RECEPCION: "+signReceipt.getReceived());
							statusElectronicReceipt = StatusElectronicReceipt.RECEPTION;
							updateReceipt(receipt, receipt.getSriAccessKey(), receipt.getAuthorizationNumber(), 
									receipt.getDate(), receipt.isSriContingencyEnvironment(), statusElectronicReceipt);
						} else {
							//System.out.println("<<<R>>> ERROR: "+signReceipt.getMessageList().toString());
							statusElectronicReceipt = StatusElectronicReceipt.ERROR;
							updateReceipt(receipt, receipt.getSriAccessKey(), receipt.getAuthorizationNumber(), 
									receipt.getDate(), receipt.isSriContingencyEnvironment(), statusElectronicReceipt);
						}
					}
				}
			} catch (Exception e) {
				//System.out.println(e);
				return false;
			}
		}
		return true;
	}

	public Map<Long, Branch> getMapBranch() {
		return mapBranch;
	}

	public void setMapBranch(Map<Long, Branch> mapBranch) {
		this.mapBranch = mapBranch;
	}

//	public DataWS consultElectronicReceipt(Receipt receipt) {
//		DataWS signReceipt = null;
//		Query query = entityManager.createNamedQuery("Receipt.findById");
//		query.setParameter("receiptId", receipt.getId());
//		Receipt rec = (Receipt)query.getSingleResult();
//		query = null;
//		if (rec.getStatusElectronicReceipt().name().equals(StatusElectronicReceipt.NONE.name())){
//			System.out.println("<<<R>>> StatusElectronicReceipt: "+rec.getStatusElectronicReceipt().name());
//			try {
//				signReceipt = sendConsultElectronicReceipt(rec);
//				if ((signReceipt.getAuthorized() != null) && (signReceipt.getAuthorized())){
//					System.out.println("<<<R>>> SriAuthorizationNumber: "+signReceipt.getAuthorizationNumber());
//					receipt.setSriAccessKey(signReceipt.getAccesKey());
//					receipt.setAuthorizationNumber(signReceipt.getAuthorizationNumber());
//					receipt.setSriAuthorizationDate(signReceipt.getAuthorizationDate());
//					receipt.setSriContingencyEnvironment(signReceipt.getContingencyEnvironment());
//					receipt.setStatusElectronicReceipt(StatusElectronicReceipt.AUTHORIZED);
//					entityManager.merge(receipt);
//					entityManager.flush();
//				}
//				else{
//					if ((signReceipt.getContingencyEnvironment() != null) && (signReceipt.getContingencyEnvironment())){
//						System.out.println("<<<R>>> En Contingencia: "+signReceipt.getAccesKey());
//						receipt.setAuthorizationNumber("");
//						receipt.setSriAccessKey(signReceipt.getAccesKey());
//						receipt.setStatusElectronicReceipt(StatusElectronicReceipt.CONTINGENCY);
//						entityManager.merge(receipt);
//						entityManager.flush();
//					} else{
//						if (signReceipt.getReceived()){
//							System.out.println("<<<R>>> SOLO RECEPCION: "+signReceipt.getReceived());
//							receipt.setStatusElectronicReceipt(StatusElectronicReceipt.RECEPTION);
//							entityManager.merge(receipt);
//							entityManager.flush();
//						} else {
//							System.out.println("<<<R>>> ERROR: "+signReceipt.getMessageList().toString());
//							receipt.setStatusElectronicReceipt(StatusElectronicReceipt.ERROR);
//							entityManager.merge(receipt);
//							entityManager.flush();
//						}
//					}
//				}
//			} catch (Exception e) {
//				System.out.println(e);
//				return null;
//			}
//		}
//		return signReceipt;
//	}

}