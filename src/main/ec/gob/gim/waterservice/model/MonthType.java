package ec.gob.gim.waterservice.model;

public enum MonthType {

	JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(
			8), SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12);

	private int monthInt;

	private MonthType(int monthInt) {
		this.monthInt=monthInt;
	}

	public int getMonthInt() {
		return monthInt;
	}
	
	public MonthType getMonthType(int month){
		return MonthType.values()[month - 1];
	}
	
	public static MonthType getByValue(int month){
		return MonthType.values()[month-1];
	}
}