package org.gob.gim.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mvel2.optimizers.impl.refl.nodes.ArrayLength;

public class Util {
	
	/**
	 * Convierte una lista de objetos en una cadena separada por comas, con los elementos de la lista.
	 * Los objetos de la lista deben implementar el método toString
	 * @param list la lista sobre la cual se construye la cadena de elementos seperadas por comas
	 * @return una cadena separada por comas, con los elementos de la lista.
	 */
	public static String listToString(List list) {
		
		if (list.isEmpty()) return "";
		
		StringBuilder buffer = new StringBuilder("[");
		int index = 0;
		for (Iterator _iterator = list.iterator(); _iterator.hasNext(); buffer.append(_iterator.next().toString())){
			if (index > 0){
				buffer.append(", ");
			}
			index++;
		}
		buffer.append("]");
		return buffer.toString();
	}
	
	/**
	 * Convierte una lista de objetos en una cadena separada por comas, con los elementos de la lista.
	 * Los objetos de la lista deben implementar el método toString
	 * @param list la lista sobre la cual se construye la cadena de elementos seperadas por comas
	 * @return una cadena separada por comas, con los elementos de la lista.
	 */
	public static String listToString(String[] list) {
		
		if (list == null) return "-";
		
		StringBuilder buffer = new StringBuilder();
	
		int length = list.length;
		for (int  i = 0; i < length; i++){
			buffer.append(list[i]);
			if (i > 0){
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Convierte una lista de objetos en una cadena separada por separador, con los elementos de la lista.
	 * Los objetos de la lista deben implementar el método toString
	 * @param list la lista sobre la cual se construye la cadena de elementos seperadas por comas
	 * @return una cadena separada por separador, con los elementos de la lista.
	 */
	public static String listToString(List list, char separador) {
		
		if (list.isEmpty()) return "-";
		
		StringBuilder buffer = new StringBuilder();
		int index = 0;
		for (Iterator _iterator = list.iterator(); _iterator.hasNext(); buffer.append(_iterator.next().toString())){
			if (index > 0){
				buffer.append(separador);
				buffer.append(' ');
			}
			index++;
		}
		return buffer.toString();
	}
	
	/**
	 * Convierte una lista de objetos en una cadena separada por separador, con los elementos de la lista.
	 * Los objetos de la lista deben implementar el método toString
	 * @param list la lista sobre la cual se construye la cadena de elementos seperadas por comas
	 * @return una cadena separada por separador, con los elementos de la lista.
	 */
	public static String listToString(List list, String separador) {
		
		if (list.isEmpty()) return "-";
		
		StringBuilder buffer = new StringBuilder();
		int index = 0;
		for (Iterator _iterator = list.iterator(); _iterator.hasNext(); buffer.append(_iterator.next().toString())){
			if (index > 0){
				buffer.append(separador);
			}
			index++;
		}
		return buffer.toString();
	}
	
	public static String toUpperCase(String str){
		if (str == null) return str;
		return str.toUpperCase();
	}
	
	private static final int MINIMUM_SUPPORTED_LENGTH = 4;
	  
	  /**
	   * Truncate text on a whitespace boundary (near a specified length). The
	   * length of the resultant string will be in the range:<br>
	   * <code>   (requested-length * .25) ~ (requested-length * 1.5) </code>
	   * 
	   * @param text
	   *          Text to truncate
	   * @param length
	   *          Target length
	   * @return Truncated text
	   */
	  public static String truncateAtWhitespace(String text, int length) {
	    int desired, lowerBound, upperBound;
	    /*
	     * Make sure we have a reasonable length to work with
	     */
	    if (length < MINIMUM_SUPPORTED_LENGTH) {
	      throw new IllegalArgumentException("Requested length too short (must be "
	          + MINIMUM_SUPPORTED_LENGTH + " or greated)");
	    }
	    /*
	     * No need to truncate - the original string "fits"
	     */
	    if (text.length() <= length) {
	      return text;
	    }
	    /*
	     * Try to find whitespace befor the requested maximum
	     */
	    lowerBound = length / 4;
	    upperBound = length + (length / 2);

	    for (int i = length - 1; i > lowerBound; i--) {
	      if (Character.isWhitespace(text.charAt(i))) {
	        return text.substring(0, i);
	      }
	    }
	    /*
	     * No whitespace - look beyond the desired maximum
	     */
	    for (int i = (length); i < upperBound; i++) {
	      if (Character.isWhitespace(text.charAt(i))) {
	        return text.substring(0, i);
	      }
	    }
	    /*
	     * No whitespace, just truncate the text at the requested length
	     */
	    return text.substring(0, length);
	  }

	  public static List<Long> splitArrayString(String arrayString){
		  
		  if (arrayString == null){
			  return new ArrayList<Long>(0);
		  }
		  
		  if (arrayString.isEmpty()){
			  return new ArrayList<Long>(0);
		  }
		  //verificar presencia de [ y ]
		  boolean haveSquareBracket = arrayString.contains("[") && arrayString.contains("]");
		  String[] part = null;
		  List<Long> result = new ArrayList<Long>(0);
		  if (haveSquareBracket){
			  arrayString = arrayString.substring(1, arrayString.length() - 1); //remove SquareBracket
			  part = arrayString.split(",");
			  for (int i = 0; i < part.length; i++) {
				  try {
					  String aux = part[i].trim();
					  if(aux.isEmpty() || aux.equals("")){
						  aux="1";
					  }
					  result.add(Long.valueOf(aux));
				  } catch (NumberFormatException nfe){
					  result.add(-1L);
					  nfe.printStackTrace();
				  }
			  }
		  } else {
			  
			  try {
				  result.add(Long.valueOf(arrayString.trim()));
			  } catch (NumberFormatException nfe){
				  result.add(-1L);
				  nfe.printStackTrace();
			  }
		  }
		  
		  return result;
	  }
}
