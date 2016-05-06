package org.gob.gim.util;

import java.util.Date;
import java.util.Locale;

import org.jboss.seam.annotations.Name;

import com.ocpsoft.pretty.time.PrettyTime;

@Name("uiHelper")
public class UiHelper {

	
	/**
	 * Imprimer en una cadena de texto amigable el tiempo en minutos delta
	 * @param delta
	 * @return
	 */
	public static String printPrettyTime(long delta){
		PrettyTime p = new PrettyTime(new Date(delta), new Locale("gim"));
		return p.format(new Date(0));
		
	}
	
	/**
	 * Imprimer en una cadena de texto amigable el tiempo en minutos delta
	 * @param delta
	 * @return
	 */
	public static String printGimPrettyTime(long delta){
		return GimPrettyTime.format(delta);
		
	}
	
}
