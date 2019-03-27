package org.aztec.autumn.common.utils;

public class TimestampStringKey implements TimestampKey{
	
	String key;

	public TimestampStringKey(String key) {
		this.key = key;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		TimestampStringKey other = (TimestampStringKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public boolean equals(TimestampKey otherKey) {
		if(otherKey instanceof TimestampStringKey){
			return key.equals(((TimestampStringKey)otherKey).key);
		}
		return false;
	}

}
