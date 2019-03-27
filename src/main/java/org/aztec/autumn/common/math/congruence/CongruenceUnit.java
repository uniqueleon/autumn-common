package org.aztec.autumn.common.math.congruence;

public class CongruenceUnit {

	
	private Integer modular;
	private Integer remainder;
	public Integer getModular() {
		return modular;
	}
	public void setModular(Integer modular) {
		this.modular = modular;
	}
	public Integer getRemainder() {
		return remainder;
	}
	public void setRemainder(Integer remainder) {
		this.remainder = remainder;
	}
	public CongruenceUnit(Integer modular, Integer remainder) {
		super();
		this.modular = modular;
		this.remainder = remainder;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modular == null) ? 0 : modular.hashCode());
		result = prime * result + ((remainder == null) ? 0 : remainder.hashCode());
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
		CongruenceUnit other = (CongruenceUnit) obj;
		if (modular == null) {
			if (other.modular != null)
				return false;
		} else if (!modular.equals(other.modular))
			return false;
		if (remainder == null) {
			if (other.remainder != null)
				return false;
		} else if (!remainder.equals(other.remainder))
			return false;
		return true;
	}
	
	
}
