package ec.gob.gim.common.dto;

import org.jboss.seam.util.Base64;

import ec.gob.loja.dinardap.dinardap.cliente.Columna;
import ec.gob.loja.dinardap.dinardap.cliente.Entidad;
import ec.gob.loja.dinardap.dinardap.cliente.Fila;
import ec.gob.loja.dinardap.dinardap.cliente.ResponseContribuyente;

public class DinardapResident {

	private String conyuge;
	private String cedulaConyuge;
	private String nacionalidad;
	private String domicilio;
	private String estadoCivil;
	private String fechaDefuncion;
	private String fechaNacimiento;
	private String genero;
	private String instruccion;
	private String lugarNacimiento;
	private String nombre;
	private String nombreMadre;
	private String nombrePadre;
	private String numeroCasa;
	
	private byte[] foto;
	
	private byte[] firma;

	public String getConyuge() {
		return conyuge;
	}

	public void setConyuge(String conyuge) {
		this.conyuge = conyuge;
	}

	public String getCedulaConyuge() {
		return cedulaConyuge;
	}

	public void setCedulaConyuge(String cedulaConyuge) {
		this.cedulaConyuge = cedulaConyuge;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getFechaDefuncion() {
		return fechaDefuncion;
	}

	public void setFechaDefuncion(String fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getInstruccion() {
		return instruccion;
	}

	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}

	public String getLugarNacimiento() {
		return lugarNacimiento;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreMadre() {
		return nombreMadre;
	}

	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}

	public String getNombrePadre() {
		return nombrePadre;
	}

	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}

	public String getNumeroCasa() {
		return numeroCasa;
	}

	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public byte[] getFirma() {
		return firma;
	}

	public void setFirma(byte[] firma) {
		this.firma = firma;
	}

	public void setData(ResponseContribuyente result) {

		for (Entidad ent : result.getPaquete().getEntidades().getEntidad()) {
			for (Fila fil : ent.getFilas().getFila()) {
				for (Columna col : fil.getColumnas().getColumna()) {
					if (col.getCampo().equals("conyuge")) {
						this.conyuge = col.getValor();
					}
					if (col.getCampo().equals("cedulaConyuge")) {
						this.cedulaConyuge = col.getValor();
					}
					if (col.getCampo().equals("nacionalidad")) {
						this.nacionalidad = col.getValor();
					}
					if (col.getCampo().equals("domicilio")) {
						this.domicilio = col.getValor();
					}
					if (col.getCampo().equals("estadoCivil")) {
						this.estadoCivil = col.getValor();
					}
					if (col.getCampo().equals("fechaDefuncion")) {
						this.fechaDefuncion = col.getValor();
					}
					if (col.getCampo().equals("fechaNacimiento")) {
						this.fechaNacimiento = col.getValor();
					}
					if (col.getCampo().equals("genero")) {
						this.genero = col.getValor();
					}
					if (col.getCampo().equals("lugarNacimiento")) {
						this.lugarNacimiento = col.getValor();
					}
					if (col.getCampo().equals("nombre")) {
						this.nombre = col.getValor();
					}
					if (col.getCampo().equals("nombreMadre")) {
						this.nombreMadre = col.getValor();
					}
					if (col.getCampo().equals("nombrePadre")) {
						this.nombrePadre = col.getValor();
					}
					if (col.getCampo().equals("numeroCasa")) {
						this.numeroCasa = col.getValor();
					}
					if (col.getCampo().equals("nombre")) {
						this.nombre = col.getValor();
					}

				}
			}
		}
	}
	
	public void setDiometricData(ResponseContribuyente result) {

		for (Entidad ent : result.getPaquete().getEntidades().getEntidad()) {
			for (Fila fil : ent.getFilas().getFila()) {
				for (Columna col : fil.getColumnas().getColumna()) {
					if (col.getCampo().equals("firma")) {
						this.firma = Base64.decode(col.getValor());
					}
					if (col.getCampo().equals("foto")) {
						this.foto = Base64.decode(col.getValor());
					}
				}
			}
		}
	}
	

}
