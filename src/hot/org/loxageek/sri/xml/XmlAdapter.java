package org.loxageek.sri.xml;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.loxageek.sri.dto.CampoAdicional;
import org.loxageek.sri.dto.Detalle;
import org.loxageek.sri.dto.InfoAdicional;
import org.loxageek.sri.dto.InformacionTributaria;
import org.loxageek.sri.dto.Invoice;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.ReceiptAuthorization;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.adjunct.ValuePair;

public class XmlAdapter{

	public static Invoice transform(Receipt receipt){
		System.out.println(">>>>Invoice Transform: ");
		MunicipalBond municipalBond = receipt.getMunicipalBond();
		Resident resident = municipalBond.getResident();
		ReceiptAuthorization receiptAuthorization = receipt.getReceiptAuthorization();
		TaxpayerRecord taxpayerRecord = receiptAuthorization.getTaxpayerRecord();
		
		Invoice invoice = new Invoice();
		
		InformacionTributaria infoTributaria = invoice.getInfoTributaria();
		
		infoTributaria.setRazonSocial(taxpayerRecord.getName());
		infoTributaria.setRuc(new Long(taxpayerRecord.getNumber()));
		infoTributaria.setNumAut(new Integer(receiptAuthorization.getAuthorizationNumber()));
		infoTributaria.setCodDoc(receipt.getReceiptType().getCode());
		
		infoTributaria.setEstab(receipt.getBranchLabel());
		infoTributaria.setPtoEmi(receipt.getTillLabel());		
		infoTributaria.setSecuencial(receipt.getNumberLabel());
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		infoTributaria.setFechaAutorizacion(df.format(receiptAuthorization.getStartDate()));
		infoTributaria.setCaducidad(df.format(receiptAuthorization.getEndDate()));
		infoTributaria.setFechaEmision(df.format(receipt.getDate()));		
		infoTributaria.setDirMatriz(taxpayerRecord.getAddress());
		infoTributaria.setRazonSocialComprador(resident.getName());
		infoTributaria.setRucCedulaComprador(resident.getIdentificationNumber());
		
		infoTributaria.setContribuyenteEspecial(taxpayerRecord.getSpecialTaxpayerResolution());
		infoTributaria.setTotalSinImpuestos(municipalBond.getSubTotal());
		infoTributaria.setTotalConImpuestos(municipalBond.getPaidTotal());
		infoTributaria.setBaseNoObjetoIVA(municipalBond.getNonTaxableTotal());
		
		System.out.println("municipalBond.getTaxableTotal().compareTo(BigDecimal.ZERO)"+municipalBond.getTaxableTotal().compareTo(BigDecimal.ZERO));
		System.out.println("municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO)"+municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO));
		
		if(municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO) > 0){
			infoTributaria.setBaseIVA0(null);
			infoTributaria.setBaseIVA12(municipalBond.getTaxableTotal());
			infoTributaria.setIVA12(municipalBond.getTaxesTotal());
		} else {
			infoTributaria.setBaseIVA0(municipalBond.getTaxableTotal());
			infoTributaria.setBaseIVA12(null);
			infoTributaria.setIVA12(null);
		}		
		
		for(Item item : municipalBond.getItems()){
			Detalle detalle = new Detalle();
			detalle.setConcepto(item.getEntry().getName());
			detalle.setCantidad(item.getAmount());
			detalle.setPrecioUnitario(item.getValue());
			detalle.setDescuentos(BigDecimal.ZERO);
			detalle.setPrecioTotal(item.getTotal());
			invoice.add(detalle);
		}

		InfoAdicional infoAdicional = invoice.getInfoAdicional();

		List<CampoAdicional> camposAdicionales = new ArrayList<CampoAdicional>();
		MunicipalBond bond;
		bond = receipt.getMunicipalBond();
		System.out.println(">>>>>>>>>>>>>>>>>Bond: "+bond);
		if (bond.getAdjunct() != null){
			System.out.println(">>>>>>>>>>>>>>>>>Bond Adjunct: "+bond.getAdjunct());
			for (ValuePair valuePair : municipalBond.getAdjunct().getDetails()){
				camposAdicionales.add(infoAdicional.createCampoAdicional(valuePair.getLabel(), valuePair.getValue()));
			}
		}

		if (camposAdicionales.size() > 0) infoAdicional.setCampoAdicional(camposAdicionales);
		
		return invoice;
	}
	
}
