package org.gob.gim.cadaster.webServiceConsumption;

public class FichaPropietario {
	
	private String id;
	private String cedRuc;
	private String nombre;
	private String calidad;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCedRuc() {
		return cedRuc;
	}

	public void setCedRuc(String cedRuc) {
		this.cedRuc = cedRuc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCalidad() {
		return calidad;
	}

	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}

	@Override
	public String toString() {
		return "FichaPropietario [id=" + id + ", cedRuc=" + cedRuc + ", nombre=" + nombre + ", calidad=" + calidad
				+ "]";
	}

	public FichaPropietario(String id, String cedRuc, String nombre, String calidad) {
		super();
		this.id = id;
		this.cedRuc = cedRuc;
		this.nombre = nombre;
		this.calidad = calidad;
	}

	public FichaPropietario() {
		super();
	}

}
