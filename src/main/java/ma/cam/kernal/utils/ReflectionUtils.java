package ma.cam.kernal.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ma.cam.kernal.constants.Constants;
import ma.cam.kernal.exceptions.TechnicalException;

public class ReflectionUtils {

	private ReflectionUtils() {

	}

	public static Map<String, Object> convertToMapWidthNull(Object obj) {
		try {
			Map<String, Object> result = new HashMap<>();
			if (!Util.isNull(obj)) {
				BeanInfo info = Introspector.getBeanInfo(obj.getClass());
				for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
					Method reader = pd.getReadMethod();
					if (reader != null) {
						final String value = getValueBinding(reader.invoke(obj));
						if (value != null) {
							result.put(getAttributeNameForMap(pd.getName()).toUpperCase(),
									getValueBinding(reader.invoke(obj)));
						} else {

							result.put(getAttributeNameForMap(pd.getName()).toUpperCase(), Constants.EMPTY_STRING);
						}
					}
				}
			}
			return result;
		} catch (Exception e) {
			throw new TechnicalException(e.getMessage(), e);
		}
	}

	

	public static int getSizeMap(Map<String, Object> resource) {
		final Set<Entry<String, Object>> t = resource.entrySet();
		return t.size();
	}

	public static Map<String, Object> convertArrayObjectToMap(
			Object[] result) {
		try {
			final Map<String, Object> map = new HashMap<>();

			for (Object tmp : result) {
				Struct row = (Struct) tmp;
				map.put((String) row.getAttributes()[0],  row.getAttributes()[1]);
			}
			return map;
		} catch (Exception e) {
			throw new TechnicalException(e.getMessage(), e);
		}
	}
	


	public static Object[][] convertMapToArrayObject(Map<String, Object> resource) {

		Object[][] recObj = null;
		final Set<Entry<String, Object>> t = resource.entrySet();
		final int taille = ReflectionUtils.getSizeMap(resource);
		recObj = new Object[taille][2];

		int j = 0;

		for (Entry<String, Object> e : t) {
			String tKey = e.getKey();
			final String cle = tKey.toUpperCase();
			String valeur = null;
			//
			final Object oValeur = resource.get(tKey);
			if (oValeur != null) {
				if (oValeur instanceof Double || oValeur instanceof BigDecimal || oValeur instanceof Float) {
					valeur = (Constants.EMPTY_STRING + oValeur).replace('.', ',');
				} else {
					valeur = Constants.EMPTY_STRING + oValeur;
				}

				if (tKey.startsWith("date_")) {
					if (e.getValue() != null) {
						if (resource.get(tKey) instanceof Date) {
							valeur = Constants.format.format((Date) resource.get(tKey));
						} else if (resource.get(tKey) instanceof String) {
							valeur = (String) resource.get(tKey);
						} else {
							valeur = Constants.EMPTY_STRING + resource.get(tKey);
						}
					} else {
						resource.put(tKey, Constants.EMPTY_STRING);
					}
				}
				recObj[j][0] = cle;
				recObj[j][1] = valeur;
				j++;
			} else {
				recObj[j][0] = cle;
				recObj[j][1] = "";
				j++;
			}
		}
		return recObj;
	}



	private static String getValueBinding(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		if (value instanceof Long) {
			return Constants.EMPTY_STRING + value;
		}
		if (value instanceof Double) {
			final String returnedValue = Constants.EMPTY_STRING + value;
			return returnedValue.replace(".", ",");
		}
		if (value instanceof Integer) {
			return Constants.EMPTY_STRING + value;
		}
		if (value instanceof Float) {
			final String returnedValue = Constants.EMPTY_STRING + value;
			return returnedValue.replace(".", ",");
		}
		if (value instanceof Date) {
			return Constants.format.format((Date) value);
		}
		if (value instanceof java.util.Date) {
			return Constants.format.format((java.util.Date) value);
		}
		if (value instanceof Boolean) {

			if ((boolean)value) {
				return "1";
			} else {
				return "0";
			}

		}
		if (value instanceof BigDecimal) {
			final String returnedValue = Constants.EMPTY_STRING + value;
			return returnedValue.replace(".", ",");
		}
		return null;
	}

	public static List<Map<String, Object>> convertToListMap(List<?> objects) {
		if (objects == null)
			return new ArrayList<>();
		List<Map<String, Object>> result = new ArrayList<>();

		Iterator<?> it = objects.iterator();
		while (it.hasNext()) {
			Object object =  it.next();
			result.add(convertSingleObject(object));
		}

		return result;
	}

	public static List<Map<String, Object>> convertToListMapEmptyIfNull(List<?> objects)  {
		List<Map<String, Object>> result = new ArrayList<>();
		if (!Util.isNull(objects)) {
			Iterator<?> it = objects.iterator();
			while (it.hasNext()) {
				Object object = it.next();
				result.add(convertSingleObject(object));
			}
		}
		return result;
	}

	public static List<Map<String, Object>> convertToListMap(List<?> objects, String attribute, Object value) {

		List<Map<String, Object>> result = new ArrayList<>();
		if (!Util.isNull(objects)) {
			Iterator<?> it = objects.iterator();
			while (it.hasNext()) {
				Object object =  it.next();
				Map<String, Object> map = convertSingleObject(object);
				map.put(attribute, value);
				result.add(map);
			}
		}
		return result;
	}

	public static Map<String, Object> convertSingleObject(Object obj) {
		try {
			Map<String, Object> result = new HashMap<>();
			BeanInfo info = Introspector.getBeanInfo(obj.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();
				if (reader != null) {
					final String value = getValueBinding(reader.invoke(obj));

					if (value != null) {
						result.put(getAttributeNameForMap(pd.getName()).toUpperCase(),
								getValueBinding(reader.invoke(obj)));
					} else {
						result.put(getAttributeNameForMap(pd.getName()).toUpperCase(), Constants.EMPTY_STRING);
					}

				}
			}
			return result;
		} catch (Exception e) {
			throw new TechnicalException(e.getMessage(), e);
		}
	}

	public static String getAttributeNameForMap(String inputString) {
		StringBuilder outputString = new StringBuilder();
		char c;

		for (int i = 0; i < inputString.length(); i++) {
			c = inputString.charAt(i);
			outputString.append(Character.isUpperCase(c) ? Constants.UNDERSCORE + c : c);
		}

		return outputString.toString();
	}
	
	protected static Object executeMethod(Method method, Object target, Object[] args) throws Exception {
		int methodModifiers = method.getModifiers();
		if (!Modifier.isStatic(methodModifiers) && target == null) {
			return null;
		}
		return method.invoke(target, args);

	}
	


}
