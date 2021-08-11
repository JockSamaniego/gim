package org.gob.gim.common;

/*
 * Utilidades para Sistema Gestión de Ingresos Municipales
 * author: GADL - Ronald Paladines Celi 
 */

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class GimUtils {

	/*
	 * Convierte un string tipo 1,2,3,4,5 a un list<Long> 
	 */
	public static List<Long> convertStringWithCommaToListLong(String string){
		List<Long> list = new ArrayList<Long>();
		List<String> entriesListStr = Arrays.asList(string.split(","));
		for (int i = 0 ; i < entriesListStr.size() ; i++){
			String str = entriesListStr.get(i);
			list.add(Long.parseLong(str));
		}
		return list;
	}
	
	public static List<String> convertStringWithCommaToListString(String string){
		List<String> list = new ArrayList<String>();
		List<String> entriesListStr = Arrays.asList(string.split(","));
		for (int i = 0 ; i < entriesListStr.size() ; i++){
			String str = entriesListStr.get(i);
			list.add(str);
		}
		return list;
	}
	
    /**
     * Genera la clave de acceso para el comprobante en un ambiente normal
     *
     * @param fechaEmision
     * @param tipoComprobante
     * @param ruc
     * @param ambiente
     * @param serie
     * @param numeroComprobante
     * @param codigoNumerico
     * @param tipoEmision
     * @return
     */
    public static String generate_acces_key(Date fechaEmision, String tipoComprobante, String ruc, String ambiente, String serie, String numeroComprobante, String tipoEmision) {
        int verificador = 0;
        if (ruc != null && ruc.length() < 13) {
            ruc = String.format("%013d", new Object[]{ruc});
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String fecha = dateFormat.format(fechaEmision);
        StringBuilder clave = new StringBuilder(fecha);
        clave.append(tipoComprobante);
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(serie);
        clave.append(numeroComprobante);
        clave.append(invoice_code());
        clave.append(tipoEmision);
        verificador = generar_digit_module11(clave.toString());
        clave.append(Integer.valueOf(verificador));
        String claveGenerada = clave.toString();
        if (clave.toString().length() != 49) {
            claveGenerada = null;
        }
        return claveGenerada;
    }

    /**
     * Genera la clave de acceso para el comprobante cuando es generado en un
     * ambiente de contingencia
     *
     * @param fechaEmision
     * @param tipoComprobante
     * @param clavesContigencia
     * @param tipoEmision
     * @return
     * @throws InputMismatchException
     */
    public static String generate_contingency_key(Date fechaEmision, String tipoComprobante, String clavesContigencia, String tipoEmision){
        int verificador = 0;
        String claveGenerada = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String fecha = dateFormat.format(fechaEmision);
        StringBuilder clave = new StringBuilder(fecha);
        clave.append(tipoComprobante);
        clave.append(clavesContigencia);
        clave.append(tipoEmision);
        verificador = generar_digit_module11(clave.toString());
        if (verificador != 10) {
            clave.append(Integer.valueOf(verificador));
            claveGenerada = clave.toString();
        }
        if (clave.toString().length() != 49) {
            claveGenerada = null;
        }
        return claveGenerada;
    }

    /**
     * Algoritmo de verificacion del modulo 11
     *
     * @param cadena
     * @return
     */
    public static int generar_digit_module11(String cadena) {
        int verificador = 0;
        int multiplicador = 2;
        int total = 0;
        int baseMultiplicador = 7;
        int aux[] = new int[cadena.length()];
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt((new StringBuilder()).append("").append(cadena.charAt(i)).toString());
            aux[i] *= multiplicador;
            total += aux[i];
            if (++multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
        }
        verificador = 11 - total % 11 != 11 ? 11 - total % 11 : 0;
        if (verificador == 10) {
            verificador = 1;
        }
        return verificador;
    }

    public static String invoice_code() {
        int hora, minutos, segundos;
        String codigo = new String();
        Calendar calendario = new GregorianCalendar();
        codigo += "0";
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        Formatter fmt = new Formatter();
        fmt.format("%02d", hora);
        codigo += fmt.toString();
        minutos = calendario.get(Calendar.MINUTE);
        fmt = new Formatter();
        fmt.format("%02d", minutos);
        codigo += fmt.toString();
        segundos = calendario.get(Calendar.SECOND);
        fmt = new Formatter();
        fmt.format("%02d", segundos);
        codigo += fmt.toString();
        codigo += "0";
        //System.out.println(codigo);
        return codigo;

    }
    
    /**
	 * Valida objeto con anotaciones sobre atributos
	 * 
	 * @author Renpe
	 * @since 2021-03-18
	 * @param T object
	 * @return List<String>
	 */
    public static <T> List<String> validateRequest(T object){
		List<String> errors = new ArrayList<String>();
		ValidatorFactory factory = Validation
				.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator
				.validate(object);
		
		for (ConstraintViolation<T> violation : violations) {
		    errors.add(violation.getMessage());
		}
		
		return errors;
	}
    
}