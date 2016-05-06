package org.loxageek.sri.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"campoAdicional"
		})

public class InfoAdicional {
	@XmlElement(required = true)

	private List<CampoAdicional> campoAdicional;

	public List<CampoAdicional> getCampoAdicional() {
		if (campoAdicional == null){
			campoAdicional = new ArrayList<CampoAdicional>();
		}
		return campoAdicional;
	}

	public void setCampoAdicional(List<CampoAdicional> campoAdicional) {
		this.campoAdicional = campoAdicional;
	}
	
	public CampoAdicional createCampoAdicional(String nombre, String value){
		CampoAdicional campoAdicional = new CampoAdicional();
		campoAdicional.setNombre(nombre);
		campoAdicional.setValue(value);
		
		return campoAdicional;
	}
}
