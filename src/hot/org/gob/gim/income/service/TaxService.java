package org.gob.gim.income.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.income.model.Tax;

@Local
public interface TaxService {

	List<Tax> findByEntryId(Long entryId);
	//cambio iva 14%  a 12%
	List<Tax> findByMunicipalBondId(Long municipalBondId);
}
