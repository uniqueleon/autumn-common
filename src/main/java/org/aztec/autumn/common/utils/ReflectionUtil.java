package org.aztec.autumn.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface ReflectionUtil {

	public final static String ANNOTATION_LOCATION_SUPER = "super";
	public final static String ANNOTATION_LOCATION_INTERFACE = "interface";
	public final static String ANNOTATION_LOCATION_TYPE = "type";
	public final static String ANNOTATION_LOCATION_CLASS = "class";
	public final static String ANNOTATION_LOCATION_ALL = "all";
	public final static String ANNOTATION_LOCATION_METHOD = "method";
	public final static String ANNOTATION_LOCATION_FIELD = "field";
	public final static String ANNOTATION_LOCATION_PARAMETER = "parameter";
	public final static String ANNOTATION_LOCATION_RETURN = "return_type";

	public Object getValue(String fieldName, Object target, boolean isForce)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException;

	public Map<String, Object> getProperties(Object target, boolean isForce) throws IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException;

	public void setValue(String fieldName, Object target, Object value, boolean isForce)
			throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException,
			InvocationTargetException, NoSuchMethodException;

	public String[] getAllFieldName(Class objCls);

	public Map<String, Object> wrapObjestAsMap(Object entity) throws Exception;

	public boolean isSubClass(Class childCls, Class parentCls);

	public Object clone(Object param) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException;

	public Object invoke(Object obj, String methodName, Object[] params, boolean isForce) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	public <T> T newInstance(Class<T> instCls, Object... args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	public <T> T newInstanceUseMap(Class<T> instCls, Map<String, Object> args, boolean force)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException;

	public <T> Map<String, Object> getPropertiesMap(Class<T> instCls, Map<String, String> textValues)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException ;

	public <T> T newPrimitiveInstance(Class<T> instCls, String value) throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException ;

	public boolean isAnnotatedWith(Class cls, Class annoCls, String annoType);

	public List<Annotation> findSpecifiedAnnotationInMethod(Method method, Class specifiedAnnotation, String location);

	public Object invoke(Object obj, String methodName, String[] paramClsNames, Object[] params, boolean isForce)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException ;

	public Class getFieldType(Class clazz,String fieldName) throws NoSuchFieldException, SecurityException;
}
