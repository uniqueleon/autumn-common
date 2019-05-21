package org.aztec.autumn.common.math.modeling.packing.impl;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import org.aztec.autumn.common.math.modeling.packing.BaseObject;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException;
import org.aztec.autumn.common.math.modeling.packing.BinPackingException.ErrorCodes;
import org.aztec.autumn.common.math.modeling.packing.FillResult;
import org.aztec.autumn.common.math.modeling.packing.FillStep;
import org.aztec.autumn.common.math.modeling.packing.Location;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class FillResultImpl implements FillResult{

	protected Location location;
	protected Stack<FillStepImpl> steps;
	protected FillUnit startPoint;
	protected FillType fillType;
	protected boolean full;
	protected boolean success;
	protected Stack<FillHistory> histories;
	protected boolean allSearch = false;
	protected Map<String, Long> usedCount = Maps.newConcurrentMap();
	
	protected List<FillUnit> unfilledArea = Lists.newArrayList();
	protected Long currentLowestHeight = Long.MAX_VALUE;

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
		SURFACE, VOLUME, RANDOM;
	}
	
	public FillResultImpl( FillType fillType, Location location) {
		steps = new Stack<FillStepImpl>();
		histories = new Stack<>();
		this.fillType = fillType;
		this.location = location;
	}

	public FillResultImpl(FillUnit startPoint, FillType fillType, Location location) {
		steps = new Stack<FillStepImpl>();
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
			FillStepImpl failStep = (FillStepImpl) steps.pop();
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
		for (FillStepImpl step : steps) {
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
			if(lastSolutionIndex < candidateSolution.size() - 1){
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
	
	public void forward(Long tatalCount, FillUnit fillObject,
			List<Long> solutions, List<FillUnit> units) throws BinPackingException{
		forward(tatalCount, fillObject, null, solutions, units);
	}

	public void forward(Long tatalCount, FillUnit fillObject,
			List<Integer> chooseIndex,
			List<Long> solutions, List<FillUnit> units)
			throws BinPackingException {
		if(chooseIndex != null && !histories.isEmpty()){
			FillHistory history = histories.peek();
			if(history.getDepth() == getCurrentStep()){
				history.setChooseIndexes(chooseIndex);
				history.setLastSolutionIndex(history.getLastSolutionIndex() + 1);
			}
		}
		Long maxHeight = 0l;
		for (int i = 0; i < units.size(); i++) {
			FillUnit unit = units.get(i);
			if (solutions.get(i) > 0 && unit.getWidth() > maxHeight) {
				maxHeight = unit.getWidth();
			}
		}
		Long beginHeight = 0l;
		if (steps.size() > 0) {
			beginHeight = fillObject.getLocation().getY().longValue();
		}
		if (maxHeight > startPoint.getWidth())
			throw new BinPackingException("The filling object is too high!", ErrorCodes.FILL_OBJECT_TO_HIGH);
		List<FillUnit> stepUnits = Lists.newArrayList();

		for (int i = 0; i < solutions.size(); i++) {
			Long unitNum = solutions.get(i);
			for (int j = 0; j < unitNum; j++) {
				FillUnit newUnit = units.get(i).clone();
				stepUnits.add(newUnit);
			}
		}
		FillStepImpl newStep = new FillStepImpl(fillObject,stepUnits, beginHeight);
		steps.push(newStep);
		
		unfilledArea.remove(fillObject);
		newStep.init(usedCount,startPoint);
		unfilledArea.addAll(newStep.getGenerateUnits());
		if (isAllFilled(tatalCount)) {
			full = false;
			success = true;
		} else if (isAllFilled()) {
			if (unfilledArea.isEmpty()) {
				full = true;
				success = true;
			}
		}
	}

	public Queue<FillStep> getSteps() {
		Queue<FillStep> retQueue = Queues.newArrayDeque();
		
		return retQueue;
	}

	public void setSteps(Stack<FillStepImpl> steps) {
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

	public boolean isAllFilled(Long totalCount) {
		if(totalCount.equals(getTotalUsedCount())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("##########FILL RESULT(" + (full ? "FULL" : "NOT_FULL") + ")########\n");
		builder.append("location=" + location + "\n");
		builder.append("startPoint=(" + startPoint.getLength() + "," + startPoint.getWidth() + ")\n");
		builder.append("fillType=" + fillType + "\n");
		builder.append("full=" + full + "\n");
		builder.append("usedCount=" + usedCount + "\n");
		builder.append("**********STEPS *************\n");
		Stack<FillStepImpl> tmpStep = (Stack<FillStepImpl>) steps.clone();
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

	public Integer getTotalUsedCount() {
		int totalCount = 0;
		if (usedCount != null && usedCount.size() > 0) {
			for (Long used : usedCount.values()) {
				totalCount += used;
			}
		}
		return totalCount;
	}

	public Double score() {
		
		int count = getTotalUsedCount();
		Double maxSquare = 0d;
		if(steps.isEmpty())
			return maxSquare;
		List<FillUnit> filledUnits = Lists.newArrayList();
		FillStepImpl lastStep = steps.peek();
		filledUnits.addAll(lastStep.getUnits());
		List<FillUnit> mergedUnits = FillUnit.mergeAll(filledUnits);
		Double maxLength = 0d;
		for(FillUnit unit :mergedUnits){
			Double fillUnitSquare = unit.getLength() * unit.getWidth() * 1d;
			if(fillUnitSquare > maxSquare){
				maxSquare = fillUnitSquare;
			}
			if(unit.getLength() > maxLength){
				maxLength = unit.getLength().doubleValue();
			}
			
		}
		return maxLength;
		//return count.doubleValue();
		//return maxSquare;
		//return maxSquare;
		/*double effector = 0.5;
		return startPoint.getBase() * ( effector * (count * 1d / mergedUnits.size()));*/
	}

	public void mergeUnfilledArea() {
		FillUnit.mergeAll(unfilledArea);
	}

	public List<FillUnit> getUnfilledArea(List<FillUnit> allUnits) {
		return unfilledArea;
	}
	
	public List<FillUnit> getAllFillUnits(){
		List<FillUnit> fillUnits = Lists.newArrayList();
		for(FillStepImpl step : steps) {
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

	public FillResultImpl clone() {
		FillResultImpl newObj = new FillResultImpl(startPoint.clone(), fillType,
				location.clone());
		newObj.steps = (Stack<FillStepImpl>) steps.clone();
		newObj.success = success;
		newObj.full = full;
		newObj.currentLowestHeight = currentLowestHeight;
		newObj.usedCount.putAll(usedCount);
		newObj.unfilledArea = Lists.newArrayList();
		newObj.unfilledArea.addAll(unfilledArea);
		return newObj;
	}
}
