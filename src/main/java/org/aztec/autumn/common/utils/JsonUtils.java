package org.aztec.autumn.common.utils;

import java.util.List;

public interface JsonUtils {

	public <T> T json2Object(String json,Class<T> entityCls) throws Exception;
	public <T> T[] json2Array(String json,Class<T> arrayCls) throws Exception;
	public <T> List<T> json2List(String json,Class<T> entityCls) throws Exception;
	public String object2Json(Object object) throws Exception;
}
