package org.aztec.autumn.common.utils.sms;

public interface SMSValidator {

	public void validate(String phoneNum,String smsCode) throws CodeNotMatchException,NoCodeFoundException;
	public String genenareCode(String phoneNum) throws SMSException;
	public boolean isSmsCodeExists(String phoneNum) throws SMSException;
	public String getCode(String phoneNum) throws SMSException;
}
