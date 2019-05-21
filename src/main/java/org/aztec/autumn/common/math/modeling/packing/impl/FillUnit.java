package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.Location;
import org.aztec.autumn.common.math.modeling.packing.RectangleObject;

import com.google.common.collect.Lists;

public class FillUnit {
	private Long length;
	private Long width;
	private Long height;
	private RectangleObject belongObject;
	private Location location;
	private Integer SurfaceChoose;

	public RectangleObject getBelongObject() {
		return belongObject;
	}

	public void setBelongObject(RectangleObject belongObject) {
		this.belongObject = belongObject;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long base) {
		this.length = base;
	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long height) {
		this.width = height;
	}

	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public FillUnit(RectangleObject baseObject,Long base, Long height) {
		super();
		this.length = base;
		this.width = height;
		this.belongObject = baseObject;
	}

	public FillUnit(RectangleObject baseObject,Long base, Long height,Location location) {
		super();
		this.length = base;
		this.width = height;
		this.belongObject = baseObject;
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
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
				+ "base=" + length + ", height=" + width + ",location=" + location + "]";
	}

	public FillUnit clone() {
		FillUnit newUnit = new FillUnit(belongObject, new Long(length), new Long(width));
		newUnit.location = location == null ? null : location.clone();
		return newUnit;
	}

	public boolean isMergable(FillUnit otherUnit) {
		if ((this.location.isLinkedOnHorizontal(otherUnit.location, length)
				|| otherUnit.location.isLinkedOnHorizontal(location, otherUnit.length))
				&& this.width.equals(otherUnit.width)) {
			return true;
		}
		return false;
	}

	public FillUnit merge(FillUnit otherUnit) {
		if (!isMergable(otherUnit))
			return null;
		boolean forward = location.getX() < otherUnit.getLocation().getX();

		if (forward) {
			double endPoint = otherUnit.getLocation().getX() + otherUnit.getLength();
			FillUnit newUnit = new FillUnit(belongObject, new Double(endPoint - location.getX()).longValue(),
					width.longValue());
			newUnit.setLocation(location.clone());
			return newUnit;
		} else {
			double endPoint = location.getX() + otherUnit.getLength();
			FillUnit newUnit = new FillUnit(belongObject,
					new Double(endPoint - otherUnit.getLocation().getX()).longValue(), width.longValue());
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
