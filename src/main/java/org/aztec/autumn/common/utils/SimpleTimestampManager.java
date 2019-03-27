package org.aztec.autumn.common.utils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleTimestampManager implements TimestampManager {

	private Map<TimestampKey, Date> timestampData = new ConcurrentHashMap<>();
	private long period = 30000l;
	
	public SimpleTimestampManager() {
	}
	
	public SimpleTimestampManager(long period) {
		this.period = period;
	}

	@Override
	public boolean isTimeOut(TimestampKey key, Date date) {
		Date timestamp = timestampData.get(key);
		if(date.getTime() - timestamp.getTime() > period){
			return true;
		}
		return false;
	}

	@Override
	public TimestampKey getStringifyKey(String key) {
		return new TimestampStringKey(key);
	}

	@Override
	public void setTimestamp(TimestampKey key, Date date) {
		timestampData.put(key, date);
	}

	@Override
	public void setTimeOut(long period) {
		this.period = period;
	}

}
