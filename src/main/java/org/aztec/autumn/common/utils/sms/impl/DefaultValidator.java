package org.aztec.autumn.common.utils.sms.impl;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.aztec.autumn.common.utils.sms.CodeNotMatchException;
import org.aztec.autumn.common.utils.sms.NoCodeFoundException;
import org.aztec.autumn.common.utils.sms.SMSException;
import org.aztec.autumn.common.utils.sms.SMSValidator;


public class DefaultValidator implements SMSValidator {
	
	private static final Map<String,ValidateKey> dataMap = new ConcurrentHashMap<>();
	private static final Random random = new Random();
	private static final long DEFAULT_TTL = 60 * 1000;
	
	public DefaultValidator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void validate(String phoneNum, String smsCode) throws NoCodeFoundException,CodeNotMatchException {
		if(!dataMap.containsKey(phoneNum) || dataMap.get(phoneNum) == null)
			throw new NoCodeFoundException("This mobile[" + phoneNum + "] has no sms code!");
		String thisCode = dataMap.get(phoneNum).getCode();
		if(smsCode == null || !thisCode.equals(smsCode))
			throw new CodeNotMatchException("SMS code not matched!");
	}
	
	private class ValidateKey{
		private String mobile;
		private String code;
		private Date createTime;
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public ValidateKey(String mobile, String code) {
			super();
			this.mobile = mobile;
			this.code = code;
			this.createTime = new Date();
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((code == null) ? 0 : code.hashCode());
			result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ValidateKey other = (ValidateKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (code == null) {
				if (other.code != null)
					return false;
			} else if (!code.equals(other.code))
				return false;
			if (mobile == null) {
				if (other.mobile != null)
					return false;
			} else if (!mobile.equals(other.mobile))
				return false;
			return true;
		}
		private DefaultValidator getOuterType() {
			return DefaultValidator.this;
		}
		
		
	}

	@Override
	public String genenareCode(String phoneNum) throws SMSException {
		String code = "" + Math.abs(random.nextInt(999999));
		if(dataMap.containsKey(phoneNum)){
			for(String key : dataMap.keySet()){
				if(key.equals(phoneNum)){
					synchronized (random) {
						dataMap.put(phoneNum, new ValidateKey(phoneNum, code));
					}
				}
			}
		}else{
			dataMap.put(phoneNum, new ValidateKey(phoneNum, code));
		}
			
		return code;
	}

	@Override
	public boolean isSmsCodeExists(String phoneNum) throws SMSException {
		// TODO Auto-generated method stub
		if(dataMap.containsKey(phoneNum)) {
			ValidateKey vKey = dataMap.get(phoneNum);
			Long curTime = System.currentTimeMillis();
			if(curTime - vKey.getCreateTime().getTime() < DEFAULT_TTL) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getCode(String phoneNum) throws SMSException {
		if(!isSmsCodeExists(phoneNum))
			return null;
		return dataMap.get(phoneNum) != null ? dataMap.get(phoneNum).getCode() : null;
	}

}
