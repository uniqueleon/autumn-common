package org.aztec.autumn.common.math.modeling.packing;

import java.util.UUID;

import org.aztec.autumn.common.constant.unit.WeightUnits;

public  class BaseObject {

	protected Location location;
	
	protected Double weight;
	
	protected Long number = 1l;
	protected String id;
	protected final static WeightUnits minWeightUnit = WeightUnits.G;
	protected String getIDPrefix() {
		return "";
	}
	protected String getIDSuffix() {
		return "";
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public BaseObject(Location location, Double weight,WeightUnits gUnit) {
		super();
		this.location = location;
		this.weight = minWeightUnit.convert(weight, gUnit);
		this.id = getIDPrefix() + UUID.randomUUID().toString() + getIDSuffix();
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BaseObject other = (BaseObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BaseObject [location=" + location + ", weight=" + weight + ", number=" + number + ", id=" + id + "]";
	}
	
}
