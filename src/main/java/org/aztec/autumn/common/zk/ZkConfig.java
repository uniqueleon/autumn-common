package org.aztec.autumn.common.zk;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.KeeperException;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkConfig extends ZkNode {

	private ConfigFormat format;
	private int lastVersion = 0;
	private JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
	private ReflectionUtil reflectUtil = UtilsFactory.getInstance().getReflectUtil();
	private static final Logger LOG = LoggerFactory.getLogger(ZkConfig.class);

	public static enum ConfigFormat {
		JSON, PROPERTIES,TEXT,MAP;
	}

	public ZkConfig(String dataID, ConfigFormat format) throws IOException, KeeperException, InterruptedException {
		super(dataID);
		this.format = format;
		init();
	}
	
	protected void notifyChanges() throws Exception {

		if (lastVersion < getCurVersion()) {
			lastVersion = getCurVersion();
			setFields();
		}
	}
	
	
	public void save() throws Exception {
		String writeContent = getDataStr();
		switch (format) {
		case JSON:
			writeContent = jsonUtil.object2Json(getJsonMap());
			break;

		case PROPERTIES:
			writeContent = getAsPropertiesLine();
			break;
		default:
			break;
		}
		write(writeContent);
	}
	
	public void update() {
		
	}
	
	private void readFromMap(Map<String, String> datas)  {
		Class thisCls = this.getClass();
		Field[] allFields = this.getClass().getDeclaredFields();
		for (Field field : allFields) {
			String fieldName = field.getName();
			if (datas.get(fieldName) == null)
				continue;
			Class fieldType = field.getType();
			Object value = datas.get(fieldName);
			Class valueType = value.getClass();
			try {
				if(!fieldType.equals(valueType)) {
					value = newInstance(fieldType, valueType, value);
					if(value == null) {
						continue;
					}
				}
				reflectUtil.setValue(fieldName, this, value, false);
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
	}
	
	private Object newInstance(Class type1,Class type2,Object value) {
		try {
			Constructor c = type1.getConstructor(type2);
			return c.newInstance(value);
		} catch (Exception e) {
			try {
				Constructor c = type1.getConstructor(String.class);
				return c.newInstance("" + value.toString());
			} catch (Exception e1) {
				return null;
			}
		}
	}
	
	private Map<String,Object> getJsonMap() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Map<String,Object> datas = new HashMap<>();
		Class thisCls = this.getClass();
		Field[] allFields = this.getClass().getDeclaredFields();
		for(Field field : allFields) {
			if(isIgnored(field, thisCls)) {
				continue;
			}
			String fieldName = field.getName();
			Object value = reflectUtil.getValue(fieldName, this, true);
			if(value == null) {
				continue;
			}
			datas.put(fieldName, value);
		}
		return datas;
	}
	
	private boolean isIgnored(Field field,Class thisCls) {
		Class fieldType = field.getType();
		if(fieldType.equals(thisCls)) {
			return true;
		}
		Ignored ignoredData = field.getAnnotation(Ignored.class);
		if(ignoredData != null) {
			return true;
		}
		return false;
	}
	
	private String getAsPropertiesLine() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		StringBuilder lines = new StringBuilder();
		Class thisCls = this.getClass();
		Field[] allFields = this.getClass().getDeclaredFields();
		for(Field field : allFields) {
			if(isIgnored(field, thisCls)) {
				continue;
			}
			String fieldName = field.getName();
			Object value = reflectUtil.getValue(fieldName, this, true);
			lines.append(fieldName + "_" + value);
		}
		return lines.toString();
	}

	public void setFields() throws Exception {
		if (format == null) {
			format = ConfigFormat.PROPERTIES;
		}
		Map<String, String> datas = new ConcurrentHashMap<>();
		String dataStr = getDataStr();
		switch (format) {
		case JSON:
			datas = jsonUtil.json2Object(getDataStr(), Map.class);
			readFromMap(datas);
			break;
		case PROPERTIES:
			String[] lines = dataStr.split("\n");
			for (String line : lines) {
				datas.put(line.split("=")[0], line.split("=")[1]);
			}
			readFromMap(datas);
			break;
		case MAP:
			
			return ;
		}
		
	}

	
}
