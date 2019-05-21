package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;

import org.aztec.autumn.common.math.MathConstant;

public class BinPackingConfig {
	
	private List<Item> items;
	private Box box;
	private SaleOrder order;
	private Integer minimumCombines = 1;
	private Integer satisfiedValve;
	//private Long maxExhaustionTolerant = 1000l;
	private double speed = 1 ;
	private int populations;
	
	public Long getItemTotalCount(){
		Long count = 0l;
		for(Item item : items){
			count += item.getNumber();
		}
		return count;
	}

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

	public Integer getLevel1TryTime() {
		return new Double( MathConstant.MAX_EXHAUTION_TOLERANT * speed).intValue();
	}


	public BinPackingConfig(List<Item> items, Box box, Double speed) {
		super();
		this.items = items;
		this.box = box;
		this.speed = speed;
	}

	
	public BinPackingConfig(SaleOrder order){
		
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public SaleOrder getOrder() {
		return order;
	}

	public void setOrder(SaleOrder order) {
		this.order = order;
	}

	public int getPopulations() {
		return populations;
	}

	public void setPopulations(int populations) {
		this.populations = populations;
	}
	
}
