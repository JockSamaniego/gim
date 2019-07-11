package ec.gob.gim.revenue.model.DTO;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ReviserUCOTUserDTO {

	@NativeQueryResultColumn(index = 0)
	private String name;
	
	@NativeQueryResultColumn(index = 1)
	private Long userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
