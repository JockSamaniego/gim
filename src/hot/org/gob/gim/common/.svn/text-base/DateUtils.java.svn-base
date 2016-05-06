package org.gob.gim.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date truncate(Date date){
		Calendar calendar = DateUtils.getTruncatedInstance(date);
		return calendar.getTime();
	}
		
	public static Calendar getTruncatedInstance(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);     
		calendar.set(Calendar.SECOND, 0);     
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	public static Date expand(Date date){
		Calendar calendar = DateUtils.getTruncatedInstance(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.add(Calendar.SECOND, -1);
		return calendar.getTime();
	}
	
	public static Date getDateInstance(int year, int month, int day){
		Calendar calendar = DateUtils.getTruncatedInstance(new Date());
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	
	public static String formatDate(Date date){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
	
	public static String formatFullDate(Date date){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		return df.format(date);
	}
	
}
