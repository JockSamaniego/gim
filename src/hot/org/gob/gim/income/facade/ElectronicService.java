package org.gob.gim.income.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.loja.client.model.DataWS;

@Local
public interface ElectronicService {
	public String LOCAL_NAME = "/gim/ElectronicService/local";
	
	public final String ENABLE_RECEIPT_GENERATION = "ENABLE_RECEIPT_GENERATION";
	public final String ELECTRONIC_INVOICE_ENABLE = "ELECTRONIC_INVOICE_ENABLE";
	public final String ELECTRONIC_INVOICE_BASE_URI = "ELECTRONIC_INVOICE_BASE_URI";
	public final String ELECTRONIC_INVOICE_XML_DIR = "ELECTRONIC_INVOICE_XML_DIR";
	public final String ELECTRONIC_INVOICE_ENVIRONMENT = "ELECTRONIC_INVOICE_ENVIRONMENT";
	
	public List<Receipt> findReceipts(Date beginDate, Date endDate, StatusElectronicReceipt statusElectronicReceipt, int limitRecords);
	public boolean authorizedElectronicReceipts(List<Receipt> receipts);
	public boolean authorizedElectronicReceipt(Receipt receipt);
	public boolean consultElectronicReceipts(List<Receipt> receipts);
	public boolean consultElectronicReceipt(Receipt receipt);
}
