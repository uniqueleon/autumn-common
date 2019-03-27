package org.aztec.autumn.common;

import java.util.HashMap;
import java.util.Map;

import org.aztec.autumn.common.utils.sms.SMSSendExeception;
import org.aztec.autumn.common.utils.sms.impl.YunpianSender;

public class SMSTest {

	public SMSTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			YunpianSender ySender = new YunpianSender();
			Map<String,Object> contents = new HashMap<>();
			contents.put("app_name", "创源网");
			contents.put("code", "111111");
			ySender.send("TEST_VALIDATE", "13422345952", "[大明测试]yyyyyy", contents);
		} catch (SMSSendExeception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
