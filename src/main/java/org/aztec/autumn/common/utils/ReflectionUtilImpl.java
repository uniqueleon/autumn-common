package org.aztec.autumn.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.constant.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtilImpl implements ReflectionUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);

	public Object getValue(String fieldName, Object target, boolean isForce)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		return getValue(fieldName, target, target.getClass(), isForce);
	}

	private Object getValue(String fieldName, Object target, Class targetCls, boolean isForce)
			throws IllegalArgumentException, IllegalAccessException {
		Field field = null;
		try {
			try {
				return getValueFromGetter(fieldName, target, targetCls, isForce);
			} catch (Exception e) {
				field = targetCls.getDeclaredField(fieldName);
				if (isForce)
					field.setAccessible(true);
				return field.get(target);
			} 
		} catch (NoSuchFieldException e) {
			if (targetCls.getSuperclass() != null && !targetCls.getSuperclass().isInterface())
				return getValue(fieldName, target, targetCls.getSuperclass(), isForce);
			else
				return null;
		}
	}
	
	private Object getValueFromGetter(String fieldName, Object target, Class targetCls, boolean isForce) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method = targetCls.getDeclaredMethod("get" + org.apache.commons.lang.StringUtils.capitalize(fieldName));
		if(isForce)
			method.setAccessible(true);
		return method.invoke(target);
	}

	public Map<String, Object> getProperties(Object target, boolean isForce) throws IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		return getAllValue(target, target.getClass(), isForce);
	}

	private Map<String, Object> getAllValue(Object target, Class targetCls, boolean isForce)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		Field[] fields = targetCls.getDeclaredFields();
		Map<String, Object> retValues = new HashMap<String, Object>();
		for (Field field : fields) {
			try {
				Object fieldValue = getValue(field.getName(), target, isForce);
				retValues.put(field.getName(), fieldValue);
			} catch (Exception e) {
				LOG.debug(e.getMessage());
			}
		}
		if (targetCls.getSuperclass() != null && !targetCls.getSuperclass().isInterface()) {
			retValues.putAll(getAllValue(target, targetCls.getSuperclass(), isForce));
		}
		return retValues;
	}

	public void setValue(String fieldName, Object target, Object value, boolean isForce)
			throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
		Class targetCls = target.getClass();
		
		Field field;
		try {
			field = targetCls.getDeclaredField(fieldName);
			if (isForce) {
				field.setAccessible(true);
				field.set(target, value);
			}
			else {
				String setter = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
				Method setMethod = targetCls.getMethod(setter,value.getClass());
				setMethod.invoke(target, value);
			}
		} catch (NoSuchFieldException e) {
			throw e;
		}
	}

	public String[] getAllFieldName(Class objCls) {
		Field[] fields = objCls.getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}

	public Map<String, Object> wrapObjestAsMap(Object entity) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Class entityCls = entity.getClass();
		Field[] fields = entityCls.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			retMap.put(field.getName(), getValue(field.getName(), entity, true));
		}
		return retMap;
	}

	public boolean isSubClass(Class childCls, Class parentCls) {
		Class castCls = childCls.asSubclass(parentCls);
		return castCls != null ? true : false;
	}

	public Object clone(Object param) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class paramType = param.getClass();
		if (param.getClass().isPrimitive()) {
			return param;
		} else if (param instanceof Number || param instanceof Byte || param instanceof Boolean
				|| param instanceof Character) {
			return param;
		} else if (param instanceof String) {
			Constructor construtor = paramType.getConstructor(paramType);
			return construtor.newInstance(param);
		} else if (param instanceof Date || param instanceof Timestamp || param instanceof java.util.Date) {
			Constructor construtor = paramType.getConstructor(long.class);
			return construtor.newInstance(invoke(param, "getTime", null, true));
		} else {
			Constructor construtor = paramType.getConstructor();
			Object retObj = construtor.newInstance();
			BeanUtils.copyProperties(retObj, param);
			return retObj;
		}
	}

	public Object invoke(Object obj, String methodName, Object[] params, boolean isForce) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (obj == null || methodName == null)
			return null;
		Class[] paramTypes = null;
		if (params != null) {
			paramTypes = new Class[params.length];
			for (int i = 0; i < paramTypes.length; i++) {
				paramTypes[i] = params[i].getClass();
			}
		}
		Method invMethod = obj.getClass().getDeclaredMethod(methodName, paramTypes);
		if (isForce)
			invMethod.setAccessible(true);
		return invMethod.invoke(obj, params);
	}

	public Object invoke(Object obj, String methodName, String[] paramClsNames, Object[] params, boolean isForce)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ClassNotFoundException {
		if (obj == null || methodName == null || paramClsNames == null)
			throw new IllegalArgumentException(
					"target object,method name and the param clas names should not be null!");
		Class[] paramTypes = null;
		if (paramClsNames != null) {
			paramTypes = new Class[paramClsNames.length];
			for (int i = 0; i < paramClsNames.length; i++) {
				if (PrimaryType.isPrimaryType(paramClsNames[i])) {
					paramTypes[i] = PrimaryType.getPrimaryType(paramClsNames[i]).primaryType;
				} else {
					paramTypes[i] = Class.forName(paramClsNames[i]);
				}
			}
		}
		Method invMethod;
		try {
			invMethod = obj.getClass().getMethod(methodName, paramTypes);
		} catch (Exception e) {
			invMethod = obj.getClass().getDeclaredMethod(methodName, paramTypes);
		}
		
		//obj.getClass().getm
		if (isForce)
			invMethod.setAccessible(true);
		return invMethod.invoke(obj, params);
	}

	public <T> T newInstance(Class<T> instCls, Object... args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {

		Constructor<T> constructor = instCls.getConstructor(getParamTypes(args));
		return constructor.newInstance(args);
	}

	private Class[] getParamTypes(Object[] params) {
		Class[] paramTypes = null;
		if (params != null) {
			paramTypes = new Class[params.length];
			for (int i = 0; i < paramTypes.length; i++) {
				paramTypes[i] = params[i].getClass();
			}
		}
		return paramTypes;
	}

	public boolean isAnnotatedWith(Class cls, Class annoCls, String annoType) {
		if (cls == null || annoCls == null)
			return false;
		if (annoType.equals(ANNOTATION_LOCATION_TYPE) || annoType.equals(ANNOTATION_LOCATION_ALL)) {
			Annotation[] typeAnnos = cls.getAnnotations();
			for (Annotation anno : typeAnnos) {
				if (annoCls.getName().equals(anno.annotationType().getName())) {
					return true;
				}
			}
		}
		if (annoType.equals(ANNOTATION_LOCATION_METHOD) || annoType.equals(ANNOTATION_LOCATION_ALL)) {
			Method[] clsMethods = cls.getDeclaredMethods();
			for (Method method : clsMethods) {
				Annotation[] typeAnnos = method.getAnnotations();
				for (Annotation anno : typeAnnos) {
					if (annoCls.getName().equals(anno.annotationType().getName())) {
						return true;
					}
				}
			}
		}
		if (annoType.equals(ANNOTATION_LOCATION_FIELD) || annoType.equals(ANNOTATION_LOCATION_ALL)) {
			Field[] clsFields = cls.getDeclaredFields();
			for (Field field : clsFields) {
				Annotation[] typeAnnos = field.getAnnotations();
				for (Annotation anno : typeAnnos) {
					if (annoCls.getName().equals(anno.annotationType().getName())) {
						return true;
					}
				}
			}
		}
		if (annoType.equals(ANNOTATION_LOCATION_INTERFACE) || annoType.equals(ANNOTATION_LOCATION_ALL)) {
			Class[] clsInterfaces = cls.getInterfaces();
			for (Class inf : clsInterfaces) {
				if (isAnnotatedWith(inf, annoCls, ANNOTATION_LOCATION_TYPE))
					return true;
				if (isAnnotatedWith(inf, annoCls, ANNOTATION_LOCATION_METHOD))
					return true;
			}
		}
		if (annoType.equals(ANNOTATION_LOCATION_SUPER) || annoType.equals(ANNOTATION_LOCATION_ALL)) {
			Class superCls = cls.getSuperclass();
			if (superCls == null)
				return false;
			if (isAnnotatedWith(superCls, annoCls, ANNOTATION_LOCATION_ALL))
				return true;
		}
		return false;
	}

	enum PrimaryType {

		INT("int", int.class,Integer.class), LONG("long", long.class,Long.class), FLOAT("float", float.class,Float.class), DOUBLE("double",
				double.class,Double.class), BYTE("byte", byte.class,Byte.class), CHAR("char", char.class,Character.class), BOOLEAN("boolean", boolean.class,Boolean.class);

		private String typeName;
		private Class primaryType;
		private Class wrapperType;

		private PrimaryType(String typeName, Class primaryType,Class wrapperType) {
			this.typeName = typeName;
			this.primaryType = primaryType;
			this.wrapperType = wrapperType;
		}

		private static boolean isPrimaryType(String typeName) {
			for (PrimaryType pType : PrimaryType.values()) {
				if (pType.typeName.equals(typeName)) {
					return true;
				}
			}
			return false;
		}
		
		private static PrimaryType getPrimaryType(Class type){
			for (PrimaryType pType : PrimaryType.values()) {
				if (pType.primaryType.equals(type)) {
					return pType;
				} else if (pType.wrapperType.equals(type)){
					return pType;
				}
				
			}
			return null;
		}

		public static PrimaryType getPrimaryType(String typeName) {
			for (PrimaryType pType : PrimaryType.values()) {
				if (pType.typeName.equals(typeName)) {
					return pType;
				}
			}
			return null;
		}

		public Class getPrimaryType() {
			return primaryType;
		}

		public void setPrimaryType(Class primaryType) {
			this.primaryType = primaryType;
		}

		public Class getWrapperType() {
			return wrapperType;
		}

		public void setWrapperType(Class wrapperType) {
			this.wrapperType = wrapperType;
		}

	}

	public List<Annotation> findSpecifiedAnnotationInMethod(Method method, Class specifiedAnnoType, String location) {
		List<Annotation> retAnnotations = new ArrayList();
		Annotation[] annos = null;
		switch (location) {
		case ANNOTATION_LOCATION_METHOD: {
			annos = method.getAnnotations();
			Annotation retAnno = null;
			for (Annotation anno : annos) {
				if (anno.annotationType().getName().equals(specifiedAnnoType.getName())) {
					retAnnotations.add(anno);
				}
			}
			break;
		}
		case ANNOTATION_LOCATION_PARAMETER: {
			Annotation[][] allParamAnnos = method.getParameterAnnotations();
			for (Annotation[] paramAnnos : allParamAnnos) {
				for (Annotation anno : paramAnnos) {
					if (anno.annotationType().getName().equals(specifiedAnnoType.getName())) {
						retAnnotations.add(anno);
					}
				}
			}
			break;
		}
		}
		return retAnnotations;
	}

	@Override
	public <T> T newPrimitiveInstance(Class<T> instCls, String value) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class wrapperCls = instCls;
		Constructor construct = instCls.getConstructor(String.class);
		return (T) construct.newInstance(value);
	}

	@Override
	public <T> T newInstanceUseMap(Class<T> instCls, Map<String, Object> args,boolean isForce)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		Object newInstance = instCls.newInstance();
		Field[] fields = instCls.getDeclaredFields();
		Map<String, Object> retValues = new HashMap<String, Object>();
		for (Field field : fields) {
			Class fieldType = field.getType();
			if(!Collection.class.isAssignableFrom(fieldType) 
					&& !Map.class.isAssignableFrom(fieldType) 
					&& fieldType.isInterface())
				continue;
			String fieldName = field.getName();
			if(!args.containsKey(fieldName) || args.get(fieldName) == null){
				continue;
			}
			if (isForce) {
				field.setAccessible(true);
				field.set(newInstance, args.get(field.getName()));
			} else {
				Object value = args.get(fieldName);
				String setter = "set" + fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1, fieldName.length());
				if(Map.class.isAssignableFrom(value.getClass())){
					Method method = instCls.getDeclaredMethod(setter,field.getType());
					method.invoke(newInstance, newInstanceUseMap(field.getType(), (Map)value, isForce));
				}
				else if(List.class.isAssignableFrom(value.getClass())){

					Method method = instCls.getDeclaredMethod(setter,field.getType());
					Type fc = field.getGenericType();
					if(fc == null)continue;
					List valueList = (List)value;
					List dataList = new ArrayList<>();
		            if(fc instanceof ParameterizedType) {
		            	ParameterizedType pt = (ParameterizedType) fc;
		            	Class genericClazz = (Class)pt.getActualTypeArguments()[0]; //【4】 得到泛型里的class类型对象。  
		            	if(!genericClazz.isInterface()) {
		            		for(Object dataObj : valueList){	
		            			if(Map.class.isAssignableFrom(dataObj.getClass())) {
					            	dataList.add(newInstanceUseMap(genericClazz, (Map<String,Object>)dataObj, isForce));
		            			}
		            			else {
				            		dataList.add(newInstance(genericClazz, dataObj.toString()));
		            			}
			            	}
		            	}
		             }
		            method.invoke(newInstance, dataList);
				}
				else{
					Method method = instCls.getDeclaredMethod(setter,args.get(fieldName).getClass());
					method.invoke(newInstance,args.get(fieldName));
				}
			}
		}
		return (T) newInstance;
	}

	@Override
	public <T> Map<String, Object> getPropertiesMap(Class<T> instCls, Map<String, String> textValues) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Field[] fields = instCls.getDeclaredFields();
		Map<String, Object> retValues = new HashMap<String, Object>();
		for (Field field : fields) {
			String fieldName = field.getName();
			PrimaryType pType = PrimaryType.getPrimaryType(field.getType());
			if(pType != null){
				Constructor constr = pType.getWrapperType().getConstructor(String.class);
				constr.setAccessible(true);
				Object value = constr.newInstance(textValues.get(fieldName));
				retValues.put(fieldName, value);
			} else if (field.getType().equals(String.class)){
				retValues.put(fieldName, textValues.get(fieldName));
			} else if (field.getType().equals(Date.class)){
				retValues.put(fieldName, new Date(Long.parseLong(textValues.get(fieldName))));
			} else if (field.getType().isArray()){
				retValues.put(fieldName, textValues.get(fieldName).split(CommonConst.XML_ELEMENT_SEPERATE_CHAR));
			}
		}
		return retValues;
	}

	@Override
	public Class getFieldType(Class clazz,String fieldName) throws NoSuchFieldException, SecurityException {
		
		return clazz.getDeclaredField(fieldName).getType();
	}

}
