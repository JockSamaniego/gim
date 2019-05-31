/**
 * WsPagoSolicitudExecute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMTATM;

public class WsPagoSolicitudExecute  implements java.io.Serializable {
    private java.lang.String ordenespagotipoiden;

    private java.lang.String ordenespagoidentidad;

    private java.lang.String ordenespagoservicio;

    private double valor_pagar;

    private java.lang.String codigo_transaccion;

    private java.lang.String ordenespagobanco;

    private java.lang.String ordenespagosucursal;

    private java.lang.String ordenespagocanal;

    private java.lang.String provincia;

    private java.lang.String fecha_hora_trx;

    private java.lang.String fecha_hora_conta;

    private java.lang.String nro_solicitud;

    public WsPagoSolicitudExecute() {
    }

    public WsPagoSolicitudExecute(
           java.lang.String ordenespagotipoiden,
           java.lang.String ordenespagoidentidad,
           java.lang.String ordenespagoservicio,
           double valor_pagar,
           java.lang.String codigo_transaccion,
           java.lang.String ordenespagobanco,
           java.lang.String ordenespagosucursal,
           java.lang.String ordenespagocanal,
           java.lang.String provincia,
           java.lang.String fecha_hora_trx,
           java.lang.String fecha_hora_conta,
           java.lang.String nro_solicitud) {
           this.ordenespagotipoiden = ordenespagotipoiden;
           this.ordenespagoidentidad = ordenespagoidentidad;
           this.ordenespagoservicio = ordenespagoservicio;
           this.valor_pagar = valor_pagar;
           this.codigo_transaccion = codigo_transaccion;
           this.ordenespagobanco = ordenespagobanco;
           this.ordenespagosucursal = ordenespagosucursal;
           this.ordenespagocanal = ordenespagocanal;
           this.provincia = provincia;
           this.fecha_hora_trx = fecha_hora_trx;
           this.fecha_hora_conta = fecha_hora_conta;
           this.nro_solicitud = nro_solicitud;
    }


    /**
     * Gets the ordenespagotipoiden value for this WsPagoSolicitudExecute.
     * 
     * @return ordenespagotipoiden
     */
    public java.lang.String getOrdenespagotipoiden() {
        return ordenespagotipoiden;
    }


    /**
     * Sets the ordenespagotipoiden value for this WsPagoSolicitudExecute.
     * 
     * @param ordenespagotipoiden
     */
    public void setOrdenespagotipoiden(java.lang.String ordenespagotipoiden) {
        this.ordenespagotipoiden = ordenespagotipoiden;
    }


    /**
     * Gets the ordenespagoidentidad value for this WsPagoSolicitudExecute.
     * 
     * @return ordenespagoidentidad
     */
    public java.lang.String getOrdenespagoidentidad() {
        return ordenespagoidentidad;
    }


    /**
     * Sets the ordenespagoidentidad value for this WsPagoSolicitudExecute.
     * 
     * @param ordenespagoidentidad
     */
    public void setOrdenespagoidentidad(java.lang.String ordenespagoidentidad) {
        this.ordenespagoidentidad = ordenespagoidentidad;
    }


    /**
     * Gets the ordenespagoservicio value for this WsPagoSolicitudExecute.
     * 
     * @return ordenespagoservicio
     */
    public java.lang.String getOrdenespagoservicio() {
        return ordenespagoservicio;
    }


    /**
     * Sets the ordenespagoservicio value for this WsPagoSolicitudExecute.
     * 
     * @param ordenespagoservicio
     */
    public void setOrdenespagoservicio(java.lang.String ordenespagoservicio) {
        this.ordenespagoservicio = ordenespagoservicio;
    }


    /**
     * Gets the valor_pagar value for this WsPagoSolicitudExecute.
     * 
     * @return valor_pagar
     */
    public double getValor_pagar() {
        return valor_pagar;
    }


    /**
     * Sets the valor_pagar value for this WsPagoSolicitudExecute.
     * 
     * @param valor_pagar
     */
    public void setValor_pagar(double valor_pagar) {
        this.valor_pagar = valor_pagar;
    }


    /**
     * Gets the codigo_transaccion value for this WsPagoSolicitudExecute.
     * 
     * @return codigo_transaccion
     */
    public java.lang.String getCodigo_transaccion() {
        return codigo_transaccion;
    }


    /**
     * Sets the codigo_transaccion value for this WsPagoSolicitudExecute.
     * 
     * @param codigo_transaccion
     */
    public void setCodigo_transaccion(java.lang.String codigo_transaccion) {
        this.codigo_transaccion = codigo_transaccion;
    }


    /**
     * Gets the ordenespagobanco value for this WsPagoSolicitudExecute.
     * 
     * @return ordenespagobanco
     */
    public java.lang.String getOrdenespagobanco() {
        return ordenespagobanco;
    }


    /**
     * Sets the ordenespagobanco value for this WsPagoSolicitudExecute.
     * 
     * @param ordenespagobanco
     */
    public void setOrdenespagobanco(java.lang.String ordenespagobanco) {
        this.ordenespagobanco = ordenespagobanco;
    }


    /**
     * Gets the ordenespagosucursal value for this WsPagoSolicitudExecute.
     * 
     * @return ordenespagosucursal
     */
    public java.lang.String getOrdenespagosucursal() {
        return ordenespagosucursal;
    }


    /**
     * Sets the ordenespagosucursal value for this WsPagoSolicitudExecute.
     * 
     * @param ordenespagosucursal
     */
    public void setOrdenespagosucursal(java.lang.String ordenespagosucursal) {
        this.ordenespagosucursal = ordenespagosucursal;
    }


    /**
     * Gets the ordenespagocanal value for this WsPagoSolicitudExecute.
     * 
     * @return ordenespagocanal
     */
    public java.lang.String getOrdenespagocanal() {
        return ordenespagocanal;
    }


    /**
     * Sets the ordenespagocanal value for this WsPagoSolicitudExecute.
     * 
     * @param ordenespagocanal
     */
    public void setOrdenespagocanal(java.lang.String ordenespagocanal) {
        this.ordenespagocanal = ordenespagocanal;
    }


    /**
     * Gets the provincia value for this WsPagoSolicitudExecute.
     * 
     * @return provincia
     */
    public java.lang.String getProvincia() {
        return provincia;
    }


    /**
     * Sets the provincia value for this WsPagoSolicitudExecute.
     * 
     * @param provincia
     */
    public void setProvincia(java.lang.String provincia) {
        this.provincia = provincia;
    }


    /**
     * Gets the fecha_hora_trx value for this WsPagoSolicitudExecute.
     * 
     * @return fecha_hora_trx
     */
    public java.lang.String getFecha_hora_trx() {
        return fecha_hora_trx;
    }


    /**
     * Sets the fecha_hora_trx value for this WsPagoSolicitudExecute.
     * 
     * @param fecha_hora_trx
     */
    public void setFecha_hora_trx(java.lang.String fecha_hora_trx) {
        this.fecha_hora_trx = fecha_hora_trx;
    }


    /**
     * Gets the fecha_hora_conta value for this WsPagoSolicitudExecute.
     * 
     * @return fecha_hora_conta
     */
    public java.lang.String getFecha_hora_conta() {
        return fecha_hora_conta;
    }


    /**
     * Sets the fecha_hora_conta value for this WsPagoSolicitudExecute.
     * 
     * @param fecha_hora_conta
     */
    public void setFecha_hora_conta(java.lang.String fecha_hora_conta) {
        this.fecha_hora_conta = fecha_hora_conta;
    }


    /**
     * Gets the nro_solicitud value for this WsPagoSolicitudExecute.
     * 
     * @return nro_solicitud
     */
    public java.lang.String getNro_solicitud() {
        return nro_solicitud;
    }


    /**
     * Sets the nro_solicitud value for this WsPagoSolicitudExecute.
     * 
     * @param nro_solicitud
     */
    public void setNro_solicitud(java.lang.String nro_solicitud) {
        this.nro_solicitud = nro_solicitud;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsPagoSolicitudExecute)) return false;
        WsPagoSolicitudExecute other = (WsPagoSolicitudExecute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ordenespagotipoiden==null && other.getOrdenespagotipoiden()==null) || 
             (this.ordenespagotipoiden!=null &&
              this.ordenespagotipoiden.equals(other.getOrdenespagotipoiden()))) &&
            ((this.ordenespagoidentidad==null && other.getOrdenespagoidentidad()==null) || 
             (this.ordenespagoidentidad!=null &&
              this.ordenespagoidentidad.equals(other.getOrdenespagoidentidad()))) &&
            ((this.ordenespagoservicio==null && other.getOrdenespagoservicio()==null) || 
             (this.ordenespagoservicio!=null &&
              this.ordenespagoservicio.equals(other.getOrdenespagoservicio()))) &&
            this.valor_pagar == other.getValor_pagar() &&
            ((this.codigo_transaccion==null && other.getCodigo_transaccion()==null) || 
             (this.codigo_transaccion!=null &&
              this.codigo_transaccion.equals(other.getCodigo_transaccion()))) &&
            ((this.ordenespagobanco==null && other.getOrdenespagobanco()==null) || 
             (this.ordenespagobanco!=null &&
              this.ordenespagobanco.equals(other.getOrdenespagobanco()))) &&
            ((this.ordenespagosucursal==null && other.getOrdenespagosucursal()==null) || 
             (this.ordenespagosucursal!=null &&
              this.ordenespagosucursal.equals(other.getOrdenespagosucursal()))) &&
            ((this.ordenespagocanal==null && other.getOrdenespagocanal()==null) || 
             (this.ordenespagocanal!=null &&
              this.ordenespagocanal.equals(other.getOrdenespagocanal()))) &&
            ((this.provincia==null && other.getProvincia()==null) || 
             (this.provincia!=null &&
              this.provincia.equals(other.getProvincia()))) &&
            ((this.fecha_hora_trx==null && other.getFecha_hora_trx()==null) || 
             (this.fecha_hora_trx!=null &&
              this.fecha_hora_trx.equals(other.getFecha_hora_trx()))) &&
            ((this.fecha_hora_conta==null && other.getFecha_hora_conta()==null) || 
             (this.fecha_hora_conta!=null &&
              this.fecha_hora_conta.equals(other.getFecha_hora_conta()))) &&
            ((this.nro_solicitud==null && other.getNro_solicitud()==null) || 
             (this.nro_solicitud!=null &&
              this.nro_solicitud.equals(other.getNro_solicitud())));
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
        if (getOrdenespagotipoiden() != null) {
            _hashCode += getOrdenespagotipoiden().hashCode();
        }
        if (getOrdenespagoidentidad() != null) {
            _hashCode += getOrdenespagoidentidad().hashCode();
        }
        if (getOrdenespagoservicio() != null) {
            _hashCode += getOrdenespagoservicio().hashCode();
        }
        _hashCode += new Double(getValor_pagar()).hashCode();
        if (getCodigo_transaccion() != null) {
            _hashCode += getCodigo_transaccion().hashCode();
        }
        if (getOrdenespagobanco() != null) {
            _hashCode += getOrdenespagobanco().hashCode();
        }
        if (getOrdenespagosucursal() != null) {
            _hashCode += getOrdenespagosucursal().hashCode();
        }
        if (getOrdenespagocanal() != null) {
            _hashCode += getOrdenespagocanal().hashCode();
        }
        if (getProvincia() != null) {
            _hashCode += getProvincia().hashCode();
        }
        if (getFecha_hora_trx() != null) {
            _hashCode += getFecha_hora_trx().hashCode();
        }
        if (getFecha_hora_conta() != null) {
            _hashCode += getFecha_hora_conta().hashCode();
        }
        if (getNro_solicitud() != null) {
            _hashCode += getNro_solicitud().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsPagoSolicitudExecute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SMTATM", ">wsPagoSolicitud.Execute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenespagotipoiden");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Ordenespagotipoiden"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenespagoidentidad");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Ordenespagoidentidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenespagoservicio");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Ordenespagoservicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valor_pagar");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Valor_pagar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_transaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Codigo_transaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenespagobanco");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Ordenespagobanco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenespagosucursal");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Ordenespagosucursal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenespagocanal");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Ordenespagocanal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provincia");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Provincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_hora_trx");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Fecha_hora_trx"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_hora_conta");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Fecha_hora_conta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nro_solicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Nro_solicitud"));
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
