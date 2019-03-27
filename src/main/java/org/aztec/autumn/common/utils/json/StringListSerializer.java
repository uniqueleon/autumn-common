package org.aztec.autumn.common.utils.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StringListSerializer extends JsonSerializer<List>{

	public StringListSerializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void serialize(List value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		int status = ((JsonWriteContext) gen.getOutputContext()).writeValue();
		switch (status) {
		case JsonWriteContext.STATUS_OK_AFTER_COLON:
			gen.writeRaw(':');
			break;
		case JsonWriteContext.STATUS_OK_AFTER_COMMA:
			gen.writeRaw(',');
			break;
		case JsonWriteContext.STATUS_EXPECT_NAME:
			throw new JsonGenerationException("Can not write string value here");
		}
		StringUnicodeSerializer stringUnicodeSerializer = new StringUnicodeSerializer();
		if(value.size() > 0){
			switch(getComponentType(value)){
			case MAP:
				gen.writeRaw("{");
				for(int i = 0;i < value.size();i++){
					Map<String,String> map = (Map<String,String>) value.get(i);
					Set<String> keySet = map.keySet();
					
					for(String key : map.keySet()){
						gen.writeFieldName(key);
						//gen.writeRaw("\"" + key + "\":");
						stringUnicodeSerializer.serialize(map.get(key), gen, serializers);
						//gen.writeRaw(",");
					}
				}
				gen.writeRaw("}");
			case STRING:
			}
		}
	}
	
	private ComponentType getComponentType(List list){
		
		Object listObj = list.get(0);
		if(Map.class.isAssignableFrom(listObj.getClass())){
			return ComponentType.MAP;
		}
		return ComponentType.STRING;
	}
	
	enum ComponentType{
		MAP,STRING;
		
		
	}

}
