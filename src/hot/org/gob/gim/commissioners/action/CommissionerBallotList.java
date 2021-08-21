package org.gob.gim.commissioners.action;

import ec.gob.gim.commissioners.model.CommissionerBallot;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

@Name("commissionerBallotList")
public class CommissionerBallotList extends EntityQuery<CommissionerBallot> {
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select commissionerBallot from CommissionerBallot commissionerBallot";

	private static final String[] RESTRICTIONS = {
		"commissionerBallot.commissionerBallotType.id = #{commissionerBallotList.commissionerType}",
		"lower(commissionerBallot.ballotNumber) like lower(concat('%',#{commissionerBallotList.commissionerBallot.ballotNumber},'%'))",
		"lower(commissionerBallot.plate) like lower(concat('%',#{commissionerBallotList.commissionerBallot.plate},'%'))",
		"commissionerBallot.sanctioningArticle.id = #{commissionerBallotList.articleId}",
		"commissionerBallot.currentStatus.statusName.id = #{commissionerBallotList.statusId}",
		"lower(commissionerBallot.infractorName) like lower(concat('%',#{commissionerBallotList.commissionerBallot.infractorName},'%'))",
		"lower(commissionerBallot.infractorIdentification) like lower(concat('%',#{commissionerBallotList.commissionerBallot.infractorIdentification},'%'))",
		"lower(commissionerBallot.inspectorIdentification) like lower(concat('%',#{commissionerBallotList.commissionerBallot.inspectorIdentification},'%'))",
		"lower(commissionerBallot.inspectorName) like lower(concat('%',#{commissionerBallotList.commissionerBallot.inspectorName},'%'))",
		"lower(commissionerBallot.plate) like lower(concat('%',#{commissionerBallotList.commissionerBallot.plate},'%'))",
		
		
		"DATE(commissionerBallot.infractionDate) >= DATE(#{commissionerBallotList.dateFrom})",
		"DATE(commissionerBallot.infractionDate) <= DATE(#{commissionerBallotList.dateUntil})",
		
		
		"DATE(commissionerBallot.creationDate) >= DATE(#{commissionerBallotList.dateFrom2})",
		"DATE(commissionerBallot.creationDate) <= DATE(#{commissionerBallotList.dateUntil2})",};

	private CommissionerBallot commissionerBallot = new CommissionerBallot();
	private Long articleId;
	private Long statusId;
	private Date dateFrom;
	private Date dateUntil;
	private Date dateFrom2;
	private Date dateUntil2;
	private Long commissionerType;


	public CommissionerBallotList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("commissionerBallot.creationDate");
		setOrderDirection("desc");
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	public CommissionerBallot getCommissionerBallot() {
		return commissionerBallot;
	}

	public void setCommissionerBallot(CommissionerBallot commissionerBallot) {
		this.commissionerBallot = commissionerBallot;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateUntil() {
		return dateUntil;
	}

	public void setDateUntil(Date dateUntil) {
		this.dateUntil = dateUntil;
	}

	public Date getDateFrom2() {
		return dateFrom2;
	}

	public void setDateFrom2(Date dateFrom2) {
		this.dateFrom2 = dateFrom2;
	}

	public Date getDateUntil2() {
		return dateUntil2;
	}

	public void setDateUntil2(Date dateUntil2) {
		this.dateUntil2 = dateUntil2;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
		
	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	
	public Long getCommissionerType() {
		return commissionerType;
	}

	public void setCommissionerType(Long commissionerType) {
		this.commissionerType = commissionerType;
	}

	public void findInfractorName(){
		if(this.commissionerBallot.getInfractorIdentification() != null && this.commissionerBallot.getInfractorIdentification() != ""){
			List<String> names = new ArrayList<String>();
			Query query = getEntityManager().createNamedQuery(
					"commissionerBallot.findResidentNameByIdent");
			query.setParameter("identNum", this.commissionerBallot.getInfractorIdentification());
			names = query.getResultList();
			if(names.size()>0){
				this.commissionerBallot.setInfractorName(names.get(0));
			}else{
				this.commissionerBallot.setInfractorName("No registrado");	
			}
		}else{
			this.commissionerBallot.setInfractorName(null);
		}	
	}
	
	public void findInspectorName(){
		if(this.commissionerBallot.getInspectorIdentification() != null && this.commissionerBallot.getInspectorIdentification() != ""){
			List<String> names = new ArrayList<String>();
			Query query = getEntityManager().createNamedQuery(
					"commissionerBallot.findResidentNameByIdent");
			query.setParameter("identNum", this.commissionerBallot.getInspectorIdentification());
			names = query.getResultList();
			if(names.size()>0){
				this.commissionerBallot.setInspectorName(names.get(0));
			}else{
				this.commissionerBallot.setInspectorName("No registrado");	
			}
		}else{
			this.commissionerBallot.setInspectorName(null);
		}	
	}

}
