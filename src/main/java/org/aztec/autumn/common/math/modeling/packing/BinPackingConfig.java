package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;

import org.aztec.autumn.common.math.MathConstant;

public class BinPackingConfig {
	
	private List<Item> items;
	private Box box;
	private Integer minimumCombines = 1;
	private Integer satisfiedValve;
	private Long maxExhaustionTolerant = MathConstant.MAX_EXHAUTION_TOLERANT;
	private Integer level1TryTime = 200;

	public BinPackingConfig() {
		// TODO Auto-generated constructor stub
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public Integer getMinimumCombines() {
		return minimumCombines;
	}

	public void setMinimumCombines(Integer minimumCombines) {
		this.minimumCombines = minimumCombines;
	}

	public Integer getSatisfiedValve() {
		return satisfiedValve;
	}

	public void setSatisfiedValve(Integer satisfiedValve) {
		this.satisfiedValve = satisfiedValve;
	}

	public Long getMaxExhaustionTolerant() {
		return maxExhaustionTolerant;
	}

	public void setMaxExhaustionTolerant(Long maxExhaustionTolerant) {
		this.maxExhaustionTolerant = maxExhaustionTolerant;
	}

	public Integer getLevel1TryTime() {
		return level1TryTime;
	}

	public void setLevel1TryTime(Integer level1TryTime) {
		this.level1TryTime = level1TryTime;
	}

	public BinPackingConfig(List<Item> items, Box box, Integer minimumCombines,Integer satifiedValve) {
		super();
		this.items = items;
		this.box = box;
		this.minimumCombines = minimumCombines;
		this.satisfiedValve = satifiedValve;
	}

	
}
