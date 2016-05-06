package org.gob.gim.accounting.service;

import java.util.Map;

import javax.ejb.Local;

import org.gob.gim.accounting.dto.AccountDetail;
import org.gob.gim.accounting.dto.AccountItem;
import org.gob.gim.accounting.dto.Criteria;
import org.gob.gim.accounting.dto.ReportFilter;

@Local
public interface FinantialService {
	
	public String LOCAL_NAME = "/gim/FinantialService/local";
	
	public Map<String, AccountItem> findBalanceReport(Criteria criteria);
	public Map<Long, AccountDetail> findDetailReport(Criteria criteria, Long accountId, ReportFilter reportFilter);
	public Map<String, AccountItem> findDuePortfolioReport(Criteria criteria);
}
