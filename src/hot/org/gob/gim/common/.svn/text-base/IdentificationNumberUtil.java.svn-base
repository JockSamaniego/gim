package org.gob.gim.common;

import java.util.regex.Pattern;

import org.gob.gim.common.exception.IdentificationNumberExistsException;
import org.gob.gim.common.exception.IdentificationNumberSizeException;
import org.gob.gim.common.exception.IdentificationNumberWrongException;
import org.gob.gim.common.exception.InvalidIdentificationNumberException;
import org.gob.gim.common.exception.InvalidIdentificationNumberFinishedException;


public final class IdentificationNumberUtil {	
	
	private static IdentificationNumberUtil instance = new IdentificationNumberUtil();
	
	public static IdentificationNumberUtil getInstance(){
		return instance;
	}

	public Boolean isNationalIdentityNumberValid(String identificationNumber)
			throws IdentificationNumberSizeException,
			IdentificationNumberWrongException,
			InvalidIdentificationNumberException {
		if (identificationNumber.length() < 10) {
			throw new IdentificationNumberSizeException();
		}
		String spatron = "[0-9]{10}";
		if (!Pattern.matches(spatron, identificationNumber)) {
			throw new IdentificationNumberWrongException();
		}
		verifyDigit(identificationNumber);
		return true;
	}

	private void verifyDigit(String identificationNumber)throws InvalidIdentificationNumberException {
		String wced = identificationNumber.substring(0, 9);
		String verif = identificationNumber.substring(9, 10);
		double wd = 0;
		double wc = 0;
		double wa = 0;
		double wb = 0;

		for (int i = 0; i <= wced.length() - 1; i = i + 2) {
			wa = Double.parseDouble(wced.substring(i, i + 1));
			wb = wa * 2;
			wc = wb;
			if (wb > 9) {
				wc = wb - 9;
			}
			wd = wd + wc;
		}

		for (int i = 1; i <= wced.length() - 1; i = i + 2) {
			wa = Double.parseDouble(wced.substring(i, i + 1));
			wd = wd + wa;
		}
		double wn;
		wn = wd / 10;
		double wn2 = Math.ceil(wn);
		wn2 = wn2 * 10;
		double digit = wn2 - wd;

		if (digit != Double.parseDouble(verif)) {
			throw new InvalidIdentificationNumberException();
		}
	}
	
	public Boolean isTaxpayerNumberValid(String identificationNumber) throws IdentificationNumberSizeException, IdentificationNumberWrongException, 
	InvalidIdentificationNumberException, InvalidIdentificationNumberFinishedException, IdentificationNumberExistsException{
		if (identificationNumber.length() < 13) {
			throw new IdentificationNumberSizeException();
		}
		if (!Pattern.matches("[0-9]{13}", identificationNumber)) {
			throw new IdentificationNumberWrongException();
		}

		identificationNumber = identificationNumber.trim();
		String rucTypeSelector = identificationNumber.substring(2,3);
		int rucType = Integer.parseInt(rucTypeSelector);
				
		if (rucType < 6) {
			System.out.println("THIS DISTANCE IN MY VOICE: PERSONA NATURAL "+rucType);
			verifyDigit(identificationNumber);
		} else if (rucType == 6) {
			System.out.println("THIS DISTANCE IN MY VOICE: PERSONA JURIDICA PUBLICA "+rucType);
			int[] coeficientes = { 3, 2, 7, 6, 5, 4, 3, 2 };
			verifyTaxpayerNumber(identificationNumber, coeficientes);
		} else if (rucType == 9) {
			System.out.println("THIS DISTANCE IN MY VOICE: PERSONA JURIDICA PRIVADA O EXTRANJERO "+rucType);
			int[] coeficientes = { 4, 3, 2, 7, 6, 5, 4, 3, 2 };
			verifyTaxpayerNumber(identificationNumber, coeficientes);
		} else {
			throw new IdentificationNumberWrongException();
		}

		return true;
	}
	
	private void verifyTaxpayerNumber(String identificationNumber, int[] coeficientes) 
			throws InvalidIdentificationNumberException,
			InvalidIdentificationNumberFinishedException {
		identificationNumber = identificationNumber.trim();
		String wced = identificationNumber.substring(0,coeficientes.length);
		String verif = identificationNumber.substring(coeficientes.length, coeficientes.length + 1);

		int sum_coef = 0;
		int wa;

		for (int i = 0; i < coeficientes.length; i++) {
			wa = Integer.parseInt(wced.substring(i, i + 1));
			sum_coef = sum_coef + (wa * coeficientes[i]);
		}

		int module = 11;
		int residue = sum_coef % module;
		int digit_verify = residue == 0 ? residue : module - residue;

		if (digit_verify != Integer.parseInt(verif)) {
			throw new InvalidIdentificationNumberException();
		}

		String suffixValue = identificationNumber.substring(coeficientes.length + 1);
		System.out.println("THIS DISTANCE IN MY VOICE: "+suffixValue);
		if (Integer.parseInt(suffixValue) == 0) { 
			throw new InvalidIdentificationNumberFinishedException();
		}
	}	


}
