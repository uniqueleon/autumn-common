package org.aztec.autumn.common.math.equations.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.aztec.autumn.common.math.MathException;
import org.aztec.autumn.common.math.equations.AdjustmentStrategy;
import org.aztec.autumn.common.math.equations.GreatestCommonDivisor;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentResult;
import org.aztec.autumn.common.math.equations.adjustment.AdjustmentStep;

import com.google.common.collect.Lists;

public abstract class BaseAdjustmentStrategy implements AdjustmentStrategy{

	protected Map<Integer,AdjustmentResult> histories;
	protected static ThreadLocal<Stack<AdjustmentResult>> currentSteps = new ThreadLocal<>();
	protected boolean searchedIgnore = true;
	
	private List<AdjustmentStep> findSearchedStep(Long[] tmpSolutions) {
		List<AdjustmentStep> snapshots = Lists.newArrayList();
		//snapshots.addAll(currentSteps.get().pop().getNeighborhood());
		return snapshots;
	}
	
	protected void move(Long[] tmpSolution,AdjustmentStep step) {
		List<Integer> targetIndexes = step.getIndexes();
		for(int i = 0;i < targetIndexes.size();i++) {
			Integer targetIndex = targetIndexes.get(i);
			tmpSolution[targetIndex] += step.getMovableStep().get(i);
		}
	}
	
	protected boolean addStep(Long[] beginSolutions,Long[] solutions,AdjustmentStep step) throws MathException{
		Stack<AdjustmentResult> stepsStack = currentSteps.get();
		AdjustmentResult adjustment = new AdjustmentResult(beginSolutions);
		Integer beginHashcode = Arrays.hashCode(beginSolutions);
		if(stepsStack.isEmpty()) {
			if(histories.get(beginHashcode) != null) {
				adjustment = histories.get(beginHashcode);
			}
			stepsStack.push(adjustment);
		}
		else {
			adjustment = currentSteps.get().peek();
			adjustment.setSearched(true);
		}
		if(!searchedIgnore && adjustment.getNeighborhood().containsKey(step)) {
			//throw new MathException(DiophantineEquationErrorCode.SOLUATION_PATH_HAS_BEEN_SEARCHED,  "Step[" + step + "] has been searched!");
			return false;
		}
		else {
			AdjustmentResult newResult = new AdjustmentResult(solutions);
			histories.put(newResult.getId(), newResult);
			adjustment.getNeighborhood().put(step,newResult);
			stepsStack.push(newResult);
			return true;
		}
	}
	
	protected void init(Map<Integer,AdjustmentResult> searchHistory) {
		currentSteps.set(new Stack<>());
		this.histories = searchHistory;
		//histories.set(Maps.newHashMap());
		//histories.get().putAll(searchHistory);
	}
	

	private List<Integer> findTowFactorExclusiveIndexes(Long[] tmpSolutions,Integer thisIndex){

		List<AdjustmentStep> searchSteps = findSearchedStep(tmpSolutions);
		List<Integer> excludeIndexes = Lists.newArrayList();
		for(AdjustmentStep snapShots : searchSteps) {
			if(snapShots.getMovableStep().size() > 0) {
				List<Integer> partnerIndexes = snapShots.getIndexes();
				if(partnerIndexes.contains(thisIndex)) {
					int index = partnerIndexes.indexOf(thisIndex);
					if(index == 0) {
						excludeIndexes.add(partnerIndexes.get(1));
					}
					else {
						excludeIndexes.add(partnerIndexes.get(0));
					}
				}
			}
		}
		return excludeIndexes;
	}

	protected AdjustmentStep findTwoFactorPartner(boolean direction,Long[] tmpSolutions,Long[][] range,
			Integer thisIndex,Integer searchBeginIndex,List<List<GreatestCommonDivisor>> allGcds,List<Long> coefficients) {
		List<GreatestCommonDivisor> indexGcds = getSingleFactorGCDs(thisIndex, coefficients, allGcds);
		List<Integer> excludeIndexes = findTowFactorExclusiveIndexes(tmpSolutions, thisIndex);
		AdjustmentStep partner = null;
		List<Integer> foundIndexes = Lists.newArrayList();
		foundIndexes.add(thisIndex);
		for(Integer i = searchBeginIndex;i < tmpSolutions.length;i++) {
			long testSolution = tmpSolutions[i];
			if(i == thisIndex)
				continue;
			long coefficient1 = coefficients.get(thisIndex) / indexGcds.get(i).getGcd();
			long coefficient2 = coefficients.get(i) / indexGcds.get(i).getGcd();  
			if(direction) {
				if(testSolution - range[i][0] > coefficient1) {
					if(!excludeIndexes.contains(i)) {
						partner = new AdjustmentStep();
						partner.getIndexes().add(thisIndex);
						partner.getIndexes().add(i);
						long movableStep = calculateMoveStep(direction, thisIndex, i, tmpSolutions, range, indexGcds,coefficients);
						partner.getMovableStep().add(movableStep * coefficient2);
						partner.getMovableStep().add(-movableStep * coefficient1);
						return partner;
					}
				}
			}
			else {
				if(range[i][1] - testSolution > coefficient1 ) {
					if(!excludeIndexes.contains(i)) {
						partner = new AdjustmentStep();
						partner.getIndexes().add(thisIndex);
						partner.getIndexes().add(i);
						long movableStep = calculateMoveStep(direction, thisIndex, i, tmpSolutions, range, indexGcds,coefficients);
						partner.getMovableStep().add(-movableStep * coefficient2);
						partner.getMovableStep().add(movableStep  * coefficient1);
						return partner;
					}
				}
			}
		}
		return partner;
	}
	
	protected List<GreatestCommonDivisor> getSingleFactorGCDs(int index ,List<Long> coefficients,
			List<List<GreatestCommonDivisor>> allGcds){

		List<GreatestCommonDivisor> indexGcds = Lists.newArrayList();
		for(int i = 0;i < index;i++) {
			indexGcds.add(allGcds.get(i).get(index - i - 1));
		}
		indexGcds.add(new GreatestCommonDivisor(coefficients.get(index), Lists.newArrayList()));
		if(index < allGcds.size()) {
			indexGcds.addAll(allGcds.get(index));
		}
		return indexGcds;
	}
	

	private long calculateMoveStep(boolean direction,Integer index,Integer findIndex,Long[] solutions,Long[][] ranges,
			List<GreatestCommonDivisor> indexGcds,List<Long> coefficients) {

		long tmpSolution = solutions[index];
		Long gcd = indexGcds.get(findIndex).getGcd();
		long coefficient1 = coefficients.get(findIndex) / gcd;
		long coefficient2 = coefficients.get(index) / gcd;
		long otherResult = solutions[findIndex];
		Long movableDistance = direction ? otherResult - ranges[findIndex][0] : ranges[findIndex][1] - otherResult; 
		long movableStep = movableDistance / coefficient2;
		long moveDistance = (direction ? ranges[index][0] - tmpSolution : tmpSolution - ranges[index][1]);
		boolean isDivided = (moveDistance % coefficient1) == 0;
		long needMoveStep = Math.abs((moveDistance / coefficient1))  + (isDivided ? 0 : 1);
		long moveStep = Math.min(movableStep, needMoveStep);
		return moveStep;
	}


	public BaseAdjustmentStrategy() {
		super();
	}

}
