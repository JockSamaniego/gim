package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.impugnment.Impugnment;
import ec.gob.gim.revenue.model.impugnment.criteria.ImpugnmentSearchCriteria;

/**
 *  * @author René Ortega
 *  * @Fecha 2016-07-14
 */

@Local
public interface ImpugnmentService {

	public String LOCAL_NAME = "/gim/ImpugnmentService/local";

	public List<Impugnment> findImpugnments(Integer numberProsecution, Integer numberInfringement, Integer firstRow,
			Integer numberOfRows);
	
	public Long findImpugnmentsNumber(Integer numberProsecution, Integer numberInfringement);
	
	public Impugnment save(Impugnment impugnment);
	
	public Impugnment update(Impugnment impugnment);
	
	public List<Impugnment> findAll();
	
	public MunicipalBond findMunicipalBondForImpugnment(String numberInfringement);
	
	public List<Impugnment> findImpugnmentsForCriteria(ImpugnmentSearchCriteria criteria);
	
	public Impugnment findById(Long impugnmentId);
	
	public MunicipalBond findMunicipalBondByNumber(Long municipalBondNumber);
	
}
