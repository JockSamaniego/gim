package org.loxageek.common.ws;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.gob.loja.gim.ws.dto.EmisionDetail;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.StatementReport;
import org.gob.loja.gim.ws.dto.Taxpayer;
import org.gob.loja.gim.ws.exception.AccountIsBlocked;
import org.gob.loja.gim.ws.exception.AccountIsNotActive;
import org.gob.loja.gim.ws.exception.EmissionOrderNotGenerate;
import org.gob.loja.gim.ws.exception.EmissionOrderNotSave;
import org.gob.loja.gim.ws.exception.EntryNotFound;
import org.gob.loja.gim.ws.exception.FiscalPeriodNotFound;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.RealEstateNotFound;
import org.gob.loja.gim.ws.exception.TaxpayerNonUnique;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;
import org.gob.loja.gim.ws.exception.TaxpayerNotSaved;
import org.gob.loja.gim.ws.service.GimService;

/**
 * Define los servicios que permiten la consulta y registro de los contribuyentes en el sistema GIM 
 * 
 * @author WilmanChamba 
 * wilman at loxageek dot com
 * 
 */

@WebService
public class GimSystem {
	
	@EJB
	private GimService gimService;
	
	@Resource
	WebServiceContext wsContext;
	
	/**
	 * Permite consultar los contribuyentes con el numero de identificacion
	 * identificationNumber a la que se le ha entregado un username y un password
	 * que se incluyen en el ServiceRequest
	 * 
	 * @param request Detalle del peticionario del servicio
	 * @return contribuyente solicitado en el request 
	 * @throws TaxpayerNotFound
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 * @throws TaxpayerNonUnique 
	 */
	@WebMethod
	public Taxpayer findTaxpayer(ServiceRequest request) throws TaxpayerNotFound, InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNonUnique{
		System.out.println("====> FINDING TAXPAYER FOR: "+request.getIdentificationNumber());
		Taxpayer taxpayer = gimService.findTaxpayer(request);
		InvalidateSession();
		return taxpayer;
	}
	
	/**
	 * Permite consultar los contribuyentes con el numero de identificacion
	 * identificationNumber a la que se le ha entregado un username y un password
	 * 
	 * @param name nombre de usuario
	 * @param password
	 * @param identificationNumber del contribuyente
	 * @return Map<String, Object> Key=nameAtributos, Object, el valor de los atributos; 
	 * @throws TaxpayerNotFound
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 * @throws TaxpayerNonUnique 
	 */
	@WebMethod
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, Object> findTaxpayerAsMap(String name, String password, String identificationNumber) 
		throws TaxpayerNotFound, InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNonUnique{
		System.out.println("====> FINDING TAXPAYER FOR: "+identificationNumber);
		return gimService.findTaxpayer(name, password, identificationNumber);

	}
		
	/**
	 * Permite registrar nuevo o actualizar contribuyente 
	 * desde la plataforma ppless a la que se le ha entregado un username y un password
	 * que se incluyen en el ServiceRequest
	 * 
	 * @param request Detalle del peticionario del servicio
	 * @param taxpayer el contribuyente a registrarlo en el GIM
	 * @return contribuyente solicitado en el request 
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 * @throws TaxpayerNotSaved 
	 */
	@WebMethod
	public Boolean saveTaxpayer(ServiceRequest request, Taxpayer taxpayer) 
		throws InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNotSaved{
		System.out.println("SAVE TAXPAYER FOR: "+taxpayer.getIdentificationNumber());
		Boolean ok = gimService.saveTaxpayer(request, taxpayer);
		InvalidateSession();
		return ok;
	}
	
	/**
	 * Permite registrar un nuevo contribuyente o actualizarlo desde un Map 
	 * a través de la plataforma ppless a la que se le ha entregado un username y un password
	 * que se incluyen en el ServiceRequest
	 * 
	 * @param name nombre de usuario
	 * @param password clave de usuario
	 * @param Map conjunto de key (nombres de los atributos de un taxPayer) value, los valores a fijar
	 * @return contribuyente solicitado en el request 
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 * @throws TaxpayerNotSaved 
	 */
	@WebMethod
	public Boolean saveTaxpayerFromMap(String name, String password, 
			@XmlJavaTypeAdapter(MapAdapter.class) Map<String, Object> map) 
		throws InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNotSaved{
		System.out.println("SAVE TAXPAYER FOR: "+ map.get("identificationNumber"));
		Boolean ok = gimService.saveTaxpayer(name, password, map);
		InvalidateSession();
		return ok;
	}
	
	
	/**
	 * Permite consultar el predio con el codigo Catastral al usuario 
	 * que se le ha entregado un username y un password
	 * 
	 * @param name nombre de usuario
	 * @param password
	 * @param cadastralCode codigo Catastral del predio
	 * @return Map<String, Object> Key=nameAtributos, Object, el valor de los atributos; 
	 * @throws RealEstateNotFound 
	 * @throws TaxpayerNotFound
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 * @throws TaxpayerNonUnique 
	 */
	@WebMethod
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, Object> findRealEstateAsMap(String name, String password, String cadastralCode) 
			throws RealEstateNotFound, TaxpayerNotFound, InvalidUser, AccountIsNotActive, AccountIsBlocked, TaxpayerNonUnique{ 
		System.out.println("====> FINDING REALESTATE FOR: "+cadastralCode);
		return gimService.findRealEstate(name, password, cadastralCode);
	}
	
	/**
	 * 
	 * @param name
	 * @param password
	 * @param identificationNumber
	 * @param entryCode
	 * @return
	 * @throws TaxpayerNotFound
	 * @throws TaxpayerNonUnique
	 * @throws EntryNotFound
	 * @throws FiscalPeriodNotFound
	 * @throws EmissionOrderNotGenerate
	 * @throws EmissionOrderNotSave
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 */
	@WebMethod
	public Boolean generateEmissionOrder(String name, String password, String identificationNumber, 
			String accountCode, String pplessuser) throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound, FiscalPeriodNotFound, 
			EmissionOrderNotGenerate, EmissionOrderNotSave, InvalidUser, AccountIsNotActive, AccountIsBlocked{
		System.out.println("====> GENERATE EmissionOrder FOR: "+accountCode);
		return gimService.generateEmissionOrder(name, password, identificationNumber, accountCode, pplessuser);
	}
	
	/**
	 * @author rfarmijosm
	 * Permite al usuario emitir multas en este caso solo es para fotomultas
	 * @param name
	 * @param password
	 * @param identificationNumber
	 * @param accountCode
	 * @param emisionDetail
	 * @return
	 * @throws TaxpayerNotFound
	 * @throws TaxpayerNonUnique
	 * @throws EntryNotFound
	 * @throws FiscalPeriodNotFound
	 * @throws EmissionOrderNotGenerate
	 * @throws EmissionOrderNotSave
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 */
	@WebMethod
	public Boolean generateEmissionOrderANT(String name, String password, String identificationNumber, 
			String accountCode, EmisionDetail emisionDetail) throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound, FiscalPeriodNotFound, 
			EmissionOrderNotGenerate, EmissionOrderNotSave, InvalidUser, AccountIsNotActive, AccountIsBlocked{
		System.out.println("====> GENERATE EmissionOrder FOR: "+accountCode);
		Boolean band = gimService.generateEmissionOrder(name, password, identificationNumber, accountCode, emisionDetail);
		InvalidateSession();
		return band;
	}
	
	/**
	 * 2016-01-30
	 * @author rfarmijosm
	 * Metodo que facilita consultar al emisor la obligaiones por estado y fecha
	 * @param request
	 * @param startDate
	 * @param endDate
	 * @param reportType
	 * @param entryId
	 * @return
	 * @throws TaxpayerNotFound
	 * @throws TaxpayerNonUnique
	 * @throws EntryNotFound
	 * @throws FiscalPeriodNotFound
	 * @throws EmissionOrderNotGenerate
	 * @throws EmissionOrderNotSave
	 * @throws InvalidUser
	 * @throws AccountIsNotActive
	 * @throws AccountIsBlocked
	 */
	@WebMethod
	public StatementReport buildReport(ServiceRequest request, Date startDate, Date endDate, String reportType, Long entryId) 
			throws TaxpayerNotFound, TaxpayerNonUnique, EntryNotFound, FiscalPeriodNotFound, 
			EmissionOrderNotGenerate, EmissionOrderNotSave, InvalidUser, AccountIsNotActive, AccountIsBlocked{
		StatementReport sr = gimService.buildReport(request, startDate, endDate, reportType, entryId);
		InvalidateSession();
		return sr; 
	}

	/**
	 * 2016-01-30
	 * Se incrementa este metodo para invalidar cada peticion
	 * @author rfarmijosm 
	 */
	private void InvalidateSession(){
		final MessageContext mc = this.wsContext.getMessageContext();
	    HttpServletRequest sr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);

	    if (sr != null && sr instanceof HttpServletRequest) {
	        final HttpServletRequest hsr = (HttpServletRequest) sr;
	        HttpSession session = hsr.getSession(false);
	        if (session != null){
	        	System.out.println("Invalidate Session Active GimSystem");
	        	session.invalidate();
	        	session.setMaxInactiveInterval(1);
	        }
	    }
	}

}
