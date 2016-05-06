package ec.gob.gim.income.model;


/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:39
 */

public enum PaymentType {
	CASH,
	CREDIT_CARD,
	CHECK,
	TRANSFER,
	CREDIT_NOTE;
	
	static PaymentType[] types = new PaymentType[3];
	static PaymentType[] devolutionTypes = new PaymentType[2];	
	
	public static PaymentType[] getRestrictedPaymentTypes(){
		types[0] = PaymentType.CASH;
		types[1] = PaymentType.CREDIT_CARD;
		types[2] = PaymentType.CHECK;
		return types;
	}
	
	public static PaymentType[] getDevolutionPaymentTypes(){
		devolutionTypes[0] = PaymentType.TRANSFER;
		devolutionTypes[1] = PaymentType.CHECK;
		return devolutionTypes;
	}
	
	public static PaymentType[] getOrderPaymentTypes(){
		types = new PaymentType[values().length];
		types[0] = PaymentType.CASH;
		types[1] = PaymentType.CHECK;
		types[2] = PaymentType.CREDIT_CARD;
		types[3] = PaymentType.CREDIT_NOTE;
		types[4] = PaymentType.TRANSFER;
		return types;
	}
	
	public static String getDevolutionName(PaymentType pt){
		return "dv_" + pt.name();
	}
}//end PaymentType