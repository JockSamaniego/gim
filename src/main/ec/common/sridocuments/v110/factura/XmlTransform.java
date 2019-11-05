package ec.common.sridocuments.v110.factura;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import org.gob.gim.common.service.SystemParameterService;

import ec.common.sridocuments.v110.factura.Factura.Detalles;
import ec.common.sridocuments.v110.factura.Factura.InfoAdicional;
import ec.common.sridocuments.v110.factura.Factura.InfoFactura;
import ec.common.sridocuments.v110.factura.Factura.Detalles.Detalle;
import ec.common.sridocuments.v110.factura.Factura.Detalles.Detalle.Impuestos;
import ec.common.sridocuments.v110.factura.Factura.InfoAdicional.CampoAdicional;
import ec.common.sridocuments.v110.factura.Factura.InfoFactura.TotalConImpuestos;
import ec.common.sridocuments.v110.factura.Factura.InfoFactura.TotalConImpuestos.TotalImpuesto;
import ec.gob.gim.appraisal.model.AppraisalRossHeidecke;
import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.common.model.SystemParameter;
import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.ReceiptAuthorization;
import ec.gob.gim.income.model.ReceiptType;
import ec.gob.gim.income.model.TaxRate;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.adjunct.ValuePair;

public class XmlTransform{
	public static String CFO="";
	
	public static Factura transform(Receipt receipt, String sriEnvironment, Map<Long, Branch> mapBranch){
		//System.out.println("<<<R>>>XML Transform:"+receipt.getReceiptNumber());
		Branch branch = new Branch();
		for (Branch b : mapBranch.values()) {
//			System.out.println("Branch: " + b.getId()+ " "+b.getAddress());
			if (b.getId().equals(receipt.getBranch().longValue())){
				branch = b;
				break;
			}
		}
		MunicipalBond municipalBond = receipt.getMunicipalBond();
		Resident resident = municipalBond.getResident();
		ReceiptAuthorization receiptAuthorization = receipt.getReceiptAuthorization();
		ReceiptType receiptType = receipt.getReceiptType();
		TaxpayerRecord taxpayerRecord = receiptAuthorization.getTaxpayerRecord();
		
		Factura factura = new Factura();
		factura.setId("comprobante");
		factura.setVersion("1.1.0");
		
//		System.out.println("<<<R>>>XML Transform PASO 1:");
		InfoTributaria infoTributaria = factura.getInfoTributaria();
		infoTributaria.setAmbiente(sriEnvironment); //1 PRUEBAS - 2 PRODUCCION
		infoTributaria.setTipoEmision("1");
		infoTributaria.setRazonSocial(taxpayerRecord.getName());
		infoTributaria.setNombreComercial(taxpayerRecord.getName());
		infoTributaria.setDirMatriz(taxpayerRecord.getAddress());
		infoTributaria.setRuc(taxpayerRecord.getNumber());
		infoTributaria.setClaveAcceso(receipt.getSriAccessKey());
		infoTributaria.setCodDoc(receiptType.getSriCode());
		infoTributaria.setEstab(receipt.getBranchLabel());
		infoTributaria.setPtoEmi(receipt.getTillLabel());
		infoTributaria.setSecuencial(receipt.getNumberLabel());
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
//		System.out.println("<<<R>>>XML Transform PASO 2:");
		InfoFactura infoFactura = factura.getInfoFactura();
		infoFactura.setObligadoContabilidad("SI");
		infoFactura.setFechaEmision(df.format(receipt.getDate()));
//		infoFactura.setContribuyenteEspecial(taxpayerRecord.getSpecialTaxpayerResolution());
		infoFactura.setContribuyenteEspecial("0590");
		infoFactura.setDirEstablecimiento(branch.getAddress());
		infoFactura.setImporteTotal(municipalBond.getPaidTotal());
//		System.out.println("municipalBond.getPaidTotal()"+municipalBond.getPaidTotal());
//		System.out.println("municipalBond.getTaxableTotal()"+municipalBond.getTaxableTotal());
		infoFactura.setPropina(BigDecimal.ZERO);
		infoFactura.setTotalDescuento(BigDecimal.ZERO);
		infoFactura.setMoneda("DOLAR");
		
//		System.out.println("<<<R>>>XML Transform PASO 3:");
		infoFactura.setIdentificacionComprador(resident.getIdentificationNumber());

		if(resident.getCurrentAddress()!=null){
			infoFactura.setDireccionComprador(resident.getCurrentAddress().getStreet());
		}else{
			infoFactura.setDireccionComprador("S/N");
		}
        
//		System.out.println("<<<R>>>XML Transform PASO 3.1:");
//		System.out.println("<<<R>>>XML Transform PASO 3.2:");
		infoFactura.setTotalSinImpuestos(municipalBond.getSubTotal().add(municipalBond.getInterest()));
//		System.out.println("<<<R>>>XML Transform PASO 3.3:");
		if (resident.getIdentificationType().name().equals(IdentificationType.TAXPAYER_DOCUMENT.name()) == true)
			infoFactura.setTipoIdentificacionComprador("04");
		if (resident.getIdentificationType().name().equals(IdentificationType.NATIONAL_IDENTITY_DOCUMENT.name()) == true)
			infoFactura.setTipoIdentificacionComprador("05");
		if (resident.getIdentificationType().name().equals(IdentificationType.PASSPORT.name()) == true)
			infoFactura.setTipoIdentificacionComprador("06");
		if ((resident.getIdentificationNumber() == null) || 
				(resident.getIdentificationType().name().equals(IdentificationType.GENERATED_NUMBER.name()) == true) || 
				(resident.getIdentificationNumber().equals("9999999999999") == true) || 
				(resident.getIdentificationNumber().equals("9999999999") == true)){
			infoFactura.setTipoIdentificacionComprador("07");
			infoFactura.setIdentificacionComprador("9999999999999");
			infoFactura.setRazonSocialComprador("CONSUMIDOR FINAL");
		} else {
//			System.out.println("<<<R>>>XML Transform PASO 3.5:");
			infoFactura.setRazonSocialComprador(resident.getName());
		}
		
//		System.out.println("<<<R>>>XML Transform PASO 4:");
		TotalConImpuestos totalConImpuestos = new TotalConImpuestos();
		totalConImpuestos.totalImpuesto = new ArrayList<Factura.InfoFactura.TotalConImpuestos.TotalImpuesto>();
		if (municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO) == 0){ //IVA 0
			TotalImpuesto totalImpuesto = new TotalImpuesto();
			totalImpuesto.setCodigo("2");
			totalImpuesto.setCodigoPorcentaje("0");
//			System.out.println("municipalBond.getId()"+municipalBond.getId());
//			System.out.println("municipalBond.getTaxableTotal()"+municipalBond.getTaxableTotal());
			totalImpuesto.setBaseImponible(municipalBond.getTaxableTotal());
			totalImpuesto.setDescuentoAdicional(BigDecimal.ZERO);
			totalImpuesto.setTarifa(BigDecimal.ZERO);
			totalImpuesto.setValor(BigDecimal.ZERO);
			totalConImpuestos.totalImpuesto.add(totalImpuesto);
			
			totalImpuesto = new TotalImpuesto();
			totalImpuesto.setCodigo("2");
			totalImpuesto.setCodigoPorcentaje("6");
			totalImpuesto.setBaseImponible(municipalBond.getNonTaxableTotal().add(municipalBond.getInterest()).add(municipalBond.getSurcharge()));
			totalImpuesto.setDescuentoAdicional(BigDecimal.ZERO);
			totalImpuesto.setTarifa(BigDecimal.ZERO);
			totalImpuesto.setValor(BigDecimal.ZERO);
			totalConImpuestos.totalImpuesto.add(totalImpuesto);

		} else { // IVA 12
			TotalImpuesto totalImpuesto = new TotalImpuesto();
			totalImpuesto.setCodigo("2");
			totalImpuesto.setCodigoPorcentaje("2");
			totalImpuesto.setBaseImponible(municipalBond.getTaxableTotal());
			totalImpuesto.setTarifa(new BigDecimal(12.00));
			totalImpuesto.setValor(municipalBond.getTaxesTotal());
			totalConImpuestos.totalImpuesto.add(totalImpuesto);
			
			totalImpuesto = new TotalImpuesto();
			totalImpuesto.setCodigo("2");
			totalImpuesto.setCodigoPorcentaje("6");
			totalImpuesto.setBaseImponible(municipalBond.getNonTaxableTotal().add(municipalBond.getInterest()).add(municipalBond.getSurcharge()));
			totalImpuesto.setTarifa(BigDecimal.ZERO);
			totalImpuesto.setValor(BigDecimal.ZERO);
			totalConImpuestos.totalImpuesto.add(totalImpuesto);
			
		}
		infoFactura.setTotalConImpuestos(totalConImpuestos);

		
//		infoTributaria.setFechaAutorizacion(df.format(receiptAuthorization.getStartDate()));
//		infoTributaria.setCaducidad(df.format(receiptAuthorization.getEndDate()));
//		infoTributaria.setFechaEmision(df.format(receipt.getDate()));		
//		infoTributaria.setDirMatriz(taxpayerRecord.getAddress());
//		infoTributaria.setRazonSocialComprador(resident.getName());
//		infoTributaria.setRucCedulaComprador(resident.getIdentificationNumber());
//		
//		infoTributaria.setContribuyenteEspecial(taxpayerRecord.getSpecialTaxpayerResolution());
//		infoTributaria.setTotalSinImpuestos(municipalBond.getSubTotal());
//		infoTributaria.setTotalConImpuestos(municipalBond.getPaidTotal());
//		infoTributaria.setBaseNoObjetoIVA(municipalBond.getNonTaxableTotal());
//		
//		System.out.println("municipalBond.getTaxableTotal().compareTo(BigDecimal.ZERO)"+municipalBond.getTaxableTotal().compareTo(BigDecimal.ZERO));
//		System.out.println("municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO)"+municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO));
//		
//		if(municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO) > 0){
//			infoTributaria.setBaseIVA0(null);
//			infoTributaria.setBaseIVA12(municipalBond.getTaxableTotal());
//			infoTributaria.setIVA12(municipalBond.getTaxesTotal());
//		} else {
//			infoTributaria.setBaseIVA0(municipalBond.getTaxableTotal());
//			infoTributaria.setBaseIVA12(null);
//			infoTributaria.setIVA12(null);
//		}		
//		

//		System.out.println("<<<R>>>XML Transform PASO 5:");
		Detalles detalles = factura.getDetalles();
		detalles.detalle = new ArrayList<Factura.Detalles.Detalle>();
		for(Item item : municipalBond.getItems()){
			Detalle detalle = new Detalle();
			detalle.setCodigoPrincipal(item.getEntry().getCode());
			detalle.setDescripcion(item.getEntry().getName());
			if (item.getAmount().compareTo(new BigDecimal(1)) == 0){
				detalle.setCantidad(item.getAmount());
				detalle.setPrecioUnitario(item.getTotal());
			} else{
				detalle.setCantidad(item.getAmount());
				detalle.setPrecioUnitario(item.getValue());
			}
			detalle.setDescuento(BigDecimal.ZERO);
			detalle.setPrecioTotalSinImpuesto(item.getTotal());
			detalle.impuestos = new Impuestos();
			detalle.impuestos.impuesto = new ArrayList<Impuesto>();
			Impuesto impuesto = new Impuesto();
			if (item.getEntry().getReceiptType() != null){
				impuesto.setBaseImponible(item.getTotal());
				if(municipalBond.getTaxesTotal().compareTo(BigDecimal.ZERO) > 0){
					impuesto.setCodigo("2");
					impuesto.setCodigoPorcentaje("2");
					impuesto.setTarifa(new BigDecimal(12.00));
					impuesto.setValor(municipalBond.getTaxesTotal());
				} else {
					impuesto.setCodigo("2");
					impuesto.setCodigoPorcentaje("0");
					impuesto.setTarifa(BigDecimal.ZERO);
					impuesto.setValor(BigDecimal.ZERO);
				}
			}
			else {
				impuesto.setBaseImponible(item.getTotal());
				impuesto.setCodigo("2");
				impuesto.setCodigoPorcentaje("6");
				impuesto.setTarifa(BigDecimal.ZERO);
				impuesto.setValor(BigDecimal.ZERO);				
			}
			
			detalle.impuestos.impuesto.add(impuesto);
			detalles.detalle.add(detalle);
		}
		
		if (municipalBond.getInterest().compareTo(BigDecimal.ZERO) > 0 || municipalBond.getSurcharge().compareTo(BigDecimal.ZERO) > 0){
			Detalle detalle = new Detalle();
			detalle.setCodigoPrincipal("INT/REC");
			detalle.setDescripcion("INTERES/RECARGO");
			detalle.setCantidad(new BigDecimal(1));
			detalle.setPrecioUnitario(municipalBond.getInterest());
			detalle.setDescuento(BigDecimal.ZERO);
			detalle.setPrecioTotalSinImpuesto(municipalBond.getInterest().add(municipalBond.getSurcharge()));
			detalle.impuestos = new Impuestos();
			detalle.impuestos.impuesto = new ArrayList<Impuesto>();
			Impuesto impuesto = new Impuesto();
				impuesto.setBaseImponible(municipalBond.getInterest().add(municipalBond.getSurcharge()));
				impuesto.setCodigo("2");
				impuesto.setCodigoPorcentaje("6");
				impuesto.setTarifa(BigDecimal.ZERO);
				impuesto.setValor(BigDecimal.ZERO);				
			
			detalle.impuestos.impuesto.add(impuesto);
			detalles.detalle.add(detalle);
		}
//		MunicipalBond bond;
//		bond = receipt.getMunicipalBond();
		InfoAdicional infoAdicional = new InfoAdicional();
		infoAdicional.campoAdicional = new ArrayList<Factura.InfoAdicional.CampoAdicional>();
//		System.out.println(">>>>>>>>>>>>>>>>>Bond: "+bond);
		int aditionalCount = 0;
//		if (bond.getAdjunct() != null){
		if (municipalBond.getAdjunct() != null){
//			System.out.println(">>>>>>>>>>>>>>>>>Bond Adjunct: "+bond.getAdjunct());
//			System.out.println(">>>>>>>>>>>>>>>>>Bond Adjunct: "+municipalBond.getAdjunct());
			
//			municipalBond.getAdjunct().getDetails().addAll(c);
			municipalBond.getAdjunct().getDetails().size();
		
			
			 List<ValuePair> values = municipalBond.getAdjunct().getDetails();
			 System.out.println("SIZE: "+values.size()); 
			 
			if (municipalBond.getBondAddress() != null && !municipalBond.getBondAddress().isEmpty()) {
				ValuePair valuePair = new ValuePair("Ubicación:", municipalBond.getBondAddress());
				values.add(valuePair);
				System.out.println("SIZE1: "+values.size());
			}
		

			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MMMM", new Locale("es", "ES"));
			if (checkString(dt.format(municipalBond.getServiceDate()))) {
				ValuePair valuePair = new ValuePair("Correspondiente a:", dt.format(municipalBond.getServiceDate()));
				values.add(valuePair); 
				System.out.println("SIZE2: "+values.size());
			}
			
			
			if (!CFO.equals("")) {
				ValuePair valuePair = new ValuePair("Director(a) Financiero:", CFO);
				values.add(valuePair); 
				System.out.println("SIZE3: "+values.size());
			}
			 
			
			for (ValuePair valuePair : values){
				if (valuePair.getValue() != null && valuePair.getValue() != ""){
					System.out.println(valuePair.getLabel()+" "+valuePair.getValue());
					CampoAdicional campoAdicional = new CampoAdicional();
					campoAdicional.setNombre(valuePair.getLabel());
					campoAdicional.setValue(valuePair.getValue());
					infoAdicional.campoAdicional.add(campoAdicional);
					aditionalCount++;
				}
			}  
		}
		
		if (aditionalCount > 0){
			factura.infoAdicional = new InfoAdicional();
			factura.infoAdicional = infoAdicional;
		}
		return factura;
	} 

	

	/**
	 * @author macartuche
	 * @param valueString
	 * @return
	 */
	private static boolean checkString(String valueString) {
		return (valueString != null && !valueString.trim().isEmpty());
	}
}
