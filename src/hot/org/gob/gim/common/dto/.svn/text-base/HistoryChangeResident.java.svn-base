package org.gob.gim.common.dto;

import java.math.BigInteger;
import java.util.Date;

public class HistoryChangeResident {// implements Comparable<HistoryChangeResident>{
	private BigInteger idRevision;
	private Date date;
	private String userName;
	private String name;
	private int revType;
	
	public HistoryChangeResident(){
		
	}
	
	public HistoryChangeResident(BigInteger idRevision, Date date, String userName,
			String name, int revType) {
		super();
		this.idRevision = idRevision;
		this.date = date;
		this.userName = userName;
		this.name = name;
		this.revType = revType;
	}

	public BigInteger getIdRevision() {
		return idRevision;
	}

	public void setIdRevision(BigInteger idRevision) {
		this.idRevision = idRevision;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRevType() {
		return revType;
	}

	public void setRevType(int revType) {
		this.revType = revType;
	}

	public String getTransactionTypeKey(Integer value){
		return "TT"+value;
	}
	
//	@Override
//	public int compareTo(HistoryChangeResident o) {
//		if(o != null){ 
//			return this.resolutionNumber.compareToIgnoreCase(o.resolutionNumber);
//		}
//		return 0;
//	}
//
}
