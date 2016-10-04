/**
 * 
 */
package org.gob.gim.common.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.exception.IdentificationNumberExistsException;
import org.gob.gim.common.exception.NonUniqueIdentificationNumberException;
import org.gob.gim.common.exception.ResidentRegisteredAsLegalPersonException;
import org.gob.gim.common.exception.ResidentRegisteredAsNaturalPersonException;

import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.common.model.LegalEntity;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

/**
 * @author wilman
 *
 */
@Stateless(name="ResidentService")
public class ResidentServiceBean implements ResidentService {
	
	@EJB
	CrudService crudService;
	
	@PersistenceContext
    private EntityManager em;
	
	public static ArrayList<Integer> publicInstitutions = new ArrayList<Integer>();

	/* (non-Javadoc)
	 * @see org.gob.gim.common.service.ResidentService#find(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Resident find(Long id) {
		Map<String, Long> parameters = new HashMap<String, Long>();
		parameters.put("id", id);
		List<Resident> result = crudService.findWithNamedQuery("Resident.findById", parameters, 1);
		if (!result.isEmpty())
			return result.get(0);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.service.ResidentService#find(java.lang.String)
	 */
	@Override
	public Resident find(String identificationNumber) throws NonUniqueIdentificationNumberException {		
		try{
			Query query = em.createNamedQuery("Resident.findByIdentificationNumber");
			query.setParameter("identificationNumber", identificationNumber);
			return (Resident)query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}catch(NonUniqueResultException e){
			throw new NonUniqueIdentificationNumberException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resident> findByCriteria(String criteria) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("criteria", criteria);
		return crudService.findWithNamedQuery("Resident.findByCriteria", parameters);
	}
		
	@Override
	public void verifyUniqueness(Long id, String identificationNumber, IdentificationType identificationType) throws IdentificationNumberExistsException{		
		try{
//			System.out.println("DREAM SENSATION ---->  identificationNumber "+identificationNumber);
//			System.out.println("DREAM SENSATION ---->  identificationType "+identificationType);
			Resident resident = null;
			
			if(id==null){
				resident = find(identificationNumber);
			} else {
				resident = findByDistinctId(id, identificationNumber);
			}
			
//			System.out.println("DREAM SENSATION ---->  resident: "+resident );
			if (resident != null) {
				if(id != resident.getId()){
					throw new IdentificationNumberExistsException();
				}
			} else {
				String prefix = identificationNumber;
				if(identificationNumber != null && identificationNumber.length() >= 10){
					prefix = identificationNumber.substring(0, 10) + "%";
				}
				
				if(id==null){
					if(identificationType == IdentificationType.NATIONAL_IDENTITY_DOCUMENT){
						resident = find(prefix);
					}
				} else {
					resident = findByDistinctId(id, prefix);
				}
				
				if(resident != null && resident.getId() != id){
					if(resident.getClass() == Person.class){
						throw new ResidentRegisteredAsNaturalPersonException();
					} 
					if(resident.getClass() == LegalEntity.class){
						throw new ResidentRegisteredAsLegalPersonException();
					}
				}
			}
		}catch(NonUniqueIdentificationNumberException e){
			throw new IdentificationNumberExistsException();
		}		
	}
	
	@SuppressWarnings("unchecked")
	private Resident findByDistinctId(Long id, String identificationNumber) throws NonUniqueIdentificationNumberException {
		try{
			Query query = em.createNamedQuery("Resident.findDistinctByIdentificationNumberAndId");
			query.setParameter("identificationNumber", identificationNumber);
			query.setParameter("id", id);
			List<Resident> residents = query.getResultList();
			if(residents.size() > 0){
				return residents.get(0);
			}
			return null;
		}catch(NonUniqueResultException e){
			throw new NonUniqueIdentificationNumberException();
		}
	}	

	@Override
	public void save(Resident resident) throws IdentificationNumberExistsException{		
		System.out.println("=======> INGRESO A GUARDAR CONTRIBUYENTE");
		if(resident.getId() != null){
			try{
				Resident r = find(resident.getIdentificationNumber());
				if (r != null){
					if (r.getId().longValue() == resident.getId().longValue()){
						crudService.update(resident);
						System.out.println("=======> ACTUALIZADO CONTRIBUYENTE CORRECTAMENTE con ids true, DNI:  " + resident.getIdentificationNumber());
					}
					else
						throw new IdentificationNumberExistsException();
				}else{
					crudService.update(resident);
					System.out.println("=======> ACTUALIZADO CONTRIBUYENTE CORRECTAMENTE: " + resident.getIdentificationNumber());
					//throw new IdentificationNumberExistsException();
				}
			}catch(NonUniqueIdentificationNumberException e){
				throw new IdentificationNumberExistsException();
			}
		}else{
			//System.out.println("=======> INGRESO A INSERTAR CONTRIBUYENTE");
			if(resident.getIdentificationType() == IdentificationType.GENERATED_NUMBER){
				Query query = em.createNativeQuery("SELECT nextval('residentNumber')");
				String generatedNumber = ((BigInteger)query.getResultList().get(0)).toString();
				resident.setIdentificationNumber(generatedNumber);
			} else {
				verifyUniqueness(resident.getId(), resident.getIdentificationNumber(), resident.getIdentificationType());
			}
			crudService.create(resident);
			System.out.println("=======> INSERTADO CONTRIBUYENTE : " + resident.getIdentificationNumber());
		}
	}

	@Override
	public Boolean isPublicInstitution(Long residentId) {
		// TODO Auto-generated method stub
		
		if (this.publicInstitutions.contains(residentId.intValue())){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
		
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.service.ResidentService#loadPublicInstitutions()
	 */
	@Override
	public void loadPublicInstitutions() {
		// TODO Auto-generated method stub
		
		
		this.publicInstitutions = new ArrayList<Integer>();
		
		Query query = this.em.createNativeQuery("select CAST ( id AS integer ) " 
				+"from resident " 
				+"where residenttype = 'J' " 
				+"and legalentityType='PUBLIC' ");
		
		List<Integer> ids = query.getResultList();
		
		
		Integer[] idsInstitution = new Integer[ids.size()];
		
		for (int i = 0; i < ids.size(); i++) {
			//System.out.println(ids.get(i).intValue());
			idsInstitution[i] = ids.get(i);
		}
		
		
		this.publicInstitutions = new ArrayList<Integer> (Arrays.asList(idsInstitution));
		//System.out.println("Entra al metodo de inicializar instituciones publicas");
		
	}

	/* (non-Javadoc)
	 * @see org.gob.gim.common.service.ResidentService#updatePublicInstitutions()
	 */
	@Override
	public void updatePublicInstitutions() {
		// TODO Auto-generated method stub
		
		this.publicInstitutions = new ArrayList<Integer>();
		
		Query query = this.em.createNativeQuery("select CAST ( id AS integer ) " 
				+"from resident " 
				+"where residenttype = 'J' " 
				+"and legalentityType='PUBLIC' ");
		
		List<Integer> ids = query.getResultList();
		
		
		Integer[] idsInstitution = new Integer[ids.size()];
		
		for (int i = 0; i < ids.size(); i++) {
			//System.out.println(ids.get(i).intValue());
			idsInstitution[i] = ids.get(i);
		}
		
		
		this.publicInstitutions = new ArrayList<Integer> (Arrays.asList(idsInstitution));

		
	}

}
