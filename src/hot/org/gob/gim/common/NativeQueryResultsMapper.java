package org.gob.gim.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;

public class NativeQueryResultsMapper {

	 public static <T> List<T> map(List<Object[]> objectArrayList, Class<T> genericType) {
	        List<T> ret = new ArrayList<T>();
	        List<Field> mappingFields = getNativeQueryResultColumnAnnotatedFields(genericType);
	        try {
	            for (Object[] objectArr : objectArrayList) {
	                T t = genericType.newInstance();
	                for (int i = 0; i < objectArr.length; i++) {
//	                	System.out.println("BD: "+objectArr[i].getClass().toString());
	                    if (mappingFields.get(i) != null) {
//	                    	System.out.println("tipo de clase................ "+mappingFields.get(i).getClass());
	                        if (mappingFields.get(i).getType().toString().equals("class java.util.Date")) {
	                            if (objectArr[i] == "" || objectArr[i] == null) {
	                                BeanUtils.setProperty(t, mappingFields.get(i).getName(), null);
	                            } else {
	                            	
	                            	//rfarmiosm para covertir fechas sl.date y no solo timestamp
	                            	if(objectArr[i].getClass().toString().equals("class java.sql.Date")){
	                            		java.sql.Date fecha = (java.sql.Date) objectArr[i];
	                            		Date d = new Date(fecha.getTime());
	                                    BeanUtils.setProperty(t, mappingFields.get(i).getName(), d);
	                            	}else if(objectArr[i].getClass().toString().equals("class java.sql.Time")){
	                            		Time fecha = (Time) objectArr[i];
	                                    Date d = new Date(fecha.getTime());
	                                    BeanUtils.setProperty(t, mappingFields.get(i).getName(), d);
	                            	}else{
	                            		Timestamp fecha = (Timestamp) objectArr[i];
	                                    Date d = new Date(fecha.getTime());
	                                    BeanUtils.setProperty(t, mappingFields.get(i).getName(), d);
	                            	}
	                            }

	                        } else {
	                        	
	                            BeanUtils.setProperty(t, mappingFields.get(i).getName(), objectArr[i]);
	                        }
	                    }

	                }
	                ret.add(t);
	            }
	        } catch (InstantiationException ie) {
	            System.out.println("Cannot instantiate: " + ie);
	            ret.clear();
	        } catch (IllegalAccessException iae) {
	            System.out.println("Illegal access: " + iae);
	            ret.clear();
	        } catch (InvocationTargetException ite) {
	            System.out.println("Cannot invoke method: " + ite);
	            ret.clear();
	        }
	        return ret;
	    }

	    // Get ordered list of fields
	    private static <T> List<Field> getNativeQueryResultColumnAnnotatedFields(Class<T> genericType) {
	        Field[] fields = genericType.getDeclaredFields();
	        List<Field> orderedFields = Arrays.asList(new Field[fields.length]);
	        for (Field field : fields) {
	            if (field.isAnnotationPresent(NativeQueryResultColumn.class)) {
	                NativeQueryResultColumn nqrc = field.getAnnotation(NativeQueryResultColumn.class);
	                orderedFields.set(nqrc.index(), field);
	            }
	        }
	        return orderedFields;
	    }

}
