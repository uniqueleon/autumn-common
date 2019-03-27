package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.aztec.autumn.common.math.modeling.packing.BaseObject;
import org.aztec.autumn.common.math.modeling.packing.Location;

import com.google.common.collect.Lists;

public class FillStep {

	private List<FillUnit> units;
	private List<FillUnit> generateUnits;
	private Long beginHeight;
	private Long minHeight;
	private Long maxHeight;
	private Long base;
	//填充对象
	private FillUnit target;
	

	public FillUnit getTarget() {
		return target;
	}

	public void setTarget(FillUnit target) {
		this.target = target;
	}

	public List<FillUnit> getGenerateUnits() {
		return generateUnits;
	}

	public void setGenerateUnits(List<FillUnit> generateUnits) {
		this.generateUnits = generateUnits;
	}

	public Long getBase() {
		return base;
	}

	public void setBase(Long base) {
		this.base = base;
	}


	public List<FillUnit> getUnits() {
		return units;
	}

	public void setUnits(List<FillUnit> units) {
		this.units = units;
	}

	public Long getBeginHeight() {
		return beginHeight;
	}

	public void setBeginHeight(Long beginHeight) {
		this.beginHeight = beginHeight;
	}

	public Long getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(Long minHeight) {
		this.minHeight = minHeight;
	}

	public Long getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(Long maxHeight) {
		this.maxHeight = maxHeight;
	}

	public FillStep(FillUnit fillTarget,List<FillUnit> units, Long beginHeight) {
		super();
		this.units = units;
		this.target = fillTarget;
		this.beginHeight = beginHeight;
	}


	public void init(Map<String,Long> usedCount,FillUnit startPoint) {

		Location tmpLocation = this.getTarget().getLocation().clone();
		for(int i = 0;i < units.size();i++) {
			units.get(i).setLocation(tmpLocation.clone());
			tmpLocation.setX(tmpLocation.getX() + units.get(i).getBase());
		}

		List<FillUnit> sortList = Lists.newArrayList(units);

		sortList.sort(new Comparator<FillUnit>() {

			@Override
			public int compare(FillUnit o1, FillUnit o2) {
				// TODO Auto-generated method stub
				return new Long(o1.getHeight() - o2.getHeight()).intValue();
			}

		});
		minHeight = sortList.get(0).getHeight();
		maxHeight = sortList.get(sortList.size() - 1).getHeight();
		generateUnits = generateNewFillUnit(startPoint);
		Long tmpBase = 0l;
		for (FillUnit unit : units) {
			if (unit.getBelongObject() == null || unit.getBelongObject() == null)
				continue;
			BaseObject realObject = unit.getBelongObject();
			if (usedCount.get(realObject.getId()) == null) {
				usedCount.put(realObject.getId(), 1l);
			} else {
				Long currentCount = usedCount.get(realObject.getId());
				usedCount.put(realObject.getId(), currentCount + 1);
			}
			tmpBase += unit.getBase();
		}
		base = tmpBase;
	}


	private List<FillUnit> generateNewFillUnit(FillUnit startPoint) {
		Long disHeight = (startPoint.getHeight() - minHeight);
		List<FillUnit> newUnits = Lists.newArrayList();
		if (disHeight > 0) {
			for (int i = 0; i < units.size(); i++) {
				FillUnit testUnit = units.get(i);
				FillUnit newFillUnit = new FillUnit(testUnit.getBelongObject(),testUnit.getBase(), 
						(startPoint.getLocation().getY().longValue() + startPoint.getHeight()) - (testUnit.getLocation().getY().longValue() + testUnit.getHeight()));
				Location tmpLocation = testUnit.getLocation().clone();
				tmpLocation.setY(tmpLocation.getY() + testUnit.getHeight());
				newFillUnit.setLocation(tmpLocation);
				newUnits.add(newFillUnit);
			}
		}
		newUnits = FillUnit.mergeAll(newUnits);
		return newUnits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginHeight == null) ? 0 : beginHeight.hashCode());
		result = prime * result + ((maxHeight == null) ? 0 : maxHeight.hashCode());
		result = prime * result + ((minHeight == null) ? 0 : minHeight.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
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
		FillStep other = (FillStep) obj;
		if (beginHeight == null) {
			if (other.beginHeight != null)
				return false;
		} else if (!beginHeight.equals(other.beginHeight))
			return false;

		if (maxHeight == null) {
			if (other.maxHeight != null)
				return false;
		} else if (!maxHeight.equals(other.maxHeight))
			return false;
		if (minHeight == null) {
			if (other.minHeight != null)
				return false;
		} else if (!minHeight.equals(other.minHeight))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder unitsBuilder = new StringBuilder();
		unitsBuilder.append("units:\n");
		for (int i = 0; i < units.size(); i++) {
			unitsBuilder.append(units.get(i).toString() + "\n");
		}
		String lable = ">>>>>>>>>>>>>>>FillStep<<<<<<<<<<<<<<<<<<<\n";
		String prefix = lable + "beginHeight=" + beginHeight + ", minHeight=" + minHeight + ", maxHeight="
				+ maxHeight + "\ntarget=" + target + "\n";

		String suffix = "\n" + lable;
		return prefix + unitsBuilder.toString() + suffix;
	}

}
