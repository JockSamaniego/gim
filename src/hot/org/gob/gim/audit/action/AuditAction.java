package org.gob.gim.audit.action;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.audit.model.AuditRequest;
import ec.gob.gim.audit.model.AuditType;
import ec.gob.gim.audit.model.AuditedEntity;
import ec.gob.gim.audit.model.AuditedField;
import ec.gob.gim.audit.model.AuditedRecord;

@Name("auditAction")
@Scope(ScopeType.CONVERSATION)
public class AuditAction extends EntityController {
	
	private static final long serialVersionUID = 1L;
	private static String SELECT_CLAUSE_PREFIX   = " SELECT e.id, r.timestamp as revisionDate, e.revtype, r.username";
	private static String FROM_CLAUSE_PREFIX     = " FROM Revision r, ";
	private static String WHERE_CLAUSE_PREFIX    = " WHERE r.id = e.rev AND CAST(r.timestamp as date) BETWEEN :startDate AND :endDate ";
	private static String ORDER_BY_CLAUSE_PREFIX = " ORDER BY e.id, revisionDate ASC";
	
	private static final int STATIC_COLUMNS = 4;

	private AuditType auditType;
	private List<String> columnNames = new LinkedList<String>();
	private List<AuditedEntity> entities;
	private List<AuditedRecord> auditRecords;
	private List<AuditedField> reportedFields = new LinkedList<AuditedField>();;
	private AuditedEntity auditedEntity;
	private Long requestedId;
	private Date startDate;
	private Date endDate;
	
	@Logger
	Log logger;
	
	@SuppressWarnings("unchecked")
	public String findAuditRecords(){
		for(AuditedField f : reportedFields){
			System.out.println(f.getIsReported()+"   "+f.getName());
		}
		saveAuditRequest();
		EntityManager entityManager = getEntityManager();
		String queryString = buildNativeQuery();
		Query query = entityManager.createNativeQuery(queryString);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);		
		List<Object[]> results = query.getResultList();		
		auditRecords = buildAuditRecords(results);
		return null;
	}
	
	private void saveAuditRequest(){
	
		AuditRequest audit = new AuditRequest();
		audit.setStartDate(startDate);
		audit.setEndDate(endDate);
		audit.setAuditType(this.auditType == null ? null : this.auditType.name());
		audit.setAuditedEntity(this.auditedEntity == null ? null : this.auditedEntity.getName());
		audit.setIdentifier(this.requestedId == null ? null : this.requestedId.toString());
		//getEntityManager().joinTransaction();
		getEntityManager().persist(audit);
		getEntityManager().flush();

	}
	
	
	private List<AuditedRecord> buildAuditRecords(List<Object[]> results){
		List<AuditedRecord> ars =  new LinkedList<AuditedRecord>();
		for(Object[] row : results){
			AuditedRecord ar = new AuditedRecord();
			ar.setId(((BigInteger)row[0]).longValue());
			ar.setDate((Date)row[1]);
			ar.setTime((Date)row[1]);
			ar.setTransactionType(((Short)row[2]).intValue());
			ar.setUsername(row[3].toString());			
			int columns = row.length;
			
			for(int i=STATIC_COLUMNS; i<columns; i++){
				ar.addValue(columnNames.get(i-STATIC_COLUMNS), row[i]);
			}
			System.out.println();
			ars.add(ar);
		}
		return ars;
	}
	
	public List<String> getColumnNames(){
		return columnNames;
	}	
	
	private String buildNativeQuery() {
		StringBuilder builder = new StringBuilder();
		builder.append(getProjectionClause());
		builder.append(getFromClause());
		builder.append(getWhereClause());
		builder.append(ORDER_BY_CLAUSE_PREFIX);
		return builder.toString();
	}
	
	private String getFromClause(){
		return FROM_CLAUSE_PREFIX+" gimaudit."+auditedEntity.getEntityName()+" e ";
	}
	
	private String getWhereClause(){
		StringBuilder builder = new StringBuilder(WHERE_CLAUSE_PREFIX);
		if (requestedId != null){
			builder.append("AND e.id = "+requestedId.toString());
		}
		return builder.toString();
	}
	
	private String getProjectionClause(){
		StringBuilder builder = new StringBuilder(SELECT_CLAUSE_PREFIX);
		if(reportedFields != null && reportedFields.size() > 0){
			columnNames.clear();
			int index = 0;
			for(AuditedField field : reportedFields){
				if(field.getIsReported()){
					builder.append(", e."+field.getName());
					columnNames.add(index, field.getLabel());
					index++;
				}
			}
			return builder.toString();
		}
		return builder.toString();
	}
	
	
	public List<AuditedField> getReportedFields() {
		return reportedFields;
	}
	
	@SuppressWarnings("unchecked")
	public void findEntities(){
		Query query = getEntityManager().createNamedQuery("AuditedEntity.findByAuditType");
		query.setParameter("auditType", auditType);
		entities = query.getResultList();
		auditedEntity = null;
		reportedFields.clear();
		clearResults();
	}
	
	/**
	 * Metodo especifico para PostgreSQL 
	 */
	@SuppressWarnings("unchecked")
	public void findFields(){
		//String fieldsQuery = "select column_name from information_schema.columns where table_schema = 'gimprod' and table_name=:tableName";
		String fieldsQuery = "SELECT c.table_schema, c.table_name, c.column_name, pgd.description " +
							 " FROM pg_catalog.pg_statio_all_tables AS st " +
							 " INNER JOIN pg_catalog.pg_description pgd ON (pgd.objoid = st.relid) " +
							 " INNER JOIN information_schema.columns c ON (pgd.objsubid = c.ordinal_position " +
							 "                                             AND c.table_schema = st.schemaname " +
							 "                                             AND c.table_name = st.relname " +
							 "                                             AND c.table_name = :tableName)";
		String auditEntityName = auditedEntity.getEntityName().toLowerCase();
		String tableName = auditEntityName;
		if(auditEntityName != null && auditEntityName.endsWith("_aud")){
			tableName = auditEntityName.substring(0, auditEntityName.lastIndexOf("_aud"));
		}
		Query query = getEntityManager().createNativeQuery(fieldsQuery);
		query.setParameter("tableName", tableName);
		List<Object[]> fields = query.getResultList();
		
		System.out.println(columnNames.getClass());
		
		reportedFields.clear();
		clearResults();
		for(Object[] field : fields){
			AuditedField af = new AuditedField();
			af.setName(field[2].toString());
			af.setLabel(field[3].toString());
			reportedFields.add(af);
		}
	}

	public static void main(String[] args) {
		AuditAction action = new AuditAction();
		//action.setEntityName("_User_AUD");
		action.setRequestedId(15L);
		//List<String> reportedFields = action.getReportedFields();
		//reportedFields.add("username");
		//reportedFields.add("password");
		System.out.println(action.buildNativeQuery());
	}


	public List<AuditedRecord> getAuditRecords() {
		return auditRecords;
	}


	public void setAuditRecords(List<AuditedRecord> auditRecords) {
		this.auditRecords = auditRecords;
	}

	public Long getRequestedId() {
		return requestedId;
	}

	public void setRequestedId(Long requestedId) {
		this.requestedId = requestedId;
	}

	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public void setReportedFields(List<AuditedField> reportedFields) {
		this.reportedFields = reportedFields;
	}
	
	public AuditType getAuditType() {
		return auditType;
	}


	public void setAuditType(AuditType auditType) {
		this.auditType = auditType;
	}

	
	@Factory("auditTypes")
	public List<AuditType> findAuditTypes(){
		return Arrays.asList(AuditType.values());
	}


	public List<AuditedEntity> getEntities() {
		return entities;
	}


	public void setEntities(List<AuditedEntity> entities) {
		this.entities = entities;
	}


	public AuditedEntity getAuditedEntity() {
		return auditedEntity;
	}


	public void setAuditedEntity(AuditedEntity auditedEntity) {
		this.auditedEntity = auditedEntity;
	}

	public String printAuditReport(){
		return "sentToPrint";
	}
	
	public String confirmPrinting(){
		return "printed";
	}
	
	public void clearResults(){
		if(auditRecords!=null){
			auditRecords.clear();
		}
	}
	
	public String getTransactionTypeKey(Integer value){
		return "TT"+value;
	}
	
}
