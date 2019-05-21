package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;
import org.aztec.autumn.common.math.modeling.packing.impl.FillResultImpl.FillType;

import com.google.common.collect.Lists;

public class FillParameter {

	private FillUnit startPoint;
	private List<List<FillUnit>> itemFillUnits;
	private List<Item> items;
	private FillType fillType;
	private Integer maxTryTime = 1;
	//Ӱ�첻�����̽�ĸ���
	private Integer solutionCount = 2;
	//
	private Long maxExhaustionSize = 10l;
	private Long totalCount = 1l;
	private boolean simple = false;

	public FillParameter(Box box, List<Item> items, FillType fillType) {
		this.fillType = fillType;
		startPoint = new FillUnit(box, box.getLengthAsLong(), box.getWidthAsLong());
		startPoint.setLocation(box.getLocation());
		itemFillUnits = Lists.newArrayList();
		for (Item item : items) {
			List<FillUnit> fillUnits = Lists.newArrayList();
			fillUnits.addAll(randomChooseUnit(item.getFillableUnits(box)));
			totalCount += item.getNumber();
			if(fillUnits.size() > 0){
				itemFillUnits.add(fillUnits);
			}
		}
		this.items = Lists.newArrayList();
		this.items.addAll(items);
	}
	

	public List<FillUnit> randomChooseUnit(List<FillUnit> fillUnits){
		List<FillUnit> sortUnits = Lists.newArrayList();
		sortUnits.addAll(fillUnits);
		List<FillUnit> retUnits = Lists.newArrayList();
		Collections.sort(sortUnits,new Comparator<FillUnit>() {

			@Override
			public int compare(FillUnit o1, FillUnit o2) {
				// TODO Auto-generated method stub
				return o2.getLength() - o1.getLength() > 0 ? 1 : -1;
			}
			
		});
		for(int i = 0;i < sortUnits.size();i++){
			FillUnit testUnit = sortUnits.get(i);
			if((i < sortUnits.size() - 1) && testUnit.getLength().equals(sortUnits.get(i + 1).getLength())){
				int chooseIndex = RandomUtils.nextInt(2);
				switch(chooseIndex){
				case 0:
					retUnits.add(testUnit);
					break;
				case 1:
					retUnits.add(testUnit);
					//retUnits.add(sortUnits.get(i + 1));
				}
				i++;
			}
			else {
				retUnits.add(testUnit);
			}
		}
		return retUnits;
	}
	
	public FillParameter(Box box, List<Item> items, List<List<FillUnit>> fillableUnits,FillType fillType) {
		this.fillType = fillType;
		startPoint = new FillUnit(box, box.getLengthAsLong(), box.getWidthAsLong());
		startPoint.setLocation(box.getLocation());
		itemFillUnits = Lists.newArrayList();
		for (int i = 0;i < items.size();i++) {
			Item item = items.get(i);
			totalCount += item.getNumber();
			itemFillUnits.add(fillableUnits.get(i));
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

	

	public Long getMaxExhaustionSize() {
		return maxExhaustionSize;
	}

	public void setMaxExhaustionSize(Long maxExhaustionSize) {
		this.maxExhaustionSize = maxExhaustionSize;
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

	public boolean isSimple() {
		return simple;
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}
}
