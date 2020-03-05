package org.gob.loja.gim.ws.service.v2;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.Payout;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.v2.ClosingStatementV2;
import org.gob.loja.gim.ws.dto.v2.DepositStatementV2;
import org.gob.loja.gim.ws.dto.v2.ReverseStatementV2;
import org.gob.loja.gim.ws.dto.v2.StatementV2;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.NotActiveWorkday;
import org.gob.loja.gim.ws.exception.NotOpenTill;

@Local
public interface PaymentServiceV2 {
	public String LOCAL_NAME = "/gim/PaymentServiceV2/local";
	
	public StatementV2 findStatement(ServiceRequest request);
	public DepositStatementV2 registerDeposit(ServiceRequest request, Payout payout);
	public ClosingStatementV2 findDeposits(ServiceRequest request, Date paymentDate);
	public Boolean isTillOpen(ServiceRequest request) throws NotActiveWorkday, NotOpenTill, InvalidUser;
	public List<Long> findDepositsIdsForReverse(ServiceRequest request, List<Long> municipalBondsIds);
	//public ReverseStatementV2 reverse(ServiceRequest request, List<Long> depositsToReverse);
	public ReverseStatementV2 reversePaymentBank(ServiceRequest request, Payout payout);
	
}
