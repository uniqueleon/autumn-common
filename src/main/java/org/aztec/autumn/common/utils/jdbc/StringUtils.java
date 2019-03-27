package org.aztec.autumn.common.utils.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;



public class StringUtils {

	public static String transform2NonCamelCase(String targetString,char seperateChar){
		char[] chars = targetString.toCharArray();
		List<Character> newCharacters = new ArrayList<Character>();
		for(char ch : chars){
			if(Character.isUpperCase(ch)){
				newCharacters.add(seperateChar);
				newCharacters.add(Character.toLowerCase(ch));
			}
			else{
				newCharacters.add(ch);
			}
		}
		char[] newChars = new char[newCharacters.size()];
		for(int i = 0;i < newChars.length;i++){
			newChars[i] = newCharacters.get(i);
		}
		return new String(newChars);
	}
	
	public static String wrapMapAsJson(List<Map<String,String>> datas) throws Exception{
		JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();
		return jsonUtil.object2Json(datas);
	}
	
}
