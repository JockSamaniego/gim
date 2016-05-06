package ec.gob.gim.common.model;

import java.util.List;

public class HolyDays {
	private int month;
	private List<Integer> days;
	

	public HolyDays() {
	}
	
	public HolyDays(int month, List<Integer> days) {
		this.month = month;
		this.days = days;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public List<Integer> getDays() {
		return days;
	}
	public void setDays(List<Integer> days) {
		this.days = days;
	}
	
	
}
