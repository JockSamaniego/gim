/**
 * 
 */
package org.gob.loja.gim.ws.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.CadastralCertificateDTOWs;
import org.gob.loja.gim.ws.dto.PropertyDTOWs;

import ec.gob.gim.common.model.Resident;

/**
 * @author Rene
 *
 */
@Local
public interface RestService {
	
	public String LOCAL_NAME = "/gim/RestService/local";
	
	public Resident findUserByIdentification(String identification);
	
	public List<PropertyDTOWs> findPropertiesByIdentification(String identification);
	
	CadastralCertificateDTOWs getCadastralCertificateData(Long property_id);
}
