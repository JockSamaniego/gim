package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity	
public class VehicleGeneralDataDTO {
	
	@NativeQueryResultColumn(index = 0)
	private String CODIGO_SUB_CATEGORIA;
	
	@NativeQueryResultColumn(index = 1)
	private String CODIGO_MARCA;
	
	@NativeQueryResultColumn(index = 2)
	private String DESCRIPCION_MARCA;
	
	@NativeQueryResultColumn(index = 3)
	private String CODIGO_MODELO;
	
	@NativeQueryResultColumn(index = 4)
	private String DRESCRIPCION_MODELO;
	
	@NativeQueryResultColumn(index = 5)
	private int ANIO_AUTO;
	
	@NativeQueryResultColumn(index = 6)
	private String CODIGO_PAIS;
	
	@NativeQueryResultColumn(index = 7)
	private String DESCRIPCION_PAIS;
	
	@NativeQueryResultColumn(index = 8)
	private BigDecimal VALOR_AVALUO;
	
	@NativeQueryResultColumn(index = 9)
	private int ANIO_DESDE;
	
	@NativeQueryResultColumn(index = 10)
	private int ANIO_HASTA;

	public String getCODIGO_SUB_CATEGORIA() {
		return CODIGO_SUB_CATEGORIA;
	}

	public void setCODIGO_SUB_CATEGORIA(String cODIGO_SUB_CATEGORIA) {
		CODIGO_SUB_CATEGORIA = cODIGO_SUB_CATEGORIA;
	}

	public String getCODIGO_MARCA() {
		return CODIGO_MARCA;
	}

	public void setCODIGO_MARCA(String cODIGO_MARCA) {
		CODIGO_MARCA = cODIGO_MARCA;
	}

	public String getDESCRIPCION_MARCA() {
		return DESCRIPCION_MARCA;
	}

	public void setDESCRIPCION_MARCA(String dESCRIPCION_MARCA) {
		DESCRIPCION_MARCA = dESCRIPCION_MARCA;
	}

	public String getCODIGO_MODELO() {
		return CODIGO_MODELO;
	}

	public void setCODIGO_MODELO(String cODIGO_MODELO) {
		CODIGO_MODELO = cODIGO_MODELO;
	}

	public String getDRESCRIPCION_MODELO() {
		return DRESCRIPCION_MODELO;
	}

	public void setDRESCRIPCION_MODELO(String dRESCRIPCION_MODELO) {
		DRESCRIPCION_MODELO = dRESCRIPCION_MODELO;
	}

	public int getANIO_AUTO() {
		return ANIO_AUTO;
	}

	public void setANIO_AUTO(int aNIO_AUTO) {
		ANIO_AUTO = aNIO_AUTO;
	}

	public String getCODIGO_PAIS() {
		return CODIGO_PAIS;
	}

	public void setCODIGO_PAIS(String cODIGO_PAIS) {
		CODIGO_PAIS = cODIGO_PAIS;
	}

	public String getDESCRIPCION_PAIS() {
		return DESCRIPCION_PAIS;
	}

	public void setDESCRIPCION_PAIS(String dESCRIPCION_PAIS) {
		DESCRIPCION_PAIS = dESCRIPCION_PAIS;
	}

	public BigDecimal getVALOR_AVALUO() {
		return VALOR_AVALUO;
	}

	public void setVALOR_AVALUO(BigDecimal vALOR_AVALUO) {
		VALOR_AVALUO = vALOR_AVALUO;
	}

	public int getANIO_DESDE() {
		return ANIO_DESDE;
	}

	public void setANIO_DESDE(int aNIO_DESDE) {
		ANIO_DESDE = aNIO_DESDE;
	}

	public int getANIO_HASTA() {
		return ANIO_HASTA;
	}

	public void setANIO_HASTA(int aNIO_HASTA) {
		ANIO_HASTA = aNIO_HASTA;
	}
	
}
