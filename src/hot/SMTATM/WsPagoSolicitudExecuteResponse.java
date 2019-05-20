/**
 * WsPagoSolicitudExecuteResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMTATM;

public class WsPagoSolicitudExecuteResponse  implements java.io.Serializable {
    private java.lang.String cod_transaccion;

    private java.lang.String codigo_error;

    private java.lang.String mensaje;

    private java.lang.String secuencia_recibo;

    public WsPagoSolicitudExecuteResponse() {
    }

    public WsPagoSolicitudExecuteResponse(
           java.lang.String cod_transaccion,
           java.lang.String codigo_error,
           java.lang.String mensaje,
           java.lang.String secuencia_recibo) {
           this.cod_transaccion = cod_transaccion;
           this.codigo_error = codigo_error;
           this.mensaje = mensaje;
           this.secuencia_recibo = secuencia_recibo;
    }


    /**
     * Gets the cod_transaccion value for this WsPagoSolicitudExecuteResponse.
     * 
     * @return cod_transaccion
     */
    public java.lang.String getCod_transaccion() {
        return cod_transaccion;
    }


    /**
     * Sets the cod_transaccion value for this WsPagoSolicitudExecuteResponse.
     * 
     * @param cod_transaccion
     */
    public void setCod_transaccion(java.lang.String cod_transaccion) {
        this.cod_transaccion = cod_transaccion;
    }


    /**
     * Gets the codigo_error value for this WsPagoSolicitudExecuteResponse.
     * 
     * @return codigo_error
     */
    public java.lang.String getCodigo_error() {
        return codigo_error;
    }


    /**
     * Sets the codigo_error value for this WsPagoSolicitudExecuteResponse.
     * 
     * @param codigo_error
     */
    public void setCodigo_error(java.lang.String codigo_error) {
        this.codigo_error = codigo_error;
    }


    /**
     * Gets the mensaje value for this WsPagoSolicitudExecuteResponse.
     * 
     * @return mensaje
     */
    public java.lang.String getMensaje() {
        return mensaje;
    }


    /**
     * Sets the mensaje value for this WsPagoSolicitudExecuteResponse.
     * 
     * @param mensaje
     */
    public void setMensaje(java.lang.String mensaje) {
        this.mensaje = mensaje;
    }


    /**
     * Gets the secuencia_recibo value for this WsPagoSolicitudExecuteResponse.
     * 
     * @return secuencia_recibo
     */
    public java.lang.String getSecuencia_recibo() {
        return secuencia_recibo;
    }


    /**
     * Sets the secuencia_recibo value for this WsPagoSolicitudExecuteResponse.
     * 
     * @param secuencia_recibo
     */
    public void setSecuencia_recibo(java.lang.String secuencia_recibo) {
        this.secuencia_recibo = secuencia_recibo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsPagoSolicitudExecuteResponse)) return false;
        WsPagoSolicitudExecuteResponse other = (WsPagoSolicitudExecuteResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cod_transaccion==null && other.getCod_transaccion()==null) || 
             (this.cod_transaccion!=null &&
              this.cod_transaccion.equals(other.getCod_transaccion()))) &&
            ((this.codigo_error==null && other.getCodigo_error()==null) || 
             (this.codigo_error!=null &&
              this.codigo_error.equals(other.getCodigo_error()))) &&
            ((this.mensaje==null && other.getMensaje()==null) || 
             (this.mensaje!=null &&
              this.mensaje.equals(other.getMensaje()))) &&
            ((this.secuencia_recibo==null && other.getSecuencia_recibo()==null) || 
             (this.secuencia_recibo!=null &&
              this.secuencia_recibo.equals(other.getSecuencia_recibo())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCod_transaccion() != null) {
            _hashCode += getCod_transaccion().hashCode();
        }
        if (getCodigo_error() != null) {
            _hashCode += getCodigo_error().hashCode();
        }
        if (getMensaje() != null) {
            _hashCode += getMensaje().hashCode();
        }
        if (getSecuencia_recibo() != null) {
            _hashCode += getSecuencia_recibo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsPagoSolicitudExecuteResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SMTATM", ">wsPagoSolicitud.ExecuteResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_transaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Cod_transaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_error");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Codigo_error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensaje");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Mensaje"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secuencia_recibo");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Secuencia_recibo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
