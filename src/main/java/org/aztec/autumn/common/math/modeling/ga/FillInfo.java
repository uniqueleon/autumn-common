package org.aztec.autumn.common.math.modeling.ga;

import java.util.List;

import org.aztec.autumn.common.math.modeling.packing.Box;
import org.aztec.autumn.common.math.modeling.packing.Item;

public class FillInfo {

	private List<Box> newBoxes;
	private Item targetItem;
	private int selectIndex;
	private int num;
	
	public FillInfo(List<Box> newBoxes, Item targetItem,int index,int num) {
		super();
		this.newBoxes = newBoxes;
		this.targetItem = targetItem;
		this.selectIndex = index;
		this.num = num;
	}
	public List<Box> getNewBoxes() {
		return newBoxes;
	}
	public void setNewBoxes(List<Box> newBoxes) {
		this.newBoxes = newBoxes;
	}
	public Item getTargetItem() {
		return targetItem;
	}
	public void setTargetItem(Item targetItem) {
		this.targetItem = targetItem;
	}
	
	public int getSelectIndex() {
		return selectIndex;
	}
	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "FillInfo [newBoxes=" + newBoxes + ", targetItem=" + targetItem
				+ "]";
	}
	
	
}
