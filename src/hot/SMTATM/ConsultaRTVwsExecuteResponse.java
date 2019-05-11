/**
 * ConsultaRTVwsExecuteResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMTATM;

public class ConsultaRTVwsExecuteResponse  implements java.io.Serializable {
    private java.lang.String identificacion;

    private java.lang.String propietario;

    private java.lang.String documento;

    private java.lang.String placa1;

    private double valor_total;

    private long ordenespagocodigo;

    private SMTATM.SDTTARIFAORDENPAGOSDTTARIFAORDENPAGOItem[] sdttarifaordenpago;

    public ConsultaRTVwsExecuteResponse() {
    }

    public ConsultaRTVwsExecuteResponse(
           java.lang.String identificacion,
           java.lang.String propietario,
           java.lang.String documento,
           java.lang.String placa1,
           double valor_total,
           long ordenespagocodigo,
           SMTATM.SDTTARIFAORDENPAGOSDTTARIFAORDENPAGOItem[] sdttarifaordenpago) {
           this.identificacion = identificacion;
           this.propietario = propietario;
           this.documento = documento;
           this.placa1 = placa1;
           this.valor_total = valor_total;
           this.ordenespagocodigo = ordenespagocodigo;
           this.sdttarifaordenpago = sdttarifaordenpago;
    }


    /**
     * Gets the identificacion value for this ConsultaRTVwsExecuteResponse.
     * 
     * @return identificacion
     */
    public java.lang.String getIdentificacion() {
        return identificacion;
    }


    /**
     * Sets the identificacion value for this ConsultaRTVwsExecuteResponse.
     * 
     * @param identificacion
     */
    public void setIdentificacion(java.lang.String identificacion) {
        this.identificacion = identificacion;
    }


    /**
     * Gets the propietario value for this ConsultaRTVwsExecuteResponse.
     * 
     * @return propietario
     */
    public java.lang.String getPropietario() {
        return propietario;
    }


    /**
     * Sets the propietario value for this ConsultaRTVwsExecuteResponse.
     * 
     * @param propietario
     */
    public void setPropietario(java.lang.String propietario) {
        this.propietario = propietario;
    }


    /**
     * Gets the documento value for this ConsultaRTVwsExecuteResponse.
     * 
     * @return documento
     */
    public java.lang.String getDocumento() {
        return documento;
    }


    /**
     * Sets the documento value for this ConsultaRTVwsExecuteResponse.
     * 
     * @param documento
     */
    public void setDocumento(java.lang.String documento) {
        this.documento = documento;
    }


    /**
     * Gets the placa1 value for this ConsultaRTVwsExecuteResponse.
     * 
     * @return placa1
     */
    public java.lang.String getPlaca1() {
        return placa1;
    }


    /**
     * Sets the placa1 value for this ConsultaRTVwsExecuteResponse.
     * 
     * @param placa1
     */
    public void setPlaca1(java.lang.String placa1) {
        this.placa1 = placa1;
    }


    /**
     * Gets the valor_total value for this ConsultaRTVwsExecuteResponse.
     * 
     * @return valor_total
     */
    public double getValor_total() {
        return valor_total;
    }


    /**
     * Sets the valor_total value for this ConsultaRTVwsExecuteResponse.
     * 
     * @param valor_total
     */
    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }


    /**
     * Gets the ordenespagocodigo value for this ConsultaRTVwsExecuteResponse.
     * 
     * @return ordenespagocodigo
     */
    public long getOrdenespagocodigo() {
        return ordenespagocodigo;
    }


    /**
     * Sets the ordenespagocodigo value for this ConsultaRTVwsExecuteResponse.
     * 
     * @param ordenespagocodigo
     */
    public void setOrdenespagocodigo(long ordenespagocodigo) {
        this.ordenespagocodigo = ordenespagocodigo;
    }


    /**
     * Gets the sdttarifaordenpago value for this ConsultaRTVwsExecuteResponse.
     * 
     * @return sdttarifaordenpago
     */
    public SMTATM.SDTTARIFAORDENPAGOSDTTARIFAORDENPAGOItem[] getSdttarifaordenpago() {
        return sdttarifaordenpago;
    }


    /**
     * Sets the sdttarifaordenpago value for this ConsultaRTVwsExecuteResponse.
     * 
     * @param sdttarifaordenpago
     */
    public void setSdttarifaordenpago(SMTATM.SDTTARIFAORDENPAGOSDTTARIFAORDENPAGOItem[] sdttarifaordenpago) {
        this.sdttarifaordenpago = sdttarifaordenpago;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaRTVwsExecuteResponse)) return false;
        ConsultaRTVwsExecuteResponse other = (ConsultaRTVwsExecuteResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identificacion==null && other.getIdentificacion()==null) || 
             (this.identificacion!=null &&
              this.identificacion.equals(other.getIdentificacion()))) &&
            ((this.propietario==null && other.getPropietario()==null) || 
             (this.propietario!=null &&
              this.propietario.equals(other.getPropietario()))) &&
            ((this.documento==null && other.getDocumento()==null) || 
             (this.documento!=null &&
              this.documento.equals(other.getDocumento()))) &&
            ((this.placa1==null && other.getPlaca1()==null) || 
             (this.placa1!=null &&
              this.placa1.equals(other.getPlaca1()))) &&
            this.valor_total == other.getValor_total() &&
            this.ordenespagocodigo == other.getOrdenespagocodigo() &&
            ((this.sdttarifaordenpago==null && other.getSdttarifaordenpago()==null) || 
             (this.sdttarifaordenpago!=null &&
              java.util.Arrays.equals(this.sdttarifaordenpago, other.getSdttarifaordenpago())));
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
        if (getIdentificacion() != null) {
            _hashCode += getIdentificacion().hashCode();
        }
        if (getPropietario() != null) {
            _hashCode += getPropietario().hashCode();
        }
        if (getDocumento() != null) {
            _hashCode += getDocumento().hashCode();
        }
        if (getPlaca1() != null) {
            _hashCode += getPlaca1().hashCode();
        }
        _hashCode += new Double(getValor_total()).hashCode();
        _hashCode += new Long(getOrdenespagocodigo()).hashCode();
        if (getSdttarifaordenpago() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSdttarifaordenpago());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSdttarifaordenpago(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaRTVwsExecuteResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SMTATM", ">ConsultaRTVws.ExecuteResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Identificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("propietario");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Propietario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documento");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Documento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placa1");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Placa1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valor_total");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Valor_total"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordenespagocodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Ordenespagocodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sdttarifaordenpago");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Sdttarifaordenpago"));
        elemField.setXmlType(new javax.xml.namespace.QName("SMTATM", "SDTTARIFAORDENPAGO.SDTTARIFAORDENPAGOItem"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("SMTATM", "SDTTARIFAORDENPAGO.SDTTARIFAORDENPAGOItem"));
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
