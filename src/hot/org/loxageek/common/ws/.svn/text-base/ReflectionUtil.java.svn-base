package org.loxageek.common.ws;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author WilmanChamba 
 * wilman at loxageek dot com
 * 
 */

public class ReflectionUtil {
	
	private static final String JAVA_LANG_PACKAGE = "java.lang";
	
	private static boolean isClassOfJavaLang(Class<?> klass){
		if (klass.getName().indexOf(JAVA_LANG_PACKAGE) >= 0)
			return true;
		else
			return false;
	}
	
	private static boolean isPrimiviteType(Class<?> klass){
		if (klass.isPrimitive()){
			return true;
		}
		if (isClassOfJavaLang(klass)){
			return true;
		}
		return false;
	}
	
	private static Map<String, Object> buildEntryForMap(Field field, String nameField, Object dto){
		Map<String, Object> resultField = new LinkedHashMap<String, Object>();
		field.setAccessible(true);
			Object value;
			try {
				value = field.get(dto);
			} catch (IllegalArgumentException e) {
				value = null;
				System.out.println("==== ERROR IllegalArgumentException: " + e );
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				value = null;
				System.out.println("==== ERROR IllegalAccessException: " + e );
				//e.printStackTrace();
			}
			Class<?> klassOfField = field.getType();
			//System.out.println("==== nameField: " + nameField + ", isPrimiviteType: " + isPrimiviteType(klassOfField) + ", name class: " + klassOfField.getName() + ", " + klassOfField.getCanonicalName());
			if (isPrimiviteType(klassOfField)){
				resultField.put(nameField, value);
			}else{
				if (value == null){
					try {
						value = klassOfField.newInstance();
					} catch (InstantiationException e) {
						System.out.println("==== ERROR InstantiationException: " + e );
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						System.out.println("==== ERROR IllegalAccessException: " + e );
						e.printStackTrace();
					}
				}
				resultField.putAll(getAsMap(value, nameField));
			}
			return resultField;
	}
	
	private static Map<String, Object> getAsMap(Object dto, String name){
		
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		
		Class<?> klass = dto.getClass();
		Field[] fields =klass.getDeclaredFields();
		if (fields != null && fields.length > 0){
			for (Field field : fields){
				StringBuffer nameField = new StringBuffer();
				
				if (name != null && !name.equals("")){
					nameField = nameField.append(name).append(".");					
				}
				nameField = nameField.append(field.getName());
				result.putAll(buildEntryForMap(field, nameField.toString(), dto));
			}
		}
		return result;
		
	}

	public static Map<String, Object> getAsMap(Object dto){
		return getAsMap(dto, null);
	}
	
	private static Object buildObjectFromEntryMap(String nameField, Object value, Object dto){
		if (dto != null){
			try{
				Class<?> klass = dto.getClass();
				System.out.println("==== name class DTO: " + klass.getName());
				Field field;
				if (nameField.indexOf(".") < 0){
					field = klass.getDeclaredField(nameField);
				}else{
					field = klass.getDeclaredField(nameField.substring(0, nameField.indexOf(".")));		
				}
				field.setAccessible(true);		
				Class<?> klassOfField = field.getType();
				//System.out.println("==== nameField: " + nameField + ", isPrimiviteType: " + isPrimiviteType(klassOfField) + ", name class: " + klassOfField.getName() + ", " + klassOfField.getCanonicalName());
				if (isPrimiviteType(klassOfField)){
					field.set(dto, value);
					//System.out.println("==== FIJADO CORRECTAMENTE EL nameField AL DTO CON EL VALOR DE: " + value);
					return dto;
				}else{
					Object dtoAux = field.get(dto);
					String subNameField = nameField.substring(nameField.indexOf("."), nameField.length());
					//System.out.println("==== LLAMANDO A RELACIONES PARA CONSTRUIR con el nameField : " + field.getName() + ". " +  subNameField + " y EL VALOR DE: " + value);
					dtoAux = buildObjectFromEntryMap(subNameField, value, dtoAux);
					return dtoAux;
				}
			}catch (NoSuchFieldException e) {
				e.printStackTrace();
				return dto;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return dto;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return dto;
			}
		}
		return dto;
	}
	
	public static Object getFromMap(Map<String, Object> map, Class<?> klass){
		try {
			Object dto = klass.newInstance();
			
			if (map != null && !map.isEmpty()){
				Set<Entry<String, Object>> entries = map.entrySet();
				for (Entry<String, Object> entry : entries){
					buildObjectFromEntryMap(entry.getKey(), entry.getValue(), dto);
				}
			}
			return dto;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
		
}
