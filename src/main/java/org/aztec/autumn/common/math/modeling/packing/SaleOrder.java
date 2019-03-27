package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;

public class SaleOrder {
	
	private String id;
	private List<Item> items;
	private List<Box> selectableBoxes;

	public SaleOrder(String id,List<Item> items,List<Box> boxes) {
		this.id = id;
		this.items = items;
		this.selectableBoxes = boxes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Box> getSelectableBoxes() {
		return selectableBoxes;
	}

	public void setSelectableBoxes(List<Box> selectableBoxes) {
		this.selectableBoxes = selectableBoxes;
	}

	@Override
	public String toString() {
		return "SaleOrder [id=" + id + ", items=" + items + ", selectableBoxes=" + selectableBoxes + "]";
	}

	
}
