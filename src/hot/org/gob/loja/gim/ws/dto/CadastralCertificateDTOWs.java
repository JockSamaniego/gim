/**
 * 
 */
package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

import ec.gob.gim.cadaster.model.dto.BoundaryDTO;
import ec.gob.gim.cadaster.model.dto.BuildingDTO;

/**
 * @author rene
 *
 */
@NativeQueryResultEntity
public class CadastralCertificateDTOWs {

	@NativeQueryResultColumn(index = 0)
	private String provincia;

	@NativeQueryResultColumn(index = 1)
	private String canton;

	@NativeQueryResultColumn(index = 2)
	private String parroquia_code;

	@NativeQueryResultColumn(index = 3)
	private String zona;

	@NativeQueryResultColumn(index = 4)
	private String sector;

	@NativeQueryResultColumn(index = 5)
	private String manzana;

	@NativeQueryResultColumn(index = 6)
	private String lote;

	@NativeQueryResultColumn(index = 7)
	private String bloque;

	@NativeQueryResultColumn(index = 8)
	private String piso;

	@NativeQueryResultColumn(index = 9)
	private String unidad;

	@NativeQueryResultColumn(index = 10)
	private String ficha_registral;

	@NativeQueryResultColumn(index = 11)
	private String nombres;

	@NativeQueryResultColumn(index = 12)
	private String apellidos;

	@NativeQueryResultColumn(index = 13)
	private String cedula;

	@NativeQueryResultColumn(index = 14)
	private String ubicacion;

	@NativeQueryResultColumn(index = 15)
	private String parroquia;

	@NativeQueryResultColumn(index = 16)
	private String barrio;

	@NativeQueryResultColumn(index = 17)
	private String urbanizacion;

	@NativeQueryResultColumn(index = 18)
	private String subdivision;

	@NativeQueryResultColumn(index = 19)
	private String unificacion;

	@NativeQueryResultColumn(index = 20)
	private String lindero_norte;

	@NativeQueryResultColumn(index = 21)
	private String lindero_sur;

	@NativeQueryResultColumn(index = 22)
	private String lindero_este;

	@NativeQueryResultColumn(index = 23)
	private String lindero_oeste;

	@NativeQueryResultColumn(index = 24)
	private BigDecimal area_total_terreno;

	@NativeQueryResultColumn(index = 25)
	private BigDecimal area_total_construccion;

	@NativeQueryResultColumn(index = 26)
	private String lot_position;

	@NativeQueryResultColumn(index = 27)
	private String construcciones;

	@NativeQueryResultColumn(index = 28)
	private String topografia_terreno;

	@NativeQueryResultColumn(index = 29)
	private Boolean water_service;

	@NativeQueryResultColumn(index = 30)
	private String alcantarillado;

	@NativeQueryResultColumn(index = 31)
	private Boolean luz_electrica;

	@NativeQueryResultColumn(index = 32)
	private Boolean telefonia;

	@NativeQueryResultColumn(index = 33)
	private String material_via;

	@NativeQueryResultColumn(index = 34)
	private BigDecimal avaluo_terreno;

	@NativeQueryResultColumn(index = 35)
	private BigDecimal avaluo_construccion;

	@NativeQueryResultColumn(index = 36)
	private BigDecimal avaluo_comercial;

	@NativeQueryResultColumn(index = 37)
	private byte[] foto;

	@NativeQueryResultColumn(index = 38)
	private String numero_casa;

	@NativeQueryResultColumn(index = 39)
	private BigDecimal valuebysquaremeter;

	@NativeQueryResultColumn(index = 40)
	private String observations;

	@NativeQueryResultColumn(index = 41)
	private String urbanization_block;

	@NativeQueryResultColumn(index = 42)
	private String urbanization_property_number;

	@NativeQueryResultColumn(index = 43)
	private BigDecimal building_area_horizontal_property;

	@NativeQueryResultColumn(index = 44)
	private BigDecimal lot_area_horizontal_property;

	@NativeQueryResultColumn(index = 45)
	private String address_reference;

	@NativeQueryResultColumn(index = 46)
	private BigDecimal buildingaliquot;

	@NativeQueryResultColumn(index = 47)
	private BigDecimal lotaliquot;

	@NativeQueryResultColumn(index = 48)
	private byte[] croquis;

	@NativeQueryResultColumn(index = 49)
	private String descriptionHorizontalProperty;

	private List<BuildingDTO> construccionesJson = new ArrayList<BuildingDTO>();

	private BoundaryDTO linderoNorteJson;

	private BoundaryDTO linderoSurJson;

	private BoundaryDTO linderoEsteJson;

	private BoundaryDTO linderoOesteJson;

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCanton() {
		return canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getParroquia_code() {
		return parroquia_code;
	}

	public void setParroquia_code(String parroquia_code) {
		this.parroquia_code = parroquia_code;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getManzana() {
		return manzana;
	}

	public void setManzana(String manzana) {
		this.manzana = manzana;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getBloque() {
		return bloque;
	}

	public void setBloque(String bloque) {
		this.bloque = bloque;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getFicha_registral() {
		return ficha_registral;
	}

	public void setFicha_registral(String ficha_registral) {
		this.ficha_registral = ficha_registral;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getParroquia() {
		return parroquia;
	}

	public void setParroquia(String parroquia) {
		this.parroquia = parroquia;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getUrbanizacion() {
		return urbanizacion;
	}

	public void setUrbanizacion(String urbanizacion) {
		this.urbanizacion = urbanizacion;
	}

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}

	public String getUnificacion() {
		return unificacion;
	}

	public void setUnificacion(String unificacion) {
		this.unificacion = unificacion;
	}

	public String getLindero_norte() {
		return lindero_norte;
	}

	public void setLindero_norte(String lindero_norte) {
		this.lindero_norte = lindero_norte;
	}

	public String getLindero_sur() {
		return lindero_sur;
	}

	public void setLindero_sur(String lindero_sur) {
		this.lindero_sur = lindero_sur;
	}

	public String getLindero_este() {
		return lindero_este;
	}

	public void setLindero_este(String lindero_este) {
		this.lindero_este = lindero_este;
	}

	public String getLindero_oeste() {
		return lindero_oeste;
	}

	public void setLindero_oeste(String lindero_oeste) {
		this.lindero_oeste = lindero_oeste;
	}

	public BigDecimal getArea_total_terreno() {
		return area_total_terreno;
	}

	public void setArea_total_terreno(BigDecimal area_total_terreno) {
		this.area_total_terreno = area_total_terreno;
	}

	public BigDecimal getArea_total_construccion() {
		return area_total_construccion;
	}

	public void setArea_total_construccion(BigDecimal area_total_construccion) {
		this.area_total_construccion = area_total_construccion;
	}

	public String getLot_position() {
		return lot_position;
	}

	public void setLot_position(String lot_position) {
		this.lot_position = lot_position;
	}

	public String getConstrucciones() {
		return construcciones;
	}

	public void setConstrucciones(String construcciones) {
		this.construcciones = construcciones;
	}

	public String getTopografia_terreno() {
		return topografia_terreno;
	}

	public void setTopografia_terreno(String topografia_terreno) {
		this.topografia_terreno = topografia_terreno;
	}

	public Boolean getWater_service() {
		return water_service;
	}

	public void setWater_service(Boolean water_service) {
		this.water_service = water_service;
	}

	public String getAlcantarillado() {
		return alcantarillado;
	}

	public void setAlcantarillado(String alcantarillado) {
		this.alcantarillado = alcantarillado;
	}

	public Boolean getLuz_electrica() {
		return luz_electrica;
	}

	public void setLuz_electrica(Boolean luz_electrica) {
		this.luz_electrica = luz_electrica;
	}

	public Boolean getTelefonia() {
		return telefonia;
	}

	public void setTelefonia(Boolean telefonia) {
		this.telefonia = telefonia;
	}

	public String getMaterial_via() {
		return material_via;
	}

	public void setMaterial_via(String material_via) {
		this.material_via = material_via;
	}

	public BigDecimal getAvaluo_terreno() {
		return avaluo_terreno;
	}

	public void setAvaluo_terreno(BigDecimal avaluo_terreno) {
		this.avaluo_terreno = avaluo_terreno;
	}

	public BigDecimal getAvaluo_construccion() {
		return avaluo_construccion;
	}

	public void setAvaluo_construccion(BigDecimal avaluo_construccion) {
		this.avaluo_construccion = avaluo_construccion;
	}

	public BigDecimal getAvaluo_comercial() {
		return avaluo_comercial;
	}

	public void setAvaluo_comercial(BigDecimal avaluo_comercial) {
		this.avaluo_comercial = avaluo_comercial;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getNumero_casa() {
		return numero_casa;
	}

	public void setNumero_casa(String numero_casa) {
		this.numero_casa = numero_casa;
	}

	public BigDecimal getValuebysquaremeter() {
		return valuebysquaremeter;
	}

	public void setValuebysquaremeter(BigDecimal valuebysquaremeter) {
		this.valuebysquaremeter = valuebysquaremeter;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getUrbanization_block() {
		return urbanization_block;
	}

	public void setUrbanization_block(String urbanization_block) {
		this.urbanization_block = urbanization_block;
	}

	public String getUrbanization_property_number() {
		return urbanization_property_number;
	}

	public void setUrbanization_property_number(
			String urbanization_property_number) {
		this.urbanization_property_number = urbanization_property_number;
	}

	public BigDecimal getBuilding_area_horizontal_property() {
		return building_area_horizontal_property;
	}

	public void setBuilding_area_horizontal_property(
			BigDecimal building_area_horizontal_property) {
		this.building_area_horizontal_property = building_area_horizontal_property;
	}

	public BigDecimal getLot_area_horizontal_property() {
		return lot_area_horizontal_property;
	}

	public void setLot_area_horizontal_property(
			BigDecimal lot_area_horizontal_property) {
		this.lot_area_horizontal_property = lot_area_horizontal_property;
	}

	public String getAddress_reference() {
		return address_reference;
	}

	public void setAddress_reference(String address_reference) {
		this.address_reference = address_reference;
	}

	public boolean tieneFoto() {
		if (this.foto.length == 1) {
			return false;
		}
		return true;
	}

	public BigDecimal getBuildingaliquot() {
		return buildingaliquot;
	}

	public void setBuildingaliquot(BigDecimal buildingaliquot) {
		this.buildingaliquot = buildingaliquot;
	}

	public BigDecimal getLotaliquot() {
		return lotaliquot;
	}

	public void setLotaliquot(BigDecimal lotaliquot) {
		this.lotaliquot = lotaliquot;
	}

	/**
	 * @return the construccionesJson
	 */
	public List<BuildingDTO> getConstruccionesJson() {
		return construccionesJson;
	}

	/**
	 * @param construccionesJson
	 *            the construccionesJson to set
	 */
	public void setConstruccionesJson(List<BuildingDTO> construccionesJson) {
		this.construccionesJson = construccionesJson;
	}

	/**
	 * @return the linderoNorteJson
	 */
	public BoundaryDTO getLinderoNorteJson() {
		return linderoNorteJson;
	}

	/**
	 * @param linderoNorteJson
	 *            the linderoNorteJson to set
	 */
	public void setLinderoNorteJson(BoundaryDTO linderoNorteJson) {
		this.linderoNorteJson = linderoNorteJson;
	}

	/**
	 * @return the linderoSurJson
	 */
	public BoundaryDTO getLinderoSurJson() {
		return linderoSurJson;
	}

	/**
	 * @param linderoSurJson
	 *            the linderoSurJson to set
	 */
	public void setLinderoSurJson(BoundaryDTO linderoSurJson) {
		this.linderoSurJson = linderoSurJson;
	}

	/**
	 * @return the linderoEsteJson
	 */
	public BoundaryDTO getLinderoEsteJson() {
		return linderoEsteJson;
	}

	/**
	 * @param linderoEsteJson
	 *            the linderoEsteJson to set
	 */
	public void setLinderoEsteJson(BoundaryDTO linderoEsteJson) {
		this.linderoEsteJson = linderoEsteJson;
	}

	/**
	 * @return the linderoOesteJson
	 */
	public BoundaryDTO getLinderoOesteJson() {
		return linderoOesteJson;
	}

	/**
	 * @param linderoOesteJson
	 *            the linderoOesteJson to set
	 */
	public void setLinderoOesteJson(BoundaryDTO linderoOesteJson) {
		this.linderoOesteJson = linderoOesteJson;
	}

	/**
	 * @return the croquis
	 */
	public byte[] getCroquis() {
		return croquis;
	}

	/**
	 * @param croquis
	 *            the croquis to set
	 */
	public void setCroquis(byte[] croquis) {
		this.croquis = croquis;
	}

	public String getDescriptionHorizontalProperty() {
		return descriptionHorizontalProperty;
	}

	public void setDescriptionHorizontalProperty(
			String descriptionHorizontalProperty) {
		this.descriptionHorizontalProperty = descriptionHorizontalProperty;
	}
}