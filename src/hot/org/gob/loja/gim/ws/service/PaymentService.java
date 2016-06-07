package org.gob.loja.gim.ws.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.ClosingStatement;
import org.gob.loja.gim.ws.dto.Payout;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.Statement;
import org.gob.loja.gim.ws.exception.HasNoObligations;
import org.gob.loja.gim.ws.exception.InvalidPayout;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.NotActiveWorkday;
import org.gob.loja.gim.ws.exception.NotOpenTill;
import org.gob.loja.gim.ws.exception.PayoutNotAllowed;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.EMoneyPayment;
import ec.gob.gim.security.model.User;

@Local
public interface PaymentService {
	public String LOCAL_NAME = "/gim/PaymentService/local";
	
	public Statement findStatement(ServiceRequest request) throws PayoutNotAllowed, TaxpayerNotFound, InvalidUser, NotActiveWorkday, HasNoObligations;
	public Boolean registerDeposit(ServiceRequest request, Payout payout) throws InvalidPayout, PayoutNotAllowed, TaxpayerNotFound, InvalidUser, NotActiveWorkday, NotOpenTill, HasNoObligations;
	public ClosingStatement findDeposits(ServiceRequest request, Date paymentDate) throws InvalidUser;
	public Boolean isTillOpen(ServiceRequest request) throws NotActiveWorkday, NotOpenTill, InvalidUser;
	public List<EMoneyPayment> findPaidsForEmoney(ServiceRequest request,String debtId, BigDecimal amount, Date date, String idAgent);
	public User findUserByUsername(ServiceRequest request, String username);
	public List<Long> findDepositsIdsForReverse(ServiceRequest request, List<Long> municipalBondsIds);
	
	/** Metodos auxiliares para emoney **/
	public String saveEmoneyPayment(ServiceRequest request, Payout payout, String debtId, boolean isPaid, String idAgent);
	public void updateEMoneyPayment(ServiceRequest request, EMoneyPayment paid);
	public void reverse(ServiceRequest request, List<Long> depositsToReverse);
	public boolean hasRolEmoney(ServiceRequest request);
	public String searchPropertyByCadastralCode(ServiceRequest request, String cadastralCode);	
}
