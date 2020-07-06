package org.gob.gim.cadaster.webServiceConsumption;

import java.util.ArrayList;
import java.util.List;

public class PropertyWs {
	
	private String tipoPredio;
	private String claveCatastral;
	private String claveCatastralAnterior;
	private String linderos;
	private String numFicha;
	private String urlFoto;
	private String nombreParroquia;
	private String observacion;

	private List <FichaPropietario> fichaPropietarios = new ArrayList<FichaPropietario>();
	
	public String getTipoPredio() {
		return tipoPredio;
	}

	public void setTipoPredio(String tipoPredio) {
		this.tipoPredio = tipoPredio;
	}

	public String getClaveCatastral() {
		return claveCatastral;
	}

	public void setClaveCatastral(String claveCatastral) {
		this.claveCatastral = claveCatastral;
	}

	public String getClaveCatastralAnterior() {
		return claveCatastralAnterior;
	}

	public void setClaveCatastralAnterior(String claveCatastralAnterior) {
		this.claveCatastralAnterior = claveCatastralAnterior;
	}

	public String getLinderos() {
		return linderos;
	}

	public void setLinderos(String linderos) {
		this.linderos = linderos;
	}

	public String getNumFicha() {
		return numFicha;
	}

	public void setNumFicha(String numFicha) {
		this.numFicha = numFicha;
	}

	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	public String getNombreParroquia() {
		return nombreParroquia;
	}

	public void setNombreParroquia(String nombreParroquia) {
		this.nombreParroquia = nombreParroquia;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<FichaPropietario> getFichaPropietarios() {
		return fichaPropietarios;
	}

	public void setFichaPropietarios(List<FichaPropietario> fichaPropietarios) {
		this.fichaPropietarios = fichaPropietarios;
	}

	@Override
	public String toString() {
		return "PropertyWs [tipoPredio=" + tipoPredio + ", claveCatastral=" + claveCatastral
				+ ", claveCatastralAnterior=" + claveCatastralAnterior + ", linderos=" + linderos + ", numFicha="
				+ numFicha + ", urlFoto=" + urlFoto + ", nombreParroquia=" + nombreParroquia + ", observacion="
				+ observacion + ", fichaPropietarios=" + fichaPropietarios + "]";
	}

	

}
