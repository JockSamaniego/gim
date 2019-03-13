package ec.gob.gim.income.model.dto; 
 
import java.math.BigDecimal; 
 
import org.gob.gim.common.NativeQueryResultColumn; 
import org.gob.gim.common.NativeQueryResultEntity; 
 
@NativeQueryResultEntity   
public class FutureEmissionDTO { 
   
  @NativeQueryResultColumn(index = 0) 
  private Long id; 
 
  @NativeQueryResultColumn(index = 1) 
  private String futureCode; 
   
  @NativeQueryResultColumn(index = 2) 
  private String futureName; 
   
  @NativeQueryResultColumn(index = 3) 
  private BigDecimal previousBalance; 
   
  @NativeQueryResultColumn(index = 4) 
  private BigDecimal futureEmission; 
   
  @NativeQueryResultColumn(index = 5) 
  private BigDecimal advancePayment; 
   
  @NativeQueryResultColumn(index = 6) 
  private BigDecimal paymentFormalization; 
   
  @NativeQueryResultColumn(index = 7) 
  private BigDecimal advanceAndFormalization; 
   
  @NativeQueryResultColumn(index = 8) 
  private BigDecimal currentBalance; 
 
 
   
  public Long getId() { 
    return id; 
  } 
 
 
  public void setId(Long id) { 
    this.id = id; 
  } 
 
 
  public String getFutureCode() { 
    return futureCode; 
  } 
 
 
  public void setFutureCode(String futureCode) { 
    this.futureCode = futureCode; 
  } 
 
 
  public String getFutureName() { 
    return futureName; 
  } 
 
 
  public void setFutureName(String futureName) { 
    this.futureName = futureName; 
  } 
 
 
  public BigDecimal getPreviousBalance() { 
    return previousBalance; 
  } 
 
 
  public void setPreviousBalance(BigDecimal previousBalance) { 
    this.previousBalance = previousBalance; 
  } 
 
 
  public BigDecimal getFutureEmission() { 
    return futureEmission; 
  } 
 
 
  public void setFutureEmission(BigDecimal futureEmission) { 
    this.futureEmission = futureEmission; 
  } 
 
 
  public BigDecimal getAdvancePayment() { 
    return advancePayment; 
  } 
 
 
  public void setAdvancePayment(BigDecimal advancePayment) { 
    this.advancePayment = advancePayment; 
  } 
 
 
  public BigDecimal getPaymentFormalization() { 
    return paymentFormalization; 
  } 
 
 
  public void setPaymentFormalization(BigDecimal paymentFormalization) { 
    this.paymentFormalization = paymentFormalization; 
  } 
 
 
  public BigDecimal getAdvanceAndFormalization() { 
    return advanceAndFormalization; 
  } 
 
 
  public void setAdvanceAndFormalization(BigDecimal advanceAndFormalization) { 
    this.advanceAndFormalization = advanceAndFormalization; 
  } 
 
 
  public BigDecimal getCurrentBalance() { 
    return currentBalance; 
  } 
 
 
  public void setCurrentBalance(BigDecimal currentBalance) { 
    this.currentBalance = currentBalance; 
  } 
   
   
} 