package ec.gob.gim.parking.model;

import java.util.Calendar;

public class TicketSummary {
	
	private int year;
	private int month;
	private int day;
	private int count;
	private double total;
	
	private String operatorName;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public int getDayOfWeek(){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, this.year);
		calendar.set(Calendar.MONTH, this.month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, this.day);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
}
