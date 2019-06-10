package ec.gob.gim.waterservice.model.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class WaterServiceHistoryDTO {

	@NativeQueryResultColumn(index = 0)
	private Integer routeOrder;
	
	@NativeQueryResultColumn(index = 1)
	private Integer serviceNumber;
	
	@NativeQueryResultColumn(index = 2)
	private String serviceOwner;
	
	@NativeQueryResultColumn(index = 3)
	private String recipeOwner;
	
	@NativeQueryResultColumn(index = 4)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@NativeQueryResultColumn(index = 5)
	private String userName;

	public Integer getRouteOrder() {
		return routeOrder;
	}

	public void setRouteOrder(Integer routeOrder) {
		this.routeOrder = routeOrder;
	}

	public Integer getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(Integer serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getServiceOwner() {
		return serviceOwner;
	}

	public void setServiceOwner(String serviceOwner) {
		this.serviceOwner = serviceOwner;
	}

	public String getRecipeOwner() {
		return recipeOwner;
	}

	public void setRecipeOwner(String recipeOwner) {
		this.recipeOwner = recipeOwner;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
