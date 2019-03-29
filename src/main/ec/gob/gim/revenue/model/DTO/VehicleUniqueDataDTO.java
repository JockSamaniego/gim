package ec.gob.gim.revenue.model.DTO;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity	
public class VehicleUniqueDataDTO {
	
	@NativeQueryResultColumn(index = 0)
	private String CODIGO_VEHICULO;
	
	@NativeQueryResultColumn(index = 1)
	private String NUMERO_PLACA;
	
	@NativeQueryResultColumn(index = 2)
	private String NUMERO_CAMV_CPN;
	
	@NativeQueryResultColumn(index = 3)
	private String SERIAL_VIN_CHASIS;
	
	@NativeQueryResultColumn(index = 4)
	private String NUMERO_MOTOR;
	
	@NativeQueryResultColumn(index = 5)
	private String CODIGO_ESTADO_AUTO;
	
	@NativeQueryResultColumn(index = 6)
	private String CODIGO_SUB_CATEGORIA;       
	
	@NativeQueryResultColumn(index = 7)
	private String FECHA_COMPRA_REGISTRO;
	
	@NativeQueryResultColumn(index = 8)
	private String TIPO_IDENTIFICAC_POTENC_PROP;
	
	@NativeQueryResultColumn(index = 9)
	private String NUMERO_IDENTIFICAC_POTENC_PROP;
	
	@NativeQueryResultColumn(index = 10)
	private String RAZON_SOCIAL_POTENC_PROP;
	
	@NativeQueryResultColumn(index = 11)
	private String CODIGO_CANTON;
	
	@NativeQueryResultColumn(index = 12)
	private String DESCRIPCION_CANTON;
	
	@NativeQueryResultColumn(index = 13)
	private int ULTIMO_ANIO_PAGADO;

	public String getCODIGO_VEHICULO() {
		return CODIGO_VEHICULO;
	}

	public void setCODIGO_VEHICULO(String cODIGO_VEHICULO) {
		CODIGO_VEHICULO = cODIGO_VEHICULO;
	}

	public String getNUMERO_PLACA() {
		return NUMERO_PLACA;
	}

	public void setNUMERO_PLACA(String nUMERO_PLACA) {
		NUMERO_PLACA = nUMERO_PLACA;
	}

	public String getNUMERO_CAMV_CPN() {
		return NUMERO_CAMV_CPN;
	}

	public void setNUMERO_CAMV_CPN(String nUMERO_CAMV_CPN) {
		NUMERO_CAMV_CPN = nUMERO_CAMV_CPN;
	}

	public String getSERIAL_VIN_CHASIS() {
		return SERIAL_VIN_CHASIS;
	}

	public void setSERIAL_VIN_CHASIS(String sERIAL_VIN_CHASIS) {
		SERIAL_VIN_CHASIS = sERIAL_VIN_CHASIS;
	}

	public String getNUMERO_MOTOR() {
		return NUMERO_MOTOR;
	}

	public void setNUMERO_MOTOR(String nUMERO_MOTOR) {
		NUMERO_MOTOR = nUMERO_MOTOR;
	}

	public String getCODIGO_ESTADO_AUTO() {
		return CODIGO_ESTADO_AUTO;
	}

	public void setCODIGO_ESTADO_AUTO(String cODIGO_ESTADO_AUTO) {
		CODIGO_ESTADO_AUTO = cODIGO_ESTADO_AUTO;
	}

	public String getCODIGO_SUB_CATEGORIA() {
		return CODIGO_SUB_CATEGORIA;
	}

	public void setCODIGO_SUB_CATEGORIA(String cODIGO_SUB_CATEGORIA) {
		CODIGO_SUB_CATEGORIA = cODIGO_SUB_CATEGORIA;
	}

	public String getFECHA_COMPRA_REGISTRO() {
		return FECHA_COMPRA_REGISTRO;
	}

	public void setFECHA_COMPRA_REGISTRO(String fECHA_COMPRA_REGISTRO) {
		FECHA_COMPRA_REGISTRO = fECHA_COMPRA_REGISTRO;
	}

	public String getTIPO_IDENTIFICAC_POTENC_PROP() {
		return TIPO_IDENTIFICAC_POTENC_PROP;
	}

	public void setTIPO_IDENTIFICAC_POTENC_PROP(String tIPO_IDENTIFICAC_POTENC_PROP) {
		TIPO_IDENTIFICAC_POTENC_PROP = tIPO_IDENTIFICAC_POTENC_PROP;
	}

	public String getNUMERO_IDENTIFICAC_POTENC_PROP() {
		return NUMERO_IDENTIFICAC_POTENC_PROP;
	}

	public void setNUMERO_IDENTIFICAC_POTENC_PROP(
			String nUMERO_IDENTIFICAC_POTENC_PROP) {
		NUMERO_IDENTIFICAC_POTENC_PROP = nUMERO_IDENTIFICAC_POTENC_PROP;
	}

	public String getRAZON_SOCIAL_POTENC_PROP() {
		return RAZON_SOCIAL_POTENC_PROP;
	}

	public void setRAZON_SOCIAL_POTENC_PROP(String rAZON_SOCIAL_POTENC_PROP) {
		RAZON_SOCIAL_POTENC_PROP = rAZON_SOCIAL_POTENC_PROP;
	}

	public String getCODIGO_CANTON() {
		return CODIGO_CANTON;
	}

	public void setCODIGO_CANTON(String cODIGO_CANTON) {
		CODIGO_CANTON = cODIGO_CANTON;
	}

	public String getDESCRIPCION_CANTON() {
		return DESCRIPCION_CANTON;
	}

	public void setDESCRIPCION_CANTON(String dESCRIPCION_CANTON) {
		DESCRIPCION_CANTON = dESCRIPCION_CANTON;
	}

	public int getULTIMO_ANIO_PAGADO() {
		return ULTIMO_ANIO_PAGADO;
	}

	public void setULTIMO_ANIO_PAGADO(int uLTIMO_ANIO_PAGADO) {
		ULTIMO_ANIO_PAGADO = uLTIMO_ANIO_PAGADO;
	}
	
	
}
