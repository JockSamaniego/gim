package ec.gob.gim.security.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.InterestPublicInstitutionUtils;

public class TestSurcharge {
	
	
	  private Integer[] publicInstitution = new Integer[]
			     {
			      270463, 221017, 270627, 29811, 271405, 272514, 261241, 272727, 48716, 206276, 273190, 273293,
			      267352, 121462, 270227, 268360, 261702, 200444, 216727, 273132, 273547, 33916, 212121, 254283, 
			      66135, 257621, 247891, 254761, 255546, 258193, 258412, 256396, 210938, 256635, 256317, 9183,
			      259367, 259627, 48708, 178202, 262042, 262112, 164060, 15172, 239348, 214474, 240420, 4483, 
			      241433, 241996, 242160, 242227, 242450, 244146, 244312, 244550, 244780, 50999, 248465, 230051, 
			      235576, 234110, 234574, 246878, 80729, 1296, 130196, 201752, 181085, 6741, 55838, 30733, 116903, 
			      59929, 68315, 30184, 30782, 40033, 6988, 234937, 3594, 3593, 3607, 211130, 45727, 45728, 30225, 
			      71501, 3679, 170591, 33700, 9184, 4484, 13994, 196942, 79181, 46312, 235126, 240845, 249102, 
			      249146, 19365, 40909, 48185, 48364, 56020, 64763, 104646, 48233, 249547, 4770, 127585, 29847, 
			      15061, 24117, 24219, 24896, 33390, 38819, 38838, 38995, 39543, 62992, 64679, 66620, 69348, 
			      74683, 151464, 39347, 44914, 36888, 40671, 139693, 193082, 230638, 230663, 56569, 231548, 231549, 
			      231563, 231607, 108184, 232761,  111199, 233184, 233280, 233308, 233351, 115958, 117222, 119408, 
			      121469, 122581, 	122821, 128868, 129375, 129707, 129556, 130370, 136693, 138443, 149654, 153052, 
			      157632, 175312, 177747, 180606, 182171, 193214, 196391, 201587, 201794, 202247, 208159, 212313, 
			      212376, 213871, 214545, 216214, 217677, 217594, 220522, 42210, 42207, 49944, 37086, 30652, 31155, 
			      33197, 36845, 39191, 55749, 58677, 156146, 160516, 161847, 173792, 176826, 179432, 179587, 199020,
			      200637, 206996, 31097, 237723, 36623, 238173, 30021, 238573, 51751, 39017, 215299, 177054, 174547, 
			      168512, 154690, 154687, 221016, 41332, 1752, 3680, 106700, 177294, 50648, 32160, 6840, 9189, 68718,
			      129044, 55558, 210692, 3707, 2812, 269753, 116331, 150274, 234622, 266274, 118263, 266591, 266887,
			      30035, 266932, 176320, 230952, 267006, 261254, 267348, 111846, 259326, 29545, 9188, 260638, 42826,
			      194364, 143553, 268290, 173911, 268696, 110754, 269132, 244883, 269123, 269482
			      };
	  
	  
	  /**
	   * 
	   * @param serviceDate
	   * @param resident_id
	   * @return
	   */
	private BigDecimal calculateSurcharge(java.util.Date serviceDate, 
				java.lang.Long resident_id){
	
			Calendar transitionDate = Calendar.getInstance();
			transitionDate.set(Calendar.DAY_OF_MONTH, 21);
			transitionDate.set(Calendar.MONTH, 5);		
			transitionDate.set(Calendar.YEAR, 2016);
			transitionDate.set(Calendar.HOUR_OF_DAY, 0);
		transitionDate.set(Calendar.MINUTE, 0);
		transitionDate.set(Calendar.MILLISECOND, 0);
		
		
		     ArrayList<Integer> publicList = new ArrayList<Integer> (Arrays.asList(publicInstitution));
		     
	        java.util.Date actual = java.util.Calendar.getInstance().getTime();	  
	        
	        
	        System.out.println(serviceDate);
	        System.out.println(transitionDate.getTime());
	        
	        if(serviceDate.before(transitionDate.getTime()) && publicList.contains(resident_id.intValue())){
	        	System.out.println("AQUI");
                return new java.math.BigDecimal(0.1);
	        }else if(serviceDate.after(transitionDate.getTime()) && publicList.contains(resident_id.intValue())){
	        	System.out.println("ACA");
	        	return new java.math.BigDecimal(0);
	        }
	        
	        if(serviceDate.getYear() != actual.getYear() ){
	                System.out.println("retorna 0.1");
	                return new java.math.BigDecimal(0.1);
	        }
	        
	        //#Desde el 01 de Julio
	        java.util.Date firstRange = new java.util.Date(actual.getYear(),6,1);
	        if( (actual.after(firstRange) || actual.equals(firstRange)) && !resident_id.equals(new java.lang.Long("132662")) ){
	                System.out.println("retorna 0.1 en 22222");
	                return new java.math.BigDecimal(0.1);
	        }
	        
	        if( (actual.after(firstRange) || actual.equals(firstRange)) && resident_id.equals(new java.lang.Long("132662"))){
	                System.out.println("retorna 0 en 33333");
	                return new java.math.BigDecimal(0);
	        }
	        
	        
	        System.out.println("retorna 0 ya al final");
	        return new BigDecimal(0);
	                
	}	  

	  
	  
	private BigDecimal findSurchargeFactor(Date expiration, Long residentId){

        Calendar paymentDate = DateUtils.getTruncatedInstance(new Date());
        Calendar expirationDate = DateUtils.getTruncatedInstance(expiration);
        
        Calendar transitionDate = Calendar.getInstance();
		transitionDate.set(Calendar.DAY_OF_MONTH, 21);
		transitionDate.set(Calendar.MONTH, 4);		
		transitionDate.set(Calendar.YEAR, 2016);
		transitionDate.set(Calendar.HOUR_OF_DAY, 0);
		transitionDate.set(Calendar.MINUTE, 0);
		transitionDate.set(Calendar.MILLISECOND, 0);
		 	 
	    java.util.Date actual = java.util.Calendar.getInstance().getTime();	        
	        
	    if(InterestPublicInstitutionUtils.isPublicInstitution(residentId)){
	     	//#compara fechas
	     	boolean paymentDateMajorTransitionDate 		= paymentDate.after(transitionDate);
	     	boolean transitionDateMajorExpirationDate 	= transitionDate.after(expirationDate);
	     	boolean paymentDateMajorExpirationDate 		= paymentDate.after(expirationDate);
	     	
	     	if (paymentDateMajorTransitionDate && paymentDateMajorExpirationDate ){
	     		//#se congela el recargo hasta el 21 de Mayo 2016
	     		if( transitionDateMajorExpirationDate ) {
	     			return calcSurcharge(transitionDate, expirationDate);
	     		}
	     	}
	     }   
        
        return calcSurcharge(paymentDate, expirationDate);
	}

	
	private BigDecimal calcSurcharge(Calendar date1, Calendar expirationDate){
		if(date1.after(expirationDate)){
	    	if (date1.get(Calendar.YEAR) == expirationDate.get(Calendar.YEAR)){
	        	Integer days = date1.get(Calendar.DAY_OF_YEAR) - expirationDate.get(Calendar.DAY_OF_YEAR);
	            System.out.println("DAYS DIFFERENCE -----> " + days);
	                
	            if( days >= 10){
	            	return new java.math.BigDecimal(1);
	            } else {
	            	return new java.math.BigDecimal(0.1).multiply(new BigDecimal(days));
	            }
	        } else
	        	return new java.math.BigDecimal(1);
	    }
	    
	    return new BigDecimal(0);
	}

	
	public static void main(String[] args) {
		
		Calendar exampleDate = Calendar.getInstance();
		exampleDate.set(Calendar.DAY_OF_MONTH, 12);
		exampleDate.set(Calendar.MONTH, 3);		
		exampleDate.set(Calendar.YEAR, 2016);
		exampleDate.set(Calendar.HOUR_OF_DAY, 0);
		exampleDate.set(Calendar.MINUTE, 0);
		exampleDate.set(Calendar.MILLISECOND, 0);
	  	
		TestSurcharge ts = new TestSurcharge();
		BigDecimal recargo = ts.calculateSurcharge(exampleDate.getTime(), 195321L);
		System.out.println("El recargo es: "+recargo);
	}

}
