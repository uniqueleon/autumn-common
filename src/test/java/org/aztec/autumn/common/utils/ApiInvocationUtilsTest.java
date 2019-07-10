package org.aztec.autumn.common.utils;

import java.util.Map;

import org.aztec.autumn.common.utils.ws.ApiException;
import org.aztec.autumn.common.utils.ws.ApiInvocationUtils;

import com.google.common.collect.Maps;

public class ApiInvocationUtilsTest {

	public ApiInvocationUtilsTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			Map<String,String> postParam = Maps.newHashMap();
			postParam.put("applyCode", "AAABBB");
			String theUrl = "https://121.14.47.186:7510/asso/oauth/bindAccount.htm";
			//String retData = ApiInvocationUtils.invokePost("https://121.14.47.186:7510/asso/oauth/bindAccount.htm", String.class, String.class, Maps.newHashMap(), Maps.newHashMap(), postParam, null);
			String retData = ApiInvocationUtils.invokeGet(theUrl,  String.class, String.class, Maps.newHashMap(), Maps.newHashMap(), postParam);
			System.out.println(retData);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
