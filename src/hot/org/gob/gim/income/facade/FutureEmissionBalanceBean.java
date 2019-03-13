package org.gob.gim.income.facade; 
 
import java.util.ArrayList; 
import java.util.Date; 
import java.util.List; 
 
import javax.ejb.Stateless; 
import javax.persistence.EntityManager; 
import javax.persistence.PersistenceContext; 
import javax.persistence.Query; 
 
import org.gob.gim.common.NativeQueryResultsMapper; 
 
import ec.gob.gim.income.model.dto.FutureEmissionDTO; 
 
 
@Stateless(name = "FutureEmissionBalance") 
public class FutureEmissionBalanceBean implements FutureEmissionBalance{ 
 
  @PersistenceContext 
  EntityManager entityManager; 
     
  @Override 
  public List<FutureEmissionDTO> generateFutureEmissionBalance( 
      Date startDate, Date endDate) { 
   
    List<FutureEmissionDTO> futureList = new ArrayList(); 
    String query =   "SELECT " 
                +"COALESCE(anterior_emision.id,anticipado_formalizacion.id) as id, " 
                +"COALESCE(anterior_emision.Codigo,anticipado_formalizacion.Codigo) as Codigo, " 
                +"COALESCE(anterior_emision.Nombre,anticipado_formalizacion.Nombre) as Nombre, " 
                +"COALESCE(anterior_emision.saldo_anterior, 0.00) as saldo_anterior, " 
                +"COALESCE(anterior_emision.emision, 0.00) as emision, " 
                +"COALESCE(anticipado_formalizacion.pago_anticipado, 0.00) as pago_anticipado, " 
                +"COALESCE(anticipado_formalizacion.formalizacion, 0.00) as formalizacion, " 
                +"COALESCE(anticipado_formalizacion.pago_anticipado, 0.00) + COALESCE(anticipado_formalizacion.formalizacion, 0.00) as total_ant_form, " 
                +"(COALESCE(anterior_emision.saldo_anterior, 0.00) + COALESCE(anterior_emision.emision, 0.00)) - (COALESCE(anticipado_formalizacion.pago_anticipado, 0.00) + COALESCE(anticipado_formalizacion.formalizacion, 0.00)) as SALDO " 
             
        +"FROM " 
 
        +"(select COALESCE(qry_emision.id,qry_saldo_anterior.id) as id, " 
                +"COALESCE(qry_emision.accountCode,qry_saldo_anterior.accountCode) as Codigo, " 
                +"COALESCE(qry_emision.accountName,qry_saldo_anterior.accountName) as Nombre, " 
                +"COALESCE(qry_saldo_anterior.saldo_anterior, 0.00) as saldo_anterior, " 
                +"COALESCE(qry_emision.emision, 0.00) as emision " 
        +"FROM " 
          +"(select qry.id as id, " 
                  +"qry.accountCode, " 
                  +"qry.accountName, " 
                  +"sum(qry.total_emision) as emision " 
              +"from " 
              +"(select ac.id as id, " 
                  +"cast(ac.parameterFutureEmission AS json)->>'accountFutureCode' as accountCode, " 
                  +"cast(ac.parameterFutureEmission AS json)->>'accountFutureName' as accountName, " 
                  +"SUM(i.total) as total_emision " 
                +"from municipalbond mb " 
                +"INNER join item i ON i.municipalbond_id = mb.id " 
                +"INNER join entry e ON i.entry_id = e.id " 
                +"left join account ac ON e.account_id = ac.id " 
                +"LEFT JOIN statuschange sch ON sch.municipalbond_id = mb.id " 
                +"where mb.creationdate between :startDate AND :endDate " 
                +"and i.total > 0 " 
                +"and mb.municipalbondstatus_id not in (10,8) " 
                +"AND (mb.municipalBondStatus_id = 13 OR sch.explanation = 'FUTURA') " 
                +"GROUP BY ac.id, e.name,ac.accountCode,ac.parameterFutureEmission " 
                +"ORDER BY ac.accountCode) qry " 
          +"GROUP BY qry.id, qry.accountCode, qry.accountname " 
          +"ORDER BY qry.accountCode ASC) as qry_emision " 
 
        +"FULL OUTER JOIN " 
 
          +"(select qry.id as id, " 
                  +"qry.accountCode, " 
                  +"qry.accountName, " 
                  +"sum(qry.saldo_anterior) as saldo_anterior " 
            +"FROM " 
            +"(select ac.id as id, " 
                    +"cast(ac.parameterFutureEmission AS json)->>'accountFutureCode' as accountCode, " 
                    +"cast(ac.parameterFutureEmission AS json)->>'accountFutureName' as accountName, " 
                    +"SUM(i.total) as saldo_anterior " 
              +"from municipalbond mb " 
              +"INNER join item i ON i.municipalbond_id = mb.id " 
              +"INNER join entry e ON i.entry_id = e.id " 
              +"left join account ac ON e.account_id = ac.id " 
              +"LEFT JOIN statuschange sch ON sch.municipalbond_id = mb.id " 
              +"where mb.creationdate < :startDate " 
              +"and i.total > 0 " 
              +"and mb.municipalbondstatus_id not in (10,8) " 
              +"AND (mb.municipalBondStatus_id = 13 OR (sch.previousbondstatus_id = 13 and sch.date >= :startDate)) " 
              +"GROUP BY ac.id, e.name,ac.accountCode,ac.parameterFutureEmission " 
              +"ORDER BY ac.accountCode) qry " 
            +"GROUP BY qry.id, qry.accountCode, qry.accountname " 
            +"ORDER BY qry.accountCode ASC) as qry_saldo_anterior ON qry_saldo_anterior.id = qry_emision.id) AS anterior_emision " 
 
        +"FULL OUTER JOIN " 
 
 
        +"(select COALESCE(qry_formalizacion.id,qry_pago_anticipado.id) as id, " 
                +"COALESCE(qry_formalizacion.accountCode,qry_pago_anticipado.accountCode) as Codigo, " 
                +"COALESCE(qry_formalizacion.accountName,qry_pago_anticipado.accountName) as Nombre, " 
                +"COALESCE(qry_pago_anticipado.pago_anticipado, 0.00) as pago_anticipado, " 
                +"COALESCE(qry_formalizacion.formalizacion, 0.00) as formalizacion " 
          +"FROM " 
          +"(select   qry.id as id, " 
                    +"qry.accountCode, " 
                    +"qry.accountName, " 
                    +"sum(qry.formalizacion) as formalizacion " 
          +"FROM " 
              +"(select  ac.id as id, " 
                  +"cast(ac.parameterFutureEmission AS json)->>'accountFutureCode' as accountCode, " 
                  +"cast(ac.parameterFutureEmission AS json)->>'accountFutureName' as accountName, " 
                  +"SUM(i.total) as formalizacion " 
                +"from StatusChange sch " 
                +"INNER JOIN municipalbond mb ON mb.id = sch.municipalbond_id " 
                +"INNER join item i ON i.municipalbond_id = mb.id " 
                +"INNER join entry e ON i.entry_id = e.id " 
                +"left join account ac ON e.account_id = ac.id " 
                +"where mb.emisionDate Between :startDate AND :endDate " 
                +"and i.total > 0 " 
                +"AND sch.explanation = (select value from systemparameter where name = 'STATUS_CHANGE_FOMALIZE_EMISSION_EXPLANATION') " 
                +"GROUP BY ac.id, e.name,ac.accountCode,ac.parameterFutureEmission " 
                +"ORDER BY ac.accountCode) qry " 
            +"GROUP BY qry.id, qry.accountCode, qry.accountname " 
            +"ORDER BY qry.accountCode ASC) AS qry_formalizacion " 
 
        +"FULL OUTER JOIN " 
 
          +"(select   qry.id as id, " 
                  +"qry.accountCode, " 
                  +"qry.accountName, " 
                  +"sum(qry.pago_anticipado) as pago_anticipado " 
          +"from " 
              +"(select  ac.id as id, " 
                  +"cast(ac.parameterFutureEmission AS json)->>'accountFutureCode' as accountCode, " 
                  +"cast(ac.parameterFutureEmission AS json)->>'accountFutureName' as accountName, " 
                  +"SUM(i.total) as pago_anticipado " 
                +"from StatusChange sch " 
                +"INNER JOIN municipalbond mb ON mb.id = sch.municipalbond_id " 
                +"INNER join item i ON i.municipalbond_id = mb.id " 
                +"INNER join entry e ON i.entry_id = e.id " 
                +"left join account ac ON e.account_id = ac.id " 
                +"where mb.emisionDate Between :startDate AND :endDate " 
                +"and i.total > 0 " 
                +"and mb.municipalbondstatus_id not in (10,8) " 
                +"AND sch.explanation = (select value from systemparameter where name = 'STATUS_CHANGE_FUTURE_EMISSION_EXPLANATION') " 
                +"GROUP BY ac.id, e.name,ac.accountCode,ac.parameterFutureEmission " 
                +"ORDER BY ac.accountCode) qry " 
            +"GROUP BY qry.id, qry.accountCode, qry.accountname " 
            +"ORDER BY qry.accountCode ASC) as qry_pago_anticipado ON qry_pago_anticipado.id = qry_formalizacion.id) as anticipado_formalizacion ON anticipado_formalizacion.id = anterior_emision.id " 
        +"ORDER BY 2 ASC "; 
           
    Query q = this.entityManager.createNativeQuery(query); 
    q.setParameter("startDate", startDate); 
    q.setParameter("endDate", endDate); 
    futureList = NativeQueryResultsMapper.map(q.getResultList(), FutureEmissionDTO.class);   
    return futureList; 
  } 
   
   
 
} 
