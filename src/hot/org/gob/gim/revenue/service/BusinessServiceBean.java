package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.commercial.model.FireRates;

@Stateless(name = "BusinessService")
public class BusinessServiceBean implements BusinessService {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@EJB
	CrudService crudService;

	@Override
	public Business save(Business business) throws Exception {
		return crudService.create(business);
	}

	@Override
	public Business update(Business business) throws Exception {
		return crudService.update(business);
	}
	
	@Override
	public List<Business> listLocals(Long residentId) {

		String qry = "SELECT bu from Business bu where bu.owner.id = ?1 and bu.isActive is true";

		Query query = entityManager.createQuery(qry);
		query.setParameter(1, residentId);

		/*List<EmissionOrderDTO> retorno = NativeQueryResultsMapper.map(
			query.getResultList(), EmissionOrderDTO.class);*/
		List<Business> resultList = query.getResultList();
		return resultList;
		
	}

	@Override
	public List<FireRates> searchFireRates(String criteria) {
		String qry = "SELECT fr from FireRates fr where lower(fr.activity) like ?1 order by fr.activity ";

		Query query = entityManager.createQuery(qry);
		query.setParameter(1, criteria.toLowerCase());

		return query.getResultList();
	}

}
