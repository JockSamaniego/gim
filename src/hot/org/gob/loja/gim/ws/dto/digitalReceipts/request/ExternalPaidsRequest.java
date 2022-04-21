/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;
import org.gob.loja.gim.ws.dto.digitalReceipts.DateFormatException;

/**
 * @author René
 *
 */
public class ExternalPaidsRequest {
	
	@NotNull(message = "identification no puede ser nulo")
	@NotEmpty(message = "identification no puede ser vacío")
	private String identification;
	
	@NotNull(message = "from no puede ser nulo")
	@NotEmpty(message = "from no puede ser vacío")
	private String from;
	
	@NotNull(message = "to no puede ser nulo")
	@NotEmpty(message = "to no puede ser vacío")
	private String to;

	/**
	 * @return the identification
	 */
	public String getIdentification() {
		return identification;
	}

	/**
	 * @param identification the identification to set
	 */
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	public Date getFromDate() throws ParseException, DateFormatException{
		try{
			Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(this.getFrom());  
			return date1;
		}catch(Exception e){
			throw new DateFormatException();
		}
	}
	
	public Date getToDate() throws ParseException, DateFormatException{
		try{
			Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(this.getTo());  
			return date1;
		}catch(Exception e){
			throw new DateFormatException();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExternalPaidsRequest [identification=" + identification
				+ ", from=" + from + ", to=" + to + "]";
	}

}
