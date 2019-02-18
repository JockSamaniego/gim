package org.gob.gim.income.facade; 
 
import java.util.Date; 
import java.util.List; 
 
import javax.ejb.Local; 
 
import ec.gob.gim.income.model.dto.FutureEmissionDTO; 
 
 
@Local 
public interface FutureEmissionBalance { 
 
  public String LOCAL_NAME = "/gim/FutureEmissionBalance/local"; 
     
  public List<FutureEmissionDTO>  generateFutureEmissionBalance(Date startDate, Date endDate); 
} 