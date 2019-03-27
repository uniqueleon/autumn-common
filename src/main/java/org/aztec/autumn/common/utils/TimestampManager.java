package org.aztec.autumn.common.utils;

import java.util.Date;

public interface TimestampManager {

	public void setTimeOut(long period);
	public void setTimestamp(TimestampKey key,Date date);
	public boolean isTimeOut(TimestampKey key,Date date);
	public TimestampKey getStringifyKey(String key);
}
