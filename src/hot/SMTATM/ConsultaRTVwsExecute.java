/**
 * ConsultaRTVwsExecute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SMTATM;

public class ConsultaRTVwsExecute  implements java.io.Serializable {
    private java.lang.String placa;

    private java.lang.String procesocodigo;

    public ConsultaRTVwsExecute() {
    }

    public ConsultaRTVwsExecute(
           java.lang.String placa,
           java.lang.String procesocodigo) {
           this.placa = placa;
           this.procesocodigo = procesocodigo;
    }


    /**
     * Gets the placa value for this ConsultaRTVwsExecute.
     * 
     * @return placa
     */
    public java.lang.String getPlaca() {
        return placa;
    }


    /**
     * Sets the placa value for this ConsultaRTVwsExecute.
     * 
     * @param placa
     */
    public void setPlaca(java.lang.String placa) {
        this.placa = placa;
    }


    /**
     * Gets the procesocodigo value for this ConsultaRTVwsExecute.
     * 
     * @return procesocodigo
     */
    public java.lang.String getProcesocodigo() {
        return procesocodigo;
    }


    /**
     * Sets the procesocodigo value for this ConsultaRTVwsExecute.
     * 
     * @param procesocodigo
     */
    public void setProcesocodigo(java.lang.String procesocodigo) {
        this.procesocodigo = procesocodigo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaRTVwsExecute)) return false;
        ConsultaRTVwsExecute other = (ConsultaRTVwsExecute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.placa==null && other.getPlaca()==null) || 
             (this.placa!=null &&
              this.placa.equals(other.getPlaca()))) &&
            ((this.procesocodigo==null && other.getProcesocodigo()==null) || 
             (this.procesocodigo!=null &&
              this.procesocodigo.equals(other.getProcesocodigo())));
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
        if (getPlaca() != null) {
            _hashCode += getPlaca().hashCode();
        }
        if (getProcesocodigo() != null) {
            _hashCode += getProcesocodigo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaRTVwsExecute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SMTATM", ">ConsultaRTVws.Execute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placa");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Placa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procesocodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("SMTATM", "Procesocodigo"));
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
