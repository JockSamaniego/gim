package org.loxageek.sri.dto;

import java.math.BigDecimal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Demo {
	 
    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Invoice.class);
 
        Invoice invoice = new Invoice();
        InformacionTributaria informacionTributaria = invoice.getInfoTributaria();
        
        informacionTributaria.setRazonSocial("Loxageek Ingenieria de Sistemas");
        informacionTributaria.setRuc(1109876543001L);
        
        Detalle detalle = new Detalle();
        detalle.setCantidad(BigDecimal.ONE);
        detalle.setConcepto("SERVICIO AGUA POTABLE");
        detalle.setDescuentos(BigDecimal.ZERO);
        detalle.setPrecioUnitario(new BigDecimal(14.5));
        detalle.setPrecioTotal(new BigDecimal(14.5));
        
        Detalle detalle1 = new Detalle();
        detalle1.setCantidad(BigDecimal.ONE);
        detalle1.setConcepto("TASA RECOLECCION BASURA");
        detalle1.setDescuentos(BigDecimal.ZERO);
        detalle1.setPrecioUnitario(new BigDecimal(10.5));
        detalle1.setPrecioTotal(new BigDecimal(10.5));
        
        invoice.add(detalle);
        invoice.add(detalle1);
 
        //SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        //Schema schema = factory.newSchema(new StreamSource(new File("factura.xsd")));
        
        Marshaller marshaller = jc.createMarshaller();
        //marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        //marshaller.setSchema(schema);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(invoice, System.out);
    }
}