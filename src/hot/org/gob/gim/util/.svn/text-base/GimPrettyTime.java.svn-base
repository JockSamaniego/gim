package org.gob.gim.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class GimPrettyTime {

	public static  int getMinutes(GregorianCalendar g1, GregorianCalendar g2) {
	      int elapsed = 0;
	      GregorianCalendar gc1, gc2;
	      if (g2.after(g1)) {
	         gc2 = (GregorianCalendar) g2.clone();
	         gc1 = (GregorianCalendar) g1.clone();
	      }
	      else   {
	         gc2 = (GregorianCalendar) g1.clone();
	         gc1 = (GregorianCalendar) g2.clone();
	      }
	      gc1.clear(Calendar.MILLISECOND);
	      gc1.clear(Calendar.SECOND);
	      gc1.clear(Calendar.MINUTE);
	      gc1.clear(Calendar.HOUR_OF_DAY);
	      gc2.clear(Calendar.MILLISECOND);
	      gc2.clear(Calendar.SECOND);
	      while ( gc1.before(gc2) ) {
	         gc1.add(Calendar.MINUTE, 1);
	         elapsed++;
	      }
	      return elapsed;
	   }
	
	public static  int getHours(GregorianCalendar g1, GregorianCalendar g2) {
	      int elapsed = 0;
	      GregorianCalendar gc1, gc2;
	      if (g2.after(g1)) {
	         gc2 = (GregorianCalendar) g2.clone();
	         gc1 = (GregorianCalendar) g1.clone();
	      }
	      else   {
	         gc2 = (GregorianCalendar) g1.clone();
	         gc1 = (GregorianCalendar) g2.clone();
	      }
	      gc1.clear(Calendar.MILLISECOND);
	      gc1.clear(Calendar.SECOND);
	      gc1.clear(Calendar.MINUTE);
	      gc1.clear(Calendar.HOUR_OF_DAY);
	      gc2.clear(Calendar.MILLISECOND);
	      gc2.clear(Calendar.SECOND);
	      gc2.clear(Calendar.MINUTE);
	      while ( gc1.before(gc2) ) {
	         gc1.add(Calendar.HOUR_OF_DAY, 1);
	         elapsed++;
	      }
	      return elapsed;
	   }
	
	   public static  int getDays(GregorianCalendar g1, GregorianCalendar g2) {
	      int elapsed = 0;
	      GregorianCalendar gc1, gc2;
	      if (g2.after(g1)) {
	         gc2 = (GregorianCalendar) g2.clone();
	         gc1 = (GregorianCalendar) g1.clone();
	      }
	      else   {
	         gc2 = (GregorianCalendar) g1.clone();
	         gc1 = (GregorianCalendar) g2.clone();
	      }
	      gc1.clear(Calendar.MILLISECOND);
	      gc1.clear(Calendar.SECOND);
	      gc1.clear(Calendar.MINUTE);
	      gc1.clear(Calendar.HOUR_OF_DAY);
	      gc2.clear(Calendar.MILLISECOND);
	      gc2.clear(Calendar.SECOND);
	      gc2.clear(Calendar.MINUTE);
	      gc2.clear(Calendar.HOUR_OF_DAY);
	      while ( gc1.before(gc2) ) {
	         gc1.add(Calendar.DATE, 1);
	         elapsed++;
	      }
	      return elapsed;
	   }
	   public static int getMonths(GregorianCalendar g1, GregorianCalendar g2) {
	      int elapsed = 0;
	      GregorianCalendar gc1, gc2;
	      if (g2.after(g1)) {
	         gc2 = (GregorianCalendar) g2.clone();
	         gc1 = (GregorianCalendar) g1.clone();
	      }
	      else   {
	         gc2 = (GregorianCalendar) g1.clone();
	         gc1 = (GregorianCalendar) g2.clone();
	      }
	      gc1.clear(Calendar.MILLISECOND);
	      gc1.clear(Calendar.SECOND);
	      gc1.clear(Calendar.MINUTE);
	      gc1.clear(Calendar.HOUR_OF_DAY);
	      gc1.clear(Calendar.DATE);
	      gc2.clear(Calendar.MILLISECOND);
	      gc2.clear(Calendar.SECOND);
	      gc2.clear(Calendar.MINUTE);
	      gc2.clear(Calendar.HOUR_OF_DAY);
	      gc2.clear(Calendar.DATE);
	      while ( gc1.before(gc2) ) {
	         gc1.add(Calendar.MONTH, 1);
	         elapsed++;
	      }
	      return elapsed;
	   }
	   
	   
	public static String format(long timeInMillis) {
		StringBuilder b = new StringBuilder();
		long timeInSeconds;
		long days, hours, minutes, seconds;
		timeInSeconds = timeInMillis / 1000;
		days = timeInSeconds / 86400;
		timeInSeconds = timeInSeconds - (days * 86400);
		hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);
		seconds = timeInSeconds;
		if (days > 0){
			b.append(days).append(" día(s) ");
		}
		
		if (hours > 0){
			b.append(hours).append(" hora(s) ");
		}
		
		if (minutes > 0){
			b.append(minutes).append(" minuto(s) ");
		}
		
		if (seconds > 0){
			b.append(seconds).append(" segundo(s)");
		}
		return b.toString();
	}
	   
	   public static void main(String[] args) {
		      GregorianCalendar gc1 = new GregorianCalendar(2001, Calendar.DECEMBER, 30);
		      GregorianCalendar gc2 = new GregorianCalendar(2002, Calendar.FEBRUARY, 1);
		      GimPrettyTime et = new GimPrettyTime();
		      int days = et.getDays(gc1, gc2);
		      int months = et.getMonths(gc1, gc2);
		      int minutes = et.getMinutes(gc1, gc2);
//		      System.out.println("Days = " + days);
//		      System.out.println("Months = " + months);
//		      System.out.println("Minutes = " + minutes);
		      System.out.println(et.format(9465650));
		   }
	}