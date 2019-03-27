package org.aztec.autumn.common.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

	public CollectionUtils() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String, Object> castToMap(Object target, String[] propNames) throws IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
		ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
		Map<String, Object> props = reflectUtil.getProperties(target, true);
		Map<String, Object> retProps = new HashMap<>();
		for (String propName : propNames) {
			retProps.put(propName, props.get(propName));
		}
		return retProps;
	}

	public static <T> List<T> arrayToList(Object array){
		List<T> retList = new ArrayList<>();
		int arrayLength = Array.getLength(array);
		for(int i = 0;i < arrayLength;i++){
			retList.add((T) Array.get(array, i));
		}
		return retList;
	}
	
	public static <T> T map2Object(Map<String,Object> dataMap,Class<T> entityCls) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
		Object newObj = reflectUtil.newInstanceUseMap(entityCls, dataMap, false);
		return (T) newObj;
	}
	
}
