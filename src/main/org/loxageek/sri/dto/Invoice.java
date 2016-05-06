package org.loxageek.sri.dto;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="factura")
@XmlAccessorType(XmlAccessType.FIELD)
public class Invoice {
	@XmlAttribute
	private static String version = "2_00";

	private InformacionTributaria infoTributaria;
	
	@XmlElementWrapper(name="detalles")
	@XmlElement(name="detalle")
	private List<Detalle> detalles;
	
	private InfoAdicional infoAdicional;
	
	public Invoice() {
		infoTributaria = new InformacionTributaria();
		detalles = new LinkedList<Detalle>();
		//infoAdicional = "";
		infoAdicional = new InfoAdicional();
	}

	public static String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		Invoice.version = version;
	}

	public InformacionTributaria getInfoTributaria() {
		return infoTributaria;
	}

	public void setInfoTributaria(InformacionTributaria infoTributaria) {
		this.infoTributaria = infoTributaria;
	}

	public List<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}	
	
	public void add(Detalle detalle){
		detalles.add(detalle);
	}

	public InfoAdicional getInfoAdicional() {
		return infoAdicional;
	}

	public void setInfoAdicional(InfoAdicional infoAdicional) {
		this.infoAdicional = infoAdicional;
	}

//	public String getInfoAdicional() {
//		return infoAdicional;
//	}
//
//	public void setInfoAdicional(String infoAdicional) {
//		this.infoAdicional = infoAdicional;
//	}	

}
