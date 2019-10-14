package org.gob.gim.income.view;

import java.math.BigDecimal;
import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class OverduePortfolio
{
  @NativeQueryResultColumn(index=0)
  private Long id;
  @NativeQueryResultColumn(index=1)
  private String identification;
  @NativeQueryResultColumn(index=2)
  private String names;
  @NativeQueryResultColumn(index=3)
  private String email;
  @NativeQueryResultColumn(index=4)
  private BigDecimal total;
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getIdentification()
  {
    return this.identification;
  }
  
  public void setIdentification(String identification)
  {
    this.identification = identification;
  }
  
  public String getNames()
  {
    return this.names;
  }
  
  public void setNames(String names)
  {
    this.names = names;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  public BigDecimal getTotal()
  {
    return this.total;
  }
  
  public void setTotal(BigDecimal total)
  {
    this.total = total;
  }
}
