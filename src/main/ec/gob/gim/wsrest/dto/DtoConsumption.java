package ec.gob.gim.wsrest.dto;

import java.util.Date;

public class DtoConsumption {
	private Long consumptionId;
	private Integer month;
	private Integer year;
	private Integer serviceCode;
	private String waterMeterNumber;
	private Long previousReading;
	private boolean average; // si se debe tomar el valor promedio
	private Long currentReading;
	private String waterMeterState;
	private String waterConsumptionState;
	private Date readingDate;
	private int amount;
	private boolean checkingReading; //si la lectura ya ha sido tomada
	private double latitudeReading;
	private double longitudeReading;
	private boolean transferred; // El Sistema transfirió el consumo al dispositivo
	private boolean received; // El sistema recibió el consumo desde el dispositivo movil 
	private Integer routeOrder;
	private String houseNumber;
	private String waterSupplyCategory;
	private String serviceOwnerName;
	private String obs;

	public DtoConsumption(){
		this.previousReading = new Long(0);
		this.average = false;
		this.currentReading = new Long(0);
		this.amount = 0;
		this.checkingReading = false;
		this.latitudeReading = 0;
		this.longitudeReading = 0;
		this.transferred = false;
		this.received = false;
		this.serviceCode = 0;
		this.waterMeterNumber = "";
	}

	public DtoConsumption(Long consumptionId, Integer month, Integer year,
			Integer serviceCode, Long previousReading, Long currentReading, 
			String waterMeterNumber, String waterMeterState, String waterConsumptionState,
			Integer routeOrder, String houseNumber, String waterSupplyCategory,
			String serviceOwnerName, String obs, Double latitudeReading, Double longitudeReading,
			boolean transferred, boolean received) {

		this.consumptionId = consumptionId;
		this.month = month;
		this.year = year;
		this.serviceCode = serviceCode;
		this.previousReading = previousReading;
		this.currentReading = currentReading;
		this.waterMeterNumber = waterMeterNumber;
		this.waterMeterState = waterMeterState;
		this.waterConsumptionState = waterConsumptionState;
		this.routeOrder = routeOrder;
		this.houseNumber = houseNumber;
		this.waterSupplyCategory = waterSupplyCategory;
		this.serviceOwnerName = serviceOwnerName;
		this.obs = obs;
		this.latitudeReading = latitudeReading;
		this.longitudeReading = longitudeReading;
		this.transferred = transferred;
		this.received = received;
		

		this.average = false;
		this.checkingReading = false;
	}

	public DtoConsumption(Long consumptionId, Integer month, Integer year,
			Integer serviceCode, String waterMeterNumber, Long previousReading,
			boolean average, Long currentReading, String waterMeterState,
			String waterConsumptionState, Date readingDate, int amount,
			boolean checkingReading, double latitudeReading,
			double longitudeReading, boolean transferred,
			boolean received, Integer routeOrder, String houseNumber,
			String waterSupplyCategory, String serviceOwnerName, String obs) {
		this.consumptionId = consumptionId;
		this.month = month;
		this.year = year;
		this.serviceCode = serviceCode;
		this.waterMeterNumber = waterMeterNumber;
		this.previousReading = previousReading;
		this.average = average;
		this.currentReading = currentReading;
		this.waterMeterState = waterMeterState;
		this.waterConsumptionState = waterConsumptionState;
		this.readingDate = readingDate;
		this.amount = amount;
		this.checkingReading = checkingReading;
		this.latitudeReading = latitudeReading;
		this.longitudeReading = longitudeReading;
		this.transferred = transferred;
		this.received = received;
		this.routeOrder = routeOrder;
		this.houseNumber = houseNumber;
		this.waterSupplyCategory = waterSupplyCategory;
		this.serviceOwnerName = serviceOwnerName;
		this.obs = obs;
	}

	public Long getConsumptionId() {
		return consumptionId;
	}

	public void setConsumptionId(Long consumptionId) {
		this.consumptionId = consumptionId;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(Integer serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getWaterMeterNumber() {
		return waterMeterNumber;
	}

	public void setWaterMeterNumber(String waterMeterNumber) {
		this.waterMeterNumber = waterMeterNumber;
	}

	public Long getPreviousReading() {
		return previousReading;
	}

	public void setPreviousReading(Long previousReading) {
		this.previousReading = previousReading;
	}

	public boolean isAverage() {
		return average;
	}

	public void setAverage(boolean average) {
		this.average = average;
	}

	public Long getCurrentReading() {
		return currentReading;
	}

	public void setCurrentReading(Long currentReading) {
		this.currentReading = currentReading;
	}

	public String getWaterMeterState() {
		return waterMeterState;
	}

	public void setWaterMeterState(String waterMeterState) {
		this.waterMeterState = waterMeterState;
	}

	public String getWaterConsumptionState() {
		return waterConsumptionState;
	}

	public void setWaterConsumptionState(String waterConsumptionState) {
		this.waterConsumptionState = waterConsumptionState;
	}

	public Date getReadingDate() {
		return readingDate;
	}

	public void setReadingDate(Date readingDate) {
		this.readingDate = readingDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isCheckingReading() {
		return checkingReading;
	}

	public void setCheckingReading(boolean checkingReading) {
		this.checkingReading = checkingReading;
	}

	public double getLatitudeReading() {
		return latitudeReading;
	}

	public void setLatitudeReading(double latitudeReading) {
		this.latitudeReading = latitudeReading;
	}

	public double getLongitudeReading() {
		return longitudeReading;
	}

	public void setLongitudeReading(double longitudeReading) {
		this.longitudeReading = longitudeReading;
	}

	public boolean isTransferred() {
		return transferred;
	}

	public void setTransferred(boolean transferred) {
		this.transferred = transferred;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public Integer getRouteOrder() {
		return routeOrder;
	}

	public void setRouteOrder(Integer routeOrder) {
		this.routeOrder = routeOrder;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getWaterSupplyCategory() {
		return waterSupplyCategory;
	}

	public void setWaterSupplyCategory(String waterSupplyCategory) {
		this.waterSupplyCategory = waterSupplyCategory;
	}

	public String getServiceOwnerName() {
		return serviceOwnerName;
	}

	public void setServiceOwnerName(String serviceOwnerName) {
		this.serviceOwnerName = serviceOwnerName;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

}