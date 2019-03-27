package org.aztec.autumn.common.utils.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.GlobalConst;
import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.ReflectionUtil;
import org.aztec.autumn.common.utils.UtilsFactory;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils implements JsonUtils {

	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonFactory factory = objectMapper.getFactory();
	
	public JacksonUtils() {
		/*if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			// 褰撴壘涓嶅埌瀵瑰簲鐨勫簭鍒楀寲鍣ㄦ椂 蹇界暐姝ゅ瓧娈�
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			//objectMapper.
			// 浣縅ackson JSON鏀寔Unicode缂栫爜闈濧SCII瀛楃
			SerializerFactory serializerFactory = BeanSerializerFactory.instance;
			//serializerFactory.c
			//serializerFactory.createKeySerializer(SerializationConfig., JavaTy, new StringUnicodeSerializer())
			//serializerFactory.addSpecificMapping(String.class, new StringUnicodeSerializer());
			objectMapper.setSerializerFactory(serializerFactory);
			// 鏀寔缁撴潫
		}*/
	}

	@Override
	public <T> T json2Object(String json, Class<T> entityCls) throws IOException {
		// TODO Auto-generated method stub
		JsonParser parser = factory.createParser(json);
		return parser.readValueAs(entityCls);
	}

	@Override
	public <T> T[] json2Array(String json, Class<T> entityCls) throws Exception {

		JsonParser parser = factory.createParser(json);
		if (entityCls != null) {
			List<T> jsonList = json2List(json, entityCls);
			Object arrayObj = Array.newInstance(entityCls, jsonList.size());
			Array array = (Array) arrayObj;
			for(int i = 0;i < jsonList.size();i++){
				Array.set(array, i, jsonList.get(i));
			}
			return (T[]) arrayObj;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String object2Json(Object object) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream(GlobalConst.MAX_BUFFER_SIZE);
		
		JsonGenerator generator = factory.createGenerator(baos, JsonEncoding.UTF8);
		generator.writeObject(object);
		return baos.toString("UTF-8");
	}

	@Override
	public <T> List<T> json2List(String json, Class<T> entityCls) throws Exception {
		// TODO Auto-generated method stub
		List<T> retList = new ArrayList<>();
		List<Map> objectList = json2Object(json, List.class);
		ReflectionUtil refUtil = UtilsFactory.getInstance().getReflectUtil();
		for(Map mapDatas : objectList){
			retList.add((T)refUtil.newInstanceUseMap(entityCls, mapDatas, false));
		}
		return retList;
	}


}
