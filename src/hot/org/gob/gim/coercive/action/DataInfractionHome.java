package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.loja.middleapp.InfractionWSV2;
import ec.gob.loja.middleapp.InfractionWSV2Service;
import ec.gob.loja.middleapp.ResponseInfraccion;


@Name("dataInfractionHome")
@Scope(ScopeType.CONVERSATION)
public class DataInfractionHome extends EntityHome<Datainfraction>{

	
	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	@In
	FacesMessages facesMessages;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void wire() {
		if (isFirstTime) {
			isFirstTime = Boolean.FALSE;
			itWasMigrated = Boolean.FALSE;
			if (itemCatalogService == null) {
				itemCatalogService = ServiceLocator.getInstance().findResource(
						ItemCatalogService.LOCAL_NAME);}
		}
	}
		private boolean isFirstTime = true;
		private Date initDate;
		private Date lastDate;
		private Boolean itWasMigrated = Boolean.FALSE;
		private List<BigInteger> idsForMigration;
		private List<Datainfraction> idsNotFound;
		private List<Datainfraction> idsMigrated;
		private List<Datainfraction> idsNotPending;
		private List<Datainfraction> idsPreviousRegistred;
		private SystemParameterService systemParameterService;
		public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
		private ItemCatalogService itemCatalogService;

		//Setters & Getters
		public Date getInitDate() {
			return initDate;
		}

		public void setInitDate(Date initDate) {
			this.initDate = initDate;
		}

		public Date getLastDate() {
			return lastDate;
		}

		public void setLastDate(Date lastDate) {
			this.lastDate = lastDate;
		}
		
		public Boolean getItWasMigrated() {
			return itWasMigrated;
		}

		public void setItWasMigrated(Boolean itWasMigrated) {
			this.itWasMigrated = itWasMigrated;
		}
		
		public List<BigInteger> getIdsForMigration() {
			return idsForMigration;
		}

		public void setIdsForMigration(List<BigInteger> idsForMigration) {
			this.idsForMigration = idsForMigration;
		}

		public List<Datainfraction> getIdsNotFound() {
			return idsNotFound;
		}

		public void setIdsNotFound(List<Datainfraction> idsNotFound) {
			this.idsNotFound = idsNotFound;
		}

		public List<Datainfraction> getIdsMigrated() {
			return idsMigrated;
		}

		public void setIdsMigrated(List<Datainfraction> idsMigrated) {
			this.idsMigrated = idsMigrated;
		}

		public List<Datainfraction> getIdsNotPending() {
			return idsNotPending;
		}

		public void setIdsNotPending(List<Datainfraction> idsNotPending) {
			this.idsNotPending = idsNotPending;
		}

		public List<Datainfraction> getIdsPreviousRegistred() {
			return idsPreviousRegistred;
		}

		public void setIdsPreviousRegistred(List<Datainfraction> idsPreviousRegistred) {
			this.idsPreviousRegistred = idsPreviousRegistred;
		}

		
		//Methods
		public void findInfractionsBetweenDates() throws ParseException{
			idsForMigration = new ArrayList<BigInteger>();
			idsNotFound = new ArrayList<Datainfraction>();
			idsMigrated = new ArrayList<Datainfraction>();
			idsNotPending = new ArrayList<Datainfraction>();
			idsPreviousRegistred = new ArrayList<Datainfraction>();
			
			String query = "Select \"ID_FACTURA\" "
						 + "FROM ant_migracion.\"LOJ_FACTURAS\" "
						 + "WHERE CAST(\"FECHA_EMISION\" AS DATE) BETWEEN :initDate and :lastDate "
						 + "ORDER BY \"ID_FACTURA\" ASC ";
			Query q = getEntityManager().createNativeQuery(query);
			q.setParameter("initDate", this.initDate);
			q.setParameter("lastDate", this.lastDate);
			idsForMigration = q.getResultList();
			
			requestAxisInfractions();
			
			itWasMigrated = Boolean.TRUE;
		}

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
		
		public void requestAxisInfractions() throws ParseException{
			joinTransaction();
			InfractionWSV2 infractionWSV2 = getInfractionWSV2Instance();
			ItemCatalog itemPending = this.itemCatalogService
					.findItemByCodeAndCodeCatalog("CATALOG_STATUS_INFRACTIONS",
							"PENDING");
			for (BigInteger id_factura : idsForMigration){
				ResponseInfraccion responseInfraccion = infractionWSV2.consultarInfraccion(id_factura.toString());
				Datainfraction newData = new Datainfraction();
				newData.setId_factura(id_factura.toString());
				if(responseInfraccion != null && responseInfraccion.getCode() == 200){
					newData.setTicket(responseInfraccion.getInfraccion().getContravencion());
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
				    Date parsedDate = dateFormat.parse(responseInfraccion.getInfraccion().getFechaEmision());
				    newData.setEmision(new java.sql.Timestamp(parsedDate.getTime()));
					Date parsedDate2 = dateFormat.parse(responseInfraccion.getInfraccion().getFechaRegistro());
					newData.setRegister(new java.sql.Timestamp(parsedDate2.getTime()));
					Calendar c = Calendar.getInstance();
			        c.setTime(parsedDate2);
			        c.add(Calendar.DATE, 10);
			        parsedDate2 = c.getTime();
			        newData.setExpiration(new java.sql.Timestamp(parsedDate2.getTime()));
					if(responseInfraccion.getInfraccion().getEstado().toUpperCase().equals("EMITIDA") 
							& responseInfraccion.getInfraccion().getPagada().toUpperCase().equals("N")){
						if(!findInfractionInDataExcel(responseInfraccion.getInfraccion().getContravencion())){
							saveInfractionInDataExcel(responseInfraccion, id_factura.toString(), itemPending);
							idsMigrated.add(newData);
						} else {
							idsPreviousRegistred.add(newData);
						}
					} else {
						idsNotPending.add(newData);
					}
				} else {
					idsNotFound.add(newData);
				}
			}
		}
		
		public Boolean findInfractionInDataExcel(String contravencion){
			List<String> idsFound = new ArrayList<String>();
			String query = "Select datax.boleta FROM infracciones.data_excel datax "
					+ "WHERE datax.boleta = :boleta ";
			Query q = getEntityManager().createNativeQuery(query);
			q.setParameter("boleta", contravencion);
			idsFound = q.getResultList();
			if(idsFound.size() > 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		
		public void saveInfractionInDataExcel(ResponseInfraccion responseInfraccion, String id_factura, ItemCatalog itemPending) throws ParseException{
			//System.out.println("===id_factura==" + id_factura);
			Datainfraction dataExcel = new Datainfraction();
			dataExcel.setArticle(responseInfraccion.getInfraccion().getIdArticulo());
			dataExcel.setArticleDescription(responseInfraccion.getInfraccion().getArticuloDescripcion());
			dataExcel.setBetterManagement(null);
			dataExcel.setContactGeneral(null);
			dataExcel.setDataDate(null);
			dataExcel.setDebtType(null);
			dataExcel.setDialedPhone(null);
			dataExcel.setDirection(responseInfraccion.getInfraccion().getLugarInfraccion());
			dataExcel.setEmail(null);
			dataExcel.setEmailIntensity(null);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		    Date parsedDate = dateFormat.parse(responseInfraccion.getInfraccion().getFechaEmision());
			dataExcel.setEmision(new java.sql.Timestamp(parsedDate.getTime()));
			Date parsedDate2 = dateFormat.parse(responseInfraccion.getInfraccion().getFechaRegistro());
			dataExcel.setRegister(new java.sql.Timestamp(parsedDate2.getTime()));
			Calendar c = Calendar.getInstance();
	        c.setTime(parsedDate2);
	        c.add(Calendar.DATE, 10);
	        parsedDate2 = c.getTime();
	        dataExcel.setExpiration(new java.sql.Timestamp(parsedDate2.getTime()));
			dataExcel.setGeneralIntensity(null);
			//dataExcel.setId(null);
			dataExcel.setIdentification(responseInfraccion.getInfraccion().getInfractorIdentificacion());
			dataExcel.setInterest(new BigDecimal(responseInfraccion.getInfraccion().getIntereses()));
			dataExcel.setIvr(null);
			dataExcel.setLicensePlate(responseInfraccion.getInfraccion().getPlaca());
			dataExcel.setLocability(responseInfraccion.getInfraccion().getDireccionAlterna());
			dataExcel.setLocabilityProvince(null);
			dataExcel.setMigrationDate(new Date());
			dataExcel.setName(responseInfraccion.getInfraccion().getInfractorNombre());
			dataExcel.setNotification(null);
			dataExcel.setObservation(responseInfraccion.getInfraccion().getObservacion());
			dataExcel.setPhoneivr(null);
			dataExcel.setPhoneSms(null);
			dataExcel.setSms(null);
			dataExcel.setState(itemPending);
			dataExcel.setStateConsortium(responseInfraccion.getInfraccion().getEstado());
			dataExcel.setStatusChange(null);
			dataExcel.setTelephoneIntensity(null);
			dataExcel.setTelephoneManagement(null);
			dataExcel.setTicket(responseInfraccion.getInfraccion().getContravencion());
			dataExcel.setToMigrate(null);
			dataExcel.setTotalValue(new BigDecimal(responseInfraccion.getInfraccion().getValorTotal()));
			dataExcel.setTypeId(responseInfraccion.getInfraccion().getInfractorTipoId());
			dataExcel.setUserid(null);
			dataExcel.setValue(new BigDecimal(responseInfraccion.getInfraccion().getValor()));
			dataExcel.setMigratedByDepartment(Boolean.TRUE);
			dataExcel.setId_factura(id_factura);
			getEntityManager().merge(dataExcel);
			getEntityManager().flush();
		}
}
