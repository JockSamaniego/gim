package org.gob.gim.contravention.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.contravention.model.Provenance;

@Name("provenanceList")
public class ProvenanceList extends EntityQuery<Provenance> {

	private static final String EJBQL = "select provenance from Provenance provenance";

	private static final String[] RESTRICTIONS = {"lower(provenance.name) like lower(concat(#{provenanceList.provenance.name},'%'))",};

	private Provenance provenance = new Provenance();

	public ProvenanceList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Provenance getProvenance() {
		return provenance;
	}
}