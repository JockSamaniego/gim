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
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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
    
    // Funcion para convertir un nro que representa dinero en pesos a letras.-
    // Funcion mejorada de la que se publico en https://gist.github.com/leog1992/f96596d93b3ff4105ed7affa724f415f
    // Sin la limitacion del rango de integer, soporte de billones y mejoras varias.
    // Se acomoda a cualquier moneda, referencias en:
    // https://es.wikipedia.org/wiki/Cent%C3%A9simo_(fracci%C3%B3n_monetaria)

    private static final String[] UNIDADES = {"", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
    private static final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
        "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
        "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa "};
    private static final String[] CENTENAS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
        "setecientos ", "ochocientos ", "novecientos "};
    
    private static String etiquetaEnteroSingular = "dólar";
    private static String etiquetaEnteroPlural = "dólares";
    private static String etiquetaFlotanteSingular = "centavo";
    private static String etiquetaFlotantePlural = "centavos";
    private static String etiquetaConector = "con";

    public static String getEtiquetaEnteroSingular() {
		return etiquetaEnteroSingular;
	}

	public static void setEtiquetaEnteroSingular(String etiquetaEnteroSingular) {
		GimUtils.etiquetaEnteroSingular = etiquetaEnteroSingular;
	}

	public static String getEtiquetaEnteroPlural() {
		return etiquetaEnteroPlural;
	}

	public static void setEtiquetaEnteroPlural(String etiquetaEnteroPlural) {
		GimUtils.etiquetaEnteroPlural = etiquetaEnteroPlural;
	}

	public static String getEtiquetaFlotanteSingular() {
		return etiquetaFlotanteSingular;
	}

	public static void setEtiquetaFlotanteSingular(String etiquetaFlotanteSingular) {
		GimUtils.etiquetaFlotanteSingular = etiquetaFlotanteSingular;
	}

	public static String getEtiquetaFlotantePlural() {
		return etiquetaFlotantePlural;
	}

	public static void setEtiquetaFlotantePlural(String etiquetaFlotantePlural) {
		GimUtils.etiquetaFlotantePlural = etiquetaFlotantePlural;
	}

	public static String getEtiquetaConector() {
		return etiquetaConector;
	}

	public static void setEtiquetaConector(String etiquetaConector) {
		GimUtils.etiquetaConector = etiquetaConector;
	}

	public static String Convertir(String numero, String etiquetaEnteroSingular, String etiquetaEnteroPlural, 
    		String etiquetaFlotanteSingular, String etiquetaFlotantePlural, String etiquetaConector, boolean mayusculas) {
        String literal = "";
        String parte_decimal = "";
        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",");
        //si el numero no tiene parte decimal, se le agrega ,00
        if (numero.indexOf(",") == -1) {
            numero = numero + ",00";
        }
        //se valida formato de entrada -> 0,00 y 999 999 999 999,00
        if (Pattern.matches("\\d{1,12},\\d{1,2}", numero)) {
            //se divide el numero 0000000,00 -> entero y decimal
            String Num[] = numero.split(",");
            //de da formato al numero decimal
            if (Num[1].length()==1) {
            	Num[1] += "0";
            }                        
            String d = getDecenas(Num[1]);
            if (d!="") {
            	if (etiquetaEnteroSingular!="") parte_decimal += " ";
            	if (Integer.parseInt(Num[1])==1) {
            		parte_decimal += etiquetaConector + " " + d + etiquetaFlotanteSingular;
            	} else {
            		parte_decimal += etiquetaConector + " " + d + etiquetaFlotantePlural;
            	}
            }
            
            //se convierte el numero a literal                       
    		BigInteger parteEntera = new BigInteger(Num[0]);
           
            if (parteEntera.compareTo(new BigInteger("0")) == 0) {//si el valor es cero
                literal = "cero ";
            } else if (parteEntera.compareTo(new BigInteger("999999999")) == 1 ) {//si es billon
                literal = getBillones(Num[0]);
            } else if (parteEntera.compareTo(new BigInteger("999999")) == 1 ) {//si es millon
                literal = getMillones(Num[0]);
            } else if (parteEntera.compareTo(new BigInteger("999")) == 1 ) {//si es miles
                literal = getMiles(Num[0]);
            } else if (parteEntera.compareTo(new BigInteger("99")) == 1 ) {//si es centena
                literal = getCentenas(Num[0]);
            } else if (parteEntera.compareTo(new BigInteger("9")) == 1 ) {//si es decena
                literal = getDecenas(Num[0]);
            } else {//sino unidades -> 9
                literal = getUnidades(Num[0]);
            }
            //devuelve el resultado en mayusculas o minusculas
            
            if (parteEntera.compareTo(new BigInteger("1")) == 0) {
            	literal += etiquetaEnteroSingular;            	
            } else {
            	literal += etiquetaEnteroPlural;
            }            
            
            if (mayusculas) {
                return (literal + parte_decimal).toUpperCase();
            } else {
                return (literal + parte_decimal);
            }
        } else {//error, no se puede convertir
            return literal = null;
        }
    }

    /* funciones para convertir los numeros a literales */
    private static String getUnidades(String numero) {// 1 - 9
        //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private static String getDecenas(String num) {// 99 
    	int n = Integer.parseInt(num); 
    	if (n < 10) {//para casos como -> 01 - 09 
    		return getUnidades(num); 
    	} else if (n > 19) {//para 20...99 
    		String u = getUnidades(num); 
    		if (u.equals("")) { //para 20,30,40,50,60,70,80,90 
    			return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8]; 
    		} else { 
    			if(n == 21) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;}
    			if(n == 22) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;}
    			if(n == 23) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;}
    			if(n == 24) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;}
    			if(n == 25) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;}
    			if(n == 26) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;}
    			if(n == 27) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;}
    			if(n == 28) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;} 
    			if(n == 29) {return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8].substring(0,5) + "i" + u;} 
    			return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u; 
    		} 
    	} else {//numeros entre 11 y 19 
    		return DECENAS[n - 10];
    	} 
    }

    private static String getCentenas(String num) {// 999 o 099
        if (Integer.parseInt(num) > 99) {//es centena
            if (Integer.parseInt(num) == 100) {//caso especial
                return " cien ";
            } else {
                return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            }
        } else {//por Ej. 099 
            //se quita el 0 antes de convertir a decenas
            return getDecenas(Integer.parseInt(num) + "");
        }
    }

    private static String getMiles(String numero) {// 999 999
        //obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        //obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n = "";
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m);
            return n + "mil " + getCentenas(c);
        } else {
            return "" + getCentenas(c);
        }

    }

    private static String getMillones(String numero) { //000 000 000        
        //se obtiene los miles
        String miles = numero.substring(numero.length() - 6);
        //se obtiene los millones
        String millon = numero.substring(0, numero.length() - 6);
        String n = "";
        if (Integer.parseInt(millon) > 0) {
        	if (Integer.parseInt(millon) == 1) {
        		n = getUnidades(millon) + "millon ";
        	} else {
        		n = getCentenas(millon) + "millones ";
        	}
        }
        
        return n + getMiles(miles);
    }

    private static String getBillones(String numero) { //000 000 000 000        
        //se obtiene los miles
        String miles = numero.substring(numero.length() - 9);
        //se obtiene los millones
        String millon = numero.substring(0, numero.length() - 9);
        String n = "";
        if (Integer.parseInt(millon) == 1) {
        	n = getUnidades(millon) + "billon ";
        } else {
        	n = getCentenas(millon) + "billones ";
        }
        
        return n + getMillones(miles);
    }

}