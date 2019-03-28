package org.aztec.autumn.common.utils.sms.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.aztec.autumn.common.utils.sms.SMSSendExeception;
import org.aztec.autumn.common.utils.sms.SMSSender;
import org.aztec.autumn.common.utils.ws.ApiException;
import org.aztec.autumn.common.utils.ws.ApiInvocationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YunpianSender implements SMSSender {

	private static final String YUN_PIAN_SMS_SEND_API_URL = "https://sms.yunpian.com/v1/sms/send.json";
	private static String API_KEY = "";
	private static final Logger LOG = LoggerFactory.getLogger(YunpianSender.class);
	private static final ResourceBundle bundler = ResourceBundle.getBundle("sms_msg",Locale.SIMPLIFIED_CHINESE);
	
	public YunpianSender() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(String templateId,String telPhone,String text, Map<String, Object> contents) throws SMSSendExeception {
		// TODO Auto-generated method stub
		Map<String,String> headers = new HashMap<>();
		headers.put("Accept", "application/json;charset=utf-8");
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		Map<String,String> params = new HashMap<>();
		for(String key : contents.keySet()){
			params.put(key, contents.get(key).toString());
		}
		if(bundler.containsKey(templateId)){
			String rawText = bundler.getString(templateId);
			for(String key : contents.keySet()){
				rawText = rawText.replace("{" + key + "}", "" + contents.get(key));
			}
			params.put("text", rawText);
		}
		else{
			params.put("text", text);
		}
		params.put("apikey", API_KEY);
		params.put("mobile", telPhone);
		try {
			Map result = ApiInvocationUtils.invokePost(YUN_PIAN_SMS_SEND_API_URL, Map.class, null, new HashMap<>(), headers, params, null);
			if(!result.get("code").equals(0)){
				throw new SMSSendExeception("Send Error!code=" + result.get("code") + ",msg:" + result.get("msg") + ",detail=" + result.get("detail"));
			}
		} catch (ApiException e) {
			throw new SMSSendExeception(e.getMessage(),e);
		}
	}

}
