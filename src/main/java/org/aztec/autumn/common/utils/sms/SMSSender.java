package org.aztec.autumn.common.utils.sms;

import java.util.Map;

public interface SMSSender {

	public void send(String templateID,String telPhone,String text,Map<String,Object> contents) throws SMSSendExeception;
}
