package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.BaseObject;
import org.aztec.autumn.common.math.modeling.packing.Location;

import com.beust.jcommander.internal.Lists;

public class FillUnit {
	private Long base;
	private Long height;
	private BaseObject belongObject;
	private Location location;

	public BaseObject getBelongObject() {
		return belongObject;
	}

	public void setBelongObject(BaseObject belongObject) {
		this.belongObject = belongObject;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Long getBase() {
		return base;
	}

	public void setBase(Long base) {
		this.base = base;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}


	public FillUnit(BaseObject baseObject,Long base, Long height) {
		super();
		this.base = base;
		this.height = height;
		this.belongObject = baseObject;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
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
		FillUnit other = (FillUnit) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FillUnit [" + (belongObject != null ? "item=" + belongObject.getId() + "," : "")
				+ "base=" + base + ", height=" + height + ",location=" + location + "]";
	}

	public FillUnit clone() {
		FillUnit newUnit = new FillUnit(belongObject, new Long(base), new Long(height));
		newUnit.location = location == null ? null : location.clone();
		return newUnit;
	}

	public boolean isMergable(FillUnit otherUnit) {
		if ((this.location.isLinkedOnHorizontal(otherUnit.location, base)
				|| otherUnit.location.isLinkedOnHorizontal(location, otherUnit.base))
				&& this.height.equals(otherUnit.height)) {
			return true;
		}
		return false;
	}

	public FillUnit merge(FillUnit otherUnit) {
		if (!isMergable(otherUnit))
			return null;
		boolean forward = location.getX() < otherUnit.getLocation().getX();

		if (forward) {
			double endPoint = otherUnit.getLocation().getX() + otherUnit.getBase();
			FillUnit newUnit = new FillUnit(belongObject, new Double(endPoint - location.getX()).longValue(),
					height.longValue());
			newUnit.setLocation(location.clone());
			return newUnit;
		} else {
			double endPoint = location.getX() + otherUnit.getBase();
			FillUnit newUnit = new FillUnit(belongObject,
					new Double(endPoint - otherUnit.getLocation().getX()).longValue(), height.longValue());
			newUnit.setLocation(otherUnit.location.clone());
			return newUnit;
		}
	}
	
	public static List<FillUnit> mergeAll(List<FillUnit> fillUnits){
		List<FillUnit> mergeList = Lists.newArrayList();
		List<Integer> mergeIndexes =  Lists.newArrayList();
		for(int i = 0;i < fillUnits.size();i++) {
			if(mergeIndexes.contains(i))
				continue;
			FillUnit tmpUnit = fillUnits.get(i).clone();
			for(int j = i + 1;j < fillUnits.size();j++) {
				if(mergeIndexes.contains(j))
					continue;
				FillUnit otherUnit = fillUnits.get(j);
				if(tmpUnit.isMergable(otherUnit)) {
					tmpUnit = tmpUnit.merge(otherUnit);
					mergeList.add(tmpUnit);
					mergeIndexes.add(i);
					mergeIndexes.add(j);
					break;
				}
			}
		}
		if(mergeList.size() == 0) {
			return fillUnits;
		}
		else {
			for(int i = 0;i < fillUnits.size();i++) {
				if(!mergeIndexes.contains(i)) {
					mergeList.add(fillUnits.get(i));
				}
			}
			mergeList = mergeAll(mergeList);
			return mergeList;
		}
	}
	
}
