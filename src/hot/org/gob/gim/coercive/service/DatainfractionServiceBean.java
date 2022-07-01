/**
 * 
 */
package org.gob.gim.coercive.service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.gob.gim.coercive.dto.criteria.DataInfractionSearchCriteria;
import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.view.InfractionUserData;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.faces.FacesMessages;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.HistoryStatusInfraction;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.coercive.model.infractions.PaymentInfraction;
import ec.gob.loja.middleapp.InfractionWSV2;
import ec.gob.loja.middleapp.InfractionWSV2Service;
import ec.gob.loja.middleapp.ResponseInfraccion;

/**
 * @author René
 *
 */
@Stateless(name = "DatainfractionService")
public class DatainfractionServiceBean implements DatainfractionService {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@EJB
	CrudService crudService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.coercive.service.DatainfractionService#
	 * findInfractionsByIdentification(java.lang.String)
	 */
	@Override
	public List<Datainfraction> findInfractionsByIdentification(
			String identification) {
		Query query = this.entityManager
				.createQuery("SELECT d FROM Datainfraction d WHERE d.identification=:identification AND d.notification IS NULL");
		query.setParameter("identification", identification);
		return query.getResultList();
	}

	@Override
	public Long getNextValue() {
		Query query = this.entityManager
				.createNativeQuery("SELECT nextval('infracciones.notificationInfraction_seq')");
		BigInteger _ret = (BigInteger) query.getSingleResult();
		return _ret.longValue();
	}

	@Override
	public Datainfraction updateDataInfraction(Datainfraction data) {

		Datainfraction dat = this.entityManager.merge(data);
		this.entityManager.flush();
		return dat;

	}

	@Override
	public Datainfraction getDataInfractionById(Long id) {
		Query query = entityManager
				.createQuery("SELECT d FROM Datainfraction d WHERE d.id=:id");
		query.setParameter("id", id);
		return (Datainfraction) query.getSingleResult();
	}

	@Override
	public List<Datainfraction> findDataInfractionByCriteria(
			DataInfractionSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows) {
		
		String strQry = "SELECT d FROM Datainfraction d JOIN d.state s WHERE 1 = 1 ";
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			strQry += "AND d.identification =:identification ";
		}
		
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			strQry += "AND UPPER(d.name) like :name ";
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			strQry += "AND d.licensePlate =:licensePlate ";
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			strQry += "AND d.ticket =:ticket ";
		}
		
		if(criteria.getStatus() != null){
			strQry += "AND s.id =:statusId ";
		}
		
		strQry += "ORDER BY d.name ASC "; 
		
		Query query = this.entityManager
				.createQuery(strQry);
		//query.setParameter("identification", identification);
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			query.setParameter("identification", criteria.getIdentification().trim());
		}
		
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			query.setParameter("name", "%"+ criteria.getName().trim().toUpperCase()+"%");
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			query.setParameter("licensePlate", criteria.getLicensePlate().trim());
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			query.setParameter("ticket", criteria.getTicket().trim());
		}
		
		if(criteria.getStatus() != null){
			query.setParameter("statusId", criteria.getStatus().getId());
		}
		
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);
		
		return query.getResultList();
	}

	@Override
	public Integer findDataInfractionNumber(
			DataInfractionSearchCriteria criteria) {
		
		String strQry = "SELECT COUNT(*) FROM Datainfraction d JOIN d.state s WHERE 1 = 1 ";
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			strQry += "AND d.identification =:identification ";
		}
			
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			strQry += "AND UPPER(d.name) like :name ";
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			strQry += "AND d.licensePlate =:licensePlate ";
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			strQry += "AND d.ticket =:ticket ";
		}
		
		if(criteria.getStatus() != null){
			strQry += "AND s.id =:statusId ";
		}
		
		
		Query query = this.entityManager
				.createQuery(strQry);
		//query.setParameter("identification", identification);
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			query.setParameter("identification", criteria.getIdentification().trim());
		}
		
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			query.setParameter("name", "%"+ criteria.getName().trim().toUpperCase()+"%");
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			query.setParameter("licensePlate", criteria.getLicensePlate().trim());
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			query.setParameter("ticket", criteria.getTicket().trim());
		}
		
		if(criteria.getStatus() != null){
			query.setParameter("statusId", criteria.getStatus().getId());
		}
		
		
		Long size = (Long) query.getSingleResult();

		return size.intValue();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentInfraction> findPaymentsByInfraction(Long infractionId, Long statusid) {
		Query query = entityManager
				.createQuery("SELECT pay FROM PaymentInfraction pay "
						+ "LEFT JOIN fetch pay.finantialInstitution "
						+ "JOIN fetch pay.cashier "
						+ "JOIN fetch pay.paymentType "
						+ "WHERE pay.infraction.id=:infractionId "
						+ "	and pay.status.id=:statusid	"
						+ "	order by pay.date desc, pay.time desc");
		query.setParameter("infractionId", infractionId);
		query.setParameter("statusid", statusid);		
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public InfractionUserData userData(Long notificationId) {
		Query query = entityManager
				.createQuery("SELECT distinct "
							+"NEW org.gob.gim.coercive.view.InfractionUserData(di.name, di.identification, di.email, "
							+ " di.phoneSms, count(di), sum(di.value), sum(di.interest), sum(di.totalValue)) "
							+"FROM Datainfraction di "
							+"WHERE di.notification.id=:notificationId "
							+"GROUP BY di.name, di.identification, di.email, di.phoneSms ");
		query.setParameter("notificationId", notificationId);		
		List<InfractionUserData>list =  query.getResultList();
		return (!list.isEmpty())? list.get(0): null;
	}
	
	
	/**
	 * Grabar un nuevo abono de coactiva-infraccion
	 * @param payment
	 */
	@Override
	public void savePaymentInfraction(PaymentInfraction payment){
		entityManager.persist(payment);
	}

	@Override
	public HistoryStatusInfraction saveHIstoryRecord(
			HistoryStatusInfraction record) {
		return this.crudService.create(record);
	}

	@Override
	public Datainfraction getDataInfractionWithHistoryById(Long id) {
		Query query = entityManager
				.createQuery("SELECT d FROM Datainfraction d LEFT JOIN d.statusChange s WHERE d.id=:id");
		query.setParameter("id", id);
		return (Datainfraction) query.getSingleResult();
	}
	
	//para obtener una instancia del web service ANT
	@In
	FacesMessages facesMessages;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private SystemParameterService systemParameterService;
	InfractionWSV2 infractionWSV2;
	
	public InfractionWSV2 getInfractionWSV2Instance() {
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		try{
			InfractionWSV2Service service = new InfractionWSV2Service();
			InfractionWSV2 infractionWSV2 = service.getInfractionWSV2Port();
			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			String usernameANT = systemParameterService.findParameter(SystemParameterService.USER_NAME_ANT_MIGRATION);
			String passwordANT = systemParameterService.findParameter(SystemParameterService.PASSWORD_ANT_MIGRATION);
	        headers.put("username", Collections.singletonList(usernameANT));
	        headers.put("password", Collections.singletonList(passwordANT));
	        Map<String, Object> req_ctx = ((BindingProvider)infractionWSV2).getRequestContext();
	        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
	        ((BindingProvider)infractionWSV2).getRequestContext().putAll(req_ctx);
	        return infractionWSV2;
		} catch (WebServiceException e){
			e.printStackTrace();
			facesMessages.add("Error de conexión al Servicio de Datos de ANT. Comuníquese con el Administrador del Sistema");
		} catch (Exception e){
			facesMessages.add("Error desconocido. Comuníquese con el Administrador del Sistema");
			e.printStackTrace();
		}
		return null;
	}
	
	//Para consultar un infraccion en la ANT por medio de su id..
	public ResponseInfraccion findInfractionByIdANT(String idANT){
		if(infractionWSV2 == null){
			infractionWSV2 = getInfractionWSV2Instance();
		}
		return infractionWSV2.consultarInfraccion(idANT);
	}
}
