package org.aztec.autumn.common.utils.sms;

import org.aztec.autumn.common.utils.sms.impl.DefaultValidator;
import org.aztec.autumn.common.utils.sms.impl.YunpianSender;

public class SMSBeanFactory {

	private static final SMSBeanFactory singleton = new SMSBeanFactory();
	
	private SMSBeanFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static SMSBeanFactory getInstance(){
		return singleton;
	}
	
	public SMSSender getSender(){
		// Not yet implement.
		//return null;
		return new YunpianSender();
	}

	public SMSValidator getValidator(){
		return new DefaultValidator();
	}
}
