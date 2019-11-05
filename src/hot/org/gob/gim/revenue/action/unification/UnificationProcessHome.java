package org.gob.gim.revenue.action.unification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.unification.TableFK;
import ec.gob.gim.revenue.model.unification.UnificationProcess;
import ec.gob.gim.revenue.model.unification.UnificationType;

@Name("unificationProcessHome")
@Scope(ScopeType.CONVERSATION)
public class UnificationProcessHome extends EntityHome<UnificationProcess>
		implements Serializable {

	@In
	private FacesMessages facesMessages;

	private static final long serialVersionUID = 1L;

	@In
	UserSession userSession;

	private String criteria;

	private String criteriaEntry;
	private String identificationNumber;
	private String identificationNumberDoneBy;
	private Long residentId;
	private List<Resident> residents;

	private Resident r1;
	private Resident r2;
	private List<TableFK> relations;
	private List<TableFK> r1Count;
	private List<TableFK> r2Count;
	boolean isFirsttime = true;
		
	private StringBuilder stringSqlBuilder;
	
	public static final String ROLE_NORMAL_UNIFICATION = "ROLE_NORMAL_UNIFICATION";
	public static final String ROLE_WATER_UNIFICATION = "ROLE_WATER_UNIFICATION";
	
	private SystemParameterService systemParameterService;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private Boolean hasNormalUnificationRole;
	private Boolean hasWaterUnificationRole;

	public void setUnificationProcessId(Long id) {
		setId(id);
	}

	public Long getUnificationProcessId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if (isFirsttime) {
			loadFK();
			this.getInstance().setPerformChange(this.userSession.getPerson());
			stringSqlBuilder = new StringBuilder();
			//identifyRole();
		}
	}

	public boolean isWired() {
		return true;
	}

	public UnificationProcess getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}	

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getIdentificationNumberDoneBy() {
		return identificationNumberDoneBy;
	}

	public void setIdentificationNumberDoneBy(String identificationNumberDoneBy) {
		this.identificationNumberDoneBy = identificationNumberDoneBy;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public Resident getR1() {
		return r1;
	}

	public void setR1(Resident r1) {
		this.r1 = r1;
	}

	public Resident getR2() {
		return r2;
	}

	public void setR2(Resident r2) {
		this.r2 = r2;
	}

	public boolean isFirsttime() {
		return isFirsttime;
	}

	public void setFirsttime(boolean isFirsttime) {
		this.isFirsttime = isFirsttime;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	@SuppressWarnings("unchecked")
	public Resident searchResidentById() {
		if (this.getResidentId() != null) {
			Query query = getEntityManager().createNamedQuery("Resident.findById");
			query.setParameter("id", this.getResidentId());
			List<?> list = query.getResultList();
			return (list != null ? (Resident) list.get(0) : null);
		}
		return null;
	}

	public void searchResident() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setR1(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}
		} catch (Exception e) {
			this.setR1(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void searchResidentDoneBy() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber",this.identificationNumberDoneBy);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setR2(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.setR2(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setR1(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

	public void doneBySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setR2(resident);
		this.setIdentificationNumberDoneBy(resident.getIdentificationNumber());
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	/**
	 * cargar las relaciones fk de resident
	 */
	public void loadFK() {
		relations = new ArrayList<TableFK>();
		String sentence = "SELECT tc.table_schema, tc.constraint_name, tc.table_name, kcu.column_name, ccu.table_name "
				+ "AS foreign_table_name, ccu.column_name AS foreign_column_name "
				+ "FROM information_schema.table_constraints tc "
				+ "JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name "
				+ "JOIN information_schema.constraint_column_usage ccu ON ccu.constraint_name = tc.constraint_name "
				+ "WHERE constraint_type = 'FOREIGN KEY' "
				+ "AND ccu.table_name= 'resident' order by tc.table_name";
		Query q = this.getEntityManager().createNativeQuery(sentence);
		@SuppressWarnings("unchecked")
		List<Object[]> objs = q.getResultList();

		TableFK tfk = null;
		//checkingrecord,deposit,payment,journal,logunification
		for (Object[] obj : objs) {
			String tableName=obj[2].toString();
			if(!tableName.equals("checkingrecord") && !tableName.equals("deposit")
					&& !tableName.equals("payment") && !tableName.equals("journal")
					&& !tableName.equals("logunification")){
				tfk = new TableFK();
				tfk.setTc_table_schema(obj[0].toString());
				tfk.setTc_constraint_name(obj[1].toString());
				tfk.setTc_table_name(tableName);
				tfk.setKcu_column_name(obj[3].toString());
				tfk.setCcu_table_name(obj[4].toString());
				relations.add(tfk);
				//System.out.println(obj[2].toString());	
			}
			
		}
		isFirsttime = false;
		//System.out.println("------relaciones: " + relations.size());
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
	}
		
	public void startCountingR1(){
		r1Count = new ArrayList<TableFK>();
		if (r1 != null) {
			Query q;
			for(TableFK table: relations){
				String sentence = "select count(*) from " + table.getTc_table_schema()+"."+table.getTc_table_name() + " where "+ table.getKcu_column_name() + " = " + r1.getId();
				q = this.getEntityManager().createNativeQuery(sentence);
				if (q.getSingleResult() != null) {
					double count = Double.parseDouble(q.getSingleResult().toString());
					if (count > 0) {
						table.setCount(count);
						table.setResident_id(r1.getId());
						r1Count.add(table);
					}
					//System.out.println(table.getTc_table_name()+" con "+q.getSingleResult());
				}
			}	
		}		
	}
	
	public void startCountingR2(){
		r2Count = new ArrayList<TableFK>();
		if (r1 != null) {
			Query q;
			for(TableFK table: relations){
				String sentence = "select count(*) from " + table.getTc_table_schema()+"."+table.getTc_table_name() + " where "+ table.getKcu_column_name() + " = " + r2.getId();
				q = this.getEntityManager().createNativeQuery(sentence);
				if (q.getSingleResult() != null) {
					double count = Double.parseDouble(q.getSingleResult().toString());
					if (count > 0) {
						table.setCount(count);
						table.setResident_id(r2.getId());
						r2Count.add(table);
					}
					//System.out.println(table.getTc_table_name()+" con "+q.getSingleResult());
				}
			}
		}
	}
	
	public List<TableFK> getR1Count() {
		return r1Count;
	}

	public void setR1Count(List<TableFK> r1Count) {
		this.r1Count = r1Count;
	}

	public List<TableFK> getR2Count() {
		return r2Count;
	}

	public void setR2Count(List<TableFK> r2Count) {
		this.r2Count = r2Count;
	}
	
	@In
	EntityManager entityManager;

	public void updateToLeft() {
		String sentence = "";
		Query q;
		int value=0;
		for (TableFK table : r2Count) {
			//if(table.getTc_table_name().equals("municipalbond")){
			sentence = "update "+table.getTc_table_schema()+"."+table.getTc_table_name()+" set "+table.getKcu_column_name()+" = "+r1.getId()+" where "+table.getKcu_column_name()+" = "+r2.getId();
			stringSqlBuilder.append(sentence+"\t").append(table.getCount()+"\n");
			q = entityManager.createNativeQuery(sentence);
			value= q.executeUpdate();
			//System.out.println(">>>>>>>>> " + sentence+" "+value);
			//}
		}
		entityManager.flush();
		this.getInstance().setUnifiedResdient(r1);
		this.getInstance().setPreviousResidentId(r2.getId());
		
		startCountingR1();
		startCountingR2();
	}
	
	public void updateToRight() {
		String sentence = "";
		Query q;
		int value=0;
		for (TableFK table : r1Count) {
			sentence = "update "+table.getTc_table_schema()+"."+table.getTc_table_name()+" set "+table.getKcu_column_name()+" = "+r2.getId()+" where "+table.getKcu_column_name()+" = "+r1.getId();
			stringSqlBuilder.append(sentence+"\t").append(table.getCount()+"\n");
			q = entityManager.createNativeQuery(sentence);
			value= q.executeUpdate();
			//System.out.println(">>>>>>>>> " + sentence+" "+value);
		}
		entityManager.flush();
		this.getInstance().setUnifiedResdient(r2);
		this.getInstance().setPreviousResidentId(r1.getId());
		
		startCountingR1();
		startCountingR2();
	}
	
	public void deleteLeft(){
		try{
			this.getInstance().setResdientDeletedId(r2.getId());
			Query q = entityManager.createNativeQuery("delete from gimprod.resident where id = "+ r2.getId());
			int value = q.executeUpdate();
			stringSqlBuilder.append("delete from gimprod.resident where id = "+ r2.getId()).append("\t");
			//System.out.println(">>>>>>>>> borrado id= "+r2.getId()+" con value= "+value);
		}catch(Exception e){
			//System.out.println(">>>>>>>>> borrando... error ");
			e.printStackTrace();
		}
	}
	
	public void deleteRight(){
		try{
			this.getInstance().setResdientDeletedId(r1.getId());
			Query q = entityManager.createNativeQuery("delete from gimprod.resident where id = "+ r1.getId());
			int value = q.executeUpdate();
			stringSqlBuilder.append("delete from gimprod.resident where id = "+ r1.getId()).append("\t");
			//System.out.println(">>>>>>>>> borrado id= "+r1.getId()+" con value= "+value);
		}catch(Exception e){
			//System.out.println(">>>>>>>>> borrando... error ");
			e.printStackTrace();
		}
	}
	
	@Override
	public String persist() {
		this.getInstance().setPerformedSql(stringSqlBuilder.toString());
		this.getInstance().setUnificationType(UnificationType.NORMAL);
		return super.persist();
	}

	public Boolean getHasNormalUnificationRole() {
		if(hasNormalUnificationRole != null) return hasNormalUnificationRole;
		if(systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String role_n = systemParameterService.findParameter(UnificationProcessHome.ROLE_NORMAL_UNIFICATION);
		if (role_n == null) {
			hasNormalUnificationRole = false;
		} else {
			hasNormalUnificationRole = userSession.getUser().hasRole(role_n);
		}
		return hasNormalUnificationRole;
	}

	public void setHasNormalUnificationRole(Boolean hasNormalUnificationRole) {
		this.hasNormalUnificationRole = hasNormalUnificationRole;
	}

	public Boolean getHasWaterUnificationRole() {
		if(hasWaterUnificationRole != null) return hasWaterUnificationRole;
		if(systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		String role_w = systemParameterService.findParameter(UnificationProcessHome.ROLE_WATER_UNIFICATION);
		if (role_w == null) {
			hasWaterUnificationRole = false;
		} else {
			hasWaterUnificationRole = userSession.getUser().hasRole(role_w);
		}
		return hasWaterUnificationRole;
	}

	public void setHasWaterUnificationRole(Boolean hasWaterUnificationRole) {
		this.hasWaterUnificationRole = hasWaterUnificationRole;
	}
	/////////////////////////// unificacion de agua
	private String waterService;
	
	public void startCountingByWaterService(){
		if(waterService!=null){
			r1Count = new ArrayList<TableFK>();
			TableFK table = new TableFK();
			Query q;
			String sentence = "select count(*) from gimprod.municipalbond where groupingcode = '" + this.waterService+"' and "
					+ "municipalbondstatus_id= 3";
			q = this.getEntityManager().createNativeQuery(sentence);
			if (q.getSingleResult() != null) {
				double count = Double.parseDouble(q.getSingleResult().toString());
				if (count > 0) {
					table.setTc_table_name("municipalbond");
					table.setKcu_column_name("resident_id");
					table.setCount(count);
					//table.setResident_id(r1.getId());
					r1Count.add(table);
				}
				//System.out.println(table.getTc_table_name()+" con "+q.getSingleResult());
			}	
		}		
	}
	
	public void updateWaterSupply() {
		String sentence = "";
		Query q;
		int value=0;
			sentence = "update gimprod.municipalbond set resident_id = "+r2.getId()+" where municipalbondstatus_id=3 and groupingcode = '"+waterService+"'";
			stringSqlBuilder.append(sentence+"\t").append(r1Count.get(0).getCount()+"\n");
			q = entityManager.createNativeQuery(sentence);
			value= q.executeUpdate();
			//System.out.println(">>>>>>>>> " + sentence+" "+value);
		entityManager.flush();
		this.getInstance().setUnifiedResdient(r2);
		//this.getInstance().setPreviousResidentId(r1.getId());
		
		startCountingByWaterService();
		startCountingRightByWaterService();
	}
	
	public void startCountingRightByWaterService(){
		if(waterService!=null){
			r2Count = new ArrayList<TableFK>();
			TableFK table = new TableFK();
			Query q;
			String sentence = "select count(*) from gimprod.municipalbond where groupingcode = '" + this.waterService+"' and "
					+ "municipalbondstatus_id= 3";
			q = this.getEntityManager().createNativeQuery(sentence);
			if (q.getSingleResult() != null) {
				double count = Double.parseDouble(q.getSingleResult().toString());
				if (count > 0) {
					table.setTc_table_name("municipalbond");
					table.setKcu_column_name("resident_id");
					table.setCount(count);
					//table.setResident_id(r1.getId());
					r2Count.add(table);
				}
				//System.out.println(table.getTc_table_name()+" con "+q.getSingleResult());
			}	
		}		
	}

	public String getWaterService() {
		return waterService;
	}

	public void setWaterService(String waterService) {
		this.waterService = waterService;
	}
	
	public String persistWS() {
		this.getInstance().setPerformedSql(stringSqlBuilder.toString());
		this.getInstance().setUnificationType(UnificationType.WATERSERVICE);
		return super.persist();
	}

}
