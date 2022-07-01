/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.coercive.dto.criteria.DataInfractionSearchCriteria;
import org.gob.gim.coercive.view.InfractionUserData;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.HistoryStatusInfraction;
import ec.gob.gim.coercive.model.infractions.PaymentInfraction;
import ec.gob.loja.middleapp.InfractionWSV2;
import ec.gob.loja.middleapp.ResponseInfraccion;

/**
 * @author René
 *
 */
@Local
public interface DatainfractionService {
	
	public String LOCAL_NAME = "/gim/DatainfractionService/local";
	
	public List<Datainfraction> findInfractionsByIdentification(String identification);
	
	public Long getNextValue();
	
	
	public Datainfraction getDataInfractionById(Long id);
	
	public Datainfraction getDataInfractionWithHistoryById(Long id);
	
	public Datainfraction updateDataInfraction(Datainfraction data);
	
	public List<Datainfraction> findDataInfractionByCriteria(DataInfractionSearchCriteria criteria, Integer firstRow,Integer numberOfRows);
	
	public Integer findDataInfractionNumber(DataInfractionSearchCriteria criteria);

	public List<PaymentInfraction> findPaymentsByInfraction(Long infractionId, Long statusid);

	public InfractionUserData userData(Long notificationId);

	public void savePaymentInfraction(PaymentInfraction payment);
	
	public HistoryStatusInfraction saveHIstoryRecord(HistoryStatusInfraction record);
	
	public InfractionWSV2 getInfractionWSV2Instance();
	
	public ResponseInfraccion findInfractionByIdANT(String idANT);
	
}
