package org.aztec.autumn.common.math.modeling.packing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class BinPackingSolution {
	
	private Box box;
	private List<Item> items;
	private List<Item> fillableItems;
	private FillResult result;
	private Map<String,Long> usedCount = Maps.newHashMap();
	List<Item> remainItems = Lists.newArrayList();
	private boolean success = false;
	private Long fillCount = 0l;
	private Long totalCount = 0l;
	private Long totalUsedCount = 0l;
	private Double score = 0d;

	public BinPackingSolution(Box box,List<Item> items) {
		
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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public void push(FillResult result) {
		this.result = result;
		fillCount += result.getTotalUsedCount();
		if(fillCount >= totalCount){
			success = true;
			remainItems.clear();
		}
		Map<String,Long> newUsedCount = result.getUsedCount();
		if(usedCount.isEmpty()){
			usedCount.putAll(newUsedCount);
			newUsedCount.clear();
		}
		for (Item item : items) {
			Item newItem = item.clone();
			Long alreadyUsed = usedCount.get(item.getId());
			if (alreadyUsed == null) {
				alreadyUsed = 0l;
			}
			if ( newUsedCount.containsKey(item.getId())) {
				Long used = newUsedCount.get(item.getId());
				alreadyUsed = alreadyUsed + used;
				usedCount.put(item.getId(), alreadyUsed);
			}
			if (alreadyUsed < item.getNumber()) {
				newItem.setNumber(item.getNumber() - alreadyUsed);
				remainItems.add(newItem);
			}
		}
		totalUsedCount = calculateTotalUsed();
	}
	
	
	public List<Item> getRemainItems(){
		
		return remainItems;
	}
	
	public List<Item> getUsedItems(){
		List<Item> usedItems = Lists.newArrayList();
		for(String itemId : usedCount.keySet()) {
			Long itemUseCount = usedCount.get(itemId);
			if(itemUseCount == null){
				itemUseCount = 0l;
			}
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
		return totalUsedCount;
	}
	
	public Long calculateTotalUsed(){
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
				+ items + "\n plans:" + result + "]";
	}
	
	public Double getFillRatio(){
		Double boxV = box.getVolume();
		Double ItemV = 0d;
		for(Item item : items){
			if(usedCount.containsKey(item.getId())){
				ItemV += item.getVolume() * usedCount.get(item.getId());
			}
		}
		return ItemV / boxV;
	}
	
	public BinPackingSolution cloneSolution(){
		List<Item> newFillableItems = Lists.newArrayList();
		Long totalCount = 0l;
		for(Item fillItem : remainItems){
			newFillableItems.add(fillItem.clone());
			totalCount += fillItem.getNumber();
		}
		BinPackingSolution newSolution = new BinPackingSolution(box.clone(), newFillableItems);
		Map<String,Long> usedCounts = new HashMap<String, Long>();
		List<Item> newRemainItems = Lists.newArrayList();
		
		for(Item fillItem : newFillableItems){
			boolean isUsed = false;
			for(Entry<String,Long> entry : usedCount.entrySet()){
				usedCounts.put(new String(entry.getKey()), new Long(entry.getValue()));
				if(fillItem.getId().equals(entry.getKey())){
					Item remainItem = fillItem.clone();
					isUsed = true;
					if(!remainItem.getNumber().equals(entry.getValue())){
						remainItem.setNumber(remainItem.getNumber() - entry.getValue());
						newRemainItems.add(remainItem);
					}
				}
			}
			if(!isUsed){
				newRemainItems.add(fillItem);
			}
		}
		newSolution.remainItems = newRemainItems;
		newSolution.usedCount = usedCounts;
		List<Item> newItems = Lists.newArrayList();
		for(Item item : items){
			newItems.add(item.clone());
		}
		newSolution.items = newItems;
		newSolution.success = success;
		newSolution.box = box.clone();
		newSolution.totalCount = new Long(totalCount);
		newSolution.totalUsedCount = new Long(totalUsedCount);
		newSolution.score = new Double(score);
		newSolution.fillCount = new Long(fillCount);
		newSolution.result = result;
		return newSolution;
	}

}
