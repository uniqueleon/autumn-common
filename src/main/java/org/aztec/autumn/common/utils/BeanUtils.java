package org.aztec.autumn.common.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;


public class BeanUtils {

	private static ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
	
	public BeanUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static <T> T map2Bean(Map<String,Object> data,Class<? extends T> entityCls) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		return reflectUtil.newInstanceUseMap(entityCls, data, false);
	}
	
	public static List<Map<String,Object>> toMap(Object bean,boolean compact,String[] beanNamePatterns) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, ArrayIndexOutOfBoundsException, NoSuchFieldException{
		return toMap(bean, compact, beanNamePatterns,null);
	}
		
	public static List<Map<String,Object>> toMap(Object bean,boolean compact,String[] beanNamePatterns,List<Class> excludeBeans) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException, ArrayIndexOutOfBoundsException, NoSuchFieldException{
		List<Map<String,Object>> retMap = new ArrayList<>();
		if(bean.getClass().isArray()){
			int length = Array.getLength(bean);
			for(int i = 0;i < length;i++){
				retMap.add(beanToMap(Array.get(bean, i), compact, beanNamePatterns,excludeBeans));
			}
		}
		else if(Collection.class.isAssignableFrom(bean.getClass())){
			Iterator itr = ((Collection) bean).iterator();
			while(itr.hasNext()){
				retMap.add(beanToMap(itr.next(), compact, beanNamePatterns,excludeBeans));
			}
		}
		else{
			retMap.add(beanToMap(bean, compact, beanNamePatterns,excludeBeans));
		}
		return retMap;
	}
	
	private static Map<String,Object> beanToMap(Object bean,boolean compact,String[] beanNamePatterns,List<Class> excludeBeans) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		Map<String,Object> properties = reflectUtil.getProperties(bean, false);
		if(compact){
			for(String propName : properties.keySet()){
				Object value = properties.get(propName);
				if(value == null)continue;
				Class beanCls = value.getClass();
				if(beanCls.isArray()){
					if(isBean(beanCls.getComponentType(),beanNamePatterns)){
						properties.put(propName, getIdArray(value));
					};
				}
				else if(Collection.class.isAssignableFrom(beanCls)){
					Collection collection = (Collection) value;
					if(collection.size() > 0 ){
						Object collectObj = collection.iterator().next();
						if(isBean(collectObj.getClass(),beanNamePatterns)){
							properties.put(propName, getIdFromCollection(collection));
						}
					}
				}
				else{
					if(isBean(value.getClass(), beanNamePatterns)){
						if(isExcludeCompactBean(excludeBeans,value.getClass())){
							properties.put(propName, beanToMap(value, compact, beanNamePatterns, excludeBeans));
						}
						else{
							properties.put(propName, reflectUtil.getValue("id", value, true));
						}
					}
					else{
						properties.put(propName, value);
					}
				}
			}
		}
		return properties;
	}
	
	public static boolean isExcludeCompactBean(List<Class> excludeBeans,Class targetCls){
		if(excludeBeans != null){
			for(Class beanClass : excludeBeans){
				if(beanClass.isAssignableFrom(targetCls)){
					return true;
				}
			}
		}
		return false;
			
	}
	
	private static List<Object> getIdArray(Object arrayObj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		List<Object> ids = new ArrayList<Object>();
		int length = Array.getLength(arrayObj);
		for(int i = 0;i < length;i++){
			Object element = Array.get(arrayObj, i);
			ids.add(reflectUtil.getValue("id", element, true));
		}
		return ids;
	}
	
	private static List<Object> getIdFromCollection(Object collectionObj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		List<Object> ids = new ArrayList<Object>();
		Collection collection = (Collection) collectionObj;
		Iterator itr = collection.iterator();
		Object obj;
		while(itr.hasNext()){
			obj = itr.next();
			ids.add(reflectUtil.getValue("id", obj, true));
		}
		return ids;
	}
	
	private static boolean isBean(Class beanCls,String[] beanNamePatterns){
		
		String className = beanCls.getName();
		for(String regex : beanNamePatterns){
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(className);
			if(matcher.find()){
				return true;
			}
		}
		return false;
	}
	
	public static void copyProperties(Object thisObj,Object other){
		ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
		
	}

}
