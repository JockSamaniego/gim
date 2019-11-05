package org.gob.gim.income.action;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.income.facade.IncomeService;
//import org.jboss.resteasy.client.core.marshallers.ResteasyClientProxy;
//import org.jboss.resteasy.client.jaxrs.ResteasyClient;
//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
//import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.loxageek.sri.dto.Invoice;
import org.loxageek.sri.xml.XmlAdapter;

//import ec.common.sridocuments.v110.factura.Factura;
//import ec.common.sridocuments.v110.factura.XmlTransform;
import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.income.model.Receipt;

@Name("taxReporter")
@Scope(ScopeType.PAGE)
public class TaxReporter extends EntityController {
	private static final long serialVersionUID = 1L;
	private Date startDate;
	private Date endDate;
	private FinancialStatus finantialStatus = FinancialStatus.VALID;
	private List<Receipt> receipts;
	
	public void findReceipts(){
		IncomeService service = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		startDate = DateUtils.truncate(startDate);
		endDate = DateUtils.expand(endDate);
		receipts = service.findReceipts(startDate, endDate, finantialStatus);
		if(finantialStatus == FinancialStatus.VOID){
			for(Receipt receipt : receipts){
				receipt.setMunicipalBond(receipt.getReversedMunicipalBond());
			}
		}
	}
	
	public void generateXml() throws Exception{
		findReceipts();
		if(receipts != null && !receipts.isEmpty()){
			JAXBContext jaxbContext = JAXBContext.newInstance(Invoice.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
	        String dirName = System.getProperty("user.home")+File.separator+df.format(new Date())+"_"+finantialStatus.name();
	        //System.out.println("Output sent to "+dirName);
	        new File(dirName).mkdirs();
	        for(Receipt receipt : receipts){
	        	String fileName = dirName+File.separator+receipt.toString()+".xml";
	        	//System.out.println("File Saved: "+fileName);
	        	File output = new File(fileName);
	        	Invoice invoice = XmlAdapter.transform(receipt);
	        	marshaller.marshal(invoice, output);
	        }
		}
		
//        ResteasyClient client = new ResteasyClientBuilder().build();
//        ResteasyWebTarget target = client.target("http://190.57.168.203:9090/electronicInvoice-service/webresources/ec.gob.loja.service.company/find/2");
//        Response response = target.request().get();
//        //fe61d00ea74727e366f6acb82c82af070d8e5b848929c512582e783d3a7837c4
//        
//        Company company = response.readEntity(Company.class); 
//        System.out.println("<<<R>>> Company: "+company.getCompanyName());
//
//		ClientResponse<String> response = clientRequest.get(String.class);
//		
//		if(receipts != null && !receipts.isEmpty()){
//			JAXBContext jaxbContext = JAXBContext.newInstance(Factura.class);
//			Marshaller marshaller = jaxbContext.createMarshaller();
//			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//			marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//	        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
//	        String dirName = System.getProperty("user.home")+File.separator+df.format(new Date())+"_"+finantialStatus.name();
//	        System.out.println("Output sent to "+dirName);
//	        new File(dirName).mkdirs();
//	        
//	        for(Receipt receipt : receipts){
//	        	String fileName = dirName+File.separator+receipt.toString()+".xml";
//	        	System.out.println("File Saved: "+fileName);
//	        	File output = new File(fileName);
//	        	Factura factura = XmlTransform.transform(receipt);
//	        	marshaller.marshal(factura, output);
//	        	
//	        }
//		}
	}
	
	public List<FinancialStatus> getFinancialStatuses(){
		return Arrays.asList(FinancialStatus.values());
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<Receipt> getReceipts() {
		return receipts;
	}
	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}

	public FinancialStatus getFinantialStatus() {
		return finantialStatus;
	}

	public void setFinantialStatus(FinancialStatus finantialStatus) {
		this.finantialStatus = finantialStatus;
	}
}
