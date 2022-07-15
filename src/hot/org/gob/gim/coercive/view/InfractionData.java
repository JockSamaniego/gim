package org.gob.gim.coercive.view;
 
import java.util.Date;

public class InfractionData {

	private String name;
	private String identification;
	private String mail;
	private String licensePlate;
	private String ticket;
	private Date emision;
	private Date register;
	private Date expiration;
	private String article;

	public InfractionData(String name, String identification, String mail,
			String licensePlate, String ticket, Date emision, Date register,
			Date expiration, String article) {
		super();
		this.name = name;
		this.identification = identification;
		this.mail = mail;
		this.licensePlate = licensePlate;
		this.ticket = ticket;
		this.emision = emision;
		this.register = register;
		this.expiration = expiration;
		this.article = article;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Date getEmision() {
		return emision;
	}
	public void setEmision(Date emision) {
		this.emision = emision;
	}
	public Date getRegister() {
		return register;
	}
	public void setRegister(Date register) {
		this.register = register;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
}
