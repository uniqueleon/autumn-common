package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResult.FillType;

import com.google.common.collect.Lists;

public class FillParameter {

	private FillUnit startPoint;
	private List<List<FillUnit>> itemFillUnits;
	private List<Item> items;
	private FillType fillType;
	private Integer maxEquationNum = 10000;
	private Integer maxTryTime = 100;
	private Integer solutionCount = 1000;
	private Long totalCount = 0l;
	private Long maxListableSize = 10000l;

	public FillParameter(Box box, List<Item> items, FillType fillType) {
		this.fillType = fillType;
		startPoint = new FillUnit(box, box.getLengthAsLong(), box.getWidthAsLong());
		startPoint.setLocation(box.getLocation());
		itemFillUnits = Lists.newArrayList();
		for (Item item : items) {
			List<FillUnit> fillUnits = Lists.newArrayList();
			Double[][] surfaceData = item.getChoosedSurfaceData();
			for (int i = 0; i < surfaceData.length; i++) {
				FillUnit fillUnit = new FillUnit(item, surfaceData[i][0].longValue(), surfaceData[i][1].longValue());
				fillUnit.setLocation(box.getLocation());
				fillUnits.add(fillUnit);
			}
			totalCount += item.getNumber();
			itemFillUnits.add(fillUnits);
		}
		this.items = Lists.newArrayList();
		this.items.addAll(items);
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getMaxTryTime() {
		return maxTryTime;
	}

	public void setMaxTryTime(Integer maxTryTime) {
		this.maxTryTime = maxTryTime;
	}

	public FillUnit getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(FillUnit startPoint) {
		this.startPoint = startPoint;
	}

	public List<List<FillUnit>> getItemFillUnits() {
		return itemFillUnits;
	}

	public void setItemFillUnits(List<List<FillUnit>> itemFillUnits) {
		this.itemFillUnits = itemFillUnits;
	}

	public Integer getMaxEquationNum() {
		return maxEquationNum;
	}

	public void setMaxEquationNum(Integer maxEquationNum) {
		this.maxEquationNum = maxEquationNum;
	}

	public FillType getFillType() {
		return fillType;
	}

	public void setFillType(FillType fillType) {
		this.fillType = fillType;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Long getMaxListableSize() {
		return maxListableSize;
	}

	public void setMaxListableSize(Long maxListableSize) {
		this.maxListableSize = maxListableSize;
	}

	public Integer getSolutionCount() {
		return solutionCount;
	}

	public void setSolutionCount(Integer solutionCount) {
		this.solutionCount = solutionCount;
	}

	public Item getItemById(String id) {
		for(Item item : items) {
			if(item.getId().equals(id)) {
				return item;
			}
		}
		return null;
	}
}
