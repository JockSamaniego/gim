package ec.gob.gim.wsrest.dto;

import java.io.Serializable;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class DtoRoute implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NativeQueryResultColumn(index = 0)	
	private Long routeId;
	
	@NativeQueryResultColumn(index = 1)
	private String routeName;
	
	@NativeQueryResultColumn(index = 2)
	private Integer routeYear;
	
	@NativeQueryResultColumn(index = 3)
	private Integer routeMonth;
	
	@NativeQueryResultColumn(index = 4)
	private Long userId;	
	

	public DtoRoute(){
		
	}

	public DtoRoute(Long routeId, String routeName, Long userId,
			Integer routeMonth, Integer routeYear) {
		this.routeId = routeId;
		this.routeName = routeName;
		this.userId = userId;
		this.routeMonth = routeMonth;
		this.routeYear = routeYear;
	}

	public Long getRouteId() {
		return routeId;
	}

	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRouteMonth() {
		return routeMonth;
	}

	public void setRouteMonth(Integer routeMonth) {
		this.routeMonth = routeMonth;
	}

	public Integer getRouteYear() {
		return routeYear;
	}

	public void setRouteYear(Integer routeYear) {
		this.routeYear = routeYear;
	}
	
}