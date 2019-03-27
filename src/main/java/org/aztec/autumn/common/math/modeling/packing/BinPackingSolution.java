package org.aztec.autumn.common.math.modeling.packing;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.aztec.autumn.common.math.modeling.packing.impl.FillResult;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class BinPackingSolution {
	
	private Box box;
	private List<Item> items;
	private List<Item> fillableItems;
	private Queue<FillResult> plans;
	private Map<String,Long> usedCount = Maps.newHashMap();
	List<Item> remainItems = Lists.newArrayList();
	private boolean success = false;
	private Long fillCount = 0l;
	private Long totalCount = 0l;
	private Double score = 0d;

	public BinPackingSolution(Box box,List<Item> items) {
		plans = Queues.newArrayDeque();
		this.box = box;
		this.items = items;
		for(Item item : items) {
			totalCount += item.getNumber();
		}
		filt(box, items);
	}
	
	private void filt(Box box,List<Item> items){
		fillableItems = Lists.newArrayList();
		for(Item item : items) {
			if(box.isFillable(item)) {
				fillableItems.add(item);
			}
		}
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public Queue<FillResult> getPlans() {
		return plans;
	}

	public void setPlans(Queue<FillResult> plans) {
		this.plans = plans;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public void push(FillResult result) {
		plans.add(result);
		fillCount += result.getTotalUsedCount();
		if(fillCount >= totalCount)
			success = true;
		Map<String,Long> newUsedCount = result.getUsedCount();
		remainItems.clear();
		for(Item item : fillableItems) {
			Item newItem = item.clone();
			Long alreadyUsed = usedCount.get(item.getId());
			if(alreadyUsed == null) {
				alreadyUsed = 0l;
			}
			if(newUsedCount.containsKey(item.getId())) {
				Long used = newUsedCount.get(item.getId());
				alreadyUsed = alreadyUsed + used;
			}
			if(alreadyUsed < item.getNumber()) {
				newItem.setNumber(item.getNumber() - alreadyUsed);
				remainItems.add(newItem);
			}
			usedCount.put(item.getId(), alreadyUsed);
		}
	}
	
	public List<Item> getRemainItems(){
		
		return remainItems;
	}
	
	public List<Item> getUsedItems(){
		List<Item> usedItems = Lists.newArrayList();
		for(String itemId : usedCount.keySet()) {
			Long itemUseCount = usedCount.get(itemId);
			Item usedItem = null;
			for(Item item : items) {
				if(item.getId().equals(itemId)) {
					usedItem = item.clone();
					break;
				}
			}
			usedItem.setNumber(itemUseCount);
			usedItems.add(usedItem);
		}
		return usedItems;
	}
	
	public Long getTotalUsedCount(){
		Long totalUsedCount = 0l;
		for(Long count : usedCount.values()){
			if(count > 0){
				totalUsedCount += count;
			}
		}
		return totalUsedCount;
	}

	public Double getScore() {
		return score;
	}
	

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "BinPackingSolution [success=" + success + "\n score= " +score+ "\n  usedCount=" + usedCount + "\n box=" + box + "\n items="
				+ items + "\n plans:" + plans + "]";
	}
	
	

}
