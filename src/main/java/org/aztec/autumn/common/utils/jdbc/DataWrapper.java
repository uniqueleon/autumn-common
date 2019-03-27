package org.aztec.autumn.common.utils.jdbc;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;


public class DataWrapper {

	public static <T> List<T> wrap(Class<T> entityClass, ResultSet result,
			boolean isNonCamel) throws InstantiationException,
			IllegalAccessException, SQLException {

		List<T> retObjects = new ArrayList<T>();
		result.beforeFirst();
		while (result.next()) {
			T newInstance = entityClass.newInstance();
			List<Array> dataArray = new ArrayList<Array>();
			for (Field entityField : entityClass.getDeclaredFields()) {
				String fieldName = entityField.getName();
				try {
					if (isNonCamel)
						fieldName = StringUtils.transform2NonCamelCase(
								fieldName, '_');
					Constructor constructor = entityField.getType()
							.getDeclaredConstructor(String.class);
					Object value = constructor.newInstance(result
							.getString(fieldName));
					entityField.set(entityField, value);
				} catch (Exception e) {
					continue;
				}
			}
			retObjects.add(newInstance);
		}
		return retObjects;
	}

	public static List<Map<String, String>> wrapResultAsMap(ResultSet result)
			throws SQLException {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();
		result.beforeFirst();
		while (result.next()) {
			Map<String, String> record = new HashMap<String, String>();
			for (int i = 1; i <= columnCount; i++) {
				record.put(metaData.getColumnName(i), result.getString(i));
			}
			resultList.add(record);
		}
		return resultList;
	}

	public static String wrapAsJsonString(ResultSet result) throws SQLException {
		try {
			return StringUtils.wrapMapAsJson(wrapResultAsMap(result));
		} catch (Exception e) {
			throw new SQLException(e.getMessage(),e);
		}
	}

}
