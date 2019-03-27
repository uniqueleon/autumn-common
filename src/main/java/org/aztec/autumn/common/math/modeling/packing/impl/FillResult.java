package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.aztec.autumn.common.math.modeling.packing.BaseObject;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException.ErrorCodes;
import org.aztec.autumn.common.math.modeling.packing.Location;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FillResult {

	private Location location;
	private Stack<FillStep> steps;
	private FillUnit startPoint;
	private FillType fillType;
	private boolean full;
	private boolean success;
	private Stack<FillHistory> histories;
	private boolean allSearch = false;
	private Map<String, Long> usedCount = Maps.newConcurrentMap();
	
	private List<FillUnit> unfilledArea = Lists.newArrayList();
	private Long currentLowestHeight = Long.MAX_VALUE;

	public List<FillUnit> getUnfilledArea() {
		return unfilledArea;
	}

	public void setUnfilledArea(List<FillUnit> unfilledArea) {
		this.unfilledArea = unfilledArea;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public static enum FillType {
		SURFACE, VOLUME;
	}
	
	public FillResult( FillType fillType, Location location) {
		steps = new Stack<FillStep>();
		histories = new Stack<>();
		this.fillType = fillType;
		this.location = location;
	}

	public FillResult(FillUnit startPoint, FillType fillType, Location location) {
		steps = new Stack<FillStep>();
		histories = new Stack<>();
		this.startPoint = startPoint;
		this.fillType = fillType;
		this.location = location;
		this.unfilledArea.add(startPoint);
	}

	public boolean isFull() {
		return full;
	}

	public void backward() {
		if (steps.size() > 0) {
			FillStep failStep = steps.pop();
			for(FillUnit generatedUnits : failStep.getGenerateUnits()) {
				unfilledArea.remove(generatedUnits);
			}
			unfilledArea.add(failStep.getTarget());
			recalculateItemCost();
			if(isAllSearch()){
				histories.pop();
			}
		}
	}
	

	public Integer getCurrentStep() {
		return steps.size();
	}

	private void recalculateItemCost() {
		usedCount.clear();
		for (FillStep step : steps) {
			for (FillUnit unit : step.getUnits()) {
				BaseObject bo = unit.getBelongObject();
				if (usedCount.containsKey(bo.getId())) {
					usedCount.put(bo.getId(), usedCount.get(bo.getId()) + 1l);
				} else {
					usedCount.put(bo.getId(), 1l);
				}
			}
		}
	}


	public Long getItemCount(String id) {
		Long itemNum = usedCount.get(id);
		return itemNum != null ? itemNum : 0;
	}
	
	public List<Long> getCandidateSolution(){
		if(histories.size() > 0){
			FillHistory history = histories.peek();
			List<List<Long>> candidateSolution = history.getCandidateSolutions();
			Integer lastSolutionIndex = history.getLastSolutionIndex();
			if(lastSolutionIndex < candidateSolution.size()){
				return candidateSolution.get(lastSolutionIndex + 1);
			}
		}
		return null;
	}
	
	public boolean hasSearchBefore(){
		if(histories.size() > getCurrentStep()){
			return true;
		}
		return false;
	}
	
	public boolean hasNoMoreSolution(){

		FillHistory history = histories.peek();
		if(history.getLastSolutionIndex() == history.getCandidateSolutions().size() - 1){
			return true;
		}
		return false;
	}
	
	public void setCandidateSolutions(List<List<Long>> solutions){
		FillHistory history = new FillHistory(getCurrentStep(),  solutions);
		history.setLastSolutionIndex(0);
		histories.push(history);
	}

	public void forward(FillParameter fillParam, FillUnit fillObject,
			List<Integer> chooseIndex,
			List<Long> solutions, List<FillUnit> units)
			throws BinPackingException {
		FillHistory history = histories.peek();
		if(history.getDepth() == getCurrentStep()){
			history.setChooseIndexes(chooseIndex);
			history.setLastSolutionIndex(history.getLastSolutionIndex() + 1);
		}
		Long maxHeight = 0l;
		for (int i = 0; i < units.size(); i++) {
			FillUnit unit = units.get(i);
			if (solutions.get(i) > 0 && unit.getHeight() > maxHeight) {
				maxHeight = unit.getHeight();
			}
		}
		Long beginHeight = 0l;
		if (steps.size() > 0) {
			beginHeight = fillObject.getLocation().getY().longValue();
		}
		if (maxHeight > startPoint.getHeight())
			throw new BinPackingException("The filling object is too high!", ErrorCodes.FILL_OBJECT_TO_HIGH);
		List<FillUnit> stepUnits = Lists.newArrayList();

		for (int i = 0; i < solutions.size(); i++) {
			Long unitNum = solutions.get(i);
			for (int j = 0; j < unitNum; j++) {
				FillUnit newUnit = units.get(i).clone();
				stepUnits.add(newUnit);
			}
		}
		FillStep newStep = new FillStep(fillObject,stepUnits, beginHeight);
		steps.push(newStep);
		
		unfilledArea.remove(fillObject);
		newStep.init(usedCount,startPoint);
		unfilledArea.addAll(newStep.getGenerateUnits());
		if (isAllFilled(fillParam)) {
			full = false;
			success = true;
		} else if (isAllFilled()) {
			if (unfilledArea.isEmpty()) {
				full = true;
				success = true;
			}
		}
	}

	public Stack<FillStep> getSteps() {
		return steps;
	}

	public void setSteps(Stack<FillStep> steps) {
		this.steps = steps;
	}

	public FillUnit getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(FillUnit startPoint) {
		this.startPoint = startPoint;
	}

	public Map<String, Long> getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Map<String, Long> usedCount) {
		this.usedCount = usedCount;
	}

	public void setFull(boolean full) {
		this.full = full;
	}


	private boolean isAllFilled() {
		/*for (FillObject fillObject : fillObjects) {
			if (!fillObject.isFilled()) {
				return false;
			}
		}*/
		return true;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isAllFilled(FillParameter fillParam) {
		if(fillParam.getTotalCount().equals(getTotalUsedCount())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("##########FILL RESULT(" + (full ? "FULL" : "NOT_FULL") + ")########\n");
		builder.append("location=" + location + "\n");
		builder.append("startPoint=(" + startPoint.getBase() + "," + startPoint.getHeight() + ")\n");
		builder.append("fillType=" + fillType + "\n");
		builder.append("full=" + full + "\n");
		builder.append("usedCount=" + usedCount + "\n");
		builder.append("**********STEPS *************\n");
		Stack<FillStep> tmpStep = (Stack<FillStep>) steps.clone();
		for(int i = 0;i < tmpStep.size();i++) {
			builder.append(tmpStep.get(i).toString() + "\n");
		}
		builder.append("**********STEPS *************\n");
		if (unfilledArea != null) {
			builder.append("*************UNFILLED******************\n");
			for (int i = 0; i < unfilledArea.size(); i++) {
				builder.append(unfilledArea.get(i) + "\n");
			}
			builder.append("*************UNFILLED******************\n");
		}
		builder.append("##########FILL RESULT########\n");
		return builder.toString();
	}

	public Long getTotalUsedCount() {
		long totalCount = 0l;
		if (usedCount != null && usedCount.size() > 0) {
			for (Long used : usedCount.values()) {
				totalCount += used;
			}
		}
		return totalCount;
	}

	public Long score() {
		return getTotalUsedCount();
	}

	public void mergeUnfilledArea() {
		FillUnit.mergeAll(unfilledArea);
	}

	public List<FillUnit> getUnfilledArea(List<FillUnit> allUnits) {
		return unfilledArea;
	}
	
	public List<FillUnit> getAllFillUnits(){
		List<FillUnit> fillUnits = Lists.newArrayList();
		for(FillStep step : steps) {
			fillUnits.addAll(step.getUnits());
		}
		return fillUnits;
	}
	
	
	public List<Integer> getLastChooseIndex(){
		FillHistory history = histories.peek();
		return history.getChooseIndexes();
	}

	public boolean isAllSearch() {
		return allSearch;
	}

	public void setAllSearch(boolean allSearch) {
		this.allSearch = allSearch;
	}
	

	public FillResult clone() {
		FillResult newObj = new FillResult(startPoint.clone(), fillType,
				location.clone());
		newObj.steps = (Stack<FillStep>) steps.clone();
		newObj.success = success;
		newObj.full = full;
		newObj.currentLowestHeight = currentLowestHeight;
		newObj.usedCount.putAll(usedCount);
		newObj.unfilledArea = Lists.newArrayList();
		newObj.unfilledArea.addAll(unfilledArea);
		return newObj;
	}
}
