package ec.gob.gim.common.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.gob.gim.common.dto.SRIDeclaration;
import org.jboss.seam.util.Base64;

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

	private List<SRIEconomicActivity> actividades = new ArrayList<SRIEconomicActivity>();

	private List<SRIDeclaration> declarations = new ArrayList<SRIDeclaration>();

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

	public void setData(
			ec.gob.loja.dinardap.cliente.ResponseContribuyente result) {

		for (ec.gob.loja.dinardap.cliente.Entidad ent : result.getPaquete()
				.getEntidades().getEntidad()) {
			for (ec.gob.loja.dinardap.cliente.Fila fil : ent.getFilas()
					.getFila()) {
				for (ec.gob.loja.dinardap.cliente.Columna col : fil
						.getColumnas().getColumna()) {
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

	public void setDiometricData(
			ec.gob.loja.dinardap.cliente.ResponseContribuyente result) {

		for (ec.gob.loja.dinardap.cliente.Entidad ent : result.getPaquete()
				.getEntidades().getEntidad()) {
			for (ec.gob.loja.dinardap.cliente.Fila fil : ent.getFilas()
					.getFila()) {
				for (ec.gob.loja.dinardap.cliente.Columna col : fil
						.getColumnas().getColumna()) {
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

	public List<SRIEconomicActivity> getActividades() {
		return actividades;
	}

	public void setActividades(List<SRIEconomicActivity> actividades) {
		this.actividades = actividades;
	}

	public void setSRIActivity(
			ec.gob.loja.dinardap.cliente.ResponseContribuyente result) {
		SRIEconomicActivity sriData;
		for (ec.gob.loja.dinardap.cliente.Entidad ent : result.getPaquete()
				.getEntidades().getEntidad()) {
			for (ec.gob.loja.dinardap.cliente.Fila fil : ent.getFilas()
					.getFila()) {
				sriData = new SRIEconomicActivity();
				for (ec.gob.loja.dinardap.cliente.Columna col : fil
						.getColumnas().getColumna()) {

					if (col.getCampo().equals("actividadEconomica")) {
						sriData.setActividadEconomica(col.getValor());
					}
					if (col.getCampo().equals("numeroEstablecimiento")) {
						sriData.setNumeroEstablecimiento(col.getValor());
					}
					if (col.getCampo().equals("numeroRuc")) {
						sriData.setNumeroRuc(col.getValor());
					}

				}
				this.actividades.add(sriData);
			}
		}
	}

	public void setSRIDeclarations(
			ec.gob.loja.dinardap.cliente.ResponseContribuyente result) {
		SRIDeclaration sriData;
		
		if(result.getPaquete()==null)
			return;
		if(result.getPaquete()
				.getEntidades()==null)
			return;

		DecimalFormat myFormatter = new DecimalFormat("$     ###,###.##");
		BigDecimal value;
		
		for (ec.gob.loja.dinardap.cliente.Entidad ent : result.getPaquete()
				.getEntidades().getEntidad()) {
			if (ent.getFilas() != null) {
				for (ec.gob.loja.dinardap.cliente.Fila fil : ent.getFilas()
						.getFila()) {

					for (ec.gob.loja.dinardap.cliente.Columna col : fil
							.getColumnas().getColumna()) {
						sriData = new SRIDeclaration();

						sriData.setField(col.getCampo());

						try {
							value = new BigDecimal(col.getValor());
							sriData.setValue(myFormatter.format(value
									.doubleValue()));
						} catch (Exception e) {
							sriData.setValue(col.getValor());
						}

						this.declarations.add(sriData);
					}

				}
			} else {
				this.declarations = new ArrayList<SRIDeclaration>();
			}

		}
	}

	public List<SRIDeclaration> getDeclarations() {
		return declarations;
	}

	public void setDeclarations(List<SRIDeclaration> declarations) {
		this.declarations = declarations;
	}

}
