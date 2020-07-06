	
	/**
	 * 
	 */
	package ec.gob.gim.cadaster.model.dto;

	import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

	/**
	 * @author Jock Samaniego
	 *
	 */
	@NativeQueryResultEntity
	public class DomainTransferDTO {
		
		@NativeQueryResultColumn(index = 0)	
		private String code;
		
		@NativeQueryResultColumn(index = 1)
		private Date date;
		
		@NativeQueryResultColumn(index = 2)
		private String cadastralCode;
		
		@NativeQueryResultColumn(index = 3)
		private String seller;
		
		@NativeQueryResultColumn(index = 4)
		private String buyer;
		
		@NativeQueryResultColumn(index = 5)
		private String user;
		
		@NativeQueryResultColumn(index = 6)
		private Long domainId;
		
		@NativeQueryResultColumn(index = 7)
		private Long id;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getSeller() {
			return seller;
		}

		public void setSeller(String seller) {
			this.seller = seller;
		}

		public String getBuyer() {
			return buyer;
		}

		public void setBuyer(String buyer) {
			this.buyer = buyer;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getCadastralCode() {
			return cadastralCode;
		}

		public void setCadastralCode(String cadastralCode) {
			this.cadastralCode = cadastralCode;
		}

		public Long getDomainId() {
			return domainId;
		}

		public void setDomainId(Long domainId) {
			this.domainId = domainId;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

}
