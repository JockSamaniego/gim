package org.gob.gim.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gob.gim.common.NativeQueryResultColumn;

public class BondUpdateMail {

	private Long bondid;
	private Date sendmaildate;
	private String mailsend;
	
	public Long getBondid() {
		return bondid;
	}
	public void setBondid(Long bondid) {
		this.bondid = bondid;
	}
	public Date getSendmaildate() {
		return sendmaildate;
	}
	public void setSendmaildate(Date sendmaildate) {
		this.sendmaildate = sendmaildate;
	}
	public String getMailsend() {
		return mailsend;
	}
	public void setMailsend(String mailsend) {
		this.mailsend = mailsend;
	}
}
